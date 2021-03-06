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

import com.willowtreeapps.saguaro.plugin.Alias
import com.willowtreeapps.saguaro.plugin.Defaults
import com.willowtreeapps.saguaro.plugin.Dependency
import com.willowtreeapps.saguaro.plugin.License
import com.willowtreeapps.saguaro.plugin.LicenseInfo

import static com.willowtreeapps.saguaro.plugin.License.LICENSES

class SaguaroExt {
    /**
     * Additional licenses to add
     */
    List<License> licenses = []

    /**
     * Alias one license name to another. Ths is useful when two dependencies have the same license but name them
     * differently
     */
    List<Alias> aliases = []

    /**
     * Dependencies to ignore
     */
    List<Dependency> ignore = []

    /**
     * Whether or not to automatically include your project's dependencies.
     * You can set this to false if you only want to define your licenses explicitly
     */
    boolean includeDependencies = true

    /**
     * The name of the generated resource
     */
    String resourceName = Defaults.RESOURCE_NAME

    def alias(String licenseName, String name) {
        alias(licenseName, [name])
    }

    def alias(String licenseName, List<String> names) {
        alias(LicenseInfo.withName(licenseName), names)
    }

    def alias(LicenseInfo licenseInfo, String name) {
        alias(licenseInfo, [name])
    }

    def alias(LicenseInfo licenseInfo, List<String> names) {
        alias(new Alias(licenseInfo, names))
    }

    def alias(Alias alias) {
        aliases << alias
    }

    def license(Map licenseInfo, String lib) {
        license(licenseInfo, [lib])
    }

    def license(Map licenseInfo, List<String> libs) {
        license(licenseInfo as LicenseInfo, libs)
    }

    def license(LicenseInfo licenseInfo, String lib) {
        license(licenseInfo, [lib])
    }

    def license(LicenseInfo licenseInfo, List<String> libs) {
        license(new License(licenseInfo, libs))
    }

    def license(License license) {
        licenses << license
    }

    def ignore(String name) {
        def (String group, String artifact) = name.split(":")
        ignore(group, artifact)
    }

    def ignore(String group, String artifact) {
        ignore(new Dependency(group, artifact))
    }

    def ignore(Dependency dependency) {
        ignore << dependency
    }

    def resourceName(String name) {
        resourceName = name
    }

    static final LicenseInfo apache2 = LICENSES.get("apache2")
    static final LicenseInfo mit = LICENSES.get("mit")
    static final LicenseInfo bsd2 = LICENSES.get("bsd2")
    static final LicenseInfo ccpl3 = LICENSES.get("ccpl3")

    static boolean isBuiltIn(String key) {
        return key.equals(apache2.key) || key.equals(mit.key) || key.equals(bsd2.key) || key.equals(ccpl3.key)
    }

}
