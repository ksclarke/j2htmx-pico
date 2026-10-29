/*
 * This code was derived from j2html, which is distributed under the Apache License, Version 2.0.
 * See META-INF/THIRD-PARTY.txt and META-INF/licenses/Apache-2.0.txt for more information.
 *
 * This file has been modified for j2htmx-pico by Kevin S. Clarke (my.j2htmx-pico@kevinclarke.info)
 *
 * The modifications are licensed under the Mozilla Public License, Version 2.0.
 * See LICENSE.txt in the parent project's root for more information.
 */

package info.freelibrary.j2htmx.pico.codegen.wattsi;

/**
 * Definition of an HTML attribute parsed from WHATWG spec sources.
 */
public interface AttributeDefinition {

    /**
     * Gets the attribute name.
     *
     * @return The attribute name
     */
    String name();

    /**
     * Checks if this attribute applies to the specified element definition.
     *
     * @param anElement The element definition
     * @return True if applicable, false otherwise
     */
    boolean appliesTo(ElementDefinition anElement);

    /**
     * Checks if this attribute has marked as obsolete in the spec.
     *
     * @return True if obsolete, false otherwise
     */
    boolean isObsolete();
}
