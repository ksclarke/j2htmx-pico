package info.freelibrary.j2htmx.pico.attributes;

import static info.freelibrary.j2htmx.pico.TagCreator.attrs;
import static info.freelibrary.j2htmx.pico.TagCreator.div;
import static info.freelibrary.j2htmx.pico.TagCreator.input;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

import info.freelibrary.util.warnings.Checkstyle;
import org.junit.jupiter.api.Test;

/**
 * Tests for attribute shorthand notation parsing and application.
 */
@SuppressWarnings({Checkstyle.MULTIPLE_STRING_LITERALS, "MultipleStringLiterals"})
public class AttrShortFormTest {

    /**
     * Tests short form attributes on empty tags.
     *
     * @throws Exception If an error occurs
     */
    @Test
    public void addTo_emptyTag() throws Exception {
        final String expected = "<input id=\"some-id\" class=\"some-class\">";
        final String actual = input(attrs("#some-id.some-class")).render();
        assertThat(actual, is(expected));
    }

    /**
     * Tests short form attributes on container tags.
     *
     * @throws Exception If an error occurs
     */
    @Test
    public void addTo_containerTag() throws Exception {
        final String expected = "<div id=\"some-id\" class=\"some-class\"></div>";
        final String actual = div(attrs("#some-id.some-class")).render();
        assertThat(actual, is(expected));
    }

    /**
     * Tests short form with ID only.
     *
     * @throws Exception If an error occurs
     */
    @Test
    public void addTo_justId() throws Exception {
        final String expected = "<div id=\"some-id\"></div>";
        final String actual = div(attrs("#some-id")).render();
        assertThat(actual, is(expected));
    }

    /**
     * Tests short form with class only.
     *
     * @throws Exception If an error occurs
     */
    @Test
    public void addTo_justClass() throws Exception {
        final String expected = "<div class=\"some-class\"></div>";
        final String actual = div(attrs(".some-class")).render();
        assertThat(actual, is(expected));
    }

    /**
     * Tests short form with two classes.
     *
     * @throws Exception If an error occurs
     */
    @Test
    public void addTo_addTwoClasses() throws Exception {
        final String expected = "<div class=\"some-class some-other-class\"></div>";
        final String actual = div(attrs(".some-class.some-other-class")).render();
        assertThat(actual, is(expected));
    }

    /**
     * Tests short form without ID or class selector throws exception.
     *
     * @throws Exception If an error occurs
     */
    @Test
    public void addTo_stringWithoutIdOrClass() throws Exception {
        assertThrows(IllegalArgumentException.class, () -> div(attrs("some-class")).render());
    }

    /**
     * Tests short form with two IDs throws exception.
     *
     * @throws Exception If an error occurs
     */
    @Test
    public void addTo_stringWithTwoIds() throws Exception {
        assertThrows(IllegalArgumentException.class, () -> div(attrs("#id1#id2")).render());
    }

    /**
     * Tests short form with misplaced ID throws exception.
     *
     * @throws Exception If an error occurs
     */
    @Test
    public void addTo_stringWithStupidlyPlacedId() throws Exception {
        assertThrows(IllegalArgumentException.class, () -> div(attrs("id1#id2")).render());
    }

}
