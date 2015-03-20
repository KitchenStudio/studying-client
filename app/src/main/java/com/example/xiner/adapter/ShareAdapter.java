package com.example.xiner.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.xiner.R;
import com.example.xiner.activity.ShareDetailActivity;
import com.example.xiner.entity.Item;
import com.example.xiner.fragment.ShareFragment;
import com.example.xiner.net.ShareNetwork;


import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by xiner on 14-12-21.
 */
public class ShareAdapter extends RecyclerView.Adapter<ShareAdapter.ViewHolder> {
    private static final String TAG = "ShareAdapter";
    Context mContext;
    ArrayList<Item> shareitems;
    ShareNetwork shareNetwork = new ShareNetwork();
    ShareFragment shareFragment;

    public ShareAdapter(Context context, ArrayList<Item> shareitems,ShareFragment shareFragment) {
        this.shareFragment = shareFragment;
        this.mContext = context;
        this.shareitems = shareitems;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        Log.v(TAG, i + "type");
        ViewHolder holder;
        if (i == 0) {
            View view1 = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_detail_last, viewGroup, false);
            holder = new ViewHolder(view1);
        } else {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.list_share, viewGroup, false);
            holder = new ViewHolder(view);
        }
        return holder;

    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        Log.v(TAG, i + "position");
        if (shareitems.size() != 0 && i < shareitems.size()) {

            viewHolder.subject.setText(shareitems.get(i).getSubject());
            SimpleDateFormat myFmt2=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            viewHolder.time.setText(myFmt2.format(shareitems.get(i).getCreatedTime()));
            viewHolder.nickname.setText(shareitems.get(i).getOwner().getNickname());
            viewHolder.detail.setText(shareitems.get(i).getContent());

            viewHolder.collection.setText(shareitems.get(i).getStarNumber().toString());
            viewHolder.praise.setText(shareitems.get(i).getPraiseNumber().toString());
            viewHolder.mCardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra("subject", "科目是");
                    intent.putExtra("time", "时间是");
                    intent.putExtra("nickname", "昵称是");
                    intent.setClass(mContext, ShareDetailActivity.class);
                    mContext.startActivity(intent);
                }
            });
        } else {
            viewHolder.load.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewHolder.load.setClickable(false);

                    shareFragment.LoadMore();

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (shareitems.size() != 0) {
            return shareitems.size() + 1;
        } else {
            return 0;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public CardView mCardview;
        private String TAG = "ViewHolder";
        TextView subject, time, nickname, detail, collection, praise, comment;

        TextView load;

        public ViewHolder(View v) {
            super(v);
            subject = (TextView) v.findViewById(R.id.subject);

            time = (TextView) v.findViewById(R.id.time);

            nickname = (TextView) v.findViewById(R.id.nickname);

            detail = (TextView) v.findViewById(R.id.detail);

            collection = (TextView) v.findViewById(R.id.collection_num);

            praise = (TextView) v.findViewById(R.id.praise_num);

            comment = (TextView) v.findViewById(R.id.comment_num);
            mCardview = (CardView) v.findViewById(R.id.card_view_share);
            load = (TextView) v.findViewById(R.id.loadmore);
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (position == shareitems.size()) {
            return 0;
        } else {
            return 1;
        }

    }
}
