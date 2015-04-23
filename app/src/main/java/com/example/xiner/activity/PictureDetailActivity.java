package com.example.xiner.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.example.xiner.R;
import com.example.xiner.main.AppBase;
import com.example.xiner.util.DownloadPicUtil;
import com.example.xiner.util.HttpUtil;

import java.io.File;

public class PictureDetailActivity extends ActionBarActivity {

    private static final String TAG = "PictureDetailActivity";
    String path = Environment.getExternalStorageDirectory()+"/xueyou/originpic/upload";
    String picpath = Environment.getExternalStorageDirectory()+"/xueyou/originpic";
    ImageView imageView;
    AppBase app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_detail);
        app = AppBase.getApp();
        String  picture = (String)getIntent().getExtras().get("picturepath");
        imageView=(ImageView)findViewById(R.id.picturedetail);
        int screenWidth  = getWindowManager().getDefaultDisplay().getWidth();
        int screenHeight = getWindowManager().getDefaultDisplay().getHeight();
        File file = new File(picpath+picture);
        if (file.exists()){
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;// 设置成了true,不占用内存，只获取bitmap宽高

            BitmapFactory.decodeFile(file.getPath(),opts);

            opts.inJustDecodeBounds=false;

            opts.inSampleSize =app.calculateInSampleSize(opts,screenWidth,screenHeight);
            Bitmap bitmap = BitmapFactory.decodeFile(file.getPath(),opts);
            imageView.setImageBitmap(bitmap);
        }else {
            File file1 = new File(path);
            file1.mkdirs();
            DownloadPicUtil downloadPicUtil = new DownloadPicUtil(imageView, new File(picpath + picture), screenWidth, screenHeight);
            downloadPicUtil.execute(HttpUtil.baseIp + picture);
        }
    }

//    public static int calculateInSampleSize(
//            BitmapFactory.Options options, int reqWidth, int reqHeight) {
//
//        // Raw height and width of image
//        final int height = options.outHeight;
//        final int width = options.outWidth;
//
//        Log.v(TAG, reqWidth + "requestwaa" + reqHeight + "requestHaaa" + height + "height" + width + "width");
//        int inSampleSize = 1;
//
//        if (height > reqHeight || width > reqWidth) {
//
//            final int halfHeight = height / 2;
//            final int halfWidth = width / 2;
//
//            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
//            // height and width larger than the requested height and width.
//            while ((halfHeight / inSampleSize) > reqHeight
//                    && (halfWidth / inSampleSize) > reqWidth) {
//                inSampleSize *= 2;
//            }
//        }
//        Log.v(TAG,inSampleSize+"sample");
//        return inSampleSize;
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_picture_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
