/*
 * * CSSMin Copyright License Agreement (BSD License)
 * <p>
 * Copyright (c) 2011-2014, Barry van Oudtshoorn
 * All rights reserved.
 * <p>
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * <p>
 * - Redistributions of source code must retain the above
 * copyright notice, this list of conditions and the
 * following disclaimer.
 * <p>
 * - Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the
 * following disclaimer in the documentation and/or other
 * materials provided with the distribution.
 * <p>
 * - Neither the name of Barryvan nor the names of its
 * contributors may be used to endorse or promote products
 * derived from this software without specific prior
 * written permission.
 * <p>
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * <p>
 * CSSMin takes in well-formed, human-readable CSS and reduces its size substantially.
 * It removes unnecessary whitespace and comments, and orders the contents of CSS
 * selectors alphabetically to enhance GZIP compression.
 * <p>
 * Originally by Barry van Oudtshoorn, with bug reports, fixes, and contributions by
 * <ul>
 * <li>Kevin de Groote</li>
 * <li>Pedro Pinheiro</li>
 * <li>Asier Lostalé</li>
 * <li>David Åse</li>
 * </ul>
 * Some code is based on the YUI CssCompressor code, by Julien Lecomte.
 *
 * @author Barry van Oudtshoorn
 * Repo: https://github.com/barryvan/CSSMin
 */

package info.freelibrary.j2htmx.pico.utils;

import static info.freelibrary.util.Constants.COMMA;
import static info.freelibrary.util.Constants.EMPTY;
import static info.freelibrary.util.Constants.HASH;
import static info.freelibrary.util.Constants.SPACE;

import info.freelibrary.util.Logger;
import info.freelibrary.util.LoggerFactory;
import info.freelibrary.util.MessageCodes;
import info.freelibrary.util.warnings.PMD;

import java.io.BufferedReader;
import java.io.Serial;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * CSS minifier that removes unnecessary whitespace and comments, and compresses CSS.
 */
@SuppressWarnings(PMD.AVOID_LITERALS_IN_IF_CONDITION)
public final class CSSMin {

    /** Logger instance for CSSMin. */
    private static final Logger LOGGER = LoggerFactory.getLogger(CSSMin.class, MessageCodes.BUNDLE);

    /**
     * Constructs a new CSSMin utility.
     */
    private CSSMin() {
        // This is intentionally left empty
    }

    /**
     * Minify supplied CSS.
     *
     * @param aInput the CSS
     * @return the compressed version
     */
    @SuppressWarnings({PMD.COGNITIVE_COMPLEXITY, PMD.CYCLOMATIC_COMPLEXITY, PMD.EXCEPTION_AS_FLOW_CONTROL})
    public static String compressCss(final String aInput) {
        try (BufferedReader reader = new BufferedReader(new StringReader(aInput))) {
            final List<Selector> selectors = new ArrayList<>();
            final StringBuilder builder = new StringBuilder();

            int k; // Number of open braces
            int n; // Current position in stream
            String string;

            LOGGER.debug("Reading file into StringBuffer...");

            while ((string = reader.readLine()) != null) {
                if (EMPTY.equals(string.trim())) {
                    continue;
                }

                builder.append(string);
            }

            LOGGER.debug("Removing comments...");

            // Find the start of the comment
            n = 0;

            while ((n = builder.indexOf("/*", n)) != -1) {
                if (builder.charAt(n + 2) == '*') { // Retain special comments
                    n += 2;
                    continue;
                }

                k = builder.indexOf("*/", n + 2);

                if (k == -1) {
                    throw new UnterminatedCommentException();
                }

                builder.delete(n, k + 2);
            }

            LOGGER.debug(builder.toString() + "\n\n");
            LOGGER.debug("Parsing and processing selectors...");

            char current;
            int j = 0;
            int next = 0;

            for (int index = 0; index < builder.length(); index++) {
                current = builder.charAt(index);

                if (j < 0) {
                    throw new UnbalancedBracesException();
                }

                if (current == '{') {
                    j++;
                } else if (current == '}') {
                    j--;

                    if (j == 0) {
                        try {
                            selectors.add(new Selector(builder.substring(next, index + 1)));
                        } catch (UnterminatedSelectorException usex) {
                            LOGGER.warn("Unterminated selector: " + usex.getMessage());
                        } catch (EmptySelectorBodyException ebex) {
                            LOGGER.debug("Empty selector body: " + ebex.getMessage());
                        }

                        next = index + 1;
                    }
                }
            }

            final StringBuilder result = new StringBuilder();

            for (final Selector selector : selectors) {
                result.append(selector);
            }

            LOGGER.debug("Process completed successfully.");

            return result.toString();
        } catch (UnterminatedCommentException ucDetails) {
            LOGGER.warn("Unterminated comment.");
        } catch (UnbalancedBracesException ubDetails) {
            LOGGER.warn("Unbalanced braces.");
        } catch (@SuppressWarnings(PMD.AVOID_CATCHING_GENERIC_EXCEPTION) Exception details) {
            LOGGER.warn(details.getMessage());
        }

        return null;
    }
}

