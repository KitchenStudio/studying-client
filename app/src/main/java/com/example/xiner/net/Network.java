package com.example.xiner.net;

import android.util.Log;

import com.example.xiner.util.PathUtil;
import com.example.xiner.util.UploadUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * Created by xiner on 15-1-27.
 */
public class Network {

    public static final String TAG="Network";

    public static void uploadItem(ArrayList<String> filePath){
                    RequestParams params = new RequestParams();
            params.put("content", "dsfsf");
            try {
                for(int i=0;i<filePath.size();i++){
                params.put("file", new File(new URI(filePath.get(i))));
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

            UploadUtil.post(PathUtil.uploadFile, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    Log.v(TAG, statusCode + "");

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Log.v(TAG, statusCode + "");
                }
            });
    }

}
