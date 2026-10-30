package info.freelibrary.j2htmx.pico;

import static info.freelibrary.j2htmx.pico.TagCreator.a;
import static info.freelibrary.j2htmx.pico.TagCreator.article;
import static info.freelibrary.j2htmx.pico.TagCreator.body;
import static info.freelibrary.j2htmx.pico.TagCreator.button;
import static info.freelibrary.j2htmx.pico.TagCreator.div;
import static info.freelibrary.j2htmx.pico.TagCreator.form;
import static info.freelibrary.j2htmx.pico.TagCreator.h1;
import static info.freelibrary.j2htmx.pico.TagCreator.h2;
import static info.freelibrary.j2htmx.pico.TagCreator.head;
import static info.freelibrary.j2htmx.pico.TagCreator.header;
import static info.freelibrary.j2htmx.pico.TagCreator.html;
import static info.freelibrary.j2htmx.pico.TagCreator.hxPartial;
import static info.freelibrary.j2htmx.pico.TagCreator.input;
import static info.freelibrary.j2htmx.pico.TagCreator.li;
import static info.freelibrary.j2htmx.pico.TagCreator.main;
import static info.freelibrary.j2htmx.pico.TagCreator.p;
import static info.freelibrary.j2htmx.pico.TagCreator.summary;
import static info.freelibrary.j2htmx.pico.TagCreator.title;
import static info.freelibrary.j2htmx.pico.TagCreator.ul;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertEquals;

import info.freelibrary.util.warnings.Checkstyle;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Pico} utilities and helpers.
 */
@SuppressWarnings({Checkstyle.MULTIPLE_STRING_LITERALS, "MultipleStringLiterals"})
public class PicoTest {

    /**
     * Tests the Pico CSS aliases.
     */
    @Test
    public void testPicoCssAliases() {
        assertEquals("<link rel=\"stylesheet\" href=\"https://cdn.jsdelivr.net/npm/@picocss/pico@2/css/pico.min.css\">",
          Pico.picoCss().render());
        assertEquals(
          "<link rel=\"stylesheet\" href=\"https://cdn.jsdelivr.net/npm/@picocss/pico@2.0.6/css/pico.min.css\">",
          Pico.picoCss("2.0.6").render());
        assertEquals(
          "<link rel=\"stylesheet\" href=\"https://cdn.jsdelivr.net/npm/@picocss/pico@2.0.6/css/pico.amber.min.css\">",
          Pico.picoCss("2.0.6", "amber").render());
        assertEquals(
          "<link rel=\"stylesheet\" href=\"https://cdn.jsdelivr.net/npm/@picocss/pico@2/css/pico.azure.min.css\">",
          Pico.picoCss(Pico.Color.AZURE).render());
        assertEquals(
          "<link rel=\"stylesheet\" href=\"https://cdn.jsdelivr.net/npm/@picocss/pico@2.1.0/css/pico.zinc.min.css\">",
          Pico.picoCss("2.1.0", Pico.Color.ZINC).render());
        assertThat(Pico.picoInlineCss().render(), containsString("Pico CSS"));
    }

    /**
     * Tests default CSS link generation.
     */
    @Test
    public void testCssLinkDefault() {
        final String html = Pico.css().render();
        assertThat(html,
          is("<link rel=\"stylesheet\" href=\"https://cdn.jsdelivr.net/npm/@picocss/pico@2/css/pico.min.css\">"));
    }

    /**
     * Tests inline CSS generation.
     */
    @Test
    public void testInlineCss() {
        final String html = Pico.inlineCss().render();
        assertThat(html, startsWith("<style>"));
        assertThat(html, endsWith("</style>"));
        assertThat(html, containsString("Pico CSS"));
        assertThat(html, containsString("--pico-font-family"));
    }

    /**
     * Tests CSS link with specific version.
     */
    @Test
    public void testCssLinkWithVersion() {
        final String html = Pico.css("2.0.6").render();
        assertThat(html,
          is("<link rel=\"stylesheet\" href=\"https://cdn.jsdelivr.net/npm/@picocss/pico@2.0.6/css/pico.min.css\">"));
    }

    /**
     * Tests CSS link with version and color scheme.
     */
    @Test
    public void testCssLinkWithVersionAndColorScheme() {
        final String html = Pico.css("2.0.6", "amber").render();
        assertThat(html, is(
          "<link rel=\"stylesheet\" href=\"https://cdn.jsdelivr.net/npm/@picocss/pico@2.0.6/css/pico.amber.min" +
          ".css\">"));
    }

    /**
     * Tests CSS link with Color enum.
     */
    @Test
    public void testCssLinkWithColorEnum() {
        final String htmlDefaultWithColor = Pico.css(Pico.Color.AZURE).render();
        assertThat(htmlDefaultWithColor,
          is("<link rel=\"stylesheet\" href=\"https://cdn.jsdelivr.net/npm/@picocss/pico@2/css/pico.azure.min.css\">"));

        final String htmlVersionedWithColor = Pico.css("2.1.0", Pico.Color.ZINC).render();
        assertThat(htmlVersionedWithColor, is(
          "<link rel=\"stylesheet\" href=\"https://cdn.jsdelivr.net/npm/@picocss/pico@2.1.0/css/pico.zinc.min.css\">"));
    }

    /**
     * Tests theme and color enums.
     */
    @Test
    public void testThemeAndColorEnums() {
        assertThat(Pico.Theme.LIGHT.getValue(), is("light"));
        assertThat(Pico.Theme.DARK.getValue(), is("dark"));
        assertThat(Pico.Theme.AUTO.getValue(), is("auto"));
        assertThat(Pico.Theme.LIGHT.toString(), is("light"));

        assertThat(Pico.Color.AMBER.getValue(), is("amber"));
        assertThat(Pico.Color.PUMPKIN.toString(), is("pumpkin"));
    }

