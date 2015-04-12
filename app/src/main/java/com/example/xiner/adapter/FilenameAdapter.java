package com.example.xiner.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.xiner.R;

import java.util.ArrayList;

/**
 * Created by xiner on 15-4-12.
 */
public class FilenameAdapter  extends RecyclerView.Adapter<FilenameAdapter.ViewHolder>{

    Context context;
    ArrayList<String> filenames;

    public FilenameAdapter(Context context,ArrayList<String> filenames){
        this.context = context;
        this.filenames=filenames;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_filename,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textView.setText(filenames.get(position));
    }

    @Override
    public int getItemCount() {
        return filenames.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView =(TextView)itemView.findViewById(R.id.filename_text);
        }
    }
}
