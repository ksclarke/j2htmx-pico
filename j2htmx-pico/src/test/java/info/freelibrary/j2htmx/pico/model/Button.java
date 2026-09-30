package info.freelibrary.j2htmx.pico.model;

import static info.freelibrary.j2htmx.pico.TagCreator.div;

import info.freelibrary.j2htmx.pico.rendering.HtmlBuilder;
import info.freelibrary.j2htmx.pico.tags.ContainerTag;

import java.io.IOException;

public class Button extends Template<PageModel> {

    private final ContainerTag<?> template;

    public Button() {
        // @formatter:off
        template =
                div()
                .withClass("button")
                .with(
                        div()
                        .withClass("button-text")
                        .with(
                                new ButtonText()
                        )
                );
        // @formatter:on
    }

    @Override
    public <T extends Appendable> void renderTemplate(final HtmlBuilder<T> writer, final PageModel model)
      throws IOException {
        template.render(writer, model.getButtonModel().getText());
    }
}

class ButtonText extends Template<String> {

    @Override
    public <T extends Appendable> void renderTemplate(final HtmlBuilder<T> writer, final String model)
      throws IOException {
        writer.appendEscapedText(model);
    }
}
