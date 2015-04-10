package com.example.xiner.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import com.example.xiner.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;

import java.io.InputStream;
import java.lang.ref.WeakReference;

/**
 * Created by xiner on 15-3-25.
 */
public class DownloadPicUtil extends AsyncTask<String, Void, Bitmap> {
    private static final String TAG = "DownloadPicUtil";
    private static final String USERNAME = "18366116016";
    private static final String PASSWORD = "..xiao";
    private final WeakReference imageViewReference;

    public DownloadPicUtil(ImageView imageView) {
        Log.v(TAG,"gouzaofangf");
        imageViewReference = new WeakReference(imageView);

    }

    @Override
    // Actual download method, run in the task thread
    protected Bitmap doInBackground(String... params) {
        // params comes from the execute() call: params[0] is the url.
        Log.v(TAG,"gouzaofangf");
        return downloadBitmap(params[0]);
    }

    @Override
    // Once the image is downloaded, associates it to the imageView
    protected void onPostExecute(Bitmap bitmap) {
        if (isCancelled()) {
            Log.v(TAG,"isCancle");
            bitmap = null;
        }

        if (imageViewReference != null) {
            ImageView imageView = (ImageView) imageViewReference.get();
            if (imageView != null) {

                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                } else {
                    imageView.setImageDrawable(imageView.getContext().getResources()
                            .getDrawable(R.drawable.tupian));
                }
            }

        }
    }


    static Bitmap downloadBitmap(String url) {
        Log.v(TAG,"DOWNLOADING"+url);
        AndroidHttpClient client = AndroidHttpClient.newInstance("Android");
        String credentials = USERNAME + ":" + PASSWORD;
        String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);


        final org.apache.http.client.methods.HttpGet getRequest = new org.apache.http.client.methods.HttpGet(url);
        getRequest.addHeader("Authorization", "Basic " + base64EncodedCredentials);
        try {
            HttpResponse response = client.execute(getRequest);
            final int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                Log.v(TAG, "Error " + statusCode
                        + " while retrieving bitmap from " + url);
                return null;
            }

            final HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream inputStream = null;
                try {
                    inputStream = entity.getContent();
                    final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    return bitmap;
                } finally {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    entity.consumeContent();
                }
            }
        } catch (Exception e) {
            // Could provide a more explicit error message for IOException or
            // IllegalStateException
            getRequest.abort();
            Log.w("ImageDownloader", "Error while retrieving bitmap from " + url);
        } finally {
            if (client != null) {
                client.close();
            }
        }
        return null;
    }
}