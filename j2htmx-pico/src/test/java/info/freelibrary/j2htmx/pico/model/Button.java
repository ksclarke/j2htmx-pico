package info.freelibrary.j2htmx.pico.model;

import static info.freelibrary.j2htmx.pico.TagCreator.div;

import info.freelibrary.j2htmx.pico.rendering.HtmlBuilder;
import info.freelibrary.j2htmx.pico.tags.ContainerTag;

import java.io.IOException;

/**
 * Button template for testing.
 */
public class Button extends Template<PageModel> {

    /** The container tag template for the button. */
    private final ContainerTag<?> myTemplate;

    /**
     * Constructs a Button template.
     */
    public Button() {
        // @formatter:off
        myTemplate =
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
    public <T extends Appendable> void renderTemplate(final HtmlBuilder<T> aWriter, final PageModel aModel)
      throws IOException {
        myTemplate.render(aWriter, aModel.getButtonModel().getText());
    }
}

/**
 * Button text template for testing.
 */
class ButtonText extends Template<String> {

    @Override
    public <T extends Appendable> void renderTemplate(final HtmlBuilder<T> aWriter, final String aModel)
      throws IOException {
        aWriter.appendEscapedText(aModel);
    }
}
