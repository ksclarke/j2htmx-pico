package info.freelibrary.j2htmx.pico.codegen;

import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parser for element and attribute definition files.
 */
public class Parser {

    private static final Pattern EMPTY_LINE_PATTERN = Pattern.compile("\\s*");
    private static final Pattern COMMENT_PATTERN = Pattern.compile("#.*");
    private static final Pattern NODE_PATTERN = Pattern.compile("(?<type>ELEMENT|EMPTY-ELEMENT|BOOLEAN|ONOFF|STRING)\\[(?<name>\\S+)\\]");
    private static final Pattern ATTRIBUTE_PATTERN = Pattern.compile("ATTRIBUTE\\[(?<element>\\S+):(?<name>\\S+)\\]");

    private Parser() {
    }

    /**
     * Listener interface to receive callbacks during model parsing.
     */
    public interface Listener {
        /**
         * Called when a line comment is encountered.
         *
         * @param line The line number
         * @param txt The comment text
         */
        void lineCommented(int line, String txt);

        /**
         * Called when a container element is defined.
         *
         * @param line The line number
         * @param name The element name
         */
        void elementDefined(int line, String name);

        /**
         * Called when an empty/void element is defined.
         *
         * @param line The line number
         * @param name The element name
         */
        void emptyElementDefined(int line, String name);

        /**
         * Called when a boolean attribute is defined.
         *
         * @param line The line number
         * @param name The attribute name
         */
        void booleanDefined(int line, String name);

        /**
         * Called when an on/off boolean attribute is defined.
         *
         * @param line The line number
         * @param name The attribute name
         */
        void onOffDefined(int line, String name);

        /**
         * Called when a string attribute is defined.
         *
         * @param line The line number
         * @param name The attribute name
         */
        void stringDefined(int line, String name);

        /**
         * Called when an attribute is mapped to an element.
         *
         * @param line The line number
         * @param element The element name
         * @param name The attribute name
         */
        void attributeDefined(int line, String element, String name);

        /**
         * Called when an invalid line is encountered.
         *
         * @param line The line number
         * @param txt The line content
         */
        void invalidLine(int line, String txt);
    }

    /**
     * Parses the specified model definition text using the given listener.
     *
     * @param txt The model text content
     * @param listener The listener callback handler
     */
    public static void parse(final String txt, final Listener listener) {
        final String[] lines = txt.split("[\r\n]+");

        for (int i = 0; i < lines.length; i++) {
            final int number = i + 1;
            final String line = lines[i];

            if (match(EMPTY_LINE_PATTERN, line)) continue;

            if (match(COMMENT_PATTERN, line, matcher -> {
                listener.lineCommented(number, line);
            })) continue;

            if (match(NODE_PATTERN, line, matcher -> {
                final String type = matcher.group("type");
                final String name = matcher.group("name");
                switch (type) {
                    case "ELEMENT":
                        listener.elementDefined(number, name);
                        break;
                    case "EMPTY-ELEMENT":
                        listener.emptyElementDefined(number, name);
                        break;
                    case "BOOLEAN":
                        listener.booleanDefined(number, name);
                        break;
                    case "ONOFF":
                        listener.onOffDefined(number, name);
                        break;
                    case "STRING":
                        listener.stringDefined(number, name);
                        break;
                }
            })) continue;

            if (match(ATTRIBUTE_PATTERN, line, matcher -> {
                listener.attributeDefined(
                    number,
                    matcher.group("element"),
                    matcher.group("name")
                );
            })) continue;

            listener.invalidLine(number, line);
        }
    }

    private static boolean match(final Pattern pattern, final String txt) {
        return pattern.matcher(txt).matches();
    }

    private static boolean match(final Pattern pattern, final String txt, final Consumer<Matcher> onMatch) {
        final Matcher matcher = pattern.matcher(txt);
        if (matcher.matches()) {
            onMatch.accept(matcher);
            return true;
        }
        return false;
    }
}
