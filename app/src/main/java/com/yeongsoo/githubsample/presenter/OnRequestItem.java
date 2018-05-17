package com.yeongsoo.githubsample.presenter;

import com.yeongsoo.githubsample.presenter.data.RequestItem;

/**
 * Created by yeongsookim on 2018-05-13.
 *
 * View에서 요청하고 이를 Presenter에서 구현을 통해 받아 처리에 관련된 interface
 */

public interface OnRequestItem {
    void onRequestItem(RequestItem item);
}
