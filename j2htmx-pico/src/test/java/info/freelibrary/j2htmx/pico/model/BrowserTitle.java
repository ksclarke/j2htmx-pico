package info.freelibrary.j2htmx.pico.model;

import info.freelibrary.j2htmx.pico.rendering.HtmlBuilder;

import java.io.IOException;

public class BrowserTitle extends Template<PageModel> {

    @Override
    public <T extends Appendable> void renderTemplate(final HtmlBuilder<T> writer, final PageModel pageModel)
      throws IOException {
        writer.appendEscapedText(pageModel.getTitle());
    }
}
