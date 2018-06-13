package com.spogss.sportifycommunity.data.connection.asynctasks;

import com.spogss.sportifycommunity.data.Exercise;
import com.spogss.sportifycommunity.data.Workout;
import com.spogss.sportifycommunity.data.connection.QueryType;
import com.spogss.sportifycommunity.data.connection.SportifyClient;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Pauli on 10.06.2018.
 */

public class LoadWorkoutsTask extends ClientTask<Void, Void, Void> {
    private int dailyWorkoutId;
    private SportifyClient client;
    private ArrayList<Workout> workouts;
    private HashMap<Workout, ArrayList<Exercise>> exercises;

    public LoadWorkoutsTask(int dailyWorkoutId) {
        this.dailyWorkoutId = dailyWorkoutId;
        client = SportifyClient.newInstance();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        workouts = new ArrayList<Workout>(client.getWorkouts(dailyWorkoutId));
        exercises = new HashMap<Workout, ArrayList<Exercise>>();

        for(Workout w : workouts)
            exercises.put(w, new ArrayList<Exercise>(client.getExercises(w.getId())));
        return null;
    }

    @Override
    protected void onPostExecute(Void v) {
        getListener().onSuccess(QueryType.LOAD_WORKOUTS, workouts, exercises);
    }

    @Override
    protected void onCancelled() {
        getListener().onSuccess(QueryType.LOAD_WORKOUTS);
    }
}
