package info.freelibrary.j2htmx.pico.codegen;

import static info.freelibrary.j2htmx.pico.codegen.Model.Metadata.ON_OFF;
import static info.freelibrary.j2htmx.pico.codegen.Model.Metadata.SELF_CLOSING;

import com.palantir.javapoet.AnnotationSpec;
import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.JavaFile;
import com.palantir.javapoet.MethodSpec;
import com.palantir.javapoet.ParameterizedTypeName;
import com.palantir.javapoet.TypeName;
import com.palantir.javapoet.TypeSpec;
import com.palantir.javapoet.TypeVariableName;
import info.freelibrary.j2htmx.pico.codegen.Model.Node;
import info.freelibrary.j2htmx.pico.tags.ContainerTag;
import info.freelibrary.j2htmx.pico.tags.EmptyTag;
import info.freelibrary.j2htmx.pico.tags.Instance;
import info.freelibrary.util.warnings.PMD;

import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Java source code generator producing tag classes and attribute interfaces from model definitions.
 */
public final class Generator {

    /**
     * Private constructor to prevent instantiation of the {@code Generator} class. This utility class is not intended
     * to be instantiated; it provides static methods for generating Java source code.
     */
    private Generator() {
        // This is intentionally left empty
    }

    /**
     * Standalone main method to test code generation.
     *
     * @param args Command-line arguments
     * @throws IOException If an I/O error occurs
     */
    public static void main(final String... args) throws IOException {
        final Path path = Paths.get("j2html-codegen", "src", "test", "resources", "html.model");
        final String definitions = Files.readString(path);
        final Model model = new Model();
        final Path dir;

        Parser.parse(definitions, model);

        dir = Paths.get("/j2html/generated-source");
        Files.createDirectories(dir);
        generate(dir, "info.freelibrary.j2htmx.pico.tags.attributes", "info.freelibrary.j2htmx.pico.tags.specialized",
          model);
    }

    /**
     * Generates Java source files for attributes and elements into the specified root directory.
     *
     * @param root The root output directory
     * @param attributePkg Package name for attribute interfaces
     * @param elementPkg Package name for element classes
     * @param model The model definitions
     * @throws IOException If an I/O error occurs
     */
    public static void generate(final Path root, final String attributePkg, final String elementPkg, final Model model)
    throws IOException {
        final Map<String, JavaFile> attributes = generateAttributePackage(attributePkg, model);
        for (final JavaFile file : attributes.values()) {
            file.writeTo(root);
        }

        final Map<String, JavaFile> elements = generateElementPackage(elementPkg, model, attributes);
        for (final JavaFile file : elements.values()) {
            file.writeTo(root);
        }
    }

    private static Map<String, JavaFile> generateElementPackage(final String pkg, final Model model,
      final Map<String, JavaFile> attributes) {
        final Map<String, JavaFile> files = new HashMap<>();

        // Convert all elements into classes.
        for (final Node element : model.elements()) {
            final ClassName className = ClassName.get(pkg, capitalize(element.name) + "Tag");

            final TypeSpec.Builder type = defineElementClass(element, className);

            // Assign attributes to this element.
            for (final Node attribute : element.children) {
                final JavaFile file = attributes.get(attribute.name);
                type.addSuperinterface(
                  ParameterizedTypeName.get(ClassName.get(file.packageName(), file.typeSpec().name()), className));
            }

            files.put(element.name, JavaFile.builder(pkg, type.build()).skipJavaLangImports(true).build());
        }

        return files;
    }

    private static Map<String, JavaFile> generateAttributePackage(final String pkg, final Model model) {
        final Map<String, JavaFile> files = new HashMap<>();

        // Convert all attributes into classes.
        for (final Node attribute : model.attributes()) {
            final TypeSpec.Builder type = defineAttributeClass(pkg, attribute);

            if (attribute.type == Node.Type.STRING) {
                defineStringAttributeMethods(attribute, type);
            } else if (attribute.type == Node.Type.BOOLEAN && !attribute.has(ON_OFF)) {
                defineBooleanAttributeMethods(attribute, type);
            } else if (attribute.type == Node.Type.BOOLEAN && attribute.has(ON_OFF)) {
                defineOnOffAttributeMethods(attribute, type);
            }

            files.put(attribute.name, JavaFile.builder(pkg, type.build()).skipJavaLangImports(true).build());
        }

        return files;
    }

    private static TypeSpec.Builder defineElementClass(final Node element, final ClassName className) {
        final MethodSpec constructor = MethodSpec.constructorBuilder()
          .addJavadoc("Constructs a {@code $L} tag.\n", element.name)
          .addModifiers(Modifier.PUBLIC)
          .addStatement("super(\"" + element.name + "\")")
          .build();

        return TypeSpec.classBuilder(className)
          .addJavadoc("Represents a &lt;{@code $L}&gt; tag.\n", element.name)
          .addAnnotation(AnnotationSpec.builder(SuppressWarnings.class)
            .addMember("value", "$T.EXCESSIVE_IMPORTS", PMD.class).build())
          .addModifiers(Modifier.PUBLIC)
          .superclass(ParameterizedTypeName.get(
            element.has(SELF_CLOSING) ? ClassName.get(EmptyTag.class) : ClassName.get(ContainerTag.class), className))
          .addMethod(constructor);
    }

