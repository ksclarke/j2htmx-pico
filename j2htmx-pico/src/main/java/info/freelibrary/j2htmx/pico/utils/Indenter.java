package info.freelibrary.j2htmx.pico.utils;

/**
 * Functional interface for indenting HTML text according to nesting level.
 */
@FunctionalInterface
public interface Indenter {

    /**
     * Indents the specified text to the given indentation level.
     *
     * @param aLevel The nesting depth level
     * @param aText The text to indent
     * @return The indented string
     */
    String indent(int aLevel, String aText);
}