/**
 * Represents a CSS selector and its associated style properties.
 */
@SuppressWarnings({PMD.AVOID_LITERALS_IN_IF_CONDITION})
class Selector {

    /** Logger instance for Selector. */
    private static final Logger LOGGER = LoggerFactory.getLogger(Selector.class.getName());

    /** Array of CSS properties belonging to this selector. */
    private Property[] myProperties;

    /** Nested sub-selectors. */
    private List<Selector> mySubSelectors;

    /** The selector text. */
    private String mySelector;

    /**
     * Creates a new Selector using the supplied strings.
     *
     * @param selector The selector; for example, "div { border: solid 1px red; color: blue; }"
     * @throws IncompleteSelectorException If the selector is incomplete and cannot be parsed
     * @throws UnterminatedSelectorException If the selector is not closed with a brace
     * @throws EmptySelectorBodyException If the selector has an empty body
     */
    @SuppressWarnings({PMD.COGNITIVE_COMPLEXITY, PMD.CYCLOMATIC_COMPLEXITY})
    Selector(final String selector)
    throws IncompleteSelectorException, UnterminatedSelectorException, EmptySelectorBodyException {
        String[] parts = selector.split("\\{"); // Escape the { with a \ for the regex, which itself requires escaping

        if (parts.length < 2) {
            throw new IncompleteSelectorException(selector);
        }

        mySelector = parts[0].trim();

        // Simplify combinators
        mySelector = mySelector.replaceAll("\\s?(\\+|~|,|=|~=|\\^=|\\$=|\\*=|\\|=|>)\\s?", "$1");

        // We're dealing with a nested property, eg @-webkit-keyframes
        if (parts.length > 2) {
            mySubSelectors = new ArrayList<>();
            parts = selector.split("(\\s*\\{\\s*)|(\\s*\\}\\s*)");

            for (int index = 1; index < parts.length; index += 2) {
                parts[index] = parts[index].trim();
                parts[index + 1] = parts[index + 1].trim();

                if (!(EMPTY.equals(parts[index]) || EMPTY.equals(parts[index + 1]))) {
                    mySubSelectors.add(new Selector(parts[index] + "{" + parts[index + 1] + "}"));
                }
            }
        } else {
            String contents = parts[parts.length - 1].trim();

            LOGGER.debug("Parsing selector: " + mySelector);
            LOGGER.debug("\t" + contents);

            if (contents.charAt(contents.length() - 1) != '}') { // Ensure that we have a leading and trailing brace.
                throw new UnterminatedSelectorException(selector);
            }

            if (contents.length() == 1) {
                throw new EmptySelectorBodyException(selector);
            }

            final int endIndex = contents.endsWith(";}") ? 2 : 1;
            contents = contents.substring(0, contents.length() - endIndex);

            myProperties = new Property[0];
            myProperties = parseProperties(contents).toArray(myProperties);
            sortProperties(myProperties);
        }
    }

