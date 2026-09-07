package info.freelibrary.j2htmx.pico.attributes;

import org.junit.jupiter.api.Test;
import static info.freelibrary.j2htmx.pico.TagCreator.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;

public class PicoAttributesTest {

    @Test
    public void testDataTheme() {
        String html = html().withDataTheme("dark").render();
        assertThat(html, containsString("data-theme=\"dark\""));
    }

    @Test
    public void testCondDataTheme() {
        String htmlTrue = html().withCondDataTheme(true, "light").render();
        assertThat(htmlTrue, containsString("data-theme=\"light\""));

        String htmlFalse = html().withCondDataTheme(false, "light").render();
        assertThat(htmlFalse, not(containsString("data-theme")));
    }

    @Test
    public void testTooltipAndPlacement() {
        String html = button("Submit")
            .withDataTooltip("Helpful text")
            .withDataPlacement("bottom")
            .render();
        assertThat(html, containsString("data-tooltip=\"Helpful text\""));
        assertThat(html, containsString("data-placement=\"bottom\""));
    }

    @Test
    public void testCondTooltipAndPlacement() {
        String html = button("Submit")
            .withCondDataTooltip(true, "Visible tooltip")
            .withCondDataPlacement(false, "top")
            .render();
        assertThat(html, containsString("data-tooltip=\"Visible tooltip\""));
        assertThat(html, not(containsString("data-placement")));
    }

    @Test
    public void testAriaBusy() {
        String busyHtml = button("Loading").isAriaBusy().render();
        assertThat(busyHtml, containsString("aria-busy=\"true\""));

        String notBusyHtml = button("Loaded").withAriaBusy(false).render();
        assertThat(notBusyHtml, containsString("aria-busy=\"false\""));
    }

    @Test
    public void testCondAriaBusy() {
        String busyHtml = button("Save").withCondAriaBusy(true).render();
        assertThat(busyHtml, containsString("aria-busy=\"true\""));

        String idleHtml = button("Save").withCondAriaBusy(false).render();
        assertThat(idleHtml, not(containsString("aria-busy")));

        String busyValHtml = button("Save").withCondAriaBusy(true, false).render();
        assertThat(busyValHtml, containsString("aria-busy=\"false\""));
    }

    @Test
    public void testAriaInvalid() {
        String invalidHtml = input().withAriaInvalid(true).render();
        assertThat(invalidHtml, containsString("aria-invalid=\"true\""));

        String validHtml = input().withAriaInvalid(false).render();
        assertThat(validHtml, containsString("aria-invalid=\"false\""));

        String isInvalidHtml = input().isAriaInvalid().render();
        assertThat(isInvalidHtml, containsString("aria-invalid=\"true\""));

        String textInvalidHtml = input().withAriaInvalid("grammar").render();
        assertThat(textInvalidHtml, containsString("aria-invalid=\"grammar\""));
    }

    @Test
    public void testCondAriaInvalid() {
        String invalidTrue = input().withCondAriaInvalid(true).render();
        assertThat(invalidTrue, containsString("aria-invalid=\"true\""));

        String invalidFalse = input().withCondAriaInvalid(false).render();
        assertThat(invalidFalse, not(containsString("aria-invalid")));

        String validCond = input().withCondAriaInvalid(true, false).render();
        assertThat(validCond, containsString("aria-invalid=\"false\""));
    }

    @Test
    public void testRoleHelpers() {
        String btnHtml = a("Button link").asButton().render();
        assertThat(btnHtml, containsString("role=\"button\""));

        String groupHtml = div().asGroup().render();
        assertThat(groupHtml, containsString("role=\"group\""));

        String searchHtml = form().asSearch().render();
        assertThat(searchHtml, containsString("role=\"search\""));

        String customRole = div().withRole("feed").render();
        assertThat(customRole, containsString("role=\"feed\""));
    }

    @Test
    public void testCondRoleHelpers() {
        String btnHtml = a("Button link").withCondAsButton(true).render();
        assertThat(btnHtml, containsString("role=\"button\""));

        String notBtnHtml = a("Regular link").withCondAsButton(false).render();
        assertThat(notBtnHtml, not(containsString("role")));

        String groupHtml = div().withCondAsGroup(true).render();
        assertThat(groupHtml, containsString("role=\"group\""));

        String notGroupHtml = div().withCondAsGroup(false).render();
        assertThat(notGroupHtml, not(containsString("role")));

        String searchHtml = form().withCondAsSearch(true).render();
        assertThat(searchHtml, containsString("role=\"search\""));

        String notSearchHtml = form().withCondAsSearch(false).render();
        assertThat(notSearchHtml, not(containsString("role")));
    }
}
