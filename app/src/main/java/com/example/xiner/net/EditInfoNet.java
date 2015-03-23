package com.example.xiner.net;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.xiner.entity.User;
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
    private static final String editInfourl = "";

    public EditInfoNet(Context context) {
        this.context = context;
    }

    public void EditInfo(String file, String nickname, String academy, String sex) {

        final Dialog dialog = LoadingDialog.createDialog(context, "正在上传，请稍后....");
        dialog.show();

        RequestParams params = new RequestParams();
        User form = new User();
        form.setNickname(nickname);
//        form.se
        params.put("nickname", nickname);
        params.put("academy", academy);
        params.put("sex", sex);

        try {
            params.put("file", new File(new URI(file)), "multipart/form-data");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }


        HttpUtil.post(editInfourl, params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.v("PublicDocA", statusCode + "codesuccess");

                dialog.dismiss();
                Toast.makeText(context, "上传成功", Toast.LENGTH_SHORT).show();
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
