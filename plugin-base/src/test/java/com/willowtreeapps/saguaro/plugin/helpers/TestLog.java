package com.willowtreeapps.saguaro.plugin.helpers;

import com.willowtreeapps.saguaro.plugin.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * User: evantatarka
 * Date: 4/3/14
 * Time: 9:38 AM
 */
public class TestLog implements Log {
    private List<String> logs = new ArrayList<String>();

    @Override
    public void info(String info) {
        logs.add(info);
    }

    public List<String> getLogs() {
        return logs;
    }
}
