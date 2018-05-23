package com.spogss.sportifycommunity.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.spogss.sportifycommunity.R;
import com.spogss.sportifycommunity.adapter.SectionsPageAdapter;
import com.spogss.sportifycommunity.data.DailyWorkout;
import com.spogss.sportifycommunity.data.Connection.SportifyClient;
import com.spogss.sportifycommunity.fragment.TabFragmentDailyWorkout;
import com.spogss.sportifycommunity.model.PlanModel;

import java.util.Collection;

public class PlanActivity extends AppCompatActivity implements View.OnClickListener {

    private PlanModel planModel;
    private SportifyClient client;
    SectionsPageAdapter sectionsPageAdapter;

    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);

        client = SportifyClient.newInstance();

        planModel = (PlanModel) getIntent().getSerializableExtra("plan");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(planModel.getPlan().getName());


        ImageView planPic = (ImageView) findViewById(R.id.imageView_plan);
        planPic.setImageResource(R.drawable.sp_plan);

        TextView planName = (TextView) findViewById(R.id.textView_plan_name);
        planName.setText(planModel.getPlan().getName());

        TextView subscribers = (TextView) findViewById(R.id.textView_plan_subscribers);
        subscribers.setText(planModel.getNumberOfSubscribers() + (planModel.getNumberOfSubscribers() == 1 ? " subscriber" : " subscribers"));

        Button subscribe = (Button) findViewById(R.id.button_plan_subscribe);
        subscribe.setOnClickListener(this);
        setButtonLayout(subscribe);

        viewPager = (ViewPager) findViewById(R.id.viewPagerDailyWorkout);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabsDailyWorkout);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new PageChangeListener(tabLayout));
    }

    private void setupViewPager(ViewPager viewPager) {
        new LoadDailyWorkoutsTask().execute((Void) null);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_plan_subscribe:
                subscribe((Button)v);
                break;
        }
    }

    private void setButtonLayout(Button button) {
        if (planModel.isSubscribed()) {
            button.setBackgroundResource(R.drawable.sp_button);
            button.setText("Unsubscribe");
            button.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
        }
        else {
            button.setBackgroundResource(R.drawable.sp_button_white);
            button.setText("Subscribe");
            button.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }
    }

    private void subscribe(Button button) {
        TextView subscribers = (TextView) findViewById(R.id.textView_plan_subscribers);

        if (planModel.isSubscribed()) {
            new SubscribeTask(planModel.getPlan().getId(), false).execute((Void) null);
            planModel.setSubscribed(false);
            planModel.setNumberOfSubscribers(planModel.getNumberOfSubscribers() - 1);
        } else {
            new SubscribeTask(planModel.getPlan().getId(), true).execute((Void) null);
            planModel.setSubscribed(true);
            planModel.setNumberOfSubscribers(planModel.getNumberOfSubscribers() + 1);
        }
        setButtonLayout(button);
        subscribers.setText(planModel.getNumberOfSubscribers() + (planModel.getNumberOfSubscribers() == 1 ? " subscriber" : " subscribers"));
    }

    //Listener
    private class PageChangeListener extends TabLayout.TabLayoutOnPageChangeListener {

        public PageChangeListener(TabLayout tabLayout) {
            super(tabLayout);
        }

        @Override
        public void onPageSelected(int index) {
            TabFragmentDailyWorkout fragment = ((TabFragmentDailyWorkout) sectionsPageAdapter.getItem(index));
            fragment.loadData();
        }
    }

    //AsyncTasks
    private class SubscribeTask extends AsyncTask<Void, Void, Boolean> {
        private int planId;
        private boolean subscribe;

        public SubscribeTask(int planId, boolean subscribe) {
            this.planId = planId;
            this.subscribe = subscribe;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            return  client.setPlanSubscription(planId, client.getCurrentUserID(), subscribe);
        }
    }

    private class LoadDailyWorkoutsTask extends AsyncTask<Void, Void, Collection<DailyWorkout>> {
        @Override
        protected Collection<DailyWorkout> doInBackground(Void... params) {
            return client.getDailyWorkouts(planModel.getPlan().getId());
        }

        @Override
        protected void onPostExecute(final Collection<DailyWorkout> dws) {
            sectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());
            for(DailyWorkout dw : dws) {
                TabFragmentDailyWorkout tabFragmentDailyWorkout = new TabFragmentDailyWorkout();

                Bundle bundle = new Bundle();
                bundle.putSerializable("dailyworkout", dw);
                tabFragmentDailyWorkout.setArguments(bundle);

                sectionsPageAdapter.addFragment(tabFragmentDailyWorkout, dw.getName());
            }
            viewPager.setAdapter(sectionsPageAdapter);

            if(viewPager.getAdapter().getCount() > 0)
                new PageChangeListener(tabLayout).onPageSelected(0);
        }
    }
}
