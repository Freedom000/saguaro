package com.willowtreeapps.saguaro.plugin;

import java.io.File;
import java.util.List;
import java.util.Set;

/**
 * User: evantatarka
 * Date: 4/2/14
 * Time: 10:03 AM
 */
public interface SaguaroConfig {
    Set<Dependency> getIgnore();
    List<License> getLicenses();
    List<Alias> getAliases();
    String getResourceName();
    boolean includeDependencies();
    File getOutputDir();
}
