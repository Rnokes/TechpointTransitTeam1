package com.example.transync;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

/*
 * Class that dynamically displays all currently listed routes in the database,
 * that are associated with a users selection of routes based on their userid.
 * Also allows for navigation to each route's alerts and the route map screen.
 */

public class MyRoutesScreen extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_routes_screen);

        /*
         *  The click listener for the menu button, sets that the
         *  button should return to main menu upon press.
         */
        ImageButton menu = findViewById(R.id.menubutton);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MyRoutesScreen.this, HomeScreen.class);
                startActivity(i);
            }
        }); /* setOnclickListener */

        /*
         *  The click listener for the pass button, sets that the
         *  button should go to the pass screen upon press.
         */
        Button myAlerts = findViewById(R.id.myAlerts);
        myAlerts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MyRoutesScreen.this, MyAlertsScreen.class);
                startActivity(i);
            }
        }); /* setOnclickListener */

        /*
         *  The click listener for the map button, sets that the
         *  button should go to the route map upon press.
         */
        Button mapButton = findViewById(R.id.routeMap);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MyRoutesScreen.this, MapScreen.class);
                startActivity(i);
            }
        }); /* setOnclickListener */


        /* TODO:
         *  Need a database call that pulls all routes that a user has
         *  selected as theirs, based on the userid. Then need to display
         *  them in the scroll view.
         */


    } /* onCreate() */
} /* MyRoutesScreen Class */