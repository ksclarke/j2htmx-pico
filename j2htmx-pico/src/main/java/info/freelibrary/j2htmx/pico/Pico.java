package info.freelibrary.j2htmx.pico;

import info.freelibrary.j2htmx.pico.tags.AbstractDomContent;
import info.freelibrary.j2htmx.pico.tags.ContainerTag;
import info.freelibrary.j2htmx.pico.tags.InlineStaticResource;
import info.freelibrary.j2htmx.pico.tags.specialized.ArticleTag;
import info.freelibrary.j2htmx.pico.tags.specialized.DetailsTag;
import info.freelibrary.j2htmx.pico.tags.specialized.DialogTag;
import info.freelibrary.j2htmx.pico.tags.specialized.DivTag;
import info.freelibrary.j2htmx.pico.tags.specialized.LinkTag;
import info.freelibrary.j2htmx.pico.tags.specialized.MainTag;
import info.freelibrary.j2htmx.pico.tags.specialized.SummaryTag;
import info.freelibrary.util.warnings.Checkstyle;
import info.freelibrary.util.warnings.PMD;

import java.util.Locale;

/**
 * Utility class and helpers for Pico.css v2 integration.
 */
@SuppressWarnings({PMD.TOO_MANY_METHODS, Checkstyle.MULTIPLE_STRING_LITERALS, "MultipleStringLiterals"})
public final class Pico {

    /** Standard responsive container class. */
    @SuppressWarnings(PMD.AVOID_FIELD_NAME_MATCHING_METHOD_NAME)
    public static final String CONTAINER = "container";

    /** Fluid, full-width container class. */
    public static final String CONTAINER_FLUID = "container-fluid";

    /** Responsive grid layout class. */
    @SuppressWarnings(PMD.AVOID_FIELD_NAME_MATCHING_METHOD_NAME)
    public static final String GRID = "grid";

    /** Dropdown menu details class. */
    @SuppressWarnings(PMD.AVOID_FIELD_NAME_MATCHING_METHOD_NAME)
    public static final String DROPDOWN = "dropdown";

    /**
     * Secondary button/element styling class.
     */
    public static final String SECONDARY = "secondary";

    /**
     * Contrast button/element styling class.
     */
    public static final String CONTRAST = "contrast";

    /**
     * Outline button styling class.
     */
    public static final String OUTLINE = "outline";

    // Color palettes
    /** Amber color palette. */
    public static final String AMBER = "amber";

    /** Azure color palette. */
    public static final String AZURE = "azure";

    /** Blue color palette. */
    public static final String BLUE = "blue";

    /** Cyan color palette. */
    public static final String CYAN = "cyan";

    /** Fuchsia color palette. */
    public static final String FUCHSIA = "fuchsia";

    /** Green color palette. */
    public static final String GREEN = "green";

    /** Grey color palette. */
    public static final String GREY = "grey";

    /** Indigo color palette. */
    public static final String INDIGO = "indigo";

    /** Jade color palette. */
    public static final String JADE = "jade";

    /** Lime color palette. */
    public static final String LIME = "lime";

    /** Orange color palette. */
    public static final String ORANGE = "orange";

    /** Pink color palette. */
    public static final String PINK = "pink";

    /** Pumpkin color palette. */
    public static final String PUMPKIN = "pumpkin";

    /** Purple color palette. */
    public static final String PURPLE = "purple";

    /** Red color palette. */
    public static final String RED = "red";

    /** Sand color palette. */
    public static final String SAND = "sand";

    /** Slate color palette. */
    public static final String SLATE = "slate";

    /** Violet color palette. */
    public static final String VIOLET = "violet";

    /** Yellow color palette. */
    public static final String YELLOW = "yellow";

    /** Zinc color palette. */
    public static final String ZINC = "zinc";

    /** Default major version of Pico CSS. */
    public static final String DEFAULT_VERSION = "2";

    /** Prefix URL for the jsDelivr CDN hosting Pico CSS. */
    public static final String CDN_URL_PREFIX = "https://cdn.jsdelivr.net/npm/@picocss/pico@";

    /** Classpath resource path to the bundled minified Pico CSS stylesheet. */
    public static final String BUNDLED_CSS_PATH = "/pico/pico.min.css";

