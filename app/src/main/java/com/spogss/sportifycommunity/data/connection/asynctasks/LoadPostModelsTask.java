package com.spogss.sportifycommunity.data.connection.asynctasks;

import com.spogss.sportifycommunity.data.Post;
import com.spogss.sportifycommunity.data.User;
import com.spogss.sportifycommunity.data.connection.QueryType;
import com.spogss.sportifycommunity.data.connection.SportifyClient;
import com.spogss.sportifycommunity.model.PostModel;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Pauli on 09.06.2018.
 */

public class LoadPostModelsTask extends ClientTask<Void, Void, Collection<PostModel>> {
    private int userId;
    private int lastPostId;
    private boolean byCreator;
    private SportifyClient client;

    public LoadPostModelsTask(int userId, int lastPostId, boolean byCreator) {
        this.userId = userId;
        this.lastPostId = lastPostId;
        this.byCreator = byCreator;
        client = SportifyClient.newInstance();
    }

    @Override
    protected Collection<PostModel> doInBackground(Void... params) {
        return loadPosts();
    }

    @Override
    protected void onPostExecute(final Collection<PostModel> postModels) {
        getListener().onSuccess(QueryType.GET_POSTS, postModels, lastPostId);
    }

    @Override
    protected void onCancelled() {
        getListener().onSuccess(QueryType.GET_POSTS);
    }

    /**
     * loads Post, creator, numberOfLikes and isLiked from database and returns the PostModels
     *
     * @return a collection of PostModels
     */
    private Collection<PostModel> loadPosts() {
        ArrayList<PostModel> postModels = new ArrayList<PostModel>();
        ArrayList<Post> posts;

        if(byCreator)
            posts = (ArrayList<Post>) client.getPostsByCreator(userId, lastPostId);
        else
            posts = (ArrayList<Post>) client.getPosts(client.getCurrentUserID(), lastPostId);

        for (Post p : posts) {
            User u = client.getProfile(p.getCreatorId());
            int numberOfLikes = client.getNumberOfLikes(p.getId());
            boolean liked = client.isLiked(client.getCurrentUserID(), p.getId());
            postModels.add(new PostModel(p, u, numberOfLikes, liked));
        }

        if (postModels.size() > 0)
            this.lastPostId = (postModels).get(postModels.size() - 1).getPost().getId();

        return postModels;
    }
}
