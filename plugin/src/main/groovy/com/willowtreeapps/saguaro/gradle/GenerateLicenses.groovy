package com.willowtreeapps.saguaro.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.*

/**
 * Task for generating dependency license resources
 */
public class GenerateLicenses extends DefaultTask {
    /**
     * Output res directory
     */
    @Input File outputDir

    /**
     * Custom license mapping that overrides existent if needed.
     */
    @Input Map<String, Object> licenses = [:]

    /**
     * Aliases for licences that has different names spelling.
     */
    @Input Map<Object, List<Object>> aliases = [:]

    /**
     * Include project dependencies.
     */
    @Input boolean includeDependencies = true

    /**
     * List of dependencies that will be omitted in the report.
     */
    @Input List<String> ignoreDependencies = []

    @TaskAction
    def downloadLicenses() {
        if (!enabled) {
            didWork = false;
            return;
        }

        // Lazy dependency resolving
        def dependencyLicensesSet = {
            def licenseResolver = new LicenseResolver(project: project,
                    includeDependencies: includeDependencies,
                    aliases: aliases.collectEntries {
                        new MapEntry(resolveAliasKey(it.key), it.value)
                    },
                    licenses: licenses.collectEntries {
                        new MapEntry(it.key, resolveAliasKey(it.value))
                    },
                    ignoreDependencies: ignoreDependencies)
            licenseResolver.provideLicenseMap4Dependencies()
        }.memoize()

        def reporter = new LicenseReporter()

        // Generate report that groups dependencies
        reporter.generate(dependencyLicensesSet(), outputDir)
    }

    static LicenseMetadata resolveAliasKey(key) {
        if(key instanceof String) {
            new LicenseMetadata(key)
        } else {
            key
        }
    }
}

