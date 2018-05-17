package com.yeongsoo.githubsample.view;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.yeongsoo.githubsample.utils.CollectionUtils;
import com.yeongsoo.githubsample.view.fragment.DefaultFragment;
import com.yeongsoo.githubsample.view.fragment.SearchListFragment;

import java.util.ArrayList;
import java.util.List;

import static com.yeongsoo.githubsample.utils.KeyConstants.EXTRA_FRAGMENT_TYPE;

/**
 * Created by yeongsookim on 2018-05-14.
 *
 * 메인화면서 ViewPager에 Fragment를 사용할 수 있도록 구현한 클래스
 * 원래 support v4에도 있지만, 거기에서 사용하는 Fragment는 android.app.Fragment가 아닌
 * support v4에 포함된 Fragment만을 지원하고 있어서
 *
 * 이 앱에서 사용하고자 하는 기능에 v4 FragmentPagerAdapter에 구현된 기능을 합쳐서 구현.
 */

public class FragmentPagerAdapter extends PagerAdapter {
    private List<DefaultFragment> mFragmentList = new ArrayList<>();
    private List<String> mTitleList = new ArrayList<>();

    private final FragmentManager mFragmentManager;
    private FragmentTransaction mCurTransaction = null;
    private Fragment mCurrentPrimaryItem = null;

    public FragmentPagerAdapter(FragmentManager manager) {
        mFragmentManager = manager;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if (mCurTransaction == null) {
            mCurTransaction = mFragmentManager.beginTransaction();
        }

        DefaultFragment fragment = getItem(position);

        Fragment FragmentFromManager = mFragmentManager.findFragmentByTag(fragment.getTag());

        if (FragmentFromManager != null) {
            mCurTransaction.attach(FragmentFromManager);
        } else {
            mCurTransaction.add(container.getId(), fragment, fragment.getTag());
        }

        FragmentFromManager = fragment;

        if (FragmentFromManager != mCurrentPrimaryItem) {
            FragmentFromManager.setMenuVisibility(false);
            FragmentFromManager.setUserVisibleHint(false);
        }

        return FragmentFromManager;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (mCurTransaction == null) {
            mCurTransaction = mFragmentManager.beginTransaction();
        }
        mCurTransaction.detach((Fragment) object);
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        if (!(object instanceof Fragment)) {
            return;
        }

        Fragment fragment = (Fragment) object;

        if (fragment != mCurrentPrimaryItem) {
            if (mCurrentPrimaryItem != null) {
                mCurrentPrimaryItem.setMenuVisibility(false);
                mCurrentPrimaryItem.setUserVisibleHint(false);
            }

            fragment.setMenuVisibility(true);
            fragment.setUserVisibleHint(true);
            mCurrentPrimaryItem = fragment;
        }
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        if (mCurTransaction != null) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                mCurTransaction.commitAllowingStateLoss();
            } else {
                mCurTransaction.commitNowAllowingStateLoss();
            }

            mCurTransaction = null;
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return object instanceof Fragment && ((Fragment) object).getView() == view;
    }


    public DefaultFragment getItem(int position) {
        if (CollectionUtils.size(mFragmentList) > position) {
            return mFragmentList.get(position);
        }

        return null;
    }

    @Override
    public int getCount() {
        return CollectionUtils.size(mFragmentList);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (CollectionUtils.size(mFragmentList) > position) {
            return mTitleList.get(position);
        }

        return super.getPageTitle(position);
    }

    public void addListFragment(DefaultFragment fragment) {
        addListFragment(fragment, null);
    }

    public void addListFragment(DefaultFragment fragment, Bundle bundle) {
        if (mFragmentList == null) {
            mFragmentList = new ArrayList<>();
        }

        if (mTitleList == null) {
            mTitleList = new ArrayList<>();
        }

        SearchListFragment.Type type = SearchListFragment.Type.ONLINE;
        if (bundle != null && bundle.keySet().contains(EXTRA_FRAGMENT_TYPE)) {
            type = (SearchListFragment.Type) bundle.get(EXTRA_FRAGMENT_TYPE);
        }
        switch (type) {
            case LOCAL:
                mTitleList.add("로컬");
                break;
            default:
                mTitleList.add("API");
        }

        if (bundle != null) {
            fragment.setArguments(bundle);
        }

        mFragmentList.add(fragment);
    }
}
