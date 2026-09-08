package info.freelibrary.j2htmx.pico.tags;

import info.freelibrary.j2htmx.pico.Config;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Scanner;

import static info.freelibrary.j2htmx.pico.TagCreator.rawHtml;
import static info.freelibrary.j2htmx.pico.TagCreator.script;
import static info.freelibrary.j2htmx.pico.TagCreator.style;

/**
 * Utility for reading static resource files (CSS, JS) from the classpath or filesystem and embedding them into tags.
 */
public class InlineStaticResource {

    private InlineStaticResource() {
    }

    /**
     * Loads a file from classpath or filesystem and returns an inline style or script tag with the file's content.
     *
     * @param path The file path on the classpath or filesystem
     * @param format The target format determining tag type and minification
     * @return A ContainerTag representing the inline style or script element
     */
    public static ContainerTag<? extends Tag<?>> get(String path, TargetFormat format) {
        String fileString = getFileAsString(path);
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
     */
    public static String getFileAsString(String path) {
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

    private static String streamToString(InputStream inputStream) {
        Scanner s = new Scanner(inputStream).useDelimiter("\\A");
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

