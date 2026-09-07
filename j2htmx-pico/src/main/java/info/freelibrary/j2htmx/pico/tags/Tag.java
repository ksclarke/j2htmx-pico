package info.freelibrary.j2htmx.pico.tags;

import info.freelibrary.j2htmx.pico.attributes.Attr;
import info.freelibrary.j2htmx.pico.attributes.Attribute;
import java.util.ArrayList;
import java.util.Iterator;

public abstract class Tag<T extends Tag<T>> extends DomContent implements Instance<T> {
    private final String tagName;
    private final ArrayList<Attribute> attributes;

    protected Tag(String tagName) {
        this.tagName = tagName;
        this.attributes = new ArrayList<>();
    }

    public String getTagName() {
        return this.tagName;
    }

    protected boolean hasTagName() {
        return tagName != null && !tagName.isEmpty();
    }

    protected ArrayList<Attribute> getAttributes() {
        return attributes;
    }

    /**
     * Sets an attribute on an element
     *
     * @param name  the attribute
     * @param value the attribute value
     */
    boolean setAttribute(String name, String value) {
        if (value == null) {
            return attributes.add(new Attribute(name));
        }
        for (Attribute attribute : attributes) {
            if (attribute.getName().equals(name)) {
                attribute.setValue(value); // update with new value
                return true;
            }
        }
        return attributes.add(new Attribute(name, value));
    }

    /**
     * Sets a custom attribute
     *
     * @param attribute the attribute name
     * @param value     the attribute value
     * @return itself for easy chaining
     */
    public T attr(String attribute, Object value) {
        setAttribute(attribute, value == null ? null : String.valueOf(value));
        return self();
    }

    /**
     * Adds the specified attribute. If the Tag previously contained an attribute with the same name, the old attribute is replaced by the specified attribute.
     *
     * @param attribute the attribute
     * @return itself for easy chaining
     */
    public T attr(Attribute attribute) {
        Iterator<Attribute> iterator = attributes.iterator();
        String name = attribute.getName();
        if (name != null) {
            // name == null is allowed, but those Attributes are not rendered. So we add them anyway.
            while (iterator.hasNext()) {
                Attribute existingAttribute = iterator.next();
                if (existingAttribute.getName().equals(name)) {
                    iterator.remove();
                }
            }
        }
        attributes.add(attribute);
        return self();
    }

    /**
     * Sets a custom attribute without value
     *
     * @param attribute the attribute name
     * @return itself for easy chaining
     * @see Tag#attr(String, Object)
     */
    public T attr(String attribute) {
        return attr(attribute, null);
    }

