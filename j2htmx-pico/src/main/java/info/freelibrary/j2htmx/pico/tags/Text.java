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
 * Represents escaped text content within the DOM.
 */
public class Text extends AbstractDomContent {

    /** The raw text content. */
    private final String myText;

    /**
     * Constructs a Text element with the given text content.
     *
     * @param aText The text content to be escaped when rendered
     */
    public Text(final String aText) {
        myText = aText;
    }

    @Override
    public <T extends Appendable> T render(final HtmlBuilder<T> aHtmlBuilder, final Object aModel) throws IOException {
        aHtmlBuilder.appendEscapedText(String.valueOf(myText));
        return aHtmlBuilder.output();
    }
}
