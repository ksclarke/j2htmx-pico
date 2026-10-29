/*
 * This code was derived from j2html, which is distributed under the Apache License, Version 2.0.
 * See META-INF/THIRD-PARTY.txt and META-INF/licenses/Apache-2.0.txt for more information.
 *
 * This file has been modified for j2htmx-pico by Kevin S. Clarke (my.j2htmx-pico@kevinclarke.info)
 *
 * The modifications are licensed under the Mozilla Public License, Version 2.0.
 * See LICENSE.txt in the parent project's root for more information.
 */

package info.freelibrary.j2htmx.pico.codegen.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import info.freelibrary.util.warnings.Checkstyle;

/**
 * Comprehensive registry of HTML attribute descriptions and their associated tags.
 */
@SuppressWarnings({Checkstyle.MULTIPLE_STRING_LITERALS, "MultipleStringLiterals"})
public final class AttributesList {

    /** The input HTML tag name constant. */
    private static final String INPUT = "input";

    /** The form HTML tag name constant. */
    private static final String FORM = "form";

    /** The area HTML tag name constant. */
    private static final String AREA = "area";

    /** The img HTML tag name constant. */
    private static final String IMAGE = "img";

    /** The script HTML tag name constant. */
    private static final String SCRIPT = "script";

    /** The button HTML tag name constant. */
    private static final String BUTTON = "button";

    /** The select HTML tag name constant. */
    private static final String SELECT = "select";

    /** The textarea HTML tag name constant. */
    private static final String TEXTAREA = "textarea";

    /** The audio HTML tag name constant. */
    private static final String AUDIO = "audio";

    /** The video HTML tag name constant. */
    private static final String VIDEO = "video";

    /** The object HTML tag name constant. */
    private static final String OBJECT = "object";

    /** The track HTML tag name constant. */
    private static final String TRACK = "track";

    /** The option HTML tag name constant. */
    private static final String OPTION = "option";

    /** The meter HTML tag name constant. */
    private static final String METER = "meter";

    /** The embed HTML tag name constant. */
    private static final String EMBED = "embed";

    /** The iframe HTML tag name constant. */
    private static final String IFRAME = "iframe";

    /** The link HTML tag name constant. */
    private static final String LINK = "link";

    /** The source HTML tag name constant. */
    private static final String SOURCE = "source";

    /** The style HTML tag name constant. */
    private static final String STYLE = "style";

