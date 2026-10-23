package info.freelibrary.j2htmx.pico.tags;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import info.freelibrary.util.warnings.Checkstyle;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link DomContentJoiner}.
 */
@SuppressWarnings({Checkstyle.MULTIPLE_STRING_LITERALS, "MultipleStringLiterals"})
public class DomContentJoinerTest {
    /**
     * Tests joining DOM content with a delimiter.
     */
    @Test
    public void testJoin() {
        assertThat(DomContentJoiner.join(",", true, "a", "b", "c"), is(new UnescapedText("a,b,c")));
        assertThat(DomContentJoiner.join(",", false, "a", "b", "c"), is(new UnescapedText("a,b,c")));
    }
}
