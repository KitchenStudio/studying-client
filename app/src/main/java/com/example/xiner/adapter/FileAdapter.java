package com.example.xiner.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xiner.R;
import com.example.xiner.util.DownloadFileUtil;
import com.example.xiner.util.HttpUtil;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by xiner on 4/23/15.
 */
public class FileAdapter extends BaseAdapter {
    Context context;
    ArrayList<String> files;
    private static final String path = Environment.getExternalStorageDirectory()+"/xueyou/other/upload";
    private static final String filepath = Environment.getExternalStorageDirectory()+"/xueyou/other/";

    public FileAdapter(Context context,ArrayList<String> files){
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
        final String filename = files.get(position);
        String [] name = filename.split("/");
        convertView = LayoutInflater.from(context).inflate(R.layout.list_filename,parent,false);
        TextView textView = (TextView) convertView.findViewById(R.id.filename_recyclertext);
        textView.setText(files.get(position));
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context).setMessage("确定下载该文件吗").setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        File file = new File(filepath+filename);
                        if (file.exists()) {
                            Toast.makeText(context, "该文件已存在" + file.getPath(), Toast.LENGTH_SHORT).show();
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



        return convertView;
    }
}
