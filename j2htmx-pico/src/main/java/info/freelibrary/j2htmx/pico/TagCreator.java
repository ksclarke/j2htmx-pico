package info.freelibrary.j2htmx.pico;

import static info.freelibrary.util.Constants.*;

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
import java.util.StringJoiner;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Primary factory class providing static builder methods to construct HTML, HTMX, and Pico CSS elements.
 */
@SuppressWarnings({PMD.SHORT_METHOD_NAME, PMD.COUPLING_BETWEEN_OBJECTS, PMD.CYCLOMATIC_COMPLEXITY, PMD.TOO_MANY_METHODS,
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
     * @param aCondition the condition to if-on
     * @param anIfValue the value to return if condition is true
     * @return value if condition is true, null otherwise
     */
    public static <T> T iff(final boolean aCondition, final T anIfValue) {
        return aCondition ? anIfValue : null;
    }

    /**
     * Generic if-expression to if'ing inside method calls.
     *
     * @param anOptional The item that may be present
     * @param anIfFunction The function that will be called if that optional is present
     * @param <T> The derived generic parameter type
     * @param <U> The supplying generic parameter type
     * @return transformed value if condition is true, null otherwise
     */
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public static <T, U> T iff(final Optional<U> anOptional, final Function<U, T> anIfFunction) {
        if (Objects.nonNull(anOptional) && anOptional.isPresent()) {
            return anOptional.map(anIfFunction).orElse(null);
        }
        return null;
    }

    /**
     * Like {@link #iff}, but returns else-value instead of null.
     *
     * @param <T> The value type
     * @param aCondition The condition to check
     * @param anIfValue The value to return if condition is true
     * @param anElseValue The value to return if condition is false
     * @return The ifValue if condition is true, otherwise the elseValue
     */
    public static <T> T iffElse(final boolean aCondition, final T anIfValue, final T anElseValue) {
        return aCondition ? anIfValue : anElseValue;
    }

    /**
     * Returns a Attr.ShortForm object with either id, classes or both, obtained from parsing the input string using css
     * selector syntax.
     *
     * @param aAttrs the string with shortform attributes, only id and class is supported
     * @return a Attr.ShortForm object
     */
    public static Attr.ShortForm attrs(final String aAttrs) {
        return Attr.ShortForm.parse(aAttrs);
    }

    /**
     * Returns the HTML created by concatenating the input elements, separated by space, in encounter order. Also
     * removes spaces before periods and commas.
     *
     * @param aStringOrDomObjects the elements to join
     * @return joined elements as HTML
     */
    public static UnescapedText join(final Object... aStringOrDomObjects) {
        return DomContentJoiner.join(" ", true, aStringOrDomObjects);
    }

    /**
     * Creates a group of AbstractDomContent objects (that may be used with {@link #iff(boolean, Object)} or returned
     * from a method call).
     *
     * @param aContents the AbstractDomContent elements
     * @return AbstractDomContent containing the given elements
     */
    public static AbstractDomContent each(final AbstractDomContent... aContents) {
        return new ContainerTag<>(null).with(aContents);
    }

    /**
     * Creates a AbstractDomContent object containing HTML elements from a stream. Intended usage:
     * {@literal each(numbers.stream().map(n -> li(n.toString())))}
     *
     * @param aStream the stream of AbstractDomContent elements
     * @return AbstractDomContent containing elements from the stream
     */
    public static AbstractDomContent each(final Stream<AbstractDomContent> aStream) {
        return new ContainerTag<>(null).with(aStream);
    }

    /**
     * Creates a AbstractDomContent object containing HTML using a mapping function on a collection. Intended usage:
     * {@literal each(numbers, n -> li(n.toString()))}
     *
     * @param <T> The derived generic parameter type
     * @param aCollection the collection to iterate over, ex: a list of values "1, 2, 3"
     * @param aMapper the mapping function, ex: {@literal "n -> li(n.toString())"}
     * @return AbstractDomContent containing mapped data {@literal (ex. docs: [li(1), li(2), li(3)])}
     */
    public static <T> AbstractDomContent each(final Collection<T> aCollection,
      final Function<? super T, AbstractDomContent> aMapper) {
        return tag(null).with(aCollection.stream().map(aMapper));
    }

    /**
     * Creates a AbstractDomContent object containing HTML using a mapping BiFunction on a collection. Intended usage:
     * {@literal each(names, (index, name) -> li(index + " " + name))}
     *
     * @param <T> The derived generic parameter type
     * @param aCollection the collection to iterate over, ex: a list of values [ "Tom", "Dick", "Harry" ]
     * @param aMapper the mapping BiFunction, ex: {@literal "(index, name) -> li(index + " " + name)"}
     * @return AbstractDomContent containing mapped data {@literal (ex. docs: [li(0 Tom), li(1 Dick), li(2 Harry)])}
     */
    public static <T> AbstractDomContent each(final Collection<T> aCollection,
      final BiFunction<Integer, ? super T, AbstractDomContent> aMapper) {
        final ContainerTag<?> dom = tag(null);
        int i = 0;
        for (final T t : aCollection) {
            dom.with(aMapper.apply(i++, t));
        }
        return dom;
    }

    /**
     * Creates a AbstractDomContent object containing HTML using a mapping function on map entries.
     *
     * @param <I> The type of the keys
     * @param <T> The type of the values
     * @param aMap The map to iterate over
     * @param aMapper The mapping function applied to each map entry
     * @return AbstractDomContent containing the mapped elements
     */
    public static <I, T> AbstractDomContent each(final Map<I, T> aMap,
      final Function<Entry<I, T>, AbstractDomContent> aMapper) {
        return each(aMap.entrySet().stream().map(aMapper));
    }

    /**
     * Creates a AbstractDomContent object containing HTML using a mapping function on a map. Intended usage:
     * {@literal each(idsToNames, (id, name) -> li(id + " " + name))}
     *
     * @param <I> The type of the keys
     * @param <T> The type of the values
     * @param aMap the map to iterate over, ex: a map of values { 1: "Tom", 2: "Dick", 3: "Harry" }
     * @param aMapper the mapping function, ex: {@literal "(id, name) -> li(id + " " + name)"}
     * @return AbstractDomContent containing mapped data {@literal (ex. docs: [li(1 Tom), li(2 Dick), li(3 Harry)])}
     */
    public static <I, T> AbstractDomContent each(final Map<I, T> aMap,
      final BiFunction<I, T, AbstractDomContent> aMapper) {
        return each(aMap.entrySet().stream().map(entry -> aMapper.apply(entry.getKey(), entry.getValue())));
    }

    /**
     * Filters a collection to a list, to be used with {@link #each}. Intended usage:
     * {@literal each(filter(numbers, n -> n % 2 == 0), n -> li(n.toString()))}
     *
     * @param <T> The derived generic parameter type
     * @param aCollection the collection to filter, ex: a list of values "1, 2, 3"
     * @param aFilter the filter predicate, {@literal ex: "n -> n % 2 == 0"}
     * @return the filtered collection as a list (ex. docs: 2)
     */
    public static <T> List<T> filter(final Collection<T> aCollection, final Predicate<? super T> aFilter) {
        return aCollection.stream().filter(aFilter).collect(Collectors.toList());
    }

    /**
     * Wraps a String in an UnescapedText element.
     *
     * @param anHtml the input html
     * @return the input html wrapped in an UnescapedText element
     */
    public static UnescapedText rawHtml(final String anHtml) {
        return new UnescapedText(anHtml);
    }

    /**
     * Wraps a String in a Text element (does html-escaping).
     *
     * @param aText the input string
     * @return the input string, html-escaped
     */
    public static Text text(final String aText) {
        return new Text(aText);
    }

    /**
     * Returns a complete html document string.
     *
     * @param anHtmlTag the html content of a website
     * @return document declaration and rendered html content
     */
    public static String document(final HtmlTag anHtmlTag) {
        return document().render() + anHtmlTag.render();
    }

    //Special tags

    /**
     * Creates a custom {@link ContainerTag} with the specified tag name.
     *
     * @param aTagName The tag name
     * @return A new ContainerTag
     */
    public static ContainerTag<? extends AbstractTag<?>> tag(final String aTagName) {
        return new ContainerTag<>(aTagName);
    }

    /**
     * Creates a custom {@link EmptyTag} with the specified tag name.
     *
     * @param aTagName The tag name
     * @return A new EmptyTag
     */
    public static EmptyTag<? extends AbstractTag<?>> emptyTag(final String aTagName) {
        return new EmptyTag<>(aTagName);
    }

    /**
     * Reads a file from the classpath or filesystem and returns its escaped text content.
     *
     * @param aPath The file path
     * @return An escaped Text element
     */
    public static Text fileAsEscapedString(final String aPath) {
        return text(InlineStaticResource.getFileAsString(aPath));
    }

    /**
     * Reads a file from the classpath or filesystem and returns its raw HTML content.
     *
     * @param aPath The file path
     * @return An UnescapedText element
     */
    public static UnescapedText fileAsString(final String aPath) {
        return rawHtml(InlineStaticResource.getFileAsString(aPath));
    }

    /**
     * Creates a {@code <style>} tag containing unminified CSS read from the specified file path.
     *
     * @param aPath The CSS file path
     * @return A StyleTag containing the CSS content
     */
    public static StyleTag styleWithInlineFile(final String aPath) {
        return (StyleTag) InlineStaticResource.get(aPath, InlineStaticResource.TargetFormat.CSS);
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
     * @param aPath The JavaScript file path
     * @return A ScriptTag containing the minified JavaScript content
     */
    public static ScriptTag scriptWithInlineFileMinimized(final String aPath) {
        return (ScriptTag) InlineStaticResource.get(aPath, InlineStaticResource.TargetFormat.JS_MIN);
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
     * @param aText The text content
     * @return A HtmlTag element
     */
    public static HtmlTag html(final String aText) {
        return html().withText(aText);
    }

    /**
     * Creates an HTML {@code <html>} element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return A HtmlTag element
     */
    public static HtmlTag html(final AbstractDomContent... aDc) {
        return html().with(aDc);
    }

    /**
     * Creates an HTML {@code <html>} element with the specified attributes.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A HtmlTag element
     */
    public static HtmlTag html(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(html(), aShortAttr);
    }

    /**
     * Creates an HTML {@code <html>} element with the specified attributes and text content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aText The text content
     * @return A HtmlTag element
     */
    public static HtmlTag html(final Attr.ShortForm aShortAttr, final String aText) {
        return Attr.ShortForm.addTo(html(aText), aShortAttr);
    }

    /**
     * Creates an HTML {@code <html>} element with the specified attributes and child DOM content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aDc The child DOM content nodes
     * @return A HtmlTag element
     */
    public static HtmlTag html(final Attr.ShortForm aShortAttr, final AbstractDomContent... aDc) {
        return Attr.ShortForm.addTo(html(aDc), aShortAttr);
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
     * @param aText The text content
     * @return A HxPartialTag element
     */
    public static HxPartialTag hxPartial(final String aText) {
        return hxPartial().withText(aText);
    }

    /**
     * Creates an HTML {@code <hxPartial>} element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return A HxPartialTag element
     */
    public static HxPartialTag hxPartial(final AbstractDomContent... aDc) {
        return hxPartial().with(aDc);
    }

    /**
     * Creates an HTML {@code <hxPartial>} element with the specified attributes.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A HxPartialTag element
     */
    public static HxPartialTag hxPartial(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(hxPartial(), aShortAttr);
    }

    /**
     * Creates an HTML {@code <hxPartial>} element with the specified attributes and text content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aText The text content
     * @return A HxPartialTag element
     */
    public static HxPartialTag hxPartial(final Attr.ShortForm aShortAttr, final String aText) {
        return Attr.ShortForm.addTo(hxPartial(aText), aShortAttr);
    }

    /**
     * Creates an HTML {@code <hxPartial>} element with the specified attributes and child DOM content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aDc The child DOM content nodes
     * @return A HxPartialTag element
     */
    public static HxPartialTag hxPartial(final Attr.ShortForm aShortAttr, final AbstractDomContent... aDc) {
        return Attr.ShortForm.addTo(hxPartial(aDc), aShortAttr);
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
     * @param aText The text content
     * @return A BodyTag element
     */
    public static BodyTag body(final String aText) {
        return new BodyTag().withText(aText);
    }

    /**
     * Creates an HTML {@code <body>} element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return A BodyTag element
     */
    public static BodyTag body(final AbstractDomContent... aDc) {
        return new BodyTag().with(aDc);
    }

    /**
     * Creates an HTML {@code <body>} element with the specified attributes.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A BodyTag element
     */
    public static BodyTag body(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new BodyTag(), aShortAttr);
    }

    /**
     * Creates an HTML {@code <body>} element with the specified attributes and text content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aText The text content
     * @return A BodyTag element
     */
    public static BodyTag body(final Attr.ShortForm aShortAttr, final String aText) {
        return Attr.ShortForm.addTo(new BodyTag().withText(aText), aShortAttr);
    }

    /**
     * Creates an HTML {@code <body>} element with the specified attributes and child DOM content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aDc The child DOM content nodes
     * @return A BodyTag element
     */
    public static BodyTag body(final Attr.ShortForm aShortAttr, final AbstractDomContent... aDc) {
        return Attr.ShortForm.addTo(new BodyTag().with(aDc), aShortAttr);
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
     * @param aText The text content
     * @return A HeadTag element
     */
    public static HeadTag head(final String aText) {
        return new HeadTag().withText(aText);
    }

    /**
     * Creates an HTML {@code <head>} element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return A HeadTag element
     */
    public static HeadTag head(final AbstractDomContent... aDc) {
        return new HeadTag().with(aDc);
    }

    /**
     * Creates an HTML {@code <head>} element with the specified attributes.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A HeadTag element
     */
    public static HeadTag head(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new HeadTag(), aShortAttr);
    }

    /**
     * Creates an HTML {@code <head>} element with the specified attributes and text content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aText The text content
     * @return A HeadTag element
     */
    public static HeadTag head(final Attr.ShortForm aShortAttr, final String aText) {
        return Attr.ShortForm.addTo(new HeadTag().withText(aText), aShortAttr);
    }

    /**
     * Creates an HTML {@code <head>} element with the specified attributes and child DOM content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aDc The child DOM content nodes
     * @return A HeadTag element
     */
    public static HeadTag head(final Attr.ShortForm aShortAttr, final AbstractDomContent... aDc) {
        return Attr.ShortForm.addTo(new HeadTag().with(aDc), aShortAttr);
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
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A AreaTag element
     */
    public static AreaTag area(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new AreaTag(), aShortAttr);
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
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A BaseTag element
     */
    public static BaseTag base(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new BaseTag(), aShortAttr);
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
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A BrTag element
     */
    public static BrTag br(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new BrTag(), aShortAttr);
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
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A ColTag element
     */
    public static ColTag col(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new ColTag(), aShortAttr);
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
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A EmbedTag element
     */
    public static EmbedTag embed(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new EmbedTag(), aShortAttr);
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
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A HrTag element
     */
    public static HrTag hr(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new HrTag(), aShortAttr);
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
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A ImgTag element
     */
    public static ImgTag img(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new ImgTag(), aShortAttr);
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
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A InputTag element
     */
    public static InputTag input(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new InputTag(), aShortAttr);
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
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A KeygenTag element
     */
    public static KeygenTag keygen(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new KeygenTag(), aShortAttr);
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
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A LinkTag element
     */
    public static LinkTag link(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new LinkTag(), aShortAttr);
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
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A MetaTag element
     */
    public static MetaTag meta(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new MetaTag(), aShortAttr);
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
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A ParamTag element
     */
    public static ParamTag param(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new ParamTag(), aShortAttr);
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
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A SourceTag element
     */
    public static SourceTag source(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new SourceTag(), aShortAttr);
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
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A TrackTag element
     */
    public static TrackTag track(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new TrackTag(), aShortAttr);
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
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A WbrTag element
     */
    public static WbrTag wbr(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new WbrTag(), aShortAttr);
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
     * @param aText The text content
     * @return A ATag element
     */
    public static ATag a(final String aText) {
        return new ATag().withText(aText);
    }

    /**
     * Creates an HTML {@code <a>} element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return A ATag element
     */
    public static ATag a(final AbstractDomContent... aDc) {
        return new ATag().with(aDc);
    }

    /**
     * Creates an HTML {@code <a>} element with the specified attributes.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A ATag element
     */
    public static ATag a(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new ATag(), aShortAttr);
    }

    /**
     * Creates an HTML {@code <a>} element with the specified attributes and text content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aText The text content
     * @return A ATag element
     */
    public static ATag a(final Attr.ShortForm aShortAttr, final String aText) {
        return Attr.ShortForm.addTo(new ATag().withText(aText), aShortAttr);
    }

    /**
     * Creates an HTML {@code <a>} element with the specified attributes and child DOM content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aDc The child DOM content nodes
     * @return A ATag element
     */
    public static ATag a(final Attr.ShortForm aShortAttr, final AbstractDomContent... aDc) {
        return Attr.ShortForm.addTo(new ATag().with(aDc), aShortAttr);
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
     * @param aText The text content
     * @return A AbbrTag element
     */
    public static AbbrTag abbr(final String aText) {
        return new AbbrTag().withText(aText);
    }

    /**
     * Creates an HTML {@code <abbr>} element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return A AbbrTag element
     */
    public static AbbrTag abbr(final AbstractDomContent... aDc) {
        return new AbbrTag().with(aDc);
    }

    /**
     * Creates an HTML {@code <abbr>} element with the specified attributes.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A AbbrTag element
     */
    public static AbbrTag abbr(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new AbbrTag(), aShortAttr);
    }

    /**
     * Creates an HTML {@code <abbr>} element with the specified attributes and text content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aText The text content
     * @return A AbbrTag element
     */
    public static AbbrTag abbr(final Attr.ShortForm aShortAttr, final String aText) {
        return Attr.ShortForm.addTo(new AbbrTag().withText(aText), aShortAttr);
    }

    /**
     * Creates an HTML {@code <abbr>} element with the specified attributes and child DOM content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aDc The child DOM content nodes
     * @return A AbbrTag element
     */
    public static AbbrTag abbr(final Attr.ShortForm aShortAttr, final AbstractDomContent... aDc) {
        return Attr.ShortForm.addTo(new AbbrTag().with(aDc), aShortAttr);
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
     * @param aText The text content
     * @return A AddressTag element
     */
    public static AddressTag address(final String aText) {
        return new AddressTag().withText(aText);
    }

    /**
     * Creates an HTML {@code <address>} element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return A AddressTag element
     */
    public static AddressTag address(final AbstractDomContent... aDc) {
        return new AddressTag().with(aDc);
    }

    /**
     * Creates an HTML {@code <address>} element with the specified attributes.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A AddressTag element
     */
    public static AddressTag address(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new AddressTag(), aShortAttr);
    }

    /**
     * Creates an HTML {@code <address>} element with the specified attributes and text content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aText The text content
     * @return A AddressTag element
     */
    public static AddressTag address(final Attr.ShortForm aShortAttr, final String aText) {
        return Attr.ShortForm.addTo(new AddressTag().withText(aText), aShortAttr);
    }

    /**
     * Creates an HTML {@code <address>} element with the specified attributes and child DOM content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aDc The child DOM content nodes
     * @return A AddressTag element
     */
    public static AddressTag address(final Attr.ShortForm aShortAttr, final AbstractDomContent... aDc) {
        return Attr.ShortForm.addTo(new AddressTag().with(aDc), aShortAttr);
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
     * @param aText The text content
     * @return A ArticleTag element
     */
    public static ArticleTag article(final String aText) {
        return new ArticleTag().withText(aText);
    }

    /**
     * Creates an HTML {@code <article>} element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return A ArticleTag element
     */
    public static ArticleTag article(final AbstractDomContent... aDc) {
        return new ArticleTag().with(aDc);
    }

    /**
     * Creates an HTML {@code <article>} element with the specified attributes.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A ArticleTag element
     */
    public static ArticleTag article(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new ArticleTag(), aShortAttr);
    }

    /**
     * Creates an HTML {@code <article>} element with the specified attributes and text content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aText The text content
     * @return A ArticleTag element
     */
    public static ArticleTag article(final Attr.ShortForm aShortAttr, final String aText) {
        return Attr.ShortForm.addTo(new ArticleTag().withText(aText), aShortAttr);
    }

    /**
     * Creates an HTML {@code <article>} element with the specified attributes and child DOM content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aDc The child DOM content nodes
     * @return A ArticleTag element
     */
    public static ArticleTag article(final Attr.ShortForm aShortAttr, final AbstractDomContent... aDc) {
        return Attr.ShortForm.addTo(new ArticleTag().with(aDc), aShortAttr);
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
     * @param aText The text content
     * @return A AsideTag element
     */
    public static AsideTag aside(final String aText) {
        return new AsideTag().withText(aText);
    }

    /**
     * Creates an HTML {@code <aside>} element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return A AsideTag element
     */
    public static AsideTag aside(final AbstractDomContent... aDc) {
        return new AsideTag().with(aDc);
    }

    /**
     * Creates an HTML {@code <aside>} element with the specified attributes.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A AsideTag element
     */
    public static AsideTag aside(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new AsideTag(), aShortAttr);
    }

    /**
     * Creates an HTML {@code <aside>} element with the specified attributes and text content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aText The text content
     * @return A AsideTag element
     */
    public static AsideTag aside(final Attr.ShortForm aShortAttr, final String aText) {
        return Attr.ShortForm.addTo(new AsideTag().withText(aText), aShortAttr);
    }

    /**
     * Creates an HTML {@code <aside>} element with the specified attributes and child DOM content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aDc The child DOM content nodes
     * @return A AsideTag element
     */
    public static AsideTag aside(final Attr.ShortForm aShortAttr, final AbstractDomContent... aDc) {
        return Attr.ShortForm.addTo(new AsideTag().with(aDc), aShortAttr);
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
     * @param aText The text content
     * @return A AudioTag element
     */
    public static AudioTag audio(final String aText) {
        return new AudioTag().withText(aText);
    }

    /**
     * Creates an HTML {@code <audio>} element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return A AudioTag element
     */
    public static AudioTag audio(final AbstractDomContent... aDc) {
        return new AudioTag().with(aDc);
    }

    /**
     * Creates an HTML {@code <audio>} element with the specified attributes.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A AudioTag element
     */
    public static AudioTag audio(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new AudioTag(), aShortAttr);
    }

    /**
     * Creates an HTML {@code <audio>} element with the specified attributes and text content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aText The text content
     * @return A AudioTag element
     */
    public static AudioTag audio(final Attr.ShortForm aShortAttr, final String aText) {
        return Attr.ShortForm.addTo(new AudioTag().withText(aText), aShortAttr);
    }

    /**
     * Creates an HTML {@code <audio>} element with the specified attributes and child DOM content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aDc The child DOM content nodes
     * @return A AudioTag element
     */
    public static AudioTag audio(final Attr.ShortForm aShortAttr, final AbstractDomContent... aDc) {
        return Attr.ShortForm.addTo(new AudioTag().with(aDc), aShortAttr);
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
     * @param aText The text content
     * @return A BTag element
     */
    public static BTag b(final String aText) {
        return new BTag().withText(aText);
    }

    /**
     * Creates an HTML {@code <b>} element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return A BTag element
     */
    public static BTag b(final AbstractDomContent... aDc) {
        return new BTag().with(aDc);
    }

    /**
     * Creates an HTML {@code <b>} element with the specified attributes.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A BTag element
     */
    public static BTag b(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new BTag(), aShortAttr);
    }

    /**
     * Creates an HTML {@code <b>} element with the specified attributes and text content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aText The text content
     * @return A BTag element
     */
    public static BTag b(final Attr.ShortForm aShortAttr, final String aText) {
        return Attr.ShortForm.addTo(new BTag().withText(aText), aShortAttr);
    }

    /**
     * Creates an HTML {@code <b>} element with the specified attributes and child DOM content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aDc The child DOM content nodes
     * @return A BTag element
     */
    public static BTag b(final Attr.ShortForm aShortAttr, final AbstractDomContent... aDc) {
        return Attr.ShortForm.addTo(new BTag().with(aDc), aShortAttr);
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
     * @param aText The text content
     * @return A BdiTag element
     */
    public static BdiTag bdi(final String aText) {
        return new BdiTag().withText(aText);
    }

    /**
     * Creates an HTML {@code <bdi>} element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return A BdiTag element
     */
    public static BdiTag bdi(final AbstractDomContent... aDc) {
        return new BdiTag().with(aDc);
    }

    /**
     * Creates an HTML {@code <bdi>} element with the specified attributes.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A BdiTag element
     */
    public static BdiTag bdi(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new BdiTag(), aShortAttr);
    }

    /**
     * Creates an HTML {@code <bdi>} element with the specified attributes and text content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aText The text content
     * @return A BdiTag element
     */
    public static BdiTag bdi(final Attr.ShortForm aShortAttr, final String aText) {
        return Attr.ShortForm.addTo(new BdiTag().withText(aText), aShortAttr);
    }

    /**
     * Creates an HTML {@code <bdi>} element with the specified attributes and child DOM content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aDc The child DOM content nodes
     * @return A BdiTag element
     */
    public static BdiTag bdi(final Attr.ShortForm aShortAttr, final AbstractDomContent... aDc) {
        return Attr.ShortForm.addTo(new BdiTag().with(aDc), aShortAttr);
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
     * @param aText The text content
     * @return A BdoTag element
     */
    public static BdoTag bdo(final String aText) {
        return new BdoTag().withText(aText);
    }

    /**
     * Creates an HTML {@code <bdo>} element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return A BdoTag element
     */
    public static BdoTag bdo(final AbstractDomContent... aDc) {
        return new BdoTag().with(aDc);
    }

    /**
     * Creates an HTML {@code <bdo>} element with the specified attributes.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A BdoTag element
     */
    public static BdoTag bdo(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new BdoTag(), aShortAttr);
    }

    /**
     * Creates an HTML {@code <bdo>} element with the specified attributes and text content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aText The text content
     * @return A BdoTag element
     */
    public static BdoTag bdo(final Attr.ShortForm aShortAttr, final String aText) {
        return Attr.ShortForm.addTo(new BdoTag().withText(aText), aShortAttr);
    }

    /**
     * Creates an HTML {@code <bdo>} element with the specified attributes and child DOM content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aDc The child DOM content nodes
     * @return A BdoTag element
     */
    public static BdoTag bdo(final Attr.ShortForm aShortAttr, final AbstractDomContent... aDc) {
        return Attr.ShortForm.addTo(new BdoTag().with(aDc), aShortAttr);
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
     * @param aText The text content
     * @return A BlockquoteTag element
     */
    public static BlockquoteTag blockquote(final String aText) {
        return new BlockquoteTag().withText(aText);
    }

    /**
     * Creates an HTML {@code <blockquote>} element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return A BlockquoteTag element
     */
    public static BlockquoteTag blockquote(final AbstractDomContent... aDc) {
        return new BlockquoteTag().with(aDc);
    }

    /**
     * Creates an HTML {@code <blockquote>} element with the specified attributes.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A BlockquoteTag element
     */
    public static BlockquoteTag blockquote(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new BlockquoteTag(), aShortAttr);
    }

    /**
     * Creates an HTML {@code <blockquote>} element with the specified attributes and text content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aText The text content
     * @return A BlockquoteTag element
     */
    public static BlockquoteTag blockquote(final Attr.ShortForm aShortAttr, final String aText) {
        return Attr.ShortForm.addTo(new BlockquoteTag().withText(aText), aShortAttr);
    }

    /**
     * Creates an HTML {@code <blockquote>} element with the specified attributes and child DOM content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aDc The child DOM content nodes
     * @return A BlockquoteTag element
     */
    public static BlockquoteTag blockquote(final Attr.ShortForm aShortAttr, final AbstractDomContent... aDc) {
        return Attr.ShortForm.addTo(new BlockquoteTag().with(aDc), aShortAttr);
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
     * @param aText The text content
     * @return A ButtonTag element
     */
    public static ButtonTag button(final String aText) {
        return new ButtonTag().withText(aText);
    }

    /**
     * Creates an HTML {@code <button>} element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return A ButtonTag element
     */
    public static ButtonTag button(final AbstractDomContent... aDc) {
        return new ButtonTag().with(aDc);
    }

    /**
     * Creates an HTML {@code <button>} element with the specified attributes.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A ButtonTag element
     */
    public static ButtonTag button(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new ButtonTag(), aShortAttr);
    }

    /**
     * Creates an HTML {@code <button>} element with the specified attributes and text content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aText The text content
     * @return A ButtonTag element
     */
    public static ButtonTag button(final Attr.ShortForm aShortAttr, final String aText) {
        return Attr.ShortForm.addTo(new ButtonTag().withText(aText), aShortAttr);
    }

    /**
     * Creates an HTML {@code <button>} element with the specified attributes and child DOM content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aDc The child DOM content nodes
     * @return A ButtonTag element
     */
    public static ButtonTag button(final Attr.ShortForm aShortAttr, final AbstractDomContent... aDc) {
        return Attr.ShortForm.addTo(new ButtonTag().with(aDc), aShortAttr);
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
     * @param aText The text content
     * @return A CanvasTag element
     */
    public static CanvasTag canvas(final String aText) {
        return new CanvasTag().withText(aText);
    }

    /**
     * Creates an HTML {@code <canvas>} element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return A CanvasTag element
     */
    public static CanvasTag canvas(final AbstractDomContent... aDc) {
        return new CanvasTag().with(aDc);
    }

    /**
     * Creates an HTML {@code <canvas>} element with the specified attributes.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A CanvasTag element
     */
    public static CanvasTag canvas(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new CanvasTag(), aShortAttr);
    }

    /**
     * Creates an HTML {@code <canvas>} element with the specified attributes and text content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aText The text content
     * @return A CanvasTag element
     */
    public static CanvasTag canvas(final Attr.ShortForm aShortAttr, final String aText) {
        return Attr.ShortForm.addTo(new CanvasTag().withText(aText), aShortAttr);
    }

    /**
     * Creates an HTML {@code <canvas>} element with the specified attributes and child DOM content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aDc The child DOM content nodes
     * @return A CanvasTag element
     */
    public static CanvasTag canvas(final Attr.ShortForm aShortAttr, final AbstractDomContent... aDc) {
        return Attr.ShortForm.addTo(new CanvasTag().with(aDc), aShortAttr);
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
     * @param aText The text content
     * @return A CaptionTag element
     */
    public static CaptionTag caption(final String aText) {
        return new CaptionTag().withText(aText);
    }

    /**
     * Creates an HTML {@code <caption>} element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return A CaptionTag element
     */
    public static CaptionTag caption(final AbstractDomContent... aDc) {
        return new CaptionTag().with(aDc);
    }

    /**
     * Creates an HTML {@code <caption>} element with the specified attributes.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A CaptionTag element
     */
    public static CaptionTag caption(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new CaptionTag(), aShortAttr);
    }

    /**
     * Creates an HTML {@code <caption>} element with the specified attributes and text content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aText The text content
     * @return A CaptionTag element
     */
    public static CaptionTag caption(final Attr.ShortForm aShortAttr, final String aText) {
        return Attr.ShortForm.addTo(new CaptionTag().withText(aText), aShortAttr);
    }

    /**
     * Creates an HTML {@code <caption>} element with the specified attributes and child DOM content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aDc The child DOM content nodes
     * @return A CaptionTag element
     */
    public static CaptionTag caption(final Attr.ShortForm aShortAttr, final AbstractDomContent... aDc) {
        return Attr.ShortForm.addTo(new CaptionTag().with(aDc), aShortAttr);
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
     * @param aText The text content
     * @return A CiteTag element
     */
    public static CiteTag cite(final String aText) {
        return new CiteTag().withText(aText);
    }

    /**
     * Creates an HTML {@code <cite>} element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return A CiteTag element
     */
    public static CiteTag cite(final AbstractDomContent... aDc) {
        return new CiteTag().with(aDc);
    }

    /**
     * Creates an HTML {@code <cite>} element with the specified attributes.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A CiteTag element
     */
    public static CiteTag cite(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new CiteTag(), aShortAttr);
    }

    /**
     * Creates an HTML {@code <cite>} element with the specified attributes and text content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aText The text content
     * @return A CiteTag element
     */
    public static CiteTag cite(final Attr.ShortForm aShortAttr, final String aText) {
        return Attr.ShortForm.addTo(new CiteTag().withText(aText), aShortAttr);
    }

    /**
     * Creates an HTML {@code <cite>} element with the specified attributes and child DOM content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aDc The child DOM content nodes
     * @return A CiteTag element
     */
    public static CiteTag cite(final Attr.ShortForm aShortAttr, final AbstractDomContent... aDc) {
        return Attr.ShortForm.addTo(new CiteTag().with(aDc), aShortAttr);
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
     * @param aText The text content
     * @return A CodeTag element
     */
    public static CodeTag code(final String aText) {
        return new CodeTag().withText(aText);
    }

    /**
     * Creates an HTML {@code <code>} element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return A CodeTag element
     */
    public static CodeTag code(final AbstractDomContent... aDc) {
        return new CodeTag().with(aDc);
    }

    /**
     * Creates an HTML {@code <code>} element with the specified attributes.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A CodeTag element
     */
    public static CodeTag code(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new CodeTag(), aShortAttr);
    }

    /**
     * Creates an HTML {@code <code>} element with the specified attributes and text content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aText The text content
     * @return A CodeTag element
     */
    public static CodeTag code(final Attr.ShortForm aShortAttr, final String aText) {
        return Attr.ShortForm.addTo(new CodeTag().withText(aText), aShortAttr);
    }

    /**
     * Creates an HTML {@code <code>} element with the specified attributes and child DOM content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aDc The child DOM content nodes
     * @return A CodeTag element
     */
    public static CodeTag code(final Attr.ShortForm aShortAttr, final AbstractDomContent... aDc) {
        return Attr.ShortForm.addTo(new CodeTag().with(aDc), aShortAttr);
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
     * @param aText The text content
     * @return A ColgroupTag element
     */
    public static ColgroupTag colgroup(final String aText) {
        return new ColgroupTag().withText(aText);
    }

    /**
     * Creates an HTML {@code <colgroup>} element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return A ColgroupTag element
     */
    public static ColgroupTag colgroup(final AbstractDomContent... aDc) {
        return new ColgroupTag().with(aDc);
    }

    /**
     * Creates an HTML {@code <colgroup>} element with the specified attributes.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A ColgroupTag element
     */
    public static ColgroupTag colgroup(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new ColgroupTag(), aShortAttr);
    }

    /**
     * Creates an HTML {@code <colgroup>} element with the specified attributes and text content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aText The text content
     * @return A ColgroupTag element
     */
    public static ColgroupTag colgroup(final Attr.ShortForm aShortAttr, final String aText) {
        return Attr.ShortForm.addTo(new ColgroupTag().withText(aText), aShortAttr);
    }

    /**
     * Creates an HTML {@code <colgroup>} element with the specified attributes and child DOM content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aDc The child DOM content nodes
     * @return A ColgroupTag element
     */
    public static ColgroupTag colgroup(final Attr.ShortForm aShortAttr, final AbstractDomContent... aDc) {
        return Attr.ShortForm.addTo(new ColgroupTag().with(aDc), aShortAttr);
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
     * @param aText The text content
     * @return A DataTag element
     */
    public static DataTag data(final String aText) {
        return new DataTag().withText(aText);
    }

    /**
     * Creates an HTML {@code <data>} element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return A DataTag element
     */
    public static DataTag data(final AbstractDomContent... aDc) {
        return new DataTag().with(aDc);
    }

    /**
     * Creates an HTML {@code <data>} element with the specified attributes.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A DataTag element
     */
    public static DataTag data(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new DataTag(), aShortAttr);
    }

    /**
     * Creates an HTML {@code <data>} element with the specified attributes and text content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aText The text content
     * @return A DataTag element
     */
    public static DataTag data(final Attr.ShortForm aShortAttr, final String aText) {
        return Attr.ShortForm.addTo(new DataTag().withText(aText), aShortAttr);
    }

    /**
     * Creates an HTML {@code <data>} element with the specified attributes and child DOM content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aDc The child DOM content nodes
     * @return A DataTag element
     */
    public static DataTag data(final Attr.ShortForm aShortAttr, final AbstractDomContent... aDc) {
        return Attr.ShortForm.addTo(new DataTag().with(aDc), aShortAttr);
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
     * @param aText The text content
     * @return A DatalistTag element
     */
    public static DatalistTag datalist(final String aText) {
        return new DatalistTag().withText(aText);
    }

    /**
     * Creates an HTML {@code <datalist>} element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return A DatalistTag element
     */
    public static DatalistTag datalist(final AbstractDomContent... aDc) {
        return new DatalistTag().with(aDc);
    }

    /**
     * Creates an HTML {@code <datalist>} element with the specified attributes.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A DatalistTag element
     */
    public static DatalistTag datalist(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new DatalistTag(), aShortAttr);
    }

    /**
     * Creates an HTML {@code <datalist>} element with the specified attributes and text content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aText The text content
     * @return A DatalistTag element
     */
    public static DatalistTag datalist(final Attr.ShortForm aShortAttr, final String aText) {
        return Attr.ShortForm.addTo(new DatalistTag().withText(aText), aShortAttr);
    }

    /**
     * Creates an HTML {@code <datalist>} element with the specified attributes and child DOM content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aDc The child DOM content nodes
     * @return A DatalistTag element
     */
    public static DatalistTag datalist(final Attr.ShortForm aShortAttr, final AbstractDomContent... aDc) {
        return Attr.ShortForm.addTo(new DatalistTag().with(aDc), aShortAttr);
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
     * @param aText The text content
     * @return A DdTag element
     */
    public static DdTag dd(final String aText) {
        return new DdTag().withText(aText);
    }

    /**
     * Creates an HTML {@code <dd>} element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return A DdTag element
     */
    public static DdTag dd(final AbstractDomContent... aDc) {
        return new DdTag().with(aDc);
    }

    /**
     * Creates an HTML {@code <dd>} element with the specified attributes.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A DdTag element
     */
    public static DdTag dd(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new DdTag(), aShortAttr);
    }

    /**
     * Creates an HTML {@code <dd>} element with the specified attributes and text content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aText The text content
     * @return A DdTag element
     */
    public static DdTag dd(final Attr.ShortForm aShortAttr, final String aText) {
        return Attr.ShortForm.addTo(new DdTag().withText(aText), aShortAttr);
    }

    /**
     * Creates an HTML {@code <dd>} element with the specified attributes and child DOM content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aDc The child DOM content nodes
     * @return A DdTag element
     */
    public static DdTag dd(final Attr.ShortForm aShortAttr, final AbstractDomContent... aDc) {
        return Attr.ShortForm.addTo(new DdTag().with(aDc), aShortAttr);
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
     * @param aText The text content
     * @return A DelTag element
     */
    public static DelTag del(final String aText) {
        return new DelTag().withText(aText);
    }

    /**
     * Creates an HTML {@code <del>} element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return A DelTag element
     */
    public static DelTag del(final AbstractDomContent... aDc) {
        return new DelTag().with(aDc);
    }

    /**
     * Creates an HTML {@code <del>} element with the specified attributes.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A DelTag element
     */
    public static DelTag del(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new DelTag(), aShortAttr);
    }

    /**
     * Creates an HTML {@code <del>} element with the specified attributes and text content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aText The text content
     * @return A DelTag element
     */
    public static DelTag del(final Attr.ShortForm aShortAttr, final String aText) {
        return Attr.ShortForm.addTo(new DelTag().withText(aText), aShortAttr);
    }

    /**
     * Creates an HTML {@code <del>} element with the specified attributes and child DOM content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aDc The child DOM content nodes
     * @return A DelTag element
     */
    public static DelTag del(final Attr.ShortForm aShortAttr, final AbstractDomContent... aDc) {
        return Attr.ShortForm.addTo(new DelTag().with(aDc), aShortAttr);
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
     * @param aText The text content
     * @return A DetailsTag element
     */
    public static DetailsTag details(final String aText) {
        return new DetailsTag().withText(aText);
    }

    /**
     * Creates an HTML {@code <details>} element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return A DetailsTag element
     */
    public static DetailsTag details(final AbstractDomContent... aDc) {
        return new DetailsTag().with(aDc);
    }

    /**
     * Creates an HTML {@code <details>} element with the specified attributes.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A DetailsTag element
     */
    public static DetailsTag details(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new DetailsTag(), aShortAttr);
    }

    /**
     * Creates an HTML {@code <details>} element with the specified attributes and text content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aText The text content
     * @return A DetailsTag element
     */
    public static DetailsTag details(final Attr.ShortForm aShortAttr, final String aText) {
        return Attr.ShortForm.addTo(new DetailsTag().withText(aText), aShortAttr);
    }

    /**
     * Creates an HTML {@code <details>} element with the specified attributes and child DOM content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aDc The child DOM content nodes
     * @return A DetailsTag element
     */
    public static DetailsTag details(final Attr.ShortForm aShortAttr, final AbstractDomContent... aDc) {
        return Attr.ShortForm.addTo(new DetailsTag().with(aDc), aShortAttr);
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
     * @param aText The text content
     * @return A DfnTag element
     */
    public static DfnTag dfn(final String aText) {
        return new DfnTag().withText(aText);
    }

    /**
     * Creates an HTML {@code <dfn>} element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return A DfnTag element
     */
    public static DfnTag dfn(final AbstractDomContent... aDc) {
        return new DfnTag().with(aDc);
    }

    /**
     * Creates an HTML {@code <dfn>} element with the specified attributes.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A DfnTag element
     */
    public static DfnTag dfn(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new DfnTag(), aShortAttr);
    }

    /**
     * Creates an HTML {@code <dfn>} element with the specified attributes and text content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aText The text content
     * @return A DfnTag element
     */
    public static DfnTag dfn(final Attr.ShortForm aShortAttr, final String aText) {
        return Attr.ShortForm.addTo(new DfnTag().withText(aText), aShortAttr);
    }

    /**
     * Creates an HTML {@code <dfn>} element with the specified attributes and child DOM content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aDc The child DOM content nodes
     * @return A DfnTag element
     */
    public static DfnTag dfn(final Attr.ShortForm aShortAttr, final AbstractDomContent... aDc) {
        return Attr.ShortForm.addTo(new DfnTag().with(aDc), aShortAttr);
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
     * @param aText The text content
     * @return A DialogTag element
     */
    public static DialogTag dialog(final String aText) {
        return new DialogTag().withText(aText);
    }

    /**
     * Creates an HTML {@code <dialog>} element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return A DialogTag element
     */
    public static DialogTag dialog(final AbstractDomContent... aDc) {
        return new DialogTag().with(aDc);
    }

    /**
     * Creates an HTML {@code <dialog>} element with the specified attributes.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A DialogTag element
     */
    public static DialogTag dialog(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new DialogTag(), aShortAttr);
    }

    /**
     * Creates an HTML {@code <dialog>} element with the specified attributes and text content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aText The text content
     * @return A DialogTag element
     */
    public static DialogTag dialog(final Attr.ShortForm aShortAttr, final String aText) {
        return Attr.ShortForm.addTo(new DialogTag().withText(aText), aShortAttr);
    }

    /**
     * Creates an HTML {@code <dialog>} element with the specified attributes and child DOM content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aDc The child DOM content nodes
     * @return A DialogTag element
     */
    public static DialogTag dialog(final Attr.ShortForm aShortAttr, final AbstractDomContent... aDc) {
        return Attr.ShortForm.addTo(new DialogTag().with(aDc), aShortAttr);
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
     * @param aText The text content
     * @return A DivTag element
     */
    public static DivTag div(final String aText) {
        return new DivTag().withText(aText);
    }

    /**
     * Creates an HTML {@code <div>} element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return A DivTag element
     */
    public static DivTag div(final AbstractDomContent... aDc) {
        return new DivTag().with(aDc);
    }

    /**
     * Creates an HTML {@code <div>} element with the specified attributes.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A DivTag element
     */
    public static DivTag div(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new DivTag(), aShortAttr);
    }

    /**
     * Creates an HTML {@code <div>} element with the specified attributes and text content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aText The text content
     * @return A DivTag element
     */
    public static DivTag div(final Attr.ShortForm aShortAttr, final String aText) {
        return Attr.ShortForm.addTo(new DivTag().withText(aText), aShortAttr);
    }

    /**
     * Creates an HTML {@code <div>} element with the specified attributes and child DOM content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aDc The child DOM content nodes
     * @return A DivTag element
     */
    public static DivTag div(final Attr.ShortForm aShortAttr, final AbstractDomContent... aDc) {
        return Attr.ShortForm.addTo(new DivTag().with(aDc), aShortAttr);
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
     * @param aText The text content
     * @return A DlTag element
     */
    public static DlTag dl(final String aText) {
        return new DlTag().withText(aText);
    }

    /**
     * Creates an HTML {@code <dl>} element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return A DlTag element
     */
    public static DlTag dl(final AbstractDomContent... aDc) {
        return new DlTag().with(aDc);
    }

    /**
     * Creates an HTML {@code <dl>} element with the specified attributes.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A DlTag element
     */
    public static DlTag dl(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new DlTag(), aShortAttr);
    }

    /**
     * Creates an HTML {@code <dl>} element with the specified attributes and text content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aText The text content
     * @return A DlTag element
     */
    public static DlTag dl(final Attr.ShortForm aShortAttr, final String aText) {
        return Attr.ShortForm.addTo(new DlTag().withText(aText), aShortAttr);
    }

    /**
     * Creates an HTML {@code <dl>} element with the specified attributes and child DOM content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aDc The child DOM content nodes
     * @return A DlTag element
     */
    public static DlTag dl(final Attr.ShortForm aShortAttr, final AbstractDomContent... aDc) {
        return Attr.ShortForm.addTo(new DlTag().with(aDc), aShortAttr);
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
     * @param aText The text content
     * @return A DtTag element
     */
    public static DtTag dt(final String aText) {
        return new DtTag().withText(aText);
    }

    /**
     * Creates an HTML {@code <dt>} element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return A DtTag element
     */
    public static DtTag dt(final AbstractDomContent... aDc) {
        return new DtTag().with(aDc);
    }

    /**
     * Creates an HTML {@code <dt>} element with the specified attributes.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A DtTag element
     */
    public static DtTag dt(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new DtTag(), aShortAttr);
    }

    /**
     * Creates an HTML {@code <dt>} element with the specified attributes and text content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aText The text content
     * @return A DtTag element
     */
    public static DtTag dt(final Attr.ShortForm aShortAttr, final String aText) {
        return Attr.ShortForm.addTo(new DtTag().withText(aText), aShortAttr);
    }

    /**
     * Creates an HTML {@code <dt>} element with the specified attributes and child DOM content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aDc The child DOM content nodes
     * @return A DtTag element
     */
    public static DtTag dt(final Attr.ShortForm aShortAttr, final AbstractDomContent... aDc) {
        return Attr.ShortForm.addTo(new DtTag().with(aDc), aShortAttr);
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
     * @param aText The text content
     * @return A EmTag element
     */
    public static EmTag em(final String aText) {
        return new EmTag().withText(aText);
    }

    /**
     * Creates an HTML {@code <em>} element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return A EmTag element
     */
    public static EmTag em(final AbstractDomContent... aDc) {
        return new EmTag().with(aDc);
    }

    /**
     * Creates an HTML {@code <em>} element with the specified attributes.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A EmTag element
     */
    public static EmTag em(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new EmTag(), aShortAttr);
    }

    /**
     * Creates an HTML {@code <em>} element with the specified attributes and text content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aText The text content
     * @return A EmTag element
     */
    public static EmTag em(final Attr.ShortForm aShortAttr, final String aText) {
        return Attr.ShortForm.addTo(new EmTag().withText(aText), aShortAttr);
    }

    /**
     * Creates an HTML {@code <em>} element with the specified attributes and child DOM content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aDc The child DOM content nodes
     * @return A EmTag element
     */
    public static EmTag em(final Attr.ShortForm aShortAttr, final AbstractDomContent... aDc) {
        return Attr.ShortForm.addTo(new EmTag().with(aDc), aShortAttr);
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
     * @param aText The text content
     * @return A FieldsetTag element
     */
    public static FieldsetTag fieldset(final String aText) {
        return new FieldsetTag().withText(aText);
    }

    /**
     * Creates an HTML {@code <fieldset>} element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return A FieldsetTag element
     */
    public static FieldsetTag fieldset(final AbstractDomContent... aDc) {
        return new FieldsetTag().with(aDc);
    }

    /**
     * Creates an HTML {@code <fieldset>} element with the specified attributes.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A FieldsetTag element
     */
    public static FieldsetTag fieldset(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new FieldsetTag(), aShortAttr);
    }

    /**
     * Creates an HTML {@code <fieldset>} element with the specified attributes and text content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aText The text content
     * @return A FieldsetTag element
     */
    public static FieldsetTag fieldset(final Attr.ShortForm aShortAttr, final String aText) {
        return Attr.ShortForm.addTo(new FieldsetTag().withText(aText), aShortAttr);
    }

    /**
     * Creates an HTML {@code <fieldset>} element with the specified attributes and child DOM content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aDc The child DOM content nodes
     * @return A FieldsetTag element
     */
    public static FieldsetTag fieldset(final Attr.ShortForm aShortAttr, final AbstractDomContent... aDc) {
        return Attr.ShortForm.addTo(new FieldsetTag().with(aDc), aShortAttr);
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
     * @param aText The text content
     * @return A FigcaptionTag element
     */
    public static FigcaptionTag figcaption(final String aText) {
        return new FigcaptionTag().withText(aText);
    }

    /**
     * Creates an HTML {@code <figcaption>} element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return A FigcaptionTag element
     */
    public static FigcaptionTag figcaption(final AbstractDomContent... aDc) {
        return new FigcaptionTag().with(aDc);
    }

    /**
     * Creates an HTML {@code <figcaption>} element with the specified attributes.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A FigcaptionTag element
     */
    public static FigcaptionTag figcaption(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new FigcaptionTag(), aShortAttr);
    }

    /**
     * Creates an HTML {@code <figcaption>} element with the specified attributes and text content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aText The text content
     * @return A FigcaptionTag element
     */
    public static FigcaptionTag figcaption(final Attr.ShortForm aShortAttr, final String aText) {
        return Attr.ShortForm.addTo(new FigcaptionTag().withText(aText), aShortAttr);
    }

    /**
     * Creates an HTML {@code <figcaption>} element with the specified attributes and child DOM content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aDc The child DOM content nodes
     * @return A FigcaptionTag element
     */
    public static FigcaptionTag figcaption(final Attr.ShortForm aShortAttr, final AbstractDomContent... aDc) {
        return Attr.ShortForm.addTo(new FigcaptionTag().with(aDc), aShortAttr);
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
     * @param aText The text content
     * @return A FigureTag element
     */
    public static FigureTag figure(final String aText) {
        return new FigureTag().withText(aText);
    }

    /**
     * Creates an HTML {@code <figure>} element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return A FigureTag element
     */
    public static FigureTag figure(final AbstractDomContent... aDc) {
        return new FigureTag().with(aDc);
    }

    /**
     * Creates an HTML {@code <figure>} element with the specified attributes.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A FigureTag element
     */
    public static FigureTag figure(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new FigureTag(), aShortAttr);
    }

    /**
     * Creates an HTML {@code <figure>} element with the specified attributes and text content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aText The text content
     * @return A FigureTag element
     */
    public static FigureTag figure(final Attr.ShortForm aShortAttr, final String aText) {
        return Attr.ShortForm.addTo(new FigureTag().withText(aText), aShortAttr);
    }

    /**
     * Creates an HTML {@code <figure>} element with the specified attributes and child DOM content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aDc The child DOM content nodes
     * @return A FigureTag element
     */
    public static FigureTag figure(final Attr.ShortForm aShortAttr, final AbstractDomContent... aDc) {
        return Attr.ShortForm.addTo(new FigureTag().with(aDc), aShortAttr);
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
     * @param aText The text content
     * @return A FooterTag element
     */
    public static FooterTag footer(final String aText) {
        return new FooterTag().withText(aText);
    }

    /**
     * Creates an HTML {@code <footer>} element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return A FooterTag element
     */
    public static FooterTag footer(final AbstractDomContent... aDc) {
        return new FooterTag().with(aDc);
    }

    /**
     * Creates an HTML {@code <footer>} element with the specified attributes.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A FooterTag element
     */
    public static FooterTag footer(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new FooterTag(), aShortAttr);
    }

    /**
     * Creates an HTML {@code <footer>} element with the specified attributes and text content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aText The text content
     * @return A FooterTag element
     */
    public static FooterTag footer(final Attr.ShortForm aShortAttr, final String aText) {
        return Attr.ShortForm.addTo(new FooterTag().withText(aText), aShortAttr);
    }

    /**
     * Creates an HTML {@code <footer>} element with the specified attributes and child DOM content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aDc The child DOM content nodes
     * @return A FooterTag element
     */
    public static FooterTag footer(final Attr.ShortForm aShortAttr, final AbstractDomContent... aDc) {
        return Attr.ShortForm.addTo(new FooterTag().with(aDc), aShortAttr);
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
     * @param aText The text content
     * @return A FormTag element
     */
    public static FormTag form(final String aText) {
        return new FormTag().withText(aText);
    }

    /**
     * Creates an HTML {@code <form>} element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return A FormTag element
     */
    public static FormTag form(final AbstractDomContent... aDc) {
        return new FormTag().with(aDc);
    }

    /**
     * Creates an HTML {@code <form>} element with the specified attributes.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A FormTag element
     */
    public static FormTag form(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new FormTag(), aShortAttr);
    }

    /**
     * Creates an HTML {@code <form>} element with the specified attributes and text content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aText The text content
     * @return A FormTag element
     */
    public static FormTag form(final Attr.ShortForm aShortAttr, final String aText) {
        return Attr.ShortForm.addTo(new FormTag().withText(aText), aShortAttr);
    }

    /**
     * Creates an HTML {@code <form>} element with the specified attributes and child DOM content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aDc The child DOM content nodes
     * @return A FormTag element
     */
    public static FormTag form(final Attr.ShortForm aShortAttr, final AbstractDomContent... aDc) {
        return Attr.ShortForm.addTo(new FormTag().with(aDc), aShortAttr);
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
     * @param aText The text content
     * @return A H1Tag element
     */
    public static H1Tag h1(final String aText) {
        return new H1Tag().withText(aText);
    }

    /**
     * Creates an HTML {@code <h1>} element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return A H1Tag element
     */
    public static H1Tag h1(final AbstractDomContent... aDc) {
        return new H1Tag().with(aDc);
    }

    /**
     * Creates an HTML {@code <h1>} element with the specified attributes.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A H1Tag element
     */
    public static H1Tag h1(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new H1Tag(), aShortAttr);
    }

    /**
     * Creates an HTML {@code <h1>} element with the specified attributes and text content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aText The text content
     * @return A H1Tag element
     */
    public static H1Tag h1(final Attr.ShortForm aShortAttr, final String aText) {
        return Attr.ShortForm.addTo(new H1Tag().withText(aText), aShortAttr);
    }

    /**
     * Creates an HTML {@code <h1>} element with the specified attributes and child DOM content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aDc The child DOM content nodes
     * @return A H1Tag element
     */
    public static H1Tag h1(final Attr.ShortForm aShortAttr, final AbstractDomContent... aDc) {
        return Attr.ShortForm.addTo(new H1Tag().with(aDc), aShortAttr);
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
     * @param aText The text content
     * @return A H2Tag element
     */
    public static H2Tag h2(final String aText) {
        return new H2Tag().withText(aText);
    }

    /**
     * Creates an HTML {@code <h2>} element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return A H2Tag element
     */
    public static H2Tag h2(final AbstractDomContent... aDc) {
        return new H2Tag().with(aDc);
    }

    /**
     * Creates an HTML {@code <h2>} element with the specified attributes.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A H2Tag element
     */
    public static H2Tag h2(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new H2Tag(), aShortAttr);
    }

    /**
     * Creates an HTML {@code <h2>} element with the specified attributes and text content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aText The text content
     * @return A H2Tag element
     */
    public static H2Tag h2(final Attr.ShortForm aShortAttr, final String aText) {
        return Attr.ShortForm.addTo(new H2Tag().withText(aText), aShortAttr);
    }

    /**
     * Creates an HTML {@code <h2>} element with the specified attributes and child DOM content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aDc The child DOM content nodes
     * @return A H2Tag element
     */
    public static H2Tag h2(final Attr.ShortForm aShortAttr, final AbstractDomContent... aDc) {
        return Attr.ShortForm.addTo(new H2Tag().with(aDc), aShortAttr);
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
     * @param aText The text content
     * @return A H3Tag element
     */
    public static H3Tag h3(final String aText) {
        return new H3Tag().withText(aText);
    }

    /**
     * Creates an HTML {@code <h3>} element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return A H3Tag element
     */
    public static H3Tag h3(final AbstractDomContent... aDc) {
        return new H3Tag().with(aDc);
    }

    /**
     * Creates an HTML {@code <h3>} element with the specified attributes.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A H3Tag element
     */
    public static H3Tag h3(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new H3Tag(), aShortAttr);
    }

    /**
     * Creates an HTML {@code <h3>} element with the specified attributes and text content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aText The text content
     * @return A H3Tag element
     */
    public static H3Tag h3(final Attr.ShortForm aShortAttr, final String aText) {
        return Attr.ShortForm.addTo(new H3Tag().withText(aText), aShortAttr);
    }

    /**
     * Creates an HTML {@code <h3>} element with the specified attributes and child DOM content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aDc The child DOM content nodes
     * @return A H3Tag element
     */
    public static H3Tag h3(final Attr.ShortForm aShortAttr, final AbstractDomContent... aDc) {
        return Attr.ShortForm.addTo(new H3Tag().with(aDc), aShortAttr);
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
     * @param aText The text content
     * @return A H4Tag element
     */
    public static H4Tag h4(final String aText) {
        return new H4Tag().withText(aText);
    }

    /**
     * Creates an HTML {@code <h4>} element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return A H4Tag element
     */
    public static H4Tag h4(final AbstractDomContent... aDc) {
        return new H4Tag().with(aDc);
    }

    /**
     * Creates an HTML {@code <h4>} element with the specified attributes.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A H4Tag element
     */
    public static H4Tag h4(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new H4Tag(), aShortAttr);
    }

    /**
     * Creates an HTML {@code <h4>} element with the specified attributes and text content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aText The text content
     * @return A H4Tag element
     */
    public static H4Tag h4(final Attr.ShortForm aShortAttr, final String aText) {
        return Attr.ShortForm.addTo(new H4Tag().withText(aText), aShortAttr);
    }

    /**
     * Creates an HTML {@code <h4>} element with the specified attributes and child DOM content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aDc The child DOM content nodes
     * @return A H4Tag element
     */
    public static H4Tag h4(final Attr.ShortForm aShortAttr, final AbstractDomContent... aDc) {
        return Attr.ShortForm.addTo(new H4Tag().with(aDc), aShortAttr);
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
     * @param aText The text content
     * @return A H5Tag element
     */
    public static H5Tag h5(final String aText) {
        return new H5Tag().withText(aText);
    }

    /**
     * Creates an HTML {@code <h5>} element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return A H5Tag element
     */
    public static H5Tag h5(final AbstractDomContent... aDc) {
        return new H5Tag().with(aDc);
    }

    /**
     * Creates an HTML {@code <h5>} element with the specified attributes.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A H5Tag element
     */
    public static H5Tag h5(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new H5Tag(), aShortAttr);
    }

    /**
     * Creates an HTML {@code <h5>} element with the specified attributes and text content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aText The text content
     * @return A H5Tag element
     */
    public static H5Tag h5(final Attr.ShortForm aShortAttr, final String aText) {
        return Attr.ShortForm.addTo(new H5Tag().withText(aText), aShortAttr);
    }

    /**
     * Creates an HTML {@code <h5>} element with the specified attributes and child DOM content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aDc The child DOM content nodes
     * @return A H5Tag element
     */
    public static H5Tag h5(final Attr.ShortForm aShortAttr, final AbstractDomContent... aDc) {
        return Attr.ShortForm.addTo(new H5Tag().with(aDc), aShortAttr);
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
     * @param aText The text content
     * @return A H6Tag element
     */
    public static H6Tag h6(final String aText) {
        return new H6Tag().withText(aText);
    }

    /**
     * Creates an HTML {@code <h6>} element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return A H6Tag element
     */
    public static H6Tag h6(final AbstractDomContent... aDc) {
        return new H6Tag().with(aDc);
    }

    /**
     * Creates an HTML {@code <h6>} element with the specified attributes.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A H6Tag element
     */
    public static H6Tag h6(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new H6Tag(), aShortAttr);
    }

    /**
     * Creates an HTML {@code <h6>} element with the specified attributes and text content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aText The text content
     * @return A H6Tag element
     */
    public static H6Tag h6(final Attr.ShortForm aShortAttr, final String aText) {
        return Attr.ShortForm.addTo(new H6Tag().withText(aText), aShortAttr);
    }

    /**
     * Creates an HTML {@code <h6>} element with the specified attributes and child DOM content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aDc The child DOM content nodes
     * @return A H6Tag element
     */
    public static H6Tag h6(final Attr.ShortForm aShortAttr, final AbstractDomContent... aDc) {
        return Attr.ShortForm.addTo(new H6Tag().with(aDc), aShortAttr);
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
     * @param aText The text content
     * @return A HeaderTag element
     */
    public static HeaderTag header(final String aText) {
        return new HeaderTag().withText(aText);
    }

    /**
     * Creates an HTML {@code <header>} element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return A HeaderTag element
     */
    public static HeaderTag header(final AbstractDomContent... aDc) {
        return new HeaderTag().with(aDc);
    }

    /**
     * Creates an HTML {@code <header>} element with the specified attributes.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A HeaderTag element
     */
    public static HeaderTag header(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new HeaderTag(), aShortAttr);
    }

    /**
     * Creates an HTML {@code <header>} element with the specified attributes and text content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aText The text content
     * @return A HeaderTag element
     */
    public static HeaderTag header(final Attr.ShortForm aShortAttr, final String aText) {
        return Attr.ShortForm.addTo(new HeaderTag().withText(aText), aShortAttr);
    }

    /**
     * Creates an HTML {@code <header>} element with the specified attributes and child DOM content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aDc The child DOM content nodes
     * @return A HeaderTag element
     */
    public static HeaderTag header(final Attr.ShortForm aShortAttr, final AbstractDomContent... aDc) {
        return Attr.ShortForm.addTo(new HeaderTag().with(aDc), aShortAttr);
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
     * @param aText The text content
     * @return A ITag element
     */
    public static ITag i(final String aText) {
        return new ITag().withText(aText);
    }

    /**
     * Creates an HTML {@code <i>} element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return A ITag element
     */
    public static ITag i(final AbstractDomContent... aDc) {
        return new ITag().with(aDc);
    }

    /**
     * Creates an HTML {@code <i>} element with the specified attributes.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A ITag element
     */
    public static ITag i(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new ITag(), aShortAttr);
    }

    /**
     * Creates an HTML {@code <i>} element with the specified attributes and text content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aText The text content
     * @return A ITag element
     */
    public static ITag i(final Attr.ShortForm aShortAttr, final String aText) {
        return Attr.ShortForm.addTo(new ITag().withText(aText), aShortAttr);
    }

    /**
     * Creates an HTML {@code <i>} element with the specified attributes and child DOM content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aDc The child DOM content nodes
     * @return A ITag element
     */
    public static ITag i(final Attr.ShortForm aShortAttr, final AbstractDomContent... aDc) {
        return Attr.ShortForm.addTo(new ITag().with(aDc), aShortAttr);
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
     * @param aText The text content
     * @return A IframeTag element
     */
    public static IframeTag iframe(final String aText) {
        return new IframeTag().withText(aText);
    }

    /**
     * Creates an HTML {@code <iframe>} element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return A IframeTag element
     */
    public static IframeTag iframe(final AbstractDomContent... aDc) {
        return new IframeTag().with(aDc);
    }

    /**
     * Creates an HTML {@code <iframe>} element with the specified attributes.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A IframeTag element
     */
    public static IframeTag iframe(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new IframeTag(), aShortAttr);
    }

    /**
     * Creates an HTML {@code <iframe>} element with the specified attributes and text content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aText The text content
     * @return A IframeTag element
     */
    public static IframeTag iframe(final Attr.ShortForm aShortAttr, final String aText) {
        return Attr.ShortForm.addTo(new IframeTag().withText(aText), aShortAttr);
    }

    /**
     * Creates an HTML {@code <iframe>} element with the specified attributes and child DOM content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aDc The child DOM content nodes
     * @return A IframeTag element
     */
    public static IframeTag iframe(final Attr.ShortForm aShortAttr, final AbstractDomContent... aDc) {
        return Attr.ShortForm.addTo(new IframeTag().with(aDc), aShortAttr);
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
     * @param aText The text content
     * @return A InsTag element
     */
    public static InsTag ins(final String aText) {
        return new InsTag().withText(aText);
    }

    /**
     * Creates an HTML {@code <ins>} element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return A InsTag element
     */
    public static InsTag ins(final AbstractDomContent... aDc) {
        return new InsTag().with(aDc);
    }

    /**
     * Creates an HTML {@code <ins>} element with the specified attributes.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A InsTag element
     */
    public static InsTag ins(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new InsTag(), aShortAttr);
    }

    /**
     * Creates an HTML {@code <ins>} element with the specified attributes and text content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aText The text content
     * @return A InsTag element
     */
    public static InsTag ins(final Attr.ShortForm aShortAttr, final String aText) {
        return Attr.ShortForm.addTo(new InsTag().withText(aText), aShortAttr);
    }

    /**
     * Creates an HTML {@code <ins>} element with the specified attributes and child DOM content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aDc The child DOM content nodes
     * @return A InsTag element
     */
    public static InsTag ins(final Attr.ShortForm aShortAttr, final AbstractDomContent... aDc) {
        return Attr.ShortForm.addTo(new InsTag().with(aDc), aShortAttr);
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
     * @param aText The text content
     * @return A KbdTag element
     */
    public static KbdTag kbd(final String aText) {
        return new KbdTag().withText(aText);
    }

    /**
     * Creates an HTML {@code <kbd>} element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return A KbdTag element
     */
    public static KbdTag kbd(final AbstractDomContent... aDc) {
        return new KbdTag().with(aDc);
    }

    /**
     * Creates an HTML {@code <kbd>} element with the specified attributes.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A KbdTag element
     */
    public static KbdTag kbd(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new KbdTag(), aShortAttr);
    }

    /**
     * Creates an HTML {@code <kbd>} element with the specified attributes and text content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aText The text content
     * @return A KbdTag element
     */
    public static KbdTag kbd(final Attr.ShortForm aShortAttr, final String aText) {
        return Attr.ShortForm.addTo(new KbdTag().withText(aText), aShortAttr);
    }

    /**
     * Creates an HTML {@code <kbd>} element with the specified attributes and child DOM content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aDc The child DOM content nodes
     * @return A KbdTag element
     */
    public static KbdTag kbd(final Attr.ShortForm aShortAttr, final AbstractDomContent... aDc) {
        return Attr.ShortForm.addTo(new KbdTag().with(aDc), aShortAttr);
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
     * @param aText The text content
     * @return A LabelTag element
     */
    public static LabelTag label(final String aText) {
        return new LabelTag().withText(aText);
    }

    /**
     * Creates an HTML {@code <label>} element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return A LabelTag element
     */
    public static LabelTag label(final AbstractDomContent... aDc) {
        return new LabelTag().with(aDc);
    }

    /**
     * Creates an HTML {@code <label>} element with the specified attributes.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A LabelTag element
     */
    public static LabelTag label(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new LabelTag(), aShortAttr);
    }

    /**
     * Creates an HTML {@code <label>} element with the specified attributes and text content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aText The text content
     * @return A LabelTag element
     */
    public static LabelTag label(final Attr.ShortForm aShortAttr, final String aText) {
        return Attr.ShortForm.addTo(new LabelTag().withText(aText), aShortAttr);
    }

    /**
     * Creates an HTML {@code <label>} element with the specified attributes and child DOM content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aDc The child DOM content nodes
     * @return A LabelTag element
     */
    public static LabelTag label(final Attr.ShortForm aShortAttr, final AbstractDomContent... aDc) {
        return Attr.ShortForm.addTo(new LabelTag().with(aDc), aShortAttr);
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
     * @param aText The text content
     * @return A LegendTag element
     */
    public static LegendTag legend(final String aText) {
        return new LegendTag().withText(aText);
    }

    /**
     * Creates an HTML {@code <legend>} element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return A LegendTag element
     */
    public static LegendTag legend(final AbstractDomContent... aDc) {
        return new LegendTag().with(aDc);
    }

    /**
     * Creates an HTML {@code <legend>} element with the specified attributes.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A LegendTag element
     */
    public static LegendTag legend(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new LegendTag(), aShortAttr);
    }

    /**
     * Creates an HTML {@code <legend>} element with the specified attributes and text content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aText The text content
     * @return A LegendTag element
     */
    public static LegendTag legend(final Attr.ShortForm aShortAttr, final String aText) {
        return Attr.ShortForm.addTo(new LegendTag().withText(aText), aShortAttr);
    }

    /**
     * Creates an HTML {@code <legend>} element with the specified attributes and child DOM content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aDc The child DOM content nodes
     * @return A LegendTag element
     */
    public static LegendTag legend(final Attr.ShortForm aShortAttr, final AbstractDomContent... aDc) {
        return Attr.ShortForm.addTo(new LegendTag().with(aDc), aShortAttr);
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
     * @param aText The text content
     * @return A LiTag element
     */
    public static LiTag li(final String aText) {
        return new LiTag().withText(aText);
    }

    /**
     * Creates an HTML {@code <li>} element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return A LiTag element
     */
    public static LiTag li(final AbstractDomContent... aDc) {
        return new LiTag().with(aDc);
    }

    /**
     * Creates an HTML {@code <li>} element with the specified attributes.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A LiTag element
     */
    public static LiTag li(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new LiTag(), aShortAttr);
    }

    /**
     * Creates an HTML {@code <li>} element with the specified attributes and text content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aText The text content
     * @return A LiTag element
     */
    public static LiTag li(final Attr.ShortForm aShortAttr, final String aText) {
        return Attr.ShortForm.addTo(new LiTag().withText(aText), aShortAttr);
    }

    /**
     * Creates an HTML {@code <li>} element with the specified attributes and child DOM content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aDc The child DOM content nodes
     * @return A LiTag element
     */
    public static LiTag li(final Attr.ShortForm aShortAttr, final AbstractDomContent... aDc) {
        return Attr.ShortForm.addTo(new LiTag().with(aDc), aShortAttr);
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
     * @param aText The text content
     * @return A MainTag element
     */
    public static MainTag main(final String aText) {
        return new MainTag().withText(aText);
    }

    /**
     * Creates an HTML {@code <main>} element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return A MainTag element
     */
    public static MainTag main(final AbstractDomContent... aDc) {
        return new MainTag().with(aDc);
    }

    /**
     * Creates an HTML {@code <main>} element with the specified attributes.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A MainTag element
     */
    public static MainTag main(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new MainTag(), aShortAttr);
    }

    /**
     * Creates an HTML {@code <main>} element with the specified attributes and text content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aText The text content
     * @return A MainTag element
     */
    public static MainTag main(final Attr.ShortForm aShortAttr, final String aText) {
        return Attr.ShortForm.addTo(new MainTag().withText(aText), aShortAttr);
    }

    /**
     * Creates an HTML {@code <main>} element with the specified attributes and child DOM content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aDc The child DOM content nodes
     * @return A MainTag element
     */
    public static MainTag main(final Attr.ShortForm aShortAttr, final AbstractDomContent... aDc) {
        return Attr.ShortForm.addTo(new MainTag().with(aDc), aShortAttr);
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
     * @param aText The text content
     * @return A MapTag element
     */
    public static MapTag map(final String aText) {
        return new MapTag().withText(aText);
    }

    /**
     * Creates an HTML {@code <map>} element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return A MapTag element
     */
    public static MapTag map(final AbstractDomContent... aDc) {
        return new MapTag().with(aDc);
    }

    /**
     * Creates an HTML {@code <map>} element with the specified attributes.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A MapTag element
     */
    public static MapTag map(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new MapTag(), aShortAttr);
    }

    /**
     * Creates an HTML {@code <map>} element with the specified attributes and text content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aText The text content
     * @return A MapTag element
     */
    public static MapTag map(final Attr.ShortForm aShortAttr, final String aText) {
        return Attr.ShortForm.addTo(new MapTag().withText(aText), aShortAttr);
    }

    /**
     * Creates an HTML {@code <map>} element with the specified attributes and child DOM content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aDc The child DOM content nodes
     * @return A MapTag element
     */
    public static MapTag map(final Attr.ShortForm aShortAttr, final AbstractDomContent... aDc) {
        return Attr.ShortForm.addTo(new MapTag().with(aDc), aShortAttr);
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
     * @param aText The text content
     * @return A MarkTag element
     */
    public static MarkTag mark(final String aText) {
        return new MarkTag().withText(aText);
    }

    /**
     * Creates an HTML {@code <mark>} element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return A MarkTag element
     */
    public static MarkTag mark(final AbstractDomContent... aDc) {
        return new MarkTag().with(aDc);
    }

    /**
     * Creates an HTML {@code <mark>} element with the specified attributes.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A MarkTag element
     */
    public static MarkTag mark(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new MarkTag(), aShortAttr);
    }

    /**
     * Creates an HTML {@code <mark>} element with the specified attributes and text content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aText The text content
     * @return A MarkTag element
     */
    public static MarkTag mark(final Attr.ShortForm aShortAttr, final String aText) {
        return Attr.ShortForm.addTo(new MarkTag().withText(aText), aShortAttr);
    }

    /**
     * Creates an HTML {@code <mark>} element with the specified attributes and child DOM content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aDc The child DOM content nodes
     * @return A MarkTag element
     */
    public static MarkTag mark(final Attr.ShortForm aShortAttr, final AbstractDomContent... aDc) {
        return Attr.ShortForm.addTo(new MarkTag().with(aDc), aShortAttr);
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
     * @param aText The text content
     * @return A MenuTag element
     */
    public static MenuTag menu(final String aText) {
        return new MenuTag().withText(aText);
    }

    /**
     * Creates an HTML {@code <menu>} element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return A MenuTag element
     */
    public static MenuTag menu(final AbstractDomContent... aDc) {
        return new MenuTag().with(aDc);
    }

    /**
     * Creates an HTML {@code <menu>} element with the specified attributes.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A MenuTag element
     */
    public static MenuTag menu(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new MenuTag(), aShortAttr);
    }

    /**
     * Creates an HTML {@code <menu>} element with the specified attributes and text content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aText The text content
     * @return A MenuTag element
     */
    public static MenuTag menu(final Attr.ShortForm aShortAttr, final String aText) {
        return Attr.ShortForm.addTo(new MenuTag().withText(aText), aShortAttr);
    }

    /**
     * Creates an HTML {@code <menu>} element with the specified attributes and child DOM content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aDc The child DOM content nodes
     * @return A MenuTag element
     */
    public static MenuTag menu(final Attr.ShortForm aShortAttr, final AbstractDomContent... aDc) {
        return Attr.ShortForm.addTo(new MenuTag().with(aDc), aShortAttr);
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
     * @param aText The text content
     * @return A MenuitemTag element
     */
    public static MenuitemTag menuitem(final String aText) {
        return new MenuitemTag().withText(aText);
    }

    /**
     * Creates an HTML {@code <menuitem>} element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return A MenuitemTag element
     */
    public static MenuitemTag menuitem(final AbstractDomContent... aDc) {
        return new MenuitemTag().with(aDc);
    }

    /**
     * Creates an HTML {@code <menuitem>} element with the specified attributes.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A MenuitemTag element
     */
    public static MenuitemTag menuitem(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new MenuitemTag(), aShortAttr);
    }

    /**
     * Creates an HTML {@code <menuitem>} element with the specified attributes and text content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aText The text content
     * @return A MenuitemTag element
     */
    public static MenuitemTag menuitem(final Attr.ShortForm aShortAttr, final String aText) {
        return Attr.ShortForm.addTo(new MenuitemTag().withText(aText), aShortAttr);
    }

    /**
     * Creates an HTML {@code <menuitem>} element with the specified attributes and child DOM content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aDc The child DOM content nodes
     * @return A MenuitemTag element
     */
    public static MenuitemTag menuitem(final Attr.ShortForm aShortAttr, final AbstractDomContent... aDc) {
        return Attr.ShortForm.addTo(new MenuitemTag().with(aDc), aShortAttr);
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
     * @param aText The text content
     * @return A MeterTag element
     */
    public static MeterTag meter(final String aText) {
        return new MeterTag().withText(aText);
    }

    /**
     * Creates an HTML {@code <meter>} element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return A MeterTag element
     */
    public static MeterTag meter(final AbstractDomContent... aDc) {
        return new MeterTag().with(aDc);
    }

    /**
     * Creates an HTML {@code <meter>} element with the specified attributes.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A MeterTag element
     */
    public static MeterTag meter(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new MeterTag(), aShortAttr);
    }

    /**
     * Creates an HTML {@code <meter>} element with the specified attributes and text content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aText The text content
     * @return A MeterTag element
     */
    public static MeterTag meter(final Attr.ShortForm aShortAttr, final String aText) {
        return Attr.ShortForm.addTo(new MeterTag().withText(aText), aShortAttr);
    }

    /**
     * Creates an HTML {@code <meter>} element with the specified attributes and child DOM content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aDc The child DOM content nodes
     * @return A MeterTag element
     */
    public static MeterTag meter(final Attr.ShortForm aShortAttr, final AbstractDomContent... aDc) {
        return Attr.ShortForm.addTo(new MeterTag().with(aDc), aShortAttr);
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
     * @param aText The text content
     * @return A NavTag element
     */
    public static NavTag nav(final String aText) {
        return new NavTag().withText(aText);
    }

    /**
     * Creates an HTML {@code <nav>} element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return A NavTag element
     */
    public static NavTag nav(final AbstractDomContent... aDc) {
        return new NavTag().with(aDc);
    }

    /**
     * Creates an HTML {@code <nav>} element with the specified attributes.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A NavTag element
     */
    public static NavTag nav(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new NavTag(), aShortAttr);
    }

    /**
     * Creates an HTML {@code <nav>} element with the specified attributes and text content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aText The text content
     * @return A NavTag element
     */
    public static NavTag nav(final Attr.ShortForm aShortAttr, final String aText) {
        return Attr.ShortForm.addTo(new NavTag().withText(aText), aShortAttr);
    }

    /**
     * Creates an HTML {@code <nav>} element with the specified attributes and child DOM content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aDc The child DOM content nodes
     * @return A NavTag element
     */
    public static NavTag nav(final Attr.ShortForm aShortAttr, final AbstractDomContent... aDc) {
        return Attr.ShortForm.addTo(new NavTag().with(aDc), aShortAttr);
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
     * @param aText The text content
     * @return A NoscriptTag element
     */
    public static NoscriptTag noscript(final String aText) {
        return new NoscriptTag().withText(aText);
    }

    /**
     * Creates an HTML {@code <noscript>} element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return A NoscriptTag element
     */
    public static NoscriptTag noscript(final AbstractDomContent... aDc) {
        return new NoscriptTag().with(aDc);
    }

    /**
     * Creates an HTML {@code <noscript>} element with the specified attributes.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A NoscriptTag element
     */
    public static NoscriptTag noscript(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new NoscriptTag(), aShortAttr);
    }

    /**
     * Creates an HTML {@code <noscript>} element with the specified attributes and text content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aText The text content
     * @return A NoscriptTag element
     */
    public static NoscriptTag noscript(final Attr.ShortForm aShortAttr, final String aText) {
        return Attr.ShortForm.addTo(new NoscriptTag().withText(aText), aShortAttr);
    }

    /**
     * Creates an HTML {@code <noscript>} element with the specified attributes and child DOM content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aDc The child DOM content nodes
     * @return A NoscriptTag element
     */
    public static NoscriptTag noscript(final Attr.ShortForm aShortAttr, final AbstractDomContent... aDc) {
        return Attr.ShortForm.addTo(new NoscriptTag().with(aDc), aShortAttr);
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
     * @param aText The text content
     * @return A ObjectTag element
     */
    public static ObjectTag object(final String aText) {
        return new ObjectTag().withText(aText);
    }

    /**
     * Creates an HTML {@code <object>} element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return A ObjectTag element
     */
    public static ObjectTag object(final AbstractDomContent... aDc) {
        return new ObjectTag().with(aDc);
    }

    /**
     * Creates an HTML {@code <object>} element with the specified attributes.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A ObjectTag element
     */
    public static ObjectTag object(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new ObjectTag(), aShortAttr);
    }

    /**
     * Creates an HTML {@code <object>} element with the specified attributes and text content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aText The text content
     * @return A ObjectTag element
     */
    public static ObjectTag object(final Attr.ShortForm aShortAttr, final String aText) {
        return Attr.ShortForm.addTo(new ObjectTag().withText(aText), aShortAttr);
    }

    /**
     * Creates an HTML {@code <object>} element with the specified attributes and child DOM content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aDc The child DOM content nodes
     * @return A ObjectTag element
     */
    public static ObjectTag object(final Attr.ShortForm aShortAttr, final AbstractDomContent... aDc) {
        return Attr.ShortForm.addTo(new ObjectTag().with(aDc), aShortAttr);
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
     * @param aText The text content
     * @return A OlTag element
     */
    public static OlTag ol(final String aText) {
        return new OlTag().withText(aText);
    }

    /**
     * Creates an HTML {@code <ol>} element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return A OlTag element
     */
    public static OlTag ol(final AbstractDomContent... aDc) {
        return new OlTag().with(aDc);
    }

    /**
     * Creates an HTML {@code <ol>} element with the specified attributes.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A OlTag element
     */
    public static OlTag ol(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new OlTag(), aShortAttr);
    }

    /**
     * Creates an HTML {@code <ol>} element with the specified attributes and text content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aText The text content
     * @return A OlTag element
     */
    public static OlTag ol(final Attr.ShortForm aShortAttr, final String aText) {
        return Attr.ShortForm.addTo(new OlTag().withText(aText), aShortAttr);
    }

    /**
     * Creates an HTML {@code <ol>} element with the specified attributes and child DOM content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aDc The child DOM content nodes
     * @return A OlTag element
     */
    public static OlTag ol(final Attr.ShortForm aShortAttr, final AbstractDomContent... aDc) {
        return Attr.ShortForm.addTo(new OlTag().with(aDc), aShortAttr);
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
     * @param aText The text content
     * @return A OptgroupTag element
     */
    public static OptgroupTag optgroup(final String aText) {
        return new OptgroupTag().withText(aText);
    }

    /**
     * Creates an HTML {@code <optgroup>} element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return A OptgroupTag element
     */
    public static OptgroupTag optgroup(final AbstractDomContent... aDc) {
        return new OptgroupTag().with(aDc);
    }

    /**
     * Creates an HTML {@code <optgroup>} element with the specified attributes.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A OptgroupTag element
     */
    public static OptgroupTag optgroup(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new OptgroupTag(), aShortAttr);
    }

    /**
     * Creates an HTML {@code <optgroup>} element with the specified attributes and text content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aText The text content
     * @return A OptgroupTag element
     */
    public static OptgroupTag optgroup(final Attr.ShortForm aShortAttr, final String aText) {
        return Attr.ShortForm.addTo(new OptgroupTag().withText(aText), aShortAttr);
    }

    /**
     * Creates an HTML {@code <optgroup>} element with the specified attributes and child DOM content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aDc The child DOM content nodes
     * @return A OptgroupTag element
     */
    public static OptgroupTag optgroup(final Attr.ShortForm aShortAttr, final AbstractDomContent... aDc) {
        return Attr.ShortForm.addTo(new OptgroupTag().with(aDc), aShortAttr);
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
     * @param aText The text content
     * @return A OptionTag element
     */
    public static OptionTag option(final String aText) {
        return new OptionTag().withText(aText);
    }

    /**
     * Creates an HTML {@code <option>} element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return A OptionTag element
     */
    public static OptionTag option(final AbstractDomContent... aDc) {
        return new OptionTag().with(aDc);
    }

    /**
     * Creates an HTML {@code <option>} element with the specified attributes.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A OptionTag element
     */
    public static OptionTag option(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new OptionTag(), aShortAttr);
    }

    /**
     * Creates an HTML {@code <option>} element with the specified attributes and text content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aText The text content
     * @return A OptionTag element
     */
    public static OptionTag option(final Attr.ShortForm aShortAttr, final String aText) {
        return Attr.ShortForm.addTo(new OptionTag().withText(aText), aShortAttr);
    }

    /**
     * Creates an HTML {@code <option>} element with the specified attributes and child DOM content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aDc The child DOM content nodes
     * @return A OptionTag element
     */
    public static OptionTag option(final Attr.ShortForm aShortAttr, final AbstractDomContent... aDc) {
        return Attr.ShortForm.addTo(new OptionTag().with(aDc), aShortAttr);
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
     * @param aText The text content
     * @return A OutputTag element
     */
    public static OutputTag output(final String aText) {
        return new OutputTag().withText(aText);
    }

    /**
     * Creates an HTML {@code <output>} element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return A OutputTag element
     */
    public static OutputTag output(final AbstractDomContent... aDc) {
        return new OutputTag().with(aDc);
    }

    /**
     * Creates an HTML {@code <output>} element with the specified attributes.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A OutputTag element
     */
    public static OutputTag output(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new OutputTag(), aShortAttr);
    }

    /**
     * Creates an HTML {@code <output>} element with the specified attributes and text content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aText The text content
     * @return A OutputTag element
     */
    public static OutputTag output(final Attr.ShortForm aShortAttr, final String aText) {
        return Attr.ShortForm.addTo(new OutputTag().withText(aText), aShortAttr);
    }

    /**
     * Creates an HTML {@code <output>} element with the specified attributes and child DOM content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aDc The child DOM content nodes
     * @return A OutputTag element
     */
    public static OutputTag output(final Attr.ShortForm aShortAttr, final AbstractDomContent... aDc) {
        return Attr.ShortForm.addTo(new OutputTag().with(aDc), aShortAttr);
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
     * @param aText The text content
     * @return A PTag element
     */
    public static PTag p(final String aText) {
        return new PTag().withText(aText);
    }

    /**
     * Creates an HTML {@code <p>} element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return A PTag element
     */
    public static PTag p(final AbstractDomContent... aDc) {
        return new PTag().with(aDc);
    }

    /**
     * Creates an HTML {@code <p>} element with the specified attributes.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A PTag element
     */
    public static PTag p(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new PTag(), aShortAttr);
    }

    /**
     * Creates an HTML {@code <p>} element with the specified attributes and text content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aText The text content
     * @return A PTag element
     */
    public static PTag p(final Attr.ShortForm aShortAttr, final String aText) {
        return Attr.ShortForm.addTo(new PTag().withText(aText), aShortAttr);
    }

    /**
     * Creates an HTML {@code <p>} element with the specified attributes and child DOM content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aDc The child DOM content nodes
     * @return A PTag element
     */
    public static PTag p(final Attr.ShortForm aShortAttr, final AbstractDomContent... aDc) {
        return Attr.ShortForm.addTo(new PTag().with(aDc), aShortAttr);
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
     * @param aText The text content
     * @return A PictureTag element
     */
    public static PictureTag picture(final String aText) {
        return new PictureTag().withText(aText);
    }

    /**
     * Creates an HTML {@code <picture>} element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return A PictureTag element
     */
    public static PictureTag picture(final AbstractDomContent... aDc) {
        return new PictureTag().with(aDc);
    }

    /**
     * Creates an HTML {@code <picture>} element with the specified attributes.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A PictureTag element
     */
    public static PictureTag picture(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new PictureTag(), aShortAttr);
    }

    /**
     * Creates an HTML {@code <picture>} element with the specified attributes and text content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aText The text content
     * @return A PictureTag element
     */
    public static PictureTag picture(final Attr.ShortForm aShortAttr, final String aText) {
        return Attr.ShortForm.addTo(new PictureTag().withText(aText), aShortAttr);
    }

    /**
     * Creates an HTML {@code <picture>} element with the specified attributes and child DOM content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aDc The child DOM content nodes
     * @return A PictureTag element
     */
    public static PictureTag picture(final Attr.ShortForm aShortAttr, final AbstractDomContent... aDc) {
        return Attr.ShortForm.addTo(new PictureTag().with(aDc), aShortAttr);
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
     * @param aText The text content
     * @return A PreTag element
     */
    public static PreTag pre(final String aText) {
        return new PreTag().withText(aText);
    }

    /**
     * Creates an HTML {@code <pre>} element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return A PreTag element
     */
    public static PreTag pre(final AbstractDomContent... aDc) {
        return new PreTag().with(aDc);
    }

    /**
     * Creates an HTML {@code <pre>} element with the specified attributes.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A PreTag element
     */
    public static PreTag pre(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new PreTag(), aShortAttr);
    }

    /**
     * Creates an HTML {@code <pre>} element with the specified attributes and text content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aText The text content
     * @return A PreTag element
     */
    public static PreTag pre(final Attr.ShortForm aShortAttr, final String aText) {
        return Attr.ShortForm.addTo(new PreTag().withText(aText), aShortAttr);
    }

    /**
     * Creates an HTML {@code <pre>} element with the specified attributes and child DOM content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aDc The child DOM content nodes
     * @return A PreTag element
     */
    public static PreTag pre(final Attr.ShortForm aShortAttr, final AbstractDomContent... aDc) {
        return Attr.ShortForm.addTo(new PreTag().with(aDc), aShortAttr);
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
     * @param aText The text content
     * @return A ProgressTag element
     */
    public static ProgressTag progress(final String aText) {
        return new ProgressTag().withText(aText);
    }

    /**
     * Creates an HTML {@code <progress>} element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return A ProgressTag element
     */
    public static ProgressTag progress(final AbstractDomContent... aDc) {
        return new ProgressTag().with(aDc);
    }

    /**
     * Creates an HTML {@code <progress>} element with the specified attributes.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A ProgressTag element
     */
    public static ProgressTag progress(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new ProgressTag(), aShortAttr);
    }

    /**
     * Creates an HTML {@code <progress>} element with the specified attributes and text content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aText The text content
     * @return A ProgressTag element
     */
    public static ProgressTag progress(final Attr.ShortForm aShortAttr, final String aText) {
        return Attr.ShortForm.addTo(new ProgressTag().withText(aText), aShortAttr);
    }

    /**
     * Creates an HTML {@code <progress>} element with the specified attributes and child DOM content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aDc The child DOM content nodes
     * @return A ProgressTag element
     */
    public static ProgressTag progress(final Attr.ShortForm aShortAttr, final AbstractDomContent... aDc) {
        return Attr.ShortForm.addTo(new ProgressTag().with(aDc), aShortAttr);
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
     * @param aText The text content
     * @return A QTag element
     */
    public static QTag q(final String aText) {
        return new QTag().withText(aText);
    }

    /**
     * Creates an HTML {@code <q>} element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return A QTag element
     */
    public static QTag q(final AbstractDomContent... aDc) {
        return new QTag().with(aDc);
    }

    /**
     * Creates an HTML {@code <q>} element with the specified attributes.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A QTag element
     */
    public static QTag q(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new QTag(), aShortAttr);
    }

    /**
     * Creates an HTML {@code <q>} element with the specified attributes and text content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aText The text content
     * @return A QTag element
     */
    public static QTag q(final Attr.ShortForm aShortAttr, final String aText) {
        return Attr.ShortForm.addTo(new QTag().withText(aText), aShortAttr);
    }

    /**
     * Creates an HTML {@code <q>} element with the specified attributes and child DOM content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aDc The child DOM content nodes
     * @return A QTag element
     */
    public static QTag q(final Attr.ShortForm aShortAttr, final AbstractDomContent... aDc) {
        return Attr.ShortForm.addTo(new QTag().with(aDc), aShortAttr);
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
     * @param aText The text content
     * @return A RpTag element
     */
    public static RpTag rp(final String aText) {
        return new RpTag().withText(aText);
    }

    /**
     * Creates an HTML {@code <rp>} element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return A RpTag element
     */
    public static RpTag rp(final AbstractDomContent... aDc) {
        return new RpTag().with(aDc);
    }

    /**
     * Creates an HTML {@code <rp>} element with the specified attributes.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A RpTag element
     */
    public static RpTag rp(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new RpTag(), aShortAttr);
    }

    /**
     * Creates an HTML {@code <rp>} element with the specified attributes and text content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aText The text content
     * @return A RpTag element
     */
    public static RpTag rp(final Attr.ShortForm aShortAttr, final String aText) {
        return Attr.ShortForm.addTo(new RpTag().withText(aText), aShortAttr);
    }

    /**
     * Creates an HTML {@code <rp>} element with the specified attributes and child DOM content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aDc The child DOM content nodes
     * @return A RpTag element
     */
    public static RpTag rp(final Attr.ShortForm aShortAttr, final AbstractDomContent... aDc) {
        return Attr.ShortForm.addTo(new RpTag().with(aDc), aShortAttr);
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
     * @param aText The text content
     * @return A RtTag element
     */
    public static RtTag rt(final String aText) {
        return new RtTag().withText(aText);
    }

    /**
     * Creates an HTML {@code <rt>} element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return A RtTag element
     */
    public static RtTag rt(final AbstractDomContent... aDc) {
        return new RtTag().with(aDc);
    }

    /**
     * Creates an HTML {@code <rt>} element with the specified attributes.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A RtTag element
     */
    public static RtTag rt(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new RtTag(), aShortAttr);
    }

    /**
     * Creates an HTML {@code <rt>} element with the specified attributes and text content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aText The text content
     * @return A RtTag element
     */
    public static RtTag rt(final Attr.ShortForm aShortAttr, final String aText) {
        return Attr.ShortForm.addTo(new RtTag().withText(aText), aShortAttr);
    }

    /**
     * Creates an HTML {@code <rt>} element with the specified attributes and child DOM content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aDc The child DOM content nodes
     * @return A RtTag element
     */
    public static RtTag rt(final Attr.ShortForm aShortAttr, final AbstractDomContent... aDc) {
        return Attr.ShortForm.addTo(new RtTag().with(aDc), aShortAttr);
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
     * @param aText The text content
     * @return A RubyTag element
     */
    public static RubyTag ruby(final String aText) {
        return new RubyTag().withText(aText);
    }

    /**
     * Creates an HTML {@code <ruby>} element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return A RubyTag element
     */
    public static RubyTag ruby(final AbstractDomContent... aDc) {
        return new RubyTag().with(aDc);
    }

    /**
     * Creates an HTML {@code <ruby>} element with the specified attributes.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A RubyTag element
     */
    public static RubyTag ruby(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new RubyTag(), aShortAttr);
    }

    /**
     * Creates an HTML {@code <ruby>} element with the specified attributes and text content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aText The text content
     * @return A RubyTag element
     */
    public static RubyTag ruby(final Attr.ShortForm aShortAttr, final String aText) {
        return Attr.ShortForm.addTo(new RubyTag().withText(aText), aShortAttr);
    }

    /**
     * Creates an HTML {@code <ruby>} element with the specified attributes and child DOM content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aDc The child DOM content nodes
     * @return A RubyTag element
     */
    public static RubyTag ruby(final Attr.ShortForm aShortAttr, final AbstractDomContent... aDc) {
        return Attr.ShortForm.addTo(new RubyTag().with(aDc), aShortAttr);
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
     * @param aText The text content
     * @return A STag element
     */
    public static STag s(final String aText) {
        return new STag().withText(aText);
    }

    /**
     * Creates an HTML {@code <s>} element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return A STag element
     */
    public static STag s(final AbstractDomContent... aDc) {
        return new STag().with(aDc);
    }

    /**
     * Creates an HTML {@code <s>} element with the specified attributes.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A STag element
     */
    public static STag s(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new STag(), aShortAttr);
    }

    /**
     * Creates an HTML {@code <s>} element with the specified attributes and text content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aText The text content
     * @return A STag element
     */
    public static STag s(final Attr.ShortForm aShortAttr, final String aText) {
        return Attr.ShortForm.addTo(new STag().withText(aText), aShortAttr);
    }

    /**
     * Creates an HTML {@code <s>} element with the specified attributes and child DOM content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aDc The child DOM content nodes
     * @return A STag element
     */
    public static STag s(final Attr.ShortForm aShortAttr, final AbstractDomContent... aDc) {
        return Attr.ShortForm.addTo(new STag().with(aDc), aShortAttr);
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
     * @param aText The text content
     * @return A SampTag element
     */
    public static SampTag samp(final String aText) {
        return new SampTag().withText(aText);
    }

    /**
     * Creates an HTML {@code <samp>} element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return A SampTag element
     */
    public static SampTag samp(final AbstractDomContent... aDc) {
        return new SampTag().with(aDc);
    }

    /**
     * Creates an HTML {@code <samp>} element with the specified attributes.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A SampTag element
     */
    public static SampTag samp(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new SampTag(), aShortAttr);
    }

    /**
     * Creates an HTML {@code <samp>} element with the specified attributes and text content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aText The text content
     * @return A SampTag element
     */
    public static SampTag samp(final Attr.ShortForm aShortAttr, final String aText) {
        return Attr.ShortForm.addTo(new SampTag().withText(aText), aShortAttr);
    }

    /**
     * Creates an HTML {@code <samp>} element with the specified attributes and child DOM content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aDc The child DOM content nodes
     * @return A SampTag element
     */
    public static SampTag samp(final Attr.ShortForm aShortAttr, final AbstractDomContent... aDc) {
        return Attr.ShortForm.addTo(new SampTag().with(aDc), aShortAttr);
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
     * @param aText The text content
     * @return A ScriptTag element
     */
    public static ScriptTag script(final String aText) {
        return new ScriptTag().with(new UnescapedText(aText));
    }

    /**
     * Creates an HTML {@code <script>} element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return A ScriptTag element
     */
    public static ScriptTag script(final AbstractDomContent... aDc) {
        return new ScriptTag().with(aDc);
    }

    /**
     * Creates an HTML {@code <script>} element with the specified attributes.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A ScriptTag element
     */
    public static ScriptTag script(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new ScriptTag(), aShortAttr);
    }

    /**
     * Creates an HTML {@code <script>} element with the specified attributes and text content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aText The text content
     * @return A ScriptTag element
     */
    public static ScriptTag script(final Attr.ShortForm aShortAttr, final String aText) {
        return Attr.ShortForm.addTo(new ScriptTag().with(new UnescapedText(aText)), aShortAttr);
    }

    /**
     * Creates an HTML {@code <script>} element with the specified attributes and child DOM content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aDc The child DOM content nodes
     * @return A ScriptTag element
     */
    public static ScriptTag script(final Attr.ShortForm aShortAttr, final AbstractDomContent... aDc) {
        return Attr.ShortForm.addTo(new ScriptTag().with(aDc), aShortAttr);
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
     * @param aText The text content
     * @return A SectionTag element
     */
    public static SectionTag section(final String aText) {
        return new SectionTag().withText(aText);
    }

    /**
     * Creates an HTML {@code <section>} element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return A SectionTag element
     */
    public static SectionTag section(final AbstractDomContent... aDc) {
        return new SectionTag().with(aDc);
    }

    /**
     * Creates an HTML {@code <section>} element with the specified attributes.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A SectionTag element
     */
    public static SectionTag section(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new SectionTag(), aShortAttr);
    }

    /**
     * Creates an HTML {@code <section>} element with the specified attributes and text content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aText The text content
     * @return A SectionTag element
     */
    public static SectionTag section(final Attr.ShortForm aShortAttr, final String aText) {
        return Attr.ShortForm.addTo(new SectionTag().withText(aText), aShortAttr);
    }

    /**
     * Creates an HTML {@code <section>} element with the specified attributes and child DOM content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aDc The child DOM content nodes
     * @return A SectionTag element
     */
    public static SectionTag section(final Attr.ShortForm aShortAttr, final AbstractDomContent... aDc) {
        return Attr.ShortForm.addTo(new SectionTag().with(aDc), aShortAttr);
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
     * @param aText The text content
     * @return A SelectTag element
     */
    public static SelectTag select(final String aText) {
        return new SelectTag().withText(aText);
    }

    /**
     * Creates an HTML {@code <select>} element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return A SelectTag element
     */
    public static SelectTag select(final AbstractDomContent... aDc) {
        return new SelectTag().with(aDc);
    }

    /**
     * Creates an HTML {@code <select>} element with the specified attributes.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A SelectTag element
     */
    public static SelectTag select(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new SelectTag(), aShortAttr);
    }

    /**
     * Creates an HTML {@code <select>} element with the specified attributes and text content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aText The text content
     * @return A SelectTag element
     */
    public static SelectTag select(final Attr.ShortForm aShortAttr, final String aText) {
        return Attr.ShortForm.addTo(new SelectTag().withText(aText), aShortAttr);
    }

    /**
     * Creates an HTML {@code <select>} element with the specified attributes and child DOM content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aDc The child DOM content nodes
     * @return A SelectTag element
     */
    public static SelectTag select(final Attr.ShortForm aShortAttr, final AbstractDomContent... aDc) {
        return Attr.ShortForm.addTo(new SelectTag().with(aDc), aShortAttr);
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
     * @param aText The text content
     * @return A SlotTag element
     */
    public static SlotTag slot(final String aText) {
        return new SlotTag().withText(aText);
    }

    /**
     * Creates an HTML {@code <slot>} element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return A SlotTag element
     */
    public static SlotTag slot(final AbstractDomContent... aDc) {
        return new SlotTag().with(aDc);
    }

    /**
     * Creates an HTML {@code <slot>} element with the specified attributes.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A SlotTag element
     */
    public static SlotTag slot(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new SlotTag(), aShortAttr);
    }

    /**
     * Creates an HTML {@code <slot>} element with the specified attributes and text content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aText The text content
     * @return A SlotTag element
     */
    public static SlotTag slot(final Attr.ShortForm aShortAttr, final String aText) {
        return Attr.ShortForm.addTo(new SlotTag().withText(aText), aShortAttr);
    }

    /**
     * Creates an HTML {@code <slot>} element with the specified attributes and child DOM content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aDc The child DOM content nodes
     * @return A SlotTag element
     */
    public static SlotTag slot(final Attr.ShortForm aShortAttr, final AbstractDomContent... aDc) {
        return Attr.ShortForm.addTo(new SlotTag().with(aDc), aShortAttr);
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
     * @param aText The text content
     * @return A SmallTag element
     */
    public static SmallTag small(final String aText) {
        return new SmallTag().withText(aText);
    }

    /**
     * Creates an HTML {@code <small>} element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return A SmallTag element
     */
    public static SmallTag small(final AbstractDomContent... aDc) {
        return new SmallTag().with(aDc);
    }

    /**
     * Creates an HTML {@code <small>} element with the specified attributes.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A SmallTag element
     */
    public static SmallTag small(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new SmallTag(), aShortAttr);
    }

    /**
     * Creates an HTML {@code <small>} element with the specified attributes and text content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aText The text content
     * @return A SmallTag element
     */
    public static SmallTag small(final Attr.ShortForm aShortAttr, final String aText) {
        return Attr.ShortForm.addTo(new SmallTag().withText(aText), aShortAttr);
    }

    /**
     * Creates an HTML {@code <small>} element with the specified attributes and child DOM content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aDc The child DOM content nodes
     * @return A SmallTag element
     */
    public static SmallTag small(final Attr.ShortForm aShortAttr, final AbstractDomContent... aDc) {
        return Attr.ShortForm.addTo(new SmallTag().with(aDc), aShortAttr);
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
     * @param aText The text content
     * @return A SpanTag element
     */
    public static SpanTag span(final String aText) {
        return new SpanTag().withText(aText);
    }

    /**
     * Creates an HTML {@code <span>} element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return A SpanTag element
     */
    public static SpanTag span(final AbstractDomContent... aDc) {
        return new SpanTag().with(aDc);
    }

    /**
     * Creates an HTML {@code <span>} element with the specified attributes.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A SpanTag element
     */
    public static SpanTag span(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new SpanTag(), aShortAttr);
    }

    /**
     * Creates an HTML {@code <span>} element with the specified attributes and text content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aText The text content
     * @return A SpanTag element
     */
    public static SpanTag span(final Attr.ShortForm aShortAttr, final String aText) {
        return Attr.ShortForm.addTo(new SpanTag().withText(aText), aShortAttr);
    }

    /**
     * Creates an HTML {@code <span>} element with the specified attributes and child DOM content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aDc The child DOM content nodes
     * @return A SpanTag element
     */
    public static SpanTag span(final Attr.ShortForm aShortAttr, final AbstractDomContent... aDc) {
        return Attr.ShortForm.addTo(new SpanTag().with(aDc), aShortAttr);
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
     * @param aText The text content
     * @return A StrongTag element
     */
    public static StrongTag strong(final String aText) {
        return new StrongTag().withText(aText);
    }

    /**
     * Creates an HTML {@code <strong>} element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return A StrongTag element
     */
    public static StrongTag strong(final AbstractDomContent... aDc) {
        return new StrongTag().with(aDc);
    }

    /**
     * Creates an HTML {@code <strong>} element with the specified attributes.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A StrongTag element
     */
    public static StrongTag strong(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new StrongTag(), aShortAttr);
    }

    /**
     * Creates an HTML {@code <strong>} element with the specified attributes and text content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aText The text content
     * @return A StrongTag element
     */
    public static StrongTag strong(final Attr.ShortForm aShortAttr, final String aText) {
        return Attr.ShortForm.addTo(new StrongTag().withText(aText), aShortAttr);
    }

    /**
     * Creates an HTML {@code <strong>} element with the specified attributes and child DOM content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aDc The child DOM content nodes
     * @return A StrongTag element
     */
    public static StrongTag strong(final Attr.ShortForm aShortAttr, final AbstractDomContent... aDc) {
        return Attr.ShortForm.addTo(new StrongTag().with(aDc), aShortAttr);
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
     * Creates an HTML {@code <style>} element with the specified CSS rules.
     *
     * @param aText The first CSS rule
     * @param aMoreTexts Additional CSS rules
     * @return A StyleTag element
     */
    public static StyleTag style(final String aText, final String... aMoreTexts) {
        return createStyleTag(null, aText, aMoreTexts);
    }

    /**
     * Creates an HTML {@code <style>} element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return A StyleTag element
     */
    public static StyleTag style(final AbstractDomContent... aDc) {
        return new StyleTag().with(aDc);
    }

    /**
     * Creates an HTML {@code <style>} element with the specified attributes.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A StyleTag element
     */
    public static StyleTag style(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new StyleTag(), aShortAttr);
    }

    /**
     * Creates an HTML {@code <style>} element with the specified attributes and CSS rules.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aText The first CSS rule
     * @param aMoreTexts Additional CSS rules
     * @return A StyleTag element
     */
    public static StyleTag style(final Attr.ShortForm aShortAttr, final String aText, final String... aMoreTexts) {
        return createStyleTag(aShortAttr, aText, aMoreTexts);
    }

    /**
     * Helper method to create a StyleTag with joined CSS rules.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes, or null
     * @param aText The first CSS rule
     * @param aMoreTexts Additional CSS rules
     * @return A StyleTag element
     */
    private static StyleTag createStyleTag(final Attr.ShortForm aShortAttr, final String aText,
      final String... aMoreTexts) {
        final StringJoiner joiner = new StringJoiner(LF);
        final StyleTag style = new StyleTag();

        if (aText != null && !aText.isBlank()) {
            joiner.add(aText.trim());
        }

        if (aMoreTexts != null) {
            for (final String rule : aMoreTexts) {
                if (rule != null && !rule.isBlank()) {
                    joiner.add(rule.trim());
                }
            }
        }

        if (joiner.length() > 0) {
            style.with(new UnescapedText(joiner.toString()));
        }

        return aShortAttr != null ? Attr.ShortForm.addTo(style, aShortAttr) : style;
    }

    /**
     * Creates an HTML {@code <style>} element with the specified attributes and child DOM content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aDc The child DOM content nodes
     * @return A StyleTag element
     */
    public static StyleTag style(final Attr.ShortForm aShortAttr, final AbstractDomContent... aDc) {
        return Attr.ShortForm.addTo(new StyleTag().with(aDc), aShortAttr);
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
     * @param aText The text content
     * @return A SubTag element
     */
    public static SubTag sub(final String aText) {
        return new SubTag().withText(aText);
    }

    /**
     * Creates an HTML {@code <sub>} element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return A SubTag element
     */
    public static SubTag sub(final AbstractDomContent... aDc) {
        return new SubTag().with(aDc);
    }

    /**
     * Creates an HTML {@code <sub>} element with the specified attributes.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A SubTag element
     */
    public static SubTag sub(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new SubTag(), aShortAttr);
    }

    /**
     * Creates an HTML {@code <sub>} element with the specified attributes and text content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aText The text content
     * @return A SubTag element
     */
    public static SubTag sub(final Attr.ShortForm aShortAttr, final String aText) {
        return Attr.ShortForm.addTo(new SubTag().withText(aText), aShortAttr);
    }

    /**
     * Creates an HTML {@code <sub>} element with the specified attributes and child DOM content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aDc The child DOM content nodes
     * @return A SubTag element
     */
    public static SubTag sub(final Attr.ShortForm aShortAttr, final AbstractDomContent... aDc) {
        return Attr.ShortForm.addTo(new SubTag().with(aDc), aShortAttr);
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
     * @param aText The text content
     * @return A SummaryTag element
     */
    public static SummaryTag summary(final String aText) {
        return new SummaryTag().withText(aText);
    }

    /**
     * Creates an HTML {@code <summary>} element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return A SummaryTag element
     */
    public static SummaryTag summary(final AbstractDomContent... aDc) {
        return new SummaryTag().with(aDc);
    }

    /**
     * Creates an HTML {@code <summary>} element with the specified attributes.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A SummaryTag element
     */
    public static SummaryTag summary(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new SummaryTag(), aShortAttr);
    }

    /**
     * Creates an HTML {@code <summary>} element with the specified attributes and text content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aText The text content
     * @return A SummaryTag element
     */
    public static SummaryTag summary(final Attr.ShortForm aShortAttr, final String aText) {
        return Attr.ShortForm.addTo(new SummaryTag().withText(aText), aShortAttr);
    }

    /**
     * Creates an HTML {@code <summary>} element with the specified attributes and child DOM content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aDc The child DOM content nodes
     * @return A SummaryTag element
     */
    public static SummaryTag summary(final Attr.ShortForm aShortAttr, final AbstractDomContent... aDc) {
        return Attr.ShortForm.addTo(new SummaryTag().with(aDc), aShortAttr);
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
     * @param aText The text content
     * @return A SupTag element
     */
    public static SupTag sup(final String aText) {
        return new SupTag().withText(aText);
    }

    /**
     * Creates an HTML {@code <sup>} element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return A SupTag element
     */
    public static SupTag sup(final AbstractDomContent... aDc) {
        return new SupTag().with(aDc);
    }

    /**
     * Creates an HTML {@code <sup>} element with the specified attributes.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A SupTag element
     */
    public static SupTag sup(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new SupTag(), aShortAttr);
    }

    /**
     * Creates an HTML {@code <sup>} element with the specified attributes and text content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aText The text content
     * @return A SupTag element
     */
    public static SupTag sup(final Attr.ShortForm aShortAttr, final String aText) {
        return Attr.ShortForm.addTo(new SupTag().withText(aText), aShortAttr);
    }

    /**
     * Creates an HTML {@code <sup>} element with the specified attributes and child DOM content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aDc The child DOM content nodes
     * @return A SupTag element
     */
    public static SupTag sup(final Attr.ShortForm aShortAttr, final AbstractDomContent... aDc) {
        return Attr.ShortForm.addTo(new SupTag().with(aDc), aShortAttr);
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
     * @param aText The text content
     * @return A TableTag element
     */
    public static TableTag table(final String aText) {
        return new TableTag().withText(aText);
    }

    /**
     * Creates an HTML {@code <table>} element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return A TableTag element
     */
    public static TableTag table(final AbstractDomContent... aDc) {
        return new TableTag().with(aDc);
    }

    /**
     * Creates an HTML {@code <table>} element with the specified attributes.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A TableTag element
     */
    public static TableTag table(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new TableTag(), aShortAttr);
    }

    /**
     * Creates an HTML {@code <table>} element with the specified attributes and text content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aText The text content
     * @return A TableTag element
     */
    public static TableTag table(final Attr.ShortForm aShortAttr, final String aText) {
        return Attr.ShortForm.addTo(new TableTag().withText(aText), aShortAttr);
    }

    /**
     * Creates an HTML {@code <table>} element with the specified attributes and child DOM content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aDc The child DOM content nodes
     * @return A TableTag element
     */
    public static TableTag table(final Attr.ShortForm aShortAttr, final AbstractDomContent... aDc) {
        return Attr.ShortForm.addTo(new TableTag().with(aDc), aShortAttr);
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
     * @param aText The text content
     * @return A TbodyTag element
     */
    public static TbodyTag tbody(final String aText) {
        return new TbodyTag().withText(aText);
    }

    /**
     * Creates an HTML {@code <tbody>} element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return A TbodyTag element
     */
    public static TbodyTag tbody(final AbstractDomContent... aDc) {
        return new TbodyTag().with(aDc);
    }

    /**
     * Creates an HTML {@code <tbody>} element with the specified attributes.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A TbodyTag element
     */
    public static TbodyTag tbody(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new TbodyTag(), aShortAttr);
    }

    /**
     * Creates an HTML {@code <tbody>} element with the specified attributes and text content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aText The text content
     * @return A TbodyTag element
     */
    public static TbodyTag tbody(final Attr.ShortForm aShortAttr, final String aText) {
        return Attr.ShortForm.addTo(new TbodyTag().withText(aText), aShortAttr);
    }

    /**
     * Creates an HTML {@code <tbody>} element with the specified attributes and child DOM content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aDc The child DOM content nodes
     * @return A TbodyTag element
     */
    public static TbodyTag tbody(final Attr.ShortForm aShortAttr, final AbstractDomContent... aDc) {
        return Attr.ShortForm.addTo(new TbodyTag().with(aDc), aShortAttr);
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
     * @param aText The text content
     * @return A TdTag element
     */
    public static TdTag td(final String aText) {
        return new TdTag().withText(aText);
    }

    /**
     * Creates an HTML {@code <td>} element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return A TdTag element
     */
    public static TdTag td(final AbstractDomContent... aDc) {
        return new TdTag().with(aDc);
    }

    /**
     * Creates an HTML {@code <td>} element with the specified attributes.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A TdTag element
     */
    public static TdTag td(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new TdTag(), aShortAttr);
    }

    /**
     * Creates an HTML {@code <td>} element with the specified attributes and text content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aText The text content
     * @return A TdTag element
     */
    public static TdTag td(final Attr.ShortForm aShortAttr, final String aText) {
        return Attr.ShortForm.addTo(new TdTag().withText(aText), aShortAttr);
    }

    /**
     * Creates an HTML {@code <td>} element with the specified attributes and child DOM content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aDc The child DOM content nodes
     * @return A TdTag element
     */
    public static TdTag td(final Attr.ShortForm aShortAttr, final AbstractDomContent... aDc) {
        return Attr.ShortForm.addTo(new TdTag().with(aDc), aShortAttr);
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
     * @param aText The text content
     * @return A TemplateTag element
     */
    public static TemplateTag template(final String aText) {
        return new TemplateTag().withText(aText);
    }

    /**
     * Creates an HTML {@code <template>} element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return A TemplateTag element
     */
    public static TemplateTag template(final AbstractDomContent... aDc) {
        return new TemplateTag().with(aDc);
    }

    /**
     * Creates an HTML {@code <template>} element with the specified attributes.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A TemplateTag element
     */
    public static TemplateTag template(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new TemplateTag(), aShortAttr);
    }

    /**
     * Creates an HTML {@code <template>} element with the specified attributes and text content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aText The text content
     * @return A TemplateTag element
     */
    public static TemplateTag template(final Attr.ShortForm aShortAttr, final String aText) {
        return Attr.ShortForm.addTo(new TemplateTag().withText(aText), aShortAttr);
    }

    /**
     * Creates an HTML {@code <template>} element with the specified attributes and child DOM content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aDc The child DOM content nodes
     * @return A TemplateTag element
     */
    public static TemplateTag template(final Attr.ShortForm aShortAttr, final AbstractDomContent... aDc) {
        return Attr.ShortForm.addTo(new TemplateTag().with(aDc), aShortAttr);
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
     * @param aText The text content
     * @return A TextareaTag element
     */
    public static TextareaTag textarea(final String aText) {
        return new TextareaTag().withText(aText);
    }

    /**
     * Creates an HTML {@code <textarea>} element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return A TextareaTag element
     */
    public static TextareaTag textarea(final AbstractDomContent... aDc) {
        return new TextareaTag().with(aDc);
    }

    /**
     * Creates an HTML {@code <textarea>} element with the specified attributes.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A TextareaTag element
     */
    public static TextareaTag textarea(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new TextareaTag(), aShortAttr);
    }

    /**
     * Creates an HTML {@code <textarea>} element with the specified attributes and text content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aText The text content
     * @return A TextareaTag element
     */
    public static TextareaTag textarea(final Attr.ShortForm aShortAttr, final String aText) {
        return Attr.ShortForm.addTo(new TextareaTag().withText(aText), aShortAttr);
    }

    /**
     * Creates an HTML {@code <textarea>} element with the specified attributes and child DOM content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aDc The child DOM content nodes
     * @return A TextareaTag element
     */
    public static TextareaTag textarea(final Attr.ShortForm aShortAttr, final AbstractDomContent... aDc) {
        return Attr.ShortForm.addTo(new TextareaTag().with(aDc), aShortAttr);
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
     * @param aText The text content
     * @return A TfootTag element
     */
    public static TfootTag tfoot(final String aText) {
        return new TfootTag().withText(aText);
    }

    /**
     * Creates an HTML {@code <tfoot>} element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return A TfootTag element
     */
    public static TfootTag tfoot(final AbstractDomContent... aDc) {
        return new TfootTag().with(aDc);
    }

    /**
     * Creates an HTML {@code <tfoot>} element with the specified attributes.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A TfootTag element
     */
    public static TfootTag tfoot(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new TfootTag(), aShortAttr);
    }

    /**
     * Creates an HTML {@code <tfoot>} element with the specified attributes and text content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aText The text content
     * @return A TfootTag element
     */
    public static TfootTag tfoot(final Attr.ShortForm aShortAttr, final String aText) {
        return Attr.ShortForm.addTo(new TfootTag().withText(aText), aShortAttr);
    }

    /**
     * Creates an HTML {@code <tfoot>} element with the specified attributes and child DOM content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aDc The child DOM content nodes
     * @return A TfootTag element
     */
    public static TfootTag tfoot(final Attr.ShortForm aShortAttr, final AbstractDomContent... aDc) {
        return Attr.ShortForm.addTo(new TfootTag().with(aDc), aShortAttr);
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
     * @param aText The text content
     * @return A ThTag element
     */
    public static ThTag th(final String aText) {
        return new ThTag().withText(aText);
    }

    /**
     * Creates an HTML {@code <th>} element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return A ThTag element
     */
    public static ThTag th(final AbstractDomContent... aDc) {
        return new ThTag().with(aDc);
    }

    /**
     * Creates an HTML {@code <th>} element with the specified attributes.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A ThTag element
     */
    public static ThTag th(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new ThTag(), aShortAttr);
    }

    /**
     * Creates an HTML {@code <th>} element with the specified attributes and text content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aText The text content
     * @return A ThTag element
     */
    public static ThTag th(final Attr.ShortForm aShortAttr, final String aText) {
        return Attr.ShortForm.addTo(new ThTag().withText(aText), aShortAttr);
    }

    /**
     * Creates an HTML {@code <th>} element with the specified attributes and child DOM content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aDc The child DOM content nodes
     * @return A ThTag element
     */
    public static ThTag th(final Attr.ShortForm aShortAttr, final AbstractDomContent... aDc) {
        return Attr.ShortForm.addTo(new ThTag().with(aDc), aShortAttr);
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
     * @param aText The text content
     * @return A TheadTag element
     */
    public static TheadTag thead(final String aText) {
        return new TheadTag().withText(aText);
    }

    /**
     * Creates an HTML {@code <thead>} element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return A TheadTag element
     */
    public static TheadTag thead(final AbstractDomContent... aDc) {
        return new TheadTag().with(aDc);
    }

    /**
     * Creates an HTML {@code <thead>} element with the specified attributes.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A TheadTag element
     */
    public static TheadTag thead(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new TheadTag(), aShortAttr);
    }

    /**
     * Creates an HTML {@code <thead>} element with the specified attributes and text content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aText The text content
     * @return A TheadTag element
     */
    public static TheadTag thead(final Attr.ShortForm aShortAttr, final String aText) {
        return Attr.ShortForm.addTo(new TheadTag().withText(aText), aShortAttr);
    }

    /**
     * Creates an HTML {@code <thead>} element with the specified attributes and child DOM content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aDc The child DOM content nodes
     * @return A TheadTag element
     */
    public static TheadTag thead(final Attr.ShortForm aShortAttr, final AbstractDomContent... aDc) {
        return Attr.ShortForm.addTo(new TheadTag().with(aDc), aShortAttr);
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
     * @param aText The text content
     * @return A TimeTag element
     */
    public static TimeTag time(final String aText) {
        return new TimeTag().withText(aText);
    }

    /**
     * Creates an HTML {@code <time>} element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return A TimeTag element
     */
    public static TimeTag time(final AbstractDomContent... aDc) {
        return new TimeTag().with(aDc);
    }

    /**
     * Creates an HTML {@code <time>} element with the specified attributes.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A TimeTag element
     */
    public static TimeTag time(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new TimeTag(), aShortAttr);
    }

    /**
     * Creates an HTML {@code <time>} element with the specified attributes and text content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aText The text content
     * @return A TimeTag element
     */
    public static TimeTag time(final Attr.ShortForm aShortAttr, final String aText) {
        return Attr.ShortForm.addTo(new TimeTag().withText(aText), aShortAttr);
    }

    /**
     * Creates an HTML {@code <time>} element with the specified attributes and child DOM content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aDc The child DOM content nodes
     * @return A TimeTag element
     */
    public static TimeTag time(final Attr.ShortForm aShortAttr, final AbstractDomContent... aDc) {
        return Attr.ShortForm.addTo(new TimeTag().with(aDc), aShortAttr);
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
     * @param aText The text content
     * @return A TitleTag element
     */
    public static TitleTag title(final String aText) {
        return new TitleTag().withText(aText);
    }

    /**
     * Creates an HTML {@code <title>} element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return A TitleTag element
     */
    public static TitleTag title(final AbstractDomContent... aDc) {
        return new TitleTag().with(aDc);
    }

    /**
     * Creates an HTML {@code <title>} element with the specified attributes.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A TitleTag element
     */
    public static TitleTag title(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new TitleTag(), aShortAttr);
    }

    /**
     * Creates an HTML {@code <title>} element with the specified attributes and text content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aText The text content
     * @return A TitleTag element
     */
    public static TitleTag title(final Attr.ShortForm aShortAttr, final String aText) {
        return Attr.ShortForm.addTo(new TitleTag().withText(aText), aShortAttr);
    }

    /**
     * Creates an HTML {@code <title>} element with the specified attributes and child DOM content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aDc The child DOM content nodes
     * @return A TitleTag element
     */
    public static TitleTag title(final Attr.ShortForm aShortAttr, final AbstractDomContent... aDc) {
        return Attr.ShortForm.addTo(new TitleTag().with(aDc), aShortAttr);
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
     * @param aText The text content
     * @return A TrTag element
     */
    public static TrTag tr(final String aText) {
        return new TrTag().withText(aText);
    }

    /**
     * Creates an HTML {@code <tr>} element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return A TrTag element
     */
    public static TrTag tr(final AbstractDomContent... aDc) {
        return new TrTag().with(aDc);
    }

    /**
     * Creates an HTML {@code <tr>} element with the specified attributes.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A TrTag element
     */
    public static TrTag tr(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new TrTag(), aShortAttr);
    }

    /**
     * Creates an HTML {@code <tr>} element with the specified attributes and text content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aText The text content
     * @return A TrTag element
     */
    public static TrTag tr(final Attr.ShortForm aShortAttr, final String aText) {
        return Attr.ShortForm.addTo(new TrTag().withText(aText), aShortAttr);
    }

    /**
     * Creates an HTML {@code <tr>} element with the specified attributes and child DOM content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aDc The child DOM content nodes
     * @return A TrTag element
     */
    public static TrTag tr(final Attr.ShortForm aShortAttr, final AbstractDomContent... aDc) {
        return Attr.ShortForm.addTo(new TrTag().with(aDc), aShortAttr);
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
     * @param aText The text content
     * @return A UTag element
     */
    public static UTag u(final String aText) {
        return new UTag().withText(aText);
    }

    /**
     * Creates an HTML {@code <u>} element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return A UTag element
     */
    public static UTag u(final AbstractDomContent... aDc) {
        return new UTag().with(aDc);
    }

    /**
     * Creates an HTML {@code <u>} element with the specified attributes.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A UTag element
     */
    public static UTag u(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new UTag(), aShortAttr);
    }

    /**
     * Creates an HTML {@code <u>} element with the specified attributes and text content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aText The text content
     * @return A UTag element
     */
    public static UTag u(final Attr.ShortForm aShortAttr, final String aText) {
        return Attr.ShortForm.addTo(new UTag().withText(aText), aShortAttr);
    }

    /**
     * Creates an HTML {@code <u>} element with the specified attributes and child DOM content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aDc The child DOM content nodes
     * @return A UTag element
     */
    public static UTag u(final Attr.ShortForm aShortAttr, final AbstractDomContent... aDc) {
        return Attr.ShortForm.addTo(new UTag().with(aDc), aShortAttr);
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
     * @param aText The text content
     * @return A UlTag element
     */
    public static UlTag ul(final String aText) {
        return new UlTag().withText(aText);
    }

    /**
     * Creates an HTML {@code <ul>} element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return A UlTag element
     */
    public static UlTag ul(final AbstractDomContent... aDc) {
        return new UlTag().with(aDc);
    }

    /**
     * Creates an HTML {@code <ul>} element with the specified attributes.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A UlTag element
     */
    public static UlTag ul(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new UlTag(), aShortAttr);
    }

    /**
     * Creates an HTML {@code <ul>} element with the specified attributes and text content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aText The text content
     * @return A UlTag element
     */
    public static UlTag ul(final Attr.ShortForm aShortAttr, final String aText) {
        return Attr.ShortForm.addTo(new UlTag().withText(aText), aShortAttr);
    }

    /**
     * Creates an HTML {@code <ul>} element with the specified attributes and child DOM content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aDc The child DOM content nodes
     * @return A UlTag element
     */
    public static UlTag ul(final Attr.ShortForm aShortAttr, final AbstractDomContent... aDc) {
        return Attr.ShortForm.addTo(new UlTag().with(aDc), aShortAttr);
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
     * @param aText The text content
     * @return A VarTag element
     */
    public static VarTag var(final String aText) {
        return new VarTag().withText(aText);
    }

    /**
     * Creates an HTML {@code <var>} element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return A VarTag element
     */
    public static VarTag var(final AbstractDomContent... aDc) {
        return new VarTag().with(aDc);
    }

    /**
     * Creates an HTML {@code <var>} element with the specified attributes.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A VarTag element
     */
    public static VarTag var(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new VarTag(), aShortAttr);
    }

    /**
     * Creates an HTML {@code <var>} element with the specified attributes and text content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aText The text content
     * @return A VarTag element
     */
    public static VarTag var(final Attr.ShortForm aShortAttr, final String aText) {
        return Attr.ShortForm.addTo(new VarTag().withText(aText), aShortAttr);
    }

    /**
     * Creates an HTML {@code <var>} element with the specified attributes and child DOM content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aDc The child DOM content nodes
     * @return A VarTag element
     */
    public static VarTag var(final Attr.ShortForm aShortAttr, final AbstractDomContent... aDc) {
        return Attr.ShortForm.addTo(new VarTag().with(aDc), aShortAttr);
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
     * @param aText The text content
     * @return A VideoTag element
     */
    public static VideoTag video(final String aText) {
        return new VideoTag().withText(aText);
    }

    /**
     * Creates an HTML {@code <video>} element with the specified child DOM content.
     *
     * @param aDc The child DOM content nodes
     * @return A VideoTag element
     */
    public static VideoTag video(final AbstractDomContent... aDc) {
        return new VideoTag().with(aDc);
    }

    /**
     * Creates an HTML {@code <video>} element with the specified attributes.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @return A VideoTag element
     */
    public static VideoTag video(final Attr.ShortForm aShortAttr) {
        return Attr.ShortForm.addTo(new VideoTag(), aShortAttr);
    }

    /**
     * Creates an HTML {@code <video>} element with the specified attributes and text content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aText The text content
     * @return A VideoTag element
     */
    public static VideoTag video(final Attr.ShortForm aShortAttr, final String aText) {
        return Attr.ShortForm.addTo(new VideoTag().withText(aText), aShortAttr);
    }

    /**
     * Creates an HTML {@code <video>} element with the specified attributes and child DOM content.
     *
     * @param aShortAttr The shorthand attribute containing ID and classes
     * @param aDc The child DOM content nodes
     * @return A VideoTag element
     */
    public static VideoTag video(final Attr.ShortForm aShortAttr, final AbstractDomContent... aDc) {
        return Attr.ShortForm.addTo(new VideoTag().with(aDc), aShortAttr);
    }

}
