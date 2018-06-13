package com.spogss.sportifycommunity.data.connection.asynctasks;

import com.spogss.sportifycommunity.data.Post;
import com.spogss.sportifycommunity.data.connection.QueryType;
import com.spogss.sportifycommunity.data.connection.SportifyClient;

/**
 * Created by Pauli on 09.06.2018.
 */

public class SetLikeTask extends ClientTask <Void, Void, Boolean> {
    private Post post;
    private boolean like;
    SportifyClient client;

    public SetLikeTask(Post post, boolean like) {
        this.post = post;
        this.like = like;
        client = SportifyClient.newInstance();
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        return client.setLike(client.getCurrentUserID(), post.getId(), like);
    }

    @Override
    protected void onPostExecute(final Boolean liked) {
        getListener().onSuccess(QueryType.SET_LIKE, liked);
    }

    @Override
    protected void onCancelled() {
        getListener().onSuccess(QueryType.SET_LIKE);
    }
}
