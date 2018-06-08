package com.spogss.sportifycommunity.data.Connection.asynctasks;

import android.app.DownloadManager;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.spogss.sportifycommunity.R;
import com.spogss.sportifycommunity.data.Connection.QueryType;
import com.spogss.sportifycommunity.data.Connection.SportifyClient;

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
        try {
            int uid = client.login(mEmail, mPassword);
            Log.i("uid", "uid: " + uid);
            return uid;
        }
        catch (Exception ex){
            return -1;
        }
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