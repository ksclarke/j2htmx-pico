package info.freelibrary.j2htmx.pico.attributes;

import info.freelibrary.j2htmx.pico.tags.Tag;

public abstract class Attr {

    public static final String ACCEPT = "accept";
    public static final String ACCEPT_CHARSET = "accept-charset";
    public static final String ACCESSKEY = "accesskey";
    public static final String ACTION = "action";
    public static final String ALIGN = "align";
    public static final String ALT = "alt";
    public static final String ASYNC = "async";
    public static final String AUTOCOMPLETE = "autocomplete";
    public static final String AUTOFOCUS = "autofocus";
    public static final String AUTOPLAY = "autoplay";
    public static final String AUTOSAVE = "autosave";
    public static final String BORDER = "border";
    public static final String BUFFERED = "buffered";
    public static final String CHALLENGE = "challenge";
    public static final String CHARSET = "charset";
    public static final String CHECKED = "checked";
    public static final String CITE = "cite";
    public static final String CLASS = "class";
    public static final String COLOR = "color";
    public static final String COLS = "cols";
    public static final String COLSPAN = "colspan";
    public static final String CONTENT = "content";
    public static final String CONTENTEDITABLE = "contenteditable";
    public static final String CONTEXTMENU = "contextmenu";
    public static final String CONTROLS = "controls";
    public static final String COORDS = "coords";
    public static final String DATA = "data";
    public static final String DATETIME = "datetime";
    public static final String DEFAULT = "default";
    public static final String DEFER = "defer";
    public static final String DIR = "dir";
    public static final String DIRNAME = "dirname";
    public static final String DISABLED = "disabled";
    public static final String DOWNLOAD = "download";
    public static final String DRAGGABLE = "draggable";
    public static final String DROPZONE = "dropzone";
    public static final String ENCTYPE = "enctype";
    public static final String FOR = "for";
    public static final String FORM = "form";
    public static final String FORMACTION = "formaction";
    public static final String HEADERS = "headers";
    public static final String HEIGHT = "height";
    public static final String HIDDEN = "hidden";
    public static final String HIGH = "high";
    public static final String HREF = "href";
    public static final String HREFLANG = "hreflang";
    public static final String HTTP_EQUIV = "http-equiv";
    public static final String ICON = "icon";
    public static final String ID = "id";
    public static final String IS = "is";
    public static final String ISMAP = "ismap";
    public static final String ITEMPROP = "itemprop";
    public static final String KEYTYPE = "keytype";
    public static final String KIND = "kind";
    public static final String LABEL = "label";
    public static final String LANG = "lang";
    public static final String LANGUAGE = "language";
    public static final String LIST = "list";
    public static final String LOOP = "loop";
    public static final String LOW = "low";
    public static final String MANIFEST = "manifest";
    public static final String MAX = "max";
    public static final String MAXLENGTH = "maxlength";
    public static final String MEDIA = "media";
    public static final String METHOD = "method";
    public static final String MIN = "min";
    public static final String MULTIPLE = "multiple";
    public static final String NAME = "name";
    public static final String NOVALIDATE = "novalidate";
    public static final String OPEN = "open";
    public static final String OPTIMUM = "optimum";
    public static final String PATTERN = "pattern";
    public static final String PING = "ping";
    public static final String PLACEHOLDER = "placeholder";
    public static final String POSTER = "poster";
    public static final String PRELOAD = "preload";
    public static final String PUBDATE = "pubdate";
    public static final String RADIOGROUP = "radiogroup";
    public static final String READONLY = "readonly";
    public static final String REL = "rel";
    public static final String REQUIRED = "required";
    public static final String REVERSED = "reversed";
    public static final String ROLE = "role";
    public static final String ROWS = "rows";
    public static final String ROWSPAN = "rowspan";
    public static final String SANDBOX = "sandbox";
    public static final String SCOPE = "scope";
    public static final String SCOPED = "scoped";
    public static final String SEAMLESS = "seamless";
    public static final String SELECTED = "selected";
    public static final String SHAPE = "shape";
    public static final String SIZE = "size";
    public static final String SIZES = "sizes";
    public static final String SLOT = "slot";
    public static final String SPAN = "span";
    public static final String SPELLCHECK = "spellcheck";
    public static final String SRC = "src";
    public static final String SRCDOC = "srcdoc";
    public static final String SRCLANG = "srclang";
    public static final String SRCSET = "srcset";
    public static final String START = "start";
    public static final String STEP = "step";
    public static final String STYLE = "style";
    public static final String SUMMARY = "summary";
    public static final String TABINDEX = "tabindex";
    public static final String TARGET = "target";
    public static final String TITLE = "title";
    public static final String TYPE = "type";
    public static final String USEMAP = "usemap";
    public static final String VALUE = "value";
    public static final String WIDTH = "width";
    public static final String WRAP = "wrap";
    public static final String TRANSLATE = "translate";

