package info.freelibrary.j2htmx.pico.tags;

import info.freelibrary.j2htmx.pico.Config;
import info.freelibrary.j2htmx.pico.rendering.FlatHtml;
import info.freelibrary.j2htmx.pico.rendering.HtmlBuilder;

import java.io.IOException;

/**
 * Represents raw, unescaped text content within the DOM.
 */
public class UnescapedText extends DomContent {

    /** The raw unescaped text content. */
    private final String text;

    /**
     * Constructs an UnescapedText element with the given raw text.
     *
     * @param text The unescaped text content
     */
    public UnescapedText(final String text) {
        this.text = text;
    }

    @Override
    public <T extends Appendable> T render(final HtmlBuilder<T> builder, final Object model) throws IOException {
        builder.appendUnescapedText(String.valueOf(text));
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

    @Override
    public boolean equals(final Object obj) {
        if (obj == null || !(obj instanceof UnescapedText)) {
            return false;
        }
        return ((UnescapedText) obj).render().equals(this.render());
    }
}
