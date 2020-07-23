package com.example.transync;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.example.transync.SignIn.stmt;
import static com.example.transync.SignIn.userid;
import static com.example.transync.MyAlertsScreen.currAlertInfo;

/*
 * The RouteScreen class displays all relevant information about a
 * route and gives the user the option to add the route to their favorites.
 */
public class RouteScreen extends Activity {
    String routeName = null;
    int routeID;
    int stopID;

    @SuppressLint("SetTextI18n")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.route_screen);

        /*
         *  The click listener for the menu button, sets that the
         *  button should return to main menu upon press.
         */
        ImageButton menu = findViewById(R.id.menubutton);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RouteScreen.this, HomeScreen.class);
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
                Intent i = new Intent(RouteScreen.this, PassScreen.class);
                startActivity(i);
            }
        }); /* setOnclickListener */

        /*
         *  The click listener for the map button, sets that the
         *  button should go to the route map upon press.
         */
        Button mapButton = findViewById(R.id.routeMap2);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RouteScreen.this, MapScreen.class);
                startActivity(i);
            }
        }); /* setOnclickListener */

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            routeName = extras.getString("selectedRoute");
        }
        else {
            Intent i = new Intent(RouteScreen.this, HomeScreen.class);
            startActivity(i);
        }

        TextView routeText = findViewById(R.id.routeName);
        routeText.setText(routeName);

        /* Handles the buttons that represent the add and remove from my favorite routes */
        final Button addRoute = findViewById(R.id.addMyRoutes);
        final Button removeRoute = findViewById(R.id.removeMyRoutes);
        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT busroutes.routename, busstops.stopname FROM busroutes, busstops, userfavorites, routes WHERE busroutes.routeid=routes.routeid AND busstops.stopid=userfavorites.stopid AND userfavorites.stopid=routes.stopid AND busstops.stopid=routes.stopid AND userid= '" + userid + "'");
            while (rs.next()) {
                if (routeName.equals(rs.getString(1))) {
                    addRoute.setVisibility(View.GONE);
                    removeRoute.setVisibility(View.VISIBLE);
                    break;
                }
                else {
                    removeRoute.setVisibility(View.GONE);
                    addRoute.setVisibility(View.VISIBLE);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        /* Gets the name and id of the current route. */
        try{
            ResultSet rs1 = stmt.executeQuery("SELECT routeID FROM busRoutes WHERE routename ='" + routeName + "'");
            rs1.next();
            routeID = rs1.getInt(1);
            ResultSet rs2 = stmt.executeQuery("SELECT stopID FROM routes WHERE routeID = '" + routeID + "'");
            rs2.next();
            stopID = rs2.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        /*
         *  The click listener for the add to my routes button. It flips
         *  the button to remove from my routes, and then add the route to the database.
         */
        addRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    stmt.executeUpdate("INSERT INTO userfavorites(userid, stopid)" + "VALUES( '" + userid + "','" + stopID + "') ");
                    addRoute.setVisibility(View.GONE);
                    removeRoute.setVisibility(View.VISIBLE);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } /* onCreate() */
        }); /* setOnclickListener */

        /*
         *  The click listener for the remove from my routes button. It flips
         *  the button to add to my routes, and then removes the route from the database.
         */
        removeRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    stmt.executeUpdate("DELETE FROM userfavorites WHERE userid = '" + userid + "' AND stopid = '" + stopID + "' ");
                    removeRoute.setVisibility(View.GONE);
                    addRoute.setVisibility(View.VISIBLE);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }); /* setOnclickListener */

        /* Gets the scrollview and puts set a linearlayout up. */
        ScrollView scrollViewToday = findViewById(R.id.scrollView2);
        LinearLayout linearLayoutToday = new LinearLayout(this);
        linearLayoutToday.setOrientation(LinearLayout.VERTICAL);

        /*
         * Following try catch gets issues related to the active route
         * and then setOnClickListeners to them to make them go to the alert details screen.
         */
        try {
            rs = stmt.executeQuery("Select busstops.stopname, busroutes.routename, problemdiscription, affectedroutes.problemid\n" +
                    "from busstops, busroutes, affectedroutes, routes, problems, userfavorites\n" +
                    "WHERE problems.problemid=affectedroutes.problemid\n" +
                    "    AND routes.routeid=busroutes.routeid\n" +
                    "    AND routes.stopid=busstops.stopid\n" +
                    "    AND routes.routeid=affectedroutes.routeid\n" +
                    "    AND routes.stopid=affectedroutes.stopid\n" +
                    "    AND affectedroutes.routeid=routes.routeid\n" +
                    "    AND affectedroutes.stopid=routes.stopid\n" +
                    "    AND affectedroutes.routeid=busroutes.routeid\n" +
                    "    AND affectedroutes.stopid=busstops.stopid\n" +
                    "    AND routes.routeid='"+ routeID +"'\n" +
                    "    AND (current_timestamp-affectedroutes.timestamp)<'1 day'");

            while(rs.next()) {
                Button button = new Button(this);
                button.setText(rs.getString(2) + " | " + rs.getString(3));
                button.setBackground(getResources().getDrawable(R.drawable.button_outline));
                final int probId = rs.getInt(4);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        currAlertInfo = probId;
                        Intent i = new Intent(RouteScreen.this, AlertDetailsScreen.class);
                        startActivity(i);
                    }
                }); /* setOnclickListener */
                linearLayoutToday.addView(button);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        scrollViewToday.addView(linearLayoutToday);
    } /* onCreate() */
} /* RouteScreen Class */