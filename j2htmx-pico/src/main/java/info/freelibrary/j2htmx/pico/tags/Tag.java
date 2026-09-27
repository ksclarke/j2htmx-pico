package info.freelibrary.j2htmx.pico.tags;

import info.freelibrary.j2htmx.pico.attributes.Attr;
import info.freelibrary.j2htmx.pico.attributes.Attribute;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Base class for all HTML tags, providing fluent builder methods for global, HTMX, and Pico CSS attributes.
 *
 * @param <T> The concrete tag type implementing this class
 */
public abstract class Tag<T extends Tag<T>> extends DomContent implements Instance<T> {
    /** The HTML tag name. */
    private final String tagName;

    /** The list of attributes belonging to this tag. */
    private final ArrayList<Attribute> attributes;

    /**
     * Constructs a Tag with the specified tag name.
     *
     * @param tagName The HTML tag name
     */
    protected Tag(final String tagName) {
        this.tagName = tagName;
        this.attributes = new ArrayList<>();
    }

    /**
     * Gets the HTML tag name.
     *
     * @return The tag name
     */
    public String getTagName() {
        return this.tagName;
    }

    /**
     * Checks if this tag has a valid tag name.
     *
     * @return True if tag name is non-null and non-empty, false otherwise
     */
    protected boolean hasTagName() {
        return tagName != null && !tagName.isEmpty();
    }

    /**
     * Gets the list of attributes associated with this tag.
     *
     * @return The attribute list
     */
    protected ArrayList<Attribute> getAttributes() {
        return attributes;
    }

