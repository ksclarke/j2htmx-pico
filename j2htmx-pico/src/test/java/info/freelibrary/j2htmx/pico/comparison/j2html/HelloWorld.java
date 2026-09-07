package info.freelibrary.j2htmx.pico.comparison.j2html;

import info.freelibrary.j2htmx.pico.tags.specialized.HtmlTag;

import static info.freelibrary.j2htmx.pico.TagCreator.attrs;
import static info.freelibrary.j2htmx.pico.TagCreator.body;
import static info.freelibrary.j2htmx.pico.TagCreator.h1;
import static info.freelibrary.j2htmx.pico.TagCreator.head;
import static info.freelibrary.j2htmx.pico.TagCreator.html;
import static info.freelibrary.j2htmx.pico.TagCreator.link;
import static info.freelibrary.j2htmx.pico.TagCreator.main;
import static info.freelibrary.j2htmx.pico.TagCreator.title;

public class HelloWorld {

    public static HtmlTag tag = html(
        head(
            title("Title"),
            link().withRel("stylesheet").withHref("/css/main.css")
        ),
        body(
            main(attrs("#main.content"),
                h1("Heading!")
            )
        )
    );

}
