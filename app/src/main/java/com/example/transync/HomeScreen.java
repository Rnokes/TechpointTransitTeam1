package com.example.transync;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static java.lang.Thread.sleep;

public class HomeScreen extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);

        //TODO: Db call to get name of a user with set global userid

        String dbNameCall = "Hello John Doe";

        TextView displayName = (TextView) findViewById(R.id.nameDisplay);
        displayName.setText(dbNameCall);

        final TextView timedisp = (TextView) findViewById(R.id.texttimedisp);
        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z");
        String date = df.format(Calendar.getInstance().getTime());
        timedisp.setText(date);


        Button purchase = findViewById(R.id.buy_pass_button);
        purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeScreen.this, PurchaseScreen.class);
                startActivity(i);
            }
        });



    }

}
