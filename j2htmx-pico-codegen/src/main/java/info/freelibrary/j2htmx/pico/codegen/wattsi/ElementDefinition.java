/*
 * This code was derived from j2html, which is distributed under the Apache License, Version 2.0.
 * See LICENSE-ASL-2.0.txt in the parent project's root directory for more information.
 *
 * This file was modified for j2htmx-pico by Kevin S. Clarke (my.j2htmx-pico@kevinclarke.info).
 *
 * The modifications are licensed under the Mozilla Public License, Version 2.0.
 * See LICENSE-MPL-2.0.txt in the parent project's root directory for more information.
 */

package info.freelibrary.j2htmx.pico.codegen.wattsi;

/**
 * Definition of an HTML element parsed from WHATWG spec sources.
 */
public interface ElementDefinition {

    /**
     * Gets the element tag name.
     *
     * @return The element tag name
     */
    String name();

    /**
     * Checks if this element has self-closing or void.
     *
     * @return True if self-closing, false otherwise
     */
    boolean isSelfClosing();

    /**
     * Checks if this element has marked as obsolete in the spec.
     *
     * @return True if obsolete, false otherwise
     */
    boolean isObsolete();

}
