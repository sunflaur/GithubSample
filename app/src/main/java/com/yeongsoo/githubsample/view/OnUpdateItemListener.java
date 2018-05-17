package com.yeongsoo.githubsample.view;

/**
 * Created by yeongsookim on 2018-05-13.
 *
 * Presenter에서 View에 데이터를 전달할 것이 있을 경우
 * View(Fragment / Activity)에 해당 인터페이스르 구현하고
 * Presenter에서 호출할 수 있도록 정의된 interface
 */

public interface OnUpdateItemListener<T> {
    void onUpdateItem(T item);
}
