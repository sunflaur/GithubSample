package com.yeongsoo.githubsample.view.recyclerview;

import android.view.View;

/**
 * Created by yeongsookim on 2018-05-13.
 *
 * RecyclerView Holder에서 Touch이벤트가 발새하였을 때,
 * 이를 (Activity나 Fragment등으로) 전달할 수 있도록 지원해주는 interface
 */

public interface OnTouchViewHolder {
    void onTouchHolder(View view, Object item);
}
