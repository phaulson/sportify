package com.spogss.sportifycommunity.data.Connection.asynctasks;

public interface ClientQueryListener {
    public void onSuccess(Object... results);
    public void onFail(Object... errors);
}
