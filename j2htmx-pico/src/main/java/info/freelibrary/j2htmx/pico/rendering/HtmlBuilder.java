package info.freelibrary.j2htmx.pico.rendering;

import java.io.IOException;

/**
 * Implementations of HtmlBuilder are wrappers around an Appendable, and support appending HTML-specific character
 * sequences to that Appendable.
 *
 * @param <T> The type of the Appendable.  Used so that the same type can be returned to the caller, allowing for
 *     additional work to be done on the Appendable without the need for manual casting.
 */
public interface HtmlBuilder<T extends Appendable> {

    /**
     * Appends a start tag with the given name to the output. The returned TagBuilder is then used to append attributes
     * and eventually complete the start tag.
     *
     * @param aName The name of the start tag.
     * @return An TagBuilder which can append attributes to the start tag.
     * @throws IOException When the Appendable throws an IOException.
     */
    TagBuilder appendStartTag(String aName) throws IOException;

    /**
     * Appends an end tag with the given name to the output.
     *
     * @param aName The name of the end tag.
     * @return An HtmlBuilder that can continue appending HTML to the output.
     * @throws IOException When the Appendable throws an IOException.
     */
    HtmlBuilder<T> appendEndTag(String aName) throws IOException;

    /**
     * Appends an empty tag with the given name to the output. The returned TagBuilder is then used to append attributes
     * and eventually complete the empty tag.
     *
     * @param aName The name of the empty tag.
     * @return An TagBuilder which can append attributes to the empty tag.
     * @throws IOException When the Appendable throws an IOException.
     */
    TagBuilder appendEmptyTag(String aName) throws IOException;

    /**
     * Appends escaped text to the output.
     *
     * @param aTxt The text to append.
     * @return An HtmlBuilder that can continue appending HTML to the output.
     * @throws IOException When the Appendable throws an IOException.
     */
    HtmlBuilder<T> appendEscapedText(String aTxt) throws IOException;

    /**
     * Appends unescaped text to the output.
     *
     * @param aTxt The text to append.
     * @return An HtmlBuilder that can continue appending HTML to the output.
     * @throws IOException When the Appendable throws an IOException.
     */
    HtmlBuilder<T> appendUnescapedText(String aTxt) throws IOException;

    /**
     * Returns the Appendable that was being wrapped.
     *
     * @return The original Appendable.
     */
    T output();

}
