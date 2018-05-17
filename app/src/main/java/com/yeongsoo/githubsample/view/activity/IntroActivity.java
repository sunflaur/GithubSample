package com.yeongsoo.githubsample.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.yeongsoo.githubsample.view.fragment.IntroFragment;

/**
 * Created by yeongsookim on 2018-05-12.
 */

public class IntroActivity extends MainActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        switchFragment(new IntroFragment(), IntroFragment.TAG, null, true);
    }
}
