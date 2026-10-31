/*
 * This code was derived from j2html, which is distributed under the Apache License, Version 2.0.
 * See LICENSE-ASL-2.0.txt in the parent project's root directory for more information.
 *
 * This file was modified for j2htmx-pico by Kevin S. Clarke (my.j2htmx-pico@kevinclarke.info).
 *
 * The modifications are licensed under the Mozilla Public License, Version 2.0.
 * See LICENSE-MPL-2.0.txt in the parent project's root directory for more information.
 */

package info.freelibrary.j2htmx.pico;

import info.freelibrary.j2htmx.pico.utils.EscapeUtil;
import info.freelibrary.util.warnings.Checkstyle;
import org.apache.commons.lang3.StringEscapeUtils;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

/**
 * Performance tests comparing HTML escape mechanisms.
 */
@SuppressWarnings({Checkstyle.MULTIPLE_STRING_LITERALS, "MultipleStringLiterals"})
@TestMethodOrder(MethodOrderer.MethodName.class)
public class PerformanceTest {

    /** Short test HTML string. */
    private final String myShortTestString = "<body>\n"
                                           + "    <h1 class=\"example\">Heading!</h1>\n"
                                           + "    <img src=\"img/hello.png\">\n"
                                           + "</body>";

    /** Long syntax-highlighted HTML string for performance testing. */
    // syntax-highlighted getting started example from j2html.com:
    private final String myLongTestString =
      "<pre class=\" language-java\"><code class=\" language-java\"><span class=\"token keyword\">import</span> <span" +
      " class=\"token keyword\">static</span> j2html<span class=\"token punctuation\">.</span>TagCreator<span " +
      "class=\"token punctuation\">.</span>*<span class=\"token punctuation\">;</span>\n"
      + "\n"
      +
      "<span class=\"token keyword\">public</span> <span class=\"token keyword\">class</span> <span class=\"token " +
      "class-name\">Main</span> <span class=\"token punctuation\">{</span>\n"
      +
      "    <span class=\"token keyword\">public</span> <span class=\"token keyword\">static</span> <span " +
      "class=\"token keyword\">void</span> <span class=\"token function\">main<span class=\"token punctuation\">" +
      "(</span></span>String<span class=\"token punctuation\">[</span><span class=\"token punctuation\">]</span> " +
      "args<span class=\"token punctuation\">)</span> <span class=\"token punctuation\">{</span>\n"
      +
      "        <span class=\"token function\">body<span class=\"token punctuation\">(</span></span><span " +
      "class=\"token punctuation\">)</span><span class=\"token punctuation\">.</span><span class=\"token " +
      "function\">with<span class=\"token punctuation\">(</span></span>\n"
      +
      "                <span class=\"token function\">h1<span class=\"token punctuation\">(</span></span><span " +
      "class=\"token string\">\"Heading!\"</span><span class=\"token punctuation\">)</span><span class=\"token " +
      "punctuation\">.</span><span class=\"token function\">withClass<span class=\"token punctuation\">" +
      "(</span></span><span class=\"token string\">\"example\"</span><span class=\"token punctuation\">)</span><span " +
      "class=\"token punctuation\">,</span>\n"
      +
      "                <span class=\"token function\">img<span class=\"token punctuation\">(</span></span><span " +
      "class=\"token punctuation\">)</span><span class=\"token punctuation\">.</span><span class=\"token " +
      "function\">withSrc<span class=\"token punctuation\">(</span></span><span class=\"token string\">\"img/hello" +
      ".png\"</span><span class=\"token punctuation\">)</span>\n"
      +
      "        <span class=\"token punctuation\">)</span><span class=\"token punctuation\">.</span><span " +
      "class=\"token function\">render<span class=\"token punctuation\">(</span></span><span class=\"token " +
      "punctuation\">)</span><span class=\"token punctuation\">;</span>\n"
      + "    <span class=\"token punctuation\">}</span>\n"
      + "<span class=\"token punctuation\">}</span>\n"
      + "</code></pre>";

    /**
     * Tests simple escaper performance on short input.
     *
     * @throws Exception If an error occurs
     */
    @Test
    public void testSimpleEscaperShort() throws Exception {
        EscapeUtil.escape(myShortTestString);
    }

    /**
     * Tests simple escaper performance on long input.
     *
     * @throws Exception If an error occurs
     */
    @Test
    public void testSimpleEscaperLong() throws Exception {
        EscapeUtil.escape(myLongTestString);
    }

    /**
     * Tests Apache escaper performance on short input.
     *
     * @throws Exception If an error occurs
     */
    @Test
    public void testApacheEscaperShort() throws Exception {
        StringEscapeUtils.escapeHtml4(myShortTestString);
    }

    /**
     * Tests Apache escaper performance on long input.
     *
     * @throws Exception If an error occurs
     */
    @Test
    public void testApacheEscaperLong() throws Exception {
        StringEscapeUtils.escapeHtml4(myLongTestString);
    }
}
