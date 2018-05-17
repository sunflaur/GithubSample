package com.yeongsoo.githubsample.presenter;

import android.app.Fragment;

import com.yeongsoo.githubsample.model.api.GetSearchUserListApi;
import com.yeongsoo.githubsample.model.api.OnResponseListener;
import com.yeongsoo.githubsample.model.data.SearchUserContents;
import com.yeongsoo.githubsample.model.data.SearchUserItemContents;
import com.yeongsoo.githubsample.model.local.GetFavoriteUserData;
import com.yeongsoo.githubsample.model.local.GithubFavoriteDbHelper;
import com.yeongsoo.githubsample.model.local.OnQueryResultListener;
import com.yeongsoo.githubsample.model.local.SetFavoriteUserData;
import com.yeongsoo.githubsample.presenter.data.RequestItem;
import com.yeongsoo.githubsample.presenter.data.RequestType;
import com.yeongsoo.githubsample.utils.CollectionUtils;
import com.yeongsoo.githubsample.view.OnUpdateItemListener;

import java.lang.ref.WeakReference;

import static com.yeongsoo.githubsample.presenter.data.RequestType.QUERY_FAVORITE;
import static com.yeongsoo.githubsample.presenter.data.RequestType.QUERY_FAVORITE_NEXT;
import static com.yeongsoo.githubsample.presenter.data.RequestType.SEARCH_USER;
import static com.yeongsoo.githubsample.presenter.data.RequestType.SEARCH_USER_NEXT;

/**
 * Created by yeongsookim on 2018-05-13.
 *
 * Api 또는 DB(Model)와 View간에 데이터 및 비지니스 로직을 담당하는 Presenter
 * GitHub사용자 List만을 처라하기 때문에 구조적으로 고도화는 적용하지 않음.
 */

public class UserListPresenter implements OnRequestItem {
    private OnUpdateItemListener<RequestItem> mUpdateListener = null;
    private WeakReference<Fragment> mFragmentRef = null;
    private GetSearchUserListApi mSearchUserApi = null;
    private GithubFavoriteDbHelper mDbHelper = null;
    private GetFavoriteUserData mGetDataModel = null;
    private SetFavoriteUserData mSetDataModel = null;

    public UserListPresenter(OnUpdateItemListener<RequestItem> listener) {
        mUpdateListener = listener;

        if (mUpdateListener instanceof Fragment) {
            mFragmentRef = new WeakReference<>((Fragment) mUpdateListener);
        }

        mSearchUserApi = new GetSearchUserListApi();
        mDbHelper = GithubFavoriteDbHelper.getInstance(mFragmentRef.get().getActivity(), null);
        mGetDataModel = new GetFavoriteUserData(mDbHelper);
        mSetDataModel = new SetFavoriteUserData(mDbHelper);

        mSearchUserApi.setResponseListener(new OnResponseListener<SearchUserContents>() {
            @Override
            public void onResponse(SearchUserContents obj) {
                RequestType mRequestType = SEARCH_USER;
                if (!CollectionUtils.isEmpty(obj.getItemList())) {
                    for (SearchUserItemContents item : obj.getItemList()) {
                        item.setSelectedItem(mGetDataModel.hasInsertedData(item.getLoginId()));
                    }
                }

                if (mSearchUserApi.getPage() > 1) {
                    mRequestType = SEARCH_USER_NEXT;
                }

                mUpdateListener.onUpdateItem(new RequestItem(mRequestType, obj));
            }

            @Override
            public void onFail(Object obj) {

            }
        });

        mGetDataModel.setListener(new OnQueryResultListener<SearchUserContents>() {
            @Override
            public void onResult(SearchUserContents obj) {
                RequestType mRequestType = QUERY_FAVORITE;

                if (mGetDataModel.getmBaseIndex() > 0) {
                    mRequestType = QUERY_FAVORITE_NEXT;
                }

                mUpdateListener.onUpdateItem(new RequestItem(mRequestType, obj));
            }

            @Override
            public void onFail(Object obj) {

            }
        });
    }

    @Override
    public void onRequestItem(RequestItem item) {
        switch (item.getType()) {
            case SEARCH_USER:
                if (item.getObject() instanceof String) {
                    mSearchUserApi.setSearchKeyword((String) item.getObject())
                            .setPage(1);
                    mSearchUserApi.send();
                }
                break;

            case SEARCH_USER_NEXT:
                if (mSearchUserApi != null) {
                    mSearchUserApi.setNextPage()
                            .send();
                }

                break;

            case QUERY_FAVORITE:
                if (item.getObject() instanceof String) {
                    mGetDataModel.requestData((String) item.getObject());
                }
                break;

            case QUERY_FAVORITE_NEXT:
                mGetDataModel.requestNextData();
                break;

            case ADD_FAVORITE:
                if (item.getObject() instanceof SearchUserItemContents) {
                    mSetDataModel.addData((SearchUserItemContents) item.getObject());
                }
                break;

            case DELETE_FAVORITE:
                if (item.getObject() instanceof SearchUserItemContents) {
                    mSetDataModel.deleteData((SearchUserItemContents) item.getObject());
                }
                break;

        }
    }

    public void finish() {
        mDbHelper.finish();
    }
}
