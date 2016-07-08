package com.example.ridiculous.ismpsdk;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by Ridiculous on 2016/6/23.
 */
public class HttpAsyncClient {
    private static final String BASE_URL = "http://202.102.41.186:9250/ISMPOpen2/serviceOrder";
    //private static final String BASE_URL = "http://202.102.120.121:8080/serviceOrder";

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }

}
