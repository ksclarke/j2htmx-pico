package info.freelibrary.j2htmx.pico.attributes;

import static info.freelibrary.j2htmx.pico.TagCreator.button;
import static info.freelibrary.j2htmx.pico.TagCreator.div;
import static info.freelibrary.j2htmx.pico.TagCreator.hxPartial;
import static info.freelibrary.j2htmx.pico.TagCreator.template;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

import org.junit.jupiter.api.Test;

public class HtmxTest {

    @Test
    public void testHtmxAttributes() {
        final String html = div().withHxGet("/url").withHxTarget("#target").render();
        assertThat(html, containsString("hx-get=\"/url\""));
        assertThat(html, containsString("hx-target=\"#target\""));
    }

    @Test
    public void testHtmxV4Attributes() {
        final String html = div().withHxUpsert("true").withHxPtag("v42").render();
        assertThat(html, containsString("hx-upsert=\"true\""));
        assertThat(html, containsString("hx-ptag=\"v42\""));
    }

    @Test
    public void testHxOn() {
        final String html = button().withHxOn("click", "alert('hi')").render();
        assertThat(html, containsString("hx-on:click=\"alert(&#x27;hi&#x27;)\""));
    }

    @Test
    public void testCondHtmxAttributes() {
        final String html = div().withCondHxGet(true, "/true").withCondHxGet(false, "/false").render();
        assertThat(html, containsString("hx-get=\"/true\""));
        assertThat(html, org.hamcrest.Matchers.not(containsString("hx-get=\"/false\"")));
    }

    @Test
    public void testHxPartialTag() {
        final String html = hxPartial(
          div("Content 1").withHxTarget("#t1"),
          hxPartial(div("Content 2")).withHxTarget("#t2")
        ).render();
        assertThat(html, containsString("<hx-partial"));
        assertThat(html, containsString("hx-target=\"#t1\""));
        assertThat(html, containsString("hx-target=\"#t2\""));
        assertThat(html, containsString("</hx-partial>"));
    }

    @Test
    public void testTemplateHx() {
        final String html = template().isHx().withType("partial").withHxTarget("#target").render();
        assertThat(html, containsString("<template hx"));
        assertThat(html, containsString("type=\"partial\""));
        assertThat(html, containsString("hx-target=\"#target\""));
    }
}