    /**
     * Prints out this selector and its contents nicely, with the contents sorted alphabetically.
     *
     * @return A string representing this selector, minified.
     */
    @SuppressWarnings({PMD.MISSING_OVERRIDE})
    public String toString() {
        final StringBuilder builder = new StringBuilder().append(this.mySelector).append('{');

        if (this.mySubSelectors != null) {
            for (final Selector selector : this.mySubSelectors) {
                builder.append(selector.toString());
            }
        }

        if (this.myProperties != null) {
            for (final Property property : this.myProperties) {
                builder.append(property.toString());
            }
        }

        if (builder.charAt(builder.length() - 1) == ';') {
            builder.deleteCharAt(builder.length() - 1);
        }

        builder.append('}');
        return builder.toString();
    }

    /**
     * Parses out the properties of a selector's body.
     *
     * @param aContents The body; for example, "border: solid 1px red; color: blue;"
     * @return An array of properties parsed from this selector.
     */
    @SuppressWarnings({PMD.COGNITIVE_COMPLEXITY, PMD.CYCLOMATIC_COMPLEXITY})
    private ArrayList<Property> parseProperties(final String aContents) {
        final ArrayList<String> parts = new ArrayList<>();

        boolean bInsideString = false;
        boolean bInsideURL = false;
        int j = 0;
        String substr;

        for (int index = 0; index < aContents.length(); index++) {
            if (bInsideString) { // If we're inside a string
                bInsideString = aContents.charAt(index) != '"';
            } else if (bInsideURL) { // If we're inside a URL
                bInsideURL = aContents.charAt(index) != ')';
            } else if (aContents.charAt(index) == '"') {
                bInsideString = true;
            } else if (aContents.charAt(index) == '(') {
                if ((index - 3) > 0 && "url".equals(aContents.substring(index - 3, index))) {
                    bInsideURL = true;
                }
            } else if (aContents.charAt(index) == ';') {
                substr = aContents.substring(j, index);

                if (!EMPTY.equals(substr.trim())) {
                    parts.add(substr);
                }

                j = index + 1;
            }
        }

        substr = aContents.substring(j, aContents.length());

        if (!EMPTY.equals(substr.trim())) {
            parts.add(substr);
        }

        final ArrayList<Property> results = new ArrayList<>();
        for (final String part : parts) {
            try {
                results.add(new Property(part));
            } catch (IncompletePropertyException ipex) {
                LOGGER.warn("Incomplete property in selector \"" + mySelector + "\": \"" + ipex.getMessage() + "\"");
            }
        }

        return results;
    }

    /**
     * Sorts the properties array to enhance gzip'ing.
     *
     * @param properties The array to be sorted
     */
    private void sortProperties(final Property... properties) {
        Arrays.sort(properties);
    }
}

/**
 * Represents a single CSS property and its value parts.
 */
@SuppressWarnings(PMD.AVOID_LITERALS_IN_IF_CONDITION)
class Property implements Comparable<Property> {

    /** Logger instance for Property. */
    private static final Logger LOGGER = LoggerFactory.getLogger(Property.class, MessageCodes.BUNDLE);

    /** The property name. */
    private final String myProperty;

    /** The parts comprising the property value. */
    private final Part[] myParts;

    /**
     * Creates a new Property using the supplied strings. Parses out the values of the property selector.
     *
     * @param aProperty The property; for example, "border: solid 1px red;" or "-moz-box-shadow: 3px 3px 3px rgba(255,
     *   255, 0, 0.5);"
     * @throws IncompletePropertyException If the property is incomplete and cannot be parsed
     */
    @SuppressWarnings({PMD.COGNITIVE_COMPLEXITY, PMD.CYCLOMATIC_COMPLEXITY, PMD.AVOID_THROWING_RAW_EXCEPTION_TYPES})
    Property(final String aProperty) throws IncompletePropertyException {
        try {
            final ArrayList<String> parts = new ArrayList<>();

            boolean bCanSplit = true;
            int j = 0;
            String substr;

            LOGGER.debug("\t\tExamining property: " + aProperty);

            for (int index = 0; index < aProperty.length(); index++) {
                if (!bCanSplit) { // If we're inside a string
                    bCanSplit = aProperty.charAt(index) == '"';
                } else if (aProperty.charAt(index) == '"') {
                    bCanSplit = false;
                } else if (aProperty.charAt(index) == ':' && parts.isEmpty()) {
                    substr = aProperty.substring(j, index);

                    if (!EMPTY.equals(substr.trim())) {
                        parts.add(substr);
                    }

                    j = index + 1;
                }
            }

            substr = aProperty.substring(j, aProperty.length());

            if (!EMPTY.equals(substr.trim())) {
                parts.add(substr);
            }

            if (parts.size() < 2) {
                throw new IncompletePropertyException(aProperty);
            }

            String property = parts.getFirst().trim();

            if (!(property.length() > 2 && "--".equals(property.substring(0, 2)))) {
                property = property.toLowerCase(Locale.US);
            }

            myProperty = property;
            myParts = parseValues(simplifyColours(parts.get(1).trim().replace(", ", COMMA)));
        } catch (PatternSyntaxException details) {
            throw new RuntimeException(details);
        }
    }

