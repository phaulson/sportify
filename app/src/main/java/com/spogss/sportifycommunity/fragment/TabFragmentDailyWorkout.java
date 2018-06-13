package com.spogss.sportifycommunity.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.spogss.sportifycommunity.R;
import com.spogss.sportifycommunity.activity.ExerciseActivity;
import com.spogss.sportifycommunity.activity.PlanActivity;
import com.spogss.sportifycommunity.adapter.ExpandableListAdapter;
import com.spogss.sportifycommunity.data.DailyWorkout;
import com.spogss.sportifycommunity.data.Exercise;
import com.spogss.sportifycommunity.data.connection.SportifyClient;
import com.spogss.sportifycommunity.data.Workout;
import com.spogss.sportifycommunity.data.connection.asynctasks.ClientQueryListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Pauli on 14.05.2018.
 */

public class TabFragmentDailyWorkout extends Fragment implements ExpandableListView.OnChildClickListener, ClientQueryListener {
    private ExpandableListView expandableListView;

    private SportifyClient client;
    private DailyWorkout dailyWorkout;
    private ExpandableListAdapter expandableListAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_fragment_workout, container, false);
        expandableListView = (ExpandableListView) view.findViewById(R.id.expandableListView_workouts);

        dailyWorkout = (DailyWorkout) getArguments().getSerializable("dailyworkout");

        expandableListView.setOnChildClickListener(this);

        client = SportifyClient.newInstance();
        return view;
    }

    public void loadData() {
        client.getWorkoutsAsync(dailyWorkout.getId(), this);
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        Exercise exercise = (Exercise) expandableListAdapter.getChild(groupPosition, childPosition);

        Intent intent = new Intent(getContext(), ExerciseActivity.class);
        intent.putExtra("exercise", exercise);
        getContext().startActivity(intent);

        return false;
    }

    @Override
    public void onSuccess(Object... results) {
        expandableListAdapter = new ExpandableListAdapter(getActivity(),
                (ArrayList<Workout>)results[1], (HashMap<Workout, ArrayList<Exercise>>)results[2]);
        expandableListView.setAdapter(expandableListAdapter);
    }

    @Override
    public void onFail(Object... errors) {
        Toast.makeText(getContext(), "Error while loading workouts", Toast.LENGTH_SHORT).show();
    }
}
