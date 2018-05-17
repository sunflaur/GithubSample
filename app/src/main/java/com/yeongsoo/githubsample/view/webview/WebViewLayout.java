package com.yeongsoo.githubsample.view.webview;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.io.File;

/**
 * Created by yeongsookim on 2018-05-16.
 *
 * 웹뷰에 대한 기본 설정을 처리기능을 포함한 WebView를 확장한 클래스
 */

public class WebViewLayout extends WebView {
    private DefaultWebChromeClient mChromeClient = null;
    private String mUrlToLoad = null;

    public WebViewLayout(Context context) {
        super(context.getApplicationContext());
    }

    public WebViewLayout(Context context, AttributeSet attrs) {
        super(context.getApplicationContext(), attrs);
    }

    public WebViewLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onAttachedToWindow() {
        initialize();

        super.onAttachedToWindow();
    }

    @Override
    public void loadUrl(String url) {
        if (mChromeClient != null) {
            super.loadUrl(url);
            mUrlToLoad = null;
        } else {
            mUrlToLoad = url;
        }
    }

    private void initialize() {
        setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);

        mChromeClient = new DefaultWebChromeClient();
        setWebChromeClient(mChromeClient);
        setWebViewClient(new DefaultWebViewClient());

        WebSettings settings;
        if ((settings = getSettings()) != null) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
                settings.setSavePassword(false);
            }

            settings.setDisplayZoomControls(true);
            settings.setJavaScriptEnabled(true);
            settings.setJavaScriptCanOpenWindowsAutomatically(true);
            settings.setSupportMultipleWindows(false);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            }

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR1) {
                settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
            }

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                // older android version, disable hardware acceleration
                setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            } else {
                // chromium, enable hardware acceleration
                setLayerType(View.LAYER_TYPE_HARDWARE, null);
            }


            // enable html5 app cache start
            settings.setDomStorageEnabled(true);
            settings.setDatabaseEnabled(true);
            File dir = getContext().getCacheDir();
            if (!dir.exists()) {
                dir.mkdirs();
            }
            settings.setAppCachePath(dir.getPath());
            settings.setAllowFileAccess(true);
            settings.setAppCacheEnabled(true);
            settings.setCacheMode(WebSettings.LOAD_DEFAULT);


            //geolocation
            settings.setGeolocationEnabled(true);

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                settings.setGeolocationDatabasePath(getContext().getFilesDir().getPath());
            }
        }

        if (mUrlToLoad != null) {
            StringBuilder urlBuilder = new StringBuilder(mUrlToLoad);
            //mUrlToLoad에 따라 최근 url이 로드 안 될수도?
            loadUrl(urlBuilder.toString());
            mUrlToLoad = null;
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void loadJavascript(String javascript, ValueCallback<String> resultCallback) {
        if (!TextUtils.isEmpty(javascript)) {
            evaluateJavascript(javascript, resultCallback);
        }
    }

    public void loadJavascript(String javascript) {
        if (!TextUtils.isEmpty(javascript)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                loadJavascript(javascript, null);
            } else {
                loadUrl("javascript:" + javascript);
            }
        }
    }
}
