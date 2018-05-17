package com.yeongsoo.githubsample.view.webview;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.yeongsoo.githubsample.utils.Utils;


/**
 * Created by yeongsookim on 2018-05-16.
 *
 * 웹뷰에서 Alert과 관련하여 재정의 된 ChromeClient
 */

public class DefaultWebChromeClient extends WebChromeClient {

    @Override
    public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
        Activity activity = Utils.getActivity(view.getContext());
        if (activity == null) {
            return super.onJsAlert(view, url, message, result);
        }

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
        dialogBuilder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, new AlertDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.confirm();
                    }
                })
                .create()
                .show();

        return true;
    }

    @Override
    public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
        Activity activity = Utils.getActivity(view.getContext());
        if (activity == null) {
            return super.onJsConfirm(view, url, message, result);
        }

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
        dialogBuilder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, new AlertDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.confirm();
                    }
                })
                .setNegativeButton(android.R.string.cancel, new AlertDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.cancel();
                    }
                })
                .create()
                .show();

        return true;
    }
}
