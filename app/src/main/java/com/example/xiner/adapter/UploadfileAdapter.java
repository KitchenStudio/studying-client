package com.example.xiner.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.xiner.R;

import java.util.ArrayList;

/**
 * Created by xiner on 15-1-20.
 */
public class UploadfileAdapter extends RecyclerView.Adapter<UploadfileAdapter.ViewHolder> {

ArrayList<String>filesupload = new ArrayList<String>();

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_uploadfile, parent, false);
        ViewHolder vh = new ViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(UploadfileAdapter.ViewHolder holder, int position) {
        holder.file_name.setText(filesupload.get(position));
    }


    @Override
    public int getItemCount() {
        return filesupload.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView file_type;
        TextView file_name;

        public ViewHolder(View view) {
            super(view);
           file_name =(TextView)view.findViewById(R.id.filename_text);
            file_type=(ImageView)view.findViewById(R.id.filetype_image);
        }
    }

    public void addAll(ArrayList<String>filenames){
        try {
        this.filesupload.clear();
        this.filesupload.addAll(filenames);
        notifyDataSetChanged();
    } catch (Exception e) {
        e.printStackTrace();
    }

    }
}

