package com.example.transync;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class PurchaseScreen extends Activity {

    public static int busTypePurchased;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.purchase_screen);

        ImageButton menu = findViewById(R.id.menubutton);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PurchaseScreen.this, HomeScreen.class);
                startActivity(i);
            }
        });

        busTypePurchased = 0;

        Button daily = findViewById(R.id.daily_button);
        Button weekly = findViewById(R.id.weekly_button);
        Button monthly = findViewById(R.id.monthly_button);

        daily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                busTypePurchased = 1;
                Intent i = new Intent(PurchaseScreen.this, PaymentScreen.class);
                startActivity(i);
            }
        });

        weekly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                busTypePurchased = 2;
                Intent i = new Intent(PurchaseScreen.this, PaymentScreen.class);
                startActivity(i);
            }
        });

        monthly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                busTypePurchased = 3;
                Intent i = new Intent(PurchaseScreen.this, PaymentScreen.class);
                startActivity(i);
            }
        });

    }

}
