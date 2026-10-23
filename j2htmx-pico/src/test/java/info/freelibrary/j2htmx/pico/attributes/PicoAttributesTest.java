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

import info.freelibrary.util.warnings.Checkstyle;
import org.junit.jupiter.api.Test;

/**
 * Tests for Pico CSS attributes and ARIA helpers.
 */
@SuppressWarnings({Checkstyle.MULTIPLE_STRING_LITERALS, "MultipleStringLiterals"})
public class PicoAttributesTest {

    /**
     * Tests data-theme attribute.
     */
    @Test
    public void testDataTheme() {
        final String html = html().withDataTheme("dark").render();
        assertThat(html, containsString("data-theme=\"dark\""));
    }

    /**
     * Tests conditional data-theme attribute.
     */
    @Test
    public void testCondDataTheme() {
        final String htmlTrue = html().withCondDataTheme(true, "light").render();
        assertThat(htmlTrue, containsString("data-theme=\"light\""));

        final String htmlFalse = html().withCondDataTheme(false, "light").render();
        assertThat(htmlFalse, not(containsString("data-theme")));
    }

    /**
     * Tests data-tooltip and data-placement attributes.
     */
    @Test
    public void testTooltipAndPlacement() {
        final String html = button("Submit")
          .withDataTooltip("Helpful text")
          .withDataPlacement("bottom")
          .render();
        assertThat(html, containsString("data-tooltip=\"Helpful text\""));
        assertThat(html, containsString("data-placement=\"bottom\""));
    }

    /**
     * Tests conditional data-tooltip and data-placement attributes.
     */
    @Test
    public void testCondTooltipAndPlacement() {
        final String html = button("Submit")
          .withCondDataTooltip(true, "Visible tooltip")
          .withCondDataPlacement(false, "top")
          .render();
        assertThat(html, containsString("data-tooltip=\"Visible tooltip\""));
        assertThat(html, not(containsString("data-placement")));
    }

    /**
     * Tests aria-busy attribute.
     */
    @Test
    public void testAriaBusy() {
        final String busyHtml = button("Loading").withAriaBusy(true).render();
        assertThat(busyHtml, containsString("aria-busy=\"true\""));

        final String notBusyHtml = button("Loaded").withAriaBusy(false).render();
        assertThat(notBusyHtml, containsString("aria-busy=\"false\""));
    }

    /**
     * Tests conditional aria-busy attribute.
     */
    @Test
    public void testCondAriaBusy() {
        final String busyHtml = button("Save").withCondAriaBusy(true).render();
        assertThat(busyHtml, containsString("aria-busy=\"true\""));

        final String idleHtml = button("Save").withCondAriaBusy(false).render();
        assertThat(idleHtml, not(containsString("aria-busy")));

        final String busyValHtml = button("Save").withCondAriaBusy(true, false).render();
        assertThat(busyValHtml, containsString("aria-busy=\"false\""));
    }

    /**
     * Tests aria-hidden attribute.
     */
    @Test
    public void testAriaHidden() {
        final String notHiddenHtml = button("Visible").withAriaHidden(false).render();
        assertThat(notHiddenHtml, containsString("aria-hidden=\"false\""));

        final String hiddenBoolHtml = button("Hidden").withAriaHidden(true).render();
        assertThat(hiddenBoolHtml, containsString("aria-hidden=\"true\""));

        final String hiddenStrHtml = button("Hidden").withAriaHidden("true").render();
        assertThat(hiddenStrHtml, containsString("aria-hidden=\"true\""));
    }

    /**
     * Tests conditional aria-hidden attribute.
     */
    @Test
    public void testCondAriaHidden() {
        final String hiddenHtml = button("Save").withCondAriaHidden(true).render();
        assertThat(hiddenHtml, containsString("aria-hidden=\"true\""));

        final String idleHtml = button("Save").withCondAriaHidden(false).render();
        assertThat(idleHtml, not(containsString("aria-hidden")));

        final String hiddenValHtml = button("Save").withCondAriaHidden(true, false).render();
        assertThat(hiddenValHtml, containsString("aria-hidden=\"false\""));

        final String notHiddenValHtml = button("Save").withCondAriaHidden(false, false).render();
        assertThat(notHiddenValHtml, not(containsString("aria-hidden")));

        final String hiddenStrHtml = button("Save").withCondAriaHidden(true, "true").render();
        assertThat(hiddenStrHtml, containsString("aria-hidden=\"true\""));

        final String notHiddenStrHtml = button("Save").withCondAriaHidden(false, "true").render();
        assertThat(notHiddenStrHtml, not(containsString("aria-hidden")));
    }

    /**
     * Tests aria-invalid attribute.
     */
    @Test
    public void testAriaInvalid() {
        final String invalidHtml = input().withAriaInvalid(true).render();
        assertThat(invalidHtml, containsString("aria-invalid=\"true\""));

        final String validHtml = input().withAriaInvalid(false).render();
        assertThat(validHtml, containsString("aria-invalid=\"false\""));

        final String textInvalidHtml = input().withAriaInvalid("grammar").render();
        assertThat(textInvalidHtml, containsString("aria-invalid=\"grammar\""));
    }

    /**
     * Tests conditional aria-invalid attribute.
     */
    @Test
    public void testCondAriaInvalid() {
        final String invalidTrue = input().withCondAriaInvalid(true).render();
        assertThat(invalidTrue, containsString("aria-invalid=\"true\""));

        final String invalidFalse = input().withCondAriaInvalid(false).render();
        assertThat(invalidFalse, not(containsString("aria-invalid")));

        final String validCond = input().withCondAriaInvalid(true, false).render();
        assertThat(validCond, containsString("aria-invalid=\"false\""));
    }

    /**
     * Tests role helper attributes.
     */
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

    /**
     * Tests conditional role helper attributes.
     */
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
