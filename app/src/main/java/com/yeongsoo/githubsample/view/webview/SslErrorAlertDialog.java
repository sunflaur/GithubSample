package com.yeongsoo.githubsample.view.webview;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.webkit.SslErrorHandler;

/**
 * Created by yeongsookim on 2018-05-16.
 *
 * 웹뷰에서 SSL Error발생시 보여줄 Dialog (Google Guide)
 */

public class SslErrorAlertDialog {
    public static void show(Context context, final SslErrorHandler handler) {
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setMessage("인증서에 문제가 있습니다. 계속 진행 하시겠습니까?")
                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        handler.proceed();
                    }
                })
                .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        handler.cancel();
                    }
                })
                .setCancelable(false)
                .create();

        if (context instanceof Activity && !((Activity) context).isFinishing()) {
            alertDialog.show();
        }
    }
}
