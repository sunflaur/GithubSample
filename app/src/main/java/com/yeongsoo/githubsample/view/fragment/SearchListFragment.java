package com.yeongsoo.githubsample.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.yeongsoo.githubsample.model.data.SearchUserContents;
import com.yeongsoo.githubsample.model.data.SearchUserItemContents;
import com.yeongsoo.githubsample.presenter.UserListPresenter;
import com.yeongsoo.githubsample.presenter.data.RequestItem;
import com.yeongsoo.githubsample.utils.CollectionUtils;
import com.yeongsoo.githubsample.utils.KeyConstants;
import com.yeongsoo.githubsample.utils.Utils;
import com.yeongsoo.githubsample.view.OnUpdateItemListener;
import com.yeongsoo.githubsample.view.recyclerview.GithubListItemAdapter;
import com.yeongsoo.githubsample.view.recyclerview.OnNextPageCallListener;
import com.yeongsoo.githubsample.view.recyclerview.OnNextPageTriggerListener;
import com.yeongsoo.githubsample.view.recyclerview.OnTouchViewHolder;

import static com.yeongsoo.githubsample.presenter.data.RequestType.ADD_FAVORITE;
import static com.yeongsoo.githubsample.presenter.data.RequestType.DELETE_FAVORITE;
import static com.yeongsoo.githubsample.presenter.data.RequestType.QUERY_FAVORITE;
import static com.yeongsoo.githubsample.presenter.data.RequestType.QUERY_FAVORITE_NEXT;
import static com.yeongsoo.githubsample.presenter.data.RequestType.SEARCH_USER;
import static com.yeongsoo.githubsample.presenter.data.RequestType.SEARCH_USER_NEXT;
import static com.yeongsoo.githubsample.utils.KeyConstants.EXTRA_FRAGMENT_TYPE;
import static com.yeongsoo.githubsample.utils.KeyConstants.EXTRA_KEYWORD;

/**
 * Created by yeongsookim on 2018-05-14.
 *
 * Github사용자 목록을 보여주고
 * 사용자 홈 화면을 웹뷰를 통해 보여주고
 * 즐겨찾기 기능을 제공하는 Fragment
 *
 * 타입별로 Api 또는 Local(DB)로 정해지며 이는 각각 데이터를 가지고 올 대상을 의미
 */