    /**
     * Prints out this property nicely.
     *
     * @return A string representing this property, minified.
     */
    @SuppressWarnings({PMD.MISSING_OVERRIDE})
    public String toString() {
        final StringBuilder builder = new StringBuilder().append(myProperty).append(':');

        for (final Part part : myParts) {
            builder.append(part.toString()).append(',');
        }

        builder.deleteCharAt(builder.length() - 1); // Delete the trailing comma.
        builder.append(';');

        LOGGER.debug(builder.toString());

        return builder.toString();
    }

    /**
     * Compare this property with another.
     *
     * @param aProperty The other Property to compare to
     * @return A negative integer, zero, or a positive integer as this property is less than, equal to, or greater than
     *   the specified property
     */
    @SuppressWarnings({PMD.MISSING_OVERRIDE})
    public int compareTo(final Property aProperty) {
        // We can't just use String.compareTo(), because we need to sort properties that have hack prefixes last --
        // eg, *display should come after display.
        return sort(myProperty).compareTo(sort(aProperty.myProperty));
    }

    /**
     * Compares this property with another object by its minified declaration.
     *
     * @param aObject The object to compare
     * @return true if both properties have the same minified declaration
     */
    @Override
    public boolean equals(final Object aObject) {
        if (this == aObject) {
            return true;
        }

        if (aObject == null || getClass() != aObject.getClass()) {
            return false;
        }

        final Property property = (Property) aObject;
        return toString().equals(property.toString());
    }

    /**
     * Returns a hash code based on this property's minified declaration.
     *
     * @return The property's hash code
     */
    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    /**
     * Normalizes a property name for sorting purposes.
     *
     * @param aProperty The property name to normalize
     * @return The normalized property name for sorting
     */
    private String sort(final String aProperty) {
        String property = aProperty;

        if (property.charAt(0) == '-') {
            property = property.substring(1);
            property = property.substring(property.indexOf('-') + 1);
        } else if (property.charAt(0) < 65) {
            property = property.substring(1);
        }

        return property;
    }

    /**
     * Parse the values out of a property.
     *
     * @param aContents The contents to parse
     * @return An array of successfully parsed Parts
     */
    private Part[] parseValues(final String aContents) {
        final String[] parts = aContents.split(COMMA);
        final List<Part> results = new ArrayList<>(parts.length);

        for (final String part : parts) {
            try {
                results.add(new Part(part, myProperty));
            } catch (@SuppressWarnings(PMD.AVOID_CATCHING_GENERIC_EXCEPTION) final Exception details) {
                LOGGER.warn(details.getMessage());
            }
        }

        return results.toArray(Part[]::new);
    }

    /**
     * Simplifies color representations in property contents.
     *
     * @param aContents The property contents to simplify
     * @return The simplified contents
     */
    private String simplifyColours(final String aContents) {
        // This replacement, although it results in a smaller uncompressed file,
        // actually makes the gzipped file bigger -- people tend to use rgba(0,0,0,0.x)
        // quite a lot, which means that rgba(0,0,0,0) has its first eight or so characters
        // compressed really efficiently; much more so than "transparent".
        //contents = contents.replaceAll("rgba\\(0,0,0,0\\)", "transparent");

        return simplifyRGBColours(aContents);
    }

