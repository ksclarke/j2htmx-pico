/*
 * This code was derived from j2html, which is distributed under the Apache License, Version 2.0.
 * See LICENSE-ASL-2.0.txt in the parent project's root directory for more information.
 *
 * This file was modified for j2htmx-pico by Kevin S. Clarke (my.j2htmx-pico@kevinclarke.info).
 *
 * The modifications are licensed under the Mozilla Public License, Version 2.0.
 * See LICENSE-MPL-2.0.txt in the parent project's root directory for more information.
 */

package info.freelibrary.j2htmx.pico.codegen;

import info.freelibrary.util.warnings.Checkstyle;

import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parser for element and attribute definition files.
 */
@SuppressWarnings({Checkstyle.MULTIPLE_STRING_LITERALS, "MultipleStringLiterals"})
public final class Parser {

    /** Regex pattern matching empty or whitespace-only lines. */
    private static final Pattern EMPTY_LINE_PATTERN = Pattern.compile("\\s*");

    /** Regex pattern matching comment lines starting with '#'. */
    private static final Pattern COMMENT_PATTERN = Pattern.compile("#.*");

    /** Regex pattern matching element and attribute node definitions. */
    private static final Pattern NODE_PATTERN = Pattern.compile(
      "(?<type>ELEMENT|EMPTY-ELEMENT|BOOLEAN|ONOFF|STRING)\\[(?<name>\\S+)\\]");

    /** Regex pattern matching element-to-attribute mapping definitions. */
    private static final Pattern ATTRIBUTE_PATTERN = Pattern.compile("ATTRIBUTE\\[(?<element>\\S+):(?<name>\\S+)\\]");

    /**
     * Constructs an instance of Parser.
     */
    private Parser() {
    }

    /**
     * Parses the specified model definition text using the given listener.
     *
     * @param aTxt The model text content
     * @param aListener The listener callback handler
     */
    public static void parse(final String aTxt, final Listener aListener) {
        final String[] lines = aTxt.split("[\r\n]+");

        for (int i = 0; i < lines.length; i++) {
            final int number = i + 1;
            final String line = lines[i];

            if (match(EMPTY_LINE_PATTERN, line)) {
                continue;
            }

            if (match(COMMENT_PATTERN, line, matcher -> {
                aListener.lineCommented(number, line);
            })) {
                continue;
            }

            if (match(NODE_PATTERN, line, matcher -> {
                final String type = matcher.group("type");
                final String name = matcher.group("name");
                switch (type) {
                    case "ELEMENT":
                        aListener.elementDefined(number, name);
                        break;
                    case "EMPTY-ELEMENT":
                        aListener.emptyElementDefined(number, name);
                        break;
                    case "BOOLEAN":
                        aListener.booleanDefined(number, name);
                        break;
                    case "ONOFF":
                        aListener.onOffDefined(number, name);
                        break;
                    case "STRING":
                        aListener.stringDefined(number, name);
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown type: " + type);
                }
            })) {
                continue;
            }

            if (match(ATTRIBUTE_PATTERN, line, matcher -> {
                aListener.attributeDefined(number, matcher.group("element"), matcher.group("name"));
            })) {
                continue;
            }

            aListener.invalidLine(number, line);
        }
    }

    /**
     * Checks if the given text matches the pattern.
     *
     * @param aPattern The regex pattern
     * @param aTxt The text to match
     * @return True if the pattern matches the text, false otherwise
     */
    private static boolean match(final Pattern aPattern, final String aTxt) {
        return aPattern.matcher(aTxt).matches();
    }

    /**
     * Checks if the given text matches the pattern and consumes the matcher on match.
     *
     * @param aPattern The regex pattern
     * @param aTxt The text to match
     * @param anOnMatch Callback invoked with the matcher when matched
     * @return True if the pattern matches the text, false otherwise
     */
    private static boolean match(final Pattern aPattern, final String aTxt, final Consumer<Matcher> anOnMatch) {
        final Matcher matcher = aPattern.matcher(aTxt);
        if (matcher.matches()) {
            anOnMatch.accept(matcher);
            return true;
        }
        return false;
    }

    /**
     * Listener interface to receive callbacks during model parsing.
     */
    public interface Listener {
        /**
         * Called when a line comment has encountered.
         *
         * @param aLine The line number
         * @param aTxt The comment text
         */
        void lineCommented(int aLine, String aTxt);

        /**
         * Called when a container element has defined.
         *
         * @param aLine The line number
         * @param aName The element name
         */
        void elementDefined(int aLine, String aName);

        /**
         * Called when an empty/void element has defined.
         *
         * @param aLine The line number
         * @param aName The element name
         */
        void emptyElementDefined(int aLine, String aName);

        /**
         * Called when a boolean attribute has defined.
         *
         * @param aLine The line number
         * @param aName The attribute name
         */
        void booleanDefined(int aLine, String aName);

        /**
         * Called when an on/off boolean attribute has defined.
         *
         * @param aLine The line number
         * @param aName The attribute name
         */
        void onOffDefined(int aLine, String aName);

        /**
         * Called when a string attribute has defined.
         *
         * @param aLine The line number
         * @param aName The attribute name
         */
        void stringDefined(int aLine, String aName);

        /**
         * Called when an attribute has mapped to an element.
         *
         * @param aLine The line number
         * @param anElement The element name
         * @param aName The attribute name
         */
        void attributeDefined(int aLine, String anElement, String aName);

        /**
         * Called when an invalid line has encountered.
         *
         * @param aLine The line number
         * @param aTxt The line content
         */
        void invalidLine(int aLine, String aTxt);
    }
}
