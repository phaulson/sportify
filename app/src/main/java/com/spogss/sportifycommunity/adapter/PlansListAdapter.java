package com.spogss.sportifycommunity.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.spogss.sportifycommunity.R;
import com.spogss.sportifycommunity.activity.PlanActivity;
import com.spogss.sportifycommunity.data.Connection.SportifyClient;
import com.spogss.sportifycommunity.model.PlanModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * Created by Pauli on 25.04.2018.
 */

public class PlansListAdapter extends BaseAdapter implements View.OnTouchListener, View.OnClickListener {
    private Context context;

    private HashMap<Integer, PlanModel> content = new HashMap<Integer, PlanModel>();
    private ArrayList<Integer> keys = new ArrayList<Integer>();

    public PlansListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return content.size();
    }

    @Override
    public Object getItem(int i) {
        return content.get(keys.get(i));
    }

    @Override
    public long getItemId(int i) {
        return keys.get(i);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater.from(context));
        view = inflater.inflate(R.layout.list_content_show_plans, null);


        //get UI controls
        RelativeLayout rl = (RelativeLayout)view.findViewById(R.id.relativeLayout_plans);
        ImageView profilePic = (ImageView)view.findViewById(R.id.imageView_show_plans);
        TextView name = (TextView)view.findViewById(R.id.textView_show_plans_name);
        TextView subscribers = (TextView)view.findViewById(R.id.textView_show_plans_subscribers);
        Button subscribe = (Button)view.findViewById(R.id.button_plans_subscribe);

        PlanModel model = content.get(keys.get(i));
        rl.setTag(keys.get(i));
        subscribe.setTag(keys.get(i));

        name.setText(model.getPlan().getName());
        subscribers.setText(model.getNumberOfSubscribers() + (model.getNumberOfSubscribers() == 1 ? " subscriber" : " subscribers"));

        //TODO: implement with real plan pictures
        profilePic.setImageResource(R.drawable.sp_plan);



        //add eventListeners
        rl.setOnTouchListener(this);
        subscribe.setOnClickListener(this);

        return view;
    }

    /**
     * adds a user to the Adapter
     * @param plans the plans that should be added
     */
    public void addPlans(Collection<PlanModel> plans) {
        for(PlanModel p : plans) {
            content.put(p.getPlan().getId(), p);
            keys.add(p.getPlan().getId());
        }
    }


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
            int idRl = Integer.parseInt(view.getTag().toString());
            PlanModel planModel = content.get(idRl);

            Intent intent = new Intent(context, PlanActivity.class);
            intent.putExtra("plan", planModel);
            context.startActivity(intent);
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_plans_subscribe:
                Button button = (Button)v;
                PlanModel planModel = content.get(Integer.parseInt(button.getTag().toString()));
                TextView subscribers = (TextView) ((RelativeLayout)button.getParent()).findViewById(R.id.textView_show_plans_subscribers);

                if(planModel.isSubscribed()) {
                    new SubscribeTask(planModel.getPlan().getId(), false).execute((Void) null);
                    planModel.setSubscribed(false);
                    planModel.setNumberOfSubscribers(planModel.getNumberOfSubscribers() - 1);
                    button.setText("Subscribe");
                    button.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
                    button.setBackgroundResource(R.drawable.sp_button_white);
                }
                else {
                    new SubscribeTask(planModel.getPlan().getId(), true).execute((Void) null);
                    planModel.setSubscribed(true);
                    planModel.setNumberOfSubscribers(planModel.getNumberOfSubscribers() + 1);
                    button.setText("Unubscribe");
                    button.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
                    button.setBackgroundResource(R.drawable.sp_button);
                }
                subscribers.setText(planModel.getNumberOfSubscribers() + (planModel.getNumberOfSubscribers() == 1 ? " subscriber" : " subscribers"));
                break;
        }
    }

    private class SubscribeTask extends AsyncTask<Void, Void, Boolean> {
        private int planId;
        private boolean subscribe;

        public SubscribeTask(int planId, boolean subscribe) {
            this.planId = planId;
            this.subscribe = subscribe;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            SportifyClient client = SportifyClient.newInstance();
            return  client.setPlanSubscription(planId, client.getCurrentUserID(), subscribe);
        }
    }
}
