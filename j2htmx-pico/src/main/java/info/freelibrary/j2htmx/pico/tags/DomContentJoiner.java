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

import info.freelibrary.util.warnings.PMD;

/**
 * Utility for joining strings and {@link AbstractDomContent} objects with a delimiter.
 */
public final class DomContentJoiner {

    /**
     * Constructs a new DOM content joiner utility.
     */
    private DomContentJoiner() {
    }

    /**
     * Joins strings and {@link AbstractDomContent} elements separated by a delimiter.
     *
     * @param aDelimiter The character sequence to insert between elements
     * @param aFixPeriodAndCommaSpacing Whether to clean up spaces before periods and commas
     * @param aStringOrDomObjects The objects to join (can be String or AbstractDomContent)
     * @return An UnescapedText instance containing the joined content
     * @throws RuntimeException If an object is not a String or AbstractDomContent
     */
    @SuppressWarnings({PMD.AVOID_THROWING_RAW_EXCEPTION_TYPES})
    public static UnescapedText join(final CharSequence aDelimiter, final boolean aFixPeriodAndCommaSpacing,
      final Object... aStringOrDomObjects) {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < aStringOrDomObjects.length; i++) {
            final Object o = aStringOrDomObjects[i];
            if (o instanceof String) {
                sb.append(((String) o).trim());
            } else if (o instanceof AbstractDomContent) {
                sb.append(((AbstractDomContent) o).render().trim());
            } else if (o == null) {
                //Discard null objects so iff/iffelse can be used with join
                continue;
            } else {
                throw new RuntimeException("You can only join AbstractDomContent and String objects");
            }
            if (i < aStringOrDomObjects.length - 1) {
                sb.append(aDelimiter);
            }
        }
        String joined = sb.toString().trim();
        if (aFixPeriodAndCommaSpacing) {
            joined = joined.replaceAll("\\s\\.", ".");
            joined = joined.replaceAll("\\s\\,", ",");
        }
        return new UnescapedText(joined);
    }

}
