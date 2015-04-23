package com.example.xiner.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.xiner.R;
import com.example.xiner.main.AppBase;
import com.example.xiner.util.DownloadPicUtil;
import com.example.xiner.util.HttpUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by xiner on 15-3-22.
 */
public class PictureAdapter extends BaseAdapter {
    Context context;
    ArrayList<String> items;
    LayoutInflater layoutInflater;
    private static final String TAG="PictureAdapter";
    String path = Environment.getExternalStorageDirectory()+"/xueyou/thumbnail/upload";
    String picpath = Environment.getExternalStorageDirectory()+"/xueyou/thumbnail";
    AppBase app;

    public PictureAdapter(Context context, ArrayList<String> items) {
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.items = items;
        app = AppBase.getApp();

    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.gridview_picture, parent,false);
            holder = new ViewHolder();

            holder.imageView = (ImageView) convertView.findViewById(R.id.gridview_nopic);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String nameFile = items.get(position).split("\\.")[0]+"resizecut."+items.get(position).split("\\.")[1];
        File file = new File(picpath+nameFile);
        if (file.exists()) {
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;// 设置成了true,不占用内存，只获取bitmap宽高

            BitmapFactory.decodeFile(file.getPath(), opts);

            opts.inJustDecodeBounds = false;

            opts.inSampleSize = app.calculateInSampleSize(opts, 100, 100);
            Bitmap bitmap = BitmapFactory.decodeFile(file.getPath(), opts);
            holder.imageView.setImageBitmap(bitmap);
        }else {
            File file1 = new File(path);
            file1.mkdirs();
            //下载图片
            DownloadPicUtil downloadPicUtil = new DownloadPicUtil(holder.imageView, new File(picpath + nameFile), 100, 100);
            downloadPicUtil.execute(HttpUtil.baseIp + items.get(position).split("\\.")[0] + "resizecut." + items.get(position).split("\\.")[1]);
        }
//        convertView.setLayoutParams(new GridView.LayoutParams(gridwidth, gridheight));
//        holder.imageView.setGravity(Gravity.CENTER);

        return convertView;
    }

    static class ViewHolder {
        ImageView imageView;
    }
}
