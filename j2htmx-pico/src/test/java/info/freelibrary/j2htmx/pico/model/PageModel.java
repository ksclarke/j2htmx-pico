package info.freelibrary.j2htmx.pico.model;

public class PageModel {

    private final String title;
    private final String text;
    private final ButtonModel buttonModel;

    public PageModel(final String title, final String text, final ButtonModel buttonModel) {
        this.title = title;
        this.text = text;
        this.buttonModel = buttonModel;
    }

    public ButtonModel getButtonModel() {
        return buttonModel;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }
}