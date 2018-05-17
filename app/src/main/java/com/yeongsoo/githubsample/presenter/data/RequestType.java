package com.yeongsoo.githubsample.presenter.data;

/**
 * Created by yeongsookim on 2018-05-13.
 *
 * View와 Presenter간에 요청 및 응답의 타입 정의
 */

public enum RequestType {
    DEFAULT,
    SEARCH_USER,
    SEARCH_USER_NEXT,
    QUERY_FAVORITE,
    QUERY_FAVORITE_NEXT,
    ADD_FAVORITE,
    DELETE_FAVORITE
}