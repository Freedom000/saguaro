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

import com.google.common.collect.HashMultimap;
import org.codehaus.plexus.util.IOUtil;

import java.io.*;
import java.net.URL;
import java.util.Set;

public class LicenseUtil {
    public static void download(LicenseInfo licenseInfo, File outputDir) throws IOException {
        File path = new File(outputDir, "raw/" + licenseInfo.getKey() + ".txt");
        path.getParentFile().mkdirs();
        URL url = new URL(licenseInfo.getUrl());

        InputStream in = null;
        OutputStream out = null;
        try {
            in = new BufferedInputStream(url.openConnection().getInputStream());
            out = new BufferedOutputStream(new FileOutputStream(path));

            IOUtil.copy(in, out);
        } finally {
            if (in != null) in.close();
            if (out != null) out.close();
        }
    }

    public static HashMultimap<String, LicenseDependency> toMultiMap(Set<LicenseDependency> licenseDependencies) {
        HashMultimap<String, LicenseDependency> licenseMap = HashMultimap.create();

        for (LicenseDependency dependency : licenseDependencies) {
            for (LicenseInfo license : dependency.getLicenses()) {
                licenseMap.put(license.getKey(), dependency);
            }
        }

        return licenseMap;
    }
}
