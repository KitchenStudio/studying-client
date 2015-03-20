package com.example.xiner.fragment;

import android.app.LoaderManager;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SearchViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.xiner.R;
import com.example.xiner.adapter.CollectionAdapter;

/**
 * Created by xiner on 14-12-20.
 */
public class CollectionFragment extends Fragment{
    LinearLayoutManager mLayoutManager;

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
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        CollectionAdapter collectionAdapter = new CollectionAdapter();
        recyclerView.setAdapter(collectionAdapter);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       // getLoaderManager().initLoader(0,null,this);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem item = menu.add("Search");
        item.setIcon(android.R.drawable.ic_menu_search);
        MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_ALWAYS
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
            MenuItemCompat.setActionView(item, searchView);
        }

    }


}
