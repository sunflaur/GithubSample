package com.yeongsoo.githubsample.model.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by yeongsookim on 2018-05-13.
 *
 * GitHub의 사용자 검색 api를 통하 받은 데이터에서
 * 한 사용자에 대한 데이터
 */

public class SearchUserItemContents {
    @JsonProperty("login")
    protected String mLoginId = "";

    @JsonProperty("id")
    private int mId = 0;

    @JsonProperty("avatar_url")
    protected String mAvatarUrl = "";

    @JsonProperty("gravatar_id")
    private String mGravatarId = "";

    @JsonProperty("url")
    private String mUserApiUrl = "";

    @JsonProperty("html_url")
    protected String mHtmlUrl = "";

    @JsonProperty("followers_url")
    private String mFollowersApiUrl = "";

    @JsonProperty("following_url")
    private String mFollowingApiUrl = "";

    @JsonProperty("gists_url")
    private String mGistsApiUrl = "";

    @JsonProperty("starred_url")
    private String mStarredApiUrl = "";

    @JsonProperty("subscriptions_url")
    private String mSubscriptionsApiUrl = "";

    @JsonProperty("organizations_url")
    private String mOrganizationsApiUrl = "";

    @JsonProperty("repos_url")
    private String mReposApiUrl = "";

    @JsonProperty("events_url")
    private String mEventsApiUrl = "";

    @JsonProperty("received_events_url")
    private String mReceivedEventsApiUrl = "";

    @JsonProperty("type") //TODO enum으로?? User/Organization/???
    private String mType = "";

    @JsonProperty("site_admin")
    private boolean mIsSiteAdmin = false;

    @JsonProperty("score")
    private float mScore = 0f;

    @JsonIgnore
    public String getLoginId() {
        return mLoginId;
    }

    @JsonIgnore
    public String getAvatarUrl() {
        return mAvatarUrl;
    }

    @JsonIgnore
    public String getUserHomeUrl() {
        return mHtmlUrl;
    }

    /**
     * 응답과 무관하며
     * 로컬DB에 저장이 되었는지 UI적으로 보여주기 위해
     * 설정되는 변수
     */
    @JsonIgnore
    private boolean mIsSelectedItem = false;

    @JsonIgnore
    public void setSelectedItem(boolean isSelected) {
        mIsSelectedItem = isSelected;
    }

    /**
     * 응답과는 무관하며
     * DB에서 Login id기준으로 정렬하여 받는데
     * 이때, 초성기준으로 같은 그룹중에 첫번째 항목인지 확인 및 사용하기 위해 관리되는 변수
     */
    @JsonIgnore
    private String mInitialConsonant = "";

    @JsonIgnore
    public String getInitialConsonant() {
        return mInitialConsonant;
    }

    @JsonIgnore
    public void setInitialConsonant(String value) {
        mInitialConsonant = value;
    }

    @JsonIgnore
    public boolean isSelectedItem() {
        return mIsSelectedItem;
    }
}
