package com.example.lexar.projecthbo_ictbv.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.lexar.projecthbo_ictbv.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * The first screen that shows up when the app is loading up.
 */
public class SplashActivity extends AppCompatActivity {

    /**
     * Intent and thread so we can go to the main activity in x milliseconds.
     */
    private Intent intent;
    private Thread timerThread;

    @BindView(R.id.iv_ibiLogo)
    ImageView ivIbiLogo;

    @BindView(R.id.iv_ibiLandscape)
    ImageView ivIbiLandscape;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        intent = new Intent(this, MainActivity.class);
        startAnimation();
        startTimer();
        timerThread.start();
    }

    /**
     * Initialize animation instance and load in our own transition.
     */
    private void startAnimation() {
        Animation myAnimation = AnimationUtils.loadAnimation(this, R.anim.transition);
        ivIbiLogo.startAnimation(myAnimation);
        ivIbiLandscape.startAnimation(myAnimation);
    }

    /**
     * Start a timer that waits x milliseconds after the animation.
     */
    private void startTimer() {
        timerThread = new Thread() {
            public void run() {
                try {
                    sleep(1750);
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                } finally {
                    startActivity(intent);
                    finish();
                }
            }
        };
    }
}
