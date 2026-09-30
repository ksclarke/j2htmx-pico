package info.freelibrary.j2htmx.pico.attributes;

import static info.freelibrary.util.Constants.EMPTY;
import static info.freelibrary.util.Constants.HASH;
import static info.freelibrary.util.Constants.PERIOD;

import info.freelibrary.j2htmx.pico.tags.Tag;
import info.freelibrary.util.warnings.PMD;

/**
 * Attribute name constants and helpers for HTML, HTMX, and Pico CSS attributes.
 */
@SuppressWarnings(PMD.AVOID_LITERALS_IN_IF_CONDITION)
public final class Attr {

    /** The "accept" attribute name. */
    public static final String ACCEPT = "accept";

    /** The "accept-charset" attribute name. */
    public static final String ACCEPT_CHARSET = "accept-charset";

    /** The "accesskey" attribute name. */
    public static final String ACCESSKEY = "accesskey";

    /** The "action" attribute name. */
    public static final String ACTION = "action";

    /** The "align" attribute name. */
    public static final String ALIGN = "align";

    /** The "alt" attribute name. */
    public static final String ALT = "alt";

    /** The "async" attribute name. */
    public static final String ASYNC = "async";

    /** The "autocomplete" attribute name. */
    public static final String AUTOCOMPLETE = "autocomplete";

    /** The "autofocus" attribute name. */
    public static final String AUTOFOCUS = "autofocus";

    /** The "autoplay" attribute name. */
    public static final String AUTOPLAY = "autoplay";

    /** The "autosave" attribute name. */
    public static final String AUTOSAVE = "autosave";

    /** The "border" attribute name. */
    public static final String BORDER = "border";

    /** The "buffered" attribute name. */
    public static final String BUFFERED = "buffered";

    /** The "challenge" attribute name. */
    public static final String CHALLENGE = "challenge";

    /** The "charset" attribute name. */
    public static final String CHARSET = "charset";

    /** The "checked" attribute name. */
    public static final String CHECKED = "checked";

    /** The "cite" attribute name. */
    public static final String CITE = "cite";

    /** The "class" attribute name. */
    public static final String CLASS = "class";

    /** The "color" attribute name. */
    public static final String COLOR = "color";

    /** The "cols" attribute name. */
    public static final String COLS = "cols";

    /** The "colspan" attribute name. */
    public static final String COLSPAN = "colspan";

    /** The "content" attribute name. */
    public static final String CONTENT = "content";

    /** The "contenteditable" attribute name. */
    public static final String CONTENTEDITABLE = "contenteditable";

    /** The "contextmenu" attribute name. */
    public static final String CONTEXTMENU = "contextmenu";

    /** The "controls" attribute name. */
    public static final String CONTROLS = "controls";

    /** The "coords" attribute name. */
    public static final String COORDS = "coords";

    /** The "data" attribute name. */
    public static final String DATA = "data";

    /** The "datetime" attribute name. */
    public static final String DATETIME = "datetime";

    /** The "default" attribute name. */
    public static final String DEFAULT = "default";

    /** The "defer" attribute name. */
    public static final String DEFER = "defer";

    /** The "dir" attribute name. */
    public static final String DIR = "dir";

    /** The "dirname" attribute name. */
    public static final String DIRNAME = "dirname";

    /** The "disabled" attribute name. */
    public static final String DISABLED = "disabled";

    /** The "download" attribute name. */
    public static final String DOWNLOAD = "download";

    /** The "draggable" attribute name. */
    public static final String DRAGGABLE = "draggable";

    /** The "dropzone" attribute name. */
    public static final String DROPZONE = "dropzone";

    /** The "enctype" attribute name. */
    public static final String ENCTYPE = "enctype";

    /** The "for" attribute name. */
    public static final String FOR = "for";

    /** The "form" attribute name. */
    public static final String FORM = "form";

    /** The "formaction" attribute name. */
    public static final String FORMACTION = "formaction";

    /** The "headers" attribute name. */
    public static final String HEADERS = "headers";

    /** The "height" attribute name. */
    public static final String HEIGHT = "height";

    /** The "hidden" attribute name. */
    public static final String HIDDEN = "hidden";

    /** The "high" attribute name. */
    public static final String HIGH = "high";

    /** The "href" attribute name. */
    public static final String HREF = "href";

    /** The "hreflang" attribute name. */
    public static final String HREFLANG = "hreflang";

    /** The "http-equiv" attribute name. */
    public static final String HTTP_EQUIV = "http-equiv";

    /** The "icon" attribute name. */
    public static final String ICON = "icon";

