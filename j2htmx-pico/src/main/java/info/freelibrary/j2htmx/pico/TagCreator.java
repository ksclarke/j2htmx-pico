package info.freelibrary.j2htmx.pico;

import info.freelibrary.j2htmx.pico.attributes.Attr;
import info.freelibrary.j2htmx.pico.tags.AbstractDomContent;
import info.freelibrary.j2htmx.pico.tags.AbstractTag;
import info.freelibrary.j2htmx.pico.tags.ContainerTag;
import info.freelibrary.j2htmx.pico.tags.DomContentJoiner;
import info.freelibrary.j2htmx.pico.tags.EmptyTag;
import info.freelibrary.j2htmx.pico.tags.InlineStaticResource;
import info.freelibrary.j2htmx.pico.tags.Text;
import info.freelibrary.j2htmx.pico.tags.UnescapedText;
import info.freelibrary.j2htmx.pico.tags.specialized.*;
import info.freelibrary.util.warnings.PMD;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Primary factory class providing static builder methods to construct HTML, HTMX, and Pico CSS elements.
 */
@SuppressWarnings(
  {PMD.SHORT_METHOD_NAME, PMD.COUPLING_BETWEEN_OBJECTS, PMD.CYCLOMATIC_COMPLEXITY, PMD.TOO_MANY_METHODS,
    PMD.EXCESSIVE_PUBLIC_COUNT})
public final class TagCreator {

    /**
     * Constructs a new TagCreator instance.
     */
    private TagCreator() {
        // This is intentionally left empty
    }

    /**
     * Generic if-expression to do if'ing inside method calls.
     *
     * @param <T> The derived generic parameter type
     * @param condition the condition to if-on
     * @param ifValue the value to return if condition is true
     * @return value if condition is true, null otherwise
     */
    public static <T> T iff(final boolean condition, final T ifValue) {
        return condition ? ifValue : null;
    }

    /**
     * Generic if-expression to if'ing inside method calls.
     *
     * @param optional The item that may be present
     * @param ifFunction The function that will be called if that optional is present
     * @param <T> The derived generic parameter type
     * @param <U> The supplying generic parameter type
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
     * Like {@link #iff}, but returns else-value instead of null.
     *
     * @param <T> The value type
     * @param condition The condition to check
     * @param ifValue The value to return if condition is true
     * @param elseValue The value to return if condition is false
     * @return The ifValue if condition is true, otherwise the elseValue
     */
    public static <T> T iffElse(final boolean condition, final T ifValue, final T elseValue) {
        return condition ? ifValue : elseValue;
    }

    /**
     * Returns a Attr.ShortForm object with either id, classes or both, obtained from parsing the input string using css
     * selector syntax.
     *
     * @param attrs the string with shortform attributes, only id and class is supported
     * @return a Attr.ShortForm object
     */
    public static Attr.ShortForm attrs(final String attrs) {
        return Attr.ShortForm.parse(attrs);
    }

    /**
     * Returns the HTML created by concatenating the input elements, separated by space, in encounter order. Also
     * removes spaces before periods and commas.
     *
     * @param stringOrDomObjects the elements to join
     * @return joined elements as HTML
     */
    public static UnescapedText join(final Object... stringOrDomObjects) {
        return DomContentJoiner.join(" ", true, stringOrDomObjects);
    }

    /**
     * Creates a group of AbstractDomContent objects (that may be used with {@link #iff(boolean, Object)} or returned from a
     * method call).
     *
     * @param contents the AbstractDomContent elements
     * @return AbstractDomContent containing the given elements
     */
    public static AbstractDomContent each(final AbstractDomContent... contents) {
        return new ContainerTag<>(null).with(contents);
    }

    /**
     * Creates a AbstractDomContent object containing HTML elements from a stream. Intended usage:
     * {@literal each(numbers.stream().map(n -> li(n.toString())))}
     *
     * @param stream the stream of AbstractDomContent elements
     * @return AbstractDomContent containing elements from the stream
     */
    public static AbstractDomContent each(final Stream<AbstractDomContent> stream) {
        return new ContainerTag<>(null).with(stream);
    }

    /**
     * Creates a AbstractDomContent object containing HTML using a mapping function on a collection. Intended usage:
     * {@literal each(numbers, n -> li(n.toString()))}
     *
     * @param <T> The derived generic parameter type
     * @param collection the collection to iterate over, ex: a list of values "1, 2, 3"
     * @param mapper the mapping function, ex: {@literal "n -> li(n.toString())"}
     * @return AbstractDomContent containing mapped data {@literal (ex. docs: [li(1), li(2), li(3)])}
     */
    public static <T> AbstractDomContent each(final Collection<T> collection, final Function<? super T, AbstractDomContent> mapper) {
        return tag(null).with(collection.stream().map(mapper));
    }

    /**
     * Creates a AbstractDomContent object containing HTML using a mapping BiFunction on a collection. Intended usage:
     * {@literal each(names, (index, name) -> li(index + " " + name))}
     *
     * @param <T> The derived generic parameter type
     * @param collection the collection to iterate over, ex: a list of values [ "Tom", "Dick", "Harry" ]
     * @param mapper the mapping BiFunction, ex: {@literal "(index, name) -> li(index + " " + name)"}
     * @return AbstractDomContent containing mapped data {@literal (ex. docs: [li(0 Tom), li(1 Dick), li(2 Harry)])}
     */
    public static <T> AbstractDomContent each(final Collection<T> collection,
      final BiFunction<Integer, ? super T, AbstractDomContent> mapper) {
        final ContainerTag<?> dom = tag(null);
        int i = 0;
        for (final T t : collection) {
            dom.with(mapper.apply(i++, t));
        }
        return dom;
    }

    /**
     * Creates a AbstractDomContent object containing HTML using a mapping function on map entries.
     *
     * @param <I> The type of the keys
     * @param <T> The type of the values
     * @param map The map to iterate over
     * @param mapper The mapping function applied to each map entry
     * @return AbstractDomContent containing the mapped elements
     */
    public static <I, T> AbstractDomContent each(final Map<I, T> map, final Function<Entry<I, T>, AbstractDomContent> mapper) {
        return each(map.entrySet().stream().map(mapper));
    }

    /**
     * Creates a AbstractDomContent object containing HTML using a mapping function on a map. Intended usage:
     * {@literal each(idsToNames, (id, name) -> li(id + " " + name))}
     *
     * @param <I> The type of the keys
     * @param <T> The type of the values
     * @param map the map to iterate over, ex: a map of values { 1: "Tom", 2: "Dick", 3: "Harry" }
     * @param mapper the mapping function, ex: {@literal "(id, name) -> li(id + " " + name)"}
     * @return AbstractDomContent containing mapped data {@literal (ex. docs: [li(1 Tom), li(2 Dick), li(3 Harry)])}
     */
    public static <I, T> AbstractDomContent each(final Map<I, T> map, final BiFunction<I, T, AbstractDomContent> mapper) {
        return each(map.entrySet().stream().map(entry -> mapper.apply(entry.getKey(), entry.getValue())));
    }

    /**
     * Filters a collection to a list, to be used with {@link #each}. Intended usage:
     * {@literal each(filter(numbers, n -> n % 2 == 0), n -> li(n.toString()))}
     *
     * @param <T> The derived generic parameter type
     * @param collection the collection to filter, ex: a list of values "1, 2, 3"
     * @param filter the filter predicate, {@literal ex: "n -> n % 2 == 0"}
     * @return the filtered collection as a list (ex. docs: 2)
     */
    public static <T> List<T> filter(final Collection<T> collection, final Predicate<? super T> filter) {
        return collection.stream().filter(filter).collect(Collectors.toList());
    }

    /**
     * Wraps a String in an UnescapedText element.
     *
     * @param html the input html
     * @return the input html wrapped in an UnescapedText element
     */
    public static UnescapedText rawHtml(final String html) {
        return new UnescapedText(html);
    }

    /**
     * Wraps a String in a Text element (does html-escaping).
     *
     * @param text the input string
     * @return the input string, html-escaped
     */
    public static Text text(final String text) {
        return new Text(text);
    }

    /**
     * Returns a complete html document string.
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
    public static ContainerTag<? extends AbstractTag<?>> tag(final String tagName) {
        return new ContainerTag<>(tagName);
    }

    /**
     * Creates a custom {@link EmptyTag} with the specified tag name.
     *
     * @param tagName The tag name
     * @return A new EmptyTag
     */
    public static EmptyTag<? extends AbstractTag<?>> emptyTag(final String tagName) {
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
     * @param aPath The JavaScript file path
     * @return A ScriptTag containing the JavaScript content
     */
    public static ScriptTag scriptWithInlineFile(final String aPath) {
        return (ScriptTag) InlineStaticResource.get(aPath, InlineStaticResource.TargetFormat.JS);
    }

    /**
     * Creates a {@code <style>} tag containing minified CSS read from the specified file path.
     *
     * @param aPath The CSS file path
     * @return A StyleTag containing the minified CSS content
     */
    public static StyleTag styleWithInlineFileMinimized(final String aPath) {
        return (StyleTag) InlineStaticResource.get(aPath, InlineStaticResource.TargetFormat.CSS_MIN);
    }

    /**
     * Creates a {@code <script>} tag containing minified JavaScript read from the specified file path.
     *
     * @param path The JavaScript file path
     * @return A ScriptTag containing the minified JavaScript content
     */
    public static ScriptTag scriptWithInlineFileMinimized(final String path) {
        return (ScriptTag) InlineStaticResource.get(path, InlineStaticResource.TargetFormat.JS_MIN);
    }

    /**
     * Creates an HTML5 doctype declaration {@code <!DOCTYPE html>}.
     *
     * @return AbstractDomContent representing the doctype declaration
     */
    public static AbstractDomContent document() {
        return rawHtml("<!DOCTYPE html>");
    }

    /**
     * Creates an HTML {@code <html>} element.
     *
     * @return A HtmlTag element
     */
    public static HtmlTag html() {
        return new HtmlTag();
    }

    /**
     * Creates an HTML {@code <html>} element with the specified text content.
     *
     * @param text The text content
     * @return A HtmlTag element
     */
    public static HtmlTag html(final String text) {
        return html().withText(text);
    }

    /**
     * Creates an HTML {@code <html>} element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return A HtmlTag element
     */
    public static HtmlTag html(final AbstractDomContent... dc) {
        return html().with(dc);
    }

    /**
     * Creates an HTML {@code <html>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A HtmlTag element
     */
    public static HtmlTag html(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(html(), shortAttr);
    }

    /**
     * Creates an HTML {@code <html>} element with the specified attributes and text content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param text The text content
     * @return A HtmlTag element
     */
    public static HtmlTag html(final Attr.ShortForm shortAttr, final String text) {
        return Attr.ShortForm.addTo(html(text), shortAttr);
    }

    /**
     * Creates an HTML {@code <html>} element with the specified attributes and child DOM content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param dc The child DOM content nodes
     * @return A HtmlTag element
     */
    public static HtmlTag html(final Attr.ShortForm shortAttr, final AbstractDomContent... dc) {
        return Attr.ShortForm.addTo(html(dc), shortAttr);
    }

    /**
     * Creates an HTML {@code <hxPartial>} element.
     *
     * @return A HxPartialTag element
     */
    public static HxPartialTag hxPartial() {
        return new HxPartialTag();
    }

    /**
     * Creates an HTML {@code <hxPartial>} element with the specified text content.
     *
     * @param text The text content
     * @return A HxPartialTag element
     */
    public static HxPartialTag hxPartial(final String text) {
        return hxPartial().withText(text);
    }

    /**
     * Creates an HTML {@code <hxPartial>} element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return A HxPartialTag element
     */
    public static HxPartialTag hxPartial(final AbstractDomContent... dc) {
        return hxPartial().with(dc);
    }

    /**
     * Creates an HTML {@code <hxPartial>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A HxPartialTag element
     */
    public static HxPartialTag hxPartial(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(hxPartial(), shortAttr);
    }

    /**
     * Creates an HTML {@code <hxPartial>} element with the specified attributes and text content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param text The text content
     * @return A HxPartialTag element
     */
    public static HxPartialTag hxPartial(final Attr.ShortForm shortAttr, final String text) {
        return Attr.ShortForm.addTo(hxPartial(text), shortAttr);
    }

    /**
     * Creates an HTML {@code <hxPartial>} element with the specified attributes and child DOM content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param dc The child DOM content nodes
     * @return A HxPartialTag element
     */
    public static HxPartialTag hxPartial(final Attr.ShortForm shortAttr, final AbstractDomContent... dc) {
        return Attr.ShortForm.addTo(hxPartial(dc), shortAttr);
    }

    /**
     * Creates an HTML {@code <body>} element.
     *
     * @return A BodyTag element
     */
    public static BodyTag body() {
        return new BodyTag();
    }

    /**
     * Creates an HTML {@code <body>} element with the specified text content.
     *
     * @param text The text content
     * @return A BodyTag element
     */
    public static BodyTag body(final String text) {
        return new BodyTag().withText(text);
    }

    /**
     * Creates an HTML {@code <body>} element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return A BodyTag element
     */
    public static BodyTag body(final AbstractDomContent... dc) {
        return new BodyTag().with(dc);
    }

    /**
     * Creates an HTML {@code <body>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A BodyTag element
     */
    public static BodyTag body(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new BodyTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <body>} element with the specified attributes and text content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param text The text content
     * @return A BodyTag element
     */
    public static BodyTag body(final Attr.ShortForm shortAttr, final String text) {
        return Attr.ShortForm.addTo(new BodyTag().withText(text), shortAttr);
    }

    /**
     * Creates an HTML {@code <body>} element with the specified attributes and child DOM content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param dc The child DOM content nodes
     * @return A BodyTag element
     */
    public static BodyTag body(final Attr.ShortForm shortAttr, final AbstractDomContent... dc) {
        return Attr.ShortForm.addTo(new BodyTag().with(dc), shortAttr);
    }

    /**
     * Creates an HTML {@code <head>} element.
     *
     * @return A HeadTag element
     */
    public static HeadTag head() {
        return new HeadTag();
    }

    /**
     * Creates an HTML {@code <head>} element with the specified text content.
     *
     * @param text The text content
     * @return A HeadTag element
     */
    public static HeadTag head(final String text) {
        return new HeadTag().withText(text);
    }

    /**
     * Creates an HTML {@code <head>} element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return A HeadTag element
     */
    public static HeadTag head(final AbstractDomContent... dc) {
        return new HeadTag().with(dc);
    }

    /**
     * Creates an HTML {@code <head>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A HeadTag element
     */
    public static HeadTag head(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new HeadTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <head>} element with the specified attributes and text content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param text The text content
     * @return A HeadTag element
     */
    public static HeadTag head(final Attr.ShortForm shortAttr, final String text) {
        return Attr.ShortForm.addTo(new HeadTag().withText(text), shortAttr);
    }

    /**
     * Creates an HTML {@code <head>} element with the specified attributes and child DOM content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param dc The child DOM content nodes
     * @return A HeadTag element
     */
    public static HeadTag head(final Attr.ShortForm shortAttr, final AbstractDomContent... dc) {
        return Attr.ShortForm.addTo(new HeadTag().with(dc), shortAttr);
    }

    // EmptyTags, generated in class info.freelibrary.j2html.tags.generators.TagCreatorCodeGenerator

    /**
     * Creates an HTML {@code <area>} element.
     *
     * @return A AreaTag element
     */
    public static AreaTag area() {
        return new AreaTag();
    }

    /**
     * Creates an HTML {@code <area>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A AreaTag element
     */
    public static AreaTag area(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new AreaTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <base>} element.
     *
     * @return A BaseTag element
     */
    public static BaseTag base() {
        return new BaseTag();
    }

    /**
     * Creates an HTML {@code <base>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A BaseTag element
     */
    public static BaseTag base(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new BaseTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <br>} element.
     *
     * @return A BrTag element
     */
    public static BrTag br() {
        return new BrTag();
    }

    /**
     * Creates an HTML {@code <br>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A BrTag element
     */
    public static BrTag br(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new BrTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <col>} element.
     *
     * @return A ColTag element
     */
    public static ColTag col() {
        return new ColTag();
    }

    /**
     * Creates an HTML {@code <col>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A ColTag element
     */
    public static ColTag col(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new ColTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <embed>} element.
     *
     * @return A EmbedTag element
     */
    public static EmbedTag embed() {
        return new EmbedTag();
    }

    /**
     * Creates an HTML {@code <embed>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A EmbedTag element
     */
    public static EmbedTag embed(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new EmbedTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <hr>} element.
     *
     * @return A HrTag element
     */
    public static HrTag hr() {
        return new HrTag();
    }

    /**
     * Creates an HTML {@code <hr>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A HrTag element
     */
    public static HrTag hr(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new HrTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <img>} element.
     *
     * @return A ImgTag element
     */
    public static ImgTag img() {
        return new ImgTag();
    }

    /**
     * Creates an HTML {@code <img>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A ImgTag element
     */
    public static ImgTag img(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new ImgTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <input>} element.
     *
     * @return A InputTag element
     */
    public static InputTag input() {
        return new InputTag();
    }

    /**
     * Creates an HTML {@code <input>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A InputTag element
     */
    public static InputTag input(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new InputTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <keygen>} element.
     *
     * @return A KeygenTag element
     */
    public static KeygenTag keygen() {
        return new KeygenTag();
    }

    /**
     * Creates an HTML {@code <keygen>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A KeygenTag element
     */
    public static KeygenTag keygen(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new KeygenTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <link>} element.
     *
     * @return A LinkTag element
     */
    public static LinkTag link() {
        return new LinkTag();
    }

    /**
     * Creates an HTML {@code <link>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A LinkTag element
     */
    public static LinkTag link(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new LinkTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <meta>} element.
     *
     * @return A MetaTag element
     */
    public static MetaTag meta() {
        return new MetaTag();
    }

    /**
     * Creates an HTML {@code <meta>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A MetaTag element
     */
    public static MetaTag meta(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new MetaTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <param>} element.
     *
     * @return A ParamTag element
     */
    public static ParamTag param() {
        return new ParamTag();
    }

    /**
     * Creates an HTML {@code <param>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A ParamTag element
     */
    public static ParamTag param(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new ParamTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <source>} element.
     *
     * @return A SourceTag element
     */
    public static SourceTag source() {
        return new SourceTag();
    }

    /**
     * Creates an HTML {@code <source>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A SourceTag element
     */
    public static SourceTag source(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new SourceTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <track>} element.
     *
     * @return A TrackTag element
     */
    public static TrackTag track() {
        return new TrackTag();
    }

    /**
     * Creates an HTML {@code <track>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A TrackTag element
     */
    public static TrackTag track(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new TrackTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <wbr>} element.
     *
     * @return A WbrTag element
     */
    public static WbrTag wbr() {
        return new WbrTag();
    }

    /**
     * Creates an HTML {@code <wbr>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A WbrTag element
     */
    public static WbrTag wbr(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new WbrTag(), shortAttr);
    }

