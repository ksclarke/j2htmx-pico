/*
 * This code was derived from j2html, which is distributed under the Apache License, Version 2.0.
 * See META-INF/THIRD-PARTY.txt and META-INF/licenses/Apache-2.0.txt for more information.
 *
 * This file has been modified for j2htmx-pico by Kevin S. Clarke (my.j2htmx-pico@kevinclarke.info)
 *
 * The modifications are licensed under the Mozilla Public License, Version 2.0.
 * See LICENSE.txt in the parent project's root for more information.
 */

package info.freelibrary.j2htmx.pico.codegen;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;

import info.freelibrary.j2htmx.pico.codegen.generators.TagCreatorCodeGenerator;
import info.freelibrary.j2htmx.pico.codegen.wattsi.ElementDefinition;
import info.freelibrary.j2htmx.pico.codegen.wattsi.WattsiSource;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Compliance tests for code generator against the WHATWG HTML specification.
 */
public class CodeGeneratorComplianceTests {

    /** The Wattsi specification source. */
    private WattsiSource mySpecification;

    /**
     * Sets up the specification test fixture.
     *
     * @throws IOException If an I/O error occurs
     */
    @BeforeEach
    public void setUp() throws IOException {
        final Path source = Paths.get("src", "test", "resources", "2022-01.wattsi");
        final Document doc = Jsoup.parse(source.toFile(), "UTF-8", "https://html.spec.whatwg.org/");
        mySpecification = new WattsiSource(doc);
    }

    /**
     * Collects all generated element names.
     *
     * @return Set of generated element names
     */
    private Set<String> generatedElements() {
        final Set<String> elements = new HashSet<>();
        elements.addAll(TagCreatorCodeGenerator.emptyTags());
        elements.addAll(TagCreatorCodeGenerator.containerTags());
        return elements;
    }

    /**
     * Collects all specified element names from the specification.
     *
     * @param aSource The Wattsi source
     * @return Set of specified element names
     */
    private Set<String> specifiedElements(final WattsiSource aSource) {
        final Set<String> elements = new HashSet<>();
        for (final ElementDefinition element : aSource.elementDefinitions()) {
            elements.add(element.name());
        }
        return elements;
    }

    /**
     * Tests that all Wattsi elements are defined in the code generator.
     */
    @Test
    @Disabled
    // TODO restore this test once a policy has been determined for obsolete elements.
    public void all_wattsi_elements_are_defined_in_the_code_generator() {
        final Set<String> generated = generatedElements();

        final List<String> undefined = mySpecification.elementDefinitions().stream()
          .filter(element -> !element.isObsolete())
          .filter(element -> !generated.contains(element.name()))
          .map(ElementDefinition::name)
          .collect(toList());

        assertEquals(emptyList(), undefined, "HTML elements are missing");
        // Currently missing (and mostly deprecated):
        //   hgroup
    }

    /**
     * Tests that only Wattsi elements are defined in the code generator.
     */
    @Test
    public void only_wattsi_elements_are_defined_in_the_code_generator() {
        final Set<String> specified = specifiedElements(mySpecification);

        final List<String> invalid = generatedElements().stream()
          .filter(element -> !specified.contains(element))
          .collect(toList());

        assertEquals(emptyList(), invalid, "HTML elements are invalid");
    }

}
