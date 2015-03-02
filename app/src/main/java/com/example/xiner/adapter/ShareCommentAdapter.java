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

/**
 * Created by xiner on 14-12-22.
 */
public class ShareCommentAdapter extends RecyclerView.Adapter<ShareCommentAdapter.ViewHolder> {

    private static String TAG="ShareCommentAdapter";
    Context mContext;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewdetail,viewlast,viewcard;
        LinearLayout linearLayoutfirst;
        LinearLayout linearLayoutlast;
        TextView t;
        CardView cardview;
        ViewHolder vh;
        mContext=parent.getContext();
       if(viewType==0){
            viewdetail = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_detail_first,parent,false);
            vh = new ViewHolder(viewdetail);
           vh.collection.setOnClickListener(new collectionListener());
           vh.praise.setOnClickListener(new praiseListener());
           vh.comment.setOnClickListener(new commentListener());
        }else if(viewType ==2){
           viewlast = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_detail_last,parent,false);
            vh = new ViewHolder(viewlast);
        }else{
           viewcard = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_detail_share_answer,parent,false);
            vh = new ViewHolder(viewcard);
        }
        return vh;
    }

    class collectionListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Toast.makeText(mContext,"collection",Toast.LENGTH_SHORT).show();
        }
    }
    class praiseListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Toast.makeText(mContext,"praise",Toast.LENGTH_SHORT).show();
        }
    }

    class commentListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Toast.makeText(mContext,"comment",Toast.LENGTH_SHORT).show();
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
        public LinearLayout linearLayout;
        public TextView mTextView;
        CardView cardview;
        private static String TAG = "ViewHolder";
        ImageView collection,praise,comment;


        public ViewHolder(View lineardetail) {
            super(lineardetail);
            collection=(ImageView)lineardetail.findViewById(R.id.detail_collection);
            praise=(ImageView)lineardetail.findViewById(R.id.detail_praise);
            comment=(ImageView)lineardetail.findViewById(R.id.detail_comment);

        }

        public ViewHolder(CardView cardView){
            super(cardView);
            cardview = cardView;

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
