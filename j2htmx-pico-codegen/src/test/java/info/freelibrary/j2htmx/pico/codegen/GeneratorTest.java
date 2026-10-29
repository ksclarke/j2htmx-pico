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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import info.freelibrary.util.warnings.Checkstyle;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Tests for {@link Generator}.
 */
@SuppressWarnings({Checkstyle.MULTIPLE_STRING_LITERALS, "MultipleStringLiterals"})
public class GeneratorTest {

    /** The temporary output directory for generated code. */
    @TempDir
    Path myOutput;

    /**
     * Tests that generated tags suppress excessive imports when appropriate.
     *
     * @throws IOException If an I/O error occurs
     */
    @Test
    public void generatedTagsSuppressExcessiveImports() throws IOException {
        final Model model = new Model();
        final Path definitions = Path.of("..", "j2htmx-pico", "src", "main", "models", "html.model");

        Parser.parse(Files.readString(definitions), model);
        Generator.generate(myOutput, "test.attributes", "test.tags", model);

        final String source = Files.readString(myOutput.resolve("test/tags/VideoTag.java"));

        assertTrue(source.contains("import info.freelibrary.util.warnings.PMD;"));
        assertTrue(source.contains("@SuppressWarnings(PMD.EXCESSIVE_IMPORTS)"));

        try (var sources = Files.list(myOutput.resolve("test/tags"))) {
            for (final Path tag : sources.toList()) {
                if (!tag.getFileName().toString().equals("VideoTag.java")) {
                    final String otherSource = Files.readString(tag);

                    assertFalse(otherSource.contains("@SuppressWarnings(PMD.EXCESSIVE_IMPORTS)"), tag.toString());
                    assertFalse(otherSource.contains("import info.freelibrary.util.warnings.PMD;"), tag.toString());
                }
            }
        }
    }

    /**
     * Tests that generated attribute interfaces use the expected method prefixes and names.
     *
     * @throws IOException If an I/O error occurs
     */
    @Test
    public void generatedAttributeMethodsUseCorrectPrefixes() throws IOException {
        final Model model = new Model();
        final Path definitions = Path.of("..", "j2htmx-pico", "src", "main", "models", "html.model");

        Parser.parse(Files.readString(definitions), model);
        Generator.generate(myOutput, "test.attributes", "test.tags", model);

        final Path attrDir = myOutput.resolve("test/attributes");

        assertTrue(Files.readString(attrDir.resolve("Controls.java")).contains("default T hasControls()"));
        assertTrue(Files.readString(attrDir.resolve("AutoFocus.java")).contains("default T hasAutoFocus()"));
        assertTrue(Files.readString(attrDir.resolve("AutoPlay.java")).contains("default T hasAutoPlay()"));
        assertTrue(Files.readString(attrDir.resolve("Loop.java")).contains("default T hasLoop()"));
        assertTrue(Files.readString(attrDir.resolve("Download.java")).contains("default T hasDownload()"));
        assertTrue(Files.readString(attrDir.resolve("Sandbox.java")).contains("default T hasSandbox()"));
        assertTrue(Files.readString(attrDir.resolve("AutoComplete.java")).contains("default T hasAutoComplete()"));
        assertTrue(Files.readString(attrDir.resolve("IsMap.java")).contains("default T hasIsMap()"));
        assertTrue(Files.readString(attrDir.resolve("NoValidate.java")).contains("default T hasNoValidate()"));

        final String deferSource = Files.readString(attrDir.resolve("Defer.java"));
        assertTrue(deferSource.contains("default T isDeferred()"));
        assertTrue(deferSource.contains("default T withCondDeferred(final boolean aEnable)"));
        assertTrue(deferSource.contains("default T withCondDefer(final boolean aEnable)"));

        assertTrue(Files.readString(attrDir.resolve("Checked.java")).contains("default T isChecked()"));
        assertTrue(Files.readString(attrDir.resolve("Disabled.java")).contains("default T isDisabled()"));

        final String readOnlySource = Files.readString(attrDir.resolve("ReadOnly.java"));
        assertTrue(readOnlySource.contains("default T isReadOnly()"));
        assertTrue(readOnlySource.contains("default T withCondReadOnly(final boolean aEnable)"));

        final String colSpanSource = Files.readString(attrDir.resolve("ColSpan.java"));
        assertTrue(colSpanSource.contains("default T withColSpan(final String aColSpan)"));
        assertTrue(colSpanSource.contains("default T withCondColSpan(final boolean aEnable, final String aColSpan)"));

        final String onAbortSource = Files.readString(attrDir.resolve("OnAbort.java"));
        assertTrue(onAbortSource.contains("default T withOnAbort(final String anOnAbort)"));

        final String charsetSource = Files.readString(attrDir.resolve("Charset.java"));
        assertTrue(charsetSource.contains("default T withCharset(final String aCharset)"));
        assertTrue(charsetSource.contains("default T withCondCharset(final boolean aEnable, final String aCharset)"));

        final String dateTimeSource = Files.readString(attrDir.resolve("DateTime.java"));
        assertTrue(dateTimeSource.contains("default T withDateTime(final String aDateTime)"));
    }
}
