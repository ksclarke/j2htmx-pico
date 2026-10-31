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

/*
 * JSMin.java 2006-02-13
 *
 * Copyright (c) 2006 John Reilly (www.inconspicuous.org)
 *
 * This work is a translation from C to Java of jsmin.c published by
 * Douglas Crockford.  Permission is hereby granted to use the Java
 * version under the same conditions as the jsmin.c on which it is
 * based.
 *
 * jsmin.c 2003-04-21
 *
 * Copyright (c) 2002 Douglas Crockford (www.crockford.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * The Software shall be used for Good, not Evil.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

import static info.freelibrary.util.Constants.EMPTY;

import info.freelibrary.util.Logger;
import info.freelibrary.util.LoggerFactory;
import info.freelibrary.util.MessageCodes;
import info.freelibrary.util.warnings.Checkstyle;
import info.freelibrary.util.warnings.PMD;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.io.Serial;
import java.nio.charset.StandardCharsets;

/**
 * JavaScript minifier based on Douglas Crockford's JSMin.
 */
@SuppressWarnings({PMD.AVOID_LITERALS_IN_IF_CONDITION, PMD.COMMENT_SIZE})
public final class JSMin {

    /** The minifier's logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(JSMin.class, MessageCodes.BUNDLE);

    /** End-of-file indicator constant. */
    private static final int EOF = -1;

    /** Input stream providing JavaScript source bytes. */
    private final PushbackInputStream myInStream;

    /** Output stream receiving minified JavaScript bytes. */
    private final OutputStream myOutStream;

    /** Lookahead character buffer A. */
    private int myACharBuf;

    /** Lookahead character buffer B. */
    private int myBCharBuf;

    /**
     * Constructs a JSMin compressor wrapping the given input and output streams.
     *
     * @param aInStream The input stream to read JavaScript from
     * @param aOutStream The output stream to write minified JavaScript to
     */
    private JSMin(final InputStream aInStream, final OutputStream aOutStream) {
        myInStream = new PushbackInputStream(aInStream);
        myOutStream = aOutStream;
    }

    /**
     * Compress a JS string.
     *
     * @param aJsCodeBlock The js-code you want to compress
     * @return The compressed code
     */
    public static String compressJs(final String aJsCodeBlock) {
        final InputStream inStream = new ByteArrayInputStream(aJsCodeBlock.getBytes(StandardCharsets.UTF_8));
        final ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        final JSMin jsmin = new JSMin(inStream, outStream);

        try {
            jsmin.jsmin();
            return outStream.toString(StandardCharsets.UTF_8).trim();
        } catch (@SuppressWarnings(PMD.AVOID_CATCHING_GENERIC_EXCEPTION) final Exception details) {
            LOGGER.error(details, details.getMessage());
            return EMPTY;
        }
    }

    /**
     * Return true if the character is a letter, digit, underscore, dollar sign, or non-ASCII character.
     *
     * @param aCharCode The character code to check
     * @return True if alphanumeric, false otherwise
     */
    @SuppressWarnings({Checkstyle.BOOLEAN_EXPR_COMPLEXITY, "BooleanExpressionComplexity"})
    private static boolean isAlphanum(final int aCharCode) {
        return aCharCode >= 'a' && aCharCode <= 'z' || aCharCode >= '0' && aCharCode <= '9' ||
               aCharCode >= 'A' && aCharCode <= 'Z' ||
               aCharCode == '_' || aCharCode == '$' || aCharCode == '\\' || aCharCode > 126;
    }

    /**
     * Determines whether the minifier treats the given character code as a prefix operator. The recognized characters
     * are: closing brace, closing bracket, closing parenthesis, plus sign, minus sign, double quote, and single quote.
     *
     * @param aCharCode The character code to check
     * @return True if the character is a prefix operator; else, false
     */
    @SuppressWarnings({Checkstyle.BOOLEAN_EXPR_COMPLEXITY, "BooleanExpressionComplexity"})
    private static boolean isPrefixOp(final int aCharCode) {
        return aCharCode == '}' || aCharCode == ']' || aCharCode == ')' || aCharCode == '+' || aCharCode == '-' ||
               aCharCode == '"' || aCharCode == '\'';
    }

