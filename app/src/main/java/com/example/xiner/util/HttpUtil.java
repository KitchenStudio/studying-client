package com.example.xiner.util;

/**
 * Created by xiner on 14-12-23.
 */
import android.content.Context;

import com.example.xiner.entity.Item;
import com.example.xiner.fragment.ShareFragment;
import com.loopj.android.http.*;

import java.util.ArrayList;
import java.util.List;


public class HttpUtil {
    public static final String baseUrl="http://211.87.226.168:8080/api/v1/item";

    private static AsyncHttpClient client = new AsyncHttpClient();

    static {
        client.setBasicAuth("18366116016", "..xiao");
    }



    public static void get(String URL,JsonHttpResponseHandler jsonHandler) {
        client.get(URL, jsonHandler);
    }


    public static void get(String URL,RequestParams params,JsonHttpResponseHandler jsonHandler) {
        client.get(URL, params,jsonHandler);
    }
    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(url, params, responseHandler);
    }


    public static void get(String URL, BinaryHttpResponseHandler binaryHttpResponseHandler){
        client.get(URL,binaryHttpResponseHandler);
    }

    public static List<Item> post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler,ArrayList<Item>list) {
        client.post(url, params, responseHandler);
        return list;
    }
}