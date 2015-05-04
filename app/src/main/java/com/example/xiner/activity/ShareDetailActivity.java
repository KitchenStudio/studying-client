package com.example.xiner.activity;

import android.os.Bundle;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.example.xiner.R;
import com.example.xiner.adapter.ShareDetailAdapter;
import com.example.xiner.entity.Comment;
import com.example.xiner.entity.DetailItem;
import com.example.xiner.entity.ListItem;

import java.util.List;

/**
 * Created by xiner on 14-12-22.
 */
public class ShareDetailActivity extends ActionBarActivity {
    private static final String TAG = "ShareDetailActivity";
    private RecyclerView.LayoutManager mLayoutManager;
    Toolbar toolbar;

    DetailItem detailItem;
    List<Comment> commentList;
    Long id;
    ListItem listitem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sharedetail);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.backarrow);
        RecyclerView mRecyclerview = (RecyclerView) findViewById(R.id.recyclerView_sharedetail);

        listitem = (ListItem) getIntent().getSerializableExtra("listitem");
        Log.v(TAG, listitem.getContent() + "contentcontent");
        detailItem = (DetailItem) getIntent().getSerializableExtra("detailitem");
        commentList = detailItem.getComments();
        Log.v(TAG,commentList.size()+"commentcommentcommentsize");
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerview.setLayoutManager(mLayoutManager);
        ShareDetailAdapter shareDetailAdapter = new ShareDetailAdapter(this,commentList,detailItem,listitem);
        mRecyclerview.setAdapter(shareDetailAdapter);

    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (android.R.id.home == item.getItemId()) {
            finish();
            return false;
        }
        return super.onOptionsItemSelected(item);
    }

}
