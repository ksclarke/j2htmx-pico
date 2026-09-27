package info.freelibrary.j2htmx.pico.codegen.generators;

import info.freelibrary.j2htmx.pico.codegen.wattsi.AttributeDefinition;
import info.freelibrary.j2htmx.pico.codegen.wattsi.ElementDefinition;
import info.freelibrary.j2htmx.pico.codegen.wattsi.WattsiSource;
import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.MethodSpec;
import com.palantir.javapoet.TypeSpec;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Generator that creates tag and attribute code by parsing WHATWG HTML spec sources.
 */
public class WattsiGenerator {

    private WattsiGenerator() {
    }

    /**
     * Main entry point to parse WHATWG spec and generate tag definitions.
     *
     * @param args Command-line arguments with path to WHATWG source
     * @throws IOException If an I/O error occurs
     */
    public static void main(final String... args) throws IOException {
        final Path source = Paths.get(args[0]);
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
            final ClassName className = ClassName.get(
                "info.freelibrary.j2htmx.pico.",
                capitalize(element.name()) + "Tag"
            );

            final TypeSpec.Builder type = TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC);

            if (element.isObsolete()) {
                type.addAnnotation(Deprecated.class);
            }

            for (final AttributeDefinition attribute : attributes) {
                if (attribute.appliesTo(element)) {
                    final String name = methodName("with", attribute.name().split("-"));
                    final MethodSpec.Builder setter = MethodSpec.methodBuilder(name)
                        .addModifiers(Modifier.PUBLIC)
                        .returns(className)
                        .addStatement("return this");

                    if(attribute.isObsolete()){
                        setter.addAnnotation(Deprecated.class);
                    }

                    type.addMethod(setter.build());
                }
            }

            System.out.println(type.build().toString());
        }

//        System.out.println(doc.select("dfn"));
    }

    private static String methodName(final String prefix, final String... words){
        final String[] tmp = new String[words.length + 1];
        tmp[0] = prefix;
        for(int i = 0; i < words.length; i++){
            tmp[i+1] = words[i];
        }
        return methodName(tmp);
    }

    private static String methodName(final String... words){
        final String[] camelCase = new String[words.length];
        camelCase[0] = words[0];
        for(int i = 1; i < words.length; i++){
            camelCase[i] = capitalize(words[i]);
        }
        return String.join("", camelCase);
    }

    private static String capitalize(final String word){
        return word.substring(0,1).toUpperCase() + word.substring(1).toLowerCase();
    }
}
