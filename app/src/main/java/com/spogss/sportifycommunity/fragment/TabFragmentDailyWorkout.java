package com.spogss.sportifycommunity.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.spogss.sportifycommunity.R;
import com.spogss.sportifycommunity.adapter.ExpandableListAdapter;
import com.spogss.sportifycommunity.data.DailyWorkout;
import com.spogss.sportifycommunity.data.Exercise;
import com.spogss.sportifycommunity.data.SportifyClient;
import com.spogss.sportifycommunity.data.Workout;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Pauli on 14.05.2018.
 */

public class TabFragmentDailyWorkout extends Fragment {
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

        return view;
    }

    public void loadData() {
        new LoadWorkoutsTask().execute((Void) null);
    }

    private class LoadWorkoutsTask extends AsyncTask<Void, Void, Void> {
        private ArrayList<Workout> workouts;
        private HashMap<Workout, ArrayList<Exercise>> exercises;

        @Override
        protected Void doInBackground(Void... params) {
            client = SportifyClient.newInstance();

            workouts = new ArrayList<Workout>(client.getWorkouts(dailyWorkout.getId()));
            exercises = new HashMap<Workout, ArrayList<Exercise>>();

            for(Workout w : workouts)
                exercises.put(w, new ArrayList<Exercise>(client.getExercises(w.getId())));
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            expandableListAdapter = new ExpandableListAdapter(getActivity(), workouts, exercises);
            expandableListView.setAdapter(expandableListAdapter);
        }
    }
}
