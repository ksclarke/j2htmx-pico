/*
 * This code was derived from j2html, which is distributed under the Apache License, Version 2.0.
 * See META-INF/THIRD-PARTY.txt and META-INF/licenses/Apache-2.0.txt for more information.
 *
 * This file has been modified for j2htmx-pico by Kevin S. Clarke (my.j2htmx-pico@kevinclarke.info)
 *
 * The modifications are licensed under the Mozilla Public License, Version 2.0.
 * See LICENSE.txt in the parent project's root for more information.
 */

package info.freelibrary.j2htmx.pico.mathml;

import static info.freelibrary.j2htmx.pico.mathml.MathML.annotation;
import static info.freelibrary.j2htmx.pico.mathml.MathML.maction;
import static info.freelibrary.j2htmx.pico.mathml.MathML.math;
import static info.freelibrary.j2htmx.pico.mathml.MathML.merror;
import static info.freelibrary.j2htmx.pico.mathml.MathML.mfrac;
import static info.freelibrary.j2htmx.pico.mathml.MathML.mi;
import static info.freelibrary.j2htmx.pico.mathml.MathML.mmultiscripts;
import static info.freelibrary.j2htmx.pico.mathml.MathML.mn;
import static info.freelibrary.j2htmx.pico.mathml.MathML.mo;
import static info.freelibrary.j2htmx.pico.mathml.MathML.mover;
import static info.freelibrary.j2htmx.pico.mathml.MathML.mpadded;
import static info.freelibrary.j2htmx.pico.mathml.MathML.mphantom;
import static info.freelibrary.j2htmx.pico.mathml.MathML.mprescripts;
import static info.freelibrary.j2htmx.pico.mathml.MathML.mroot;
import static info.freelibrary.j2htmx.pico.mathml.MathML.mrow;
import static info.freelibrary.j2htmx.pico.mathml.MathML.ms;
import static info.freelibrary.j2htmx.pico.mathml.MathML.mspace;
import static info.freelibrary.j2htmx.pico.mathml.MathML.msqrt;
import static info.freelibrary.j2htmx.pico.mathml.MathML.mstyle;
import static info.freelibrary.j2htmx.pico.mathml.MathML.msub;
import static info.freelibrary.j2htmx.pico.mathml.MathML.msubsup;
import static info.freelibrary.j2htmx.pico.mathml.MathML.msup;
import static info.freelibrary.j2htmx.pico.mathml.MathML.mtable;
import static info.freelibrary.j2htmx.pico.mathml.MathML.mtd;
import static info.freelibrary.j2htmx.pico.mathml.MathML.mtext;
import static info.freelibrary.j2htmx.pico.mathml.MathML.mtr;
import static info.freelibrary.j2htmx.pico.mathml.MathML.munder;
import static info.freelibrary.j2htmx.pico.mathml.MathML.munderover;
import static info.freelibrary.j2htmx.pico.mathml.MathML.none;
import static info.freelibrary.j2htmx.pico.mathml.MathML.semantics;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link MathML}.
 */
public class MathMLTests {

    /**
     * Tests that all MathML tags render as containers.
     */
    @Test
    public void all_tags_render_containers() {
        assertEquals("<annotation></annotation>", annotation().render());
        assertEquals("<maction></maction>", maction().render());
        assertEquals("<math></math>", math().render());
        assertEquals("<merror></merror>", merror().render());
        assertEquals("<mfrac></mfrac>", mfrac().render());
        assertEquals("<mi></mi>", mi().render());
        assertEquals("<mmultiscripts></mmultiscripts>", mmultiscripts().render());
        assertEquals("<mn></mn>", mn().render());
        assertEquals("<mo></mo>", mo().render());
        assertEquals("<mover></mover>", mover().render());
        assertEquals("<mpadded></mpadded>", mpadded().render());
        assertEquals("<mphantom></mphantom>", mphantom().render());
        assertEquals("<mprescripts></mprescripts>", mprescripts().render());
        assertEquals("<mroot></mroot>", mroot().render());
        assertEquals("<mrow></mrow>", mrow().render());
        assertEquals("<ms></ms>", ms().render());
        assertEquals("<mspace></mspace>", mspace().render());
        assertEquals("<msqrt></msqrt>", msqrt().render());
        assertEquals("<mstyle></mstyle>", mstyle().render());
        assertEquals("<msub></msub>", msub().render());
        assertEquals("<msubsup></msubsup>", msubsup().render());
        assertEquals("<msup></msup>", msup().render());
        assertEquals("<mtable></mtable>", mtable().render());
        assertEquals("<mtd></mtd>", mtd().render());
        assertEquals("<mtext></mtext>", mtext().render());
        assertEquals("<mtr></mtr>", mtr().render());
        assertEquals("<munder></munder>", munder().render());
        assertEquals("<munderover></munderover>", munderover().render());
        assertEquals("<none></none>", none().render());
        assertEquals("<semantics></semantics>", semantics().render());
    }

    /**
     * Tests that MathML boolean attributes use has* methods and render properly.
     */
    @Test
    public void boolean_attributes_render() {
        assertEquals("<math displaystyle></math>", math().hasDisplayStyle().render());
        assertEquals("<math nonce></math>", math().hasNonce().render());
        assertEquals("<math scriptlevel></math>", math().hasScriptLevel().render());
        assertEquals("<mstyle mathbackground></mstyle>", mstyle().hasMathBackground().render());
        assertEquals("<mstyle mathcolor></mstyle>", mstyle().hasMathColor().render());
        assertEquals("<mstyle mathsize></mstyle>", mstyle().hasMathSize().render());
        assertEquals("<mtext mathvariant></mtext>", mtext().hasMathVariant().render());
    }

}
