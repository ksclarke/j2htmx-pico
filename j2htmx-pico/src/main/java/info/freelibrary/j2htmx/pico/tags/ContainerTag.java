package info.freelibrary.j2htmx.pico.tags;

import info.freelibrary.j2htmx.pico.Config;
import info.freelibrary.j2htmx.pico.attributes.Attribute;
import info.freelibrary.j2htmx.pico.rendering.TagBuilder;
import info.freelibrary.j2htmx.pico.rendering.FlatHtml;
import info.freelibrary.j2htmx.pico.rendering.HtmlBuilder;
import info.freelibrary.j2htmx.pico.rendering.IndentedHtml;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Represents an HTML element that can contain child nodes and text content.
 *
 * @param <T> The concrete container tag type
 */
public class ContainerTag<T extends ContainerTag<T>> extends Tag<T> {

    /** The child DOM content nodes of this container tag. */
    protected List<DomContent> children;

    /**
     * Constructs a ContainerTag with the given tag name.
     *
     * @param tagName The HTML tag name
     */
    public ContainerTag(String tagName) {
        super(tagName);
        this.children = new ArrayList<>();
    }


    /**
     * Appends a DomContent-object to the end of this element
     *
     * @param child DomContent-object to be appended
     * @return itself for easy chaining
     */
    public T with(DomContent child) {
        if (this == child) {
            throw new RuntimeException("Cannot append a tag to itself.");
        }
        if (child != null) {
            // in some cases, like when using iff(), we ignore null children
            children.add(child);
        }
        return self();
    }


    /**
     * Call with-method based on condition
     * {@link #with(DomContent child)}
     *
     * @param condition the condition to use
     * @param child     DomContent-object to be appended if condition met
     * @return itself for easy chaining
     */
    public T condWith(boolean condition, DomContent child) {
        return condition ? this.with(child) : self();
    }


    /**
     * Appends a list of DomContent-objects to the end of this element
     *
     * @param children DomContent-objects to be appended
     * @return itself for easy chaining
     */
    public T with(Iterable<? extends DomContent> children) {
        if (children != null) {
            for (DomContent child : children) {
                this.with(child);
            }
        }
        return self();
    }


    /**
     * Call with-method based on condition
     * {@link #with(java.lang.Iterable)}
     *
     * @param condition the condition to use
     * @param children  DomContent-objects to be appended if condition met
     * @return itself for easy chaining
     */
    public T condWith(boolean condition, Iterable<? extends DomContent> children) {
        return condition ? this.with(children) : self();
    }


    /**
     * Appends the DomContent-objects to the end of this element
     *
     * @param children DomContent-objects to be appended
     * @return itself for easy chaining
     */
    public T with(DomContent... children) {
        for (DomContent child : children) {
            with(child);
        }
        return self();
    }


    /**
     * Appends the DomContent-objects in the stream to the end of this element
     *
     * @param children Stream of DomContent-objects to be appended
     * @return itself for easy chaining
     */
    public T with(Stream<DomContent> children) {
        children.forEach(this::with);
        return self();
    }


    /**
     * Call with-method based on condition
     * {@link #with(DomContent... children)}
     *
     * @param condition the condition to use
     * @param children  DomContent-objects to be appended if condition met
     * @return itself for easy chaining
     */
    public T condWith(boolean condition, DomContent... children) {
        return condition ? this.with(children) : self();
    }


    /**
     * Appends a Text-object to this element
     *
     * @param text the text to be appended
     * @return itself for easy chaining
     */
    public T withText(String text) {
        return with(new Text(text));
    }

    /**
     * Gets number of child nodes this tag element contains.
     *
     * @return The number of child nodes
     */
    public int getNumChildren() {
        return children.size();
    }

    /**
     * Render the ContainerTag and its children, adding newlines before each
     * child and using Config.indenter to indent child based on how deep
     * in the tree it is
     *
     * @return the rendered and formatted string
     */
    public String renderFormatted() {
        try {
            return render(IndentedHtml.into(new StringBuilder(), Config.global())).toString();
        }catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public <A extends Appendable> A render(HtmlBuilder<A> builder, Object model) throws IOException {
        if (hasTagName()) {
            TagBuilder tagBuilder = builder.appendStartTag(getTagName());
            for(Attribute attribute : getAttributes()){
                attribute.render(tagBuilder, model);
            }
            tagBuilder.completeTag();
        }

        for(DomContent child : children){
            child.render(builder, model);
        }

        if(hasTagName()) {
            builder.appendEndTag(getTagName());
        }

        return builder.output();
    }

    @Override
    @Deprecated
    public void renderModel(Appendable writer, Object model) throws IOException {
        HtmlBuilder<?> builder = (writer instanceof HtmlBuilder)
            ? (HtmlBuilder<?>) writer
            : FlatHtml.into(writer, Config.global());

        render(builder, model);
    }
}
