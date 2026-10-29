/*
 * This code was derived from j2html, which is distributed under the Apache License, Version 2.0.
 * See META-INF/THIRD-PARTY.txt and META-INF/licenses/Apache-2.0.txt for more information.
 *
 * This file has been modified for j2htmx-pico by Kevin S. Clarke (my.j2htmx-pico@kevinclarke.info)
 *
 * The modifications are licensed under the Mozilla Public License, Version 2.0.
 * See LICENSE.txt in the parent project's root for more information.
 */

package info.freelibrary.j2htmx.pico.codegen.wattsi;

import static info.freelibrary.util.Constants.SINGLE_INSTANCE;
import static info.freelibrary.util.Constants.SPACE;
import static java.util.stream.Collectors.toList;

import info.freelibrary.util.warnings.Checkstyle;
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
@SuppressWarnings({Checkstyle.MULTIPLE_STRING_LITERALS, "MultipleStringLiterals"})
public class WattsiSource {

    /** The data-x attribute. */
    private static final String DATA_X = "data-x";

    /** The HTML document being parsed. */
    private final Document myDoc;

    /** Set of references marked as obsolete in the specification. */
    private final Set<Reference> myObsolete = new HashSet<>();

    /**
     * Constructs a WattsiSource from the specified HTML specification document.
     *
     * @param aDoc The parsed WHATWG HTML spec document
     */
    public WattsiSource(final Document aDoc) {
        this.myDoc = aDoc;

        // Find where obsolete elements are defined or referenced.
        final Elements obsoleteElements = aDoc.select(
          "p:contains(Elements in the following list are entirely obsolete) + dl");

        // Convert definitions into references to record obsolete elements.
        obsoleteElements.select("dt > dfn[element]")
          .stream()
          .map(WattsiElement::new)
          .map(WattsiElement::reference)
          .forEach(myObsolete::add);

        // Extract references to record obsolete elements.
        obsoleteElements.select("dt > code")
          .stream()
          .map(Element::childNodes)
          .map(Reference::from)
          .forEach(myObsolete::add);

        // Find where obsolete attributes are defined or referenced.
        final Elements obsoleteAttributes = aDoc.select("p:contains(The following attributes are obsolete) + dl");

        // Convert definitions into references to record obsolete attributes.
        obsoleteAttributes.select("dt > dfn[element-attr]").stream()
          .map(WattsiAttribute::new)
          .map(WattsiAttribute::reference)
          .forEach(myObsolete::add);

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
        return myDoc.select("dfn[element]").stream()
          .map(WattsiElement::new)
          .collect(toList());
    }

    /**
     * Extracts all attribute definitions found in the document.
     *
     * @return List of attribute definitions
     */
    public List<AttributeDefinition> attributeDefinitions() {
        return myDoc.select("dfn[element-attr]").stream()
          .map(WattsiAttribute::new)
          .collect(toList());
    }

    /**
     * Internal reference representation pairing a unique key with display text.
     */
    private static class Reference {

        /** The normalized lookup key. */
        private final String myKey;

        /** The display text. */
        private final String myText;

        /**
         * Constructs a Reference with the given key and display text.
         *
         * @param aKey The reference key
         * @param aText The display text
         */
        Reference(final String aKey, final String aText) {
            this.myKey = aKey;
            this.myText = aText;
        }

        /**
         * Creates a Reference from a list of DOM child nodes.
         *
         * @param aNodes The child nodes to inspect
         * @return A Reference instance or null if not resolved
         */
        public static Reference from(final List<Node> aNodes) {
            if (aNodes.stream().allMatch(TextNode.class::isInstance)) {
                final String txt = aNodes.stream()
                  .map(TextNode.class::cast)
                  .map(TextNode::text)
                  .collect(Collectors.joining(SPACE));
                return new Reference(txt, txt);
            }

            for (final Node node : aNodes) {
                if (node instanceof Element element && (element.is("code") || element.is("span"))) {
                    final String key = element.hasAttr(DATA_X) ? element.attr(DATA_X) : element.text();
                    return new Reference(key.toLowerCase(Locale.US), element.text());
                }
            }

            return null;
        }

        @Override
        public String toString() {
            return myKey + "[" + myText + "]";
        }

        @Override
        public boolean equals(final Object anObject) {
            if (this == anObject) {
                return true;
            }
            if (anObject == null || getClass() != anObject.getClass()) {
                return false;
            }
            final Reference reference = (Reference) anObject;
            return myKey.equals(reference.myKey);
        }

        @Override
        public int hashCode() {
            return Objects.hash(myKey);
        }
    }

    /**
     * Element definition implementation backed by a JSoup element.
     */
    public class WattsiElement implements ElementDefinition {

        /** The underlying dfn element defining this HTML element. */
        private final Element myDfn;

        /**
         * Constructs a WattsiElement from a definition element.
         *
         * @param aDfn The dfn element
         * @throws IllegalArgumentException If the definition element is invalid
         */
        WattsiElement(final Element aDfn) {
            if (!"dfn".equals(aDfn.tagName())) {
                throw new IllegalArgumentException("Element cannot be defined from: " + aDfn);
            }

            if (!aDfn.hasAttr("element")) {
                throw new IllegalArgumentException("Does not define an element: " + aDfn);
            }

            if ( aDfn.childrenSize() != SINGLE_INSTANCE) {
                throw new IllegalArgumentException("Element cannot have multiple definitions: " + aDfn);
            }

            this.myDfn = aDfn;
        }

        /**
         * Gets the reference for this element definition.
         *
         * @return The reference
         */
        private Reference reference() {
            return Reference.from(myDfn.childNodes());
        }

        @Override
        public String name() {
            if (myDfn.hasAttr(DATA_X)) {
                return myDfn.attr(DATA_X);
            }

            return Reference.from(myDfn.childNodes()).myKey;
        }

        @Override
        public boolean isSelfClosing() {
            return false;
        }

        @Override
        public boolean isObsolete() {
            return myObsolete.contains(reference());
        }
    }

    /**
     * Attribute definition implementation backed by a JSoup element.
     */
    public class WattsiAttribute implements AttributeDefinition {

        /** The underlying dfn element defining this attribute. */
        private final Element myDfn;

        /**
         * Constructs a WattsiAttribute from a definition element.
         *
         * @param aDfn The dfn element
         * @throws IllegalArgumentException If the definition element is invalid
         */
        WattsiAttribute(final Element aDfn) {
            if (!"dfn".equals(aDfn.tagName())) {
                throw new IllegalArgumentException("Attribute cannot be defined from: " + aDfn);
            }
            if (!aDfn.hasAttr("element-attr")) {
                throw new IllegalArgumentException("Does not define an attribute: " + aDfn);
            }
            if (aDfn.childrenSize() != SINGLE_INSTANCE) {
                throw new IllegalArgumentException("Attribute cannot have multiple definitions: " + aDfn);
            }

            this.myDfn = aDfn;
        }

        /**
         * Gets the reference for this attribute definition.
         *
         * @return The reference
         */
        private Reference reference() {
            return Reference.from(myDfn.childNodes());
        }

        @Override
        public String name() {
            return reference().myText;
        }

        /**
         * Gets the target elements this attribute applies to.
         *
         * @return List of target element names
         */
        private List<String> targets() {
            if (myDfn.hasAttr("for")) {
                return Arrays.asList(myDfn.attr("for").trim().split(","));
            }
            return new ArrayList<>();
        }

        @Override
        public boolean appliesTo(final ElementDefinition anElement) {
            return targets().contains(anElement.name());
        }

        @Override
        public boolean isObsolete() {
            return myObsolete.contains(reference());
        }
    }
}
