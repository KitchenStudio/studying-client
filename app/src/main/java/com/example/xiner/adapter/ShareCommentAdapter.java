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
    DetailItem detailItem;
//    List<Comment>comments;
    ArrayList<String>picturepath;

    public ShareCommentAdapter(Context context) {
        this.mContext = context;
        Log.v(TAG,"sharecomment");
//        this.comments = comments;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View  viewlast, viewcard,viewDetail;

        ViewHolder vh;
         if (viewType == 2) {
            viewlast = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_detail_last, parent, false);

            vh = new ViewHolder(viewlast);
        }else if (viewType==0){
             viewDetail = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_share_detail,parent,false);
             vh = new ViewHolder(viewDetail);
         }

         else {
            viewcard = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_detail_share_answer, parent, false);
            vh = new ViewHolder(viewcard);


        }
        return vh;
    }



    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
//        holder.holder
//        holder.nicknametext.setText(comments.get(position).getUsername());
//        holder.commenttext.setText(comments.get(position).getContent());
//        SimpleDateFormat myFmt2=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        holder.timetext.setText(myFmt2.format(comments.get(position).getCreatedTime()));// 可能会有问题，因为有图片下载可能没有处理好
//        new DownloadPicUtil(holder.head).execute(comments.get(position).getUserFigure());
//        List<FileItem>fileItems = comments.get(position).getFiles();
//        picturepath = new ArrayList<>();
//        for (int i =0;i< fileItems.size();i++){
//           String type = fileItems.get(i).getType();
//            if (type.equals("PICTURE")){
//               picturepath.add(fileItems.get(i).getUrl());
//
//            }
//        }
//        if (picturepath.size()!=0){
//            holder.gridView.setVisibility(View.VISIBLE);
//            PictureAdapter pictureAdapter = new PictureAdapter(mContext,picturepath);
//            holder.gridView.setAdapter(pictureAdapter);
//        }

    }

    @Override
    public int getItemCount() {
        return 7;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private static String TAG = "ViewHolder";
        ImageView head;
        GridView gridView;

        TextView timetext,commenttext,nicknametext;
        private TextView collectionText, praiseText, commentText, nicknametextdetail, timetextdetail, subjecttext, detailtext;

        private ImageView zan, collection, comment;


        public ViewHolder(View view) {
            super(view);
//            if (getItemViewType()==1) {
//                timetext = (TextView) view.findViewById(R.id.timedetail);
//                commenttext = (TextView) view.findViewById(R.id.comment_text);
//                nicknametext = (TextView) view.findViewById(R.id.nickname_detail);
//                head = (ImageView) view.findViewById(R.id.face);
//                gridView = (GridView) view.findViewById(R.id.picturegridviewcomment);
//            }
//            if (getItemViewType()==0){
//                collectionText = (TextView)view.findViewById(R.id.collectionnum_text);
//                praiseText = (TextView) view.findViewById(R.id.praisenum_text);
//                commentText = (TextView) view.findViewById(R.id.commentnum_text);
//                nicknametextdetail = (TextView) view.findViewById(R.id.nickname_text);
//                timetextdetail = (TextView) view.findViewById(R.id.time_text);
//                subjecttext = (TextView) view.findViewById(R.id.subject_text);
//                detailtext = (TextView) view.findViewById(R.id.detail_text);
//                zan = (ImageView) view.findViewById(R.id.idima_praise);
//                collection = (ImageView) view.findViewById(R.id.idima_collection);
//                comment = (ImageView) view.findViewById(R.id.idima_comment);
//            }

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
