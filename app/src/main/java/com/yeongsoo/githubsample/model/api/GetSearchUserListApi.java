package com.yeongsoo.githubsample.model.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yeongsoo.githubsample.model.data.SearchUserContents;

import java.io.IOException;

/**
 * Created by yeongsookim on 2018-05-13.
 *
 * GitHub의 사용자 검색을 하기 위한 Api
 * api응답으로 json타입의 데이터클래스로 변환하고 이를
 * 상위 layer로 받을 수 있게 되돌려준다.
 *
 * 해당 api에서는 특정 값에 의한 정렬기능이 없고 Score에 의한 정렬만 지원하고 있고
 * 사용자 이름이 아닌 Login정보포함한 사용자 검색 api이므로 login id이름만을 확인할 수 있다.
 * (사용자 이름을 확인하려면, 이 api를 통하고 또다른 api를 호출해서 확인해야 한다)
 *
 * 이런 상황으로 인해 정렬방식에 따라 오름차순, 내림차순 구현에 대해 지원까지 구현은 할 수 없는 환경이므로 구현하지 못했습니다.
 *
 * 이름등에 의한 정렬은 local에 저장하고 가져올 때 해당 기능을 구현
 */

public class GetSearchUserListApi extends GetApi<SearchUserContents> {
    public static int DEFAULT_SIZE_PER_PAGE = 30;

    private String mKeyword = "";
    private int mCountPerPage = 30;
    private int mPage = 1;

    public GetSearchUserListApi() {
        this(DEFAULT_SIZE_PER_PAGE);
    }

    public GetSearchUserListApi(int countPerPage) {
        //mCountPerPage = countPerPage;
        setBaseRequestUrl("https://api.github.com/search");
        appendQuery("per_page", String.valueOf(countPerPage));
    }

    public GetSearchUserListApi setSearchKeyword(String keyword) {
//        mKeyword = keyword;
        appendQuery("q",keyword);
        return this;
    }

    public GetSearchUserListApi setPage(int pageNumber) {
        mPage = pageNumber;
        appendQuery("page", String.valueOf(pageNumber));
        return this;
    }

    public GetSearchUserListApi setNextPage() {
        appendQuery("page", String.valueOf(++mPage));
        return this;
    }

    public int getPage() {
        return mPage;
    }

    @Override
    protected String getDetailPath() {
        return "users";
    }

    @Override
    protected SearchUserContents getMappingData(String json, ObjectMapper mapper) throws IOException {
        SearchUserContents contents = mapper.readValue(json, SearchUserContents.class);
        contents.setIsEndOfList((mPage) * mCountPerPage >= contents.getTotalCount());

        return contents;
    }
}
