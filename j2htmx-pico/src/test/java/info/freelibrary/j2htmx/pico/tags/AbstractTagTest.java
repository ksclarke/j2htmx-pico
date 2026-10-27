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
import info.freelibrary.util.warnings.Checkstyle;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;

/**
 * Tests for {@link AbstractTag}.
 */
@SuppressWarnings({Checkstyle.MULTIPLE_STRING_LITERALS, "MultipleStringLiterals"})
public class AbstractTagTest {

    /**
     * Sets up the test fixture.
     */
    @BeforeEach
    public void setUp() {
        Config.setGlobalEmptyTagsClosed(false);
    }

    /**
     * Tears down the test fixture.
     */
    @AfterEach
    public void tearDown() {
        // Restore Config defaults.
        Config.setGlobalEmptyTagsClosed(false);
    }

    // TODO Introduce a different concept for a sequence of tags, and require valid names for all Tags.

    /**
     * Tests that unnamed containers do not render tags or attributes.
     */
    @Test
    public void unnamed_containers_do_not_render_tags_or_attributes() {
        final AbstractTag<?> tag = new ContainerTag<>(null).withAttr("xyz", "123");
        assertThat(tag.render(), is(""));
    }

    /**
     * Tests that unnamed containers render children.
     */
    @Test
    public void unnamed_containers_render_children() {
        final ContainerTag<?> tag = new ContainerTag<>(null)
          .with(new EmptyTag<>("abc"))
          .with(new ContainerTag<>("def"))
          .withText("ghi");
        assertThat(tag.render(), is("<abc><def></def>ghi"));
    }

    /**
     * Tests that named containers without children only render tags and attributes.
     */
    @Test
    public void named_containers_without_children_only_render_tags_and_attributes() {
        final AbstractTag<?> tag = new ContainerTag<>("abc").withAttr("xyz", "123");
        assertThat(tag.render(), is("<abc xyz=\"123\"></abc>"));
    }

    /**
     * Tests that populated named containers render tags, attributes, and children.
     */
    @Test
    public void populated_named_containers_render_tags_attributes_and_children() {
        final AbstractTag<?> tag = new ContainerTag<>("abc")
          .with(new EmptyTag<>("def"))
          .with(new ContainerTag<>("ghi"))
          .withText("jkl")
          .withAttr("xyz", "123");
        assertThat(tag.render(), is("<abc xyz=\"123\"><def><ghi></ghi>jkl</abc>"));
    }

    /**
     * Tests that empty tags must be named.
     */
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

    /**
     * Tests that empty tags can be configured to self close.
     */
    @Test
    public void empty_tags_can_be_configured_to_self_close() {
        // By default they will not be self-closing.
        assertThat(new EmptyTag<>("xyz").render(), is("<xyz>"));

        Config.setGlobalEmptyTagsClosed(true);
        assertThat(new EmptyTag<>("xyz").render(), is("<xyz/>"));
    }

    /**
     * Tests that attributes are rendered in the order defined.
     */
    @Test
    public void attributes_are_rendered_in_the_order_that_they_are_defined() {
        final AbstractTag<?> container = new ContainerTag<>("abc")
          .withAttr("a", "A")
          .withAttr(new Attribute("b", "B"))
          .withAttr("c");
        assertThat(container.render(), is("<abc a=\"A\" b=\"B\" c></abc>"));

        final AbstractTag<?> tag = new EmptyTag<>("abc")
          .withAttr("c")
          .withAttr(new Attribute("b", "B"))
          .withAttr("a", "A");
        assertThat(tag.render(), is("<abc c b=\"B\" a=\"A\">"));
    }

    /**
     * Tests rendering of container and html tags.
     */
    @Test
    public void testRender() {
        final ContainerTag<?> testTag = new ContainerTag<>("a");
        testTag.storeAttribute("href", "http://example.com");
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
        assertThat(complexTestTag.render(), is(expectedResult));
    }

    /**
     * Tests formatted tags rendering.
     *
     * @throws Exception If an error occurs
     */
    @Test
    public void testFormattedTags() throws Exception { // better test in ComplexRenderTest.java
        assertThat(div(p("Hello")).renderFormatted(), is("<div>\n    <p>\n        Hello\n    </p>\n</div>\n"));
    }

    /**
     * Tests equality comparison of tags.
     *
     * @throws Exception If an error occurs
     */
    @Test
    public void testEquals() throws Exception {
        final AbstractTag<?> tagOne = tag("p").withText("Test").withClass("class");
        final AbstractTag<?> tagTwo = p("Test").withClass("class");
        assertThat(tagOne.equals(tagTwo), is(true));
    }

