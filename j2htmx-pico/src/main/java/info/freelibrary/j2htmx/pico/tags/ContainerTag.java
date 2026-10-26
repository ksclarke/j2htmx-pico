package info.freelibrary.j2htmx.pico.tags;

import info.freelibrary.j2htmx.pico.Config;
import info.freelibrary.j2htmx.pico.attributes.Attribute;
import info.freelibrary.j2htmx.pico.rendering.HtmlBuilder;
import info.freelibrary.j2htmx.pico.rendering.IndentedHtml;
import info.freelibrary.j2htmx.pico.rendering.TagBuilder;
import info.freelibrary.util.warnings.PMD;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Represents an HTML element that can contain child nodes and text content.
 *
 * @param <T> The concrete container tag type
 */
public class ContainerTag<T extends ContainerTag<T>> extends AbstractTag<T> {

    /** The child DOM content nodes of this container tag. */
    protected final List<AbstractDomContent> myChildren;

    /**
     * Constructs a ContainerTag with the given tag name.
     *
     * @param aTagName The HTML tag name
     */
    public ContainerTag(final String aTagName) {
        super(aTagName);
        this.myChildren = new ArrayList<>();
    }


    /**
     * Appends a AbstractDomContent-object to the end of this element.
     *
     * @param aChild AbstractDomContent-object to be appended
     * @return itself for easy chaining
     * @throws RuntimeException If trying to append a tag to itself
     */
    @SuppressWarnings({PMD.AVOID_THROWING_RAW_EXCEPTION_TYPES, "PMD.CompareObjectsWithEquals"})
    public T with(final AbstractDomContent aChild) {
        if (this == aChild) {
            throw new RuntimeException("Cannot append a tag to itself.");
        }

        if (aChild != null) {
            // in some cases, like when using iff(), we ignore null children
            myChildren.add(aChild);
        }

        return self();
    }


    /**
     * Call with-method based on condition {@link #with(AbstractDomContent)}.
     *
     * @param aCondition the condition to use
     * @param aChild AbstractDomContent-object to be appended if condition met
     * @return itself for easy chaining
     */
    public T condWith(final boolean aCondition, final AbstractDomContent aChild) {
        return aCondition ? this.with(aChild) : self();
    }


    /**
     * Appends a list of AbstractDomContent-objects to the end of this element.
     *
     * @param aChildren AbstractDomContent-objects to be appended
     * @return itself for easy chaining
     */
    public T with(final Iterable<? extends AbstractDomContent> aChildren) {
        if (aChildren != null) {
            for (final AbstractDomContent child : aChildren) {
                this.with(child);
            }
        }
        return self();
    }


    /**
     * Call with-method based on condition {@link #with(java.lang.Iterable)}.
     *
     * @param aCondition the condition to use
     * @param aChildren AbstractDomContent-objects to be appended if condition met
     * @return itself for easy chaining
     */
    public T condWith(final boolean aCondition, final Iterable<? extends AbstractDomContent> aChildren) {
        return aCondition ? this.with(aChildren) : self();
    }


    /**
     * Appends the AbstractDomContent-objects to the end of this element.
     *
     * @param aChildren AbstractDomContent-objects to be appended
     * @return itself for easy chaining
     */
    public T with(final AbstractDomContent... aChildren) {
        for (final AbstractDomContent child : aChildren) {
            with(child);
        }
        return self();
    }


    /**
     * Appends the AbstractDomContent-objects in the stream to the end of this element.
     *
     * @param aChildren Stream of AbstractDomContent-objects to be appended
     * @return itself for easy chaining
     */
    public T with(final Stream<AbstractDomContent> aChildren) {
        aChildren.forEach(this::with);
        return self();
    }


    /**
     * Call with-method based on condition {@link #with(AbstractDomContent...)}.
     *
     * @param aCondition the condition to use
     * @param aChildren AbstractDomContent-objects to be appended if condition met
     * @return itself for easy chaining
     */
    public T condWith(final boolean aCondition, final AbstractDomContent... aChildren) {
        return aCondition ? this.with(aChildren) : self();
    }


    /**
     * Appends a Text-object to this element.
     *
     * @param aText the text to be appended
     * @return itself for easy chaining
     */
    public T withText(final String aText) {
        return with(new Text(aText));
    }

    /**
     * Gets number of child nodes this tag element contains.
     *
     * @return The number of child nodes
     */
    public int getNumChildren() {
        return myChildren.size();
    }

    /**
     * Render the ContainerTag and its children, adding newlines before each child and using Config.indenter to indent
     * child based on how deep in the tree it is.
     *
     * @return the rendered and formatted string
     * @throws RuntimeException If rendering fails with an IOException
     */
    @SuppressWarnings({PMD.AVOID_THROWING_RAW_EXCEPTION_TYPES})
    public String renderFormatted() {
        try {
            return render(IndentedHtml.into(new StringBuilder(), Config.global())).toString();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public <A extends Appendable> A render(final HtmlBuilder<A> aHtmlBuilder, final Object aModel) throws IOException {
        if (hasTagName()) {
            final TagBuilder tagBuilder = aHtmlBuilder.appendStartTag(getTagName());
            for (final Attribute attribute : getAttributes()) {
                attribute.render(tagBuilder, aModel);
            }
            tagBuilder.completeTag();
        }

        for (final AbstractDomContent child : myChildren) {
            child.render(aHtmlBuilder, aModel);
        }

        if (hasTagName()) {
            aHtmlBuilder.appendEndTag(getTagName());
        }

        return aHtmlBuilder.output();
    }

}
