package info.freelibrary.j2htmx.pico.mathml;

import info.freelibrary.j2htmx.pico.mathml.tags.AnnotationTag;
import info.freelibrary.j2htmx.pico.mathml.tags.MactionTag;
import info.freelibrary.j2htmx.pico.mathml.tags.MathTag;
import info.freelibrary.j2htmx.pico.mathml.tags.MerrorTag;
import info.freelibrary.j2htmx.pico.mathml.tags.MfracTag;
import info.freelibrary.j2htmx.pico.mathml.tags.MiTag;
import info.freelibrary.j2htmx.pico.mathml.tags.MmultiscriptsTag;
import info.freelibrary.j2htmx.pico.mathml.tags.MnTag;
import info.freelibrary.j2htmx.pico.mathml.tags.MoTag;
import info.freelibrary.j2htmx.pico.mathml.tags.MoverTag;
import info.freelibrary.j2htmx.pico.mathml.tags.MpaddedTag;
import info.freelibrary.j2htmx.pico.mathml.tags.MphantomTag;
import info.freelibrary.j2htmx.pico.mathml.tags.MprescriptsTag;
import info.freelibrary.j2htmx.pico.mathml.tags.MrootTag;
import info.freelibrary.j2htmx.pico.mathml.tags.MrowTag;
import info.freelibrary.j2htmx.pico.mathml.tags.MsTag;
import info.freelibrary.j2htmx.pico.mathml.tags.MspaceTag;
import info.freelibrary.j2htmx.pico.mathml.tags.MsqrtTag;
import info.freelibrary.j2htmx.pico.mathml.tags.MstyleTag;
import info.freelibrary.j2htmx.pico.mathml.tags.MsubTag;
import info.freelibrary.j2htmx.pico.mathml.tags.MsubsupTag;
import info.freelibrary.j2htmx.pico.mathml.tags.MsupTag;
import info.freelibrary.j2htmx.pico.mathml.tags.MtableTag;
import info.freelibrary.j2htmx.pico.mathml.tags.MtdTag;
import info.freelibrary.j2htmx.pico.mathml.tags.MtextTag;
import info.freelibrary.j2htmx.pico.mathml.tags.MtrTag;
import info.freelibrary.j2htmx.pico.mathml.tags.MunderTag;
import info.freelibrary.j2htmx.pico.mathml.tags.MunderoverTag;
import info.freelibrary.j2htmx.pico.mathml.tags.NoneTag;
import info.freelibrary.j2htmx.pico.mathml.tags.SemanticsTag;
import info.freelibrary.j2htmx.pico.tags.AbstractDomContent;
import info.freelibrary.util.warnings.PMD;

/**
 * Factory class providing static builder methods for creating MathML elements.
 */
@SuppressWarnings(
  {PMD.SHORT_METHOD_NAME, PMD.TOO_MANY_METHODS, PMD.EXCESSIVE_PUBLIC_COUNT, PMD.COUPLING_BETWEEN_OBJECTS,
    PMD.EXCESSIVE_IMPORTS})
public final class MathML {

    /**
     * Private constructor to prevent instantiation of utility class.
     */
    private MathML() {
    }

    /**
     * Creates a {@code annotation} MathML element with the specified text content.
     *
     * @param text The text content
     * @return The created {@link AnnotationTag} instance
     */
    public static AnnotationTag annotation(final String text) {
        return new AnnotationTag().withText(text);
    }

    /**
     * Creates a {@code annotation} MathML element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return The created {@link AnnotationTag} instance
     */
    public static AnnotationTag annotation(final AbstractDomContent... dc) {
        return new AnnotationTag().with(dc);
    }

    /**
     * Creates a {@code maction} MathML element with the specified text content.
     *
     * @param text The text content
     * @return The created {@link MactionTag} instance
     */
    public static MactionTag maction(final String text) {
        return new MactionTag().withText(text);
    }

    /**
     * Creates a {@code maction} MathML element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return The created {@link MactionTag} instance
     */
    public static MactionTag maction(final AbstractDomContent... dc) {
        return new MactionTag().with(dc);
    }

    /**
     * Creates a {@code math} MathML element with the specified text content.
     *
     * @param text The text content
     * @return The created {@link MathTag} instance
     */
    public static MathTag math(final String text) {
        return new MathTag().withText(text);
    }

