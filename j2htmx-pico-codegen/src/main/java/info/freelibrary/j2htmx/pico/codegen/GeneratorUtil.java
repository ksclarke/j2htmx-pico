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
     * @param dir The directory whose contents should be deleted
     * @throws IOException If an I/O error occurs
     */
    public static void deleteAllFilesInDir(final Path dir) throws IOException {
        for (final File file : dir.toFile().listFiles()) {
            System.out.println("deleting " + file.toPath());
            Files.delete(file.toPath());
        }
    }
}
