package com.willowtreeapps.saguaro.plugin.helpers;

import com.willowtreeapps.saguaro.plugin.LicenseDependency;
import com.willowtreeapps.saguaro.plugin.PluginException;
import com.willowtreeapps.saguaro.plugin.SaguaroConfig;
import com.willowtreeapps.saguaro.plugin.SaguaroGenerate;

import java.io.IOException;
import java.util.Set;

/**
 * User: evantatarka
 * Date: 4/3/14
 * Time: 9:30 AM
 */
public class TestHelper {
    public static Set<LicenseDependency> resolve(Set<LicenseDependency> dependencies, SaguaroConfig config) throws PluginException {
        TestGenerator generator = new TestGenerator();
        TestResolver resolver = new TestResolver(dependencies);
        SaguaroGenerate generate = new SaguaroGenerate(generator, resolver);
        try {
            generate.execute(config, new TestLog());
        } catch (IOException e) {
            // Should not happen since testing classes don't do IO
            throw new RuntimeException(e);
        }
        return generator.getDependencies();
    }
}
