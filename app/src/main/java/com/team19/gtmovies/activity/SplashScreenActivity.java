package com.team19.gtmovies.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.team19.gtmovies.R;
import com.team19.gtmovies.abstractClasses.MovieControlActivity;
import com.team19.gtmovies.controller.MovieControllerTask;
import com.team19.gtmovies.data.IOActions;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashScreenActivity extends MovieControlActivity {

    private static boolean splashScreenVisited = false;
    public static final String SPLASH_SCREEN_VISITED = "Screen Visited";
    private View rootView;

    private boolean active = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.w("GTMovies", "SPLASH SCREEN ONCREATE!!");
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        setContentView(R.layout.activity_splash_screen);
        rootView = findViewById(R.id.splash_view);
        Typeface myTypeface = Typeface.createFromAsset(getAssets(), "fonts/Lobster-Regular.ttf");
        TextView myTextView = (TextView) findViewById(R.id.splash_view);
        if (myTypeface != null) {
            myTextView.setTypeface(myTypeface);
        }
        splashScreenVisited = true;
        try {
            //MainActivity.setIOA(new IOActions(this));
//            IOActions ioa = ((IOActions) this.getApplication());
            new IOActions(this);
        } catch (Exception e) {
            Log.e("GTMovies", e.getMessage());
        }
        new MovieControllerTask().execute(this, MovieControllerTask.GET_ALL_MOVIES,
                null, null, null);

    }

    @Override
    public void finishedGettingMovies(int RequestType) {
        Log.d("MovieController", "called finish");
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(SPLASH_SCREEN_VISITED, true);
        //NEED THIS setFlags TO CLEAR OLD LOGINACTIVITY INTENTS
        // WHICH MAY PREVENT NEW LOGINACTIVITY
        //FROM BEING STARTED -Matt
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
