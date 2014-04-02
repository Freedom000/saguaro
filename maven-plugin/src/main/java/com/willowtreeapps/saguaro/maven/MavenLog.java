package com.willowtreeapps.saguaro.maven;

import com.willowtreeapps.saguaro.plugin.Log;

/**
 * User: evantatarka
 * Date: 4/2/14
 * Time: 10:00 AM
 */
public class MavenLog implements Log {
    private org.apache.maven.plugin.logging.Log log;

    public MavenLog(org.apache.maven.plugin.logging.Log log) {
        this.log = log;
    }

    @Override
    public void info(String info) {
        log.info(info);
    }
}
