package info.freelibrary.j2htmx.pico.model;

import info.freelibrary.j2htmx.pico.rendering.HtmlBuilder;
import info.freelibrary.j2htmx.pico.tags.DomContent;

import java.io.IOException;

public abstract class Template<T> extends DomContent {

    public abstract <U extends Appendable> void renderTemplate(final HtmlBuilder<U> writer, final T model)
      throws IOException;

    @Override
    @SuppressWarnings("unchecked")
    public final <U extends Appendable> U render(final HtmlBuilder<U> builder, final Object model) throws IOException {
        renderTemplate(builder, (T) model);
        return builder.output();
    }
}
