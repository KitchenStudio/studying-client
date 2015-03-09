package com.example.xiner.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.xiner.R;

/**
 * Created by xiner on 15-3-8.
 */
public class LoadingDialog extends Dialog {

    public LoadingDialog(Context context) {
        super(context);
    }

    public static Dialog createDialog(Context context,String message){
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_style,null);
        LinearLayout linearLayout = (LinearLayout)view.findViewById(R.id.dialog_view);
        TextView textView =(TextView)view.findViewById(R.id.dialog_TextView);
        ImageView imageView =(ImageView)view.findViewById(R.id.dialog_img);
        Animation animation = AnimationUtils.loadAnimation(context,R.anim.loading_anim);
        imageView.startAnimation(animation);
        textView.setTextColor(Color.parseColor("#00C5CD"));
        textView.setText(message);

        Dialog dialog = new Dialog(context,R.style.CustomProgressDialog);

        dialog.setCancelable(false);
        dialog.setContentView(linearLayout,new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        return dialog;

    }
}
