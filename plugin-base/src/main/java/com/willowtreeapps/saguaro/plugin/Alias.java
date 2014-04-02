package com.willowtreeapps.saguaro.plugin;

import org.apache.maven.plugins.annotations.Parameter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.willowtreeapps.saguaro.plugin.License.LICENSES;

/**
* User: evantatarka
* Date: 3/31/14
* Time: 11:32 AM
*/
public class Alias {
    public static final List<Alias> DEFAULT_ALIASES = Arrays.asList(
            new Alias(LICENSES.get("apache2"), "Apache License Version 2.0", "Apache 2.0 License"),
            new Alias(LICENSES.get("mit"), "Mit License", "MIT")
    );

    @Parameter
    private String key;

    @Parameter
    private String name;

    @Parameter
    private String alias;

    @Parameter
    private List<String> aliases = new ArrayList<String>();

    private LicenseInfo licenseInfo;

    public Alias() {

    }

    public Alias(LicenseInfo licenseInfo, String...aliases) {
        this(licenseInfo, Arrays.asList(aliases));
    }

    public Alias(LicenseInfo licenseInfo, List<String> aliases) {
        this.licenseInfo = licenseInfo;
        this.aliases = aliases;
    }

    public Alias(LicenseInfo licenseInfo, List<String> aliases1, List<String> aliases2) {
        this.licenseInfo = licenseInfo;
        this.aliases = new ArrayList<String>();
        this.aliases.addAll(aliases1);
        this.aliases.addAll(aliases2);
    }


    public LicenseInfo getLicenseInfo() {
        if (licenseInfo == null && (name != null || key != null)) {
            licenseInfo = LicenseInfo.withKey(name, key);
        }
        return this.licenseInfo;
    }

    public List<String> getAliases() {
        if (aliases.isEmpty() && alias != null) {
            aliases.add(alias);
        }
        return Collections.unmodifiableList(aliases);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Alias alias = (Alias) o;

        return getLicenseInfo().equals(alias.getLicenseInfo());
    }

    @Override
    public int hashCode() {
        return getLicenseInfo().hashCode();
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder("Alias(license: " + getLicenseInfo() + ", aliases: [");
        for (int i = 0; i < getAliases().size(); i++) {
            b.append(getAliases().get(i));
            if (i < getAliases().size() - 1) b.append(", ");
        }
        return b.append("])").toString();
    }

    public static List<Alias> getAliasesWithDefaults(List<Alias> otherAliases) {
        List<Alias> allAliases = new ArrayList<Alias>();

        for (Alias defaultAlias : DEFAULT_ALIASES) {
            int index = otherAliases.indexOf(defaultAlias);
            if (index >= 0) {
                Alias alias = otherAliases.remove(index);
                allAliases.add(new Alias(defaultAlias.getLicenseInfo(), defaultAlias.getAliases(), alias.getAliases()));
            } else {
                allAliases.add(defaultAlias);
            }
        }

        allAliases.addAll(otherAliases);
        return allAliases;
    }
}
