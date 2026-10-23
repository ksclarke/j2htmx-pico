package info.freelibrary.j2htmx.pico.model;

/**
 * Page model representation for testing.
 */
public class PageModel {

    /** The page title. */
    private final String myTitle;

    /** The page text. */
    private final String myText;

    /** The button model. */
    private final ButtonModel myButtonModel;

    /**
     * Constructs a PageModel instance.
     *
     * @param aTitle The page title
     * @param aText The page text
     * @param aButtonModel The button model
     */
    public PageModel(final String aTitle, final String aText, final ButtonModel aButtonModel) {
        this.myTitle = aTitle;
        this.myText = aText;
        this.myButtonModel = aButtonModel;
    }

    /**
     * Gets the button model.
     *
     * @return The button model
     */
    public ButtonModel getButtonModel() {
        return myButtonModel;
    }

    /**
     * Gets the page title.
     *
     * @return The page title
     */
    public String getTitle() {
        return myTitle;
    }

    /**
     * Gets the page text.
     *
     * @return The page text
     */
    public String getText() {
        return myText;
    }
}
