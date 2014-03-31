package com.willowtreeapps.saguaro.maven;

import org.apache.maven.plugins.annotations.Parameter;

/**
* User: evantatarka
* Date: 3/31/14
* Time: 11:32 AM
*/
public class License {
    @Parameter
    private String name;

    @Parameter
    private String url;

    @Parameter
    private String key;

    public static License withUrl(String name, String url) {
        return new License(name, null, url);
    }

    public static License withKey(String name, String key) {
        return new License(name, key, null);
    }

    public License() {

    }

    public License(String name, String key, String url) {
        this.name = name;
        this.url = url;
        this.key = key;
    }

    public String getKey() {
        if (key == null && name != null) {
            key = name.replace(' ', '_').replaceAll("[^a-zA-Z0-9_]", "").toLowerCase();
        }
        return key;
    }

    public String getName() {
        if (name == null && key != null && SaguaroMojo.LICENSES.containsKey(key)) {
            name = SaguaroMojo.LICENSES.get(key).getName();
        }
        return name;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public int hashCode() {
        return getKey().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof License)) return false;
        License other = (License) obj;
        return getKey().equals(other.getKey());
    }

    @Override
    public String toString() {
        return "License(name: " + getName() + ", key: " + getKey() + ", url: " + getUrl() + ")";
    }
}
