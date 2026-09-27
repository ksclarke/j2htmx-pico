package info.freelibrary.j2htmx.pico;

import info.freelibrary.j2htmx.pico.attributes.Attr;
import info.freelibrary.j2htmx.pico.tags.ContainerTag;
import info.freelibrary.j2htmx.pico.tags.DomContent;
import info.freelibrary.j2htmx.pico.tags.DomContentJoiner;
import info.freelibrary.j2htmx.pico.tags.EmptyTag;
import info.freelibrary.j2htmx.pico.tags.InlineStaticResource;
import info.freelibrary.j2htmx.pico.tags.Tag;
import info.freelibrary.j2htmx.pico.tags.Text;
import info.freelibrary.j2htmx.pico.tags.UnescapedText;
import info.freelibrary.j2htmx.pico.tags.specialized.*;

import java.util.*;
import java.util.Map.Entry;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Primary factory class providing static builder methods to construct HTML, HTMX, and Pico CSS elements.
 */
public class TagCreator {

    private TagCreator() {
    }

    /**
     * Generic if-expression to do if'ing inside method calls
     *
     * @param <T>       The derived generic parameter type
     * @param condition the condition to if-on
     * @param ifValue   the value to return if condition is true
     * @return value if condition is true, null otherwise
     */
    public static <T> T iff(final boolean condition, final T ifValue) {
        return condition ? ifValue : null;
    }

    /**
     * Generic if-expression to if'ing inside method calls
     *
     * @param optional   The item that may be present
     * @param ifFunction The function that will be called if that optional is present
     * @param <T>        The derived generic parameter type
     * @param <U>        The supplying generic parameter type
     * @return transformed value if condition is true, null otherwise
     */
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public static <T, U> T iff(final Optional<U> optional, final Function<U, T> ifFunction) {
        if (Objects.nonNull(optional) && optional.isPresent()) {
            return optional.map(ifFunction).orElse(null);
        }
        return null;
    }

    /**
     * Like {@link #iff}, but returns else-value instead of null
     */
    public static <T> T iffElse(final boolean condition, final T ifValue, final T elseValue) {
        return condition ? ifValue : elseValue;
    }

    /**
     * Returns a Attr.ShortForm object with either id, classes or both,
     * obtained from parsing the input string using css selector syntax
     *
     * @param attrs the string with shortform attributes, only id and class is supported
     * @return a Attr.ShortForm object
     */
    public static Attr.ShortForm attrs(final String attrs) {
        return Attr.shortFormFromAttrsString(attrs);
    }

    /**
     * Returns the HTML created by concatenating the input elements,
     * separated by space, in encounter order.
     * Also removes spaces before periods and commas.
     *
     * @param stringOrDomObjects the elements to join
     * @return joined elements as HTML
     */
    public static UnescapedText join(final Object... stringOrDomObjects) {
        return DomContentJoiner.join(" ", true, stringOrDomObjects);
    }

    /**
     * Creates a group of DomContent objects (that may be used with {@link #iff(boolean, Object)} or returned from a
     * method call).
     *
     * @param contents the DomContent elements
     * @return DomContent containing the given elements
     */
    public static DomContent each(final DomContent... contents) {
        return new ContainerTag<>(null).with(contents);
    }

    /**
     * Creates a DomContent object containing HTML elements from a stream.
     * Intended usage: {@literal each(numbers.stream().map(n -> li(n.toString())))}
     *
     * @param stream the stream of DomContent elements
     * @return DomContent containing elements from the stream
     */
    public static DomContent each(final Stream<DomContent> stream) {
        return new ContainerTag<>(null).with(stream);
    }

    /**
     * Creates a DomContent object containing HTML using a mapping function on a collection
     * Intended usage: {@literal each(numbers, n -> li(n.toString()))}
     *
     * @param <T>        The derived generic parameter type
     * @param collection the collection to iterate over, ex: a list of values "1, 2, 3"
     * @param mapper     the mapping function, ex: {@literal "n -> li(n.toString())"}
     * @return DomContent containing mapped data {@literal (ex. docs: [li(1), li(2), li(3)])}
     */
    public static <T> DomContent each(final Collection<T> collection, final Function<? super T, DomContent> mapper) {
        return tag(null).with(collection.stream().map(mapper));
    }

    /**
     * Creates a DomContent object containing HTML using a mapping BiFunction on a collection
     * Intended usage: {@literal each(names, (index, name) -> li(index + " " + name))}
     *
     * @param <T>        The derived generic parameter type
     * @param collection the collection to iterate over, ex: a list of values [ "Tom", "Dick", "Harry" ]
     * @param mapper     the mapping BiFunction, ex: {@literal "(index, name) -> li(index + " " + name)"}
     * @return DomContent containing mapped data {@literal (ex. docs: [li(0 Tom), li(1 Dick), li(2 Harry)])}
     */
    public static <T> DomContent each(final Collection<T> collection, final BiFunction<Integer, ? super T, DomContent> mapper) {
        final ContainerTag<?> dom = tag(null);
        int i = 0;
        for(final T t : collection){
            dom.with(mapper.apply(i++, t));
        }
        return dom;
    }

    /**
     * Creates a DomContent object containing HTML using a mapping function on map entries.
     *
     * @param <I> The type of the keys
     * @param <T> The type of the values
     * @param map The map to iterate over
     * @param mapper The mapping function applied to each map entry
     * @return DomContent containing the mapped elements
     */
    public static <I, T> DomContent each(final Map<I, T> map, final Function<Entry<I, T>, DomContent> mapper) {
        return each(map.entrySet().stream().map(mapper));
    }

    /**
     * Creates a DomContent object containing HTML using a mapping function on a map
     * Intended usage: {@literal each(idsToNames, (id, name) -> li(id + " " + name))}
     *
     * @param <I>    The type of the keys
     * @param <T>    The type of the values
     * @param map    the map to iterate over, ex: a map of values { 1: "Tom", 2: "Dick", 3: "Harry" }
     * @param mapper the mapping function, ex: {@literal "(id, name) -> li(id + " " + name)"}
     * @return DomContent containing mapped data {@literal (ex. docs: [li(1 Tom), li(2 Dick), li(3 Harry)])}
     */
    public static <I, T> DomContent each(final Map<I, T> map, final BiFunction<I, T, DomContent> mapper) {
        return each(map.entrySet().stream().map(entry -> mapper.apply(entry.getKey(), entry.getValue())));
    }

    /**
     * Filters a collection to a list, to be used with {@link #each}
     * Intended usage: {@literal each(filter(numbers, n -> n % 2 == 0), n -> li(n.toString()))}
     *
     * @param <T>        The derived generic parameter type
     * @param collection the collection to filter, ex: a list of values "1, 2, 3"
     * @param filter     the filter predicate, {@literal ex: "n -> n % 2 == 0"}
     * @return the filtered collection as a list (ex. docs: 2)
     */
    public static <T> List<T> filter(final Collection<T> collection, final Predicate<? super T> filter) {
        return collection.stream().filter(filter).collect(Collectors.toList());
    }

    /**
     * Wraps a String in an UnescapedText element
     *
     * @param html the input html
     * @return the input html wrapped in an UnescapedText element
     */
    public static UnescapedText rawHtml(final String html) {
        return new UnescapedText(html);
    }

    /**
     * Wraps a String in a Text element (does html-escaping)
     *
     * @param text the input string
     * @return the input string, html-escaped
     */
    public static Text text(final String text) {
        return new Text(text);
    }

    /**
     * Return a complete html document string
     *
     * @param htmlTag the html content of a website
     * @return document declaration and rendered html content
     */
    public static String document(final HtmlTag htmlTag) {
        return document().render() + htmlTag.render();
    }

    //Special tags
    /**
     * Creates a custom {@link ContainerTag} with the specified tag name.
     *
     * @param tagName The tag name
     * @return A new ContainerTag
     */
    public static ContainerTag<? extends Tag<?>> tag(final String tagName) {
        return new ContainerTag<>(tagName);
    }

    /**
     * Creates a custom {@link EmptyTag} with the specified tag name.
     *
     * @param tagName The tag name
     * @return A new EmptyTag
     */
    public static EmptyTag<? extends Tag<?>> emptyTag(final String tagName) {
        return new EmptyTag<>(tagName);
    }

    /**
     * Reads a file from the classpath or filesystem and returns its escaped text content.
     *
     * @param path The file path
     * @return An escaped Text element
     */
    public static Text fileAsEscapedString(final String path) {
        return text(InlineStaticResource.getFileAsString(path));
    }

    /**
     * Reads a file from the classpath or filesystem and returns its raw HTML content.
     *
     * @param path The file path
     * @return An UnescapedText element
     */
    public static UnescapedText fileAsString(final String path) {
        return rawHtml(InlineStaticResource.getFileAsString(path));
    }

    /**
     * Creates a {@code <style>} tag containing unminified CSS read from the specified file path.
     *
     * @param path The CSS file path
     * @return A StyleTag containing the CSS content
     */
    public static StyleTag styleWithInlineFile(final String path) {
        return (StyleTag) InlineStaticResource.get(path, InlineStaticResource.TargetFormat.CSS);
    }

    /**
     * Creates a {@code <script>} tag containing unminified JavaScript read from the specified file path.
     *
     * @param path The JavaScript file path
     * @return A ScriptTag containing the JavaScript content
     */
    public static ScriptTag scriptWithInlineFile(final String path) {
        return (ScriptTag) InlineStaticResource.get(path, InlineStaticResource.TargetFormat.JS);
    }

    /**
     * Creates a {@code <style>} tag containing minified CSS read from the specified file path.
     *
     * @param path The CSS file path
     * @return A StyleTag containing the minified CSS content
     */
    public static StyleTag styleWithInlineFile_min(final String path) {
        return (StyleTag) InlineStaticResource.get(path, InlineStaticResource.TargetFormat.CSS_MIN);
    }

    /**
     * Creates a {@code <script>} tag containing minified JavaScript read from the specified file path.
     *
     * @param path The JavaScript file path
     * @return A ScriptTag containing the minified JavaScript content
     */
    public static ScriptTag scriptWithInlineFile_min(final String path) {
        return (ScriptTag) InlineStaticResource.get(path, InlineStaticResource.TargetFormat.JS_MIN);
    }

    /**
     * Creates an HTML5 doctype declaration {@code <!DOCTYPE html>}.
     *
     * @return DomContent representing the doctype declaration
     */
    public static DomContent document() {
        return rawHtml("<!DOCTYPE html>");
    }

    public static HtmlTag html() {
        return new HtmlTag();
    }

    public static HtmlTag html(final String text) { return html().withText(text); }

    public static HtmlTag html(final DomContent... dc) { return html().with(dc); }

    public static HtmlTag html(final Attr.ShortForm shortAttr) {
        return Attr.addTo(html(), shortAttr);
    }

    public static HtmlTag html(final Attr.ShortForm shortAttr, final String text) {
        return Attr.addTo(html(text), shortAttr);
    }

