package com.yeongsoo.githubsample.model.api;

import android.net.Uri;
import android.text.TextUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by yeongsookim on 2018-05-13.
 *
 * url, 헤더 또는 body를 구성하여
 * http/https 프로토콜에 맞는 api를 호출하고
 * 데이터를 되돌려주는 기본 구성이 되어 있다.
 *
 * 이 클래스는 바로 사용할 수 없으며,
 * json데이터를 변환하고 기본 url을 구성하는
 * 각 목적별로 api를 상속구현할 때, 상속해야 만 하는 클래스
 */

abstract class AbsApi<D> extends Api<D> {
    private RequestMethodType mRequestMethodType = RequestMethodType.GET;
    private Protocol mHttpProtocol = Protocol.HTTP;
    private Uri.Builder mRequestUrlBuilder = new Uri.Builder();
    private HashMap<String, String> mHeader = new HashMap<>();
    private OnResponseListener<D> mListener = null;

    public AbsApi(RequestMethodType type) {
        mRequestMethodType = type;
    }

    public AbsApi setBaseRequestUrl(String url) {
        Uri uri = Uri.parse(url);

        mRequestUrlBuilder.scheme(uri.getScheme());
        mRequestUrlBuilder.encodedAuthority(uri.getEncodedAuthority());
        mRequestUrlBuilder.encodedPath(uri.getEncodedPath());
        mRequestUrlBuilder.encodedQuery(uri.getEncodedQuery());

        setUrlProtocol(uri.getScheme());

        appendPath(getDetailPath());

        return this;
    }

    void setUrlProtocol(String scheme) {
        if (Protocol.HTTP.getProtocolName().endsWith(scheme)) {
            mHttpProtocol = Protocol.HTTP;
        } else if (Protocol.HTTPS.getProtocolName().endsWith(scheme)) {
            mHttpProtocol = Protocol.HTTPS;
        }
    }

    public String getUrlProtocol() {
        return mHttpProtocol.getProtocolName();
    }

    public RequestMethodType getMethodType() {
        return mRequestMethodType;
    }

    protected abstract String getDetailPath();

    //Post/Put에서만 한정.
    protected abstract HttpURLConnection setupBody(HttpURLConnection connection);

    public AbsApi putHeader(String key, String value) {
        if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)) {
            mHeader.put(key, value);
        }
        return this;
    }

    public AbsApi appendPath(String newSegment) {
        if (!TextUtils.isEmpty(newSegment)) {
            mRequestUrlBuilder.appendEncodedPath(newSegment);
        }
        return this;
    }

    public AbsApi appendQuery(String key, String value) {
        if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)) {
            mRequestUrlBuilder.appendQueryParameter(key, value);
        }
        return this;
    }

    private HashMap<String, String> getHeader() {
        return mHeader;
    }

    private Uri getUri() {
        return mRequestUrlBuilder.build();
    }

    public AbsApi setResponseListener(OnResponseListener<D> listener) {
        mListener = listener;
        return this;
    }

    public HttpURLConnection getApiConnection() {
        if (mRequestUrlBuilder == null) {
            return null;
        }

        URL url = null;
        HttpURLConnection connection = null;

        try {
            url = new URL(getUri().toString());
        } catch (MalformedURLException e) {
            return null;
        }

        if (Protocol.HTTP.equals(mHttpProtocol)) {
            connection = getHttpConnection(url);
        } else if (Protocol.HTTPS.equals(mHttpProtocol)) {
            connection = getHttpsConnection(url);
        }

        for (String key : getHeader().keySet()) {
            connection.setRequestProperty(key, getHeader().get(key));
        }

        return connection;
    }

    public void request(final HttpURLConnection connection) throws ProtocolException {
        Observable<D> observable = Observable.create(
                new Observable.OnSubscribe<D>() {
                    @Override
                    public void call(Subscriber<? super D> sub) {
                        D data = null;

                        try {
                            data = doRequest(connection);
                        } catch (ProtocolException e) {
                            sub.onError(e);
                        }

                        if (data != null) {
                            sub.onNext(data);
                            sub.onCompleted();
                        } else {
                            sub.onError(new Throwable());
                            sub.unsubscribe();
                        }
                    }
                }
        ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        observable.subscribe(new Subscriber<D>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                mListener.onFail(e);
            }

            @Override
            public void onNext(D d) {
                mListener.onResponse(d);
            }
        });
    }

    public D doRequest(HttpURLConnection connection) throws ProtocolException {
        D data = null;
        BufferedReader br = null;

        try {
            connection.setRequestMethod(mRequestMethodType.getMethodName());
            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = br.readLine()) != null) {
                    response.append(inputLine);
                }

                data = getMappingData(response.toString(), new ObjectMapper());
            } else {    // 에러 발생
                br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            }

        } catch (ProtocolException e) {
            e.printStackTrace();
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {}
            }
        }

        return data;
    }

    //각 Api에서 json을 mapping한 데이터클래스 인스턴스를 리턴한다.
    protected abstract D getMappingData(String json, ObjectMapper mapper) throws  IOException;
    public abstract void send();

    enum Protocol {
        HTTP("http"),
        HTTPS("https");

        private String mProtocol = "";

        Protocol(String protocol) {
            mProtocol = protocol;
        }

        public String getProtocolName() {
            return mProtocol;
        }

        public static Protocol create(String type) {
            Protocol protocol = Protocol.HTTP;

            for (Protocol p: values()) {
                if (p.getProtocolName().endsWith((type))) {
                    protocol = p;
                    break;
                }
            }

            return protocol;
        }
    }
}
