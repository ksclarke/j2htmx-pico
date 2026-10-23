package info.freelibrary.j2htmx.pico.codegen.wattsi;

/**
 * Definition of an HTML attribute parsed from WHATWG spec sources.
 */
public interface AttributeDefinition {

    /**
     * Gets the attribute name.
     *
     * @return The attribute name
     */
    String name();

    /**
     * Checks if this attribute applies to the specified element definition.
     *
     * @param anElement The element definition
     * @return True if applicable, false otherwise
     */
    boolean appliesTo(ElementDefinition anElement);

    /**
     * Checks if this attribute has marked as obsolete in the spec.
     *
     * @return True if obsolete, false otherwise
     */
    boolean isObsolete();
}
