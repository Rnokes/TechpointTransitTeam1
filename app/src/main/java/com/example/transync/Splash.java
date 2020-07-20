package com.example.transync;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


/*
 *  Loading screen / Splash screen for app. Call whenever a
 *  process takes a significant amount of time such as the initial
 *  database connection. XML layout is variable splash.
 */

public class Splash extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        /*
         *  Initializer thread of the app, loads upon app startup,
         *  and sets a 3 second delay for general background processes
         *  to finish setting up. Upon completion, it loads the SignIn Screen.
         */

        Thread welcomeThread = new Thread() {
            @Override
            public void run() {
                try {
                    super.run();
                    sleep(3000);  //Delay of 3 seconds
                } catch (Exception e) {
                    /* Catches any exception that occurs during startup */
                    e.printStackTrace();
                } finally {
                    Intent i = new Intent(Splash.this, SignIn.class);
                    startActivity(i);
                    finish();
                }
            }
        };
        welcomeThread.start();
    }
}
