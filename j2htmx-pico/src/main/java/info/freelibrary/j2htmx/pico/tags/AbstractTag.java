/*
 * This code was derived from j2html, which is distributed under the Apache License, Version 2.0.
 * See LICENSE-ASL-2.0.txt in the parent project's root directory for more information.
 *
 * This file was modified for j2htmx-pico by Kevin S. Clarke (my.j2htmx-pico@kevinclarke.info).
 *
 * The modifications are licensed under the Mozilla Public License, Version 2.0.
 * See LICENSE-MPL-2.0.txt in the parent project's root directory for more information.
 */

package info.freelibrary.j2htmx.pico.tags;

import static info.freelibrary.j2htmx.pico.attributes.Attr.*;
import static info.freelibrary.util.Constants.*;

import info.freelibrary.j2htmx.pico.attributes.Attribute;
import info.freelibrary.util.warnings.Checkstyle;
import info.freelibrary.util.warnings.PMD;

import java.util.ArrayList;
import java.util.StringJoiner;

/**
 * Base class for all HTML tags, providing fluent builder methods for global, HTMX, and Pico CSS attributes.
 *
 * @param <T> The concrete tag type implementing this class
 */
@SuppressWarnings({PMD.CYCLOMATIC_COMPLEXITY, PMD.EXCESSIVE_PUBLIC_COUNT, PMD.TOO_MANY_METHODS, PMD.GOD_CLASS,
  Checkstyle.MULTIPLE_STRING_LITERALS, "MultipleStringLiterals"})
public abstract class AbstractTag<T extends AbstractTag<T>> extends AbstractDomContent implements Instance<T> {

    /** The HTML tag name. */
    private final String myTagName;

    /** The list of attributes belonging to this tag. */
    private final ArrayList<Attribute> myAttributes;

    /**
     * Constructs a AbstractTag with the specified tag name.
     *
     * @param aTagName The HTML tag name
     */
    protected AbstractTag(final String aTagName) {
        myTagName = aTagName;
        myAttributes = new ArrayList<>();
    }

    /**
     * Gets the HTML tag name.
     *
     * @return The tag name
     */
    public String getTagName() {
        return this.myTagName;
    }

    /**
     * Checks if this tag has a valid tag name.
     *
     * @return True if tag name is non-null and non-empty, false otherwise
     */
    protected boolean hasTagName() {
        return myTagName != null && !myTagName.isEmpty();
    }

    /**
     * Gets the list of attributes associated with this tag.
     *
     * @return The attribute list
     */
    protected ArrayList<Attribute> getAttributes() {
        return myAttributes;
    }

    /**
     * Stores an attribute on an element, adding or updating it.
     *
     * @param aName The attribute name
     * @param aValue The attribute value, or null for a boolean attribute
     * @return True if the attribute list was modified
     */
    boolean storeAttribute(final String aName, final String aValue) {
        for (final Attribute attribute : myAttributes) {
            if (attribute.getName().equals(aName)) {
                attribute.setValue(aValue);
                return true;
            }
        }

        return myAttributes.add(aValue == null ? new Attribute(aName) : new Attribute(aName, aValue));
    }

    /**
     * Stores an attribute on an element, replacing any existing attribute with the same name.
     *
     * @param anAttribute The attribute to store
     * @return True if the attribute list was modified
     */
    boolean storeAttribute(final Attribute anAttribute) {
        final String name = anAttribute.getName();

        if (name != null) {
            myAttributes.removeIf(existing -> name.equals(existing.getName()));
        }

        return myAttributes.add(anAttribute);
    }

    /**
     * Sets a custom attribute by taking the string value of the supplied object.
     *
     * @param anAttribute the attribute name
     * @param aValue the attribute value
     * @return itself for easy chaining
     */
    public T withAttr(final String anAttribute, final Object aValue) {
        storeAttribute(anAttribute, aValue == null ? null : String.valueOf(aValue));
        return self();
    }

    /**
     * Adds the specified attribute. If the AbstractTag previously contained an attribute with the same name, the old
     * attribute is replaced by the specified attribute.
     *
     * @param anAttribute the attribute
     * @return itself for easy chaining
     */
    public T withAttr(final Attribute anAttribute) {
        storeAttribute(anAttribute);
        return self();
    }

    /**
     * Sets a custom attribute without value.
     *
     * @param anAttribute the attribute name
     * @return itself for easy chaining
     * @see AbstractTag#withAttr(String, Object)
     */
    public T withAttr(final String anAttribute) {
        return withAttr(anAttribute, null);
    }

    /**
     * Call withAttr-method based on condition {@link #withAttr(String, Object)}.
     *
     * @param aCondition the condition
     * @param anAttribute the attribute name
     * @param aValue the attribute value
     * @return itself for easy chaining
     */
    public T withCondAttr(final boolean aCondition, final String anAttribute, final String aValue) {
        return aCondition ? withAttr(anAttribute, aValue) : self();
    }

    @Override
    public boolean equals(final Object anObject) {
        if (this == anObject) {
            return true;
        }

        if (!(anObject instanceof AbstractTag<?> other)) {
            return false;
        }

        return render().equals(other.render());
    }

    @Override
    public int hashCode() {
        return render().hashCode();
    }

    /**
     * Convenience methods that call withAttr with predefined attributes.
     *
     * @param aClasses The CSS classes to add
     * @return itself for easy chaining
     */
    public T withClasses(final String... aClasses) {
        final StringJoiner joiner = new StringJoiner(SPACE);

        if (aClasses != null) {
            for (final String className : aClasses) {
                if (className != null && !className.isBlank()) {
                    joiner.add(className.trim());
                }
            }
        }

        return withAttr(CLASS, joiner.toString());
    }

    /**
     * Convenience method that calls withAttr with predefined attributes.
     *
     * @param aStyles The CSS styles to add
     * @return Itself for easy chaining
     */
    public T withStyles(final String... aStyles) {
        final StringJoiner joiner = new StringJoiner(SPACE);

        if (aStyles != null) {
            for (final String style : aStyles) {
                if (style != null && !style.isBlank()) {
                    final String trimmed = style.trim();
                    joiner.add(trimmed.endsWith(SEMICOLON) ? trimmed : trimmed + SEMICOLON);
                }
            }
        }

        return withAttr(STYLE, joiner.toString());
    }

