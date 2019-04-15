package com.spogss.sportifycommunity.data.connection.asynctasks;

import com.spogss.sportifycommunity.data.connection.QueryType;
import com.spogss.sportifycommunity.data.connection.SportifyClient;

/**
 * Created by Pauli on 09.06.2018.
 */

public class SubscribeTask extends ClientTask<Void, Void, Boolean> {
    private int planId;
    private boolean subscribe;
    private SportifyClient client;

    public SubscribeTask(int planId, boolean subscribe) {
        this.planId = planId;
        this.subscribe = subscribe;
        client = SportifyClient.newInstance();
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        return  client.setPlanSubscription(planId, client.getCurrentUserID(), subscribe);
    }

    @Override
    protected void onPostExecute(final Boolean subscribed) {
        getListener().onSuccess(QueryType.SUBSCRIBE, subscribed);
    }

    @Override
    protected void onCancelled() {
        getListener().onSuccess(QueryType.SUBSCRIBE);
    }
}
