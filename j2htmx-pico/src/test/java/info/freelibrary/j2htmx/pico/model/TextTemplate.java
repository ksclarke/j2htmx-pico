package info.freelibrary.j2htmx.pico.model;

import java.io.IOException;

public class TextTemplate extends Template<PageModel> {

    @Override
    public void renderTemplate(Appendable writer, PageModel pageModel) throws IOException {
        writer.append(pageModel.getText());
    }
}