    /*
    AbstractTag.java contains all Global Attributes, Attributes which are
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
     * @param anAccessKey The accesskey value
     * @return Itself for easy chaining
     */
    public T withAccessKey(final String anAccessKey) {
        return withAttr(ACCESSKEY, anAccessKey);
    }

    /**
     * Sets the {@code class} attribute.
     *
     * @param aClassName The className value
     * @return Itself for easy chaining
     */
    public T withClass(final String aClassName) {
        return withAttr(CLASS, aClassName);
    }

    /**
     * Sets the {@code contenteditable} attribute.
     *
     * @return Itself for easy chaining
     */
    public T isContentEditable() {
        return withAttr(CONTENTEDITABLE, Boolean.TRUE.toString());
    }

    /**
     * Sets the {@code data} attribute.
     *
     * @param aDataAttr The dataAttr value
     * @param aValue The attribute value
     * @return Itself for easy chaining
     */
    public T withData(final String aDataAttr, final String aValue) {
        return withAttr(DATA + "-" + aDataAttr, aValue);
    }

    /**
     * Sets the {@code dir} attribute.
     *
     * @param aDir The dir value
     * @return Itself for easy chaining
     */
    public T withDir(final String aDir) {
        return withAttr(DIR, aDir);
    }

    /**
     * Sets the {@code draggable} attribute.
     *
     * @return Itself for easy chaining
     */
    public T isDraggable() {
        return withAttr(DRAGGABLE, Boolean.TRUE.toString());
    }

    /**
     * Sets the {@code hidden} attribute.
     *
     * @return Itself for easy chaining
     */
    public T isHidden() {
        return withAttr(HIDDEN, null);
    }

    /**
     * Sets the {@code id} attribute.
     *
     * @param anId The id value
     * @return Itself for easy chaining
     */
    public T withId(final String anId) {
        return withAttr(ID, anId);
    }

    /**
     * Sets the {@code is} attribute.
     *
     * @param anElement The element value
     * @return Itself for easy chaining
     */
    public T withIs(final String anElement) {
        return withAttr(IS, anElement);
    }

    /**
     * Sets the {@code lang} attribute.
     *
     * @param aLang The lang value
     * @return Itself for easy chaining
     */
    public T withLang(final String aLang) {
        return withAttr(LANG, aLang);
    }

    /**
     * Sets the {@code slot} attribute.
     *
     * @param aName The name value
     * @return Itself for easy chaining
     */
    public T withSlot(final String aName) {
        return withAttr(SLOT, aName);
    }

    /**
     * Sets the {@code spellcheck} attribute.
     *
     * @return Itself for easy chaining
     */
    public T hasSpellCheck() {
        return withAttr(SPELLCHECK, Boolean.TRUE.toString());
    }

    /**
     * Sets the {@code style} attribute.
     *
     * @param aStyle The style value
     * @return Itself for easy chaining
     */
    public T withStyle(final String aStyle) {
        return withAttr(STYLE, aStyle);
    }

    /**
     * Sets the {@code tabindex} attribute.
     *
     * @param anIndex The index value
     * @return Itself for easy chaining
     */
    public T withTabIndex(final int anIndex) {
        return withAttr(TABINDEX, anIndex);
    }

    /**
     * Sets the {@code title} attribute.
     *
     * @param aTitle The title value
     * @return Itself for easy chaining
     */
    public T withTitle(final String aTitle) {
        return withAttr(TITLE, aTitle);
    }

    /**
     * Sets the {@code translate} attribute.
     *
     * @return Itself for easy chaining
     */
    public T withTranslate() {
        return withAttr(TRANSLATE, "yes");
    }

    // htmx

    /**
     * Sets the {@code hx} attribute.
     *
     * @return Itself for easy chaining
     */
    public T hasHx() {
        return withAttr(HX);
    }

    /**
     * Sets the {@code hxget} attribute.
     *
     * @param aUrl The target URL
     * @return Itself for easy chaining
     */
    public T withHxGet(final String aUrl) {
        return withAttr(HX_GET, aUrl);
    }

    /**
     * Sets the {@code hxpost} attribute.
     *
     * @param aUrl The target URL
     * @return Itself for easy chaining
     */
    public T withHxPost(final String aUrl) {
        return withAttr(HX_POST, aUrl);
    }

    /**
     * Sets the {@code hxput} attribute.
     *
     * @param aUrl The target URL
     * @return Itself for easy chaining
     */
    public T withHxPut(final String aUrl) {
        return withAttr(HX_PUT, aUrl);
    }

    /**
     * Sets the {@code hxdelete} attribute.
     *
     * @param aUrl The target URL
     * @return Itself for easy chaining
     */
    public T withHxDelete(final String aUrl) {
        return withAttr(HX_DELETE, aUrl);
    }

    /**
     * Sets the {@code hxpatch} attribute.
     *
     * @param aUrl The target URL
     * @return Itself for easy chaining
     */
    public T withHxPatch(final String aUrl) {
        return withAttr(HX_PATCH, aUrl);
    }

    /**
     * Sets the {@code hxtrigger} attribute.
     *
     * @param aTrigger The trigger value
     * @return Itself for easy chaining
     */
    public T withHxTrigger(final String aTrigger) {
        return withAttr(HX_TRIGGER, aTrigger);
    }

    /**
     * Sets the {@code hxtarget} attribute.
     *
     * @param aTarget The target value
     * @return Itself for easy chaining
     */
    public T withHxTarget(final String aTarget) {
        return withAttr(HX_TARGET, aTarget);
    }

    /**
     * Sets the {@code hxswap} attribute.
     *
     * @param aSwap The swap value
     * @return Itself for easy chaining
     */
    public T withHxSwap(final String aSwap) {
        return withAttr(HX_SWAP, aSwap);
    }

