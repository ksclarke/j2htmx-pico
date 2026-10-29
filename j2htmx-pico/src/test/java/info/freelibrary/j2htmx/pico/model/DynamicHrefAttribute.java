/*
 * This code was derived from j2html, which is distributed under the Apache License, Version 2.0.
 * See META-INF/THIRD-PARTY.txt and META-INF/licenses/Apache-2.0.txt for more information.
 *
 * This file has been modified for j2htmx-pico by Kevin S. Clarke (my.j2htmx-pico@kevinclarke.info)
 *
 * The modifications are licensed under the Mozilla Public License, Version 2.0.
 * See LICENSE.txt in the parent project's root for more information.
 */

package info.freelibrary.j2htmx.pico.model;

import info.freelibrary.j2htmx.pico.attributes.Attribute;
import info.freelibrary.j2htmx.pico.rendering.TagBuilder;

import java.io.IOException;

/**
 * Dynamic href attribute implementation for testing.
 */
public class DynamicHrefAttribute extends Attribute {

    /**
     * Constructs a DynamicHrefAttribute instance.
     */
    public DynamicHrefAttribute() {
        super("href");
    }

    @Override
    public void render(final TagBuilder aWriter, final Object aModel) throws IOException {
        aWriter.appendAttribute(getName(), getUrl(aModel));
    }

    /**
     * Resolves the URL for the given model.
     *
     * @param aModel The model object
     * @return The URL string
     */
    public String getUrl(final Object aModel) {
        return "/";
    }
}
