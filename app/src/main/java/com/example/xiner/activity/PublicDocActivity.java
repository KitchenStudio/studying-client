package com.example.xiner.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;
import com.example.xiner.R;
import com.example.xiner.adapter.GalleryAdapter;
import com.example.xiner.adapter.UploadfileAdapter;
import com.example.xiner.util.Action;
import com.example.xiner.entity.CustomGallery;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import java.io.File;
import java.util.ArrayList;

public class PublicDocActivity extends ActionBarActivity {
    private ImageView uploadFile;
    private String TAG ="PublicDocActiviy";
    private Dialog myDialog;
    ImageLoader imageLoader;
    GridView gridGallery;
    Handler handler;
    GalleryAdapter adapter;
    ViewSwitcher viewSwitcher,fileupload_switcher;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    ImageView imgSinglePick;
    CardView upload_filecard;
    GridView gridgallary;
    UploadfileAdapter uploadfileAdapter;
    ArrayList<String> listfilename;
    Button upserverButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_doc);
        initImageLoader();
        init();
    }

    class uploadListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
           showDialog();

//            RequestParams params = new RequestParams();
//            params.put("content", "dsfsf");
//            try {
//                params.put("file", new File(new URI(getUri().toString())));
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (URISyntaxException e) {
//                e.printStackTrace();
//            }
//
//            UploadUtil.post(PathUtil.uploadFile, params, new AsyncHttpResponseHandler() {
//                @Override
//                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                    Log.v("PublicDocA", statusCode + "");
//
//                }
//
//                @Override
//                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                    Log.v("PublicDocAf", statusCode + "");
//                }
//            });

        }
    }

    private void init() {
        handler = new Handler();
        gridGallery = (GridView) findViewById(R.id.gridGallery);
        gridGallery.setFastScrollEnabled(true);
        adapter = new GalleryAdapter(getApplicationContext(), imageLoader);
        adapter.setActionMultiplePick(false);
        gridGallery.setAdapter(adapter);
        recyclerView =(RecyclerView)findViewById(R.id.recyclerView_uploadfile);

        linearLayoutManager =new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        uploadfileAdapter = new UploadfileAdapter();
        recyclerView.setAdapter(uploadfileAdapter);
        viewSwitcher = (ViewSwitcher) findViewById(R.id.viewSwitcher);
        viewSwitcher.setDisplayedChild(1);
        fileupload_switcher=(ViewSwitcher)findViewById(R.id.file_uploadswitch);
        fileupload_switcher.setDisplayedChild(1);
        uploadFile =(ImageView)findViewById(R.id.image_upload);
        uploadFile.setOnClickListener(new uploadListener());
        imgSinglePick =(ImageView)findViewById(R.id.imgSinglePick);
        upload_filecard=(CardView)findViewById(R.id.public_filecard_upload);
        upload_filecard.setOnClickListener(new FileUploadListener());
        upserverButton=(Button)findViewById(R.id.upserver_button);
        upserverButton.setOnClickListener(new upserverListener());


    }

    class upserverListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {

        }
    }
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

    private void showDialog(){
        TextView takepictures,selectpicture;
        myDialog = new Dialog(PublicDocActivity.this);
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        myDialog.setContentView(R.layout.dialog_style);
        takepictures = (TextView)myDialog.findViewById(R.id.takepicture);
        takepictures.setOnClickListener(new takepicListener());
        selectpicture=(TextView)myDialog.findViewById(R.id.selectpicture);
        selectpicture.setOnClickListener(new picselListener());
        myDialog.show();
    }
    class FileUploadListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(PublicDocActivity.this,FileManagerMain.class);
            startActivityForResult(intent,1);
        }
    }

    class picselListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Intent i = new Intent(Action.ACTION_MULTIPLE_PICK);
            startActivityForResult(i, 3);
            myDialog.dismiss();
        }
    }

    class takepicListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            myDialog.dismiss();
            String state = Environment.getExternalStorageState();
            if(state.equals(Environment.MEDIA_MOUNTED)){
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"head.png")));
                startActivityForResult(intent,2);
            }

        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if(resultCode == RESULT_OK){
                    listfilename =data.getExtras().getStringArrayList("urilist");
                    for(int i =0;i<listfilename.size();i++){
                        Log.v(TAG,listfilename.get(i)+"string");
                    }
                    fileupload_switcher.setDisplayedChild(0);
                    uploadfileAdapter.addAll(listfilename);
                }
                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    uploadFile.setVisibility(View.GONE);
                    adapter.clear();
                    viewSwitcher.setDisplayedChild(1);
                    String single_path = Environment.getExternalStorageDirectory()+"/head.png";
                    imageLoader.displayImage("file://"+single_path, imgSinglePick);
                }
                break;
            case 3:
                if(resultCode==RESULT_OK){
                    if(data!=null){
                        uploadFile.setVisibility(View.GONE);
                    }
                    String[] all_path = data.getStringArrayExtra("all_path");

                    ArrayList<CustomGallery> dataT= new ArrayList<CustomGallery>();

                    for (String string : all_path) {
                        CustomGallery item = new CustomGallery();
                        item.sdcardPath = string;
                        dataT.add(item);
                    }

                    viewSwitcher.setDisplayedChild(0);
                    adapter.addAll(dataT);
                }

                break;
        }

    }
}
