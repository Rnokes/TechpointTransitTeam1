package com.example.transync;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;


/*
 * Class that dynamically displays all currently listed routes in the database.
 * Also allows for navigation to see more information about routes and view the route map.
 */

public class AllRoutesScreen extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_routes_screen);

        /*
         *  The click listener for the menu button, sets that the
         *  button should return to main menu upon press.
         */
        ImageButton menu = findViewById(R.id.menubutton);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AllRoutesScreen.this, HomeScreen.class);
                startActivity(i);
            }
        }); /* setOnclickListener */

        /*
         *  The click listener for the pass button, sets that the
         *  button should go to the pass screen upon press.
         */
        ImageButton passButton = findViewById(R.id.passButton);
        passButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AllRoutesScreen.this, PassScreen.class);
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
                Intent i = new Intent(AllRoutesScreen.this, MapScreen.class);
                startActivity(i);
            }
        }); /* setOnclickListener */

        /*
        * TODO:
        *  Need to make a database call that gets all current routes listed in the database,
        *  and then add each one as a button in the list view on this screen.
        */

    } /* onCreate() */
} /* AllRoutesScreen Class */