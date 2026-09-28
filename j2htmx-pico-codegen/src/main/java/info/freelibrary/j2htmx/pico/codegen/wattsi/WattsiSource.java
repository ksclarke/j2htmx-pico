package info.freelibrary.j2htmx.pico.codegen.wattsi;

import static info.freelibrary.util.Constants.SINGLE_INSTANCE;
import static info.freelibrary.util.Constants.SPACE;
import static java.util.stream.Collectors.toList;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Parses and extracts element and attribute definitions from a WHATWG HTML specification document.
 */
public class WattsiSource {

    /** The data-x attribute. */
    private static final String DATA_X = "data-x";

    /** The HTML document being parsed. */
    private final Document doc;

    /** Set of references marked as obsolete in the specification. */
    private final Set<Reference> obsolete = new HashSet<>();

    /**
     * Constructs a WattsiSource from the specified HTML specification document.
     *
     * @param doc The parsed WHATWG HTML spec document
     */
    public WattsiSource(final Document doc) {
        this.doc = doc;

        // Find where obsolete elements are defined or referenced.
        final Elements obsoleteElements = doc.select(
          "p:contains(Elements in the following list are entirely obsolete) + dl");

        // Convert definitions into references to record obsolete elements.
        obsoleteElements.select("dt > dfn[element]")
          .stream()
          .map(WattsiElement::new)
          .map(WattsiElement::reference)
          .forEach(obsolete::add);

        // Extract references to record obsolete elements.
        obsoleteElements.select("dt > code")
          .stream()
          .map(Element::childNodes)
          .map(Reference::from)
          .forEach(obsolete::add);

        // Find where obsolete attributes are defined or referenced.
        final Elements obsoleteAttributes = doc.select("p:contains(The following attributes are obsolete) + dl");

        // Convert definitions into references to record obsolete attributes.
        obsoleteAttributes.select("dt > dfn[element-attr]").stream()
          .map(WattsiAttribute::new)
          .map(WattsiAttribute::reference)
          .forEach(obsolete::add);

        //        System.out.println(obsoleteAttributes.select("dt"));

        //        obsoleteAttributes.select("dt > code").stream()
        //            .map(Element::childNodes)
        //            .map(Reference::from)
        //            .forEach(System.err::println);

        //        System.out.println(
        //            doc.select("dfn[obsolete]")
        //        );
    }

    /**
     * Extracts all element definitions found in the document.
     *
     * @return List of element definitions
     */
    public List<ElementDefinition> elementDefinitions() {
        return doc.select("dfn[element]").stream()
          .map(WattsiElement::new)
          .collect(toList());
    }

    /**
     * Extracts all attribute definitions found in the document.
     *
     * @return List of attribute definitions
     */
    public List<AttributeDefinition> attributeDefinitions() {
        return doc.select("dfn[element-attr]").stream()
          .map(WattsiAttribute::new)
          .collect(toList());
    }

    /**
     * Internal reference representation pairing a unique key with display text.
     */
    private static class Reference {

        /** The normalized lookup key. */
        private final String key;

        /** The display text. */
        private final String text;

        Reference(final String key, final String text) {
            this.key = key;
            this.text = text;
        }

        /**
         * Creates a Reference from a list of DOM child nodes.
         *
         * @param nodes The child nodes to inspect
         * @return A Reference instance or null if not resolved
         */
        public static Reference from(final List<Node> nodes) {
            if (nodes.stream().allMatch(TextNode.class::isInstance)) {
                final String txt = nodes.stream()
                  .map(TextNode.class::cast)
                  .map(TextNode::text)
                  .collect(Collectors.joining(SPACE));
                return new Reference(txt, txt);
            }

            for (final Node node : nodes) {
                if (node instanceof Element element && (element.is("code") || element.is("span"))) {
                    final String key = element.hasAttr(DATA_X) ? element.attr(DATA_X) : element.text();
                    return new Reference(key.toLowerCase(Locale.US), element.text());
                }
            }

            return null;
        }

        @Override
        public String toString() {
            return key + "[" + text + "]";
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            final Reference reference = (Reference) o;
            return key.equals(reference.key);
        }

        @Override
        public int hashCode() {
            return Objects.hash(key);
        }
    }

    /**
     * Element definition implementation backed by a JSoup element.
     */
    public class WattsiElement implements ElementDefinition {

        /** The underlying dfn element defining this HTML element. */
        private final Element dfn;

        WattsiElement(final Element dfn) {
            if (!"dfn".equals(dfn.tagName())) {
                throw new IllegalArgumentException("Element cannot be defined from: " + dfn);
            }

            if (!dfn.hasAttr("element")) {
                throw new IllegalArgumentException("Does not define an element: " + dfn);
            }

            if ( dfn.childrenSize() != SINGLE_INSTANCE) {
                throw new IllegalArgumentException("Element cannot have multiple definitions: " + dfn);
            }

            this.dfn = dfn;
        }

        private Reference reference() {
            return Reference.from(dfn.childNodes());
        }

        @Override
        public String name() {
            if (dfn.hasAttr(DATA_X)) {
                return dfn.attr(DATA_X);
            }

            return Reference.from(dfn.childNodes()).key;
        }

        @Override
        public boolean isSelfClosing() {
            return false;
        }

        @Override
        public boolean isObsolete() {
            return obsolete.contains(reference());
        }
    }

    /**
     * Attribute definition implementation backed by a JSoup element.
     */
    public class WattsiAttribute implements AttributeDefinition {

        /** The underlying dfn element defining this attribute. */
        private final Element dfn;

        WattsiAttribute(final Element dfn) {
            if (!"dfn".equals(dfn.tagName())) {
                throw new IllegalArgumentException("Attribute cannot be defined from: " + dfn);
            }
            if (!dfn.hasAttr("element-attr")) {
                throw new IllegalArgumentException("Does not define an attribute: " + dfn);
            }
            if (dfn.childrenSize() != SINGLE_INSTANCE) {
                throw new IllegalArgumentException("Attribute cannot have multiple definitions: " + dfn);
            }

            this.dfn = dfn;
        }

        private Reference reference() {
            return Reference.from(dfn.childNodes());
        }

        @Override
        public String name() {
            return reference().text;
        }

        private List<String> targets() {
            if (dfn.hasAttr("for")) {
                return Arrays.asList(dfn.attr("for").trim().split(","));
            }
            return new ArrayList<>();
        }

        @Override
        public boolean appliesTo(final ElementDefinition element) {
            return targets().contains(element.name());
        }

        @Override
        public boolean isObsolete() {
            return obsolete.contains(reference());
        }
    }
}
