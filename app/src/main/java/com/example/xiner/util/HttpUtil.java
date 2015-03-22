package com.example.xiner.util;

/**
 * Created by xiner on 14-12-23.
 */

import android.content.ClipData;

import com.example.xiner.entity.ListItem;
import com.loopj.android.http.*;

import java.util.ArrayList;
import java.util.List;


public class HttpUtil {
    public static final String baseUrl="http://211.87.226.156:8080/api/v1/item";

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

    public static void post(String url, AsyncHttpResponseHandler responseHandler) {
        client.post(url, responseHandler);
    }


    public static void get(String URL, BinaryHttpResponseHandler binaryHttpResponseHandler){
        client.get(URL,binaryHttpResponseHandler);
    }

//    public static List<ListItem> post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler,ArrayList<ClipData.Item> list) {
//        client.post(url, params, responseHandler);
//        return list;
//    }
}