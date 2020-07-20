package com.example.transync;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.transync.SignIn.dbCall;


public class ForgotPasswordScreen extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password_screen);


        Button checkButton = findViewById(R.id.checkButton);
        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.unrecognizedEmailText).setVisibility(View.GONE);
                findViewById(R.id.newAccountButton).setVisibility(View.GONE);

                findViewById(R.id.recognizedEmailText).setVisibility(view.GONE);
                findViewById(R.id.recoveryButton).setVisibility(View.GONE);

                EditText user = (EditText) findViewById(R.id.emailBox);
                final String email = user.getText().toString();


                ResultSet rs;
                int out = 0;
                try {
                    rs = dbCall("SELECT CASE WHEN email like '" + email + "' THEN 1 ELSE 0 END FROM users");
                    while (rs.next()) {
                        out = rs.getInt(1);
                        if (out == 1) {
                            break;
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                if (out == 0) {
                    findViewById(R.id.unrecognizedEmailText).setVisibility(View.VISIBLE);
                    findViewById(R.id.newAccountButton).setVisibility(View.VISIBLE);
                }
                else{
                    findViewById(R.id.recognizedEmailText).setVisibility(view.VISIBLE);
                    findViewById(R.id.recoveryButton).setVisibility(View.VISIBLE);
                }
            }
        });
        Button returnButton = findViewById(R.id.returnButton);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ForgotPasswordScreen.this, SignIn.class);
                startActivity(i);
            }
        });

        Button accountButton = findViewById(R.id.newAccountButton);
        accountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ForgotPasswordScreen.this, SignUp.class);
                startActivity(i);
            }
        });

        Button recoverButton = findViewById(R.id.recoveryButton);
        recoverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.unrecognizedEmailText).setVisibility(View.GONE);
                findViewById(R.id.newAccountButton).setVisibility(View.GONE);
                findViewById(R.id.recognizedEmailText).setVisibility(view.GONE);
                findViewById(R.id.recoveryButton).setVisibility(View.GONE);
                findViewById(R.id.checkButton).setVisibility(View.GONE);
                findViewById(R.id.recoveryText).setVisibility(View.VISIBLE);

            }
        });

    }
}