    // ContainerTags, generated in class info.freelibrary.j2html.tags.generators.TagCreatorCodeGenerator

    /**
     * Creates an HTML {@code <a>} element.
     *
     * @return A ATag element
     */
    public static ATag a() {
        return new ATag();
    }

    /**
     * Creates an HTML {@code <a>} element with the specified text content.
     *
     * @param text The text content
     * @return A ATag element
     */
    public static ATag a(final String text) {
        return new ATag().withText(text);
    }

    /**
     * Creates an HTML {@code <a>} element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return A ATag element
     */
    public static ATag a(final AbstractDomContent... dc) {
        return new ATag().with(dc);
    }

    /**
     * Creates an HTML {@code <a>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A ATag element
     */
    public static ATag a(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new ATag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <a>} element with the specified attributes and text content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param text The text content
     * @return A ATag element
     */
    public static ATag a(final Attr.ShortForm shortAttr, final String text) {
        return Attr.ShortForm.addTo(new ATag().withText(text), shortAttr);
    }

    /**
     * Creates an HTML {@code <a>} element with the specified attributes and child DOM content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param dc The child DOM content nodes
     * @return A ATag element
     */
    public static ATag a(final Attr.ShortForm shortAttr, final AbstractDomContent... dc) {
        return Attr.ShortForm.addTo(new ATag().with(dc), shortAttr);
    }

    /**
     * Creates an HTML {@code <abbr>} element.
     *
     * @return A AbbrTag element
     */
    public static AbbrTag abbr() {
        return new AbbrTag();
    }

    /**
     * Creates an HTML {@code <abbr>} element with the specified text content.
     *
     * @param text The text content
     * @return A AbbrTag element
     */
    public static AbbrTag abbr(final String text) {
        return new AbbrTag().withText(text);
    }

    /**
     * Creates an HTML {@code <abbr>} element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return A AbbrTag element
     */
    public static AbbrTag abbr(final AbstractDomContent... dc) {
        return new AbbrTag().with(dc);
    }

    /**
     * Creates an HTML {@code <abbr>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A AbbrTag element
     */
    public static AbbrTag abbr(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new AbbrTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <abbr>} element with the specified attributes and text content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param text The text content
     * @return A AbbrTag element
     */
    public static AbbrTag abbr(final Attr.ShortForm shortAttr, final String text) {
        return Attr.ShortForm.addTo(new AbbrTag().withText(text), shortAttr);
    }

    /**
     * Creates an HTML {@code <abbr>} element with the specified attributes and child DOM content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param dc The child DOM content nodes
     * @return A AbbrTag element
     */
    public static AbbrTag abbr(final Attr.ShortForm shortAttr, final AbstractDomContent... dc) {
        return Attr.ShortForm.addTo(new AbbrTag().with(dc), shortAttr);
    }

    /**
     * Creates an HTML {@code <address>} element.
     *
     * @return A AddressTag element
     */
    public static AddressTag address() {
        return new AddressTag();
    }

    /**
     * Creates an HTML {@code <address>} element with the specified text content.
     *
     * @param text The text content
     * @return A AddressTag element
     */
    public static AddressTag address(final String text) {
        return new AddressTag().withText(text);
    }

    /**
     * Creates an HTML {@code <address>} element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return A AddressTag element
     */
    public static AddressTag address(final AbstractDomContent... dc) {
        return new AddressTag().with(dc);
    }

    /**
     * Creates an HTML {@code <address>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A AddressTag element
     */
    public static AddressTag address(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new AddressTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <address>} element with the specified attributes and text content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param text The text content
     * @return A AddressTag element
     */
    public static AddressTag address(final Attr.ShortForm shortAttr, final String text) {
        return Attr.ShortForm.addTo(new AddressTag().withText(text), shortAttr);
    }

    /**
     * Creates an HTML {@code <address>} element with the specified attributes and child DOM content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param dc The child DOM content nodes
     * @return A AddressTag element
     */
    public static AddressTag address(final Attr.ShortForm shortAttr, final AbstractDomContent... dc) {
        return Attr.ShortForm.addTo(new AddressTag().with(dc), shortAttr);
    }

    /**
     * Creates an HTML {@code <article>} element.
     *
     * @return A ArticleTag element
     */
    public static ArticleTag article() {
        return new ArticleTag();
    }

    /**
     * Creates an HTML {@code <article>} element with the specified text content.
     *
     * @param text The text content
     * @return A ArticleTag element
     */
    public static ArticleTag article(final String text) {
        return new ArticleTag().withText(text);
    }

    /**
     * Creates an HTML {@code <article>} element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return A ArticleTag element
     */
    public static ArticleTag article(final AbstractDomContent... dc) {
        return new ArticleTag().with(dc);
    }

    /**
     * Creates an HTML {@code <article>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A ArticleTag element
     */
    public static ArticleTag article(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new ArticleTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <article>} element with the specified attributes and text content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param text The text content
     * @return A ArticleTag element
     */
    public static ArticleTag article(final Attr.ShortForm shortAttr, final String text) {
        return Attr.ShortForm.addTo(new ArticleTag().withText(text), shortAttr);
    }

    /**
     * Creates an HTML {@code <article>} element with the specified attributes and child DOM content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param dc The child DOM content nodes
     * @return A ArticleTag element
     */
    public static ArticleTag article(final Attr.ShortForm shortAttr, final AbstractDomContent... dc) {
        return Attr.ShortForm.addTo(new ArticleTag().with(dc), shortAttr);
    }

    /**
     * Creates an HTML {@code <aside>} element.
     *
     * @return A AsideTag element
     */
    public static AsideTag aside() {
        return new AsideTag();
    }

    /**
     * Creates an HTML {@code <aside>} element with the specified text content.
     *
     * @param text The text content
     * @return A AsideTag element
     */
    public static AsideTag aside(final String text) {
        return new AsideTag().withText(text);
    }

    /**
     * Creates an HTML {@code <aside>} element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return A AsideTag element
     */
    public static AsideTag aside(final AbstractDomContent... dc) {
        return new AsideTag().with(dc);
    }

    /**
     * Creates an HTML {@code <aside>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A AsideTag element
     */
    public static AsideTag aside(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new AsideTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <aside>} element with the specified attributes and text content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param text The text content
     * @return A AsideTag element
     */
    public static AsideTag aside(final Attr.ShortForm shortAttr, final String text) {
        return Attr.ShortForm.addTo(new AsideTag().withText(text), shortAttr);
    }

    /**
     * Creates an HTML {@code <aside>} element with the specified attributes and child DOM content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param dc The child DOM content nodes
     * @return A AsideTag element
     */
    public static AsideTag aside(final Attr.ShortForm shortAttr, final AbstractDomContent... dc) {
        return Attr.ShortForm.addTo(new AsideTag().with(dc), shortAttr);
    }

    /**
     * Creates an HTML {@code <audio>} element.
     *
     * @return A AudioTag element
     */
    public static AudioTag audio() {
        return new AudioTag();
    }

    /**
     * Creates an HTML {@code <audio>} element with the specified text content.
     *
     * @param text The text content
     * @return A AudioTag element
     */
    public static AudioTag audio(final String text) {
        return new AudioTag().withText(text);
    }

    /**
     * Creates an HTML {@code <audio>} element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return A AudioTag element
     */
    public static AudioTag audio(final AbstractDomContent... dc) {
        return new AudioTag().with(dc);
    }

    /**
     * Creates an HTML {@code <audio>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A AudioTag element
     */
    public static AudioTag audio(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new AudioTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <audio>} element with the specified attributes and text content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param text The text content
     * @return A AudioTag element
     */
    public static AudioTag audio(final Attr.ShortForm shortAttr, final String text) {
        return Attr.ShortForm.addTo(new AudioTag().withText(text), shortAttr);
    }

    /**
     * Creates an HTML {@code <audio>} element with the specified attributes and child DOM content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param dc The child DOM content nodes
     * @return A AudioTag element
     */
    public static AudioTag audio(final Attr.ShortForm shortAttr, final AbstractDomContent... dc) {
        return Attr.ShortForm.addTo(new AudioTag().with(dc), shortAttr);
    }

    /**
     * Creates an HTML {@code <b>} element.
     *
     * @return A BTag element
     */
    public static BTag b() {
        return new BTag();
    }

    /**
     * Creates an HTML {@code <b>} element with the specified text content.
     *
     * @param text The text content
     * @return A BTag element
     */
    public static BTag b(final String text) {
        return new BTag().withText(text);
    }

    /**
     * Creates an HTML {@code <b>} element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return A BTag element
     */
    public static BTag b(final AbstractDomContent... dc) {
        return new BTag().with(dc);
    }

    /**
     * Creates an HTML {@code <b>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A BTag element
     */
    public static BTag b(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new BTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <b>} element with the specified attributes and text content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param text The text content
     * @return A BTag element
     */
    public static BTag b(final Attr.ShortForm shortAttr, final String text) {
        return Attr.ShortForm.addTo(new BTag().withText(text), shortAttr);
    }

    /**
     * Creates an HTML {@code <b>} element with the specified attributes and child DOM content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param dc The child DOM content nodes
     * @return A BTag element
     */
    public static BTag b(final Attr.ShortForm shortAttr, final AbstractDomContent... dc) {
        return Attr.ShortForm.addTo(new BTag().with(dc), shortAttr);
    }

    /**
     * Creates an HTML {@code <bdi>} element.
     *
     * @return A BdiTag element
     */
    public static BdiTag bdi() {
        return new BdiTag();
    }

    /**
     * Creates an HTML {@code <bdi>} element with the specified text content.
     *
     * @param text The text content
     * @return A BdiTag element
     */
    public static BdiTag bdi(final String text) {
        return new BdiTag().withText(text);
    }

    /**
     * Creates an HTML {@code <bdi>} element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return A BdiTag element
     */
    public static BdiTag bdi(final AbstractDomContent... dc) {
        return new BdiTag().with(dc);
    }

    /**
     * Creates an HTML {@code <bdi>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A BdiTag element
     */
    public static BdiTag bdi(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new BdiTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <bdi>} element with the specified attributes and text content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param text The text content
     * @return A BdiTag element
     */
    public static BdiTag bdi(final Attr.ShortForm shortAttr, final String text) {
        return Attr.ShortForm.addTo(new BdiTag().withText(text), shortAttr);
    }

    /**
     * Creates an HTML {@code <bdi>} element with the specified attributes and child DOM content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param dc The child DOM content nodes
     * @return A BdiTag element
     */
    public static BdiTag bdi(final Attr.ShortForm shortAttr, final AbstractDomContent... dc) {
        return Attr.ShortForm.addTo(new BdiTag().with(dc), shortAttr);
    }

    /**
     * Creates an HTML {@code <bdo>} element.
     *
     * @return A BdoTag element
     */
    public static BdoTag bdo() {
        return new BdoTag();
    }

    /**
     * Creates an HTML {@code <bdo>} element with the specified text content.
     *
     * @param text The text content
     * @return A BdoTag element
     */
    public static BdoTag bdo(final String text) {
        return new BdoTag().withText(text);
    }

    /**
     * Creates an HTML {@code <bdo>} element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return A BdoTag element
     */
    public static BdoTag bdo(final AbstractDomContent... dc) {
        return new BdoTag().with(dc);
    }

    /**
     * Creates an HTML {@code <bdo>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A BdoTag element
     */
    public static BdoTag bdo(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new BdoTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <bdo>} element with the specified attributes and text content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param text The text content
     * @return A BdoTag element
     */
    public static BdoTag bdo(final Attr.ShortForm shortAttr, final String text) {
        return Attr.ShortForm.addTo(new BdoTag().withText(text), shortAttr);
    }

    /**
     * Creates an HTML {@code <bdo>} element with the specified attributes and child DOM content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param dc The child DOM content nodes
     * @return A BdoTag element
     */
    public static BdoTag bdo(final Attr.ShortForm shortAttr, final AbstractDomContent... dc) {
        return Attr.ShortForm.addTo(new BdoTag().with(dc), shortAttr);
    }

    /**
     * Creates an HTML {@code <blockquote>} element.
     *
     * @return A BlockquoteTag element
     */
    public static BlockquoteTag blockquote() {
        return new BlockquoteTag();
    }

    /**
     * Creates an HTML {@code <blockquote>} element with the specified text content.
     *
     * @param text The text content
     * @return A BlockquoteTag element
     */
    public static BlockquoteTag blockquote(final String text) {
        return new BlockquoteTag().withText(text);
    }

    /**
     * Creates an HTML {@code <blockquote>} element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return A BlockquoteTag element
     */
    public static BlockquoteTag blockquote(final AbstractDomContent... dc) {
        return new BlockquoteTag().with(dc);
    }

    /**
     * Creates an HTML {@code <blockquote>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A BlockquoteTag element
     */
    public static BlockquoteTag blockquote(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new BlockquoteTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <blockquote>} element with the specified attributes and text content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param text The text content
     * @return A BlockquoteTag element
     */
    public static BlockquoteTag blockquote(final Attr.ShortForm shortAttr, final String text) {
        return Attr.ShortForm.addTo(new BlockquoteTag().withText(text), shortAttr);
    }

    /**
     * Creates an HTML {@code <blockquote>} element with the specified attributes and child DOM content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param dc The child DOM content nodes
     * @return A BlockquoteTag element
     */
    public static BlockquoteTag blockquote(final Attr.ShortForm shortAttr, final AbstractDomContent... dc) {
        return Attr.ShortForm.addTo(new BlockquoteTag().with(dc), shortAttr);
    }

    /**
     * Creates an HTML {@code <button>} element.
     *
     * @return A ButtonTag element
     */
    public static ButtonTag button() {
        return new ButtonTag();
    }

    /**
     * Creates an HTML {@code <button>} element with the specified text content.
     *
     * @param text The text content
     * @return A ButtonTag element
     */
    public static ButtonTag button(final String text) {
        return new ButtonTag().withText(text);
    }

    /**
     * Creates an HTML {@code <button>} element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return A ButtonTag element
     */
    public static ButtonTag button(final AbstractDomContent... dc) {
        return new ButtonTag().with(dc);
    }

    /**
     * Creates an HTML {@code <button>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A ButtonTag element
     */
    public static ButtonTag button(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new ButtonTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <button>} element with the specified attributes and text content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param text The text content
     * @return A ButtonTag element
     */
    public static ButtonTag button(final Attr.ShortForm shortAttr, final String text) {
        return Attr.ShortForm.addTo(new ButtonTag().withText(text), shortAttr);
    }

    /**
     * Creates an HTML {@code <button>} element with the specified attributes and child DOM content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param dc The child DOM content nodes
     * @return A ButtonTag element
     */
    public static ButtonTag button(final Attr.ShortForm shortAttr, final AbstractDomContent... dc) {
        return Attr.ShortForm.addTo(new ButtonTag().with(dc), shortAttr);
    }

    /**
     * Creates an HTML {@code <canvas>} element.
     *
     * @return A CanvasTag element
     */
    public static CanvasTag canvas() {
        return new CanvasTag();
    }

    /**
     * Creates an HTML {@code <canvas>} element with the specified text content.
     *
     * @param text The text content
     * @return A CanvasTag element
     */
    public static CanvasTag canvas(final String text) {
        return new CanvasTag().withText(text);
    }

    /**
     * Creates an HTML {@code <canvas>} element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return A CanvasTag element
     */
    public static CanvasTag canvas(final AbstractDomContent... dc) {
        return new CanvasTag().with(dc);
    }

    /**
     * Creates an HTML {@code <canvas>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A CanvasTag element
     */
    public static CanvasTag canvas(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new CanvasTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <canvas>} element with the specified attributes and text content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param text The text content
     * @return A CanvasTag element
     */
    public static CanvasTag canvas(final Attr.ShortForm shortAttr, final String text) {
        return Attr.ShortForm.addTo(new CanvasTag().withText(text), shortAttr);
    }

    /**
     * Creates an HTML {@code <canvas>} element with the specified attributes and child DOM content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param dc The child DOM content nodes
     * @return A CanvasTag element
     */
    public static CanvasTag canvas(final Attr.ShortForm shortAttr, final AbstractDomContent... dc) {
        return Attr.ShortForm.addTo(new CanvasTag().with(dc), shortAttr);
    }

    /**
     * Creates an HTML {@code <caption>} element.
     *
     * @return A CaptionTag element
     */
    public static CaptionTag caption() {
        return new CaptionTag();
    }

    /**
     * Creates an HTML {@code <caption>} element with the specified text content.
     *
     * @param text The text content
     * @return A CaptionTag element
     */
    public static CaptionTag caption(final String text) {
        return new CaptionTag().withText(text);
    }

    /**
     * Creates an HTML {@code <caption>} element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return A CaptionTag element
     */
    public static CaptionTag caption(final AbstractDomContent... dc) {
        return new CaptionTag().with(dc);
    }

    /**
     * Creates an HTML {@code <caption>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A CaptionTag element
     */
    public static CaptionTag caption(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new CaptionTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <caption>} element with the specified attributes and text content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param text The text content
     * @return A CaptionTag element
     */
    public static CaptionTag caption(final Attr.ShortForm shortAttr, final String text) {
        return Attr.ShortForm.addTo(new CaptionTag().withText(text), shortAttr);
    }

