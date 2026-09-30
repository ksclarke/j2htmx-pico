package info.freelibrary.j2htmx.pico.rendering;

import static info.freelibrary.j2htmx.pico.TagCreator.div;
import static info.freelibrary.j2htmx.pico.TagCreator.input;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import info.freelibrary.j2htmx.pico.attributes.Attribute;
import info.freelibrary.j2htmx.pico.tags.DomContent;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class RenderingCompatabilityTest {

    @Test
    public void client_classes_which_implement_dom_rendering_continue_to_work_with_html_builder() throws IOException {
        final DomContent dom = div(
          new DomContent() {
              @Override
              public <T extends Appendable> T render(final HtmlBuilder<T> writer, final Object model)
                throws IOException {
                  writer.appendEscapedText(String.valueOf(model));
                  return writer.output();
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
          input().attr(mock).render(FlatHtml.inMemory(), "success").toString(),
          is("<input mock=\"success\">")
        );

        // In container tags.
        assertThat(
          div().attr(mock).render(FlatHtml.inMemory(), "success").toString(),
          is("<div mock=\"success\"></div>")
        );
    }

    private final class MockAttribute extends Attribute {

        public MockAttribute(final String name, final String value) {
            super(name, value);
        }

        @Override
        public void render(final TagBuilder writer, final Object model) throws IOException {
            writer.appendAttribute("mock", String.valueOf(model));
        }
    }
}
