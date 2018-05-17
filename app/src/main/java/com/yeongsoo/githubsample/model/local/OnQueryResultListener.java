package com.yeongsoo.githubsample.model.local;

/**
 * Created by yeongsookim on 2018-05-16.
 *
 * DB Helper에 요청한 데이터를 되돌려 주기 위해 정의된 interface
 */

public interface OnQueryResultListener<D> {
    void onResult(D obj);
    void onFail(Object obj);
}
