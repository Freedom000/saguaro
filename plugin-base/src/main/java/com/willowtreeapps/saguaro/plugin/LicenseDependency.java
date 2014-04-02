package com.willowtreeapps.saguaro.plugin;

import com.google.common.base.Objects;

import java.util.Collections;
import java.util.Set;

/**
 * User: evantatarka
 * Date: 3/31/14
 * Time: 1:15 PM
 */
public class LicenseDependency {
    private String name;
    private Dependency dependency;
    private Set<LicenseInfo> licenses;

    public LicenseDependency(String name, Dependency dependency, Set<LicenseInfo> licenses) {
        this.name = name;
        this.dependency = dependency;
        this.licenses = licenses;
    }

    public static LicenseDependency withoutDependency(String name, Set<LicenseInfo> licenses) {
        return new LicenseDependency(name, null, licenses);
    }

    public String getName() {
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
        return "LicenseDependency(name: " + getName() + ", dependency: " + getDependency() + ", licenses: " + getLicenses() + ")";
    }
}
