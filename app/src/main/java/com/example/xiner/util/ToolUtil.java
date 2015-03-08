package com.example.xiner.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by xiner on 15-1-2.
 */
public class ToolUtil {
    public static Bitmap getLocalBitMap(String path){
        Bitmap bitmap = null;
        try {
            FileInputStream inputStream = new FileInputStream(path);
            bitmap = BitmapFactory.decodeFile(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return  bitmap;
    }
}
