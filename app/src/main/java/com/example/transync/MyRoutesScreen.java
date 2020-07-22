package com.example.transync;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.RequiresApi;

import java.sql.ResultSet;
import java.sql.SQLException;

import static android.icu.text.MessagePattern.ArgType.NONE;
import static android.icu.text.MessagePattern.ArgType.SELECT;
import static com.example.transync.SignIn.stmt;
import static com.example.transync.SignIn.userid;

/*
 * Class that dynamically displays all currently listed routes in the database,
 * that are associated with a users selection of routes based on their userid.
 * Also allows for navigation to each route's alerts and the route map screen.
 */

public class MyRoutesScreen extends Activity {

    @RequiresApi(api = Build.VERSION_CODES.N)
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
         * SELECT busroutes.routename, busstops.stopname
           FROM busroutes, busstops, userfavorites, routes
           WHERE busroutes.routeid=routes.routeid AND busstops.stopid=userfavorites.stopid AND userfavorites.stopid=routes.stopid
           AND busstops.stopid=routes.stopid AND userid= "+userid"
         */
        LinearLayout linearLayout = findViewById(R.id.routeLayout);
        final String[] route_names = getResources().getStringArray(R.array.route_names);
        for (int i = 0; i < route_names.length; i++) {
            Button button = new Button(this);
            button.setText(route_names[i]);
            button.setVisibility(View.GONE);
            button.setPadding(20,20,20,20);
            button.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            button.setTextSize(18);
            linearLayout.addView(button);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(MyRoutesScreen.this, RouteScreen.class);
                    startActivity(i);
                }

            }); /* setOnclickListener */
            ResultSet rs;
            try {
                rs = stmt.executeQuery("SELECT busroutes.routename, busstops.stopname FROM busroutes, busstops, userfavorites, routes WHERE busroutes.routeid=routes.routeid AND busstops.stopid=userfavorites.stopid AND userfavorites.stopid=routes.stopid AND busstops.stopid=routes.stopid AND userid= '" + userid + "'");
                int j = 0;
                while (rs.next()) {
                    j++;
                    if (route_names[i].equals(rs.getString(j))) {
                        button.setVisibility(View.VISIBLE);
                        final int finalI = i;
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(MyRoutesScreen.this, RouteScreen.class);
                                intent.putExtra("selectedRoute", route_names[finalI]);
                                startActivity(intent);
                            }
                        });
                        break;
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


    } /* onCreate() */
} /* MyRoutesScreen Class */