    /**
     * Converts rgb(r,g,b) color formats to hex #rrggbb notation.
     *
     * @param aContents The property contents to simplify
     * @return The simplified contents with RGB colors converted to hex
     */
    private String simplifyRGBColours(final String aContents) {
        final StringBuffer newContents = new StringBuffer();

        StringBuffer hexColour;
        String[] rgbColours;
        int colourValue;

        final Pattern pattern = Pattern.compile("rgb\\s*\\(\\s*([0-9,\\s]+)\\s*\\)");
        final Matcher matcher = pattern.matcher(aContents);

        while (matcher.find()) {
            hexColour = new StringBuffer(HASH);
            rgbColours = matcher.group(1).split(COMMA);

            for (final String rgbColour : rgbColours) {
                colourValue = Integer.parseInt(rgbColour);
                if (colourValue < 16) {
                    hexColour.append('0');
                }
                hexColour.append(Integer.toHexString(colourValue));
            }

            matcher.appendReplacement(newContents, hexColour.toString());
        }

        matcher.appendTail(newContents);

        return newContents.toString();
    }
}

/**
 * Represents an individual part/token within a CSS property value.
 */
@SuppressWarnings(PMD.AVOID_LITERALS_IN_IF_CONDITION)
class Part {

    /** A regular expression for matching strings. */
    private static final String STRING_REGEX = "('|\")?(.*?)\\1";

    /** The property name to which this part belongs. */
    private final String myProperty;

    /** The text content of this part. */
    private String myContents;

    /**
     * Create a new property by parsing the given string.
     *
     * @param aContents The string to parse
     * @param aProperty The property name to which this part belongs
     */
    Part(final String aContents, final String aProperty) {
        myContents = SPACE + aContents;
        myProperty = aProperty;

        simplify();
    }

    /**
     * Simplifies this property part by applying whitespace, unit, and color optimizations.
     */
    private void simplify() {

        // !important doesn't need to be spaced
        myContents = myContents.replaceAll(" !important", "!important");

        // Replace 0in, 0cm, etc. with just 0
        myContents = myContents.replaceAll("(\\s)(0)(px|em|%|in|cm|mm|pc|pt|ex)", "$1$2");

        myContents = myContents.trim();

        // Simplify multiple zeroes
        if ("0 0".equals(myContents) || "0 0 0".equals(myContents) || "0 0 0 0".equals(myContents)) {
            myContents = "0";
        }

        // Simplify multiple-parameter properties
        simplifyParameters();

        // Simplify font weights
        simplifyFontWeights();

        // Strip unnecessary quotes from url() and single-word parts, and make as much lowercase as possible.
        simplifyQuotesAndCaps();

        // Simplify colours
        simplifyColourNames();
        simplifyHexColours();
    }

    /**
     * Simplifies multiple parameters (e.g. margin/padding values).
     */
    private void simplifyParameters() {
        if ("background-size".equals(myProperty)) {
            return;
        }

        final StringBuilder newContents = new StringBuilder();

        String[] params = myContents.split(SPACE);

        if (params.length == 4 && params[1].equalsIgnoreCase(params[3])) {
            // We can drop off the fourth item if the second and fourth items match
            // ie turn 3px 0 3px 0 into 3px 0 3px
            params = Arrays.copyOf(params, 3);
        }
        if (params.length == 3 && params[0].equalsIgnoreCase(params[2])) {
            // We can drop off the third item if the first and third items match (i.e., turn 3px 0 3px into 3px 0)
            params = Arrays.copyOf(params, 2);
        }
        if (params.length == 2 && params[0].equalsIgnoreCase(params[1])) {
            // We can drop off the second item if the first and second items match (i.e., turn 3px 3px into 3px)
            params = Arrays.copyOf(params, 1);
        }

        for (final String param : params) {
            newContents.append(param).append(' ');
        }

        newContents.deleteCharAt(newContents.length() - 1); // Delete the trailing space
        myContents = newContents.toString();
    }

