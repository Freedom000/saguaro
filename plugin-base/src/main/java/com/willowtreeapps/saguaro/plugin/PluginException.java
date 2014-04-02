package com.willowtreeapps.saguaro.plugin;

/**
 * User: evantatarka
 * Date: 4/2/14
 * Time: 10:47 AM
 */
public class PluginException extends Exception {
    public PluginException(String message) {
        super(message);
    }

    public PluginException(String message, Throwable cause) {
        super(message, cause);
    }

    public PluginException(Throwable cause) {
        super(cause);
    }
}
