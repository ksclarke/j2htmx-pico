package info.freelibrary.j2htmx.pico.codegen;

import static info.freelibrary.j2htmx.pico.codegen.generators.TagCreatorCodeGenerator.containerTags;
import static info.freelibrary.j2htmx.pico.codegen.generators.TagCreatorCodeGenerator.emptyTags;
import static info.freelibrary.j2htmx.pico.codegen.model.AttributesList.attributesDescriptive;
import static java.lang.System.out;

import info.freelibrary.j2htmx.pico.codegen.model.AttrD;
import info.freelibrary.util.Constants;
import info.freelibrary.util.warnings.Checkstyle;

/**
 * Utility to export HTML element and attribute definitions to standard output.
 */
public final class Export {

    /**
     * Constructs an instance of Export.
     */
    private Export() {
        // This is intentionally left empty
    }

    /**
     * Main entry point to print element and attribute definitions.
     *
     * @param args Command-line arguments
     */
    @SuppressWarnings({"UncommentedMain", Checkstyle.UNCOMMENTED_MAIN})
    static void main(final String[] args) {
        for (final String tag : emptyTags()) {
            out.print("EMPTY-ELEMENT[");
            out.print(tag);
            out.print(Constants.CLOSE_BRACKET);
            out.println();
        }

        for (final String tag : containerTags()) {
            out.print("ELEMENT[");
            out.print(tag);
            out.print(Constants.CLOSE_BRACKET);
            out.println();
        }

        out.println();

        for (final AttrD attr : attributesDescriptive()) {
            if (attr.hasArgument) {
                out.print("STRING");
            } else {
                out.print("BOOLEAN");
            }

            out.print(Constants.OPEN_BRACKET);
            out.print(attr.myAttr);
            out.print(Constants.CLOSE_BRACKET);
            out.println();

            for (final String tag : attr.myTags) {
                out.print("ATTRIBUTE[");
                out.print(tag);
                out.print(Constants.COLON);
                out.print(attr.myAttr);
                out.print(Constants.CLOSE_BRACKET);
                out.println();
            }

            out.println();
        }
    }
}
