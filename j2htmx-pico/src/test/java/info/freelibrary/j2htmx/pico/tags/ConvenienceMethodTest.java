/*
 * This code was derived from j2html, which is distributed under the Apache License, Version 2.0.
 * See LICENSE-ASL-2.0.txt in the parent project's root directory for more information.
 *
 * This file was modified for j2htmx-pico by Kevin S. Clarke (my.j2htmx-pico@kevinclarke.info).
 *
 * The modifications are licensed under the Mozilla Public License, Version 2.0.
 * See LICENSE-MPL-2.0.txt in the parent project's root directory for more information.
 */

package info.freelibrary.j2htmx.pico.tags;

import static info.freelibrary.j2htmx.pico.TagCreator.a;
import static info.freelibrary.j2htmx.pico.TagCreator.div;
import static info.freelibrary.j2htmx.pico.TagCreator.form;
import static info.freelibrary.j2htmx.pico.TagCreator.img;
import static info.freelibrary.j2htmx.pico.TagCreator.input;
import static info.freelibrary.j2htmx.pico.TagCreator.link;
import static info.freelibrary.j2htmx.pico.TagCreator.meta;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import info.freelibrary.util.warnings.Checkstyle;
import org.junit.jupiter.api.Test;

/**
 * Tests for tag convenience methods.
 */
@SuppressWarnings({Checkstyle.MULTIPLE_STRING_LITERALS, "MultipleStringLiterals"})
public class ConvenienceMethodTest {

    /**
     * Tests all tag convenience methods.
     *
     * @throws Exception If an error occurs
     */
    @Test
    public void testAllConvenienceMethods() throws Exception {
        assertThat(input().hasAutoComplete().render(), is("<input autocomplete=\"on\">"));
        assertThat(input().hasAutoFocus().render(), is("<input autofocus>"));
        assertThat(input().isHidden().render(), is("<input hidden>"));
        assertThat(input().isRequired().render(), is("<input required>"));
        assertThat(img().withAlt("An image").render(), is("<img alt=\"An image\">"));
        assertThat(form().withAction("post").render(), is("<form action=\"post\"></form>"));
        assertThat(meta().withCharset("UTF-8").render(), is("<meta charset=\"UTF-8\">"));
        assertThat(div().withClass("test-class").render(), is("<div class=\"test-class\"></div>"));
        assertThat(meta().withContent("Test Content").render(), is("<meta content=\"Test Content\">"));
        assertThat(a().withHref("http://example.com").render(), is("<a href=\"http://example.com\"></a>"));
        assertThat(div().withId("test-id").render(), is("<div id=\"test-id\"></div>"));
        assertThat(div().withData("testdata", "test").render(), is("<div data-testdata=\"test\"></div>"));
        assertThat(form().withMethod("get").render(), is("<form method=\"get\"></form>"));
        assertThat(input().withName("test-name").render(), is("<input name=\"test-name\">"));
        assertThat(input().withPlaceholder("test-placeholder").render(),
          is("<input placeholder=\"test-placeholder\">"));
        assertThat(a().withTarget("_blank").render(), is("<a target=\"_blank\"></a>"));
        assertThat(a().withTitle("Title").render(), is("<a title=\"Title\"></a>"));
        assertThat(input().withAttr("type", "email").render(), is("<input type=\"email\">"));
        assertThat(link().withRel("stylesheet").render(), is("<link rel=\"stylesheet\">"));
        assertThat(link().withAttr("role", "role").render(), is("<link role=\"role\">"));
        assertThat(img().withSrc("/img/test.png").render(), is("<img src=\"/img/test.png\">"));
        assertThat(input().withStep("0.1").render(), is("<input step=\"0.1\">"));
        assertThat(div().withStyle("background:red;").render(), is("<div style=\"background:red;\"></div>"));
        assertThat(input().withAttr("value", "test-value").render(), is("<input value=\"test-value\">"));
    }
}
