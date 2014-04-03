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
import org.apache.maven.artifact.Artifact;
import org.apache.maven.project.MavenProject;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

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

            Set<LicenseInfo> licenses = new LinkedHashSet<LicenseInfo>();
            for (org.apache.maven.model.License license : licenseList) {
                licenses.add(LicenseInfo.withUrl(license.getName(), license.getUrl()));
            }
            licenseDependencies.add(new LicenseDependency(depProject.getName(), dependency, licenses));
        }

        return licenseDependencies;
    }
}
