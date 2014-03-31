package com.willowtreeapps.saguaro.maven;

import org.apache.maven.plugins.annotations.Parameter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
* User: evantatarka
* Date: 3/31/14
* Time: 11:32 AM
*/
public class LicenseEntry {
    @Parameter
    private License license;

    @Parameter
    private String name;

    @Parameter
    private String key;

    @Parameter
    private String url;

    @Parameter
    private List<String> libraries = new ArrayList<String>();

    @Parameter
    private String library;

    public License getLicense() {
        if (license == null) {
            license = new License(name, key, url);
        }
        return license;
    }

    public List<String> getLibraries() {
        if (libraries.isEmpty() && library != null) {
            libraries.add(library);
        }

        return Collections.unmodifiableList(libraries);
    }
}
