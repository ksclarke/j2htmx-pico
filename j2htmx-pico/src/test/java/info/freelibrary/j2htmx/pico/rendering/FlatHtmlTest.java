/*
 * This code was derived from j2html, which is distributed under the Apache License, Version 2.0.
 * See META-INF/THIRD-PARTY.txt and META-INF/licenses/Apache-2.0.txt for more information.
 *
 * This file has been modified for j2htmx-pico by Kevin S. Clarke (my.j2htmx-pico@kevinclarke.info)
 *
 * The modifications are licensed under the Mozilla Public License, Version 2.0.
 * See LICENSE.txt in the parent project's root for more information.
 */

package info.freelibrary.j2htmx.pico.rendering;

import static info.freelibrary.j2htmx.pico.TagCreator.div;
import static info.freelibrary.j2htmx.pico.TagCreator.input;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import info.freelibrary.j2htmx.pico.Config;
import info.freelibrary.util.warnings.Checkstyle;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * Tests for {@link FlatHtml} rendering.
 */
@SuppressWarnings({Checkstyle.MULTIPLE_STRING_LITERALS, "MultipleStringLiterals"})
public class FlatHtmlTest {

    /**
     * Tests that start tags contain attributes.
     *
     * @throws IOException If an I/O error occurs
     */
    @Test
    public void start_tags_contain_attributes() throws IOException {
        assertThat(
          FlatHtml.inMemory().appendStartTag("abc")
            .appendAttribute("x", "X")
            .appendBooleanAttribute("y")
            .completeTag().output().toString(),
          is("<abc x=\"X\" y>")
        );
    }

    /**
     * Tests that empty tags contain attributes.
     *
     * @throws IOException If an I/O error occurs
     */
    @Test
    public void empty_tags_contain_attributes() throws IOException {
        assertThat(
          FlatHtml.inMemory().appendEmptyTag("abc")
            .appendAttribute("x", "X")
            .appendBooleanAttribute("y")
            .completeTag().output().toString(),
          is("<abc x=\"X\" y>")
        );
    }

    /**
     * Tests that unescaped text is not modified.
     *
     * @throws Exception If an error occurs
     */
    @Test
    public void unescaped_text_is_not_modified() throws Exception {
        assertThat(
          FlatHtml.inMemory().appendUnescapedText("<>&\"\'").output().toString(),
          is("<>&\"\'")
        );
    }

    /**
     * Tests that escaped text replaces special characters with character entities.
     *
     * @throws Exception If an error occurs
     */
    @Test
    public void escaped_text_replaces_special_characters_with_character_entities() throws Exception {
        assertThat(
          FlatHtml.inMemory().appendEscapedText("<>&\"\'").output().toString(),
          is("&lt;&gt;&amp;&quot;&#x27;")
        );
    }

    /**
     * Tests that attribute values are escaped.
     *
     * @throws IOException If an I/O error occurs
     */
    @Test
    public void attribute_values_are_escaped() throws IOException {
        assertThat(
          div().withId("<>&\"\'").render(FlatHtml.inMemory()).toString(),
          is("<div id=\"&lt;&gt;&amp;&quot;&#x27;\"></div>")
        );
    }

    /**
     * Tests that empty tags are closed when configured.
     *
     * @throws IOException If an I/O error occurs
     */
    @Test
    public void empty_tags_are_closed_when_configured() throws IOException {
        assertThat(
          input().render(FlatHtml.inMemory(Config.getDefaults().withEmptyTagsClosed(false))).toString(),
          is("<input>")
        );

        assertThat(
          input().render(FlatHtml.inMemory(Config.getDefaults().withEmptyTagsClosed(true))).toString(),
          is("<input/>")
        );
    }

    /**
     * Tests that end tags are never closed.
     *
     * @throws IOException If an I/O error occurs
     */
    @Test
    public void end_tags_are_never_closed() throws IOException {
        assertThat(
          div().render(FlatHtml.inMemory(Config.getDefaults().withEmptyTagsClosed(false))).toString(),
          is("<div></div>")
        );

        assertThat(
          div().render(FlatHtml.inMemory(Config.getDefaults().withEmptyTagsClosed(true))).toString(),
          is("<div></div>")
        );
    }
}
