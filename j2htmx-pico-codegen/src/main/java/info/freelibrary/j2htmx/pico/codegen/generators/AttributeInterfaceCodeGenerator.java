package info.freelibrary.j2htmx.pico.codegen.generators;

import info.freelibrary.j2htmx.pico.codegen.GeneratorUtil;
import info.freelibrary.j2htmx.pico.codegen.model.AttrD;
import info.freelibrary.j2htmx.pico.codegen.model.AttributesList;
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

/**
 * Code generator that outputs Java attribute interfaces for HTML elements.
 */
@SuppressWarnings(PMD.SYSTEM_PRINTLN)
public final class AttributeInterfaceCodeGenerator {

    /** The relative output path for attribute interface classes. */
    private static final String RELATIVE_OUTPUT_PATH = "tags/attributes/";

    /**
     * Private constructor to prevent instantiation.
     */
    private AttributeInterfaceCodeGenerator() {
        // This is intentionally left empty
    }

    /**
     * Generates or deletes attribute interface source files in the specified path.
     *
     * @param absPath The absolute path to output files
     * @param delete Whether to delete files instead of writing them
     * @throws IOException If an I/O error occurs
     */
    public static void generate(final Path absPath, final boolean delete) throws IOException {
        final Path dir = Paths.get(absPath.toString(), RELATIVE_OUTPUT_PATH);

        // Delete all files in the directory for fresh generation
        GeneratorUtil.deleteAllFilesInDir(dir);

        for (final AttrD attr : AttributesList.attributesDescriptive()) {
            final Path path = makePath(attr.attr, absPath);
            final String interfaceName = interfaceNameFromAttribute(attr.attr) + "<T extends Tag<T>>";
            final String interfaceStr = getInterfaceTemplate(
              interfaceName,
              Optional.of("Instance<T>"),
              Arrays.asList("info.freelibrary.j2htmx.pico.tags.Tag", "info.freelibrary.j2htmx.pico.tags.Instance"),
              interfaceNameFromAttribute(attr.attr),
              attr
            );

            if (!delete) {
                System.out.println("writing to " + path);
                Files.write(path, interfaceStr.getBytes(StandardCharsets.UTF_8));
            }
        }

    }

    private static String getPackage() {
        return "package info.freelibrary.j2htmx.pico.tags.attributes;\n";
    }

    private static String makeReturnTypeAndMethodName(final String name) {
        return "default T " + name;
    }

    private static String getInterfaceTemplate(
      final String interfaceName,
      final Optional<String> optExtends,
      final List<String> imports,
      final String interfaceNameSimple,
      final AttrD attrD
    ) {

        final StringBuilder sb = new StringBuilder(512);

        sb.append(getPackage()).append('\n');

        for (final String importName : imports) {
            sb.append("import ").append(importName).append(";\n");
        }

        sb.append("\n/**\n * Defines the {@code ")
          .append(attrD.attr)
          .append("} attribute for HTML tags.\n *\n * @param <T> The tag type\n */\npublic interface ")
          .append(interfaceName);

        optExtends.ifPresent(ext -> sb.append(" extends ").append(ext).append(' '));

        sb.append(" {\n");

        // Method parameter uses the aName convention to avoid Java keywords and satisfy PMD rules.
        final String attrName = interfaceNameSimple.toLowerCase(Locale.US);
        final String paramName = "a" + interfaceNameSimple;

        //depending on if the attribute has an argument or not,
        //generate methods according to the convention in Tag.java
        // arg -> with$ATTR(arg), withCond$ATTR(condition, arg)
        // no arg -> has$ATTR(), withCond$ATTR(condition)

        //append the 'with$ATTR' method
        writeAttributeMethod(interfaceNameSimple, attrD, sb, attrName, paramName);
        writeAttributeMethodCond(interfaceNameSimple, attrD, sb, attrName, paramName);

        sb.append("}\n");

        return sb.toString();
    }

    @SuppressWarnings(PMD.AVOID_LITERALS_IN_IF_CONDITION)
    private static void addAttributeNoArg(final StringBuilder sb, final String attrName) {
        if ("autocomplete".equals(attrName)) {
            sb.append("self().attr(\"").append(attrName).append("\",\"on\");\n");
        } else {
            sb.append("self().attr(\"").append(attrName).append("\");\n");
        }
    }

    private static void writeAttributeMethodCond(final String interfaceNameSimple, final AttrD attrD,
      final StringBuilder sb, final String attrName, final String paramName) {

        sb.append("\n    /**\n     * Conditionally sets the {@code ").append(attrName).append("} attribute");
        if (attrD.hasArgument) {
            sb.append(" with the specified value.\n     *\n     * @param enable Whether to set the attribute\n"
              + "     * @param ").append(paramName).append(" The attribute value\n     * @return The tag instance\n"
              + "     */\n    ");
        } else {
            sb.append(".\n     *\n     * @param enable Whether to set the attribute\n"
              + "     * @return The tag instance\n     */\n    ");
        }
        sb.append(makeReturnTypeAndMethodName("withCond" + interfaceNameSimple));

        if (attrD.hasArgument) {
            //add a variant where you can specify the argument
            sb.append("(final boolean enable, final String ")
              .append(paramName)
              .append(") {if (enable){\nself().attr(\"")
              .append(attrName)
              .append("\", ")
              .append(paramName)
              .append(");\n}\nreturn self();\n}\n");
        } else {
            //add a variant where you can toggle the attribute
            sb.append("(final boolean enable) {if (enable){\n");
            addAttributeNoArg(sb, attrName);
            sb.append("}\nreturn self();\n}\n");
        }
    }

    private static void writeAttributeMethod(final String interfaceNameSimple, final AttrD attrD,
      final StringBuilder sb, final String attrName, final String paramName) {

        sb.append("\n    /**\n     * Sets the {@code ").append(attrName).append("} attribute");
        if (attrD.hasArgument) {
            sb.append(" with the specified value.\n     *\n     * @param ").append(paramName)
              .append(" The attribute value\n     * @return The tag instance\n     */\n    ");
        } else {
            sb.append(".\n     *\n     * @return The tag instance\n     */\n    ");
        }
        sb.append(makeReturnTypeAndMethodName(
          (attrD.hasArgument ? "with" : "has") + interfaceNameSimple)
        );

        if (attrD.hasArgument) {
            //add a variant where you can specify the argument
            sb.append("(final String ")
              .append(paramName)
              .append(") {return self().attr(\"")
              .append(attrName)
              .append("\", ")
              .append(paramName)
              .append(");\n}\n");
        } else {
            //add a variant where you can toggle the attribute
            sb.append("() {");
            addAttributeNoArg(sb, attrName);
            sb.append("return self();\n}\n");
        }
    }

    /**
     * Converts an attribute name into its corresponding interface name.
     *
     * @param attribute The attribute name
     * @return The interface name with first letter capitalized
     */
    public static String interfaceNameFromAttribute(final String attribute) {
        return attribute.substring(0, 1).toUpperCase(Locale.US) + attribute.substring(1);
    }

    private static Path makePath(final String tagLowerCase, final Path absPath) {
        final String filename = interfaceNameFromAttribute(tagLowerCase) + ".java";
        return Paths.get(absPath.toString(), RELATIVE_OUTPUT_PATH, filename);
    }

}
