package com.example.xiner.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.xiner.R;
import com.example.xiner.main.AppBase;

public class SplashActivity extends ActionBarActivity {

    AppBase app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        app=AppBase.getApp();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
//                if (ifRegister()) {
//                    //
//                    intent.setClass(SplashActivity.this,
//                            LoginActivity.class);
//                } else {

                    intent.setClass(SplashActivity.this, RegisterActivity.class);
//                }
                startActivity(intent);
                finish();
            }
        }, 5000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

   private boolean ifRegister(){
     return   app.getDataStore().getBoolean("ifregister",false);
   }
}
