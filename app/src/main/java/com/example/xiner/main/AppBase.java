package com.example.xiner.main;

import android.app.Application;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class AppBase extends Application {

    public static AppBase app;


   public SharedPreferences getDataStore(){
       return this.getSharedPreferences("xueyou",MODE_PRIVATE);
   }

    @Override
    public void onCreate() {
        super.onCreate();
        app=this;
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