package com.example.xiner.net;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.xiner.activity.LoginActivity;
import com.example.xiner.entity.User;
import com.example.xiner.util.HttpUtil;
import com.example.xiner.util.LoadingDialog;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

/**
 * Created by xiner on 15-3-23.
 */
public class RegisterNetwork {

    Context context;

    public RegisterNetwork(Context context) {
        this.context = context;
    }

    public static final String registerurl = "";

    public void uploadRegister(String email, String password) {

        final Dialog dialog = LoadingDialog.createDialog(context, "正在上传，请稍后....");
        dialog.show();


        RequestParams params = new RequestParams();
        User user = new User();
        user.setMail(email);
        user.setPassword(password);
        params.put("user", user);

        HttpUtil.post(registerurl, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                dialog.dismiss();
                Toast.makeText(context, "上传成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setClass(context, LoginActivity.class);
                context.startActivity(intent);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                dialog.dismiss();
                Toast.makeText(context, "上传失败", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
