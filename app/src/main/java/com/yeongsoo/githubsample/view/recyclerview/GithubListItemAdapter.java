package com.yeongsoo.githubsample.view.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yeongsoo.githubsample.R;
import com.yeongsoo.githubsample.model.data.SearchUserItemContents;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yeongsookim on 2018-05-13.
 *
 * RecyclerView와 연결되어 데이터를 사용해 Holder를 구성해 주는 어댑터
 */

public class GithubListItemAdapter extends RecyclerView.Adapter<GithubItemViewHolder> {
    private List<SearchUserItemContents> mItemList = new ArrayList<>();
    private WeakReference<OnTouchViewHolder> mUpdaterRef = null;

    public GithubListItemAdapter(OnTouchViewHolder updater) {
        mUpdaterRef = new WeakReference<>(updater);
    }

    @Override
    public void onBindViewHolder(GithubItemViewHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public void onBindViewHolder(GithubItemViewHolder holder, int position) {
        final SearchUserItemContents item = mItemList.get(position);
        holder.setData(item, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUpdaterRef.get().onTouchHolder(v, item);
            }
        });
    }

    @Override
    public GithubItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_itemholder_layout, null);
        GithubItemViewHolder viewHolder = new GithubItemViewHolder(itemLayoutView);

        return viewHolder;
    }


    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return ((null != mItemList) ? mItemList.size() : 0);
    }

    public void setItemList(List<SearchUserItemContents> list) {
        if (mItemList != null) {
            mItemList.clear();
        }

        mItemList.addAll(list);
    }

    public void clearList() {
        if (mItemList != null) {
            mItemList.clear();
        }
    }

    public void addItemList(List<SearchUserItemContents> list) {
        if (mItemList == null) {
            mItemList = new ArrayList<>();
        }

        mItemList.addAll(list);
    }

    public void notifyDataChanged() {
        notifyDataSetChanged();
    }
}
