package com.willowtreeapps.saguaro.plugin;

import java.io.File;
import java.util.*;

/**
 * User: evantatarka
 * Date: 4/3/14
 * Time: 9:42 AM
 */
public class SaguaroConfig {
    private Set<Dependency> ignore;
    private List<License> licenses;
    private List<Alias> aliases;
    private String resourceName = Defaults.RESOURCE_NAME;
    private boolean includeDependencies;
    private File outputDir;

    private SaguaroConfig(Set<Dependency> ignore, List<License> licenses, List<Alias> aliases, String resourceName, boolean includeDependencies, File outputDir) {
        this.ignore = ignore;
        this.licenses = licenses;
        this.aliases = aliases;
        this.resourceName = resourceName;
        this.includeDependencies = includeDependencies;
        this.outputDir = outputDir;
    }

    public static Builder of() {
        return new Builder();
    }

    public static class Builder {
        private Set<Dependency> ignore = new LinkedHashSet<Dependency>();
        private List<License> licenses = new ArrayList<License>();
        private List<Alias> aliases = new ArrayList<Alias>();
        private String resourceName = Defaults.RESOURCE_NAME;
        private boolean includeDependencies = true;
        private File outputDir = new File("");

        public Builder ignore(Set<Dependency> ignore) {
            this.ignore.addAll(ignore);
            return this;
        }

        public Builder ignore(Dependency...ignore) {
            ignore(new LinkedHashSet<Dependency>(Arrays.asList(ignore)));
            return this;
        }

        public Builder licenses(List<License> licenses) {
            this.licenses.addAll(licenses);
            return this;
        }

        public Builder licenses(License...licenses) {
            licenses(Arrays.asList(licenses));
            return this;
        }

        public Builder aliases(List<Alias> aliases) {
            this.aliases.addAll(aliases);
            return this;
        }

        public Builder aliases(Alias...aliases) {
            aliases(Arrays.asList(aliases));
            return this;
        }

        public Builder resourceName(String resourceName) {
            this.resourceName = resourceName;
            return this;
        }

        public Builder includeDependencies(boolean includeDependencies) {
            this.includeDependencies = includeDependencies;
            return this;
        }

        public Builder outputDir(File outputDir) {
            this.outputDir = outputDir;
            return this;
        }

        public SaguaroConfig build() {
            return new SaguaroConfig(ignore, licenses, aliases, resourceName, includeDependencies, outputDir);
        }
    }

    public Set<Dependency> getIgnore() {
        return ignore;
    }

    public List<License> getLicenses() {
        return licenses;
    }

    public List<Alias> getAliases() {
        return Alias.getAliasesWithDefaults(aliases);
    }

    public String getResourceName() {
        return resourceName;
    }

    public boolean includeDependencies() {
        return includeDependencies;
    }

    public File getOutputDir() {
        return outputDir;
    }
}
