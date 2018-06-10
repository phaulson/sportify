package com.spogss.sportifycommunity.data.connection.asynctasks;

import com.spogss.sportifycommunity.data.Plan;
import com.spogss.sportifycommunity.data.connection.QueryType;
import com.spogss.sportifycommunity.data.connection.SportifyClient;
import com.spogss.sportifycommunity.fragment.TabFragmentSearch;
import com.spogss.sportifycommunity.model.PlanModel;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Pauli on 09.06.2018.
 */

public class SearchPlansTask extends ClientTask<Void, Void, Collection<PlanModel>> {
    private String name;
    private TabFragmentSearch fragment;
    private SportifyClient client;

    public SearchPlansTask(String name, TabFragmentSearch fragment) {
        this.name = name;
        this.fragment = fragment;
        client = SportifyClient.newInstance();
    }

    @Override
    protected Collection<PlanModel> doInBackground(Void... params) {
        // TODO: implement with lastPlanID
        if(name.equals(""))
            return new ArrayList<PlanModel>();

        ArrayList<PlanModel> planModels = new ArrayList<PlanModel>();
        ArrayList<Plan> plans = (ArrayList<Plan>) client.searchPlans(name, -1);
        for (Plan p : plans) {
            boolean subscribed = client.isPlanSubscribed(client.getCurrentUserID(), p.getId());
            int numberOfSubscribers = client.getNumberOfSubscribers(p.getId());
            planModels.add(new PlanModel(p, numberOfSubscribers, subscribed));
        }

        return planModels;
    }

    @Override
    protected void onPostExecute(final Collection<PlanModel> plans) {
        getListener().onSuccess(QueryType.SEARCH_PLANS, plans, fragment);
    }

    @Override
    protected void onCancelled() {
        getListener().onSuccess(QueryType.SEARCH_PLANS);
    }
}
