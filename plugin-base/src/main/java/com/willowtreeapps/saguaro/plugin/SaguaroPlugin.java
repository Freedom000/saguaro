package com.willowtreeapps.saguaro.plugin;

import com.google.common.base.Predicate;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import java.io.IOException;
import java.util.*;

import static com.willowtreeapps.saguaro.plugin.Alias.getAliasesWithDefaults;

/**
 * User: evantatarka
 * Date: 4/2/14
 * Time: 10:12 AM
 */
public class SaguaroPlugin {
    private LicenseResolver licenseResolver;

    public SaguaroPlugin(LicenseResolver licenseResolver) {
        this.licenseResolver = licenseResolver;
    }

    public void execute(SaguaroConfig config, Log log) throws IOException, PluginException {
        Set<LicenseDependency> licenses = collectLicenses(config);
        LicenseResourceGenerator generator = new LicenseResourceGenerator();
        generator.generate(licenses, config.getOutputDir(), config.getResourceName(), log);
    }

    private Set<LicenseDependency> collectLicenses(SaguaroConfig config) throws PluginException {
        List<Alias> aliases = getAliasesWithDefaults(config.getAliases());

        Set<LicenseDependency> licenses = new LinkedHashSet<LicenseDependency>();

        if (config.includeDependencies()) {
            final Set<Dependency> ignore = config.getIgnore();

            Set<LicenseDependency> licenseDependencies = Sets.filter(licenseResolver.resolveLicenseDependencies(),
                    new Predicate<LicenseDependency>() {
                        @Override
                        public boolean apply(LicenseDependency licenseDependency) {
                            return !ignore.contains(licenseDependency.getDependency());
                        }
                    }
            );

            for (LicenseDependency dependency : licenseDependencies) {
                licenses.add(applyAliases(aliases, dependency));
            }
        }

        Multimap<String, LicenseInfo> licenseMap = HashMultimap.create();

        for (License license : config.getLicenses()) {
            for (String name : license.getLibraries()) {
                licenseMap.put(name, license.getLicenseInfo());
            }
        }

        for (Map.Entry<String, Collection<LicenseInfo>> entry : licenseMap.asMap().entrySet()) {
            licenses.add(LicenseDependency.withoutDependency(entry.getKey(), new LinkedHashSet<LicenseInfo>(entry.getValue())));
        }

        return licenses;
    }

    private LicenseDependency applyAliases(List<Alias> aliases, LicenseDependency dependency) {
        Set<LicenseInfo> appliedLicenses = new LinkedHashSet<LicenseInfo>();
        for (LicenseInfo license : dependency.getLicenses()) {
            appliedLicenses.add(applyAliases(aliases, license));
        }
        return new LicenseDependency(dependency.getName(), dependency.getDependency(), appliedLicenses);
    }

    private LicenseInfo applyAliases(List<Alias> aliases, LicenseInfo license) {
        for (Alias alias : aliases) {
            for (String aliasName : alias.getAliases()) {
                if (aliasName.equals(license.getName())) {
                    return alias.getLicenseInfo();
                }
            }
        }
        return license;
    }
}
