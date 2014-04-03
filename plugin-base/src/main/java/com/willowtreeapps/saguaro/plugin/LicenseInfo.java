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

import org.apache.maven.plugins.annotations.Parameter;

import static com.willowtreeapps.saguaro.plugin.License.LICENSES;

public class LicenseInfo {
    @Parameter
    private String name;

    @Parameter
    private String url;

    @Parameter
    private String key;

    public static LicenseInfo withName(String name) {
        return new LicenseInfo(name, null, null);
    }

    public static LicenseInfo withUrl(String name, String url) {
        return new LicenseInfo(name, null, url);
    }

    public static LicenseInfo withKey(String name, String key) {
        return new LicenseInfo(name, key, null);
    }

    public static LicenseInfo withKey(String key) {
        return new LicenseInfo(null, key, null);
    }

    public LicenseInfo() {

    }

    public LicenseInfo(String name, String key, String url) {
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
        if (name == null && key != null && LICENSES.containsKey(key)) {
            name = LICENSES.get(key).getName();
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
        if (obj == null || !(obj instanceof LicenseInfo)) return false;
        LicenseInfo other = (LicenseInfo) obj;
        return getKey().equals(other.getKey());
    }

    @Override
    public String toString() {
        return "LicenseInfo(name: " + getName() + ", key: " + getKey() + ", url: " + getUrl() + ")";
    }
}
