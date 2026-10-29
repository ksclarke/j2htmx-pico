/*
 * This code was derived from j2html, which is distributed under the Apache License, Version 2.0.
 * See META-INF/THIRD-PARTY.txt and META-INF/licenses/Apache-2.0.txt for more information.
 *
 * This file has been modified for j2htmx-pico by Kevin S. Clarke (my.j2htmx-pico@kevinclarke.info)
 *
 * The modifications are licensed under the Mozilla Public License, Version 2.0.
 * See LICENSE.txt in the parent project's root for more information.
 */

package info.freelibrary.j2htmx.pico.tags;

import info.freelibrary.j2htmx.pico.attributes.Attribute;
import info.freelibrary.j2htmx.pico.rendering.HtmlBuilder;
import info.freelibrary.j2htmx.pico.rendering.TagBuilder;
import info.freelibrary.util.warnings.PMD;

import java.io.IOException;

/**
 * Represents a self-closing or void HTML element (such as {@code <img />}, {@code <br />}, or {@code <input />}) that
 * cannot have child nodes.
 *
 * @param <T> The concrete empty tag type
 */
@SuppressWarnings(PMD.AVOID_LITERALS_IN_IF_CONDITION)
public class EmptyTag<T extends EmptyTag<T>> extends AbstractTag<T> {

    /**
     * Constructs an EmptyTag with the given tag name.
     *
     * @param aTagName The HTML tag name
     * @throws IllegalArgumentException If the tag name is null or empty
     */
    public EmptyTag(final String aTagName) {
        super(aTagName);
        if (aTagName == null) {
            throw new IllegalArgumentException("Illegal tag name: null");
        }
        if ("".equals(aTagName)) {
            throw new IllegalArgumentException("Illegal tag name: \"\"");
        }
    }

    @Override
    public <A extends Appendable> A render(final HtmlBuilder<A> aHtmlBuilder, final Object aModel) throws IOException {
        final TagBuilder attrs = aHtmlBuilder.appendEmptyTag(getTagName());
        for (final Attribute attr : getAttributes()) {
            attr.render(attrs, aModel);
        }
        attrs.completeTag();
        return aHtmlBuilder.output();
    }

}
