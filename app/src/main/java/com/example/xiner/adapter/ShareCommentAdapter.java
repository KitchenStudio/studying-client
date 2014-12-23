package com.example.xiner.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.xiner.R;

/**
 * Created by xiner on 14-12-22.
 */
public class ShareCommentAdapter extends RecyclerView.Adapter<ShareCommentAdapter.ViewHolder> {

    private static String TAG="ShareCommentAdapter";

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        LinearLayout linearLayoutfirst;
        LinearLayout linearLayoutlast;
        TextView t;
        CardView cardview;
        ViewHolder vh;

       if(viewType==0){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_detail_first,parent,false);
           linearLayoutfirst = (LinearLayout) view.findViewById(R.id.first_linear);
            vh = new ViewHolder(linearLayoutfirst);
        }else if(viewType ==2){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_detail_last,parent,false);
           linearLayoutlast = (LinearLayout) view.findViewById(R.id.last_linear);
            vh = new ViewHolder(linearLayoutlast);
        }else{
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_detail_share_answer,parent,false);
           cardview = (CardView) view.findViewById(R.id.card_viewdetailanswer);
            vh = new ViewHolder(cardview);
        }
        return vh;
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


        public ViewHolder(LinearLayout linear) {
            super(linear);
            linearLayout = linear;
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
