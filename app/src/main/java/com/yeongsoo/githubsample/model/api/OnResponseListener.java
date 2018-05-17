package com.yeongsoo.githubsample.model.api;

/**
 * Created by yeongsookim on 2018-05-13.
 *
 * Api에 응답처리를 Model로 전달할 수 있게 해주는 Interface
 */

public interface OnResponseListener<D> {
    void onResponse(D obj);
    void onFail(Object obj);
}