    public static HtmlTag html(final Attr.ShortForm shortAttr, final DomContent... dc) {
        return Attr.addTo(html(dc), shortAttr);
    }

    public static HxPartialTag hxPartial() {
        return new HxPartialTag();
    }

    public static HxPartialTag hxPartial(final String text) {
        return hxPartial().withText(text);
    }

    public static HxPartialTag hxPartial(final DomContent... dc) {
        return hxPartial().with(dc);
    }

    public static HxPartialTag hxPartial(final Attr.ShortForm shortAttr) {
        return Attr.addTo(hxPartial(), shortAttr);
    }

    public static HxPartialTag hxPartial(final Attr.ShortForm shortAttr, final String text) {
        return Attr.addTo(hxPartial(text), shortAttr);
    }

    public static HxPartialTag hxPartial(final Attr.ShortForm shortAttr, final DomContent... dc) {
        return Attr.addTo(hxPartial(dc), shortAttr);
    }

    public static BodyTag body() {
        return new BodyTag();
    }

    public static BodyTag body(final String text) {
        return new BodyTag().withText(text);
    }

    public static BodyTag body(final DomContent... dc) {
        return new BodyTag().with(dc);
    }

    public static BodyTag body(final Attr.ShortForm shortAttr) {
        return Attr.addTo(new BodyTag(), shortAttr);
    }

    public static BodyTag body(final Attr.ShortForm shortAttr, final String text) {
        return Attr.addTo(new BodyTag().withText(text), shortAttr);
    }

    public static BodyTag body(final Attr.ShortForm shortAttr, final DomContent... dc) {
        return Attr.addTo(new BodyTag().with(dc), shortAttr);
    }

    public static HeadTag head() {
        return new HeadTag();
    }

    public static HeadTag head(final String text) {
        return new HeadTag().withText(text);
    }

    public static HeadTag head(final DomContent... dc) {
        return new HeadTag().with(dc);
    }

    public static HeadTag head(final Attr.ShortForm shortAttr) {
        return Attr.addTo(new HeadTag(), shortAttr);
    }

    public static HeadTag head(final Attr.ShortForm shortAttr, final String text) {
        return Attr.addTo(new HeadTag().withText(text), shortAttr);
    }

    public static HeadTag head(final Attr.ShortForm shortAttr, final DomContent... dc) {
        return Attr.addTo(new HeadTag().with(dc), shortAttr);
    }

    // EmptyTags, generated in class info.freelibrary.j2html.tags.generators.TagCreatorCodeGenerator
    public static AreaTag area ()                                                   { return  new AreaTag(); }
    public static AreaTag area (final Attr.ShortForm shortAttr)                           {  return Attr.addTo( new AreaTag(), shortAttr); }

    public static BaseTag base ()                                                   { return  new BaseTag(); }
    public static BaseTag base (final Attr.ShortForm shortAttr)                           {  return Attr.addTo( new BaseTag(), shortAttr); }

    public static BrTag br ()                                                       { return  new BrTag(); }
    public static BrTag br (final Attr.ShortForm shortAttr)                               {  return Attr.addTo( new BrTag(), shortAttr); }

    public static ColTag col ()                                                     { return  new ColTag(); }
    public static ColTag col (final Attr.ShortForm shortAttr)                             {  return Attr.addTo( new ColTag(), shortAttr); }

    public static EmbedTag embed ()                                                 { return  new EmbedTag(); }
    public static EmbedTag embed (final Attr.ShortForm shortAttr)                         {  return Attr.addTo( new EmbedTag(), shortAttr); }

    public static HrTag hr ()                                                       { return  new HrTag(); }
    public static HrTag hr (final Attr.ShortForm shortAttr)                               {  return Attr.addTo( new HrTag(), shortAttr); }

    public static ImgTag img ()                                                     { return  new ImgTag(); }
    public static ImgTag img (final Attr.ShortForm shortAttr)                             {  return Attr.addTo( new ImgTag(), shortAttr); }

    public static InputTag input ()                                                 { return  new InputTag(); }
    public static InputTag input (final Attr.ShortForm shortAttr)                         {  return Attr.addTo( new InputTag(), shortAttr); }

    public static KeygenTag keygen ()                                               { return  new KeygenTag(); }
    public static KeygenTag keygen (final Attr.ShortForm shortAttr)                       {  return Attr.addTo( new KeygenTag(), shortAttr); }

    public static LinkTag link ()                                                   { return  new LinkTag(); }
    public static LinkTag link (final Attr.ShortForm shortAttr)                           {  return Attr.addTo( new LinkTag(), shortAttr); }

    public static MetaTag meta ()                                                   { return  new MetaTag(); }
    public static MetaTag meta (final Attr.ShortForm shortAttr)                           {  return Attr.addTo( new MetaTag(), shortAttr); }

    public static ParamTag param ()                                                 { return  new ParamTag(); }
    public static ParamTag param (final Attr.ShortForm shortAttr)                         {  return Attr.addTo( new ParamTag(), shortAttr); }

    public static SourceTag source ()                                               { return  new SourceTag(); }
    public static SourceTag source (final Attr.ShortForm shortAttr)                       {  return Attr.addTo( new SourceTag(), shortAttr); }

    public static TrackTag track ()                                                 { return  new TrackTag(); }
    public static TrackTag track (final Attr.ShortForm shortAttr)                         {  return Attr.addTo( new TrackTag(), shortAttr); }

    public static WbrTag wbr ()                                                     { return  new WbrTag(); }
    public static WbrTag wbr (final Attr.ShortForm shortAttr)                             {  return Attr.addTo( new WbrTag(), shortAttr); }

    // ContainerTags, generated in class info.freelibrary.j2html.tags.generators.TagCreatorCodeGenerator
    public static ATag a ()                                                         {  return new ATag(); }
    public static ATag a (final String text)                                              {  return new ATag().withText(text); }
    public static ATag a (final DomContent... dc)                                         {  return new ATag().with(dc); }
    public static ATag a (final Attr.ShortForm shortAttr)                                 {  return Attr.addTo( new ATag(), shortAttr); }
    public static ATag a (final Attr.ShortForm shortAttr, final String text)                    {  return Attr.addTo( new ATag().withText(text), shortAttr); }
    public static ATag a (final Attr.ShortForm shortAttr, final DomContent... dc)               {  return Attr.addTo( new ATag().with(dc), shortAttr); }

    public static AbbrTag abbr ()                                                   {  return new AbbrTag(); }
    public static AbbrTag abbr (final String text)                                        {  return new AbbrTag().withText(text); }
    public static AbbrTag abbr (final DomContent... dc)                                   {  return new AbbrTag().with(dc); }
    public static AbbrTag abbr (final Attr.ShortForm shortAttr)                           {  return Attr.addTo( new AbbrTag(), shortAttr); }
    public static AbbrTag abbr (final Attr.ShortForm shortAttr, final String text)              {  return Attr.addTo( new AbbrTag().withText(text), shortAttr); }
    public static AbbrTag abbr (final Attr.ShortForm shortAttr, final DomContent... dc)         {  return Attr.addTo( new AbbrTag().with(dc), shortAttr); }

    public static AddressTag address ()                                             {  return new AddressTag(); }
    public static AddressTag address (final String text)                                  {  return new AddressTag().withText(text); }
    public static AddressTag address (final DomContent... dc)                             {  return new AddressTag().with(dc); }
    public static AddressTag address (final Attr.ShortForm shortAttr)                     {  return Attr.addTo( new AddressTag(), shortAttr); }
    public static AddressTag address (final Attr.ShortForm shortAttr, final String text)        {  return Attr.addTo( new AddressTag().withText(text), shortAttr); }
    public static AddressTag address (final Attr.ShortForm shortAttr, final DomContent... dc)   {  return Attr.addTo( new AddressTag().with(dc), shortAttr); }

    public static ArticleTag article ()                                             {  return new ArticleTag(); }
    public static ArticleTag article (final String text)                                  {  return new ArticleTag().withText(text); }
    public static ArticleTag article (final DomContent... dc)                             {  return new ArticleTag().with(dc); }
    public static ArticleTag article (final Attr.ShortForm shortAttr)                     {  return Attr.addTo( new ArticleTag(), shortAttr); }
    public static ArticleTag article (final Attr.ShortForm shortAttr, final String text)        {  return Attr.addTo( new ArticleTag().withText(text), shortAttr); }
    public static ArticleTag article (final Attr.ShortForm shortAttr, final DomContent... dc)   {  return Attr.addTo( new ArticleTag().with(dc), shortAttr); }

    public static AsideTag aside ()                                                 {  return new AsideTag(); }
    public static AsideTag aside (final String text)                                      {  return new AsideTag().withText(text); }
    public static AsideTag aside (final DomContent... dc)                                 {  return new AsideTag().with(dc); }
    public static AsideTag aside (final Attr.ShortForm shortAttr)                         {  return Attr.addTo( new AsideTag(), shortAttr); }
    public static AsideTag aside (final Attr.ShortForm shortAttr, final String text)            {  return Attr.addTo( new AsideTag().withText(text), shortAttr); }
    public static AsideTag aside (final Attr.ShortForm shortAttr, final DomContent... dc)       {  return Attr.addTo( new AsideTag().with(dc), shortAttr); }

    public static AudioTag audio ()                                                 {  return new AudioTag(); }
    public static AudioTag audio (final String text)                                      {  return new AudioTag().withText(text); }
    public static AudioTag audio (final DomContent... dc)                                 {  return new AudioTag().with(dc); }
    public static AudioTag audio (final Attr.ShortForm shortAttr)                         {  return Attr.addTo( new AudioTag(), shortAttr); }
    public static AudioTag audio (final Attr.ShortForm shortAttr, final String text)            {  return Attr.addTo( new AudioTag().withText(text), shortAttr); }
    public static AudioTag audio (final Attr.ShortForm shortAttr, final DomContent... dc)       {  return Attr.addTo( new AudioTag().with(dc), shortAttr); }

    public static BTag b ()                                                         {  return new BTag(); }
    public static BTag b (final String text)                                              {  return new BTag().withText(text); }
    public static BTag b (final DomContent... dc)                                         {  return new BTag().with(dc); }
    public static BTag b (final Attr.ShortForm shortAttr)                                 {  return Attr.addTo( new BTag(), shortAttr); }
    public static BTag b (final Attr.ShortForm shortAttr, final String text)                    {  return Attr.addTo( new BTag().withText(text), shortAttr); }
    public static BTag b (final Attr.ShortForm shortAttr, final DomContent... dc)               {  return Attr.addTo( new BTag().with(dc), shortAttr); }

    public static BdiTag bdi ()                                                     {  return new BdiTag(); }
    public static BdiTag bdi (final String text)                                          {  return new BdiTag().withText(text); }
    public static BdiTag bdi (final DomContent... dc)                                     {  return new BdiTag().with(dc); }
    public static BdiTag bdi (final Attr.ShortForm shortAttr)                             {  return Attr.addTo( new BdiTag(), shortAttr); }
    public static BdiTag bdi (final Attr.ShortForm shortAttr, final String text)                {  return Attr.addTo( new BdiTag().withText(text), shortAttr); }
    public static BdiTag bdi (final Attr.ShortForm shortAttr, final DomContent... dc)           {  return Attr.addTo( new BdiTag().with(dc), shortAttr); }

