package info.freelibrary.j2htmx.pico.tags;

import static info.freelibrary.j2htmx.pico.TagCreator.fileAsEscapedString;
import static info.freelibrary.j2htmx.pico.TagCreator.fileAsString;
import static info.freelibrary.j2htmx.pico.TagCreator.scriptWithInlineFile_min;
import static info.freelibrary.j2htmx.pico.TagCreator.styleWithInlineFile_min;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class InlineStaticResourceTest {

    private static final String EOL = System.getProperty("line.separator"); // System independent End Of Line

    @Test
    public void testAllTags() throws Exception {

        final String expectedCss = "<style>body{background:#daa520;margin-bottom:10px;margin-left:10px;" +
                                   "margin-right:10px;margin-top:10px}</style>";
        final String expectedJs = "<script>(function(){var test=5;var tast=10;var testTast=test+tast;console.log" +
                                  "(testTast);})();</script>";
        final String expectedHtml = "<body>" + EOL + "    Any content" + EOL + "</body>" + EOL;
        final String expectedEscapedHtml = "&lt;body&gt;" + EOL + "    Any content" + EOL + "&lt;/body&gt;" + EOL;
        final String expectedAnyContent = "public class AnyContent {" + EOL + "}" + EOL;

        // classpath files
        assertThat(styleWithInlineFile_min("/test.css").render(), is(expectedCss));
        assertThat(styleWithInlineFile_min("/test-without-trailing-semis.css").render(), is(expectedCss));
        assertThat(scriptWithInlineFile_min("/test.js").render(), is(expectedJs));
        assertThat(fileAsString("/test.html").render(), is(expectedHtml));
        assertThat(fileAsEscapedString("/test.html").render(), is(expectedEscapedHtml));
        assertThat(fileAsString("/AnyContent.java").render(), is(expectedAnyContent));

        // files outside classpath
        assertThat(styleWithInlineFile_min("src/test/files/test.css").render(), is(expectedCss));
        assertThat(scriptWithInlineFile_min("src/test/files/test.js").render(), is(expectedJs));
        assertThat(fileAsString("src/test/files/test.html").render(), is(expectedHtml));
        assertThat(fileAsEscapedString("src/test/files/test.html").render(), is(expectedEscapedHtml));
        assertThat(fileAsString("src/test/files/AnyContent.java").render(), is(expectedAnyContent));
    }

    @Test
    public void testExceptionForBadPath() throws Exception {
        assertThrows(RuntimeException.class, () -> styleWithInlineFile_min("NOT A FILE"));
    }

}
