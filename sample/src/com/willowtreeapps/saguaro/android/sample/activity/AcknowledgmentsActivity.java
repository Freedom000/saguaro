package com.willowtreeapps.saguaro.android.sample.activity;

import com.willowtreeapps.saguaro.android.Saguaro;
import com.willowtreeapps.saguaro.android.sample.R;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * User: derek Date: 2/13/14 Time: 4:37 PM
 */
public class AcknowledgmentsActivity extends BaseSampleActivity {

    private Button showOpenSourceDialogsButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acknowledgments);

        showOpenSourceDialogsButton = (Button) findViewById(R.id.show_open_source_dialogs);
        showOpenSourceDialogsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Saguaro.showOpenSourceDialog(AcknowledgmentsActivity.this);
            }
        });
    }
}
