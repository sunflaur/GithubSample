package com.yeongsoo.githubsample.view.fragment;

import android.app.Fragment;
import android.os.Bundle;

import com.yeongsoo.githubsample.view.activity.MainActivity;

/**
 * Created by yeongsookim on 2018-05-13.
 *
 * Fragment의 onBackPressed()와 Fragment전환 등을 처리할 수 있게 하는 확장된 Fragment
 * onBackPressed()이벤트는 Activity에서 얻을 수 있으므로
 * 여기에서 처리하는 것을 Fragment로 전달하기 위해
 * onBackPressed()을 정의함
 */

public abstract class DefaultFragment extends Fragment {
    public abstract String getTagName();

    public boolean onBackPressed() {
        return false;
    }

    public final void switchFragment(Fragment fragment, String tag, Bundle arg, boolean rootStack) {
        if (getActivity() instanceof MainActivity) {
            MainActivity activity = (MainActivity) getActivity();
            activity.switchFragment(fragment, tag, arg, rootStack);
        }
    }
}
