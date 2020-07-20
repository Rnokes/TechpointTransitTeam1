package com.example.transync;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MyAlertsScreen extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_alerts_screen);

        ImageButton menu = findViewById(R.id.menubutton);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MyAlertsScreen.this, HomeScreen.class);
                startActivity(i);
            }
        });

        ImageButton passButton = findViewById(R.id.passButton6);
        passButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MyAlertsScreen.this, PassScreen.class);
                startActivity(i);
            }
        });


        //TODO: DB call to get all routes related to the userid and correct days

    }
}