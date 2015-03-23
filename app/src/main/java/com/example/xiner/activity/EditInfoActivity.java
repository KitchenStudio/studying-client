package com.example.xiner.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.example.xiner.main.AppBase;

import com.example.xiner.R;
import com.example.xiner.net.EditInfoNet;
import com.example.xiner.util.HttpUtil;
import com.example.xiner.view.CircularImage;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class EditInfoActivity extends ActionBarActivity {

    CircularImage editPhoto;
    private Bitmap head;
    Button saveButton;
    RadioGroup sexgroup;
    EditText nickname,academy;
    private final static String path = Environment.getExternalStorageDirectory()+"/xueyou/myHead/";// sd路径
    Toolbar toolbar;
    String sexString;
    AppBase appBase;
    RadioButton manButton,womanButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);
        appBase = AppBase.getApp();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.drawable.ic_ab_drawer);
        }
        String nicknametext= AppBase.getApp().getDataStore().getString("nickname","昵称");
        sexString=AppBase.getApp().getDataStore().getString("sex","男");
        String academytext=AppBase.getApp().getDataStore().getString("academy","学院");


        sexgroup =(RadioGroup)findViewById(R.id.sex_group);
        manButton =(RadioButton)findViewById(R.id.man_sex);
        womanButton=(RadioButton)findViewById(R.id.woman_sex);
        sexgroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
               if (checkedId == R.id.man_sex){
                   sexString = "男";
               }else {
                   sexString ="女";
               }
            }
        });

        editPhoto =(CircularImage)findViewById(R.id.edit_cover_user_photo);
        editPhoto.setOnClickListener(new headeditListener());
        if(AppBase.getApp().getDataStore().getBoolean("ifeditphoto",false)){
            editPhoto.setImageBitmap(AppBase.filetoBitmap("/sdcard/xueyou/myHead/head.jpg"));
        }
        saveButton=(Button)findViewById(R.id.save_button);
        saveButton.setOnClickListener(new saveListener());
        nickname=(EditText)findViewById(R.id.nickname_edit);

        academy=(EditText)findViewById(R.id.academy_edit);


        if(nicknametext!=null){
            nickname.setText(nicknametext);
        }
        if(academytext!=null){
            academy.setText(academytext);
        }

        if (sexString.equals("男")){
            manButton.setChecked(true);
        }else {
            womanButton.setChecked(true);
        }
    }

    class saveListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {




            if(nickname.getText()==null  || academy.getText() ==null){
                Toast.makeText(EditInfoActivity.this,"请将信息填写完整",Toast.LENGTH_SHORT).show();
            }else {
                AppBase.getApp().getDataStore().edit().putBoolean("ifeditphoto", true).commit();
                AppBase.getApp().getDataStore().edit().putString("nickname", nickname.getText().toString()).commit();
                AppBase.getApp().getDataStore().edit().putString("sex", sexString).commit();
                AppBase.getApp().getDataStore().edit().putString("academy", academy.getText().toString()).commit();


                EditInfoNet editInfoNet = new EditInfoNet(EditInfoActivity.this);
                editInfoNet.EditInfo(path+"head.jpg",nickname.getText().toString(),academy.getText().toString(),sexString);

            }

           finish();
        }
    }

    class headeditListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            new android.app.AlertDialog.Builder(EditInfoActivity.this)
                    .setTitle("头像选择")
                    .setNegativeButton("相册选取",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.cancel();
                                    Intent intent1 = new Intent(
                                            Intent.ACTION_PICK, null);
                                    intent1.setDataAndType(
                                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                            "image/*");
                                    startActivityForResult(intent1, 1);
                                }
                            })
                    .setPositiveButton("相机拍照",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.cancel();
                                    String status = Environment
                                            .getExternalStorageState();
                                    if (status
                                            .equals(Environment.MEDIA_MOUNTED)) {// 判断是否有SD卡
                                        Intent intent2 = new Intent(
                                                MediaStore.ACTION_IMAGE_CAPTURE);
                                        intent2.putExtra(
                                                MediaStore.EXTRA_OUTPUT,
                                                Uri.fromFile(new File(
                                                        Environment
                                                                .getExternalStorageDirectory(),
                                                        "head.jpg")));
                                        startActivityForResult(intent2, 2);// 采用ForResult打开
                                    }
                                }
                            }).show();
        }
    }

    public void cropPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 3);


    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == Activity.RESULT_OK) {
                    cropPhoto(data.getData());// 裁剪图片
                }

                break;
            case 2:
                if (resultCode == Activity.RESULT_OK) {
                    File temp = new File(Environment.getExternalStorageDirectory()
                            + "/head.jpg");
                    cropPhoto(Uri.fromFile(temp));// 裁剪图片
                }

                break;
            case 3:
                if (data != null) {
                    Bundle extras = data.getExtras();
                    head = extras.getParcelable("data");
                    if (head != null) {
                        appBase.setPicToView(head,path);// 保存在SD卡中
                        editPhoto.setImageBitmap(head);// 用ImageView显示出来
                    }
                }
                break;
            default:
                break;

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }
}
