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
import java.util.Set;

/**
 * Java source code generator producing tag classes and attribute interfaces from model definitions.
 */
@SuppressWarnings({Checkstyle.MULTIPLE_STRING_LITERALS, "MultipleStringLiterals"})
public final class Generator {

    /** The package name for base tag classes and interfaces. */
    private static final String TAGS_PACKAGE = "info.freelibrary.j2htmx.pico.tags";

    /** The defer attribute name constant. */
    private static final String DEFER = "defer";

    /** Parameter name for conditional methods. */
    private static final String A_ENABLE = "aEnable";

    /** Mapping of multi-word attribute names to their PascalCase / CamelCase representations. */
    private static final Map<String, String> CAMEL_CASE_NAMES = Map.ofEntries(
        // HTML Boolean & Flag Attributes
        Map.entry("autocomplete", "AutoComplete"),
        Map.entry("autofocus", "AutoFocus"),
        Map.entry("autoplay", "AutoPlay"),
        Map.entry("ismap", "IsMap"),
        Map.entry("novalidate", "NoValidate"),
        Map.entry("readonly", "ReadOnly"),

        // HTML String & Value Attributes
        Map.entry("colspan", "ColSpan"),
        Map.entry("datetime", "DateTime"),
        Map.entry("dirname", "DirName"),
        Map.entry("enctype", "EncType"),
        Map.entry("formaction", "FormAction"),
        Map.entry("hreflang", "HrefLang"),
        Map.entry("maxlength", "MaxLength"),
        Map.entry("rowspan", "RowSpan"),
        Map.entry("srcdoc", "SrcDoc"),
        Map.entry("srclang", "SrcLang"),
        Map.entry("srcset", "SrcSet"),
        Map.entry("usemap", "UseMap"),

        // HTML Event Handlers
        Map.entry("onabort", "OnAbort"),
        Map.entry("onafterprint", "OnAfterPrint"),
        Map.entry("onbeforeprint", "OnBeforePrint"),
        Map.entry("onbeforeunload", "OnBeforeUnload"),
        Map.entry("oncanplay", "OnCanPlay"),
        Map.entry("oncanplaythrough", "OnCanPlayThrough"),
        Map.entry("oncuechange", "OnCueChange"),
        Map.entry("ondurationchange", "OnDurationChange"),
        Map.entry("onemptied", "OnEmptied"),
        Map.entry("onended", "OnEnded"),
        Map.entry("onerror", "OnError"),
        Map.entry("onhashchange", "OnHashChange"),
        Map.entry("onload", "OnLoad"),
        Map.entry("onloadeddata", "OnLoadedData"),
        Map.entry("onloadedmetadata", "OnLoadedMetadata"),
        Map.entry("onloadstart", "OnLoadStart"),
        Map.entry("onoffline", "OnOffline"),
        Map.entry("ononline", "OnOnline"),
        Map.entry("onpagehide", "OnPageHide"),
        Map.entry("onpageshow", "OnPageShow"),
        Map.entry("onpause", "OnPause"),
        Map.entry("onplay", "OnPlay"),
        Map.entry("onplaying", "OnPlaying"),
        Map.entry("onpopstate", "OnPopState"),
        Map.entry("onprogress", "OnProgress"),
        Map.entry("onratechange", "OnRateChange"),
        Map.entry("onreset", "OnReset"),
        Map.entry("onresize", "OnResize"),
        Map.entry("onsearch", "OnSearch"),
        Map.entry("onseeked", "OnSeeked"),
        Map.entry("onseeking", "OnSeeking"),
        Map.entry("onstalled", "OnStalled"),
        Map.entry("onstorage", "OnStorage"),
        Map.entry("onsubmit", "OnSubmit"),
        Map.entry("onsuspend", "OnSuspend"),
        Map.entry("ontimeupdate", "OnTimeUpdate"),
        Map.entry("ontoggle", "OnToggle"),
        Map.entry("onunload", "OnUnload"),
        Map.entry("onvolumechanged", "OnVolumeChanged"),
        Map.entry("onwaiting", "OnWaiting"),

        // MathML Attributes
        Map.entry("displaystyle", "DisplayStyle"),
        Map.entry("mathbackground", "MathBackground"),
        Map.entry("mathcolor", "MathColor"),
        Map.entry("mathsize", "MathSize"),
        Map.entry("mathvariant", "MathVariant"),
        Map.entry("scriptlevel", "ScriptLevel")
    );

