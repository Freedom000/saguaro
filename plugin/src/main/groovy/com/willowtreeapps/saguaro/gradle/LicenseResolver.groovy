package com.willowtreeapps.saguaro.gradle

import groovy.util.slurpersupport.GPathResult
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.FileCollectionDependency
import org.gradle.api.artifacts.ResolvedArtifact
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging

import static com.google.common.base.Strings.isNullOrEmpty
import static com.google.common.collect.Sets.newHashSet
import static com.willowtreeapps.saguaro.gradle.DependencyMetadata.noLicenseMetaData

/**
 * License resolver for dependencies.
 */
class LicenseResolver {

    private static final Logger logger = Logging.getLogger(LicenseResolver);

    private static final String DEFAULT_CONFIGURATION_TO_HANDLE = "compile"

    /**
     * Reference to gradle project.
     */
    private Project project
    private Map<String, LicenseMetadata> licenses
    private Map<LicenseMetadata, List<Object>> aliases
    private List<String> ignoreDependencies
    private boolean includeDependencies

    /**
     * Provide set with dependencies metadata.
     *
     * For cases when we have no license information we try to use licenses file that can contains licenses.
     * Otherwise we put 'No license was found' into report and group dependencies without licenses.
     *
     * @return set with licenses
     */
    public Set<DependencyMetadata> provideLicenseMap4Dependencies() {
        Set<DependencyMetadata> licenseSet = newHashSet()
        def subprojects = project.rootProject.subprojects.groupBy { Project p -> "$p.group:$p.name:$p.version".toString()}

        // Resolve each dependency
        if (includeDependencies) {
            resolveProjectDependencies(project).each { rd ->
                String dependencyDesc = "$rd.moduleVersion.id.group:$rd.moduleVersion.id.name:$rd.moduleVersion.id.version".toString()
                Closure<DependencyMetadata> dependencyMetadata = {
                    if(!subprojects[dependencyDesc]) {
                        def depMetadata = retrieveLicensesForDependency(dependencyDesc)
                        depMetadata.dependencyFileName = rd.file.name
                        depMetadata
                    } else {
                        noLicenseMetaData(dependencyDesc, rd.file.name)
                    }
                }
                licenseSet << dependencyMetadata()
            }
        }

        // Include manually defined licenses
        licenses.each {
            licenseSet << new DependencyMetadata(name: it.key, licenseMetadataList: [it.value])
        }

        provideFileDependencies(ignoreDependencies).each { fileDependency ->
                Closure<DependencyMetadata> licenseMetadata = {
                    if (licenses.containsKey(fileDependency)) {
                        def license = licenses[fileDependency]
                        LicenseMetadata licenseMetadata = license instanceof String ? new LicenseMetadata(license) : license
                        def alias = aliases.find { aliasEntry ->
                            aliasEntry.value.any { aliasElem ->
                                if (aliasElem instanceof String) {
                                    return aliasElem == licenseMetadata.name
                                } else if(aliasElem instanceof LicenseMetadata) {
                                    return aliasElem.name == licenseMetadata.name
                                }
                            }
                        }
                        if (alias) {
                            licenseMetadata = alias.key
                        }
                        new DependencyMetadata(dependency: fileDependency, name: fileDependency, dependencyFileName: fileDependency, licenseMetadataList: [licenseMetadata])
                    } else {
                        noLicenseMetaData(fileDependency, fileDependency)
                    }
                }

                licenseSet << licenseMetadata()
        }

        licenseSet
    }

    /**
     * Provide full list of resolved artifacts to handle for a given project.
     *
     * @param project                       the project
     * @param dependenciesToIgnore list of dependencies that will be excluded from the report
     * @return Set with resolved artifacts
     */
    Set<ResolvedArtifact> resolveProjectDependencies(Project project) {

        Set<ResolvedArtifact> dependenciesToHandle = newHashSet()
        def subprojects = project.rootProject.subprojects.groupBy { Project p -> "$p.group:$p.name:$p.version".toString()}

        if (project.configurations.any { it.name == DEFAULT_CONFIGURATION_TO_HANDLE }) {
            def runtimeConfiguration = project.configurations.getByName(DEFAULT_CONFIGURATION_TO_HANDLE)
            runtimeConfiguration.resolvedConfiguration.resolvedArtifacts.each { ResolvedArtifact d ->
                String dependencyDesc = "$d.moduleVersion.id.group:$d.moduleVersion.id.name:$d.moduleVersion.id.version".toString()
                if(!ignoreDependencies.contains(dependencyDesc)) {
                    Project subproject = subprojects[dependencyDesc]?.first()
                    if (subproject) {
                        dependenciesToHandle.add(d)
                        dependenciesToHandle.addAll(resolveProjectDependencies(subproject))
                    } else if (!subproject) {
                        dependenciesToHandle.add(d)
                    }
                }
            }
        }

        logger.debug("Project $project.name found ${dependenciesToHandle.size()} dependencies to handle.")
        dependenciesToHandle
    }

    Set<String> provideFileDependencies(List<String> dependenciesToIgnore) {
        Set<String> fileDependencies = newHashSet()

        if (project.configurations.any { it.name == DEFAULT_CONFIGURATION_TO_HANDLE }) {
            Configuration configuration = project.configurations.getByName(DEFAULT_CONFIGURATION_TO_HANDLE)

            Set<Dependency> d = configuration.allDependencies.findAll {
                it instanceof FileCollectionDependency
            }

            d.each { FileCollectionDependency fileDependency ->
                fileDependency.source.files.each {
                    if (!dependenciesToIgnore.contains(it.name)) {
                        fileDependencies.add(it.name)
                    }
                }
            }

        }

        logger.debug("Project $project.name found ${fileDependencies.size()} file dependencies to handle.")
        fileDependencies
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
     * @param aliases alias mapping for similar license names
     * @param initialDependency base dependency (not parent)
     * @return dependency metadata, includes license info
     */
    private DependencyMetadata retrieveLicensesForDependency(String dependencyDesc,
                                                             String dependencyName = null,
                                                             String initialDependency = dependencyDesc) {
        Dependency d = project.dependencies.create("$dependencyDesc@pom")
        Configuration pomConfiguration = project.configurations.detachedConfiguration(d)

        File pStream = pomConfiguration.resolve().asList().first()
        GPathResult xml = new XmlSlurper().parse(pStream)
        String name = xml.name.text().trim()
        if (dependencyName == null && name != null) dependencyName = name
        DependencyMetadata pomData = new DependencyMetadata(dependency: initialDependency, name: dependencyName)

        xml.licenses.license.each {
            def license = new LicenseMetadata(name: it.name.text().trim(), url: it.url.text().trim())
            def alias = aliases.find { aliasEntry ->
                aliasEntry.value.any { aliasElem ->
                    if (aliasElem instanceof String) {
                        return aliasElem == license.name
                    } else if(aliasElem instanceof LicenseMetadata) {
                        return aliasElem.name == license.name
                    }
                }
            }
            if (alias) {
                license = alias.key
            }
            pomData.addLicense(license)
        }

        if (pomData.hasLicense()) {
            pomData
        } else if (!isNullOrEmpty(xml.parent.text())) {
            String parentGroup = xml.parent.groupId.text().trim()
            String parentName = xml.parent.artifactId.text().trim()
            String parentVersion = xml.parent.version.text().trim()

            retrieveLicensesForDependency("$parentGroup:$parentName:$parentVersion", dependencyName, initialDependency)
        } else {
            noLicenseMetaData(dependencyDesc, dependencyName)
        }
    }
}
