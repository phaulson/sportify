package com.spogss.sportifycommunity.data.connection.asynctasks;

import com.spogss.sportifycommunity.data.Plan;
import com.spogss.sportifycommunity.data.connection.QueryType;
import com.spogss.sportifycommunity.data.connection.SportifyClient;
import com.spogss.sportifycommunity.model.PlanModel;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Pauli on 10.06.2018.
 */

public class LoadPlanModelsTask extends ClientTask <Void, Void, Collection<PlanModel>> {
    private int userId;
    private SportifyClient client;

    public LoadPlanModelsTask(int userId) {
        this.userId = userId;
        this.client = SportifyClient.newInstance();
    }

    @Override
    protected Collection<PlanModel> doInBackground(Void... voids) {
        return loadPlans();
    }

    @Override
    protected void onPostExecute(Collection<PlanModel> plans) {
        getListener().onSuccess(QueryType.LOAD_PLANS, plans);
    }

    @Override
    protected void onCancelled() {
        getListener().onSuccess(QueryType.LOAD_PLANS);
    }


    /**
     * loads Plan and numberOfSubscribers from database and returns the PlanModels
     *
     * @return a collection of PlanModels
     */
    private Collection<PlanModel> loadPlans() {
        ArrayList<PlanModel> planModels = new ArrayList<PlanModel>();
        ArrayList<Plan> plans = new ArrayList<>();

        if(userId == -1)
            plans = (ArrayList<Plan>) client.getSubscribedPlans(client.getCurrentUserID());
        else if (userId > -1)
            plans = (ArrayList<Plan>) client.getPlans(userId);

        for (Plan p : plans) {
            boolean subscribed = client.isPlanSubscribed(client.getCurrentUserID(), p.getId());
            int numberOfSubscribers = client.getNumberOfSubscribers(p.getId());
            planModels.add(new PlanModel(p, numberOfSubscribers, subscribed));
        }

        return planModels;
    }
}
