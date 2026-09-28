package info.freelibrary.j2htmx.pico.tags;

import static info.freelibrary.j2htmx.pico.TagCreator.rawHtml;
import static info.freelibrary.j2htmx.pico.TagCreator.script;
import static info.freelibrary.j2htmx.pico.TagCreator.style;

import info.freelibrary.j2htmx.pico.Config;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Scanner;

/**
 * Utility for reading static resource files (CSS, JS) from the classpath or filesystem and embedding them into tags.
 */
public final class InlineStaticResource {

    /**
     * Constructs a new inline static resource utility.
     */
    private InlineStaticResource() {
    }

    /**
     * Loads a file from classpath or filesystem and returns an inline style or script tag with the file's content.
     *
     * @param path The file path on the classpath or filesystem
     * @param format The target format determining tag type and minification
     * @return A ContainerTag representing the inline style or script element
     * @throws RuntimeException If an invalid target format is specified
     */
    public static ContainerTag<? extends Tag<?>> get(final String path, final TargetFormat format) {
        final String fileString = getFileAsString(path);
        switch (format) {
            case CSS_MIN:
                return style().with(rawHtml(Config.cssMinifier.minify(fileString)));
            case JS_MIN:
                return script().with(rawHtml(Config.jsMinifier.minify((fileString))));
            case CSS:
                return style().with(rawHtml(fileString));
            case JS:
                return script().with(rawHtml(fileString));
            default:
                throw new RuntimeException("Invalid target format");
        }
    }

    /**
     * Reads a file from the classpath or filesystem as a string.
     *
     * @param path The path to the file
     * @return The file contents as a string
     * @throws RuntimeException If the file cannot be found on the classpath or filesystem
     */
    public static String getFileAsString(final String path) {
        try {
            return streamToString(InlineStaticResource.class.getResourceAsStream(path));
        } catch (Exception expected) { // we don't ask users to specify classpath or file-system
            try {
                return streamToString(new FileInputStream(path));
            } catch (Exception exception) {
                throw new RuntimeException("Couldn't find file with path='" + path + "'");
            }
        }
    }

    /**
     * Reads an input stream into a string.
     *
     * @param inputStream The input stream to read
     * @return The string content of the stream
     */
    private static String streamToString(final InputStream inputStream) {
        final Scanner s = new Scanner(inputStream).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    /**
     * Supported formats for embedding inline static resources.
     */
    public enum TargetFormat {
        /** Minified CSS embedded in a style element. */
        CSS_MIN,
        /** Unminified CSS embedded in a style element. */
        CSS,
        /** Minified JavaScript embedded in a script element. */
        JS_MIN,
        /** Unminified JavaScript embedded in a script element. */
        JS
    }

}