    /**
     * Determines whether the minifier treats the given character code as a postfix operator. The recognized characters
     * are: opening brace, opening bracket, opening parenthesis, plus sign, and minus sign.
     *
     * @param aCharCode The character code to check
     * @return True if the character is a postfix operator; else, false
     */
    private static boolean isPostfixOp(final int aCharCode) {
        return aCharCode == '{' || aCharCode == '[' || aCharCode == '(' || aCharCode == '+' || aCharCode == '-';
    }

    /**
     * Return the next character from stdin. Watch out for lookahead. If the character is a control character, translate
     * it to a space or linefeed.
     *
     * @return The next character code
     * @throws IOException If an I/O error occurs
     */
    private int get() throws IOException {
        final int charCode = myInStream.read();

        if (charCode >= ' ' || charCode == '\n' || charCode == EOF) {
            return charCode;
        }

        if (charCode == '\r') {
            return '\n';
        }

        return ' ';
    }

    /**
     * Get the next character without getting it.
     *
     * @return The peeked character code
     * @throws IOException If an I/O error occurs
     */
    private int peek() throws IOException {
        final int lookaheadChar = myInStream.read();
        myInStream.unread(lookaheadChar);
        return lookaheadChar;
    }

    /**
     * Get the next character, excluding comments. peek() is used to see if a '/' is followed by a '/' or '*'.
     *
     * @return The next non-comment character code
     * @throws IOException If an I/O error occurs
     * @throws UnterminatedCommentException If a comment is not terminated
     */
    @SuppressWarnings({"PMD.ImplicitSwitchFallThrough", "PMD.NonExhaustiveSwitch", PMD.COGNITIVE_COMPLEXITY,
      PMD.CYCLOMATIC_COMPLEXITY, "PMD.TooFewBranchesForSwitch", "MissingSwitchDefault",
      "checkstyle:MissingSwitchDefault"})
    private int next() throws IOException, UnterminatedCommentException {
        int charCode = get();

        if (charCode == '/') {
            switch (peek()) {
                case '/':
                    for (; ; ) {
                        charCode = get();

                        if (charCode <= '\n') {
                            return charCode;
                        }
                    }
                case '*':
                    get();

                    for (; ; ) {
                        switch (get()) {
                            case '*':
                                if (peek() == '/') {
                                    get();
                                    return ' ';
                                }

                                break;
                            case EOF:
                                throw new UnterminatedCommentException();
                        }
                    }

                default:
                    return charCode;
            }
        }

        return charCode;
    }

