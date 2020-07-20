package com.example.transync;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

/*
 * Class that dynamically displays all currently listed alerts in the database,
 * that are associated with a users my routes and their userid. Displays the
 * alerts pertinent to today and the day before.
 */

public class MyAlertsScreen extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_alerts_screen);

        /*
         *  The click listener for the menu button, sets that the
         *  button should return to main menu upon press.
         */
        ImageButton menu = findViewById(R.id.menubutton);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MyAlertsScreen.this, HomeScreen.class);
                startActivity(i);
            }
        }); /* setOnclickListener */

        /*
         *  The click listener for the pass button, sets that the
         *  button should go to the pass screen upon press.
         */
        ImageButton passButton = findViewById(R.id.passButton6);
        passButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MyAlertsScreen.this, PassScreen.class);
                startActivity(i);
            }
        }); /* setOnclickListener */


        /*
         * TODO:
         *  Need a database calls that gets all of a user's route alerts,
         *  based on their routes and their userid. Then need to add each
         *  to the scroll view. We need the name of the route that has the
         *  issue, the type of issue, and possibly the description of the issue.
         */

    } /* onCreate() */
} /* MyAlertsScreen Class */