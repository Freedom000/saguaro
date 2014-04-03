/*
 * Copyright (C) 2014 WillowTree Apps Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.willowtreeapps.saguaro.plugin;

import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import java.io.IOException;
import java.util.*;

public class SaguaroGenerate {
    private LicenseResolver licenseResolver;
    private LicenseGenerator licenseGenerator;

    public SaguaroGenerate(LicenseGenerator licenseGenerator, LicenseResolver licenseResolver) {
        this.licenseGenerator = licenseGenerator;
        this.licenseResolver = licenseResolver;
    }

    public void execute(SaguaroConfig config, Log log) throws IOException, PluginException {
        Set<LicenseDependency> licenses = collectLicenses(config);
        licenseGenerator.generate(licenses, config.getOutputDir(), config.getResourceName(), log);
    }

    private Set<LicenseDependency> collectLicenses(SaguaroConfig config) throws PluginException {
        return Sets.union(collectDefinedLicenses(config), collectDependencyLicences(config));
    }

    private Set<LicenseDependency> collectDependencyLicences(SaguaroConfig config) throws PluginException {
        Set<LicenseDependency> licenseDependencies;

        if (config.includeDependencies()) {
            final Set<Dependency> ignore = config.getIgnore();

            licenseDependencies = Sets.filter(licenseResolver.resolveLicenseDependencies(),
                    new Predicate<LicenseDependency>() {
                        @Override
                        public boolean apply(LicenseDependency licenseDependency) {
                            return !ignore.contains(licenseDependency.getDependency());
                        }
                    }
            );

            checkForMissingLicenses(licenseDependencies);
        } else {
            licenseDependencies = new LinkedHashSet<LicenseDependency>();
        }

        return applyAliases(config.getAliases(), licenseDependencies);
    }

    private Set<LicenseDependency> collectDefinedLicenses(SaguaroConfig config) {
        Set<LicenseDependency> licenseDependencies = new LinkedHashSet<LicenseDependency>();
        Multimap<String, LicenseInfo> licenseMap = HashMultimap.create();

        for (License license : config.getLicenses()) {
            for (String name : license.getLibraries()) {
                licenseMap.put(name, license.getLicenseInfo());
            }
        }

        for (Map.Entry<String, Collection<LicenseInfo>> entry : licenseMap.asMap().entrySet()) {
            licenseDependencies.add(LicenseDependency.of(entry.getKey(), new LinkedHashSet<LicenseInfo>(entry.getValue())));
        }

        return licenseDependencies;
    }

    private void checkForMissingLicenses(Set<LicenseDependency> dependencies) throws PluginException {
        Set<LicenseDependency> noLicenseDependencies = getDependenciesWithoutLicenses(dependencies);
        if (!noLicenseDependencies.isEmpty()) {
            String depList = Joiner.on("\n").join(noLicenseDependencies);
            throw new PluginException("The following dependencies are missing licences, please explicitly ignore and configure them manually.\n" + depList);
        }
    }

    private Set<LicenseDependency> getDependenciesWithoutLicenses(final Set<LicenseDependency> dependencies) {
        return Sets.filter(dependencies, new Predicate<LicenseDependency>() {
            @Override
            public boolean apply(LicenseDependency dependency) {
                return dependencies.isEmpty();
            }
        });
    }

    private Set<LicenseDependency> applyAliases(List<Alias> aliases, Set<LicenseDependency> dependencies) {
        Set<LicenseDependency> resultSet = new LinkedHashSet<LicenseDependency>();
        for (LicenseDependency dependency : dependencies) {
            resultSet.add(applyAliases(aliases, dependency));
        }
        return resultSet;
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
            if (alias.getLicenseInfo().getName().equals(license.getName())) {
                return alias.getLicenseInfo();
            }

            for (String aliasName : alias.getAliases()) {
                if (aliasName.equals(license.getName())) {
                    return alias.getLicenseInfo();
                }
            }
        }
        return license;
    }
}