    private static TypeSpec.Builder defineAttributeClass(final String pkg, final Node attribute) {
        final ClassName name = ClassName.get(pkg, capitalize(attribute.name));
        return TypeSpec.interfaceBuilder(name)
          .addJavadoc("Defines the {@code $L} attribute for HTML/XML tags.\n\n@param <T> The tag type\n",
            attribute.name)
          .addSuperinterface(
            ParameterizedTypeName.get(ClassName.get(Instance.class), TypeVariableName.get("T"))).addTypeVariable(
            TypeVariableName.get("T", ParameterizedTypeName.get(
              ClassName.get("info.freelibrary.j2htmx.pico.tags", "AbstractTag"), TypeVariableName.get("T"))))
          .addModifiers(Modifier.PUBLIC);
    }

    private static void defineBooleanAttributeMethods(final Node attribute, final TypeSpec.Builder type) {
        final MethodSpec with = MethodSpec.methodBuilder(methodName("is", attribute.name))
          .addJavadoc("Sets the {@code $L} attribute.\n\n@return The tag instance\n", attribute.name)
          .addModifiers(Modifier.PUBLIC, Modifier.DEFAULT)
          .addStatement("return self().attr(\"" + attribute.name + "\")")
          .returns(TypeVariableName.get("T"))
          .build();

        final MethodSpec withCond = MethodSpec.methodBuilder(methodName("withCond", attribute.name))
          .addJavadoc("Conditionally sets the {@code $L} attribute.\n\n"
            + "@param enable Whether to set the attribute\n@return The tag instance\n", attribute.name)
          .addModifiers(Modifier.PUBLIC, Modifier.DEFAULT)
          .addParameter(TypeName.BOOLEAN, "enable", Modifier.FINAL)
          .addStatement("return enable ? self().attr(\"" + attribute.name + "\") : self()")
          .returns(TypeVariableName.get("T"))
          .build();

        type.addMethod(with);
        type.addMethod(withCond);
    }

    private static void defineOnOffAttributeMethods(final Node attribute, final TypeSpec.Builder type) {
        final MethodSpec with = MethodSpec.methodBuilder(methodName("is", attribute.name))
          .addJavadoc("Sets the {@code $L} attribute to \"on\".\n\n@return The tag instance\n", attribute.name)
          .addModifiers(Modifier.PUBLIC, Modifier.DEFAULT)
          .addStatement("return self().attr(\"" + attribute.name + "\", \"on\")")
          .returns(TypeVariableName.get("T"))
          .build();

        final MethodSpec withCond = MethodSpec.methodBuilder(methodName("withCond", attribute.name))
          .addJavadoc("Conditionally sets the {@code $L} attribute to \"on\".\n\n"
            + "@param enable Whether to set the attribute\n@return The tag instance\n", attribute.name)
          .addModifiers(Modifier.PUBLIC, Modifier.DEFAULT)
          .addParameter(TypeName.BOOLEAN, "enable", Modifier.FINAL)
          .addStatement("return enable ? self().attr(\"" + attribute.name + "\", \"on\") : self()")
          .returns(TypeVariableName.get("T"))
          .build();

        type.addMethod(with);
        type.addMethod(withCond);
    }

    private static void defineStringAttributeMethods(final Node attribute, final TypeSpec.Builder type) {
        final MethodSpec with = MethodSpec.methodBuilder(methodName("with", attribute.name))
          .addJavadoc("Sets the {@code $L} attribute with the specified value.\n\n"
            + "@param $L The attribute value\n@return The tag instance\n", attribute.name, parameter(attribute))
          .addModifiers(Modifier.PUBLIC, Modifier.DEFAULT)
          .addParameter(String.class, parameter(attribute), Modifier.FINAL)
          .addStatement("return self().attr(\"" + attribute.name + "\", " + parameter(attribute) + ")")
          .returns(TypeVariableName.get("T"))
          .build();

        final MethodSpec withCond = MethodSpec.methodBuilder(methodName("withCond", attribute.name))
          .addJavadoc("Conditionally sets the {@code $L} attribute with the specified value.\n\n"
            + "@param enable Whether to set the attribute\n@param $L The attribute value\n@return The tag instance\n",
            attribute.name, parameter(attribute))
          .addModifiers(Modifier.PUBLIC, Modifier.DEFAULT)
          .addParameter(TypeName.BOOLEAN, "enable", Modifier.FINAL)
          .addParameter(String.class, parameter(attribute), Modifier.FINAL)
          .addStatement("return enable ? self().attr(\"" + attribute.name + "\", " + parameter(attribute) + ") : self()")
          .returns(TypeVariableName.get("T"))
          .build();

        type.addMethod(with);
        type.addMethod(withCond);
    }

    private static String parameter(final Node attribute) {
        return methodName("a", attribute.name);
    }

    private static String methodName(final String... words) {
        final String[] camelCase = new String[words.length];
        camelCase[0] = words[0];
        for (int i = 1; i < words.length; i++) {
            camelCase[i] = capitalize(words[i]);
        }
        return String.join("", camelCase);
    }

    private static String capitalize(final String word) {
        if (word.contains("-")) {
            final String[] parts = word.split("-");
            final StringBuilder sb = new StringBuilder();
            for (final String part : parts) {
                sb.append(part.substring(0, 1).toUpperCase(Locale.US)).append(part.substring(1).toLowerCase(Locale.US));
            }
            return sb.toString();
        }
        return word.substring(0, 1).toUpperCase(Locale.US) + word.substring(1).toLowerCase(Locale.US);
    }
}
