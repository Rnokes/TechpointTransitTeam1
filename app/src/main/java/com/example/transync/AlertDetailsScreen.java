package com.example.transync;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.example.transync.MyAlertsScreen.currAlertInfo;
import static com.example.transync.SignIn.stmt;

/*
 * Class that displays the details of the alerts associated
 * with a particular route.
 */

public class AlertDetailsScreen extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alert_detail_screen);

        /*
         *  The click listener for the menu button, sets that the
         *  button should return to main menu upon press.
         */
        ImageButton menu = findViewById(R.id.menubutton7);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AlertDetailsScreen.this, HomeScreen.class);
                startActivity(i);
            }
        }); /* setOnclickListener */

        /*
         *  The click listener for the pass button, sets that the
         *  button should go to the pass screen upon press.
         */
        ImageButton passButton = findViewById(R.id.passButton7);
        passButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AlertDetailsScreen.this, PassScreen.class);
                startActivity(i);
            }
        }); /* setOnclickListener */

        /* Try-Catch for getting all database information on the alert, and then displays info */
        try {
            ResultSet rs = stmt.executeQuery("SELECT affectedroutes.timestamp, affectedroutes.routeid,busroutes.routename,problems.problemdiscription\n" +
                                                "FROM affectedroutes, busroutes, problems\n" +
                                                "WHERE affectedroutes.problemid='" + currAlertInfo + "'\n" +
                                                "AND affectedroutes.routeid=busroutes.routeid\n" +
                                                "AND affectedroutes.problemid=problems.problemid");
            rs.next();
            String timestamp = rs.getString(1);
            String busName = "Bus #" + rs.getString(2);
            String routeName = rs.getString(3);
            String probDesc = rs.getString(4);

            TextView dateInfo = findViewById(R.id.dateField);
            @SuppressLint("SimpleDateFormat")
            DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy");
            String date = df.format(Calendar.getInstance().getTime());
            dateInfo.setText(date);

            TextView timeInfo = findViewById(R.id.timeField);
            timeInfo.setText(timestamp.substring(timestamp.indexOf(' '), timestamp.length() - 1));

            TextView busInfo = findViewById(R.id.busNumberField);
            busInfo.setText(busName);

            TextView routeInfo = findViewById(R.id.routeName);
            routeInfo.setText(routeName);

            TextView descInfo = findViewById(R.id.alertDescription);
            descInfo.setText(probDesc);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
} /* AlertDetailsScreen Class */