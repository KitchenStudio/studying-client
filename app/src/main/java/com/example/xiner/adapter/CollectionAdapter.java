package com.example.xiner.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.xiner.R;

/**
 * Created by xiner on 14-12-21.
 */
public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.ViewHolder> {
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_collection, viewGroup, false);
        CardView v = (CardView)view.findViewById(R.id.collection_card);
        ViewHolder vh = new ViewHolder(v);
        return vh;

    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        // viewHolder.mTextView.setText("hello" + i);

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private  CardView mTextView;
        private static String TAG="ViewHolder";

        public ViewHolder(CardView v) {

            super(v);
            mTextView = v;
            // Define click listener for the ViewHolder's View.
//            v.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Log.d(TAG, "Element " + getPosition() + " clicked.");
//                }
//            });
//          //  textView = (TextView) v.findViewById(R.id.textView);
//        }

//        public TextView getTextView() {
//            return textView;
        }
    }
}
