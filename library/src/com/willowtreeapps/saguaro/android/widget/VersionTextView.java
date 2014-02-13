package com.willowtreeapps.saguaro.android.widget;

import com.willowtreeapps.saguaro.android.R;
import com.willowtreeapps.saguaro.android.Saguaro;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * User: derek Date: 2/13/14 Time: 4:09 PM
 */
public class VersionTextView extends TextView {

    private boolean mIsFullVersionText = true;

    public VersionTextView(Context context) {
        this(context, null);
    }

    public VersionTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VersionTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.VersionTextView);
        mIsFullVersionText = a.getBoolean(R.styleable.VersionTextView_isFullVersionText, true);
        a.recycle();
    }

    public boolean getIsFullVersionText() {
        return mIsFullVersionText;
    }

    public void setIsFullVersionText(boolean isFullVersionText) {
        mIsFullVersionText = isFullVersionText;
        refreshVersionText();
    }

    private void refreshVersionText() {
        if (mIsFullVersionText) {
            setText(Saguaro.getFullVersionString(getContext()));
        } else {
            setText(Saguaro.getMinVersionString(getContext()));
        }
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();

        refreshVersionText();
    }
}
