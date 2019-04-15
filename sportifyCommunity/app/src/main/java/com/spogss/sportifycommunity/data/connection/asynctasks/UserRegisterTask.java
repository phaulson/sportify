package com.spogss.sportifycommunity.data.connection.asynctasks;

import com.spogss.sportifycommunity.data.connection.QueryType;
import com.spogss.sportifycommunity.data.connection.SportifyClient;

public class UserRegisterTask extends ClientTask<Void, Void, Integer> {
    private final String mEmail;
    private final String mPassword;

    private SportifyClient client;

    public UserRegisterTask(String email, String password) {
        mEmail = email;
        mPassword = password;
        client = SportifyClient.newInstance();
    }

    @Override
    protected Integer doInBackground(Void... params) {
        int u = client.register(mEmail, mPassword, false);
        return u;
    }

    @Override
    protected void onPostExecute(final Integer success) {
        getListener().onSuccess(QueryType.REGISTER, success, mEmail, mPassword);
    }

    @Override
    protected void onCancelled() {
        getListener().onFail(QueryType.REGISTER);
    }
}
