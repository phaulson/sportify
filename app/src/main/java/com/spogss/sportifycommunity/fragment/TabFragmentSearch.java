package com.spogss.sportifycommunity.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.spogss.sportifycommunity.adapter.SearchListAdapter;
import com.spogss.sportifycommunity.R;

/**
 * Created by Pauli on 25.03.2018.
 */

public class TabFragmentSearch extends Fragment {
    private ListView listSearch;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_fragment_search, container, false);
        listSearch = (ListView) view.findViewById(R.id.listView_search);
        return view;
    }

    /**
     * sets the Adapter to the ListView
     * @param adapter the Adapter ro fill the ListView with
     */
    public void fillList(SearchListAdapter adapter) {
        listSearch.setAdapter(adapter);
    }
}
