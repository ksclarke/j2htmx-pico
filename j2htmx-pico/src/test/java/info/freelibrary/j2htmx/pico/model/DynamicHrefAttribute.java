package info.freelibrary.j2htmx.pico.model;

import info.freelibrary.j2htmx.pico.attributes.Attribute;
import info.freelibrary.j2htmx.pico.rendering.TagBuilder;

import java.io.IOException;

/**
 * Dynamic href attribute implementation for testing.
 */
public class DynamicHrefAttribute extends Attribute {

    /**
     * Constructs a DynamicHrefAttribute instance.
     */
    public DynamicHrefAttribute() {
        super("href");
    }

    @Override
    public void render(final TagBuilder aWriter, final Object aModel) throws IOException {
        aWriter.appendAttribute(getName(), getUrl(aModel));
    }

    /**
     * Resolves the URL for the given model.
     *
     * @param aModel The model object
     * @return The URL string
     */
    public String getUrl(final Object aModel) {
        return "/";
    }
}
