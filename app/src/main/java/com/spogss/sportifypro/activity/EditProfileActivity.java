package com.spogss.sportifypro.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.spogss.sportifypro.R;
import com.spogss.sportifypro.data.User;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private Button button_saveChanges;
    private EditText editText_description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        initialize();

        User user = (User) getIntent().getSerializableExtra("profile");
        fillProfileData(user);
    }

    private void initialize(){
        button_saveChanges = (Button) findViewById(R.id.button_saveProfileChanges);
        button_saveChanges.setOnClickListener(this);
        editText_description = (EditText) findViewById(R.id.editText_description);
    }

    private void fillProfileData(User u){
        editText_description.setText(u.getBiography());
    }

    private void saveChanges(){
        // TODO: 09.04.2018 implement saveChanges in EditProfile
        Toast.makeText(getApplicationContext(), "Saving Changes...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_saveProfileChanges:
                saveChanges();
                break;
        }
    }
}
