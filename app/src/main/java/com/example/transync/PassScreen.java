package com.example.transync;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Spinner;

public class PassScreen extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pass_screen);

        ImageButton menu = findViewById(R.id.menubutton3);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PassScreen.this, HomeScreen.class);
                startActivity(i);
            }
        });

        Spinner passSpin = findViewById(R.id.passSpinner);

        // DB call to get an array of values that go into passSpin

        // Set array

        // Do checks to see if pass is available, if not display data, of so display first option
        // and then add an onClick listener for changing the codes if they pick a new pass


    }
}
