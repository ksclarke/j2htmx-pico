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
}
