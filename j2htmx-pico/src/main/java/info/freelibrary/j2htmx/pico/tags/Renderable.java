package info.freelibrary.j2htmx.pico.tags;

import info.freelibrary.j2htmx.pico.Config;
import info.freelibrary.j2htmx.pico.rendering.FlatHtml;
import info.freelibrary.j2htmx.pico.rendering.HtmlBuilder;

import java.io.IOException;
import java.io.UncheckedIOException;

/**
 * An interface for objects that can be rendered to HTML.
 */
public interface Renderable {

    /**
     * Render the Renderable and it's children using the supplied builder.
     *
     * @param builder A builder that can compose HTML elements.
     * @param model A model object to provide data for children to render.
     * @param <T> The type of the Appendable which HTML is being appended to.
     * @return The Appendable to which HTML has been appended.
     * @throws IOException If an I/O error occurs.
     */
    <T extends Appendable> T render(HtmlBuilder<T> builder, Object model) throws IOException;

    /**
     * Render the Renderable and it's children using the supplied builder.
     *
     * @param builder A builder that can compose HTML elements.
     * @param <T> The type of the Appendable to which HTML is being appended.
     * @return The Appendable to which HTML has been appended.
     * @throws IOException If an I/O error occurs.
     */
    default <T extends Appendable> T render(final HtmlBuilder<T> builder) throws IOException {
        return render(builder, null);
    }

    /**
     * Create a StringBuilder and use it to render the Renderable and it's children.
     *
     * @return The rendered HTML string
     * @throws UncheckedIOException If an I/O error occurs
     */
    default String render() {
        try {
            return render(FlatHtml.into(new StringBuilder(), Config.global())).toString();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
