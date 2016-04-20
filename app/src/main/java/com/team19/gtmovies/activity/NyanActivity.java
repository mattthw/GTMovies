package com.team19.gtmovies.activity;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;

import com.team19.gtmovies.R;

/**
 * Created by Jim Jang on 2016-04-19.
 */
public class NyanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_nyan);
        ImageView imgView = (ImageView) findViewById(R.id.nyanView);
        imgView.setBackgroundResource(R.drawable.nyan);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int w = dm.widthPixels;
        int h = dm.heightPixels;
        int count = 0;
        final int nyanW = 301;
        final int nyanH = 119;


        getWindow().setLayout((int)(w * 0.8), (int) (w * 0.8 / nyanW * nyanH));

        AnimationDrawable frameAnimation = (AnimationDrawable) imgView.getBackground();

        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                return;
            }
        });

        frameAnimation.start();
    }

}
