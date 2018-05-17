package com.yeongsoo.githubsample.view.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yeongsoo.githubsample.R;
import com.yeongsoo.githubsample.model.data.SearchUserItemContents;
import com.yeongsoo.githubsample.view.DownloadableImageView;

/**
 * Created by yeongsookim on 2018-05-13.
 *
 * RecyclerView에 Github 한 유저의 정보를 보여주는 View Holder
 */

public class GithubItemViewHolder extends RecyclerView.ViewHolder {
    private View mInitialConsonantLayout = null;
    private TextView mInitialConsonant = null;
    private DownloadableImageView mTitleImage = null;
    private TextView mLoginId = null;
    private ImageView mFavoriteBtn= null;
    private View mDescViewLayout = null;

    public GithubItemViewHolder(View itemLayoutView) {
        super(itemLayoutView);

        mInitialConsonantLayout = itemLayoutView.findViewById(R.id.initial_consonant_layout);
        mInitialConsonant = itemLayoutView.findViewById(R.id.initial_consonant);
        mTitleImage = itemLayoutView.findViewById(R.id.avatar_image);
        mLoginId = itemLayoutView.findViewById(R.id.login_id);
        mFavoriteBtn = itemLayoutView.findViewById(R.id.favorite_btn);
        mDescViewLayout = itemLayoutView.findViewById(R.id.desc_layout);
    }

    public void setData(SearchUserItemContents contents, View.OnClickListener listener) {
        if (!TextUtils.isEmpty(contents.getInitialConsonant())) {
            mInitialConsonant.setText(contents.getInitialConsonant());
            mInitialConsonantLayout.setVisibility(View.VISIBLE);
        } else {
            mInitialConsonantLayout.setVisibility(View.GONE);
        }

        mTitleImage.setImageUrl(contents.getAvatarUrl());
        mLoginId.setText(contents.getLoginId());

        mFavoriteBtn.setSelected(contents.isSelectedItem());

        mFavoriteBtn.setOnClickListener(listener);
        mDescViewLayout.setOnClickListener(listener);
    }
}
