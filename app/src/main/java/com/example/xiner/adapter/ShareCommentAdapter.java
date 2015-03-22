package com.example.xiner.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xiner.R;
import com.example.xiner.entity.ListItem;

import java.text.SimpleDateFormat;

/**
 * Created by xiner on 14-12-22.
 */
public class ShareCommentAdapter extends RecyclerView.Adapter<ShareCommentAdapter.ViewHolder> {

    private static String TAG = "ShareCommentAdapter";
    Context mContext;
//    ListItem item;

    public ShareCommentAdapter(Context context) {
        this.mContext = context;
//        this.item = item;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewdetail, viewlast, viewcard;
        LinearLayout linearLayoutfirst;
        LinearLayout linearLayoutlast;
        TextView t;
        CardView cardview;
        ViewHolder vh;
//        mContext = parent.getContext();
//        if (viewType == 0) {
//            viewdetail = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_detail_first, parent, false);
//            vh = new ViewHolder(viewdetail);
//            vh.nicknametext.setText(item.getNickname());
//            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
//            vh.timetext.setText(sdf1.format(item.getCreatedTime()).toString());
//            vh.subjecttext.setText(item.getSubject());
//            vh.detailtext.setText(item.getContent());
//            vh.collectionText.setText("(" + item.getStars() + ")");
//            vh.commentText.setText("(" + item.getComments() + ")");
//            vh.praiseText.setText("(" + item.getUps() + ")");
//            vh.collection.setOnClickListener(new collectionListener());
//            vh.praise.setOnClickListener(new praiseListener());
//            vh.comment.setOnClickListener(new commentListener());
//        }
         if (viewType == 2) {
            viewlast = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_detail_last, parent, false);
            vh = new ViewHolder(viewlast);
        } else {
            viewcard = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_detail_share_answer, parent, false);
            vh = new ViewHolder(viewcard);
        }
        return vh;
    }

    class collectionListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Toast.makeText(mContext, "collection", Toast.LENGTH_SHORT).show();
        }
    }

    class praiseListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Toast.makeText(mContext, "praise", Toast.LENGTH_SHORT).show();
        }
    }

    class commentListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Toast.makeText(mContext, "comment", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


    }

    @Override
    public int getItemCount() {
        return 7;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
//        public LinearLayout linearLayout;
//        public TextView collectionText, praiseText, commentText, nicknametext, timetext, subjecttext, detailtext;
        CardView cardview;
        private static String TAG = "ViewHolder";
        ImageView collection, praise, comment;


//        public ViewHolder(View lineardetail) {
//            super(lineardetail);
//            collection = (ImageView) lineardetail.findViewById(R.id.detail_collection);
//            praise = (ImageView) lineardetail.findViewById(R.id.detail_praise);
//            comment = (ImageView) lineardetail.findViewById(R.id.detail_comment);
//            collectionText = (TextView) lineardetail.findViewById(R.id.collectionnum_text);
//            praiseText = (TextView) lineardetail.findViewById(R.id.praisenum_text);
//            commentText = (TextView) lineardetail.findViewById(R.id.commentnum_text);
//            nicknametext = (TextView) lineardetail.findViewById(R.id.nickname_detail);
//            timetext = (TextView) lineardetail.findViewById(R.id.time_text);
//            subjecttext = (TextView) lineardetail.findViewById(R.id.subject_text);
//            detailtext = (TextView) lineardetail.findViewById(R.id.detail_text);
//
//
//        }

        public ViewHolder(View cardView) {
            super(cardView);


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
