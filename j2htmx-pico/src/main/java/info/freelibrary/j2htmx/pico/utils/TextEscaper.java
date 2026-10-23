package info.freelibrary.j2htmx.pico.utils;

/**
 * Functional interface for escaping HTML text content.
 */
@FunctionalInterface
public interface TextEscaper {

    /**
     * Escapes the specified text string.
     *
     * @param aText The text to escape
     * @return The escaped text string
     */
    String escape(String aText);
}
