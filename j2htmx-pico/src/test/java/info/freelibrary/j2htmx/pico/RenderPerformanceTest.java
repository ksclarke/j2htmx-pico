/*
 * This code was derived from j2html, which is distributed under the Apache License, Version 2.0.
 * See META-INF/THIRD-PARTY.txt and META-INF/licenses/Apache-2.0.txt for more information.
 *
 * This file has been modified for j2htmx-pico by Kevin S. Clarke (my.j2htmx-pico@kevinclarke.info)
 *
 * The modifications are licensed under the Mozilla Public License, Version 2.0.
 * See LICENSE.txt in the parent project's root for more information.
 */

package info.freelibrary.j2htmx.pico;

import static info.freelibrary.j2htmx.pico.TagCreator.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import info.freelibrary.j2htmx.pico.model.BrowserTitle;
import info.freelibrary.j2htmx.pico.model.Button;
import info.freelibrary.j2htmx.pico.model.ButtonModel;
import info.freelibrary.j2htmx.pico.model.PageModel;
import info.freelibrary.j2htmx.pico.model.TextTemplate;
import info.freelibrary.j2htmx.pico.rendering.FlatHtml;
import info.freelibrary.j2htmx.pico.tags.AbstractDomContent;
import info.freelibrary.util.warnings.Checkstyle;
import org.junit.jupiter.api.Test;

/**
 * Performance tests for template and DOM rendering.
 */
@SuppressWarnings({Checkstyle.MULTIPLE_STRING_LITERALS, "MultipleStringLiterals"})
public class RenderPerformanceTest {

    /** The expected HTML output string. */
    final String myExpected = "<html><head><title>Browsertitle</title></head><body><h1>Hello " +
                              "World!</h1><div><div><div><div><div><div><div><div><div><div><div><div><div><div><div" +
                              "><div><div><div><div><div><div><div><div><div><div><div><p>Hello " +
                              "World!</p></div></div></div></div></div></div></div></div></div></div></div></div" +
                              "></div" +
                              "></div></div></div></div></div></div></div></div></div></div></div></div></div><h2 " +
                              "id=\"title\" class=\"visible-small\">Hello " +
                              "World!</h2><div><div><div><div><div><div><div><div><div><div><div><div><div><div><div" +
                              "><div><div><div><div><div><div><div><div><div><div><div><p>Hello " +
                              "World!</p></div></div></div></div></div></div></div></div></div></div></div></div" +
                              "></div" +
                              "></div></div></div></div></div></div></div></div></div></div></div></div></div><h2" +
                              ">Hello" +
                              " World!</h2><div><div><div><div><div><div><div><div><div><div><div><div><div><div><div" +
                              "><div><div><div><div><div><div><div><div><div><div><div><p>Hello " +
                              "World!</p></div></div></div></div></div></div></div></div></div></div></div></div" +
                              "></div" +
                              "></div></div></div></div></div></div></div></div></div></div></div></div></div><h2 " +
                              "id=\"title\" class=\"visible-small\">Hello World!</h2><div class=\"button\"><div " +
                              "class=\"button-text\">Action!</div></div><div><div><div><div><div><div><div><div><div" +
                              "><div><div><div><div><div><div><div><div><div><div><div><div><div><div><div><div><div" +
                              "><p" +
                              ">Hello World!</p></div></div></div></div></div></div></div></div></div></div></div" +
                              "></div" +
                              "></div></div></div></div></div></div></div></div></div></div></div></div></div></div" +
                              "><h2" +
                              ">Hello World!</h2><div><div><div><div><div><div><div><div><div><div><div><div><div" +
                              "><div" + "><div><div><div><div><div><div><div><div><div><div><div><div><p>Hello " +
                              "World!</p></div></div></div></div></div></div></div></div></div></div></div></div" +
                              "></div" +
                              "></div></div></div></div></div></div></div></div></div></div></div></div></div><h1" +
                              ">Hello" +
                              " World!</h1><div><div><div><div><div><div><div><div><div><div><div><div><div><div><div" +
                              "><div><div><div><div><div><div><div><div><div><div><div><p>Hello " +
                              "World!</p></div></div></div></div></div></div></div></div></div></div></div></div" +
                              "></div" +
                              "></div></div></div></div></div></div></div></div></div></div></div></div></div><h2" +
                              ">Hello" +
                              " World!</h2><div><div><div><div><div><div><div><div><div><div><div><div><div><div><div" +
                              "><div><div><div><div><div><div><div><div><div><div><div><p>Hello " +
                              "World!</p></div></div></div></div></div></div></div></div></div></div></div></div" +
                              "></div" +
                              "></div></div></div></div></div></div></div></div></div></div></div></div></div><h2" +
                              ">Hello" +
                              " World!</h2><div><div><div><div><div><div><div><div><div><div><div><div><div><div><div" +
                              "><div><div><div><div><div><div><div><div><div><div><div><p>Hello " +
                              "World!</p></div></div></div></div></div></div></div></div></div></div></div></div" +
                              "></div" +
                              "></div></div></div></div></div></div></div></div></div></div></div></div></div><h2" +
                              ">Hello" +
                              " World!</h2><div><div><div><div><div><div><div><div><div><div><div><div><div><div><div" +
                              "><div><div><div><div><div><div><div><div><div><div><div><p>Hello " +
                              "World!</p></div></div></div></div></div></div></div></div></div></div></div></div" +
                              "></div" +
                              "></div></div></div></div></div></div></div></div></div></div></div></div></div><h2" +
                              ">Hello" +
                              " World!</h2><div><div><div><div><div><div><div><div><div><div><div><div><div><div><div" +
                              "><div><div><div><div><div><div><div><div><div><div><div><p>Hello " +
                              "World!</p></div></div></div></div></div></div></div></div></div></div></div></div" +
                              "></div" +
                              "></div></div></div></div></div></div></div></div></div></div></div></div></div></body" +
                              "></html>";
    /** The template DOM content for rendering tests. */
    private final AbstractDomContent myTemplate;

