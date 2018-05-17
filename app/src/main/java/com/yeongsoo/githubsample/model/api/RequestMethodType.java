package com.yeongsoo.githubsample.model.api;

/**
 * Created by yeongsookim on 2018-05-13.
 *
 * CRUD 타입을 정의
 */

enum RequestMethodType {
    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    DELETE("DELETE");

    private String mMethod = "";

    RequestMethodType(String method) {
        mMethod = method;
    }

    public String getMethodName() {
        return mMethod;
    }

    public static RequestMethodType create(String type) {
        RequestMethodType requestType = RequestMethodType.GET;

        for (RequestMethodType t : values()) {
            if (t.getMethodName().endsWith((type))) {
                requestType = t;
                break;
            }
        }

        return requestType;
    }
}
