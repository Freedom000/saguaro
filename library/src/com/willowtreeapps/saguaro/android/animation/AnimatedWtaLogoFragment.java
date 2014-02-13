/*
 * Copyright 2014 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.willowtreeapps.saguaro.android.animation;

import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;
import com.willowtreeapps.saguaro.android.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;

public class AnimatedWtaLogoFragment extends Fragment {

    private View mRootView;
    private Runnable mOnFillStartedCallback;
    private View mSubtitleView;
    private AnimatedWtaLogoView mLogoView;
    private float mInitialLogoOffset;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mInitialLogoOffset = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 172,
                getResources().getDisplayMetrics());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_animated_wta_logo, container, false);
        mSubtitleView = mRootView.findViewById(R.id.logo_subtitle);

        mLogoView = (AnimatedWtaLogoView) mRootView.findViewById(R.id.animated_logo);
        mLogoView.setOnStateChangeListener(new AnimatedWtaLogoView.OnStateChangeListener() {
            @Override
            public void onStateChange(int state) {
                if (state == AnimatedWtaLogoView.STATE_FILL_STARTED) {
                    ViewHelper.setAlpha(mSubtitleView, 0);
                    mSubtitleView.setVisibility(View.VISIBLE);
                    //mSubtitleView.setTranslationX(-mSubtitleView.getWidth());

                    // Bug in older versions where set.setInterpolator didn't work
                    AnimatorSet set = new AnimatorSet();
                    Interpolator interpolator = new AccelerateInterpolator();
                    ObjectAnimator a1 = ObjectAnimator.ofFloat(mLogoView, "translationX", 0);
                    ObjectAnimator a2 = ObjectAnimator.ofFloat(mSubtitleView, "translationX", 0);
                    ObjectAnimator a3 = ObjectAnimator.ofFloat(mSubtitleView, "alpha", 1);
                    a1.setInterpolator(interpolator);
                    a2.setInterpolator(interpolator);
                    set.setDuration(500).playTogether(a1, a2, a3);
                    set.start();

                    if (mOnFillStartedCallback != null) {
                        mOnFillStartedCallback.run();
                    }
                }
            }
        });
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        reset();
    }

    public void start() {
        mLogoView.start();
    }

    public void setOnFillStartedCallback(Runnable fillStartedCallback) {
        mOnFillStartedCallback = fillStartedCallback;
    }

    public void reset() {
        mLogoView.reset();
        ViewHelper.setTranslationX(mLogoView, mInitialLogoOffset / 2);
        mSubtitleView.setVisibility(View.INVISIBLE);
    }
}
