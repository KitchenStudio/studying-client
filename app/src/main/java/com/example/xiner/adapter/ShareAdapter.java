package com.example.xiner.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.xiner.activity.ShareDetailActivity;

import com.example.xiner.R;
import com.example.xiner.entity.DetailItem;
import com.example.xiner.entity.ListItem;
import com.example.xiner.fragment.CollectionFragment;
import com.example.xiner.fragment.ShareFragment;
import com.example.xiner.net.ShareNetwork;
import com.example.xiner.util.HttpUtil;
import com.loopj.android.http.JsonHttpResponseHandler;


import org.apache.http.Header;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by xiner on 14-12-21.
 */
public class ShareAdapter extends RecyclerView.Adapter<ShareAdapter.ViewHolder> {
    private static final String TAG = "ShareAdapter";
    Context mContext;
    ArrayList<ListItem> shareitems;
    ShareNetwork shareNetwork = new ShareNetwork();
    ShareFragment shareFragment;
    CollectionFragment collectionFragment;

    public ShareAdapter(Context context, ArrayList<ListItem> shareitems,ShareFragment shareFragment) {
        this.shareFragment = shareFragment;
        this.mContext = context;
        this.shareitems = shareitems;
    }
    public ShareAdapter(Context context, ArrayList<ListItem> shareitems) {
        this.mContext = context;
        this.shareitems = shareitems;
    }

    public ShareAdapter(Context context, ArrayList<ListItem> shareItemlist, CollectionFragment collectionFragment) {
        this.collectionFragment = collectionFragment;
        this.shareitems = shareItemlist;
        this.mContext = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

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
        if (shareitems.size() != 0 && i < shareitems.size()) {

            final ListItem listItem = shareitems.get(i);

            viewHolder.subject.setText(listItem.getSubject());
            SimpleDateFormat myFmt2=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            viewHolder.time.setText(myFmt2.format(listItem.getCreatedTime()));
            viewHolder.nickname.setText(listItem.getNickname());
            viewHolder.detail.setText(listItem.getContent());
            viewHolder.collection.setText("("+listItem.getStars().toString()+")");
//            viewHolder.praise.setText("("+listItem.getUps().toString()+")");
            viewHolder.comment.setText("("+listItem.getComments().toString()+")");


            viewHolder.mCardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Long id = listItem.getId();
                    Log.v(TAG,"点击了");
                    HttpUtil.get(shareNetwork.getitemdetail+"/"+id, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);
                            DetailItem item = shareNetwork.ParseNet(response);
                            Intent intent = new Intent();
                            intent.setClass(mContext, ShareDetailActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("detailitem",item);
                            bundle.putSerializable("listitem",listItem);
                            Log.v(TAG,listItem.getContent()+"listitemlistitem");
                            intent.putExtras(bundle);

                            mContext.startActivity(intent);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            super.onFailure(statusCode, headers, responseString, throwable);
                        }
                    });

                }
            });
        } else {
            viewHolder.load.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (shareFragment!=null) {
                        viewHolder.load.setClickable(false);

                        shareFragment.LoadMore();
                    }

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

//            praise = (TextView) v.findViewById(R.id.praise_num);

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
