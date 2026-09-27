package info.freelibrary.j2htmx.pico.model;

import info.freelibrary.j2htmx.pico.attributes.Attribute;
import java.io.IOException;

public class DynamicHrefAttribute extends Attribute {

    public DynamicHrefAttribute() {
        super("href");
    }

    @Override
    public void renderModel(final Appendable writer, final Object model) throws IOException {
        writer.append(" ");
        writer.append(getName());
        writer.append("=\"");
        writer.append(getUrl(model));
        writer.append("\"");
    }

    public String getUrl(final Object model) {
        return "/";
    }
}
