package com.yeongsoo.githubsample.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.yeongsoo.githubsample.R;
import com.yeongsoo.githubsample.utils.KeyConstants;
import com.yeongsoo.githubsample.utils.Utils;

/**
 * Created by yeongsookim on 2018-05-12.
 *
 * 사용자 검색을 받아 바로 검색을 시작하여 리스트여 보여주는 것을 유도하도록
 * 첫 화면을 구성하는 Fragment
 */

public class IntroFragment extends DefaultFragment {
    public static final String TAG = IntroFragment.class.getSimpleName();

    private ImageView mSearchButton = null;
    private EditText mSearchText = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.intro_fragment, container, false);

        mSearchButton = view.findViewById(R.id.search);
        mSearchText = view.findViewById(R.id.searchKeyword);

        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSearchText == null) {
                    return;
                }

                Utils.hideKeyboard(getActivity(), v);
                requestSearch(mSearchText.getText().toString());
            }
        });

        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_SEARCH:
                        Utils.hideKeyboard(getActivity(), v);
                        requestSearch(mSearchText.getText().toString());
                        return true;
                }

                return false;
            }
        });

        return view;
    }

    @Override
    public String getTagName() {
        return TAG;
    }

    private void requestSearch(String keyword) {
        if (!TextUtils.isEmpty(keyword)) {
            Bundle bundle = new Bundle();
            bundle.putString(KeyConstants.EXTRA_KEYWORD, keyword);

            switchFragment(new SearchMainFragment(), SearchMainFragment.TAG, bundle, false);
        }
    }
}
