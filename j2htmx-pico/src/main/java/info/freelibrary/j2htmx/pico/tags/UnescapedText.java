/*
 * This code was derived from j2html, which is distributed under the Apache License, Version 2.0.
 * See LICENSE-ASL-2.0.txt in the parent project's root directory for more information.
 *
 * This file was modified for j2htmx-pico by Kevin S. Clarke (my.j2htmx-pico@kevinclarke.info).
 *
 * The modifications are licensed under the Mozilla Public License, Version 2.0.
 * See LICENSE-MPL-2.0.txt in the parent project's root directory for more information.
 */

package info.freelibrary.j2htmx.pico.tags;

import info.freelibrary.j2htmx.pico.rendering.HtmlBuilder;

import java.io.IOException;

/**
 * Represents raw, unescaped text content within the DOM.
 */
public class UnescapedText extends AbstractDomContent {

    /** The raw unescaped text content. */
    private final String myText;

    /**
     * Constructs an UnescapedText element with the given raw text.
     *
     * @param aText The unescaped text content
     */
    public UnescapedText(final String aText) {
        myText = aText;
    }

    @Override
    public <T extends Appendable> T render(final HtmlBuilder<T> aHtmlBuilder, final Object aModel) throws IOException {
        aHtmlBuilder.appendUnescapedText(String.valueOf(myText));
        return aHtmlBuilder.output();
    }

    @Override
    public boolean equals(final Object aObject) {
        return aObject instanceof UnescapedText other && other.render().equals(render());
    }

    @Override
    public int hashCode() {
        return render().hashCode();
    }
}
