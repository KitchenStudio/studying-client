package com.example.xiner.net;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.xiner.util.HttpUtil;
import com.example.xiner.util.LoadingDialog;
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
public class PublicDocNetwork {

    public static final String TAG = "Network";
    public static final String uploadFile = HttpUtil.baseUrl+"/files";
    Context context;

    public PublicDocNetwork(Context context){
        this.context = context;
    }



    public  void uploadshare(ArrayList<String> file, String content, String subject) {

        final Dialog dialog =LoadingDialog.createDialog(context,"正在上传，请稍后....");
        dialog.show();

        RequestParams params = new RequestParams();
        params.put("content", content);
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

                dialog.dismiss();
                Toast.makeText(context,"上传成功",Toast.LENGTH_SHORT).show();
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.v("PublicDocAf", statusCode + "codefailed");
                error.printStackTrace(System.out);
                dialog.dismiss();
                Toast.makeText(context,"上传失败",Toast.LENGTH_SHORT).show();
            }
        });
    }

}
