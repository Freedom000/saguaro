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
 * Time: 9:30 AM
 */
@RunWith(JUnit4.class)
public class TestAliases {
    @Test
    public void testSingleAlias() {
        Set<LicenseDependency> dependencies = Sets.newLinkedHashSet(
                LicenseDependency.of("Lib 1", LicenseInfo.withName("Test License Alias"))
        );
        SaguaroConfig config = SaguaroConfig.of()
                .aliases(new Alias(LicenseInfo.withName("Test License"), "Test License Alias"))
                .build();
        LicenseDependency result = resolve(dependencies, config).iterator().next();

        assertThat(result.getLicenses()).contains(LicenseInfo.withName("Test License"));
    }

    @Test
    public void testBuildInAlias() {
        Set<LicenseDependency> dependencies = Sets.newLinkedHashSet(
                LicenseDependency.of("Lib 1", LicenseInfo.withName("Apache License Version 2.0"))
        );
        SaguaroConfig config = SaguaroConfig.of().build();
        LicenseDependency result = resolve(dependencies, config).iterator().next();

        assertThat(result.getLicenses()).contains(LicenseInfo.withKey("apache2"));
    }
}
