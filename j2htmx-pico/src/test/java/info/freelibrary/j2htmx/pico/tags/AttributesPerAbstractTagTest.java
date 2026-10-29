package info.freelibrary.j2htmx.pico.tags;

import static info.freelibrary.j2htmx.pico.TagCreator.a;
import static info.freelibrary.j2htmx.pico.TagCreator.area;
import static info.freelibrary.j2htmx.pico.TagCreator.audio;
import static info.freelibrary.j2htmx.pico.TagCreator.blockquote;
import static info.freelibrary.j2htmx.pico.TagCreator.button;
import static info.freelibrary.j2htmx.pico.TagCreator.div;
import static info.freelibrary.j2htmx.pico.TagCreator.form;
import static info.freelibrary.j2htmx.pico.TagCreator.iframe;
import static info.freelibrary.j2htmx.pico.TagCreator.img;
import static info.freelibrary.j2htmx.pico.TagCreator.input;
import static info.freelibrary.j2htmx.pico.TagCreator.label;
import static info.freelibrary.j2htmx.pico.TagCreator.meta;
import static info.freelibrary.j2htmx.pico.TagCreator.object;
import static info.freelibrary.j2htmx.pico.TagCreator.script;
import static info.freelibrary.j2htmx.pico.TagCreator.td;
import static info.freelibrary.j2htmx.pico.TagCreator.textarea;
import static info.freelibrary.j2htmx.pico.TagCreator.time;
import static info.freelibrary.j2htmx.pico.TagCreator.track;
import static info.freelibrary.j2htmx.pico.TagCreator.video;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import info.freelibrary.util.warnings.Checkstyle;
import org.junit.jupiter.api.Test;

/**
 * Tests verifying custom HTML attribute generation on tags.
 */
@SuppressWarnings({Checkstyle.MULTIPLE_STRING_LITERALS, "MultipleStringLiterals"})
public final class AttributesPerAbstractTagTest {

    //This class should contain Tests that check
    //if the generated Interface Methods indeed produce the correct HTML for
    //their custom HTML Attributes.

    //TODO: WARNING: only testing .with$ATTR and .is$ATTR Methods, not the 'Cond' Variants

    /**
     * Tests first group of tag attributes.
     */
    @Test
    public void testAttributesPerTag1() {
        //TEST:
        //accept, action, alt, async,
        //autocomplete, autofocus,
        //autoplay, charset, checked
        assertThat(input().withAccept("image/*").render(), is("<input accept=\"image/*\">"));
        assertThat(form().withAction("/1/2").render(), is("<form action=\"/1/2\"></form>"));
        assertThat(img().withAlt("horse").render(), is("<img alt=\"horse\">"));
        assertThat(script().isAsync().render(), is("<script async></script>"));
        assertThat(form().hasAutoComplete().render(), is("<form autocomplete=\"on\"></form>"));
        assertThat(input().hasAutoFocus().render(), is("<input autofocus>"));
        assertThat(video().hasAutoPlay().render(), is("<video autoplay></video>"));
        assertThat(script().withCharset("mycharset").render(), is("<script charset=\"mycharset\"></script>"));
        assertThat(script().withCondCharset(true, "mycharset").render(), is("<script charset=\"mycharset\"></script>"));
        assertThat(script().withCondCharset(false, "mycharset").render(), is("<script></script>"));
        assertThat(input().isChecked().render(), is("<input checked>"));
    }

    /**
     * Tests second group of tag attributes.
     */
    @Test
    public void testAttributesPerTag2() {
        //TEST: cite, cols, colspan, content, controls
        assertThat(blockquote().withCite("hi").render(), is("<blockquote cite=\"hi\"></blockquote>"));
        assertThat(textarea().withCols("3").render(), is("<textarea cols=\"3\"></textarea>"));
        assertThat(td().withColSpan("4").render(), is("<td colspan=\"4\"></td>"));
        assertThat(meta().withContent("mycontent").render(), is("<meta content=\"mycontent\">"));
        assertThat(audio().hasControls().render(), is("<audio controls></audio>"));
    }