    /**
     * Sets the {@code hxswapoob} attribute.
     *
     * @param aSwapOob The swapOob value
     * @return Itself for easy chaining
     */
    public T withHxSwapOob(final String aSwapOob) {
        return withAttr(HX_SWAP_OOB, aSwapOob);
    }

    /**
     * Sets the {@code hxselect} attribute.
     *
     * @param aSelect The select value
     * @return Itself for easy chaining
     */
    public T withHxSelect(final String aSelect) {
        return withAttr(HX_SELECT, aSelect);
    }

    /**
     * Sets the {@code hxpushurl} attribute.
     *
     * @param aPushUrl The pushUrl value
     * @return Itself for easy chaining
     */
    public T withHxPushUrl(final String aPushUrl) {
        return withAttr(HX_PUSH_URL, aPushUrl);
    }

    /**
     * Sets the {@code hxvals} attribute.
     *
     * @param aVals The vals value
     * @return Itself for easy chaining
     */
    public T withHxVals(final String aVals) {
        return withAttr(HX_VALS, aVals);
    }

    /**
     * Sets the {@code hxconfirm} attribute.
     *
     * @param aConfirm The confirm value
     * @return Itself for easy chaining
     */
    public T withHxConfirm(final String aConfirm) {
        return withAttr(HX_CONFIRM, aConfirm);
    }

    /**
     * Sets the {@code hxboost} attribute.
     *
     * @param aBoost The boost value
     * @return Itself for easy chaining
     */
    public T withHxBoost(final String aBoost) {
        return withAttr(HX_BOOST, aBoost);
    }

    /**
     * Sets the {@code hxon} attribute.
     *
     * @param anEvent The event value
     * @param aValue The attribute value
     * @return Itself for easy chaining
     */
    public T withHxOn(final String anEvent, final String aValue) {
        return withAttr(HX_ON + ":" + anEvent, aValue);
    }

    /**
     * Sets the {@code hxparams} attribute.
     *
     * @param aParams The params value
     * @return Itself for easy chaining
     */
    public T withHxParams(final String aParams) {
        return withAttr(HX_PARAMS, aParams);
    }

    /**
     * Sets the {@code hxheaders} attribute.
     *
     * @param aHeaders The headers value
     * @return Itself for easy chaining
     */
    public T withHxHeaders(final String aHeaders) {
        return withAttr(HX_HEADERS, aHeaders);
    }

    /**
     * Sets the {@code hxsync} attribute.
     *
     * @param aSync The sync value
     * @return Itself for easy chaining
     */
    public T withHxSync(final String aSync) {
        return withAttr(HX_SYNC, aSync);
    }

    /**
     * Sets the {@code hxext} attribute.
     *
     * @param anExt The ext value
     * @return Itself for easy chaining
     */
    public T withHxExt(final String anExt) {
        return withAttr(HX_EXT, anExt);
    }

    /**
     * Sets the {@code hxdisinherit} attribute.
     *
     * @param aDisinherit The disinherit value
     * @return Itself for easy chaining
     */
    public T withHxDisinherit(final String aDisinherit) {
        return withAttr(HX_DISINHERIT, aDisinherit);
    }

    /**
     * Sets the {@code hxindicator} attribute.
     *
     * @param anIndicator The indicator value
     * @return Itself for easy chaining
     */
    public T withHxIndicator(final String anIndicator) {
        return withAttr(HX_INDICATOR, anIndicator);
    }

    /**
     * Sets the {@code hxhistory} attribute.
     *
     * @param aHistory The history value
     * @return Itself for easy chaining
     */
    public T withHxHistory(final String aHistory) {
        return withAttr(HX_HISTORY, aHistory);
    }

    /**
     * Sets the {@code hxhistoryelt} attribute.
     *
     * @param aHistoryElt The historyElt value
     * @return Itself for easy chaining
     */
    public T withHxHistoryElt(final String aHistoryElt) {
        return withAttr(HX_HISTORY_ELT, aHistoryElt);
    }

    /**
     * Sets the {@code hxinclude} attribute.
     *
     * @param anInclude The include value
     * @return Itself for easy chaining
     */
    public T withHxInclude(final String anInclude) {
        return withAttr(HX_INCLUDE, anInclude);
    }

    /**
     * Sets the {@code hxprompt} attribute.
     *
     * @param aPrompt The prompt value
     * @return Itself for easy chaining
     */
    public T withHxPrompt(final String aPrompt) {
        return withAttr(HX_PROMPT, aPrompt);
    }

    /**
     * Sets the {@code hxptag} attribute.
     *
     * @param aPtag The ptag value
     * @return Itself for easy chaining
     */
    public T withHxPtag(final String aPtag) {
        return withAttr(HX_PTAG, aPtag);
    }

    /**
     * Sets the {@code hxdownload} attribute.
     *
     * @param aDownload The download value
     * @return Itself for easy chaining
     */
    public T withHxDownload(final String aDownload) {
        return withAttr(HX_DOWNLOAD, aDownload);
    }

    /**
     * Sets the {@code hxhead} attribute.
     *
     * @param aHead The head value
     * @return Itself for easy chaining
     */
    public T withHxHead(final String aHead) {
        return withAttr(HX_HEAD, aHead);
    }

    /**
     * Sets the {@code hxtargets} attribute.
     *
     * @param aTargets The targets value
     * @return Itself for easy chaining
     */
    public T withHxTargets(final String aTargets) {
        return withAttr(HX_TARGETS, aTargets);
    }

    /**
     * Sets the {@code hxupsert} attribute.
     *
     * @param anUpsert The upsert value
     * @return Itself for easy chaining
     */
    public T withHxUpsert(final String anUpsert) {
        return withAttr(HX_UPSERT, anUpsert);
    }

    /**
     * Sets the {@code hxstatus} attribute.
     *
     * @param aStatus The status value
     * @return Itself for easy chaining
     */
    public T withHxStatus(final String aStatus) {
        return withAttr(HX_STATUS, aStatus);
    }

