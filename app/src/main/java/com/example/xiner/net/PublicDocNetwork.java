package com.example.xiner.net;

import android.content.Context;
import android.util.Log;

import com.example.xiner.entity.FileItem;
import com.example.xiner.entity.Item;
import com.example.xiner.entity.User;
import com.example.xiner.fragment.ShareFragment;
import com.example.xiner.util.HttpUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by xiner on 15-1-27.
 */
public class PublicDocNetwork {

    public static final String TAG = "Network";
    public static final String uploadFile = "http://211.87.226.208:8080/api/v1/item/files";



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


        HttpUtil.post(uploadFile, params, new AsyncHttpResponseHandler() {

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

}