    /** The "id" attribute name. */
    public static final String ID = "id";

    /** The "is" attribute name. */
    public static final String IS = "is";

    /** The "ismap" attribute name. */
    public static final String ISMAP = "ismap";

    /** The "itemprop" attribute name. */
    public static final String ITEMPROP = "itemprop";

    /** The "keytype" attribute name. */
    public static final String KEYTYPE = "keytype";

    /** The "kind" attribute name. */
    public static final String KIND = "kind";

    /** The "label" attribute name. */
    public static final String LABEL = "label";

    /** The "lang" attribute name. */
    public static final String LANG = "lang";

    /** The "language" attribute name. */
    public static final String LANGUAGE = "language";

    /** The "list" attribute name. */
    public static final String LIST = "list";

    /** The "loop" attribute name. */
    public static final String LOOP = "loop";

    /** The "low" attribute name. */
    public static final String LOW = "low";

    /** The "manifest" attribute name. */
    public static final String MANIFEST = "manifest";

    /** The "max" attribute name. */
    public static final String MAX = "max";

    /** The "maxlength" attribute name. */
    public static final String MAXLENGTH = "maxlength";

    /** The "media" attribute name. */
    public static final String MEDIA = "media";

    /** The "method" attribute name. */
    public static final String METHOD = "method";

    /** The "min" attribute name. */
    public static final String MIN = "min";

    /** The "multiple" attribute name. */
    public static final String MULTIPLE = "multiple";

    /** The "name" attribute name. */
    public static final String NAME = "name";

    /** The "novalidate" attribute name. */
    public static final String NOVALIDATE = "novalidate";

    /** The "open" attribute name. */
    public static final String OPEN = "open";

    /** The "optimum" attribute name. */
    public static final String OPTIMUM = "optimum";

    /** The "pattern" attribute name. */
    public static final String PATTERN = "pattern";

    /** The "ping" attribute name. */
    public static final String PING = "ping";

    /** The "placeholder" attribute name. */
    public static final String PLACEHOLDER = "placeholder";

    /** The "poster" attribute name. */
    public static final String POSTER = "poster";

    /** The "preload" attribute name. */
    public static final String PRELOAD = "preload";

    /** The "pubdate" attribute name. */
    public static final String PUBDATE = "pubdate";

    /** The "radiogroup" attribute name. */
    public static final String RADIOGROUP = "radiogroup";

    /** The "readonly" attribute name. */
    public static final String READONLY = "readonly";

    /** The "rel" attribute name. */
    public static final String REL = "rel";

    /** The "required" attribute name. */
    public static final String REQUIRED = "required";

    /** The "reversed" attribute name. */
    public static final String REVERSED = "reversed";

    /** The "role" attribute name. */
    public static final String ROLE = "role";

    /** The "rows" attribute name. */
    public static final String ROWS = "rows";

    /** The "rowspan" attribute name. */
    public static final String ROWSPAN = "rowspan";

    /** The "sandbox" attribute name. */
    public static final String SANDBOX = "sandbox";

    /** The "scope" attribute name. */
    public static final String SCOPE = "scope";

    /** The "scoped" attribute name. */
    public static final String SCOPED = "scoped";

    /** The "seamless" attribute name. */
    public static final String SEAMLESS = "seamless";

    /** The "selected" attribute name. */
    public static final String SELECTED = "selected";

    /** The "shape" attribute name. */
    public static final String SHAPE = "shape";

    /** The "size" attribute name. */
    public static final String SIZE = "size";

    /** The "sizes" attribute name. */
    public static final String SIZES = "sizes";

    /** The "slot" attribute name. */
    public static final String SLOT = "slot";

    /** The "span" attribute name. */
    public static final String SPAN = "span";

    /** The "spellcheck" attribute name. */
    public static final String SPELLCHECK = "spellcheck";

    /** The "src" attribute name. */
    public static final String SRC = "src";

    /** The "srcdoc" attribute name. */
    public static final String SRCDOC = "srcdoc";

    /** The "srclang" attribute name. */
    public static final String SRCLANG = "srclang";

    /** The "srcset" attribute name. */
    public static final String SRCSET = "srcset";

    /** The "start" attribute name. */
    public static final String START = "start";

    /** The "step" attribute name. */
    public static final String STEP = "step";

    /** The "style" attribute name. */
    public static final String STYLE = "style";

    /** The "summary" attribute name. */
    public static final String SUMMARY = "summary";

    /** The "tabindex" attribute name. */
    public static final String TABINDEX = "tabindex";

    /** The "target" attribute name. */
    public static final String TARGET = "target";