    /**
     * Constructs a RenderPerformanceTest instance.
     */
    public RenderPerformanceTest() {
        this.myTemplate =
          // @formatter:off
                html(
                     head(
                          title(new BrowserTitle())
                     ),
                     body(
                          h1(new TextTemplate()),
                          div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(
                            div(div(div(p(new TextTemplate()))))))))))))))))))))))))))),
                          h2(attrs("#title.visible-small"),new TextTemplate()),
                          div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(
                            div(div(div(p(new TextTemplate()))))))))))))))))))))))))))),
                          h2(new TextTemplate()),
                          div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(
                            div(div(div(p(new TextTemplate()))))))))))))))))))))))))))),
                          h2(attrs("#title.visible-small"),new TextTemplate()),
                          new Button(),
                          div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(
                            div(div(div(p(new TextTemplate()))))))))))))))))))))))))))),
                          h2(new TextTemplate()),
                          div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(
                            div(div(div(p(new TextTemplate()))))))))))))))))))))))))))),
                          h1(new TextTemplate()),
                          div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(
                            div(div(div(p(new TextTemplate()))))))))))))))))))))))))))),
                          h2(new TextTemplate()),
                          div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(
                            div(div(div(p(new TextTemplate()))))))))))))))))))))))))))),
                          h2(new TextTemplate()),
                          div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(
                            div(div(div(p(new TextTemplate()))))))))))))))))))))))))))),
                          h2(new TextTemplate()),
                          div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(
                            div(div(div(p(new TextTemplate()))))))))))))))))))))))))))),
                          h2(new TextTemplate()),
                          div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(
                            div(div(div(p(new TextTemplate())))))))))))))))))))))))))))
                     )
                );
        // @formatter:on
    }

    /**
     * Builds DOM content for the page model.
     *
     * @param aPageModel The page model
     * @return The DOM content
     * @throws Exception If an error occurs
     */
    private AbstractDomContent getDomContent(final PageModel aPageModel) throws Exception {
        return
          // @formatter:off
        html(
             head(
                  title(aPageModel.getTitle())
             ),
             body(
               h1(aPageModel.getText()),
               div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(
                 p(aPageModel.getText()))))))))))))))))))))))))))),
               h2(attrs("#title.visible-small"), aPageModel.getText()),
               div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(
                 p(aPageModel.getText()))))))))))))))))))))))))))),
               h2(aPageModel.getText()),
               div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(
                 p(aPageModel.getText()))))))))))))))))))))))))))),
               h2(attrs("#title.visible-small"),aPageModel.getText()),
               div()
               .withClass("button")
               .with(
                       div()
                       .withClass("button-text")
                       .withText(aPageModel.getButtonModel().getText())
               ),
               div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(
                 p(aPageModel.getText()))))))))))))))))))))))))))),
               h2(aPageModel.getText()),
               div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(
                 p(aPageModel.getText()))))))))))))))))))))))))))),
               h1(aPageModel.getText()),
               div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(
                 p(aPageModel.getText()))))))))))))))))))))))))))),
               h2(aPageModel.getText()),
               div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(
                 p(aPageModel.getText()))))))))))))))))))))))))))),
               h2(aPageModel.getText()),
               div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(
                 p(aPageModel.getText()))))))))))))))))))))))))))),
               h2(aPageModel.getText()),
               div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(
                 p(aPageModel.getText()))))))))))))))))))))))))))),
               h2(aPageModel.getText()),
               div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(
                 p(aPageModel.getText())))))))))))))))))))))))))))
             )
        );

        // @formatter:on
    }

    /**
     * Tests template rendering performance.
     *
     * @throws Exception If an error occurs
     */
    @Test
    public void templatePerfomanceTest() throws Exception {
        final PageModel pageModel = new PageModel("Browsertitle", "Hello World!", new ButtonModel("Action!"));
        final StringBuilder stringBuilder = new StringBuilder();
        myTemplate.render(FlatHtml.into(stringBuilder), pageModel);
        assertEquals(myExpected, stringBuilder.toString());
    }

    /**
     * Tests static DOM rendering performance.
     *
     * @throws Exception If an error occurs
     */
    @Test
    public void staticPerfomanceTest() throws Exception {
        final PageModel pageModel = new PageModel("Browsertitle", "Hello World!", new ButtonModel("Action!"));
        final String result = getDomContent(pageModel).render();
        assertEquals(myExpected, result);
    }
}
