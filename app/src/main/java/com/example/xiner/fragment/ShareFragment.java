package com.example.xiner.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SearchViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.xiner.R;
import com.example.xiner.activity.PublicItemActivity;
import com.example.xiner.activity.SearchActivity;
import com.example.xiner.adapter.ShareAdapter;

import com.example.xiner.entity.ListItem;
import com.example.xiner.main.AppBase;
import com.example.xiner.net.ShareNetwork;
import com.example.xiner.util.HttpUtil;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by xiner on 14-12-20.
 */
public class ShareFragment extends Fragment {
    LinearLayoutManager mLayoutManager;
    SwipeRefreshLayout swipeRefreshLayout;
    ShareNetwork shareNetwork;
    private String TAG = "ShareFragment";
    ArrayList<ListItem> shareItemlist = new ArrayList<>();
    ShareAdapter shareAdapter;
    RefreshListener listener;
    RecyclerView mRecyclerView;



    public static ShareFragment newInstance(int position) {
        ShareFragment f = new ShareFragment();
        Bundle b = new Bundle();
        b.putInt("position", position);
        f.setArguments(b);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_share, null);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_share);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);

        swipeRefreshLayout.setOnRefreshListener(new RefreshListener());
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_red_light, android.R.color.holo_green_light,
                android.R.color.holo_blue_bright, android.R.color.holo_orange_light);


        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        shareAdapter = new ShareAdapter(getActivity(), shareItemlist, ShareFragment.this);
        mRecyclerView.setAdapter(shareAdapter);
        shareNetwork = new ShareNetwork(getActivity(), shareAdapter, shareItemlist, swipeRefreshLayout);




        swipeRefreshLayout.setProgressViewOffset(false, 0,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
        swipeRefreshLayout.setRefreshing(true);
        LoadInfo();
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        swipeRefreshLayout.setProgressViewOffset(false, 0,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
        swipeRefreshLayout.setRefreshing(true);
        LoadInfo();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "onCreate");
        setHasOptionsMenu(true);
    }


    private class RefreshListener implements SwipeRefreshLayout.OnRefreshListener {

        @Override
        public void onRefresh() {
            Log.v(TAG,"refreshingrefreshing");
            LoadInfo();
        }

    }

    private void LoadInfo(){
        HttpUtil.get(shareNetwork.getshareList, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                super.onSuccess(statusCode, headers, response);
                shareItemlist.clear();
                shareItemlist.addAll(shareNetwork.ParseNet(response));
                shareAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
                AppBase.getApp().getDataStore().edit().putInt("contentpage", 0).commit();
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.v(TAG, statusCode + "codefailer");
                throwable.printStackTrace(System.out);
            }
        });

    }


    public void LoadMore() {

        RequestParams params = new RequestParams();
        int contentPage = AppBase.getApp().getDataStore().getInt("contentpage", 0);
        contentPage++;
        params.put("page", contentPage);
        final int finalContentPage = contentPage;
        Log.v(TAG, finalContentPage + "content");
        HttpUtil.get(shareNetwork.getshareList, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                ArrayList<ListItem> listitem = shareNetwork.ParseNet(response);
                shareItemlist.addAll(listitem);
                shareAdapter.notifyDataSetChanged();
                AppBase.getApp().getDataStore().edit().putInt("contentpage", finalContentPage).commit();


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                throwable.printStackTrace(System.out);
                Log.v(TAG, "failer");
            }
        });

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {


        MenuItem item1 = menu.add("Search");
        item1.setIcon(R.drawable.ic_menu_search);
        MenuItemCompat.setShowAsAction(item1, MenuItemCompat.SHOW_AS_ACTION_ALWAYS
                | MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);

        item1.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), SearchActivity.class);
                getActivity().startActivity(intent);
                return false;
            }
        });


        MenuItem item = menu.add("发布资料");
        item.setIcon(R.drawable.publicshare);
        MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_ALWAYS
                | MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), PublicItemActivity.class);
                getActivity().startActivity(intent);

                return false;
            }
        });


        super.onCreateOptionsMenu(menu, inflater);
    }


}
