package com.example.transync;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MyRoutesScreen extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_routes_screen);

        ImageButton menu = findViewById(R.id.menubutton);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MyRoutesScreen.this, HomeScreen.class);
                startActivity(i);
            }
        });

        Button myAlerts = findViewById(R.id.myAlerts);
        myAlerts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MyRoutesScreen.this, MyAlertsScreen.class);
                startActivity(i);
            }
        });

        Button mapButton = findViewById(R.id.routeMap);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MyRoutesScreen.this, MapScreen.class);
                startActivity(i);
            }
        });


        //TODO: Get DB call to the get routes related to userid
    }
}