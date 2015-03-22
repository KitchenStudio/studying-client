package com.example.xiner.activity;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.xiner.R;
import com.example.xiner.adapter.ViewPagerAdapter;
import com.example.xiner.view.SlidingTabLayout;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;

import java.io.InputStream;
import java.lang.ref.WeakReference;


public class MainActivity extends ActionBarActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    private ListView mDrawerList;
    ViewPager pager;
    private String titles[] = new String[]{"广场", "我的收藏", "个人信息"};
    private Toolbar toolbar;

    SlidingTabLayout slidingTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.navdrawer);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.drawable.ic_ab_drawer);
        }
        pager = (ViewPager) findViewById(R.id.viewpager);
        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);


        pager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), titles));

        slidingTabLayout.setViewPager(pager);
        slidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return Color.WHITE;
            }
        });
        drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name, R.string.app_name);
        mDrawerLayout.setDrawerListener(drawerToggle);
        String[] values = new String[]{
                "DEFAULT", "RED", "BLUE", "MATERIAL GREY"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                switch (position) {
                    case 0:
                        mDrawerList.setBackgroundColor(getResources().getColor(R.color.material_deep_teal_500));
                        toolbar.setBackgroundColor(getResources().getColor(R.color.material_deep_teal_500));
                        slidingTabLayout.setBackgroundColor(getResources().getColor(R.color.material_deep_teal_500));
                        mDrawerLayout.closeDrawer(Gravity.START);

                        break;
                    case 1:
                        mDrawerList.setBackgroundColor(getResources().getColor(R.color.red));
                        toolbar.setBackgroundColor(getResources().getColor(R.color.red));
                        slidingTabLayout.setBackgroundColor(getResources().getColor(R.color.red));
                        mDrawerLayout.closeDrawer(Gravity.START);

                        break;
                    case 2:
                        mDrawerList.setBackgroundColor(getResources().getColor(R.color.blue));
                        toolbar.setBackgroundColor(getResources().getColor(R.color.blue));
                        slidingTabLayout.setBackgroundColor(getResources().getColor(R.color.blue));
                        mDrawerLayout.closeDrawer(Gravity.START);

                        break;
                    case 3:
                        mDrawerList.setBackgroundColor(getResources().getColor(R.color.material_blue_grey_800));
                        toolbar.setBackgroundColor(getResources().getColor(R.color.material_blue_grey_800));
                        slidingTabLayout.setBackgroundColor(getResources().getColor(R.color.material_blue_grey_800));
                        mDrawerLayout.closeDrawer(Gravity.START);

                        break;
                }

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(Gravity.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }


//    class ImageDownloaderTask extends AsyncTask<String, Void, Bitmap> {
//        private final WeakReference imageViewReference;
//
//        public ImageDownloaderTask(ImageView imageView) {
//            imageViewReference = new WeakReference(imageView);
//        }
//
//        @Override
//        // Actual download method, run in the task thread
//        protected Bitmap doInBackground(String... params) {
//            // params comes from the execute() call: params[0] is the url.
//            return downloadBitmap(params[0]);
//        }
//
//        @Override
//        // Once the image is downloaded, associates it to the imageView
//        protected void onPostExecute(Bitmap bitmap) {
//            if (isCancelled()) {
//                bitmap = null;
//            }
//
//            if (imageViewReference != null) {
//                ImageView imageView = (ImageView) imageViewReference.get();
//                if (imageView != null) {
//
//                    if (bitmap != null) {
//                        imageView.setImageBitmap(bitmap);
//                    } else {
//                        imageView.setImageDrawable(imageView.getContext().getResources()
//                                .getDrawable(R.drawable.list_placeholder));
//                    }
//                }
//
//            }
//        }
//
//    }
//
//    static Bitmap downloadBitmap(String url) {
//        final AndroidHttpClient client = AndroidHttpClient.newInstance("Android");
//        final HttpGet getRequest = new HttpGet(url);
//        try {
//            HttpResponse response = client.execute(getRequest);
//            final int statusCode = response.getStatusLine().getStatusCode();
//            if (statusCode != HttpStatus.SC_OK) {
//                Log.w("ImageDownloader", "Error " + statusCode
//                        + " while retrieving bitmap from " + url);
//                return null;
//            }
//
//            final HttpEntity entity = response.getEntity();
//            if (entity != null) {
//                InputStream inputStream = null;
//                try {
//                    inputStream = entity.getContent();
//                    final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//                    return bitmap;
//                } finally {
//                    if (inputStream != null) {
//                        inputStream.close();
//                    }
//                    entity.consumeContent();
//                }
//            }
//        } catch (Exception e) {
//            // Could provide a more explicit error message for IOException or
//            // IllegalStateException
//            getRequest.abort();
//            Log.w("ImageDownloader", "Error while retrieving bitmap from " + url);
//        } finally {
//            if (client != null) {
//                client.close();
//            }
//        }
//        return null;
//    }

}
