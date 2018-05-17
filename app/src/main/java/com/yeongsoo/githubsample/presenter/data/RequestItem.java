package com.yeongsoo.githubsample.presenter.data;

import java.lang.ref.WeakReference;

/**
 * Created by yeongsookim on 2018-05-13.
 *
 * View와 Presenter사이에 교환할 데이터 필드가 정의된 클래스
 */

public class RequestItem {
    private RequestType mRequestType = RequestType.DEFAULT;
    private WeakReference<Object> mRequestObjectRef = null;

    public RequestItem(RequestType requestType, Object object) {
        mRequestType = requestType;
        mRequestObjectRef = new WeakReference<>(object);
    }

    public RequestType getType() {
        return mRequestType;
    }

    public Object getObject() {
        return mRequestObjectRef.get();
    }
}