    /**
     * Creates a {@code math} MathML element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return The created {@link MathTag} instance
     */
    public static MathTag math(final AbstractDomContent... dc) {
        return new MathTag().with(dc);
    }

    /**
     * Creates a {@code merror} MathML element with the specified text content.
     *
     * @param text The text content
     * @return The created {@link MerrorTag} instance
     */
    public static MerrorTag merror(final String text) {
        return new MerrorTag().withText(text);
    }

    /**
     * Creates a {@code merror} MathML element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return The created {@link MerrorTag} instance
     */
    public static MerrorTag merror(final AbstractDomContent... dc) {
        return new MerrorTag().with(dc);
    }

    /**
     * Creates a {@code mfrac} MathML element with the specified text content.
     *
     * @param text The text content
     * @return The created {@link MfracTag} instance
     */
    public static MfracTag mfrac(final String text) {
        return new MfracTag().withText(text);
    }

    /**
     * Creates a {@code mfrac} MathML element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return The created {@link MfracTag} instance
     */
    public static MfracTag mfrac(final AbstractDomContent... dc) {
        return new MfracTag().with(dc);
    }

    /**
     * Creates a {@code mi} MathML element with the specified text content.
     *
     * @param text The text content
     * @return The created {@link MiTag} instance
     */
    public static MiTag mi(final String text) {
        return new MiTag().withText(text);
    }

    /**
     * Creates a {@code mi} MathML element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return The created {@link MiTag} instance
     */
    public static MiTag mi(final AbstractDomContent... dc) {
        return new MiTag().with(dc);
    }

    /**
     * Creates a {@code mmultiscripts} MathML element with the specified text content.
     *
     * @param text The text content
     * @return The created {@link MmultiscriptsTag} instance
     */
    public static MmultiscriptsTag mmultiscripts(final String text) {
        return new MmultiscriptsTag().withText(text);
    }

    /**
     * Creates a {@code mmultiscripts} MathML element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return The created {@link MmultiscriptsTag} instance
     */
    public static MmultiscriptsTag mmultiscripts(final AbstractDomContent... dc) {
        return new MmultiscriptsTag().with(dc);
    }

    /**
     * Creates a {@code mn} MathML element with the specified text content.
     *
     * @param text The text content
     * @return The created {@link MnTag} instance
     */
    public static MnTag mn(final String text) {
        return new MnTag().withText(text);
    }

    /**
     * Creates a {@code mn} MathML element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return The created {@link MnTag} instance
     */
    public static MnTag mn(final AbstractDomContent... dc) {
        return new MnTag().with(dc);
    }

    /**
     * Creates a {@code mo} MathML element with the specified text content.
     *
     * @param text The text content
     * @return The created {@link MoTag} instance
     */
    public static MoTag mo(final String text) {
        return new MoTag().withText(text);
    }

    /**
     * Creates a {@code mo} MathML element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return The created {@link MoTag} instance
     */
    public static MoTag mo(final AbstractDomContent... dc) {
        return new MoTag().with(dc);
    }

    /**
     * Creates a {@code mover} MathML element with the specified text content.
     *
     * @param text The text content
     * @return The created {@link MoverTag} instance
     */
    public static MoverTag mover(final String text) {
        return new MoverTag().withText(text);
    }

    /**
     * Creates a {@code mover} MathML element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return The created {@link MoverTag} instance
     */
    public static MoverTag mover(final AbstractDomContent... dc) {
        return new MoverTag().with(dc);
    }

    /**
     * Creates a {@code mpadded} MathML element with the specified text content.
     *
     * @param text The text content
     * @return The created {@link MpaddedTag} instance
     */
    public static MpaddedTag mpadded(final String text) {
        return new MpaddedTag().withText(text);
    }

    /**
     * Creates a {@code mpadded} MathML element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return The created {@link MpaddedTag} instance
     */
    public static MpaddedTag mpadded(final AbstractDomContent... dc) {
        return new MpaddedTag().with(dc);
    }

    /**
     * Creates a {@code mphantom} MathML element with the specified text content.
     *
     * @param text The text content
     * @return The created {@link MphantomTag} instance
     */
    public static MphantomTag mphantom(final String text) {
        return new MphantomTag().withText(text);
    }

