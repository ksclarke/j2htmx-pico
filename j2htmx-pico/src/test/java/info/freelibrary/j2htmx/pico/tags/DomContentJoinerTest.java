/*
 * This code was derived from j2html, which is distributed under the Apache License, Version 2.0.
 * See META-INF/THIRD-PARTY.txt and META-INF/licenses/Apache-2.0.txt for more information.
 *
 * This file has been modified for j2htmx-pico by Kevin S. Clarke (my.j2htmx-pico@kevinclarke.info)
 *
 * The modifications are licensed under the Mozilla Public License, Version 2.0.
 * See LICENSE.txt in the parent project's root for more information.
 */

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