    /**
     * Sets the {@code hxignore} attribute.
     *
     * @param anIgnore The ignore value
     * @return Itself for easy chaining
     */
    public T withHxIgnore(final String anIgnore) {
        return withAttr(HX_IGNORE, anIgnore);
    }

    /**
     * Sets the {@code hxhistorycache} attribute.
     *
     * @param aHistoryCache The historyCache value
     * @return Itself for easy chaining
     */
    public T withHxHistoryCache(final String aHistoryCache) {
        return withAttr(HX_HISTORY_CACHE, aHistoryCache);
    }

    /**
     * Sets the {@code hxcsp} attribute.
     *
     * @param aCsp The csp value
     * @return Itself for easy chaining
     */
    public T withHxCsp(final String aCsp) {
        return withAttr(HX_CSP, aCsp);
    }

    /**
     * Sets the {@code hxalpinecompat} attribute.
     *
     * @param anAlpineCompat The alpineCompat value
     * @return Itself for easy chaining
     */
    public T withHxAlpineCompat(final String anAlpineCompat) {
        return withAttr(HX_ALPINE_COMPAT, anAlpineCompat);
    }

    /**
     * Sets the {@code hxmultipart} attribute.
     *
     * @param aMultipart The multipart value
     * @return Itself for easy chaining
     */
    public T withHxMultipart(final String aMultipart) {
        return withAttr(HX_MULTIPART, aMultipart);
    }

    /**
     * Sets the {@code hxlive} attribute.
     *
     * @param aLive The live value
     * @return Itself for easy chaining
     */
    public T withHxLive(final String aLive) {
        return withAttr(HX_LIVE, aLive);
    }

    /**
     * Sets the {@code hxsse} attribute.
     *
     * @param anSse The sse value
     * @return Itself for easy chaining
     */
    public T withHxSse(final String anSse) {
        return withAttr(HX_SSE, anSse);
    }

    /**
     * Sets the {@code hxws} attribute.
     *
     * @param aWs The ws value
     * @return Itself for easy chaining
     */
    public T withHxWs(final String aWs) {
        return withAttr(HX_WS, aWs);
    }

    /**
     * Sets the {@code hxvalidate} attribute.
     *
     * @param aValidate The validate value
     * @return Itself for easy chaining
     */
    public T withHxValidate(final String aValidate) {
        return withAttr(HX_VALIDATE, aValidate);
    }

    // pico.css & aria

    /**
     * Sets the {@code datatheme} attribute.
     *
     * @param aTheme The theme value
     * @return Itself for easy chaining
     */
    public T withDataTheme(final String aTheme) {
        return withAttr(DATA_THEME, aTheme);
    }

    /**
     * Sets the {@code datatooltip} attribute.
     *
     * @param aTooltip The tooltip value
     * @return Itself for easy chaining
     */
    public T withDataTooltip(final String aTooltip) {
        return withAttr(DATA_TOOLTIP, aTooltip);
    }

    /**
     * Sets the {@code dataplacement} attribute.
     *
     * @param aPlacement The placement value
     * @return Itself for easy chaining
     */
    public T withDataPlacement(final String aPlacement) {
        return withAttr(DATA_PLACEMENT, aPlacement);
    }


    /**
     * Sets the {@code ariabusy} attribute.
     *
     * @param aBusy The busy value
     * @return Itself for easy chaining
     */
    public T withAriaBusy(final boolean aBusy) {
        return withAttr(ARIA_BUSY, String.valueOf(aBusy));
    }


    /**
     * Sets the {@code ariahidden} attribute.
     *
     * @param aHidden The hidden value
     * @return Itself for easy chaining
     */
    public T withAriaHidden(final boolean aHidden) {
        return withAttr(ARIA_HIDDEN, String.valueOf(aHidden));
    }

    /**
     * Sets the {@code ariahidden} attribute.
     *
     * @param aHidden The hidden value
     * @return Itself for easy chaining
     */
    public T withAriaHidden(final String aHidden) {
        return withAttr(ARIA_HIDDEN, aHidden);
    }

    /**
     * Sets the {@code ariainvalid} attribute.
     *
     * @param anInvalid The invalid value
     * @return Itself for easy chaining
     */
    public T withAriaInvalid(final boolean anInvalid) {
        return withAttr(ARIA_INVALID, String.valueOf(anInvalid));
    }

    /**
     * Sets the {@code ariainvalid} attribute.
     *
     * @param anInvalid The invalid value
     * @return Itself for easy chaining
     */
    public T withAriaInvalid(final String anInvalid) {
        return withAttr(ARIA_INVALID, anInvalid);
    }


    /**
     * Sets the {@code role} attribute.
     *
     * @param aRole The role value
     * @return Itself for easy chaining
     */
    public T withRole(final String aRole) {
        return withAttr(ROLE, aRole);
    }

    /**
     * Sets the {@code role} attribute to button.
     *
     * @return Itself for easy chaining
     */
    public T asButton() {
        return withAttr(ROLE, ROLE_BUTTON);
    }

    /**
     * Sets the {@code role} attribute to group.
     *
     * @return Itself for easy chaining
     */
    public T asGroup() {
        return withAttr(ROLE, ROLE_GROUP);
    }

    /**
     * Sets the {@code role} attribute to search.
     *
     * @return Itself for easy chaining
     */
    public T asSearch() {
        return withAttr(ROLE, ROLE_SEARCH);
    }

    // ----- start of withCond$ATTR variants -----

    /**
     * Conditionally sets the {@code accesskey} attribute.
     *
     * @param aCondition The condition to evaluate
     * @param anAccessKey The accesskey value
     * @return Itself for easy chaining
     */
    public T withCondAccessKey(final boolean aCondition, final String anAccessKey) {
        return withCondAttr(aCondition, ACCESSKEY, anAccessKey);
    }

    /**
     * Conditionally sets the {@code class} attribute.
     *
     * @param aCondition The condition to evaluate
     * @param aClassName The className value
     * @return Itself for easy chaining
     */
    public T withCondClass(final boolean aCondition, final String aClassName) {
        return withCondAttr(aCondition, CLASS, aClassName);
    }

