package info.freelibrary.j2htmx.pico;

import org.junit.jupiter.api.Test;

import static info.freelibrary.j2htmx.pico.TagCreator.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class PicoTest {

    @Test
    public void testCssLinkDefault() {
        String html = Pico.css().render();
        assertThat(html, is("<link rel=\"stylesheet\" href=\"https://cdn.jsdelivr.net/npm/@picocss/pico@2/css/pico.min.css\">"));
    }

    @Test
    public void testInlineCss() {
        String html = Pico.inlineCss().render();
        assertThat(html, startsWith("<style>"));
        assertThat(html, endsWith("</style>"));
        assertThat(html, containsString("Pico CSS"));
        assertThat(html, containsString("--pico-font-family"));
    }

    @Test
    public void testCssLinkWithVersion() {
        String html = Pico.css("2.0.6").render();
        assertThat(html, is("<link rel=\"stylesheet\" href=\"https://cdn.jsdelivr.net/npm/@picocss/pico@2.0.6/css/pico.min.css\">"));
    }

    @Test
    public void testCssLinkWithVersionAndColorScheme() {
        String html = Pico.css("2.0.6", "amber").render();
        assertThat(html, is("<link rel=\"stylesheet\" href=\"https://cdn.jsdelivr.net/npm/@picocss/pico@2.0.6/css/pico.amber.min.css\">"));
    }

    @Test
    public void testCssLinkWithColorEnum() {
        String htmlDefaultWithColor = Pico.css(Pico.Color.AZURE).render();
        assertThat(htmlDefaultWithColor, is("<link rel=\"stylesheet\" href=\"https://cdn.jsdelivr.net/npm/@picocss/pico@2/css/pico.azure.min.css\">"));

        String htmlVersionedWithColor = Pico.css("2.1.0", Pico.Color.ZINC).render();
        assertThat(htmlVersionedWithColor, is("<link rel=\"stylesheet\" href=\"https://cdn.jsdelivr.net/npm/@picocss/pico@2.1.0/css/pico.zinc.min.css\">"));
    }

    @Test
    public void testThemeAndColorEnums() {
        assertThat(Pico.Theme.LIGHT.getValue(), is("light"));
        assertThat(Pico.Theme.DARK.getValue(), is("dark"));
        assertThat(Pico.Theme.AUTO.getValue(), is("auto"));
        assertThat(Pico.Theme.LIGHT.toString(), is("light"));

        assertThat(Pico.Color.AMBER.getValue(), is("amber"));
        assertThat(Pico.Color.PUMPKIN.toString(), is("pumpkin"));
    }

    @Test
    public void testContainer() {
        String html = Pico.container(h1("Title")).render();
        assertThat(html, is("<main class=\"container\"><h1>Title</h1></main>"));

        String textHtml = Pico.container("Plain text").render();
        assertThat(textHtml, is("<main class=\"container\">Plain text</main>"));
    }

    @Test
    public void testContainerFluid() {
        String html = Pico.containerFluid(p("Full width")).render();
        assertThat(html, is("<main class=\"container-fluid\"><p>Full width</p></main>"));

        String textHtml = Pico.containerFluid("Fluid text").render();
        assertThat(textHtml, is("<main class=\"container-fluid\">Fluid text</main>"));
    }

    @Test
    public void testGrid() {
        String html = Pico.grid(div("Col 1"), div("Col 2")).render();
        assertThat(html, is("<div class=" + "\"grid\"" + "><div>Col 1</div><div>Col 2</div></div>"));
    }

    @Test
    public void testDropdown() {
        String htmlWithSummaryTag = Pico.dropdown(summary("Options"), a("Link 1")).render();
        assertThat(htmlWithSummaryTag, is("<details class=\"dropdown\"><summary>Options</summary><a>Link 1</a></details>"));

        String htmlWithStringSummary = Pico.dropdown("Actions", button("Action 1")).render();
        assertThat(htmlWithStringSummary, is("<details class=\"dropdown\"><summary>Actions</summary><button>Action 1</button></details>"));
    }

    @Test
    public void testCard() {
        String html = Pico.card(h2("Card Title"), p("Body content")).render();
        assertThat(html, is("<article><h2>Card Title</h2><p>Body content</p></article>"));

        String textHtml = Pico.card("Simple card text").render();
        assertThat(textHtml, is("<article>Simple card text</article>"));
    }

    @Test
    public void testModal() {
        String html = Pico.modal("modal-example", article("Modal text")).render();
        assertThat(html, is("<dialog id=\"modal-example\"><article>Modal text</article></dialog>"));

        String htmlNoId = Pico.modal(article("No id")).render();
        assertThat(htmlNoId, is("<dialog><article>No id</article></dialog>"));
    }

    @Test
    public void testHtmxAndPicoSynergy() {
        String html = Pico.grid(
            button("Save")
                .asButton()
                .isAriaBusy()
                .withHxPost("/api/save")
                .withHxTarget("#result")
        ).render();

        assertThat(html, containsString("class=\"grid\""));
        assertThat(html, containsString("role=\"button\""));
        assertThat(html, containsString("aria-busy=\"true\""));
        assertThat(html, containsString("hx-post=\"/api/save\""));
        assertThat(html, containsString("hx-target=\"#result\""));
    }
}
