package info.freelibrary.j2htmx.pico.tags;

import info.freelibrary.j2htmx.pico.attributes.Attr;
import org.junit.jupiter.api.Test;
import static info.freelibrary.j2htmx.pico.TagCreator.a;
import static info.freelibrary.j2htmx.pico.TagCreator.body;
import static info.freelibrary.j2htmx.pico.TagCreator.button;
import static info.freelibrary.j2htmx.pico.TagCreator.div;
import static info.freelibrary.j2htmx.pico.TagCreator.document;
import static info.freelibrary.j2htmx.pico.TagCreator.footer;
import static info.freelibrary.j2htmx.pico.TagCreator.h1;
import static info.freelibrary.j2htmx.pico.TagCreator.h2;
import static info.freelibrary.j2htmx.pico.TagCreator.head;
import static info.freelibrary.j2htmx.pico.TagCreator.header;
import static info.freelibrary.j2htmx.pico.TagCreator.html;
import static info.freelibrary.j2htmx.pico.TagCreator.iff;
import static info.freelibrary.j2htmx.pico.TagCreator.input;
import static info.freelibrary.j2htmx.pico.TagCreator.main;
import static info.freelibrary.j2htmx.pico.TagCreator.script;
import static info.freelibrary.j2htmx.pico.TagCreator.text;
import static info.freelibrary.j2htmx.pico.TagCreator.title;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ComplexRenderTest {

    private String renderTest() {
        final boolean USER_SHOULD_LOG_IN = true;
        final boolean USER_SHOULD_SIGN_UP = false;
        return document().render() +
            html(
                head(
                    title().withText("Test")
                ),
                body(
                    header().with(
                        h1().with(
                            text("Test Header "),
                            a("with link").withHref("http://example.com"),
                            text(".")
                        )
                    ),
                    main().with(
                        h2("Test Form"),
                        div().with(
                            input().attr("type","email").withName("email").withPlaceholder("Email"),
                            input().attr("type","password").withName("password").withPlaceholder("Password")
                        ).condWith(USER_SHOULD_LOG_IN, button().attr("type","submit").withText("Login")
                        ).condWith(USER_SHOULD_SIGN_UP, button().attr("type","submit").withText("Signup"))
                    ),
                    footer().attr(Attr.CLASS, "footer").condAttr(1 == 1, Attr.ID, "id").withText("Test Footer"),
                    script().withSrc("/testScript.js")
                )
            ).render();
    }

    private String renderTest2() {
        final boolean USER_SHOULD_LOG_IN = true;
        final boolean USER_SHOULD_SIGN_UP = false;
        return document().render() +
            html(
                head(
                    title("Test")
                ),
                body(
                    header(
                        h1(
                            text("Test Header "),
                            a("with link").withHref("http://example.com"),
                            text(".")
                        )
                    ),
                    main(
                        h2("Test Form"),
                        div(
                            input().attr("type","email").withName("email").withPlaceholder("Email"),
                            input().attr("type","password").withName("password").withPlaceholder("Password"),
                            iff(USER_SHOULD_LOG_IN, button().attr("type","submit").withText("Login")),
                            iff(USER_SHOULD_SIGN_UP, button().attr("type","submit").withText("Signup"))
                        )
                    ),
                    footer("Test Footer").attr(Attr.CLASS, "footer").condAttr(1 == 1, Attr.ID, "id"),
                    script().withSrc("/testScript.js")
                )
            ).render();
    }

    private String renderTest3() {
        final boolean USER_SHOULD_LOG_IN = true;
        final boolean USER_SHOULD_SIGN_UP = false;
        return document().render() + "\n" +
            html(
                head(
                    title("Test")
                ),
                body(
                    header(
                        h1(
                            text("Test Header "),
                            a("with link").withHref("http://example.com"),
                            text(".")
                        )
                    ),
                    main(
                        h2("Test Form"),
                        div(
                            input().attr("type","email").withName("email").withPlaceholder("Email"),
                            input().attr("type","password").withName("password").withPlaceholder("Password"),
                            iff(USER_SHOULD_LOG_IN, button().attr("type","submit").withText("Login")),
                            iff(USER_SHOULD_SIGN_UP, button().attr("type","submit").withText("Signup"))
                        )
                    ),
                    footer("Test Footer").attr(Attr.CLASS, "footer").condAttr(1 == 1, Attr.ID, "id"),
                    script().withSrc("/testScript.js")
                )
            ).renderFormatted();
    }

    @Test
    public void testComplexRender() {
        final String expectedResult = "<!DOCTYPE html><html><head><title>Test</title></head><body><header><h1>Test Header <a href=\"http://example.com\">with link</a>.</h1></header><main><h2>Test Form</h2><div><input type=\"email\" name=\"email\" placeholder=\"Email\"><input type=\"password\" name=\"password\" placeholder=\"Password\"><button type=\"submit\">Login</button></div></main><footer class=\"footer\" id=\"id\">Test Footer</footer><script src=\"/testScript.js\"></script></body></html>";
        assertThat(renderTest(), is(expectedResult));
        assertThat(renderTest2(), is(expectedResult));
    }

    @Test
    public void testComplexRender_formatted() {
        assertThat(renderTest3(),
            is("<!DOCTYPE html>\n"
                + "<html>\n"
                + "    <head>\n"
                + "        <title>\n"
                + "            Test\n"
                + "        </title>\n"
                + "    </head>\n"
                + "    <body>\n"
                + "        <header>\n"
                + "            <h1>\n"
                + "                Test Header \n"
                + "                <a href=\"http://example.com\">\n"
                + "                    with link\n"
                + "                </a>\n"
                + "                .\n"
                + "            </h1>\n"
                + "        </header>\n"
                + "        <main>\n"
                + "            <h2>\n"
                + "                Test Form\n"
                + "            </h2>\n"
                + "            <div>\n"
                + "                <input type=\"email\" name=\"email\" placeholder=\"Email\">\n"
                + "                <input type=\"password\" name=\"password\" placeholder=\"Password\">\n"
                + "                <button type=\"submit\">\n"
                + "                    Login\n"
                + "                </button>\n"
                + "            </div>\n"
                + "        </main>\n"
                + "        <footer class=\"footer\" id=\"id\">\n"
                + "            Test Footer\n"
                + "        </footer>\n"
                + "        <script src=\"/testScript.js\">\n"
                + "        </script>\n"
                + "    </body>\n"
                + "</html>\n"));
    }
}
