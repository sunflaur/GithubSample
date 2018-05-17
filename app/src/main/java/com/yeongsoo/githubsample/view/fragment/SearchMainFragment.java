package com.yeongsoo.githubsample.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yeongsoo.githubsample.R;
import com.yeongsoo.githubsample.presenter.data.RequestItem;
import com.yeongsoo.githubsample.view.FragmentPagerAdapter;
import com.yeongsoo.githubsample.view.OnUpdateItemListener;

import static com.yeongsoo.githubsample.utils.KeyConstants.EXTRA_FRAGMENT_TYPE;

/**
 * Created by yeongsookim on 2018-05-13.
 *
 * 목록을 가진 ListFragment를 Tab과 ViewPager를 활용하여 여러개 붙여 보여줄 수 있도록 틀을 가진 Fragment
 * 현재는 2가지 Api타입과 DB타입 ListFragment만 지원한다.
 *
 * IntroFragment를 통해 받은 검색 키워드를 각 Fragment에도 전달해준다.
 *
 * @see SearchListFragment
 *
 */

public class SearchMainFragment extends DefaultFragment
        implements OnUpdateItemListener<RequestItem> {
    public static final String TAG = SearchMainFragment.class.getSimpleName();

    private TabLayout mListTabs = null;
    private ViewPager mListPager = null;
    private FragmentPagerAdapter mPagerAdapter = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_mainfragment, container, false);

        mListTabs = view.findViewById(R.id.tabs);
        mListPager = view.findViewById(R.id.list_pager);
        mPagerAdapter = new FragmentPagerAdapter(getFragmentManager());

        Bundle arg = getArguments();
        Bundle onlineType = new Bundle(arg);
        Bundle localType = new Bundle(arg);

        onlineType.putSerializable(EXTRA_FRAGMENT_TYPE, SearchListFragment.Type.ONLINE);
        localType.putSerializable(EXTRA_FRAGMENT_TYPE, SearchListFragment.Type.LOCAL);

        mPagerAdapter.addListFragment(new SearchListFragment(), onlineType);
        mPagerAdapter.addListFragment(new SearchListFragment(), localType);

        mListPager.setAdapter(mPagerAdapter);
        mListTabs.setupWithViewPager(mListPager);

        return view;
    }

    @Override
    public void onUpdateItem(RequestItem item) {

    }

    @Override
    public String getTagName() {
        return TAG;
    }
}
