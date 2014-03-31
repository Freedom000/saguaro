package com.willowtreeapps.saguaro.maven;

import com.willowtreeapps.saguaro.maven.util.ProjectHelper;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;

import java.util.*;

/**
 * User: evantatarka
 * Date: 3/31/14
 * Time: 11:35 AM
 */
public class LicenseResolver {
    private ProjectHelper projectHelper;
    private List<LicenseEntry> licenses;
    private List<Alias> aliases;
    private List<Dependency> ignoreDependencies;
    private boolean includeDependencies;

    public LicenseResolver(ProjectHelper projectHelper, boolean includeDependencies, List<Alias> aliases, List<LicenseEntry> licenses, List<Dependency> ignoreDependencies) {
        this.projectHelper = projectHelper;
        this.licenses = licenses;
        this.aliases = aliases;
        this.ignoreDependencies = ignoreDependencies;
        this.includeDependencies = includeDependencies;
    }

    public Set<LicenseDependency> resolveLicenseDependencies() throws MojoExecutionException {
        Set<Artifact> deps = projectHelper.getArtifacts();

        Set<LicenseDependency> licenseDependencies = new LinkedHashSet<LicenseDependency>();

        for (Artifact artifact : deps) {
            if (shouldIgnore(artifact)) continue;

            MavenProject depProject = projectHelper.buildFromRepository(artifact);

            List<org.apache.maven.model.License> licenseList = depProject.getLicenses();
            if (!licenseList.isEmpty()) {
                Set<License> licenses = new LinkedHashSet<License>();
                for (org.apache.maven.model.License license : licenseList) {
                    licenses.add(getLicense(license));
                }
                licenseDependencies.add(new LicenseDependency(depProject.getName(), licenses));
            }
        }

        for (LicenseEntry license : licenses) {
            for (String lib : license.getLibraries()) {
                licenseDependencies.add(new LicenseDependency(lib, Collections.singleton(license.getLicense())));
            }
        }

        return licenseDependencies;
    }

    private License getLicense(org.apache.maven.model.License license) {
        for (Alias alias : aliases) {
            for (String aliasName : alias.getAliases()) {
                if (aliasName.equals(license.getName())) {
                    return alias.getLicense();
                }
            }
        }
        return License.withUrl(license.getName(), license.getUrl());
    }

    private boolean shouldIgnore(Artifact artifact) {
        for (Dependency ignore : ignoreDependencies) {
            if (ignore.getGroupId().equals(artifact.getGroupId()) && ignore.getArtifactId().equals(artifact.getArtifactId())) {
                return true;
            }
        }
        return false;
    }
}
