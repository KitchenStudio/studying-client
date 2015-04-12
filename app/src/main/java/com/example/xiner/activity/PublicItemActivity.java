package com.example.xiner.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.example.xiner.R;
import com.example.xiner.adapter.GalleryAdapter;
import com.example.xiner.adapter.UploadfileAdapter;
import com.example.xiner.net.PublicDocNetwork;
import com.example.xiner.util.Action;
import com.example.xiner.entity.CustomGallery;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;


public class PublicItemActivity extends ActionBarActivity {
    private String TAG = "PublicDocActiviy";
    ImageLoader imageLoader;
    CardView gridGallerycard;
    Handler handler;
    GalleryAdapter adapter;
    ViewSwitcher viewSwitcher;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    ImageView imgSinglePick;
    UploadfileAdapter uploadfileAdapter;


    ArrayList<String> listfilename;
    ImageView uploadfileimage, recordsoundimage, takepictureimage, uploadpictureimage;
    ImageView recordImageView, recordstart, recordover;
    MediaRecorder mediaRecorder;
    private String mFileName;
    MediaPlayer mediaPlayer;
    boolean mStartRecording = true;
    PublicDocNetwork publicDocNetwork;
    ArrayList<CustomGallery> dataT;
    EditText shareContentedit, subjectEdit;
    ArrayList<String> allPictures = new ArrayList<>();
    private String single_path;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_item);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
//            toolbar.setNavigationIcon(R.drawable.backarrow);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.backarrow);
        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/audiorecordxueyou.mp4";
        publicDocNetwork = new PublicDocNetwork(this);
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

                publicDocNetwork.uploadshare(getAlllist(), shareContentedit.getText().toString(), subjectEdit.getText().toString());
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (android.R.id.home == item.getItemId()) {
            finish();
            return false;
        }
        return super.onOptionsItemSelected(item);
    }


    /*
    * 这个方法是初始化界面
    * */

    private void init() {
        handler = new Handler();
        ClickListener clickListener = new ClickListener();
        shareContentedit = (EditText) findViewById(R.id.sharecontent_edit);
        subjectEdit = (EditText) findViewById(R.id.subject_edit);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_uploadfile);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        uploadfileAdapter = new UploadfileAdapter();
        recyclerView.setAdapter(uploadfileAdapter);
        viewSwitcher = (ViewSwitcher) findViewById(R.id.viewSwitcher);
        viewSwitcher.setDisplayedChild(1);
        imgSinglePick = (ImageView) findViewById(R.id.imgSinglePick);
        uploadfileimage = (ImageView) findViewById(R.id.uploadfile_image);
        takepictureimage = (ImageView) findViewById(R.id.takepicture_image);
        recordsoundimage = (ImageView) findViewById(R.id.soundrecord_image);
        uploadpictureimage = (ImageView) findViewById(R.id.uploadpicture_image);
        recordImageView = (ImageView) findViewById(R.id.record_press);
        recordstart = (ImageView) findViewById(R.id.record_pressstart);
        recordover = (ImageView) findViewById(R.id.record_pressover);
        uploadfileimage.setOnClickListener(clickListener);
        takepictureimage.setOnClickListener(clickListener);
        recordsoundimage.setOnClickListener(clickListener);
        uploadpictureimage.setOnClickListener(clickListener);
        recordImageView.setOnClickListener(clickListener);
        recordstart.setOnClickListener(clickListener);
        recordover.setOnClickListener(clickListener);


    }


    class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent;
            switch (v.getId()) {
                case R.id.soundrecord_image://录音的监听
                    if (recordImageView.getVisibility() == View.VISIBLE || recordstart.getVisibility() == View.VISIBLE || recordover.getVisibility() == View.VISIBLE) {
                        recordImageView.setClickable(false);
                        return;
                    }
                    recordImageView.setVisibility(View.VISIBLE);
                    break;

                case R.id.record_press: //开始录音的监听
                    recordImageView.setVisibility(View.GONE);
                    if (recordstart.getVisibility() != View.VISIBLE) {
                        recordstart.setVisibility(View.VISIBLE);
                    }
                    onRecord(mStartRecording);
                    if (mStartRecording) {
                        Toast.makeText(PublicItemActivity.this, "开始录音", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(PublicItemActivity.this, "停止录音", Toast.LENGTH_SHORT).show();
                    }
                    mStartRecording = !mStartRecording;
                    break;

                case R.id.record_pressstart://结束录音的监听
                    recordImageView.setVisibility(View.GONE);
                    recordstart.setVisibility(View.GONE);
                    if (recordover.getVisibility() != View.VISIBLE) {
                        recordover.setVisibility(View.VISIBLE);
                    }
                    onRecord(mStartRecording);
                    break;

                case R.id.record_pressover:
                    boolean mStartPlaying = true;
                    onPlay(mStartPlaying);
                    if (mStartPlaying) {
                        Toast.makeText(PublicItemActivity.this, "开始播放", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(PublicItemActivity.this, "停止播放", Toast.LENGTH_SHORT).show();
                    }
                    mStartPlaying = !mStartPlaying;
                    break;

                case R.id.uploadfile_image://选择文件监听
                    intent = new Intent();
                    intent.setClass(PublicItemActivity.this, FileManagerActivity.class);
                    startActivityForResult(intent, 1);
                    break;

                case R.id.uploadpicture_image://选择图片监听
                    intent = new Intent(Action.ACTION_MULTIPLE_PICK);
                    startActivityForResult(intent, 3);
                    break;

                case R.id.takepicture_image://拍照监听
                    String state = Environment.getExternalStorageState();
                    if (state.equals(Environment.MEDIA_MOUNTED)) {
                        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, 2);
                    }
                    break;
            }
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                //返回的选择的文件的结果
                if (resultCode == RESULT_OK) {
                    recyclerView.setVisibility(View.VISIBLE);
                    listfilename = data.getExtras().getStringArrayList("urilist");
                    uploadfileAdapter.addAll(listfilename);
                }
                break;
            case 2:
//                返回的拍照的结果
                if (resultCode == RESULT_OK) {
                    String sdStatus = Environment.getExternalStorageState();
                    if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用

                        return;
                    }

                    Bundle bundle = data.getExtras();
                    Bitmap bitmap = bundle.getParcelable("data");// 获取相机返回的数据，并转换为Bitmap图片格式
                    if (bitmap != null) {


                        gridGallerycard = (CardView) findViewById(R.id.public_card_uploadpic);
                        gridGallerycard.setVisibility(View.VISIBLE);
                        viewSwitcher.setDisplayedChild(1);
                        single_path = Environment.getExternalStorageDirectory() + "/xueyou/takepic.jpg";
                        imgSinglePick.setImageBitmap(bitmap);
                        if (single_path != null) {
                            allPictures.clear();
                            allPictures.add("file://" + single_path);
                        }
                        setPicToView(bitmap);
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
                            allPictures.add("file://" + all_path[i]);
                        }
                    }
                }

                break;
        }


    }

    //保存头像
    private void setPicToView(Bitmap mBitmap) {
        Log.v(TAG, "保存");
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            return;
        }
        FileOutputStream b = null;
        String single_path = Environment.getExternalStorageDirectory() + "/xueyou";
        File file = new File(single_path);
        file.mkdirs();// 创建文件夹
        String fileName = single_path + "/takepic.jpg";// 图片名字
        Log.v(TAG, fileName + "filename");
        try {
            b = new FileOutputStream(fileName);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                // 关闭流
                b.flush();
                b.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

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
            filenames.add("file://" + mFileName);
        }

        return filenames;
    }
}

