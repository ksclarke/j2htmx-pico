package info.freelibrary.j2htmx.pico.rendering;

import info.freelibrary.j2htmx.pico.Config;
import info.freelibrary.j2htmx.pico.utils.TextEscaper;
import info.freelibrary.util.warnings.Checkstyle;

import java.io.IOException;

/**
 * Composes HTML without any extra line breaks or indentation.
 *
 * @param <T> The type of the Appendable to which HTML will be appended.
 */
@SuppressWarnings({Checkstyle.MULTIPLE_STRING_LITERALS, "MultipleStringLiterals"})
public final class FlatHtml<T extends Appendable> implements HtmlBuilder<T> {

    /** The Appendable output destination. */
    private final T myOut;

    /** The text escaper for escaping text and attribute values. */
    private final TextEscaper myTextEscaper;

    /** TagBuilder used for appending attributes to enclosing tags. */
    private final TagBuilder myEnclosingElementAttributes;

    /** TagBuilder used for appending attributes to empty/void tags. */
    private final TagBuilder myEmptyElementAttributes;

    /**
     * Creates a new FlatHtml builder.
     *
     * @param aOut The Appendable to which HTML will be appended
     * @param aConfig The configuration to use
     */
    private FlatHtml(final T aOut, final Config aConfig) {
        this.myOut = aOut;
        this.myTextEscaper = aConfig.textEscaper();
        this.myEnclosingElementAttributes = new FlatTagBuilder(false);
        this.myEmptyElementAttributes = new FlatTagBuilder(aConfig.closeEmptyTags());
    }

    /**
     * Returns an HtmlBuilder that will generate flat HTML using Config defaults.
     *
     * @param aOut The Appendable to which HTML will be appended.
     * @param <T> The type of the Appendable to which HTML will be appended.
     * @return An HtmlBuilder for flat HTML.
     */
    public static <T extends Appendable> FlatHtml<T> into(final T aOut) {
        return new FlatHtml<>(aOut, Config.getDefaults());
    }

    /**
     * Returns an HtmlBuilder that will generate flat HTML using the given Config.
     *
     * @param aOut The Appendable to which HTML will be appended.
     * @param aConfig The Config which will specify text escapement, tag closing, etc.
     * @param <T> The type of the Appendable to which HTML will be appended.
     * @return An HtmlBuilder for flat HTML.
     */
    public static <T extends Appendable> FlatHtml<T> into(final T aOut, final Config aConfig) {
        return new FlatHtml<>(aOut, aConfig);
    }

    /**
     * Returns an HtmlBuilder that will generate flat HTML in memory using Config defaults.
     *
     * @return An HtmlBuilder for flat HTML.
     */
    public static FlatHtml<StringBuilder> inMemory() {
        return into(new StringBuilder());
    }

    /**
     * Returns an HtmlBuilder that will generate flat HTML in memory using the given Config.
     *
     * @param aConfig The Config which will specify text escapement, tag closing, etc.
     * @return An HtmlBuilder for flat HTML.
     */
    public static FlatHtml<StringBuilder> inMemory(final Config aConfig) {
        return into(new StringBuilder(), aConfig);
    }

    /**
     * Returns the underlying Appendable receiving the HTML output.
     *
     * @return The Appendable output destination
     */
    @Override
    public T output() {
        return myOut;
    }

    @Override
    public TagBuilder appendStartTag(final String aName) throws IOException {
        myOut.append("<").append(aName);
        return myEnclosingElementAttributes;
    }

    @Override
    public HtmlBuilder<T> appendEndTag(final String aName) throws IOException {
        myOut.append("</").append(aName).append(">");
        return this;
    }

    @Override
    public TagBuilder appendEmptyTag(final String aName) throws IOException {
        myOut.append("<").append(aName);
        return myEmptyElementAttributes;
    }

    @Override
    public HtmlBuilder<T> appendEscapedText(final String aTxt) throws IOException {
        myOut.append(myTextEscaper.escape(aTxt));
        return this;
    }

    @Override
    public HtmlBuilder<T> appendUnescapedText(final String aTxt) throws IOException {
        myOut.append(aTxt);
        return this;
    }

    /**
     * TagBuilder implementation for flat, unindented HTML rendering.
     */
    private final class FlatTagBuilder implements TagBuilder {

        /** Whether to append a trailing slash before closing the tag. */
        private final boolean isCloseTag;

        /**
         * Creates a new FlatTagBuilder.
         *
         * @param aCloseTag Whether to close empty tags
         */
        private FlatTagBuilder(final boolean aCloseTag) {
            this.isCloseTag = aCloseTag;
        }

        @Override
        public TagBuilder appendAttribute(final String aName, final String aValue) throws IOException {
            myOut.append(" ")
              .append(aName)
              .append("=\"")
              .append(myTextEscaper.escape(aValue))
              .append("\"");
            return this;
        }

        @Override
        public TagBuilder appendBooleanAttribute(final String aName) throws IOException {
            myOut.append(" ").append(aName);
            return this;
        }

        @Override
        public HtmlBuilder<T> completeTag() throws IOException {
            if (isCloseTag) {
                myOut.append("/");
            }
            myOut.append(">");

            return FlatHtml.this;
        }

    }
}
