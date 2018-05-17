package com.yeongsoo.githubsample.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

/**
 * Created by yeongsookim on 2018-05-14.
 *
 * 웹으로 부터 이미지를 받아와서 View에 보여줄 수 있게 하는 클래스
 *
 * Volley를 쓰기 보다 직접 한번 구현해 봤습니다.
 * 디스크 캐시는 구현하지 못했습니다.(시간이...)
 */

public class DownloadableImageView extends ImageView {
    private AsyncTask mDownloadTask = null;

    public DownloadableImageView(Context context) {
        super(context);
    }

    public DownloadableImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DownloadableImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setImageUrl(String url) {
        AsyncTask task = new DownloadImageTask(this).execute(url);
        if (mDownloadTask != null && !mDownloadTask.isCancelled()) {
            mDownloadTask.cancel(true);
        }
        mDownloadTask = task;
    }

    @Override
    protected void onDetachedFromWindow() {
        if (mDownloadTask != null && !mDownloadTask.isCancelled()) {
            mDownloadTask.cancel(true);
        }
        super.onDetachedFromWindow();
    }

    //캐싱은 구현할 시간이....
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView mImageView;

        public DownloadImageTask(ImageView image) {
            mImageView = image;
        }

        protected Bitmap doInBackground(String... urls) {
            String imageUrl = urls[0];
            Bitmap image = null;

            try {
                InputStream in = new java.net.URL(imageUrl).openStream();

                if (isCancelled()) {
                    return null;
                }

                image = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return image;
        }

        protected void onPostExecute(Bitmap result) {
            mImageView.setImageBitmap(result);
        }
    }
}
