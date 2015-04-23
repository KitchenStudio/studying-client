package com.example.xiner.util;

/**
 * Created by xiner on 14-12-23.
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.example.xiner.R;
import com.loopj.android.http.*;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;

import java.io.InputStream;
import java.lang.ref.WeakReference;

public class HttpUtil {
    public static final String baseUrl="http://211.87.226.245:8080/api/v1/item";
    public static final String baseIp="http://211.87.226.245:8080";

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
    public static void post(String url, RequestParams params, TextHttpResponseHandler responseHandler) {
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