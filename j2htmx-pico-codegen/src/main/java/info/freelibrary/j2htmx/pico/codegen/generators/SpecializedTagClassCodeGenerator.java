package info.freelibrary.j2htmx.pico.codegen.generators;


import static info.freelibrary.j2htmx.pico.codegen.generators.TagCreatorCodeGenerator.containerTags;
import static info.freelibrary.j2htmx.pico.codegen.generators.TagCreatorCodeGenerator.emptyTags;

import info.freelibrary.j2htmx.pico.codegen.GeneratorUtil;
import info.freelibrary.j2htmx.pico.codegen.model.AttributesList;
import info.freelibrary.util.warnings.PMD;

import java.io.IOException;
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
@SuppressWarnings(PMD.SYSTEM_PRINTLN)
public final class SpecializedTagClassCodeGenerator {

    /** The relative output path for specialized tag classes. */
    private static final String relPath = "tags/specialized";

    private SpecializedTagClassCodeGenerator() {
    }

    /**
     * Generates or deletes specialized tag classes in the specified path.
     *
     * @param absPath The absolute output path
     * @param delete Whether to delete files instead of generating them
     * @throws IOException If an I/O error occurs
     */
    public static void generate(final Path absPath, final boolean delete) throws IOException {

        //delete all files in the directory for fresh generation
        final Path dir = Paths.get(absPath.toString(), relPath);
        GeneratorUtil.deleteAllFilesInDir(dir);

        //the delete argument serves to give the possibility
        //to delete the classes that were written before
        System.out.println("// EmptyTags, generated in " + SpecializedTagClassCodeGenerator.class);

        for (final String tag : emptyTags()) {
            final String className = classNameFromTag(tag);
            final Path path = makePath(absPath, tag);

            final List<String> interfaceNames = getInterfaceNamesForTag(tag);

            final String classString =
              getClassTemplate(
                className,
                Optional.of("EmptyTag<" + className + ">"),
                Arrays.asList(
                  "info.freelibrary.j2htmx.pico.tags.EmptyTag",
                  "info.freelibrary.j2htmx.pico.tags.attributes.*"
                ),
                tag,
                interfaceNames
              );

            /*
            public InputTag() {
                super("input");
            }
            */

            if (!delete) {
                System.out.println("writing to " + path);
                Files.write(path, classString.getBytes());
            }
        }

        System.out.println("// ContainerTags, generated in " + SpecializedTagClassCodeGenerator.class);

        for (final String tag : containerTags()) {
            final Path path = makePath(absPath, tag);
            final String className = classNameFromTag(tag);

            final List<String> interfaceNames = getInterfaceNamesForTag(tag);

            final String classString =
              getClassTemplate(
                className,
                Optional.of("ContainerTag<" + className + ">"),
                Arrays.asList(
                  "info.freelibrary.j2htmx.pico.tags.ContainerTag",
                  "info.freelibrary.j2htmx.pico.tags.attributes.*"
                ),
                tag,
                interfaceNames
              );

            if (delete) {
                if (Files.exists(path)) {
                    System.out.println("deleting " + path);
                    Files.delete(path);
                }
            } else {
                System.out.println("writing to " + path);
                Files.write(path, classString.getBytes());
            }
        }
    }

    /**
     * Converts an HTML tag name into its corresponding class name.
     *
     * @param tageNameLowerCase The lowercase tag name
     * @return The Java class name with "Tag" suffix
     */
    public static String classNameFromTag(final String tageNameLowerCase) {
        final String res = tageNameLowerCase.substring(0, 1).toUpperCase(Locale.US) + tageNameLowerCase.substring(1);
        return res + "Tag";
    }

    private static Path makePath(final Path absPath, final String tagLowerCase) {
        final String filename = classNameFromTag(tagLowerCase) + ".java";
        return Paths.get(absPath.toString(), relPath, filename);
    }

    private static String getPackage() {
        return "package info.freelibrary.j2htmx.pico.tags.specialized;\n";
    }

    private static String getClassTemplate(
      final String className,
      final Optional<String> optExtends,
      final List<String> imports,
      final String tag,
      final List<String> interfaces
    ) {

        final StringBuilder sb = new StringBuilder(256);

        sb.append(getPackage()).append('\n');

        for (final String importName : imports) {
            sb.append("import ").append(importName).append(";\n");
        }

        sb.append("\npublic final class ")
          .append(className)
          .append(' ');

        optExtends.ifPresent(ext -> sb.append("extends ").append(ext).append(' '));

        //add the 'implements' clause
        if (!interfaces.isEmpty()) {
            final List<String> genericInterfaceNames
              = interfaces.stream().map(iName -> iName + "<" + className + ">")
              .collect(Collectors.toList());
            sb.append("\nimplements ").append(String.join(",", genericInterfaceNames));
        }

        //class contents
        sb.append(" {\npublic ")
          .append(className)
          .append("() {super(\"")
          .append(tag)
          .append("\");}\n}\n");

        return sb.toString();
    }

    private static List<String> getInterfaceNamesForTag(final String tagNameLowercase) {
        return AttributesList.getCustomAttributesForHtmlTag(tagNameLowercase)
          .stream()
          .map(
            AttributeInterfaceCodeGenerator::interfaceNameFromAttribute
          ).collect(Collectors.toList());
    }

}
