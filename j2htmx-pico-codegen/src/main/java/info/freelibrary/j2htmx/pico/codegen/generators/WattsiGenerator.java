package info.freelibrary.j2htmx.pico.codegen.generators;

import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.MethodSpec;
import com.palantir.javapoet.TypeSpec;
import info.freelibrary.j2htmx.pico.codegen.wattsi.AttributeDefinition;
import info.freelibrary.j2htmx.pico.codegen.wattsi.ElementDefinition;
import info.freelibrary.j2htmx.pico.codegen.wattsi.WattsiSource;
import info.freelibrary.util.warnings.Checkstyle;
import info.freelibrary.util.warnings.PMD;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Locale;

/**
 * Generator that creates tag and attribute code by parsing WHATWG HTML spec sources.
 */
@SuppressWarnings(PMD.SYSTEM_PRINTLN)
public final class WattsiGenerator {

    /**
     * Private constructor to prevent instantiation.
     */
    private WattsiGenerator() {
        // This has intentionally left empty
    }

    /**
     * Main entry point to parse WHATWG spec and generate tag definitions.
     *
     * @param aArgs Command-line arguments with path to WHATWG source
     * @throws IOException If an I/O error occurs
     */
    @SuppressWarnings({Checkstyle.UNCOMMENTED_MAIN, "UncommentedMain"})
    public static void main(final String... aArgs) throws IOException {
        final Path source = Paths.get(aArgs[0]);
        final Document doc = Jsoup.parse(source.toFile(), "UTF-8", "https://html.spec.whatwg.org/");
        final WattsiSource wattsi = new WattsiSource(doc);

        final List<ElementDefinition> elements = wattsi.elementDefinitions();
        final List<AttributeDefinition> attributes = wattsi.attributeDefinitions();

        //        for (ElementDefinition element : elements) {
        //            System.out.println((element.isObsolete() ? "!" : "") + element.name());
        //            for (AttributeDefinition attribute : attributes) {
        //                if (attribute.appliesTo(element)) {
        //                    System.out.println("  " + (attribute.isObsolete() ? "!" : "") + attribute.name());
        //                }
        //            }
        //            System.out.println();
        //        }

        for (final ElementDefinition element : elements) {
            if (element.isObsolete()) {
                continue;
            }

            final ClassName className = ClassName.get(
              "info.freelibrary.j2htmx.pico.", capitalize(element.name()) + "Tag"
            );

            final TypeSpec.Builder type = TypeSpec.classBuilder(className)
              .addModifiers(Modifier.PUBLIC);

            for (final AttributeDefinition attribute : attributes) {
                if (!attribute.isObsolete() && attribute.appliesTo(element)) {
                    final String name = methodName("with", attribute.name().split("-"));
                    final MethodSpec.Builder setter = MethodSpec.methodBuilder(name)
                      .addModifiers(Modifier.PUBLIC)
                      .returns(className)
                      .addStatement("return this");

                    type.addMethod(setter.build());
                }
            }

            System.out.println(type.build().toString());
        }
    }

    /**
     * Constructs a camelCase method name starting with the given prefix.
     *
     * @param aPrefix The method prefix
     * @param aWords The word parts
     * @return The camelCase method name
     */
    private static String methodName(final String aPrefix, final String... aWords) {
        final String[] tmpArray = new String[aWords.length + 1];

        tmpArray[0] = aPrefix;
        System.arraycopy(aWords, 0, tmpArray, 1, aWords.length);

        return methodName(tmpArray);
    }

    /**
     * Constructs a camelCase method name from an array of words.
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
     * Capitalizes the first letter of the given word.
     *
     * @param aWord The word to capitalize
     * @return The capitalized word
     */
    private static String capitalize(final String aWord) {
        return aWord.substring(0, 1).toUpperCase(Locale.US) + aWord.substring(1).toLowerCase(Locale.US);
    }
}
