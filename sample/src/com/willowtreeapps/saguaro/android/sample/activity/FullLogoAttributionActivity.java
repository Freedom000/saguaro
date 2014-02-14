package com.willowtreeapps.saguaro.android.sample.activity;

import com.willowtreeapps.saguaro.android.Saguaro;
import com.willowtreeapps.saguaro.android.animation.AnimatedWtaLogoView;
import com.willowtreeapps.saguaro.android.animation.WtaLogoPaths;
import com.willowtreeapps.saguaro.android.sample.R;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

/**
 * User: derek Date: 2/13/14 Time: 3:34 PM
 */
public class FullLogoAttributionActivity extends BaseSampleActivity {

    private Handler mHandler = new Handler();

    private Button attributionButton;
    private Button resetAnimationsButton;
    private AnimatedWtaLogoView fullLogo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_logo_attribution);

        attributionButton = (Button) findViewById(R.id.button_attribution);
        attributionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(Saguaro.getAttributionIntent(FullLogoAttributionActivity.this));
            }
        });
        resetAnimationsButton = (Button) findViewById(R.id.reset_animations);
        resetAnimationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fullLogo.reset();
                fullLogo.start();
            }
        });
        fullLogo = (AnimatedWtaLogoView) findViewById(R.id.full_logo);
        fullLogo.setViewportSize(1319, 382);
        fullLogo.setGlyphStrings(WtaLogoPaths.FULL_LOGO_GLYPHS);
        fullLogo.setFillPaints(new int[]{136, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65},
                new int[]{194, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65},
                new int[]{200, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65});
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                fullLogo.start();
            }
        }, 1000);
    }
}
