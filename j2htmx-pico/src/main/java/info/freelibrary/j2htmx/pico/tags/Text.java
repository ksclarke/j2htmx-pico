package info.freelibrary.j2htmx.pico.tags;

import info.freelibrary.j2htmx.pico.rendering.HtmlBuilder;

import java.io.IOException;

/**
 * Represents escaped text content within the DOM.
 */
public class Text extends DomContent {

    /** The raw text content. */
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

}
