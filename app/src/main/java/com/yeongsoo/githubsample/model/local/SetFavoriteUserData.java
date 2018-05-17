package com.yeongsoo.githubsample.model.local;

import com.yeongsoo.githubsample.model.data.SearchUserItemContents;

/**
 * Created by yeongsookim on 2018-05-16.
 *
 * GitHub사용자 데이터를 저장 및 삭제를 하는 클래스 (현 기준에서 수정은 필요 없다)
 */

public class SetFavoriteUserData {
    private GithubFavoriteDbHelper mDbHelper;

    public SetFavoriteUserData(GithubFavoriteDbHelper helper) {
        mDbHelper = helper;
    }

    public void addData(SearchUserItemContents data) {
        if (mDbHelper != null) {
            mDbHelper.insertData(data);
        }
    }

    public void deleteData(SearchUserItemContents data) {
        if (mDbHelper != null) {
            mDbHelper.deleteData(data.getLoginId());
        }
    }
}
