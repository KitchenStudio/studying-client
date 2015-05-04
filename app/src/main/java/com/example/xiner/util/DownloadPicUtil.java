package com.example.xiner.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import com.example.xiner.R;
import com.example.xiner.main.AppBase;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;

/**
 * Created by xiner on 15-3-25.
 */
public class DownloadPicUtil extends AsyncTask<String, Void, Bitmap> {
    private static  final String TAG = "DownloadPicUtil";
    private static  String USERNAME = "";
    private static  String PASSWORD = "";
    private final WeakReference imageViewReference;
    AppBase app;

    int width,height;
    File file ;
    public DownloadPicUtil(ImageView imageView, File file,int width,int height) {
        imageViewReference = new WeakReference(imageView);
        this.file = file;
        this.width =width;
        this.height = height;
        app = AppBase.getApp();
        USERNAME = app.getDataStore().getString("username","1");
        PASSWORD = app.getDataStore().getString("password","..xiao");
    }

    @Override
    // Actual download method, run in the task thread
    protected Bitmap doInBackground(String... params) {
        // params comes from the execute() call: params[0] is the url.
        return downloadBitmap(params[0]);
    }

    @Override
    // Once the image is downloaded, associates it to the imageView
    protected void onPostExecute(Bitmap bitmap) {
        if (isCancelled()) {
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


     Bitmap downloadBitmap(String url) {
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
                    inputTofile(inputStream,file);
                    BitmapFactory.Options opts = new BitmapFactory.Options();
                    opts.inJustDecodeBounds = true;// 设置成了true,不占用内存，只获取bitmap宽高

                    BitmapFactory.decodeFile(file.getPath(),opts);

                    opts.inJustDecodeBounds=false;

                    opts.inSampleSize =app.calculateInSampleSize(opts,width,height);
                    final Bitmap bitmap = BitmapFactory.decodeFile(file.getPath(),opts);
                    return bitmap;
                } finally {

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

    private void inputTofile(InputStream inputStream,File file){
        OutputStream out=null;
        try {
            out = new FileOutputStream(file);
            int byteRead=0;
            byte [] buffer = new byte[8192];
            while((byteRead=inputStream.read(buffer,0,8192))!=-1){
                out.write(buffer,0,byteRead);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (out!=null){
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream!=null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }



    public static byte[] decodeBitmap(String path) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;// 设置成了true,不占用内存，只获取bitmap宽高
        BitmapFactory.decodeFile(path, opts);
        opts.inSampleSize = computeSampleSize(opts, -1, 1024 * 800);
        opts.inJustDecodeBounds = false;// 这里一定要将其设置回false，因为之前我们将其设置成了true
        opts.inPurgeable = true;
        opts.inInputShareable = true;
        opts.inDither = false;
        opts.inPurgeable = true;
        opts.inTempStorage = new byte[16 * 1024];
        FileInputStream is = null;
        Bitmap bmp = null;
        ByteArrayOutputStream baos = null;
        try {
            is = new FileInputStream(path) ;
            bmp = BitmapFactory.decodeFileDescriptor(is.getFD(), null, opts);
            double scale = getScaling(opts.outWidth * opts.outHeight,
                    1024 * 600);
            Bitmap bmp2 = Bitmap.createScaledBitmap(bmp,
                    (int) (opts.outWidth * scale),
                    (int) (opts.outHeight * scale), true);
            bmp.recycle();
            baos = new ByteArrayOutputStream() ;
            bmp2.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            bmp2.recycle();
            return baos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException  e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.gc();
        }
        return baos.toByteArray();
    }

    private static double getScaling(int src, int des) {
        /**
         * 48 目标尺寸÷原尺寸 sqrt开方，得出宽高百分比 49
         */
        double scale = Math.sqrt((double) des / (double) src);
        return scale;
    }

    public static int computeSampleSize(BitmapFactory.Options options,
                                        int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength,
                maxNumOfPixels);

        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }

        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options,
                                                int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;

        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
                .sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
                Math.floor(w / minSideLength), Math.floor(h / minSideLength));

        if (upperBound < lowerBound) {
            return lowerBound;
        }

        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }


}