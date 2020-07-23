package com.example.transync;

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

public class RouteScreen extends Activity {
    String routeName = null;
    int routeID;
    int stopID;
    public static int currAlertInfo;
    int prevProbID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.route_screen);

        ImageButton menu = findViewById(R.id.menubutton);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RouteScreen.this, HomeScreen.class);
                startActivity(i);
            }
        });

        ImageButton passButton = findViewById(R.id.passButton);
        passButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RouteScreen.this, PassScreen.class);
                startActivity(i);
            }
        });

        Button mapButton = findViewById(R.id.routeMap2);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RouteScreen.this, MapScreen.class);
                startActivity(i);
            }
        });

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            routeName = extras.getString("selectedRoute");

        } else {
            Intent i = new Intent(RouteScreen.this, HomeScreen.class);
            startActivity(i);
        }

        TextView routeText = findViewById(R.id.routeName);
        routeText.setText(routeName);

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
                else{
                    removeRoute.setVisibility(View.GONE);
                    addRoute.setVisibility(View.VISIBLE);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

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
            }
        });


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
        });
        ScrollView scrollViewIssues = (ScrollView) findViewById(R.id.scrollView2);

        // Create a LinearLayout element
        LinearLayout linearLayoutIssues = new LinearLayout(this);
        linearLayoutIssues.setOrientation(LinearLayout.VERTICAL);

        try {
            ResultSet rs3 = stmt.executeQuery("Select busstops.stopname, busroutes.routename, problemdiscription, affectedroutes.problemid\n" +
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
                    "    AND routes.routeid = '"+routeID+"'\n" +
                    "    AND (current_timestamp-affectedroutes.timestamp)<'1 day'");

            while(rs3.next()) {
                Button button = new Button(this);
                button.setText(rs3.getString(2) + " | " + rs3.getString(3));
                button.setBackground(getResources().getDrawable(R.drawable.button_outline));
                final int probId = rs3.getInt(4);
                if (probId != prevProbID) {
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            currAlertInfo = probId;
                            Intent intent = new Intent(RouteScreen.this, AlertDetailsScreen.class);
                            intent.putExtra("selectedAlert", currAlertInfo);
                            startActivity(intent);
                        }
                    }); /* setOnclickListener */

                    linearLayoutIssues.addView(button);
                }
                prevProbID = probId;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Add the LinearLayout element to the ScrollView
        scrollViewIssues.addView(linearLayoutIssues);
    }
}