package com.example.xiner.fragment;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.xiner.R;
import com.example.xiner.activity.ChangePasswordActivity;
import com.example.xiner.activity.EditInfoActivity;
import com.example.xiner.activity.LoginActivity;
import com.example.xiner.entity.User;
import com.example.xiner.main.AppBase;
import com.example.xiner.net.ShareNetwork;
import com.example.xiner.util.DownloadFileUtil;
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
    String picdownpath = Environment.getExternalStorageDirectory() + "/xueyou/thumbnail/userFigure";

    private CircularImage faceImage;
    private ImageView sexImage;
    private TextView nickname, ageText, grade;
    private String username;
    private ShareNetwork shareNetwork;
    private RelativeLayout changepassword_rel, changeInfo_rel, logout_rel;
    private boolean ifgetUserFigure;
    private SwipeRefreshLayout swipeRefreshLayout;

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
        username = AppBase.getApp().getDataStore().getString("username", "18366116016");
        String nicknametext = AppBase.getApp().getDataStore().getString("nickname", "昵称");
        String sextext = AppBase.getApp().getDataStore().getString("sex", "性别");
        int academytext = AppBase.getApp().getDataStore().getInt("age", 0);
        String gradetext = AppBase.getApp().getDataStore().getString("grade", "年级");
        ifgetUserFigure = AppBase.getApp().getDataStore().getBoolean("ifuserFigure", false);
        ClickListener clickListener = new ClickListener();
        RefreshListener refreshListener = new RefreshListener();
        faceImage = (CircularImage) view.findViewById(R.id.person_iv_head);
        nickname = (TextView) view.findViewById(R.id.individual_center_et_name);
        sexImage = (ImageView) view.findViewById(R.id.individual_center_iv_sex);
        ageText = (TextView) view.findViewById(R.id.individual_center_tv_age);
        grade = (TextView) view.findViewById(R.id.individual_center_et_grade);
        changepassword_rel = (RelativeLayout) view.findViewById(R.id.changepassword_rel);
        swipeRefreshLayout =(SwipeRefreshLayout)view.findViewById(R.id.person_swipe);
        swipeRefreshLayout.setOnRefreshListener(refreshListener);
        changepassword_rel.setOnClickListener(clickListener);
        changeInfo_rel = (RelativeLayout) view.findViewById(R.id.changeindo);
        logout_rel = (RelativeLayout) view.findViewById(R.id.logout);
        changeInfo_rel.setOnClickListener(clickListener);
        logout_rel.setOnClickListener(clickListener);


        if (nicknametext != null) {
            nickname.setText(nicknametext);
        }
        if (sextext.equals("男")) {
            sexImage.setBackgroundResource(R.drawable.man);
        } else {
            sexImage.setBackgroundResource(R.drawable.woman);
        }
        ageText.setText(String.valueOf(academytext));
        if (gradetext != null) {
            grade.setText(username);
        }


        return view;
    }

    class RefreshListener implements SwipeRefreshLayout.OnRefreshListener{

        @Override
        public void onRefresh() {
            downloadInfo(username);
        }
    }

    class ClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()) {

                case R.id.changepassword_rel:
                    intent = new Intent();
                    intent.setClass(getActivity(), ChangePasswordActivity.class);
                    startActivity(intent);
                    break;
                case R.id.changeindo:
                    Log.v(TAG,"changeinfochangeinfo");
                    intent = new Intent();
                    intent.setClass(getActivity(), EditInfoActivity.class);
                    startActivity(intent);
                    break;
                case R.id.logout:
                    AppBase.getApp().getDataStore().edit().remove("username").remove("password")
                            .remove("nickname").remove("age").remove("userFigure")
                            .remove("ifuserFigure").commit();
                    intent.setClass(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    }

    private void downloadInfo(final String username){
        HttpUtil.get(HttpUtil.baseUserUrl+"/"+username+"/info",new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                User user = shareNetwork.ParseUser(response);
                swipeRefreshLayout.setRefreshing(false);
                Log.v(TAG,user+"useruseruser");
                if (user!=null) {

                    if (user.getNickname() != null) {
                        nickname.setText(user.getNickname());
                        AppBase.getApp().getDataStore().edit().putString("nickname", user.getNickname()).commit();
                    }
                    AppBase.getApp().getDataStore().edit().putInt("age", user.getAge()).commit();
                    ageText.setText(String.valueOf(user.getAge()));

                    if (user.getUsername() != null) {
                        grade.setText(username);
                        AppBase.getApp().getDataStore().edit().putString("username", user.getUsername()).commit();
                    }
                    if (user.getFileFigure()!=null) {
                        String Figure = user.getFileFigure().getUrl();
                        if (!Figure.equals("null")) {
                            String nameFile = Figure.substring(Figure.lastIndexOf("/"));
                            Log.v(TAG, nameFile + "namefilenamefile");

                            File file = new File(picdownpath + nameFile);
                            if (file.exists()) {
                                Log.v(TAG, "fileexistsfileexists");
                                BitmapFactory.Options opts = new BitmapFactory.Options();
                                opts.inJustDecodeBounds = true;// 设置成了true,不占用内存，只获取bitmap宽高

                                BitmapFactory.decodeFile(file.getPath(), opts);

                                opts.inJustDecodeBounds = false;

                                opts.inSampleSize = AppBase.getApp().calculateInSampleSize(opts, 50, 50);
                                Log.v(TAG, file.getPath() + "file.getPath");
                                Bitmap bitmap = BitmapFactory.decodeFile(file.getPath(), opts);

                                faceImage.setImageBitmap(bitmap);
                            } else {
                                Log.v(TAG, "fileexistsfileexistsnot");

                                File file1 = new File(picdownpath);
                                file1.mkdirs();
                                //下载图片
                                DownloadPicUtil downloadPicUtil = new DownloadPicUtil(faceImage, new File(picdownpath + nameFile), 100, 100);
                                downloadPicUtil.execute(HttpUtil.baseIp + Figure);
                            }
                        }
                    }

                }

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
        downloadInfo(username);

    }
}
