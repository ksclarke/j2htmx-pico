package info.freelibrary.j2htmx.pico.codegen.generators;

import java.util.Arrays;
import java.util.List;

import info.freelibrary.j2htmx.pico.tags.Tag;
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
        return Arrays.asList(Tag.AREA, Tag.BASE, Tag.BR, Tag.COL, Tag.EMBED, Tag.HR, Tag.IMG, Tag.INPUT, Tag.KEYGEN,
          Tag.LINK, Tag.META, Tag.PARAM, Tag.SOURCE, Tag.TRACK, Tag.WBR);
    }

    /**
     * Returns a list of all HTML container tag names.
     *
     * @return List of container tag names
     */
    public static List<String> containerTags() {
        return Arrays.asList(Tag.A, Tag.ABBR, Tag.ADDRESS, Tag.ARTICLE, Tag.ASIDE, Tag.AUDIO, Tag.B, Tag.BDI, Tag.BDO,
          Tag.BLOCKQUOTE, Tag.BODY, Tag.BUTTON, Tag.CANVAS, Tag.CAPTION, Tag.CITE, Tag.CODE, Tag.COLGROUP, Tag.DATA,
          Tag.DATALIST, Tag.DD, Tag.DEL, Tag.DETAILS, Tag.DFN, Tag.DIALOG, Tag.DIV, Tag.DL, Tag.DT, Tag.EM,
          Tag.FIELDSET, Tag.FIGCAPTION, Tag.FIGURE, Tag.FOOTER, Tag.FORM, Tag.H1, Tag.H2, Tag.H3, Tag.H4, Tag.H5,
          Tag.H6, Tag.HEAD, Tag.HEADER, Tag.HTML, Tag.I, Tag.IFRAME, Tag.INS, Tag.KBD, Tag.LABEL, Tag.LEGEND, Tag.LI,
          Tag.MAIN, Tag.MAP, Tag.MARK, Tag.MENU, Tag.MENUITEM, Tag.METER, Tag.NAV, Tag.NOSCRIPT, Tag.OBJECT, Tag.OL,
          Tag.OPTGROUP, Tag.OPTION, Tag.OUTPUT, Tag.P, Tag.PICTURE, Tag.PRE, Tag.PROGRESS, Tag.Q, Tag.RP, Tag.RT,
          Tag.RUBY, Tag.S, Tag.SAMP, Tag.SCRIPT, Tag.SECTION, Tag.SELECT, Tag.SLOT, Tag.SMALL, Tag.SPAN, Tag.STRONG,
          Tag.STYLE, Tag.SUB, Tag.SUMMARY, Tag.SUP, Tag.TABLE, Tag.TBODY, Tag.TD, Tag.TEMPLATE, Tag.TEXTAREA,
          Tag.TFOOT, Tag.TH, Tag.THEAD, Tag.TIME, Tag.TITLE, Tag.TR, Tag.U, Tag.UL, Tag.VAR, Tag.VIDEO);
    }
}
