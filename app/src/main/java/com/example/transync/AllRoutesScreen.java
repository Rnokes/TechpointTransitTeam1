package com.example.transync;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

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
        *  The following gets all routes that are listed in the database and then
        *  displays them in a linearLayout on the screen, and sets onClick listeners
        *  for each route in the database that lead to the Route Info Panel.
        */
        LinearLayout linearLayout = findViewById(R.id.routeLayout1);
        final String route_names[] = getResources().getStringArray(R.array.route_names);
        for(int i = 0; i<route_names.length; i++){
            Button button = new Button(this);
            button.setText(route_names[i]);
            button.setPadding(20,20,20,20);
            button.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            button.setTextSize(18);

            linearLayout.addView(button);

            final int finalI = i;
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(AllRoutesScreen.this, RouteScreen.class);
                    intent.putExtra("selectedRoute",route_names[finalI]);
                    startActivity(intent);
                }
            }); /* setOnclickListener */

        }

    } /* onCreate() */
} /* AllRoutesScreen Class */