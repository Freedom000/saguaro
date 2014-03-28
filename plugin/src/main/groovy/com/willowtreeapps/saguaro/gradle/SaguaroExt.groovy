package com.willowtreeapps.saguaro.gradle

class SaguaroExt {
    /**
     * Additional licenses to add
     */
    Map<String, Object> licenses = [:]

    /**
     * Alias one license name to another. Ths is useful when two dependencies have the same license but name them
     * differently
     */
    Map<Object, List<Object>> aliases = [
            (apache2): ['Apache License Version 2.0'],
            (mit): ['Mit License', 'MIT']
    ]

    /**
     * Dependencies to ignore
     */
    List<String> ignoreDependencies = []

    /**
     * Whether or not to automatically include your project's dependencies.
     * You can set this to false if you only want to define your licenses explicitly
     */
    boolean includeDependencies = true

    def alias(Object name, Object value) {
        this.alias(name, [value])
    }

    def alias(Object name, List<Object> values) {
        if (aliases.containsKey(name)) {
            aliases.get(name).addAll(values)
        } else {
            aliases.put(name, values)
        }
    }

    def license(Object license, List<String> names) {
        names.each { licenses.put(it, license) }
    }

    def license(Object license, String name) {
        this.license(license, [name])
    }

    def ignore(String dependency) {
        ignoreDependencies << dependency
    }

    static final LicenseMetadata apache2 = [name: 'Apache License, Version 2.0', key: 'apache2']
    static final LicenseMetadata mit = [name: 'Mit License (MIT)', key: 'mit']
    static final LicenseMetadata bsd2 = [name: 'BSD 2-Clause License', key: 'bsd2']
    static final LicenseMetadata ccpl3 = [name: 'Creative Commons Public License, Attribution 3.0', key: 'ccpl3']

    static boolean isBuiltIn(String key) {
        return key.equals(apache2.key) || key.equals(mit.key) || key.equals(bsd2.key) || key.equals(ccpl3.key)
    }

}
