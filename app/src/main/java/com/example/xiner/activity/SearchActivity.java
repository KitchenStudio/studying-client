package com.example.xiner.activity;

import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.xiner.R;
import com.example.xiner.adapter.CollectionAdapter;
import com.example.xiner.adapter.ShareAdapter;
import com.example.xiner.entity.ListItem;
import com.example.xiner.net.ShareNetwork;
import com.example.xiner.util.HttpUtil;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchActivity extends ActionBarActivity {

    private static final String TAG = "SearchActivity";
    Toolbar toolbar;
    ImageView backArraw;
    Button searchButton;
    EditText searchEdit;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    ShareAdapter shareAdapter;
    ShareNetwork shareNetwork;
    ArrayList<ListItem> searchlist = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowCustomEnabled(true);
            getSupportActionBar().setCustomView(R.layout.activity_search_customview);

        }
        searchListener searchListener = new searchListener();
        searchButton =(Button)findViewById(R.id.search_button);
        searchButton.setOnClickListener(searchListener);
        searchEdit =(EditText)findViewById(R.id.seach_edittext);

        backArraw =(ImageView)findViewById(R.id.search_back_arrow);
        backArraw.setOnClickListener(searchListener);
        recyclerView =(RecyclerView)findViewById(R.id.recyclerView_search);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        shareAdapter = new ShareAdapter(this,searchlist);
        recyclerView.setAdapter(shareAdapter);
        shareNetwork = new ShareNetwork(this, shareAdapter, searchlist);

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


        return super.onOptionsItemSelected(item);
    }

    class searchListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {

            switch (v.getId()){
                case R.id.search_button:
                    String keyword = searchEdit.getText().toString();
                    if (searchEdit.getText().toString()!=null) {
//                        RequestParams params = new RequestParams();

//                        params.put("keyword", searchEdit.getText().toString());

                        HttpUtil.get(HttpUtil.baseUrl+"/search?keyword="+keyword,new JsonHttpResponseHandler(){
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                                super.onSuccess(statusCode, headers, response);
                                searchlist.clear();
                                searchlist.addAll(shareNetwork.ParseNet(response));
                                shareAdapter.notifyDataSetChanged();

                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                super.onFailure(statusCode, headers, responseString, throwable);
                            }
                        });
                    }

                    break;
                case R.id.search_back_arrow:
                    Log.v(TAG,"click back arrow");
                    finish();
                break;
            }


        }
    }
}
