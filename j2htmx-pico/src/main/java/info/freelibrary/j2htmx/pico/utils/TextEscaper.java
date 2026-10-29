/*
 * This code was derived from j2html, which is distributed under the Apache License, Version 2.0.
 * See META-INF/THIRD-PARTY.txt and META-INF/licenses/Apache-2.0.txt for more information.
 *
 * This file has been modified for j2htmx-pico by Kevin S. Clarke (my.j2htmx-pico@kevinclarke.info)
 *
 * The modifications are licensed under the Mozilla Public License, Version 2.0.
 * See LICENSE.txt in the parent project's root for more information.
 */

package info.freelibrary.j2htmx.pico.utils;

/**
 * Functional interface for escaping HTML text content.
 */
@FunctionalInterface
public interface TextEscaper {

    /**
     * Escapes the specified text string.
     *
     * @param aText The text to escape
     * @return The escaped text string
     */
    String escape(String aText);
}
