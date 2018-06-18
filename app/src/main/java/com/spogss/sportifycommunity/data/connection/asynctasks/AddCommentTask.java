package com.spogss.sportifycommunity.data.connection.asynctasks;

import com.spogss.sportifycommunity.data.connection.QueryType;
import com.spogss.sportifycommunity.data.connection.SportifyClient;

/**
 * Created by Pauli on 13.06.2018.
 */

public class AddCommentTask extends ClientTask <Void, Void, Integer> {
    private int postId;
    private String text;
    private SportifyClient client;

    public AddCommentTask(int postId, String text) {
        this.postId = postId;
        this.text = text;
        this.client = SportifyClient.newInstance();
    }

    @Override
    protected Integer doInBackground(Void... params) {
        return client.addComment(client.getCurrentUserID(), postId, text);
    }

    @Override
    protected void onPostExecute(final Integer commentId) {
        getListener().onSuccess(QueryType.ADD_COMMENT, commentId);
    }

    @Override
    protected void onCancelled() {
        getListener().onSuccess(QueryType.ADD_COMMENT);
    }
}
