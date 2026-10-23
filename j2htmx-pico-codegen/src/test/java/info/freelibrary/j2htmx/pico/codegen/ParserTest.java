package info.freelibrary.j2htmx.pico.codegen;

import static info.freelibrary.util.Constants.*;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import java.util.function.Consumer;

/**
 * Tests for {@link Parser}.
 */
public class ParserTest {

    /**
     * Verifies parsing events against the provided listener checks.
     *
     * @param aTxt The input text to parse
     * @param aChecks Consumer containing listener verification checks
     */
    private void verifyParsing(final String aTxt, final Consumer<Parser.Listener> aChecks) {
        final Parser.Listener listener = mock(Parser.Listener.class);

        Parser.parse(aTxt, listener);
        aChecks.accept(listener);
    }

    /**
     * Tests that empty input produces no events.
     */
    @Test
    public void an_empty_input_has_no_events() {
        verifyParsing(EMPTY, Mockito::verifyNoInteractions);
    }

    /**
     * Tests that whitespace input produces no events.
     */
    @Test
    public void whitespace_has_no_events() {
        verifyParsing("    \t\t\t\t", Mockito::verifyNoInteractions);
    }

    /**
     * Tests that commented lines are signaled.
     */
    @Test
    public void commented_lines_are_signaled() {
        verifyParsing("#Comment 1.\n# Comment B?", listener -> {
            final InOrder order = inOrder(listener);

            order.verify(listener).lineCommented(1, "#Comment 1.");
            order.verify(listener).lineCommented(2, "# Comment B?");
        });
    }

    /**
     * Tests that node definitions are signaled.
     */
    @Test
    public void node_definitions_are_signaled() {
        verifyParsing("ELEMENT[a]\nEMPTY-ELEMENT[b]\nBOOLEAN[c]\nONOFF[d]\nSTRING[e]", listener -> {
            final InOrder order = inOrder(listener);
            order.verify(listener).elementDefined(1, "a");
            order.verify(listener).emptyElementDefined(2, "b");
            order.verify(listener).booleanDefined(3, "c");
            order.verify(listener).onOffDefined(4, "d");
            order.verify(listener).stringDefined(5, "e");
        });
    }

    /**
     * Tests that attribute definitions are signaled.
     */
    @Test
    public void attribute_definitions_are_signaled() {
        verifyParsing("ATTRIBUTE[f:g]", listener -> {
            final InOrder order = inOrder(listener);
            order.verify(listener).attributeDefined(1, "f", "g");
        });
    }

    /**
     * Tests that invalid lines are signaled.
     */
    @Test
    public void invalid_lines_are_signaled() {
        verifyParsing("lol, I dunno!\nIt Broke...", listener -> {
            final InOrder order = inOrder(listener);
            order.verify(listener).invalidLine(1, "lol, I dunno!");
            order.verify(listener).invalidLine(2, "It Broke...");
        });
    }
}
