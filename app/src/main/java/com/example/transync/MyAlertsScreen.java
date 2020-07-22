package com.example.transync;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;

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

        // Find the ScrollView
        ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView2);

        // Create a LinearLayout element
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        // Add Buttons
        Button button = new Button(this);
        button.setText("Some text");
        linearLayout.addView(button);

        // Add the LinearLayout element to the ScrollView
        scrollView.addView(linearLayout);


    } /* onCreate() */
} /* MyAlertsScreen Class */