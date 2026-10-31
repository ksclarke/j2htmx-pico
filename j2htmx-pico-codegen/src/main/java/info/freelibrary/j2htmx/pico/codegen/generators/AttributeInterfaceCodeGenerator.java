/*
 * This code was derived from j2html, which is distributed under the Apache License, Version 2.0.
 * See LICENSE-ASL-2.0.txt in the parent project's root directory for more information.
 *
 * This file was modified for j2htmx-pico by Kevin S. Clarke (my.j2htmx-pico@kevinclarke.info).
 *
 * The modifications are licensed under the Mozilla Public License, Version 2.0.
 * See LICENSE-MPL-2.0.txt in the parent project's root directory for more information.
 */

package info.freelibrary.j2htmx.pico.codegen.generators;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import info.freelibrary.j2htmx.pico.codegen.Generator;
import info.freelibrary.j2htmx.pico.codegen.GeneratorUtil;
import info.freelibrary.j2htmx.pico.codegen.model.AttrD;
import info.freelibrary.j2htmx.pico.codegen.model.AttributesList;
import info.freelibrary.util.warnings.Checkstyle;
import info.freelibrary.util.warnings.PMD;

/**
 * Code generator that outputs Java attribute interfaces for HTML elements.
 */
@SuppressWarnings({PMD.SYSTEM_PRINTLN, Checkstyle.MULTIPLE_STRING_LITERALS, "MultipleStringLiterals"})
public final class AttributeInterfaceCodeGenerator {

    /** The relative output path for attribute interface classes. */
    private static final String RELATIVE_OUTPUT_PATH = "tags/attributes/";

    /** The autocomplete attribute name constant. */
    private static final String AUTOCOMPLETE = "autocomplete";

    /**
     * Private constructor to prevent instantiation.
     */
    private AttributeInterfaceCodeGenerator() {
        // This is intentionally left empty
    }

    /**
     * Generates or deletes attribute interface source files in the specified path.
     *
     * @param aAbsPath The absolute path to output files
     * @param aDelete Whether to delete files instead of writing them
     * @throws IOException If an I/O error occurs
     */
    public static void generate(final Path aAbsPath, final boolean aDelete) throws IOException {
        final Path dir = Paths.get(aAbsPath.toString(), RELATIVE_OUTPUT_PATH);

        // Delete all files in the directory for fresh generation
        GeneratorUtil.deleteAllFilesInDir(dir);

        for (final AttrD attr : AttributesList.attributesDescriptive()) {
            final Path path = makePath(attr.myAttr, aAbsPath);
            final String interfaceName = interfaceNameFromAttribute(attr.myAttr) + "<T extends AbstractTag<T>>";
            final String interfaceStr = getInterfaceTemplate(
              interfaceName,
              Optional.of("Instance<T>"),
              Arrays.asList("info.freelibrary.j2htmx.pico.tags.AbstractTag",
                "info.freelibrary.j2htmx.pico.tags.Instance"),
              interfaceNameFromAttribute(attr.myAttr),
              attr
            );

            if (!aDelete) {
                System.out.println("writing to " + path);
                Files.write(path, interfaceStr.getBytes(StandardCharsets.UTF_8));
            }
        }

    }

    /**
     * Returns the package declaration line.
     *
     * @return The package statement
     */
    private static String getPackage() {
        return "package info.freelibrary.j2htmx.pico.tags.attributes;\n";
    }

    /**
     * Generates a default method signature string.
     *
     * @param aName The method name
     * @return The method declaration snippet
     */
    private static String makeReturnTypeAndMethodName(final String aName) {
        return "default T " + aName;
    }

    /**
     * Generates the interface template source code.
     *
     * @param anInterfaceName The name of the interface
     * @param anOptExtends Optional extended interface
     * @param anImports List of imports
     * @param anInterfaceNameSimple Simple interface name
     * @param anAttrD Attribute descriptor
     * @return Complete interface source code
     */
    private static String getInterfaceTemplate(
      final String anInterfaceName,
      final Optional<String> anOptExtends,
      final List<String> anImports,
      final String anInterfaceNameSimple,
      final AttrD anAttrD
    ) {

        final StringBuilder sb = new StringBuilder(512);

        sb.append(getPackage()).append('\n');

        for (final String importName : anImports) {
            sb.append("import ").append(importName).append(";\n");
        }

        sb.append("\n/**\n * Defines the {@code ")
          .append(anAttrD.myAttr)
          .append("} attribute for HTML tags.\n *\n * @param <T> The tag type\n */\npublic interface ")
          .append(anInterfaceName);

        anOptExtends.ifPresent(ext -> sb.append(" extends ").append(ext).append(' '));

        sb.append(" {\n");

        // Method parameter uses the aName convention to avoid Java keywords and satisfy PMD rules.
        final String attrName = anAttrD.myAttr;
        final char firstChar = Character.toLowerCase(attrName.charAt(0));
        final String prefix = (
          firstChar == 'a' || firstChar == 'e' || firstChar == 'i' || firstChar == 'o' || firstChar == 'u'
        ) ? "an" : "a";
        final String paramName = prefix + anInterfaceNameSimple;

        //depending on if the attribute has an argument or not,
        //generate methods according to the convention in AbstractTag.java
        // arg -> with$ATTR(arg), withCond$ATTR(condition, arg)
        // no arg -> has$ATTR(), withCond$ATTR(condition)

        //append the 'with$ATTR' method
        writeAttributeMethod(anInterfaceNameSimple, anAttrD, sb, attrName, paramName);
        writeAttributeMethodCond(anInterfaceNameSimple, anAttrD, sb, attrName, paramName);

        sb.append("}\n");

        return sb.toString();
    }

