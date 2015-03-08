package com.example.xiner.util;

/**
 * Created by xiner on 14-12-23.
 */
import android.content.Context;

import com.example.xiner.fragment.ShareFragment;
import com.loopj.android.http.*;


public class HttpUtil {

    private static AsyncHttpClient client = new AsyncHttpClient();

    static {
        client.setBasicAuth("18366116016", "..xiao");
    }



    public static void get(String URL,JsonHttpResponseHandler jsonHandler) {
        client.get(URL, jsonHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(url, params, responseHandler);
    }




}