package com.willowtreeapps.saguaro.plugin.helpers;

import com.willowtreeapps.saguaro.plugin.LicenseDependency;
import com.willowtreeapps.saguaro.plugin.LicenseResolver;
import com.willowtreeapps.saguaro.plugin.PluginException;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * User: evantatarka
 * Date: 4/3/14
 * Time: 9:24 AM
 */
public class TestResolver implements LicenseResolver {
    private Set<LicenseDependency> dependencies = new LinkedHashSet<LicenseDependency>();

    public TestResolver(LicenseDependency...dependencies) {
        this(new LinkedHashSet<LicenseDependency>(Arrays.asList(dependencies)));
    }

    public TestResolver(Set<LicenseDependency> dependencies) {
        this.dependencies = dependencies;
    }

    @Override
    public Set<LicenseDependency> resolveLicenseDependencies() throws PluginException {
        return dependencies;
    }
}
