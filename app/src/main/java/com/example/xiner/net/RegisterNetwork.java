package com.example.xiner.net;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.xiner.activity.LoginActivity;
import com.example.xiner.entity.User;
import com.example.xiner.main.AppBase;
import com.example.xiner.util.HttpUtil;
import com.example.xiner.util.LoadingDialog;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

/**
 * Created by xiner on 15-3-23.
 */
public class RegisterNetwork {

    private static final String TAG = "RegisterNetwork";
    Context context;
    AppBase appBase;

    public RegisterNetwork(Context context) {
        this.context = context;
        appBase = AppBase.getApp();
    }

    public static final String registerurl = HttpUtil.baseUserUrl+"/add";

    public void uploadRegister(String email, String password) {

        final Dialog dialog = LoadingDialog.createDialog(context, "正在注册，请稍后....");
        dialog.show();


        RequestParams params = new RequestParams();
        User user = new User();
        user.setMail(email);
        user.setPassword(password);
        params.put("username", user.getMail());
        params.put("password",user.getPassword());

        HttpUtil.post(registerurl, params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                dialog.dismiss();
                String back = new String(responseBody);
                if (back.equals("user has been registered")){
                    Toast.makeText(context, "该用户已经注册过", Toast.LENGTH_SHORT).show();


                }else if (back.equals("success")){
                    Toast.makeText(context, "注册成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.setClass(context, LoginActivity.class);
                    context.startActivity(intent);
                    appBase.getDataStore().edit().putBoolean("ifregister",true).commit();


                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                dialog.dismiss();
                Log.v(TAG,error.toString()+"error"+statusCode);
                Toast.makeText(context, "上传失败", Toast.LENGTH_SHORT).show();

            }

//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                super.onSuccess(statusCode, headers, response);
//                dialog.dismiss();
//                Toast.makeText(context, "上传成功", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent();
//                intent.setClass(context, LoginActivity.class);
//                context.startActivity(intent);
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                super.onFailure(statusCode, headers, responseString, throwable);
//                dialog.dismiss();
//                Toast.makeText(context, "上传失败", Toast.LENGTH_SHORT).show();
//            }
        });
    }


}
