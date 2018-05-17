package com.yeongsoo.githubsample.model.api;

import java.net.HttpURLConnection;
import java.net.ProtocolException;

/**
 * Created by yeongsookim on 2018-05-13.
 *
 * Get 타입 api의 기본을 갖고 있는 api
 */

abstract class GetApi<D> extends AbsApi<D> {
    public GetApi() {
        super(RequestMethodType.GET);
    }

    //Get Api에서는 Body를 사용하지 않는다.
    protected HttpURLConnection setupBody(HttpURLConnection connection)
    {
        return null;
    }

    public void send() {
        try {
            request(getApiConnection());
        } catch (ProtocolException e) {
        }
    }
}