    /** The "title" attribute name. */
    public static final String TITLE = "title";

    /** The "type" attribute name. */
    public static final String TYPE = "type";

    /** The "usemap" attribute name. */
    public static final String USEMAP = "usemap";

    /** The "value" attribute name. */
    public static final String VALUE = "value";

    /** The "width" attribute name. */
    public static final String WIDTH = "width";

    /** The "wrap" attribute name. */
    public static final String WRAP = "wrap";

    /** The "translate" attribute name. */
    public static final String TRANSLATE = "translate";

    // htmx
    /** The "hx" attribute name. */
    public static final String HX = "hx";

    /** The "hx-get" attribute name. */
    public static final String HX_GET = "hx-get";

    /** The "hx-post" attribute name. */
    public static final String HX_POST = "hx-post";

    /** The "hx-put" attribute name. */
    public static final String HX_PUT = "hx-put";

    /** The "hx-delete" attribute name. */
    public static final String HX_DELETE = "hx-delete";

    /** The "hx-patch" attribute name. */
    public static final String HX_PATCH = "hx-patch";

    /** The "hx-trigger" attribute name. */
    public static final String HX_TRIGGER = "hx-trigger";

    /** The "hx-target" attribute name. */
    public static final String HX_TARGET = "hx-target";

    /** The "hx-swap" attribute name. */
    public static final String HX_SWAP = "hx-swap";

    /** The "hx-swap-oob" attribute name. */
    public static final String HX_SWAP_OOB = "hx-swap-oob";

    /** The "hx-select" attribute name. */
    public static final String HX_SELECT = "hx-select";

    /** The "hx-push-url" attribute name. */
    public static final String HX_PUSH_URL = "hx-push-url";

    /** The "hx-vals" attribute name. */
    public static final String HX_VALS = "hx-vals";

    /** The "hx-confirm" attribute name. */
    public static final String HX_CONFIRM = "hx-confirm";

    /** The "hx-boost" attribute name. */
    public static final String HX_BOOST = "hx-boost";

    /** The "hx-on" attribute name. */
    public static final String HX_ON = "hx-on";

    /** The "hx-params" attribute name. */
    public static final String HX_PARAMS = "hx-params";

    /** The "hx-headers" attribute name. */
    public static final String HX_HEADERS = "hx-headers";

    /** The "hx-sync" attribute name. */
    public static final String HX_SYNC = "hx-sync";

    /** The "hx-ext" attribute name. */
    public static final String HX_EXT = "hx-ext";

    /** The "hx-disinherit" attribute name. */
    public static final String HX_DISINHERIT = "hx-disinherit";

    /** The "hx-indicator" attribute name. */
    public static final String HX_INDICATOR = "hx-indicator";

    /** The "hx-history" attribute name. */
    public static final String HX_HISTORY = "hx-history";

    /** The "hx-history-elt" attribute name. */
    public static final String HX_HISTORY_ELT = "hx-history-elt";

    /** The "hx-include" attribute name. */
    public static final String HX_INCLUDE = "hx-include";

    /** The "hx-prompt" attribute name. */
    public static final String HX_PROMPT = "hx-prompt";

    /** The "hx-ptag" attribute name. */
    public static final String HX_PTAG = "hx-ptag";

    /** The "hx-download" attribute name. */
    public static final String HX_DOWNLOAD = "hx-download";

    /** The "hx-head" attribute name. */
    public static final String HX_HEAD = "hx-head";

    /** The "hx-targets" attribute name. */
    public static final String HX_TARGETS = "hx-targets";

    /** The "hx-upsert" attribute name. */
    public static final String HX_UPSERT = "hx-upsert";

    /** The "hx-status" attribute name. */
    public static final String HX_STATUS = "hx-status";

    /** The "hx-ignore" attribute name. */
    public static final String HX_IGNORE = "hx-ignore";

    /** The "hx-history-cache" attribute name. */
    public static final String HX_HISTORY_CACHE = "hx-history-cache";

    /** The "hx-csp" attribute name. */
    public static final String HX_CSP = "hx-csp";

    /** The "hx-alpine-compat" attribute name. */
    public static final String HX_ALPINE_COMPAT = "hx-alpine-compat";

    /** The "hx-multipart" attribute name. */
    public static final String HX_MULTIPART = "hx-multipart";

    /** The "hx-live" attribute name. */
    public static final String HX_LIVE = "hx-live";

    /** The "hx-sse" attribute name. */
    public static final String HX_SSE = "hx-sse";

    /** The "hx-ws" attribute name. */
    public static final String HX_WS = "hx-ws";

