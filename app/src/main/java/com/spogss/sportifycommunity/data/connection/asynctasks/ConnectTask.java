package com.spogss.sportifycommunity.data.connection.asynctasks;


import com.spogss.sportifycommunity.data.connection.QueryType;
import com.spogss.sportifycommunity.data.connection.SportifyClient;

/**
 * Created by Pauli on 09.06.2018.
 */

public class ConnectTask extends ClientTask<Void, Void, Void> {
    private SportifyClient client;

    @Override
    protected Void doInBackground(Void... voids) {
        client = SportifyClient.newInstance();
        return null;
    }

    @Override
    protected void onPostExecute(Void v) {
        getListener().onSuccess(QueryType.CONNECT, client);
    }

    @Override
    protected void onCancelled() {
        getListener().onSuccess(QueryType.CONNECT);
    }
}
