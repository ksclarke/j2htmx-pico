package info.freelibrary.j2htmx.pico.attributes;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import info.freelibrary.j2htmx.pico.rendering.FlatHtml;
import info.freelibrary.j2htmx.pico.rendering.TagBuilder;
import info.freelibrary.j2htmx.pico.tags.ContainerTag;
import org.junit.jupiter.api.Test;

public class AttributeTest {

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

    @Test
    public void testSetAttribute() throws Exception {
        final ContainerTag<?> testTag = new ContainerTag<>("a");
        testTag.attr("href", "http://example.com");
        testTag.attr("href", "http://example.org");
        assertThat(testTag.render(), is("<a href=\"http://example.org\"></a>"));
    }

}
