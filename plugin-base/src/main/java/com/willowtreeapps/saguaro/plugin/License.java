package com.willowtreeapps.saguaro.plugin;

import com.google.common.collect.ImmutableMap;
import org.apache.maven.plugins.annotations.Parameter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
* User: evantatarka
* Date: 3/31/14
* Time: 11:32 AM
*/
public class License {
    public static final Map<String, LicenseInfo> LICENSES = ImmutableMap.of(
            "apache2", LicenseInfo.withKey("Apache License, Version 2.0", "apache2"),
            "mit", LicenseInfo.withKey("Mit License (MIT)", "mit"),
            "bsd2", LicenseInfo.withKey("BSD 2-Clause License", "bsd2"),
            "ccpl3", LicenseInfo.withKey("Creative Commons Public License, Attribution 3.0", "ccpl3")
    );

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

    private LicenseInfo licenseInfo;

    public License() {

    }

    public License(LicenseInfo licenseInfo, List<String> libraries) {
        this.licenseInfo = licenseInfo;
        this.libraries = libraries;
    }

    public LicenseInfo getLicenseInfo() {
        if (licenseInfo == null && (name != null || key != null)) {
            licenseInfo = new LicenseInfo(name, key, url);
        }
        return licenseInfo;
    }

    public List<String> getLibraries() {
        if (libraries.isEmpty() && library != null) {
            libraries.add(library);
        }

        return Collections.unmodifiableList(libraries);
    }

    public static boolean isBuiltIn(String key) {
        return LICENSES.containsKey(key);
    }
}