    /**
     * Appends code for an attribute without arguments to the builder.
     *
     * @param aSb The string builder
     * @param anAttrName The attribute name
     */
    private static void addAttributeNoArg(final StringBuilder aSb, final String anAttrName) {
        if (AUTOCOMPLETE.equals(anAttrName)) {
            aSb.append("self().withAttr(\"").append(anAttrName).append("\",\"on\");\n");
        } else {
            aSb.append("self().withAttr(\"").append(anAttrName).append("\");\n");
        }
    }

    /**
     * Writes the conditional attribute method to the string builder.
     *
     * @param anInterfaceNameSimple Simple interface name
     * @param anAttrD Attribute descriptor
     * @param aSb The string builder
     * @param anAttrName The attribute name
     * @param aParamName The parameter name
     */
    private static void writeAttributeMethodCond(final String anInterfaceNameSimple, final AttrD anAttrD,
      final StringBuilder aSb, final String anAttrName, final String aParamName) {

        aSb.append("\n    /**\n     * Conditionally sets the {@code ").append(anAttrName).append("} attribute");
        if (anAttrD.hasArgument) {
            aSb.append(" with the specified value.\n     *\n     * @param aEnable Whether to set the attribute\n"
              + "     * @param ").append(aParamName).append(" The attribute value\n     * @return The tag instance\n"
              + "     */\n    ");
        } else {
            aSb.append(".\n     *\n     * @param aEnable Whether to set the attribute\n"
              + "     * @return The tag instance\n     */\n    ");
        }
        aSb.append(makeReturnTypeAndMethodName("withCond" + anInterfaceNameSimple));

        if (anAttrD.hasArgument) {
            //add a variant where you can specify the argument
            aSb.append("(final boolean aEnable, final String ")
              .append(aParamName)
              .append(") {if (aEnable){\nself().withAttr(\"")
              .append(anAttrName)
              .append("\", ")
              .append(aParamName)
              .append(");\n}\nreturn self();\n}\n");
        } else {
            //add a variant where you can toggle the attribute
            aSb.append("(final boolean aEnable) {if (aEnable){\n");
            addAttributeNoArg(aSb, anAttrName);
            aSb.append("}\nreturn self();\n}\n");
        }
    }

    /**
     * Writes the attribute method to the string builder.
     *
     * @param anInterfaceNameSimple Simple interface name
     * @param anAttrD Attribute descriptor
     * @param aSb The string builder
     * @param anAttrName The attribute name
     * @param aParamName The parameter name
     */
    private static void writeAttributeMethod(final String anInterfaceNameSimple, final AttrD anAttrD,
      final StringBuilder aSb, final String anAttrName, final String aParamName) {

        aSb.append("\n    /**\n     * Sets the {@code ").append(anAttrName).append("} attribute");
        if (anAttrD.hasArgument) {
            aSb.append(" with the specified value.\n     *\n     * @param ").append(aParamName)
              .append(" The attribute value\n     * @return The tag instance\n     */\n    ");
        } else {
            aSb.append(".\n     *\n     * @return The tag instance\n     */\n    ");
        }
        aSb.append(makeReturnTypeAndMethodName(
          (anAttrD.hasArgument ? "with" : "has") + anInterfaceNameSimple)
        );

        if (anAttrD.hasArgument) {
            //add a variant where you can specify the argument
            aSb.append("(final String ")
              .append(aParamName)
              .append(") {return self().withAttr(\"")
              .append(anAttrName)
              .append("\", ")
              .append(aParamName)
              .append(");\n}\n");
        } else {
            //add a variant where you can toggle the attribute
            aSb.append("() {");
            addAttributeNoArg(aSb, anAttrName);
            aSb.append("return self();\n}\n");
        }
    }

    /**
     * Converts an attribute name into its corresponding interface name.
     *
     * @param anAttribute The attribute name
     * @return The interface name with first letter capitalized
     */
    public static String interfaceNameFromAttribute(final String anAttribute) {
        return Generator.attributePascalCase(anAttribute);
    }

    /**
     * Constructs the file path for the given interface.
     *
     * @param aTagLowerCase The lowercase tag/attribute name
     * @param aAbsPath The absolute base directory path
     * @return The path to the interface file
     */
    private static Path makePath(final String aTagLowerCase, final Path aAbsPath) {
        final String filename = interfaceNameFromAttribute(aTagLowerCase) + ".java";
        return Paths.get(aAbsPath.toString(), RELATIVE_OUTPUT_PATH, filename);
    }

}
