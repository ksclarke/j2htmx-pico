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
 * Utility for escaping special characters in HTML strings.
 */
public final class EscapeUtil {

    /**
     * Constructs a new HTML escape utility.
     */
    private EscapeUtil() {
    }

    /**
     * Escapes HTML special characters in the supplied string.
     *
     * @param aString The string to escape
     * @return The HTML-escaped string, or null if input is null
     */
    public static String escape(final String aString) {
        if (aString == null) {
            return null;
        }
        final StringBuilder escapedText = new StringBuilder(aString.length() + 16);
        char currentChar;
        for (int i = 0; i < aString.length(); i++) {
            currentChar = aString.charAt(i);
            switch (currentChar) {
                case '<':
                    escapedText.append("&lt;");
                    break;
                case '>':
                    escapedText.append("&gt;");
                    break;
                case '&':
                    escapedText.append("&amp;");
                    break;
                case '"':
                    escapedText.append("&quot;");
                    break;
                case '\'':
                    escapedText.append("&#x27;");
                    break;
                default:
                    escapedText.append(currentChar);
            }
        }
        return escapedText.toString();
    }

}
