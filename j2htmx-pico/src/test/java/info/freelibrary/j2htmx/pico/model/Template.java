package info.freelibrary.j2htmx.pico.model;

import info.freelibrary.j2htmx.pico.rendering.HtmlBuilder;
import info.freelibrary.j2htmx.pico.tags.DomContent;
import java.io.IOException;

public abstract class Template<T> extends DomContent {

    @Override
    @SuppressWarnings("unchecked")
    public final void renderModel(final Appendable writer, final Object model) throws IOException {
        renderTemplate(writer, (T) model);
    }

    public abstract void renderTemplate(final Appendable writer, final T model) throws IOException;

    @Override
    public <T extends Appendable> T render(final HtmlBuilder<T> builder, final Object model) throws IOException{
        renderModel(builder.output(), model);
        return builder.output();
    }
}
