package com.example.xiner.adapter;

import android.content.Context;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.xiner.R;
import com.example.xiner.entity.CustomGallery;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

import java.util.ArrayList;

/**
 * Created by xiner on 15-1-20.
 */
public class GalleryAdapter extends BaseAdapter {
    LayoutInflater infalter;
    Context mContext;
    ImageLoader imageLoader;
    private ArrayList<CustomGallery> data = new ArrayList<CustomGallery>();

    public boolean isActionMultiplePick() {
        return isActionMultiplePick;
    }

    public void setActionMultiplePick(boolean isActionMultiplePick) {
        this.isActionMultiplePick = isActionMultiplePick;
    }

    private boolean isActionMultiplePick;

    public GalleryAdapter(Context c, ImageLoader imageLoader) {
        infalter = (LayoutInflater) c
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext = c;
        this.imageLoader = imageLoader;
        // clearCache();
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public void changeSelection(View v, int position) {

        if (data.get(position).isSeleted) {
            data.get(position).isSeleted = false;
        } else {
            data.get(position).isSeleted = true;
        }

        ((ViewHolder) v.getTag()).imgQueueMultiSelected.setSelected(data
                .get(position).isSeleted);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if(convertView==null){
            convertView = infalter.inflate(R.layout.gallery_item,null);
            holder = new ViewHolder();
            holder.imgQueue=(ImageView)convertView.findViewById(R.id.imgQueue);
            holder.imgQueueMultiSelected =(ImageView)convertView.findViewById(R.id.imgQueueMultiSelected);
            if(isActionMultiplePick){
                holder.imgQueueMultiSelected.setVisibility(View.VISIBLE);

            }else {
                holder.imgQueueMultiSelected.setVisibility(View.GONE);
            }
            convertView.setTag(holder);

        }else {
            holder=(ViewHolder)convertView.getTag();
        }
        holder.imgQueue.setTag(position);
        try {
            //显示图片
            imageLoader.displayImage("file://" + data.get(position).sdcardPath,
                    holder.imgQueue, new SimpleImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {
                            holder.imgQueue
                                    .setImageResource(R.drawable.no_media);
                            super.onLoadingStarted(imageUri, view);
                        }
                    });

            if (isActionMultiplePick) {

                holder.imgQueueMultiSelected
                        .setSelected(data.get(position).isSeleted);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }


    //把数据加到list中
    public void addAll(ArrayList<CustomGallery> files) {

        try {
            this.data.clear();
            this.data.addAll(files);

        } catch (Exception e) {
            e.printStackTrace();
        }

        notifyDataSetChanged();
    }

    //获得选中的图片
    public ArrayList<CustomGallery> getSelected() {
        ArrayList<CustomGallery> dataT = new ArrayList<CustomGallery>();

        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).isSeleted) {
                dataT.add(data.get(i));
            }
        }

        return dataT;
    }
    public class ViewHolder {
        ImageView imgQueue;
        ImageView imgQueueMultiSelected;
    }
    public void clear() {
        data.clear();
        notifyDataSetChanged();
    }
}
