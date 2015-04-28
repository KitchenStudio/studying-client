package com.example.xiner.fragment;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xiner.R;
import com.example.xiner.activity.EditInfoActivity;
import com.example.xiner.entity.User;
import com.example.xiner.main.AppBase;
import com.example.xiner.net.ShareNetwork;
import com.example.xiner.util.DownloadPicUtil;
import com.example.xiner.util.HttpUtil;
import com.example.xiner.view.CircularImage;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import java.io.File;

/**
 * Created by xiner on 14-12-20.
 */
public class PersonFragment extends Fragment {

    private static final String TAG = "PersonFragment";
//    ImageView headedit;
    private Bitmap head;
//    private final static String path = "/sdcard/sduonline/myHead/";// sd路径
    String picpath = "/sdcard/xueyou/myHead/head.jpg";
    String httpInfo=HttpUtil.baseUserUrl;
    ImageView person_edit;
    CircularImage faceImage;
    ImageView sexImage;
    TextView nickname, ageText, grade;
    String username;
ShareNetwork shareNetwork;
    public static PersonFragment newInstance(int position) {
        PersonFragment f = new PersonFragment();
        Bundle b = new Bundle();
        b.putInt("position", position);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_person, null);
        shareNetwork = new ShareNetwork();
//        downloadInfo();
        username = AppBase.getApp().getDataStore().getString("username", "18366116016");

        String nicknametext = AppBase.getApp().getDataStore().getString("nickname", "昵称");
        String sextext = AppBase.getApp().getDataStore().getString("sex", "性别");
        int academytext = AppBase.getApp().getDataStore().getInt("age", 0);
        String gradetext = AppBase.getApp().getDataStore().getString("grade", "年级");

//        headedit = (ImageView) view.findViewById(R.id.person_iv_head);
//        headedit.setOnClickListener(new headeditListener());
        person_edit = (ImageView) view.findViewById(R.id.person_edit);
//        Log.v(TAG, AppBase.getApp().getDataStore().getBoolean("ifeditphoto", false) + "getdatasote");
        person_edit.setOnClickListener(new personEditListener());
        faceImage = (CircularImage) view.findViewById(R.id.person_iv_head);
        nickname = (TextView) view.findViewById(R.id.individual_center_et_name);
        sexImage = (ImageView) view.findViewById(R.id.individual_center_iv_sex);
        ageText = (TextView) view.findViewById(R.id.individual_center_tv_age);
        grade = (TextView) view.findViewById(R.id.individual_center_et_grade);
//        faceImage.setImageDrawable(getResources().getDrawable(R.drawable.faceimage));
        File file = new File(picpath);
        if (file.exists()) {
            Log.v(TAG,"exis");
            Bitmap bitmap = AppBase.filetoBitmap("/sdcard/xueyou/myHead/head.jpg");
            if (bitmap != null) {
                faceImage.setImageBitmap(bitmap);

            }
////
        } else {
            faceImage.setImageDrawable(getResources().getDrawable(R.drawable.faceimage));

        }
//        Log.v(TAG, bitmap + "bitmapbitmap");

//        if (bitmap!=null){
//            faceImage.setImageBitmap(bitmap);
//
//        }
////        else{
//            Log.v(TAG,"here bitmap is");
//            faceImage.setImageDrawable(getResources().getDrawable(R.drawable.faceimage));
//        }
//        if(AppBase.getApp().getDataStore().getBoolean("ifeditphoto",false)){
//            faceImage.setImageBitmap(AppBase.filetoBitmap("/sdcard/xueyou/myHead/head.jpg"));
//        }
        if (nicknametext != null) {
            nickname.setText(nicknametext);
        }
        if (sextext.equals("男")) {
            sexImage.setBackgroundResource(R.drawable.man);
        } else {
            sexImage.setBackgroundResource(R.drawable.woman);
        }
//        if(academytext!=null){
            ageText.setText(String.valueOf(academytext));
//        }
        if (gradetext != null) {
            grade.setText(username);
        }

        return view;
    }


    class personEditListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(getActivity(), EditInfoActivity.class);
            getActivity().startActivity(intent);
        }
    }

    private void downloadInfo(){
        HttpUtil.get(httpInfo+"/"+username+"/info",new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
              User user = shareNetwork.ParseUser(response);
//                AppBase.getApp().getDataStore().edit().putBoolean("ifeditphoto", true).commit();
                AppBase.getApp().getDataStore().edit().putString("nickname", user.getNickname()).commit();
//                AppBase.getApp().getDataStore().edit().putString("sex", sexString).commit();
                AppBase.getApp().getDataStore().edit().putInt("age", user.getAge()).commit();
                AppBase.getApp().getDataStore().edit().putString("username", user.getUsername()).commit();
                AppBase.getApp().getDataStore().edit().putString("filefigure", user.getFileFigure().getUrl()).commit();
//                DownloadPicUtil downloadPicUtil = new DownloadPicUtil(faceImage, new File(picpath + nameFile), 50, 50);
//                downloadPicUtil.execute(HttpUtil.baseIp + detailItem.getUserFigure());



            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        String nicknametext = AppBase.getApp().getDataStore().getString("nickname", "昵称");
        String sextext = AppBase.getApp().getDataStore().getString("sex", "性别");
        int academytext = AppBase.getApp().getDataStore().getInt("age", 0);
        String gradetext = AppBase.getApp().getDataStore().getString("grade", "年级");
        File file = new File(picpath);
        if (file.exists()) {
            Log.v(TAG,"exis");
            Bitmap bitmap = AppBase.filetoBitmap("/sdcard/xueyou/myHead/head.jpg");
            if (bitmap != null) {
                faceImage.setImageBitmap(bitmap);

            }
////
        } else {
            faceImage.setImageDrawable(getResources().getDrawable(R.drawable.faceimage));

        }
        if (nicknametext != null) {
            nickname.setText(nicknametext);
        }
        if (sextext.equals("男")) {
            sexImage.setBackgroundResource(R.drawable.man);
        } else {
            sexImage.setBackgroundResource(R.drawable.woman);
        }
//        if(academytext!=null){
//            academy.setText(academytext);
//        }
//        ageText.setText(academytext);
        if (gradetext != null) {
            grade.setText(gradetext);
        }

    }
}