    /** The "hx-validate" attribute name. */
    public static final String HX_VALIDATE = "hx-validate";

    // pico.css & aria
    /** The "data-theme" attribute name. */
    public static final String DATA_THEME = "data-theme";

    /** The "data-tooltip" attribute name. */
    public static final String DATA_TOOLTIP = "data-tooltip";

    /** The "data-placement" attribute name. */
    public static final String DATA_PLACEMENT = "data-placement";

    /** The "aria-busy" attribute name. */
    public static final String ARIA_BUSY = "aria-busy";

    /** The "aria-invalid" attribute name. */
    public static final String ARIA_INVALID = "aria-invalid";

    /** The "button" role attribute value. */
    public static final String ROLE_BUTTON = "button";

    /** The "group" role attribute value. */
    public static final String ROLE_GROUP = "group";

    /** The "search" role attribute value. */
    public static final String ROLE_SEARCH = "search";

    /**
     * Constructs a new Attr instance.
     */
    private Attr() {
        // This is intentionally left empty
    }

    /**
     * Represents a shorthand attribute containing parsed ID and CSS class names.
     */
    public static final class ShortForm {

        /** The ID attribute value. */
        final String myID;

        /** The CSS myClasses string. */
        final String myClasses;

        /**
         * Constructs a new ShortForm attribute.
         *
         * @param aID The element ID
         * @param aClasses The CSS class names
         */
        private ShortForm(final String aID, final String aClasses) {
            myID = aID;
            myClasses = aClasses;
        }

        /**
         * Parses a CSS-like selector shorthand string (e.g. {@code "#my-id.class1.class2"}) into a {@link ShortForm}
         * attribute.
         *
         * @param attrs A selector string containing an optional ID and myClasses
         * @return A ShortForm instance containing the parsed ID and myClasses
         * @throws IllegalArgumentException If the string is malformed or contains multiple IDs
         */
        @SuppressWarnings(PMD.CYCLOMATIC_COMPLEXITY)
        public static ShortForm parse(final String attrs) {
            if (!attrs.contains(PERIOD) && !attrs.contains(HASH)) {
                throw new IllegalArgumentException("String must contain either id (#) or class (.)");
            }

            if (attrs.split(HASH).length > 2) {
                throw new IllegalArgumentException("Only one id (#) allowed");
            }

            final StringBuilder classes = new StringBuilder();
            String id = EMPTY;

            for (final String attr : attrs.split("\\.")) {
                if (attr.contains(HASH)) {
                    if (!attr.startsWith(HASH)) {
                        throw new IllegalArgumentException("# cannot be in the middle of string");
                    }

                    id = attr.replace(HASH, EMPTY);
                } else {
                    classes.append(attr).append(' ');
                }
            }

            return new ShortForm(id.trim(), classes.toString().trim());
        }

        /**
         * Adds the ID and classes from the specified ShortForm attribute to the given tag, if the attribute is not {@code null}.
         * If the attribute is {@code null}, the tag is returned unchanged.
         *
         * @param <T> the type of the tag
         * @param aTag the tag to which the ID and classes are to be added; must not be null
         * @param aShortAttr the ShortForm attribute containing the ID and classes, possibly null
         * @return the same tag instance, with the ID and classes applied if {@code aShortAttr} was not null
         */
        public static <T extends Tag<T>> T addTo(final T aTag, final ShortForm aShortAttr) {
            if (aShortAttr != null) {
                aShortAttr.addTo(aTag);
            }

            return aTag;
        }

        /**
         * Checks if an ID is present.
         *
         * @return True if ID is non-null and non-empty, false otherwise
         */
        boolean hasId() {
            return myID != null && !myID.isBlank();
        }

        /**
         * Checks if myClasses are present.
         *
         * @return True if myClasses are non-null and non-empty, false otherwise
         */
        boolean hasClasses() {
            return myClasses != null && !myClasses.isBlank();
        }

        /**
         * Adds this ShortForm's ID and class names to the given tag, if present.
         * If ID and classes are both present, both are added. If only one is present,
         * only that one is added. If neither is present, the tag is returned unchanged.
         *
         * @param <T> the type of the tag
         * @param aTag the tag to which the ID and classes are to be added; must not be null
         * @return the same tag instance, with the ID and classes applied if present
         */
        public <T extends Tag<T>> T addTo(final T aTag) {
            if (hasId() && hasClasses()) {
                return aTag.withId(myID).withClass(myClasses);
            }

            if (hasId()) {
                return aTag.withId(myID);
            }

            if (hasClasses()) {
                return aTag.withClass(myClasses);
            }

            return aTag;
        }
    }
}
