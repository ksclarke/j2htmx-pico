package info.freelibrary.j2htmx.pico.rendering;

import info.freelibrary.j2htmx.pico.Config;
import info.freelibrary.j2htmx.pico.utils.TextEscaper;

import java.io.IOException;

/**
 * Composes HTML without any extra line breaks or indentation.
 *
 * @param <T> The type of the Appendable to which HTML will be appended.
 */
public final class FlatHtml<T extends Appendable> implements HtmlBuilder<T> {

    /** The Appendable output destination. */
    private final T out;
    /** The text escaper for escaping text and attribute values. */
    private final TextEscaper textEscaper;
    /** TagBuilder used for appending attributes to enclosing tags. */
    private final TagBuilder enclosingElementAttributes;
    /** TagBuilder used for appending attributes to empty/void tags. */
    private final TagBuilder emptyElementAttributes;

    /**
     * Creates a new FlatHtml builder.
     *
     * @param out The Appendable to which HTML will be appended
     * @param config The configuration to use
     */
    private FlatHtml(final T out, final Config config) {
        this.out = out;
        this.textEscaper = config.textEscaper();
        this.enclosingElementAttributes = new FlatTagBuilder(false);
        this.emptyElementAttributes = new FlatTagBuilder(config.closeEmptyTags());
    }

    /**
     * Returns an HtmlBuilder that will generate flat HTML using Config defaults.
     *
     * @param out The Appendable to which HTML will be appended.
     * @param <T> The type of the Appendable to which HTML will be appended.
     * @return An HtmlBuilder for flat HTML.
     */
    public static final <T extends Appendable> FlatHtml<T> into(final T out) {
        return new FlatHtml<>(out, Config.getDefaults());
    }

    /**
     * Returns an HtmlBuilder that will generate flat HTML using the given Config.
     *
     * @param out The Appendable to which HTML will be appended.
     * @param config The Config which will specify text escapement, tag closing, etc.
     * @param <T> The type of the Appendable to which HTML will be appended.
     * @return An HtmlBuilder for flat HTML.
     */
    public static final <T extends Appendable> FlatHtml<T> into(final T out, final Config config) {
        return new FlatHtml<>(out, config);
    }

    /**
     * Returns an HtmlBuilder that will generate flat HTML in memory using Config defaults.
     *
     * @return An HtmlBuilder for flat HTML.
     */
    public static final FlatHtml<StringBuilder> inMemory() {
        return into(new StringBuilder());
    }

    /**
     * Returns an HtmlBuilder that will generate flat HTML in memory using the given Config.
     *
     * @param config The Config which will specify text escapement, tag closing, etc.
     * @return An HtmlBuilder for flat HTML.
     */
    public static final FlatHtml<StringBuilder> inMemory(final Config config) {
        return into(new StringBuilder(), config);
    }

    /**
     * Returns the underlying Appendable receiving the HTML output.
     *
     * @return The Appendable output destination
     */
    public T output() {
        return out;
    }

    @Override
    @Deprecated
    public HtmlBuilder<T> append(final CharSequence csq) throws IOException {
        out.append(csq);
        return this;
    }

    @Override
    @Deprecated
    public HtmlBuilder<T> append(final CharSequence csq, final int start, final int end) throws IOException {
        out.append(csq, start, end);
        return this;
    }

    @Override
    @Deprecated
    public HtmlBuilder<T> append(final char c) throws IOException {
        out.append(c);
        return this;
    }

    @Override
    public TagBuilder appendStartTag(final String name) throws IOException {
        out.append("<").append(name);
        return enclosingElementAttributes;
    }

    @Override
    public HtmlBuilder<T> appendEndTag(final String name) throws IOException {
        out.append("</").append(name).append(">");
        return this;
    }

    @Override
    public TagBuilder appendEmptyTag(final String name) throws IOException {
        out.append("<").append(name);
        return emptyElementAttributes;
    }

    @Override
    public HtmlBuilder<T> appendEscapedText(final String txt) throws IOException {
        out.append(textEscaper.escape(txt));
        return this;
    }

    @Override
    public HtmlBuilder<T> appendUnescapedText(final String txt) throws IOException {
        out.append(txt);
        return this;
    }

    /**
     * TagBuilder implementation for flat, unindented HTML rendering.
     */
    private final class FlatTagBuilder implements TagBuilder {

        /** Whether to append a trailing slash before closing the tag. */
        private final boolean closeTag;

        /**
         * Creates a new FlatTagBuilder.
         *
         * @param closeTag Whether to close empty tags
         */
        private FlatTagBuilder(final boolean closeTag) {
            this.closeTag = closeTag;
        }

        @Override
        public TagBuilder appendAttribute(final String name, final String value) throws IOException {
            out.append(" ")
              .append(name)
              .append("=\"")
              .append(textEscaper.escape(value))
              .append("\"");
            return this;
        }

        @Override
        public TagBuilder appendBooleanAttribute(final String name) throws IOException {
            out.append(" ").append(name);
            return this;
        }

        @Override
        public HtmlBuilder<T> completeTag() throws IOException {
            if (closeTag) {
                out.append("/");
            }
            out.append(">");

            return FlatHtml.this;
        }

        @Override
        @Deprecated
        public TagBuilder append(final CharSequence csq) throws IOException {
            out.append(csq);
            return this;
        }

        @Override
        @Deprecated
        public TagBuilder append(final CharSequence csq, final int start, final int end) throws IOException {
            out.append(csq, start, end);
            return this;
        }

        @Override
        @Deprecated
        public TagBuilder append(final char c) throws IOException {
            out.append(c);
            return this;
        }
    }
}
