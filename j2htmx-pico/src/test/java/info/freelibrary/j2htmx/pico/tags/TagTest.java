package info.freelibrary.j2htmx.pico.tags;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Locale;

import org.junit.jupiter.api.Test;

import info.freelibrary.j2htmx.pico.TagCreator;
import info.freelibrary.j2htmx.pico.attributes.Attr;

/**
 * Tests for the {@link Tag} constants class and its usage.
 */
public class TagTest {

    /**
     * Verifies that the Tag utility class has a private constructor.
     *
     * @throws Exception If reflection fails
     */
    @Test
    public void testPrivateConstructor() throws Exception {
        final Constructor<Tag> constructor = Tag.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        final Tag instance = constructor.newInstance();
        assertNotNull(instance);
    }

    /**
     * Verifies that all Tag constants are non-null and match expected lowercase element names.
     *
     * @throws Exception If reflection fails
     */
    @Test
    public void testTagConstants() throws Exception {
        for (final Field field : Tag.class.getDeclaredFields()) {
            if (Modifier.isPublic(field.getModifiers()) && Modifier.isStatic(field.getModifiers())
                    && field.getType().equals(String.class)) {
                final String value = (String) field.get(null);
                assertThat(value, is(notNullValue()));
                if ("IMAGE".equals(field.getName())) {
                    assertThat(value, is("img"));
                } else {
                    assertThat(value, is(field.getName().toLowerCase(Locale.US)));
                }
            }
        }
    }

    /**
     * Tests standard HTML elements using Tag constants with TagCreator.
     */
    @Test
    public void testTagUsageWithTagCreator() {
        final ContainerTag<?> div = TagCreator.tag(Tag.DIV);
        assertThat(div.render(), is("<div></div>"));

        final EmptyTag<?> input = TagCreator.emptyTag(Tag.INPUT);
        assertThat(input.render(), is("<input>"));

        final ContainerTag<?> paragraph = TagCreator.tag(Tag.P).withText("Hello world");
        assertThat(paragraph.render(), is("<p>Hello world</p>"));

        final ContainerTag<?> anchor = TagCreator.tag(Tag.A).attr(Attr.HREF, "https://example.com");
        assertThat(anchor.render(), is("<a href=\"https://example.com\"></a>"));
    }

    /**
     * Tests newly added event and standard attributes in Attr class.
     */
    @Test
    public void testNewAttrConstants() {
        assertThat(Attr.ONLOAD, is("onload"));
        assertThat(Attr.ONERROR, is("onerror"));
        assertThat(Attr.ONSUBMIT, is("onsubmit"));
        assertThat(Attr.ONCHANGE, is("onchange"));
        assertThat(Attr.ONRESET, is("onreset"));
        assertThat(Attr.ONMOUSEOVER, is("onmouseover"));
    }
}
