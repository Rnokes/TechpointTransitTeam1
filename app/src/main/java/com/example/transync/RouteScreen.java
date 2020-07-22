package com.example.transync;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.example.transync.SignIn.stmt;
import static com.example.transync.SignIn.userid;

public class RouteScreen extends Activity {
    String routeName = null;
    int routeID;
    int stopID;

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

    }
}