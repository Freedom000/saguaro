package com.willowtreeapps.saguaro.plugin;

import com.google.common.base.Objects;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * User: evantatarka
 * Date: 4/2/14
 * Time: 10:05 AM
 */
public class Dependency {
    @Parameter
    private String groupId;

    @Parameter
    private String artifactId;

    public Dependency() {

    }

    public Dependency(String group, String artifact) {
        this.groupId = group;
        this.artifactId = artifact;
    }

    public boolean equals(String groupId, String artifactId) {
        return this.groupId.equals(groupId) && this.artifactId.equals(artifactId);
    }

    public String getName() {
        return artifactId;
    }

    public String getGroup() {
        return groupId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Dependency that = (Dependency) o;

        return artifactId.equals(that.artifactId) && groupId.equals(that.groupId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(groupId, artifactId);
    }

    @Override
    public String toString() {
        return groupId + ":" + artifactId;
    }
}