    /**
     * Creates an HTML {@code <caption>} element with the specified attributes and child DOM content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param dc The child DOM content nodes
     * @return A CaptionTag element
     */
    public static CaptionTag caption(final Attr.ShortForm shortAttr, final AbstractDomContent... dc) {
        return Attr.ShortForm.addTo(new CaptionTag().with(dc), shortAttr);
    }

    /**
     * Creates an HTML {@code <cite>} element.
     *
     * @return A CiteTag element
     */
    public static CiteTag cite() {
        return new CiteTag();
    }

    /**
     * Creates an HTML {@code <cite>} element with the specified text content.
     *
     * @param text The text content
     * @return A CiteTag element
     */
    public static CiteTag cite(final String text) {
        return new CiteTag().withText(text);
    }

    /**
     * Creates an HTML {@code <cite>} element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return A CiteTag element
     */
    public static CiteTag cite(final AbstractDomContent... dc) {
        return new CiteTag().with(dc);
    }

    /**
     * Creates an HTML {@code <cite>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A CiteTag element
     */
    public static CiteTag cite(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new CiteTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <cite>} element with the specified attributes and text content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param text The text content
     * @return A CiteTag element
     */
    public static CiteTag cite(final Attr.ShortForm shortAttr, final String text) {
        return Attr.ShortForm.addTo(new CiteTag().withText(text), shortAttr);
    }

    /**
     * Creates an HTML {@code <cite>} element with the specified attributes and child DOM content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param dc The child DOM content nodes
     * @return A CiteTag element
     */
    public static CiteTag cite(final Attr.ShortForm shortAttr, final AbstractDomContent... dc) {
        return Attr.ShortForm.addTo(new CiteTag().with(dc), shortAttr);
    }

    /**
     * Creates an HTML {@code <code>} element.
     *
     * @return A CodeTag element
     */
    public static CodeTag code() {
        return new CodeTag();
    }

    /**
     * Creates an HTML {@code <code>} element with the specified text content.
     *
     * @param text The text content
     * @return A CodeTag element
     */
    public static CodeTag code(final String text) {
        return new CodeTag().withText(text);
    }

    /**
     * Creates an HTML {@code <code>} element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return A CodeTag element
     */
    public static CodeTag code(final AbstractDomContent... dc) {
        return new CodeTag().with(dc);
    }

    /**
     * Creates an HTML {@code <code>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A CodeTag element
     */
    public static CodeTag code(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new CodeTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <code>} element with the specified attributes and text content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param text The text content
     * @return A CodeTag element
     */
    public static CodeTag code(final Attr.ShortForm shortAttr, final String text) {
        return Attr.ShortForm.addTo(new CodeTag().withText(text), shortAttr);
    }

    /**
     * Creates an HTML {@code <code>} element with the specified attributes and child DOM content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param dc The child DOM content nodes
     * @return A CodeTag element
     */
    public static CodeTag code(final Attr.ShortForm shortAttr, final AbstractDomContent... dc) {
        return Attr.ShortForm.addTo(new CodeTag().with(dc), shortAttr);
    }

    /**
     * Creates an HTML {@code <colgroup>} element.
     *
     * @return A ColgroupTag element
     */
    public static ColgroupTag colgroup() {
        return new ColgroupTag();
    }

    /**
     * Creates an HTML {@code <colgroup>} element with the specified text content.
     *
     * @param text The text content
     * @return A ColgroupTag element
     */
    public static ColgroupTag colgroup(final String text) {
        return new ColgroupTag().withText(text);
    }

    /**
     * Creates an HTML {@code <colgroup>} element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return A ColgroupTag element
     */
    public static ColgroupTag colgroup(final AbstractDomContent... dc) {
        return new ColgroupTag().with(dc);
    }

    /**
     * Creates an HTML {@code <colgroup>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A ColgroupTag element
     */
    public static ColgroupTag colgroup(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new ColgroupTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <colgroup>} element with the specified attributes and text content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param text The text content
     * @return A ColgroupTag element
     */
    public static ColgroupTag colgroup(final Attr.ShortForm shortAttr, final String text) {
        return Attr.ShortForm.addTo(new ColgroupTag().withText(text), shortAttr);
    }

    /**
     * Creates an HTML {@code <colgroup>} element with the specified attributes and child DOM content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param dc The child DOM content nodes
     * @return A ColgroupTag element
     */
    public static ColgroupTag colgroup(final Attr.ShortForm shortAttr, final AbstractDomContent... dc) {
        return Attr.ShortForm.addTo(new ColgroupTag().with(dc), shortAttr);
    }

    /**
     * Creates an HTML {@code <data>} element.
     *
     * @return A DataTag element
     */
    public static DataTag data() {
        return new DataTag();
    }

    /**
     * Creates an HTML {@code <data>} element with the specified text content.
     *
     * @param text The text content
     * @return A DataTag element
     */
    public static DataTag data(final String text) {
        return new DataTag().withText(text);
    }

    /**
     * Creates an HTML {@code <data>} element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return A DataTag element
     */
    public static DataTag data(final AbstractDomContent... dc) {
        return new DataTag().with(dc);
    }

    /**
     * Creates an HTML {@code <data>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A DataTag element
     */
    public static DataTag data(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new DataTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <data>} element with the specified attributes and text content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param text The text content
     * @return A DataTag element
     */
    public static DataTag data(final Attr.ShortForm shortAttr, final String text) {
        return Attr.ShortForm.addTo(new DataTag().withText(text), shortAttr);
    }

    /**
     * Creates an HTML {@code <data>} element with the specified attributes and child DOM content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param dc The child DOM content nodes
     * @return A DataTag element
     */
    public static DataTag data(final Attr.ShortForm shortAttr, final AbstractDomContent... dc) {
        return Attr.ShortForm.addTo(new DataTag().with(dc), shortAttr);
    }

    /**
     * Creates an HTML {@code <datalist>} element.
     *
     * @return A DatalistTag element
     */
    public static DatalistTag datalist() {
        return new DatalistTag();
    }

    /**
     * Creates an HTML {@code <datalist>} element with the specified text content.
     *
     * @param text The text content
     * @return A DatalistTag element
     */
    public static DatalistTag datalist(final String text) {
        return new DatalistTag().withText(text);
    }

    /**
     * Creates an HTML {@code <datalist>} element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return A DatalistTag element
     */
    public static DatalistTag datalist(final AbstractDomContent... dc) {
        return new DatalistTag().with(dc);
    }

    /**
     * Creates an HTML {@code <datalist>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A DatalistTag element
     */
    public static DatalistTag datalist(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new DatalistTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <datalist>} element with the specified attributes and text content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param text The text content
     * @return A DatalistTag element
     */
    public static DatalistTag datalist(final Attr.ShortForm shortAttr, final String text) {
        return Attr.ShortForm.addTo(new DatalistTag().withText(text), shortAttr);
    }

    /**
     * Creates an HTML {@code <datalist>} element with the specified attributes and child DOM content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param dc The child DOM content nodes
     * @return A DatalistTag element
     */
    public static DatalistTag datalist(final Attr.ShortForm shortAttr, final AbstractDomContent... dc) {
        return Attr.ShortForm.addTo(new DatalistTag().with(dc), shortAttr);
    }

    /**
     * Creates an HTML {@code <dd>} element.
     *
     * @return A DdTag element
     */
    public static DdTag dd() {
        return new DdTag();
    }

    /**
     * Creates an HTML {@code <dd>} element with the specified text content.
     *
     * @param text The text content
     * @return A DdTag element
     */
    public static DdTag dd(final String text) {
        return new DdTag().withText(text);
    }

    /**
     * Creates an HTML {@code <dd>} element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return A DdTag element
     */
    public static DdTag dd(final AbstractDomContent... dc) {
        return new DdTag().with(dc);
    }

    /**
     * Creates an HTML {@code <dd>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A DdTag element
     */
    public static DdTag dd(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new DdTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <dd>} element with the specified attributes and text content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param text The text content
     * @return A DdTag element
     */
    public static DdTag dd(final Attr.ShortForm shortAttr, final String text) {
        return Attr.ShortForm.addTo(new DdTag().withText(text), shortAttr);
    }

    /**
     * Creates an HTML {@code <dd>} element with the specified attributes and child DOM content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param dc The child DOM content nodes
     * @return A DdTag element
     */
    public static DdTag dd(final Attr.ShortForm shortAttr, final AbstractDomContent... dc) {
        return Attr.ShortForm.addTo(new DdTag().with(dc), shortAttr);
    }

    /**
     * Creates an HTML {@code <del>} element.
     *
     * @return A DelTag element
     */
    public static DelTag del() {
        return new DelTag();
    }

    /**
     * Creates an HTML {@code <del>} element with the specified text content.
     *
     * @param text The text content
     * @return A DelTag element
     */
    public static DelTag del(final String text) {
        return new DelTag().withText(text);
    }

    /**
     * Creates an HTML {@code <del>} element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return A DelTag element
     */
    public static DelTag del(final AbstractDomContent... dc) {
        return new DelTag().with(dc);
    }

    /**
     * Creates an HTML {@code <del>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A DelTag element
     */
    public static DelTag del(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new DelTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <del>} element with the specified attributes and text content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param text The text content
     * @return A DelTag element
     */
    public static DelTag del(final Attr.ShortForm shortAttr, final String text) {
        return Attr.ShortForm.addTo(new DelTag().withText(text), shortAttr);
    }

    /**
     * Creates an HTML {@code <del>} element with the specified attributes and child DOM content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param dc The child DOM content nodes
     * @return A DelTag element
     */
    public static DelTag del(final Attr.ShortForm shortAttr, final AbstractDomContent... dc) {
        return Attr.ShortForm.addTo(new DelTag().with(dc), shortAttr);
    }

    /**
     * Creates an HTML {@code <details>} element.
     *
     * @return A DetailsTag element
     */
    public static DetailsTag details() {
        return new DetailsTag();
    }

    /**
     * Creates an HTML {@code <details>} element with the specified text content.
     *
     * @param text The text content
     * @return A DetailsTag element
     */
    public static DetailsTag details(final String text) {
        return new DetailsTag().withText(text);
    }

    /**
     * Creates an HTML {@code <details>} element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return A DetailsTag element
     */
    public static DetailsTag details(final AbstractDomContent... dc) {
        return new DetailsTag().with(dc);
    }

    /**
     * Creates an HTML {@code <details>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A DetailsTag element
     */
    public static DetailsTag details(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new DetailsTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <details>} element with the specified attributes and text content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param text The text content
     * @return A DetailsTag element
     */
    public static DetailsTag details(final Attr.ShortForm shortAttr, final String text) {
        return Attr.ShortForm.addTo(new DetailsTag().withText(text), shortAttr);
    }

    /**
     * Creates an HTML {@code <details>} element with the specified attributes and child DOM content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param dc The child DOM content nodes
     * @return A DetailsTag element
     */
    public static DetailsTag details(final Attr.ShortForm shortAttr, final AbstractDomContent... dc) {
        return Attr.ShortForm.addTo(new DetailsTag().with(dc), shortAttr);
    }

    /**
     * Creates an HTML {@code <dfn>} element.
     *
     * @return A DfnTag element
     */
    public static DfnTag dfn() {
        return new DfnTag();
    }

    /**
     * Creates an HTML {@code <dfn>} element with the specified text content.
     *
     * @param text The text content
     * @return A DfnTag element
     */
    public static DfnTag dfn(final String text) {
        return new DfnTag().withText(text);
    }

    /**
     * Creates an HTML {@code <dfn>} element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return A DfnTag element
     */
    public static DfnTag dfn(final AbstractDomContent... dc) {
        return new DfnTag().with(dc);
    }

    /**
     * Creates an HTML {@code <dfn>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A DfnTag element
     */
    public static DfnTag dfn(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new DfnTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <dfn>} element with the specified attributes and text content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param text The text content
     * @return A DfnTag element
     */
    public static DfnTag dfn(final Attr.ShortForm shortAttr, final String text) {
        return Attr.ShortForm.addTo(new DfnTag().withText(text), shortAttr);
    }

    /**
     * Creates an HTML {@code <dfn>} element with the specified attributes and child DOM content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param dc The child DOM content nodes
     * @return A DfnTag element
     */
    public static DfnTag dfn(final Attr.ShortForm shortAttr, final AbstractDomContent... dc) {
        return Attr.ShortForm.addTo(new DfnTag().with(dc), shortAttr);
    }

    /**
     * Creates an HTML {@code <dialog>} element.
     *
     * @return A DialogTag element
     */
    public static DialogTag dialog() {
        return new DialogTag();
    }

    /**
     * Creates an HTML {@code <dialog>} element with the specified text content.
     *
     * @param text The text content
     * @return A DialogTag element
     */
    public static DialogTag dialog(final String text) {
        return new DialogTag().withText(text);
    }

    /**
     * Creates an HTML {@code <dialog>} element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return A DialogTag element
     */
    public static DialogTag dialog(final AbstractDomContent... dc) {
        return new DialogTag().with(dc);
    }

    /**
     * Creates an HTML {@code <dialog>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A DialogTag element
     */
    public static DialogTag dialog(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new DialogTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <dialog>} element with the specified attributes and text content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param text The text content
     * @return A DialogTag element
     */
    public static DialogTag dialog(final Attr.ShortForm shortAttr, final String text) {
        return Attr.ShortForm.addTo(new DialogTag().withText(text), shortAttr);
    }

    /**
     * Creates an HTML {@code <dialog>} element with the specified attributes and child DOM content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param dc The child DOM content nodes
     * @return A DialogTag element
     */
    public static DialogTag dialog(final Attr.ShortForm shortAttr, final AbstractDomContent... dc) {
        return Attr.ShortForm.addTo(new DialogTag().with(dc), shortAttr);
    }

    /**
     * Creates an HTML {@code <div>} element.
     *
     * @return A DivTag element
     */
    public static DivTag div() {
        return new DivTag();
    }

    /**
     * Creates an HTML {@code <div>} element with the specified text content.
     *
     * @param text The text content
     * @return A DivTag element
     */
    public static DivTag div(final String text) {
        return new DivTag().withText(text);
    }

    /**
     * Creates an HTML {@code <div>} element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return A DivTag element
     */
    public static DivTag div(final AbstractDomContent... dc) {
        return new DivTag().with(dc);
    }

    /**
     * Creates an HTML {@code <div>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A DivTag element
     */
    public static DivTag div(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new DivTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <div>} element with the specified attributes and text content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param text The text content
     * @return A DivTag element
     */
    public static DivTag div(final Attr.ShortForm shortAttr, final String text) {
        return Attr.ShortForm.addTo(new DivTag().withText(text), shortAttr);
    }

    /**
     * Creates an HTML {@code <div>} element with the specified attributes and child DOM content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param dc The child DOM content nodes
     * @return A DivTag element
     */
    public static DivTag div(final Attr.ShortForm shortAttr, final AbstractDomContent... dc) {
        return Attr.ShortForm.addTo(new DivTag().with(dc), shortAttr);
    }

    /**
     * Creates an HTML {@code <dl>} element.
     *
     * @return A DlTag element
     */
    public static DlTag dl() {
        return new DlTag();
    }

    /**
     * Creates an HTML {@code <dl>} element with the specified text content.
     *
     * @param text The text content
     * @return A DlTag element
     */
    public static DlTag dl(final String text) {
        return new DlTag().withText(text);
    }

    /**
     * Creates an HTML {@code <dl>} element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return A DlTag element
     */
    public static DlTag dl(final AbstractDomContent... dc) {
        return new DlTag().with(dc);
    }

    /**
     * Creates an HTML {@code <dl>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A DlTag element
     */
    public static DlTag dl(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new DlTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <dl>} element with the specified attributes and text content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param text The text content
     * @return A DlTag element
     */
    public static DlTag dl(final Attr.ShortForm shortAttr, final String text) {
        return Attr.ShortForm.addTo(new DlTag().withText(text), shortAttr);
    }

    /**
     * Creates an HTML {@code <dl>} element with the specified attributes and child DOM content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param dc The child DOM content nodes
     * @return A DlTag element
     */
    public static DlTag dl(final Attr.ShortForm shortAttr, final AbstractDomContent... dc) {
        return Attr.ShortForm.addTo(new DlTag().with(dc), shortAttr);
    }

    /**
     * Creates an HTML {@code <dt>} element.
     *
     * @return A DtTag element
     */
    public static DtTag dt() {
        return new DtTag();
    }

    /**
     * Creates an HTML {@code <dt>} element with the specified text content.
     *
     * @param text The text content
     * @return A DtTag element
     */
    public static DtTag dt(final String text) {
        return new DtTag().withText(text);
    }

    /**
     * Creates an HTML {@code <dt>} element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return A DtTag element
     */
    public static DtTag dt(final AbstractDomContent... dc) {
        return new DtTag().with(dc);
    }

    /**
     * Creates an HTML {@code <dt>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A DtTag element
     */
    public static DtTag dt(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new DtTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <dt>} element with the specified attributes and text content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param text The text content
     * @return A DtTag element
     */
    public static DtTag dt(final Attr.ShortForm shortAttr, final String text) {
        return Attr.ShortForm.addTo(new DtTag().withText(text), shortAttr);
    }

    /**
     * Creates an HTML {@code <dt>} element with the specified attributes and child DOM content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param dc The child DOM content nodes
     * @return A DtTag element
     */
    public static DtTag dt(final Attr.ShortForm shortAttr, final AbstractDomContent... dc) {
        return Attr.ShortForm.addTo(new DtTag().with(dc), shortAttr);
    }

    /**
     * Creates an HTML {@code <em>} element.
     *
     * @return A EmTag element
     */
    public static EmTag em() {
        return new EmTag();
    }

    /**
     * Creates an HTML {@code <em>} element with the specified text content.
     *
     * @param text The text content
     * @return A EmTag element
     */
    public static EmTag em(final String text) {
        return new EmTag().withText(text);
    }

    /**
     * Creates an HTML {@code <em>} element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return A EmTag element
     */
    public static EmTag em(final AbstractDomContent... dc) {
        return new EmTag().with(dc);
    }

