/*
 * This code was derived from j2html, which is distributed under the Apache License, Version 2.0.
 * See LICENSE-ASL-2.0.txt in the parent project's root directory for more information.
 *
 * This file was modified for j2htmx-pico by Kevin S. Clarke (my.j2htmx-pico@kevinclarke.info).
 *
 * The modifications are licensed under the Mozilla Public License, Version 2.0.
 * See LICENSE-MPL-2.0.txt in the parent project's root directory for more information.
 */

package info.freelibrary.j2htmx.pico.rendering;

import info.freelibrary.j2htmx.pico.Config;
import info.freelibrary.j2htmx.pico.utils.Indenter;
import info.freelibrary.j2htmx.pico.utils.TextEscaper;
import info.freelibrary.util.warnings.Checkstyle;
import info.freelibrary.util.warnings.PMD;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Composes HTML with lines breaks and indentation between tags and text.
 *
 * @param <T> The type of the Appendable to which HTML will be appended.
 */
@SuppressWarnings({Checkstyle.MULTIPLE_STRING_LITERALS, "MultipleStringLiterals"})
public final class IndentedHtml<T extends Appendable> implements HtmlBuilder<T> {

    /** The Appendable output destination. */
    private final T myOut;

    /** The indenter specifying indentation rules. */
    private final Indenter myIndenter;

    /** The text escaper for escaping text and attribute values. */
    private final TextEscaper myTextEscaper;

    /** TagBuilder used for appending attributes to enclosing tags. */
    private final TagBuilder myEnclosingElementAttributes;

    /** TagBuilder used for appending attributes to empty/void tags. */
    private final TagBuilder myEmptyElementAttributes;

    /**
     * Stack tracking currently open element tags to handle indentation and preformatted blocks.
     */
    private final Deque<String> myTrace = new ArrayDeque<>();

    /**
     * Creates a new IndentedHtml builder.
     *
     * @param aOut The Appendable to which HTML will be appended
     * @param aConfig The configuration to use
     */
    private IndentedHtml(final T aOut, final Config aConfig) {
        this.myOut = aOut;
        this.myIndenter = aConfig.indenter();
        this.myTextEscaper = aConfig.textEscaper();
        this.myEnclosingElementAttributes = new IndentedTagBuilder(false);
        this.myEmptyElementAttributes = new IndentedTagBuilder(aConfig.closeEmptyTags());
    }

    /**
     * Returns an HtmlBuilder that will generate indented HTML using Config defaults.
     *
     * @param aOut The Appendable to which HTML will be appended.
     * @param <T> The type of the Appendable to which HTML will be appended.
     * @return An HtmlBuilder for indented HTML.
     */
    public static <T extends Appendable> IndentedHtml<T> into(final T aOut) {
        return new IndentedHtml<>(aOut, Config.getDefaults());
    }

    /**
     * Returns an HtmlBuilder that will generate indented HTML using the given Config.
     *
     * @param aOut The Appendable to which HTML will be appended.
     * @param aConfig The Config which will specify indentation, text escapement, tag closing, etc.
     * @param <T> The type of the Appendable to which HTML will be appended.
     * @return An HtmlBuilder for indented HTML.
     */
    public static <T extends Appendable> IndentedHtml<T> into(final T aOut, final Config aConfig) {
        return new IndentedHtml<>(aOut, aConfig);
    }

    // Dealing with preformatted elements (pre and textarea) requires
    // that we know what our parent elements are.  To do that we use
    // a stack; adding items as start tags are created, and removing them
    // as those tags are closed.  Determining whether or not we are
    // currently rendering into a preformatted element is as simple as
    // asking if any tags on the stack match a preformatted element name.

    /**
     * Returns an HtmlBuilder that will generate indented HTML in memory using Config defaults.
     *
     * @return An HtmlBuilder for indented HTML.
     */
    public static IndentedHtml<StringBuilder> inMemory() {
        return into(new StringBuilder());
    }

    /**
     * Returns an HtmlBuilder that will generate indented HTML in memory using the given Config.
     *
     * @param aConfig The Config which will specify indentation, text escapement, tag closing, etc.
     * @return An HtmlBuilder for indented HTML.
     */
    public static IndentedHtml<StringBuilder> inMemory(final Config aConfig) {
        return into(new StringBuilder(), aConfig);
    }

    /**
     * Checks if the current element context is self-formatting (e.g. {@code <pre>} or {@code <textarea>}).
     *
     * @return True if self-formatting, false otherwise
     */
    private boolean isContentSelfFormatting() {
        return myTrace.contains("pre") || myTrace.contains("textarea");
    }

    /**
     * Gets the current indentation level based on the tag nesting depth.
     *
     * @return The current indentation level
     */
    private int lvl() {
        return myTrace.size();
    }

    @Override
    public TagBuilder appendStartTag(final String aName) throws IOException {
        if (!isContentSelfFormatting()) {
            myOut.append(myIndenter.indent(lvl(), ""));
        }

        myTrace.push(aName);
        myOut.append("<").append(aName);
        return myEnclosingElementAttributes;
    }

    @Override
    @SuppressWarnings(PMD.AVOID_THROWING_RAW_EXCEPTION_TYPES)
    public HtmlBuilder<T> appendEndTag(final String aName) throws IOException {
        if (!aName.equals(myTrace.peek())) {
            throw new RuntimeException("Incorrect element closed: " + aName + ".  Expected: " + myTrace.peek());
        }

        if (!isContentSelfFormatting()) {
            myTrace.pop();
            myOut.append(myIndenter.indent(lvl(), ""));
        } else {
            myTrace.pop();
        }

        myOut.append("</").append(aName).append(">");

        if (!isContentSelfFormatting()) {
            myOut.append("\n");
        }

        return this;
    }

    @Override
    public TagBuilder appendEmptyTag(final String aName) throws IOException {
        if (!isContentSelfFormatting()) {
            myOut.append(myIndenter.indent(lvl(), ""));
        }
        myOut.append("<").append(aName);
        return myEmptyElementAttributes;
    }

    /**
     * Appends lines of text with appropriate indentation.
     *
     * @param aTxt The text lines to append
     * @throws IOException If an I/O error occurs
     */
    private void appendLines(final String aTxt) throws IOException {
        if (!isContentSelfFormatting()) {
            final String[] lines = aTxt.split("\n");
            for (final String line : lines) {
                myOut.append(myIndenter.indent(lvl(), line)).append("\n");
            }
        } else {
            myOut.append(aTxt);
        }
    }

    @Override
    public HtmlBuilder<T> appendEscapedText(final String aTxt) throws IOException {
        appendLines(myTextEscaper.escape(aTxt));
        return this;
    }

    @Override
    public HtmlBuilder<T> appendUnescapedText(final String aTxt) throws IOException {
        appendLines(aTxt);
        return this;
    }

    /**
     * Returns the underlying Appendable receiving the formatted HTML output.
     *
     * @return The Appendable output destination
     */
    @Override
    public T output() {
        return myOut;
    }

    /**
     * TagBuilder implementation for formatted, indented HTML rendering.
     */
    private final class IndentedTagBuilder implements TagBuilder {

        /** Whether to append a trailing slash before closing the tag. */
        private final boolean isCloseTag;

        /**
         * Creates a new IndentedTagBuilder.
         *
         * @param aCloseTag Whether to close empty tags
         */
        private IndentedTagBuilder(final boolean aCloseTag) {
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

            if (!isContentSelfFormatting()) {
                myOut.append("\n");
            }

            return IndentedHtml.this;
        }

    }
}
