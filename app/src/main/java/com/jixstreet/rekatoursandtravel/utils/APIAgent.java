package com.jixstreet.rekatoursandtravel.utils;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by satryaway on 12/8/2015.
 * REQUEST HANDLER
 */
public class APIAgent {
    private AsyncHttpClient client;

    public APIAgent() {
        client = new AsyncHttpClient();
        client.addHeader("user-agent", "twh:[22691871];[CV. Rajawali Reka Cipta (Rekatours)];");
        client.setUserAgent("twh:[22691871];[CV. Rajawali Reka Cipta (Rekatours)];");
        client.setTimeout(100000);
    }

    public void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(url, params, responseHandler);
    }

    public void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(url, params, responseHandler);
    }

    public void put(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.put(url, params, responseHandler);
    }

    public void patch(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.patch(url, params, responseHandler);
    }

    public void delete(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.delete(url, params, responseHandler);
    }
}
