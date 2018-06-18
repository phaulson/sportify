package com.spogss.sportifycommunity.data.connection.asynctasks;

import com.spogss.sportifycommunity.data.Comment;
import com.spogss.sportifycommunity.data.User;
import com.spogss.sportifycommunity.data.connection.QueryType;
import com.spogss.sportifycommunity.data.connection.SportifyClient;
import com.spogss.sportifycommunity.model.CommentModel;
import com.spogss.sportifycommunity.model.PostModel;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Pauli on 13.06.2018.
 */

public class LoadCommentModelsActivity extends ClientTask <Void, Void, Collection<CommentModel>> {
    private int postId;
    private int lastCommentId;
    private SportifyClient client;

    public LoadCommentModelsActivity(int postId, int lastCommentId) {
        this.postId = postId;
        this.lastCommentId = lastCommentId;
        client = SportifyClient.newInstance();
    }

    @Override
    protected Collection<CommentModel> doInBackground(Void... params) {
        return loadComments();
    }

    @Override
    protected void onPostExecute(final Collection<CommentModel> commentModels) {
        getListener().onSuccess(QueryType.LOAD_COMMENTS, commentModels, lastCommentId);
    }

    @Override
    protected void onCancelled() {
        getListener().onSuccess(QueryType.LOAD_COMMENTS);
    }

    /**
     * loads comment, user from database and returns the CommentModels
     *
     * @return a collection of CommentModels
     */
    private Collection<CommentModel> loadComments() {
        ArrayList<CommentModel> commentModels = new ArrayList<CommentModel>();
        ArrayList<Comment> comments;

        comments = (ArrayList<Comment>)client.getComments(postId, lastCommentId);

        for (Comment c : comments) {
            User u = client.getProfile(c.getCreatorId());
            commentModels.add(new CommentModel(c, u));
        }

        if (commentModels.size() > 0)
            this.lastCommentId = commentModels.get(commentModels.size() - 1).getComment().getId();

        return commentModels;
    }
}
