package info.freelibrary.j2htmx.pico.tags;

import static info.freelibrary.j2htmx.pico.TagCreator.body;
import static info.freelibrary.j2htmx.pico.TagCreator.div;
import static info.freelibrary.j2htmx.pico.TagCreator.footer;
import static info.freelibrary.j2htmx.pico.TagCreator.header;
import static info.freelibrary.j2htmx.pico.TagCreator.html;
import static info.freelibrary.j2htmx.pico.TagCreator.iff;
import static info.freelibrary.j2htmx.pico.TagCreator.main;
import static info.freelibrary.j2htmx.pico.TagCreator.p;
import static info.freelibrary.j2htmx.pico.TagCreator.tag;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.fail;

import info.freelibrary.j2htmx.pico.Config;
import info.freelibrary.j2htmx.pico.attributes.Attribute;
import info.freelibrary.j2htmx.pico.model.DynamicHrefAttribute;
import info.freelibrary.j2htmx.pico.rendering.FlatHtml;
import info.freelibrary.j2htmx.pico.tags.specialized.HtmlTag;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;

public class AbstractTagTest {

    @BeforeEach
    public void setUp() {
        Config.setGlobalEmptyTagsClosed(false);
    }

    @AfterEach
    public void tearDown() {
        // Restore Config defaults.
        Config.setGlobalEmptyTagsClosed(false);
    }

    // TODO Introduce a different concept for a sequence of tags, and require valid names for all Tags.

    @Test
    public void unnamed_containers_do_not_render_tags_or_attributes() {
        final AbstractTag<?> tag = new ContainerTag<>(null).attr("xyz", "123");
        assertThat(tag.render(), is(""));
    }

    @Test
    public void unnamed_containers_render_children() {
        final ContainerTag<?> tag = new ContainerTag<>(null)
          .with(new EmptyTag<>("abc"))
          .with(new ContainerTag<>("def"))
          .withText("ghi");
        assertThat(tag.render(), is("<abc><def></def>ghi"));
    }

    @Test
    public void named_containers_without_children_only_render_tags_and_attributes() {
        final AbstractTag<?> tag = new ContainerTag<>("abc").attr("xyz", "123");
        assertThat(tag.render(), is("<abc xyz=\"123\"></abc>"));
    }

    @Test
    public void populated_named_containers_render_tags_attributes_and_children() {
        final AbstractTag<?> tag = new ContainerTag<>("abc")
          .with(new EmptyTag<>("def"))
          .with(new ContainerTag<>("ghi"))
          .withText("jkl")
          .attr("xyz", "123");
        assertThat(tag.render(), is("<abc xyz=\"123\"><def><ghi></ghi>jkl</abc>"));
    }

