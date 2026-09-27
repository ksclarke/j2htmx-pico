package info.freelibrary.j2htmx.pico.codegen.model;

/**
 * Descriptor representing an HTML attribute and the elements it applies to.
 */
public final class AttrD {
    //attribute descriptor

    /** The attribute name. */
    public final String attr;

    /** Whether the attribute requires a value argument. */
    public final boolean hasArgument;

    /** The HTML tag names this attribute can be applied to. */
    public final String[] tags;

    /**
     * Constructs an attribute descriptor with no element restrictions.
     *
     * @param attr The attribute name
     * @param hasArgument Whether the attribute accepts an argument
     */
    public AttrD(final String attr, final boolean hasArgument) {
        this.attr = attr;
        this.hasArgument = hasArgument;
        this.tags = new String[]{};
    }

    /**
     * Constructs an attribute descriptor applicable to specific tags.
     *
     * @param attr The attribute name
     * @param hasArgument Whether the attribute accepts an argument
     * @param tags The HTML tag names this attribute applies to
     */
    public AttrD(final String attr, final boolean hasArgument, final String... tags) {
        this.attr = attr;
        this.hasArgument = hasArgument;
        this.tags = tags;
    }
}
