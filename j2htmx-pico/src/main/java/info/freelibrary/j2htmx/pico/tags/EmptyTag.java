package info.freelibrary.j2htmx.pico.tags;

import info.freelibrary.j2htmx.pico.attributes.Attribute;
import info.freelibrary.j2htmx.pico.rendering.HtmlBuilder;
import info.freelibrary.j2htmx.pico.rendering.TagBuilder;
import info.freelibrary.util.warnings.PMD;

import java.io.IOException;

/**
 * Represents a self-closing or void HTML element (such as {@code <img />}, {@code <br />}, or {@code <input />}) that
 * cannot have child nodes.
 *
 * @param <T> The concrete empty tag type
 */
@SuppressWarnings(PMD.AVOID_LITERALS_IN_IF_CONDITION)
public class EmptyTag<T extends EmptyTag<T>> extends AbstractTag<T> {

    /**
     * Constructs an EmptyTag with the given tag name.
     *
     * @param tagName The HTML tag name
     * @throws IllegalArgumentException If the tag name is null or empty
     */
    public EmptyTag(final String tagName) {
        super(tagName);
        if (tagName == null) {
            throw new IllegalArgumentException("Illegal tag name: null");
        }
        if ("".equals(tagName)) {
            throw new IllegalArgumentException("Illegal tag name: \"\"");
        }
    }

    @Override
    public <A extends Appendable> A render(final HtmlBuilder<A> aHtmlBuilder, final Object model) throws IOException {
        final TagBuilder attrs = aHtmlBuilder.appendEmptyTag(getTagName());
        for (final Attribute attr : getAttributes()) {
            attr.render(attrs, model);
        }
        attrs.completeTag();
        return aHtmlBuilder.output();
    }

}
