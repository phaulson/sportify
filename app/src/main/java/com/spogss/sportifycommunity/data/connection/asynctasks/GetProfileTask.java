package com.spogss.sportifycommunity.data.connection.asynctasks;

import com.spogss.sportifycommunity.data.User;
import com.spogss.sportifycommunity.data.connection.QueryType;
import com.spogss.sportifycommunity.data.connection.SportifyClient;

/**
 * Created by Pauli on 09.06.2018.
 */

public class GetProfileTask extends ClientTask<Void, Void, User> {
    private int userId;
    private SportifyClient client;

    public GetProfileTask(int userId) {
        this.userId = userId;
        this.client = SportifyClient.newInstance();
    }

    @Override
    protected User doInBackground(Void... params) {
        return client.getProfile(client.getCurrentUserID());
    }

    @Override
    protected void onPostExecute(final User user) {
        getListener().onSuccess(QueryType.GET_PROFILE, user);
    }

    @Override
    protected void onCancelled() {
        getListener().onSuccess(QueryType.GET_PROFILE);
    }
}
