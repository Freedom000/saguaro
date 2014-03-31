package com.willowtreeapps.saguaro.maven;

import com.google.common.collect.ImmutableMap;
import com.willowtreeapps.saguaro.maven.util.ProjectHelper;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Plugin;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectBuilder;
import org.codehaus.plexus.util.xml.Xpp3Dom;

import java.io.File;
import java.util.*;


/**
 * User: evantatarka
 * Date: 3/31/14
 * Time: 11:00 AM
 */
@Mojo(name = "generate", requiresDependencyResolution = ResolutionScope.COMPILE)
public class SaguaroMojo extends AbstractMojo {
    @Parameter
    private List<Dependency> ignore = new ArrayList<Dependency>();

    @Parameter
    private List<LicenseEntry> licenses = new ArrayList<LicenseEntry>();

    @Parameter
    private List<Alias> aliases = new ArrayList<Alias>();

    @Parameter(defaultValue = "saguaro_plugin_config")
    private String resourceName;

    @Parameter
    private boolean includeDependencies = true;



    @Parameter(defaultValue = "${project}", readonly = true)
    private MavenProject project;

    @Component
    private MavenProjectBuilder projectBuilder;

    @Parameter(defaultValue = "${localRepository}", readonly = true)
    private ArtifactRepository localRepository;

    @Parameter(defaultValue = "${project.remoteArtifactRepositories}", readonly = true)
    private List<ArtifactRepository> remoteRepositories;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        String packaging = project.getPackaging();

        // Only process android projects
        if (!(packaging.equals("aar") || packaging.equals("apk") || packaging.equals("apk-lib"))) {
            return;
        }

        File outputDir = new File(project.getBasedir(), "res");

        List<Plugin> plugins =  project.getBuildPlugins();
        for (Plugin plugin : plugins) {
            if (plugin.getGroupId().equals("com.jayway.maven.plugins.android.generation2") && plugin.getArtifactId().equals("android-maven-plugin")) {
                Xpp3Dom dom = (Xpp3Dom) plugin.getConfiguration();
                Xpp3Dom resDir = dom.getChild("resourceDirectory");
                if (resDir != null) {
                    outputDir = new File(resDir.getValue());
                }
            }
        }

        aliases.addAll(DEFAULT_ALIASES);

        ProjectHelper projectHelper = new ProjectHelper(project, projectBuilder, remoteRepositories, localRepository);
        LicenseResolver licenseResolver = new LicenseResolver(projectHelper, includeDependencies, aliases, licenses, ignore);

        Set<LicenseDependency> dependencies = licenseResolver.resolveLicenseDependencies();
        LicenseReporter reporter = new LicenseReporter();
        reporter.generate(dependencies, outputDir, resourceName, getLog());
    }

    static final Map<String, License> LICENSES = ImmutableMap.of(
            "apache2", License.withKey("Apache License, Version 2.0", "apache2"),
            "mit", License.withKey("Mit License (MIT)", "mit"),
            "bsd2", License.withKey("BSD 2-Clause License", "bsd2"),
            "ccpl3", License.withKey("Creative Commons Public License, Attribution 3.0", "ccpl3")
    );

    static final List<Alias> DEFAULT_ALIASES = Arrays.asList(
            new Alias(LICENSES.get("apache2"), "Apache License Version 2.0", "Apache 2.0 License"),
            new Alias(LICENSES.get("mit"), "Mit License", "MIT")
    );

    static boolean isBuiltIn(String key) {
        return LICENSES.containsKey(key);
    }
}
