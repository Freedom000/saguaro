package com.willowtreeapps.saguaro.gradle
import com.google.common.collect.HashMultimap
import groovy.xml.MarkupBuilder
import org.gradle.api.logging.Logger

/**
 * License file reporter.
 */
class LicenseReporter {
    /**
     * Generate license resource files
     *
     * @param dependencyMetadataSet set with dependencies
     * @param fileName report file name
     */
    public void generate(Set<DependencyMetadata> dependencyMetadataSet, File outputDir, String resourceName, Logger logger) {
        File outputFile = new File(outputDir, "values/" + resourceName + ".xml")
        MarkupBuilder xml = getMarkupBuilder(outputFile)
        HashMultimap<String, DependencyMetadata> licensesMap = getLicenseMap(dependencyMetadataSet)

        List<String> nonBuiltIn = []
        xml.resources() {
            licensesMap.asMap().each { entry ->
                'string-array'(name: "${entry.key}_projects") {
                    entry.value.each { d ->
                        item(d.name)
                    }
                }

                if (!SaguaroExt.isBuiltIn(entry.key)) {
                    nonBuiltIn << entry.key
                    entry.value.each { dependency ->
                        LicenseMetadata license = dependency.licenseMetadataList[0]
                        string(name: "${license.key}_name", license.name)
                        if (license.url != null) {
                            downloadLicense(entry.key, license, outputDir)
                        }
                    }
                }
            }

            if (!nonBuiltIn.isEmpty()) {
                'string-array'(name: 'saguaro_licenses') {
                    nonBuiltIn.each { d ->
                        item(d)
                    }
                }
            }
        }

        logger.info("Created resource: " + outputFile)
    }

    private void downloadLicense(String key, LicenseMetadata license, File output, Logger logger) {
        File path = new File(output, "raw/${key}.txt")
        path.parentFile.mkdirs()
        URL url = new URL(license.url)
        path.withOutputStream { out ->
            url.withInputStream { is ->
                out << is
                logger.info("Downloaded License: " + license.name + " to " + path);
            }
        }
    }

    // Utility
    private static HashMultimap<String, DependencyMetadata> getLicenseMap(Set<DependencyMetadata> dependencyMetadataSet) {
        HashMultimap<String, DependencyMetadata> licensesMap = HashMultimap.create()

        dependencyMetadataSet.each { dependencyMetadata ->
            dependencyMetadata.licenseMetadataList.each { license ->
                licensesMap.put(license.key, dependencyMetadata)
            }
        }

        licensesMap
    }

    private static MarkupBuilder getMarkupBuilder(File output) {
        output.getParentFile().mkdirs()
        def writer = new FileWriter(output)
        new MarkupBuilder(writer)
    }
}

