package com.willowtreeapps.saguaro.plugin.helpers;

import com.willowtreeapps.saguaro.plugin.LicenseDependency;
import com.willowtreeapps.saguaro.plugin.LicenseGenerator;
import com.willowtreeapps.saguaro.plugin.Log;

import java.io.File;
import java.io.IOException;
import java.util.Set;

/**
 * User: evantatarka
 * Date: 4/3/14
 * Time: 9:37 AM
 */
public class TestGenerator implements LicenseGenerator {
    private Set<LicenseDependency> dependencies;

    @Override
    public void generate(Set<LicenseDependency> dependencies, File outputDir, String resourceName, Log log) throws IOException {
        this.dependencies = dependencies;
    }

    public Set<LicenseDependency> getDependencies() {
        return dependencies;
    }
}
