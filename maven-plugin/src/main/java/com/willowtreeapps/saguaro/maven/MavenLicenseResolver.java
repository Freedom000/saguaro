package com.willowtreeapps.saguaro.maven;

import com.willowtreeapps.saguaro.maven.util.ProjectHelper;
import com.willowtreeapps.saguaro.plugin.*;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.project.MavenProject;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * User: evantatarka
 * Date: 3/31/14
 * Time: 11:35 AM
 */
public class MavenLicenseResolver implements LicenseResolver {
    private ProjectHelper projectHelper;

    public MavenLicenseResolver(ProjectHelper projectHelper) {
        this.projectHelper = projectHelper;
    }

    @Override
    public Set<LicenseDependency> resolveLicenseDependencies() throws PluginException {
        Set<Artifact> deps = projectHelper.getArtifacts();

        Set<LicenseDependency> licenseDependencies = new LinkedHashSet<LicenseDependency>();

        for (Artifact artifact : deps) {
            MavenProject depProject = projectHelper.buildFromRepository(artifact);
            Dependency dependency = new Dependency(depProject.getGroupId(), depProject.getArtifactId());

            List<org.apache.maven.model.License> licenseList = depProject.getLicenses();
            if (!licenseList.isEmpty()) {
                Set<LicenseInfo> licenses = new LinkedHashSet<LicenseInfo>();
                for (org.apache.maven.model.License license : licenseList) {
                    licenses.add(LicenseInfo.withUrl(license.getName(), license.getUrl()));
                }
                licenseDependencies.add(new LicenseDependency(depProject.getName(), dependency, licenses));
            }
        }

        return licenseDependencies;
    }
}