    /** Attributes that use the "has" prefix instead of "is". */
    private static final Set<String> HAS_ATTRIBUTES = Set.of(
        "autocomplete",
        "autofocus",
        "autoplay",
        "controls",
        "displaystyle",
        "download",
        "ismap",
        "loop",
        "mathbackground",
        "mathcolor",
        "mathsize",
        "mathvariant",
        "nonce",
        "novalidate",
        "sandbox",
        "scriptlevel"
    );

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
            ? ClassName.get(TAGS_PACKAGE, "EmptyTag") : ClassName.get(TAGS_PACKAGE, "ContainerTag"), aClassName))
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
        final ClassName name = ClassName.get(aPkg, attributePascalCase(anAttribute.myName));
        return TypeSpec.interfaceBuilder(name)
          .addJavadoc("Defines the {@code $L} attribute for HTML/XML tags.\n\n@param <T> The tag type\n",
            anAttribute.myName)
          .addSuperinterface(
            ParameterizedTypeName.get(ClassName.get(TAGS_PACKAGE, "Instance"), TypeVariableName.get("T")))
          .addTypeVariable(TypeVariableName.get("T", ParameterizedTypeName.get(
            ClassName.get(TAGS_PACKAGE, "AbstractTag"), TypeVariableName.get("T"))))
          .addModifiers(Modifier.PUBLIC);
    }

    /**
     * Determines the method name for a boolean or on/off attribute.
     *
     * @param anAttrName The attribute name
     * @return The method name
     */
    private static String booleanMethodName(final String anAttrName) {
        if (DEFER.equals(anAttrName)) {
            return "isDeferred";
        }
        if (HAS_ATTRIBUTES.contains(anAttrName)) {
            return methodName("has", anAttrName);
        }
        return methodName("is", anAttrName);
    }

    /**
     * Defines methods for a boolean attribute.
     *
     * @param anAttribute The attribute model node
     * @param aType The interface builder
     */
    private static void defineBooleanAttributeMethods(final Node anAttribute, final TypeSpec.Builder aType) {
        final MethodSpec with = MethodSpec.methodBuilder(booleanMethodName(anAttribute.myName))
          .addJavadoc("Sets the {@code $L} attribute.\n\n@return The tag instance\n", anAttribute.myName)
          .addModifiers(Modifier.PUBLIC, Modifier.DEFAULT)
          .addStatement("return self().withAttr(\"" + anAttribute.myName + "\")")
          .returns(TypeVariableName.get("T"))
          .build();

        final MethodSpec withCond = MethodSpec.methodBuilder(methodName("withCond", anAttribute.myName))
          .addJavadoc("Conditionally sets the {@code $L} attribute.\n\n"
            + "@param aEnable Whether to set the attribute\n@return The tag instance\n", anAttribute.myName)
          .addModifiers(Modifier.PUBLIC, Modifier.DEFAULT)
          .addParameter(TypeName.BOOLEAN, A_ENABLE, Modifier.FINAL)
          .addStatement("return aEnable ? self().withAttr(\"" + anAttribute.myName + "\") : self()")
          .returns(TypeVariableName.get("T"))
          .build();

        aType.addMethod(with);
        aType.addMethod(withCond);

        if (DEFER.equals(anAttribute.myName)) {
            final MethodSpec withCondDeferred = MethodSpec.methodBuilder("withCondDeferred")
              .addJavadoc("Conditionally sets the {@code $L} attribute.\n\n"
                + "@param aEnable Whether to set the attribute\n@return The tag instance\n", anAttribute.myName)
              .addModifiers(Modifier.PUBLIC, Modifier.DEFAULT)
              .addParameter(TypeName.BOOLEAN, A_ENABLE, Modifier.FINAL)
              .addStatement("return withCondDefer(aEnable)")
              .returns(TypeVariableName.get("T"))
              .build();
            aType.addMethod(withCondDeferred);
        }
    }

    /**
     * Defines methods for an on/off boolean attribute.
     *
     * @param anAttribute The attribute model node
     * @param aType The interface builder
     */
    private static void defineOnOffAttributeMethods(final Node anAttribute, final TypeSpec.Builder aType) {
        final MethodSpec with = MethodSpec.methodBuilder(booleanMethodName(anAttribute.myName))
          .addJavadoc("Sets the {@code $L} attribute to \"on\".\n\n@return The tag instance\n", anAttribute.myName)
          .addModifiers(Modifier.PUBLIC, Modifier.DEFAULT)
          .addStatement("return self().withAttr(\"" + anAttribute.myName + "\", \"on\")")
          .returns(TypeVariableName.get("T"))
          .build();

        final MethodSpec withCond = MethodSpec.methodBuilder(methodName("withCond", anAttribute.myName))
          .addJavadoc("Conditionally sets the {@code $L} attribute to \"on\".\n\n"
            + "@param aEnable Whether to set the attribute\n@return The tag instance\n", anAttribute.myName)
          .addModifiers(Modifier.PUBLIC, Modifier.DEFAULT)
          .addParameter(TypeName.BOOLEAN, A_ENABLE, Modifier.FINAL)
          .addStatement("return aEnable ? self().withAttr(\"" + anAttribute.myName + "\", \"on\") : self()")
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
          .addStatement("return self().withAttr(\"" + anAttribute.myName + "\", " + parameter(anAttribute) + ")")
          .returns(TypeVariableName.get("T"))
          .build();

        final MethodSpec withCond = MethodSpec.methodBuilder(methodName("withCond", anAttribute.myName))
          .addJavadoc("Conditionally sets the {@code $L} attribute with the specified value.\n\n"
            + "@param aEnable Whether to set the attribute\n@param $L The attribute value\n@return The tag instance\n",
            anAttribute.myName, parameter(anAttribute))
          .addModifiers(Modifier.PUBLIC, Modifier.DEFAULT)
          .addParameter(TypeName.BOOLEAN, A_ENABLE, Modifier.FINAL)
          .addParameter(String.class, parameter(anAttribute), Modifier.FINAL)
          .addStatement("return aEnable ? self().withAttr(\"" + anAttribute.myName + "\", " + parameter(anAttribute) +
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
            camelCase[i] = attributePascalCase(aWords[i]);
        }
        return String.join("", camelCase);
    }

    /**
     * Converts an attribute name into its PascalCase form.
     *
     * @param anAttrName The attribute name
     * @return The PascalCase attribute name
     */
    public static String attributePascalCase(final String anAttrName) {
        final String mapped = CAMEL_CASE_NAMES.get(anAttrName);
        return mapped != null ? mapped : capitalize(anAttrName);
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
