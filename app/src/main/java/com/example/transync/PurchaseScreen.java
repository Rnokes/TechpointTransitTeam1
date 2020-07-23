package com.example.transync;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

/*
 * The PurchaseScreen class sets up the option for users to pick a
 * pass type they would like to buy, and then directs them to the payment
 * screen with the pass type id set in a global variable.
 */
public class PurchaseScreen extends Activity {

    public static int busTypePurchased;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.purchase_screen);

        /*
         *  The click listener for the menu button, sets that the
         *  button should return to main menu upon press.
         */
        ImageButton menu = findViewById(R.id.menubutton);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PurchaseScreen.this, HomeScreen.class);
                startActivity(i);
            }
        }); /* setOnclickListener */

        /*
         *  The click listener for the map button, sets that the
         *  button should go to the pass screen upon press.
         */
        ImageButton passButton = findViewById(R.id.passButton2);
        passButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PurchaseScreen.this, PassScreen.class);
                startActivity(i);
            }
        }); /* setOnclickListener */

        busTypePurchased = 0;

        Button daily = findViewById(R.id.daily_button);
        Button weekly = findViewById(R.id.weekly_button);
        Button monthly = findViewById(R.id.monthly_button);

        /* Following three OnClickListeners set buttons to go to the payment screen with a pass type */
        daily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                busTypePurchased = 1;
                Intent i = new Intent(PurchaseScreen.this, PaymentScreen.class);
                startActivity(i);
            }
        }); /* setOnclickListener - Daily */

        weekly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                busTypePurchased = 2;
                Intent i = new Intent(PurchaseScreen.this, PaymentScreen.class);
                startActivity(i);
            }
        }); /* setOnclickListener - Weekly */

        monthly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                busTypePurchased = 3;
                Intent i = new Intent(PurchaseScreen.this, PaymentScreen.class);
                startActivity(i);
            }
        }); /* setOnclickListener - Monthly*/

    } /* onCreate() */
} /* Purchase Screen Class */
