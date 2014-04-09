package com.willowtreeapps.saguaro.android.widget;

import com.willowtreeapps.saguaro.android.R;
import com.willowtreeapps.saguaro.android.Saguaro;

import android.content.Context;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

/**
 * User: derek Date: 2/13/14 Time: 4:09 PM
 */
public class SendFeedbackTextView extends TextView {

    public SendFeedbackTextView(Context context) {
        this(context, null);
    }

    public SendFeedbackTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SendFeedbackTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private void refreshSendFeedbackText() {
        String myText = getText().toString();
        if (TextUtils.isEmpty(myText)) {
            myText = getContext().getString(R.string.saguaro__send_feedback);
        }
        SpannableString sendFeedbackLink = Saguaro.makeLinkSpan(myText, new OnClickListener() {
            @Override
            public void onClick(View v) {
                getContext().startActivity(Saguaro.getSendFeedbackIntent(getContext()));
            }
        });
        setText(sendFeedbackLink);
        setFocusable(false);
        Saguaro.makeLinksFocusable(this);
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();

        refreshSendFeedbackText();
    }
}
