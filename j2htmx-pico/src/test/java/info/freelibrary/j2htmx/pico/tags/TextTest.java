package info.freelibrary.j2htmx.pico.tags;

import static info.freelibrary.j2htmx.pico.TagCreator.rawHtml;
import static info.freelibrary.j2htmx.pico.TagCreator.text;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import info.freelibrary.j2htmx.pico.rendering.FlatHtml;
import info.freelibrary.j2htmx.pico.rendering.IndentedHtml;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class TextTest {

    @Test
    public void null_text_is_rendered_as_a_string_literal() throws IOException {
        assertThat(text(null).render(FlatHtml.inMemory()).toString(), is("null"));
        assertThat(text(null).render(IndentedHtml.inMemory()).toString(), is("null\n"));
    }

    @Test
    public void null_unescaped_text_is_rendered_as_a_string_literal() throws IOException {
        assertThat(rawHtml(null).render(FlatHtml.inMemory()).toString(), is("null"));
        assertThat(rawHtml(null).render(IndentedHtml.inMemory()).toString(), is("null\n"));
    }
}
