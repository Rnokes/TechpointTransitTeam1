package com.example.transync;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import static com.example.transync.PurchaseScreen.busTypePurchased;

public class PaymentScreen extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_screen);

        ImageButton menu = findViewById(R.id.menubutton2);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PaymentScreen.this, HomeScreen.class);
                startActivity(i);
            }
        });

        Button back = findViewById(R.id.back_button);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PaymentScreen.this, PurchaseScreen.class);
                startActivity(i);
            }
        });

        double price = 0.00;
        String timeLength = "";
        String type = "";

        switch(busTypePurchased) {
            case 1:
                // Daily
                price = 5.00;
                timeLength = "24 hours";
                type = "Daily";
                break;
            case 2:
                // Weekly
                price = 25.00;
                timeLength = "7 days";
                type = "Weekly";
                break;
            case 3:
                // Monthly
                price = 40.00;
                timeLength = "30 days";
                type = "Monthly";
                break;
            default:
                System.out.println("No Pass Specified, Returning to Main Menu");
                Intent i = new Intent(PaymentScreen.this, HomeScreen.class);
                startActivity(i);
        }

        TextView priceText = findViewById(R.id.pricedesc);
        TextView passType = findViewById(R.id.passtype);
        TextView indyGo = findViewById(R.id.indygo_desc);
        Button pay = findViewById(R.id.purchase_button);

        priceText.setText("Price: $" + price +"0");
        passType.setText(type);
        indyGo.setText("This pass we will be valid for " + timeLength + " in the IndyGo bus system upon purchase.");

        Button payment = findViewById(R.id.purchase_button);
        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }
}

