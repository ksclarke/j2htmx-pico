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

import static info.freelibrary.j2htmx.pico.TagCreator.rawHtml;
import static info.freelibrary.j2htmx.pico.TagCreator.text;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import info.freelibrary.j2htmx.pico.rendering.FlatHtml;
import info.freelibrary.j2htmx.pico.rendering.IndentedHtml;
import info.freelibrary.util.warnings.Checkstyle;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * Tests for text rendering.
 */
@SuppressWarnings({Checkstyle.MULTIPLE_STRING_LITERALS, "MultipleStringLiterals"})
public class TextTest {

    /**
     * Tests that null text is rendered as string literal.
     *
     * @throws IOException If an I/O error occurs
     */
    @Test
    public void null_text_is_rendered_as_a_string_literal() throws IOException {
        assertThat(text(null).render(FlatHtml.inMemory()).toString(), is("null"));
        assertThat(text(null).render(IndentedHtml.inMemory()).toString(), is("null\n"));
    }

    /**
     * Tests that null unescaped text is rendered as string literal.
     *
     * @throws IOException If an I/O error occurs
     */
    @Test
    public void null_unescaped_text_is_rendered_as_a_string_literal() throws IOException {
        assertThat(rawHtml(null).render(FlatHtml.inMemory()).toString(), is("null"));
        assertThat(rawHtml(null).render(IndentedHtml.inMemory()).toString(), is("null\n"));
    }
}
