package info.freelibrary.j2htmx.pico.model;

import info.freelibrary.j2htmx.pico.tags.ContainerTag;
import java.io.IOException;
import static info.freelibrary.j2htmx.pico.TagCreator.div;

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
    public void renderTemplate(final Appendable writer, final PageModel model) throws IOException {
        template.renderModel(writer, model.getButtonModel().getText());
    }
}

class ButtonText extends Template<String> {

    @Override
    public void renderTemplate(final Appendable writer, final String model) throws IOException {
        writer.append(model);
    }
}
