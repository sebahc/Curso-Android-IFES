package com.miempresa.myappui;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

public class SplashActivity extends Activity {

    private Handler handler;
    private Thread thread;
    private final int SPLASH_SCREEN_TIME_IN_MILLIS=3000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        handler = new Handler();
    }

    @Override
    protected void onResume() {
        super.onResume();
        thread = new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(SPLASH_SCREEN_TIME_IN_MILLIS);
                    handler.post(new Runnable() {
                        public void run() {
                            goToNextScreen();
                        }
                    });
                } catch (InterruptedException e) {
                }
            }
        };

        thread.start();
    }

    protected void goToNextScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


}
