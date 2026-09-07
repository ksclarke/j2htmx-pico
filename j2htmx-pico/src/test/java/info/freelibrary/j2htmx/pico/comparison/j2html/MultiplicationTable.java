package info.freelibrary.j2htmx.pico.comparison.j2html;

import info.freelibrary.j2htmx.pico.comparison.ComparisonData;
import info.freelibrary.j2htmx.pico.tags.ContainerTag;
import static info.freelibrary.j2htmx.pico.TagCreator.each;
import static info.freelibrary.j2htmx.pico.TagCreator.table;
import static info.freelibrary.j2htmx.pico.TagCreator.tbody;
import static info.freelibrary.j2htmx.pico.TagCreator.td;
import static info.freelibrary.j2htmx.pico.TagCreator.tr;

public class MultiplicationTable {

    public static ContainerTag<?> tag = table(
        tbody(
            each(ComparisonData.tableNumbers, i -> tr(
                each(ComparisonData.tableNumbers, j -> td(
                    String.valueOf(i * j)
                ))
            ))
        )
    );

}