    public static BdoTag bdo ()                                                     {  return new BdoTag(); }
    public static BdoTag bdo (final String text)                                          {  return new BdoTag().withText(text); }
    public static BdoTag bdo (final DomContent... dc)                                     {  return new BdoTag().with(dc); }
    public static BdoTag bdo (final Attr.ShortForm shortAttr)                             {  return Attr.addTo( new BdoTag(), shortAttr); }
    public static BdoTag bdo (final Attr.ShortForm shortAttr, final String text)                {  return Attr.addTo( new BdoTag().withText(text), shortAttr); }
    public static BdoTag bdo (final Attr.ShortForm shortAttr, final DomContent... dc)           {  return Attr.addTo( new BdoTag().with(dc), shortAttr); }

    public static BlockquoteTag blockquote ()                                       {  return new BlockquoteTag(); }
    public static BlockquoteTag blockquote (final String text)                            {  return new BlockquoteTag().withText(text); }
    public static BlockquoteTag blockquote (final DomContent... dc)                       {  return new BlockquoteTag().with(dc); }
    public static BlockquoteTag blockquote (final Attr.ShortForm shortAttr)               {  return Attr.addTo( new BlockquoteTag(), shortAttr); }
    public static BlockquoteTag blockquote (final Attr.ShortForm shortAttr, final String text)  {  return Attr.addTo( new BlockquoteTag().withText(text), shortAttr); }
    public static BlockquoteTag blockquote (final Attr.ShortForm shortAttr, final DomContent... dc){  return Attr.addTo( new BlockquoteTag().with(dc), shortAttr); }

    public static ButtonTag button ()                                               {  return new ButtonTag(); }
    public static ButtonTag button (final String text)                                    {  return new ButtonTag().withText(text); }
    public static ButtonTag button (final DomContent... dc)                               {  return new ButtonTag().with(dc); }
    public static ButtonTag button (final Attr.ShortForm shortAttr)                       {  return Attr.addTo( new ButtonTag(), shortAttr); }
    public static ButtonTag button (final Attr.ShortForm shortAttr, final String text)          {  return Attr.addTo( new ButtonTag().withText(text), shortAttr); }
    public static ButtonTag button (final Attr.ShortForm shortAttr, final DomContent... dc)     {  return Attr.addTo( new ButtonTag().with(dc), shortAttr); }

    public static CanvasTag canvas ()                                               {  return new CanvasTag(); }
    public static CanvasTag canvas (final String text)                                    {  return new CanvasTag().withText(text); }
    public static CanvasTag canvas (final DomContent... dc)                               {  return new CanvasTag().with(dc); }
    public static CanvasTag canvas (final Attr.ShortForm shortAttr)                       {  return Attr.addTo( new CanvasTag(), shortAttr); }
    public static CanvasTag canvas (final Attr.ShortForm shortAttr, final String text)          {  return Attr.addTo( new CanvasTag().withText(text), shortAttr); }
    public static CanvasTag canvas (final Attr.ShortForm shortAttr, final DomContent... dc)     {  return Attr.addTo( new CanvasTag().with(dc), shortAttr); }

    public static CaptionTag caption ()                                             {  return new CaptionTag(); }
    public static CaptionTag caption (final String text)                                  {  return new CaptionTag().withText(text); }
    public static CaptionTag caption (final DomContent... dc)                             {  return new CaptionTag().with(dc); }
    public static CaptionTag caption (final Attr.ShortForm shortAttr)                     {  return Attr.addTo( new CaptionTag(), shortAttr); }
    public static CaptionTag caption (final Attr.ShortForm shortAttr, final String text)        {  return Attr.addTo( new CaptionTag().withText(text), shortAttr); }
    public static CaptionTag caption (final Attr.ShortForm shortAttr, final DomContent... dc)   {  return Attr.addTo( new CaptionTag().with(dc), shortAttr); }

    public static CiteTag cite ()                                                   {  return new CiteTag(); }
    public static CiteTag cite (final String text)                                        {  return new CiteTag().withText(text); }
    public static CiteTag cite (final DomContent... dc)                                   {  return new CiteTag().with(dc); }
    public static CiteTag cite (final Attr.ShortForm shortAttr)                           {  return Attr.addTo( new CiteTag(), shortAttr); }
    public static CiteTag cite (final Attr.ShortForm shortAttr, final String text)              {  return Attr.addTo( new CiteTag().withText(text), shortAttr); }
    public static CiteTag cite (final Attr.ShortForm shortAttr, final DomContent... dc)         {  return Attr.addTo( new CiteTag().with(dc), shortAttr); }

    public static CodeTag code ()                                                   {  return new CodeTag(); }
    public static CodeTag code (final String text)                                        {  return new CodeTag().withText(text); }
    public static CodeTag code (final DomContent... dc)                                   {  return new CodeTag().with(dc); }
    public static CodeTag code (final Attr.ShortForm shortAttr)                           {  return Attr.addTo( new CodeTag(), shortAttr); }
    public static CodeTag code (final Attr.ShortForm shortAttr, final String text)              {  return Attr.addTo( new CodeTag().withText(text), shortAttr); }
    public static CodeTag code (final Attr.ShortForm shortAttr, final DomContent... dc)         {  return Attr.addTo( new CodeTag().with(dc), shortAttr); }

    public static ColgroupTag colgroup ()                                           {  return new ColgroupTag(); }
    public static ColgroupTag colgroup (final String text)                                {  return new ColgroupTag().withText(text); }
    public static ColgroupTag colgroup (final DomContent... dc)                           {  return new ColgroupTag().with(dc); }
    public static ColgroupTag colgroup (final Attr.ShortForm shortAttr)                   {  return Attr.addTo( new ColgroupTag(), shortAttr); }
    public static ColgroupTag colgroup (final Attr.ShortForm shortAttr, final String text)      {  return Attr.addTo( new ColgroupTag().withText(text), shortAttr); }
    public static ColgroupTag colgroup (final Attr.ShortForm shortAttr, final DomContent... dc) {  return Attr.addTo( new ColgroupTag().with(dc), shortAttr); }

    public static DataTag data ()                                           {  return new DataTag(); }
    public static DataTag data (final String text)                                {  return new DataTag().withText(text); }
    public static DataTag data (final DomContent... dc)                           {  return new DataTag().with(dc); }
    public static DataTag data (final Attr.ShortForm shortAttr)                   {  return Attr.addTo( new DataTag(), shortAttr); }
    public static DataTag data (final Attr.ShortForm shortAttr, final String text)      {  return Attr.addTo( new DataTag().withText(text), shortAttr); }
    public static DataTag data (final Attr.ShortForm shortAttr, final DomContent... dc) {  return Attr.addTo( new DataTag().with(dc), shortAttr); }

    public static DatalistTag datalist ()                                           {  return new DatalistTag(); }
    public static DatalistTag datalist (final String text)                                {  return new DatalistTag().withText(text); }
    public static DatalistTag datalist (final DomContent... dc)                           {  return new DatalistTag().with(dc); }
    public static DatalistTag datalist (final Attr.ShortForm shortAttr)                   {  return Attr.addTo( new DatalistTag(), shortAttr); }
    public static DatalistTag datalist (final Attr.ShortForm shortAttr, final String text)      {  return Attr.addTo( new DatalistTag().withText(text), shortAttr); }
    public static DatalistTag datalist (final Attr.ShortForm shortAttr, final DomContent... dc) {  return Attr.addTo( new DatalistTag().with(dc), shortAttr); }

    public static DdTag dd ()                                                       {  return new DdTag(); }
    public static DdTag dd (final String text)                                            {  return new DdTag().withText(text); }
    public static DdTag dd (final DomContent... dc)                                       {  return new DdTag().with(dc); }
    public static DdTag dd (final Attr.ShortForm shortAttr)                               {  return Attr.addTo( new DdTag(), shortAttr); }
    public static DdTag dd (final Attr.ShortForm shortAttr, final String text)                  {  return Attr.addTo( new DdTag().withText(text), shortAttr); }
    public static DdTag dd (final Attr.ShortForm shortAttr, final DomContent... dc)             {  return Attr.addTo( new DdTag().with(dc), shortAttr); }

    public static DelTag del ()                                                     {  return new DelTag(); }
    public static DelTag del (final String text)                                          {  return new DelTag().withText(text); }
    public static DelTag del (final DomContent... dc)                                     {  return new DelTag().with(dc); }
    public static DelTag del (final Attr.ShortForm shortAttr)                             {  return Attr.addTo( new DelTag(), shortAttr); }
    public static DelTag del (final Attr.ShortForm shortAttr, final String text)                {  return Attr.addTo( new DelTag().withText(text), shortAttr); }
    public static DelTag del (final Attr.ShortForm shortAttr, final DomContent... dc)           {  return Attr.addTo( new DelTag().with(dc), shortAttr); }

    public static DetailsTag details ()                                             {  return new DetailsTag(); }
    public static DetailsTag details (final String text)                                  {  return new DetailsTag().withText(text); }
    public static DetailsTag details (final DomContent... dc)                             {  return new DetailsTag().with(dc); }
    public static DetailsTag details (final Attr.ShortForm shortAttr)                     {  return Attr.addTo( new DetailsTag(), shortAttr); }
    public static DetailsTag details (final Attr.ShortForm shortAttr, final String text)        {  return Attr.addTo( new DetailsTag().withText(text), shortAttr); }
    public static DetailsTag details (final Attr.ShortForm shortAttr, final DomContent... dc)   {  return Attr.addTo( new DetailsTag().with(dc), shortAttr); }

    public static DfnTag dfn ()                                                     {  return new DfnTag(); }
    public static DfnTag dfn (final String text)                                          {  return new DfnTag().withText(text); }
    public static DfnTag dfn (final DomContent... dc)                                     {  return new DfnTag().with(dc); }
    public static DfnTag dfn (final Attr.ShortForm shortAttr)                             {  return Attr.addTo( new DfnTag(), shortAttr); }
    public static DfnTag dfn (final Attr.ShortForm shortAttr, final String text)                {  return Attr.addTo( new DfnTag().withText(text), shortAttr); }
    public static DfnTag dfn (final Attr.ShortForm shortAttr, final DomContent... dc)           {  return Attr.addTo( new DfnTag().with(dc), shortAttr); }

    public static DialogTag dialog ()                                               {  return new DialogTag(); }
    public static DialogTag dialog (final String text)                                    {  return new DialogTag().withText(text); }
    public static DialogTag dialog (final DomContent... dc)                               {  return new DialogTag().with(dc); }
    public static DialogTag dialog (final Attr.ShortForm shortAttr)                       {  return Attr.addTo( new DialogTag(), shortAttr); }
    public static DialogTag dialog (final Attr.ShortForm shortAttr, final String text)          {  return Attr.addTo( new DialogTag().withText(text), shortAttr); }
    public static DialogTag dialog (final Attr.ShortForm shortAttr, final DomContent... dc)     {  return Attr.addTo( new DialogTag().with(dc), shortAttr); }

