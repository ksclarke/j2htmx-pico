package info.freelibrary.j2htmx.pico.tags;

import info.freelibrary.j2htmx.pico.Config;
import info.freelibrary.j2htmx.pico.attributes.Attribute;
import info.freelibrary.j2htmx.pico.rendering.FlatHtml;
import info.freelibrary.j2htmx.pico.rendering.HtmlBuilder;
import info.freelibrary.j2htmx.pico.rendering.TagBuilder;

import java.io.IOException;

/**
 * Represents a self-closing or void HTML element (such as {@code <img />}, {@code <br />}, or {@code <input />}) that
 * cannot have child nodes.
 *
 * @param <T> The concrete empty tag type
 */
public class EmptyTag<T extends EmptyTag<T>> extends Tag<T> {

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
    public <A extends Appendable> A render(final HtmlBuilder<A> builder, final Object model) throws IOException {
        final TagBuilder attrs = builder.appendEmptyTag(getTagName());
        for (final Attribute attr : getAttributes()) {
            attr.render(attrs, model);
        }
        attrs.completeTag();
        return builder.output();
    }

    @Override
    @Deprecated
    public void renderModel(final Appendable writer, final Object model) throws IOException {
        final HtmlBuilder<?> builder = (writer instanceof HtmlBuilder)
          ? (HtmlBuilder<?>) writer
          : FlatHtml.into(writer, Config.global());

        render(builder, model);
    }
}
