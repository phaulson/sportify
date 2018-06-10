package com.spogss.sportifycommunity.data.connection.asynctasks;

public interface ClientQueryListener {
    public void onSuccess(Object... results);
    public void onFail(Object... errors);
}
