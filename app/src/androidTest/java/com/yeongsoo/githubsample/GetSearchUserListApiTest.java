package com.yeongsoo.githubsample;

import android.text.TextUtils;

import com.yeongsoo.githubsample.model.api.GetSearchUserListApi;
import com.yeongsoo.githubsample.model.api.OnResponseListener;
import com.yeongsoo.githubsample.model.data.SearchUserContents;
import com.yeongsoo.githubsample.model.data.SearchUserItemContents;
import com.yeongsoo.githubsample.utils.CollectionUtils;

import junit.framework.Assert;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by yeongsookim on 2018-05-13.
 */

public class GetSearchUserListApiTest extends Assert implements OnResponseListener<SearchUserContents> {
    private CountDownLatch mLatch = null;

    @Test
    public void test_CallApiTest() throws InterruptedException {
        mLatch = new CountDownLatch(1);

        GetSearchUserListApi api = new GetSearchUserListApi(30);

        api.setSearchKeyword("tom")
                .setPage(1)
                .setResponseListener(this)
                .send();

        mLatch.await(10, TimeUnit.SECONDS);
    }

    private void evaluateData(SearchUserContents obj) { //TODO T Generic
        assertNotNull("WikiRelatedContents object is null", obj);
        assertTrue("WikiRelatedContents object is null", !CollectionUtils.isEmpty(obj.getItemList()));

        int count = 0;
        for (SearchUserItemContents contents : obj.getItemList()) {
            assertTrue("SearchUserItemContents (" + count + ") object's title is empty",
                    !TextUtils.isEmpty(contents.getLoginId()));

            assertTrue("SearchUserItemContents (" + count + ") object's avatarImageUrl is empty",
                    !TextUtils.isEmpty(contents.getAvatarUrl()));

            count++;
        }
    }

    @Override
    public void onResponse(SearchUserContents obj) {
        evaluateData(obj);

        mLatch.countDown();
    }

    @Override
    public void onFail(Object obj) {
        fail();

        mLatch.countDown();
    }
}
