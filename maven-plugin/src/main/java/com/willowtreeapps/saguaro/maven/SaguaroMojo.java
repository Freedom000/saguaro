/*
 * Copyright (C) 2014 WillowTree Apps Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.willowtreeapps.saguaro.maven;

import com.willowtreeapps.saguaro.maven.util.ProjectHelper;
import com.willowtreeapps.saguaro.plugin.*;
import org.apache.maven.artifact.repository.ArtifactRepository;
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Mojo(name = "generate", requiresDependencyResolution = ResolutionScope.COMPILE)
public class SaguaroMojo extends AbstractMojo {
    @Parameter
    private Set<Dependency> ignore = new LinkedHashSet<Dependency>();

    @Parameter
    private List<License> licenses = new ArrayList<License>();

    @Parameter
    private List<Alias> aliases = new ArrayList<Alias>();

    @Parameter(defaultValue = Defaults.RESOURCE_NAME)
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

        ProjectHelper projectHelper = new ProjectHelper(project, projectBuilder, remoteRepositories, localRepository);
        LicenseGenerator licenseGenerator = new LicenseResourceGenerator();
        MavenLicenseResolver licenseResolver = new MavenLicenseResolver(projectHelper);
        SaguaroConfig config = SaguaroConfig.of()
                .ignore(ignore)
                .licenses(licenses)
                .aliases(aliases)
                .resourceName(resourceName)
                .includeDependencies(includeDependencies)
                .outputDir(getOutputDir())
                .build();

        SaguaroGenerate generate = new SaguaroGenerate(licenseGenerator, licenseResolver);

        try {
            generate.execute(config, new MavenLog(getLog()));
        } catch (IOException e) {
            throw new MojoExecutionException("Cannot create resource files", e);
        } catch (PluginException e) {
            throw new MojoExecutionException("Cannot create resource files", e);
        }
    }

    private File getOutputDir() {
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

        return outputDir;
    }
}
