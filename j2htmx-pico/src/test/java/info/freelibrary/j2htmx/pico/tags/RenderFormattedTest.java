package info.freelibrary.j2htmx.pico.tags;

import static info.freelibrary.j2htmx.pico.TagCreator.div;
import static info.freelibrary.j2htmx.pico.TagCreator.each;
import static info.freelibrary.j2htmx.pico.TagCreator.li;
import static info.freelibrary.j2htmx.pico.TagCreator.p;
import static info.freelibrary.j2htmx.pico.TagCreator.pre;
import static info.freelibrary.j2htmx.pico.TagCreator.span;
import static info.freelibrary.j2htmx.pico.TagCreator.textarea;
import static info.freelibrary.j2htmx.pico.TagCreator.ul;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;

public class RenderFormattedTest {

    @Test
    public void testFormattedTags() throws Exception {
        assertThat(div(p("Hello")).renderFormatted(), is("<div>\n    <p>\n        Hello\n    </p>\n</div>\n"));
    }

    @Test
    public void testFormattedTags_doesntFormatPre() throws Exception {
        assertThat(div(pre("public void renderModel(Appendable writer, Object model) throws IOException {\n" +
                           "        writer.append(text);\n" +
                           "    }")).renderFormatted(), is("<div>\n" +
                                                           "    <pre>public void renderModel(Appendable writer, " +
                                                           "Object model) throws IOException {\n" +
                                                           "        writer.append(text);\n" +
                                                           "    }</pre>\n" +
                                                           "</div>\n"));
    }

    @Test
    public void testFormattedTags_doesntFormatPreChildren() throws Exception {
        assertThat(div(pre("public void renderModel(Appendable writer, Object model) throws IOException {\n" +
                           "        writer.append(text);\n" +
                           "    }").with(span("Test"))).renderFormatted(), is("<div>\n" +
                                                                              "    <pre>public void renderModel" +
                                                                              "(Appendable writer, Object model) " +
                                                                              "throws IOException {\n" +
                                                                              "        writer.append(text);\n" +
                                                                              "    }<span>Test</span></pre>\n" +
                                                                              "</div>\n"));
    }

    @Test
    public void testFormattedTags_doesntFormatTextArea() throws Exception {
        assertThat(div(textarea("fred\ntom")).renderFormatted(), is("<div>\n" +
                                                                    "    <textarea>fred\n" +
                                                                    "tom</textarea>\n" +
                                                                    "</div>\n"));
    }

    @Test
    public void testFormattedTags_each() throws Exception {
        assertThat(ul(each(asList(1, 2, 3), i -> li("Number " + i))).renderFormatted(), is(
          "<ul>\n" +
          "    <li>\n" +
          "        Number 1\n" +
          "    </li>\n" +
          "    <li>\n" +
          "        Number 2\n" +
          "    </li>\n" +
          "    <li>\n" +
          "        Number 3\n" +
          "    </li>\n" +
          "</ul>\n"
        ));
    }

    @Test
    public void testFormattedTags_nestedEach() throws Exception {
        assertThat(div(ul(each(asList(1, 2, 3), i -> li("Number " + i)))).renderFormatted(), is(
          "<div>\n" +
          "    <ul>\n" +
          "        <li>\n" +
          "            Number 1\n" +
          "        </li>\n" +
          "        <li>\n" +
          "            Number 2\n" +
          "        </li>\n" +
          "        <li>\n" +
          "            Number 3\n" +
          "        </li>\n" +
          "    </ul>\n" +
          "</div>\n"
        ));
    }

}
