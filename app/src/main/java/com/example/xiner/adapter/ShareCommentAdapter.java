package com.example.xiner.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xiner.R;
import com.example.xiner.entity.Comment;
import com.example.xiner.entity.DetailItem;
import com.example.xiner.entity.FileItem;
import com.example.xiner.entity.ListItem;
import com.example.xiner.util.DownloadPicUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiner on 14-12-22.
 */
public class ShareCommentAdapter extends RecyclerView.Adapter<ShareCommentAdapter.ViewHolder> {

    private static String TAG = "ShareCommentAdapter";
    Context mContext;
    List<Comment>comments;
    ArrayList<String>picturepath;

    public ShareCommentAdapter(Context context,List<Comment>comments) {
        this.mContext = context;
        this.comments = comments;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View  viewlast, viewcard;

        ViewHolder vh;
         if (viewType == 2) {
            viewlast = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_detail_last, parent, false);

            vh = new ViewHolder(viewlast);
        } else {
            viewcard = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_detail_share_answer, parent, false);
            vh = new ViewHolder(viewcard);


        }
        return vh;
    }



    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.nicknametext.setText(comments.get(position).getUsername());
        holder.commenttext.setText(comments.get(position).getContent());
        SimpleDateFormat myFmt2=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        holder.timetext.setText(myFmt2.format(comments.get(position).getCreatedTime()));// 可能会有问题，因为有图片下载可能没有处理好
        new DownloadPicUtil(holder.head).execute(comments.get(position).getUserFigure());
        List<FileItem>fileItems = comments.get(position).getFiles();
        picturepath = new ArrayList<>();
        for (int i =0;i< fileItems.size();i++){
           String type = fileItems.get(i).getType();
            if (type.equals("PICTURE")){
               picturepath.add(fileItems.get(i).getUrl());

            }
        }
        if (picturepath.size()!=0){
            holder.gridView.setVisibility(View.VISIBLE);
            PictureAdapter pictureAdapter = new PictureAdapter(mContext,picturepath);
            holder.gridView.setAdapter(pictureAdapter);
        }

    }

    @Override
    public int getItemCount() {
        return comments.size()+1;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardview;
        private static String TAG = "ViewHolder";
        ImageView head;
        GridView gridView;

        TextView timetext,commenttext,nicknametext;

        public ViewHolder(View cardView) {
            super(cardView);

            timetext =(TextView)cardView.findViewById(R.id.timedetail);
            commenttext=(TextView)cardView.findViewById(R.id.comment_text);
            nicknametext=(TextView)cardView.findViewById(R.id.nickname_detail);
            head=(ImageView)cardView.findViewById(R.id.face);
            gridView =(GridView)cardView.findViewById(R.id.picturegridviewcomment);


        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        } else if (position == 6) {
            return 2;
        } else {
            return 1;
        }
    }
}
