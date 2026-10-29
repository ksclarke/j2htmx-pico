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
    MavenProject myProject;

    /** Path to the model definition file. */
    @Parameter(alias = "modelFile", property = "modelFile", required = true)
    String myModelFile;

    /** Java package name for generated attribute interfaces. */
    @Parameter(alias = "attributePackage", property = "attributePackage", required = true)
    String myAttributePackage;

    /** Java package name for generated tag classes. */
    @Parameter(alias = "tagPackage", property = "tagPackage", required = true)
    String myTagPackage;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().debug("Model File: " + myModelFile);
        getLog().debug("Attribute Package: " + myAttributePackage);
        getLog().debug("Tag Package: " + myTagPackage);

        final String outputDirectory = myProject.getBuild().getDirectory() + "/generated-sources/j2html-codegen";
        myProject.addCompileSourceRoot(outputDirectory);
        getLog().debug("Generating J2Html sources in: " + outputDirectory);

        final String definitions;
        try {
            definitions = Files.readString(Paths.get(myModelFile));
        } catch (IOException e) {
            throw new MojoFailureException("Unable to locate model file: " + myModelFile, e);
        }

        final Model model = new Model();
        try {
            Parser.parse(definitions, model);
        } catch (@SuppressWarnings(PMD.AVOID_CATCHING_GENERIC_EXCEPTION) RuntimeException details) {
            throw new MojoFailureException("Unable to parse model file.", details);
        }

        try {
            Generator.generate(Paths.get(outputDirectory).toAbsolutePath(), myAttributePackage, myTagPackage, model);
        } catch (IOException details) {
            throw new MojoFailureException("Failed to generate source files.", details);
        }
    }
}