    /** The body HTML tag name constant. */
    private static final String BODY = "body";

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
          new AttrD("accept", true, INPUT),
          //new AttrD("accept-charset","form"), //contains dashes, TODO
          //new AttrD("accesskey"), //global attribute
          new AttrD("action", true, FORM),
          //"align", not supported in HTML5
          new AttrD("alt", true, AREA, IMAGE, INPUT),
          new AttrD("async", false, SCRIPT),
          new AttrD("autocomplete", false, FORM, INPUT),
          new AttrD("autofocus", false, BUTTON, INPUT, SELECT, TEXTAREA),
          new AttrD("autoplay", false, AUDIO, VIDEO),
          //"bgcolor", not supported in HTMTL5
          //"border", not supported in HTML5
          new AttrD("charset", true, "meta", SCRIPT),
          new AttrD("checked", false, INPUT),
          new AttrD("cite", true, "blockquote", "del", "ins", "q"),
          //"class" already implemented in Tag.java // global attribute
          new AttrD("cols", true, TEXTAREA),
          new AttrD("colspan", true, "td", "th"),
          new AttrD("content", true, "meta"),
          //"contenteditable" global attribute, should be in Tag.java
          new AttrD("controls", false, AUDIO, VIDEO),
          new AttrD("coords", true, AREA),
          new AttrD("data", true, OBJECT),
          new AttrD("datetime", true, "del", "ins", "time"),
          new AttrD("default", false, TRACK),
          new AttrD("defer", false, SCRIPT),
          //new AttrD("dir"), //global attribute
          new AttrD("dirname", true, INPUT, TEXTAREA),
          new AttrD("disabled", false, BUTTON, "fieldset", INPUT, "optgroup", OPTION, SELECT, TEXTAREA),
          new AttrD("download", false, "a", AREA),
          //new AttrD("draggable") global attribute, should be in Tag.java
          new AttrD("enctype", true, FORM),
          new AttrD("for", true, "label", "output"),
          new AttrD("form", true, BUTTON, "fieldset", INPUT, "label", METER, OBJECT, "output", SELECT, TEXTAREA),
          new AttrD("formaction", true, BUTTON, INPUT),
          new AttrD("headers", true, "td", "th"),
          new AttrD("height", true, "canvas", EMBED, IFRAME, IMAGE, INPUT, OBJECT, VIDEO),
          //new AttrD("hidden"), global attribute
          new AttrD("high", true, METER),
          new AttrD("href", true, "a", AREA, "base", LINK),
          new AttrD("hreflang", true, "a", AREA, LINK),
          //"http-equiv", //TODO: '-' has problematic in code generation
          //"id" global attribute, should be in Tag.java
          new AttrD("ismap", false, IMAGE),
          new AttrD("kind", true, TRACK),
          new AttrD("label", true, TRACK, OPTION, "optgroup"),
          //"lang" global attribute, should be in Tag.java
          new AttrD("list", true, INPUT),
          new AttrD("loop", false, AUDIO, VIDEO),
          new AttrD("low", true, METER),
          new AttrD("max", true, INPUT, METER, "progress"),
          new AttrD("maxlength", true, INPUT, TEXTAREA),
          new AttrD("media", true, "a", AREA, LINK, SOURCE, STYLE),
          new AttrD("method", true, FORM),
          new AttrD("min", true, INPUT, METER),
          new AttrD("multiple", false, INPUT, SELECT),
          new AttrD("muted", false, VIDEO, AUDIO),
          new AttrD("name", true, BUTTON, "fieldset", FORM, IFRAME, INPUT, "map", "meta", OBJECT, "output",
            "param", SELECT, "slot", TEXTAREA),
          new AttrD("novalidate", false, FORM),
          new AttrD("onabort", true, AUDIO, EMBED, IMAGE, OBJECT, VIDEO),
          new AttrD("onafterprint", true, BODY),
          new AttrD("onbeforeprint", true, BODY),
          new AttrD("onbeforeunload", true, BODY),
          //new AttrD("onblur"), global attribute
          new AttrD("oncanplay", true, AUDIO, EMBED, OBJECT, VIDEO),
          new AttrD("oncanplaythrough", true, VIDEO, VIDEO),
            /* a bunch of event attributes that are on all visible elements (so should be in Tag.java)
            "onchange",
            "onclick",
            "oncontextmenu",
            "oncopy",
            */
          new AttrD("oncuechange", true, TRACK),
            /*
            "oncut",
            ...
            "ondrop",
             */
          new AttrD("ondurationchange", true, AUDIO, VIDEO),
          new AttrD("onemptied", true, AUDIO, VIDEO),
          new AttrD("onended", true, AUDIO, VIDEO),
          new AttrD("onerror", true, AUDIO, BODY, EMBED, IMAGE, OBJECT, SCRIPT, STYLE, VIDEO),
          //new AttrD("onfocus"),// global attribute
          new AttrD("onhashchange", true, BODY),
          // ... a bunch of event attributes visible on all elements
          new AttrD("onload", true, BODY, IFRAME, IMAGE, INPUT, LINK, SCRIPT, STYLE),
          new AttrD("onloadeddata", true, AUDIO, VIDEO),
          new AttrD("onloadedmetadata", true, AUDIO, VIDEO),
          new AttrD("onloadstart", true, AUDIO, VIDEO),
          // ... a bunch of event attributes visible on all elements
          new AttrD("onoffline", true, BODY),
          new AttrD("ononline", true, BODY),
          new AttrD("onpagehide", true, BODY),
          new AttrD("onpageshow", true, BODY),
          //new AttrD("onpaste"),// global attribute
          new AttrD("onpause", true, AUDIO, VIDEO),
          new AttrD("onplay", true, AUDIO, VIDEO),
          new AttrD("onplaying", true, AUDIO, VIDEO),
          new AttrD("onpopstate", true, BODY),
          new AttrD("onprogress", true, AUDIO, VIDEO),
          new AttrD("onratechange", true, AUDIO, VIDEO),
          new AttrD("onreset", true, FORM),
          new AttrD("onresize", true, BODY),
          //new AttrD("onscroll"), //global attribute
          new AttrD("onsearch", true, INPUT),
          new AttrD("onseeked", true, AUDIO, VIDEO),
          new AttrD("onseeking", true, AUDIO, VIDEO),
          //new AttrD("onselect"), //global attribute
          new AttrD("onstalled", true, AUDIO, VIDEO),
          new AttrD("onstorage", true, BODY),
          new AttrD("onsubmit", true, FORM),
          new AttrD("onsuspend", true, AUDIO, VIDEO),
          new AttrD("ontimeupdate", true, AUDIO, VIDEO),
          new AttrD("ontoggle", true, "details"),
          new AttrD("onunload", true, BODY),
          new AttrD("onvolumechanged", true, AUDIO, VIDEO),
          new AttrD("onwaiting", true, AUDIO, VIDEO),
          //new AttrD("onwheel"), //global attribute
          new AttrD("open", false, "details"),
          new AttrD("optimum", true, METER),
          new AttrD("pattern", true, INPUT),
          new AttrD("placeholder", true, INPUT, TEXTAREA),
          new AttrD("poster", true, VIDEO),
          new AttrD("preload", true, AUDIO, VIDEO),
          new AttrD("readonly", false, INPUT, TEXTAREA),
          new AttrD("rel", true, "a", AREA, FORM, LINK),
          new AttrD("required", false, INPUT, SELECT, TEXTAREA),
          new AttrD("reversed", false, "ol"),
          new AttrD("rows", true, TEXTAREA),
          new AttrD("rowspan", true, "td", "th"),
          new AttrD("sandbox", false, IFRAME),
          new AttrD("scope", true, "th"),
          new AttrD("selected", false, OPTION),
          new AttrD("shape", true, AREA),
          new AttrD("size", true, INPUT, SELECT),
          new AttrD("sizes", true, IMAGE, LINK, SOURCE),
          new AttrD("span", true, "col", "colgroup"),
          //new AttrD("spellcheck"), //global attribute
          new AttrD("src", true, AUDIO, EMBED, IFRAME, IMAGE, INPUT, SCRIPT, SOURCE, TRACK, VIDEO),
          new AttrD("srcdoc", true, IFRAME),
          new AttrD("srclang", true, TRACK),
          new AttrD("srcset", true, IMAGE, SOURCE),
          new AttrD("start", true, "ol"),
          new AttrD("step", true, INPUT),
          //new AttrD("style"), //global attribute
          //new AttrD("tabindex"), //global attribute
          new AttrD("target", true, "a", AREA, "base", FORM),
          //new AttrD("title"), //global attribute
          //new AttrD("translate"),// global attribute
          new AttrD("type", true, "a", BUTTON, EMBED, INPUT, LINK, "menu", OBJECT, SCRIPT, SOURCE,
            "style"),
          new AttrD("usemap", true, IMAGE, OBJECT),
          new AttrD("value", true, BUTTON, "data", INPUT, "li", OPTION, METER, "progress", "param"),
          new AttrD("width", true, "canvas", EMBED, IFRAME, IMAGE, INPUT, OBJECT, VIDEO),
          new AttrD("wrap", true, TEXTAREA)
        );
    }
}
