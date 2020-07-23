package com.example.transync;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/*
 * Class that displays the Registration Completion Screen
 * and sets up listeners for the button on the screen.
 */
public class RegComp extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reg_comp);

        /*
         *  The click listener for the back button, sets that the
         *  button should return to sign in upon press.
         */
        Button reg_comp = findViewById(R.id.reg_comp_button);
        reg_comp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RegComp.this, SignIn.class);
                startActivity(i);
            }
        }); /* setOnclickListener */
    } /* onCreate() */
} /* RegComp Class */
