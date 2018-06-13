package com.spogss.sportifycommunity.data.connection.asynctasks;

import com.spogss.sportifycommunity.data.DailyWorkout;
import com.spogss.sportifycommunity.data.connection.QueryType;
import com.spogss.sportifycommunity.data.connection.SportifyClient;

import java.util.Collection;

/**
 * Created by Pauli on 09.06.2018.
 */

public class LoadDailyWorkoutsTask extends ClientTask<Void, Void, Collection<DailyWorkout>> {
    private int planId;
    private SportifyClient client;

    public LoadDailyWorkoutsTask(int planId) {
        this.planId = planId;
        this.client = SportifyClient.newInstance();
    }

    @Override
    protected Collection<DailyWorkout> doInBackground(Void... params) {
        return client.getDailyWorkouts(planId);
    }

    @Override
    protected void onPostExecute(final Collection<DailyWorkout> dws) {
        getListener().onSuccess(QueryType.LOAD_DAILYWORKOUTS, dws);
    }

    @Override
    protected void onCancelled() {
        getListener().onSuccess(QueryType.LOAD_DAILYWORKOUTS);
    }
}