    @Test
    public void empty_tags_must_be_named() {
        try {
            new EmptyTag<>(null);
            fail("Exception was not thrown.");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), is("Illegal tag name: null"));
        }

        try {
            new EmptyTag<>("");
            fail("Exception was not thrown.");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), is("Illegal tag name: \"\""));
        }
    }

    @Test
    public void empty_tags_can_be_configured_to_self_close() {
        // By default they will not be self-closing.
        assertThat(new EmptyTag<>("xyz").render(), is("<xyz>"));

        Config.setGlobalEmptyTagsClosed(true);
        assertThat(new EmptyTag<>("xyz").render(), is("<xyz/>"));
    }

    @Test
    public void attributes_are_rendered_in_the_order_that_they_are_defined() {
        final AbstractTag<?> container = new ContainerTag<>("abc")
          .attr("a", "A")
          .attr(new Attribute("b", "B"))
          .attr("c");
        assertThat(container.render(), is("<abc a=\"A\" b=\"B\" c></abc>"));

        final AbstractTag<?> tag = new EmptyTag<>("abc")
          .attr("c")
          .attr(new Attribute("b", "B"))
          .attr("a", "A");
        assertThat(tag.render(), is("<abc c b=\"B\" a=\"A\">"));
    }

    @Test
    public void testRender() {
        final ContainerTag<?> testTag = new ContainerTag<>("a");
        testTag.setAttribute("href", "http://example.com");
        assertThat(testTag.render(), is("<a href=\"http://example.com\"></a>"));

        final HtmlTag complexTestTag = html(
          body(
            header(),
            main().with(p("Main stuff...")),
            footer().condWith(1 == 1, p("Conditional with!"))
          )
        );
        final String expectedResult = "<html><body><header></header><main><p>Main stuff.." +
                                      ".</p></main><footer><p>Conditional with!</p></footer></body></html>";
        assertThat(complexTestTag.render(), is((expectedResult)));
    }

    @Test
    public void testFormattedTags() throws Exception { // better test in ComplexRenderTest.java
        assertThat(div(p("Hello")).renderFormatted(), is("<div>\n    <p>\n        Hello\n    </p>\n</div>\n"));
    }

    @Test
    public void testEquals() throws Exception {
        final AbstractTag<?> tagOne = tag("p").withText("Test").withClass("class");
        final AbstractTag<?> tagTwo = p("Test").withClass("class");
        assertThat(tagOne.equals(tagTwo), is(true));
    }

    @Test
    public void attribute_values_are_converted_to_strings() throws Exception {
        final AbstractTag<?> container = new ContainerTag<>("abc")
          .attr("string", "value1")
          .attr("integer", 2)
          .attr("none", null);
        assertThat(container.render(), is("<abc string=\"value1\" integer=\"2\" none></abc>"));

        final AbstractTag<?> tag = new EmptyTag<>("abc")
          .attr("string", "value1")
          .attr("integer", 2)
          .attr("none", null);
        assertThat(tag.render(), is("<abc string=\"value1\" integer=\"2\" none>"));
    }

    @Test
    public void testWithClasses() throws Exception {
        final String expected = "<div class=\"c1 c2\"></div>";
        final String actual = div().withClasses("c1", iff(1 == 1, "c2"), iff(1 == 2, "c3")).render();
        assertThat(actual, is(expected));
    }

    @Test
    public void testWithStyles() throws Exception {
        final String expected = "<div style=\"color: red; margin: 10px;\"></div>";
        final String actual = div().withStyles("color: red", iff(1 == 1, "margin: 10px"), iff(1 == 2, "display: none")).render();
        assertThat(actual, is(expected));

        final String expectedWithSemicolons = "<div style=\"color: red; margin: 10px;\"></div>";
        final String actualWithSemicolons = div().withStyles("color: red;", "margin: 10px;").render();
        assertThat(actualWithSemicolons, is(expectedWithSemicolons));

        final String expectedWithNullAndBlank = "<div style=\"color: red;\"></div>";
        final String actualWithNullAndBlank = div().withStyles("color: red", null, "", "   ").render();
        assertThat(actualWithNullAndBlank, is(expectedWithNullAndBlank));

        final String expectedEmpty = "<div style=\"\"></div>";
        assertThat(div().withStyles().render(), is(expectedEmpty));
        assertThat(div().withStyles((String[]) null).render(), is(expectedEmpty));
    }

    @Test
    public void testEmptyAttribute() throws Exception {
        final AbstractTag<?> testTagWithAttrValueNull = new ContainerTag<>("a").attr("attribute", null);
        assertThat(testTagWithAttrValueNull.render(), is("<a attribute></a>"));

        final AbstractTag<?> testTagAttrWithoutAttr = new ContainerTag<>("a").attr("attribute");
        assertThat(testTagAttrWithoutAttr.render(), is("<a attribute></a>"));
    }

    @Test
    public void testDynamicAttribute() throws Exception {
        final AbstractTag<?> testTagWithAttrValueNull = new ContainerTag<>("a").attr(new DynamicHrefAttribute());
        assertThat(testTagWithAttrValueNull.render(), is("<a href=\"/\"></a>"));
    }

    @Test
    public void testDynamicAttributeReplacement() throws Exception {
        final AbstractTag<?> testTagWithAttrValueNull = new ContainerTag<>("a").attr("href", "/link").attr(
          new DynamicHrefAttribute());
        assertThat(testTagWithAttrValueNull.render(), is("<a href=\"/\"></a>"));
    }

    @Test
    public void renderToFile() throws Exception {
        final FileWriter fileWriter = new FileWriter("file.txt");
        div("Test").render(FlatHtml.into(fileWriter, Config.global()), null);
        fileWriter.close();
        new File("file.txt").delete();
    }

}
