package com.team19.gtmovies;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    //Keep track of the login task to ensure we can cancel it if requested.
    private UserLoginTask mAuthTask = null;
    // UI references.
    private TextView mEmailView;


    private EditText mPasswordView;
    private EditText mPassConfirmView;
    private EditText mNameView;
    //private View mProgressView;
    //private View mLoginFormView;

    //is user logged in
    public static final String USER_STATUS = "USER";

    //USER DB
    protected static HashSet<User> accounts;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    //private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        //load login info
        //SharedPreferences pref = getSharedPreferences(USER_STATUS, 0);
        //String USER = pref.getString("USER", "null");
        startActivity(new Intent(this, WelcomeActivity.class));
        //load existing users
        try {
            accounts = IOActions.getAccounts();
        } catch (Exception e) {
            Log.e("GTMovies", "Exception: "+Log.getStackTraceString(e));
        }

        // Set up the login form.
        mEmailView = (TextView) findViewById(R.id.email);
        mNameView = (EditText) findViewById(R.id.name);
        mPassConfirmView = (EditText) findViewById(R.id.passwordConfirm);
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        //buttons
        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                    attemptLogin();
            }
        });
        Button mRegisterButton = (Button) findViewById(R.id.register_button);
        mRegisterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.register_button).setVisibility(View.GONE);
                findViewById(R.id.pwRegister).setVisibility(View.VISIBLE);
                findViewById(R.id.nameLayout).setVisibility(View.VISIBLE);
                findViewById(R.id.cancel_button).setVisibility(View.VISIBLE);
                ((Button) findViewById(R.id.email_sign_in_button)).setText("Create account");
            }
        });
        //mLoginFormView = findViewById(R.id.login_form);
        //mProgressView = findViewById(R.id.login_progress);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        // client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void cancel(View view) {
        findViewById(R.id.register_button).setVisibility(View.VISIBLE);
        findViewById(R.id.pwRegister).setVisibility(View.GONE);
        findViewById(R.id.nameLayout).setVisibility(View.GONE);
        findViewById(R.id.cancel_button).setVisibility(View.GONE);
        ((Button) findViewById(R.id.email_sign_in_button)).setText("Sign in");

    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);
        mPassConfirmView.setError(null);
        mNameView.setError(null);
        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String pwConfirm = mPassConfirmView.getText().toString();
        String name = mNameView.getText().toString();
        boolean cancel = false;
        View focusView = null;
        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }
        // Check for a valid username
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isUserValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }
        //ensure registration info is valid
        if (findViewById(R.id.pwRegister).getVisibility() == (View.VISIBLE)) {
            if (!password.equals(pwConfirm)) {
                mPassConfirmView.setError("Passwords do not match.");
                focusView = mPassConfirmView;
                cancel = true;
            } else if (TextUtils.isEmpty(name)) {
                mNameView.setError("Please enter your name.");
                focusView = mNameView;
                cancel = true;
            }
        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else if (name.length() > 0) {
            mAuthTask = new UserLoginTask(email, password, name);
            mAuthTask.execute((Void) null);
            mPasswordView.setText("");
            mPassConfirmView.setText("");
            mNameView.setText("");
        } else {
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
            mPasswordView.setText("");
            mPassConfirmView.setText("");
        }
    }

    private boolean isUserValid(String email) {
        return email.length() > 3;
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 3;
    }


    @Override
    public void onBackPressed() {
    }
    @Override
    public void onStart() {
        super.onStart();
//        // ATTENTION: This was auto-generated to implement the App Indexing API.
//        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        client.connect();
//        Action viewAction = Action.newAction(
//                Action.TYPE_VIEW, // TODO: choose an action type.
//                "Login Page", // TODO: Define a title for the content shown.
//                // TODO: If you have web page content that matches this app activity's content,
//                // make sure this auto-generated web page URL is correct.
//                // Otherwise, set the URL to null.
//                Uri.parse("http://host/path"),
//                // TODO: Make sure this auto-generated app deep link URI is correct.
//                Uri.parse("android-app://com.team19.gtmovies/http/host/path")
//        );
//        AppIndex.AppIndexApi.start(client, viewAction);
    }
    @Override
    public void onStop() {
        super.onStop();
//        // ATTENTION: This was auto-generated to implement the App Indexing API.
//        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        Action viewAction = Action.newAction(
//                Action.TYPE_VIEW, // TODO: choose an action type.
//                "Login Page", // TODO: Define a title for the content shown.
//                // TODO: If you have web page content that matches this app activity's content,
//                // make sure this auto-generated web page URL is correct.
//                // Otherwise, set the URL to null.
//                Uri.parse("http://host/path"),
//                // TODO: Make sure this auto-generated app deep link URI is correct.
//                Uri.parse("android-app://com.team19.gtmovies/http/host/path")
//        );
//        AppIndex.AppIndexApi.end(client, viewAction);
//        client.disconnect();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
//        SharedPreferences state = getSharedPreferences(USER_STATUS, 0);
//        SharedPreferences.Editor editor = state.edit();
//        editor.putString("USER", "");
//        editor.putString("username", IOActions.currentUser.getUsername());
//        editor.commit();

        Intent returnIntent = new Intent();
        //returnIntent.putExtra("user", IOActions.currentUser);
        setResult(Activity.RESULT_OK, returnIntent);

    }


    /**
     * INNER CLASS FOR USER LOGIN
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;
        private String mName = "";

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }
        //use when registering
        UserLoginTask(String email, String password, String name) {
            mEmail = email;
            mPassword = password;
            mName = name;
        }
        @Override
        protected Boolean doInBackground(Void... params) {
            if (mName.length() == 0) {
                //log in
                try {
                    IOActions.getUserByUsername(mEmail);
                    IOActions.loginUser(mEmail, mPassword);
                    //if (IOActions.currentUser != null) {
                    //    MainActivity.currentUser = ioa.currentUser;
                    //    return true;
                    //}
                    return true;
                } catch (NullUserException e) {
                    Log.e("GTMovies", e.getMessage());
                }
            } else {
                //register
                try {
                    IOActions.addUser(new User(mEmail,mPassword, mName));
                    IOActions.commit();
                    //MainActivity.currentUser = ioa.currentUser;
                    return true;
                } catch (NullUserException e) {
                    Log.e("GTMovies", e.getMessage());
                } catch (DuplicateUserException d) {
                    Log.e("GTMovies", d.getMessage());
                }
            }
            return false;
        }
        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            if (success) {
//                SharedPreferences state = getSharedPreferences(APP_PREF, 0);
//                SharedPreferences.Editor editor = state.edit();
//                editor.putBoolean("verifiedMode", true);
//                editor.putString("username", currentUser.getUsername());
//                editor.commit();
//
//                Intent returnIntent = new Intent();
//                returnIntent.putExtra("user", (Serializable)ioa.currentUser);
//                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }
        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }
}

