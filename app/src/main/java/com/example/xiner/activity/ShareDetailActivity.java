package com.example.xiner.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xiner.R;
import com.example.xiner.adapter.FileAdapter;
import com.example.xiner.adapter.FilenameAdapter;
import com.example.xiner.adapter.PictureAdapter;
import com.example.xiner.adapter.ShareCommentAdapter;
import com.example.xiner.adapter.ShareDetailAdapter;
import com.example.xiner.entity.Comment;
import com.example.xiner.entity.DetailItem;
import com.example.xiner.entity.FileItem;
import com.example.xiner.entity.ListItem;
import com.example.xiner.entity.User;
import com.example.xiner.util.HttpUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.utils.StorageUtils;

import org.apache.http.Header;
import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (android.R.id.home == item.getItemId()) {
            finish();
            return false;
        }
        return super.onOptionsItemSelected(item);
    }

}
