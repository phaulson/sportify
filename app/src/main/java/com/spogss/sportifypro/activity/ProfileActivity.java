package com.spogss.sportifypro.activity;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.spogss.sportifypro.R;
import com.spogss.sportifypro.data.User;

public class ProfileActivity extends AppCompatActivity {
    private CollapsingToolbarLayout collapsingToolbar;
    private TextView textView_description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initialize();

        User user = (User) getIntent().getSerializableExtra("profile");
        setProfile(user);

    }

    private void initialize(){
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbar);
        textView_description = (TextView) findViewById(R.id.textView_description);
    }

    private void setProfile(User u){
        collapsingToolbar.setTitle(u.getUsername());
        String bio = u.getBiography();
        bio = (bio == null || bio.isEmpty()) ? getString(R.string.not_specified) : bio;
        Log.i("bio:" , bio);
        textView_description.setText(bio);
    }
}
