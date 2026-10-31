/*
 * This code was derived from j2html, which is distributed under the Apache License, Version 2.0.
 * See LICENSE-ASL-2.0.txt in the parent project's root directory for more information.
 *
 * This file was modified for j2htmx-pico by Kevin S. Clarke (my.j2htmx-pico@kevinclarke.info).
 *
 * The modifications are licensed under the Mozilla Public License, Version 2.0.
 * See LICENSE-MPL-2.0.txt in the parent project's root directory for more information.
 */

package info.freelibrary.j2htmx.pico.codegen;

import static info.freelibrary.j2htmx.pico.codegen.generators.TagCreatorCodeGenerator.containerTags;
import static info.freelibrary.j2htmx.pico.codegen.generators.TagCreatorCodeGenerator.emptyTags;
import static info.freelibrary.j2htmx.pico.codegen.model.AttributesList.attributesDescriptive;
import static info.freelibrary.util.Constants.*;
import static java.lang.System.out;

import info.freelibrary.j2htmx.pico.codegen.model.AttrD;
import info.freelibrary.util.warnings.Checkstyle;
import info.freelibrary.util.warnings.PMD;

/**
 * Utility to export HTML element and attribute definitions to standard output.
 */
@SuppressWarnings(PMD.TOO_MANY_STATIC_IMPORTS)
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
            out.print(CLOSE_BRACKET_CHAR);
            out.println();
        }

        for (final String tag : containerTags()) {
            out.print("ELEMENT[");
            out.print(tag);
            out.print(CLOSE_BRACKET_CHAR);
            out.println();
        }

        out.println();

        for (final AttrD attr : attributesDescriptive()) {
            if (attr.hasArgument) {
                out.print("STRING");
            } else {
                out.print("BOOLEAN");
            }

            out.print(OPEN_BRACKET_CHAR);
            out.print(attr.myAttr);
            out.print(CLOSE_BRACKET_CHAR);
            out.println();

            for (final String tag : attr.myTags) {
                out.print("ATTRIBUTE[");
                out.print(tag);
                out.print(COLON);
                out.print(attr.myAttr);
                out.print(CLOSE_BRACKET_CHAR);
                out.println();
            }

            out.println();
        }
    }
}
