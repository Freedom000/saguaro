package com.willowtreeapps.saguaro.android.sample.activity;

import com.willowtreeapps.saguaro.android.Saguaro;
import com.willowtreeapps.saguaro.android.sample.R;

import android.os.Bundle;
import android.widget.TextView;

/**
 * User: derek Date: 2/13/14 Time: 3:07 PM
 */
public class VersionTextActivity extends BaseSampleActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_version_text);

        TextView versionText = (TextView) findViewById(R.id.version_text);
        versionText.setText(Saguaro.getFullVersionString(this));
        versionText.append("\n" + Saguaro.getMinVersionString(this));
    }
}