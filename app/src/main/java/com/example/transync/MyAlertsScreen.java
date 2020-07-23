package com.example.transync;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.example.transync.SignIn.stmt;
import static com.example.transync.SignIn.userid;


/*
 * Class that dynamically displays all currently listed alerts in the database,
 * that are associated with a users my routes and their userid. Displays the
 * alerts pertinent to today and the day before.
 */

public class MyAlertsScreen extends Activity {

    public static int currAlertInfo;

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



        /* Gets the scrollview responsible for yesterday and puts set a linearlayout up. */
        ScrollView scrollViewToday = (ScrollView) findViewById(R.id.scrollView2);
        LinearLayout linearLayoutToday = new LinearLayout(this);
        linearLayoutToday.setOrientation(LinearLayout.VERTICAL);

        /*
         * Following try catch gets issues related to the current day based on the user
         * and then setOnClickListeners to them to make them go to the alert details screen.
         */
        try {
            ResultSet rs = stmt.executeQuery("Select busstops.stopname, busroutes.routename, problemdiscription, affectedroutes.problemid\n" +
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
                                                "    AND userfavorites.stopid=busstops.stopid\n" +
                                                "    AND userid="+ userid +"\n" +
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
                        Intent intent = new Intent(MyAlertsScreen.this, AlertDetailsScreen.class);
                        intent.putExtra("selectedAlert",currAlertInfo);
                        startActivity(intent);
                    }
                }); /* setOnclickListener */
                linearLayoutToday.addView(button);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        scrollViewToday.addView(linearLayoutToday);


        /* Gets the scrollview responsible for yesterday and puts set a linearlayout up. */
        ScrollView scrollViewYest = (ScrollView) findViewById(R.id.scrollView3);
        LinearLayout linearLayoutYest = new LinearLayout(this);
        linearLayoutYest.setOrientation(LinearLayout.VERTICAL);

        /*
         * Following try catch gets issues related to the previous day based on the user
         * and then setOnClickListeners to them to make them go to the alert details screen.
         */
        try {
            ResultSet rs = stmt.executeQuery("Select busstops.stopname, busroutes.routename, problemdiscription, affectedroutes.problemid\n" +
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
                                                "    AND userfavorites.stopid=busstops.stopid\n" +
                                                "    AND userid="+ userid +"\n" +
                                                "    AND (current_timestamp-affectedroutes.timestamp)<'2 day'" +
                                                "    AND (current_timestamp-affectedroutes.timestamp)>'1 day'");

            while(rs.next()) {
                Button button = new Button(this);
                button.setText(rs.getString(2) + " | " + rs.getString(3));
                button.setBackground(getResources().getDrawable(R.drawable.button_outline));
                final int probId = rs.getInt(4);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        currAlertInfo = probId;
                        Intent intent = new Intent(MyAlertsScreen.this, AlertDetailsScreen.class);
                        intent.putExtra("selectedAlert",currAlertInfo);
                        startActivity(intent);
                    }
                }); /* setOnclickListener */

                linearLayoutYest.addView(button);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        scrollViewYest.addView(linearLayoutYest);

    } /* onCreate() */
} /* MyAlertsScreen Class */