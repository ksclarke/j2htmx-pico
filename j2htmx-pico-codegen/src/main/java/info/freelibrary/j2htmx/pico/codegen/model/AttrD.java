package info.freelibrary.j2htmx.pico.codegen.model;

/**
 * Descriptor representing an HTML attribute and the elements it applies to.
 */
public final class AttrD {
    //attribute descriptor

    /** The attribute name. */
    public final String myAttr;

    /** Whether the attribute requires a value argument. */
    public final boolean hasArgument;

    /** The HTML tag names this attribute can be applied to. */
    public final String[] myTags;

    /**
     * Constructs an attribute descriptor with no element restrictions.
     *
     * @param anAttr The attribute name
     * @param aHasArgument Whether the attribute accepts an argument
     */
    public AttrD(final String anAttr, final boolean aHasArgument) {
        this.myAttr = anAttr;
        this.hasArgument = aHasArgument;
        this.myTags = new String[]{};
    }

    /**
     * Constructs an attribute descriptor applicable to specific tags.
     *
     * @param anAttr The attribute name
     * @param aHasArgument Whether the attribute accepts an argument
     * @param aTags The HTML tag names this attribute applies to
     */
    public AttrD(final String anAttr, final boolean aHasArgument, final String... aTags) {
        this.myAttr = anAttr;
        this.hasArgument = aHasArgument;
        this.myTags = aTags != null ? aTags.clone() : null;
    }
}
