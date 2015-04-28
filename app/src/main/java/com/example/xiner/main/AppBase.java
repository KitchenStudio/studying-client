package com.example.xiner.main;

import android.app.Application;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.example.xiner.sqlite.SqliteBase;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class AppBase extends Application {
    private static final String APP_NAME="study";
    private static final String TAG ="AppBase" ;
    SqliteBase sqliteBase;

    public static AppBase app;
    SQLiteDatabase db;


   public SharedPreferences getDataStore(){
       return this.getSharedPreferences(APP_NAME,MODE_PRIVATE);
   }

    @Override
    public void onCreate() {
        super.onCreate();
        app=this;
        sqliteBase = new SqliteBase(this,"xueyou.db",null,2);
        sqliteBase.getWritableDatabase();
    }

    public static AppBase getApp(){
        return app;
    }

    public static Bitmap filetoBitmap(String path){
        FileInputStream fis=null;
        Bitmap bit = null;
        try{
            fis=new FileInputStream(path);
            bit = BitmapFactory.decodeStream(fis);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            try {
                if (fis!=null) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bit;
    }

    //保存头像
    public void setPicToView(Bitmap mBitmap,String path) {

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

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {

        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;

        Log.v(TAG, reqWidth + "requestwaa" + reqHeight + "requestHaaa" + height + "height" + width + "width");
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        Log.v(TAG, inSampleSize + "sample");
        return inSampleSize;
    }

}