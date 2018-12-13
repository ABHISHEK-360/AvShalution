package com.abhishek360.dev;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

public class SplashActivity extends Activity
{

    private static final long SPLASH_TIME_OUT = 3000;
    private ImageView logoView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        logoView= findViewById(R.id.logo_view_splash);
        AlphaAnimation blinkanimation= new AlphaAnimation(0.3f, 1); // Change alpha from fully visible to invisible
        blinkanimation.setDuration(1000); // duration - half a second
        blinkanimation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
        blinkanimation.setRepeatCount(3); // Repeat animation infinitely
        blinkanimation.setRepeatMode(Animation.REVERSE);
        logoView.startAnimation(blinkanimation);

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                Intent in = new Intent(getApplicationContext(),AndroidLauncher.class);
                startActivity(in);
                finish();


            }
        },SPLASH_TIME_OUT);
    }
}
