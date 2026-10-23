package info.freelibrary.j2htmx.pico;

import static info.freelibrary.util.Constants.EMPTY;

import info.freelibrary.j2htmx.pico.utils.CSSMin;
import info.freelibrary.j2htmx.pico.utils.EscapeUtil;
import info.freelibrary.j2htmx.pico.utils.Indenter;
import info.freelibrary.j2htmx.pico.utils.JSMin;
import info.freelibrary.j2htmx.pico.utils.Minifier;
import info.freelibrary.j2htmx.pico.utils.TextEscaper;

import java.util.Collections;

/**
 * Configuration options for rendering HTML, including minification, indentation, and text escaping.
 */
public final class Config {

    /** Constant representing four space characters used for indentation. */
    private static final String FOUR_SPACES = "    ";

    /** Default configuration instance with standard settings. */
    private static final Config DEFAULTS = new Config(EscapeUtil::escape, CSSMin::compressCss, JSMin::compressJs, false,
      (level, text) -> String.join(EMPTY, Collections.nCopies(level, FOUR_SPACES)) + text);

    /** The global text escaper. */
    private static volatile TextEscaper myGlobalTextEscaper = EscapeUtil::escape;

    /** The global CSS minifier. The default minifier is https://github.com/barryvan/CSSMin */
    private static volatile Minifier myGlobalCssMinifier = CSSMin::compressCss;

    /** The global JavaScript minifier. The default minifier is a simple whitespace/newline stripper. */
    private static volatile Minifier myGlobalJsMinifier = JSMin::compressJs;

    /** Whether global empty tags should be closed. The default is to NOT close them. */
    private static volatile boolean myGlobalEmptyTagsAreClosed;

    /** The global HTML indenter. The default is four spaces. */
    private static volatile Indenter myGlobalIndenter = (level, text) -> String.join(EMPTY,
      Collections.nCopies(level, FOUR_SPACES)) + text;

    /** The configured text escaper. */
    private TextEscaper myLocalTextEscaper;

    /** The configured CSS minifier. */
    private Minifier myLocalCssMinifier;

    /** The configured JavaScript minifier. */
    private Minifier myLocalJsMinifier;

    /** Whether empty tags should be closed. */
    private boolean myLocalEmptyTagsAreClosed;

    /** The configured HTML indenter. */
    private Indenter myLocalIndenter;

    /**
     * Creates a new configuration instance with the specified settings.
     *
     * @param aLocalTextEscaper The text escaper
     * @param aLocalCssMinifier The CSS minifier
     * @param aLocalJsMinifier The JavaScript minifier
     * @param aLocalEmptyTagsAreClosed Whether empty tags should be closed
     * @param aLocalIndenter The HTML indenter
     */
    private Config(final TextEscaper aLocalTextEscaper, final Minifier aLocalCssMinifier,
      final Minifier aLocalJsMinifier, final boolean aLocalEmptyTagsAreClosed, final Indenter aLocalIndenter) {
        myLocalTextEscaper = aLocalTextEscaper;
        myLocalCssMinifier = aLocalCssMinifier;
        myLocalJsMinifier = aLocalJsMinifier;
        myLocalEmptyTagsAreClosed = aLocalEmptyTagsAreClosed;
        myLocalIndenter = aLocalIndenter;
    }

    /**
     * A copy constructor.
     *
     * @param aConfig The Config to copy fields from.
     */
    private Config(final Config aConfig) {
        myLocalTextEscaper = aConfig.myLocalTextEscaper;
        myLocalCssMinifier = aConfig.myLocalCssMinifier;
        myLocalJsMinifier = aConfig.myLocalJsMinifier;
        myLocalEmptyTagsAreClosed = aConfig.myLocalEmptyTagsAreClosed;
        myLocalIndenter = aConfig.myLocalIndenter;
    }

    /**
     * Returns the default configuration.
     *
     * @return The default Config instance
     */
    public static Config getDefaults() {
        return DEFAULTS;
    }

    /**
     * Gets the global text escaper.
     *
     * @return The global text escaper
     */
    public static TextEscaper getGlobalTextEscaper() {
        return myGlobalTextEscaper;
    }

    /**
     * Sets the global text-escaper.
     *
     * @param aTextEscaper The text-escaper to use globally
     */
    public static void setGlobalTextEscaper(final TextEscaper aTextEscaper) {
        myGlobalTextEscaper = aTextEscaper;
    }

    /**
     * Gets the global CSS minifier.
     *
     * @return The global CSS minifier
     */
    public static Minifier getGlobalCssMinifier() {
        return myGlobalCssMinifier;
    }

