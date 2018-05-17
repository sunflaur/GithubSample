package com.yeongsoo.githubsample.model.api;

import android.text.TextUtils;

import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.ProtocolException;

/**
 * Created by yeongsookim on 2018-05-13.
 *
 * Post 타입 api의 기본을 갖고 있는 api
 * Post 타입이므로 body에 데이터를 넣을 수 있게 제공을 한다.
 */

abstract class PostApi<D> extends AbsApi<D> {
    private String mBody = "";

    public PostApi() {
        super(RequestMethodType.POST);
    }

    public void setBody(JSONObject json) {
        if (json == null) {
            return;
        }

        setBody(json.toString());
    }

    public void setBody(String body) {
        mBody = body;
    }

    //Get Api에서는 Body를 사용하지 않는다.
    @Override
    protected HttpURLConnection setupBody(HttpURLConnection connection) {
        if (TextUtils.isEmpty(mBody) || connection == null) {
            return null;
        }

        try {
            OutputStreamWriter outStream = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
            PrintWriter writer = new PrintWriter(outStream);
            writer.write(mBody);
            writer.flush();
            writer.close();
            outStream.close();
        } catch (IOException e) {

        }

        return connection;
    }

    public void send() {
        try {
            request(setupBody(getApiConnection()));
        } catch (ProtocolException e) {
        }
    }
}