    /**
     * Simplifies font weight names to numerical values where shorter.
     */
    private void simplifyFontWeights() {
        if (!"font-weight".equals(this.myProperty)) {
            return;
        }

        final String lcContents = this.myContents.toLowerCase(Locale.US);

        for (int i = 0; i < Constants.FONT_WEIGHT_NAMES.length; i++) {
            if (lcContents.equals(Constants.FONT_WEIGHT_NAMES[i])) {
                this.myContents = Constants.FONT_WEIGHT_VALUES[i];
                break;
            }
        }
    }

    /**
     * Strips unnecessary quotes from URLs and converts keywords to lowercase.
     */
    private void simplifyQuotesAndCaps() {
        this.myContents = normalizeContents(this.myContents, this.myProperty);
    }

    /**
     * Normalizes contents by stripping quotes from URLs, removing whitespace from var(), and converting keywords to
     * lowercase.
     */
    private String normalizeContents(final String contents, final String property) {
        if (contents.regionMatches(true, 0, "url(", 0, 4)) {
            return contents.replaceAll("(?i)url\\(('|\")?(.*?)\\1\\)", "url($2)");
        }

        if (contents.regionMatches(true, 0, "var(", 0, 4)) {
            return contents.replaceAll("\\s", "");
        }

        if (!contents.contains(" ")) { // or !contents.matches(".*\\s.*")
            if (!"animation-name".equalsIgnoreCase(property)) {
                return contents.toLowerCase(Locale.US).replaceAll(STRING_REGEX, "$2");
            }

            return contents.replaceAll(STRING_REGEX, "$2");
        }

        return contents;
    }

    /**
     * Replaces HTML color names with hex values where shorter.
     */
    private void simplifyColourNames() {
        final String lcContents = myContents.toLowerCase(Locale.US);

        for (int index = 0; index < Constants.HTML_COLOR_NAMES.length; index++) {
            if (lcContents.equals(Constants.HTML_COLOR_NAMES[index])) {
                if (Constants.HTML_COLOR_VALUES[index].length() < Constants.HTML_COLOR_NAMES[index].length()) {
                    myContents = Constants.HTML_COLOR_VALUES[index];
                }

                break;
            } else if (lcContents.equals(Constants.HTML_COLOR_VALUES[index]) &&
                       Constants.HTML_COLOR_NAMES[index].length() < Constants.HTML_COLOR_VALUES[index].length()) {
                myContents = Constants.HTML_COLOR_NAMES[index];
            }
        }
    }

    /**
     * Compresses 6-digit hex colors to 3-digit hex where possible.
     */
    private void simplifyHexColours() {
        final StringBuilder newContents = new StringBuilder();

        final Pattern pattern = Pattern.compile(
          "#([0-9a-fA-F])([0-9a-fA-F])([0-9a-fA-F])([0-9a-fA-F])([0-9a-fA-F])([0-9a-fA-F])");
        final Matcher matcher = pattern.matcher(this.myContents);

        while (matcher.find()) {
            if (matcher.group(1).equalsIgnoreCase(matcher.group(2)) && matcher.group(3).equalsIgnoreCase(
              matcher.group(4)) && matcher.group(5).equalsIgnoreCase(matcher.group(6))) {
                matcher.appendReplacement(newContents,
                  "#" + matcher.group(1).toLowerCase(Locale.US) + matcher.group(3).toLowerCase(Locale.US) +
                  matcher.group(5).toLowerCase(Locale.US));
            } else {
                matcher.appendReplacement(newContents, matcher.group().toLowerCase(Locale.US));
            }
        }
        matcher.appendTail(newContents);

        this.myContents = newContents.toString();
    }

    /**
     * Returns itself.
     *
     * @return this part's string representation.
     */
    @SuppressWarnings({PMD.MISSING_OVERRIDE})
    public String toString() {
        return this.myContents;
    }
}

/**
 * Exception thrown when an unterminated comment is encountered.
 */
class UnterminatedCommentException extends Exception {

    private static final long serialVersionUID = 8876781670383774065L;
}

