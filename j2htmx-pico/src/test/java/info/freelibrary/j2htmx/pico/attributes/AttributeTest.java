package info.freelibrary.j2htmx.pico.attributes;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import info.freelibrary.j2htmx.pico.tags.ContainerTag;
import org.junit.jupiter.api.Test;

public class AttributeTest {

    @Test
    public void testRender() throws Exception {
        final Attribute attributeWithValue = new Attribute("href", "http://example.com");
        assertThat(attributeWithValue.render(), is(" href=\"http://example.com\""));

        final Attribute attribute = new Attribute("required", null);
        assertThat(attribute.render(), is(" required"));

        final Attribute nullAttribute = new Attribute(null, null);
        assertThat(nullAttribute.render(), is(""));
    }

    @Test
    public void testSetAttribute() throws Exception {
        final ContainerTag<?> testTag = new ContainerTag<>("a");
        testTag.attr("href", "http://example.com");
        testTag.attr("href", "http://example.org");
        assertThat(testTag.render(), is("<a href=\"http://example.org\"></a>"));
    }

}