    /**
     * Tests the container method.
     */
    @Test
    public void testContainer() {
        final String html = Pico.container(h1("Title")).render();
        assertThat(html, is("<main class=\"container\"><h1>Title</h1></main>"));

        final String textHtml = Pico.container("Plain text").render();
        assertThat(textHtml, is("<main class=\"container\">Plain text</main>"));
    }

    /**
     * Tests the fluid container method.
     */
    @Test
    public void testContainerFluid() {
        final String html = Pico.containerFluid(p("Full width")).render();
        assertThat(html, is("<main class=\"container-fluid\"><p>Full width</p></main>"));

        final String textHtml = Pico.containerFluid("Fluid text").render();
        assertThat(textHtml, is("<main class=\"container-fluid\">Fluid text</main>"));
    }

    /**
     * Tests the grid method.
     */
    @Test
    public void testGrid() {
        final String html = Pico.grid(div("Col 1"), div("Col 2")).render();
        assertThat(html, is("<div class=" + "\"grid\"" + "><div>Col 1</div><div>Col 2</div></div>"));
    }

    /**
     * Tests the dropdown method.
     */
    @Test
    public void testDropdown() {
        final String htmlWithSummaryTag = Pico.dropdown(summary("Options"), a("Link 1")).render();
        assertThat(htmlWithSummaryTag,
          is("<details class=\"dropdown\"><summary>Options</summary><a>Link 1</a></details>"));

        final String htmlWithStringSummary = Pico.dropdown("Actions", button("Action 1")).render();
        assertThat(htmlWithStringSummary,
          is("<details class=\"dropdown\"><summary>Actions</summary><button>Action 1</button></details>"));
    }

    /**
     * Tests the card method.
     */
    @Test
    public void testCard() {
        final String html = Pico.card(h2("Card Title"), p("Body content")).render();
        assertThat(html, is("<article><h2>Card Title</h2><p>Body content</p></article>"));

        final String textHtml = Pico.card("Simple card text").render();
        assertThat(textHtml, is("<article>Simple card text</article>"));
    }

    /**
     * Tests the modal method.
     */
    @Test
    public void testModal() {
        final String html = Pico.modal("modal-example", article("Modal text")).render();
        assertThat(html, is("<dialog id=\"modal-example\"><article>Modal text</article></dialog>"));

        final String htmlNoId = Pico.modal(article("No id")).render();
        assertThat(htmlNoId, is("<dialog><article>No id</article></dialog>"));
    }

    /**
     * Tests HTMX and Pico CSS synergy.
     */
    @Test
    public void testHtmxAndPicoSynergy() {
        final String html = Pico.grid(
          button("Save")
            .asButton()
            .withAriaBusy(true)
            .withHxPost("/api/save")
            .withHxTarget("#result")
        ).render();

        assertThat(html, containsString("class=\"grid\""));
        assertThat(html, containsString("role=\"button\""));
        assertThat(html, containsString("aria-busy=\"true\""));
        assertThat(html, containsString("hx-post=\"/api/save\""));
        assertThat(html, containsString("hx-target=\"#result\""));
    }

    /**
     * Tests README examples.
     */
    @Test
    public void testReadmeExamples() {
        // Basic Use Example
        final String basicHtml = html(
          head(
            title("Basic Example")
          ),
          body(
            header(
              h1("Welcome to j2htmx-pico")
            ),
            main(
              p("Generate type-safe HTML directly in Java with minimal runtime overhead."),
              ul(
                li("Compile-time safety for element hierarchies"),
                li("Zero reflection or external template parsing costs"),
                li("Fluent tag chaining and conditional attributes")
              )
            )
          )
        ).render();
        assertThat(basicHtml, containsString("<h1>Welcome to j2htmx-pico</h1>"));

        // HTMX Example
        final String htmxBtn = button("Load Items")
          .withHxGet("/api/items")
          .withHxTarget("#item-list")
          .withHxSwap("innerHTML")
          .withHxIndicator("#loading-spinner")
          .render();
        assertThat(htmxBtn, containsString("hx-get=\"/api/items\""));

        final String partialResponse = hxPartial(
          div("Update successful!").withId("status-message"),
          ul(li("Updated item 1"), li("Updated item 2")).withId("item-list")
        ).render();
        assertThat(partialResponse, containsString("<hx-partial"));

        // Pico CSS Example
        final String page = html(
          head(
            title("Pico CSS Dashboard"),
            Pico.css()
          ),
          body(
            Pico.container(
              h2("Interactive Form"),
              Pico.card(
                form(
                  Pico.grid(
                    input().withType("text").withName("name").withPlaceholder("Name"),
                    input().withType("email").withName("email").withPlaceholder("Email")
                  ),
                  button("Submit")
                    .asButton()
                    .withAriaBusy(true)
                    .withDataTooltip("Click to save record")
                    .withDataPlacement("top")
                    .withHxPost("/api/users")
                    .withHxTarget("#result")
                )
              ),
              div().withId("result")
            )
          )
        ).withDataTheme("dark").render();
        assertThat(page, containsString("data-theme=\"dark\""));
        assertThat(page, containsString("class=\"container\""));
        assertThat(page, containsString("class=\"grid\""));
        assertThat(page, containsString("role=\"button\""));
        assertThat(page, containsString("aria-busy=\"true\""));
    }
}
