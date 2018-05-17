package com.yeongsoo.githubsample.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.yeongsoo.githubsample.R;
import com.yeongsoo.githubsample.utils.KeyConstants;

/**
 * Created by yeongsookim on 2018-05-16.
 *
 * Github의 사용자 홈을
 * 웹뷰에서 보여주기 위한 WebView를 지원하는 Fragment
 */


public class HomeWebViewFragment extends DefaultFragment {
    public static final String TAG = HomeWebViewFragment.class.getSimpleName();
    private WebView mWebView = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.only_webview, container, false);

        mWebView = view.findViewById(R.id.webContents);

        return view != null? view : super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();

        Bundle arg = getArguments();
        String url = arg.getString(KeyConstants.EXTRA_WEB_URL, "");
        mWebView.loadUrl(url);
    }

    @Override
    public String getTagName() {
        return TAG;
    }

    @Override
    public boolean onBackPressed() {
        if (mWebView != null && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }

        return false;
    }
}
