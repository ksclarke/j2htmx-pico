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


import static info.freelibrary.j2htmx.pico.codegen.generators.TagCreatorCodeGenerator.containerTags;
import static info.freelibrary.j2htmx.pico.codegen.generators.TagCreatorCodeGenerator.emptyTags;

import info.freelibrary.j2htmx.pico.codegen.GeneratorUtil;
import info.freelibrary.j2htmx.pico.codegen.model.AttributesList;
import info.freelibrary.util.warnings.Checkstyle;
import info.freelibrary.util.warnings.PMD;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Code generator that outputs specialized HTML tag classes extending ContainerTag or EmptyTag.
 */
@SuppressWarnings({PMD.SYSTEM_PRINTLN, Checkstyle.MULTIPLE_STRING_LITERALS, "MultipleStringLiterals"})
public final class SpecializedTagClassCodeGenerator {

    /** The relative output path for specialized tag classes. */
    private static final String RELATIVE_OUTPUT_PATH = "tags/specialized";

    /**
     * Constructs an instance of SpecializedTagClassCodeGenerator.
     */
    private SpecializedTagClassCodeGenerator() {
        // This has intentionally left empty
    }

    /**
     * Generates or deletes specialized tag classes in the specified path.
     *
     * @param aAbsPath The absolute output path
     * @param aDelete Whether to delete files instead of generating them
     * @throws IOException If an I/O error occurs
     */
    public static void generate(final Path aAbsPath, final boolean aDelete) throws IOException {

        //delete all files in the directory for fresh generation
        final Path dir = Paths.get(aAbsPath.toString(), RELATIVE_OUTPUT_PATH);
        GeneratorUtil.deleteAllFilesInDir(dir);

        //the delete argument serves to give the possibility
        //to delete the classes that were written before
        System.out.println("// EmptyTags, generated in " + SpecializedTagClassCodeGenerator.class);

        for (final String tag : emptyTags()) {
            final String className = classNameFromTag(tag);
            final Path path = makePath(aAbsPath, tag);

            final List<String> interfaceNames = getInterfaceNamesForTag(tag);

            final String classString = getClassTemplate(className, Optional.of("EmptyTag<" + className + ">"),
              Arrays.asList("info.freelibrary.j2htmx.pico.tags.EmptyTag",
                "info.freelibrary.j2htmx.pico.tags.attributes.*"), tag, interfaceNames);

            /*
            public InputTag() {
                super("input");
            }
            */

            if (!aDelete) {
                System.out.println("writing to " + path);
                Files.write(path, classString.getBytes(StandardCharsets.UTF_8));
            }
        }

        System.out.println("// ContainerTags, generated in " + SpecializedTagClassCodeGenerator.class);

        for (final String tag : containerTags()) {
            final Path path = makePath(aAbsPath, tag);
            final String className = classNameFromTag(tag);

            final List<String> interfaceNames = getInterfaceNamesForTag(tag);

            final String classString = getClassTemplate(className, Optional.of("ContainerTag<" + className + ">"),
              Arrays.asList("info.freelibrary.j2htmx.pico.tags.ContainerTag",
                "info.freelibrary.j2htmx.pico.tags.attributes.*"), tag, interfaceNames);

            if (aDelete) {
                if (Files.exists(path)) {
                    System.out.println("deleting " + path);
                    Files.delete(path);
                }
            } else {
                System.out.println("writing to " + path);
                Files.write(path, classString.getBytes(StandardCharsets.UTF_8));
            }
        }
    }

    /**
     * Converts an HTML tag name into its corresponding class name.
     *
     * @param aTagNameLowerCase The lowercase tag name
     * @return The Java class name with "Tag" suffix
     */
    public static String classNameFromTag(final String aTagNameLowerCase) {
        final String res = aTagNameLowerCase.substring(0, 1).toUpperCase(Locale.US) + aTagNameLowerCase.substring(1);
        return res + "Tag";
    }

    /**
     * Constructs the file path for the given tag class.
     *
     * @param aAbsPath The base output path
     * @param aTagLowerCase The lowercase tag name
     * @return The path to the tag class file
     */
    private static Path makePath(final Path aAbsPath, final String aTagLowerCase) {
        final String filename = classNameFromTag(aTagLowerCase) + ".java";
        return Paths.get(aAbsPath.toString(), RELATIVE_OUTPUT_PATH, filename);
    }

    /**
     * Returns the package declaration line.
     *
     * @return The package statement
     */
    private static String getPackage() {
        return "package info.freelibrary.j2htmx.pico.tags.specialized;\n";
    }

    /**
     * Generates the class template string.
     *
     * @param aClassName The class name
     * @param anOptExtends The optional superclass
     * @param anImports The list of imports
     * @param aTag The tag name
     * @param anInterfaces The list of interfaces
     * @return The complete class source code
     */
    private static String getClassTemplate(final String aClassName, final Optional<String> anOptExtends,
      final List<String> anImports, final String aTag, final List<String> anInterfaces) {
        final StringBuilder builder = new StringBuilder(317);

        builder.append(getPackage()).append('\n');

        for (final String importName : anImports) {
            builder.append("import ").append(importName).append(";\n");
        }

        builder.append("\n/**\n * Represents a &lt;{@code ").append(aTag)
          .append("}&gt; tag.\n */\npublic final class ").append(aClassName).append(' ');
        anOptExtends.ifPresent(ext -> builder.append("extends ").append(ext).append(' '));

        // Add the 'implements' clause
        if (!anInterfaces.isEmpty()) {
            final List<String> genericInterfaceNames =
              anInterfaces.stream().map(iName -> iName + "<" + aClassName + ">").collect(Collectors.toList());
            builder.append("\nimplements ").append(String.join(",", genericInterfaceNames));
        }

        // Class contents
        builder.append(" {\n\n    /**\n     * Constructs a {@code ").append(aTag).append("} tag.\n     */\n    public ")
          .append(aClassName).append("() {\n        super(\"").append(aTag).append("\");\n    }\n}\n");

        return builder.toString();
    }

    /**
     * Retrieves the attribute interface names applicable for the given tag.
     *
     * @param aTagNameLowercase The lowercase tag name
     * @return List of interface names
     */
    private static List<String> getInterfaceNamesForTag(final String aTagNameLowercase) {
        return AttributesList.getCustomAttributesForHtmlTag(aTagNameLowercase).stream().map(
          AttributeInterfaceCodeGenerator::interfaceNameFromAttribute).collect(Collectors.toList());
    }

}
