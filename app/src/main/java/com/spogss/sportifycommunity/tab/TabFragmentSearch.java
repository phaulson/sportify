package com.spogss.sportifycommunity.tab;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.spogss.sportifycommunity.R;

import java.util.ArrayList;

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

    public ListView getListSearch() {
        return listSearch;
    }

    public <T> void fillListSearch(Activity activity, ArrayList<T> list) {
        ArrayAdapter<T> adapter = new ArrayAdapter<T>(activity, android.R.layout.simple_list_item_1, list);
        listSearch.setAdapter(adapter);
    }
}
