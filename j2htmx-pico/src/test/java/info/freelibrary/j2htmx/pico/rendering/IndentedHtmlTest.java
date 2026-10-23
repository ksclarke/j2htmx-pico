package info.freelibrary.j2htmx.pico.rendering;

import static info.freelibrary.j2htmx.pico.TagCreator.code;
import static info.freelibrary.j2htmx.pico.TagCreator.div;
import static info.freelibrary.j2htmx.pico.TagCreator.input;
import static info.freelibrary.j2htmx.pico.TagCreator.pre;
import static info.freelibrary.j2htmx.pico.TagCreator.text;
import static info.freelibrary.j2htmx.pico.TagCreator.textarea;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import info.freelibrary.j2htmx.pico.Config;
import info.freelibrary.util.warnings.Checkstyle;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * Tests for {@link IndentedHtml} rendering.
 */
@SuppressWarnings({Checkstyle.MULTIPLE_STRING_LITERALS, "MultipleStringLiterals"})
public class IndentedHtmlTest {

    /**
     * Tests that unescaped text is not modified.
     *
     * @throws Exception If an error occurs
     */
    @Test
    public void unescaped_text_is_not_modified() throws Exception {
        assertThat(
          IndentedHtml.inMemory().appendUnescapedText("<>&\"\'").output().toString(),
          is("<>&\"\'\n")
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
          IndentedHtml.inMemory().appendEscapedText("<>&\"\'").output().toString(),
          is("&lt;&gt;&amp;&quot;&#x27;\n")
        );
    }

    /**
     * Tests that root elements are not indented.
     *
     * @throws IOException If an I/O error occurs
     */
    @Test
    public void root_elements_are_not_indented() throws IOException {
        assertThat(
          div().render(IndentedHtml.inMemory()).toString(),
          is("<div>\n</div>\n")
        );

        assertThat(
          input().render(IndentedHtml.inMemory()).toString(),
          is("<input>\n")
        );
    }

    /**
     * Tests that indentation increases for each layer of children.
     *
     * @throws IOException If an I/O error occurs
     */
    @Test
    public void indentation_increases_for_each_layer_of_children() throws IOException {
        assertThat(
          div(div(div(input()))).render(IndentedHtml.inMemory()).toString(),
          is(
            "<div>\n" +
            "    <div>\n" +
            "        <div>\n" +
            "            <input>\n" +
            "        </div>\n" +
            "    </div>\n" +
            "</div>\n"
          )
        );
    }

    /**
     * Tests that lines of text are indented as siblings.
     *
     * @throws IOException If an I/O error occurs
     */
    @Test
    public void lines_of_text_are_indented_as_siblings() throws IOException {
        assertThat(
          div("abc\ndef\nghi").render(IndentedHtml.inMemory()).toString(),
          is(
            "<div>\n" +
            "    abc\n" +
            "    def\n" +
            "    ghi\n" +
            "</div>\n"
          )
        );
    }

    /**
     * Tests that indentation remains the same for siblings.
     *
     * @throws IOException If an I/O error occurs
     */
    @Test
    public void indentation_remains_the_same_for_sibling() throws IOException {
        assertThat(
          div(div(), input(), text("abc\ndef"), div()).render(IndentedHtml.inMemory()).toString(),
          is(
            "<div>\n" +
            "    <div>\n" +
            "    </div>\n" +
            "    <input>\n" +
            "    abc\n" +
            "    def\n" +
            "    <div>\n" +
            "    </div>\n" +
            "</div>\n"
          )
        );
    }

    /**
     * Tests that attributes are defined on a single line within start tag.
     *
     * @throws IOException If an I/O error occurs
     */
    @Test
    public void attributes_are_defined_within_the_start_tag_on_a_single_line() throws IOException {
        assertThat(
          div(
            div(
              div(
                input().withId("D").withClass("xyz")
              ).withId("C")
            ).withId("B")
          ).withId("A").render(IndentedHtml.inMemory()).toString(),
          is(
            "<div id=\"A\">\n" +
            "    <div id=\"B\">\n" +
            "        <div id=\"C\">\n" +
            "            <input id=\"D\" class=\"xyz\">\n" +
            "        </div>\n" +
            "    </div>\n" +
            "</div>\n"
          )
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
          div().withId("<>&\"\'").render(IndentedHtml.inMemory()).toString(),
          is("<div id=\"&lt;&gt;&amp;&quot;&#x27;\">\n</div>\n")
        );
    }

    /**
     * Tests that content within pre elements is not indented.
     *
     * @throws IOException If an I/O error occurs
     */
    @Test
    public void content_within_pre_elements_is_not_indented() throws IOException {
        //Single line text.
        assertThat(
          div(pre("abc")).render(IndentedHtml.inMemory()).toString(),
          is(
            "<div>\n" +
            "    <pre>abc</pre>\n" +
            "</div>\n"
          )
        );

        //Multiline text.
        assertThat(
          div(pre("abc\ndef")).render(IndentedHtml.inMemory()).toString(),
          is(
            "<div>\n" +
            "    <pre>abc\ndef</pre>\n" +
            "</div>\n"
          )
        );

        //Child elements.
        assertThat(
          div(pre(code("abc\ndef"))).render(IndentedHtml.inMemory()).toString(),
          is(
            "<div>\n" +
            "    <pre><code>abc\ndef</code></pre>\n" +
            "</div>\n"
          )
        );
    }

    /**
     * Tests that content within textarea elements is not indented.
     *
     * @throws IOException If an I/O error occurs
     */
    @Test
    public void content_within_textarea_elements_is_not_indented() throws IOException {
        //Single line text.
        assertThat(
          textarea("abc").render(IndentedHtml.inMemory()).toString(),
          is("<textarea>abc</textarea>\n")
        );

        //Multiline text.
        assertThat(
          textarea("abc\ndef").render(IndentedHtml.inMemory()).toString(),
          is("<textarea>abc\ndef</textarea>\n")
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
          input().render(IndentedHtml.inMemory(Config.getDefaults().withEmptyTagsClosed(false))).toString(),
          is("<input>\n")
        );

        assertThat(
          input().render(IndentedHtml.inMemory(Config.getDefaults().withEmptyTagsClosed(true))).toString(),
          is("<input/>\n")
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
          div().render(IndentedHtml.inMemory(Config.getDefaults().withEmptyTagsClosed(false))).toString(),
          is("<div>\n</div>\n")
        );

        assertThat(
          div().render(IndentedHtml.inMemory(Config.getDefaults().withEmptyTagsClosed(true))).toString(),
          is("<div>\n</div>\n")
        );
    }
}
