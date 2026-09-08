package info.freelibrary.j2htmx.pico.codegen;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Utilities for code generation file management.
 */
public final class GeneratorUtil {

    private GeneratorUtil() {
    }

    /**
     * Deletes all files in the specified directory.
     *
     * @param dir The directory whose contents should be deleted
     * @throws IOException If an I/O error occurs
     */
    public static final void deleteAllFilesInDir(final Path dir) throws IOException {
        for(final File file : dir.toFile().listFiles()){
            System.out.println("deleting " + file.toPath());
            Files.delete(file.toPath());
        }
    }
}
