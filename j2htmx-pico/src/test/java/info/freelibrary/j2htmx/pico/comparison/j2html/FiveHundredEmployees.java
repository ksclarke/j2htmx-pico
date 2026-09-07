package info.freelibrary.j2htmx.pico.comparison.j2html;

import info.freelibrary.j2htmx.pico.comparison.ComparisonData;
import info.freelibrary.j2htmx.pico.tags.specialized.UlTag;

import static info.freelibrary.j2htmx.pico.TagCreator.each;
import static info.freelibrary.j2htmx.pico.TagCreator.li;
import static info.freelibrary.j2htmx.pico.TagCreator.ul;
import static java.lang.String.join;
import static java.lang.String.valueOf;

public class FiveHundredEmployees {

    public static UlTag tag = ul(
        each(ComparisonData.fiveHundredEmployees(), employee ->
            li(join(" ", valueOf(employee.getId()), employee.getName(), employee.getTitle()))
        )
    );

}
