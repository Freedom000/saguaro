package com.willowtreeapps.saguaro.plugin;

import org.fest.util.Sets;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Set;

import static com.willowtreeapps.saguaro.plugin.helpers.TestHelper.resolve;
import static org.fest.assertions.api.Assertions.assertThat;

/**
 * User: evantatarka
 * Date: 4/3/14
 * Time: 11:01 AM
 */
@RunWith(JUnit4.class)
public class TestDependencies {
    @Test(expected = PluginException.class)
    public void testNonLicensedDependencyFails() throws PluginException {
        Set<LicenseDependency> dependencies = Sets.newLinkedHashSet(
                LicenseDependency.of("Lib 1", new Dependency("group", "test"))
        );
        SaguaroConfig config = SaguaroConfig.of().build();
        resolve(dependencies, config).iterator().next();
    }

    @Test
    public void testIgnoredNonLicensedDependency() throws PluginException {
        Set<LicenseDependency> dependencies = Sets.newLinkedHashSet(
                LicenseDependency.of("Lib 1", new Dependency("group", "test"))
        );
        SaguaroConfig config = SaguaroConfig.of()
                .ignore(new Dependency("group", "test"))
                .build();
        Set<LicenseDependency> result = resolve(dependencies, config);
        assertThat(result).isEmpty();
    }
}
