package com.spogss.sportifycommunity.data.connection.asynctasks;

import com.spogss.sportifycommunity.data.User;
import com.spogss.sportifycommunity.data.connection.QueryType;
import com.spogss.sportifycommunity.data.connection.SportifyClient;

/**
 * Created by Pauli on 09.06.2018.
 */

public class FollowUserTask extends ClientTask <Void, Void, Boolean> {
    private int followsId;
    private boolean follow;
    private SportifyClient client;

    public FollowUserTask(int followsId, boolean follow) {
        this.followsId = followsId;
        this.follow = follow;
        this.client = SportifyClient.newInstance();
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        return client.setUserFollow(client.getCurrentUserID(),followsId, follow);
    }

    @Override
    protected void onPostExecute(final Boolean follow) {
        getListener().onSuccess(QueryType.FOLLOW, follow);
    }

    @Override
    protected void onCancelled() {
        getListener().onSuccess(QueryType.FOLLOW);
    }
}
