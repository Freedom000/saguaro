package com.willowtreeapps.saguaro.plugin;

import java.io.File;
import java.io.IOException;
import java.util.Set;

/**
 * User: evantatarka
 * Date: 4/3/14
 * Time: 9:32 AM
 */
public interface LicenseGenerator {
    void generate(Set<LicenseDependency> dependencies, File outputDir, String resourceName, Log log) throws IOException;
}
