package com.spogss.sportifycommunity.data.connection.asynctasks;

import com.spogss.sportifycommunity.data.User;
import com.spogss.sportifycommunity.data.connection.QueryType;
import com.spogss.sportifycommunity.data.connection.SportifyClient;
import com.spogss.sportifycommunity.model.UserModel;

/**
 * Created by Pauli on 09.06.2018.
 */

public class GetProfileTask extends ClientTask<Void, Void, UserModel> {
    private int userId;
    private SportifyClient client;

    public GetProfileTask(int userId) {
        this.userId = userId;
        this.client = SportifyClient.newInstance();
    }

    @Override
    protected UserModel doInBackground(Void... params) {
        UserModel userModel = new UserModel();
        userModel.setUser(client.getProfile(userId));
        userModel.setFollowing(client.isFollowing(client.getCurrentUserID(), userId));
        return userModel;
    }

    @Override
    protected void onPostExecute(final UserModel user) {
        getListener().onSuccess(QueryType.GET_PROFILE, user);
    }

    @Override
    protected void onCancelled() {
        getListener().onSuccess(QueryType.GET_PROFILE);
    }
}