    /**
     * Conditionally sets the {@code contenteditable} attribute.
     *
     * @param aCondition The condition to evaluate
     * @return Itself for easy chaining
     */
    public T withCondContentEditable(final boolean aCondition) {
        return withAttr(CONTENTEDITABLE, aCondition ? Boolean.TRUE.toString() : "false");
    }

    /**
     * Conditionally sets the {@code data} attribute.
     *
     * @param aCondition The condition to evaluate
     * @param aDataAttr The dataAttr value
     * @param aValue The attribute value
     * @return Itself for easy chaining
     */
    public T withCondData(final boolean aCondition, final String aDataAttr, final String aValue) {
        return withCondAttr(aCondition, DATA + "-" + aDataAttr, aValue);
    }

    /**
     * Conditionally sets the {@code dir} attribute.
     *
     * @param aCondition The condition to evaluate
     * @param aDir The dir value
     * @return Itself for easy chaining
     */
    public T withCondDir(final boolean aCondition, final String aDir) {
        return withCondAttr(aCondition, DIR, aDir);
    }

    /**
     * Conditionally sets the {@code draggable} attribute.
     *
     * @param aCondition The condition to evaluate
     * @return Itself for easy chaining
     */
    public T withCondDraggable(final boolean aCondition) {
        return withAttr(DRAGGABLE, aCondition ? Boolean.TRUE.toString() : "false");
    }

    /**
     * Conditionally sets the {@code hidden} attribute.
     *
     * @param aCondition The condition to evaluate
     * @return Itself for easy chaining
     */
    public T withCondHidden(final boolean aCondition) {
        return withCondAttr(aCondition, HIDDEN, null);
    }

    /**
     * Conditionally sets the {@code id} attribute.
     *
     * @param aCondition The condition to evaluate
     * @param anId The id value
     * @return Itself for easy chaining
     */
    public T withCondId(final boolean aCondition, final String anId) {
        return withCondAttr(aCondition, ID, anId);
    }

    /**
     * Conditionally sets the {@code is} attribute.
     *
     * @param aCondition The condition to evaluate
     * @param anElement The element value
     * @return Itself for easy chaining
     */
    public T withCondIs(final boolean aCondition, final String anElement) {
        return withCondAttr(aCondition, IS, anElement);
    }

    /**
     * Conditionally sets the {@code lang} attribute.
     *
     * @param aCondition The condition to evaluate
     * @param aLang The lang value
     * @return Itself for easy chaining
     */
    public T withCondLang(final boolean aCondition, final String aLang) {
        return withCondAttr(aCondition, LANG, aLang);
    }

    /**
     * Conditionally sets the {@code slot} attribute.
     *
     * @param aCondition The condition to evaluate
     * @param aName The name value
     * @return Itself for easy chaining
     */
    public T withCondSlot(final boolean aCondition, final String aName) {
        return withCondAttr(aCondition, SLOT, aName);
    }

    /**
     * Conditionally sets the {@code spellcheck} attribute.
     *
     * @param aCondition The condition to evaluate
     * @return Itself for easy chaining
     */
    public T withCondSpellCheck(final boolean aCondition) {
        return withAttr(SPELLCHECK, aCondition ? Boolean.TRUE.toString() : "false");
    }

    /**
     * Conditionally sets the {@code style} attribute.
     *
     * @param aCondition The condition to evaluate
     * @param aStyle The style value
     * @return Itself for easy chaining
     */
    public T withCondStyle(final boolean aCondition, final String aStyle) {
        return withCondAttr(aCondition, STYLE, aStyle);
    }

    /**
     * Conditionally sets the {@code tabindex} attribute.
     *
     * @param aCondition The condition to evaluate
     * @param anIndex The index value
     * @return Itself for easy chaining
     */
    public T withCondTabIndex(final boolean aCondition, final int anIndex) {
        return withCondAttr(aCondition, TABINDEX, anIndex + EMPTY);
    }

    /**
     * Conditionally sets the {@code title} attribute.
     *
     * @param aCondition The condition to evaluate
     * @param aTitle The title value
     * @return Itself for easy chaining
     */
    public T withCondTitle(final boolean aCondition, final String aTitle) {
        return withCondAttr(aCondition, TITLE, aTitle);
    }

    /**
     * Conditionally sets the {@code translate} attribute.
     *
     * @param aCondition The condition to evaluate
     * @return Itself for easy chaining
     */
    public T withCondTranslate(final boolean aCondition) {
        return withAttr(TRANSLATE, aCondition ? "yes" : "no");
    }

    /**
     * Conditionally sets the {@code hx} attribute.
     *
     * @param aCondition The condition to evaluate
     * @return Itself for easy chaining
     */
    public T withCondHx(final boolean aCondition) {
        return withCondAttr(aCondition, HX, null);
    }

    /**
     * Conditionally sets the {@code hxget} attribute.
     *
     * @param aCondition The condition to evaluate
     * @param aUrl The target URL
     * @return Itself for easy chaining
     */
    public T withCondHxGet(final boolean aCondition, final String aUrl) {
        return withCondAttr(aCondition, HX_GET, aUrl);
    }

    /**
     * Conditionally sets the {@code hxpost} attribute.
     *
     * @param aCondition The condition to evaluate
     * @param aUrl The target URL
     * @return Itself for easy chaining
     */
    public T withCondHxPost(final boolean aCondition, final String aUrl) {
        return withCondAttr(aCondition, HX_POST, aUrl);
    }

    /**
     * Conditionally sets the {@code hxput} attribute.
     *
     * @param aCondition The condition to evaluate
     * @param aUrl The target URL
     * @return Itself for easy chaining
     */
    public T withCondHxPut(final boolean aCondition, final String aUrl) {
        return withCondAttr(aCondition, HX_PUT, aUrl);
    }

    /**
     * Conditionally sets the {@code hxdelete} attribute.
     *
     * @param aCondition The condition to evaluate
     * @param aUrl The target URL
     * @return Itself for easy chaining
     */
    public T withCondHxDelete(final boolean aCondition, final String aUrl) {
        return withCondAttr(aCondition, HX_DELETE, aUrl);
    }

