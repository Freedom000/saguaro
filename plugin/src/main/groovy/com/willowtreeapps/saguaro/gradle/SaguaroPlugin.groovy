package com.willowtreeapps.saguaro.gradle
import org.gradle.api.Plugin
import org.gradle.api.Project

class SaguaroPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        File buildOutputDir = new File(project.buildDir, "saguaro/res")

        SaguaroExt ext = project.extensions.create("saguaro", SaguaroExt)

        project.afterEvaluate {
            project.task("generateLicenses", type: GenerateLicenses) {
                outputDir = buildOutputDir
                licenses = ext.licenses
                aliases = ext.aliases
                includeDependencies = ext.includeDependencies
                ignoreDependencies = ext.ignoreDependencies
            }

            project.android.sourceSets.main.res.srcDirs += buildOutputDir
        }
    }
}
