package com.example.xiner.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xiner.R;
import com.example.xiner.util.DownloadFileUtil;
import com.example.xiner.util.DownloadPicUtil;
import com.example.xiner.util.HttpUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;

/**
 * Created by xiner on 15-4-12.
 */
public class FilenameAdapter  extends RecyclerView.Adapter<FilenameAdapter.ViewHolder>{

    private static final String TAG = "FilenameAdapter";
    Context context;
    private static final String path = Environment.getExternalStorageDirectory()+"/xueyou/other/upload";
    private static final String filepath = Environment.getExternalStorageDirectory()+"/xueyou/other/";

    ArrayList<String> filenames;

    public FilenameAdapter(Context context,ArrayList<String> filenames){
        Log.v(TAG,"hello");
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
        final String filename = filenames.get(position);
        String [] name = filename.split("/");
        holder.textView.setText(name[name.length-1]);
        holder.textView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context).setMessage("确定下载该文件吗").setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        File file = new File(filepath+filename);
                        if (file.exists()) {
                            Toast.makeText(context,"该文件已存在"+file.getPath(),Toast.LENGTH_SHORT).show();
                           return;
                        }else {
                            File file1 = new File(path);
                            file1.mkdirs();
                            //下载图片
                            DownloadFileUtil downloadfileUtil = new DownloadFileUtil(new File(filepath + filename),context);
                            downloadfileUtil.execute(HttpUtil.baseIp + filename);
                        }

                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alert.create().show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return filenames.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView =(TextView)itemView.findViewById(R.id.filename_recyclertext);
        }
    }
}