    public static DivTag div ()                                                     {  return new DivTag(); }
    public static DivTag div (final String text)                                          {  return new DivTag().withText(text); }
    public static DivTag div (final DomContent... dc)                                     {  return new DivTag().with(dc); }
    public static DivTag div (final Attr.ShortForm shortAttr)                             {  return Attr.addTo( new DivTag(), shortAttr); }
    public static DivTag div (final Attr.ShortForm shortAttr, final String text)                {  return Attr.addTo( new DivTag().withText(text), shortAttr); }
    public static DivTag div (final Attr.ShortForm shortAttr, final DomContent... dc)           {  return Attr.addTo( new DivTag().with(dc), shortAttr); }

    public static DlTag dl ()                                                       {  return new DlTag(); }
    public static DlTag dl (final String text)                                            {  return new DlTag().withText(text); }
    public static DlTag dl (final DomContent... dc)                                       {  return new DlTag().with(dc); }
    public static DlTag dl (final Attr.ShortForm shortAttr)                               {  return Attr.addTo( new DlTag(), shortAttr); }
    public static DlTag dl (final Attr.ShortForm shortAttr, final String text)                  {  return Attr.addTo( new DlTag().withText(text), shortAttr); }
    public static DlTag dl (final Attr.ShortForm shortAttr, final DomContent... dc)             {  return Attr.addTo( new DlTag().with(dc), shortAttr); }

    public static DtTag dt ()                                                       {  return new DtTag(); }
    public static DtTag dt (final String text)                                            {  return new DtTag().withText(text); }
    public static DtTag dt (final DomContent... dc)                                       {  return new DtTag().with(dc); }
    public static DtTag dt (final Attr.ShortForm shortAttr)                               {  return Attr.addTo( new DtTag(), shortAttr); }
    public static DtTag dt (final Attr.ShortForm shortAttr, final String text)                  {  return Attr.addTo( new DtTag().withText(text), shortAttr); }
    public static DtTag dt (final Attr.ShortForm shortAttr, final DomContent... dc)             {  return Attr.addTo( new DtTag().with(dc), shortAttr); }

    public static EmTag em ()                                                       {  return new EmTag(); }
    public static EmTag em (final String text)                                            {  return new EmTag().withText(text); }
    public static EmTag em (final DomContent... dc)                                       {  return new EmTag().with(dc); }
    public static EmTag em (final Attr.ShortForm shortAttr)                               {  return Attr.addTo( new EmTag(), shortAttr); }
    public static EmTag em (final Attr.ShortForm shortAttr, final String text)                  {  return Attr.addTo( new EmTag().withText(text), shortAttr); }
    public static EmTag em (final Attr.ShortForm shortAttr, final DomContent... dc)             {  return Attr.addTo( new EmTag().with(dc), shortAttr); }

    public static FieldsetTag fieldset ()                                           {  return new FieldsetTag(); }
    public static FieldsetTag fieldset (final String text)                                {  return new FieldsetTag().withText(text); }
    public static FieldsetTag fieldset (final DomContent... dc)                           {  return new FieldsetTag().with(dc); }
    public static FieldsetTag fieldset (final Attr.ShortForm shortAttr)                   {  return Attr.addTo( new FieldsetTag(), shortAttr); }
    public static FieldsetTag fieldset (final Attr.ShortForm shortAttr, final String text)      {  return Attr.addTo( new FieldsetTag().withText(text), shortAttr); }
    public static FieldsetTag fieldset (final Attr.ShortForm shortAttr, final DomContent... dc) {  return Attr.addTo( new FieldsetTag().with(dc), shortAttr); }

    public static FigcaptionTag figcaption ()                                       {  return new FigcaptionTag(); }
    public static FigcaptionTag figcaption (final String text)                            {  return new FigcaptionTag().withText(text); }
    public static FigcaptionTag figcaption (final DomContent... dc)                       {  return new FigcaptionTag().with(dc); }
    public static FigcaptionTag figcaption (final Attr.ShortForm shortAttr)               {  return Attr.addTo( new FigcaptionTag(), shortAttr); }
    public static FigcaptionTag figcaption (final Attr.ShortForm shortAttr, final String text)  {  return Attr.addTo( new FigcaptionTag().withText(text), shortAttr); }
    public static FigcaptionTag figcaption (final Attr.ShortForm shortAttr, final DomContent... dc){  return Attr.addTo( new FigcaptionTag().with(dc), shortAttr); }

    public static FigureTag figure ()                                               {  return new FigureTag(); }
    public static FigureTag figure (final String text)                                    {  return new FigureTag().withText(text); }
    public static FigureTag figure (final DomContent... dc)                               {  return new FigureTag().with(dc); }
    public static FigureTag figure (final Attr.ShortForm shortAttr)                       {  return Attr.addTo( new FigureTag(), shortAttr); }
    public static FigureTag figure (final Attr.ShortForm shortAttr, final String text)          {  return Attr.addTo( new FigureTag().withText(text), shortAttr); }
    public static FigureTag figure (final Attr.ShortForm shortAttr, final DomContent... dc)     {  return Attr.addTo( new FigureTag().with(dc), shortAttr); }

    public static FooterTag footer ()                                               {  return new FooterTag(); }
    public static FooterTag footer (final String text)                                    {  return new FooterTag().withText(text); }
    public static FooterTag footer (final DomContent... dc)                               {  return new FooterTag().with(dc); }
    public static FooterTag footer (final Attr.ShortForm shortAttr)                       {  return Attr.addTo( new FooterTag(), shortAttr); }
    public static FooterTag footer (final Attr.ShortForm shortAttr, final String text)          {  return Attr.addTo( new FooterTag().withText(text), shortAttr); }
    public static FooterTag footer (final Attr.ShortForm shortAttr, final DomContent... dc)     {  return Attr.addTo( new FooterTag().with(dc), shortAttr); }

    public static FormTag form ()                                                   {  return new FormTag(); }
    public static FormTag form (final String text)                                        {  return new FormTag().withText(text); }
    public static FormTag form (final DomContent... dc)                                   {  return new FormTag().with(dc); }
    public static FormTag form (final Attr.ShortForm shortAttr)                           {  return Attr.addTo( new FormTag(), shortAttr); }
    public static FormTag form (final Attr.ShortForm shortAttr, final String text)              {  return Attr.addTo( new FormTag().withText(text), shortAttr); }
    public static FormTag form (final Attr.ShortForm shortAttr, final DomContent... dc)         {  return Attr.addTo( new FormTag().with(dc), shortAttr); }

    public static H1Tag h1 ()                                                       {  return new H1Tag(); }
    public static H1Tag h1 (final String text)                                            {  return new H1Tag().withText(text); }
    public static H1Tag h1 (final DomContent... dc)                                       {  return new H1Tag().with(dc); }
    public static H1Tag h1 (final Attr.ShortForm shortAttr)                               {  return Attr.addTo( new H1Tag(), shortAttr); }
    public static H1Tag h1 (final Attr.ShortForm shortAttr, final String text)                  {  return Attr.addTo( new H1Tag().withText(text), shortAttr); }
    public static H1Tag h1 (final Attr.ShortForm shortAttr, final DomContent... dc)             {  return Attr.addTo( new H1Tag().with(dc), shortAttr); }

    public static H2Tag h2 ()                                                       {  return new H2Tag(); }
    public static H2Tag h2 (final String text)                                            {  return new H2Tag().withText(text); }
    public static H2Tag h2 (final DomContent... dc)                                       {  return new H2Tag().with(dc); }
    public static H2Tag h2 (final Attr.ShortForm shortAttr)                               {  return Attr.addTo( new H2Tag(), shortAttr); }
    public static H2Tag h2 (final Attr.ShortForm shortAttr, final String text)                  {  return Attr.addTo( new H2Tag().withText(text), shortAttr); }
    public static H2Tag h2 (final Attr.ShortForm shortAttr, final DomContent... dc)             {  return Attr.addTo( new H2Tag().with(dc), shortAttr); }

    public static H3Tag h3 ()                                                       {  return new H3Tag(); }
    public static H3Tag h3 (final String text)                                            {  return new H3Tag().withText(text); }
    public static H3Tag h3 (final DomContent... dc)                                       {  return new H3Tag().with(dc); }
    public static H3Tag h3 (final Attr.ShortForm shortAttr)                               {  return Attr.addTo( new H3Tag(), shortAttr); }
    public static H3Tag h3 (final Attr.ShortForm shortAttr, final String text)                  {  return Attr.addTo( new H3Tag().withText(text), shortAttr); }
    public static H3Tag h3 (final Attr.ShortForm shortAttr, final DomContent... dc)             {  return Attr.addTo( new H3Tag().with(dc), shortAttr); }

    public static H4Tag h4 ()                                                       {  return new H4Tag(); }
    public static H4Tag h4 (final String text)                                            {  return new H4Tag().withText(text); }
    public static H4Tag h4 (final DomContent... dc)                                       {  return new H4Tag().with(dc); }
    public static H4Tag h4 (final Attr.ShortForm shortAttr)                               {  return Attr.addTo( new H4Tag(), shortAttr); }
    public static H4Tag h4 (final Attr.ShortForm shortAttr, final String text)                  {  return Attr.addTo( new H4Tag().withText(text), shortAttr); }
    public static H4Tag h4 (final Attr.ShortForm shortAttr, final DomContent... dc)             {  return Attr.addTo( new H4Tag().with(dc), shortAttr); }

    public static H5Tag h5 ()                                                       {  return new H5Tag(); }
    public static H5Tag h5 (final String text)                                            {  return new H5Tag().withText(text); }
    public static H5Tag h5 (final DomContent... dc)                                       {  return new H5Tag().with(dc); }
    public static H5Tag h5 (final Attr.ShortForm shortAttr)                               {  return Attr.addTo( new H5Tag(), shortAttr); }
    public static H5Tag h5 (final Attr.ShortForm shortAttr, final String text)                  {  return Attr.addTo( new H5Tag().withText(text), shortAttr); }
    public static H5Tag h5 (final Attr.ShortForm shortAttr, final DomContent... dc)             {  return Attr.addTo( new H5Tag().with(dc), shortAttr); }

    public static H6Tag h6 ()                                                       {  return new H6Tag(); }
    public static H6Tag h6 (final String text)                                            {  return new H6Tag().withText(text); }
    public static H6Tag h6 (final DomContent... dc)                                       {  return new H6Tag().with(dc); }
    public static H6Tag h6 (final Attr.ShortForm shortAttr)                               {  return Attr.addTo( new H6Tag(), shortAttr); }
    public static H6Tag h6 (final Attr.ShortForm shortAttr, final String text)                  {  return Attr.addTo( new H6Tag().withText(text), shortAttr); }
    public static H6Tag h6 (final Attr.ShortForm shortAttr, final DomContent... dc)             {  return Attr.addTo( new H6Tag().with(dc), shortAttr); }

