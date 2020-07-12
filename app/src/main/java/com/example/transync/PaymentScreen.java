package com.example.transync;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import static com.example.transync.PurchaseScreen.busTypePurchased;

public class PaymentScreen extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_screen);

        switch(busTypePurchased) {
            case 1:
                // Daily
                break;
            case 2:
                // Weekly
                break;
            case 3:
                // Monthly
                break;
            default:
                System.out.println("No Pass Specified, Returning to Main Menu");
                Intent i = new Intent(PaymentScreen.this, HomeScreen.class);
                startActivity(i);
        }

    }
}