    /**
     * Creates a {@code mphantom} MathML element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return The created {@link MphantomTag} instance
     */
    public static MphantomTag mphantom(final AbstractDomContent... dc) {
        return new MphantomTag().with(dc);
    }

    /**
     * Creates a {@code mprescripts} MathML element with the specified text content.
     *
     * @param text The text content
     * @return The created {@link MprescriptsTag} instance
     */
    public static MprescriptsTag mprescripts(final String text) {
        return new MprescriptsTag().withText(text);
    }

    /**
     * Creates a {@code mprescripts} MathML element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return The created {@link MprescriptsTag} instance
     */
    public static MprescriptsTag mprescripts(final AbstractDomContent... dc) {
        return new MprescriptsTag().with(dc);
    }

    /**
     * Creates a {@code mroot} MathML element with the specified text content.
     *
     * @param text The text content
     * @return The created {@link MrootTag} instance
     */
    public static MrootTag mroot(final String text) {
        return new MrootTag().withText(text);
    }

    /**
     * Creates a {@code mroot} MathML element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return The created {@link MrootTag} instance
     */
    public static MrootTag mroot(final AbstractDomContent... dc) {
        return new MrootTag().with(dc);
    }

    /**
     * Creates a {@code mrow} MathML element with the specified text content.
     *
     * @param text The text content
     * @return The created {@link MrowTag} instance
     */
    public static MrowTag mrow(final String text) {
        return new MrowTag().withText(text);
    }

    /**
     * Creates a {@code mrow} MathML element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return The created {@link MrowTag} instance
     */
    public static MrowTag mrow(final AbstractDomContent... dc) {
        return new MrowTag().with(dc);
    }

    /**
     * Creates a {@code ms} MathML element with the specified text content.
     *
     * @param text The text content
     * @return The created {@link MsTag} instance
     */
    public static MsTag ms(final String text) {
        return new MsTag().withText(text);
    }

    /**
     * Creates a {@code ms} MathML element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return The created {@link MsTag} instance
     */
    public static MsTag ms(final AbstractDomContent... dc) {
        return new MsTag().with(dc);
    }

    /**
     * Creates a {@code mspace} MathML element with the specified text content.
     *
     * @param text The text content
     * @return The created {@link MspaceTag} instance
     */
    public static MspaceTag mspace(final String text) {
        return new MspaceTag().withText(text);
    }

    /**
     * Creates a {@code mspace} MathML element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return The created {@link MspaceTag} instance
     */
    public static MspaceTag mspace(final AbstractDomContent... dc) {
        return new MspaceTag().with(dc);
    }

    /**
     * Creates a {@code msqrt} MathML element with the specified text content.
     *
     * @param text The text content
     * @return The created {@link MsqrtTag} instance
     */
    public static MsqrtTag msqrt(final String text) {
        return new MsqrtTag().withText(text);
    }

    /**
     * Creates a {@code msqrt} MathML element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return The created {@link MsqrtTag} instance
     */
    public static MsqrtTag msqrt(final AbstractDomContent... dc) {
        return new MsqrtTag().with(dc);
    }

    /**
     * Creates a {@code mstyle} MathML element with the specified text content.
     *
     * @param text The text content
     * @return The created {@link MstyleTag} instance
     */
    public static MstyleTag mstyle(final String text) {
        return new MstyleTag().withText(text);
    }

    /**
     * Creates a {@code mstyle} MathML element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return The created {@link MstyleTag} instance
     */
    public static MstyleTag mstyle(final AbstractDomContent... dc) {
        return new MstyleTag().with(dc);
    }

    /**
     * Creates a {@code msub} MathML element with the specified text content.
     *
     * @param text The text content
     * @return The created {@link MsubTag} instance
     */
    public static MsubTag msub(final String text) {
        return new MsubTag().withText(text);
    }

    /**
     * Creates a {@code msub} MathML element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return The created {@link MsubTag} instance
     */
    public static MsubTag msub(final AbstractDomContent... dc) {
        return new MsubTag().with(dc);
    }

    /**
     * Creates a {@code msubsup} MathML element with the specified text content.
     *
     * @param text The text content
     * @return The created {@link MsubsupTag} instance
     */
    public static MsubsupTag msubsup(final String text) {
        return new MsubsupTag().withText(text);
    }

