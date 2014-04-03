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
package com.willowtreeapps.saguaro.gradle
import com.willowtreeapps.saguaro.plugin.LicenseDependency
import com.willowtreeapps.saguaro.plugin.LicenseInfo
import com.willowtreeapps.saguaro.plugin.LicenseResolver
import com.willowtreeapps.saguaro.plugin.PluginException
import groovy.util.slurpersupport.GPathResult
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.ResolvedArtifact

import static com.google.common.base.Strings.isNullOrEmpty
import static com.google.common.collect.Sets.newHashSet

class GradleLicenseResolver implements LicenseResolver {
    private static final String DEFAULT_CONFIGURATION_TO_HANDLE = "compile"

    /**
     * Reference to gradle project.
     */
    private Project project

    public GradleLicenseResolver(Project project) {
        this.project = project
    }

    @Override
    Set<LicenseDependency> resolveLicenseDependencies() throws PluginException {
        def subprojects = subprojects(project)

        resolveProjectDependencies(project).collect { rd ->
            "$rd.moduleVersion.id.group:$rd.moduleVersion.id.name:$rd.moduleVersion.id.version"
        }
        .findAll { !subprojects[it] }
        .collect { retrieveLicensesForDependency(it) }
        .findAll { it != null }
    }

    /**
     * Provide full list of resolved artifacts to handle for a given project.
     *
     * @param project                       the project
     * @return Set with resolved artifacts
     */
    Set<ResolvedArtifact> resolveProjectDependencies(Project project) {
        Set<ResolvedArtifact> dependenciesToHandle = newHashSet()
        def subprojects = subprojects(project)

        if (project.configurations.any { it.name == DEFAULT_CONFIGURATION_TO_HANDLE }) {
            def runtimeConfiguration = project.configurations.getByName(DEFAULT_CONFIGURATION_TO_HANDLE)
            runtimeConfiguration.resolvedConfiguration.resolvedArtifacts.each { ResolvedArtifact d ->
                String dependencyDesc = "$d.moduleVersion.id.group:$d.moduleVersion.id.name:$d.moduleVersion.id.version".toString()

                Project subproject = subprojects[dependencyDesc]?.first()
                if (subproject) {
                    dependenciesToHandle.add(d)
                    dependenciesToHandle.addAll(resolveProjectDependencies(subproject))
                } else if (!subproject) {
                    dependenciesToHandle.add(d)
                }
            }
        }

        dependenciesToHandle
    }

    private static def subprojects(Project project) {
        project.rootProject.subprojects.groupBy { Project p -> "$p.group:$p.name:$p.version" }
    }

    /**
     * Recursive function for retrieving licenses via creating
     * and resolving detached configuration with "pom" extension.
     *
     * If no license was found in pom, we try to find it in parent pom declaration.
     * Parent pom descriptors are resolved in recursive way until we have no parent.
     *
     * Implementation note: We rely that while resolving configuration with one dependency we get one pom.
     * Otherwise we have IllegalStateException
     *
     * @param dependencyDesc dependency description
     * @param dependencyName dependency display name (not parent)
     * @param initialDependency base dependency (not parent)
     * @return dependency metadata, includes license info
     */
    private LicenseDependency retrieveLicensesForDependency(String dependencyDesc,
                                                             String dependencyName = null,
                                                             String initialDependency = dependencyDesc) {
        Dependency d = project.dependencies.create("$dependencyDesc@pom")
        Configuration pomConfiguration = project.configurations.detachedConfiguration(d)

        File pStream = pomConfiguration.resolve().asList().first()
        GPathResult xml = new XmlSlurper().parse(pStream)
        String name = xml.name.text().trim()
        if (dependencyName == null && name != null) dependencyName = name
        com.willowtreeapps.saguaro.plugin.Dependency dependency = new com.willowtreeapps.saguaro.plugin.Dependency(d.group, d.name)

        Set<LicenseInfo> licenses = xml.licenses.license.collect {
            LicenseInfo.withUrl(it.name.text().trim(), it.url.text().trim())
        }

        if (!licenses.isEmpty()) {
            new LicenseDependency(dependencyName, dependency, licenses)
        } else if (!isNullOrEmpty(xml.parent.text())) {
            String parentGroup = xml.parent.groupId.text().trim()
            String parentName = xml.parent.artifactId.text().trim()
            String parentVersion = xml.parent.version.text().trim()

            retrieveLicensesForDependency("$parentGroup:$parentName:$parentVersion", dependencyName, initialDependency)
        } else {
            return new LicenseDependency(dependencyName, dependency, [] as Set)
        }
    }
}
