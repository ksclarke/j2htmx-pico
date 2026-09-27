package info.freelibrary.j2htmx.pico.tags;

import info.freelibrary.j2htmx.pico.Config;
import info.freelibrary.j2htmx.pico.rendering.FlatHtml;
import info.freelibrary.j2htmx.pico.rendering.HtmlBuilder;

import java.io.IOException;

/**
 * Represents escaped text content within the DOM.
 */
public class Text extends DomContent {

    private final String text;

    /**
     * Constructs a Text element with the given text content.
     *
     * @param text The text content to be escaped when rendered
     */
    public Text(final String text) {
        this.text = text;
    }

    @Override
    public <T extends Appendable> T render(final HtmlBuilder<T> builder, final Object model) throws IOException {
        builder.appendEscapedText(String.valueOf(text));
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
