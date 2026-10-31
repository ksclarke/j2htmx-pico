/*
 * This code was derived from j2html, which is distributed under the Apache License, Version 2.0.
 * See LICENSE-ASL-2.0.txt in the parent project's root directory for more information.
 *
 * This file was modified for j2htmx-pico by Kevin S. Clarke (my.j2htmx-pico@kevinclarke.info).
 *
 * The modifications are licensed under the Mozilla Public License, Version 2.0.
 * See LICENSE-MPL-2.0.txt in the parent project's root directory for more information.
 */

package info.freelibrary.j2htmx.pico.attributes;

import static info.freelibrary.util.Constants.EMPTY;
import static info.freelibrary.util.Constants.HASH;
import static info.freelibrary.util.Constants.PERIOD;

import info.freelibrary.j2htmx.pico.tags.AbstractTag;
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

    /** The "muted" attribute name. */
    public static final String MUTED = "muted";

    /** The "name" attribute name. */
    public static final String NAME = "name";

    /** The "novalidate" attribute name. */
    public static final String NOVALIDATE = "novalidate";

    /** The "onabort" attribute name. */
    public static final String ONABORT = "onabort";

    /** The "onafterprint" attribute name. */
    public static final String ONAFTERPRINT = "onafterprint";

    /** The "onbeforeprint" attribute name. */
    public static final String ONBEFOREPRINT = "onbeforeprint";

    /** The "onbeforeunload" attribute name. */
    public static final String ONBEFOREUNLOAD = "onbeforeunload";

    /** The "onblur" attribute name. */
    public static final String ONBLUR = "onblur";

    /** The "oncanplay" attribute name. */
    public static final String ONCANPLAY = "oncanplay";

    /** The "oncanplaythrough" attribute name. */
    public static final String ONCANPLAYTHROUGH = "oncanplaythrough";

    /** The "onchange" attribute name. */
    public static final String ONCHANGE = "onchange";

    /** The "onclick" attribute name. */
    public static final String ONCLICK = "onclick";

    /** The "oncontextmenu" attribute name. */
    public static final String ONCONTEXTMENU = "oncontextmenu";

    /** The "oncopy" attribute name. */
    public static final String ONCOPY = "oncopy";

    /** The "oncuechange" attribute name. */
    public static final String ONCUECHANGE = "oncuechange";

    /** The "oncut" attribute name. */
    public static final String ONCUT = "oncut";

    /** The "ondrop" attribute name. */
    public static final String ONDROP = "ondrop";

    /** The "ondurationchange" attribute name. */
    public static final String ONDURATIONCHANGE = "ondurationchange";

    /** The "onemptied" attribute name. */
    public static final String ONEMPTIED = "onemptied";

    /** The "onended" attribute name. */
    public static final String ONENDED = "onended";

    /** The "onerror" attribute name. */
    public static final String ONERROR = "onerror";

    /** The "onfocus" attribute name. */
    public static final String ONFOCUS = "onfocus";

    /** The "onhashchange" attribute name. */
    public static final String ONHASHCHANGE = "onhashchange";

    /** The "oninput" attribute name. */
    public static final String ONINPUT = "oninput";

    /** The "oninvalid" attribute name. */
    public static final String ONINVALID = "oninvalid";

    /** The "onkeydown" attribute name. */
    public static final String ONKEYDOWN = "onkeydown";

    /** The "onkeypress" attribute name. */
    public static final String ONKEYPRESS = "onkeypress";

    /** The "onkeyup" attribute name. */
    public static final String ONKEYUP = "onkeyup";

    /** The "onload" attribute name. */
    public static final String ONLOAD = "onload";

    /** The "onloadeddata" attribute name. */
    public static final String ONLOADEDDATA = "onloadeddata";

    /** The "onloadedmetadata" attribute name. */
    public static final String ONLOADEDMETADATA = "onloadedmetadata";

    /** The "onloadstart" attribute name. */
    public static final String ONLOADSTART = "onloadstart";

    /** The "onmousedown" attribute name. */
    public static final String ONMOUSEDOWN = "onmousedown";

    /** The "onmouseenter" attribute name. */
    public static final String ONMOUSEENTER = "onmouseenter";

    /** The "onmouseleave" attribute name. */
    public static final String ONMOUSELEAVE = "onmouseleave";

    /** The "onmousemove" attribute name. */
    public static final String ONMOUSEMOVE = "onmousemove";

    /** The "onmouseout" attribute name. */
    public static final String ONMOUSEOUT = "onmouseout";

    /** The "onmouseover" attribute name. */
    public static final String ONMOUSEOVER = "onmouseover";

    /** The "onmouseup" attribute name. */
    public static final String ONMOUSEUP = "onmouseup";

    /** The "onmousewheel" attribute name. */
    public static final String ONMOUSEWHEEL = "onmousewheel";

    /** The "onoffline" attribute name. */
    public static final String ONOFFLINE = "onoffline";

    /** The "ononline" attribute name. */
    public static final String ONONLINE = "ononline";

    /** The "onpagehide" attribute name. */
    public static final String ONPAGEHIDE = "onpagehide";

    /** The "onpageshow" attribute name. */
    public static final String ONPAGESHOW = "onpageshow";

    /** The "onpaste" attribute name. */
    public static final String ONPASTE = "onpaste";

    /** The "onpause" attribute name. */
    public static final String ONPAUSE = "onpause";

    /** The "onplay" attribute name. */
    public static final String ONPLAY = "onplay";

    /** The "onplaying" attribute name. */
    public static final String ONPLAYING = "onplaying";

    /** The "onpopstate" attribute name. */
    public static final String ONPOPSTATE = "onpopstate";

    /** The "onprogress" attribute name. */
    public static final String ONPROGRESS = "onprogress";

    /** The "onratechange" attribute name. */
    public static final String ONRATECHANGE = "onratechange";

    /** The "onreset" attribute name. */
    public static final String ONRESET = "onreset";

    /** The "onresize" attribute name. */
    public static final String ONRESIZE = "onresize";

    /** The "onscroll" attribute name. */
    public static final String ONSCROLL = "onscroll";

    /** The "onsearch" attribute name. */
    public static final String ONSEARCH = "onsearch";

    /** The "onseeked" attribute name. */
    public static final String ONSEEKED = "onseeked";

    /** The "onseeking" attribute name. */
    public static final String ONSEEKING = "onseeking";

    /** The "onselect" attribute name. */
    public static final String ONSELECT = "onselect";

    /** The "onstalled" attribute name. */
    public static final String ONSTALLED = "onstalled";

    /** The "onstorage" attribute name. */
    public static final String ONSTORAGE = "onstorage";

    /** The "onsubmit" attribute name. */
    public static final String ONSUBMIT = "onsubmit";

    /** The "onsuspend" attribute name. */
    public static final String ONSUSPEND = "onsuspend";

    /** The "ontimeupdate" attribute name. */
    public static final String ONTIMEUPDATE = "ontimeupdate";

    /** The "ontoggle" attribute name. */
    public static final String ONTOGGLE = "ontoggle";

    /** The "onunload" attribute name. */
    public static final String ONUNLOAD = "onunload";

    /** The "onvolumechange" attribute name. */
    public static final String ONVOLUMECHANGE = "onvolumechange";

    /** The "onvolumechanged" attribute name. */
    public static final String ONVOLUMECHANGED = "onvolumechanged";

    /** The "onwaiting" attribute name. */
    public static final String ONWAITING = "onwaiting";

    /** The "onwheel" attribute name. */
    public static final String ONWHEEL = "onwheel";

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

    /** The "aria-hidden" attribute name. */
    public static final String ARIA_HIDDEN = "aria-hidden";

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
         * Adds the ID and classes from the specified ShortForm attribute to the given tag, if the attribute is not
         * {@code null}. If the attribute is {@code null}, the tag is returned unchanged.
         *
         * @param <T> the type of the tag
         * @param aTag the tag to which the ID and classes are to be added; must not be null
         * @param aShortAttr the ShortForm attribute containing the ID and classes, possibly null
         * @return the same tag instance, with the ID and classes applied if {@code aShortAttr} was not null
         */
        public static <T extends AbstractTag<T>> T addTo(final T aTag, final ShortForm aShortAttr) {
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
        public <T extends AbstractTag<T>> T addTo(final T aTag) {
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
