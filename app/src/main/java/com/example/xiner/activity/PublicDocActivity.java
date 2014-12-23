package com.example.xiner.activity;

import android.app.DownloadManager;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.xiner.R;
import com.example.xiner.util.twitter;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class PublicDocActivity extends ActionBarActivity {
    private ImageView uploadFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_doc);
        uploadFile =(ImageView)findViewById(R.id.image_upload);
        uploadFile.setOnClickListener(new uploadListener());
    }

    class uploadListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            RequestParams params = new RequestParams();
            params.put("content", "dsfsf");
            Log.v("PublicDocc",uri+"");
            try {
                params.put("file", new File(new URI(uri.toString())));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

            twitter.post("http://211.87.234.180:8080/studying/api/v1/item/files",params,new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    Log.v("PublicDocA",statusCode+"");

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Log.v("PublicDocAf",statusCode+"");
                }
            });

        }
    }
    static Uri uri;
    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        Log.v("publicdoc",bundle+"");
        if(bundle!=null){
        uri = (Uri)bundle.get(Intent.EXTRA_STREAM);
        Log.v("PublicDoc",uri+"");}
        //对于大图片未做优化处理
       // mImageView.setImageURI(image);
    }




}
