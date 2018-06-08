package com.spogss.sportifycommunity.data.Connection.asynctasks;

import android.os.AsyncTask;

public abstract class ClientTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {
    private ClientQueryListener listener;

    public ClientQueryListener getListener() {
        return listener;
    }

    public void setListener(ClientQueryListener listener) {
        this.listener = listener;
    }
}
