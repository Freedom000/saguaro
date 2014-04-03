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
import com.willowtreeapps.saguaro.plugin.*
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
/**
 * Task for generating dependency license resources
 */
public class SaguaroTask extends DefaultTask {
    /**
     * Output res directory
     */
    @Input File outputDir

    /**
     * Custom license mapping
     */
    @Input List<License> licenses = []

    /**
     * Aliases for licences that has different names spelling.
     */
    @Input List<Alias> aliases = []

    /**
     * Include project dependencies.
     */
    @Input boolean includeDependencies = true

    /**
     * List of dependencies that will be omitted in the report.
     */
    @Input Set<Dependency> ignore = []

    /**
     * Name of the generated resource file
     */
    @Input String resourceName = Defaults.RESOURCE_NAME

    @TaskAction
    def generateLicenses() {
        if (!enabled) {
            didWork = false;
            return;
        }

        LicenseGenerator licenceGenerator = new LicenseResourceGenerator()
        LicenseResolver licenseResolver = new GradleLicenseResolver(project)
        SaguaroGenerate generate = new SaguaroGenerate(licenceGenerator, licenseResolver)
        SaguaroConfig config = SaguaroConfig.of()
                .ignore(ignore)
                .aliases(aliases)
                .outputDir(outputDir)
                .licenses(licenses)
                .includeDependencies(includeDependencies)
                .resourceName(resourceName)
                .build()

        generate.execute(config, new GradleLog(logger))
    }
}

