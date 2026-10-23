package info.freelibrary.j2htmx.pico.codegen.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import info.freelibrary.j2htmx.pico.attributes.Attr;
import info.freelibrary.j2htmx.pico.tags.Tag;

/**
 * Comprehensive registry of HTML attribute descriptions and their associated tags.
 */
public final class AttributesList {

    /**
     * Constructs a new {@code AttributesList}.
     */
    private AttributesList() {
        // This has intentionally left empty
    }

    // Cf. https://www.w3schools.com/tags/ref_attributes.asp

    /**
     * Retrieves all attribute names applicable to the specified lowercase HTML tag name.
     *
     * @param aTagLowercase The lowercase tag name
     * @return List of attribute names
     */
    public static List<String> getCustomAttributesForHtmlTag(final String aTagLowercase) {
        final List<String> attrs = new ArrayList<>();

        for (final AttrD attrD : attributesDescriptive()) {
            if (Arrays.asList(attrD.myTags).contains(aTagLowercase)) {
                attrs.add(attrD.myAttr);
            }
        }

        return attrs;
    }

    /**
     * Returns the master list of HTML attribute descriptors.
     *
     * @return List of attribute descriptors
     */
    public static List<AttrD> attributesDescriptive() {
        return Arrays.asList(
          new AttrD(Attr.ACCEPT, true, Tag.INPUT),
          //new AttrD("accept-charset","form"), //contains dashes, TODO
          //new AttrD("accesskey"), //global attribute
          new AttrD(Attr.ACTION, true, Tag.FORM),
          //"align", not supported in HTML5
          new AttrD(Attr.ALT, true, Tag.AREA, Tag.IMAGE, Tag.INPUT),
          new AttrD(Attr.ASYNC, false, Tag.SCRIPT),
          new AttrD(Attr.AUTOCOMPLETE, false, Tag.FORM, Tag.INPUT),
          new AttrD(Attr.AUTOFOCUS, false, Tag.BUTTON, Tag.INPUT, Tag.SELECT, Tag.TEXTAREA),
          new AttrD(Attr.AUTOPLAY, false, Tag.AUDIO, Tag.VIDEO),
          //"bgcolor", not supported in HTMTL5
          //"border", not supported in HTML5
          new AttrD(Attr.CHARSET, true, Tag.META, Tag.SCRIPT),
          new AttrD(Attr.CHECKED, false, Tag.INPUT),
          new AttrD(Attr.CITE, true, Tag.BLOCKQUOTE, Tag.DEL, Tag.INS, Tag.Q),
          //"class" already implemented in Tag.java // global attribute
          new AttrD(Attr.COLS, true, Tag.TEXTAREA),
          new AttrD(Attr.COLSPAN, true, Tag.TD, Tag.TH),
          new AttrD(Attr.CONTENT, true, Tag.META),
          //"contenteditable" global attribute, should be in Tag.java
          new AttrD(Attr.CONTROLS, false, Tag.AUDIO, Tag.VIDEO),
          new AttrD(Attr.COORDS, true, Tag.AREA),
          new AttrD(Attr.DATA, true, Tag.OBJECT),
          new AttrD(Attr.DATETIME, true, Tag.DEL, Tag.INS, Tag.TIME),
          new AttrD(Attr.DEFAULT, false, Tag.TRACK),
          new AttrD(Attr.DEFER, false, Tag.SCRIPT),
          //new AttrD("dir"), //global attribute
          new AttrD(Attr.DIRNAME, true, Tag.INPUT, Tag.TEXTAREA),
          new AttrD(Attr.DISABLED, false, Tag.BUTTON, Tag.FIELDSET, Tag.INPUT, Tag.OPTGROUP, Tag.OPTION,
            Tag.SELECT, Tag.TEXTAREA),
          new AttrD(Attr.DOWNLOAD, false, Tag.A, Tag.AREA),
          //new AttrD("draggable") global attribute, should be in Tag.java
          new AttrD(Attr.ENCTYPE, true, Tag.FORM),
          new AttrD(Attr.FOR, true, Tag.LABEL, Tag.OUTPUT),
          new AttrD(Attr.FORM, true, Tag.BUTTON, Tag.FIELDSET, Tag.INPUT, Tag.LABEL, Tag.METER, Tag.OBJECT,
            Tag.OUTPUT, Tag.SELECT, Tag.TEXTAREA),
          new AttrD(Attr.FORMACTION, true, Tag.BUTTON, Tag.INPUT),
          new AttrD(Attr.HEADERS, true, Tag.TD, Tag.TH),
          new AttrD(Attr.HEIGHT, true, Tag.CANVAS, Tag.EMBED, Tag.IFRAME, Tag.IMAGE, Tag.INPUT, Tag.OBJECT,
            Tag.VIDEO),
          //new AttrD("hidden"), global attribute
          new AttrD(Attr.HIGH, true, Tag.METER),
          new AttrD(Attr.HREF, true, Tag.A, Tag.AREA, Tag.BASE, Tag.LINK),
          new AttrD(Attr.HREFLANG, true, Tag.A, Tag.AREA, Tag.LINK),
          //"http-equiv", //TODO: '-' has problematic in code generation
          //"id" global attribute, should be in Tag.java
          new AttrD(Attr.ISMAP, false, Tag.IMAGE),
          new AttrD(Attr.KIND, true, Tag.TRACK),
          new AttrD(Attr.LABEL, true, Tag.TRACK, Tag.OPTION, Tag.OPTGROUP),
          //"lang" global attribute, should be in Tag.java
          new AttrD(Attr.LIST, true, Tag.INPUT),
          new AttrD(Attr.LOOP, false, Tag.AUDIO, Tag.VIDEO),
          new AttrD(Attr.LOW, true, Tag.METER),
          new AttrD(Attr.MAX, true, Tag.INPUT, Tag.METER, Tag.PROGRESS),
          new AttrD(Attr.MAXLENGTH, true, Tag.INPUT, Tag.TEXTAREA),
          new AttrD(Attr.MEDIA, true, Tag.A, Tag.AREA, Tag.LINK, Tag.SOURCE, Tag.STYLE),
          new AttrD(Attr.METHOD, true, Tag.FORM),
          new AttrD(Attr.MIN, true, Tag.INPUT, Tag.METER),
          new AttrD(Attr.MULTIPLE, false, Tag.INPUT, Tag.SELECT),
          new AttrD(Attr.MUTED, false, Tag.VIDEO, Tag.AUDIO),
          new AttrD(Attr.NAME, true, Tag.BUTTON, Tag.FIELDSET, Tag.FORM, Tag.IFRAME, Tag.INPUT, Tag.MAP,
            Tag.META, Tag.OBJECT, Tag.OUTPUT, Tag.PARAM, Tag.SELECT, Tag.SLOT, Tag.TEXTAREA),
          new AttrD(Attr.NOVALIDATE, false, Tag.FORM),
          new AttrD(Attr.ONABORT, true, Tag.AUDIO, Tag.EMBED, Tag.IMAGE, Tag.OBJECT, Tag.VIDEO),
          new AttrD(Attr.ONAFTERPRINT, true, Tag.BODY),
          new AttrD(Attr.ONBEFOREPRINT, true, Tag.BODY),
          new AttrD(Attr.ONBEFOREUNLOAD, true, Tag.BODY),
          //new AttrD("onblur"), global attribute
          new AttrD(Attr.ONCANPLAY, true, Tag.AUDIO, Tag.EMBED, Tag.OBJECT, Tag.VIDEO),
          new AttrD(Attr.ONCANPLAYTHROUGH, true, Tag.VIDEO, Tag.VIDEO),
            /* a bunch of event attributes that are on all visible elements (so should be in Tag.java)
            "onchange",
            "onclick",
            "oncontextmenu",
            "oncopy",
            */
          new AttrD(Attr.ONCUECHANGE, true, Tag.TRACK),
            /*
            "oncut",
            ...
            "ondrop",
             */
          new AttrD(Attr.ONDURATIONCHANGE, true, Tag.AUDIO, Tag.VIDEO),
          new AttrD(Attr.ONEMPTIED, true, Tag.AUDIO, Tag.VIDEO),
          new AttrD(Attr.ONENDED, true, Tag.AUDIO, Tag.VIDEO),
          new AttrD(Attr.ONERROR, true, Tag.AUDIO, Tag.BODY, Tag.EMBED, Tag.IMAGE, Tag.OBJECT, Tag.SCRIPT,
            Tag.STYLE, Tag.VIDEO),
          //new AttrD("onfocus"),// global attribute
          new AttrD(Attr.ONHASHCHANGE, true, Tag.BODY),
          // ... a bunch of event attributes visible on all elements
          new AttrD(Attr.ONLOAD, true, Tag.BODY, Tag.IFRAME, Tag.IMAGE, Tag.INPUT, Tag.LINK, Tag.SCRIPT,
            Tag.STYLE),
          new AttrD(Attr.ONLOADEDDATA, true, Tag.AUDIO, Tag.VIDEO),
          new AttrD(Attr.ONLOADEDMETADATA, true, Tag.AUDIO, Tag.VIDEO),
          new AttrD(Attr.ONLOADSTART, true, Tag.AUDIO, Tag.VIDEO),
          // ... a bunch of event attributes visible on all elements
          new AttrD(Attr.ONOFFLINE, true, Tag.BODY),
          new AttrD(Attr.ONONLINE, true, Tag.BODY),
          new AttrD(Attr.ONPAGEHIDE, true, Tag.BODY),
          new AttrD(Attr.ONPAGESHOW, true, Tag.BODY),
          //new AttrD("onpaste"),// global attribute
          new AttrD(Attr.ONPAUSE, true, Tag.AUDIO, Tag.VIDEO),
          new AttrD(Attr.ONPLAY, true, Tag.AUDIO, Tag.VIDEO),
          new AttrD(Attr.ONPLAYING, true, Tag.AUDIO, Tag.VIDEO),
          new AttrD(Attr.ONPOPSTATE, true, Tag.BODY),
          new AttrD(Attr.ONPROGRESS, true, Tag.AUDIO, Tag.VIDEO),
          new AttrD(Attr.ONRATECHANGE, true, Tag.AUDIO, Tag.VIDEO),
          new AttrD(Attr.ONRESET, true, Tag.FORM),
          new AttrD(Attr.ONRESIZE, true, Tag.BODY),
          //new AttrD("onscroll"), //global attribute
          new AttrD(Attr.ONSEARCH, true, Tag.INPUT),
          new AttrD(Attr.ONSEEKED, true, Tag.AUDIO, Tag.VIDEO),
          new AttrD(Attr.ONSEEKING, true, Tag.AUDIO, Tag.VIDEO),
          //new AttrD("onselect"), //global attribute
          new AttrD(Attr.ONSTALLED, true, Tag.AUDIO, Tag.VIDEO),
          new AttrD(Attr.ONSTORAGE, true, Tag.BODY),
          new AttrD(Attr.ONSUBMIT, true, Tag.FORM),
          new AttrD(Attr.ONSUSPEND, true, Tag.AUDIO, Tag.VIDEO),
          new AttrD(Attr.ONTIMEUPDATE, true, Tag.AUDIO, Tag.VIDEO),
          new AttrD(Attr.ONTOGGLE, true, Tag.DETAILS),
          new AttrD(Attr.ONUNLOAD, true, Tag.BODY),
          new AttrD(Attr.ONVOLUMECHANGED, true, Tag.AUDIO, Tag.VIDEO),
          new AttrD(Attr.ONWAITING, true, Tag.AUDIO, Tag.VIDEO),
          //new AttrD("onwheel"), //global attribute
          new AttrD(Attr.OPEN, false, Tag.DETAILS),
          new AttrD(Attr.OPTIMUM, true, Tag.METER),
          new AttrD(Attr.PATTERN, true, Tag.INPUT),
          new AttrD(Attr.PLACEHOLDER, true, Tag.INPUT, Tag.TEXTAREA),
          new AttrD(Attr.POSTER, true, Tag.VIDEO),
          new AttrD(Attr.PRELOAD, true, Tag.AUDIO, Tag.VIDEO),
          new AttrD(Attr.READONLY, false, Tag.INPUT, Tag.TEXTAREA),
          new AttrD(Attr.REL, true, Tag.A, Tag.AREA, Tag.FORM, Tag.LINK),
          new AttrD(Attr.REQUIRED, false, Tag.INPUT, Tag.SELECT, Tag.TEXTAREA),
          new AttrD(Attr.REVERSED, false, Tag.OL),
          new AttrD(Attr.ROWS, true, Tag.TEXTAREA),
          new AttrD(Attr.ROWSPAN, true, Tag.TD, Tag.TH),
          new AttrD(Attr.SANDBOX, false, Tag.IFRAME),
          new AttrD(Attr.SCOPE, true, Tag.TH),
          new AttrD(Attr.SELECTED, false, Tag.OPTION),
          new AttrD(Attr.SHAPE, true, Tag.AREA),
          new AttrD(Attr.SIZE, true, Tag.INPUT, Tag.SELECT),
          new AttrD(Attr.SIZES, true, Tag.IMAGE, Tag.LINK, Tag.SOURCE),
          new AttrD(Attr.SPAN, true, Tag.COL, Tag.COLGROUP),
          //new AttrD("spellcheck"), //global attribute
          new AttrD(Attr.SRC, true, Tag.AUDIO, Tag.EMBED, Tag.IFRAME, Tag.IMAGE, Tag.INPUT, Tag.SCRIPT,
            Tag.SOURCE, Tag.TRACK, Tag.VIDEO),
          new AttrD(Attr.SRCDOC, true, Tag.IFRAME),
          new AttrD(Attr.SRCLANG, true, Tag.TRACK),
          new AttrD(Attr.SRCSET, true, Tag.IMAGE, Tag.SOURCE),
          new AttrD(Attr.START, true, Tag.OL),
          new AttrD(Attr.STEP, true, Tag.INPUT),
          //new AttrD("style"), //global attribute
          //new AttrD("tabindex"), //global attribute
          new AttrD(Attr.TARGET, true, Tag.A, Tag.AREA, Tag.BASE, Tag.FORM),
          //new AttrD("title"), //global attribute
          //new AttrD("translate"),// global attribute
          new AttrD(Attr.TYPE, true, Tag.A, Tag.BUTTON, Tag.EMBED, Tag.INPUT, Tag.LINK, Tag.MENU,
            Tag.OBJECT, Tag.SCRIPT, Tag.SOURCE, Tag.STYLE),
          new AttrD(Attr.USEMAP, true, Tag.IMAGE, Tag.OBJECT),
          new AttrD(Attr.VALUE, true, Tag.BUTTON, Tag.DATA, Tag.INPUT, Tag.LI, Tag.OPTION, Tag.METER,
            Tag.PROGRESS, Tag.PARAM),
          new AttrD(Attr.WIDTH, true, Tag.CANVAS, Tag.EMBED, Tag.IFRAME, Tag.IMAGE, Tag.INPUT, Tag.OBJECT,
            Tag.VIDEO),
          new AttrD(Attr.WRAP, true, Tag.TEXTAREA)
        );
    }
}
