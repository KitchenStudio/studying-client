package com.example.xiner.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SearchViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.xiner.R;
import com.example.xiner.activity.PublicDocActivity;
import com.example.xiner.adapter.ShareAdapter;
import com.example.xiner.net.Network;
import com.example.xiner.util.UploadUtil;
import com.example.xiner.view.RefreshLayout;

/**
 * Created by xiner on 14-12-20.
 */
public class ShareFragment extends Fragment{
    LinearLayoutManager mLayoutManager;
    RefreshLayout swipeRefreshLayout;
    Network network;
    UploadUtil uploadUtil;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_share,null);
        RecyclerView mRecyclerView = (RecyclerView)view.findViewById(R.id.recyclerView_share);
        swipeRefreshLayout = (RefreshLayout)view.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(new RefreshListener());
        swipeRefreshLayout.setOnLoadListener(new mLoadListener());

        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_red_light, android.R.color.holo_green_light,
                android.R.color.holo_blue_bright, android.R.color.holo_orange_light);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setAdapter(new ShareAdapter(getActivity()));

        new Thread(new Runnable() {
            @Override
            public void run() {
                network.getSharelist();
            }
        }).start();
//
        return view;
    }

    public ShareFragment (){
        network = new Network(getActivity());
//        uploadUtil = new UploadUtil(getActivity());
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    private class RefreshListener implements SwipeRefreshLayout.OnRefreshListener{

        @Override
        public void onRefresh() {
            Toast.makeText(getActivity(),"refresh",Toast.LENGTH_SHORT).show();

        }
    }
    class mLoadListener implements RefreshLayout.OnLoadListener{


        @Override
        public void onLoad() {
            Toast.makeText(getActivity(),"load",Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {


        MenuItem item1 = menu.add("Search");
        item1.setIcon(android.R.drawable.ic_menu_search);
        MenuItemCompat.setShowAsAction(item1, MenuItemCompat.SHOW_AS_ACTION_ALWAYS
                | MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
        final View searchView = SearchViewCompat.newSearchView(getActivity());
        if (searchView != null) {
            SearchViewCompat.setOnQueryTextListener(searchView,
                    new SearchViewCompat.OnQueryTextListenerCompat() {
                        @Override
                        public boolean onQueryTextChange(String newText) {
                            // Called when the action bar search text has changed.  Update
                            // the search filter, and restart the loader to do a new query
                            // with this filter.
                            String newFilter = !TextUtils.isEmpty(newText) ? newText : null;
                            // Don't do anything if the filter hasn't actually changed.
                            // Prevents restarting the loader when restoring state.
//                            if (mCurFilter == null && newFilter == null) {
//                                return true;
//                            }
//                            if (mCurFilter != null && mCurFilter.equals(newFilter)) {
//                                return true;
//                            }
//                            mCurFilter = newFilter;
                            //getLoaderManager().restartLoader(0, null, CollectionFragment.this);
                            return true;
                        }
                    });
            SearchViewCompat.setOnCloseListener(searchView,
                    new SearchViewCompat.OnCloseListenerCompat() {
                        @Override
                        public boolean onClose() {
                            if (!TextUtils.isEmpty(SearchViewCompat.getQuery(searchView))) {
                                SearchViewCompat.setQuery(searchView, null, true);
                            }
                            return true;
                        }

                    });
            MenuItemCompat.setActionView(item1, searchView);


            MenuItem item = menu.add("发布资料");
            item.setIcon(R.drawable.publicshare);
            MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_ALWAYS
                    | MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
            item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    Intent intent = new Intent();
                    intent.setClass(getActivity(),PublicDocActivity.class);
                    getActivity().startActivity(intent);
                    return false;
                }
            });
        }
        super.onCreateOptionsMenu(menu, inflater);
    }
}
