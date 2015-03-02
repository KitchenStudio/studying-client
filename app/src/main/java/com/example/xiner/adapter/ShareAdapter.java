package com.example.xiner.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xiner.R;
import com.example.xiner.activity.DetailShareActivity;


import java.util.ArrayList;
import java.util.zip.CRC32;

/**
 * Created by xiner on 14-12-21.
 */
public class ShareAdapter extends RecyclerView.Adapter<ShareAdapter.ViewHolder> {
    Context mContext;
    public ShareAdapter(Context context){
        this.mContext=context;
    }

//ArrayList<DetailShareEntity> shareList = new ArrayList<DetailShareEntity>();
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_share, viewGroup, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;

    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        viewHolder.mCardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("subject","科目是");
                intent.putExtra("time","时间是");
                intent.putExtra("nickname","昵称是");
                intent.setClass(mContext,DetailShareActivity.class);
                mContext.startActivity(intent);
            }
        });
        // viewHolder.mTextView.setText("hello" + i);

//        viewHolder.mCardview.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setClass(v.getContext(), DetailShareActivity.class);
//                v.getContext().startActivity(intent);
//                Log.v("ShareAdapter",i+"");
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public CardView mCardview;
        private static String TAG = "ViewHolder";
        TextView subject,time,grade,detail,collection,praise,comment;


        public ViewHolder(View v) {
            super(v);
            subject=(TextView)v.findViewById(R.id.subject);
            time=(TextView)v.findViewById(R.id.time);
            grade=(TextView)v.findViewById(R.id.grade);
            detail=(TextView)v.findViewById(R.id.detail);
            collection=(TextView)v.findViewById(R.id.collection_num);
            praise=(TextView)v.findViewById(R.id.praise_num);
            comment=(TextView)v.findViewById(R.id.comment_num);
            mCardview=(CardView)v.findViewById(R.id.card_view_share);

        }
    }

}
