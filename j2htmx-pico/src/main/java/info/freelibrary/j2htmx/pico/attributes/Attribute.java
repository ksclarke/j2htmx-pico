package info.freelibrary.j2htmx.pico.attributes;

import info.freelibrary.j2htmx.pico.Config;
import info.freelibrary.j2htmx.pico.rendering.TagBuilder;
import info.freelibrary.j2htmx.pico.tags.Renderable;

import java.io.IOException;

/**
 * Represents an HTML element attribute key-value pair or boolean attribute.
 */
public class Attribute implements Renderable {

    /** The attribute name. */
    private final String myName;

    /** The attribute value, or null for a boolean attribute. */
    private String myValue;

    /**
     * Constructs an attribute with the given name and value.
     *
     * @param aName The attribute name
     * @param aValue The attribute value
     */
    public Attribute(final String aName, final String aValue) {
        myName = aName;
        myValue = aValue;
    }

    /**
     * Constructs a boolean attribute with the given name.
     *
     * @param aName The attribute name
     */
    public Attribute(final String aName) {
        myName = aName;
    }

    @Override
    @Deprecated
    public void renderModel(final Appendable writer, final Object model) throws IOException {
        if (writer instanceof TagBuilder) {
            if (myName != null) {
                if (myValue != null) {
                    ((TagBuilder) writer).appendAttribute(myName, myValue);
                } else {
                    ((TagBuilder) writer).appendBooleanAttribute(myName);
                }
            }
        } else {
            if (myName == null) {
                return;
            }

            writer.append(' ');
            writer.append(myName);

            if (myValue != null) {
                writer.append("=\"");
                writer.append(Config.getGlobalTextEscaper().escape(myValue));
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
    public void render(final TagBuilder builder, final Object model) throws IOException {
        // Maintain compatibility with myClasses that extend Attribute, for now...
        renderModel(builder, model);
    }

    /**
     * Gets the attribute name.
     *
     * @return The attribute name
     */
    public String getName() {
        return myName;
    }

    /**
     * Gets the attribute value.
     *
     * @return The attribute value, or null if boolean
     */
    public String getValue() {
        return myValue;
    }

    /**
     * Sets the attribute value.
     *
     * @param value The attribute value
     */
    public void setValue(final String value) {
        this.myValue = value;
    }
}