    /**
     * Tests third group of tag attributes.
     */
    @Test
    public void testAttributesPerTag3() {
        //TEST: coords, data, datetime, default, defer, dirname
        assertThat(area().withCoords("0,0,82,126").render(), is("<area coords=\"0,0,82,126\">"));
        assertThat(object().withData("/1/2").render(), is("<object data=\"/1/2\"></object>"));
        assertThat(time().withDateTime("2015-11-15T22:55:03Z").render(),
          is("<time datetime=\"2015-11-15T22:55:03Z\"></time>"));
        assertThat(object().withData("/1/2").render(), is("<object data=\"/1/2\"></object>"));
        assertThat(track().isDefault().render(), is("<track default>"));
        assertThat(script().isDeferred().render(), is("<script defer></script>"));
        assertThat(script().withCondDeferred(true).render(), is("<script defer></script>"));
        assertThat(input().withDirName("fname.dir").render(), is("<input dirname=\"fname.dir\">"));
    }

    /**
     * Tests fourth group of tag attributes.
     */
    @Test
    public void testAttributesPerTag4() {
        //TEST: disabled, download, enctype, for, form, formaction
        assertThat(input().isDisabled().render(), is("<input disabled>"));
        assertThat(a().hasDownload().render(), is("<a download></a>"));
        assertThat(form().withEncType("enc").render(), is("<form enctype=\"enc\"></form>"));
        assertThat(label().withFor("idk").render(), is("<label for=\"idk\"></label>"));
        assertThat(button().withForm("myform").render(), is("<button form=\"myform\"></button>"));
        assertThat(button().withFormAction("/1/2").render(), is("<button formaction=\"/1/2\"></button>"));
    }

    /**
     * Tests fifth group of tag attributes.
     */
    @Test
    public void testAttributesPerTag5() {
        //TEST: loop, sandbox, ismap, novalidate, spellcheck, translate, hx
        assertThat(video().hasLoop().render(), is("<video loop></video>"));
        assertThat(iframe().hasSandbox().render(), is("<iframe sandbox></iframe>"));
        assertThat(img().hasIsMap().render(), is("<img ismap>"));
        assertThat(form().hasNoValidate().render(), is("<form novalidate></form>"));
        assertThat(div().hasSpellCheck().render(), is("<div spellcheck=\"true\"></div>"));
        assertThat(div().withTranslate().render(), is("<div translate=\"yes\"></div>"));
        assertThat(div().hasHx().render(), is("<div hx></div>"));
    }

    /**
     * Tests sixth group of multi-word tag attributes.
     */
    @Test
    public void testAttributesPerTag6() {
        //TEST: readonly, rowspan, srcdoc, srclang, srcset, usemap, hreflang, maxlength, onabort, contenteditable
        assertThat(input().isReadOnly().render(), is("<input readonly>"));
        assertThat(td().withRowSpan("2").render(), is("<td rowspan=\"2\"></td>"));
        assertThat(iframe().withSrcDoc("<p>test</p>").render(),
          is("<iframe srcdoc=\"&lt;p&gt;test&lt;/p&gt;\"></iframe>"));
        assertThat(track().withSrcLang("en").render(), is("<track srclang=\"en\">"));
        assertThat(img().withSrcSet("img.jpg 1x").render(), is("<img srcset=\"img.jpg 1x\">"));
        assertThat(img().withUseMap("#map").render(), is("<img usemap=\"#map\">"));
        assertThat(a().withHrefLang("en").render(), is("<a hreflang=\"en\"></a>"));
        assertThat(input().withMaxLength("10").render(), is("<input maxlength=\"10\">"));
        assertThat(img().withOnAbort("abort()").render(), is("<img onabort=\"abort()\">"));
        assertThat(div().isContentEditable().render(), is("<div contenteditable=\"true\"></div>"));
        assertThat(div().withAccessKey("s").render(), is("<div accesskey=\"s\"></div>"));
        assertThat(div().withTabIndex(1).render(), is("<div tabindex=\"1\"></div>"));
    }

    //TODO: leaving the rest of the tests for the rest
    //of the custom attributes for later, or for other people
    //to possibly implement
}
