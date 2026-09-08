package info.freelibrary.j2htmx.pico.codegen;

import info.freelibrary.j2htmx.pico.codegen.generators.SpecializedTagClassCodeGenerator;
import info.freelibrary.j2htmx.pico.codegen.generators.AttributeInterfaceCodeGenerator;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Standalone application runner for invoking code generation.
 */
public final class App
{
    private App() {
    }

    /**
     * Main entry point to generate attribute interfaces and specialized tag classes.
     *
     * @param args Command-line arguments
     */
    public static void main( String[] args )
    {
        final Path relPath = Paths.get("../library/src/main/java/j2html/");
        final Path absPath = relPath.toAbsolutePath();

        System.out.println("writing in "+absPath);

        //decide if the files should be
        //deleted or generated
        final boolean delete = false;

        try {
            AttributeInterfaceCodeGenerator.generate(absPath, delete);
            SpecializedTagClassCodeGenerator.generate(absPath, delete);
            //TagCreatorCodeGenerator.print();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //don't forget to auto-reformat the generated code.
    }
}
