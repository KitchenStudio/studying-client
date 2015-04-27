package com.example.xiner.net;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.xiner.activity.LoginActivity;
import com.example.xiner.activity.MainActivity;
import com.example.xiner.entity.User;
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
public class LoginNetwork {

    public static final String loginurl =HttpUtil.baseUserUrl+"/login";

    Context context;
    public LoginNetwork(Context context){
        this.context = context;
    }

    public void loginUpload(String email,String password) {

        final Dialog dialog = LoadingDialog.createDialog(context, "正在上传，请稍后....");
        dialog.show();


        RequestParams params = new RequestParams();
        User user = new User();
        user.setMail(email);
        user.setPassword(password);
        params.put("username", email);
        params.put("password",password);
        HttpUtil.post(loginurl, params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                dialog.dismiss();
                String result = new String(responseBody);
                if (result.equals("login success")){
                    Toast.makeText(context, "登陆成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.setClass(context, MainActivity.class);
                    context.startActivity(intent);
                }else if (result.equals("password error")){
                    Toast.makeText(context, "密码错误", Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(context, "用户不存在", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                dialog.dismiss();
                Toast.makeText(context, "登陆失败", Toast.LENGTH_SHORT).show();

            }

//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                super.onSuccess(statusCode, headers, response);
//                dialog.dismiss();
//
//                Toast.makeText(context, "上传成功", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent();
//                intent.setClass(context, MainActivity.class);
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
