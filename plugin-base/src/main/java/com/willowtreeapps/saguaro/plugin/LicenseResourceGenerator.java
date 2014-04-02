package com.willowtreeapps.saguaro.plugin;

import com.google.common.collect.HashMultimap;
import com.jamesmurty.utils.XMLBuilder;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import static com.willowtreeapps.saguaro.plugin.License.isBuiltIn;

/**
 * User: evantatarka
 * Date: 3/31/14
 * Time: 1:18 PM
 */
public class LicenseResourceGenerator {
    public void generate(Set<LicenseDependency> dependencies, File outputDir, String resourceName, Log log) throws IOException {
        File outputFile = new File(outputDir, "values/" + resourceName + ".xml");
        outputFile.getParentFile().mkdirs();

        try {
            XMLBuilder xml = XMLBuilder.create("resources");
            HashMultimap<String, LicenseDependency> licenseMap = LicenseUtil.toMultiMap(dependencies);

            List<String> nonBuiltIn = new ArrayList<String>();
            for (Map.Entry<String, Collection<LicenseDependency>> entry : licenseMap.asMap().entrySet()) {
                XMLBuilder stringArray = xml.elem("string-array").attr("name", entry.getKey() + "_projects");
                for (LicenseDependency d : entry.getValue()) {
                    stringArray.elem("item").text(d.getName());
                }

                if (!isBuiltIn(entry.getKey())) {
                    nonBuiltIn.add(entry.getKey());
                    for (LicenseDependency dependency : entry.getValue()) {
                        LicenseInfo license = dependency.getLicenses().iterator().next();
                        xml.elem("string").attr("name", license.getKey() + "_name").text(license.getName());
                        if (license.getUrl() != null) {
                            LicenseUtil.download(license, outputDir);
                            log.info("Downloaded License: " + license.getName() + " to " + outputDir);
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
            outputProperties.setProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            outputProperties.setProperty("{http://xml.apache.org/xslt}indent-amount", "4");

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
        }
    }
}
