package info.freelibrary.j2htmx.pico.codegen;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import info.freelibrary.j2htmx.pico.codegen.generators.WattsiGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class WattsiGeneratorTest {

    @TempDir
    Path directory;

    @Test
    public void omitsObsoleteElementsAndAttributes() throws IOException {
        final Path source = directory.resolve("spec.html");
        Files.writeString(source,
          "<dfn element><code>div</code></dfn>" +
          "<dfn element-attr for=div><code>id</code></dfn>" +
          "<p>Elements in the following list are entirely obsolete</p><dl>" +
          "<dt><dfn element><code>applet</code></dfn></dt></dl>" +
          "<p>The following attributes are obsolete</p><dl>" +
          "<dt><dfn element-attr for=div><code>align</code></dfn></dt></dl>");

        final PrintStream original = System.out;
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        try (PrintStream stream = new PrintStream(output, true, StandardCharsets.UTF_8)) {
            System.setOut(stream);
            WattsiGenerator.main(source.toString());
        } finally {
            System.setOut(original);
        }

        final String generated = output.toString(StandardCharsets.UTF_8);
        assertTrue(generated.contains("class DivTag"));
        assertTrue(generated.contains("withId()"));
        assertFalse(generated.contains("AppletTag"));
        assertFalse(generated.contains("withAlign()"));
        assertFalse(generated.contains("@Deprecated"));
    }
}