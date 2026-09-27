package info.freelibrary.j2htmx.pico.codegen;

import static info.freelibrary.j2htmx.pico.codegen.Model.Metadata.ON_OFF;
import static info.freelibrary.j2htmx.pico.codegen.Model.Metadata.SELF_CLOSING;

import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.JavaFile;
import com.palantir.javapoet.MethodSpec;
import com.palantir.javapoet.ParameterizedTypeName;
import com.palantir.javapoet.TypeName;
import com.palantir.javapoet.TypeSpec;
import com.palantir.javapoet.TypeVariableName;
import info.freelibrary.j2htmx.pico.codegen.Model.Node;

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
public class Generator {

    /** Class name constant for Instance. */
    public static final ClassName INSTANCE = ClassName.get("info.freelibrary.j2htmx.pico.tags", "Instance");

    /** Class name constant for Tag. */
    public static final ClassName TAG = ClassName.get("info.freelibrary.j2htmx.pico.tags", "Tag");

    /** Class name constant for EmptyTag. */
    public static final ClassName EMPTY_TAG = ClassName.get("info.freelibrary.j2htmx.pico.tags", "EmptyTag");

    /** Class name constant for ContainerTag. */
    public static final ClassName CONTAINER_TAG = ClassName.get("info.freelibrary.j2htmx.pico.tags", "ContainerTag");

    private Generator() {
    }

    /**
     * Standalone main method to test code generation.
     *
     * @param args Command-line arguments
     * @throws IOException If an I/O error occurs
     */
    public static void main(final String... args) throws IOException {
        final Path path = Paths.get("j2html-codegen", "src", "test", "resources", "html.model");
        final String definitions = new String(Files.readAllBytes(path));
        final Model model = new Model();
        Parser.parse(definitions, model);

        final Path dir = Paths.get("/j2html/generated-source");
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
                  ParameterizedTypeName.get(
                    ClassName.get(file.packageName(), file.typeSpec().name()),
                    className
                  )
                );
            }

            files.put(
              element.name,
              JavaFile.builder(pkg, type.build())
                .skipJavaLangImports(true)
                .build()
            );
        }

        return files;
    }

    private static Map<String, JavaFile> generateAttributePackage(final String pkg, final Model model) {
        final Map<String, JavaFile> files = new HashMap<>();

        // Convert all attributes into classes.
        for (final Node attribute : model.attributes()) {
            final TypeSpec.Builder type = defineAttributeClass(pkg, attribute);

            if (attribute.type.equals(Node.Type.STRING)) {
                defineStringAttributeMethods(attribute, type);
            } else if (attribute.type.equals(Node.Type.BOOLEAN) && !attribute.is(ON_OFF)) {
                defineBooleanAttributeMethods(attribute, type);
            } else if (attribute.type.equals(Node.Type.BOOLEAN) && attribute.is(ON_OFF)) {
                defineOnOffAttributeMethods(attribute, type);
            }

            files.put(
              attribute.name,
              JavaFile.builder(pkg, type.build())
                .skipJavaLangImports(true)
                .build()
            );
        }

        return files;
    }

    private static TypeSpec.Builder defineElementClass(final Node element, final ClassName className) {
        final MethodSpec constructor = MethodSpec.constructorBuilder()
          .addModifiers(Modifier.PUBLIC)
          .addStatement("super(\"" + element.name + "\")")
          .build();

        final TypeSpec.Builder type = TypeSpec.classBuilder(className)
          .addModifiers(Modifier.PUBLIC)
          .superclass(
            ParameterizedTypeName.get(element.is(SELF_CLOSING) ? EMPTY_TAG : CONTAINER_TAG, className)
          )
          .addMethod(constructor);
        return type;
    }

    private static TypeSpec.Builder defineAttributeClass(final String pkg, final Node attribute) {
        final ClassName name = ClassName.get(pkg, capitalize(attribute.name));
        return TypeSpec.interfaceBuilder(name)
          .addSuperinterface(ParameterizedTypeName.get(INSTANCE, TypeVariableName.get("T")))
          .addTypeVariable(TypeVariableName.get("T", ParameterizedTypeName.get(TAG, TypeVariableName.get("T"))))
          .addModifiers(Modifier.PUBLIC);
    }

    private static void defineBooleanAttributeMethods(final Node attribute, final TypeSpec.Builder type) {
        final MethodSpec with = MethodSpec.methodBuilder(methodName("is", attribute.name))
          .addModifiers(Modifier.PUBLIC, Modifier.DEFAULT)
          .addStatement("return self().attr(\"" + attribute.name + "\")")
          .returns(TypeVariableName.get("T"))
          .build();

        final MethodSpec withCond = MethodSpec.methodBuilder(methodName("withCond", attribute.name))
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
          .addModifiers(Modifier.PUBLIC, Modifier.DEFAULT)
          .addStatement("return self().attr(\"" + attribute.name + "\", \"on\")")
          .returns(TypeVariableName.get("T"))
          .build();

        final MethodSpec withCond = MethodSpec.methodBuilder(methodName("withCond", attribute.name))
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
          .addModifiers(Modifier.PUBLIC, Modifier.DEFAULT)
          .addParameter(String.class, parameter(attribute), Modifier.FINAL)
          .addStatement("return self().attr(\"" + attribute.name + "\", " + parameter(attribute) + ")")
          .returns(TypeVariableName.get("T"))
          .build();

        final MethodSpec withCond = MethodSpec.methodBuilder(methodName("withCond", attribute.name))
          .addModifiers(Modifier.PUBLIC, Modifier.DEFAULT)
          .addParameter(TypeName.BOOLEAN, "enable", Modifier.FINAL)
          .addParameter(String.class, parameter(attribute), Modifier.FINAL)
          .addStatement(
            "return enable ? self().attr(\"" + attribute.name + "\", " + parameter(attribute) + ") : self()")
          .returns(TypeVariableName.get("T"))
          .build();

        type.addMethod(with);
        type.addMethod(withCond);
    }

    private static String parameter(final Node attribute) {
        return attribute.name.replace("-", "_") + "_";
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
