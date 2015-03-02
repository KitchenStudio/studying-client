package com.example.xiner.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;

import com.example.xiner.R;
import com.example.xiner.adapter.ShareCommentAdapter;

/**
 * Created by xiner on 14-12-22.
 */
public class DetailShareActivity extends ActionBarActivity {
    private static final String TAG = "DetailShareActivity";
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_detail);
        String subject=getIntent().getExtras().getString("subject");
        String grade=getIntent().getExtras().getString("grade");
        String time=getIntent().getExtras().getString("time");
        String detail=getIntent().getExtras().getString("detail");
        RecyclerView mRecyclerview =(RecyclerView)findViewById(R.id.recyclerView_sharedetail);
        ShareCommentAdapter shareCommentAdapter = new ShareCommentAdapter();
        mRecyclerview.setAdapter(shareCommentAdapter);
        mRecyclerview.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerview.setLayoutManager(mLayoutManager);

    }
}
