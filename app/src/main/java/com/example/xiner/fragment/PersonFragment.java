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
import com.example.xiner.main.AppBase;
import com.example.xiner.view.CircularImage;

/**
 * Created by xiner on 14-12-20.
 */
public class PersonFragment extends Fragment {

    private static final String TAG ="PersonFragment" ;
    ImageView headedit;
    private Bitmap head;
    private final static String path = "/sdcard/sduonline/myHead/";// sd路径
    ImageView person_edit;
    CircularImage faceImage;
    ImageView sexImage;
    TextView nickname,academy,grade;

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
        View view = inflater.inflate(R.layout.fragment_person,null);
        String nicknametext= AppBase.getApp().getDataStore().getString("nickname","昵称");
        String sextext=AppBase.getApp().getDataStore().getString("sex","性别");
        String academytext=AppBase.getApp().getDataStore().getString("academy","学院");
        String gradetext=AppBase.getApp().getDataStore().getString("grade","年级");

        headedit =(ImageView)view.findViewById(R.id.person_iv_head);
//        headedit.setOnClickListener(new headeditListener());
        person_edit=(ImageView)view.findViewById(R.id.person_edit);
        Log.v(TAG,AppBase.getApp().getDataStore().getBoolean("ifeditphoto",false)+"getdatasote");
        person_edit.setOnClickListener(new personEditListener());
        faceImage =(CircularImage)view.findViewById(R.id.person_iv_head);
        nickname=(TextView)view.findViewById(R.id.individual_center_et_name);
        sexImage=(ImageView)view.findViewById(R.id.individual_center_iv_sex);
        academy=(TextView)view.findViewById(R.id.individual_center_tv_collage);
        grade=(TextView)view.findViewById(R.id.individual_center_et_grade);
        if(AppBase.getApp().getDataStore().getBoolean("ifeditphoto",false)){
            faceImage.setImageBitmap(AppBase.filetoBitmap("/sdcard/xueyou/myHead/head.jpg"));
        }
        if(nicknametext!=null){
            nickname.setText(nicknametext);
        }
        if(sextext.equals("男")){
            sexImage.setBackgroundResource(R.drawable.man);
        }else {
            sexImage.setBackgroundResource(R.drawable.woman);
        }
        if(academytext!=null){
            academy.setText(academytext);
        }
        if(gradetext!=null){
            grade.setText(gradetext);
        }

        return view;
    }


    class personEditListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(getActivity(), EditInfoActivity.class);
            getActivity().startActivity(intent);
        }
    }





    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        String nicknametext= AppBase.getApp().getDataStore().getString("nickname","昵称");
        String sextext=AppBase.getApp().getDataStore().getString("sex","性别");
        String academytext=AppBase.getApp().getDataStore().getString("academy","学院");
        String gradetext=AppBase.getApp().getDataStore().getString("grade","年级");
        if(AppBase.getApp().getDataStore().getBoolean("ifeditphoto",false)){
            faceImage.setImageBitmap(AppBase.filetoBitmap("/sdcard/xueyou/myHead/head.jpg"));
        }
        if(nicknametext!=null){
            nickname.setText(nicknametext);
        }
        if(sextext.equals("男")){
            sexImage.setBackgroundResource(R.drawable.man);
        }else {
            sexImage.setBackgroundResource(R.drawable.woman);
        }
        if(academytext!=null){
            academy.setText(academytext);
        }
        if(gradetext!=null){
            grade.setText(gradetext);
        }

    }
}
