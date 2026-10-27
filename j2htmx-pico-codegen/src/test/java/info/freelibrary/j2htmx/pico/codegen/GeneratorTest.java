package info.freelibrary.j2htmx.pico.codegen;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import info.freelibrary.util.warnings.Checkstyle;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Tests for {@link Generator}.
 */
@SuppressWarnings({Checkstyle.MULTIPLE_STRING_LITERALS, "MultipleStringLiterals"})
public class GeneratorTest {

    /** The temporary output directory for generated code. */
    @TempDir
    Path myOutput;

    /**
     * Tests that generated tags suppress excessive imports when appropriate.
     *
     * @throws IOException If an I/O error occurs
     */
    @Test
    public void generatedTagsSuppressExcessiveImports() throws IOException {
        final Model model = new Model();
        final Path definitions = Path.of("..", "j2htmx-pico", "src", "main", "models", "html.model");

        Parser.parse(Files.readString(definitions), model);
        Generator.generate(myOutput, "test.attributes", "test.tags", model);

        final String source = Files.readString(myOutput.resolve("test/tags/VideoTag.java"));

        assertTrue(source.contains("import info.freelibrary.util.warnings.PMD;"));
        assertTrue(source.contains("@SuppressWarnings(PMD.EXCESSIVE_IMPORTS)"));

        try (var sources = Files.list(myOutput.resolve("test/tags"))) {
            for (final Path tag : sources.toList()) {
                if (!tag.getFileName().toString().equals("VideoTag.java")) {
                    final String otherSource = Files.readString(tag);

                    assertFalse(otherSource.contains("@SuppressWarnings(PMD.EXCESSIVE_IMPORTS)"), tag.toString());
                    assertFalse(otherSource.contains("import info.freelibrary.util.warnings.PMD;"), tag.toString());
                }
            }
        }
    }

    /**
     * Tests that generated attribute interfaces use the expected method prefixes and names.
     *
     * @throws IOException If an I/O error occurs
     */
    @Test
    public void generatedAttributeMethodsUseCorrectPrefixes() throws IOException {
        final Model model = new Model();
        final Path definitions = Path.of("..", "j2htmx-pico", "src", "main", "models", "html.model");

        Parser.parse(Files.readString(definitions), model);
        Generator.generate(myOutput, "test.attributes", "test.tags", model);

        final Path attrDir = myOutput.resolve("test/attributes");

        assertTrue(Files.readString(attrDir.resolve("Controls.java")).contains("default T hasControls()"));
        assertTrue(Files.readString(attrDir.resolve("Autofocus.java")).contains("default T hasAutofocus()"));
        assertTrue(Files.readString(attrDir.resolve("Autoplay.java")).contains("default T hasAutoplay()"));
        assertTrue(Files.readString(attrDir.resolve("Loop.java")).contains("default T hasLoop()"));
        assertTrue(Files.readString(attrDir.resolve("Download.java")).contains("default T hasDownload()"));
        assertTrue(Files.readString(attrDir.resolve("Sandbox.java")).contains("default T hasSandbox()"));
        assertTrue(Files.readString(attrDir.resolve("Autocomplete.java")).contains("default T hasAutocomplete()"));
        assertTrue(Files.readString(attrDir.resolve("Ismap.java")).contains("default T hasIsmap()"));
        assertTrue(Files.readString(attrDir.resolve("Novalidate.java")).contains("default T hasNovalidate()"));

        final String deferSource = Files.readString(attrDir.resolve("Defer.java"));
        assertTrue(deferSource.contains("default T isDeferred()"));
        assertTrue(deferSource.contains("default T withCondDeferred(final boolean aEnable)"));
        assertTrue(deferSource.contains("default T withCondDefer(final boolean aEnable)"));

        assertTrue(Files.readString(attrDir.resolve("Checked.java")).contains("default T isChecked()"));
        assertTrue(Files.readString(attrDir.resolve("Disabled.java")).contains("default T isDisabled()"));
    }
}
