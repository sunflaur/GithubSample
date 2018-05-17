package com.yeongsoo.githubsample.model.local;

import android.text.TextUtils;

import com.yeongsoo.githubsample.model.data.SearchUserContents;
import com.yeongsoo.githubsample.model.data.SearchUserItemContents;
import com.yeongsoo.githubsample.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by yeongsookim on 2018-05-16.
 *
 * DB Helper를 통해
 * 저장된 사용자 정보를 가져오고 이를 콜백을 통해 되돌려주는 클래스
 *
 * api에서는 score만으로 정렬되어 가져오지만
 * DB에서는 직접 Login id를 통해 정렬하여 가져오도록 지원함
 * Login id이므로 영문이고 한글등 유니코드에 대한 정렬은 구현하지 못했습니다.(시간이...)
 */

public class GetFavoriteUserData {
    private static final int COUNT_PER_REQUEST = 100;
    private GithubFavoriteDbHelper mDbHelper;
    private OnQueryResultListener<SearchUserContents> mListener;
    private int mCountPerRequest = COUNT_PER_REQUEST; //default
    private int mBaseIndex = 0;
    private String mQueryIdString;

    public GetFavoriteUserData(GithubFavoriteDbHelper helper) {
        this(helper, COUNT_PER_REQUEST);
    }

    public GetFavoriteUserData(GithubFavoriteDbHelper helper, int size) {
        mDbHelper = helper;
        mCountPerRequest = size;
    }

    public GetFavoriteUserData setListener(OnQueryResultListener<SearchUserContents> listener) {
        mListener = listener;
        return this;
    }

    private void requestData(final String queryIdString, final boolean isCompleteAccordance,
                             final int count, final int skip) {
        Observable<SearchUserContents> observable = Observable.create(
                new Observable.OnSubscribe<SearchUserContents>() {
                    @Override
                    public void call(Subscriber<? super SearchUserContents> sub) {
                        List<SearchUserItemContents> data = null;
                        List<FavoriteUserItemVo> result = null;

                        try {
                            result = mDbHelper.queryData(queryIdString, isCompleteAccordance, count, skip);
                        } catch (Exception e) {
                            sub.onError(e);
                        }

                        if (CollectionUtils.isEmpty(result)) {
                            sub.onError(new Throwable());
                            sub.unsubscribe();
                        } else {
                            String initialConsonant = "";

                            data = new ArrayList<>();
                            for (FavoriteUserItemVo vo : result) {
                                //db에 저장된 것이므로 선택된 상태로 한다.
                                vo.setSelectedItem(true);

                                if (!TextUtils.equals(initialConsonant, String.valueOf(vo.getLoginId().charAt(0)))) {
                                    initialConsonant = String.valueOf(vo.getLoginId().charAt(0));
                                    vo.setInitialConsonant(initialConsonant);
                                }

                                data.add(vo);
                            }

                            SearchUserContents contents = new SearchUserContents();
                            contents.setItemList(data);
                            contents.setIsEndOfList((mBaseIndex + 1) * mCountPerRequest >=
                                    mDbHelper.getConditionalCount(queryIdString, isCompleteAccordance));
                            sub.onNext(contents);
                            sub.onCompleted();
                        }
                    }
                }
        ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        observable.subscribe(new Subscriber<SearchUserContents>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                mListener.onFail(e);
            }

            @Override
            public void onNext(SearchUserContents data) {
                if (mListener != null) {
                    mListener.onResult(data);
                }
            }
        });
    }

    public boolean hasInsertedData(String loginId) {
        boolean hasData = false;

        try {

            List<FavoriteUserItemVo> result
                    = mDbHelper.queryData(loginId, true, COUNT_PER_REQUEST, 0);
            if (!CollectionUtils.isEmpty(result)) {
                hasData = true;
            }
        } catch (Exception e) {

        }

        return hasData;
    }

    public int getmBaseIndex() {
        return mBaseIndex;
    }

    public void requestData(String queryIdString) {
        mBaseIndex = 0;
        mQueryIdString = queryIdString;
        requestData(queryIdString,  false, mCountPerRequest,mCountPerRequest * mBaseIndex);
    }
    public void requestNextData() {
        mBaseIndex++;
        requestData(mQueryIdString, false, mCountPerRequest, mCountPerRequest * mBaseIndex);
    }
}
