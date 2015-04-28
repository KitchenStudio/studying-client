package com.example.xiner.activity;

import android.app.Dialog;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.xiner.R;
import com.example.xiner.entity.Comment;
import com.example.xiner.entity.CommentItem;
import com.example.xiner.main.AppBase;
import com.example.xiner.util.HttpUtil;
import com.example.xiner.util.LoadingDialog;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

public class CommentActivity extends ActionBarActivity {

    private static final String TAG = "CommentActivity";
    Toolbar toolbar;
    EditText commentEdit;
    Button release;
    Long id;
    private static String commenturl=HttpUtil.baseUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.drawable.backarrow);
        }
        id=getIntent().getExtras().getLong("id");
        Log.v(TAG,id+"id is");
        commentEdit=(EditText)findViewById(R.id.writecomment_edit);
        release =(Button)findViewById(R.id.publishcomment_button);
        clickListener clickListener = new clickListener();
        release.setOnClickListener(clickListener);
    }

    class clickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            final Dialog dialog = LoadingDialog.createDialog(CommentActivity.this, "正在上传，请稍后....");
            dialog.show();

            RequestParams params = new RequestParams();
            params.put("content",commentEdit.getText().toString());
            HttpUtil.post(commenturl+"/"+id+"/comment", params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    dialog.dismiss();
                    Toast.makeText(CommentActivity.this,"发布成功",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Toast.makeText(CommentActivity.this,"发布失败",Toast.LENGTH_SHORT).show();

                }
            });
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