    public static HeaderTag header ()                                               {  return new HeaderTag(); }
    public static HeaderTag header (final String text)                                    {  return new HeaderTag().withText(text); }
    public static HeaderTag header (final DomContent... dc)                               {  return new HeaderTag().with(dc); }
    public static HeaderTag header (final Attr.ShortForm shortAttr)                       {  return Attr.addTo( new HeaderTag(), shortAttr); }
    public static HeaderTag header (final Attr.ShortForm shortAttr, final String text)          {  return Attr.addTo( new HeaderTag().withText(text), shortAttr); }
    public static HeaderTag header (final Attr.ShortForm shortAttr, final DomContent... dc)     {  return Attr.addTo( new HeaderTag().with(dc), shortAttr); }

    public static ITag i ()                                                         {  return new ITag(); }
    public static ITag i (final String text)                                              {  return new ITag().withText(text); }
    public static ITag i (final DomContent... dc)                                         {  return new ITag().with(dc); }
    public static ITag i (final Attr.ShortForm shortAttr)                                 {  return Attr.addTo( new ITag(), shortAttr); }
    public static ITag i (final Attr.ShortForm shortAttr, final String text)                    {  return Attr.addTo( new ITag().withText(text), shortAttr); }
    public static ITag i (final Attr.ShortForm shortAttr, final DomContent... dc)               {  return Attr.addTo( new ITag().with(dc), shortAttr); }

    public static IframeTag iframe ()                                               {  return new IframeTag(); }
    public static IframeTag iframe (final String text)                                    {  return new IframeTag().withText(text); }
    public static IframeTag iframe (final DomContent... dc)                               {  return new IframeTag().with(dc); }
    public static IframeTag iframe (final Attr.ShortForm shortAttr)                       {  return Attr.addTo( new IframeTag(), shortAttr); }
    public static IframeTag iframe (final Attr.ShortForm shortAttr, final String text)          {  return Attr.addTo( new IframeTag().withText(text), shortAttr); }
    public static IframeTag iframe (final Attr.ShortForm shortAttr, final DomContent... dc)     {  return Attr.addTo( new IframeTag().with(dc), shortAttr); }

    public static InsTag ins ()                                                     {  return new InsTag(); }
    public static InsTag ins (final String text)                                          {  return new InsTag().withText(text); }
    public static InsTag ins (final DomContent... dc)                                     {  return new InsTag().with(dc); }
    public static InsTag ins (final Attr.ShortForm shortAttr)                             {  return Attr.addTo( new InsTag(), shortAttr); }
    public static InsTag ins (final Attr.ShortForm shortAttr, final String text)                {  return Attr.addTo( new InsTag().withText(text), shortAttr); }
    public static InsTag ins (final Attr.ShortForm shortAttr, final DomContent... dc)           {  return Attr.addTo( new InsTag().with(dc), shortAttr); }

    public static KbdTag kbd ()                                                     {  return new KbdTag(); }
    public static KbdTag kbd (final String text)                                          {  return new KbdTag().withText(text); }
    public static KbdTag kbd (final DomContent... dc)                                     {  return new KbdTag().with(dc); }
    public static KbdTag kbd (final Attr.ShortForm shortAttr)                             {  return Attr.addTo( new KbdTag(), shortAttr); }
    public static KbdTag kbd (final Attr.ShortForm shortAttr, final String text)                {  return Attr.addTo( new KbdTag().withText(text), shortAttr); }
    public static KbdTag kbd (final Attr.ShortForm shortAttr, final DomContent... dc)           {  return Attr.addTo( new KbdTag().with(dc), shortAttr); }

    public static LabelTag label ()                                                 {  return new LabelTag(); }
    public static LabelTag label (final String text)                                      {  return new LabelTag().withText(text); }
    public static LabelTag label (final DomContent... dc)                                 {  return new LabelTag().with(dc); }
    public static LabelTag label (final Attr.ShortForm shortAttr)                         {  return Attr.addTo( new LabelTag(), shortAttr); }
    public static LabelTag label (final Attr.ShortForm shortAttr, final String text)            {  return Attr.addTo( new LabelTag().withText(text), shortAttr); }
    public static LabelTag label (final Attr.ShortForm shortAttr, final DomContent... dc)       {  return Attr.addTo( new LabelTag().with(dc), shortAttr); }

    public static LegendTag legend ()                                               {  return new LegendTag(); }
    public static LegendTag legend (final String text)                                    {  return new LegendTag().withText(text); }
    public static LegendTag legend (final DomContent... dc)                               {  return new LegendTag().with(dc); }
    public static LegendTag legend (final Attr.ShortForm shortAttr)                       {  return Attr.addTo( new LegendTag(), shortAttr); }
    public static LegendTag legend (final Attr.ShortForm shortAttr, final String text)          {  return Attr.addTo( new LegendTag().withText(text), shortAttr); }
    public static LegendTag legend (final Attr.ShortForm shortAttr, final DomContent... dc)     {  return Attr.addTo( new LegendTag().with(dc), shortAttr); }

    public static LiTag li ()                                                       {  return new LiTag(); }
    public static LiTag li (final String text)                                            {  return new LiTag().withText(text); }
    public static LiTag li (final DomContent... dc)                                       {  return new LiTag().with(dc); }
    public static LiTag li (final Attr.ShortForm shortAttr)                               {  return Attr.addTo( new LiTag(), shortAttr); }
    public static LiTag li (final Attr.ShortForm shortAttr, final String text)                  {  return Attr.addTo( new LiTag().withText(text), shortAttr); }
    public static LiTag li (final Attr.ShortForm shortAttr, final DomContent... dc)             {  return Attr.addTo( new LiTag().with(dc), shortAttr); }

    public static MainTag main ()                                                   {  return new MainTag(); }
    public static MainTag main (final String text)                                        {  return new MainTag().withText(text); }
    public static MainTag main (final DomContent... dc)                                   {  return new MainTag().with(dc); }
    public static MainTag main (final Attr.ShortForm shortAttr)                           {  return Attr.addTo( new MainTag(), shortAttr); }
    public static MainTag main (final Attr.ShortForm shortAttr, final String text)              {  return Attr.addTo( new MainTag().withText(text), shortAttr); }
    public static MainTag main (final Attr.ShortForm shortAttr, final DomContent... dc)         {  return Attr.addTo( new MainTag().with(dc), shortAttr); }

    public static MapTag map ()                                                     {  return new MapTag(); }
    public static MapTag map (final String text)                                          {  return new MapTag().withText(text); }
    public static MapTag map (final DomContent... dc)                                     {  return new MapTag().with(dc); }
    public static MapTag map (final Attr.ShortForm shortAttr)                             {  return Attr.addTo( new MapTag(), shortAttr); }
    public static MapTag map (final Attr.ShortForm shortAttr, final String text)                {  return Attr.addTo( new MapTag().withText(text), shortAttr); }
    public static MapTag map (final Attr.ShortForm shortAttr, final DomContent... dc)           {  return Attr.addTo( new MapTag().with(dc), shortAttr); }

    public static MarkTag mark ()                                                   {  return new MarkTag(); }
    public static MarkTag mark (final String text)                                        {  return new MarkTag().withText(text); }
    public static MarkTag mark (final DomContent... dc)                                   {  return new MarkTag().with(dc); }
    public static MarkTag mark (final Attr.ShortForm shortAttr)                           {  return Attr.addTo( new MarkTag(), shortAttr); }
    public static MarkTag mark (final Attr.ShortForm shortAttr, final String text)              {  return Attr.addTo( new MarkTag().withText(text), shortAttr); }
    public static MarkTag mark (final Attr.ShortForm shortAttr, final DomContent... dc)         {  return Attr.addTo( new MarkTag().with(dc), shortAttr); }

    public static MenuTag menu ()                                                   {  return new MenuTag(); }
    public static MenuTag menu (final String text)                                        {  return new MenuTag().withText(text); }
    public static MenuTag menu (final DomContent... dc)                                   {  return new MenuTag().with(dc); }
    public static MenuTag menu (final Attr.ShortForm shortAttr)                           {  return Attr.addTo( new MenuTag(), shortAttr); }
    public static MenuTag menu (final Attr.ShortForm shortAttr, final String text)              {  return Attr.addTo( new MenuTag().withText(text), shortAttr); }
    public static MenuTag menu (final Attr.ShortForm shortAttr, final DomContent... dc)         {  return Attr.addTo( new MenuTag().with(dc), shortAttr); }

    public static MenuitemTag menuitem ()                                           {  return new MenuitemTag(); }
    public static MenuitemTag menuitem (final String text)                                {  return new MenuitemTag().withText(text); }
    public static MenuitemTag menuitem (final DomContent... dc)                           {  return new MenuitemTag().with(dc); }
    public static MenuitemTag menuitem (final Attr.ShortForm shortAttr)                   {  return Attr.addTo( new MenuitemTag(), shortAttr); }
    public static MenuitemTag menuitem (final Attr.ShortForm shortAttr, final String text)      {  return Attr.addTo( new MenuitemTag().withText(text), shortAttr); }
    public static MenuitemTag menuitem (final Attr.ShortForm shortAttr, final DomContent... dc) {  return Attr.addTo( new MenuitemTag().with(dc), shortAttr); }

    public static MeterTag meter ()                                                 {  return new MeterTag(); }
    public static MeterTag meter (final String text)                                      {  return new MeterTag().withText(text); }
    public static MeterTag meter (final DomContent... dc)                                 {  return new MeterTag().with(dc); }
    public static MeterTag meter (final Attr.ShortForm shortAttr)                         {  return Attr.addTo( new MeterTag(), shortAttr); }
    public static MeterTag meter (final Attr.ShortForm shortAttr, final String text)            {  return Attr.addTo( new MeterTag().withText(text), shortAttr); }
    public static MeterTag meter (final Attr.ShortForm shortAttr, final DomContent... dc)       {  return Attr.addTo( new MeterTag().with(dc), shortAttr); }

    public static NavTag nav ()                                                     {  return new NavTag(); }
    public static NavTag nav (final String text)                                          {  return new NavTag().withText(text); }
    public static NavTag nav (final DomContent... dc)                                     {  return new NavTag().with(dc); }
    public static NavTag nav (final Attr.ShortForm shortAttr)                             {  return Attr.addTo( new NavTag(), shortAttr); }
    public static NavTag nav (final Attr.ShortForm shortAttr, final String text)                {  return Attr.addTo( new NavTag().withText(text), shortAttr); }
    public static NavTag nav (final Attr.ShortForm shortAttr, final DomContent... dc)           {  return Attr.addTo( new NavTag().with(dc), shortAttr); }