    /**
     * Sets an attribute on an element
     *
     * @param name  the attribute
     * @param value the attribute value
     */
    boolean setAttribute(final String name, final String value) {
        if (value == null) {
            return attributes.add(new Attribute(name));
        }
        for (final Attribute attribute : attributes) {
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
    public T attr(final String attribute, final Object value) {
        setAttribute(attribute, value == null ? null : String.valueOf(value));
        return self();
    }

    /**
     * Adds the specified attribute. If the Tag previously contained an attribute with the same name, the old attribute is replaced by the specified attribute.
     *
     * @param attribute the attribute
     * @return itself for easy chaining
     */
    public T attr(final Attribute attribute) {
        final Iterator<Attribute> iterator = attributes.iterator();
        final String name = attribute.getName();
        if (name != null) {
            // name == null is allowed, but those Attributes are not rendered. So we add them anyway.
            while (iterator.hasNext()) {
                final Attribute existingAttribute = iterator.next();
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
    public T attr(final String attribute) {
        return attr(attribute, null);
    }

    /**
     * Call attr-method based on condition
     * {@link #attr(String, Object)}
     *
     * @param condition the condition
     * @param attribute the attribute name
     * @param value     the attribute value
     * @return itself for easy chaining
     */
    public T condAttr(final boolean condition, final String attribute, final String value) {
        return (condition ? attr(attribute, value) : self());
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof Tag)) {
            return false;
        }
        return ((Tag<?>) obj).render().equals(this.render());
    }

    /**
     * Convenience methods that call attr with predefined attributes
     *
     * @param classes The CSS classes to add
     * @return itself for easy chaining
     */
    public T withClasses(final String... classes) {
        final StringBuilder sb = new StringBuilder();
        for (final String s : classes) {
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

    /**
     * Sets the {@code accesskey} attribute.
     *
     * @param accesskey The accesskey value
     * @return Itself for easy chaining
     */
        public T withAccesskey(final String accesskey){ return attr(Attr.ACCESSKEY, accesskey); }

    /**
     * Sets the {@code class} attribute.
     *
     * @param className The className value
     * @return Itself for easy chaining
     */
        public T withClass(final String className) { return attr(Attr.CLASS, className); }

    /**
     * Sets the {@code contenteditable} attribute.
     *
     * @return Itself for easy chaining
     */
        public T isContenteditable(){ return attr(Attr.CONTENTEDITABLE, "true"); }

    /**
     * Sets the {@code data} attribute.
     *
     * @param dataAttr The dataAttr value
     * @param value The attribute value
     * @return Itself for easy chaining
     */
        public T withData(final String dataAttr, final String value) { return attr(Attr.DATA + "-" + dataAttr, value); }

    /**
     * Sets the {@code dir} attribute.
     *
     * @param dir The dir value
     * @return Itself for easy chaining
     */
        public T withDir(final String dir) { return attr(Attr.DIR, dir); }

    /**
     * Sets the {@code draggable} attribute.
     *
     * @return Itself for easy chaining
     */
        public T isDraggable(){ return attr(Attr.DRAGGABLE, "true"); }

    /**
     * Sets the {@code hidden} attribute.
     *
     * @return Itself for easy chaining
     */
        public T isHidden() { return attr(Attr.HIDDEN, null); }

    /**
     * Sets the {@code id} attribute.
     *
     * @param id The id value
     * @return Itself for easy chaining
     */
        public T withId(final String id) { return attr(Attr.ID, id); }

    /**
     * Sets the {@code is} attribute.
     *
     * @param element The element value
     * @return Itself for easy chaining
     */
        public T withIs(final String element){ return attr(Attr.IS, element); }

    /**
     * Sets the {@code lang} attribute.
     *
     * @param lang The lang value
     * @return Itself for easy chaining
     */
        public T withLang(final String lang) { return attr(Attr.LANG, lang); }

    /**
     * Sets the {@code slot} attribute.
     *
     * @param name The name value
     * @return Itself for easy chaining
     */
        public T withSlot(final String name){ return attr(Attr.SLOT, name); }

    /**
     * Sets the {@code spellcheck} attribute.
     *
     * @return Itself for easy chaining
     */
        public T isSpellcheck(){ return attr(Attr.SPELLCHECK, "true"); }

    /**
     * Sets the {@code style} attribute.
     *
     * @param style The style value
     * @return Itself for easy chaining
     */
        public T withStyle(final String style) { return attr(Attr.STYLE, style); }

    /**
     * Sets the {@code tabindex} attribute.
     *
     * @param index The index value
     * @return Itself for easy chaining
     */
        public T withTabindex(final int index){ return attr(Attr.TABINDEX, index); }

    /**
     * Sets the {@code title} attribute.
     *
     * @param title The title value
     * @return Itself for easy chaining
     */
        public T withTitle(final String title) { return attr(Attr.TITLE, title); }

    /**
     * Sets the {@code translate} attribute.
     *
     * @return Itself for easy chaining
     */
        public T isTranslate(){ return attr(Attr.TRANSLATE, "yes"); }

    // htmx
    /**
     * Sets the {@code hx} attribute.
     *
     * @return Itself for easy chaining
     */
        public T isHx() { return attr(Attr.HX); }
    /**
     * Sets the {@code hxget} attribute.
     *
     * @param url The target URL
     * @return Itself for easy chaining
     */
        public T withHxGet(final String url) { return attr(Attr.HX_GET, url); }
    /**
     * Sets the {@code hxpost} attribute.
     *
     * @param url The target URL
     * @return Itself for easy chaining
     */
        public T withHxPost(final String url) { return attr(Attr.HX_POST, url); }
    /**
     * Sets the {@code hxput} attribute.
     *
     * @param url The target URL
     * @return Itself for easy chaining
     */
        public T withHxPut(final String url) { return attr(Attr.HX_PUT, url); }
    /**
     * Sets the {@code hxdelete} attribute.
     *
     * @param url The target URL
     * @return Itself for easy chaining
     */
        public T withHxDelete(final String url) { return attr(Attr.HX_DELETE, url); }
    /**
     * Sets the {@code hxpatch} attribute.
     *
     * @param url The target URL
     * @return Itself for easy chaining
     */
        public T withHxPatch(final String url) { return attr(Attr.HX_PATCH, url); }
    /**
     * Sets the {@code hxtrigger} attribute.
     *
     * @param trigger The trigger value
     * @return Itself for easy chaining
     */
        public T withHxTrigger(final String trigger) { return attr(Attr.HX_TRIGGER, trigger); }
    /**
     * Sets the {@code hxtarget} attribute.
     *
     * @param target The target value
     * @return Itself for easy chaining
     */
        public T withHxTarget(final String target) { return attr(Attr.HX_TARGET, target); }
    /**
     * Sets the {@code hxswap} attribute.
     *
     * @param swap The swap value
     * @return Itself for easy chaining
     */
        public T withHxSwap(final String swap) { return attr(Attr.HX_SWAP, swap); }
    /**
     * Sets the {@code hxswapoob} attribute.
     *
     * @param swapOob The swapOob value
     * @return Itself for easy chaining
     */
        public T withHxSwapOob(final String swapOob) { return attr(Attr.HX_SWAP_OOB, swapOob); }
    /**
     * Sets the {@code hxselect} attribute.
     *
     * @param select The select value
     * @return Itself for easy chaining
     */
        public T withHxSelect(final String select) { return attr(Attr.HX_SELECT, select); }
    /**
     * Sets the {@code hxpushurl} attribute.
     *
     * @param pushUrl The pushUrl value
     * @return Itself for easy chaining
     */
        public T withHxPushUrl(final String pushUrl) { return attr(Attr.HX_PUSH_URL, pushUrl); }
    /**
     * Sets the {@code hxvals} attribute.
     *
     * @param vals The vals value
     * @return Itself for easy chaining
     */
        public T withHxVals(final String vals) { return attr(Attr.HX_VALS, vals); }
    /**
     * Sets the {@code hxconfirm} attribute.
     *
     * @param confirm The confirm value
     * @return Itself for easy chaining
     */
        public T withHxConfirm(final String confirm) { return attr(Attr.HX_CONFIRM, confirm); }
    /**
     * Sets the {@code hxboost} attribute.
     *
     * @param boost The boost value
     * @return Itself for easy chaining
     */
        public T withHxBoost(final String boost) { return attr(Attr.HX_BOOST, boost); }
    /**
     * Sets the {@code hxon} attribute.
     *
     * @param event The event value
     * @param value The attribute value
     * @return Itself for easy chaining
     */
        public T withHxOn(final String event, final String value) { return attr(Attr.HX_ON + ":" + event, value); }
    /**
     * Sets the {@code hxparams} attribute.
     *
     * @param params The params value
     * @return Itself for easy chaining
     */
        public T withHxParams(final String params) { return attr(Attr.HX_PARAMS, params); }
    /**
     * Sets the {@code hxheaders} attribute.
     *
     * @param headers The headers value
     * @return Itself for easy chaining
     */
        public T withHxHeaders(final String headers) { return attr(Attr.HX_HEADERS, headers); }
    /**
     * Sets the {@code hxsync} attribute.
     *
     * @param sync The sync value
     * @return Itself for easy chaining
     */
        public T withHxSync(final String sync) { return attr(Attr.HX_SYNC, sync); }
    /**
     * Sets the {@code hxext} attribute.
     *
     * @param ext The ext value
     * @return Itself for easy chaining
     */
        public T withHxExt(final String ext) { return attr(Attr.HX_EXT, ext); }
    /**
     * Sets the {@code hxdisinherit} attribute.
     *
     * @param disinherit The disinherit value
     * @return Itself for easy chaining
     */
        public T withHxDisinherit(final String disinherit) { return attr(Attr.HX_DISINHERIT, disinherit); }
    /**
     * Sets the {@code hxindicator} attribute.
     *
     * @param indicator The indicator value
     * @return Itself for easy chaining
     */
        public T withHxIndicator(final String indicator) { return attr(Attr.HX_INDICATOR, indicator); }
    /**
     * Sets the {@code hxhistory} attribute.
     *
     * @param history The history value
     * @return Itself for easy chaining
     */
        public T withHxHistory(final String history) { return attr(Attr.HX_HISTORY, history); }
    /**
     * Sets the {@code hxhistoryelt} attribute.
     *
     * @param historyElt The historyElt value
     * @return Itself for easy chaining
     */
        public T withHxHistoryElt(final String historyElt) { return attr(Attr.HX_HISTORY_ELT, historyElt); }
    /**
     * Sets the {@code hxinclude} attribute.
     *
     * @param include The include value
     * @return Itself for easy chaining
     */
        public T withHxInclude(final String include) { return attr(Attr.HX_INCLUDE, include); }
    /**
     * Sets the {@code hxprompt} attribute.
     *
     * @param prompt The prompt value
     * @return Itself for easy chaining
     */
        public T withHxPrompt(final String prompt) { return attr(Attr.HX_PROMPT, prompt); }
    /**
     * Sets the {@code hxptag} attribute.
     *
     * @param ptag The ptag value
     * @return Itself for easy chaining
     */
        public T withHxPtag(final String ptag) { return attr(Attr.HX_PTAG, ptag); }
    /**
     * Sets the {@code hxdownload} attribute.
     *
     * @param download The download value
     * @return Itself for easy chaining
     */
        public T withHxDownload(final String download) { return attr(Attr.HX_DOWNLOAD, download); }
    /**
     * Sets the {@code hxhead} attribute.
     *
     * @param head The head value
     * @return Itself for easy chaining
     */
        public T withHxHead(final String head) { return attr(Attr.HX_HEAD, head); }
    /**
     * Sets the {@code hxtargets} attribute.
     *
     * @param targets The targets value
     * @return Itself for easy chaining
     */
        public T withHxTargets(final String targets) { return attr(Attr.HX_TARGETS, targets); }
    /**
     * Sets the {@code hxupsert} attribute.
     *
     * @param upsert The upsert value
     * @return Itself for easy chaining
     */
        public T withHxUpsert(final String upsert) { return attr(Attr.HX_UPSERT, upsert); }
    /**
     * Sets the {@code hxstatus} attribute.
     *
     * @param status The status value
     * @return Itself for easy chaining
     */
        public T withHxStatus(final String status) { return attr(Attr.HX_STATUS, status); }
    /**
     * Sets the {@code hxignore} attribute.
     *
     * @param ignore The ignore value
     * @return Itself for easy chaining
     */
        public T withHxIgnore(final String ignore) { return attr(Attr.HX_IGNORE, ignore); }
    /**
     * Sets the {@code hxhistorycache} attribute.
     *
     * @param historyCache The historyCache value
     * @return Itself for easy chaining
     */
        public T withHxHistoryCache(final String historyCache) { return attr(Attr.HX_HISTORY_CACHE, historyCache); }
    /**
     * Sets the {@code hxcsp} attribute.
     *
     * @param csp The csp value
     * @return Itself for easy chaining
     */
        public T withHxCsp(final String csp) { return attr(Attr.HX_CSP, csp); }
    /**
     * Sets the {@code hxalpinecompat} attribute.
     *
     * @param alpineCompat The alpineCompat value
     * @return Itself for easy chaining
     */
        public T withHxAlpineCompat(final String alpineCompat) { return attr(Attr.HX_ALPINE_COMPAT, alpineCompat); }
    /**
     * Sets the {@code hxmultipart} attribute.
     *
     * @param multipart The multipart value
     * @return Itself for easy chaining
     */
        public T withHxMultipart(final String multipart) { return attr(Attr.HX_MULTIPART, multipart); }
    /**
     * Sets the {@code hxlive} attribute.
     *
     * @param live The live value
     * @return Itself for easy chaining
     */
        public T withHxLive(final String live) { return attr(Attr.HX_LIVE, live); }
    /**
     * Sets the {@code hxsse} attribute.
     *
     * @param sse The sse value
     * @return Itself for easy chaining
     */
        public T withHxSse(final String sse) { return attr(Attr.HX_SSE, sse); }
    /**
     * Sets the {@code hxws} attribute.
     *
     * @param ws The ws value
     * @return Itself for easy chaining
     */
        public T withHxWs(final String ws) { return attr(Attr.HX_WS, ws); }
    /**
     * Sets the {@code hxvalidate} attribute.
     *
     * @param validate The validate value
     * @return Itself for easy chaining
     */
        public T withHxValidate(final String validate) { return attr(Attr.HX_VALIDATE, validate); }

    // pico.css & aria
    /**
     * Sets the {@code datatheme} attribute.
     *
     * @param theme The theme value
     * @return Itself for easy chaining
     */
        public T withDataTheme(final String theme) { return attr(Attr.DATA_THEME, theme); }
    /**
     * Sets the {@code datatooltip} attribute.
     *
     * @param tooltip The tooltip value
     * @return Itself for easy chaining
     */
        public T withDataTooltip(final String tooltip) { return attr(Attr.DATA_TOOLTIP, tooltip); }
    /**
     * Sets the {@code dataplacement} attribute.
     *
     * @param placement The placement value
     * @return Itself for easy chaining
     */
        public T withDataPlacement(final String placement) { return attr(Attr.DATA_PLACEMENT, placement); }
    /**
     * Sets the {@code ariabusy} attribute.
     *
     * @return Itself for easy chaining
     */
        public T isAriaBusy() { return attr(Attr.ARIA_BUSY, "true"); }
    /**
     * Sets the {@code ariabusy} attribute.
     *
     * @param busy The busy value
     * @return Itself for easy chaining
     */
        public T withAriaBusy(final boolean busy) { return attr(Attr.ARIA_BUSY, String.valueOf(busy)); }
    /**
     * Sets the {@code ariainvalid} attribute.
     *
     * @param invalid The invalid value
     * @return Itself for easy chaining
     */
        public T withAriaInvalid(final boolean invalid) { return attr(Attr.ARIA_INVALID, String.valueOf(invalid)); }
    /**
     * Sets the {@code ariainvalid} attribute.
     *
     * @param invalid The invalid value
     * @return Itself for easy chaining
     */
        public T withAriaInvalid(final String invalid) { return attr(Attr.ARIA_INVALID, invalid); }
    /**
     * Sets the {@code ariainvalid} attribute.
     *
     * @return Itself for easy chaining
     */
        public T isAriaInvalid() { return attr(Attr.ARIA_INVALID, "true"); }
    /**
     * Sets the {@code role} attribute.
     *
     * @param role The role value
     * @return Itself for easy chaining
     */
        public T withRole(final String role) { return attr(Attr.ROLE, role); }
    /**
     * Sets the {@code role} attribute to button.
     *
     * @return Itself for easy chaining
     */
        public T asButton() { return attr(Attr.ROLE, Attr.ROLE_BUTTON); }
    /**
     * Sets the {@code role} attribute to group.
     *
     * @return Itself for easy chaining
     */
        public T asGroup() { return attr(Attr.ROLE, Attr.ROLE_GROUP); }
    /**
     * Sets the {@code role} attribute to search.
     *
     * @return Itself for easy chaining
     */
        public T asSearch() { return attr(Attr.ROLE, Attr.ROLE_SEARCH); }

    // ----- start of withCond$ATTR variants -----
    /**
     * Conditionally sets the {@code accesskey} attribute.
     *
     * @param condition The condition to evaluate
     * @param accesskey The accesskey value
     * @return Itself for easy chaining
     */
        public T withCondAccessKey(final boolean condition, final String accesskey){ return condAttr(condition, Attr.ACCESSKEY, accesskey); }

    /**
     * Conditionally sets the {@code class} attribute.
     *
     * @param condition The condition to evaluate
     * @param className The className value
     * @return Itself for easy chaining
     */
        public T withCondClass(final boolean condition, final String className) { return condAttr(condition, Attr.CLASS, className); }

    /**
     * Conditionally sets the {@code contenteditable} attribute.
     *
     * @param condition The condition to evaluate
     * @return Itself for easy chaining
     */
        public T withCondContenteditable(final boolean condition){ return attr(Attr.CONTENTEDITABLE, (condition)?"true":"false");}

    /**
     * Conditionally sets the {@code data} attribute.
     *
     * @param condition The condition to evaluate
     * @param dataAttr The dataAttr value
     * @param value The attribute value
     * @return Itself for easy chaining
     */
        public T withCondData(final boolean condition, final String dataAttr, final String value) { return condAttr(condition, Attr.DATA + "-" + dataAttr, value); }

    /**
     * Conditionally sets the {@code dir} attribute.
     *
     * @param condition The condition to evaluate
     * @param dir The dir value
     * @return Itself for easy chaining
     */
        public T withCondDir(final boolean condition, final String dir) { return condAttr(condition, Attr.DIR, dir); }

    /**
     * Conditionally sets the {@code draggable} attribute.
     *
     * @param condition The condition to evaluate
     * @return Itself for easy chaining
     */
        public T withCondDraggable(final boolean condition){ return attr(Attr.DRAGGABLE, (condition)?"true":"false"); }

    /**
     * Conditionally sets the {@code hidden} attribute.
     *
     * @param condition The condition to evaluate
     * @return Itself for easy chaining
     */
        public T withCondHidden(final boolean condition) { return condAttr(condition, Attr.HIDDEN, null); }

    /**
     * Conditionally sets the {@code id} attribute.
     *
     * @param condition The condition to evaluate
     * @param id The id value
     * @return Itself for easy chaining
     */
        public T withCondId(final boolean condition, final String id) { return condAttr(condition, Attr.ID, id); }

    /**
     * Conditionally sets the {@code is} attribute.
     *
     * @param condition The condition to evaluate
     * @param element The element value
     * @return Itself for easy chaining
     */
        public T withCondIs(final boolean condition, final String element){ return condAttr(condition, Attr.IS, element); }

    /**
     * Conditionally sets the {@code lang} attribute.
     *
     * @param condition The condition to evaluate
     * @param lang The lang value
     * @return Itself for easy chaining
     */
        public T withCondLang(final boolean condition, final String lang) { return condAttr(condition, Attr.LANG, lang); }

    /**
     * Conditionally sets the {@code slot} attribute.
     *
     * @param condition The condition to evaluate
     * @param name The name value
     * @return Itself for easy chaining
     */
        public T withCondSlot(final boolean condition, final String name){ return condAttr(condition, Attr.SLOT, name); }

    /**
     * Conditionally sets the {@code spellcheck} attribute.
     *
     * @param condition The condition to evaluate
     * @return Itself for easy chaining
     */
        public T withCondSpellcheck(final boolean condition){ return attr(Attr.SPELLCHECK, (condition)?"true":"false"); }

    /**
     * Conditionally sets the {@code style} attribute.
     *
     * @param condition The condition to evaluate
     * @param style The style value
     * @return Itself for easy chaining
     */
        public T withCondStyle(final boolean condition, final String style) { return condAttr(condition, Attr.STYLE, style); }

    /**
     * Conditionally sets the {@code tabindex} attribute.
     *
     * @param condition The condition to evaluate
     * @param index The index value
     * @return Itself for easy chaining
     */
        public T withCondTabindex(final boolean condition, final int index){ return condAttr(condition, Attr.TABINDEX, index+""); }

    /**
     * Conditionally sets the {@code title} attribute.
     *
     * @param condition The condition to evaluate
     * @param title The title value
     * @return Itself for easy chaining
     */
        public T withCondTitle(final boolean condition, final String title) { return condAttr(condition, Attr.TITLE, title); }

    /**
     * Conditionally sets the {@code translate} attribute.
     *
     * @param condition The condition to evaluate
     * @return Itself for easy chaining
     */
        public T withCondTranslate(final boolean condition){ return attr(Attr.TRANSLATE, (condition)?"yes":"no"); }

    /**
     * Conditionally sets the {@code hx} attribute.
     *
     * @param condition The condition to evaluate
     * @return Itself for easy chaining
     */
        public T withCondHx(final boolean condition) { return condAttr(condition, Attr.HX, null); }
    /**
     * Conditionally sets the {@code hxget} attribute.
     *
     * @param condition The condition to evaluate
     * @param url The target URL
     * @return Itself for easy chaining
     */
        public T withCondHxGet(final boolean condition, final String url) { return condAttr(condition, Attr.HX_GET, url); }
    /**
     * Conditionally sets the {@code hxpost} attribute.
     *
     * @param condition The condition to evaluate
     * @param url The target URL
     * @return Itself for easy chaining
     */
        public T withCondHxPost(final boolean condition, final String url) { return condAttr(condition, Attr.HX_POST, url); }
    /**
     * Conditionally sets the {@code hxput} attribute.
     *
     * @param condition The condition to evaluate
     * @param url The target URL
     * @return Itself for easy chaining
     */
        public T withCondHxPut(final boolean condition, final String url) { return condAttr(condition, Attr.HX_PUT, url); }
    /**
     * Conditionally sets the {@code hxdelete} attribute.
     *
     * @param condition The condition to evaluate
     * @param url The target URL
     * @return Itself for easy chaining
     */
        public T withCondHxDelete(final boolean condition, final String url) { return condAttr(condition, Attr.HX_DELETE, url); }
    /**
     * Conditionally sets the {@code hxpatch} attribute.
     *
     * @param condition The condition to evaluate
     * @param url The target URL
     * @return Itself for easy chaining
     */
        public T withCondHxPatch(final boolean condition, final String url) { return condAttr(condition, Attr.HX_PATCH, url); }
    /**
     * Conditionally sets the {@code hxtrigger} attribute.
     *
     * @param condition The condition to evaluate
     * @param trigger The trigger value
     * @return Itself for easy chaining
     */
        public T withCondHxTrigger(final boolean condition, final String trigger) { return condAttr(condition, Attr.HX_TRIGGER, trigger); }
    /**
     * Conditionally sets the {@code hxtarget} attribute.
     *
     * @param condition The condition to evaluate
     * @param target The target value
     * @return Itself for easy chaining
     */
        public T withCondHxTarget(final boolean condition, final String target) { return condAttr(condition, Attr.HX_TARGET, target); }
    /**
     * Conditionally sets the {@code hxswap} attribute.
     *
     * @param condition The condition to evaluate
     * @param swap The swap value
     * @return Itself for easy chaining
     */
        public T withCondHxSwap(final boolean condition, final String swap) { return condAttr(condition, Attr.HX_SWAP, swap); }
    /**
     * Conditionally sets the {@code hxswapoob} attribute.
     *
     * @param condition The condition to evaluate
     * @param swapOob The swapOob value
     * @return Itself for easy chaining
     */
        public T withCondHxSwapOob(final boolean condition, final String swapOob) { return condAttr(condition, Attr.HX_SWAP_OOB, swapOob); }
    /**
     * Conditionally sets the {@code hxselect} attribute.
     *
     * @param condition The condition to evaluate
     * @param select The select value
     * @return Itself for easy chaining
     */
        public T withCondHxSelect(final boolean condition, final String select) { return condAttr(condition, Attr.HX_SELECT, select); }
    /**
     * Conditionally sets the {@code hxpushurl} attribute.
     *
     * @param condition The condition to evaluate
     * @param pushUrl The pushUrl value
     * @return Itself for easy chaining
     */
        public T withCondHxPushUrl(final boolean condition, final String pushUrl) { return condAttr(condition, Attr.HX_PUSH_URL, pushUrl); }
    /**
     * Conditionally sets the {@code hxvals} attribute.
     *
     * @param condition The condition to evaluate
     * @param vals The vals value
     * @return Itself for easy chaining
     */
        public T withCondHxVals(final boolean condition, final String vals) { return condAttr(condition, Attr.HX_VALS, vals); }
    /**
     * Conditionally sets the {@code hxconfirm} attribute.
     *
     * @param condition The condition to evaluate
     * @param confirm The confirm value
     * @return Itself for easy chaining
     */
        public T withCondHxConfirm(final boolean condition, final String confirm) { return condAttr(condition, Attr.HX_CONFIRM, confirm); }
    /**
     * Conditionally sets the {@code hxboost} attribute.
     *
     * @param condition The condition to evaluate
     * @param boost The boost value
     * @return Itself for easy chaining
     */
        public T withCondHxBoost(final boolean condition, final String boost) { return condAttr(condition, Attr.HX_BOOST, boost); }
    /**
     * Conditionally sets the {@code hxon} attribute.
     *
     * @param condition The condition to evaluate
     * @param event The event value
     * @param value The attribute value
     * @return Itself for easy chaining
     */
        public T withCondHxOn(final boolean condition, final String event, final String value) { return condAttr(condition, Attr.HX_ON + ":" + event, value); }
    /**
     * Conditionally sets the {@code hxparams} attribute.
     *
     * @param condition The condition to evaluate
     * @param params The params value
     * @return Itself for easy chaining
     */
        public T withCondHxParams(final boolean condition, final String params) { return condAttr(condition, Attr.HX_PARAMS, params); }
    /**
     * Conditionally sets the {@code hxheaders} attribute.
     *
     * @param condition The condition to evaluate
     * @param headers The headers value
     * @return Itself for easy chaining
     */
        public T withCondHxHeaders(final boolean condition, final String headers) { return condAttr(condition, Attr.HX_HEADERS, headers); }
    /**
     * Conditionally sets the {@code hxsync} attribute.
     *
     * @param condition The condition to evaluate
     * @param sync The sync value
     * @return Itself for easy chaining
     */
        public T withCondHxSync(final boolean condition, final String sync) { return condAttr(condition, Attr.HX_SYNC, sync); }
    /**
     * Conditionally sets the {@code hxext} attribute.
     *
     * @param condition The condition to evaluate
     * @param ext The ext value
     * @return Itself for easy chaining
     */
        public T withCondHxExt(final boolean condition, final String ext) { return condAttr(condition, Attr.HX_EXT, ext); }
    /**
     * Conditionally sets the {@code hxdisinherit} attribute.
     *
     * @param condition The condition to evaluate
     * @param disinherit The disinherit value
     * @return Itself for easy chaining
     */
        public T withCondHxDisinherit(final boolean condition, final String disinherit) { return condAttr(condition, Attr.HX_DISINHERIT, disinherit); }
    /**
     * Conditionally sets the {@code hxindicator} attribute.
     *
     * @param condition The condition to evaluate
     * @param indicator The indicator value
     * @return Itself for easy chaining
     */
        public T withCondHxIndicator(final boolean condition, final String indicator) { return condAttr(condition, Attr.HX_INDICATOR, indicator); }
    /**
     * Conditionally sets the {@code hxhistory} attribute.
     *
     * @param condition The condition to evaluate
     * @param history The history value
     * @return Itself for easy chaining
     */
        public T withCondHxHistory(final boolean condition, final String history) { return condAttr(condition, Attr.HX_HISTORY, history); }
    /**
     * Conditionally sets the {@code hxhistoryelt} attribute.
     *
     * @param condition The condition to evaluate
     * @param historyElt The historyElt value
     * @return Itself for easy chaining
     */
        public T withCondHxHistoryElt(final boolean condition, final String historyElt) { return condAttr(condition, Attr.HX_HISTORY_ELT, historyElt); }
    /**
     * Conditionally sets the {@code hxinclude} attribute.
     *
     * @param condition The condition to evaluate
     * @param include The include value
     * @return Itself for easy chaining
     */
        public T withCondHxInclude(final boolean condition, final String include) { return condAttr(condition, Attr.HX_INCLUDE, include); }
    /**
     * Conditionally sets the {@code hxprompt} attribute.
     *
     * @param condition The condition to evaluate
     * @param prompt The prompt value
     * @return Itself for easy chaining
     */
        public T withCondHxPrompt(final boolean condition, final String prompt) { return condAttr(condition, Attr.HX_PROMPT, prompt); }
    /**
     * Conditionally sets the {@code hxptag} attribute.
     *
     * @param condition The condition to evaluate
     * @param ptag The ptag value
     * @return Itself for easy chaining
     */
        public T withCondHxPtag(final boolean condition, final String ptag) { return condAttr(condition, Attr.HX_PTAG, ptag); }
    /**
     * Conditionally sets the {@code hxdownload} attribute.
     *
     * @param condition The condition to evaluate
     * @param download The download value
     * @return Itself for easy chaining
     */
        public T withCondHxDownload(final boolean condition, final String download) { return condAttr(condition, Attr.HX_DOWNLOAD, download); }
    /**
     * Conditionally sets the {@code hxhead} attribute.
     *
     * @param condition The condition to evaluate
     * @param head The head value
     * @return Itself for easy chaining
     */
        public T withCondHxHead(final boolean condition, final String head) { return condAttr(condition, Attr.HX_HEAD, head); }
    /**
     * Conditionally sets the {@code hxtargets} attribute.
     *
     * @param condition The condition to evaluate
     * @param targets The targets value
     * @return Itself for easy chaining
     */
        public T withCondHxTargets(final boolean condition, final String targets) { return condAttr(condition, Attr.HX_TARGETS, targets); }
    /**
     * Conditionally sets the {@code hxupsert} attribute.
     *
     * @param condition The condition to evaluate
     * @param upsert The upsert value
     * @return Itself for easy chaining
     */
        public T withCondHxUpsert(final boolean condition, final String upsert) { return condAttr(condition, Attr.HX_UPSERT, upsert); }
    /**
     * Conditionally sets the {@code hxstatus} attribute.
     *
     * @param condition The condition to evaluate
     * @param status The status value
     * @return Itself for easy chaining
     */
        public T withCondHxStatus(final boolean condition, final String status) { return condAttr(condition, Attr.HX_STATUS, status); }
    /**
     * Conditionally sets the {@code hxignore} attribute.
     *
     * @param condition The condition to evaluate
     * @param ignore The ignore value
     * @return Itself for easy chaining
     */
        public T withCondHxIgnore(final boolean condition, final String ignore) { return condAttr(condition, Attr.HX_IGNORE, ignore); }
    /**
     * Conditionally sets the {@code hxhistorycache} attribute.
     *
     * @param condition The condition to evaluate
     * @param historyCache The historyCache value
     * @return Itself for easy chaining
     */
        public T withCondHxHistoryCache(final boolean condition, final String historyCache) { return condAttr(condition, Attr.HX_HISTORY_CACHE, historyCache); }
    /**
     * Conditionally sets the {@code hxcsp} attribute.
     *
     * @param condition The condition to evaluate
     * @param csp The csp value
     * @return Itself for easy chaining
     */
        public T withCondHxCsp(final boolean condition, final String csp) { return condAttr(condition, Attr.HX_CSP, csp); }
    /**
     * Conditionally sets the {@code hxalpinecompat} attribute.
     *
     * @param condition The condition to evaluate
     * @param alpineCompat The alpineCompat value
     * @return Itself for easy chaining
     */
        public T withCondHxAlpineCompat(final boolean condition, final String alpineCompat) { return condAttr(condition, Attr.HX_ALPINE_COMPAT, alpineCompat); }
    /**
     * Conditionally sets the {@code hxmultipart} attribute.
     *
     * @param condition The condition to evaluate
     * @param multipart The multipart value
     * @return Itself for easy chaining
     */
        public T withCondHxMultipart(final boolean condition, final String multipart) { return condAttr(condition, Attr.HX_MULTIPART, multipart); }
    /**
     * Conditionally sets the {@code hxlive} attribute.
     *
     * @param condition The condition to evaluate
     * @param live The live value
     * @return Itself for easy chaining
     */
        public T withCondHxLive(final boolean condition, final String live) { return condAttr(condition, Attr.HX_LIVE, live); }
    /**
     * Conditionally sets the {@code hxsse} attribute.
     *
     * @param condition The condition to evaluate
     * @param sse The sse value
     * @return Itself for easy chaining
     */
        public T withCondHxSse(final boolean condition, final String sse) { return condAttr(condition, Attr.HX_SSE, sse); }
    /**
     * Conditionally sets the {@code hxws} attribute.
     *
     * @param condition The condition to evaluate
     * @param ws The ws value
     * @return Itself for easy chaining
     */
        public T withCondHxWs(final boolean condition, final String ws) { return condAttr(condition, Attr.HX_WS, ws); }
    /**
     * Conditionally sets the {@code hxvalidate} attribute.
     *
     * @param condition The condition to evaluate
     * @param validate The validate value
     * @return Itself for easy chaining
     */
        public T withCondHxValidate(final boolean condition, final String validate) { return condAttr(condition, Attr.HX_VALIDATE, validate); }

    // pico.css & aria conditional variants
    /**
     * Conditionally sets the {@code datatheme} attribute.
     *
     * @param condition The condition to evaluate
     * @param theme The theme value
     * @return Itself for easy chaining
     */
        public T withCondDataTheme(final boolean condition, final String theme) { return condAttr(condition, Attr.DATA_THEME, theme); }
    /**
     * Conditionally sets the {@code datatooltip} attribute.
     *
     * @param condition The condition to evaluate
     * @param tooltip The tooltip value
     * @return Itself for easy chaining
     */
        public T withCondDataTooltip(final boolean condition, final String tooltip) { return condAttr(condition, Attr.DATA_TOOLTIP, tooltip); }
    /**
     * Conditionally sets the {@code dataplacement} attribute.
     *
     * @param condition The condition to evaluate
     * @param placement The placement value
     * @return Itself for easy chaining
     */
        public T withCondDataPlacement(final boolean condition, final String placement) { return condAttr(condition, Attr.DATA_PLACEMENT, placement); }
    /**
     * Conditionally sets the {@code ariabusy} attribute.
     *
     * @param condition The condition to evaluate
     * @return Itself for easy chaining
     */
        public T withCondAriaBusy(final boolean condition) { return condAttr(condition, Attr.ARIA_BUSY, "true"); }
    /**
     * Conditionally sets the {@code ariabusy} attribute.
     *
     * @param condition The condition to evaluate
     * @param busy The busy value
     * @return Itself for easy chaining
     */
        public T withCondAriaBusy(final boolean condition, final boolean busy) { return condAttr(condition, Attr.ARIA_BUSY, String.valueOf(busy)); }
    /**
     * Conditionally sets the {@code ariainvalid} attribute.
     *
     * @param condition The condition to evaluate
     * @return Itself for easy chaining
     */
        public T withCondAriaInvalid(final boolean condition) { return condAttr(condition, Attr.ARIA_INVALID, "true"); }
    /**
     * Conditionally sets the {@code ariainvalid} attribute.
     *
     * @param condition The condition to evaluate
     * @param invalid The invalid value
     * @return Itself for easy chaining
     */
        public T withCondAriaInvalid(final boolean condition, final boolean invalid) { return condAttr(condition, Attr.ARIA_INVALID, String.valueOf(invalid)); }
    /**
     * Conditionally sets the {@code ariainvalid} attribute.
     *
     * @param condition The condition to evaluate
     * @param invalid The invalid value
     * @return Itself for easy chaining
     */
        public T withCondAriaInvalid(final boolean condition, final String invalid) { return condAttr(condition, Attr.ARIA_INVALID, invalid); }
    /**
     * Conditionally sets the {@code role} attribute.
     *
     * @param condition The condition to evaluate
     * @param role The role value
     * @return Itself for easy chaining
     */
        public T withCondRole(final boolean condition, final String role) { return condAttr(condition, Attr.ROLE, role); }
    /**
     * Conditionally sets the {@code asbutton} attribute.
     *
     * @param condition The condition to evaluate
     * @return Itself for easy chaining
     */
        public T withCondAsButton(final boolean condition) { return condAttr(condition, Attr.ROLE, Attr.ROLE_BUTTON); }
    /**
     * Conditionally sets the {@code asgroup} attribute.
     *
     * @param condition The condition to evaluate
     * @return Itself for easy chaining
     */
        public T withCondAsGroup(final boolean condition) { return condAttr(condition, Attr.ROLE, Attr.ROLE_GROUP); }
    /**
     * Conditionally sets the {@code assearch} attribute.
     *
     * @param condition The condition to evaluate
     * @return Itself for easy chaining
     */
        public T withCondAsSearch(final boolean condition) { return condAttr(condition, Attr.ROLE, Attr.ROLE_SEARCH); }
}