    /**
     * Conditionally sets the {@code hxpatch} attribute.
     *
     * @param aCondition The condition to evaluate
     * @param aUrl The target URL
     * @return Itself for easy chaining
     */
    public T withCondHxPatch(final boolean aCondition, final String aUrl) {
        return withCondAttr(aCondition, HX_PATCH, aUrl);
    }

    /**
     * Conditionally sets the {@code hxtrigger} attribute.
     *
     * @param aCondition The condition to evaluate
     * @param aTrigger The trigger value
     * @return Itself for easy chaining
     */
    public T withCondHxTrigger(final boolean aCondition, final String aTrigger) {
        return withCondAttr(aCondition, HX_TRIGGER, aTrigger);
    }

    /**
     * Conditionally sets the {@code hxtarget} attribute.
     *
     * @param aCondition The condition to evaluate
     * @param aTarget The target value
     * @return Itself for easy chaining
     */
    public T withCondHxTarget(final boolean aCondition, final String aTarget) {
        return withCondAttr(aCondition, HX_TARGET, aTarget);
    }

    /**
     * Conditionally sets the {@code hxswap} attribute.
     *
     * @param aCondition The condition to evaluate
     * @param aSwap The swap value
     * @return Itself for easy chaining
     */
    public T withCondHxSwap(final boolean aCondition, final String aSwap) {
        return withCondAttr(aCondition, HX_SWAP, aSwap);
    }

    /**
     * Conditionally sets the {@code hxswapoob} attribute.
     *
     * @param aCondition The condition to evaluate
     * @param aSwapOob The swapOob value
     * @return Itself for easy chaining
     */
    public T withCondHxSwapOob(final boolean aCondition, final String aSwapOob) {
        return withCondAttr(aCondition, HX_SWAP_OOB, aSwapOob);
    }

    /**
     * Conditionally sets the {@code hxselect} attribute.
     *
     * @param aCondition The condition to evaluate
     * @param aSelect The select value
     * @return Itself for easy chaining
     */
    public T withCondHxSelect(final boolean aCondition, final String aSelect) {
        return withCondAttr(aCondition, HX_SELECT, aSelect);
    }

    /**
     * Conditionally sets the {@code hxpushurl} attribute.
     *
     * @param aCondition The condition to evaluate
     * @param aPushUrl The pushUrl value
     * @return Itself for easy chaining
     */
    public T withCondHxPushUrl(final boolean aCondition, final String aPushUrl) {
        return withCondAttr(aCondition, HX_PUSH_URL, aPushUrl);
    }

    /**
     * Conditionally sets the {@code hxvals} attribute.
     *
     * @param aCondition The condition to evaluate
     * @param aVals The vals value
     * @return Itself for easy chaining
     */
    public T withCondHxVals(final boolean aCondition, final String aVals) {
        return withCondAttr(aCondition, HX_VALS, aVals);
    }

    /**
     * Conditionally sets the {@code hxconfirm} attribute.
     *
     * @param aCondition The condition to evaluate
     * @param aConfirm The confirm value
     * @return Itself for easy chaining
     */
    public T withCondHxConfirm(final boolean aCondition, final String aConfirm) {
        return withCondAttr(aCondition, HX_CONFIRM, aConfirm);
    }

    /**
     * Conditionally sets the {@code hxboost} attribute.
     *
     * @param aCondition The condition to evaluate
     * @param aBoost The boost value
     * @return Itself for easy chaining
     */
    public T withCondHxBoost(final boolean aCondition, final String aBoost) {
        return withCondAttr(aCondition, HX_BOOST, aBoost);
    }

    /**
     * Conditionally sets the {@code hxon} attribute.
     *
     * @param aCondition The condition to evaluate
     * @param anEvent The event value
     * @param aValue The attribute value
     * @return Itself for easy chaining
     */
    public T withCondHxOn(final boolean aCondition, final String anEvent, final String aValue) {
        return withCondAttr(aCondition, HX_ON + ":" + anEvent, aValue);
    }

    /**
     * Conditionally sets the {@code hxparams} attribute.
     *
     * @param aCondition The condition to evaluate
     * @param aParams The params value
     * @return Itself for easy chaining
     */
    public T withCondHxParams(final boolean aCondition, final String aParams) {
        return withCondAttr(aCondition, HX_PARAMS, aParams);
    }

    /**
     * Conditionally sets the {@code hxheaders} attribute.
     *
     * @param aCondition The condition to evaluate
     * @param aHeaders The headers value
     * @return Itself for easy chaining
     */
    public T withCondHxHeaders(final boolean aCondition, final String aHeaders) {
        return withCondAttr(aCondition, HX_HEADERS, aHeaders);
    }

    /**
     * Conditionally sets the {@code hxsync} attribute.
     *
     * @param aCondition The condition to evaluate
     * @param aSync The sync value
     * @return Itself for easy chaining
     */
    public T withCondHxSync(final boolean aCondition, final String aSync) {
        return withCondAttr(aCondition, HX_SYNC, aSync);
    }

    /**
     * Conditionally sets the {@code hxext} attribute.
     *
     * @param aCondition The condition to evaluate
     * @param anExt The ext value
     * @return Itself for easy chaining
     */
    public T withCondHxExt(final boolean aCondition, final String anExt) {
        return withCondAttr(aCondition, HX_EXT, anExt);
    }

    /**
     * Conditionally sets the {@code hxdisinherit} attribute.
     *
     * @param aCondition The condition to evaluate
     * @param aDisinherit The disinherit value
     * @return Itself for easy chaining
     */
    public T withCondHxDisinherit(final boolean aCondition, final String aDisinherit) {
        return withCondAttr(aCondition, HX_DISINHERIT, aDisinherit);
    }

    /**
     * Conditionally sets the {@code hxindicator} attribute.
     *
     * @param aCondition The condition to evaluate
     * @param anIndicator The indicator value
     * @return Itself for easy chaining
     */
    public T withCondHxIndicator(final boolean aCondition, final String anIndicator) {
        return withCondAttr(aCondition, HX_INDICATOR, anIndicator);
    }

