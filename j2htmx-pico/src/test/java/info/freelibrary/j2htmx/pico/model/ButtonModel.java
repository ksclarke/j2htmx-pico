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

/**
 * Button model representation for testing.
 */
public class ButtonModel {

    /** The button text. */
    private final String myText;

    /**
     * Constructs a ButtonModel with the given text.
     *
     * @param aText The button text
     */
    public ButtonModel(final String aText) {
        this.myText = aText;
    }

    /**
     * Gets the button text.
     *
     * @return The text
     */
    public String getText() {
        return myText;
    }
}