    /**
     * Creates an HTML {@code <em>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A EmTag element
     */
    public static EmTag em(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new EmTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <em>} element with the specified attributes and text content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param text The text content
     * @return A EmTag element
     */
    public static EmTag em(final Attr.ShortForm shortAttr, final String text) {
        return Attr.ShortForm.addTo(new EmTag().withText(text), shortAttr);
    }

    /**
     * Creates an HTML {@code <em>} element with the specified attributes and child DOM content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param dc The child DOM content nodes
     * @return A EmTag element
     */
    public static EmTag em(final Attr.ShortForm shortAttr, final AbstractDomContent... dc) {
        return Attr.ShortForm.addTo(new EmTag().with(dc), shortAttr);
    }

    /**
     * Creates an HTML {@code <fieldset>} element.
     *
     * @return A FieldsetTag element
     */
    public static FieldsetTag fieldset() {
        return new FieldsetTag();
    }

    /**
     * Creates an HTML {@code <fieldset>} element with the specified text content.
     *
     * @param text The text content
     * @return A FieldsetTag element
     */
    public static FieldsetTag fieldset(final String text) {
        return new FieldsetTag().withText(text);
    }

    /**
     * Creates an HTML {@code <fieldset>} element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return A FieldsetTag element
     */
    public static FieldsetTag fieldset(final AbstractDomContent... dc) {
        return new FieldsetTag().with(dc);
    }

    /**
     * Creates an HTML {@code <fieldset>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A FieldsetTag element
     */
    public static FieldsetTag fieldset(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new FieldsetTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <fieldset>} element with the specified attributes and text content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param text The text content
     * @return A FieldsetTag element
     */
    public static FieldsetTag fieldset(final Attr.ShortForm shortAttr, final String text) {
        return Attr.ShortForm.addTo(new FieldsetTag().withText(text), shortAttr);
    }

    /**
     * Creates an HTML {@code <fieldset>} element with the specified attributes and child DOM content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param dc The child DOM content nodes
     * @return A FieldsetTag element
     */
    public static FieldsetTag fieldset(final Attr.ShortForm shortAttr, final AbstractDomContent... dc) {
        return Attr.ShortForm.addTo(new FieldsetTag().with(dc), shortAttr);
    }

    /**
     * Creates an HTML {@code <figcaption>} element.
     *
     * @return A FigcaptionTag element
     */
    public static FigcaptionTag figcaption() {
        return new FigcaptionTag();
    }

    /**
     * Creates an HTML {@code <figcaption>} element with the specified text content.
     *
     * @param text The text content
     * @return A FigcaptionTag element
     */
    public static FigcaptionTag figcaption(final String text) {
        return new FigcaptionTag().withText(text);
    }

    /**
     * Creates an HTML {@code <figcaption>} element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return A FigcaptionTag element
     */
    public static FigcaptionTag figcaption(final AbstractDomContent... dc) {
        return new FigcaptionTag().with(dc);
    }

    /**
     * Creates an HTML {@code <figcaption>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A FigcaptionTag element
     */
    public static FigcaptionTag figcaption(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new FigcaptionTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <figcaption>} element with the specified attributes and text content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param text The text content
     * @return A FigcaptionTag element
     */
    public static FigcaptionTag figcaption(final Attr.ShortForm shortAttr, final String text) {
        return Attr.ShortForm.addTo(new FigcaptionTag().withText(text), shortAttr);
    }

    /**
     * Creates an HTML {@code <figcaption>} element with the specified attributes and child DOM content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param dc The child DOM content nodes
     * @return A FigcaptionTag element
     */
    public static FigcaptionTag figcaption(final Attr.ShortForm shortAttr, final AbstractDomContent... dc) {
        return Attr.ShortForm.addTo(new FigcaptionTag().with(dc), shortAttr);
    }

    /**
     * Creates an HTML {@code <figure>} element.
     *
     * @return A FigureTag element
     */
    public static FigureTag figure() {
        return new FigureTag();
    }

    /**
     * Creates an HTML {@code <figure>} element with the specified text content.
     *
     * @param text The text content
     * @return A FigureTag element
     */
    public static FigureTag figure(final String text) {
        return new FigureTag().withText(text);
    }

    /**
     * Creates an HTML {@code <figure>} element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return A FigureTag element
     */
    public static FigureTag figure(final AbstractDomContent... dc) {
        return new FigureTag().with(dc);
    }

    /**
     * Creates an HTML {@code <figure>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A FigureTag element
     */
    public static FigureTag figure(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new FigureTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <figure>} element with the specified attributes and text content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param text The text content
     * @return A FigureTag element
     */
    public static FigureTag figure(final Attr.ShortForm shortAttr, final String text) {
        return Attr.ShortForm.addTo(new FigureTag().withText(text), shortAttr);
    }

    /**
     * Creates an HTML {@code <figure>} element with the specified attributes and child DOM content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param dc The child DOM content nodes
     * @return A FigureTag element
     */
    public static FigureTag figure(final Attr.ShortForm shortAttr, final AbstractDomContent... dc) {
        return Attr.ShortForm.addTo(new FigureTag().with(dc), shortAttr);
    }

    /**
     * Creates an HTML {@code <footer>} element.
     *
     * @return A FooterTag element
     */
    public static FooterTag footer() {
        return new FooterTag();
    }

    /**
     * Creates an HTML {@code <footer>} element with the specified text content.
     *
     * @param text The text content
     * @return A FooterTag element
     */
    public static FooterTag footer(final String text) {
        return new FooterTag().withText(text);
    }

    /**
     * Creates an HTML {@code <footer>} element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return A FooterTag element
     */
    public static FooterTag footer(final AbstractDomContent... dc) {
        return new FooterTag().with(dc);
    }

    /**
     * Creates an HTML {@code <footer>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A FooterTag element
     */
    public static FooterTag footer(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new FooterTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <footer>} element with the specified attributes and text content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param text The text content
     * @return A FooterTag element
     */
    public static FooterTag footer(final Attr.ShortForm shortAttr, final String text) {
        return Attr.ShortForm.addTo(new FooterTag().withText(text), shortAttr);
    }

    /**
     * Creates an HTML {@code <footer>} element with the specified attributes and child DOM content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param dc The child DOM content nodes
     * @return A FooterTag element
     */
    public static FooterTag footer(final Attr.ShortForm shortAttr, final AbstractDomContent... dc) {
        return Attr.ShortForm.addTo(new FooterTag().with(dc), shortAttr);
    }

    /**
     * Creates an HTML {@code <form>} element.
     *
     * @return A FormTag element
     */
    public static FormTag form() {
        return new FormTag();
    }

    /**
     * Creates an HTML {@code <form>} element with the specified text content.
     *
     * @param text The text content
     * @return A FormTag element
     */
    public static FormTag form(final String text) {
        return new FormTag().withText(text);
    }

    /**
     * Creates an HTML {@code <form>} element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return A FormTag element
     */
    public static FormTag form(final AbstractDomContent... dc) {
        return new FormTag().with(dc);
    }

    /**
     * Creates an HTML {@code <form>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A FormTag element
     */
    public static FormTag form(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new FormTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <form>} element with the specified attributes and text content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param text The text content
     * @return A FormTag element
     */
    public static FormTag form(final Attr.ShortForm shortAttr, final String text) {
        return Attr.ShortForm.addTo(new FormTag().withText(text), shortAttr);
    }

    /**
     * Creates an HTML {@code <form>} element with the specified attributes and child DOM content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param dc The child DOM content nodes
     * @return A FormTag element
     */
    public static FormTag form(final Attr.ShortForm shortAttr, final AbstractDomContent... dc) {
        return Attr.ShortForm.addTo(new FormTag().with(dc), shortAttr);
    }

    /**
     * Creates an HTML {@code <h1>} element.
     *
     * @return A H1Tag element
     */
    public static H1Tag h1() {
        return new H1Tag();
    }

    /**
     * Creates an HTML {@code <h1>} element with the specified text content.
     *
     * @param text The text content
     * @return A H1Tag element
     */
    public static H1Tag h1(final String text) {
        return new H1Tag().withText(text);
    }

    /**
     * Creates an HTML {@code <h1>} element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return A H1Tag element
     */
    public static H1Tag h1(final AbstractDomContent... dc) {
        return new H1Tag().with(dc);
    }

    /**
     * Creates an HTML {@code <h1>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A H1Tag element
     */
    public static H1Tag h1(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new H1Tag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <h1>} element with the specified attributes and text content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param text The text content
     * @return A H1Tag element
     */
    public static H1Tag h1(final Attr.ShortForm shortAttr, final String text) {
        return Attr.ShortForm.addTo(new H1Tag().withText(text), shortAttr);
    }

    /**
     * Creates an HTML {@code <h1>} element with the specified attributes and child DOM content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param dc The child DOM content nodes
     * @return A H1Tag element
     */
    public static H1Tag h1(final Attr.ShortForm shortAttr, final AbstractDomContent... dc) {
        return Attr.ShortForm.addTo(new H1Tag().with(dc), shortAttr);
    }

    /**
     * Creates an HTML {@code <h2>} element.
     *
     * @return A H2Tag element
     */
    public static H2Tag h2() {
        return new H2Tag();
    }

    /**
     * Creates an HTML {@code <h2>} element with the specified text content.
     *
     * @param text The text content
     * @return A H2Tag element
     */
    public static H2Tag h2(final String text) {
        return new H2Tag().withText(text);
    }

    /**
     * Creates an HTML {@code <h2>} element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return A H2Tag element
     */
    public static H2Tag h2(final AbstractDomContent... dc) {
        return new H2Tag().with(dc);
    }

    /**
     * Creates an HTML {@code <h2>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A H2Tag element
     */
    public static H2Tag h2(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new H2Tag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <h2>} element with the specified attributes and text content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param text The text content
     * @return A H2Tag element
     */
    public static H2Tag h2(final Attr.ShortForm shortAttr, final String text) {
        return Attr.ShortForm.addTo(new H2Tag().withText(text), shortAttr);
    }

    /**
     * Creates an HTML {@code <h2>} element with the specified attributes and child DOM content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param dc The child DOM content nodes
     * @return A H2Tag element
     */
    public static H2Tag h2(final Attr.ShortForm shortAttr, final AbstractDomContent... dc) {
        return Attr.ShortForm.addTo(new H2Tag().with(dc), shortAttr);
    }

    /**
     * Creates an HTML {@code <h3>} element.
     *
     * @return A H3Tag element
     */
    public static H3Tag h3() {
        return new H3Tag();
    }

    /**
     * Creates an HTML {@code <h3>} element with the specified text content.
     *
     * @param text The text content
     * @return A H3Tag element
     */
    public static H3Tag h3(final String text) {
        return new H3Tag().withText(text);
    }

    /**
     * Creates an HTML {@code <h3>} element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return A H3Tag element
     */
    public static H3Tag h3(final AbstractDomContent... dc) {
        return new H3Tag().with(dc);
    }

    /**
     * Creates an HTML {@code <h3>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A H3Tag element
     */
    public static H3Tag h3(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new H3Tag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <h3>} element with the specified attributes and text content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param text The text content
     * @return A H3Tag element
     */
    public static H3Tag h3(final Attr.ShortForm shortAttr, final String text) {
        return Attr.ShortForm.addTo(new H3Tag().withText(text), shortAttr);
    }

    /**
     * Creates an HTML {@code <h3>} element with the specified attributes and child DOM content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param dc The child DOM content nodes
     * @return A H3Tag element
     */
    public static H3Tag h3(final Attr.ShortForm shortAttr, final AbstractDomContent... dc) {
        return Attr.ShortForm.addTo(new H3Tag().with(dc), shortAttr);
    }

    /**
     * Creates an HTML {@code <h4>} element.
     *
     * @return A H4Tag element
     */
    public static H4Tag h4() {
        return new H4Tag();
    }

    /**
     * Creates an HTML {@code <h4>} element with the specified text content.
     *
     * @param text The text content
     * @return A H4Tag element
     */
    public static H4Tag h4(final String text) {
        return new H4Tag().withText(text);
    }

    /**
     * Creates an HTML {@code <h4>} element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return A H4Tag element
     */
    public static H4Tag h4(final AbstractDomContent... dc) {
        return new H4Tag().with(dc);
    }

    /**
     * Creates an HTML {@code <h4>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A H4Tag element
     */
    public static H4Tag h4(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new H4Tag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <h4>} element with the specified attributes and text content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param text The text content
     * @return A H4Tag element
     */
    public static H4Tag h4(final Attr.ShortForm shortAttr, final String text) {
        return Attr.ShortForm.addTo(new H4Tag().withText(text), shortAttr);
    }

    /**
     * Creates an HTML {@code <h4>} element with the specified attributes and child DOM content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param dc The child DOM content nodes
     * @return A H4Tag element
     */
    public static H4Tag h4(final Attr.ShortForm shortAttr, final AbstractDomContent... dc) {
        return Attr.ShortForm.addTo(new H4Tag().with(dc), shortAttr);
    }

    /**
     * Creates an HTML {@code <h5>} element.
     *
     * @return A H5Tag element
     */
    public static H5Tag h5() {
        return new H5Tag();
    }

    /**
     * Creates an HTML {@code <h5>} element with the specified text content.
     *
     * @param text The text content
     * @return A H5Tag element
     */
    public static H5Tag h5(final String text) {
        return new H5Tag().withText(text);
    }

    /**
     * Creates an HTML {@code <h5>} element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return A H5Tag element
     */
    public static H5Tag h5(final AbstractDomContent... dc) {
        return new H5Tag().with(dc);
    }

    /**
     * Creates an HTML {@code <h5>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A H5Tag element
     */
    public static H5Tag h5(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new H5Tag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <h5>} element with the specified attributes and text content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param text The text content
     * @return A H5Tag element
     */
    public static H5Tag h5(final Attr.ShortForm shortAttr, final String text) {
        return Attr.ShortForm.addTo(new H5Tag().withText(text), shortAttr);
    }

    /**
     * Creates an HTML {@code <h5>} element with the specified attributes and child DOM content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param dc The child DOM content nodes
     * @return A H5Tag element
     */
    public static H5Tag h5(final Attr.ShortForm shortAttr, final AbstractDomContent... dc) {
        return Attr.ShortForm.addTo(new H5Tag().with(dc), shortAttr);
    }

    /**
     * Creates an HTML {@code <h6>} element.
     *
     * @return A H6Tag element
     */
    public static H6Tag h6() {
        return new H6Tag();
    }

    /**
     * Creates an HTML {@code <h6>} element with the specified text content.
     *
     * @param text The text content
     * @return A H6Tag element
     */
    public static H6Tag h6(final String text) {
        return new H6Tag().withText(text);
    }

    /**
     * Creates an HTML {@code <h6>} element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return A H6Tag element
     */
    public static H6Tag h6(final AbstractDomContent... dc) {
        return new H6Tag().with(dc);
    }

    /**
     * Creates an HTML {@code <h6>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A H6Tag element
     */
    public static H6Tag h6(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new H6Tag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <h6>} element with the specified attributes and text content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param text The text content
     * @return A H6Tag element
     */
    public static H6Tag h6(final Attr.ShortForm shortAttr, final String text) {
        return Attr.ShortForm.addTo(new H6Tag().withText(text), shortAttr);
    }

    /**
     * Creates an HTML {@code <h6>} element with the specified attributes and child DOM content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param dc The child DOM content nodes
     * @return A H6Tag element
     */
    public static H6Tag h6(final Attr.ShortForm shortAttr, final AbstractDomContent... dc) {
        return Attr.ShortForm.addTo(new H6Tag().with(dc), shortAttr);
    }

    /**
     * Creates an HTML {@code <header>} element.
     *
     * @return A HeaderTag element
     */
    public static HeaderTag header() {
        return new HeaderTag();
    }

    /**
     * Creates an HTML {@code <header>} element with the specified text content.
     *
     * @param text The text content
     * @return A HeaderTag element
     */
    public static HeaderTag header(final String text) {
        return new HeaderTag().withText(text);
    }

    /**
     * Creates an HTML {@code <header>} element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return A HeaderTag element
     */
    public static HeaderTag header(final AbstractDomContent... dc) {
        return new HeaderTag().with(dc);
    }

    /**
     * Creates an HTML {@code <header>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A HeaderTag element
     */
    public static HeaderTag header(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new HeaderTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <header>} element with the specified attributes and text content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param text The text content
     * @return A HeaderTag element
     */
    public static HeaderTag header(final Attr.ShortForm shortAttr, final String text) {
        return Attr.ShortForm.addTo(new HeaderTag().withText(text), shortAttr);
    }

    /**
     * Creates an HTML {@code <header>} element with the specified attributes and child DOM content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param dc The child DOM content nodes
     * @return A HeaderTag element
     */
    public static HeaderTag header(final Attr.ShortForm shortAttr, final AbstractDomContent... dc) {
        return Attr.ShortForm.addTo(new HeaderTag().with(dc), shortAttr);
    }

    /**
     * Creates an HTML {@code <i>} element.
     *
     * @return A ITag element
     */
    public static ITag i() {
        return new ITag();
    }

    /**
     * Creates an HTML {@code <i>} element with the specified text content.
     *
     * @param text The text content
     * @return A ITag element
     */
    public static ITag i(final String text) {
        return new ITag().withText(text);
    }

    /**
     * Creates an HTML {@code <i>} element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return A ITag element
     */
    public static ITag i(final AbstractDomContent... dc) {
        return new ITag().with(dc);
    }

    /**
     * Creates an HTML {@code <i>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A ITag element
     */
    public static ITag i(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new ITag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <i>} element with the specified attributes and text content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param text The text content
     * @return A ITag element
     */
    public static ITag i(final Attr.ShortForm shortAttr, final String text) {
        return Attr.ShortForm.addTo(new ITag().withText(text), shortAttr);
    }

    /**
     * Creates an HTML {@code <i>} element with the specified attributes and child DOM content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param dc The child DOM content nodes
     * @return A ITag element
     */
    public static ITag i(final Attr.ShortForm shortAttr, final AbstractDomContent... dc) {
        return Attr.ShortForm.addTo(new ITag().with(dc), shortAttr);
    }

