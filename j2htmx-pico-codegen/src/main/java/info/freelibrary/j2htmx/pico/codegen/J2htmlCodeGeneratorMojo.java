package info.freelibrary.j2htmx.pico.codegen;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Maven plugin mojo that generates tag classes and attribute interfaces from a model definition file.
 */
@Mojo(name = "generate-source-files", defaultPhase = LifecyclePhase.GENERATE_SOURCES, threadSafe = true)
public class J2htmlCodeGeneratorMojo extends AbstractMojo {

    /** The Maven project instance. */
    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    MavenProject project;

    /** Path to the model definition file. */
    @Parameter(property = "modelFile", required = true)
    String modelFile;

    /** Java package name for generated attribute interfaces. */
    @Parameter(property = "attributePackage", required = true)
    String attributePackage;

    /** Java package name for generated tag classes. */
    @Parameter(property = "tagPackage", required = true)
    String tagPackage;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().debug("Model File: " + modelFile);
        getLog().debug("Attribute Package: " + attributePackage);
        getLog().debug("Tag Package: " + tagPackage);

        final String outputDirectory = project.getBuild().getDirectory() + "/generated-sources/j2html-codegen";
        project.addCompileSourceRoot(outputDirectory);
        getLog().debug("Generating J2Html sources in: " + outputDirectory);

        final String definitions;
        try {
            definitions = new String(Files.readAllBytes(Paths.get(modelFile)));
        } catch (IOException e) {
            throw new MojoFailureException("Unable to locate model file: " + modelFile, e);
        }

        final Model model = new Model();
        try {
            Parser.parse(definitions, model);
        } catch (RuntimeException e) {
            throw new MojoFailureException("Unable to parse model file.", e);
        }

        try {
            Generator.generate(
              Paths.get(outputDirectory).toAbsolutePath(),
              attributePackage,
              tagPackage,
              model
            );
        } catch (IOException e) {
            throw new MojoFailureException("Failed to generate source files.", e);
        }
    }
}
