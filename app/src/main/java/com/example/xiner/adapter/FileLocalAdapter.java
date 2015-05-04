package com.example.xiner.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import com.example.xiner.R;


/**
 * Created by xiner on 5/2/15.
 */
public class FileLocalAdapter extends BaseAdapter {
    private static final String TAG = "FileLocalAdapter";
    Context context;
    public ArrayList<String> files;
    public FileLocalAdapter(Context context,ArrayList<String>files){
        this.context = context;
        this.files = files;
    }
    @Override
    public int getCount() {
        return files.size();
    }

    @Override
    public Object getItem(int position) {
        return files.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.list_filename,parent,false);
        TextView textView = (TextView)convertView.findViewById(R.id.filename_recyclertext);
        textView.setText(files.get(position));
        return convertView;
    }

        public void addAll(ArrayList<String>filenames){
            try {
                this.files.clear();
                this.files.addAll(filenames);
                notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }



}
