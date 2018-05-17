package com.yeongsoo.githubsample.model.api;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

/**
 * Created by yeongsookim on 2018-05-13.
 *
 * http/https 에 따른 기본 연결 구성을 담당한다
 */

abstract class Api<D> {
    public HttpURLConnection getHttpConnection(URL url) {
        HttpURLConnection con = null;

        try {
            if (!(url.openConnection() instanceof HttpURLConnection)) {
                //TODO Throw Exception??
                return null;
            }

            con = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {

        }

        return con;
    }

    public HttpsURLConnection getHttpsConnection(URL url) {
        HttpsURLConnection con = null;

        try {
            if (!(url.openConnection() instanceof HttpsURLConnection)) {
                //TODO Throw Exception??
                return null;
            }

            con = (HttpsURLConnection) url.openConnection();
        } catch (IOException e) {
            return null;
        }

        con.setHostnameVerifier(new AllowAllHostnameVerifier());

        return con;
    }

    //항상 허용하도록 한다
    private class AllowAllHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }
}
