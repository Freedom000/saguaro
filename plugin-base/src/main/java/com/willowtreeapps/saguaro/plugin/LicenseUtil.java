package com.willowtreeapps.saguaro.plugin;

import com.google.common.collect.HashMultimap;
import org.codehaus.plexus.util.IOUtil;

import java.io.*;
import java.net.URL;
import java.util.Set;

/**
 * User: evantatarka
 * Date: 4/2/14
 * Time: 9:33 AM
 */
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
