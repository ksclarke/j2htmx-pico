package info.freelibrary.j2htmx.pico.model;

import java.io.IOException;

public class BrowserTitle extends Template<PageModel> {

    @Override
    public void renderTemplate(final Appendable writer, final PageModel pageModel) throws IOException {
        writer.append(pageModel.getTitle());
    }
}