    /**
     * Conditionally sets the {@code hxhistory} attribute.
     *
     * @param aCondition The condition to evaluate
     * @param aHistory The history value
     * @return Itself for easy chaining
     */
    public T withCondHxHistory(final boolean aCondition, final String aHistory) {
        return withCondAttr(aCondition, HX_HISTORY, aHistory);
    }

    /**
     * Conditionally sets the {@code hxhistoryelt} attribute.
     *
     * @param aCondition The condition to evaluate
     * @param aHistoryElt The historyElt value
     * @return Itself for easy chaining
     */
    public T withCondHxHistoryElt(final boolean aCondition, final String aHistoryElt) {
        return withCondAttr(aCondition, HX_HISTORY_ELT, aHistoryElt);
    }

    /**
     * Conditionally sets the {@code hxinclude} attribute.
     *
     * @param aCondition The condition to evaluate
     * @param anInclude The include value
     * @return Itself for easy chaining
     */
    public T withCondHxInclude(final boolean aCondition, final String anInclude) {
        return withCondAttr(aCondition, HX_INCLUDE, anInclude);
    }

    /**
     * Conditionally sets the {@code hxprompt} attribute.
     *
     * @param aCondition The condition to evaluate
     * @param aPrompt The prompt value
     * @return Itself for easy chaining
     */
    public T withCondHxPrompt(final boolean aCondition, final String aPrompt) {
        return withCondAttr(aCondition, HX_PROMPT, aPrompt);
    }

    /**
     * Conditionally sets the {@code hxptag} attribute.
     *
     * @param aCondition The condition to evaluate
     * @param aPtag The ptag value
     * @return Itself for easy chaining
     */
    public T withCondHxPtag(final boolean aCondition, final String aPtag) {
        return withCondAttr(aCondition, HX_PTAG, aPtag);
    }

    /**
     * Conditionally sets the {@code hxdownload} attribute.
     *
     * @param aCondition The condition to evaluate
     * @param aDownload The download value
     * @return Itself for easy chaining
     */
    public T withCondHxDownload(final boolean aCondition, final String aDownload) {
        return withCondAttr(aCondition, HX_DOWNLOAD, aDownload);
    }

    /**
     * Conditionally sets the {@code hxhead} attribute.
     *
     * @param aCondition The condition to evaluate
     * @param aHead The head value
     * @return Itself for easy chaining
     */
    public T withCondHxHead(final boolean aCondition, final String aHead) {
        return withCondAttr(aCondition, HX_HEAD, aHead);
    }

    /**
     * Conditionally sets the {@code hxtargets} attribute.
     *
     * @param aCondition The condition to evaluate
     * @param aTargets The targets value
     * @return Itself for easy chaining
     */
    public T withCondHxTargets(final boolean aCondition, final String aTargets) {
        return withCondAttr(aCondition, HX_TARGETS, aTargets);
    }

    /**
     * Conditionally sets the {@code hxupsert} attribute.
     *
     * @param aCondition The condition to evaluate
     * @param anUpsert The upsert value
     * @return Itself for easy chaining
     */
    public T withCondHxUpsert(final boolean aCondition, final String anUpsert) {
        return withCondAttr(aCondition, HX_UPSERT, anUpsert);
    }

    /**
     * Conditionally sets the {@code hxstatus} attribute.
     *
     * @param aCondition The condition to evaluate
     * @param aStatus The status value
     * @return Itself for easy chaining
     */
    public T withCondHxStatus(final boolean aCondition, final String aStatus) {
        return withCondAttr(aCondition, HX_STATUS, aStatus);
    }

    /**
     * Conditionally sets the {@code hxignore} attribute.
     *
     * @param aCondition The condition to evaluate
     * @param anIgnore The ignore value
     * @return Itself for easy chaining
     */
    public T withCondHxIgnore(final boolean aCondition, final String anIgnore) {
        return withCondAttr(aCondition, HX_IGNORE, anIgnore);
    }

    /**
     * Conditionally sets the {@code hxhistorycache} attribute.
     *
     * @param aCondition The condition to evaluate
     * @param aHistoryCache The historyCache value
     * @return Itself for easy chaining
     */
    public T withCondHxHistoryCache(final boolean aCondition, final String aHistoryCache) {
        return withCondAttr(aCondition, HX_HISTORY_CACHE, aHistoryCache);
    }

    /**
     * Conditionally sets the {@code hxcsp} attribute.
     *
     * @param aCondition The condition to evaluate
     * @param aCsp The csp value
     * @return Itself for easy chaining
     */
    public T withCondHxCsp(final boolean aCondition, final String aCsp) {
        return withCondAttr(aCondition, HX_CSP, aCsp);
    }

    /**
     * Conditionally sets the {@code hxalpinecompat} attribute.
     *
     * @param aCondition The condition to evaluate
     * @param anAlpineCompat The alpineCompat value
     * @return Itself for easy chaining
     */
    public T withCondHxAlpineCompat(final boolean aCondition, final String anAlpineCompat) {
        return withCondAttr(aCondition, HX_ALPINE_COMPAT, anAlpineCompat);
    }

    /**
     * Conditionally sets the {@code hxmultipart} attribute.
     *
     * @param aCondition The condition to evaluate
     * @param aMultipart The multipart value
     * @return Itself for easy chaining
     */
    public T withCondHxMultipart(final boolean aCondition, final String aMultipart) {
        return withCondAttr(aCondition, HX_MULTIPART, aMultipart);
    }

    /**
     * Conditionally sets the {@code hxlive} attribute.
     *
     * @param aCondition The condition to evaluate
     * @param aLive The live value
     * @return Itself for easy chaining
     */
    public T withCondHxLive(final boolean aCondition, final String aLive) {
        return withCondAttr(aCondition, HX_LIVE, aLive);
    }

    /**
     * Conditionally sets the {@code hxsse} attribute.
     *
     * @param aCondition The condition to evaluate
     * @param anSse The sse value
     * @return Itself for easy chaining
     */
    public T withCondHxSse(final boolean aCondition, final String anSse) {
        return withCondAttr(aCondition, HX_SSE, anSse);
    }

