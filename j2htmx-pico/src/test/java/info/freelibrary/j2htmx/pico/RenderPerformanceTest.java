package info.freelibrary.j2htmx.pico;

import static info.freelibrary.j2htmx.pico.TagCreator.attrs;
import static info.freelibrary.j2htmx.pico.TagCreator.body;
import static info.freelibrary.j2htmx.pico.TagCreator.div;
import static info.freelibrary.j2htmx.pico.TagCreator.h1;
import static info.freelibrary.j2htmx.pico.TagCreator.h2;
import static info.freelibrary.j2htmx.pico.TagCreator.head;
import static info.freelibrary.j2htmx.pico.TagCreator.html;
import static info.freelibrary.j2htmx.pico.TagCreator.p;
import static info.freelibrary.j2htmx.pico.TagCreator.title;
import static org.junit.jupiter.api.Assertions.assertEquals;

import info.freelibrary.j2htmx.pico.model.BrowserTitle;
import info.freelibrary.j2htmx.pico.model.Button;
import info.freelibrary.j2htmx.pico.model.ButtonModel;
import info.freelibrary.j2htmx.pico.model.PageModel;
import info.freelibrary.j2htmx.pico.model.TextTemplate;
import info.freelibrary.j2htmx.pico.rendering.FlatHtml;
import info.freelibrary.j2htmx.pico.tags.DomContent;
import org.junit.jupiter.api.Test;

public class RenderPerformanceTest {
    final String expected = "<html><head><title>Browsertitle</title></head><body><h1>Hello " +
                            "World!</h1><div><div><div><div><div><div><div><div><div><div><div><div><div><div><div" +
                            "><div><div><div><div><div><div><div><div><div><div><div><p>Hello " +
                            "World!</p></div></div></div></div></div></div></div></div></div></div></div></div></div" +
                            "></div></div></div></div></div></div></div></div></div></div></div></div></div><h2 " +
                            "id=\"title\" class=\"visible-small\">Hello " +
                            "World!</h2><div><div><div><div><div><div><div><div><div><div><div><div><div><div><div" +
                            "><div><div><div><div><div><div><div><div><div><div><div><p>Hello " +
                            "World!</p></div></div></div></div></div></div></div></div></div></div></div></div></div" +
                            "></div></div></div></div></div></div></div></div></div></div></div></div></div><h2>Hello" +
                            " World!</h2><div><div><div><div><div><div><div><div><div><div><div><div><div><div><div" +
                            "><div><div><div><div><div><div><div><div><div><div><div><p>Hello " +
                            "World!</p></div></div></div></div></div></div></div></div></div></div></div></div></div" +
                            "></div></div></div></div></div></div></div></div></div></div></div></div></div><h2 " +
                            "id=\"title\" class=\"visible-small\">Hello World!</h2><div class=\"button\"><div " +
                            "class=\"button-text\">Action!</div></div><div><div><div><div><div><div><div><div><div" +
                            "><div><div><div><div><div><div><div><div><div><div><div><div><div><div><div><div><div><p" +
                            ">Hello World!</p></div></div></div></div></div></div></div></div></div></div></div></div" +
                            "></div></div></div></div></div></div></div></div></div></div></div></div></div></div><h2" +
                            ">Hello World!</h2><div><div><div><div><div><div><div><div><div><div><div><div><div><div" +
                            "><div><div><div><div><div><div><div><div><div><div><div><div><p>Hello " +
                            "World!</p></div></div></div></div></div></div></div></div></div></div></div></div></div" +
                            "></div></div></div></div></div></div></div></div></div></div></div></div></div><h1>Hello" +
                            " World!</h1><div><div><div><div><div><div><div><div><div><div><div><div><div><div><div" +
                            "><div><div><div><div><div><div><div><div><div><div><div><p>Hello " +
                            "World!</p></div></div></div></div></div></div></div></div></div></div></div></div></div" +
                            "></div></div></div></div></div></div></div></div></div></div></div></div></div><h2>Hello" +
                            " World!</h2><div><div><div><div><div><div><div><div><div><div><div><div><div><div><div" +
                            "><div><div><div><div><div><div><div><div><div><div><div><p>Hello " +
                            "World!</p></div></div></div></div></div></div></div></div></div></div></div></div></div" +
                            "></div></div></div></div></div></div></div></div></div></div></div></div></div><h2>Hello" +
                            " World!</h2><div><div><div><div><div><div><div><div><div><div><div><div><div><div><div" +
                            "><div><div><div><div><div><div><div><div><div><div><div><p>Hello " +
                            "World!</p></div></div></div></div></div></div></div></div></div></div></div></div></div" +
                            "></div></div></div></div></div></div></div></div></div></div></div></div></div><h2>Hello" +
                            " World!</h2><div><div><div><div><div><div><div><div><div><div><div><div><div><div><div" +
                            "><div><div><div><div><div><div><div><div><div><div><div><p>Hello " +
                            "World!</p></div></div></div></div></div></div></div></div></div></div></div></div></div" +
                            "></div></div></div></div></div></div></div></div></div></div></div></div></div><h2>Hello" +
                            " World!</h2><div><div><div><div><div><div><div><div><div><div><div><div><div><div><div" +
                            "><div><div><div><div><div><div><div><div><div><div><div><p>Hello " +
                            "World!</p></div></div></div></div></div></div></div></div></div></div></div></div></div" +
                            "></div></div></div></div></div></div></div></div></div></div></div></div></div></body" +
                            "></html>";
    private final DomContent template;

