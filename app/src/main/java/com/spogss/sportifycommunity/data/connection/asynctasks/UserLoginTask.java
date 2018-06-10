package com.spogss.sportifycommunity.data.connection.asynctasks;

import com.spogss.sportifycommunity.data.connection.QueryType;
import com.spogss.sportifycommunity.data.connection.SportifyClient;

public class UserLoginTask extends ClientTask<Void, Void, Integer> {

    private final String mEmail;
    private final String mPassword;
    private SportifyClient client;

    public UserLoginTask(String email, String password) {
        mEmail = email;
        mPassword = password;
        client = SportifyClient.newInstance();
    }

    @Override
    protected Integer doInBackground(Void... params) {
        return client.login(mEmail, mPassword);
    }

    @Override
    protected void onPostExecute(final Integer loggedInUser) {
        getListener().onSuccess(QueryType.LOGIN, loggedInUser, mEmail, mPassword);
    }

    @Override
    protected void onCancelled() {
        getListener().onSuccess(QueryType.LOGIN);
    }
}