    /**
     * Call attr-method based on condition
     * {@link #attr(String attribute, Object value)}
     *
     * @param condition the condition
     * @param attribute the attribute name
     * @param value     the attribute value
     * @return itself for easy chaining
     */
    public T condAttr(boolean condition, String attribute, String value) {
        return (condition ? attr(attribute, value) : self());
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Tag)) {
            return false;
        }
        return ((Tag<?>) obj).render().equals(this.render());
    }

    /**
     * Convenience methods that call attr with predefined attributes
     *
     * @return itself for easy chaining
     */
    public T withClasses(String... classes) {
        StringBuilder sb = new StringBuilder();
        for (String s : classes) {
            sb.append(s != null ? s : "").append(" ");
        }
        return attr(Attr.CLASS, sb.toString().trim());
    }

    /*
    Tag.java contains all Global Attributes, Attributes which are
    valid on all HTML Tags. Reference:
    https://www.w3schools.com/tags/ref_standardattributes.asp
    Attributes:

    accesskey
    class
    contenteditable
    data-*
    dir
    draggable
    hidden
    id
    lang
    spellcheck
    style
    tabindex
    title
    translate
     */

    public T withAccesskey(String accesskey){ return attr(Attr.ACCESSKEY, accesskey); }

    public T withClass(String className) { return attr(Attr.CLASS, className); }

    public T isContenteditable(){ return attr(Attr.CONTENTEDITABLE, "true"); }

    public T withData(String dataAttr, String value) { return attr(Attr.DATA + "-" + dataAttr, value); }

    public T withDir(String dir) { return attr(Attr.DIR, dir); }

    public T isDraggable(){ return attr(Attr.DRAGGABLE, "true"); }

    public T isHidden() { return attr(Attr.HIDDEN, null); }

    public T withId(String id) { return attr(Attr.ID, id); }

    public T withIs(String element){ return attr(Attr.IS, element); }

    public T withLang(String lang) { return attr(Attr.LANG, lang); }

    public T withSlot(String name){ return attr(Attr.SLOT, name); }

    public T isSpellcheck(){ return attr(Attr.SPELLCHECK, "true"); }

    public T withStyle(String style) { return attr(Attr.STYLE, style); }

    public T withTabindex(int index){ return attr(Attr.TABINDEX, index); }

    public T withTitle(String title) { return attr(Attr.TITLE, title); }

    public T isTranslate(){ return attr(Attr.TRANSLATE, "yes"); }

    // htmx
    public T isHx() { return attr(Attr.HX); }
    public T withHxGet(String url) { return attr(Attr.HX_GET, url); }
    public T withHxPost(String url) { return attr(Attr.HX_POST, url); }
    public T withHxPut(String url) { return attr(Attr.HX_PUT, url); }
    public T withHxDelete(String url) { return attr(Attr.HX_DELETE, url); }
    public T withHxPatch(String url) { return attr(Attr.HX_PATCH, url); }
    public T withHxTrigger(String trigger) { return attr(Attr.HX_TRIGGER, trigger); }
    public T withHxTarget(String target) { return attr(Attr.HX_TARGET, target); }
    public T withHxSwap(String swap) { return attr(Attr.HX_SWAP, swap); }
    public T withHxSwapOob(String swapOob) { return attr(Attr.HX_SWAP_OOB, swapOob); }
    public T withHxSelect(String select) { return attr(Attr.HX_SELECT, select); }
    public T withHxPushUrl(String pushUrl) { return attr(Attr.HX_PUSH_URL, pushUrl); }
    public T withHxVals(String vals) { return attr(Attr.HX_VALS, vals); }
    public T withHxConfirm(String confirm) { return attr(Attr.HX_CONFIRM, confirm); }
    public T withHxBoost(String boost) { return attr(Attr.HX_BOOST, boost); }
    public T withHxOn(String event, String value) { return attr(Attr.HX_ON + ":" + event, value); }
    public T withHxParams(String params) { return attr(Attr.HX_PARAMS, params); }
    public T withHxHeaders(String headers) { return attr(Attr.HX_HEADERS, headers); }
    public T withHxSync(String sync) { return attr(Attr.HX_SYNC, sync); }
    public T withHxExt(String ext) { return attr(Attr.HX_EXT, ext); }
    public T withHxDisinherit(String disinherit) { return attr(Attr.HX_DISINHERIT, disinherit); }
    public T withHxIndicator(String indicator) { return attr(Attr.HX_INDICATOR, indicator); }
    public T withHxHistory(String history) { return attr(Attr.HX_HISTORY, history); }
    public T withHxHistoryElt(String historyElt) { return attr(Attr.HX_HISTORY_ELT, historyElt); }
    public T withHxInclude(String include) { return attr(Attr.HX_INCLUDE, include); }
    public T withHxPrompt(String prompt) { return attr(Attr.HX_PROMPT, prompt); }
    public T withHxPtag(String ptag) { return attr(Attr.HX_PTAG, ptag); }
    public T withHxDownload(String download) { return attr(Attr.HX_DOWNLOAD, download); }
    public T withHxHead(String head) { return attr(Attr.HX_HEAD, head); }
    public T withHxTargets(String targets) { return attr(Attr.HX_TARGETS, targets); }
    public T withHxUpsert(String upsert) { return attr(Attr.HX_UPSERT, upsert); }
    public T withHxStatus(String status) { return attr(Attr.HX_STATUS, status); }
    public T withHxIgnore(String ignore) { return attr(Attr.HX_IGNORE, ignore); }
    public T withHxHistoryCache(String historyCache) { return attr(Attr.HX_HISTORY_CACHE, historyCache); }
    public T withHxCsp(String csp) { return attr(Attr.HX_CSP, csp); }
    public T withHxAlpineCompat(String alpineCompat) { return attr(Attr.HX_ALPINE_COMPAT, alpineCompat); }
    public T withHxMultipart(String multipart) { return attr(Attr.HX_MULTIPART, multipart); }
    public T withHxLive(String live) { return attr(Attr.HX_LIVE, live); }
    public T withHxSse(String sse) { return attr(Attr.HX_SSE, sse); }
    public T withHxWs(String ws) { return attr(Attr.HX_WS, ws); }
    public T withHxValidate(String validate) { return attr(Attr.HX_VALIDATE, validate); }

    // ----- start of withCond$ATTR variants -----
    public T withCondAccessKey(boolean condition, String accesskey){ return condAttr(condition, Attr.ACCESSKEY, accesskey); }

    public T withCondClass(boolean condition, String className) { return condAttr(condition, Attr.CLASS, className); }

    public T withCondContenteditable(boolean condition){ return attr(Attr.CONTENTEDITABLE, (condition)?"true":"false");}

    public T withCondData(boolean condition, String dataAttr, String value) { return condAttr(condition, Attr.DATA + "-" + dataAttr, value); }

    public T withCondDir(boolean condition, String dir) { return condAttr(condition, Attr.DIR, dir); }

    public T withCondDraggable(boolean condition){ return attr(Attr.DRAGGABLE, (condition)?"true":"false"); }

    public T withCondHidden(boolean condition) { return condAttr(condition, Attr.HIDDEN, null); }

    public T withCondId(boolean condition, String id) { return condAttr(condition, Attr.ID, id); }

    public T withCondIs(boolean condition, String element){ return condAttr(condition, Attr.IS, element); }

    public T withCondLang(boolean condition, String lang) { return condAttr(condition, Attr.LANG, lang); }

    public T withCondSlot(boolean condition, String name){ return condAttr(condition, Attr.SLOT, name); }

    public T withCondSpellcheck(boolean condition){ return attr(Attr.SPELLCHECK, (condition)?"true":"false"); }

    public T withCondStyle(boolean condition, String style) { return condAttr(condition, Attr.STYLE, style); }

    public T withCondTabindex(boolean condition, int index){ return condAttr(condition, Attr.TABINDEX, index+""); }

    public T withCondTitle(boolean condition, String title) { return condAttr(condition, Attr.TITLE, title); }

    public T withCondTranslate(boolean condition){ return attr(Attr.TRANSLATE, (condition)?"yes":"no"); }

    public T withCondHx(boolean condition) { return condAttr(condition, Attr.HX, null); }
    public T withCondHxGet(boolean condition, String url) { return condAttr(condition, Attr.HX_GET, url); }
    public T withCondHxPost(boolean condition, String url) { return condAttr(condition, Attr.HX_POST, url); }
    public T withCondHxPut(boolean condition, String url) { return condAttr(condition, Attr.HX_PUT, url); }
    public T withCondHxDelete(boolean condition, String url) { return condAttr(condition, Attr.HX_DELETE, url); }
    public T withCondHxPatch(boolean condition, String url) { return condAttr(condition, Attr.HX_PATCH, url); }
    public T withCondHxTrigger(boolean condition, String trigger) { return condAttr(condition, Attr.HX_TRIGGER, trigger); }
    public T withCondHxTarget(boolean condition, String target) { return condAttr(condition, Attr.HX_TARGET, target); }
    public T withCondHxSwap(boolean condition, String swap) { return condAttr(condition, Attr.HX_SWAP, swap); }
    public T withCondHxSwapOob(boolean condition, String swapOob) { return condAttr(condition, Attr.HX_SWAP_OOB, swapOob); }
    public T withCondHxSelect(boolean condition, String select) { return condAttr(condition, Attr.HX_SELECT, select); }
    public T withCondHxPushUrl(boolean condition, String pushUrl) { return condAttr(condition, Attr.HX_PUSH_URL, pushUrl); }
    public T withCondHxVals(boolean condition, String vals) { return condAttr(condition, Attr.HX_VALS, vals); }
    public T withCondHxConfirm(boolean condition, String confirm) { return condAttr(condition, Attr.HX_CONFIRM, confirm); }
    public T withCondHxBoost(boolean condition, String boost) { return condAttr(condition, Attr.HX_BOOST, boost); }
    public T withCondHxOn(boolean condition, String event, String value) { return condAttr(condition, Attr.HX_ON + ":" + event, value); }
    public T withCondHxParams(boolean condition, String params) { return condAttr(condition, Attr.HX_PARAMS, params); }
    public T withCondHxHeaders(boolean condition, String headers) { return condAttr(condition, Attr.HX_HEADERS, headers); }
    public T withCondHxSync(boolean condition, String sync) { return condAttr(condition, Attr.HX_SYNC, sync); }
    public T withCondHxExt(boolean condition, String ext) { return condAttr(condition, Attr.HX_EXT, ext); }
    public T withCondHxDisinherit(boolean condition, String disinherit) { return condAttr(condition, Attr.HX_DISINHERIT, disinherit); }
    public T withCondHxIndicator(boolean condition, String indicator) { return condAttr(condition, Attr.HX_INDICATOR, indicator); }
    public T withCondHxHistory(boolean condition, String history) { return condAttr(condition, Attr.HX_HISTORY, history); }
    public T withCondHxHistoryElt(boolean condition, String historyElt) { return condAttr(condition, Attr.HX_HISTORY_ELT, historyElt); }
    public T withCondHxInclude(boolean condition, String include) { return condAttr(condition, Attr.HX_INCLUDE, include); }
    public T withCondHxPrompt(boolean condition, String prompt) { return condAttr(condition, Attr.HX_PROMPT, prompt); }
    public T withCondHxPtag(boolean condition, String ptag) { return condAttr(condition, Attr.HX_PTAG, ptag); }
    public T withCondHxDownload(boolean condition, String download) { return condAttr(condition, Attr.HX_DOWNLOAD, download); }
    public T withCondHxHead(boolean condition, String head) { return condAttr(condition, Attr.HX_HEAD, head); }
    public T withCondHxTargets(boolean condition, String targets) { return condAttr(condition, Attr.HX_TARGETS, targets); }
    public T withCondHxUpsert(boolean condition, String upsert) { return condAttr(condition, Attr.HX_UPSERT, upsert); }
    public T withCondHxStatus(boolean condition, String status) { return condAttr(condition, Attr.HX_STATUS, status); }
    public T withCondHxIgnore(boolean condition, String ignore) { return condAttr(condition, Attr.HX_IGNORE, ignore); }
    public T withCondHxHistoryCache(boolean condition, String historyCache) { return condAttr(condition, Attr.HX_HISTORY_CACHE, historyCache); }
    public T withCondHxCsp(boolean condition, String csp) { return condAttr(condition, Attr.HX_CSP, csp); }
    public T withCondHxAlpineCompat(boolean condition, String alpineCompat) { return condAttr(condition, Attr.HX_ALPINE_COMPAT, alpineCompat); }
    public T withCondHxMultipart(boolean condition, String multipart) { return condAttr(condition, Attr.HX_MULTIPART, multipart); }
    public T withCondHxLive(boolean condition, String live) { return condAttr(condition, Attr.HX_LIVE, live); }
    public T withCondHxSse(boolean condition, String sse) { return condAttr(condition, Attr.HX_SSE, sse); }
    public T withCondHxWs(boolean condition, String ws) { return condAttr(condition, Attr.HX_WS, ws); }
    public T withCondHxValidate(boolean condition, String validate) { return condAttr(condition, Attr.HX_VALIDATE, validate); }
}
