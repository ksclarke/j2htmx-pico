package info.freelibrary.j2htmx.pico.codegen;

import info.freelibrary.j2htmx.pico.codegen.model.AttrD;

import static info.freelibrary.j2htmx.pico.codegen.generators.TagCreatorCodeGenerator.containerTags;
import static info.freelibrary.j2htmx.pico.codegen.generators.TagCreatorCodeGenerator.emptyTags;
import static info.freelibrary.j2htmx.pico.codegen.model.AttributesList.attributesDescriptive;
import static info.freelibrary.j2htmx.pico.codegen.model.AttributesList.getCustomAttributesForHtmlTag;
import static java.lang.System.*;

/**
 * Utility to export HTML element and attribute definitions to standard output.
 */
public class Export {

    private Export() {
    }

    /**
     * Main entry point to print element and attribute definitions.
     *
     * @param args Command-line arguments
     */
    public static void main(final String[] args){
        for (final String tag : emptyTags()) {
            out.print("EMPTY-ELEMENT[");
            out.print(tag);
            out.print("]");
            out.println();
        }
        for (final String tag : containerTags()) {
            out.print("ELEMENT[");
            out.print(tag);
            out.print("]");
            out.println();
        }

        out.println();

        for(final AttrD attr : attributesDescriptive()){
            if(attr.hasArgument){
                out.print("STRING");
            }else{
                out.print("BOOLEAN");
            }
            out.print("[");
            out.print(attr.attr);
            out.print("]");
            out.println();
            for(final String tag : attr.tags){
                out.print("ATTRIBUTE[");
                out.print(tag);
                out.print(":");
                out.print(attr.attr);
                out.print("]");
                out.println();
            }
            out.println();
        }
    }
}
