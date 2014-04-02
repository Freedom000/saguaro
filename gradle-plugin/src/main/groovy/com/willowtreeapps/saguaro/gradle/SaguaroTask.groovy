package com.willowtreeapps.saguaro.gradle
import com.willowtreeapps.saguaro.plugin.*
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
/**
 * Task for generating dependency license resources
 */
public class SaguaroTask extends DefaultTask implements SaguaroConfig {
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

        LicenseResolver licenseResolver = new GradleLicenseResolver(project)
        SaguaroGenerate generate = new SaguaroGenerate(licenseResolver)
        generate.execute(this, new GradleLog(logger))
    }

    @Override
    boolean includeDependencies() {
        return includeDependencies
    }
}