    /**
     * Creates a {@code msubsup} MathML element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return The created {@link MsubsupTag} instance
     */
    public static MsubsupTag msubsup(final AbstractDomContent... dc) {
        return new MsubsupTag().with(dc);
    }

    /**
     * Creates a {@code msup} MathML element with the specified text content.
     *
     * @param text The text content
     * @return The created {@link MsupTag} instance
     */
    public static MsupTag msup(final String text) {
        return new MsupTag().withText(text);
    }

    /**
     * Creates a {@code msup} MathML element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return The created {@link MsupTag} instance
     */
    public static MsupTag msup(final AbstractDomContent... dc) {
        return new MsupTag().with(dc);
    }

    /**
     * Creates a {@code mtable} MathML element with the specified text content.
     *
     * @param text The text content
     * @return The created {@link MtableTag} instance
     */
    public static MtableTag mtable(final String text) {
        return new MtableTag().withText(text);
    }

    /**
     * Creates a {@code mtable} MathML element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return The created {@link MtableTag} instance
     */
    public static MtableTag mtable(final AbstractDomContent... dc) {
        return new MtableTag().with(dc);
    }

    /**
     * Creates a {@code mtd} MathML element with the specified text content.
     *
     * @param text The text content
     * @return The created {@link MtdTag} instance
     */
    public static MtdTag mtd(final String text) {
        return new MtdTag().withText(text);
    }

    /**
     * Creates a {@code mtd} MathML element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return The created {@link MtdTag} instance
     */
    public static MtdTag mtd(final AbstractDomContent... dc) {
        return new MtdTag().with(dc);
    }

    /**
     * Creates a {@code mtext} MathML element with the specified text content.
     *
     * @param text The text content
     * @return The created {@link MtextTag} instance
     */
    public static MtextTag mtext(final String text) {
        return new MtextTag().withText(text);
    }

    /**
     * Creates a {@code mtext} MathML element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return The created {@link MtextTag} instance
     */
    public static MtextTag mtext(final AbstractDomContent... dc) {
        return new MtextTag().with(dc);
    }

    /**
     * Creates a {@code mtr} MathML element with the specified text content.
     *
     * @param text The text content
     * @return The created {@link MtrTag} instance
     */
    public static MtrTag mtr(final String text) {
        return new MtrTag().withText(text);
    }

    /**
     * Creates a {@code mtr} MathML element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return The created {@link MtrTag} instance
     */
    public static MtrTag mtr(final AbstractDomContent... dc) {
        return new MtrTag().with(dc);
    }

    /**
     * Creates a {@code munder} MathML element with the specified text content.
     *
     * @param text The text content
     * @return The created {@link MunderTag} instance
     */
    public static MunderTag munder(final String text) {
        return new MunderTag().withText(text);
    }

    /**
     * Creates a {@code munder} MathML element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return The created {@link MunderTag} instance
     */
    public static MunderTag munder(final AbstractDomContent... dc) {
        return new MunderTag().with(dc);
    }

    /**
     * Creates a {@code munderover} MathML element with the specified text content.
     *
     * @param text The text content
     * @return The created {@link MunderoverTag} instance
     */
    public static MunderoverTag munderover(final String text) {
        return new MunderoverTag().withText(text);
    }

    /**
     * Creates a {@code munderover} MathML element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return The created {@link MunderoverTag} instance
     */
    public static MunderoverTag munderover(final AbstractDomContent... dc) {
        return new MunderoverTag().with(dc);
    }

    /**
     * Creates a {@code none} MathML element with the specified text content.
     *
     * @param text The text content
     * @return The created {@link NoneTag} instance
     */
    public static NoneTag none(final String text) {
        return new NoneTag().withText(text);
    }

    /**
     * Creates a {@code none} MathML element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return The created {@link NoneTag} instance
     */
    public static NoneTag none(final AbstractDomContent... dc) {
        return new NoneTag().with(dc);
    }

    /**
     * Creates a {@code semantics} MathML element with the specified text content.
     *
     * @param text The text content
     * @return The created {@link SemanticsTag} instance
     */
    public static SemanticsTag semantics(final String text) {
        return new SemanticsTag().withText(text);
    }

    /**
     * Creates a {@code semantics} MathML element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return The created {@link SemanticsTag} instance
     */
    public static SemanticsTag semantics(final AbstractDomContent... dc) {
        return new SemanticsTag().with(dc);
    }
}
