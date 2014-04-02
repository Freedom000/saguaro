package com.willowtreeapps.saguaro.plugin;

import java.util.Set;

/**
 * User: evantatarka
 * Date: 4/2/14
 * Time: 10:11 AM
 */
public interface LicenseResolver {
    public Set<LicenseDependency> resolveLicenseDependencies() throws PluginException;
}
