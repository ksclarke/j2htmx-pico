package info.freelibrary.j2htmx.pico.model;

import java.io.IOException;

public class BrowserTitle extends Template<PageModel> {

    @Override
    public void renderTemplate(Appendable writer, PageModel pageModel) throws IOException {
        writer.append(pageModel.getTitle());
    }
}
