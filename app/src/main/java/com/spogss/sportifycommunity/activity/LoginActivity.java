package com.spogss.sportifycommunity.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;

import com.spogss.sportifycommunity.R;
import com.spogss.sportifycommunity.data.SportifyClient;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements OnClickListener {
    private SportifyClient client;
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private AsyncTask<Void, Void, Integer> authTask = null;

    // UI references.
    private ConstraintLayout constraintLayout_initializeOverlay;

    private EditText editText_username;
    private EditText editText_password;
    private Button button_signIn;
    private View view_progress;
    private View view_loginForm;

    private EditText editText_username_r;
    private EditText editText_password_r;
    private EditText editText_password_r_2;

    private Button button_register;

    private TabHost tabHost_host;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //client = SportifyClient.newInstance();

        //initialize();

        //new ConnectTask().execute();

        initialize();
        new ConnectTask().execute();
    }

    private void initialize(){
        setTitle(R.string.app_name); // Sportify pro

        constraintLayout_initializeOverlay = (ConstraintLayout) findViewById(R.id.constraintLayout_initializeOverlay);

        editText_username = (EditText) findViewById(R.id.editText_username);
        editText_password = (EditText) findViewById(R.id.editText_password);

        button_signIn = (Button) findViewById(R.id.button_signIn);
        button_signIn.setOnClickListener(this);

        view_loginForm = findViewById(R.id.scrollView_loginForm);
        view_progress = findViewById(R.id.progressBar_login);

        editText_username_r = (EditText) findViewById(R.id.editText_username_r);
        editText_password_r = (EditText) findViewById(R.id.editText_password_r);
        editText_password_r_2 = (EditText) findViewById(R.id.editText_retype_password_r);

        button_register = (Button) findViewById(R.id.button_register);
        button_register.setOnClickListener(this);

        tabHost_host = (TabHost) findViewById(R.id.tabHost_host);
        tabHost_host.setup();

        // initialize tabs
        TabHost.TabSpec spec = tabHost_host.newTabSpec(getString(R.string.action_sign_in));
        spec.setContent(R.id.tab_login);
        spec.setIndicator(getString(R.string.action_sign_in));
        tabHost_host.addTab(spec);

        //Tab 2
        spec = tabHost_host.newTabSpec(getString(R.string.action_register));
        spec.setContent(R.id.tab_register);
        spec.setIndicator(getString(R.string.action_register));
        tabHost_host.addTab(spec);
    }

    /**
     * attempts login with saved credentials
     */
    private boolean attemptLoginWithSavedCredentials() {
        SharedPreferences sp1 = this.getSharedPreferences("Login", MODE_PRIVATE);
        String username = sp1.getString("username", null);
        String password = sp1.getString("password", null);

        if(username != null && password != null) {
            attemptLogin(username, password);
            return true;
        }
        return false;
    }

    /**
     * saves the login credentials locally
     *
     * @param username that should be saved
     * @param password that should be saved
     */
    private void saveLoginCredentials(String username, String password) {
        SharedPreferences sp = getSharedPreferences("Login", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("username", username);
        editor.putString("password", password);
        editor.commit();
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin(String username, String password) {
        if (authTask != null) {
            return;
        }

        // Reset errors.
        editText_username.setError(null);
        editText_password.setError(null);

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            editText_password.setError(getString(R.string.error_invalid_password));
            focusView = editText_password;
            cancel = true;
        }

        // Check for a valid email username.
        if (TextUtils.isEmpty(username)) {
            editText_username.setError(getString(R.string.error_field_required));
            focusView = editText_username;
            cancel = true;
        } else if (!isUsernameValid(username)) {
            editText_username.setError(getString(R.string.error_invalid_username));
            focusView = editText_username;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            authTask = new UserLoginTask(username, password);
            authTask.execute();
        }
    }

    private void attemptRegistration() {
        if (authTask != null) {
            return;
        }

        // Reset errors.
        editText_username_r.setError(null);
        editText_password_r.setError(null);
        editText_password_r_2.setError(null);

        // Store values at the time of the login attempt.
        String username = editText_username_r.getText().toString();
        String password = editText_password_r.getText().toString();
        String pw_conf = editText_password_r_2.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if(!password.equals(pw_conf)){
            editText_password_r_2.setError(getString(R.string.error_password_match));
            focusView = editText_password_r_2;
            cancel = true;
        }

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            editText_password_r.setError(getString(R.string.error_invalid_password));
            focusView = editText_password_r;
            cancel = true;
        }

        // Check for a valid username.
        if (TextUtils.isEmpty(username)) {
            editText_username_r.setError(getString(R.string.error_field_required));
            focusView = editText_username_r;
            cancel = true;
        } else if (!isUsernameValid(username)) {
            editText_username_r.setError(getString(R.string.error_invalid_username));
            focusView = editText_username_r;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            authTask = new UserRegisterTask(username, password);
            authTask.execute((Void) null);
        }
    }

    private boolean isUsernameValid(String username) {
        return username.length() >= 4 && !username.contains(" ");
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 6;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            tabHost_host.setVisibility(show ? View.GONE : View.VISIBLE);
            tabHost_host.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    tabHost_host.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            view_progress.setVisibility(show ? View.VISIBLE : View.GONE);
            view_progress.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    view_progress.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            view_progress.setVisibility(show ? View.VISIBLE : View.GONE);
            tabHost_host.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_signIn:
                // Store values at the time of the login attempt.
                String username = editText_username.getText().toString();
                String password = editText_password.getText().toString();
                attemptLogin(username, password);
                break;
            case R.id.button_register:
                attemptRegistration();
                break;
        }
    }

    private void onLoginSuccess(String username, String password) {
        saveLoginCredentials(username, password);
        launchFeed();
    }

    private void onRegistrationSuccess(String username, String password) {
        saveLoginCredentials(username, password);
        launchFeed();
    }

    private void launchFeed() {
        Intent intent = new Intent(this, FeedActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    private class UserLoginTask extends AsyncTask<Void, Void, Integer> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Integer doInBackground(Void... params) {
            int uid = client.login(mEmail, mPassword);
            Log.i("uid", "uid: " + uid);
            return uid;
        }

        @Override
        protected void onPostExecute(final Integer loggedInUser) {
            authTask = null;
            showProgress(false);
            constraintLayout_initializeOverlay.setVisibility(View.GONE);

            if (loggedInUser >= 0) {
                onLoginSuccess(mEmail, mPassword);
            } else {
                editText_password.setError(getString(R.string.error_incorrect_username_or_password));
                editText_username.setError(getString(R.string.error_incorrect_username_or_password));
                editText_password.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            authTask = null;
            showProgress(false);
        }


    }

    private class ConnectTask extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            client = SportifyClient.newInstance();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(!attemptLoginWithSavedCredentials())
                constraintLayout_initializeOverlay.setVisibility(View.GONE);
        }
    }

    private class UserRegisterTask extends AsyncTask<Void, Void, Integer> {

        private final String mEmail;
        private final String mPassword;

        UserRegisterTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Integer doInBackground(Void... params) {
            if (isUsernameValid(mEmail) && isPasswordValid(mPassword)) {
                int u = client.register(mEmail, mPassword, true);
                return u;
            }

            return null;
        }

        @Override
        protected void onPostExecute(final Integer success) {
            authTask = null;
            showProgress(false);

            if (success >= 0) {
                onRegistrationSuccess(mEmail, mPassword);
            } else {
                editText_password.setError(getString(R.string.error_incorrect_username_or_password));
                editText_username.setError(getString(R.string.error_incorrect_username_or_password));
                editText_password.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            authTask = null;
            showProgress(false);
        }
    }
}