    /**
     * Action -- do something! What you do is determined by the argument: 1 Output A. Copy B to A. Get the next B. 2
     * Copy B to A. Get the next B. (Delete A). 3 Get the next B. (Delete B). action treats a string as a single
     * character. Wow! action recognizes a regular expression if it is preceded by ( or , or =.
     *
     * @param aAction The action to perform (1, 2, or 3)
     * @throws IOException If an I/O error occurs
     * @throws UnterminatedRegExpLiteralException If a regular expression is not closed
     * @throws UnterminatedCommentException If a comment is not closed
     * @throws UnterminatedStringLiteralException If a string literal is not closed
     */
    @SuppressWarnings({"PMD.ImplicitSwitchFallThrough", PMD.N_PATH_COMPLEXITY, PMD.CYCLOMATIC_COMPLEXITY,
      PMD.SWITCH_DENSITY, "PMD.NonExhaustiveSwitch", PMD.COGNITIVE_COMPLEXITY, "MissingSwitchDefault",
      "checkstyle:MissingSwitchDefault"})
    private void action(final int aAction)
    throws IOException, UnterminatedRegExpLiteralException, UnterminatedCommentException,
           UnterminatedStringLiteralException {
        switch (aAction) {
            case 1:
                myOutStream.write(myACharBuf);
            case 2:
                myACharBuf = myBCharBuf;

                if (myACharBuf == '\'' || myACharBuf == '"') {
                    for (; ; ) {
                        myOutStream.write(myACharBuf);
                        myACharBuf = get();

                        if (myACharBuf == myBCharBuf) {
                            break;
                        }

                        if (myACharBuf <= '\n') {
                            throw new UnterminatedStringLiteralException();
                        }

                        if (myACharBuf == '\\') {
                            myOutStream.write(myACharBuf);
                            myACharBuf = get();
                        }
                    }
                }
            case 3:
                myBCharBuf = next();

                if (myBCharBuf == '/' && (myACharBuf == '(' || myACharBuf == ',' || myACharBuf == '=')) {
                    myOutStream.write(myACharBuf);
                    myOutStream.write(myBCharBuf);

                    for (; ; ) {
                        myACharBuf = get();

                        if (myACharBuf == '/') {
                            break;
                        } else if (myACharBuf == '\\') {
                            myOutStream.write(myACharBuf);
                            myACharBuf = get();
                        } else if (myACharBuf <= '\n') {
                            throw new UnterminatedRegExpLiteralException();
                        }

                        myOutStream.write(myACharBuf);
                    }

                    myBCharBuf = next();
                }
        }
    }

    /**
     * Copy the input to the output, deleting the characters which are insignificant to JavaScript. Comments will be
     * removed. Tabs will be replaced with spaces. Carriage returns will be replaced with line feeds. Most spaces and
     * line feeds will be removed.
     *
     * @throws IOException If an I/O error occurs
     * @throws UnterminatedRegExpLiteralException If a regular expression is not closed
     * @throws UnterminatedCommentException If a comment is not closed
     * @throws UnterminatedStringLiteralException If a string literal is not closed
     */
    public void jsmin() throws IOException, UnterminatedRegExpLiteralException, UnterminatedCommentException,
                               UnterminatedStringLiteralException {
        myACharBuf = '\n';
        action(3);

        while (myACharBuf != EOF) {
            action(determineAction(myACharBuf, myBCharBuf));
        }

        myOutStream.flush();
    }

    /**
     * Determines the action to take based on the current and next characters.
     *
     * @param anACharCode The current character
     * @param aBCharCode The next character
     * @return The action to take
     */
    @SuppressWarnings({PMD.COGNITIVE_COMPLEXITY, PMD.CYCLOMATIC_COMPLEXITY})
    private int determineAction(final int anACharCode, final int aBCharCode) {
        if (anACharCode == ' ') {
            return isAlphanum(aBCharCode) ? 1 : 2;
        }

        if (anACharCode == '\n') {
            if (aBCharCode == ' ') {
                return 3;
            }

            if (isPostfixOp(aBCharCode) || isAlphanum(aBCharCode)) {
                return 1;
            }

            return 2;
        }

        // Default branch (anACharCode is any character other than ' ' or '\n')
        if (aBCharCode == ' ') {
            return isAlphanum(anACharCode) ? 1 : 3;
        }

        if (aBCharCode == '\n') {
            return (isPrefixOp(anACharCode) || isAlphanum(anACharCode)) ? 1 : 3;
        }

        return 1;
    }

    /**
     * Exception thrown when an unterminated comment is encountered.
     */
    public class UnterminatedCommentException extends Exception {

        @Serial
        private static final long serialVersionUID = -6843493496391588799L;
    }

    /**
     * Exception thrown when an unterminated string literal is encountered.
     */
    public class UnterminatedStringLiteralException extends Exception {

        @Serial
        private static final long serialVersionUID = 7919153854243389391L;
    }

    /**
     * Exception thrown when an unterminated regular expression literal is encountered.
     */
    public class UnterminatedRegExpLiteralException extends Exception {

        @Serial
        private static final long serialVersionUID = 3904862912673589752L;
    }

}