    /**
     * Conditionally sets the {@code hxws} attribute.
     *
     * @param aCondition The condition to evaluate
     * @param aWs The ws value
     * @return Itself for easy chaining
     */
    public T withCondHxWs(final boolean aCondition, final String aWs) {
        return withCondAttr(aCondition, HX_WS, aWs);
    }

    /**
     * Conditionally sets the {@code hxvalidate} attribute.
     *
     * @param aCondition The condition to evaluate
     * @param aValidate The validate value
     * @return Itself for easy chaining
     */
    public T withCondHxValidate(final boolean aCondition, final String aValidate) {
        return withCondAttr(aCondition, HX_VALIDATE, aValidate);
    }

    // pico.css & aria conditional variants

    /**
     * Conditionally sets the {@code datatheme} attribute.
     *
     * @param aCondition The condition to evaluate
     * @param aTheme The theme value
     * @return Itself for easy chaining
     */
    public T withCondDataTheme(final boolean aCondition, final String aTheme) {
        return withCondAttr(aCondition, DATA_THEME, aTheme);
    }

    /**
     * Conditionally sets the {@code datatooltip} attribute.
     *
     * @param aCondition The condition to evaluate
     * @param aTooltip The tooltip value
     * @return Itself for easy chaining
     */
    public T withCondDataTooltip(final boolean aCondition, final String aTooltip) {
        return withCondAttr(aCondition, DATA_TOOLTIP, aTooltip);
    }

    /**
     * Conditionally sets the {@code dataplacement} attribute.
     *
     * @param aCondition The condition to evaluate
     * @param aPlacement The placement value
     * @return Itself for easy chaining
     */
    public T withCondDataPlacement(final boolean aCondition, final String aPlacement) {
        return withCondAttr(aCondition, DATA_PLACEMENT, aPlacement);
    }

    /**
     * Conditionally sets the {@code ariabusy} attribute.
     *
     * @param aCondition The condition to evaluate
     * @return Itself for easy chaining
     */
    public T withCondAriaBusy(final boolean aCondition) {
        return withCondAttr(aCondition, ARIA_BUSY, Boolean.TRUE.toString());
    }

    /**
     * Conditionally sets the {@code ariabusy} attribute.
     *
     * @param aCondition The condition to evaluate
     * @param aBusy The busy value
     * @return Itself for easy chaining
     */
    public T withCondAriaBusy(final boolean aCondition, final boolean aBusy) {
        return withCondAttr(aCondition, ARIA_BUSY, String.valueOf(aBusy));
    }

    /**
     * Conditionally sets the {@code ariahidden} attribute.
     *
     * @param aCondition The condition to evaluate
     * @return Itself for easy chaining
     */
    public T withCondAriaHidden(final boolean aCondition) {
        return withCondAttr(aCondition, ARIA_HIDDEN, Boolean.TRUE.toString());
    }

    /**
     * Conditionally sets the {@code ariahidden} attribute.
     *
     * @param aCondition The condition to evaluate
     * @param aHidden The hidden value
     * @return Itself for easy chaining
     */
    public T withCondAriaHidden(final boolean aCondition, final boolean aHidden) {
        return withCondAttr(aCondition, ARIA_HIDDEN, String.valueOf(aHidden));
    }

    /**
     * Conditionally sets the {@code ariahidden} attribute.
     *
     * @param aCondition The condition to evaluate
     * @param aHidden The hidden value
     * @return Itself for easy chaining
     */
    public T withCondAriaHidden(final boolean aCondition, final String aHidden) {
        return withCondAttr(aCondition, ARIA_HIDDEN, aHidden);
    }

    /**
     * Conditionally sets the {@code ariainvalid} attribute.
     *
     * @param aCondition The condition to evaluate
     * @return Itself for easy chaining
     */
    public T withCondAriaInvalid(final boolean aCondition) {
        return withCondAttr(aCondition, ARIA_INVALID, Boolean.TRUE.toString());
    }

    /**
     * Conditionally sets the {@code ariainvalid} attribute.
     *
     * @param aCondition The condition to evaluate
     * @param anInvalid The invalid value
     * @return Itself for easy chaining
     */
    public T withCondAriaInvalid(final boolean aCondition, final boolean anInvalid) {
        return withCondAttr(aCondition, ARIA_INVALID, String.valueOf(anInvalid));
    }

    /**
     * Conditionally sets the {@code ariainvalid} attribute.
     *
     * @param aCondition The condition to evaluate
     * @param anInvalid The invalid value
     * @return Itself for easy chaining
     */
    public T withCondAriaInvalid(final boolean aCondition, final String anInvalid) {
        return withCondAttr(aCondition, ARIA_INVALID, anInvalid);
    }

    /**
     * Conditionally sets the {@code role} attribute.
     *
     * @param aCondition The condition to evaluate
     * @param aRole The role value
     * @return Itself for easy chaining
     */
    public T withCondRole(final boolean aCondition, final String aRole) {
        return withCondAttr(aCondition, ROLE, aRole);
    }

    /**
     * Conditionally sets the {@code asbutton} attribute.
     *
     * @param aCondition The condition to evaluate
     * @return Itself for easy chaining
     */
    public T withCondAsButton(final boolean aCondition) {
        return withCondAttr(aCondition, ROLE, ROLE_BUTTON);
    }

    /**
     * Conditionally sets the {@code asgroup} attribute.
     *
     * @param aCondition The condition to evaluate
     * @return Itself for easy chaining
     */
    public T withCondAsGroup(final boolean aCondition) {
        return withCondAttr(aCondition, ROLE, ROLE_GROUP);
    }

    /**
     * Conditionally sets the {@code assearch} attribute.
     *
     * @param aCondition The condition to evaluate
     * @return Itself for easy chaining
     */
    public T withCondAsSearch(final boolean aCondition) {
        return withCondAttr(aCondition, ROLE, ROLE_SEARCH);
    }
}
