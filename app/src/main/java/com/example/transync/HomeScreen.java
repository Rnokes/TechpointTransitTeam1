package com.example.transync;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.example.transync.SignIn.dbCall;
import static com.example.transync.SignIn.userid;
import static java.lang.Thread.sleep;

public class HomeScreen extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);

        String first = "";
        String last = "";
        try {
            ResultSet rs = dbCall("SELECT firstname, lastname FROM users WHERE userid = \'" + userid + "\'");
            while (rs.next()) {
                first = rs.getString(1);
                last = rs.getString(2);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String dbNameCall = "Hello " + first + " " + last + "!";

        TextView displayName = findViewById(R.id.nameDisplay);
        displayName.setText(dbNameCall);

        final TextView timedisp = findViewById(R.id.texttimedisp);
        @SuppressLint("SimpleDateFormat")
        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z");
        String date = df.format(Calendar.getInstance().getTime());
        timedisp.setText(date);


        ImageButton passButton = findViewById(R.id.passButton);
        passButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeScreen.this, PassScreen.class);
                startActivity(i);
            }
        });

        Button my_alert = findViewById(R.id.my_alert_button);
        my_alert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeScreen.this, MyAlertsScreen.class);
                startActivity(i);
            }
        });


        Button purchase = findViewById(R.id.buy_pass_button);
        purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeScreen.this, PurchaseScreen.class);
                startActivity(i);
            }
        });

        Button pass = findViewById(R.id.my_pass_button);
        pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeScreen.this, PassScreen.class);
                startActivity(i);
            }
        });

        Button report = findViewById(R.id.report_button);
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeScreen.this, ReportScreen.class);
                startActivity(i);
            }
        });

        Button all_routes = findViewById(R.id.all_routes_button);
        all_routes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeScreen.this, AllRoutesScreen.class);
                startActivity(i);
            }
        });

        Button my_routes = findViewById(R.id.my_routes_button);
        my_routes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeScreen.this, MyRoutesScreen.class);
                startActivity(i);
            }
        });

    }

}
