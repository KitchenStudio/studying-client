package com.example.xiner.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.example.xiner.R;
import com.example.xiner.adapter.GalleryAdapter;
import com.example.xiner.adapter.UploadfileAdapter;
import com.example.xiner.net.Network;
import com.example.xiner.util.Action;
import com.example.xiner.entity.CustomGallery;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class PublicDocActivity extends ActionBarActivity {
    private ImageView uploadFile;
    private String TAG = "PublicDocActiviy";
    ImageLoader imageLoader;
    CardView gridGallerycard;
    Handler handler;
    GalleryAdapter adapter;
    ViewSwitcher viewSwitcher, fileupload_switcher;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    ImageView imgSinglePick;
    CardView upload_filecard;
    UploadfileAdapter uploadfileAdapter;


    ArrayList<String> listfilename;
    ImageView uploadfileimage, recordsoundimage, takepictureimage, uploadpictureimage;
    ImageView recordImageView, recordstart, recordover;
    MediaRecorder mediaRecorder;
    private String mFileName;
    MediaPlayer mediaPlayer;
    boolean mStartRecording = true;
    Network network;
    ArrayList<CustomGallery> dataT;
    EditText shareContentedit;
    ArrayList<String> allPictures = new ArrayList<>();




    public PublicDocActivity() {
        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/audiorecordxueyou.3gp";
        network = new Network();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_doc);
        initImageLoader();
        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem item = menu.add("发布");
        item.setIcon(R.drawable.publiclist);
        MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_ALWAYS
                | MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                network.uploadshare(getAlllist(), shareContentedit.getText().toString(), "大一年级", "数据结构");
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }


    /*
    * 这个方法是初始化界面
    * */

    private void init() {
        handler = new Handler();

        shareContentedit = (EditText) findViewById(R.id.sharecontent_edit);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_uploadfile);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        uploadfileAdapter = new UploadfileAdapter();
        recyclerView.setAdapter(uploadfileAdapter);

        viewSwitcher = (ViewSwitcher) findViewById(R.id.viewSwitcher);
        viewSwitcher.setDisplayedChild(1);
        imgSinglePick = (ImageView) findViewById(R.id.imgSinglePick);
        upload_filecard = (CardView) findViewById(R.id.public_filecard_upload);
        upload_filecard.setOnClickListener(new FileUploadListener());
        uploadfileimage = (ImageView) findViewById(R.id.uploadfile_image);
        uploadfileimage.setOnClickListener(new FileUploadListener());
        takepictureimage = (ImageView) findViewById(R.id.takepicture_image);
        takepictureimage.setOnClickListener(new takepicListener());
        recordsoundimage = (ImageView) findViewById(R.id.soundrecord_image);
        recordsoundimage.setOnClickListener(new recordListener());
        uploadpictureimage = (ImageView) findViewById(R.id.uploadpicture_image);
        uploadpictureimage.setOnClickListener(new picselListener());
        recordImageView = (ImageView) findViewById(R.id.record_press);
        recordImageView.setOnClickListener(new startrecordListener());
        recordstart = (ImageView) findViewById(R.id.record_pressstart);
        recordstart.setOnClickListener(new overrecordListener());
        recordover = (ImageView) findViewById(R.id.record_pressover);
        recordover.setOnClickListener(new playrecordListener());


    }

    /*
    * 录音的监听
    *
    * */
    class recordListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (recordImageView.getVisibility() == View.VISIBLE || recordstart.getVisibility() == View.VISIBLE || recordover.getVisibility() == View.VISIBLE) {
                recordImageView.setClickable(false);
                return;
            }
            recordImageView.setVisibility(View.VISIBLE);
        }
    }

/*
* 开始录音的监听
* */
    class startrecordListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            recordImageView.setVisibility(View.GONE);
            if (recordstart.getVisibility() != View.VISIBLE) {
                recordstart.setVisibility(View.VISIBLE);
            }
            onRecord(mStartRecording);
            if (mStartRecording) {
                Toast.makeText(PublicDocActivity.this, "开始录音", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(PublicDocActivity.this, "停止录音", Toast.LENGTH_SHORT).show();
            }
            mStartRecording = !mStartRecording;
        }
    }

    /*
    *
    * 结束录音监听
    * */
    class overrecordListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            recordImageView.setVisibility(View.GONE);
            recordstart.setVisibility(View.GONE);
            if (recordover.getVisibility() != View.VISIBLE) {
                recordover.setVisibility(View.VISIBLE);
            }
            onRecord(mStartRecording);

        }
    }

    /*
    * 播放录音界面
    * */
    class playrecordListener implements View.OnClickListener {
        boolean mStartPlaying = true;

        @Override
        public void onClick(View v) {
            onPlay(mStartPlaying);
            if (mStartPlaying) {
                Toast.makeText(PublicDocActivity.this, "开始播放", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(PublicDocActivity.this, "停止播放", Toast.LENGTH_SHORT).show();
            }
            mStartPlaying = !mStartPlaying;

        }
    }

    /*
    * 初始化加载图画类库
    * */
    private void initImageLoader() {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc().imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(
                this).defaultDisplayImageOptions(defaultOptions).memoryCache(
                new WeakMemoryCache());

        ImageLoaderConfiguration config = builder.build();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);
    }

