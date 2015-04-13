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
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xiner.R;
import com.example.xiner.adapter.FilenameAdapter;
import com.example.xiner.adapter.PictureAdapter;
import com.example.xiner.adapter.ShareCommentAdapter;
import com.example.xiner.entity.Comment;
import com.example.xiner.entity.DetailItem;
import com.example.xiner.entity.FileItem;
import com.example.xiner.entity.ListItem;
import com.example.xiner.entity.User;
import com.example.xiner.util.HttpUtil;
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
    public TextView collectionText, praiseText, commentText, nicknametext, timetext, subjecttext, detailtext;
    private static final String TAG = "ShareDetailActivity";
//    private static final String collectionUrl=HttpUtil.baseUrl+
    private RecyclerView.LayoutManager mLayoutManager;
    Toolbar toolbar;
    List<FileItem> filesurl;
    ArrayList<String> pictureurl;
    ArrayList<String> audiourl;
    ArrayList<String> other;
    DetailItem item;
    List<Comment> commentList;
    ImageView zan,collection,comment;
    Long id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_detail);

        init();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.drawable.ic_ab_drawer);
        }
        RecyclerView mRecyclerview = (RecyclerView) findViewById(R.id.recyclerView_sharedetail);

        ShareCommentAdapter shareCommentAdapter = new ShareCommentAdapter(this);
        if (pictureurl != null) {
            PictureAdapter pictureAdapter = new PictureAdapter(this, pictureurl);
            GridView gridView = (GridView) findViewById(R.id.picturegridview);
            gridView.setAdapter(pictureAdapter);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String path = pictureurl.get(position);
                    Intent intent = new Intent();
                    intent.setClass(ShareDetailActivity.this,PictureDetailActivity.class);
                    intent.putExtra("picturepath",path);
                    startActivity(intent);
                }
            });
        }
//        else if(other !=null){
//            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.filenameother_recycler);
//            FilenameAdapter filenameotherAdapter = new FilenameAdapter(this,other);
//            recyclerView.setAdapter(filenameotherAdapter);
//        }else if (audiourl !=null){
//            Log.v(TAG,audiourl.size()+"size");
//            RecyclerView audiorecycler =(RecyclerView)findViewById(R.id.filenameaudio_recycler);
//            FilenameAdapter filenameaudioAdapter = new FilenameAdapter(this,audiourl);
//            audiorecycler.setAdapter(filenameaudioAdapter);
//        }
        mRecyclerview.setAdapter(shareCommentAdapter);
        mRecyclerview.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerview.setLayoutManager(mLayoutManager);
        Log.v(TAG, "hellonihao");

    }


    private void init() {

        collectionText = (TextView) findViewById(R.id.collectionnum_text);
        praiseText = (TextView) findViewById(R.id.praisenum_text);
        commentText = (TextView) findViewById(R.id.commentnum_text);
        nicknametext = (TextView) findViewById(R.id.nickname_text);
        timetext = (TextView) findViewById(R.id.time_text);
        subjecttext = (TextView) findViewById(R.id.subject_text);
        detailtext = (TextView) findViewById(R.id.detail_text);
        ClickListener clickListener = new ClickListener();
        zan =(ImageView)findViewById(R.id.idima_praise);
        zan.setOnClickListener(clickListener);
        collection =(ImageView)findViewById(R.id.idima_collection);
        collection.setOnClickListener(clickListener);
        comment =(ImageView)findViewById(R.id.idima_comment);
        comment.setOnClickListener(clickListener);

        String subject = getIntent().getExtras().getString("subject");
        String time = getIntent().getExtras().getString("time");
        String nickname = getIntent().getExtras().getString("nickname");
        String content = getIntent().getExtras().getString("content");
        String praiseNum = getIntent().getExtras().getString("praiseNum");
        String collectionNum = getIntent().getExtras().getString("collectionNum");
        String comments = getIntent().getExtras().getString("comments");
        id = getIntent().getExtras().getLong("id");
        item = (DetailItem) getIntent().getSerializableExtra("detailitem");
        Log.v(TAG, item.getUserFigure() + "firgure");
        filesurl = item.getFiles();
        pictureurl = new ArrayList<>();
        audiourl = new ArrayList<>();
        other = new ArrayList<>();
        for (int i = 0; i < filesurl.size(); i++) {
            Log.v(TAG,filesurl.get(i).getType()+"typetype");
            if (filesurl.get(i).getType()!=null) {
                if (filesurl.get(i).getType().equals("PICTURE")) {
                    pictureurl.add(filesurl.get(i).getUrl());
                }
//                else if (filesurl.get(i).getType().equals("AUDIO")) {
//                    audiourl.add(filesurl.get(i).getUrl());
//                } else {
//
//                    other.add(filesurl.get(i).getUrl());
//                }
            }
//          commentList =  item.getComments();
        }

        nicknametext.setText(nickname);
        timetext.setText(time);
        subjecttext.setText(subject);
        detailtext.setText(content);
        collectionText.setText("(" + collectionNum + ")");
        commentText.setText("(" + comments + ")");
        praiseText.setText("(" + praiseNum + ")");
        Log.v(TAG,"nihao");
    }

    class  ClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.idima_collection:

                    HttpUtil.post(HttpUtil.baseUrl+"/"+id+"/star",new JsonHttpResponseHandler(){
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);
                            Toast.makeText(ShareDetailActivity.this,"收藏成功",Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            super.onFailure(statusCode, headers, responseString, throwable);
                            Toast.makeText(ShareDetailActivity.this,"收藏失败",Toast.LENGTH_SHORT).show();
                        }
                    });

                    break;
                case R.id.idima_comment:
                    break;
                case R.id.idima_praise:
                    break;
            }

        }
    }


}
