package com.spogss.sportifycommunity.activity;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.spogss.sportifycommunity.R;
import com.spogss.sportifycommunity.adapter.PlansListAdapter;
import com.spogss.sportifycommunity.data.Plan;
import com.spogss.sportifycommunity.data.connection.SportifyClient;
import com.spogss.sportifycommunity.model.PlanModel;

import java.util.ArrayList;
import java.util.Collection;

public class ShowPlansActivity extends AppCompatActivity {
    private ListView listViewPlans;
    private View footerView;

    private SportifyClient client;
    private PlansListAdapter plansListAdapter;

    private int userid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_plans);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Plans");

        userid = (Integer) getIntent().getSerializableExtra("userid");

        client = SportifyClient.newInstance();
        plansListAdapter = new PlansListAdapter(getApplicationContext());

        listViewPlans = (ListView)findViewById(R.id.listView_show_plans);
        listViewPlans.setAdapter(plansListAdapter);

        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        footerView = layoutInflater.inflate(R.layout.list_footer_feed, null);

        new LoadPlansTask().execute((Void) null);
        listViewPlans.addHeaderView(footerView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * loads Plan and numberOfSubscribers from database and returns the PlanModels
     *
     * @return a collection of PlanModels
     */
    private Collection<PlanModel> loadPlans() {
        // TODO: find way to implement with better performance
        ArrayList<PlanModel> planModels = new ArrayList<PlanModel>();
        ArrayList<Plan> plans = new ArrayList<>();

        if(userid == -1)
            plans = (ArrayList<Plan>) client.getSubscribedPlans(client.getCurrentUserID());
        else if (userid > -1)
            plans = (ArrayList<Plan>) client.getPlans(userid);

        for (Plan p : plans) {
            boolean subscribed = client.isPlanSubscribed(client.getCurrentUserID(), p.getId());
            int numberOfSubscribers = client.getNumberOfSubscribers(p.getId());
            planModels.add(new PlanModel(p, numberOfSubscribers, subscribed));
        }

        return planModels;
    }


    //AsyncTasks
    private class LoadPlansTask extends AsyncTask<Void, Void, Collection<PlanModel>> {
        @Override
        protected Collection<PlanModel> doInBackground(Void... params) {
            return loadPlans();
        }

        @Override
        protected void onPostExecute(final Collection<PlanModel> plans) {
            plansListAdapter.addPlans(plans);
            listViewPlans.removeHeaderView(footerView);
        }
    }
}
