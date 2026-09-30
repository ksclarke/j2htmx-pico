package info.freelibrary.j2htmx.pico.model;

import info.freelibrary.j2htmx.pico.attributes.Attribute;
import info.freelibrary.j2htmx.pico.rendering.TagBuilder;

import java.io.IOException;

public class DynamicHrefAttribute extends Attribute {

    public DynamicHrefAttribute() {
        super("href");
    }

    @Override
    public void render(final TagBuilder writer, final Object model) throws IOException {
        writer.appendAttribute(getName(), getUrl(model));
    }

    public String getUrl(final Object model) {
        return "/";
    }
}
