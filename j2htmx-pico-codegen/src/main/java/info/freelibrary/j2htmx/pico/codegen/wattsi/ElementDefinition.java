package info.freelibrary.j2htmx.pico.codegen.wattsi;

/**
 * Definition of an HTML element parsed from WHATWG spec sources.
 */
public interface ElementDefinition {

    /**
     * Gets the element tag name.
     *
     * @return The element tag name
     */
    String name();

    /**
     * Checks if this element is self-closing or void.
     *
     * @return True if self-closing, false otherwise
     */
    boolean isSelfClosing();

    /**
     * Checks if this element is marked as obsolete in the spec.
     *
     * @return True if obsolete, false otherwise
     */
    boolean isObsolete();

}
