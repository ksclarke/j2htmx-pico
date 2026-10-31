/*
 * This code was derived from j2html, which is distributed under the Apache License, Version 2.0.
 * See LICENSE-ASL-2.0.txt in the parent project's root directory for more information.
 *
 * This file was modified for j2htmx-pico by Kevin S. Clarke (my.j2htmx-pico@kevinclarke.info).
 *
 * The modifications are licensed under the Mozilla Public License, Version 2.0.
 * See LICENSE-MPL-2.0.txt in the parent project's root directory for more information.
 */

package info.freelibrary.j2htmx.pico.utils;

/**
 * Functional interface for indenting HTML text according to nesting level.
 */
@FunctionalInterface
public interface Indenter {

    /**
     * Indents the specified text to the given indentation level.
     *
     * @param aLevel The nesting depth level
     * @param aText The text to indent
     * @return The indented string
     */
    String indent(int aLevel, String aText);
}
