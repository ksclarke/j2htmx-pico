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

import static info.freelibrary.j2htmx.pico.TagCreator.fileAsEscapedString;
import static info.freelibrary.j2htmx.pico.TagCreator.fileAsString;
import static info.freelibrary.j2htmx.pico.TagCreator.scriptWithInlineFileMinimized;
import static info.freelibrary.j2htmx.pico.TagCreator.styleWithInlineFileMinimized;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

import info.freelibrary.util.warnings.Checkstyle;
import org.junit.jupiter.api.Test;

/**
 * Tests for inline static resource handling.
 */
@SuppressWarnings({Checkstyle.MULTIPLE_STRING_LITERALS, "MultipleStringLiterals"})
public class InlineStaticResourceTest {

    /** The system line separator. */
    private static final String EOL = System.getProperty("line.separator"); // System independent End Of Line

    /**
     * Tests inlining static resources for all tag types.
     *
     * @throws Exception If an error occurs
     */
    @Test
    public void testAllTags() throws Exception {

        final String expectedCss = "<style>body{background:#daa520;margin-bottom:10px;margin-left:10px;" +
                                   "margin-right:10px;margin-top:10px}</style>";
        final String expectedJs = "<script>(function(){var test=5;var tast=10;var testTast=test+tast;console.log" +
                                  "(testTast);})();</script>";
        final String expectedHtml = "<body>" + EOL + "    Any content" + EOL + "</body>" + EOL;
        final String expectedEscapedHtml = "&lt;body&gt;" + EOL + "    Any content" + EOL + "&lt;/body&gt;" + EOL;
        final String expectedAnyContent = "public class AnyContent {" + EOL + "}" + EOL;

        // classpath files
        assertThat(styleWithInlineFileMinimized("/test.css").render(), is(expectedCss));
        assertThat(styleWithInlineFileMinimized("/test-without-trailing-semis.css").render(), is(expectedCss));
        assertThat(scriptWithInlineFileMinimized("/test.js").render(), is(expectedJs));
        assertThat(fileAsString("/test.html").render(), is(expectedHtml));
        assertThat(fileAsEscapedString("/test.html").render(), is(expectedEscapedHtml));
        assertThat(fileAsString("/AnyContent.java").render(), is(expectedAnyContent));

        // files outside classpath
        assertThat(styleWithInlineFileMinimized("src/test/files/test.css").render(), is(expectedCss));
        assertThat(scriptWithInlineFileMinimized("src/test/files/test.js").render(), is(expectedJs));
        assertThat(fileAsString("src/test/files/test.html").render(), is(expectedHtml));
        assertThat(fileAsEscapedString("src/test/files/test.html").render(), is(expectedEscapedHtml));
        assertThat(fileAsString("src/test/files/AnyContent.java").render(), is(expectedAnyContent));
    }

    /**
     * Tests that bad paths throw runtime exception.
     *
     * @throws Exception If an error occurs
     */
    @Test
    public void testExceptionForBadPath() throws Exception {
        assertThrows(RuntimeException.class, () -> styleWithInlineFileMinimized("NOT A FILE"));
    }

}