/*
* 文件上传监听
* */
    class FileUploadListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(PublicDocActivity.this, FileManagerMain.class);
            startActivityForResult(intent, 1);
        }
    }

    /*
    *
    *图片选择监听
    * */
    class picselListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent i = new Intent(Action.ACTION_MULTIPLE_PICK);
            startActivityForResult(i, 3);
        }
    }

    /*
    * 拍照的监听
    * */

    class takepicListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            String state = Environment.getExternalStorageState();
            if (state.equals(Environment.MEDIA_MOUNTED)) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "head.png")));
                startActivityForResult(intent, 2);
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                //返回的选择的文件的结果
                if (resultCode == RESULT_OK) {
                    fileupload_switcher = (ViewSwitcher) findViewById(R.id.file_uploadswitch);
                    fileupload_switcher.setVisibility(View.VISIBLE);
                    fileupload_switcher.setDisplayedChild(1);
                    listfilename = data.getExtras().getStringArrayList("urilist");
                    fileupload_switcher.setDisplayedChild(0);
                    uploadfileAdapter.addAll(listfilename);
                }
                break;
            case 2:
//                返回的拍照的结果
                if (resultCode == RESULT_OK) {
                    gridGallerycard = (CardView) findViewById(R.id.public_card_uploadpic);
                    gridGallerycard.setVisibility(View.VISIBLE);
                    GridView gridView = (GridView) findViewById(R.id.gridGallery);
                    gridView.setFastScrollEnabled(true);
                    adapter = new GalleryAdapter(getApplicationContext(), imageLoader);
                    adapter.setActionMultiplePick(false);
                    gridView.setAdapter(adapter);
                    adapter.clear();
                    viewSwitcher.setDisplayedChild(1);
                    String single_path = Environment.getExternalStorageDirectory() + "/head.png";
                    imageLoader.displayImage("file://" + single_path, imgSinglePick);
                    if (single_path != null) {
                        allPictures.clear();

                        allPictures.add("file://" + single_path);
                    }
                }
                break;
            case 3:
                //返回的选择的图片的结果
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        gridGallerycard = (CardView) findViewById(R.id.public_card_uploadpic);
                        gridGallerycard.setVisibility(View.VISIBLE);
                        GridView gridView = (GridView) findViewById(R.id.gridGallery);
                        gridView.setFastScrollEnabled(true);
                        adapter = new GalleryAdapter(getApplicationContext(), imageLoader);
                        adapter.setActionMultiplePick(false);
                        gridView.setAdapter(adapter);
                    }
                    String[] all_path = data.getStringArrayExtra("all_path");

                    dataT = new ArrayList<CustomGallery>();

                    for (String string : all_path) {
                        CustomGallery item = new CustomGallery();
                        item.sdcardPath = string;
                        dataT.add(item);
                    }

                    viewSwitcher.setDisplayedChild(0);
                    adapter.addAll(dataT);

                    if (all_path.length != 0) {
                        allPictures.clear();
                        for (int i = 0; i < all_path.length; i++) {
                            allPictures.add("file://"+all_path[i]);
                        }
                    }
                }

                break;
        }


    }

    //开始录音
    private void startRecorded() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setOutputFile(mFileName);

        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaRecorder.start();
    }

    //结束录音
    private void stopRecording() {
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;
    }

    //开始播放
    private void startPlaying() {
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(mFileName);
            mediaPlayer.prepare();
            mediaPlayer.start();

        } catch (IOException e) {

            e.printStackTrace();

        }
    }

    //结束播放

    private void stopPlaying() {
        mediaPlayer.release();
        mediaPlayer = null;
    }

//正在播放
    private void onPlay(boolean start) {

        if (start) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }

    //正在录音

    private void onRecord(boolean start) {
        if (start) {
            startRecorded();
        } else {
            stopRecording();
        }
    }

//获得上传的多个文件
    private ArrayList<String> getAlllist() {
        ArrayList<String> filenames = new ArrayList<>();
        if (listfilename != null) {
            filenames.addAll(listfilename);
        }
        if (allPictures != null) {
            filenames.addAll(allPictures);
        }
        if (mFileName != null) {
            filenames.add("file://"+mFileName);
        }

        return filenames;
    }
}