    public static NoscriptTag noscript ()                                           {  return new NoscriptTag(); }
    public static NoscriptTag noscript (final String text)                                {  return new NoscriptTag().withText(text); }
    public static NoscriptTag noscript (final DomContent... dc)                           {  return new NoscriptTag().with(dc); }
    public static NoscriptTag noscript (final Attr.ShortForm shortAttr)                   {  return Attr.addTo( new NoscriptTag(), shortAttr); }
    public static NoscriptTag noscript (final Attr.ShortForm shortAttr, final String text)      {  return Attr.addTo( new NoscriptTag().withText(text), shortAttr); }
    public static NoscriptTag noscript (final Attr.ShortForm shortAttr, final DomContent... dc) {  return Attr.addTo( new NoscriptTag().with(dc), shortAttr); }

    public static ObjectTag object ()                                               {  return new ObjectTag(); }
    public static ObjectTag object (final String text)                                    {  return new ObjectTag().withText(text); }
    public static ObjectTag object (final DomContent... dc)                               {  return new ObjectTag().with(dc); }
    public static ObjectTag object (final Attr.ShortForm shortAttr)                       {  return Attr.addTo( new ObjectTag(), shortAttr); }
    public static ObjectTag object (final Attr.ShortForm shortAttr, final String text)          {  return Attr.addTo( new ObjectTag().withText(text), shortAttr); }
    public static ObjectTag object (final Attr.ShortForm shortAttr, final DomContent... dc)     {  return Attr.addTo( new ObjectTag().with(dc), shortAttr); }

    public static OlTag ol ()                                                       {  return new OlTag(); }
    public static OlTag ol (final String text)                                            {  return new OlTag().withText(text); }
    public static OlTag ol (final DomContent... dc)                                       {  return new OlTag().with(dc); }
    public static OlTag ol (final Attr.ShortForm shortAttr)                               {  return Attr.addTo( new OlTag(), shortAttr); }
    public static OlTag ol (final Attr.ShortForm shortAttr, final String text)                  {  return Attr.addTo( new OlTag().withText(text), shortAttr); }
    public static OlTag ol (final Attr.ShortForm shortAttr, final DomContent... dc)             {  return Attr.addTo( new OlTag().with(dc), shortAttr); }

    public static OptgroupTag optgroup ()                                           {  return new OptgroupTag(); }
    public static OptgroupTag optgroup (final String text)                                {  return new OptgroupTag().withText(text); }
    public static OptgroupTag optgroup (final DomContent... dc)                           {  return new OptgroupTag().with(dc); }
    public static OptgroupTag optgroup (final Attr.ShortForm shortAttr)                   {  return Attr.addTo( new OptgroupTag(), shortAttr); }
    public static OptgroupTag optgroup (final Attr.ShortForm shortAttr, final String text)      {  return Attr.addTo( new OptgroupTag().withText(text), shortAttr); }
    public static OptgroupTag optgroup (final Attr.ShortForm shortAttr, final DomContent... dc) {  return Attr.addTo( new OptgroupTag().with(dc), shortAttr); }

    public static OptionTag option ()                                               {  return new OptionTag(); }
    public static OptionTag option (final String text)                                    {  return new OptionTag().withText(text); }
    public static OptionTag option (final DomContent... dc)                               {  return new OptionTag().with(dc); }
    public static OptionTag option (final Attr.ShortForm shortAttr)                       {  return Attr.addTo( new OptionTag(), shortAttr); }
    public static OptionTag option (final Attr.ShortForm shortAttr, final String text)          {  return Attr.addTo( new OptionTag().withText(text), shortAttr); }
    public static OptionTag option (final Attr.ShortForm shortAttr, final DomContent... dc)     {  return Attr.addTo( new OptionTag().with(dc), shortAttr); }

    public static OutputTag output ()                                               {  return new OutputTag(); }
    public static OutputTag output (final String text)                                    {  return new OutputTag().withText(text); }
    public static OutputTag output (final DomContent... dc)                               {  return new OutputTag().with(dc); }
    public static OutputTag output (final Attr.ShortForm shortAttr)                       {  return Attr.addTo( new OutputTag(), shortAttr); }
    public static OutputTag output (final Attr.ShortForm shortAttr, final String text)          {  return Attr.addTo( new OutputTag().withText(text), shortAttr); }
    public static OutputTag output (final Attr.ShortForm shortAttr, final DomContent... dc)     {  return Attr.addTo( new OutputTag().with(dc), shortAttr); }

    public static PTag p ()                                                         {  return new PTag(); }
    public static PTag p (final String text)                                              {  return new PTag().withText(text); }
    public static PTag p (final DomContent... dc)                                         {  return new PTag().with(dc); }
    public static PTag p (final Attr.ShortForm shortAttr)                                 {  return Attr.addTo( new PTag(), shortAttr); }
    public static PTag p (final Attr.ShortForm shortAttr, final String text)                    {  return Attr.addTo( new PTag().withText(text), shortAttr); }
    public static PTag p (final Attr.ShortForm shortAttr, final DomContent... dc)               {  return Attr.addTo( new PTag().with(dc), shortAttr); }

    public static PictureTag picture ()                                             {  return new PictureTag(); }
    public static PictureTag picture (final String text)                                  {  return new PictureTag().withText(text); }
    public static PictureTag picture (final DomContent... dc)                             {  return new PictureTag().with(dc); }
    public static PictureTag picture (final Attr.ShortForm shortAttr)                     {  return Attr.addTo( new PictureTag(), shortAttr); }
    public static PictureTag picture (final Attr.ShortForm shortAttr, final String text)        {  return Attr.addTo( new PictureTag().withText(text), shortAttr); }
    public static PictureTag picture (final Attr.ShortForm shortAttr, final DomContent... dc)   {  return Attr.addTo( new PictureTag().with(dc), shortAttr); }

    public static PreTag pre ()                                                     {  return new PreTag(); }
    public static PreTag pre (final String text)                                          {  return new PreTag().withText(text); }
    public static PreTag pre (final DomContent... dc)                                     {  return new PreTag().with(dc); }
    public static PreTag pre (final Attr.ShortForm shortAttr)                             {  return Attr.addTo( new PreTag(), shortAttr); }
    public static PreTag pre (final Attr.ShortForm shortAttr, final String text)                {  return Attr.addTo( new PreTag().withText(text), shortAttr); }
    public static PreTag pre (final Attr.ShortForm shortAttr, final DomContent... dc)           {  return Attr.addTo( new PreTag().with(dc), shortAttr); }

    public static ProgressTag progress ()                                           {  return new ProgressTag(); }
    public static ProgressTag progress (final String text)                                {  return new ProgressTag().withText(text); }
    public static ProgressTag progress (final DomContent... dc)                           {  return new ProgressTag().with(dc); }
    public static ProgressTag progress (final Attr.ShortForm shortAttr)                   {  return Attr.addTo( new ProgressTag(), shortAttr); }
    public static ProgressTag progress (final Attr.ShortForm shortAttr, final String text)      {  return Attr.addTo( new ProgressTag().withText(text), shortAttr); }
    public static ProgressTag progress (final Attr.ShortForm shortAttr, final DomContent... dc) {  return Attr.addTo( new ProgressTag().with(dc), shortAttr); }

    public static QTag q ()                                                         {  return new QTag(); }
    public static QTag q (final String text)                                              {  return new QTag().withText(text); }
    public static QTag q (final DomContent... dc)                                         {  return new QTag().with(dc); }
    public static QTag q (final Attr.ShortForm shortAttr)                                 {  return Attr.addTo( new QTag(), shortAttr); }
    public static QTag q (final Attr.ShortForm shortAttr, final String text)                    {  return Attr.addTo( new QTag().withText(text), shortAttr); }
    public static QTag q (final Attr.ShortForm shortAttr, final DomContent... dc)               {  return Attr.addTo( new QTag().with(dc), shortAttr); }

    public static RpTag rp ()                                                       {  return new RpTag(); }
    public static RpTag rp (final String text)                                            {  return new RpTag().withText(text); }
    public static RpTag rp (final DomContent... dc)                                       {  return new RpTag().with(dc); }
    public static RpTag rp (final Attr.ShortForm shortAttr)                               {  return Attr.addTo( new RpTag(), shortAttr); }
    public static RpTag rp (final Attr.ShortForm shortAttr, final String text)                  {  return Attr.addTo( new RpTag().withText(text), shortAttr); }
    public static RpTag rp (final Attr.ShortForm shortAttr, final DomContent... dc)             {  return Attr.addTo( new RpTag().with(dc), shortAttr); }

    public static RtTag rt ()                                                       {  return new RtTag(); }
    public static RtTag rt (final String text)                                            {  return new RtTag().withText(text); }
    public static RtTag rt (final DomContent... dc)                                       {  return new RtTag().with(dc); }
    public static RtTag rt (final Attr.ShortForm shortAttr)                               {  return Attr.addTo( new RtTag(), shortAttr); }
    public static RtTag rt (final Attr.ShortForm shortAttr, final String text)                  {  return Attr.addTo( new RtTag().withText(text), shortAttr); }
    public static RtTag rt (final Attr.ShortForm shortAttr, final DomContent... dc)             {  return Attr.addTo( new RtTag().with(dc), shortAttr); }

    public static RubyTag ruby ()                                                   {  return new RubyTag(); }
    public static RubyTag ruby (final String text)                                        {  return new RubyTag().withText(text); }
    public static RubyTag ruby (final DomContent... dc)                                   {  return new RubyTag().with(dc); }
    public static RubyTag ruby (final Attr.ShortForm shortAttr)                           {  return Attr.addTo( new RubyTag(), shortAttr); }
    public static RubyTag ruby (final Attr.ShortForm shortAttr, final String text)              {  return Attr.addTo( new RubyTag().withText(text), shortAttr); }
    public static RubyTag ruby (final Attr.ShortForm shortAttr, final DomContent... dc)         {  return Attr.addTo( new RubyTag().with(dc), shortAttr); }

    public static STag s ()                                                         {  return new STag(); }
    public static STag s (final String text)                                              {  return new STag().withText(text); }
    public static STag s (final DomContent... dc)                                         {  return new STag().with(dc); }
    public static STag s (final Attr.ShortForm shortAttr)                                 {  return Attr.addTo( new STag(), shortAttr); }
    public static STag s (final Attr.ShortForm shortAttr, final String text)                    {  return Attr.addTo( new STag().withText(text), shortAttr); }
    public static STag s (final Attr.ShortForm shortAttr, final DomContent... dc)               {  return Attr.addTo( new STag().with(dc), shortAttr); }

    public static SampTag samp ()                                                   {  return new SampTag(); }
    public static SampTag samp (final String text)                                        {  return new SampTag().withText(text); }
    public static SampTag samp (final DomContent... dc)                                   {  return new SampTag().with(dc); }
    public static SampTag samp (final Attr.ShortForm shortAttr)                           {  return Attr.addTo( new SampTag(), shortAttr); }
    public static SampTag samp (final Attr.ShortForm shortAttr, final String text)              {  return Attr.addTo( new SampTag().withText(text), shortAttr); }
    public static SampTag samp (final Attr.ShortForm shortAttr, final DomContent... dc)         {  return Attr.addTo( new SampTag().with(dc), shortAttr); }

