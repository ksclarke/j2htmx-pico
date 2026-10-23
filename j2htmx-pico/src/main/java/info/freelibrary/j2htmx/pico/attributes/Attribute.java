package info.freelibrary.j2htmx.pico.attributes;

import info.freelibrary.j2htmx.pico.rendering.TagBuilder;

import java.io.IOException;

/**
 * Represents an HTML element attribute key-value pair or boolean attribute.
 */
public class Attribute {

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

    /**
     * Renders this attribute using the supplied TagBuilder.
     *
     * @param aBuilder The tag builder to append this attribute to
     * @param aModel An optional data model object
     * @throws IOException If an I/O error occurs
     */
    public void render(final TagBuilder aBuilder, final Object aModel) throws IOException {
        if (myName != null) {
            if (myValue != null) {
                aBuilder.appendAttribute(myName, myValue);
            } else {
                aBuilder.appendBooleanAttribute(myName);
            }
        }
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
     * @param aValue The attribute value
     */
    public void setValue(final String aValue) {
        this.myValue = aValue;
    }
}
