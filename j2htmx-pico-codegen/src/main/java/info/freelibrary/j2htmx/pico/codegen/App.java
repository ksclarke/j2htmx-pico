package info.freelibrary.j2htmx.pico.codegen;

import info.freelibrary.j2htmx.pico.codegen.generators.AttributeInterfaceCodeGenerator;
import info.freelibrary.j2htmx.pico.codegen.generators.SpecializedTagClassCodeGenerator;
import info.freelibrary.util.Logger;
import info.freelibrary.util.LoggerFactory;
import info.freelibrary.util.warnings.PMD;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Standalone application runner for invoking code generation.
 */
@SuppressWarnings(PMD.SYSTEM_PRINTLN)
public final class App {

    /** The application logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    private App() {
        // This is intentionally left empty
    }

    /**
     * Main entry point to generate attribute interfaces and specialized tag classes.
     *
     * @param args Command-line arguments
     */
    public static void main(final String[] args) {
        final Path relPath = Paths.get("../library/src/main/java/j2html/");
        final Path absPath = relPath.toAbsolutePath();

        System.out.println("writing in " + absPath);

        // Decide if the files should be deleted or generated
        final boolean delete = false;

        try {
            AttributeInterfaceCodeGenerator.generate(absPath, delete);
            SpecializedTagClassCodeGenerator.generate(absPath, delete);
            //TagCreatorCodeGenerator.print();
        } catch (IOException details) {
            LOGGER.error("Error generating code", details);
        }

        // Remember to auto-reformat the generated code.
    }
}
