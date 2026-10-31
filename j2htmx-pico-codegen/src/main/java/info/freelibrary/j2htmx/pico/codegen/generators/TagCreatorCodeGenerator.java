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

import java.util.Arrays;
import java.util.List;

import info.freelibrary.util.warnings.Checkstyle;
import info.freelibrary.util.warnings.PMD;

/**
 * Generator that creates method definitions for empty and container tags in TagCreator.
 */
@SuppressWarnings({PMD.SYSTEM_PRINTLN, Checkstyle.MULTIPLE_STRING_LITERALS, "MultipleStringLiterals"})
public final class TagCreatorCodeGenerator {

    /** The formatting for a single line of code. */
    private static final String LINE_FORMAT = "%-120s%s%n";

    /** Java code that adds to an attribute. */
    private static final String ATTR_ADD_TO_METHOD = " Attr.addTo(";

    /**
     * Constructs an instance of TagCreatorCodeGenerator.
     */
    private TagCreatorCodeGenerator() {
        // This has intentionally left empty
    }

    /**
     * Prints Java method definitions for all empty and container tags to standard output.
     */
    public static void print() {

        System.out.println("// EmptyTags, generated in " + TagCreatorCodeGenerator.class);

        for (final String tag : emptyTags()) {
            final String className = SpecializedTagClassCodeGenerator.classNameFromTag(tag);
            final String publicStaticTypeMethod = "public static " + className + " " + tag + " ";
            final String castReturn = " return (" + className + ") ";
            final String construct = " new " + className + "()";

            final String emptyA1 = publicStaticTypeMethod + "()";
            final String emptyA2 = "{ return " + construct + "; }";
            // Attr shorthands
            final String emptyB1 = publicStaticTypeMethod + "(Attr.ShortForm aShortAttr)";
            final String emptyB2 = "{ " + castReturn + ATTR_ADD_TO_METHOD + construct + ", aShortAttr); }";

            System.out.printf(LINE_FORMAT, emptyA1, emptyA2);
            System.out.printf(LINE_FORMAT, emptyB1, emptyB2);
            System.out.println();
        }

        System.out.println("// ContainerTags, generated in " + TagCreatorCodeGenerator.class);

        for (final String tag : containerTags()) {
            final String className = SpecializedTagClassCodeGenerator.classNameFromTag(tag);
            final String publicstaticTypeMethod = "public static " + className + " " + tag + " ";
            final String castReturn = " return (" + className + ") ";
            final String construct = " new " + className + "()";

            final String containerA1 = publicstaticTypeMethod + "()";
            final String containerA2 = "{ " + castReturn + construct + "; }";

            final String containerB1 = publicstaticTypeMethod + "(String aText)";
            final String containerB2 = "{ " + castReturn + construct + ".withText(aText); }";

            final String containerC1 = publicstaticTypeMethod + "(AbstractDomContent... aDc)";
            final String containerC2 = "{ " + castReturn + construct + ".with(aDc); }";
            // Attr shorthands
            final String containerD1 = publicstaticTypeMethod + "(Attr.ShortForm aShortAttr)";
            final String containerD2 = "{ " + castReturn + ATTR_ADD_TO_METHOD + construct + ", aShortAttr); }";

            final String containerE1 = publicstaticTypeMethod + "(Attr.ShortForm aShortAttr, String aText)";
            final String containerE2 =
              "{ " + castReturn + ATTR_ADD_TO_METHOD + construct + ".withText(aText), aShortAttr); }";

            final String containerF1 = publicstaticTypeMethod
              + "(Attr.ShortForm aShortAttr, AbstractDomContent... aDc)";
            final String containerF2 =
              "{ " + castReturn + ATTR_ADD_TO_METHOD + construct + ".with(aDc), aShortAttr); }";

            System.out.printf(LINE_FORMAT, containerA1, containerA2);
            System.out.printf(LINE_FORMAT, containerB1, containerB2);
            System.out.printf(LINE_FORMAT, containerC1, containerC2);
            System.out.printf(LINE_FORMAT, containerD1, containerD2);
            System.out.printf(LINE_FORMAT, containerE1, containerE2);
            System.out.printf(LINE_FORMAT, containerF1, containerF2);
            System.out.println();
        }
    }

    /**
     * Returns a list of all HTML empty/void tag names.
     *
     * @return List of empty tag names
     */
    public static List<String> emptyTags() {
        return Arrays.asList("area", "base", "br", "col", "embed", "hr", "img", "input", "keygen", "link", "meta",
          "param", "source", "track", "wbr");
    }

    /**
     * Returns a list of all HTML container tag names.
     *
     * @return List of container tag names
     */
    public static List<String> containerTags() {
        return Arrays.asList("a", "abbr", "address", "article", "aside", "audio", "b", "bdi", "bdo", "blockquote",
          "body", "button", "canvas", "caption", "cite", "code", "colgroup", "data", "datalist", "dd", "del", "details",
          "dfn", "dialog", "div", "dl", "dt", "em", "fieldset", "figcaption", "figure", "footer", "form", "h1", "h2",
          "h3", "h4", "h5", "h6", "head", "header", "html", "i", "iframe", "ins", "kbd", "label", "legend", "li",
          "main", "map", "mark", "menu", "menuitem", "meter", "nav", "noscript", "object", "ol", "optgroup", "option",
          "output", "p", "picture", "pre", "progress", "q", "rp", "rt", "ruby", "s", "samp", "script", "section",
          "select", "slot", "small", "span", "strong", "style", "sub", "summary", "sup", "table", "tbody", "td",
          "template", "textarea", "tfoot", "th", "thead", "time", "title", "tr", "u", "ul", "var", "video");
    }
}
