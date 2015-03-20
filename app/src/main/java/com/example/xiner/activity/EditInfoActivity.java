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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.xiner.fragment.PersonFragment;
import com.example.xiner.main.AppBase;

import com.example.xiner.R;
import com.example.xiner.net.InfoNet;
import com.example.xiner.view.CircularImage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class EditInfoActivity extends ActionBarActivity {

    CircularImage editPhoto;
    private Bitmap head;
    Button saveButton;
    EditText nickname,sex,academy,grade;
    private final static String path = "/sdcard/xueyou/myHead/";// sd路径
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);

        String nicknametext= AppBase.getApp().getDataStore().getString("nickname","昵称");
        String sextext=AppBase.getApp().getDataStore().getString("sex","性别");
        String academytext=AppBase.getApp().getDataStore().getString("academy","学院");
        String gradetext=AppBase.getApp().getDataStore().getString("grade","年级");

        editPhoto =(CircularImage)findViewById(R.id.edit_cover_user_photo);
        editPhoto.setOnClickListener(new headeditListener());
        if(AppBase.getApp().getDataStore().getBoolean("ifeditphoto",false)){
            editPhoto.setImageBitmap(AppBase.filetoBitmap("/sdcard/xueyou/myHead/head.jpg"));
        }
        saveButton=(Button)findViewById(R.id.save_button);
        saveButton.setOnClickListener(new saveListener());
        nickname=(EditText)findViewById(R.id.nickname_edit);
        sex=(EditText)findViewById(R.id.sex_edit);
        academy=(EditText)findViewById(R.id.academy_edit);
        grade=(EditText)findViewById(R.id.grade_edit);

        if(nicknametext!=null){
            nickname.setText(nicknametext);
        }
        if(sextext!=null){
          sex.setText(sextext);
        }
        if(academytext!=null){
            academy.setText(academytext);
        }
        if(gradetext!=null){
            grade.setText(gradetext);
        }

    }

    class saveListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {


            new Thread(new Runnable() {
                @Override
                public void run() {
                    InfoNet infoNet = new InfoNet();
                    infoNet.uploadFile(path + "head.jpg");
                }
            }).start();

            if(nickname.getText()==null || sex.getText()==null || academy.getText() ==null || grade.getText() ==null){
                Toast.makeText(EditInfoActivity.this,"请将信息填写完整",Toast.LENGTH_SHORT).show();
            }else {
                AppBase.getApp().getDataStore().edit().putBoolean("ifeditphoto", true).commit();
                AppBase.getApp().getDataStore().edit().putString("nickname", nickname.getText().toString()).commit();
                AppBase.getApp().getDataStore().edit().putString("sex", sex.getText().toString()).commit();
                AppBase.getApp().getDataStore().edit().putString("academy", academy.getText().toString()).commit();
                AppBase.getApp().getDataStore().edit().putString("grade", grade.getText().toString()).commit();
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
                        setPicToView(head);// 保存在SD卡中
                        editPhoto.setImageBitmap(head);// 用ImageView显示出来
                    }
                }
                break;
            default:
                break;

        }
    }
    //保存头像
    private void setPicToView(Bitmap mBitmap) {

        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            return;
        }
        FileOutputStream b = null;
        File file = new File(path);
        file.mkdirs();// 创建文件夹
        String fileName = path + "head.jpg";// 图片名字
        try {
            b = new FileOutputStream(fileName);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                // 关闭流
                b.flush();
                b.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

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
