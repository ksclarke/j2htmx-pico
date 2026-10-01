package info.freelibrary.j2htmx.pico.tags;

import info.freelibrary.j2htmx.pico.rendering.HtmlBuilder;

import java.io.IOException;

/**
 * Represents escaped text content within the DOM.
 */
public class Text extends AbstractDomContent {

    /** The raw text content. */
    private final String myText;

    /**
     * Constructs a Text element with the given text content.
     *
     * @param aText The text content to be escaped when rendered
     */
    public Text(final String aText) {
        myText = aText;
    }

    @Override
    public <T extends Appendable> T render(final HtmlBuilder<T> aHtmlBuilder, final Object aModel) throws IOException {
        aHtmlBuilder.appendEscapedText(String.valueOf(myText));
        return aHtmlBuilder.output();
    }
}
