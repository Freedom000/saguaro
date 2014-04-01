package com.willowtreeapps.saguaro.gradle

import groovy.transform.Canonical

/**
 * License metadata. Includes name and text url.
 */
@Canonical
class LicenseMetadata implements Serializable {
    /**
     * License name.
     */
    String name

    /**
     * URL with license text.
     */
    String url

    /**
     * License key.
     */
    String key

    public String getKey() {
        if (key != null) return key
        return key = name.replace(' ', '_').replaceAll(/[^a-zA-Z0-9_]/, '').toLowerCase()
    }
}
