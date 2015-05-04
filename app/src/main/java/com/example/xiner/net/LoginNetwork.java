package com.example.xiner.net;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.xiner.activity.LoginActivity;
import com.example.xiner.activity.MainActivity;
import com.example.xiner.entity.User;
import com.example.xiner.main.AppBase;
import com.example.xiner.util.DownloadFileUtil;
import com.example.xiner.util.DownloadPicUtil;
import com.example.xiner.util.HttpUtil;
import com.example.xiner.util.LoadingDialog;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

import java.io.File;

/**
 * Created by xiner on 15-3-23.
 */
public class LoginNetwork {

    public static final String loginurl =HttpUtil.baseUserUrl+"/login";
    private static final String TAG = "LoginNetwork";
    String httpInfo=HttpUtil.baseUserUrl;
    ShareNetwork shareNetwork;
    String picpath = "/sdcard/xueyou/myHead/head.jpg";

    Context context;
    public LoginNetwork(Context context){
        this.context = context;
        shareNetwork = new ShareNetwork();
    }

    public void loginUpload(final String email,final String password) {
        if (AppBase.getApp().isConnected()) {
            Log.v(TAG,email+"email"+password+"password");
            HttpUtil.client = new AsyncHttpClient();
//            client.setBasicAuth(email, password);

            HttpUtil.client.setBasicAuth(email, password);


            final Dialog dialog = LoadingDialog.createDialog(context, "正在上传，请稍后....");
            dialog.show();

            HttpUtil.get(loginurl, new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    dialog.dismiss();
                    String result = new String(responseBody);
                    if (result.equals("login success")) {
                        Toast.makeText(context, "登陆成功", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.setClass(context, MainActivity.class);
                        context.startActivity(intent);
                        AppBase.getApp().getDataStore().edit().putString("username", email).commit();
                        AppBase.getApp().getDataStore().edit().putString("password", password).commit();
                    } else if (result.equals("password error")) {
                        Toast.makeText(context, "密码错误", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(context, "用户不存在", Toast.LENGTH_SHORT).show();

                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    dialog.dismiss();
                    Toast.makeText(context, "登陆失败", Toast.LENGTH_SHORT).show();

                }


            });
        }else{
            Toast.makeText(context, "网络无连接", Toast.LENGTH_SHORT).show();

        }
    }



}
