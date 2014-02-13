package com.willowtreeapps.saguaro.android.widget;

import com.willowtreeapps.saguaro.android.R;
import com.willowtreeapps.saguaro.android.Saguaro;

import android.content.Context;
import android.text.SpannableString;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

/**
 * User: derek Date: 2/13/14 Time: 4:09 PM
 */
public class AcknowledgmentsTextView extends TextView {

    public AcknowledgmentsTextView(Context context) {
        this(context, null);
    }

    public AcknowledgmentsTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AcknowledgmentsTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private void refreshAcknowledgmentsText() {
        SpannableString openSourceLink = Saguaro.makeLinkSpan(getContext().getString(R.string.acknowledgments),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Saguaro.showOpenSourceDialog(getContext());
                    }
                });
        setText(openSourceLink);
        setFocusable(false);
        Saguaro.makeLinksFocusable(this);
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();

        refreshAcknowledgmentsText();
    }
}
