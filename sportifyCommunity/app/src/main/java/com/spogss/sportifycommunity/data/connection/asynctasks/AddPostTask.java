package com.spogss.sportifycommunity.data.connection.asynctasks;

import com.spogss.sportifycommunity.data.connection.QueryType;
import com.spogss.sportifycommunity.data.connection.SportifyClient;

/**
 * Created by Pauli on 09.06.2018.
 */

public class AddPostTask extends ClientTask <Void, Void, Integer> {
    private String caption;
    private SportifyClient client;

    public AddPostTask(String caption) {
        this.caption = caption;
        this.client = SportifyClient.newInstance();
    }
    @Override
    protected Integer doInBackground(Void... params) {
        return client.addPost(client.getCurrentUserID(), caption);
    }

    @Override
    protected void onPostExecute(final Integer postId) {
        getListener().onSuccess(QueryType.ADD_POST, postId);
    }

    @Override
    protected void onCancelled() {
        getListener().onSuccess(QueryType.ADD_POST);
    }
}
