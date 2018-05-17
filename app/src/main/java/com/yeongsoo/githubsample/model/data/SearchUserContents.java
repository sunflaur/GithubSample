package com.yeongsoo.githubsample.model.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.yeongsoo.githubsample.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yeongsookim on 2018-05-13.
 *
 * Github 사용자 검색 api의 reponse로 들어오는 json에 대응되는 데이터 클래스
 */

public class SearchUserContents {
    @JsonProperty("total_count")
    private int total = 0;

    @JsonProperty("incomplete_results")
    private boolean incompleteResults = false;

    @JsonProperty("items")
    private List<SearchUserItemContents> mItemList;

    /**
     * api 응답 field와는 무관하고
     * api에서 제공하지 않는
     * 현재 데이터가 마지막 데이터인지 확인하는 플래그
     */
    @JsonIgnore
    private boolean mIsEndOfList = false;

    @JsonIgnore
    public int getTotalCount() {
        return total;
    }

    @JsonIgnore
    public boolean isEndOfList() {
        return mIsEndOfList;
    }

    @JsonIgnore
    public void setIsEndOfList(boolean end) {
        mIsEndOfList = end;
    }

    @JsonIgnore
    public List<SearchUserItemContents> getItemList() {
        if (!CollectionUtils.isEmpty(mItemList)) {
            return mItemList;
        }

        //null return을 지양하여 NPE발생을 줄이는 방향을 위해 빈 List를 리턴
        return new ArrayList<>();
    }

    @JsonIgnore
    public void setItemList(List<SearchUserItemContents> list) {
        if (!CollectionUtils.isEmpty(list)) {
            mItemList = list;
        }
    }
}
