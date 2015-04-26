package com.example.xiner.adapter;

import android.app.UiAutomation;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xiner.R;
import com.example.xiner.entity.Comment;
import com.example.xiner.entity.DetailItem;
import com.example.xiner.entity.ListItem;
import com.example.xiner.util.HttpUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiner on 4/25/15.
 */
public class ShareDetailAdapter extends RecyclerView.Adapter<ShareDetailAdapter.ViewHoler> {

    private static final String TAG = "ShareDetailAdapter";

    Context context;
    List<Comment> commentList;
    DetailItem detailItem;
    Long id;
    int comments;
    int collectionNum;
    int praiseNum;
    ListItem listItem;

    public ShareDetailAdapter(Context context, List<Comment> commentList, DetailItem detailItem, ListItem listItem) {
        Log.v(TAG,commentList.size()+"sizesizesize");
        this.commentList = commentList;
        this.context = context;
        this.detailItem = detailItem;
        this.listItem = listItem;
    }

    @Override
    public ViewHoler onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewlast, viewcard, viewDetail;

        ViewHoler vh;
        if (viewType == 2) {

            viewlast = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_detail_last, parent, false);

            vh = new ViewHoler(viewlast);
        } else if (viewType == 0) {

            viewDetail = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_share_detail, parent, false);
            vh = new ViewHoler(viewDetail);
        } else {

            viewcard = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_detail_share_answer, parent, false);
            vh = new ViewHoler(viewcard);


        }
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHoler holder, int position) {
        SimpleDateFormat myFmt2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        clickListener clicklistenern  = new clickListener(holder);
        if (position == 0) {
            Log.v(TAG,holder.nicknametextdetail+"nicknamenickname");
            holder.nicknametextdetail.setText(listItem.getNickname());
            holder.timetextdetail.setText(myFmt2.format(listItem.getCreatedTime()));
            holder.subjecttext.setText(listItem.getSubject());
            holder.detailtext.setText(listItem.getContent());
            collectionNum = listItem.getStars();
            comments = listItem.getComments();
            praiseNum = listItem.getUps();
            id = listItem.getId();
            holder.collectionText.setText("(" + collectionNum + ")");
            holder.commentText.setText("(" + comments + ")");
            holder.praiseText.setText("(" + praiseNum + ")");
            holder.commentnum.setText(commentList.size()+"个评论");
            holder.collection.setOnClickListener(clicklistenern);
        } else if (position == commentList.size() + 1) {

        } else {
            holder.timetext.setText(myFmt2.format(commentList.get(position - 1).getCreatedTime()));
            holder.nicknametext.setText(commentList.get(position - 1).getUsername());
            holder.commenttext.setText(commentList.get(position - 1).getContent());
        }

    }

    class clickListener implements View.OnClickListener {
        ViewHoler viewHoler;
        public clickListener(ViewHoler viewHoler){
            this.viewHoler = viewHoler;
        }
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.idima_collection:
                    HttpUtil.post(HttpUtil.baseUrl + "/" + id + "/star", new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            String str = new String(responseBody);
                            if (str.equals("has been starred")) {
                                Toast.makeText(context, "已经收藏过", Toast.LENGTH_SHORT).show();
                                return;

                            } else {
                                collectionNum = collectionNum + 1;
                                viewHoler.collectionText.setText("(" + collectionNum + ")");

                                Toast.makeText(context, "收藏成功", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            Log.v(TAG, error.toString() + "error" + statusCode);
                            Toast.makeText(context, "收藏失败", Toast.LENGTH_SHORT).show();

                        }
                    });

                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return commentList.size() + 1;
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

    static class ViewHoler extends RecyclerView.ViewHolder {
        TextView timetext, commenttext, nicknametext;
        ImageView head;
        GridView gridView;
        private TextView collectionText, praiseText, commentText, nicknametextdetail, timetextdetail, subjecttext, detailtext,commentnum;

        private ImageView zan, collection, comment;

        public ViewHoler(View itemView) {
            super(itemView);

                timetext = (TextView) itemView.findViewById(R.id.timedetail);
                commenttext = (TextView) itemView.findViewById(R.id.comment_text);
                nicknametext = (TextView) itemView.findViewById(R.id.nickname_detail);
                head = (ImageView) itemView.findViewById(R.id.face);
                gridView = (GridView) itemView.findViewById(R.id.picturegridviewcomment);

                collectionText = (TextView) itemView.findViewById(R.id.collectionnum_text);
                praiseText = (TextView) itemView.findViewById(R.id.praisenum_text);
                commentText = (TextView) itemView.findViewById(R.id.commentnum_text);
                nicknametextdetail = (TextView) itemView.findViewById(R.id.nickname_text);
                timetextdetail = (TextView) itemView.findViewById(R.id.time_text);
                subjecttext = (TextView) itemView.findViewById(R.id.subject_text);
                detailtext = (TextView) itemView.findViewById(R.id.detail_text);
                zan = (ImageView) itemView.findViewById(R.id.idima_praise);
                collection = (ImageView) itemView.findViewById(R.id.idima_collection);
                comment = (ImageView) itemView.findViewById(R.id.idima_comment);
                commentnum=(TextView)itemView.findViewById(R.id.pinglun_numtext);

        }
    }
}
