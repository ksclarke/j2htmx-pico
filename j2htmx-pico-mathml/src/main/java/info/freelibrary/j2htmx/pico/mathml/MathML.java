package info.freelibrary.j2htmx.pico.mathml;

import info.freelibrary.j2htmx.pico.mathml.tags.*;
import info.freelibrary.j2htmx.pico.tags.DomContent;

/**
 * Factory class providing static builder methods for creating MathML elements.
 */
public final class MathML {

    private MathML() {
    }

    public static AnnotationTag annotation(final String text) {
        return new AnnotationTag().withText(text);
    }

    public static AnnotationTag annotation(final DomContent... dc) {
        return new AnnotationTag().with(dc);
    }

    public static MactionTag maction(final String text) {
        return new MactionTag().withText(text);
    }

    public static MactionTag maction(final DomContent... dc) {
        return new MactionTag().with(dc);
    }

    public static MathTag math(final String text) {
        return new MathTag().withText(text);
    }

    public static MathTag math(final DomContent... dc) {
        return new MathTag().with(dc);
    }

    public static MerrorTag merror(final String text) {
        return new MerrorTag().withText(text);
    }

    public static MerrorTag merror(final DomContent... dc) {
        return new MerrorTag().with(dc);
    }

    public static MfracTag mfrac(final String text) {
        return new MfracTag().withText(text);
    }

    public static MfracTag mfrac(final DomContent... dc) {
        return new MfracTag().with(dc);
    }

    public static MiTag mi(final String text) {
        return new MiTag().withText(text);
    }

    public static MiTag mi(final DomContent... dc) {
        return new MiTag().with(dc);
    }

    public static MmultiscriptsTag mmultiscripts(final String text) {
        return new MmultiscriptsTag().withText(text);
    }

    public static MmultiscriptsTag mmultiscripts(final DomContent... dc) {
        return new MmultiscriptsTag().with(dc);
    }

    public static MnTag mn(final String text) {
        return new MnTag().withText(text);
    }

    public static MnTag mn(final DomContent... dc) {
        return new MnTag().with(dc);
    }

    public static MoTag mo(final String text) {
        return new MoTag().withText(text);
    }

    public static MoTag mo(final DomContent... dc) {
        return new MoTag().with(dc);
    }

    public static MoverTag mover(final String text) {
        return new MoverTag().withText(text);
    }

    public static MoverTag mover(final DomContent... dc) {
        return new MoverTag().with(dc);
    }

    public static MpaddedTag mpadded(final String text) {
        return new MpaddedTag().withText(text);
    }

    public static MpaddedTag mpadded(final DomContent... dc) {
        return new MpaddedTag().with(dc);
    }

    public static MphantomTag mphantom(final String text) {
        return new MphantomTag().withText(text);
    }

    public static MphantomTag mphantom(final DomContent... dc) {
        return new MphantomTag().with(dc);
    }

    public static MprescriptsTag mprescripts(final String text) {
        return new MprescriptsTag().withText(text);
    }

    public static MprescriptsTag mprescripts(final DomContent... dc) {
        return new MprescriptsTag().with(dc);
    }

    public static MrootTag mroot(final String text) {
        return new MrootTag().withText(text);
    }

    public static MrootTag mroot(final DomContent... dc) {
        return new MrootTag().with(dc);
    }

    public static MrowTag mrow(final String text) {
        return new MrowTag().withText(text);
    }

    public static MrowTag mrow(final DomContent... dc) {
        return new MrowTag().with(dc);
    }

    public static MsTag ms(final String text) {
        return new MsTag().withText(text);
    }

    public static MsTag ms(final DomContent... dc) {
        return new MsTag().with(dc);
    }

    public static MspaceTag mspace(final String text) {
        return new MspaceTag().withText(text);
    }

    public static MspaceTag mspace(final DomContent... dc) {
        return new MspaceTag().with(dc);
    }

    public static MsqrtTag msqrt(final String text) {
        return new MsqrtTag().withText(text);
    }

    public static MsqrtTag msqrt(final DomContent... dc) {
        return new MsqrtTag().with(dc);
    }

    public static MstyleTag mstyle(final String text) {
        return new MstyleTag().withText(text);
    }

    public static MstyleTag mstyle(final DomContent... dc) {
        return new MstyleTag().with(dc);
    }

    public static MsubTag msub(final String text) {
        return new MsubTag().withText(text);
    }

    public static MsubTag msub(final DomContent... dc) {
        return new MsubTag().with(dc);
    }

    public static MsubsupTag msubsup(final String text) {
        return new MsubsupTag().withText(text);
    }

    public static MsubsupTag msubsup(final DomContent... dc) {
        return new MsubsupTag().with(dc);
    }

    public static MsupTag msup(final String text) {
        return new MsupTag().withText(text);
    }

    public static MsupTag msup(final DomContent... dc) {
        return new MsupTag().with(dc);
    }

    public static MtableTag mtable(final String text) {
        return new MtableTag().withText(text);
    }

    public static MtableTag mtable(final DomContent... dc) {
        return new MtableTag().with(dc);
    }

    public static MtdTag mtd(final String text) {
        return new MtdTag().withText(text);
    }

    public static MtdTag mtd(final DomContent... dc) {
        return new MtdTag().with(dc);
    }

    public static MtextTag mtext(final String text) {
        return new MtextTag().withText(text);
    }

    public static MtextTag mtext(final DomContent... dc) {
        return new MtextTag().with(dc);
    }

    public static MtrTag mtr(final String text) {
        return new MtrTag().withText(text);
    }

    public static MtrTag mtr(final DomContent... dc) {
        return new MtrTag().with(dc);
    }

    public static MunderTag munder(final String text) {
        return new MunderTag().withText(text);
    }

    public static MunderTag munder(final DomContent... dc) {
        return new MunderTag().with(dc);
    }

    public static MunderoverTag munderover(final String text) {
        return new MunderoverTag().withText(text);
    }

    public static MunderoverTag munderover(final DomContent... dc) {
        return new MunderoverTag().with(dc);
    }

    public static NoneTag none(final String text) {
        return new NoneTag().withText(text);
    }

    public static NoneTag none(final DomContent... dc) {
        return new NoneTag().with(dc);
    }

    public static SemanticsTag semantics(final String text) {
        return new SemanticsTag().withText(text);
    }

    public static SemanticsTag semantics(final DomContent... dc) {
        return new SemanticsTag().with(dc);
    }
}
