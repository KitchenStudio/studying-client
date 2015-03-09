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
import com.example.xiner.entity.Item;


import java.util.ArrayList;
import java.util.zip.CRC32;

/**
 * Created by xiner on 14-12-21.
 */
public class ShareAdapter extends RecyclerView.Adapter<ShareAdapter.ViewHolder> {
    private static final String TAG = "ShareAdapter";
    Context mContext;
    ArrayList<Item>shareitems;

    public ShareAdapter(Context context, ArrayList<Item> shareitems){
        this.mContext=context;
        this.shareitems = shareitems;
        Log.v(TAG,shareitems.size()+"sharesize");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_share, viewGroup, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;

    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        viewHolder.subject.setText(shareitems.get(i).getSubject());
        viewHolder.time.setText(shareitems.get(i).getCreatedTime().toString());
        viewHolder.nickname.setText(shareitems.get(i).getOwner().getNickname());
        viewHolder.detail.setText(shareitems.get(i).getContent());
        viewHolder.collection.setText(shareitems.get(i).getStarNumber().toString());
        viewHolder.praise.setText(shareitems.get(i).getPraiseNumber().toString());
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
        return shareitems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public CardView mCardview;
        private  String TAG = "ViewHolder";
        TextView subject,time,nickname,detail,collection,praise,comment;



        public ViewHolder(View v) {
            super(v);
            subject=(TextView)v.findViewById(R.id.subject);

            time=(TextView)v.findViewById(R.id.time);

            nickname=(TextView)v.findViewById(R.id.nickname);

            detail=(TextView)v.findViewById(R.id.detail);

            collection=(TextView)v.findViewById(R.id.collection_num);

            praise=(TextView)v.findViewById(R.id.praise_num);

            comment=(TextView)v.findViewById(R.id.comment_num);
            mCardview=(CardView)v.findViewById(R.id.card_view_share);

        }
    }

}
