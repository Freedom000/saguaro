package com.willowtreeapps.saguaro.gradle
import groovy.transform.Canonical
/**
 * Dependency metadata. Contains:
 * Dependency name, license metadata list.
 */
@Canonical
class DependencyMetadata {
    /**
     * Create Dependency metadata for dependencies without licenses.
     */
    public static final DependencyMetadata noLicenseMetaData(String dependency, String fileName = null) {
        return new DependencyMetadata(
                dependency: dependency,
                name: dependency,
                dependencyFileName: fileName,
                licenseMetadataList: []
        )
    }

    /**
     * List with license metadata.
     */
    List<LicenseMetadata> licenseMetadataList = []

    /**
     * Dependency name.
     */
    String dependency

    /**
     * Dependency human-readable name
     */
    String name

    /**
     * Dependency jar file name.
     */
    String dependencyFileName

    /**
     * Check whether metadata list is empty.
     *
     * @return license metadata list is empty or not
     */
    boolean hasLicense() {
        !licenseMetadataList.empty
    }

    /**
     * Add license.
     *
     * @param licenseMetadata license metadata to add
     */
    void addLicense(LicenseMetadata licenseMetadata) {
        licenseMetadataList.add(licenseMetadata)
    }

}
