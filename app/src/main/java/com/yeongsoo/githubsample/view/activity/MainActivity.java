package com.yeongsoo.githubsample.view.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.yeongsoo.githubsample.R;
import com.yeongsoo.githubsample.view.fragment.DefaultFragment;

/**
 * Created by yeongsookim on 2018-05-12.
 */

public class MainActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getFragmentManager().findFragmentById(R.id.main_content);

        if (fragment instanceof DefaultFragment) {
            if (((DefaultFragment) fragment).onBackPressed()) {
                return;
            }
        }

        super.onBackPressed();
    }

    public void switchFragment(Fragment fragment, String tag, Bundle arg, boolean rootStack) {
        if (fragment == null) {
            return;
        }

        Fragment stackedFragment = null;
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if ((stackedFragment = fragmentManager.findFragmentByTag(tag)) != null) {
            stackedFragment.setArguments(arg);
            fragmentTransaction.replace(R.id.main_content, stackedFragment, tag);
        } else {
            //mFragmentList.add(fragment);
            fragment.setArguments(arg);
            fragmentTransaction.add(R.id.main_content, fragment, tag);
        }

        if (!rootStack) {
            fragmentTransaction.addToBackStack(null);
        }

        fragmentTransaction.commit();
    }
}
