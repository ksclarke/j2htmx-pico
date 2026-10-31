/*
 * This code was derived from j2html, which is distributed under the Apache License, Version 2.0.
 * See LICENSE-ASL-2.0.txt in the parent project's root directory for more information.
 *
 * This file was modified for j2htmx-pico by Kevin S. Clarke (my.j2htmx-pico@kevinclarke.info).
 *
 * The modifications are licensed under the Mozilla Public License, Version 2.0.
 * See LICENSE-MPL-2.0.txt in the parent project's root directory for more information.
 */

package info.freelibrary.j2htmx.pico.rendering;

import static info.freelibrary.j2htmx.pico.TagCreator.div;
import static info.freelibrary.j2htmx.pico.TagCreator.input;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import info.freelibrary.j2htmx.pico.attributes.Attribute;
import info.freelibrary.j2htmx.pico.tags.AbstractDomContent;
import info.freelibrary.util.warnings.Checkstyle;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * Tests verifying rendering compatibility for DOM and attribute elements.
 */
@SuppressWarnings({Checkstyle.MULTIPLE_STRING_LITERALS, "MultipleStringLiterals"})
public class RenderingCompatabilityTest {

    /**
     * Tests that client classes implementing DOM rendering work with HtmlBuilder.
     *
     * @throws IOException If an I/O error occurs
     */
    @Test
    public void client_classes_which_implement_dom_rendering_continue_to_work_with_html_builder() throws IOException {
        final AbstractDomContent dom = div(
          new AbstractDomContent() {
              @Override
              public <T extends Appendable> T render(final HtmlBuilder<T> aHtmlBuilder, final Object aModel)
                throws IOException {
                  aHtmlBuilder.appendEscapedText(String.valueOf(aModel));
                  return aHtmlBuilder.output();
              }
          }
        );

        // Default rendering assumes a null model. Ensure that
        // convenience methods continue to work.
        assertThat(dom.render(), is("<div>null</div>"));

        final StringBuilder stringBuilder = new StringBuilder();
        dom.render(FlatHtml.into(stringBuilder), "victory");
        assertThat(stringBuilder.toString(), is("<div>victory</div>"));

        assertThat(
          dom.render(FlatHtml.inMemory(), "success").toString(),
          is("<div>success</div>")
        );
    }

    /**
     * Tests that client classes implementing attribute rendering work with TagBuilder.
     *
     * @throws IOException If an I/O error occurs
     */
    @Test
    public void client_classes_which_implement_attribute_rendering_continue_to_work_with_Attribute_builder()
    throws IOException {
        final Attribute mock = new MockAttribute("XXX", "fail");

        final TagBuilder nullBuilder = FlatHtml.inMemory().appendStartTag("input");
        mock.render(nullBuilder, null);
        assertThat(nullBuilder.completeTag().output().toString(), is("<input mock=\"null\">"));

        final TagBuilder modelBuilder = FlatHtml.inMemory().appendStartTag("input");
        mock.render(modelBuilder, "success");
        assertThat(modelBuilder.completeTag().output().toString(), is("<input mock=\"success\">"));

        // In empty tags.
        assertThat(
          input().withAttr(mock).render(FlatHtml.inMemory(), "success").toString(),
          is("<input mock=\"success\">")
        );

        // In container tags.
        assertThat(
          div().withAttr(mock).render(FlatHtml.inMemory(), "success").toString(),
          is("<div mock=\"success\"></div>")
        );
    }

    /**
     * Mock attribute implementation for testing tag rendering.
     */
    private final class MockAttribute extends Attribute {

        /**
         * Constructs a MockAttribute.
         *
         * @param aName The attribute name
         * @param aValue The attribute value
         */
        MockAttribute(final String aName, final String aValue) {
            super(aName, aValue);
        }

        @Override
        public void render(final TagBuilder aWriter, final Object aModel) throws IOException {
            aWriter.appendAttribute("mock", String.valueOf(aModel));
        }
    }
}