    /**
     * Tests that attribute values are converted to strings.
     *
     * @throws Exception If an error occurs
     */
    @Test
    public void attribute_values_are_converted_to_strings() throws Exception {
        final AbstractTag<?> container = new ContainerTag<>("abc")
          .withAttr("string", "value1")
          .withAttr("integer", 2)
          .withAttr("none", null);
        assertThat(container.render(), is("<abc string=\"value1\" integer=\"2\" none></abc>"));

        final AbstractTag<?> tag = new EmptyTag<>("abc")
          .withAttr("string", "value1")
          .withAttr("integer", 2)
          .withAttr("none", null);
        assertThat(tag.render(), is("<abc string=\"value1\" integer=\"2\" none>"));
    }

    /**
     * Tests withClasses method.
     *
     * @throws Exception If an error occurs
     */
    @Test
    public void testWithClasses() throws Exception {
        final String expected = "<div class=\"c1 c2\"></div>";
        final String actual = div().withClasses("c1", iff(1 == 1, "c2"), iff(1 == 2, "c3")).render();
        assertThat(actual, is(expected));
    }

    /**
     * Tests withStyles method.
     *
     * @throws Exception If an error occurs
     */
    @Test
    public void testWithStyles() throws Exception {
        final String expected = "<div style=\"color: red; margin: 10px;\"></div>";
        final String actual = div().withStyles("color: red", iff(1 == 1, "margin: 10px"), iff(1 == 2, "display: none"))
          .render();
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

    /**
     * Tests empty attribute rendering.
     *
     * @throws Exception If an error occurs
     */
    @Test
    public void testEmptyAttribute() throws Exception {
        final AbstractTag<?> testTagWithAttrValueNull = new ContainerTag<>("a").withAttr("attribute", null);
        assertThat(testTagWithAttrValueNull.render(), is("<a attribute></a>"));

        final AbstractTag<?> testTagAttrWithoutAttr = new ContainerTag<>("a").withAttr("attribute");
        assertThat(testTagAttrWithoutAttr.render(), is("<a attribute></a>"));
    }

    /**
     * Tests dynamic attribute rendering.
     *
     * @throws Exception If an error occurs
     */
    @Test
    public void testDynamicAttribute() throws Exception {
        final AbstractTag<?> testTagWithAttrValueNull = new ContainerTag<>("a").withAttr(new DynamicHrefAttribute());
        assertThat(testTagWithAttrValueNull.render(), is("<a href=\"/\"></a>"));
    }

    /**
     * Tests dynamic attribute replacement.
     *
     * @throws Exception If an error occurs
     */
    @Test
    public void testDynamicAttributeReplacement() throws Exception {
        final AbstractTag<?> testTagWithAttrValueNull = new ContainerTag<>("a").withAttr("href", "/link").withAttr(
          new DynamicHrefAttribute());
        assertThat(testTagWithAttrValueNull.render(), is("<a href=\"/\"></a>"));
    }

    /**
     * Tests attribute replacement and uniqueness enforcement across valued and boolean attributes.
     */
    @Test
    public void testAttributeReplacementAndUniqueness() {
        final AbstractTag<?> tag1 = new ContainerTag<>("a")
          .withAttr("href", "http://old.example.com")
          .withAttr("href", "http://new.example.com");
        assertThat(tag1.render(), is("<a href=\"http://new.example.com\"></a>"));

        final AbstractTag<?> tag2 = new ContainerTag<>("input")
          .withAttr("disabled", "false")
          .withAttr("disabled");
        assertThat(tag2.render(), is("<input disabled></input>"));

        final AbstractTag<?> tag3 = new ContainerTag<>("input")
          .withAttr("disabled", "false")
          .withAttr("disabled", null);
        assertThat(tag3.render(), is("<input disabled></input>"));

        final AbstractTag<?> tag4 = new ContainerTag<>("input")
          .withAttr("disabled")
          .withAttr("disabled", "true");
        assertThat(tag4.render(), is("<input disabled=\"true\"></input>"));

        final AbstractTag<?> tag5 = new ContainerTag<>("div")
          .withCondAttr(true, "data-test", "val1")
          .withCondAttr(true, "data-test", "val2");
        assertThat(tag5.render(), is("<div data-test=\"val2\"></div>"));

        final AbstractTag<?> tag6 = new ContainerTag<>("div")
          .withCondAttr(false, "data-test", "val1");
        assertThat(tag6.render(), is("<div></div>"));
    }

    /**
     * Tests rendering to file writer.
     *
     * @throws Exception If an error occurs
     */
    @Test
    public void renderToFile() throws Exception {
        final FileWriter fileWriter = new FileWriter("file.txt");
        div("Test").render(FlatHtml.into(fileWriter, Config.global()), null);
        fileWriter.close();
        new File("file.txt").delete();
    }

}
