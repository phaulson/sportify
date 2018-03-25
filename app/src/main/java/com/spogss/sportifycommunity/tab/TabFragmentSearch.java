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

import java.util.List;

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
     * gets the ListView that contains the search results
     * @return ListView that contains the search results
     */
    public ListView getListSearch() {
        return listSearch;
    }

    /**
     * creates a new ArrayAdapter<T> with generic typing and
     * fills the ListView with data specified in the List<T> passed as parameter
     * @param activity the Activity that contains the ListView
     * @param list the List<t> that contains the search results
     * @param <T> the generic type (it cna be any)
     */
    public <T> void fillListSearch(Activity activity, List<T> list) {
        ArrayAdapter<T> adapter = new ArrayAdapter<T>(activity, android.R.layout.simple_list_item_1, list);
        listSearch.setAdapter(adapter);
    }
}
