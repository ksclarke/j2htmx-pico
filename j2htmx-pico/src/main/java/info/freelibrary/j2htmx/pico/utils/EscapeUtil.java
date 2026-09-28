package info.freelibrary.j2htmx.pico.utils;

/**
 * Utility for escaping special characters in HTML strings.
 */
public final class EscapeUtil {

    private EscapeUtil() {
    }

    /**
     * Escapes HTML special characters in the supplied string.
     *
     * @param s The string to escape
     * @return The HTML-escaped string, or null if input is null
     */
    public static String escape(final String s) {
        if (s == null) {
            return null;
        }
        final StringBuilder escapedText = new StringBuilder(s.length() + 16);
        char currentChar;
        for (int i = 0; i < s.length(); i++) {
            currentChar = s.charAt(i);
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
