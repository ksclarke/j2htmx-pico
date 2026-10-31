/*
 * This code was derived from j2html, which is distributed under the Apache License, Version 2.0.
 * See LICENSE-ASL-2.0.txt in the parent project's root directory for more information.
 *
 * This file was modified for j2htmx-pico by Kevin S. Clarke (my.j2htmx-pico@kevinclarke.info).
 *
 * The modifications are licensed under the Mozilla Public License, Version 2.0.
 * See LICENSE-MPL-2.0.txt in the parent project's root directory for more information.
 */

package info.freelibrary.j2htmx.pico.model;

import info.freelibrary.j2htmx.pico.rendering.HtmlBuilder;
import info.freelibrary.j2htmx.pico.tags.AbstractDomContent;

import java.io.IOException;

/**
 * Abstract template representation for testing.
 *
 * @param <T> The model type
 */
public abstract class Template<T> extends AbstractDomContent {

    /**
     * Renders the template with the provided model.
     *
     * @param <U> The appendable type
     * @param aWriter The html builder writer
     * @param aModel The model object
     * @throws IOException If an I/O error occurs
     */
    public abstract <U extends Appendable> void renderTemplate(HtmlBuilder<U> aWriter, T aModel)
      throws IOException;

    @Override
    @SuppressWarnings("unchecked")
    public final <U extends Appendable> U render(final HtmlBuilder<U> aHtmlBuilder, final Object aModel)
    throws IOException {
        renderTemplate(aHtmlBuilder, (T) aModel);
        return aHtmlBuilder.output();
    }
}