    /**
     * Creates an HTML {@code <iframe>} element.
     *
     * @return A IframeTag element
     */
    public static IframeTag iframe() {
        return new IframeTag();
    }

    /**
     * Creates an HTML {@code <iframe>} element with the specified text content.
     *
     * @param text The text content
     * @return A IframeTag element
     */
    public static IframeTag iframe(final String text) {
        return new IframeTag().withText(text);
    }

    /**
     * Creates an HTML {@code <iframe>} element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return A IframeTag element
     */
    public static IframeTag iframe(final AbstractDomContent... dc) {
        return new IframeTag().with(dc);
    }

    /**
     * Creates an HTML {@code <iframe>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A IframeTag element
     */
    public static IframeTag iframe(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new IframeTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <iframe>} element with the specified attributes and text content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param text The text content
     * @return A IframeTag element
     */
    public static IframeTag iframe(final Attr.ShortForm shortAttr, final String text) {
        return Attr.ShortForm.addTo(new IframeTag().withText(text), shortAttr);
    }

    /**
     * Creates an HTML {@code <iframe>} element with the specified attributes and child DOM content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param dc The child DOM content nodes
     * @return A IframeTag element
     */
    public static IframeTag iframe(final Attr.ShortForm shortAttr, final AbstractDomContent... dc) {
        return Attr.ShortForm.addTo(new IframeTag().with(dc), shortAttr);
    }

    /**
     * Creates an HTML {@code <ins>} element.
     *
     * @return A InsTag element
     */
    public static InsTag ins() {
        return new InsTag();
    }

    /**
     * Creates an HTML {@code <ins>} element with the specified text content.
     *
     * @param text The text content
     * @return A InsTag element
     */
    public static InsTag ins(final String text) {
        return new InsTag().withText(text);
    }

    /**
     * Creates an HTML {@code <ins>} element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return A InsTag element
     */
    public static InsTag ins(final AbstractDomContent... dc) {
        return new InsTag().with(dc);
    }

    /**
     * Creates an HTML {@code <ins>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A InsTag element
     */
    public static InsTag ins(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new InsTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <ins>} element with the specified attributes and text content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param text The text content
     * @return A InsTag element
     */
    public static InsTag ins(final Attr.ShortForm shortAttr, final String text) {
        return Attr.ShortForm.addTo(new InsTag().withText(text), shortAttr);
    }

    /**
     * Creates an HTML {@code <ins>} element with the specified attributes and child DOM content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param dc The child DOM content nodes
     * @return A InsTag element
     */
    public static InsTag ins(final Attr.ShortForm shortAttr, final AbstractDomContent... dc) {
        return Attr.ShortForm.addTo(new InsTag().with(dc), shortAttr);
    }

    /**
     * Creates an HTML {@code <kbd>} element.
     *
     * @return A KbdTag element
     */
    public static KbdTag kbd() {
        return new KbdTag();
    }

    /**
     * Creates an HTML {@code <kbd>} element with the specified text content.
     *
     * @param text The text content
     * @return A KbdTag element
     */
    public static KbdTag kbd(final String text) {
        return new KbdTag().withText(text);
    }

    /**
     * Creates an HTML {@code <kbd>} element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return A KbdTag element
     */
    public static KbdTag kbd(final AbstractDomContent... dc) {
        return new KbdTag().with(dc);
    }

    /**
     * Creates an HTML {@code <kbd>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A KbdTag element
     */
    public static KbdTag kbd(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new KbdTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <kbd>} element with the specified attributes and text content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param text The text content
     * @return A KbdTag element
     */
    public static KbdTag kbd(final Attr.ShortForm shortAttr, final String text) {
        return Attr.ShortForm.addTo(new KbdTag().withText(text), shortAttr);
    }

    /**
     * Creates an HTML {@code <kbd>} element with the specified attributes and child DOM content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param dc The child DOM content nodes
     * @return A KbdTag element
     */
    public static KbdTag kbd(final Attr.ShortForm shortAttr, final AbstractDomContent... dc) {
        return Attr.ShortForm.addTo(new KbdTag().with(dc), shortAttr);
    }

    /**
     * Creates an HTML {@code <label>} element.
     *
     * @return A LabelTag element
     */
    public static LabelTag label() {
        return new LabelTag();
    }

    /**
     * Creates an HTML {@code <label>} element with the specified text content.
     *
     * @param text The text content
     * @return A LabelTag element
     */
    public static LabelTag label(final String text) {
        return new LabelTag().withText(text);
    }

    /**
     * Creates an HTML {@code <label>} element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return A LabelTag element
     */
    public static LabelTag label(final AbstractDomContent... dc) {
        return new LabelTag().with(dc);
    }

    /**
     * Creates an HTML {@code <label>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A LabelTag element
     */
    public static LabelTag label(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new LabelTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <label>} element with the specified attributes and text content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param text The text content
     * @return A LabelTag element
     */
    public static LabelTag label(final Attr.ShortForm shortAttr, final String text) {
        return Attr.ShortForm.addTo(new LabelTag().withText(text), shortAttr);
    }

    /**
     * Creates an HTML {@code <label>} element with the specified attributes and child DOM content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param dc The child DOM content nodes
     * @return A LabelTag element
     */
    public static LabelTag label(final Attr.ShortForm shortAttr, final AbstractDomContent... dc) {
        return Attr.ShortForm.addTo(new LabelTag().with(dc), shortAttr);
    }

    /**
     * Creates an HTML {@code <legend>} element.
     *
     * @return A LegendTag element
     */
    public static LegendTag legend() {
        return new LegendTag();
    }

    /**
     * Creates an HTML {@code <legend>} element with the specified text content.
     *
     * @param text The text content
     * @return A LegendTag element
     */
    public static LegendTag legend(final String text) {
        return new LegendTag().withText(text);
    }

    /**
     * Creates an HTML {@code <legend>} element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return A LegendTag element
     */
    public static LegendTag legend(final AbstractDomContent... dc) {
        return new LegendTag().with(dc);
    }

    /**
     * Creates an HTML {@code <legend>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A LegendTag element
     */
    public static LegendTag legend(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new LegendTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <legend>} element with the specified attributes and text content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param text The text content
     * @return A LegendTag element
     */
    public static LegendTag legend(final Attr.ShortForm shortAttr, final String text) {
        return Attr.ShortForm.addTo(new LegendTag().withText(text), shortAttr);
    }

    /**
     * Creates an HTML {@code <legend>} element with the specified attributes and child DOM content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param dc The child DOM content nodes
     * @return A LegendTag element
     */
    public static LegendTag legend(final Attr.ShortForm shortAttr, final AbstractDomContent... dc) {
        return Attr.ShortForm.addTo(new LegendTag().with(dc), shortAttr);
    }

    /**
     * Creates an HTML {@code <li>} element.
     *
     * @return A LiTag element
     */
    public static LiTag li() {
        return new LiTag();
    }

    /**
     * Creates an HTML {@code <li>} element with the specified text content.
     *
     * @param text The text content
     * @return A LiTag element
     */
    public static LiTag li(final String text) {
        return new LiTag().withText(text);
    }

    /**
     * Creates an HTML {@code <li>} element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return A LiTag element
     */
    public static LiTag li(final AbstractDomContent... dc) {
        return new LiTag().with(dc);
    }

    /**
     * Creates an HTML {@code <li>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A LiTag element
     */
    public static LiTag li(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new LiTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <li>} element with the specified attributes and text content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param text The text content
     * @return A LiTag element
     */
    public static LiTag li(final Attr.ShortForm shortAttr, final String text) {
        return Attr.ShortForm.addTo(new LiTag().withText(text), shortAttr);
    }

    /**
     * Creates an HTML {@code <li>} element with the specified attributes and child DOM content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param dc The child DOM content nodes
     * @return A LiTag element
     */
    public static LiTag li(final Attr.ShortForm shortAttr, final AbstractDomContent... dc) {
        return Attr.ShortForm.addTo(new LiTag().with(dc), shortAttr);
    }

    /**
     * Creates an HTML {@code <main>} element.
     *
     * @return A MainTag element
     */
    public static MainTag main() {
        return new MainTag();
    }

    /**
     * Creates an HTML {@code <main>} element with the specified text content.
     *
     * @param text The text content
     * @return A MainTag element
     */
    public static MainTag main(final String text) {
        return new MainTag().withText(text);
    }

    /**
     * Creates an HTML {@code <main>} element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return A MainTag element
     */
    public static MainTag main(final AbstractDomContent... dc) {
        return new MainTag().with(dc);
    }

    /**
     * Creates an HTML {@code <main>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A MainTag element
     */
    public static MainTag main(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new MainTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <main>} element with the specified attributes and text content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param text The text content
     * @return A MainTag element
     */
    public static MainTag main(final Attr.ShortForm shortAttr, final String text) {
        return Attr.ShortForm.addTo(new MainTag().withText(text), shortAttr);
    }

    /**
     * Creates an HTML {@code <main>} element with the specified attributes and child DOM content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param dc The child DOM content nodes
     * @return A MainTag element
     */
    public static MainTag main(final Attr.ShortForm shortAttr, final AbstractDomContent... dc) {
        return Attr.ShortForm.addTo(new MainTag().with(dc), shortAttr);
    }

    /**
     * Creates an HTML {@code <map>} element.
     *
     * @return A MapTag element
     */
    public static MapTag map() {
        return new MapTag();
    }

    /**
     * Creates an HTML {@code <map>} element with the specified text content.
     *
     * @param text The text content
     * @return A MapTag element
     */
    public static MapTag map(final String text) {
        return new MapTag().withText(text);
    }

    /**
     * Creates an HTML {@code <map>} element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return A MapTag element
     */
    public static MapTag map(final AbstractDomContent... dc) {
        return new MapTag().with(dc);
    }

    /**
     * Creates an HTML {@code <map>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A MapTag element
     */
    public static MapTag map(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new MapTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <map>} element with the specified attributes and text content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param text The text content
     * @return A MapTag element
     */
    public static MapTag map(final Attr.ShortForm shortAttr, final String text) {
        return Attr.ShortForm.addTo(new MapTag().withText(text), shortAttr);
    }

    /**
     * Creates an HTML {@code <map>} element with the specified attributes and child DOM content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param dc The child DOM content nodes
     * @return A MapTag element
     */
    public static MapTag map(final Attr.ShortForm shortAttr, final AbstractDomContent... dc) {
        return Attr.ShortForm.addTo(new MapTag().with(dc), shortAttr);
    }

    /**
     * Creates an HTML {@code <mark>} element.
     *
     * @return A MarkTag element
     */
    public static MarkTag mark() {
        return new MarkTag();
    }

    /**
     * Creates an HTML {@code <mark>} element with the specified text content.
     *
     * @param text The text content
     * @return A MarkTag element
     */
    public static MarkTag mark(final String text) {
        return new MarkTag().withText(text);
    }

    /**
     * Creates an HTML {@code <mark>} element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return A MarkTag element
     */
    public static MarkTag mark(final AbstractDomContent... dc) {
        return new MarkTag().with(dc);
    }

    /**
     * Creates an HTML {@code <mark>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A MarkTag element
     */
    public static MarkTag mark(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new MarkTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <mark>} element with the specified attributes and text content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param text The text content
     * @return A MarkTag element
     */
    public static MarkTag mark(final Attr.ShortForm shortAttr, final String text) {
        return Attr.ShortForm.addTo(new MarkTag().withText(text), shortAttr);
    }

    /**
     * Creates an HTML {@code <mark>} element with the specified attributes and child DOM content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param dc The child DOM content nodes
     * @return A MarkTag element
     */
    public static MarkTag mark(final Attr.ShortForm shortAttr, final AbstractDomContent... dc) {
        return Attr.ShortForm.addTo(new MarkTag().with(dc), shortAttr);
    }

    /**
     * Creates an HTML {@code <menu>} element.
     *
     * @return A MenuTag element
     */
    public static MenuTag menu() {
        return new MenuTag();
    }

    /**
     * Creates an HTML {@code <menu>} element with the specified text content.
     *
     * @param text The text content
     * @return A MenuTag element
     */
    public static MenuTag menu(final String text) {
        return new MenuTag().withText(text);
    }

    /**
     * Creates an HTML {@code <menu>} element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return A MenuTag element
     */
    public static MenuTag menu(final AbstractDomContent... dc) {
        return new MenuTag().with(dc);
    }

    /**
     * Creates an HTML {@code <menu>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A MenuTag element
     */
    public static MenuTag menu(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new MenuTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <menu>} element with the specified attributes and text content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param text The text content
     * @return A MenuTag element
     */
    public static MenuTag menu(final Attr.ShortForm shortAttr, final String text) {
        return Attr.ShortForm.addTo(new MenuTag().withText(text), shortAttr);
    }

    /**
     * Creates an HTML {@code <menu>} element with the specified attributes and child DOM content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param dc The child DOM content nodes
     * @return A MenuTag element
     */
    public static MenuTag menu(final Attr.ShortForm shortAttr, final AbstractDomContent... dc) {
        return Attr.ShortForm.addTo(new MenuTag().with(dc), shortAttr);
    }

    /**
     * Creates an HTML {@code <menuitem>} element.
     *
     * @return A MenuitemTag element
     */
    public static MenuitemTag menuitem() {
        return new MenuitemTag();
    }

    /**
     * Creates an HTML {@code <menuitem>} element with the specified text content.
     *
     * @param text The text content
     * @return A MenuitemTag element
     */
    public static MenuitemTag menuitem(final String text) {
        return new MenuitemTag().withText(text);
    }

    /**
     * Creates an HTML {@code <menuitem>} element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return A MenuitemTag element
     */
    public static MenuitemTag menuitem(final AbstractDomContent... dc) {
        return new MenuitemTag().with(dc);
    }

    /**
     * Creates an HTML {@code <menuitem>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A MenuitemTag element
     */
    public static MenuitemTag menuitem(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new MenuitemTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <menuitem>} element with the specified attributes and text content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param text The text content
     * @return A MenuitemTag element
     */
    public static MenuitemTag menuitem(final Attr.ShortForm shortAttr, final String text) {
        return Attr.ShortForm.addTo(new MenuitemTag().withText(text), shortAttr);
    }

    /**
     * Creates an HTML {@code <menuitem>} element with the specified attributes and child DOM content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param dc The child DOM content nodes
     * @return A MenuitemTag element
     */
    public static MenuitemTag menuitem(final Attr.ShortForm shortAttr, final AbstractDomContent... dc) {
        return Attr.ShortForm.addTo(new MenuitemTag().with(dc), shortAttr);
    }

    /**
     * Creates an HTML {@code <meter>} element.
     *
     * @return A MeterTag element
     */
    public static MeterTag meter() {
        return new MeterTag();
    }

    /**
     * Creates an HTML {@code <meter>} element with the specified text content.
     *
     * @param text The text content
     * @return A MeterTag element
     */
    public static MeterTag meter(final String text) {
        return new MeterTag().withText(text);
    }

    /**
     * Creates an HTML {@code <meter>} element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return A MeterTag element
     */
    public static MeterTag meter(final AbstractDomContent... dc) {
        return new MeterTag().with(dc);
    }

    /**
     * Creates an HTML {@code <meter>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A MeterTag element
     */
    public static MeterTag meter(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new MeterTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <meter>} element with the specified attributes and text content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param text The text content
     * @return A MeterTag element
     */
    public static MeterTag meter(final Attr.ShortForm shortAttr, final String text) {
        return Attr.ShortForm.addTo(new MeterTag().withText(text), shortAttr);
    }

    /**
     * Creates an HTML {@code <meter>} element with the specified attributes and child DOM content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param dc The child DOM content nodes
     * @return A MeterTag element
     */
    public static MeterTag meter(final Attr.ShortForm shortAttr, final AbstractDomContent... dc) {
        return Attr.ShortForm.addTo(new MeterTag().with(dc), shortAttr);
    }

    /**
     * Creates an HTML {@code <nav>} element.
     *
     * @return A NavTag element
     */
    public static NavTag nav() {
        return new NavTag();
    }

    /**
     * Creates an HTML {@code <nav>} element with the specified text content.
     *
     * @param text The text content
     * @return A NavTag element
     */
    public static NavTag nav(final String text) {
        return new NavTag().withText(text);
    }

    /**
     * Creates an HTML {@code <nav>} element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return A NavTag element
     */
    public static NavTag nav(final AbstractDomContent... dc) {
        return new NavTag().with(dc);
    }

    /**
     * Creates an HTML {@code <nav>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A NavTag element
     */
    public static NavTag nav(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new NavTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <nav>} element with the specified attributes and text content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param text The text content
     * @return A NavTag element
     */
    public static NavTag nav(final Attr.ShortForm shortAttr, final String text) {
        return Attr.ShortForm.addTo(new NavTag().withText(text), shortAttr);
    }

    /**
     * Creates an HTML {@code <nav>} element with the specified attributes and child DOM content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param dc The child DOM content nodes
     * @return A NavTag element
     */
    public static NavTag nav(final Attr.ShortForm shortAttr, final AbstractDomContent... dc) {
        return Attr.ShortForm.addTo(new NavTag().with(dc), shortAttr);
    }

    /**
     * Creates an HTML {@code <noscript>} element.
     *
     * @return A NoscriptTag element
     */
    public static NoscriptTag noscript() {
        return new NoscriptTag();
    }

    /**
     * Creates an HTML {@code <noscript>} element with the specified text content.
     *
     * @param text The text content
     * @return A NoscriptTag element
     */
    public static NoscriptTag noscript(final String text) {
        return new NoscriptTag().withText(text);
    }

    /**
     * Creates an HTML {@code <noscript>} element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return A NoscriptTag element
     */
    public static NoscriptTag noscript(final AbstractDomContent... dc) {
        return new NoscriptTag().with(dc);
    }

    /**
     * Creates an HTML {@code <noscript>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A NoscriptTag element
     */
    public static NoscriptTag noscript(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new NoscriptTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <noscript>} element with the specified attributes and text content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param text The text content
     * @return A NoscriptTag element
     */
    public static NoscriptTag noscript(final Attr.ShortForm shortAttr, final String text) {
        return Attr.ShortForm.addTo(new NoscriptTag().withText(text), shortAttr);
    }

