package com.willowtreeapps.saguaro.android.sample.activity;

import com.willowtreeapps.saguaro.android.Saguaro;
import com.willowtreeapps.saguaro.android.sample.R;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * User: derek Date: 2/13/14 Time: 4:37 PM
 */
public class SendFeedbackActivity extends BaseSampleActivity {

    private Button sendFeedbackButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_feedback);

        sendFeedbackButton = (Button) findViewById(R.id.send_feedback);
        sendFeedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(Saguaro.getSendFeedbackIntent(SendFeedbackActivity.this));
            }
        });
    }
}
