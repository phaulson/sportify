package com.spogss.sportifycommunity.activity;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.spogss.sportifycommunity.R;
import com.spogss.sportifycommunity.adapter.PlansListAdapter;
import com.spogss.sportifycommunity.data.Plan;
import com.spogss.sportifycommunity.data.connection.SportifyClient;
import com.spogss.sportifycommunity.data.connection.asynctasks.ClientQueryListener;
import com.spogss.sportifycommunity.model.PlanModel;

import java.util.ArrayList;
import java.util.Collection;

public class ShowPlansActivity extends AppCompatActivity implements ClientQueryListener {
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

        client.getPlansAsync(userid, this);
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

    @Override
    public void onSuccess(Object... results) {
        plansListAdapter.addPlans((Collection<PlanModel>)results[1]);
        listViewPlans.removeHeaderView(footerView);
    }

    @Override
    public void onFail(Object... errors) {
        Toast.makeText(this, "Error while loading plans", Toast.LENGTH_SHORT).show();
    }
}