/**
 * Exception thrown when braces in CSS are unbalanced.
 */
class UnbalancedBracesException extends Exception {

    private static final long serialVersionUID = 1017211373204212357L;
}

/**
 * Exception thrown when a property definition is incomplete.
 */
class IncompletePropertyException extends Exception {

    private static final long serialVersionUID = -6934738141411722363L;

    /** The exception detail message. */
    private final String message;

    /**
     * Constructs an IncompletePropertyException with the specified detail message.
     *
     * @param message The detail message
     */
    IncompletePropertyException(final String message) {
        this.message = message;
    }

    /**
     * Returns the detail message string of this throwable.
     *
     * @return The detail message string
     */
    @Override
    public String getMessage() {
        return this.message;
    }
}

/**
 * Exception thrown when a selector body is empty.
 */
class EmptySelectorBodyException extends Exception {

    private static final long serialVersionUID = -2757452093745366543L;

    /** The exception detail message. */
    private final String message;

    /**
     * Constructs an EmptySelectorBodyException with the specified detail message.
     *
     * @param message The detail message
     */
    EmptySelectorBodyException(final String message) {
        this.message = message;
    }

    /**
     * Returns the detail message string of this throwable.
     *
     * @return The detail message string
     */
    @Override
    public String getMessage() {
        return this.message;
    }
}

/**
 * Exception thrown when a selector is not properly terminated.
 */
class UnterminatedSelectorException extends Exception {

    @Serial
    private static final long serialVersionUID = 88678974254059629L;

    /** The exception detail message. */
    private final String message;

    /**
     * Constructs an UnterminatedSelectorException with the specified detail message.
     *
     * @param message The detail message
     */
    UnterminatedSelectorException(final String message) {
        this.message = message;
    }

    /**
     * Returns the detail message string of this throwable.
     *
     * @return The detail message string
     */
    @Override
    public String getMessage() {
        return this.message;
    }
}

/**
 * Exception thrown when a selector definition is incomplete.
 */
class IncompleteSelectorException extends Exception {

    @Serial
    private static final long serialVersionUID = 6351992580967153497L;

    /** The exception detail message. */
    private final String message;

    /**
     * Constructs an IncompleteSelectorException with the specified detail message.
     *
     * @param message The detail message
     */
    IncompleteSelectorException(final String message) {
        this.message = message;
    }

    /**
     * Returns the detail message string of this throwable.
     *
     * @return The detail message string
     */
    @Override
    public String getMessage() {
        return this.message;
    }
}

/**
 * Constants used in CSS minification.
 */
final class Constants {

    /** Array of standard HTML color names. */
    static final String[] HTML_COLOR_NAMES = {"aliceblue", "antiquewhite", "aqua", "aquamarine", "azure", "beige",
      "bisque", "black", "blanchedalmond", "blue", "blueviolet", "brown", "burlywood", "cadetblue", "chartreuse",
      "chocolate", "coral", "cornflowerblue", "cornsilk", "crimson", "cyan", "darkblue", "darkcyan", "darkgoldenrod",
      "darkgray", "darkgreen", "darkkhaki", "darkmagenta", "darkolivegreen", "darkorange", "darkorchid", "darkred",
      "darksalmon", "darkseagreen", "darkslateblue", "darkslategray", "darkturquoise", "darkviolet", "deeppink",
      "deepskyblue", "dimgray", "dodgerblue", "firebrick", "floralwhite", "forestgreen", "fuchsia", "gainsboro",
      "ghostwhite", "gold", "goldenrod", "gray", "green", "greenyellow", "honeydew", "hotpink", "indianred ", "indigo ",
      "ivory", "khaki", "lavender", "lavenderblush", "lawngreen", "lemonchiffon", "lightblue", "lightcoral",
      "lightcyan", "lightgoldenrodyellow", "lightgrey", "lightgreen", "lightpink", "lightsalmon", "lightseagreen",
      "lightskyblue", "lightslategray", "lightsteelblue", "lightyellow", "lime", "limegreen", "linen", "magenta",
      "maroon", "mediumaquamarine", "mediumblue", "mediumorchid", "mediumpurple", "mediumseagreen", "mediumslateblue",
      "mediumspringgreen", "mediumturquoise", "mediumvioletred", "midnightblue", "mintcream", "mistyrose", "moccasin",
      "navajowhite", "navy", "oldlace", "olive", "olivedrab", "orange", "orangered", "orchid", "palegoldenrod",
      "palegreen", "paleturquoise", "palevioletred", "papayawhip", "peachpuff", "peru", "pink", "plum", "powderblue",
      "purple", "red", "rosybrown", "royalblue", "saddlebrown", "salmon", "sandybrown", "seagreen", "seashell",
      "sienna", "silver", "skyblue", "slateblue", "slategray", "snow", "springgreen", "steelblue", "tan", "teal",
      "thistle", "tomato", "turquoise", "violet", "wheat", "white", "whitesmoke", "yellow", "yellowgreen"};

