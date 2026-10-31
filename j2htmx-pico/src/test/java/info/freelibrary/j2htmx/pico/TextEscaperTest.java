/*
 * This code was derived from j2html, which is distributed under the Apache License, Version 2.0.
 * See LICENSE-ASL-2.0.txt in the parent project's root directory for more information.
 *
 * This file was modified for j2htmx-pico by Kevin S. Clarke (my.j2htmx-pico@kevinclarke.info).
 *
 * The modifications are licensed under the Mozilla Public License, Version 2.0.
 * See LICENSE-MPL-2.0.txt in the parent project's root directory for more information.
 */

package info.freelibrary.j2htmx.pico;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

import info.freelibrary.j2htmx.pico.utils.EscapeUtil;
import info.freelibrary.j2htmx.pico.utils.TextEscaper;
import info.freelibrary.util.warnings.Checkstyle;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link TextEscaper}.
 */
@SuppressWarnings({Checkstyle.MULTIPLE_STRING_LITERALS, "MultipleStringLiterals"})
public class TextEscaperTest {

    /**
     * Tests custom and default text escaper implementations.
     *
     * @throws Exception If an error occurs
     */
    @Test
    public void testTextEscaper() throws Exception {
        String expected = "&lt;div&gt;&lt;/div&gt;";
        assertThat("default text escaper works",
          Config.getGlobalTextEscaper().escape("<div></div>"), is(expected));

        Config.setGlobalTextEscaper(new NoOpEscaper());
        assertThat("user can change text escaper implementation",
          Config.getGlobalTextEscaper(), is(instanceOf(NoOpEscaper.class)));

        expected = "<div></div>";
        assertThat("user provided text escaper actually works",
          Config.getGlobalTextEscaper().escape("<div></div>"), is(expected));
        Config.setGlobalTextEscaper(EscapeUtil::escape); // reset escaper
    }

    /**
     * No-op text escaper implementation for testing.
     */
    private static final class NoOpEscaper implements TextEscaper {

        @Override
        public String escape(final String aText) {
            return aText;
        }

    }

}
