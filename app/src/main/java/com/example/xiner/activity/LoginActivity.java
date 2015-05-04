package com.example.xiner.activity;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.xiner.R;
import com.example.xiner.net.LoginNetwork;

public class LoginActivity extends ActionBarActivity {

    EditText username,password;
    Button loginButton;
    LoginNetwork loginNetwork;
    Toolbar toolbar;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginNetwork = new LoginNetwork(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.drawable.ic_ab_drawer);
        }
        username =(EditText)findViewById(R.id.login_et_name);
        password=(EditText)findViewById(R.id.login_et_password);
        textView = (TextView)findViewById(R.id.register_text);
        loginButton=(Button)findViewById(R.id.loginbutton);

        LoginListener loginListener = new LoginListener();
        loginButton.setOnClickListener(loginListener);
        textView.setOnClickListener(loginListener);
    }

    class LoginListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.loginbutton:
                    loginNetwork.loginUpload(username.getText().toString(),password.getText().toString());//登陆按钮的操作

                    break;
                case R.id.register_text:
                    Intent intent = new Intent();
                    intent.setClass(LoginActivity.this,RegisterActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        return super.onOptionsItemSelected(item);
    }
}
