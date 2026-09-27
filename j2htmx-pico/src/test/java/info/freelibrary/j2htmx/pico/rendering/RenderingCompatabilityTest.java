package info.freelibrary.j2htmx.pico.rendering;

import static info.freelibrary.j2htmx.pico.TagCreator.div;
import static info.freelibrary.j2htmx.pico.TagCreator.input;
import static info.freelibrary.j2htmx.pico.TagCreator.rawHtml;
import static info.freelibrary.j2htmx.pico.TagCreator.text;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import info.freelibrary.j2htmx.pico.attributes.Attribute;
import info.freelibrary.j2htmx.pico.tags.DomContent;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class RenderingCompatabilityTest {

    @Test
    public void container_tags_continue_to_support_original_model_rendering_methods() throws IOException {
        final StringBuilder stringBuilder = new StringBuilder();
        div(div()).renderModel(stringBuilder, null);
        assertThat(stringBuilder.toString(), is("<div><div></div></div>"));
    }

    @Test
    public void empty_tags_continue_to_support_original_model_rendering_methods() throws IOException {
        final StringBuilder stringBuilder = new StringBuilder();
        input().withId("X").renderModel(stringBuilder, null);
        assertThat(stringBuilder.toString(), is("<input id=\"X\">"));
    }

    @Test
    public void text_continues_to_support_original_model_rendering_methods() throws IOException {
        final StringBuilder stringBuilder = new StringBuilder();
        text("abc").renderModel(stringBuilder, null);
        assertThat(stringBuilder.toString(), is("abc"));
    }

    @Test
    public void unescaped_text_continues_to_support_original_model_rendering_methods() throws IOException {
        final StringBuilder stringBuilder = new StringBuilder();
        rawHtml("abc").renderModel(stringBuilder, null);
        assertThat(stringBuilder.toString(), is("abc"));
    }

    @Test
    public void client_classes_which_implement_dom_rendering_continue_to_work_with_html_builder() throws IOException {
        // Simulate a client class which can be injected into ContainerTags as
        // regular DomContent.  Those client classes should continue to work
        // even though HtmlBuilders are the newer approach to rendering.
        final DomContent dom = div(
          new DomContent() {
              @Override
              public void renderModel(final Appendable writer, final Object model) throws IOException {
                  writer.append(String.valueOf(model));
              }
          }
        );

        // Default rendering assumes a null model. Ensure that
        // convenience methods continue to work.
        assertThat(dom.render(), is("<div>null</div>"));

        StringBuilder stringBuilder = new StringBuilder();
        dom.render(stringBuilder);
        assertThat(stringBuilder.toString(), is("<div>null</div>"));

        stringBuilder = new StringBuilder();
        dom.renderModel(stringBuilder, "victory");
        assertThat(stringBuilder.toString(), is("<div>victory</div>"));

        // Rendering with an HtmlBuilder defers to the original methods.
        assertThat(
          dom.render(FlatHtml.inMemory(), "success").toString(),
          is("<div>success</div>")
        );
    }

    @Test
    public void client_classes_which_implement_attribute_rendering_continue_to_work_with_Attribute_builder()
    throws IOException {
        final Attribute mock = new MockAttribute("XXX", "fail");

        // Stand-alone rendering.
        assertThat(mock.render(), is(" mock=\"null\""));

        final StringBuilder stringBuilder = new StringBuilder();
        mock.render(stringBuilder);
        assertThat(stringBuilder.toString(), is(" mock=\"null\""));

        assertThat(
          mock.render(FlatHtml.inMemory(), "success").toString(),
          is(" mock=\"success\"")
        );

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
        public void renderModel(final Appendable writer, final Object model) throws IOException {
            writer.append(" mock=\"").append(String.valueOf(model)).append("\"");
        }
    }
}
