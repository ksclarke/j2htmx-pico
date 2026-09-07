package info.freelibrary.j2htmx.pico;

import info.freelibrary.j2htmx.pico.tags.ContainerTag;
import info.freelibrary.j2htmx.pico.tags.DomContent;
import info.freelibrary.j2htmx.pico.tags.InlineStaticResource;
import info.freelibrary.j2htmx.pico.tags.specialized.ArticleTag;
import info.freelibrary.j2htmx.pico.tags.specialized.DetailsTag;
import info.freelibrary.j2htmx.pico.tags.specialized.DialogTag;
import info.freelibrary.j2htmx.pico.tags.specialized.DivTag;
import info.freelibrary.j2htmx.pico.tags.specialized.LinkTag;
import info.freelibrary.j2htmx.pico.tags.specialized.MainTag;
import info.freelibrary.j2htmx.pico.tags.specialized.SummaryTag;

import java.util.Locale;

/**
 * Utility class and helpers for Pico.css v2 integration.
 */
public final class Pico {

    public static final String CONTAINER = "container";
    public static final String CONTAINER_FLUID = "container-fluid";
    public static final String GRID = "grid";
    public static final String DROPDOWN = "dropdown";
    public static final String SECONDARY = "secondary";
    public static final String CONTRAST = "contrast";
    public static final String OUTLINE = "outline";

    // Color palettes
    public static final String AMBER = "amber";
    public static final String AZURE = "azure";
    public static final String BLUE = "blue";
    public static final String CYAN = "cyan";
    public static final String FUCHSIA = "fuchsia";
    public static final String GREEN = "green";
    public static final String GREY = "grey";
    public static final String INDIGO = "indigo";
    public static final String JADE = "jade";
    public static final String LIME = "lime";
    public static final String ORANGE = "orange";
    public static final String PINK = "pink";
    public static final String PUMPKIN = "pumpkin";
    public static final String PURPLE = "purple";
    public static final String RED = "red";
    public static final String SAND = "sand";
    public static final String SLATE = "slate";
    public static final String VIOLET = "violet";
    public static final String YELLOW = "yellow";
    public static final String ZINC = "zinc";

    public static final String DEFAULT_VERSION = "2";
    public static final String CDN_URL_PREFIX = "https://cdn.jsdelivr.net/npm/@picocss/pico@";
    public static final String BUNDLED_CSS_PATH = "/pico/pico.min.css";

    private Pico() {
    }

    /**
     * Theme options for Pico CSS.
     */
    public enum Theme {
        LIGHT("light"),
        DARK("dark"),
        AUTO("auto");

        private final String myValue;

        Theme(final String aValue) {
            myValue = aValue;
        }

        public String getValue() {
            return myValue;
        }

        @Override
        public String toString() {
            return myValue;
        }
    }

    /**
     * Color scheme variants supported by Pico CSS v2.
     */
    public enum Color {
        AMBER("amber"),
        AZURE("azure"),
        BLUE("blue"),
        CYAN("cyan"),
        FUCHSIA("fuchsia"),
        GREEN("green"),
        GREY("grey"),
        INDIGO("indigo"),
        JADE("jade"),
        LIME("lime"),
        ORANGE("orange"),
        PINK("pink"),
        PUMPKIN("pumpkin"),
        PURPLE("purple"),
        RED("red"),
        SAND("sand"),
        SLATE("slate"),
        VIOLET("violet"),
        YELLOW("yellow"),
        ZINC("zinc");

        private final String myValue;

        Color(final String aValue) {
            myValue = aValue;
        }

        public String getValue() {
            return myValue;
        }

        @Override
        public String toString() {
            return myValue;
        }
    }

    /**
     * Creates a {@code <link>} stylesheet pointing to the default CDN Pico CSS v2.
     *
     * @return A LinkTag element
     */
    public static LinkTag css() {
        return css(DEFAULT_VERSION, (String) null);
    }

    /**
     * Creates a {@code <link>} stylesheet pointing to the CDN Pico CSS with a specified version.
     *
     * @param version The Pico CSS version (e.g., "2", "2.0.6")
     * @return A LinkTag element
     */
    public static LinkTag css(final String version) {
        return css(version, (String) null);
    }

    /**
     * Creates a {@code <link>} stylesheet pointing to the CDN Pico CSS with a specified color scheme.
     *
     * @param colorScheme The color scheme palette
     * @return A LinkTag element
     */
    public static LinkTag css(final Color colorScheme) {
        return css(DEFAULT_VERSION, colorScheme != null ? colorScheme.getValue() : null);
    }

    /**
     * Creates a {@code <link>} stylesheet pointing to the CDN Pico CSS with a specified version and color scheme.
     *
     * @param version The Pico CSS version
     * @param colorScheme The color scheme palette
     * @return A LinkTag element
     */
    public static LinkTag css(final String version, final Color colorScheme) {
        return css(version, colorScheme != null ? colorScheme.getValue() : null);
    }

