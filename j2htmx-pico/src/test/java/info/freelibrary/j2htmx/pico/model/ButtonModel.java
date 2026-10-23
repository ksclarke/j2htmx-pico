package info.freelibrary.j2htmx.pico.model;

/**
 * Button model representation for testing.
 */
public class ButtonModel {

    /** The button text. */
    private final String myText;

    /**
     * Constructs a ButtonModel with the given text.
     *
     * @param aText The button text
     */
    public ButtonModel(final String aText) {
        this.myText = aText;
    }

    /**
     * Gets the button text.
     *
     * @return The text
     */
    public String getText() {
        return myText;
    }
}
