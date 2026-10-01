package info.freelibrary.j2htmx.pico.tags;

import static info.freelibrary.j2htmx.pico.TagCreator.rawHtml;
import static info.freelibrary.j2htmx.pico.TagCreator.script;
import static info.freelibrary.j2htmx.pico.TagCreator.style;
import static info.freelibrary.util.Constants.EMPTY;

import info.freelibrary.j2htmx.pico.Config;
import info.freelibrary.util.warnings.PMD;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.Scanner;

/**
 * Utility for reading static resource files (CSS, JS) from the classpath or filesystem and embedding them into tags.
 */
public final class InlineStaticResource {

    /**
     * Constructs a new inline static resource utility.
     */
    private InlineStaticResource() {
        // This is intentionally left empty
    }

    /**
     * Loads a file from classpath or filesystem and returns an inline style or script tag with the file's content.
     *
     * @param aPath The file path on the classpath or filesystem
     * @param aFormat The target format determining tag type and minification
     * @return A ContainerTag representing the inline style or script element
     * @throws RuntimeException If an invalid target format is specified
     */
    public static ContainerTag<? extends AbstractTag<?>> get(final String aPath, final TargetFormat aFormat) {
        final String fileString = getFileAsString(aPath);

        return switch (aFormat) {
            case CSS_MIN -> style().with(rawHtml(Config.getGlobalCssMinifier().minify(fileString)));
            case JS_MIN -> script().with(rawHtml(Config.getGlobalJsMinifier().minify(fileString)));
            case CSS -> style().with(rawHtml(fileString));
            case JS -> script().with(rawHtml(fileString));
        };
    }

    /**
     * Reads a file from the classpath or filesystem as a string.
     *
     * @param aPath The path to the file
     * @return The file contents as a string
     * @throws RuntimeException If the file cannot be found on the classpath or filesystem
     */
    @SuppressWarnings({PMD.AVOID_THROWING_RAW_EXCEPTION_TYPES})
    public static String getFileAsString(final String aPath) {
        try {
            return streamToString(InlineStaticResource.class.getResourceAsStream(aPath));
        } catch (@SuppressWarnings(PMD.AVOID_CATCHING_GENERIC_EXCEPTION) final Exception expected) {
            // We don't ask users to specify classpath or file-system
            try {
                return Files.readString(Path.of(aPath));
            } catch (final InvalidPathException | IOException details) {
                details.addSuppressed(expected);
                throw new RuntimeException("Couldn't find file with path='" + aPath + "'", details);
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
        final Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8).useDelimiter("\\A");
        return scanner.hasNext() ? scanner.next() : EMPTY;
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