    /**
     * Creates a {@code <link>} stylesheet pointing to the CDN Pico CSS with a specified version and color scheme name.
     *
     * @param version The Pico CSS version
     * @param colorScheme The color scheme name (e.g., "amber", "zinc")
     * @return A LinkTag element
     */
    public static LinkTag css(final String version, final String colorScheme) {
        final String ver = (version == null || version.isEmpty()) ? DEFAULT_VERSION : version;
        final String href;
        if (colorScheme != null && !colorScheme.trim().isEmpty()) {
            href = CDN_URL_PREFIX + ver + "/css/pico." + colorScheme.trim().toLowerCase(Locale.ROOT) + ".min.css";
        } else {
            href = CDN_URL_PREFIX + ver + "/css/pico.min.css";
        }
        return TagCreator.link().withRel("stylesheet").withHref(href);
    }

    /**
     * Loads the bundled minified Pico CSS stylesheet from resources as an inline {@code <style>} element.
     *
     * @return A ContainerTag containing the inlined CSS
     */
    public static ContainerTag<?> inlineCss() {
        return InlineStaticResource.get(BUNDLED_CSS_PATH, InlineStaticResource.TargetFormat.CSS);
    }

    /**
     * Creates a centered, responsive container {@code <main class="container">}.
     *
     * @param content Elements inside the container
     * @return A MainTag with the container class
     */
    public static MainTag container(final DomContent... content) {
        return TagCreator.main().withClass(CONTAINER).with(content);
    }

    /**
     * Creates a centered, responsive container {@code <main class="container">} with text content.
     *
     * @param text Text inside the container
     * @return A MainTag with the container class
     */
    public static MainTag container(final String text) {
        return TagCreator.main().withClass(CONTAINER).withText(text);
    }

    /**
     * Creates a fluid, full-width container {@code <main class="container-fluid">}.
     *
     * @param content Elements inside the fluid container
     * @return A MainTag with the container-fluid class
     */
    public static MainTag containerFluid(final DomContent... content) {
        return TagCreator.main().withClass(CONTAINER_FLUID).with(content);
    }

    /**
     * Creates a fluid, full-width container {@code <main class="container-fluid">} with text content.
     *
     * @param text Text inside the fluid container
     * @return A MainTag with the container-fluid class
     */
    public static MainTag containerFluid(final String text) {
        return TagCreator.main().withClass(CONTAINER_FLUID).withText(text);
    }

    /**
     * Creates a responsive grid layout {@code <div class="grid">}.
     *
     * @param content Elements inside the grid
     * @return A DivTag with the grid class
     */
    public static DivTag grid(final DomContent... content) {
        return TagCreator.div().withClass(GRID).with(content);
    }

    /**
     * Creates a responsive grid layout {@code <div class="grid">} with text content.
     *
     * @param text Text inside the grid
     * @return A DivTag with the grid class
     */
    public static DivTag grid(final String text) {
        return TagCreator.div().withClass(GRID).withText(text);
    }

    /**
     * Creates a dropdown menu {@code <details class="dropdown">}.
     *
     * @param summary The summary element or label
     * @param content Dropdown items/menu content
     * @return A DetailsTag with the dropdown class
     */
    public static DetailsTag dropdown(final DomContent summary, final DomContent... content) {
        final DomContent summaryTag = (summary instanceof SummaryTag) ? summary : TagCreator.summary(summary);
        return TagCreator.details().withClass(DROPDOWN).with(summaryTag).with(content);
    }

    /**
     * Creates a dropdown menu {@code <details class="dropdown">} with a summary title.
     *
     * @param summaryText The summary label
     * @param content Dropdown items/menu content
     * @return A DetailsTag with the dropdown class
     */
    public static DetailsTag dropdown(final String summaryText, final DomContent... content) {
        return dropdown(TagCreator.summary(summaryText), content);
    }

    /**
     * Creates a semantic card container using {@code <article>}.
     *
     * @param content Content of the card
     * @return An ArticleTag element
     */
    public static ArticleTag card(final DomContent... content) {
        return TagCreator.article(content);
    }

    /**
     * Creates a semantic card container using {@code <article>} with text content.
     *
     * @param text Text content of the card
     * @return An ArticleTag element
     */
    public static ArticleTag card(final String text) {
        return TagCreator.article(text);
    }

    /**
     * Creates a modal dialog {@code <dialog id="...">}.
     *
     * @param id The id for the modal dialog
     * @param content Elements inside the modal
     * @return A DialogTag element
     */
    public static DialogTag modal(final String id, final DomContent... content) {
        final DialogTag dialog = TagCreator.dialog().with(content);
        return (id != null && !id.isEmpty()) ? dialog.withId(id) : dialog;
    }

    /**
     * Creates a modal dialog {@code <dialog>}.
     *
     * @param content Elements inside the modal
     * @return A DialogTag element
     */
    public static DialogTag modal(final DomContent... content) {
        return TagCreator.dialog().with(content);
    }
}
