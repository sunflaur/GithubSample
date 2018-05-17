package com.yeongsoo.githubsample.view.recyclerview;

/**
 * Created by yeongsookim on 2018-05-17.
 *
 * RecyclerView에서 List끝까지 가기전에 다음 페이지에 대한 아이템 요청을 시도할 수 있도록
 * 관련된 메서드가 정의된 interface
 */

public interface OnNextPageCallListener {
    void onNext();
    boolean hasNextPage();
    int getCurrentPage();
}
