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
import info.freelibrary.util.warnings.Checkstyle;
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
@SuppressWarnings({Checkstyle.MULTIPLE_STRING_LITERALS, "MultipleStringLiterals"})
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
    @SuppressWarnings({Checkstyle.UNCOMMENTED_MAIN, "UncommentedMain"})
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
     * @param aRoot The root output directory
     * @param anAttributePkg Package name for attribute interfaces
     * @param anElementPkg Package name for element classes
     * @param aModel The model definitions
     * @throws IOException If an I/O error occurs
     */
    public static void generate(final Path aRoot, final String anAttributePkg, final String anElementPkg,
      final Model aModel) throws IOException {
        final Map<String, JavaFile> attributes = generateAttributePackage(anAttributePkg, aModel);
        for (final JavaFile file : attributes.values()) {
            file.writeTo(aRoot);
        }

        final Map<String, JavaFile> elements = generateElementPackage(anElementPkg, aModel, attributes);
        for (final JavaFile file : elements.values()) {
            file.writeTo(aRoot);
        }
    }

    /**
     * Generates the element package containing tag classes.
     *
     * @param aPkg The package name
     * @param aModel The model definitions
     * @param anAttributes Map of attribute java files
     * @return Map of element name to java file
     */
    private static Map<String, JavaFile> generateElementPackage(final String aPkg, final Model aModel,
      final Map<String, JavaFile> anAttributes) {
        final Map<String, JavaFile> files = new HashMap<>();

        // Convert all elements into classes.
        for (final Node element : aModel.elements()) {
            final ClassName className = ClassName.get(aPkg, capitalize(element.myName) + "Tag");

            final TypeSpec.Builder type = defineElementClass(element, className);

            // Assign attributes to this element.
            for (final Node attribute : element.myChildren) {
                final JavaFile file = anAttributes.get(attribute.myName);
                type.addSuperinterface(
                  ParameterizedTypeName.get(ClassName.get(file.packageName(), file.typeSpec().name()), className));
            }

            files.put(element.myName, JavaFile.builder(aPkg, type.build()).skipJavaLangImports(true).build());
        }

        return files;
    }

    /**
     * Generates the attribute package containing attribute interfaces.
     *
     * @param aPkg The package name
     * @param aModel The model definitions
     * @return Map of attribute name to java file
     */
    private static Map<String, JavaFile> generateAttributePackage(final String aPkg, final Model aModel) {
        final Map<String, JavaFile> files = new HashMap<>();

        // Convert all attributes into classes.
        for (final Node attribute : aModel.attributes()) {
            final TypeSpec.Builder type = defineAttributeClass(aPkg, attribute);

            if (attribute.myType == Node.Type.STRING) {
                defineStringAttributeMethods(attribute, type);
            } else if (attribute.myType == Node.Type.BOOLEAN && !attribute.has(ON_OFF)) {
                defineBooleanAttributeMethods(attribute, type);
            } else if (attribute.myType == Node.Type.BOOLEAN && attribute.has(ON_OFF)) {
                defineOnOffAttributeMethods(attribute, type);
            }

            files.put(attribute.myName, JavaFile.builder(aPkg, type.build()).skipJavaLangImports(true).build());
        }

        return files;
    }

    /**
     * Defines the element class specification.
     *
     * @param anElement The element model node
     * @param aClassName The class name
     * @return The class builder
     */
    @SuppressWarnings(PMD.AVOID_LITERALS_IN_IF_CONDITION)
    private static TypeSpec.Builder defineElementClass(final Node anElement, final ClassName aClassName) {
        final MethodSpec constructor = MethodSpec.constructorBuilder()
          .addJavadoc("Constructs a {@code $L} tag.\n", anElement.myName)
          .addModifiers(Modifier.PUBLIC)
          .addStatement("super(\"" + anElement.myName + "\")")
          .build();

        final TypeSpec.Builder type = TypeSpec.classBuilder(aClassName)
          .addJavadoc("Represents a &lt;{@code $L}&gt; tag.\n", anElement.myName)
          .addModifiers(Modifier.PUBLIC)
          .superclass(ParameterizedTypeName.get(anElement.has(SELF_CLOSING)
            ? ClassName.get(EmptyTag.class) : ClassName.get(ContainerTag.class), aClassName))
          .addMethod(constructor);

        if ("video".equals(anElement.myName)) {
            type.addAnnotation(AnnotationSpec.builder(SuppressWarnings.class)
              .addMember("value", "$T.EXCESSIVE_IMPORTS", PMD.class).build());
        }

        return type;
    }

    /**
     * Defines the attribute interface specification.
     *
     * @param aPkg The package name
     * @param anAttribute The attribute model node
     * @return The interface builder
     */
    private static TypeSpec.Builder defineAttributeClass(final String aPkg, final Node anAttribute) {
        final ClassName name = ClassName.get(aPkg, capitalize(anAttribute.myName));
        return TypeSpec.interfaceBuilder(name)
          .addJavadoc("Defines the {@code $L} attribute for HTML/XML tags.\n\n@param <T> The tag type\n",
            anAttribute.myName)
          .addSuperinterface(
            ParameterizedTypeName.get(ClassName.get(Instance.class), TypeVariableName.get("T"))).addTypeVariable(
            TypeVariableName.get("T", ParameterizedTypeName.get(
              ClassName.get("info.freelibrary.j2htmx.pico.tags", "AbstractTag"), TypeVariableName.get("T"))))
          .addModifiers(Modifier.PUBLIC);
    }

    /**
     * Defines methods for a boolean attribute.
     *
     * @param anAttribute The attribute model node
     * @param aType The interface builder
     */
    private static void defineBooleanAttributeMethods(final Node anAttribute, final TypeSpec.Builder aType) {
        final MethodSpec with = MethodSpec.methodBuilder(methodName("is", anAttribute.myName))
          .addJavadoc("Sets the {@code $L} attribute.\n\n@return The tag instance\n", anAttribute.myName)
          .addModifiers(Modifier.PUBLIC, Modifier.DEFAULT)
          .addStatement("return self().attr(\"" + anAttribute.myName + "\")")
          .returns(TypeVariableName.get("T"))
          .build();

        final MethodSpec withCond = MethodSpec.methodBuilder(methodName("withCond", anAttribute.myName))
          .addJavadoc("Conditionally sets the {@code $L} attribute.\n\n"
            + "@param aEnable Whether to set the attribute\n@return The tag instance\n", anAttribute.myName)
          .addModifiers(Modifier.PUBLIC, Modifier.DEFAULT)
          .addParameter(TypeName.BOOLEAN, "aEnable", Modifier.FINAL)
          .addStatement("return aEnable ? self().attr(\"" + anAttribute.myName + "\") : self()")
          .returns(TypeVariableName.get("T"))
          .build();

        aType.addMethod(with);
        aType.addMethod(withCond);
    }

    /**
     * Defines methods for an on/off boolean attribute.
     *
     * @param anAttribute The attribute model node
     * @param aType The interface builder
     */
    private static void defineOnOffAttributeMethods(final Node anAttribute, final TypeSpec.Builder aType) {
        final MethodSpec with = MethodSpec.methodBuilder(methodName("is", anAttribute.myName))
          .addJavadoc("Sets the {@code $L} attribute to \"on\".\n\n@return The tag instance\n", anAttribute.myName)
          .addModifiers(Modifier.PUBLIC, Modifier.DEFAULT)
          .addStatement("return self().attr(\"" + anAttribute.myName + "\", \"on\")")
          .returns(TypeVariableName.get("T"))
          .build();

        final MethodSpec withCond = MethodSpec.methodBuilder(methodName("withCond", anAttribute.myName))
          .addJavadoc("Conditionally sets the {@code $L} attribute to \"on\".\n\n"
            + "@param aEnable Whether to set the attribute\n@return The tag instance\n", anAttribute.myName)
          .addModifiers(Modifier.PUBLIC, Modifier.DEFAULT)
          .addParameter(TypeName.BOOLEAN, "aEnable", Modifier.FINAL)
          .addStatement("return aEnable ? self().attr(\"" + anAttribute.myName + "\", \"on\") : self()")
          .returns(TypeVariableName.get("T"))
          .build();

        aType.addMethod(with);
        aType.addMethod(withCond);
    }

    /**
     * Defines methods for a string attribute.
     *
     * @param anAttribute The attribute model node
     * @param aType The interface builder
     */
    private static void defineStringAttributeMethods(final Node anAttribute, final TypeSpec.Builder aType) {
        final MethodSpec with = MethodSpec.methodBuilder(methodName("with", anAttribute.myName))
          .addJavadoc("Sets the {@code $L} attribute with the specified value.\n\n"
            + "@param $L The attribute value\n@return The tag instance\n", anAttribute.myName, parameter(anAttribute))
          .addModifiers(Modifier.PUBLIC, Modifier.DEFAULT)
          .addParameter(String.class, parameter(anAttribute), Modifier.FINAL)
          .addStatement("return self().attr(\"" + anAttribute.myName + "\", " + parameter(anAttribute) + ")")
          .returns(TypeVariableName.get("T"))
          .build();

        final MethodSpec withCond = MethodSpec.methodBuilder(methodName("withCond", anAttribute.myName))
          .addJavadoc("Conditionally sets the {@code $L} attribute with the specified value.\n\n"
            + "@param aEnable Whether to set the attribute\n@param $L The attribute value\n@return The tag instance\n",
            anAttribute.myName, parameter(anAttribute))
          .addModifiers(Modifier.PUBLIC, Modifier.DEFAULT)
          .addParameter(TypeName.BOOLEAN, "aEnable", Modifier.FINAL)
          .addParameter(String.class, parameter(anAttribute), Modifier.FINAL)
          .addStatement("return aEnable ? self().attr(\"" + anAttribute.myName + "\", " + parameter(anAttribute) +
            ") : self()")
          .returns(TypeVariableName.get("T"))
          .build();

        aType.addMethod(with);
        aType.addMethod(withCond);
    }

    /**
     * Generates the parameter name for an attribute.
     *
     * @param anAttribute The attribute model node
     * @return The parameter name
     */
    private static String parameter(final Node anAttribute) {
        final char firstChar = Character.toLowerCase(anAttribute.myName.charAt(0));
        final String prefix = (
          firstChar == 'a' || firstChar == 'e' || firstChar == 'i' || firstChar == 'o' || firstChar == 'u'
        ) ? "an" : "a";

        return methodName(prefix, anAttribute.myName);
    }

    /**
     * Generates a camelCase method name from word components.
     *
     * @param aWords The word parts
     * @return The camelCase method name
     */
    private static String methodName(final String... aWords) {
        final String[] camelCase = new String[aWords.length];
        camelCase[0] = aWords[0];
        for (int i = 1; i < aWords.length; i++) {
            camelCase[i] = capitalize(aWords[i]);
        }
        return String.join("", camelCase);
    }

    /**
     * Capitalizes the given word.
     *
     * @param aWord The word to capitalize
     * @return The capitalized word
     */
    private static String capitalize(final String aWord) {
        if (aWord.contains("-")) {
            final String[] parts = aWord.split("-");
            final StringBuilder sb = new StringBuilder();
            for (final String part : parts) {
                sb.append(part.substring(0, 1).toUpperCase(Locale.US)).append(part.substring(1).toLowerCase(Locale.US));
            }
            return sb.toString();
        }
        return aWord.substring(0, 1).toUpperCase(Locale.US) + aWord.substring(1).toLowerCase(Locale.US);
    }
}
