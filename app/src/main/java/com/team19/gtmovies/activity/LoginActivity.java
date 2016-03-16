package com.team19.gtmovies.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
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

import com.team19.gtmovies.R;
import com.team19.gtmovies.data.CurrentState;
import com.team19.gtmovies.data.IOActions;
import com.team19.gtmovies.exception.DuplicateUserException;
import com.team19.gtmovies.exception.IllegalUserException;
import com.team19.gtmovies.exception.NullUserException;
import com.team19.gtmovies.pojo.User;

import java.util.HashMap;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

//import com.team19.gtmovies.JinuTestActivity;

/**
 * A login screen that offers login via email/password.
 *
 * @author Matt McCoy
 * @version 2.0
 */
public class LoginActivity extends AppCompatActivity {

    //Keep track of the login task to ensure we can cancel it if requested.
    private UserLoginTask mAuthTask = null;
    // UI references.
    private TextView mEmailView;
    private EditText mPasswordView;
    private EditText mPassConfirmView;
    private EditText mNameView;
    private boolean register = false;
    private String error = "";
    //user credentials
    private String email = null; //username
    private String password = null;
    private String passwordCheck = null;
    private String name = null;
    private HashMap<String, Integer> attempts = new HashMap<>();
    //app user status
    public static final String USER_STATUS = "USER";
    //app users storage
    protected static Set<User> accounts;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    //private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (IOActions.userSignedIn()) {
            Log.d("IOActions", "not considered logged in");
            finish();
            return;
        }

        setContentView(R.layout.activity_login);
        //load login info
        startActivityForResult(new Intent(this, WelcomeActivity.class), 1);
        //load existing users
        try {
            accounts = IOActions.getAccounts();
        } catch (Exception e) {
            Log.e("GTMovies", "Exception: "+Log.getStackTraceString(e));
        }


        //remove up button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowHomeEnabled(false);
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
                onRegisterPressed();
            }
        });
        setResult(1, new Intent().putExtra("done", true));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null && !data.getBooleanExtra("login", true)) {
                onRegisterPressed();
        }
    }

    /**
     * Response to press on register button.
     */
    public void onRegisterPressed() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Register");
        }
        findViewById(R.id.register_button).setVisibility(View.GONE);
        findViewById(R.id.pwRegister).setVisibility(View.VISIBLE);
        findViewById(R.id.nameLayout).setVisibility(View.VISIBLE);
        findViewById(R.id.cancel_button).setVisibility(View.VISIBLE);
        ((Button) findViewById(R.id.email_sign_in_button)).setText("Create account");
        register = true;
        Log.println(Log.DEBUG, "GTMovies",
                "register/sign in toggle- register? '" + register + "'");
    }

    /**
     * XML/UI function to change layout from registering back to just
     * signing in
     * @param view current view
     */
    public void cancel(View view) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Login");
        }
        findViewById(R.id.register_button).setVisibility(View.VISIBLE);
        findViewById(R.id.pwRegister).setVisibility(View.GONE);
        findViewById(R.id.nameLayout).setVisibility(View.GONE);
        findViewById(R.id.cancel_button).setVisibility(View.GONE);
        ((Button) findViewById(R.id.email_sign_in_button)).setText("Sign in");
        register = false;
        Log.println(Log.DEBUG, "GTMovies",
                "register/sign in toggle- register? '" + register + "'");
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid username, missing fields, etc.), the
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
        email = mEmailView.getText().toString();
        password = mPasswordView.getText().toString();
        passwordCheck = mPassConfirmView.getText().toString();
        name = mNameView.getText().toString();
        boolean cancel = false;
        View focusView = null;

        //ALWAYS CHECK THESE FIELDS
        if (TextUtils.isEmpty(email)) {
            //username empty
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (TextUtils.isEmpty(password)) {
            //password empty
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        } else if (email.length() <= 3) {
            //username length
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        } else if (password.length() <= 3) {
            //password length
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        //REGISTERING?
        if (findViewById(R.id.pwRegister)
                .getVisibility() == (View.VISIBLE)) {
            if (!password.equals(passwordCheck)) {
                //password confirm
                mPassConfirmView.setError("Passwords do not match.");
                focusView = mPassConfirmView;
                cancel = true;
            } else if (TextUtils.isEmpty(name)) {
                //name entry
                mNameView.setError("Please enter your name.");
                focusView = mNameView;
                cancel = true;
            }
        }
        if (cancel) {
            //cancel and focus on bad field
            focusView.requestFocus();
        } else {
            //sign in or register
            mAuthTask = new UserLoginTask();
            mAuthTask.execute((Void) null);
            mPasswordView.setText("");
            mPassConfirmView.setText("");
        }
    }

    /**
     * void parents function. We
     * dont want them to push back!
     */
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

        //update header

//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        });
    }


    /**
     * INNER CLASS FOR USER LOGIN
     *
     * @author Matt McCoy
     * @version 2.0
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            error = "";
            if (register) { //register a new user
                try {
                    IOActions.addUser(new User(email, password, name));
                    boolean result = IOActions.loginUser(email, password);
                    Log.println(Log.DEBUG, "GTMovies",
                            "REGISTER: returning '" + result + "'");
                    return result;
                } catch (DuplicateUserException e) {
                    Log.e("GTMovies", e.getMessage());
                    error = "duplicate";
                    return false;
                } catch (IllegalUserException e) {
                    Log.e("GTMovies", e.getMessage());
                    return false;
                } catch (NullUserException e) {
                    Log.e("GTMovies", e.getMessage());
                    return false;
                }
            } else { //login like normal
                try {
                    boolean result = IOActions.loginUser(email, password);
                    Log.println(Log.DEBUG, "GTMovies",
                            "REGISTER: returning '" + result + "'");
                    return result;
                } catch (NullUserException e) {
                    Log.e("GTMovies", e.getMessage());
                    error = "nulluser";
                    return false;
                }
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            if (success) {
                Snackbar.make(findViewById(R.id.login_root),
                        "'" + CurrentState.getUser().getUsername()
                                + "' signed in." , Snackbar.LENGTH_LONG).show();
                // We are done. Go back to MainActivity, after a set delay.
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        NavUtils.navigateUpFromSameTask(LoginActivity.this);
                    }
                };
                Timer t = new Timer();
                t.schedule(task, 1000);
                finish();
            } else {
                Snackbar.make(MainActivity.rootView,
                        "Invalid password or username!" , Snackbar.LENGTH_LONG).show();
                Log.i("GTMovies", error);
                if (error.equals("duplicate")) {
                    mEmailView.setError("User already exists");
                    mEmailView.requestFocus();
                } else if (error.equals("nulluser")) {
                    mEmailView.setError("User not found");
                    mEmailView.requestFocus();
                } else {
                    mPasswordView.setError(getString(R.string.error_incorrect_password));
                    mPasswordView.requestFocus();
                }
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }
}

