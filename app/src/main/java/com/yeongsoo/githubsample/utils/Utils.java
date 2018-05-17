package com.yeongsoo.githubsample.utils;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by yeongsookim on 2018-05-15.
 */

public class Utils {
    /**
     * Context가 Activity Context이거나 혹은 갖고 있는지 판별
     * @param context
     * @return
     */
    public static Activity getActivity(Context context) {
        if (context == null) {
            return null;
        }

        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }

    /**
     * 소프트 키보드를 숨긴다.
     *
     * @param activity
     */
    public static void hideKeyboard(Activity activity) {
        hideKeyboard(activity, activity.getCurrentFocus());
    }

    public static void hideKeyboard(Activity activity, View focusedView) {
        if (focusedView != null) {
            InputMethodManager imm
                    = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);

            if (imm != null) {
                imm.hideSoftInputFromWindow(focusedView.getWindowToken(), 0);
            }
        }
    }
}