    /**
     * Sets the global CSS minifier.
     *
     * @param aCssMinifier The CSS minifier to use globally
     */
    public static void setGlobalCssMinifier(final Minifier aCssMinifier) {
        myGlobalCssMinifier = aCssMinifier;
    }

    /**
     * Gets the global JavaScript minifier.
     *
     * @return The global JavaScript minifier
     */
    public static Minifier getGlobalJsMinifier() {
        return myGlobalJsMinifier;
    }

    /**
     * Sets the global JavaScript minifier.
     *
     * @param aJsMinifier The JavaScript minifier to use globally
     */
    public static void setGlobalJsMinifier(final Minifier aJsMinifier) {
        myGlobalJsMinifier = aJsMinifier;
    }

    /**
     * Checks whether empty tags should be closed globally.
     *
     * @return True if empty tags are closed globally, false otherwise
     */
    public static boolean isGlobalEmptyTagsClosed() {
        return myGlobalEmptyTagsAreClosed;
    }

    /**
     * Sets whether empty tags should be closed globally.
     *
     * @param aCloseEmptyTags Whether empty tags should be closed globally
     */
    public static void setGlobalEmptyTagsClosed(final boolean aCloseEmptyTags) {
        myGlobalEmptyTagsAreClosed = aCloseEmptyTags;
    }

    /**
     * Gets the global HTML indenter.
     *
     * @return The global HTML indenter
     */
    public static Indenter getGlobalIndenter() {
        return myGlobalIndenter;
    }

    /**
     * Sets the global HTML indenter.
     *
     * @param anIndenter The HTML indenter to use globally
     */
    public static void setGlobalIndenter(final Indenter anIndenter) {
        myGlobalIndenter = anIndenter;
    }

    /**
     * Returns a new configuration snapshot reflecting the current global static settings.
     *
     * @return A Config instance with global settings
     */
    public static Config global() {
        return new Config(myGlobalTextEscaper, myGlobalCssMinifier, myGlobalJsMinifier, myGlobalEmptyTagsAreClosed,
          myGlobalIndenter);
    }

    /**
     * Gets the configured text escaper.
     *
     * @return The text escaper
     */
    public TextEscaper textEscaper() {
        return myLocalTextEscaper;
    }

    /**
     * Gets the configured CSS minifier.
     *
     * @return The CSS minifier
     */
    public Minifier cssMinifier() {
        return myLocalCssMinifier;
    }

    /**
     * Gets the configured JavaScript minifier.
     *
     * @return The JavaScript minifier
     */
    public Minifier jsMinifier() {
        return myLocalJsMinifier;
    }

    /**
     * Checks whether empty tags should be self-closed.
     *
     * @return True if empty tags should be closed, false otherwise
     */
    public boolean closeEmptyTags() {
        return myLocalEmptyTagsAreClosed;
    }

    /**
     * Gets the configured indenter.
     *
     * @return The indenter
     */
    public Indenter indenter() {
        return myLocalIndenter;
    }

    /**
     * Returns a copy of this configuration with the specified text escaper.
     *
     * @param aTextEscaper The text escaper
     * @return A new Config instance
     */
    public Config withTextEscaper(final TextEscaper aTextEscaper) {
        final Config copy = new Config(this);
        copy.myLocalTextEscaper = aTextEscaper;
        return copy;
    }

    /**
     * Returns a copy of this configuration with the specified CSS minifier.
     *
     * @param aCssMinifier The CSS minifier
     * @return A new Config instance
     */
    public Config withCssMinifier(final Minifier aCssMinifier) {
        final Config copy = new Config(this);
        copy.myLocalCssMinifier = aCssMinifier;
        return copy;
    }

    /**
     * Returns a copy of this configuration with the specified JavaScript minifier.
     *
     * @param aJsMinifier The JavaScript minifier
     * @return A new Config instance
     */
    public Config withJsMinifier(final Minifier aJsMinifier) {
        final Config copy = new Config(this);
        copy.myLocalJsMinifier = aJsMinifier;
        return copy;
    }

    /**
     * Returns a copy of this configuration with the empty tag closing setting.
     *
     * @param aCloseEmptyTags Whether empty tags should be closed
     * @return A new Config instance
     */
    public Config withEmptyTagsClosed(final boolean aCloseEmptyTags) {
        final Config copy = new Config(this);
        copy.myLocalEmptyTagsAreClosed = aCloseEmptyTags;
        return copy;
    }

    /**
     * Returns a copy of this configuration with the specified indenter.
     *
     * @param anIndenter The indenter
     * @return A new Config instance
     */
    public Config withIndenter(final Indenter anIndenter) {
        final Config copy = new Config(this);
        copy.myLocalIndenter = anIndenter;
        return copy;
    }

}
