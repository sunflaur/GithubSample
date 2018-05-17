package com.yeongsoo.githubsample.model.local;

import com.yeongsoo.githubsample.model.data.SearchUserItemContents;

/**
 * Created by yeongsookim on 2018-05-16.
 *
 * DB에 GitHub사용자 데이터를 가져올 때 사용되는 데이터 클래스
 * DB에서 가져오는 만큼 index번호가 추가 되어 있다.
 */

public class FavoriteUserItemVo extends SearchUserItemContents {
    int mDbIdx = 0;

    FavoriteUserItemVo(int index, String loginId, String avatarUrl, String userHomeUrl) {
        mDbIdx = index;
        mLoginId = loginId;
        mAvatarUrl = avatarUrl;
        mHtmlUrl = userHomeUrl;
    }

    public int getIndex() {
        return mDbIdx;
    }

    @Deprecated
    void setIndex(int index) {
        mDbIdx = index;
    }

    @Deprecated
    void setLoginId(String loginId) {
        mLoginId = loginId;
    }

    @Deprecated
    void setAvatarUrl(String url) {
        mAvatarUrl = url;
    }

    @Deprecated
    void setUserHomeUrl(String url) {
        mHtmlUrl = url;
    }
}
