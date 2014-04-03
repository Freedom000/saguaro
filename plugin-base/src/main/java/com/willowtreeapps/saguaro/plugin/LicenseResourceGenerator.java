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
import com.jamesmurty.utils.XMLBuilder;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import static com.willowtreeapps.saguaro.plugin.License.isBuiltIn;

public class LicenseResourceGenerator implements LicenseGenerator {
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
