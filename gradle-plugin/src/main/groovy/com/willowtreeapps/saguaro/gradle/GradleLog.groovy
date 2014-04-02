package com.willowtreeapps.saguaro.gradle

import com.willowtreeapps.saguaro.plugin.Log
import org.gradle.api.logging.Logger

/**
 * User: evantatarka
 * Date: 4/2/14
 * Time: 1:58 PM
 *
 */
class GradleLog implements Log {
    private Logger logger;

    public GradleLog(Logger logger) {
        this.logger = logger;
    }

    @Override
    void info(String info) {
        logger.info(info)
    }
}
