package com.willowtreeapps.saguaro.maven;

import java.util.Collections;
import java.util.Set;

/**
 * User: evantatarka
 * Date: 3/31/14
 * Time: 1:15 PM
 */
public class LicenseDependency {
    private String name;
    private Set<License> licenses;

    public LicenseDependency(String name, Set<License> licenses) {
        this.name = name;
        this.licenses = licenses;
    }

    public String getName() {
        return name;
    }

    public Set<License> getLicenses() {
        return Collections.unmodifiableSet(licenses);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LicenseDependency that = (LicenseDependency) o;

        return !(name != null ? !name.equals(that.name) : that.name != null);
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
