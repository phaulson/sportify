package com.spogss.sportifycommunity.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.spogss.sportifycommunity.R;
import com.spogss.sportifycommunity.data.Exercise;

public class ExerciseActivity extends AppCompatActivity {
    private Exercise exercise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_);

        exercise = (Exercise) getIntent().getSerializableExtra("exercise");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(exercise.getName());

        TextView exerciseName = (TextView)findViewById(R.id.textView_exercise_name);
        exerciseName.setText(exercise.getName());

        TextView exerciseDescr = (TextView)findViewById(R.id.textView_exercise_description);
        exerciseDescr.setText(exercise.getDescription());


        //TODO: implement with real pics
        ImageView exercisePic1 = (ImageView) findViewById(R.id.imageView_exercise_1);
        exercisePic1.setImageResource(R.drawable.sp_exercise_test1);

        ImageView exercisePic2 = (ImageView) findViewById(R.id.imageView_exercise_2);
        exercisePic2.setImageResource(R.drawable.sp_exercise_test2);
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
}
