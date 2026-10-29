/*
 * This code was derived from j2html, which is distributed under the Apache License, Version 2.0.
 * See META-INF/THIRD-PARTY.txt and META-INF/licenses/Apache-2.0.txt for more information.
 *
 * This file has been modified for j2htmx-pico by Kevin S. Clarke (my.j2htmx-pico@kevinclarke.info)
 *
 * The modifications are licensed under the Mozilla Public License, Version 2.0.
 * See LICENSE.txt in the parent project's root for more information.
 */

package info.freelibrary.j2htmx.pico.attributes;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import info.freelibrary.j2htmx.pico.rendering.FlatHtml;
import info.freelibrary.j2htmx.pico.rendering.TagBuilder;
import info.freelibrary.j2htmx.pico.tags.ContainerTag;
import info.freelibrary.util.warnings.Checkstyle;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Attribute}.
 */
@SuppressWarnings({Checkstyle.MULTIPLE_STRING_LITERALS, "MultipleStringLiterals"})
public class AttributeTest {

    /**
     * Tests rendering of attributes with value, without value, and null.
     *
     * @throws Exception If an error occurs
     */
    @Test
    public void testRender() throws Exception {
        final Attribute attributeWithValue = new Attribute("href", "http://example.com");
        final TagBuilder valueBuilder = FlatHtml.inMemory().appendStartTag("a");
        attributeWithValue.render(valueBuilder, null);
        assertThat(valueBuilder.completeTag().output().toString(), is("<a href=\"http://example.com\">"));

        final Attribute attribute = new Attribute("required", null);
        final TagBuilder booleanBuilder = FlatHtml.inMemory().appendStartTag("input");
        attribute.render(booleanBuilder, null);
        assertThat(booleanBuilder.completeTag().output().toString(), is("<input required>"));

        final Attribute nullAttribute = new Attribute(null, null);
        final TagBuilder nullBuilder = FlatHtml.inMemory().appendStartTag("input");
        nullAttribute.render(nullBuilder, null);
        assertThat(nullBuilder.completeTag().output().toString(), is("<input>"));
    }

    /**
     * Tests setting attributes on a container tag.
     *
     * @throws Exception If an error occurs
     */
    @Test
    public void testSetAttribute() throws Exception {
        final ContainerTag<?> testTag = new ContainerTag<>("a");
        testTag.withAttr("href", "http://example.com");
        testTag.withAttr("href", "http://example.org");
        assertThat(testTag.render(), is("<a href=\"http://example.org\"></a>"));
    }

}
