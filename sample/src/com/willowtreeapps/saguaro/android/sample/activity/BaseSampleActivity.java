package com.willowtreeapps.saguaro.android.sample.activity;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.willowtreeapps.saguaro.android.Saguaro;
import com.willowtreeapps.saguaro.android.sample.R;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

public class BaseSampleActivity extends SherlockFragmentActivity {

    protected RelativeLayout fullLayout;
    protected FrameLayout actContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void setContentView(int layoutResId) {
        fullLayout = (RelativeLayout) getLayoutInflater().inflate(R.layout.activity_base_sample, null);
        actContent = (FrameLayout) fullLayout.findViewById(R.id.content);
        getLayoutInflater().inflate(layoutResId, actContent, true);
        View attribution = fullLayout.findViewById(R.id.attribution);
        attribution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(Saguaro.getAttributionIntent(BaseSampleActivity.this));
            }
        });
        super.setContentView(fullLayout);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return true;
    }
}