    /**
     * Constructs a new Pico instance.
     */
    private Pico() {
        // This is intentionally left empty
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
     * @param aVersion The Pico CSS version (e.g., "2", "2.0.6")
     * @return A LinkTag element
     */
    public static LinkTag css(final String aVersion) {
        return css(aVersion, (String) null);
    }

    /**
     * Creates a {@code <link>} stylesheet pointing to the CDN Pico CSS with a specified color scheme.
     *
     * @param aColorScheme The color scheme palette
     * @return A LinkTag element
     */
    public static LinkTag css(final Color aColorScheme) {
        return css(DEFAULT_VERSION, aColorScheme != null ? aColorScheme.getValue() : null);
    }

    /**
     * Creates a {@code <link>} stylesheet pointing to the CDN Pico CSS with a specified version and color scheme.
     *
     * @param aVersion The Pico CSS version
     * @param aColorScheme The color scheme palette
     * @return A LinkTag element
     */
    public static LinkTag css(final String aVersion, final Color aColorScheme) {
        return css(aVersion, aColorScheme != null ? aColorScheme.getValue() : null);
    }

    /**
     * Creates a {@code <link>} stylesheet pointing to the CDN Pico CSS with a specified version and color scheme name.
     *
     * @param aVersion The Pico CSS version
     * @param aColorScheme The color scheme name (e.g., "amber", "zinc")
     * @return A LinkTag element
     */
    public static LinkTag css(final String aVersion, final String aColorScheme) {
        final String ver = (aVersion == null || aVersion.isEmpty()) ? DEFAULT_VERSION : aVersion;
        final String href;

        if (aColorScheme != null && !aColorScheme.isBlank()) {
            href = CDN_URL_PREFIX + ver + "/css/pico." + aColorScheme.trim().toLowerCase(Locale.ROOT) + ".min.css";
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
     * Creates a {@code <link>} stylesheet pointing to the default CDN Pico CSS v2.
     *
     * @return A LinkTag referencing the Pico CSS stylesheet
     */
    public static LinkTag picoCss() {
        return css();
    }

    /**
     * Creates a {@code <link>} stylesheet pointing to the CDN Pico CSS with a specified version.
     *
     * @param aVersion The Pico CSS version (e.g., "2", "2.0.6")
     * @return A LinkTag referencing the versioned Pico CSS stylesheet
     */
    public static LinkTag picoCss(final String aVersion) {
        return css(aVersion);
    }

    /**
     * Creates a {@code <link>} stylesheet pointing to the CDN Pico CSS with a specified color scheme.
     *
     * @param aColorScheme The color scheme palette
     * @return A LinkTag referencing the themed Pico CSS stylesheet
     */
    public static LinkTag picoCss(final Color aColorScheme) {
        return css(aColorScheme);
    }

    /**
     * Creates a {@code <link>} stylesheet pointing to the CDN Pico CSS with a specified version and color scheme.
     *
     * @param aVersion The Pico CSS version
     * @param aColorScheme The color scheme palette
     * @return A LinkTag referencing the versioned and themed Pico CSS stylesheet
     */
    public static LinkTag picoCss(final String aVersion, final Color aColorScheme) {
        return css(aVersion, aColorScheme);
    }

    /**
     * Creates a {@code <link>} stylesheet pointing to the CDN Pico CSS with a specified version and color scheme name.
     *
     * @param aVersion The Pico CSS version
     * @param aColorScheme The color scheme name (e.g., "amber", "zinc")
     * @return A LinkTag referencing the themed Pico CSS stylesheet
     */
    public static LinkTag picoCss(final String aVersion, final String aColorScheme) {
        return css(aVersion, aColorScheme);
    }

    /**
     * Loads the bundled minified Pico CSS stylesheet from resources as an inline {@code <style>} element.
     *
     * @return A ContainerTag containing the inlined CSS
     */
    public static ContainerTag<?> picoInlineCss() {
        return inlineCss();
    }

    /**
     * Creates a centered, responsive container {@code <main class="container">}.
     *
     * @param aContent Elements inside the container
     * @return A MainTag with the container class
     */
    public static MainTag container(final AbstractDomContent... aContent) {
        return TagCreator.main().withClass(CONTAINER).with(aContent);
    }

    /**
     * Creates a centered, responsive container {@code <main class="container">} with text content.
     *
     * @param aText Text inside the container
     * @return A MainTag with the container class
     */
    public static MainTag container(final String aText) {
        return TagCreator.main().withClass(CONTAINER).withText(aText);
    }

    /**
     * Creates a fluid, full-width container {@code <main class="container-fluid">}.
     *
     * @param aContent Elements inside the fluid container
     * @return A MainTag with the container-fluid class
     */
    public static MainTag containerFluid(final AbstractDomContent... aContent) {
        return TagCreator.main().withClass(CONTAINER_FLUID).with(aContent);
    }

    /**
     * Creates a fluid, full-width container {@code <main class="container-fluid">} with text content.
     *
     * @param aText Text inside the fluid container
     * @return A MainTag with the container-fluid class
     */
    public static MainTag containerFluid(final String aText) {
        return TagCreator.main().withClass(CONTAINER_FLUID).withText(aText);
    }

    /**
     * Creates a responsive grid layout {@code <div class="grid">}.
     *
     * @param aContent Elements inside the grid
     * @return A DivTag with the grid class
     */
    public static DivTag grid(final AbstractDomContent... aContent) {
        return TagCreator.div().withClass(GRID).with(aContent);
    }

    /**
     * Creates a responsive grid layout {@code <div class="grid">} with text content.
     *
     * @param aText Text inside the grid
     * @return A DivTag with the grid class
     */
    public static DivTag grid(final String aText) {
        return TagCreator.div().withClass(GRID).withText(aText);
    }

    /**
     * Creates a dropdown menu {@code <details class="dropdown">}.
     *
     * @param aSummary The summary element or label
     * @param aContent Dropdown items/menu content
     * @return A DetailsTag with the dropdown class
     */
    public static DetailsTag dropdown(final AbstractDomContent aSummary, final AbstractDomContent... aContent) {
        final AbstractDomContent summaryTag = (aSummary instanceof SummaryTag)
          ? aSummary : TagCreator.summary(aSummary);
        return TagCreator.details().withClass(DROPDOWN).with(summaryTag).with(aContent);
    }

    /**
     * Creates a dropdown menu {@code <details class="dropdown">} with a summary title.
     *
     * @param aSummaryText The summary label
     * @param aContent Dropdown items/menu content
     * @return A DetailsTag with the dropdown class
     */
    public static DetailsTag dropdown(final String aSummaryText, final AbstractDomContent... aContent) {
        return dropdown(TagCreator.summary(aSummaryText), aContent);
    }

    /**
     * Creates a semantic card container using {@code <article>}.
     *
     * @param aContent Content of the card
     * @return An ArticleTag element
     */
    public static ArticleTag card(final AbstractDomContent... aContent) {
        return TagCreator.article(aContent);
    }

    /**
     * Creates a semantic card container using {@code <article>} with text content.
     *
     * @param aText Text content of the card
     * @return An ArticleTag element
     */
    public static ArticleTag card(final String aText) {
        return TagCreator.article(aText);
    }

    /**
     * Creates a modal dialog {@code <dialog id="...">}.
     *
     * @param anId The id for the modal dialog
     * @param aContent Elements inside the modal
     * @return A DialogTag element
     */
    public static DialogTag modal(final String anId, final AbstractDomContent... aContent) {
        final DialogTag dialog = TagCreator.dialog().with(aContent);
        return (anId != null && !anId.isEmpty()) ? dialog.withId(anId) : dialog;
    }

    /**
     * Creates a modal dialog {@code <dialog>}.
     *
     * @param aContent Elements inside the modal
     * @return A DialogTag element
     */
    public static DialogTag modal(final AbstractDomContent... aContent) {
        return TagCreator.dialog().with(aContent);
    }

    /**
     * Theme options for Pico CSS.
     */
    public enum Theme {
        /** Light theme mode. */
        LIGHT("light"),
        /** Dark theme mode. */
        DARK("dark"),
        /** Automatic theme mode following system preferences. */
        AUTO("auto");

        /** The theme string value. */
        private final String myValue;

        /**
         * Constructs a Theme option.
         *
         * @param aValue The theme string value
         */
        Theme(final String aValue) {
            myValue = aValue;
        }

        /**
         * Gets the CSS value string of this theme.
         *
         * @return The theme string value
         */
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
        /** Amber color palette. */
        AMBER("amber"),
        /** Azure color palette. */
        AZURE("azure"),
        /** Blue color palette. */
        BLUE("blue"),
        /** Cyan color palette. */
        CYAN("cyan"),
        /** Fuchsia color palette. */
        FUCHSIA("fuchsia"),
        /** Green color palette. */
        GREEN("green"),
        /** Grey color palette. */
        GREY("grey"),
        /** Indigo color palette. */
        INDIGO("indigo"),
        /** Jade color palette. */
        JADE("jade"),
        /** Lime color palette. */
        LIME("lime"),
        /** Orange color palette. */
        ORANGE("orange"),
        /** Pink color palette. */
        PINK("pink"),
        /** Pumpkin color palette. */
        PUMPKIN("pumpkin"),
        /** Purple color palette. */
        PURPLE("purple"),
        /** Red color palette. */
        RED("red"),
        /** Sand color palette. */
        SAND("sand"),
        /** Slate color palette. */
        SLATE("slate"),
        /** Violet color palette. */
        VIOLET("violet"),
        /** Yellow color palette. */
        YELLOW("yellow"),
        /** Zinc color palette. */
        ZINC("zinc");

        /** The color scheme string value. */
        private final String myValue;

        /**
         * Constructs a Color scheme option.
         *
         * @param aValue The color scheme string value
         */
        Color(final String aValue) {
            myValue = aValue;
        }

        /**
         * Gets the CSS color scheme name.
         *
         * @return The color scheme name
         */
        public String getValue() {
            return myValue;
        }

        @Override
        public String toString() {
            return myValue;
        }
    }
}
