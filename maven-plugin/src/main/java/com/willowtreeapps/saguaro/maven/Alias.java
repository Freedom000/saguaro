package com.willowtreeapps.saguaro.maven;

import org.apache.maven.plugins.annotations.Parameter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
* User: evantatarka
* Date: 3/31/14
* Time: 11:32 AM
*/
public class Alias {
    @Parameter
    private License license;

    @Parameter
    private List<String> aliases;

    public Alias() {

    }

    public Alias(License license, String...aliases) {
        this.license = license;
        this.aliases = Arrays.asList(aliases);
    }

    public License getLicense() {
        return this.license;
    }

    public List<String> getAliases() {
        return Collections.unmodifiableList(aliases);
    }
}
