package info.freelibrary.j2htmx.pico;

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
public class Config {

    /**
     * Change this to configure text-escaping
     * For example, to disable escaping, do {@code Config.textEscaper = text -> text;}
     */
    public static volatile TextEscaper textEscaper = EscapeUtil::escape;
    /**
     * Change this to configure css-minification.
     * The default minifier is https://github.com/barryvan/CSSMin
     */
    public static volatile Minifier cssMinifier = CSSMin::compressCss;
    /**
     * Change this to configure js-minification.
     * The default minifier is a simple whitespace/newline stripper
     */
    public static volatile Minifier jsMinifier = JSMin::compressJs;
    /**
     * Change this to configure enable/disable closing empty tags
     * The default is to NOT close them
     */
    public static volatile boolean closeEmptyTags = false;

    /** Constant representing four space characters used for indentation. */
    private static final String FOUR_SPACES = "    ";

    /**
     * Change this to configure indentation when rendering formatted html
     * The default is four spaces
     */
    public static volatile Indenter indenter = (level, text) -> String.join("", Collections.nCopies(level, FOUR_SPACES)) + text;


    /** The configured text escaper. */
    private TextEscaper _textEscaper;

    /** The configured CSS minifier. */
    private Minifier _cssMinifier;

    /** The configured JavaScript minifier. */
    private Minifier _jsMinifier;

    /** Whether empty tags should be closed. */
    private boolean _closeEmptyTags;

    /** The configured HTML indenter. */
    private Indenter _indenter;


    private Config(
        final TextEscaper _textEscaper,
        final Minifier _cssMinifier,
        final Minifier _jsMinifier,
        final boolean _closeEmptyTags,
        final Indenter _indenter
    ) {
        this._textEscaper = _textEscaper;
        this._cssMinifier = _cssMinifier;
        this._jsMinifier = _jsMinifier;
        this._closeEmptyTags = _closeEmptyTags;
        this._indenter = _indenter;
    }

    /**
     * A copy constructor.
     *
     * @param original The Config to copy fields from.
     */
    private Config(final Config original) {
        this._textEscaper = original._textEscaper;
        this._cssMinifier = original._cssMinifier;
        this._jsMinifier = original._jsMinifier;
        this._closeEmptyTags = original._closeEmptyTags;
        this._indenter = original._indenter;
    }

    /**
     * Gets the configured text escaper.
     *
     * @return The text escaper
     */
    public TextEscaper textEscaper() {
        return _textEscaper;
    }

    /**
     * Gets the configured CSS minifier.
     *
     * @return The CSS minifier
     */
    public Minifier cssMinifier() {
        return _cssMinifier;
    }

    /**
     * Gets the configured JavaScript minifier.
     *
     * @return The JavaScript minifier
     */
    public Minifier jsMinifier() {
        return _jsMinifier;
    }

    /**
     * Checks whether empty tags should be self-closed.
     *
     * @return True if empty tags should be closed, false otherwise
     */
    public boolean closeEmptyTags() {
        return _closeEmptyTags;
    }

    /**
     * Gets the configured indenter.
     *
     * @return The indenter
     */
    public Indenter indenter() {
        return _indenter;
    }

    /**
     * Returns a copy of this configuration with the specified text escaper.
     *
     * @param textEscaper The text escaper
     * @return A new Config instance
     */
    public Config withTextEscaper(final TextEscaper textEscaper){
        final Config copy = new Config(this);
        copy._textEscaper = textEscaper;
        return copy;
    }

    /**
     * Returns a copy of this configuration with the specified CSS minifier.
     *
     * @param cssMinifier The CSS minifier
     * @return A new Config instance
     */
    public Config withCssMinifier(final Minifier cssMinifier){
        final Config copy = new Config(this);
        copy._cssMinifier = cssMinifier;
        return copy;
    }

    /**
     * Returns a copy of this configuration with the specified JavaScript minifier.
     *
     * @param jsMinifier The JavaScript minifier
     * @return A new Config instance
     */
    public Config withJsMinifier(final Minifier jsMinifier){
        final Config copy = new Config(this);
        copy._jsMinifier = jsMinifier;
        return copy;
    }

    /**
     * Returns a copy of this configuration with the empty tag closing setting.
     *
     * @param closeEmptyTags Whether empty tags should be closed
     * @return A new Config instance
     */
    public Config withEmptyTagsClosed(final boolean closeEmptyTags){
        final Config copy = new Config(this);
        copy._closeEmptyTags = closeEmptyTags;
        return copy;
    }

    /**
     * Returns a copy of this configuration with the specified indenter.
     *
     * @param indenter The indenter
     * @return A new Config instance
     */
    public Config withIndenter(final Indenter indenter){
        final Config copy = new Config(this);
        copy._indenter = indenter;
        return copy;
    }

    /** Default configuration instance with standard settings. */
    private static final Config DEFAULTS = new Config(
        EscapeUtil::escape,
        CSSMin::compressCss,
        JSMin::compressJs,
        false,
        (level, text) -> String.join("", Collections.nCopies(level, FOUR_SPACES)) + text
    );

    /**
     * Returns the default configuration.
     *
     * @return The default Config instance
     */
    public static final Config defaults() {
        return DEFAULTS;
    }

    /**
     * Returns a new configuration snapshot reflecting the current global static settings.
     *
     * @return A Config instance with global settings
     */
    public static final Config global() {
        return new Config(
            textEscaper,
            cssMinifier,
            jsMinifier,
            closeEmptyTags,
            indenter
        );
    }

}