    /** Array of hex color values corresponding to htmlColourNames. */
    static final String[] HTML_COLOR_VALUES = {"#f0f8ff", "#faebd7", "#00ffff", "#7fffd4", "#f0ffff", "#f5f5dc",
      "#ffe4c4", "#000", "#ffebcd", "#00f", "#8a2be2", "#a52a2a", "#deb887", "#5f9ea0", "#7fff00", "#d2691e", "#ff7f50",
      "#6495ed", "#fff8dc", "#dc143c", "#0ff", "#00008b", "#008b8b", "#b8860b", "#a9a9a9", "#006400", "#bdb76b",
      "#8b008b", "#556b2f", "#ff8c00", "#9932cc", "#8b0000", "#e9967a", "#8fbc8f", "#483d8b", "#2f4f4f", "#00ced1",
      "#9400d3", "#ff1493", "#00bfff", "#696969", "#1e90ff", "#b22222", "#fffaf0", "#228b22", "#f0f", "#dcdcdc",
      "#f8f8ff", "#ffd700", "#daa520", "#808080", "#008000", "#adff2f", "#f0fff0", "#ff69b4", "#cd5c5c", "#4b0082",
      "#fffff0", "#f0e68c", "#e6e6fa", "#fff0f5", "#7cfc00", "#fffacd", "#add8e6", "#f08080", "#e0ffff", "#fafad2",
      "#d3d3d3", "#90ee90", "#ffb6c1", "#ffa07a", "#20b2aa", "#87cefa", "#789", "#b0c4de", "#ffffe0", "#0f0", "#32cd32",
      "#faf0e6", "#f0f", "#800000", "#66cdaa", "#0000cd", "#ba55d3", "#9370d8", "#3cb371", "#7b68ee", "#00fa9a",
      "#48d1cc", "#c71585", "#191970", "#f5fffa", "#ffe4e1", "#ffe4b5", "#ffdead", "#000080", "#fdf5e6", "#808000",
      "#6b8e23", "#ffa500", "#ff4500", "#da70d6", "#eee8aa", "#98fb98", "#afeeee", "#d87093", "#ffefd5", "#ffdab9",
      "#cd853f", "#ffc0cb", "#dda0dd", "#b0e0e6", "#800080", "#f00", "#bc8f8f", "#4169e1", "#8b4513", "#fa8072",
      "#f4a460", "#2e8b57", "#fff5ee", "#a0522d", "#c0c0c0", "#87ceeb", "#6a5acd", "#708090", "#fffafa", "#00ff7f",
      "#4682b4", "#d2b48c", "#008080", "#d8bfd8", "#ff6347", "#40e0d0", "#ee82ee", "#f5deb3", "#fff", "#f5f5f5", "#ff0",
      "#9acd32"};

    /** Array of font weight name keywords. */
    static final String[] FONT_WEIGHT_NAMES = {"normal", "bold", "bolder", "lighter"};

    /** Array of numeric font weight values corresponding to fontWeightNames. */
    static final String[] FONT_WEIGHT_VALUES = {"400", "700", "900", "100"};

    /**
     * Instantiates a constants class.
     */
    private Constants() {
        // This is intentionally left empty
    }
}
