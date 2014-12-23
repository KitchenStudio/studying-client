package com.example.xiner.activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.xiner.R;

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

        }
    }

}
