package com.yeongsoo.githubsample.view.recyclerview;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

/**
 * Created by yeongsookim on 2018-05-17.
 *
 * RecyclerView에서 다음페이지를 부를 때, 마지막 첫 페이지가 남았을 경우에
 * 요청하도록 하고
 * 이를 알기 위해 scroll이벤트를 오버라이딩하여 구현했다.
 *
 * 다음 페이지에 대한 데이터를 요청하는 조건이 되면
 * 이와 관련된 정의된 interface의 메서드를 호출하여 콜백시킨다.
 * @see com.yeongsoo.githubsample.view.recyclerview.OnNextPageCallListener
 *
 * 0 page 부터 시작한다. (한 페이지 당 비율은 customizing 할 수 있도록 함 mScaleAPage)
 * |---| 0 page : 1/2 지점 onNext
 * |---|---| 1 page : 1/2 지점 onNext
 * |---|---|---| 2 page : 2/3 지점 onNext
 * |---|---|---|---| 3 page : 3/4 지점 onNext
 */

public class OnNextPageTriggerListener extends RecyclerView.OnScrollListener {
    private final OnNextPageCallListener mOnNextPageCallListener;
    private float mScaleAPage = 1f; // 페이지 스케일

    public OnNextPageTriggerListener(@NonNull OnNextPageCallListener onNextPageCallListener) {
        mOnNextPageCallListener = onNextPageCallListener;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if (mOnNextPageCallListener.hasNextPage()) {
            int offset = recyclerView.computeVerticalScrollOffset();
            float range = (float) recyclerView.computeVerticalScrollRange();
            int currentPage = mOnNextPageCallListener.getCurrentPage();
            float ratio = 1 - (1f / ((float) (currentPage + 1) * mScaleAPage));
            if (ratio == 0) {
                ratio = 0.5f;
            }

            if (offset > ratio * range) {
                mOnNextPageCallListener.onNext();
            }
        }
    }
}
