package info.freelibrary.j2htmx.pico.attributes;

import static info.freelibrary.j2htmx.pico.TagCreator.a;
import static info.freelibrary.j2htmx.pico.TagCreator.button;
import static info.freelibrary.j2htmx.pico.TagCreator.div;
import static info.freelibrary.j2htmx.pico.TagCreator.form;
import static info.freelibrary.j2htmx.pico.TagCreator.html;
import static info.freelibrary.j2htmx.pico.TagCreator.input;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;

import org.junit.jupiter.api.Test;

public class PicoAttributesTest {

    @Test
    public void testDataTheme() {
        final String html = html().withDataTheme("dark").render();
        assertThat(html, containsString("data-theme=\"dark\""));
    }

    @Test
    public void testCondDataTheme() {
        final String htmlTrue = html().withCondDataTheme(true, "light").render();
        assertThat(htmlTrue, containsString("data-theme=\"light\""));

        final String htmlFalse = html().withCondDataTheme(false, "light").render();
        assertThat(htmlFalse, not(containsString("data-theme")));
    }

    @Test
    public void testTooltipAndPlacement() {
        final String html = button("Submit")
          .withDataTooltip("Helpful text")
          .withDataPlacement("bottom")
          .render();
        assertThat(html, containsString("data-tooltip=\"Helpful text\""));
        assertThat(html, containsString("data-placement=\"bottom\""));
    }

    @Test
    public void testCondTooltipAndPlacement() {
        final String html = button("Submit")
          .withCondDataTooltip(true, "Visible tooltip")
          .withCondDataPlacement(false, "top")
          .render();
        assertThat(html, containsString("data-tooltip=\"Visible tooltip\""));
        assertThat(html, not(containsString("data-placement")));
    }

    @Test
    public void testAriaBusy() {
        final String busyHtml = button("Loading").isAriaBusy().render();
        assertThat(busyHtml, containsString("aria-busy=\"true\""));

        final String notBusyHtml = button("Loaded").withAriaBusy(false).render();
        assertThat(notBusyHtml, containsString("aria-busy=\"false\""));
    }

    @Test
    public void testCondAriaBusy() {
        final String busyHtml = button("Save").withCondAriaBusy(true).render();
        assertThat(busyHtml, containsString("aria-busy=\"true\""));

        final String idleHtml = button("Save").withCondAriaBusy(false).render();
        assertThat(idleHtml, not(containsString("aria-busy")));

        final String busyValHtml = button("Save").withCondAriaBusy(true, false).render();
        assertThat(busyValHtml, containsString("aria-busy=\"false\""));
    }

    @Test
    public void testAriaInvalid() {
        final String invalidHtml = input().withAriaInvalid(true).render();
        assertThat(invalidHtml, containsString("aria-invalid=\"true\""));

        final String validHtml = input().withAriaInvalid(false).render();
        assertThat(validHtml, containsString("aria-invalid=\"false\""));

        final String isInvalidHtml = input().isAriaInvalid().render();
        assertThat(isInvalidHtml, containsString("aria-invalid=\"true\""));

        final String textInvalidHtml = input().withAriaInvalid("grammar").render();
        assertThat(textInvalidHtml, containsString("aria-invalid=\"grammar\""));
    }

    @Test
    public void testCondAriaInvalid() {
        final String invalidTrue = input().withCondAriaInvalid(true).render();
        assertThat(invalidTrue, containsString("aria-invalid=\"true\""));

        final String invalidFalse = input().withCondAriaInvalid(false).render();
        assertThat(invalidFalse, not(containsString("aria-invalid")));

        final String validCond = input().withCondAriaInvalid(true, false).render();
        assertThat(validCond, containsString("aria-invalid=\"false\""));
    }

    @Test
    public void testRoleHelpers() {
        final String btnHtml = a("Button link").asButton().render();
        assertThat(btnHtml, containsString("role=\"button\""));

        final String groupHtml = div().asGroup().render();
        assertThat(groupHtml, containsString("role=\"group\""));

        final String searchHtml = form().asSearch().render();
        assertThat(searchHtml, containsString("role=\"search\""));

        final String customRole = div().withRole("feed").render();
        assertThat(customRole, containsString("role=\"feed\""));
    }

    @Test
    public void testCondRoleHelpers() {
        final String btnHtml = a("Button link").withCondAsButton(true).render();
        assertThat(btnHtml, containsString("role=\"button\""));

        final String notBtnHtml = a("Regular link").withCondAsButton(false).render();
        assertThat(notBtnHtml, not(containsString("role")));

        final String groupHtml = div().withCondAsGroup(true).render();
        assertThat(groupHtml, containsString("role=\"group\""));

        final String notGroupHtml = div().withCondAsGroup(false).render();
        assertThat(notGroupHtml, not(containsString("role")));

        final String searchHtml = form().withCondAsSearch(true).render();
        assertThat(searchHtml, containsString("role=\"search\""));

        final String notSearchHtml = form().withCondAsSearch(false).render();
        assertThat(notSearchHtml, not(containsString("role")));
    }
}
