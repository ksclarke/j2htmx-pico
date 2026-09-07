package info.freelibrary.j2htmx.pico.comparison.j2html;

import info.freelibrary.j2htmx.pico.tags.ContainerTag;
import info.freelibrary.j2htmx.pico.tags.DomContent;
import info.freelibrary.j2htmx.pico.tags.specialized.HtmlTag;

import static info.freelibrary.j2htmx.pico.TagCreator.attrs;
import static info.freelibrary.j2htmx.pico.TagCreator.body;
import static info.freelibrary.j2htmx.pico.TagCreator.div;
import static info.freelibrary.j2htmx.pico.TagCreator.h1;
import static info.freelibrary.j2htmx.pico.TagCreator.head;
import static info.freelibrary.j2htmx.pico.TagCreator.html;
import static info.freelibrary.j2htmx.pico.TagCreator.link;
import static info.freelibrary.j2htmx.pico.TagCreator.main;
import static info.freelibrary.j2htmx.pico.TagCreator.title;

public class Macros {

    public static HtmlTag tag = mainLayout(
        div(
            h1("Example content"),
            someMacro(1),
            someMacro(2),
            someMacro(3)
        )
    );

    private static HtmlTag mainLayout(DomContent content) {
        return html(
            head(
                title("Title"),
                link().withRel("stylesheet").withHref("/css/main.css")
            ),
            body(
                main(attrs("#main.content"),
                    content
                )
            )
        );
    }

    private static ContainerTag<?> someMacro(int i) {
        return div(
            "Macro call " + i
        );
    }

}