    /**
     * Creates an HTML {@code <noscript>} element with the specified attributes and child DOM content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param dc The child DOM content nodes
     * @return A NoscriptTag element
     */
    public static NoscriptTag noscript(final Attr.ShortForm shortAttr, final AbstractDomContent... dc) {
        return Attr.ShortForm.addTo(new NoscriptTag().with(dc), shortAttr);
    }

    /**
     * Creates an HTML {@code <object>} element.
     *
     * @return A ObjectTag element
     */
    public static ObjectTag object() {
        return new ObjectTag();
    }

    /**
     * Creates an HTML {@code <object>} element with the specified text content.
     *
     * @param text The text content
     * @return A ObjectTag element
     */
    public static ObjectTag object(final String text) {
        return new ObjectTag().withText(text);
    }

    /**
     * Creates an HTML {@code <object>} element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return A ObjectTag element
     */
    public static ObjectTag object(final AbstractDomContent... dc) {
        return new ObjectTag().with(dc);
    }

    /**
     * Creates an HTML {@code <object>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A ObjectTag element
     */
    public static ObjectTag object(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new ObjectTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <object>} element with the specified attributes and text content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param text The text content
     * @return A ObjectTag element
     */
    public static ObjectTag object(final Attr.ShortForm shortAttr, final String text) {
        return Attr.ShortForm.addTo(new ObjectTag().withText(text), shortAttr);
    }

    /**
     * Creates an HTML {@code <object>} element with the specified attributes and child DOM content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param dc The child DOM content nodes
     * @return A ObjectTag element
     */
    public static ObjectTag object(final Attr.ShortForm shortAttr, final AbstractDomContent... dc) {
        return Attr.ShortForm.addTo(new ObjectTag().with(dc), shortAttr);
    }

    /**
     * Creates an HTML {@code <ol>} element.
     *
     * @return A OlTag element
     */
    public static OlTag ol() {
        return new OlTag();
    }

    /**
     * Creates an HTML {@code <ol>} element with the specified text content.
     *
     * @param text The text content
     * @return A OlTag element
     */
    public static OlTag ol(final String text) {
        return new OlTag().withText(text);
    }

    /**
     * Creates an HTML {@code <ol>} element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return A OlTag element
     */
    public static OlTag ol(final AbstractDomContent... dc) {
        return new OlTag().with(dc);
    }

    /**
     * Creates an HTML {@code <ol>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A OlTag element
     */
    public static OlTag ol(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new OlTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <ol>} element with the specified attributes and text content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param text The text content
     * @return A OlTag element
     */
    public static OlTag ol(final Attr.ShortForm shortAttr, final String text) {
        return Attr.ShortForm.addTo(new OlTag().withText(text), shortAttr);
    }

    /**
     * Creates an HTML {@code <ol>} element with the specified attributes and child DOM content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param dc The child DOM content nodes
     * @return A OlTag element
     */
    public static OlTag ol(final Attr.ShortForm shortAttr, final AbstractDomContent... dc) {
        return Attr.ShortForm.addTo(new OlTag().with(dc), shortAttr);
    }

    /**
     * Creates an HTML {@code <optgroup>} element.
     *
     * @return A OptgroupTag element
     */
    public static OptgroupTag optgroup() {
        return new OptgroupTag();
    }

    /**
     * Creates an HTML {@code <optgroup>} element with the specified text content.
     *
     * @param text The text content
     * @return A OptgroupTag element
     */
    public static OptgroupTag optgroup(final String text) {
        return new OptgroupTag().withText(text);
    }

    /**
     * Creates an HTML {@code <optgroup>} element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return A OptgroupTag element
     */
    public static OptgroupTag optgroup(final AbstractDomContent... dc) {
        return new OptgroupTag().with(dc);
    }

    /**
     * Creates an HTML {@code <optgroup>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A OptgroupTag element
     */
    public static OptgroupTag optgroup(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new OptgroupTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <optgroup>} element with the specified attributes and text content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param text The text content
     * @return A OptgroupTag element
     */
    public static OptgroupTag optgroup(final Attr.ShortForm shortAttr, final String text) {
        return Attr.ShortForm.addTo(new OptgroupTag().withText(text), shortAttr);
    }

    /**
     * Creates an HTML {@code <optgroup>} element with the specified attributes and child DOM content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param dc The child DOM content nodes
     * @return A OptgroupTag element
     */
    public static OptgroupTag optgroup(final Attr.ShortForm shortAttr, final AbstractDomContent... dc) {
        return Attr.ShortForm.addTo(new OptgroupTag().with(dc), shortAttr);
    }

    /**
     * Creates an HTML {@code <option>} element.
     *
     * @return A OptionTag element
     */
    public static OptionTag option() {
        return new OptionTag();
    }

    /**
     * Creates an HTML {@code <option>} element with the specified text content.
     *
     * @param text The text content
     * @return A OptionTag element
     */
    public static OptionTag option(final String text) {
        return new OptionTag().withText(text);
    }

    /**
     * Creates an HTML {@code <option>} element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return A OptionTag element
     */
    public static OptionTag option(final AbstractDomContent... dc) {
        return new OptionTag().with(dc);
    }

    /**
     * Creates an HTML {@code <option>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A OptionTag element
     */
    public static OptionTag option(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new OptionTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <option>} element with the specified attributes and text content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param text The text content
     * @return A OptionTag element
     */
    public static OptionTag option(final Attr.ShortForm shortAttr, final String text) {
        return Attr.ShortForm.addTo(new OptionTag().withText(text), shortAttr);
    }

    /**
     * Creates an HTML {@code <option>} element with the specified attributes and child DOM content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param dc The child DOM content nodes
     * @return A OptionTag element
     */
    public static OptionTag option(final Attr.ShortForm shortAttr, final AbstractDomContent... dc) {
        return Attr.ShortForm.addTo(new OptionTag().with(dc), shortAttr);
    }

    /**
     * Creates an HTML {@code <output>} element.
     *
     * @return A OutputTag element
     */
    public static OutputTag output() {
        return new OutputTag();
    }

    /**
     * Creates an HTML {@code <output>} element with the specified text content.
     *
     * @param text The text content
     * @return A OutputTag element
     */
    public static OutputTag output(final String text) {
        return new OutputTag().withText(text);
    }

    /**
     * Creates an HTML {@code <output>} element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return A OutputTag element
     */
    public static OutputTag output(final AbstractDomContent... dc) {
        return new OutputTag().with(dc);
    }

    /**
     * Creates an HTML {@code <output>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A OutputTag element
     */
    public static OutputTag output(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new OutputTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <output>} element with the specified attributes and text content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param text The text content
     * @return A OutputTag element
     */
    public static OutputTag output(final Attr.ShortForm shortAttr, final String text) {
        return Attr.ShortForm.addTo(new OutputTag().withText(text), shortAttr);
    }

    /**
     * Creates an HTML {@code <output>} element with the specified attributes and child DOM content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param dc The child DOM content nodes
     * @return A OutputTag element
     */
    public static OutputTag output(final Attr.ShortForm shortAttr, final AbstractDomContent... dc) {
        return Attr.ShortForm.addTo(new OutputTag().with(dc), shortAttr);
    }

    /**
     * Creates an HTML {@code <p>} element.
     *
     * @return A PTag element
     */
    public static PTag p() {
        return new PTag();
    }

    /**
     * Creates an HTML {@code <p>} element with the specified text content.
     *
     * @param text The text content
     * @return A PTag element
     */
    public static PTag p(final String text) {
        return new PTag().withText(text);
    }

    /**
     * Creates an HTML {@code <p>} element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return A PTag element
     */
    public static PTag p(final AbstractDomContent... dc) {
        return new PTag().with(dc);
    }

    /**
     * Creates an HTML {@code <p>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A PTag element
     */
    public static PTag p(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new PTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <p>} element with the specified attributes and text content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param text The text content
     * @return A PTag element
     */
    public static PTag p(final Attr.ShortForm shortAttr, final String text) {
        return Attr.ShortForm.addTo(new PTag().withText(text), shortAttr);
    }

    /**
     * Creates an HTML {@code <p>} element with the specified attributes and child DOM content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param dc The child DOM content nodes
     * @return A PTag element
     */
    public static PTag p(final Attr.ShortForm shortAttr, final AbstractDomContent... dc) {
        return Attr.ShortForm.addTo(new PTag().with(dc), shortAttr);
    }

    /**
     * Creates an HTML {@code <picture>} element.
     *
     * @return A PictureTag element
     */
    public static PictureTag picture() {
        return new PictureTag();
    }

    /**
     * Creates an HTML {@code <picture>} element with the specified text content.
     *
     * @param text The text content
     * @return A PictureTag element
     */
    public static PictureTag picture(final String text) {
        return new PictureTag().withText(text);
    }

    /**
     * Creates an HTML {@code <picture>} element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return A PictureTag element
     */
    public static PictureTag picture(final AbstractDomContent... dc) {
        return new PictureTag().with(dc);
    }

    /**
     * Creates an HTML {@code <picture>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A PictureTag element
     */
    public static PictureTag picture(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new PictureTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <picture>} element with the specified attributes and text content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param text The text content
     * @return A PictureTag element
     */
    public static PictureTag picture(final Attr.ShortForm shortAttr, final String text) {
        return Attr.ShortForm.addTo(new PictureTag().withText(text), shortAttr);
    }

    /**
     * Creates an HTML {@code <picture>} element with the specified attributes and child DOM content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param dc The child DOM content nodes
     * @return A PictureTag element
     */
    public static PictureTag picture(final Attr.ShortForm shortAttr, final AbstractDomContent... dc) {
        return Attr.ShortForm.addTo(new PictureTag().with(dc), shortAttr);
    }

    /**
     * Creates an HTML {@code <pre>} element.
     *
     * @return A PreTag element
     */
    public static PreTag pre() {
        return new PreTag();
    }

    /**
     * Creates an HTML {@code <pre>} element with the specified text content.
     *
     * @param text The text content
     * @return A PreTag element
     */
    public static PreTag pre(final String text) {
        return new PreTag().withText(text);
    }

    /**
     * Creates an HTML {@code <pre>} element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return A PreTag element
     */
    public static PreTag pre(final AbstractDomContent... dc) {
        return new PreTag().with(dc);
    }

    /**
     * Creates an HTML {@code <pre>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A PreTag element
     */
    public static PreTag pre(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new PreTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <pre>} element with the specified attributes and text content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param text The text content
     * @return A PreTag element
     */
    public static PreTag pre(final Attr.ShortForm shortAttr, final String text) {
        return Attr.ShortForm.addTo(new PreTag().withText(text), shortAttr);
    }

    /**
     * Creates an HTML {@code <pre>} element with the specified attributes and child DOM content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param dc The child DOM content nodes
     * @return A PreTag element
     */
    public static PreTag pre(final Attr.ShortForm shortAttr, final AbstractDomContent... dc) {
        return Attr.ShortForm.addTo(new PreTag().with(dc), shortAttr);
    }

    /**
     * Creates an HTML {@code <progress>} element.
     *
     * @return A ProgressTag element
     */
    public static ProgressTag progress() {
        return new ProgressTag();
    }

    /**
     * Creates an HTML {@code <progress>} element with the specified text content.
     *
     * @param text The text content
     * @return A ProgressTag element
     */
    public static ProgressTag progress(final String text) {
        return new ProgressTag().withText(text);
    }

    /**
     * Creates an HTML {@code <progress>} element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return A ProgressTag element
     */
    public static ProgressTag progress(final AbstractDomContent... dc) {
        return new ProgressTag().with(dc);
    }

    /**
     * Creates an HTML {@code <progress>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A ProgressTag element
     */
    public static ProgressTag progress(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new ProgressTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <progress>} element with the specified attributes and text content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param text The text content
     * @return A ProgressTag element
     */
    public static ProgressTag progress(final Attr.ShortForm shortAttr, final String text) {
        return Attr.ShortForm.addTo(new ProgressTag().withText(text), shortAttr);
    }

    /**
     * Creates an HTML {@code <progress>} element with the specified attributes and child DOM content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param dc The child DOM content nodes
     * @return A ProgressTag element
     */
    public static ProgressTag progress(final Attr.ShortForm shortAttr, final AbstractDomContent... dc) {
        return Attr.ShortForm.addTo(new ProgressTag().with(dc), shortAttr);
    }

    /**
     * Creates an HTML {@code <q>} element.
     *
     * @return A QTag element
     */
    public static QTag q() {
        return new QTag();
    }

    /**
     * Creates an HTML {@code <q>} element with the specified text content.
     *
     * @param text The text content
     * @return A QTag element
     */
    public static QTag q(final String text) {
        return new QTag().withText(text);
    }

    /**
     * Creates an HTML {@code <q>} element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return A QTag element
     */
    public static QTag q(final AbstractDomContent... dc) {
        return new QTag().with(dc);
    }

    /**
     * Creates an HTML {@code <q>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A QTag element
     */
    public static QTag q(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new QTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <q>} element with the specified attributes and text content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param text The text content
     * @return A QTag element
     */
    public static QTag q(final Attr.ShortForm shortAttr, final String text) {
        return Attr.ShortForm.addTo(new QTag().withText(text), shortAttr);
    }

    /**
     * Creates an HTML {@code <q>} element with the specified attributes and child DOM content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param dc The child DOM content nodes
     * @return A QTag element
     */
    public static QTag q(final Attr.ShortForm shortAttr, final AbstractDomContent... dc) {
        return Attr.ShortForm.addTo(new QTag().with(dc), shortAttr);
    }

    /**
     * Creates an HTML {@code <rp>} element.
     *
     * @return A RpTag element
     */
    public static RpTag rp() {
        return new RpTag();
    }

    /**
     * Creates an HTML {@code <rp>} element with the specified text content.
     *
     * @param text The text content
     * @return A RpTag element
     */
    public static RpTag rp(final String text) {
        return new RpTag().withText(text);
    }

    /**
     * Creates an HTML {@code <rp>} element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return A RpTag element
     */
    public static RpTag rp(final AbstractDomContent... dc) {
        return new RpTag().with(dc);
    }

    /**
     * Creates an HTML {@code <rp>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A RpTag element
     */
    public static RpTag rp(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new RpTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <rp>} element with the specified attributes and text content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param text The text content
     * @return A RpTag element
     */
    public static RpTag rp(final Attr.ShortForm shortAttr, final String text) {
        return Attr.ShortForm.addTo(new RpTag().withText(text), shortAttr);
    }

    /**
     * Creates an HTML {@code <rp>} element with the specified attributes and child DOM content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param dc The child DOM content nodes
     * @return A RpTag element
     */
    public static RpTag rp(final Attr.ShortForm shortAttr, final AbstractDomContent... dc) {
        return Attr.ShortForm.addTo(new RpTag().with(dc), shortAttr);
    }

    /**
     * Creates an HTML {@code <rt>} element.
     *
     * @return A RtTag element
     */
    public static RtTag rt() {
        return new RtTag();
    }

    /**
     * Creates an HTML {@code <rt>} element with the specified text content.
     *
     * @param text The text content
     * @return A RtTag element
     */
    public static RtTag rt(final String text) {
        return new RtTag().withText(text);
    }

    /**
     * Creates an HTML {@code <rt>} element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return A RtTag element
     */
    public static RtTag rt(final AbstractDomContent... dc) {
        return new RtTag().with(dc);
    }

    /**
     * Creates an HTML {@code <rt>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A RtTag element
     */
    public static RtTag rt(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new RtTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <rt>} element with the specified attributes and text content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param text The text content
     * @return A RtTag element
     */
    public static RtTag rt(final Attr.ShortForm shortAttr, final String text) {
        return Attr.ShortForm.addTo(new RtTag().withText(text), shortAttr);
    }

    /**
     * Creates an HTML {@code <rt>} element with the specified attributes and child DOM content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param dc The child DOM content nodes
     * @return A RtTag element
     */
    public static RtTag rt(final Attr.ShortForm shortAttr, final AbstractDomContent... dc) {
        return Attr.ShortForm.addTo(new RtTag().with(dc), shortAttr);
    }

    /**
     * Creates an HTML {@code <ruby>} element.
     *
     * @return A RubyTag element
     */
    public static RubyTag ruby() {
        return new RubyTag();
    }

    /**
     * Creates an HTML {@code <ruby>} element with the specified text content.
     *
     * @param text The text content
     * @return A RubyTag element
     */
    public static RubyTag ruby(final String text) {
        return new RubyTag().withText(text);
    }

    /**
     * Creates an HTML {@code <ruby>} element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return A RubyTag element
     */
    public static RubyTag ruby(final AbstractDomContent... dc) {
        return new RubyTag().with(dc);
    }

    /**
     * Creates an HTML {@code <ruby>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A RubyTag element
     */
    public static RubyTag ruby(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new RubyTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <ruby>} element with the specified attributes and text content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param text The text content
     * @return A RubyTag element
     */
    public static RubyTag ruby(final Attr.ShortForm shortAttr, final String text) {
        return Attr.ShortForm.addTo(new RubyTag().withText(text), shortAttr);
    }

    /**
     * Creates an HTML {@code <ruby>} element with the specified attributes and child DOM content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param dc The child DOM content nodes
     * @return A RubyTag element
     */
    public static RubyTag ruby(final Attr.ShortForm shortAttr, final AbstractDomContent... dc) {
        return Attr.ShortForm.addTo(new RubyTag().with(dc), shortAttr);
    }

    /**
     * Creates an HTML {@code <s>} element.
     *
     * @return A STag element
     */
    public static STag s() {
        return new STag();
    }

    /**
     * Creates an HTML {@code <s>} element with the specified text content.
     *
     * @param text The text content
     * @return A STag element
     */
    public static STag s(final String text) {
        return new STag().withText(text);
    }

    /**
     * Creates an HTML {@code <s>} element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return A STag element
     */
    public static STag s(final AbstractDomContent... dc) {
        return new STag().with(dc);
    }

