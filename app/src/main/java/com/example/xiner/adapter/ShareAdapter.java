package com.example.xiner.adapter;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.xiner.R;
import com.example.xiner.activity.DetailShareActivity;

/**
 * Created by xiner on 14-12-21.
 */
public class ShareAdapter extends RecyclerView.Adapter<ShareAdapter.ViewHolder> {


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_share, viewGroup, false);
        CardView v = (CardView) view.findViewById(R.id.card_view);
        ViewHolder vh = new ViewHolder(v);
        return vh;

    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        // viewHolder.mTextView.setText("hello" + i);

        viewHolder.mCardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(v.getContext(), DetailShareActivity.class);
                v.getContext().startActivity(intent);
                Log.v("ShareAdapter",i+"");
            }
        });


    }

    @Override
    public int getItemCount() {
        return 10;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public CardView mCardview;
        private static String TAG = "ViewHolder";
        public int position;


        public ViewHolder(CardView v) {

            super(v);
            mCardview = v;

        }
    }
}
