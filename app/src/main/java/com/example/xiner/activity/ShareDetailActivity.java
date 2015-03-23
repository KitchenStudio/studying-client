package com.example.xiner.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xiner.R;
import com.example.xiner.adapter.PictureAdapter;
import com.example.xiner.adapter.ShareCommentAdapter;
import com.example.xiner.entity.Comment;
import com.example.xiner.entity.DetailItem;
import com.example.xiner.entity.FileItem;
import com.example.xiner.entity.ListItem;
import com.example.xiner.entity.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiner on 14-12-22.
 */
public class ShareDetailActivity extends ActionBarActivity {
    public TextView collectionText, praiseText, commentText, nicknametext, timetext, subjecttext, detailtext;
    private static final String TAG = "DetailShareActivity";
    private RecyclerView.LayoutManager mLayoutManager;
    Toolbar toolbar;
    List<FileItem> filesurl;
    ArrayList<String> pictureurl;
    ArrayList<String> audiourl;
    ArrayList<String> other;
    DetailItem item;
    List<Comment> commentList;

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
        }
        mRecyclerview.setAdapter(shareCommentAdapter);
        mRecyclerview.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerview.setLayoutManager(mLayoutManager);

    }

    private void init() {

        collectionText = (TextView) findViewById(R.id.collectionnum_text);
        praiseText = (TextView) findViewById(R.id.praisenum_text);
        commentText = (TextView) findViewById(R.id.commentnum_text);
        nicknametext = (TextView) findViewById(R.id.nickname_text);
        timetext = (TextView) findViewById(R.id.time_text);
        subjecttext = (TextView) findViewById(R.id.subject_text);
        detailtext = (TextView) findViewById(R.id.detail_text);

        String subject = getIntent().getExtras().getString("subject");
        String time = getIntent().getExtras().getString("time");
        String nickname = getIntent().getExtras().getString("nickname");
        String content = getIntent().getExtras().getString("content");
        String praiseNum = getIntent().getExtras().getString("praiseNum");
        String collectionNum = getIntent().getExtras().getString("collectionNum");
        String comments = getIntent().getExtras().getString("comments");
        item = (DetailItem) getIntent().getSerializableExtra("detailitem");
        Log.v(TAG, item.getUserFigure() + "firgure");
        filesurl = item.getFiles();
        for (int i = 0; i < filesurl.size(); i++) {
            if (filesurl.get(i).getType().equals("PICTURE")) {
                pictureurl = new ArrayList<>();
                pictureurl.add(filesurl.get(i).getUrl());

            } else if (filesurl.get(i).getType().equals("AUDIO")) {
                audiourl = new ArrayList<>();
                audiourl.add(filesurl.get(i).getUrl());
            } else {
                other = new ArrayList<>();
                other.add(filesurl.get(i).getUrl());
            }
          commentList =  item.getComments();
        }

        nicknametext.setText(nickname);
        timetext.setText(time);
        subjecttext.setText(subject);
        detailtext.setText(content);
        collectionText.setText("(" + collectionNum + ")");
        commentText.setText("(" + comments + ")");
        praiseText.setText("(" + praiseNum + ")");
    }
}
