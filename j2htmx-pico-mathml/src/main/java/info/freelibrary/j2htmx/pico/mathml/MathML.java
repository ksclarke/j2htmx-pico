/*
 * This code was derived from j2html, which is distributed under the Apache License, Version 2.0.
 * See LICENSE-ASL-2.0.txt in the parent project's root directory for more information.
 *
 * This file was modified for j2htmx-pico by Kevin S. Clarke (my.j2htmx-pico@kevinclarke.info).
 *
 * The modifications are licensed under the Mozilla Public License, Version 2.0.
 * See LICENSE-MPL-2.0.txt in the parent project's root directory for more information.
 */

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
  {"PMD.ShortMethodName", PMD.TOO_MANY_METHODS, PMD.EXCESSIVE_PUBLIC_COUNT, PMD.COUPLING_BETWEEN_OBJECTS,
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
     * @param aText The text content
     * @return The created {@link AnnotationTag} instance
     */
    public static AnnotationTag annotation(final String aText) {
        return new AnnotationTag().withText(aText);
    }

    /**
     * Creates a {@code annotation} MathML element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return The created {@link AnnotationTag} instance
     */
    public static AnnotationTag annotation(final AbstractDomContent... aDc) {
        return new AnnotationTag().with(aDc);
    }

    /**
     * Creates a {@code maction} MathML element with the specified text content.
     *
     * @param aText The text content
     * @return The created {@link MactionTag} instance
     */
    public static MactionTag maction(final String aText) {
        return new MactionTag().withText(aText);
    }

    /**
     * Creates a {@code maction} MathML element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return The created {@link MactionTag} instance
     */
    public static MactionTag maction(final AbstractDomContent... aDc) {
        return new MactionTag().with(aDc);
    }

    /**
     * Creates a {@code math} MathML element with the specified text content.
     *
     * @param aText The text content
     * @return The created {@link MathTag} instance
     */
    public static MathTag math(final String aText) {
        return new MathTag().withText(aText);
    }

    /**
     * Creates a {@code math} MathML element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return The created {@link MathTag} instance
     */
    public static MathTag math(final AbstractDomContent... aDc) {
        return new MathTag().with(aDc);
    }

    /**
     * Creates a {@code merror} MathML element with the specified text content.
     *
     * @param aText The text content
     * @return The created {@link MerrorTag} instance
     */
    public static MerrorTag merror(final String aText) {
        return new MerrorTag().withText(aText);
    }

    /**
     * Creates a {@code merror} MathML element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return The created {@link MerrorTag} instance
     */
    public static MerrorTag merror(final AbstractDomContent... aDc) {
        return new MerrorTag().with(aDc);
    }

    /**
     * Creates a {@code mfrac} MathML element with the specified text content.
     *
     * @param aText The text content
     * @return The created {@link MfracTag} instance
     */
    public static MfracTag mfrac(final String aText) {
        return new MfracTag().withText(aText);
    }

    /**
     * Creates a {@code mfrac} MathML element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return The created {@link MfracTag} instance
     */
    public static MfracTag mfrac(final AbstractDomContent... aDc) {
        return new MfracTag().with(aDc);
    }

    /**
     * Creates a {@code mi} MathML element with the specified text content.
     *
     * @param aText The text content
     * @return The created {@link MiTag} instance
     */
    public static MiTag mi(final String aText) {
        return new MiTag().withText(aText);
    }

    /**
     * Creates a {@code mi} MathML element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return The created {@link MiTag} instance
     */
    public static MiTag mi(final AbstractDomContent... aDc) {
        return new MiTag().with(aDc);
    }

    /**
     * Creates a {@code mmultiscripts} MathML element with the specified text content.
     *
     * @param aText The text content
     * @return The created {@link MmultiscriptsTag} instance
     */
    public static MmultiscriptsTag mmultiscripts(final String aText) {
        return new MmultiscriptsTag().withText(aText);
    }

    /**
     * Creates a {@code mmultiscripts} MathML element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return The created {@link MmultiscriptsTag} instance
     */
    public static MmultiscriptsTag mmultiscripts(final AbstractDomContent... aDc) {
        return new MmultiscriptsTag().with(aDc);
    }

    /**
     * Creates a {@code mn} MathML element with the specified text content.
     *
     * @param aText The text content
     * @return The created {@link MnTag} instance
     */
    public static MnTag mn(final String aText) {
        return new MnTag().withText(aText);
    }

    /**
     * Creates a {@code mn} MathML element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return The created {@link MnTag} instance
     */
    public static MnTag mn(final AbstractDomContent... aDc) {
        return new MnTag().with(aDc);
    }

    /**
     * Creates a {@code mo} MathML element with the specified text content.
     *
     * @param aText The text content
     * @return The created {@link MoTag} instance
     */
    public static MoTag mo(final String aText) {
        return new MoTag().withText(aText);
    }

    /**
     * Creates a {@code mo} MathML element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return The created {@link MoTag} instance
     */
    public static MoTag mo(final AbstractDomContent... aDc) {
        return new MoTag().with(aDc);
    }

    /**
     * Creates a {@code mover} MathML element with the specified text content.
     *
     * @param aText The text content
     * @return The created {@link MoverTag} instance
     */
    public static MoverTag mover(final String aText) {
        return new MoverTag().withText(aText);
    }

    /**
     * Creates a {@code mover} MathML element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return The created {@link MoverTag} instance
     */
    public static MoverTag mover(final AbstractDomContent... aDc) {
        return new MoverTag().with(aDc);
    }

    /**
     * Creates a {@code mpadded} MathML element with the specified text content.
     *
     * @param aText The text content
     * @return The created {@link MpaddedTag} instance
     */
    public static MpaddedTag mpadded(final String aText) {
        return new MpaddedTag().withText(aText);
    }

    /**
     * Creates a {@code mpadded} MathML element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return The created {@link MpaddedTag} instance
     */
    public static MpaddedTag mpadded(final AbstractDomContent... aDc) {
        return new MpaddedTag().with(aDc);
    }

    /**
     * Creates a {@code mphantom} MathML element with the specified text content.
     *
     * @param aText The text content
     * @return The created {@link MphantomTag} instance
     */
    public static MphantomTag mphantom(final String aText) {
        return new MphantomTag().withText(aText);
    }

    /**
     * Creates a {@code mphantom} MathML element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return The created {@link MphantomTag} instance
     */
    public static MphantomTag mphantom(final AbstractDomContent... aDc) {
        return new MphantomTag().with(aDc);
    }

    /**
     * Creates a {@code mprescripts} MathML element with the specified text content.
     *
     * @param aText The text content
     * @return The created {@link MprescriptsTag} instance
     */
    public static MprescriptsTag mprescripts(final String aText) {
        return new MprescriptsTag().withText(aText);
    }

    /**
     * Creates a {@code mprescripts} MathML element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return The created {@link MprescriptsTag} instance
     */
    public static MprescriptsTag mprescripts(final AbstractDomContent... aDc) {
        return new MprescriptsTag().with(aDc);
    }

    /**
     * Creates a {@code mroot} MathML element with the specified text content.
     *
     * @param aText The text content
     * @return The created {@link MrootTag} instance
     */
    public static MrootTag mroot(final String aText) {
        return new MrootTag().withText(aText);
    }

    /**
     * Creates a {@code mroot} MathML element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return The created {@link MrootTag} instance
     */
    public static MrootTag mroot(final AbstractDomContent... aDc) {
        return new MrootTag().with(aDc);
    }

    /**
     * Creates a {@code mrow} MathML element with the specified text content.
     *
     * @param aText The text content
     * @return The created {@link MrowTag} instance
     */
    public static MrowTag mrow(final String aText) {
        return new MrowTag().withText(aText);
    }

    /**
     * Creates a {@code mrow} MathML element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return The created {@link MrowTag} instance
     */
    public static MrowTag mrow(final AbstractDomContent... aDc) {
        return new MrowTag().with(aDc);
    }

    /**
     * Creates a {@code ms} MathML element with the specified text content.
     *
     * @param aText The text content
     * @return The created {@link MsTag} instance
     */
    public static MsTag ms(final String aText) {
        return new MsTag().withText(aText);
    }

    /**
     * Creates a {@code ms} MathML element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return The created {@link MsTag} instance
     */
    public static MsTag ms(final AbstractDomContent... aDc) {
        return new MsTag().with(aDc);
    }

    /**
     * Creates a {@code mspace} MathML element with the specified text content.
     *
     * @param aText The text content
     * @return The created {@link MspaceTag} instance
     */
    public static MspaceTag mspace(final String aText) {
        return new MspaceTag().withText(aText);
    }

    /**
     * Creates a {@code mspace} MathML element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return The created {@link MspaceTag} instance
     */
    public static MspaceTag mspace(final AbstractDomContent... aDc) {
        return new MspaceTag().with(aDc);
    }

    /**
     * Creates a {@code msqrt} MathML element with the specified text content.
     *
     * @param aText The text content
     * @return The created {@link MsqrtTag} instance
     */
    public static MsqrtTag msqrt(final String aText) {
        return new MsqrtTag().withText(aText);
    }

    /**
     * Creates a {@code msqrt} MathML element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return The created {@link MsqrtTag} instance
     */
    public static MsqrtTag msqrt(final AbstractDomContent... aDc) {
        return new MsqrtTag().with(aDc);
    }

    /**
     * Creates a {@code mstyle} MathML element with the specified text content.
     *
     * @param aText The text content
     * @return The created {@link MstyleTag} instance
     */
    public static MstyleTag mstyle(final String aText) {
        return new MstyleTag().withText(aText);
    }

    /**
     * Creates a {@code mstyle} MathML element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return The created {@link MstyleTag} instance
     */
    public static MstyleTag mstyle(final AbstractDomContent... aDc) {
        return new MstyleTag().with(aDc);
    }

    /**
     * Creates a {@code msub} MathML element with the specified text content.
     *
     * @param aText The text content
     * @return The created {@link MsubTag} instance
     */
    public static MsubTag msub(final String aText) {
        return new MsubTag().withText(aText);
    }

    /**
     * Creates a {@code msub} MathML element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return The created {@link MsubTag} instance
     */
    public static MsubTag msub(final AbstractDomContent... aDc) {
        return new MsubTag().with(aDc);
    }

    /**
     * Creates a {@code msubsup} MathML element with the specified text content.
     *
     * @param aText The text content
     * @return The created {@link MsubsupTag} instance
     */
    public static MsubsupTag msubsup(final String aText) {
        return new MsubsupTag().withText(aText);
    }

    /**
     * Creates a {@code msubsup} MathML element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return The created {@link MsubsupTag} instance
     */
    public static MsubsupTag msubsup(final AbstractDomContent... aDc) {
        return new MsubsupTag().with(aDc);
    }

    /**
     * Creates a {@code msup} MathML element with the specified text content.
     *
     * @param aText The text content
     * @return The created {@link MsupTag} instance
     */
    public static MsupTag msup(final String aText) {
        return new MsupTag().withText(aText);
    }

    /**
     * Creates a {@code msup} MathML element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return The created {@link MsupTag} instance
     */
    public static MsupTag msup(final AbstractDomContent... aDc) {
        return new MsupTag().with(aDc);
    }

    /**
     * Creates a {@code mtable} MathML element with the specified text content.
     *
     * @param aText The text content
     * @return The created {@link MtableTag} instance
     */
    public static MtableTag mtable(final String aText) {
        return new MtableTag().withText(aText);
    }

    /**
     * Creates a {@code mtable} MathML element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return The created {@link MtableTag} instance
     */
    public static MtableTag mtable(final AbstractDomContent... aDc) {
        return new MtableTag().with(aDc);
    }

    /**
     * Creates a {@code mtd} MathML element with the specified text content.
     *
     * @param aText The text content
     * @return The created {@link MtdTag} instance
     */
    public static MtdTag mtd(final String aText) {
        return new MtdTag().withText(aText);
    }

    /**
     * Creates a {@code mtd} MathML element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return The created {@link MtdTag} instance
     */
    public static MtdTag mtd(final AbstractDomContent... aDc) {
        return new MtdTag().with(aDc);
    }

    /**
     * Creates a {@code mtext} MathML element with the specified text content.
     *
     * @param aText The text content
     * @return The created {@link MtextTag} instance
     */
    public static MtextTag mtext(final String aText) {
        return new MtextTag().withText(aText);
    }

    /**
     * Creates a {@code mtext} MathML element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return The created {@link MtextTag} instance
     */
    public static MtextTag mtext(final AbstractDomContent... aDc) {
        return new MtextTag().with(aDc);
    }

    /**
     * Creates a {@code mtr} MathML element with the specified text content.
     *
     * @param aText The text content
     * @return The created {@link MtrTag} instance
     */
    public static MtrTag mtr(final String aText) {
        return new MtrTag().withText(aText);
    }

    /**
     * Creates a {@code mtr} MathML element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return The created {@link MtrTag} instance
     */
    public static MtrTag mtr(final AbstractDomContent... aDc) {
        return new MtrTag().with(aDc);
    }

    /**
     * Creates a {@code munder} MathML element with the specified text content.
     *
     * @param aText The text content
     * @return The created {@link MunderTag} instance
     */
    public static MunderTag munder(final String aText) {
        return new MunderTag().withText(aText);
    }

    /**
     * Creates a {@code munder} MathML element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return The created {@link MunderTag} instance
     */
    public static MunderTag munder(final AbstractDomContent... aDc) {
        return new MunderTag().with(aDc);
    }

    /**
     * Creates a {@code munderover} MathML element with the specified text content.
     *
     * @param aText The text content
     * @return The created {@link MunderoverTag} instance
     */
    public static MunderoverTag munderover(final String aText) {
        return new MunderoverTag().withText(aText);
    }

    /**
     * Creates a {@code munderover} MathML element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return The created {@link MunderoverTag} instance
     */
    public static MunderoverTag munderover(final AbstractDomContent... aDc) {
        return new MunderoverTag().with(aDc);
    }

    /**
     * Creates a {@code none} MathML element with the specified text content.
     *
     * @param aText The text content
     * @return The created {@link NoneTag} instance
     */
    public static NoneTag none(final String aText) {
        return new NoneTag().withText(aText);
    }

    /**
     * Creates a {@code none} MathML element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return The created {@link NoneTag} instance
     */
    public static NoneTag none(final AbstractDomContent... aDc) {
        return new NoneTag().with(aDc);
    }

    /**
     * Creates a {@code semantics} MathML element with the specified text content.
     *
     * @param aText The text content
     * @return The created {@link SemanticsTag} instance
     */
    public static SemanticsTag semantics(final String aText) {
        return new SemanticsTag().withText(aText);
    }

    /**
     * Creates a {@code semantics} MathML element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return The created {@link SemanticsTag} instance
     */
    public static SemanticsTag semantics(final AbstractDomContent... aDc) {
        return new SemanticsTag().with(aDc);
    }
}