    public static ScriptTag script ()                                               {  return new ScriptTag(); }
    public static ScriptTag script (final String text)                                    {  return new ScriptTag().with(new UnescapedText(text)); }
    public static ScriptTag script (final DomContent... dc)                               {  return new ScriptTag().with(dc); }
    public static ScriptTag script (final Attr.ShortForm shortAttr)                       {  return Attr.addTo( new ScriptTag(), shortAttr); }
    public static ScriptTag script (final Attr.ShortForm shortAttr, final String text)          {  return Attr.addTo( new ScriptTag().with(new UnescapedText(text)), shortAttr); }
    public static ScriptTag script (final Attr.ShortForm shortAttr, final DomContent... dc)     {  return Attr.addTo( new ScriptTag().with(dc), shortAttr); }

    public static SectionTag section ()                                             {  return new SectionTag(); }
    public static SectionTag section (final String text)                                  {  return new SectionTag().withText(text); }
    public static SectionTag section (final DomContent... dc)                             {  return new SectionTag().with(dc); }
    public static SectionTag section (final Attr.ShortForm shortAttr)                     {  return Attr.addTo( new SectionTag(), shortAttr); }
    public static SectionTag section (final Attr.ShortForm shortAttr, final String text)        {  return Attr.addTo( new SectionTag().withText(text), shortAttr); }
    public static SectionTag section (final Attr.ShortForm shortAttr, final DomContent... dc)   {  return Attr.addTo( new SectionTag().with(dc), shortAttr); }

    public static SelectTag select ()                                               {  return new SelectTag(); }
    public static SelectTag select (final String text)                                    {  return new SelectTag().withText(text); }
    public static SelectTag select (final DomContent... dc)                               {  return new SelectTag().with(dc); }
    public static SelectTag select (final Attr.ShortForm shortAttr)                       {  return Attr.addTo( new SelectTag(), shortAttr); }
    public static SelectTag select (final Attr.ShortForm shortAttr, final String text)          {  return Attr.addTo( new SelectTag().withText(text), shortAttr); }
    public static SelectTag select (final Attr.ShortForm shortAttr, final DomContent... dc)     {  return Attr.addTo( new SelectTag().with(dc), shortAttr); }

    public static SlotTag slot ()                                                 {  return new SlotTag(); }
    public static SlotTag slot (final String text)                                      {  return new SlotTag().withText(text); }
    public static SlotTag slot (final DomContent... dc)                                 {  return new SlotTag().with(dc); }
    public static SlotTag slot (final Attr.ShortForm shortAttr)                         {  return Attr.addTo( new SlotTag(), shortAttr); }
    public static SlotTag slot (final Attr.ShortForm shortAttr, final String text)            {  return Attr.addTo( new SlotTag().withText(text), shortAttr); }
    public static SlotTag slot (final Attr.ShortForm shortAttr, final DomContent... dc)       {  return Attr.addTo( new SlotTag().with(dc), shortAttr); }

    public static SmallTag small ()                                                 {  return new SmallTag(); }
    public static SmallTag small (final String text)                                      {  return new SmallTag().withText(text); }
    public static SmallTag small (final DomContent... dc)                                 {  return new SmallTag().with(dc); }
    public static SmallTag small (final Attr.ShortForm shortAttr)                         {  return Attr.addTo( new SmallTag(), shortAttr); }
    public static SmallTag small (final Attr.ShortForm shortAttr, final String text)            {  return Attr.addTo( new SmallTag().withText(text), shortAttr); }
    public static SmallTag small (final Attr.ShortForm shortAttr, final DomContent... dc)       {  return Attr.addTo( new SmallTag().with(dc), shortAttr); }

    public static SpanTag span ()                                                   {  return new SpanTag(); }
    public static SpanTag span (final String text)                                        {  return new SpanTag().withText(text); }
    public static SpanTag span (final DomContent... dc)                                   {  return new SpanTag().with(dc); }
    public static SpanTag span (final Attr.ShortForm shortAttr)                           {  return Attr.addTo( new SpanTag(), shortAttr); }
    public static SpanTag span (final Attr.ShortForm shortAttr, final String text)              {  return Attr.addTo( new SpanTag().withText(text), shortAttr); }
    public static SpanTag span (final Attr.ShortForm shortAttr, final DomContent... dc)         {  return Attr.addTo( new SpanTag().with(dc), shortAttr); }

    public static StrongTag strong ()                                               {  return new StrongTag(); }
    public static StrongTag strong (final String text)                                    {  return new StrongTag().withText(text); }
    public static StrongTag strong (final DomContent... dc)                               {  return new StrongTag().with(dc); }
    public static StrongTag strong (final Attr.ShortForm shortAttr)                       {  return Attr.addTo( new StrongTag(), shortAttr); }
    public static StrongTag strong (final Attr.ShortForm shortAttr, final String text)          {  return Attr.addTo( new StrongTag().withText(text), shortAttr); }
    public static StrongTag strong (final Attr.ShortForm shortAttr, final DomContent... dc)     {  return Attr.addTo( new StrongTag().with(dc), shortAttr); }

    public static StyleTag style ()                                                 {  return new StyleTag(); }
    public static StyleTag style (final String text)                                      {  return new StyleTag().with(new UnescapedText(text)); }
    public static StyleTag style (final DomContent... dc)                                 {  return new StyleTag().with(dc); }
    public static StyleTag style (final Attr.ShortForm shortAttr)                         {  return Attr.addTo( new StyleTag(), shortAttr); }
    public static StyleTag style (final Attr.ShortForm shortAttr, final String text)            {  return Attr.addTo( new StyleTag().with(new UnescapedText(text)), shortAttr); }
    public static StyleTag style (final Attr.ShortForm shortAttr, final DomContent... dc)       {  return Attr.addTo( new StyleTag().with(dc), shortAttr); }

    public static SubTag sub ()                                                     {  return new SubTag(); }
    public static SubTag sub (final String text)                                          {  return new SubTag().withText(text); }
    public static SubTag sub (final DomContent... dc)                                     {  return new SubTag().with(dc); }
    public static SubTag sub (final Attr.ShortForm shortAttr)                             {  return Attr.addTo( new SubTag(), shortAttr); }
    public static SubTag sub (final Attr.ShortForm shortAttr, final String text)                {  return Attr.addTo( new SubTag().withText(text), shortAttr); }
    public static SubTag sub (final Attr.ShortForm shortAttr, final DomContent... dc)           {  return Attr.addTo( new SubTag().with(dc), shortAttr); }

    public static SummaryTag summary ()                                             {  return new SummaryTag(); }
    public static SummaryTag summary (final String text)                                  {  return new SummaryTag().withText(text); }
    public static SummaryTag summary (final DomContent... dc)                             {  return new SummaryTag().with(dc); }
    public static SummaryTag summary (final Attr.ShortForm shortAttr)                     {  return Attr.addTo( new SummaryTag(), shortAttr); }
    public static SummaryTag summary (final Attr.ShortForm shortAttr, final String text)        {  return Attr.addTo( new SummaryTag().withText(text), shortAttr); }
    public static SummaryTag summary (final Attr.ShortForm shortAttr, final DomContent... dc)   {  return Attr.addTo( new SummaryTag().with(dc), shortAttr); }

    public static SupTag sup ()                                                     {  return new SupTag(); }
    public static SupTag sup (final String text)                                          {  return new SupTag().withText(text); }
    public static SupTag sup (final DomContent... dc)                                     {  return new SupTag().with(dc); }
    public static SupTag sup (final Attr.ShortForm shortAttr)                             {  return Attr.addTo( new SupTag(), shortAttr); }
    public static SupTag sup (final Attr.ShortForm shortAttr, final String text)                {  return Attr.addTo( new SupTag().withText(text), shortAttr); }
    public static SupTag sup (final Attr.ShortForm shortAttr, final DomContent... dc)           {  return Attr.addTo( new SupTag().with(dc), shortAttr); }

    public static TableTag table ()                                                 {  return new TableTag(); }
    public static TableTag table (final String text)                                      {  return new TableTag().withText(text); }
    public static TableTag table (final DomContent... dc)                                 {  return new TableTag().with(dc); }
    public static TableTag table (final Attr.ShortForm shortAttr)                         {  return Attr.addTo( new TableTag(), shortAttr); }
    public static TableTag table (final Attr.ShortForm shortAttr, final String text)            {  return Attr.addTo( new TableTag().withText(text), shortAttr); }
    public static TableTag table (final Attr.ShortForm shortAttr, final DomContent... dc)       {  return Attr.addTo( new TableTag().with(dc), shortAttr); }

    public static TbodyTag tbody ()                                                 {  return new TbodyTag(); }
    public static TbodyTag tbody (final String text)                                      {  return new TbodyTag().withText(text); }
    public static TbodyTag tbody (final DomContent... dc)                                 {  return new TbodyTag().with(dc); }
    public static TbodyTag tbody (final Attr.ShortForm shortAttr)                         {  return Attr.addTo( new TbodyTag(), shortAttr); }
    public static TbodyTag tbody (final Attr.ShortForm shortAttr, final String text)            {  return Attr.addTo( new TbodyTag().withText(text), shortAttr); }
    public static TbodyTag tbody (final Attr.ShortForm shortAttr, final DomContent... dc)       {  return Attr.addTo( new TbodyTag().with(dc), shortAttr); }

    public static TdTag td ()                                                       {  return new TdTag(); }
    public static TdTag td (final String text)                                            {  return new TdTag().withText(text); }
    public static TdTag td (final DomContent... dc)                                       {  return new TdTag().with(dc); }
    public static TdTag td (final Attr.ShortForm shortAttr)                               {  return Attr.addTo( new TdTag(), shortAttr); }
    public static TdTag td (final Attr.ShortForm shortAttr, final String text)                  {  return Attr.addTo( new TdTag().withText(text), shortAttr); }
    public static TdTag td (final Attr.ShortForm shortAttr, final DomContent... dc)             {  return Attr.addTo( new TdTag().with(dc), shortAttr); }

    public static TemplateTag template ()                                           {  return new TemplateTag(); }
    public static TemplateTag template (final String text)                                {  return new TemplateTag().withText(text); }
    public static TemplateTag template (final DomContent... dc)                           {  return new TemplateTag().with(dc); }
    public static TemplateTag template (final Attr.ShortForm shortAttr)                   {  return Attr.addTo( new TemplateTag(), shortAttr); }
    public static TemplateTag template (final Attr.ShortForm shortAttr, final String text)      {  return Attr.addTo( new TemplateTag().withText(text), shortAttr); }
    public static TemplateTag template (final Attr.ShortForm shortAttr, final DomContent... dc) {  return Attr.addTo( new TemplateTag().with(dc), shortAttr); }

