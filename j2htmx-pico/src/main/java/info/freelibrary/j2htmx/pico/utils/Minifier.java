package info.freelibrary.j2htmx.pico.utils;

/**
 * Functional interface for minifying text content (such as CSS or JavaScript).
 */
@FunctionalInterface
public interface Minifier {

    /**
     * Minifies the supplied content string.
     *
     * @param s The string to minify
     * @return The minified string
     */
    String minify(String s);
}
