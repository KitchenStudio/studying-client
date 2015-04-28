package com.example.xiner.activity;

import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.xiner.R;
import com.example.xiner.entity.User;
import com.example.xiner.net.RegisterNetwork;
import com.example.xiner.util.HttpUtil;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends ActionBarActivity {

    Toolbar toolbar;
    Button registerButton;
    EditText emailEdit,passwordEdit,passwordagainEdit,nicknameEdit;
    RegisterNetwork registerNetwork;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        registerNetwork = new RegisterNetwork(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.drawable.ic_ab_drawer);
        }

        registerButton=(Button)findViewById(R.id.register_button);
        emailEdit =(EditText)findViewById(R.id.email_register);
        passwordEdit=(EditText)findViewById(R.id.password_register);
        nicknameEdit=(EditText)findViewById(R.id.nickname_register);
        passwordagainEdit=(EditText)findViewById(R.id.passwordagain_register);
        registerButton.setOnClickListener(new RegisterListener());
    }

    class RegisterListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            if (emailEdit.getText().toString().equals("") || passwordEdit.getText().toString().equals("") || passwordagainEdit.getText().toString().equals("")) {
                Toast.makeText(RegisterActivity.this, "请将信息填写完整", Toast.LENGTH_SHORT).show();
            }
//            else if (!emailFormat(emailEdit.getText().toString())) {
//                Toast.makeText(RegisterActivity.this, "邮箱格式不正确", Toast.LENGTH_SHORT).show();
//            }
            else if (!((passwordEdit.getText().toString()).equals(passwordagainEdit.getText().toString()))) {
                Toast.makeText(RegisterActivity.this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
            } else {
                registerNetwork.uploadRegister(emailEdit.getText().toString(),passwordEdit.getText().toString(),nicknameEdit.getText().toString());
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


        return super.onOptionsItemSelected(item);
    }

    public static boolean emailFormat(String email)
    {
        boolean tag = true;
        final String pattern1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        final Pattern pattern = Pattern.compile(pattern1);
        final Matcher mat = pattern.matcher(email);
        if (!mat.find()) {
            tag = false;
        }
        return tag;
    }
}
