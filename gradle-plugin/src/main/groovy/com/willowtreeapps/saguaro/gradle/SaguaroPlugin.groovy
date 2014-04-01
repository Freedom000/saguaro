package com.willowtreeapps.saguaro.gradle
import org.gradle.api.Plugin
import org.gradle.api.Project

class SaguaroPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {

        SaguaroExt ext = project.extensions.create("saguaro", SaguaroExt)

        project.afterEvaluate {
            File buildOutputDir = project.android.sourceSets.main.res.srcDirs.iterator().next()

            project.task("saguaroGenerate", type: SaguaroGenerate) {
                outputDir = buildOutputDir
                resourceName = ext.resourceName
                licenses = ext.licenses
                aliases = ext.aliases
                includeDependencies = ext.includeDependencies
                ignoreDependencies = ext.ignoreDependencies
            }

            project.android.sourceSets.main.res.srcDirs += buildOutputDir
        }
    }
}
