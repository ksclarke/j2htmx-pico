package info.freelibrary.j2htmx.pico.attributes;

import org.junit.jupiter.api.Test;
import static info.freelibrary.j2htmx.pico.TagCreator.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

public class HtmxTest {

    @Test
    public void testHtmxAttributes() {
        String html = div().withHxGet("/url").withHxTarget("#target").render();
        assertThat(html, containsString("hx-get=\"/url\""));
        assertThat(html, containsString("hx-target=\"#target\""));
    }

    @Test
    public void testHtmxV4Attributes() {
        String html = div().withHxUpsert("true").withHxPtag("v42").render();
        assertThat(html, containsString("hx-upsert=\"true\""));
        assertThat(html, containsString("hx-ptag=\"v42\""));
    }

    @Test
    public void testHxOn() {
        String html = button().withHxOn("click", "alert('hi')").render();
        assertThat(html, containsString("hx-on:click=\"alert(&#x27;hi&#x27;)\""));
    }

    @Test
    public void testCondHtmxAttributes() {
        String html = div().withCondHxGet(true, "/true").withCondHxGet(false, "/false").render();
        assertThat(html, containsString("hx-get=\"/true\""));
        assertThat(html, org.hamcrest.Matchers.not(containsString("hx-get=\"/false\"")));
    }

    @Test
    public void testHxPartialTag() {
        String html = hxPartial(
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
        String html = template().isHx().withType("partial").withHxTarget("#target").render();
        assertThat(html, containsString("<template hx"));
        assertThat(html, containsString("type=\"partial\""));
        assertThat(html, containsString("hx-target=\"#target\""));
    }
}
