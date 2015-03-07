package com.example.xiner.net;

import android.content.Context;
import android.os.Looper;
import android.preference.PreferenceActivity;
import android.util.Log;

import com.example.xiner.entity.Item;
import com.example.xiner.util.UploadUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.apache.http.client.ResponseHandler;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiner on 15-1-27.
 */
public class Network {

    Context context;

    public static final String TAG = "Network";
    public static final String uploadFile = "http://211.87.226.208:8080/api/v1/item/files";
    public static final String getshareList = "http://211.87.226.208:8080/api/v1/item";
    UploadUtil uploadUtil ;

    public Network(Context context) {

        this.context = context;
        uploadUtil = new UploadUtil(context);
    }

    public static void uploadshare(ArrayList<String> file, String content, String grade, String subject) {

        RequestParams params = new RequestParams();
        params.put("content", content);
        params.put("grade", grade);
        params.put("subject", subject);


        for (int i = 0; i < file.size(); i++) {
            try {

                params.put("file" + i, new File(new URI(file.get(i))), "multipart/form-data");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }


        UploadUtil.post(uploadFile, params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.v("PublicDocA", statusCode + "codesuccess");

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.v("PublicDocAf", statusCode + "codefailed");
                error.printStackTrace(System.out);
            }
        });
    }


    public void getSharelist() {

        Looper.prepare();
        AsyncHttpClient client = new AsyncHttpClient();
        client.setBasicAuth("18366116016", "..xiao");

        client.get(getshareList,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                Log.v(TAG,statusCode+"codesuccess");
                Log.v(TAG, response.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.v(TAG,statusCode+"codefailer");
                throwable.printStackTrace(System.out);
            }
        });

    }


}