    public RenderPerformanceTest() {
        this.template =
          // @formatter:off
                html(
                     head(
                          title(new BrowserTitle())
                     ),
                     body(
                          h1(new TextTemplate()),
                          div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(p(new TextTemplate()))))))))))))))))))))))))))),
                          h2(attrs("#title.visible-small"),new TextTemplate()),
                          div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(p(new TextTemplate()))))))))))))))))))))))))))),
                          h2(new TextTemplate()),
                          div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(p(new TextTemplate()))))))))))))))))))))))))))),
                          h2(attrs("#title.visible-small"),new TextTemplate()),
                          new Button(),
                          div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(p(new TextTemplate()))))))))))))))))))))))))))),
                          h2(new TextTemplate()),
                          div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(p(new TextTemplate()))))))))))))))))))))))))))),
                          h1(new TextTemplate()),
                          div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(p(new TextTemplate()))))))))))))))))))))))))))),
                          h2(new TextTemplate()),
                          div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(p(new TextTemplate()))))))))))))))))))))))))))),
                          h2(new TextTemplate()),
                          div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(p(new TextTemplate()))))))))))))))))))))))))))),
                          h2(new TextTemplate()),
                          div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(p(new TextTemplate()))))))))))))))))))))))))))),
                          h2(new TextTemplate()),
                          div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(p(new TextTemplate())))))))))))))))))))))))))))
                     )
                );
        // @formatter:on
    }

    private DomContent getDomContent(final PageModel pageModel) throws Exception {
        return
          // @formatter:off
        html(
             head(
                  title(pageModel.getTitle())
             ),
             body(
               h1(pageModel.getText()),
               div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(p(pageModel.getText()))))))))))))))))))))))))))),
               h2(attrs("#title.visible-small"), pageModel.getText()),
               div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(p(pageModel.getText()))))))))))))))))))))))))))),
               h2(pageModel.getText()),
               div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(p(pageModel.getText()))))))))))))))))))))))))))),
               h2(attrs("#title.visible-small"),pageModel.getText()),
               div()
               .withClass("button")
               .with(
                       div()
                       .withClass("button-text")
                       .withText(pageModel.getButtonModel().getText())
               ),
               div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(p(pageModel.getText()))))))))))))))))))))))))))),
               h2(pageModel.getText()),
               div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(p(pageModel.getText()))))))))))))))))))))))))))),
               h1(pageModel.getText()),
               div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(p(pageModel.getText()))))))))))))))))))))))))))),
               h2(pageModel.getText()),
               div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(p(pageModel.getText()))))))))))))))))))))))))))),
               h2(pageModel.getText()),
               div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(p(pageModel.getText()))))))))))))))))))))))))))),
               h2(pageModel.getText()),
               div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(p(pageModel.getText()))))))))))))))))))))))))))),
               h2(pageModel.getText()),
               div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(div(p(pageModel.getText())))))))))))))))))))))))))))
             )
        );

        // @formatter:on
    }

    @Test
    public void templatePerfomanceTest() throws Exception {
        final PageModel pageModel = new PageModel("Browsertitle", "Hello World!", new ButtonModel("Action!"));
        final StringBuilder stringBuilder = new StringBuilder();
        template.render(FlatHtml.into(stringBuilder), pageModel);
        assertEquals(expected, stringBuilder.toString());
    }

    @Test
    public void staticPerfomanceTest() throws Exception {
        final PageModel pageModel = new PageModel("Browsertitle", "Hello World!", new ButtonModel("Action!"));
        final String result = getDomContent(pageModel).render();
        assertEquals(expected, result);
    }
}
