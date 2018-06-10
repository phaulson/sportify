package com.spogss.sportifycommunity.data.connection.asynctasks;

import com.spogss.sportifycommunity.data.connection.QueryType;
import com.spogss.sportifycommunity.data.connection.SportifyClient;

/**
 * Created by Pauli on 09.06.2018.
 */

public class SetDescriptionTask extends ClientTask<Void, Void, Boolean> {
    SportifyClient client;
    private String description;

    public SetDescriptionTask(String description){
        this.description = description;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        return client.changeDescription(client.getCurrentUserID(), description);
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        getListener().onSuccess(QueryType.SET_DESCRIPTION, success);
    }

    @Override
    protected void onCancelled() {
        getListener().onSuccess(QueryType.SET_DESCRIPTION);
    }
}
