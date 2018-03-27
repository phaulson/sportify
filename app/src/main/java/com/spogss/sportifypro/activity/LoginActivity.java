package com.spogss.sportifypro.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.spogss.sportifypro.R;
import com.spogss.sportifypro.data.User;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements OnClickListener{
    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final ArrayList<String> DUMMY_CREDENTIALS = new ArrayList<String>(Arrays.asList("admin:nimda1", "pauli:paulim15"));

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private AsyncTask<Void, Void, Boolean> authTask = null;

    // UI references.
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

        initialize();
    }

    private void initialize(){
        setTitle(R.string.app_name); // Sportify pro

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
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (authTask != null) {
            return;
        }

        // Reset errors.
        editText_username.setError(null);
        editText_password.setError(null);

        // Store values at the time of the login attempt.
        String username = editText_username.getText().toString();
        String password = editText_password.getText().toString();

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
            authTask.execute((Void) null);
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
                attemptLogin();
                break;
            case R.id.button_register:
                attemptRegistration();
                break;
        }
    }

    private void onLoginSuccess(){
        // TODO: 15/03/2018 start next activity
        finish();
    }

    private void onRegistrationSuccess(){
        Intent intent_viewProfile = new Intent(getApplicationContext(), ProfileActivity.class);
        User user = new User(editText_username_r.getText().toString(), editText_password_r.getText().toString());
        intent_viewProfile.putExtra("profile", user);
        startActivity(intent_viewProfile);
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    private class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: actual login process

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }

            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            authTask = null;
            showProgress(false);

            if (success) {
                onLoginSuccess();
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

    private class UserRegisterTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserRegisterTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: actual register process

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            if (isUsernameValid(mEmail) && isPasswordValid(mPassword)) {
                DUMMY_CREDENTIALS.add(mEmail + ":" + mPassword);
                return true;
            }

            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            authTask = null;
            showProgress(false);

            if (success) {
                onRegistrationSuccess();
            } else {
                //todo change
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

