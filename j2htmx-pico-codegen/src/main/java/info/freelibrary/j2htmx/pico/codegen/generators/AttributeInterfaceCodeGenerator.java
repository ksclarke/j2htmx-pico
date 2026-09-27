package info.freelibrary.j2htmx.pico.codegen.generators;

import info.freelibrary.j2htmx.pico.codegen.GeneratorUtil;
import info.freelibrary.j2htmx.pico.codegen.model.AttrD;
import info.freelibrary.j2htmx.pico.codegen.model.AttributesList;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Code generator that outputs Java attribute interfaces for HTML elements.
 */
public final class AttributeInterfaceCodeGenerator {

    private static final String relPath = "tags/attributes/";

    private AttributeInterfaceCodeGenerator() {
    }

    /**
     * Generates or deletes attribute interface source files in the specified path.
     *
     * @param absPath The absolute path to output files
     * @param delete Whether to delete files instead of writing them
     * @throws IOException If an I/O error occurs
     */
    public static void generate(final Path absPath, final boolean delete) throws IOException {

        //delete all files in the directory for fresh generation
        final Path dir = Paths.get(absPath.toString(),relPath);
        GeneratorUtil.deleteAllFilesInDir(dir);

        for (final AttrD attr : AttributesList.attributesDescriptive()) {
            final Path path = makePath(attr.attr, absPath);
            final String interfaceName = interfaceNameFromAttribute(attr.attr)+"<T extends Tag<T>>";
            /*
            FormAction<T extends Tag<T>> extends Instance<T>

            default T withFormAction(String formAction){
                return self().attr("formaction", formAction);
            }
             */
            final String interfaceStr = getInterfaceTemplate(
                interfaceName,
                Optional.of("Instance<T>"),
                Arrays.asList("info.freelibrary.j2htmx.pico.tags.Tag","info.freelibrary.j2htmx.pico.tags.Instance"),
                interfaceNameFromAttribute(attr.attr),
                attr
            );

            if (!delete) {
                System.out.println("writing to "+path);
                Files.write(path, interfaceStr.getBytes());
            }
        }

    }

    private static String getPackage(){
        return "package info.freelibrary.j2htmx.pico.tags.attributes;\n";
    }

    private static String makeReturnTypeAndMethodName(final String name){
        return "default "+ "T "+name;
    }

    private static String getInterfaceTemplate(
        final String interfaceName,
        final Optional<String> optExtends,
        final List<String> imports,
        final String interfaceNameSimple,
        final AttrD attrD
    ){

        final StringBuilder sb = new StringBuilder();

        sb.append(getPackage());
        sb.append("\n");

        for(final String importName : imports){
            sb.append("import ").append(importName).append(";\n");
        }
        sb.append("\n");
        sb.append("public interface ")
            .append(interfaceName);

        optExtends.ifPresent(ext -> sb.append(" extends ").append(ext).append(" "));

        sb.append(" {\n");

        //interface contents
        /*
        FormAction<T extends Tag> extends Instance<T>

        default T withFormAction(String formAction){
            return self().attr("formaction", formAction);
        }
         */
        //IMPORTANT: '_' added as suffix to mitigate problems
        //where attributes are java keywords. Just to make it consistent and avoid special cases.
        final String attrName = interfaceNameSimple.toLowerCase();
        final String paramName = attrName+"_";

        //depending on if the attribute has an argument or not,
        //generate methods according to the convention in Tag.java
        // arg -> with$ATTR(arg), withCond$ATTR(condition, arg)
        // no arg -> is$ATTR(), withCond$ATTR(condition)

        //append the 'with$ATTR' method
        writeAttributeMethod(interfaceNameSimple, attrD, sb, attrName, paramName);
        writeAttributeMethodCond(interfaceNameSimple, attrD, sb, attrName, paramName);

        sb.append("}\n");

        return sb.toString();
    }

    private static void addAttributeNoArg(final StringBuilder sb, final String attrName){
        //generate the code to add an attribute without an argument

        //there are some special attributes
        //which do take an argument, but where the argument
        //is boolean (meaning on/off, yes/no and the like)
        sb.append("self().attr(\"");
        if (attrName.equals("autocomplete")){
            sb.append(attrName).append("\",\"on\"");
        } else {
            sb.append(attrName).append("\"");
        }
        sb.append(");\n");
    }

    private static void writeAttributeMethodCond(final String interfaceNameSimple, final AttrD attrD, final StringBuilder sb, final String attrName, final String paramName) {

        sb.append(makeReturnTypeAndMethodName("withCond"+interfaceNameSimple));

        if(attrD.hasArgument){
            //add a variant where you can specify the argument

            sb.append("(final boolean enable, final String ").append(paramName).append(") {");

                sb.append("if (enable){\n");
                    sb.append("self().attr(\"").append(attrName).append("\", ").append(paramName).append(");\n");
                sb.append("}\n");

                sb.append("return self();\n");
        }else{
            //add a variant where you can toggle the attribute

            sb.append("(final boolean enable) {");
                sb.append("if (enable){\n");
                    addAttributeNoArg(sb, attrName);
                sb.append("}\n");
                sb.append("return self();\n");
        }
        sb.append("}\n");
    }

    private static void writeAttributeMethod(final String interfaceNameSimple, final AttrD attrD, final StringBuilder sb, final String attrName, final String paramName) {

        sb.append(makeReturnTypeAndMethodName(
            ((attrD.hasArgument)?"with":"is")+interfaceNameSimple)
        );

        if(attrD.hasArgument){
            //add a variant where you can specify the argument

            sb.append("(final String ").append(paramName).append(") {")

                .append("return self().attr(\"").append(attrName).append("\", ").append(paramName).append(");\n");
        }else{
            //add a variant where you can toggle the attribute

            sb.append("() {");

                addAttributeNoArg(sb, attrName);

                sb.append("return self();\n");
        }
        sb.append("}\n");
    }

    /**
     * Converts an attribute name into its corresponding interface name.
     *
     * @param attribute The attribute name
     * @return The interface name with first letter capitalized
     */
    public static String interfaceNameFromAttribute(final String attribute){
        return attribute.substring(0,1).toUpperCase()+attribute.substring(1);
    }

    private static Path makePath(final String tagLowerCase, final Path absPath){
        final String filename = interfaceNameFromAttribute(tagLowerCase)+".java";
        return Paths.get(absPath.toString(),relPath,filename);
    }

}
