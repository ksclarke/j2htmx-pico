package info.freelibrary.j2htmx.pico.codegen;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class GeneratorTest {

    @TempDir
    Path output;

    @Test
    public void generatedTagsSuppressExcessiveImports() throws IOException {
        final Model model = new Model();
        final Path definitions = Path.of("..", "j2htmx-pico", "src", "main", "models", "html.model");

        Parser.parse(Files.readString(definitions), model);
        Generator.generate(output, "test.attributes", "test.tags", model);

        final String source = Files.readString(output.resolve("test/tags/VideoTag.java"));

        assertTrue(source.contains("import info.freelibrary.util.warnings.PMD;"));
        assertTrue(source.contains("@SuppressWarnings(PMD.EXCESSIVE_IMPORTS)"));

        try (var sources = Files.list(output.resolve("test/tags"))) {
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