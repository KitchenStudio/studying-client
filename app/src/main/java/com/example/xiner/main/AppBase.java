package com.example.xiner.main;

import android.app.Application;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.xiner.sqlite.SqliteBase;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class AppBase extends Application {

    private static final String TAG ="AppBase" ;
    SqliteBase sqliteBase;

    public static AppBase app;
    SQLiteDatabase db;


   public SharedPreferences getDataStore(){
       return this.getSharedPreferences("xueyou",MODE_PRIVATE);
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
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bit;
    }
}