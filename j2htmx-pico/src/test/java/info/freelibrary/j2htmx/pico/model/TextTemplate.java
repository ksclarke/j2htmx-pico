package info.freelibrary.j2htmx.pico.model;

import info.freelibrary.j2htmx.pico.rendering.HtmlBuilder;

import java.io.IOException;

/**
 * Text template for testing.
 */
public class TextTemplate extends Template<PageModel> {

    @Override
    public <T extends Appendable> void renderTemplate(final HtmlBuilder<T> aWriter, final PageModel aPageModel)
      throws IOException {
        aWriter.appendEscapedText(aPageModel.getText());
    }
}
