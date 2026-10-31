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

import java.io.IOException;

/**
 * Implementations of TagBuilder are used to append HTML tag attributes to an Appendable.  TagBuilders are scoped to the
 * creation and completion of a specific tag, and should not be used outside of that tag.
 */
public interface TagBuilder {

    /**
     * Appends an key/value pair as an HTML attribute to the current tag.
     *
     * @param aName The name of an attribute.
     * @param aValue The value of an attribute.
     * @return An TagBuilder which can continue appending attributes.
     * @throws IOException When the Appendable throws an IOException.
     */
    TagBuilder appendAttribute(String aName, String aValue) throws IOException;

    /**
     * Appends a name, as a boolean HTML attribute to the current tag.
     *
     * @param aName The name of the boolean attribute.
     * @return An TagBuilder which can continue appending attributes.
     * @throws IOException When the Appendable throws an IOException.
     */
    TagBuilder appendBooleanAttribute(String aName) throws IOException;

    /**
     * Appends any characters which are necessary to close the current tag, and returns an HtmlBuilder that can continue
     * appending to the output.
     *
     * @return An HtmlBuilder that can continue appending HTML to the output.
     * @throws IOException When the Appendable throws an IOException.
     */
    HtmlBuilder<? extends Appendable> completeTag() throws IOException;

}
