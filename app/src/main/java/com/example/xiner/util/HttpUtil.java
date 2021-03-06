package com.example.xiner.util;

/**
 * Created by xiner on 14-12-23.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.example.xiner.R;
import com.example.xiner.main.AppBase;
import com.loopj.android.http.*;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;

import java.io.InputStream;
import java.lang.ref.WeakReference;

public class HttpUtil {
//    public static final String baseUrl="http://211.87.226.141:8080/api/v1/item";
//    public static final String baseIp="http://211.87.226.141:8080";
//    public static final String baseUserUrl="http://211.87.226.141:8080/api/v1/user";

    public static final String baseUrl="http://211.87.234.180:8080/studying/api/v1/item";
    public static final String baseIp="http://211.87.234.180:8080";
    public static final String baseUserUrl="http://211.87.234.180:8080/studying/api/v1/user";

    public static AsyncHttpClient client = new AsyncHttpClient();

    static {
        String username = AppBase.getApp().getDataStore().getString("username","18366116016");
        String password = AppBase.getApp().getDataStore().getString("password","..xiao");
        client.setBasicAuth(username, password);
    }

    public static void get(String URL,JsonHttpResponseHandler jsonHandler) {
        client.get(URL, jsonHandler);
    }

    public static void get(String URL,AsyncHttpResponseHandler jsonHandler) {
        client.get(URL, jsonHandler);
    }



    public static void get(String URL,RequestParams params,JsonHttpResponseHandler jsonHandler) {
        client.get(URL, params,jsonHandler);
    }
    public static void postregis(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        AsyncHttpClient clientregis = new AsyncHttpClient();
        clientregis.post(url, params, responseHandler);
    }
    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(url, params, responseHandler);
    }
    public static void post(String url, RequestParams params, TextHttpResponseHandler responseHandler) {
        client.post(url, params, responseHandler);

    }



    public static void put(String url,RequestParams params,AsyncHttpResponseHandler responseHandler){
        client.put(url,params,responseHandler);
    }

    public static void get(String URL, BinaryHttpResponseHandler binaryHttpResponseHandler){
        client.get(URL,binaryHttpResponseHandler);
    }





}