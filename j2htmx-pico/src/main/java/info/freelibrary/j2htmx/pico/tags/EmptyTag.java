package info.freelibrary.j2htmx.pico.tags;

import info.freelibrary.j2htmx.pico.Config;
import info.freelibrary.j2htmx.pico.attributes.Attribute;
import info.freelibrary.j2htmx.pico.rendering.TagBuilder;
import info.freelibrary.j2htmx.pico.rendering.FlatHtml;
import info.freelibrary.j2htmx.pico.rendering.HtmlBuilder;

import java.io.IOException;

/**
 * Represents a self-closing or void HTML element (such as {@code <img />}, {@code <br />}, or {@code <input />})
 * that cannot have child nodes.
 *
 * @param <T> The concrete empty tag type
 */
public class EmptyTag<T extends EmptyTag<T>> extends Tag<T> {

    /**
     * Constructs an EmptyTag with the given tag name.
     *
     * @param tagName The HTML tag name
     */
    public EmptyTag(String tagName) {
        super(tagName);
        if (tagName == null) {
            throw new IllegalArgumentException("Illegal tag name: null");
        }
        if ("".equals(tagName)) {
            throw new IllegalArgumentException("Illegal tag name: \"\"");
        }
    }

    @Override
    public <A extends Appendable> A render(HtmlBuilder<A> builder, Object model) throws IOException {
        TagBuilder attrs = builder.appendEmptyTag(getTagName());
        for (Attribute attr : getAttributes()) {
            attr.render(attrs, model);
        }
        attrs.completeTag();
        return builder.output();
    }

    @Override
    @Deprecated
    public void renderModel(Appendable writer, Object model) throws IOException {
        HtmlBuilder<?> builder = (writer instanceof HtmlBuilder)
            ? (HtmlBuilder<?>) writer
            : FlatHtml.into(writer, Config.global());

        render(builder, model);
    }
}