public class SearchListFragment extends DefaultFragment
        implements SwipeRefreshLayout.OnRefreshListener, OnUpdateItemListener<RequestItem>,
        OnTouchViewHolder, OnNextPageCallListener {
    public static final String TAG = SearchListFragment.class.getSimpleName();

    public Type mListType = Type.ONLINE;
    private ImageView mSearchButton = null;
    private EditText mSearchText = null;
    private SwipeRefreshLayout mRefreshLayout = null;
    private RecyclerView mRecyclerView = null;
    private RecyclerView.LayoutManager mLayoutManager = null;
    private GithubListItemAdapter mRecyclerViewAdapter = null;

    private UserListPresenter mPresenter = null;
    private int mPage = 0;
    private boolean mIsEndOfList = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle.keySet().contains(EXTRA_FRAGMENT_TYPE)) {
            mListType = (Type) bundle.get(EXTRA_FRAGMENT_TYPE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mPresenter = new UserListPresenter(this);

        View view = inflater.inflate(R.layout.search_listfragment, container, false);

        String keyword = getArguments().getString(EXTRA_KEYWORD, "");

        mSearchButton = view.findViewById(R.id.search);
        mSearchText = view.findViewById(R.id.searchKeyword);
        mRefreshLayout = view.findViewById(R.id.list_refresh_layout);
        mRecyclerView = view.findViewById(R.id.itemList);

        mRefreshLayout.setOnRefreshListener(this);
        mRecyclerView.addOnScrollListener(new OnNextPageTriggerListener(this));

        mRecyclerViewAdapter = new GithubListItemAdapter(this);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);

        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSearchText == null) {
                    return;
                }

                Utils.hideKeyboard(getActivity(), v);
                requestSearchList(mSearchText.getText().toString(), false);
            }
        });

        mSearchText.setText(keyword);
        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_SEARCH:
                        Utils.hideKeyboard(getActivity(), v);
                        requestSearchList(mSearchText.getText().toString(), false);
                        return true;
                }

                return false;
            }
        });

        if (!TextUtils.isEmpty(keyword)) {
            requestSearchList(keyword, false);
        }

        return view;
    }

    @Override
    public void onDestroyView() {
        mPresenter.finish();
        super.onDestroyView();
    }

    @Override
    public void onRefresh() {
        requestSearchList(mSearchText.getText().toString(), false);
    }

    @Override
    public void onNext() {
        requestSearchList(mSearchText.getText().toString(), true);
    }

    @Override
    public boolean hasNextPage() {
        return !mIsEndOfList;
    }

    @Override
    public int getCurrentPage() {
        return mPage;
    }

    @Override
    public void onUpdateItem(RequestItem item) {
        if (item == null) {
            return;
        }

        switch (item.getType()) {
            case SEARCH_USER:
                mRefreshLayout.setRefreshing(false);
            case SEARCH_USER_NEXT:
                if (item.getObject() instanceof SearchUserContents) {
                    SearchUserContents contents = (SearchUserContents) item.getObject();
                    mIsEndOfList = contents.isEndOfList();
                    addItems((SearchUserContents) item.getObject());
                }
                break;

            case QUERY_FAVORITE:
                mRefreshLayout.setRefreshing(false);
            case QUERY_FAVORITE_NEXT:
                if (item.getObject() instanceof SearchUserContents) {
                    SearchUserContents contents = (SearchUserContents) item.getObject();
                    mIsEndOfList = contents.isEndOfList();
                    addItems((SearchUserContents) item.getObject());
                }
                break;
        }
    }

    @Override
    public String getTagName() {
        return TAG;
    }

    @Override
    public void onTouchHolder(View view, Object item) {
        if (!(item instanceof SearchUserItemContents)) {
            return;
        }

        SearchUserItemContents selectedItem = (SearchUserItemContents) item;

        if (view instanceof ImageView) {
            if (view.isSelected()) {
                requestRemoveFavoriteList(selectedItem);
            } else {
                requestAddFavoriteList(selectedItem);
            }

            view.setSelected(!view.isSelected());
        } else if (view instanceof ViewGroup) {
            Bundle bundle = new Bundle();
            bundle.putString(KeyConstants.EXTRA_WEB_URL, selectedItem.getUserHomeUrl());
            switchFragment(new HomeWebViewFragment(), HomeWebViewFragment.TAG, bundle, false);
        }
    }

    private void requestSearchList(String keyword, boolean next) {
        switch (mListType) {
            case ONLINE:
                if (!TextUtils.isEmpty(keyword)) {
                    if (next) {
                        mPage++;
                        mPresenter.onRequestItem(new RequestItem(SEARCH_USER_NEXT, keyword));
                    } else {
                        mPresenter.onRequestItem(new RequestItem(SEARCH_USER, keyword));
                        clearItems();
                    }

                }
                break;

            case LOCAL:
                if (keyword != null) {
                    if (next) {
                        mPage++;
                        mPresenter.onRequestItem(new RequestItem(QUERY_FAVORITE_NEXT, keyword));
                    } else {
                        mPresenter.onRequestItem(new RequestItem(QUERY_FAVORITE, keyword));
                        clearItems();
                    }
                }
                break;
        }
    }

    //예외처리인 동기화를 통한 db추가 성공여부 이번에 구현하지 않는다
    private void requestAddFavoriteList(SearchUserItemContents item) {
        if (item != null) {
            switch (mListType) {
                case ONLINE:
                    mPresenter.onRequestItem(new RequestItem(ADD_FAVORITE, item));
                    break;
            }
        }
    }

    //예외처리인 동기화를 통한 db추가 성공여부 이번에 구현하지 않는다
    private void requestRemoveFavoriteList(SearchUserItemContents item) {
        if (item != null) {
            switch (mListType) {
                case ONLINE:
                case LOCAL:
                    mPresenter.onRequestItem(new RequestItem(DELETE_FAVORITE, item));
                    break;
            }
        }
    }

    private void addItems(SearchUserContents contents) {
        if (CollectionUtils.isEmpty(contents.getItemList())) {
            return;
        }

        mRecyclerViewAdapter.addItemList(contents.getItemList());
        mRecyclerViewAdapter.notifyDataChanged();
    }

    private void clearItems() {
        mPage = 0;
        mRecyclerViewAdapter.clearList();
        mRecyclerViewAdapter.notifyDataChanged();
    }


    public enum Type {
        ONLINE,
        LOCAL
    }
}
