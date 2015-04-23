package com.example.xiner.fragment;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SearchViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.xiner.R;
import com.example.xiner.activity.SearchActivity;
import com.example.xiner.adapter.CollectionAdapter;
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
public class CollectionFragment extends Fragment{
    private static final String TAG = "CollectionFragment";
    LinearLayoutManager mLayoutManager;
    SwipeRefreshLayout swipeRefreshLayout;
    ShareNetwork shareNetwork;
    ArrayList<ListItem>shareItemlist = new ArrayList<>();
    ShareAdapter collectionAdapter;

    public static CollectionFragment newInstance(int position) {
        CollectionFragment f = new CollectionFragment();
        Bundle b = new Bundle();
        b.putInt("position", position);
        f.setArguments(b);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_collection,null);
        RecyclerView recyclerView =(RecyclerView)view.findViewById(R.id.recyclerView_collection);
        swipeRefreshLayout =(SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(new RefreshListener());
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_red_light, android.R.color.holo_green_light,
                android.R.color.holo_blue_bright, android.R.color.holo_orange_light);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        collectionAdapter = new ShareAdapter(getActivity(), shareItemlist, CollectionFragment.this);
        recyclerView.setAdapter(collectionAdapter);
        shareNetwork = new ShareNetwork(getActivity(), collectionAdapter, shareItemlist, swipeRefreshLayout);

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private class RefreshListener implements SwipeRefreshLayout.OnRefreshListener {

        @Override
        public void onRefresh() {
            HttpUtil.get(shareNetwork.getshareList+"/18366116016"+"/collectionlist", new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                    super.onSuccess(statusCode, headers, response);
                    shareItemlist.clear();
                    shareItemlist.addAll(shareNetwork.ParseNet(response));
                    collectionAdapter.notifyDataSetChanged();
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

    }    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, final MenuInflater inflater) {
        MenuItem item = menu.add("Search");
        item.setIcon(android.R.drawable.ic_menu_search);
        MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_ALWAYS
                | MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
       item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
           @Override
           public boolean onMenuItemClick(MenuItem item) {
               Intent intent = new Intent();
               intent.setClass(getActivity(), SearchActivity.class);
               startActivity(intent);
               return false;
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
                collectionAdapter.notifyDataSetChanged();
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
}
