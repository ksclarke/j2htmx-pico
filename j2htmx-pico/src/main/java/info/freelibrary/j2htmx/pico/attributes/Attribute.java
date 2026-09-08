package info.freelibrary.j2htmx.pico.attributes;

import info.freelibrary.j2htmx.pico.Config;
import info.freelibrary.j2htmx.pico.rendering.TagBuilder;
import info.freelibrary.j2htmx.pico.tags.Renderable;

import java.io.IOException;

/**
 * Represents an HTML element attribute key-value pair or boolean attribute.
 */
public class Attribute implements Renderable {
    private String name;
    private String value;

    /**
     * Constructs an attribute with the given name and value.
     *
     * @param name The attribute name
     * @param value The attribute value
     */
    public Attribute(String name, String value) {
        this.name = name;
        this.value = value;
    }

    /**
     * Constructs a boolean attribute with the given name.
     *
     * @param name The attribute name
     */
    public Attribute(String name) {
        this.name = name;
        this.value = null;
    }

    @Override
    @Deprecated
    public void renderModel(Appendable writer, Object model) throws IOException {
        if (writer instanceof TagBuilder) {
            if (name != null) {
                if (value != null) {
                    ((TagBuilder) writer).appendAttribute(name, value);
                } else {
                    ((TagBuilder) writer).appendBooleanAttribute(name);
                }
            }
        } else {
            if (name == null) {
                return;
            }
            writer.append(' ');
            writer.append(name);
            if (value != null) {
                writer.append("=\"");
                writer.append(Config.textEscaper.escape(value));
                writer.append('"');
            }
        }
    }

    /**
     * Renders this attribute using the supplied TagBuilder.
     *
     * @param builder The tag builder to append this attribute to
     * @param model An optional data model object
     * @throws IOException If an I/O error occurs
     */
    public void render(TagBuilder builder, Object model) throws IOException {
        // Maintain compatibility with classes that extend Attribute, for now...
        renderModel(builder, model);
    }

    /**
     * Gets the attribute name.
     *
     * @return The attribute name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the attribute value.
     *
     * @param value The attribute value
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Gets the attribute value.
     *
     * @return The attribute value, or null if boolean
     */
    public String getValue() {
        return value;
    }
}