    public static TextareaTag textarea ()                                           {  return new TextareaTag(); }
    public static TextareaTag textarea (final String text)                                {  return new TextareaTag().withText(text); }
    public static TextareaTag textarea (final DomContent... dc)                           {  return new TextareaTag().with(dc); }
    public static TextareaTag textarea (final Attr.ShortForm shortAttr)                   {  return Attr.addTo( new TextareaTag(), shortAttr); }
    public static TextareaTag textarea (final Attr.ShortForm shortAttr, final String text)      {  return Attr.addTo( new TextareaTag().withText(text), shortAttr); }
    public static TextareaTag textarea (final Attr.ShortForm shortAttr, final DomContent... dc) {  return Attr.addTo( new TextareaTag().with(dc), shortAttr); }

    public static TfootTag tfoot ()                                                 {  return new TfootTag(); }
    public static TfootTag tfoot (final String text)                                      {  return new TfootTag().withText(text); }
    public static TfootTag tfoot (final DomContent... dc)                                 {  return new TfootTag().with(dc); }
    public static TfootTag tfoot (final Attr.ShortForm shortAttr)                         {  return Attr.addTo( new TfootTag(), shortAttr); }
    public static TfootTag tfoot (final Attr.ShortForm shortAttr, final String text)            {  return Attr.addTo( new TfootTag().withText(text), shortAttr); }
    public static TfootTag tfoot (final Attr.ShortForm shortAttr, final DomContent... dc)       {  return Attr.addTo( new TfootTag().with(dc), shortAttr); }

    public static ThTag th ()                                                       {  return new ThTag(); }
    public static ThTag th (final String text)                                            {  return new ThTag().withText(text); }
    public static ThTag th (final DomContent... dc)                                       {  return new ThTag().with(dc); }
    public static ThTag th (final Attr.ShortForm shortAttr)                               {  return Attr.addTo( new ThTag(), shortAttr); }
    public static ThTag th (final Attr.ShortForm shortAttr, final String text)                  {  return Attr.addTo( new ThTag().withText(text), shortAttr); }
    public static ThTag th (final Attr.ShortForm shortAttr, final DomContent... dc)             {  return Attr.addTo( new ThTag().with(dc), shortAttr); }

    public static TheadTag thead ()                                                 {  return new TheadTag(); }
    public static TheadTag thead (final String text)                                      {  return new TheadTag().withText(text); }
    public static TheadTag thead (final DomContent... dc)                                 {  return new TheadTag().with(dc); }
    public static TheadTag thead (final Attr.ShortForm shortAttr)                         {  return Attr.addTo( new TheadTag(), shortAttr); }
    public static TheadTag thead (final Attr.ShortForm shortAttr, final String text)            {  return Attr.addTo( new TheadTag().withText(text), shortAttr); }
    public static TheadTag thead (final Attr.ShortForm shortAttr, final DomContent... dc)       {  return Attr.addTo( new TheadTag().with(dc), shortAttr); }

    public static TimeTag time ()                                                   {  return new TimeTag(); }
    public static TimeTag time (final String text)                                        {  return new TimeTag().withText(text); }
    public static TimeTag time (final DomContent... dc)                                   {  return new TimeTag().with(dc); }
    public static TimeTag time (final Attr.ShortForm shortAttr)                           {  return Attr.addTo( new TimeTag(), shortAttr); }
    public static TimeTag time (final Attr.ShortForm shortAttr, final String text)              {  return Attr.addTo( new TimeTag().withText(text), shortAttr); }
    public static TimeTag time (final Attr.ShortForm shortAttr, final DomContent... dc)         {  return Attr.addTo( new TimeTag().with(dc), shortAttr); }

    public static TitleTag title ()                                                 {  return new TitleTag(); }
    public static TitleTag title (final String text)                                      {  return new TitleTag().withText(text); }
    public static TitleTag title (final DomContent... dc)                                 {  return new TitleTag().with(dc); }
    public static TitleTag title (final Attr.ShortForm shortAttr)                         {  return Attr.addTo( new TitleTag(), shortAttr); }
    public static TitleTag title (final Attr.ShortForm shortAttr, final String text)            {  return Attr.addTo( new TitleTag().withText(text), shortAttr); }
    public static TitleTag title (final Attr.ShortForm shortAttr, final DomContent... dc)       {  return Attr.addTo( new TitleTag().with(dc), shortAttr); }

    public static TrTag tr ()                                                       {  return new TrTag(); }
    public static TrTag tr (final String text)                                            {  return new TrTag().withText(text); }
    public static TrTag tr (final DomContent... dc)                                       {  return new TrTag().with(dc); }
    public static TrTag tr (final Attr.ShortForm shortAttr)                               {  return Attr.addTo( new TrTag(), shortAttr); }
    public static TrTag tr (final Attr.ShortForm shortAttr, final String text)                  {  return Attr.addTo( new TrTag().withText(text), shortAttr); }
    public static TrTag tr (final Attr.ShortForm shortAttr, final DomContent... dc)             {  return Attr.addTo( new TrTag().with(dc), shortAttr); }

    public static UTag u ()                                                         {  return new UTag(); }
    public static UTag u (final String text)                                              {  return new UTag().withText(text); }
    public static UTag u (final DomContent... dc)                                         {  return new UTag().with(dc); }
    public static UTag u (final Attr.ShortForm shortAttr)                                 {  return Attr.addTo( new UTag(), shortAttr); }
    public static UTag u (final Attr.ShortForm shortAttr, final String text)                    {  return Attr.addTo( new UTag().withText(text), shortAttr); }
    public static UTag u (final Attr.ShortForm shortAttr, final DomContent... dc)               {  return Attr.addTo( new UTag().with(dc), shortAttr); }

    public static UlTag ul ()                                                       {  return new UlTag(); }
    public static UlTag ul (final String text)                                            {  return new UlTag().withText(text); }
    public static UlTag ul (final DomContent... dc)                                       {  return new UlTag().with(dc); }
    public static UlTag ul (final Attr.ShortForm shortAttr)                               {  return Attr.addTo( new UlTag(), shortAttr); }
    public static UlTag ul (final Attr.ShortForm shortAttr, final String text)                  {  return Attr.addTo( new UlTag().withText(text), shortAttr); }
    public static UlTag ul (final Attr.ShortForm shortAttr, final DomContent... dc)             {  return Attr.addTo( new UlTag().with(dc), shortAttr); }

    public static VarTag var ()                                                     {  return new VarTag(); }
    public static VarTag var (final String text)                                          {  return new VarTag().withText(text); }
    public static VarTag var (final DomContent... dc)                                     {  return new VarTag().with(dc); }
    public static VarTag var (final Attr.ShortForm shortAttr)                             {  return Attr.addTo( new VarTag(), shortAttr); }
    public static VarTag var (final Attr.ShortForm shortAttr, final String text)                {  return Attr.addTo( new VarTag().withText(text), shortAttr); }
    public static VarTag var (final Attr.ShortForm shortAttr, final DomContent... dc)           {  return Attr.addTo( new VarTag().with(dc), shortAttr); }

    public static VideoTag video ()                                                 {  return new VideoTag(); }
    public static VideoTag video (final String text)                                      {  return new VideoTag().withText(text); }
    public static VideoTag video (final DomContent... dc)                                 {  return new VideoTag().with(dc); }
    public static VideoTag video (final Attr.ShortForm shortAttr)                         {  return Attr.addTo( new VideoTag(), shortAttr); }
    public static VideoTag video (final Attr.ShortForm shortAttr, final String text)            {  return Attr.addTo( new VideoTag().withText(text), shortAttr); }
    public static VideoTag video (final Attr.ShortForm shortAttr, final DomContent... dc)       {  return Attr.addTo( new VideoTag().with(dc), shortAttr); }

    // pico.css convenience methods
    /**
     * Creates a {@code <link>} stylesheet pointing to the default CDN Pico CSS v2.
     *
     * @return A LinkTag referencing the Pico CSS stylesheet
     */
    public static LinkTag picoCss() {
        return Pico.css();
    }

    /**
     * Creates a {@code <link>} stylesheet pointing to the CDN Pico CSS with a specified version.
     *
     * @param version The Pico CSS version
     * @return A LinkTag referencing the versioned Pico CSS stylesheet
     */
    public static LinkTag picoCss(final String version) {
        return Pico.css(version);
    }

    /**
     * Creates a {@code <link>} stylesheet pointing to the CDN Pico CSS with a specified version and color scheme name.
     *
     * @param version The Pico CSS version
     * @param colorScheme The color scheme name
     * @return A LinkTag referencing the themed Pico CSS stylesheet
     */
    public static LinkTag picoCss(final String version, final String colorScheme) {
        return Pico.css(version, colorScheme);
    }

    /**
     * Creates a {@code <link>} stylesheet pointing to the CDN Pico CSS with a specified color scheme.
     *
     * @param colorScheme The color scheme palette
     * @return A LinkTag referencing the themed Pico CSS stylesheet
     */
    public static LinkTag picoCss(final Pico.Color colorScheme) {
        return Pico.css(colorScheme);
    }

    /**
     * Creates a {@code <link>} stylesheet pointing to the CDN Pico CSS with a specified version and color scheme.
     *
     * @param version The Pico CSS version
     * @param colorScheme The color scheme palette
     * @return A LinkTag referencing the versioned and themed Pico CSS stylesheet
     */
    public static LinkTag picoCss(final String version, final Pico.Color colorScheme) {
        return Pico.css(version, colorScheme);
    }

    /**
     * Loads the bundled minified Pico CSS stylesheet from resources as an inline {@code <style>} element.
     *
     * @return A ContainerTag containing the inlined CSS
     */
    public static ContainerTag<?> picoInlineCss() {
        return Pico.inlineCss();
    }

    /**
     * Creates a centered, responsive container {@code <main class="container">}.
     *
     * @param content Elements inside the container
     * @return A MainTag with the container class
     */
    public static MainTag container(final DomContent... content) {
        return Pico.container(content);
    }

    /**
     * Creates a centered, responsive container {@code <main class="container">} with text content.
     *
     * @param text Text inside the container
     * @return A MainTag with the container class
     */
    public static MainTag container(final String text) {
        return Pico.container(text);
    }

    /**
     * Creates a fluid, full-width container {@code <main class="container-fluid">}.
     *
     * @param content Elements inside the fluid container
     * @return A MainTag with the container-fluid class
     */
    public static MainTag containerFluid(final DomContent... content) {
        return Pico.containerFluid(content);
    }

    /**
     * Creates a fluid, full-width container {@code <main class="container-fluid">} with text content.
     *
     * @param text Text inside the fluid container
     * @return A MainTag with the container-fluid class
     */
    public static MainTag containerFluid(final String text) {
        return Pico.containerFluid(text);
    }

    /**
     * Creates a responsive grid layout {@code <div class="grid">}.
     *
     * @param content Elements inside the grid
     * @return A DivTag with the grid class
     */
    public static DivTag grid(final DomContent... content) {
        return Pico.grid(content);
    }

    /**
     * Creates a responsive grid layout {@code <div class="grid">} with text content.
     *
     * @param text Text inside the grid
     * @return A DivTag with the grid class
     */
    public static DivTag grid(final String text) {
        return Pico.grid(text);
    }

}
