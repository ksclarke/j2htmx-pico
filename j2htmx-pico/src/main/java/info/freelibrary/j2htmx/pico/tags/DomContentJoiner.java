package info.freelibrary.j2htmx.pico.tags;

import info.freelibrary.util.warnings.PMD;

/**
 * Utility for joining strings and {@link DomContent} objects with a delimiter.
 */
public final class DomContentJoiner {

    /**
     * Constructs a new DOM content joiner utility.
     */
    private DomContentJoiner() {
    }

    /**
     * Joins strings and {@link DomContent} elements separated by a delimiter.
     *
     * @param delimiter The character sequence to insert between elements
     * @param fixPeriodAndCommaSpacing Whether to clean up spaces before periods and commas
     * @param stringOrDomObjects The objects to join (can be String or DomContent)
     * @return An UnescapedText instance containing the joined content
     * @throws RuntimeException If an object is not a String or DomContent
     */
    @SuppressWarnings({PMD.AVOID_THROWING_RAW_EXCEPTION_TYPES})
    public static UnescapedText join(final CharSequence delimiter, final boolean fixPeriodAndCommaSpacing,
      final Object... stringOrDomObjects) {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < stringOrDomObjects.length; i++) {
            final Object o = stringOrDomObjects[i];
            if (o instanceof String) {
                sb.append(((String) o).trim());
            } else if (o instanceof DomContent) {
                sb.append(((DomContent) o).render().trim());
            } else if (o == null) {
                //Discard null objects so iff/iffelse can be used with join
                continue;
            } else {
                throw new RuntimeException("You can only join DomContent and String objects");
            }
            if (i < stringOrDomObjects.length - 1) {
                sb.append(delimiter);
            }
        }
        String joined = sb.toString().trim();
        if (fixPeriodAndCommaSpacing) {
            joined = joined.replaceAll("\\s\\.", ".");
            joined = joined.replaceAll("\\s\\,", ",");
        }
        return new UnescapedText(joined);
    }

}
