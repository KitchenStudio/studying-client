package com.example.xiner.net;

import android.app.Dialog;
import android.content.Context;
import android.media.MediaRouter;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.example.xiner.entity.User;
import com.example.xiner.entity.Userinfo;
import com.example.xiner.main.AppBase;
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
 * Created by xiner on 15-3-22.
 */
public class EditInfoNet {
    private static final String TAG = "EditInfoNet";
    Context context;
    private static final String editInfourl = HttpUtil.baseUserUrl;
    private static final String uploadFigure = HttpUtil.baseUserUrl;
    AppBase appBase;
    String username;
    private final static String path = Environment.getExternalStorageDirectory() + "/xueyou/myHead/head.jpg";// sd路径


    public EditInfoNet(Context context) {
        this.context = context;
        appBase = AppBase.getApp();
    }

    public void EditInfo(String filename, String nickname, int age, String sex) {
        username = appBase.getDataStore().getString("username", "18366116016");
        Log.v(TAG,"usename"+username+"username");
        final Dialog dialog = LoadingDialog.createDialog(context, "正在上传，请稍后....");
        dialog.show();

        RequestParams params = new RequestParams();
        Userinfo form = new Userinfo();

        form.setNickname(nickname);
        form.setAge(age);
        form.setSex(sex);
//        form.
//        form.setS
//        form.se
        params.put("username", form);


//        try {
////            params.put("file", new File(new URI(file)), "multipart/form-data");
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }


        HttpUtil.put(editInfourl + "/" + username + "/info", params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.v("PublicDocA", statusCode + "codesuccess");
                RequestParams params = new RequestParams();
                try {
                    for (int i = 0; i < 1; i++) {
                        params.put("file" + i, new File(new URI("file://" + path+"")), "multipart/form-data");
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (URISyntaxException e) {
                    e.printStackTrace();

                }
                HttpUtil.post(uploadFigure + "/" + username + "/savefigure", params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        dialog.dismiss();
                        Toast.makeText(context, "上传成功", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        error.printStackTrace();
                    }
                });


//                dialog.dismiss();
//                Toast.makeText(context, "修改成功", Toast.LENGTH_SHORT).show();
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.v("PublicDocAf", statusCode + "codefailed");
                error.printStackTrace(System.out);
                dialog.dismiss();
                Toast.makeText(context, "上传失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
