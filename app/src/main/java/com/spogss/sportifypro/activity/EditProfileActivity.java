package com.spogss.sportifypro.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.spogss.sportifypro.R;
import com.spogss.sportifypro.data.SportifyClient;
import com.spogss.sportifypro.data.pojo.User;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private Button button_saveChanges;
    private EditText editText_description;

    private boolean edited;

    private SportifyClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        setTitle("Edit Profile");

        client = SportifyClient.newInstance();

        initialize();

        User user = (User) getIntent().getSerializableExtra("profile");
        fillProfileData(user);
    }

    private void initialize(){
        edited = false;
        button_saveChanges = (Button) findViewById(R.id.button_saveProfileChanges);
        button_saveChanges.setOnClickListener(this);
        editText_description = (EditText) findViewById(R.id.editText_description);
    }

    private void fillProfileData(User u){
        editText_description.setText(u.getDescription());
    }

    private void saveChanges(){
        // TODO: 09.04.2018 implement saveChanges in EditProfile
        button_saveChanges.setVisibility(View.GONE);
        Toast.makeText(getApplicationContext(), "Saving Changes...", Toast.LENGTH_SHORT).show();
        String desc = editText_description.getText().toString();
        new SetDescriptionTask(desc).execute();
        edited = true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_saveProfileChanges:
                saveChanges();
                break;
        }
    }

    private class SetDescriptionTask extends AsyncTask<Void, Void, Void> {
        private String description;

        public SetDescriptionTask(String description){
            this.description = description;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            client.changeDescription(client.getCurrentUserID(), description);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(getApplicationContext(), "Changes Saved.", Toast.LENGTH_SHORT).show();
            setResult(Activity.RESULT_OK);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }
}
