package com.willowtreeapps.saguaro.android.sample.activity;

import com.willowtreeapps.saguaro.android.Saguaro;
import com.willowtreeapps.saguaro.android.animation.AnimatedWtaLogoFragment;
import com.willowtreeapps.saguaro.android.sample.R;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

/**
 * User: derek Date: 2/13/14 Time: 3:34 PM
 */
public class AttributionActivity extends BaseSampleActivity {

    private Handler mHandler = new Handler();

    private Button attributionButton;
    private Button resetAnimationsButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attribution);

        attributionButton = (Button) findViewById(R.id.button_attribution);
        attributionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(Saguaro.getAttributionIntent(AttributionActivity.this));
            }
        });
        resetAnimationsButton = (Button) findViewById(R.id.reset_animations);
        resetAnimationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimatedWtaLogoFragment logoFragment = (AnimatedWtaLogoFragment)
                        getSupportFragmentManager().findFragmentById(R.id.animated_logo_fragment);
                logoFragment.reset();
                logoFragment.start();
            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                AnimatedWtaLogoFragment logoFragment = (AnimatedWtaLogoFragment)
                        getSupportFragmentManager().findFragmentById(R.id.animated_logo_fragment);
                logoFragment.start();
            }
        }, 1000);
    }
}