    /**
     * Creates an HTML {@code <s>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A STag element
     */
    public static STag s(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new STag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <s>} element with the specified attributes and text content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param text The text content
     * @return A STag element
     */
    public static STag s(final Attr.ShortForm shortAttr, final String text) {
        return Attr.ShortForm.addTo(new STag().withText(text), shortAttr);
    }

    /**
     * Creates an HTML {@code <s>} element with the specified attributes and child DOM content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param dc The child DOM content nodes
     * @return A STag element
     */
    public static STag s(final Attr.ShortForm shortAttr, final AbstractDomContent... dc) {
        return Attr.ShortForm.addTo(new STag().with(dc), shortAttr);
    }

    /**
     * Creates an HTML {@code <samp>} element.
     *
     * @return A SampTag element
     */
    public static SampTag samp() {
        return new SampTag();
    }

    /**
     * Creates an HTML {@code <samp>} element with the specified text content.
     *
     * @param text The text content
     * @return A SampTag element
     */
    public static SampTag samp(final String text) {
        return new SampTag().withText(text);
    }

    /**
     * Creates an HTML {@code <samp>} element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return A SampTag element
     */
    public static SampTag samp(final AbstractDomContent... dc) {
        return new SampTag().with(dc);
    }

    /**
     * Creates an HTML {@code <samp>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A SampTag element
     */
    public static SampTag samp(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new SampTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <samp>} element with the specified attributes and text content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param text The text content
     * @return A SampTag element
     */
    public static SampTag samp(final Attr.ShortForm shortAttr, final String text) {
        return Attr.ShortForm.addTo(new SampTag().withText(text), shortAttr);
    }

    /**
     * Creates an HTML {@code <samp>} element with the specified attributes and child DOM content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param dc The child DOM content nodes
     * @return A SampTag element
     */
    public static SampTag samp(final Attr.ShortForm shortAttr, final AbstractDomContent... dc) {
        return Attr.ShortForm.addTo(new SampTag().with(dc), shortAttr);
    }

    /**
     * Creates an HTML {@code <script>} element.
     *
     * @return A ScriptTag element
     */
    public static ScriptTag script() {
        return new ScriptTag();
    }

    /**
     * Creates an HTML {@code <script>} element with the specified text content.
     *
     * @param text The text content
     * @return A ScriptTag element
     */
    public static ScriptTag script(final String text) {
        return new ScriptTag().with(new UnescapedText(text));
    }

    /**
     * Creates an HTML {@code <script>} element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return A ScriptTag element
     */
    public static ScriptTag script(final AbstractDomContent... dc) {
        return new ScriptTag().with(dc);
    }

    /**
     * Creates an HTML {@code <script>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A ScriptTag element
     */
    public static ScriptTag script(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new ScriptTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <script>} element with the specified attributes and text content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param text The text content
     * @return A ScriptTag element
     */
    public static ScriptTag script(final Attr.ShortForm shortAttr, final String text) {
        return Attr.ShortForm.addTo(new ScriptTag().with(new UnescapedText(text)), shortAttr);
    }

    /**
     * Creates an HTML {@code <script>} element with the specified attributes and child DOM content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param dc The child DOM content nodes
     * @return A ScriptTag element
     */
    public static ScriptTag script(final Attr.ShortForm shortAttr, final AbstractDomContent... dc) {
        return Attr.ShortForm.addTo(new ScriptTag().with(dc), shortAttr);
    }

    /**
     * Creates an HTML {@code <section>} element.
     *
     * @return A SectionTag element
     */
    public static SectionTag section() {
        return new SectionTag();
    }

    /**
     * Creates an HTML {@code <section>} element with the specified text content.
     *
     * @param text The text content
     * @return A SectionTag element
     */
    public static SectionTag section(final String text) {
        return new SectionTag().withText(text);
    }

    /**
     * Creates an HTML {@code <section>} element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return A SectionTag element
     */
    public static SectionTag section(final AbstractDomContent... dc) {
        return new SectionTag().with(dc);
    }

    /**
     * Creates an HTML {@code <section>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A SectionTag element
     */
    public static SectionTag section(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new SectionTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <section>} element with the specified attributes and text content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param text The text content
     * @return A SectionTag element
     */
    public static SectionTag section(final Attr.ShortForm shortAttr, final String text) {
        return Attr.ShortForm.addTo(new SectionTag().withText(text), shortAttr);
    }

    /**
     * Creates an HTML {@code <section>} element with the specified attributes and child DOM content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param dc The child DOM content nodes
     * @return A SectionTag element
     */
    public static SectionTag section(final Attr.ShortForm shortAttr, final AbstractDomContent... dc) {
        return Attr.ShortForm.addTo(new SectionTag().with(dc), shortAttr);
    }

    /**
     * Creates an HTML {@code <select>} element.
     *
     * @return A SelectTag element
     */
    public static SelectTag select() {
        return new SelectTag();
    }

    /**
     * Creates an HTML {@code <select>} element with the specified text content.
     *
     * @param text The text content
     * @return A SelectTag element
     */
    public static SelectTag select(final String text) {
        return new SelectTag().withText(text);
    }

    /**
     * Creates an HTML {@code <select>} element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return A SelectTag element
     */
    public static SelectTag select(final AbstractDomContent... dc) {
        return new SelectTag().with(dc);
    }

    /**
     * Creates an HTML {@code <select>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A SelectTag element
     */
    public static SelectTag select(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new SelectTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <select>} element with the specified attributes and text content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param text The text content
     * @return A SelectTag element
     */
    public static SelectTag select(final Attr.ShortForm shortAttr, final String text) {
        return Attr.ShortForm.addTo(new SelectTag().withText(text), shortAttr);
    }

    /**
     * Creates an HTML {@code <select>} element with the specified attributes and child DOM content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param dc The child DOM content nodes
     * @return A SelectTag element
     */
    public static SelectTag select(final Attr.ShortForm shortAttr, final AbstractDomContent... dc) {
        return Attr.ShortForm.addTo(new SelectTag().with(dc), shortAttr);
    }

    /**
     * Creates an HTML {@code <slot>} element.
     *
     * @return A SlotTag element
     */
    public static SlotTag slot() {
        return new SlotTag();
    }

    /**
     * Creates an HTML {@code <slot>} element with the specified text content.
     *
     * @param text The text content
     * @return A SlotTag element
     */
    public static SlotTag slot(final String text) {
        return new SlotTag().withText(text);
    }

    /**
     * Creates an HTML {@code <slot>} element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return A SlotTag element
     */
    public static SlotTag slot(final AbstractDomContent... dc) {
        return new SlotTag().with(dc);
    }

    /**
     * Creates an HTML {@code <slot>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A SlotTag element
     */
    public static SlotTag slot(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new SlotTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <slot>} element with the specified attributes and text content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param text The text content
     * @return A SlotTag element
     */
    public static SlotTag slot(final Attr.ShortForm shortAttr, final String text) {
        return Attr.ShortForm.addTo(new SlotTag().withText(text), shortAttr);
    }

    /**
     * Creates an HTML {@code <slot>} element with the specified attributes and child DOM content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param dc The child DOM content nodes
     * @return A SlotTag element
     */
    public static SlotTag slot(final Attr.ShortForm shortAttr, final AbstractDomContent... dc) {
        return Attr.ShortForm.addTo(new SlotTag().with(dc), shortAttr);
    }

    /**
     * Creates an HTML {@code <small>} element.
     *
     * @return A SmallTag element
     */
    public static SmallTag small() {
        return new SmallTag();
    }

    /**
     * Creates an HTML {@code <small>} element with the specified text content.
     *
     * @param text The text content
     * @return A SmallTag element
     */
    public static SmallTag small(final String text) {
        return new SmallTag().withText(text);
    }

    /**
     * Creates an HTML {@code <small>} element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return A SmallTag element
     */
    public static SmallTag small(final AbstractDomContent... dc) {
        return new SmallTag().with(dc);
    }

    /**
     * Creates an HTML {@code <small>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A SmallTag element
     */
    public static SmallTag small(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new SmallTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <small>} element with the specified attributes and text content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param text The text content
     * @return A SmallTag element
     */
    public static SmallTag small(final Attr.ShortForm shortAttr, final String text) {
        return Attr.ShortForm.addTo(new SmallTag().withText(text), shortAttr);
    }

    /**
     * Creates an HTML {@code <small>} element with the specified attributes and child DOM content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param dc The child DOM content nodes
     * @return A SmallTag element
     */
    public static SmallTag small(final Attr.ShortForm shortAttr, final AbstractDomContent... dc) {
        return Attr.ShortForm.addTo(new SmallTag().with(dc), shortAttr);
    }

    /**
     * Creates an HTML {@code <span>} element.
     *
     * @return A SpanTag element
     */
    public static SpanTag span() {
        return new SpanTag();
    }

    /**
     * Creates an HTML {@code <span>} element with the specified text content.
     *
     * @param text The text content
     * @return A SpanTag element
     */
    public static SpanTag span(final String text) {
        return new SpanTag().withText(text);
    }

    /**
     * Creates an HTML {@code <span>} element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return A SpanTag element
     */
    public static SpanTag span(final AbstractDomContent... dc) {
        return new SpanTag().with(dc);
    }

    /**
     * Creates an HTML {@code <span>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A SpanTag element
     */
    public static SpanTag span(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new SpanTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <span>} element with the specified attributes and text content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param text The text content
     * @return A SpanTag element
     */
    public static SpanTag span(final Attr.ShortForm shortAttr, final String text) {
        return Attr.ShortForm.addTo(new SpanTag().withText(text), shortAttr);
    }

    /**
     * Creates an HTML {@code <span>} element with the specified attributes and child DOM content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param dc The child DOM content nodes
     * @return A SpanTag element
     */
    public static SpanTag span(final Attr.ShortForm shortAttr, final AbstractDomContent... dc) {
        return Attr.ShortForm.addTo(new SpanTag().with(dc), shortAttr);
    }

    /**
     * Creates an HTML {@code <strong>} element.
     *
     * @return A StrongTag element
     */
    public static StrongTag strong() {
        return new StrongTag();
    }

    /**
     * Creates an HTML {@code <strong>} element with the specified text content.
     *
     * @param text The text content
     * @return A StrongTag element
     */
    public static StrongTag strong(final String text) {
        return new StrongTag().withText(text);
    }

    /**
     * Creates an HTML {@code <strong>} element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return A StrongTag element
     */
    public static StrongTag strong(final AbstractDomContent... dc) {
        return new StrongTag().with(dc);
    }

    /**
     * Creates an HTML {@code <strong>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A StrongTag element
     */
    public static StrongTag strong(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new StrongTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <strong>} element with the specified attributes and text content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param text The text content
     * @return A StrongTag element
     */
    public static StrongTag strong(final Attr.ShortForm shortAttr, final String text) {
        return Attr.ShortForm.addTo(new StrongTag().withText(text), shortAttr);
    }

    /**
     * Creates an HTML {@code <strong>} element with the specified attributes and child DOM content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param dc The child DOM content nodes
     * @return A StrongTag element
     */
    public static StrongTag strong(final Attr.ShortForm shortAttr, final AbstractDomContent... dc) {
        return Attr.ShortForm.addTo(new StrongTag().with(dc), shortAttr);
    }

    /**
     * Creates an HTML {@code <style>} element.
     *
     * @return A StyleTag element
     */
    public static StyleTag style() {
        return new StyleTag();
    }

    /**
     * Creates an HTML {@code <style>} element with the specified text content.
     *
     * @param text The text content
     * @return A StyleTag element
     */
    public static StyleTag style(final String text) {
        return new StyleTag().with(new UnescapedText(text));
    }

    /**
     * Creates an HTML {@code <style>} element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return A StyleTag element
     */
    public static StyleTag style(final AbstractDomContent... dc) {
        return new StyleTag().with(dc);
    }

    /**
     * Creates an HTML {@code <style>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A StyleTag element
     */
    public static StyleTag style(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new StyleTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <style>} element with the specified attributes and text content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param text The text content
     * @return A StyleTag element
     */
    public static StyleTag style(final Attr.ShortForm shortAttr, final String text) {
        return Attr.ShortForm.addTo(new StyleTag().with(new UnescapedText(text)), shortAttr);
    }

    /**
     * Creates an HTML {@code <style>} element with the specified attributes and child DOM content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param dc The child DOM content nodes
     * @return A StyleTag element
     */
    public static StyleTag style(final Attr.ShortForm shortAttr, final AbstractDomContent... dc) {
        return Attr.ShortForm.addTo(new StyleTag().with(dc), shortAttr);
    }

    /**
     * Creates an HTML {@code <sub>} element.
     *
     * @return A SubTag element
     */
    public static SubTag sub() {
        return new SubTag();
    }

    /**
     * Creates an HTML {@code <sub>} element with the specified text content.
     *
     * @param text The text content
     * @return A SubTag element
     */
    public static SubTag sub(final String text) {
        return new SubTag().withText(text);
    }

    /**
     * Creates an HTML {@code <sub>} element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return A SubTag element
     */
    public static SubTag sub(final AbstractDomContent... dc) {
        return new SubTag().with(dc);
    }

    /**
     * Creates an HTML {@code <sub>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A SubTag element
     */
    public static SubTag sub(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new SubTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <sub>} element with the specified attributes and text content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param text The text content
     * @return A SubTag element
     */
    public static SubTag sub(final Attr.ShortForm shortAttr, final String text) {
        return Attr.ShortForm.addTo(new SubTag().withText(text), shortAttr);
    }

    /**
     * Creates an HTML {@code <sub>} element with the specified attributes and child DOM content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param dc The child DOM content nodes
     * @return A SubTag element
     */
    public static SubTag sub(final Attr.ShortForm shortAttr, final AbstractDomContent... dc) {
        return Attr.ShortForm.addTo(new SubTag().with(dc), shortAttr);
    }

    /**
     * Creates an HTML {@code <summary>} element.
     *
     * @return A SummaryTag element
     */
    public static SummaryTag summary() {
        return new SummaryTag();
    }

    /**
     * Creates an HTML {@code <summary>} element with the specified text content.
     *
     * @param text The text content
     * @return A SummaryTag element
     */
    public static SummaryTag summary(final String text) {
        return new SummaryTag().withText(text);
    }

    /**
     * Creates an HTML {@code <summary>} element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return A SummaryTag element
     */
    public static SummaryTag summary(final AbstractDomContent... dc) {
        return new SummaryTag().with(dc);
    }

    /**
     * Creates an HTML {@code <summary>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A SummaryTag element
     */
    public static SummaryTag summary(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new SummaryTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <summary>} element with the specified attributes and text content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param text The text content
     * @return A SummaryTag element
     */
    public static SummaryTag summary(final Attr.ShortForm shortAttr, final String text) {
        return Attr.ShortForm.addTo(new SummaryTag().withText(text), shortAttr);
    }

    /**
     * Creates an HTML {@code <summary>} element with the specified attributes and child DOM content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param dc The child DOM content nodes
     * @return A SummaryTag element
     */
    public static SummaryTag summary(final Attr.ShortForm shortAttr, final AbstractDomContent... dc) {
        return Attr.ShortForm.addTo(new SummaryTag().with(dc), shortAttr);
    }

    /**
     * Creates an HTML {@code <sup>} element.
     *
     * @return A SupTag element
     */
    public static SupTag sup() {
        return new SupTag();
    }

    /**
     * Creates an HTML {@code <sup>} element with the specified text content.
     *
     * @param text The text content
     * @return A SupTag element
     */
    public static SupTag sup(final String text) {
        return new SupTag().withText(text);
    }

    /**
     * Creates an HTML {@code <sup>} element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return A SupTag element
     */
    public static SupTag sup(final AbstractDomContent... dc) {
        return new SupTag().with(dc);
    }

    /**
     * Creates an HTML {@code <sup>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A SupTag element
     */
    public static SupTag sup(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new SupTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <sup>} element with the specified attributes and text content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param text The text content
     * @return A SupTag element
     */
    public static SupTag sup(final Attr.ShortForm shortAttr, final String text) {
        return Attr.ShortForm.addTo(new SupTag().withText(text), shortAttr);
    }

    /**
     * Creates an HTML {@code <sup>} element with the specified attributes and child DOM content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param dc The child DOM content nodes
     * @return A SupTag element
     */
    public static SupTag sup(final Attr.ShortForm shortAttr, final AbstractDomContent... dc) {
        return Attr.ShortForm.addTo(new SupTag().with(dc), shortAttr);
    }

    /**
     * Creates an HTML {@code <table>} element.
     *
     * @return A TableTag element
     */
    public static TableTag table() {
        return new TableTag();
    }

    /**
     * Creates an HTML {@code <table>} element with the specified text content.
     *
     * @param text The text content
     * @return A TableTag element
     */
    public static TableTag table(final String text) {
        return new TableTag().withText(text);
    }

    /**
     * Creates an HTML {@code <table>} element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return A TableTag element
     */
    public static TableTag table(final AbstractDomContent... dc) {
        return new TableTag().with(dc);
    }

    /**
     * Creates an HTML {@code <table>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A TableTag element
     */
    public static TableTag table(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new TableTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <table>} element with the specified attributes and text content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param text The text content
     * @return A TableTag element
     */
    public static TableTag table(final Attr.ShortForm shortAttr, final String text) {
        return Attr.ShortForm.addTo(new TableTag().withText(text), shortAttr);
    }

    /**
     * Creates an HTML {@code <table>} element with the specified attributes and child DOM content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param dc The child DOM content nodes
     * @return A TableTag element
     */
    public static TableTag table(final Attr.ShortForm shortAttr, final AbstractDomContent... dc) {
        return Attr.ShortForm.addTo(new TableTag().with(dc), shortAttr);
    }

    /**
     * Creates an HTML {@code <tbody>} element.
     *
     * @return A TbodyTag element
     */
    public static TbodyTag tbody() {
        return new TbodyTag();
    }

    /**
     * Creates an HTML {@code <tbody>} element with the specified text content.
     *
     * @param text The text content
     * @return A TbodyTag element
     */
    public static TbodyTag tbody(final String text) {
        return new TbodyTag().withText(text);
    }

    /**
     * Creates an HTML {@code <tbody>} element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return A TbodyTag element
     */
    public static TbodyTag tbody(final AbstractDomContent... dc) {
        return new TbodyTag().with(dc);
    }

