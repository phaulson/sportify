package com.spogss.sportifycommunity.data.connection.asynctasks;

import com.spogss.sportifycommunity.data.User;
import com.spogss.sportifycommunity.data.connection.QueryType;
import com.spogss.sportifycommunity.data.connection.SportifyClient;
import com.spogss.sportifycommunity.fragment.TabFragmentSearch;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Pauli on 09.06.2018.
 */

public class SearchUsersTask extends ClientTask<Void, Void, Collection<User>> {
    private String name;
    private TabFragmentSearch fragment;
    private boolean isPro;
    private SportifyClient client;

    public SearchUsersTask(String name, TabFragmentSearch fragment, boolean isPro) {
        this.name = name;
        this.fragment = fragment;
        this.isPro = isPro;
        client = SportifyClient.newInstance();
    }

    @Override
    protected Collection<User> doInBackground(Void... params) {
        // TODO: implement with lastUserID
        if(name.equals(""))
            return new ArrayList<User>();
        return client.searchUsers(name, isPro, -1);
    }

    @Override
    protected void onPostExecute(final Collection<User> users) {
        getListener().onSuccess(QueryType.SEARCH_USERS, users, fragment);
    }

    @Override
    protected void onCancelled() {
        getListener().onSuccess(QueryType.SEARCH_USERS);
    }
}
