package com.willowtreeapps.saguaro.android.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
import com.willowtreeapps.saguaro.android.Saguaro;

public class OpenSourceTextView extends TextView {

    public OpenSourceTextView(Context context) {
        this(context, null);
    }

    public OpenSourceTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OpenSourceTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        setText(Saguaro.getOpenSourceText(getContext(), getResources()));
    }
}