    /**
     * Creates an HTML {@code <tbody>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A TbodyTag element
     */
    public static TbodyTag tbody(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new TbodyTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <tbody>} element with the specified attributes and text content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param text The text content
     * @return A TbodyTag element
     */
    public static TbodyTag tbody(final Attr.ShortForm shortAttr, final String text) {
        return Attr.ShortForm.addTo(new TbodyTag().withText(text), shortAttr);
    }

    /**
     * Creates an HTML {@code <tbody>} element with the specified attributes and child DOM content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param dc The child DOM content nodes
     * @return A TbodyTag element
     */
    public static TbodyTag tbody(final Attr.ShortForm shortAttr, final AbstractDomContent... dc) {
        return Attr.ShortForm.addTo(new TbodyTag().with(dc), shortAttr);
    }

    /**
     * Creates an HTML {@code <td>} element.
     *
     * @return A TdTag element
     */
    public static TdTag td() {
        return new TdTag();
    }

    /**
     * Creates an HTML {@code <td>} element with the specified text content.
     *
     * @param text The text content
     * @return A TdTag element
     */
    public static TdTag td(final String text) {
        return new TdTag().withText(text);
    }

    /**
     * Creates an HTML {@code <td>} element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return A TdTag element
     */
    public static TdTag td(final AbstractDomContent... dc) {
        return new TdTag().with(dc);
    }

    /**
     * Creates an HTML {@code <td>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A TdTag element
     */
    public static TdTag td(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new TdTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <td>} element with the specified attributes and text content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param text The text content
     * @return A TdTag element
     */
    public static TdTag td(final Attr.ShortForm shortAttr, final String text) {
        return Attr.ShortForm.addTo(new TdTag().withText(text), shortAttr);
    }

    /**
     * Creates an HTML {@code <td>} element with the specified attributes and child DOM content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param dc The child DOM content nodes
     * @return A TdTag element
     */
    public static TdTag td(final Attr.ShortForm shortAttr, final AbstractDomContent... dc) {
        return Attr.ShortForm.addTo(new TdTag().with(dc), shortAttr);
    }

    /**
     * Creates an HTML {@code <template>} element.
     *
     * @return A TemplateTag element
     */
    public static TemplateTag template() {
        return new TemplateTag();
    }

    /**
     * Creates an HTML {@code <template>} element with the specified text content.
     *
     * @param text The text content
     * @return A TemplateTag element
     */
    public static TemplateTag template(final String text) {
        return new TemplateTag().withText(text);
    }

    /**
     * Creates an HTML {@code <template>} element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return A TemplateTag element
     */
    public static TemplateTag template(final AbstractDomContent... dc) {
        return new TemplateTag().with(dc);
    }

    /**
     * Creates an HTML {@code <template>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A TemplateTag element
     */
    public static TemplateTag template(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new TemplateTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <template>} element with the specified attributes and text content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param text The text content
     * @return A TemplateTag element
     */
    public static TemplateTag template(final Attr.ShortForm shortAttr, final String text) {
        return Attr.ShortForm.addTo(new TemplateTag().withText(text), shortAttr);
    }

    /**
     * Creates an HTML {@code <template>} element with the specified attributes and child DOM content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param dc The child DOM content nodes
     * @return A TemplateTag element
     */
    public static TemplateTag template(final Attr.ShortForm shortAttr, final AbstractDomContent... dc) {
        return Attr.ShortForm.addTo(new TemplateTag().with(dc), shortAttr);
    }

    /**
     * Creates an HTML {@code <textarea>} element.
     *
     * @return A TextareaTag element
     */
    public static TextareaTag textarea() {
        return new TextareaTag();
    }

    /**
     * Creates an HTML {@code <textarea>} element with the specified text content.
     *
     * @param text The text content
     * @return A TextareaTag element
     */
    public static TextareaTag textarea(final String text) {
        return new TextareaTag().withText(text);
    }

    /**
     * Creates an HTML {@code <textarea>} element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return A TextareaTag element
     */
    public static TextareaTag textarea(final AbstractDomContent... dc) {
        return new TextareaTag().with(dc);
    }

    /**
     * Creates an HTML {@code <textarea>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A TextareaTag element
     */
    public static TextareaTag textarea(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new TextareaTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <textarea>} element with the specified attributes and text content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param text The text content
     * @return A TextareaTag element
     */
    public static TextareaTag textarea(final Attr.ShortForm shortAttr, final String text) {
        return Attr.ShortForm.addTo(new TextareaTag().withText(text), shortAttr);
    }

    /**
     * Creates an HTML {@code <textarea>} element with the specified attributes and child DOM content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param dc The child DOM content nodes
     * @return A TextareaTag element
     */
    public static TextareaTag textarea(final Attr.ShortForm shortAttr, final AbstractDomContent... dc) {
        return Attr.ShortForm.addTo(new TextareaTag().with(dc), shortAttr);
    }

    /**
     * Creates an HTML {@code <tfoot>} element.
     *
     * @return A TfootTag element
     */
    public static TfootTag tfoot() {
        return new TfootTag();
    }

    /**
     * Creates an HTML {@code <tfoot>} element with the specified text content.
     *
     * @param text The text content
     * @return A TfootTag element
     */
    public static TfootTag tfoot(final String text) {
        return new TfootTag().withText(text);
    }

    /**
     * Creates an HTML {@code <tfoot>} element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return A TfootTag element
     */
    public static TfootTag tfoot(final AbstractDomContent... dc) {
        return new TfootTag().with(dc);
    }

    /**
     * Creates an HTML {@code <tfoot>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A TfootTag element
     */
    public static TfootTag tfoot(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new TfootTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <tfoot>} element with the specified attributes and text content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param text The text content
     * @return A TfootTag element
     */
    public static TfootTag tfoot(final Attr.ShortForm shortAttr, final String text) {
        return Attr.ShortForm.addTo(new TfootTag().withText(text), shortAttr);
    }

    /**
     * Creates an HTML {@code <tfoot>} element with the specified attributes and child DOM content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param dc The child DOM content nodes
     * @return A TfootTag element
     */
    public static TfootTag tfoot(final Attr.ShortForm shortAttr, final AbstractDomContent... dc) {
        return Attr.ShortForm.addTo(new TfootTag().with(dc), shortAttr);
    }

    /**
     * Creates an HTML {@code <th>} element.
     *
     * @return A ThTag element
     */
    public static ThTag th() {
        return new ThTag();
    }

    /**
     * Creates an HTML {@code <th>} element with the specified text content.
     *
     * @param text The text content
     * @return A ThTag element
     */
    public static ThTag th(final String text) {
        return new ThTag().withText(text);
    }

    /**
     * Creates an HTML {@code <th>} element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return A ThTag element
     */
    public static ThTag th(final AbstractDomContent... dc) {
        return new ThTag().with(dc);
    }

    /**
     * Creates an HTML {@code <th>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A ThTag element
     */
    public static ThTag th(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new ThTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <th>} element with the specified attributes and text content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param text The text content
     * @return A ThTag element
     */
    public static ThTag th(final Attr.ShortForm shortAttr, final String text) {
        return Attr.ShortForm.addTo(new ThTag().withText(text), shortAttr);
    }

    /**
     * Creates an HTML {@code <th>} element with the specified attributes and child DOM content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param dc The child DOM content nodes
     * @return A ThTag element
     */
    public static ThTag th(final Attr.ShortForm shortAttr, final AbstractDomContent... dc) {
        return Attr.ShortForm.addTo(new ThTag().with(dc), shortAttr);
    }

    /**
     * Creates an HTML {@code <thead>} element.
     *
     * @return A TheadTag element
     */
    public static TheadTag thead() {
        return new TheadTag();
    }

    /**
     * Creates an HTML {@code <thead>} element with the specified text content.
     *
     * @param text The text content
     * @return A TheadTag element
     */
    public static TheadTag thead(final String text) {
        return new TheadTag().withText(text);
    }

    /**
     * Creates an HTML {@code <thead>} element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return A TheadTag element
     */
    public static TheadTag thead(final AbstractDomContent... dc) {
        return new TheadTag().with(dc);
    }

    /**
     * Creates an HTML {@code <thead>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A TheadTag element
     */
    public static TheadTag thead(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new TheadTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <thead>} element with the specified attributes and text content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param text The text content
     * @return A TheadTag element
     */
    public static TheadTag thead(final Attr.ShortForm shortAttr, final String text) {
        return Attr.ShortForm.addTo(new TheadTag().withText(text), shortAttr);
    }

    /**
     * Creates an HTML {@code <thead>} element with the specified attributes and child DOM content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param dc The child DOM content nodes
     * @return A TheadTag element
     */
    public static TheadTag thead(final Attr.ShortForm shortAttr, final AbstractDomContent... dc) {
        return Attr.ShortForm.addTo(new TheadTag().with(dc), shortAttr);
    }

    /**
     * Creates an HTML {@code <time>} element.
     *
     * @return A TimeTag element
     */
    public static TimeTag time() {
        return new TimeTag();
    }

    /**
     * Creates an HTML {@code <time>} element with the specified text content.
     *
     * @param text The text content
     * @return A TimeTag element
     */
    public static TimeTag time(final String text) {
        return new TimeTag().withText(text);
    }

    /**
     * Creates an HTML {@code <time>} element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return A TimeTag element
     */
    public static TimeTag time(final AbstractDomContent... dc) {
        return new TimeTag().with(dc);
    }

    /**
     * Creates an HTML {@code <time>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A TimeTag element
     */
    public static TimeTag time(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new TimeTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <time>} element with the specified attributes and text content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param text The text content
     * @return A TimeTag element
     */
    public static TimeTag time(final Attr.ShortForm shortAttr, final String text) {
        return Attr.ShortForm.addTo(new TimeTag().withText(text), shortAttr);
    }

    /**
     * Creates an HTML {@code <time>} element with the specified attributes and child DOM content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param dc The child DOM content nodes
     * @return A TimeTag element
     */
    public static TimeTag time(final Attr.ShortForm shortAttr, final AbstractDomContent... dc) {
        return Attr.ShortForm.addTo(new TimeTag().with(dc), shortAttr);
    }

    /**
     * Creates an HTML {@code <title>} element.
     *
     * @return A TitleTag element
     */
    public static TitleTag title() {
        return new TitleTag();
    }

    /**
     * Creates an HTML {@code <title>} element with the specified text content.
     *
     * @param text The text content
     * @return A TitleTag element
     */
    public static TitleTag title(final String text) {
        return new TitleTag().withText(text);
    }

    /**
     * Creates an HTML {@code <title>} element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return A TitleTag element
     */
    public static TitleTag title(final AbstractDomContent... dc) {
        return new TitleTag().with(dc);
    }

    /**
     * Creates an HTML {@code <title>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A TitleTag element
     */
    public static TitleTag title(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new TitleTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <title>} element with the specified attributes and text content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param text The text content
     * @return A TitleTag element
     */
    public static TitleTag title(final Attr.ShortForm shortAttr, final String text) {
        return Attr.ShortForm.addTo(new TitleTag().withText(text), shortAttr);
    }

    /**
     * Creates an HTML {@code <title>} element with the specified attributes and child DOM content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param dc The child DOM content nodes
     * @return A TitleTag element
     */
    public static TitleTag title(final Attr.ShortForm shortAttr, final AbstractDomContent... dc) {
        return Attr.ShortForm.addTo(new TitleTag().with(dc), shortAttr);
    }

    /**
     * Creates an HTML {@code <tr>} element.
     *
     * @return A TrTag element
     */
    public static TrTag tr() {
        return new TrTag();
    }

    /**
     * Creates an HTML {@code <tr>} element with the specified text content.
     *
     * @param text The text content
     * @return A TrTag element
     */
    public static TrTag tr(final String text) {
        return new TrTag().withText(text);
    }

    /**
     * Creates an HTML {@code <tr>} element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return A TrTag element
     */
    public static TrTag tr(final AbstractDomContent... dc) {
        return new TrTag().with(dc);
    }

    /**
     * Creates an HTML {@code <tr>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A TrTag element
     */
    public static TrTag tr(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new TrTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <tr>} element with the specified attributes and text content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param text The text content
     * @return A TrTag element
     */
    public static TrTag tr(final Attr.ShortForm shortAttr, final String text) {
        return Attr.ShortForm.addTo(new TrTag().withText(text), shortAttr);
    }

    /**
     * Creates an HTML {@code <tr>} element with the specified attributes and child DOM content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param dc The child DOM content nodes
     * @return A TrTag element
     */
    public static TrTag tr(final Attr.ShortForm shortAttr, final AbstractDomContent... dc) {
        return Attr.ShortForm.addTo(new TrTag().with(dc), shortAttr);
    }

    /**
     * Creates an HTML {@code <u>} element.
     *
     * @return A UTag element
     */
    public static UTag u() {
        return new UTag();
    }

    /**
     * Creates an HTML {@code <u>} element with the specified text content.
     *
     * @param text The text content
     * @return A UTag element
     */
    public static UTag u(final String text) {
        return new UTag().withText(text);
    }

    /**
     * Creates an HTML {@code <u>} element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return A UTag element
     */
    public static UTag u(final AbstractDomContent... dc) {
        return new UTag().with(dc);
    }

    /**
     * Creates an HTML {@code <u>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A UTag element
     */
    public static UTag u(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new UTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <u>} element with the specified attributes and text content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param text The text content
     * @return A UTag element
     */
    public static UTag u(final Attr.ShortForm shortAttr, final String text) {
        return Attr.ShortForm.addTo(new UTag().withText(text), shortAttr);
    }

    /**
     * Creates an HTML {@code <u>} element with the specified attributes and child DOM content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param dc The child DOM content nodes
     * @return A UTag element
     */
    public static UTag u(final Attr.ShortForm shortAttr, final AbstractDomContent... dc) {
        return Attr.ShortForm.addTo(new UTag().with(dc), shortAttr);
    }

    /**
     * Creates an HTML {@code <ul>} element.
     *
     * @return A UlTag element
     */
    public static UlTag ul() {
        return new UlTag();
    }

    /**
     * Creates an HTML {@code <ul>} element with the specified text content.
     *
     * @param text The text content
     * @return A UlTag element
     */
    public static UlTag ul(final String text) {
        return new UlTag().withText(text);
    }

    /**
     * Creates an HTML {@code <ul>} element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return A UlTag element
     */
    public static UlTag ul(final AbstractDomContent... dc) {
        return new UlTag().with(dc);
    }

    /**
     * Creates an HTML {@code <ul>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A UlTag element
     */
    public static UlTag ul(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new UlTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <ul>} element with the specified attributes and text content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param text The text content
     * @return A UlTag element
     */
    public static UlTag ul(final Attr.ShortForm shortAttr, final String text) {
        return Attr.ShortForm.addTo(new UlTag().withText(text), shortAttr);
    }

    /**
     * Creates an HTML {@code <ul>} element with the specified attributes and child DOM content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param dc The child DOM content nodes
     * @return A UlTag element
     */
    public static UlTag ul(final Attr.ShortForm shortAttr, final AbstractDomContent... dc) {
        return Attr.ShortForm.addTo(new UlTag().with(dc), shortAttr);
    }

    /**
     * Creates an HTML {@code <var>} element.
     *
     * @return A VarTag element
     */
    public static VarTag var() {
        return new VarTag();
    }

    /**
     * Creates an HTML {@code <var>} element with the specified text content.
     *
     * @param text The text content
     * @return A VarTag element
     */
    public static VarTag var(final String text) {
        return new VarTag().withText(text);
    }

    /**
     * Creates an HTML {@code <var>} element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return A VarTag element
     */
    public static VarTag var(final AbstractDomContent... dc) {
        return new VarTag().with(dc);
    }

    /**
     * Creates an HTML {@code <var>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A VarTag element
     */
    public static VarTag var(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new VarTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <var>} element with the specified attributes and text content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param text The text content
     * @return A VarTag element
     */
    public static VarTag var(final Attr.ShortForm shortAttr, final String text) {
        return Attr.ShortForm.addTo(new VarTag().withText(text), shortAttr);
    }

    /**
     * Creates an HTML {@code <var>} element with the specified attributes and child DOM content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param dc The child DOM content nodes
     * @return A VarTag element
     */
    public static VarTag var(final Attr.ShortForm shortAttr, final AbstractDomContent... dc) {
        return Attr.ShortForm.addTo(new VarTag().with(dc), shortAttr);
    }

    /**
     * Creates an HTML {@code <video>} element.
     *
     * @return A VideoTag element
     */
    public static VideoTag video() {
        return new VideoTag();
    }

    /**
     * Creates an HTML {@code <video>} element with the specified text content.
     *
     * @param text The text content
     * @return A VideoTag element
     */
    public static VideoTag video(final String text) {
        return new VideoTag().withText(text);
    }

    /**
     * Creates an HTML {@code <video>} element with the specified child DOM content.
     *
     * @param dc The child DOM content nodes
     * @return A VideoTag element
     */
    public static VideoTag video(final AbstractDomContent... dc) {
        return new VideoTag().with(dc);
    }

    /**
     * Creates an HTML {@code <video>} element with the specified attributes.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @return A VideoTag element
     */
    public static VideoTag video(final Attr.ShortForm shortAttr) {
        return Attr.ShortForm.addTo(new VideoTag(), shortAttr);
    }

    /**
     * Creates an HTML {@code <video>} element with the specified attributes and text content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param text The text content
     * @return A VideoTag element
     */
    public static VideoTag video(final Attr.ShortForm shortAttr, final String text) {
        return Attr.ShortForm.addTo(new VideoTag().withText(text), shortAttr);
    }

    /**
     * Creates an HTML {@code <video>} element with the specified attributes and child DOM content.
     *
     * @param shortAttr The shorthand attribute containing ID and classes
     * @param dc The child DOM content nodes
     * @return A VideoTag element
     */
    public static VideoTag video(final Attr.ShortForm shortAttr, final AbstractDomContent... dc) {
        return Attr.ShortForm.addTo(new VideoTag().with(dc), shortAttr);
    }

}
