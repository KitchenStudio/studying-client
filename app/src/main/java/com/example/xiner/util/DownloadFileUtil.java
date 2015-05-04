package com.example.xiner.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.example.xiner.main.AppBase;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by xiner on 4/23/15.
 */
public class DownloadFileUtil extends AsyncTask<String,Void,String> {
    private static  String USERNAME = "";
    private static  String PASSWORD = "";
    private static final String TAG = "DownloadFileUtil";
    private File file;
    Context context;
    AppBase app;
    public DownloadFileUtil(File file,Context context){
        this.file = file;
        this.context =context;
        app = AppBase.getApp();
        USERNAME = app.getDataStore().getString("username","1");
        PASSWORD = app.getDataStore().getString("password","..xiao");
    }
    @Override
    protected String doInBackground(String... params) {
        return downloadFile(params[0]);
    }

    private String downloadFile(String url){
        AndroidHttpClient client = AndroidHttpClient.newInstance("Android");
        String credentials = USERNAME + ":" + PASSWORD;
        String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);


        final org.apache.http.client.methods.HttpGet getRequest = new org.apache.http.client.methods.HttpGet(url);
        getRequest.addHeader("Authorization", "Basic " + base64EncodedCredentials);
        try {
            HttpResponse response = client.execute(getRequest);
            final int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                Toast.makeText(context,"下载文件失败，请检查网络设置",Toast.LENGTH_SHORT).show();
                Log.v(TAG, "Error " + statusCode
                        + " while retrieving  from " + url);
                return null;
            }

            final HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream inputStream = null;
                try {
                    inputStream = entity.getContent();
                    inputTofile(inputStream, file);
                    Toast.makeText(context,"文件所在位置"+file.getPath(),Toast.LENGTH_LONG).show();
                    return file.getPath();
                } finally {

                    entity.consumeContent();
                }
            }
        } catch (Exception e) {
            // Could provide a more explicit error message for IOException or
            // IllegalStateException
            getRequest.abort();
            Log.w("ImageDownloader", "Error while retrieving bitmap from " + url);
        } finally {
            if (client != null) {
                client.close();
            }
        }
        return null;
    }

    private void inputTofile(InputStream inputStream,File file){
        OutputStream out=null;
        try {
            out = new FileOutputStream(file);
            int byteRead=0;
            byte [] buffer = new byte[8192];
            while((byteRead=inputStream.read(buffer,0,8192))!=-1){
                out.write(buffer,0,byteRead);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (out!=null){
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream!=null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
