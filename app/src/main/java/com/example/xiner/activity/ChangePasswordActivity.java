package com.example.xiner.activity;

import android.app.Dialog;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.xiner.R;
import com.example.xiner.main.AppBase;
import com.example.xiner.util.HttpUtil;
import com.example.xiner.util.LoadingDialog;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

public class ChangePasswordActivity extends ActionBarActivity {

    EditText rawpassword,newpassword,newpasswordagain;
    Button saveButton;
    Toolbar toolbar;
    private static final String changepasswordurl=HttpUtil.baseUserUrl+"/changepassword";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        if (toolbar!=null){
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.drawable.backarrow);
        }
        rawpassword =(EditText)findViewById(R.id.rawpassword);
        newpassword=(EditText)findViewById(R.id.newpassword);
        newpasswordagain=(EditText)findViewById(R.id.newpasswordagain);
        saveButton=(Button)findViewById(R.id.savepasswordbutton);
        ClickListener clickListener = new ClickListener();
        saveButton.setOnClickListener(clickListener);

    }

    class ClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            final Dialog dialog = LoadingDialog.createDialog(ChangePasswordActivity.this, "正在修改");

            switch (v.getId()){
                case R.id.savepasswordbutton:
                    if (rawpassword.getText().toString().equals("")||newpassword.getText().toString().equals("") || newpasswordagain.getText().toString().equals("")){
                        Toast.makeText(ChangePasswordActivity.this,"请将信息填写完整",Toast.LENGTH_SHORT).show();
                        dialog.dismiss();

                    }else if (!(newpassword.getText().toString()).equals(newpasswordagain.getText().toString())){
                        Toast.makeText(ChangePasswordActivity.this,"两次填写的密码不正确",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        dialog.show();
                        RequestParams params = new RequestParams();
                        params.put("rawpassword", rawpassword.getText().toString());
                        params.put("newpassword", newpassword.getText().toString());
                        HttpUtil.post(changepasswordurl, params, new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                String str = new String(responseBody);
                                if (str.equals("change success")) {
                                    AppBase.getApp().getDataStore().edit().putString("password",newpassword.getText().toString()).commit();
                                    dialog.dismiss();
                                    Toast.makeText(ChangePasswordActivity.this, "修改成功 ", Toast.LENGTH_SHORT).show();
                                } else if (str.equals("raw password is wrong")) {
                                    Toast.makeText(ChangePasswordActivity.this, "原密码不正确", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();

                                }
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                dialog.dismiss();
                                Toast.makeText(ChangePasswordActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    break;
            }
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
