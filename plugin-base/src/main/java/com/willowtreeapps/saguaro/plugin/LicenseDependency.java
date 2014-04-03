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

import com.google.common.base.Objects;
import org.codehaus.plexus.util.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class LicenseDependency {
    private String name;
    private Dependency dependency;
    private Set<LicenseInfo> licenses;

    public LicenseDependency(String name, Dependency dependency, Set<LicenseInfo> licenses) {
        this.name = name;
        this.dependency = dependency;
        this.licenses = licenses;
    }

    public static LicenseDependency of(String name, Set<LicenseInfo> licenses) {
        return new LicenseDependency(name, null, licenses);
    }

    public static LicenseDependency of(String name, LicenseInfo...licenses) {
        return new LicenseDependency(name, null, new LinkedHashSet<LicenseInfo>(Arrays.asList(licenses)));
    }

    public static LicenseDependency of(String name, Dependency dependency, LicenseInfo...licenses) {
        return new LicenseDependency(name, dependency, new LinkedHashSet<LicenseInfo>(Arrays.asList(licenses)));
    }

    public String getName() {
        if (StringUtils.isEmpty(name) && dependency != null) {
            name = dependency.getName();
        }
        return name;
    }

    public Dependency getDependency() {
        return dependency;
    }

    public Set<LicenseInfo> getLicenses() {
        return Collections.unmodifiableSet(licenses);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LicenseDependency that = (LicenseDependency) o;

        if (dependency != null && that.dependency != null) {
            return dependency.equals(that.dependency);
        }

        return Objects.equal(name, that.name);
    }

    @Override
    public int hashCode() {
        if (dependency != null) return dependency.hashCode();
        if (name != null) return name.hashCode();
        return 0;
    }

    @Override
    public String toString() {
        if (dependency != null) return dependency.toString();
        if (name != null) return name;
        return "unknown";
    }
}
