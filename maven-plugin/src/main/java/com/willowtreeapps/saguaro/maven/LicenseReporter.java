package com.willowtreeapps.saguaro.maven;

import com.google.common.collect.HashMultimap;
import com.jamesmurty.utils.XMLBuilder;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.codehaus.plexus.util.IOUtil;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerException;
import java.io.*;
import java.net.URL;
import java.util.*;

/**
 * User: evantatarka
 * Date: 3/31/14
 * Time: 1:18 PM
 */
public class LicenseReporter {
    public void generate(Set<LicenseDependency> dependencies, File outputDir, String resourceName, Log log) throws MojoExecutionException {
        File outputFile = new File(outputDir, "values/" + resourceName + ".xml");
        outputFile.getParentFile().mkdirs();

        try {
            XMLBuilder xml = XMLBuilder.create("resources");
            HashMultimap<String, LicenseDependency>  licenseMap = getLicenseMap(dependencies);

            List<String> nonBuiltIn = new ArrayList<String>();
            for (Map.Entry<String, Collection<LicenseDependency>> entry: licenseMap.asMap().entrySet()) {
                XMLBuilder stringArray = xml.elem("string-array").attr("name", entry.getKey() + "_projects");
                for (LicenseDependency d : entry.getValue()) {
                    stringArray.elem("item").text(d.getName());
                }

                if (!SaguaroMojo.isBuiltIn(entry.getKey())) {
                    nonBuiltIn.add(entry.getKey());
                    for (LicenseDependency dependency : entry.getValue()) {
                        LicenseInfo license = dependency.getLicenses().iterator().next();
                        xml.elem("string").attr("name", license.getKey() + "_name").text(license.getName());
                        if (license.getUrl() != null) {
                            try {
                                downloadLicense(entry.getKey(), license, outputDir, log);
                            } catch (IOException e) {
                                throw new MojoExecutionException("Cannot download license: " + license.getUrl(), e);
                            }
                        }
                    }
                }
            }

            if (!nonBuiltIn.isEmpty()) {
                XMLBuilder stringArray = xml.elem("string-array").attr("name", "saguaro_licenses");
                for (String d : nonBuiltIn) {
                    stringArray.elem("item").text(d);
                }
            }

            Properties outputProperties = new Properties();
            outputProperties.setProperty(OutputKeys.INDENT, "yes");

            FileWriter writer = null;
            try {
                writer = new FileWriter(outputFile);
                xml.toWriter(writer, outputProperties);
                log.info("Created resource: " + outputFile);
            } finally {
                if (writer != null) writer.close();
            }
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new MojoExecutionException("Cannot write: " + outputFile, e);
        }
    }

    private void downloadLicense(String key, LicenseInfo license, File outputDir, Log log) throws IOException {
        File path = new File(outputDir, "raw/" + key + ".txt");
        path.getParentFile().mkdirs();
        URL url = new URL(license.getUrl());

        InputStream in = null;
        OutputStream out = null;
        try {
            in = new BufferedInputStream(url.openConnection().getInputStream());
            out = new BufferedOutputStream(new FileOutputStream(path));

            IOUtil.copy(in, out);
            log.info("Downloaded LicenseInfo: " + license.getName() + " to " + path);
        } finally {
            if (in != null) in.close();
            if (out != null) out.close();
        }
    }

    private HashMultimap<String, LicenseDependency> getLicenseMap(Set<LicenseDependency> dependencies) {
        HashMultimap<String, LicenseDependency> licenseMap = HashMultimap.create();

        for (LicenseDependency dependency : dependencies) {
            for (LicenseInfo license : dependency.getLicenses()) {
                licenseMap.put(license.getKey(), dependency);
            }
        }

        return licenseMap;
    }
}