    // htmx
    public static final String HX = "hx";
    public static final String HX_GET = "hx-get";
    public static final String HX_POST = "hx-post";
    public static final String HX_PUT = "hx-put";
    public static final String HX_DELETE = "hx-delete";
    public static final String HX_PATCH = "hx-patch";
    public static final String HX_TRIGGER = "hx-trigger";
    public static final String HX_TARGET = "hx-target";
    public static final String HX_SWAP = "hx-swap";
    public static final String HX_SWAP_OOB = "hx-swap-oob";
    public static final String HX_SELECT = "hx-select";
    public static final String HX_PUSH_URL = "hx-push-url";
    public static final String HX_VALS = "hx-vals";
    public static final String HX_CONFIRM = "hx-confirm";
    public static final String HX_BOOST = "hx-boost";
    public static final String HX_ON = "hx-on";
    public static final String HX_PARAMS = "hx-params";
    public static final String HX_HEADERS = "hx-headers";
    public static final String HX_SYNC = "hx-sync";
    public static final String HX_EXT = "hx-ext";
    public static final String HX_DISINHERIT = "hx-disinherit";
    public static final String HX_INDICATOR = "hx-indicator";
    public static final String HX_HISTORY = "hx-history";
    public static final String HX_HISTORY_ELT = "hx-history-elt";
    public static final String HX_INCLUDE = "hx-include";
    public static final String HX_PROMPT = "hx-prompt";
    public static final String HX_PTAG = "hx-ptag";
    public static final String HX_DOWNLOAD = "hx-download";
    public static final String HX_HEAD = "hx-head";
    public static final String HX_TARGETS = "hx-targets";
    public static final String HX_UPSERT = "hx-upsert";
    public static final String HX_STATUS = "hx-status";
    public static final String HX_IGNORE = "hx-ignore";
    public static final String HX_HISTORY_CACHE = "hx-history-cache";
    public static final String HX_CSP = "hx-csp";
    public static final String HX_ALPINE_COMPAT = "hx-alpine-compat";
    public static final String HX_MULTIPART = "hx-multipart";
    public static final String HX_LIVE = "hx-live";
    public static final String HX_SSE = "hx-sse";
    public static final String HX_WS = "hx-ws";
    public static final String HX_VALIDATE = "hx-validate";

    // pico.css & aria
    public static final String DATA_THEME = "data-theme";
    public static final String DATA_TOOLTIP = "data-tooltip";
    public static final String DATA_PLACEMENT = "data-placement";
    public static final String ARIA_BUSY = "aria-busy";
    public static final String ARIA_INVALID = "aria-invalid";
    public static final String ROLE_BUTTON = "button";
    public static final String ROLE_GROUP = "group";
    public static final String ROLE_SEARCH = "search";

    public static ShortForm shortFormFromAttrsString(String attrs) {
        if (!attrs.contains(".") && !attrs.contains(("#"))) {
            throw new IllegalArgumentException("String must contain either id (#) or class (.)");
        }
        if (attrs.split("#").length > 2) {
            throw new IllegalArgumentException("Only one id (#) allowed");
        }
        String id = "";
        StringBuilder classes = new StringBuilder();
        for (String attr : attrs.split("\\.")) {
            if (attr.contains("#")) {
                if (!attr.startsWith("#")) {
                    throw new IllegalArgumentException("# cannot be in the middle of string");
                }
                id = attr.replace("#", "");
            } else {
                classes.append(attr).append(" ");
            }
        }
        return new ShortForm(id.trim(), classes.toString().trim());
    }

    public static <T extends Tag<T>> T addTo(T tag, Attr attr) {
        if (null != attr)
            attr.addTo(tag);
        return tag;
    }

    public abstract <T extends Tag<T>> T addTo(T tag);

    public static class ShortForm extends Attr {
        String id;
        String classes;

        private ShortForm(String id, String classes) {
            this.id = id;
            this.classes = classes;
        }

        boolean hasId() {
            return id != null && !"".equals(id);
        }

        boolean hasClasses() {
            return classes != null && !"".equals(classes);
        }

        @Override
        public <T extends Tag<T>> T addTo(T tag) {
            if (hasId() && hasClasses()) {
                return tag.withId(id).withClass(classes);
            }
            if (hasId()) {
                return tag.withId(id);
            }
            if (hasClasses()) {
                return tag.withClass(classes);
            }
            return tag;
        }
    }
}
