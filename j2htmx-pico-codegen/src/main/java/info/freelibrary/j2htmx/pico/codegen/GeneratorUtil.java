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

import info.freelibrary.util.warnings.PMD;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Utilities for code generation file management.
 */
@SuppressWarnings(PMD.SYSTEM_PRINTLN)
public final class GeneratorUtil {

    /**
     * Private constructor to prevent instantiation.
     */
    private GeneratorUtil() {
        // This is intentionally left empty
    }

    /**
     * Deletes all files in the specified directory.
     *
     * @param aDir The directory whose contents should be deleted
     * @throws IOException If an I/O error occurs
     */
    public static void deleteAllFilesInDir(final Path aDir) throws IOException {
        for (final File file : aDir.toFile().listFiles()) {
            System.out.println("deleting " + file.toPath());
            Files.delete(file.toPath());
        }
    }
}
