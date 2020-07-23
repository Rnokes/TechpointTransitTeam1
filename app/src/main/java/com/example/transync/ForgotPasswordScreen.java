package com.example.transync;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.example.transync.SignIn.stmt;

/*
 * This class represents the Forgot Password Screen that
 * lets the user enter their email and get a change password email
 * sent to the email they entered if the email is in the database.
 */

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

                findViewById(R.id.recognizedEmailText).setVisibility(View.GONE);
                findViewById(R.id.recoveryButton).setVisibility(View.GONE);

                EditText user = findViewById(R.id.emailBox);
                final String email = user.getText().toString();


                ResultSet rs;
                int out = 0;
                try {
                    rs = stmt.executeQuery("SELECT CASE WHEN email like '" + email + "' THEN 1 ELSE 0 END FROM users");
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
                } else {
                    findViewById(R.id.recognizedEmailText).setVisibility(View.VISIBLE);
                    findViewById(R.id.recoveryButton).setVisibility(View.VISIBLE);
                }
            }
        }); /* setOnclickListener */


        Button returnButton = findViewById(R.id.returnButton);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ForgotPasswordScreen.this, SignIn.class);
                startActivity(i);
            }
        }); /* setOnclickListener */


        Button accountButton = findViewById(R.id.newAccountButton);
        accountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ForgotPasswordScreen.this, SignUp.class);
                startActivity(i);
            }
        }); /* setOnclickListener */


        Button recoverButton = findViewById(R.id.recoveryButton);
        recoverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.unrecognizedEmailText).setVisibility(View.GONE);
                findViewById(R.id.newAccountButton).setVisibility(View.GONE);
                findViewById(R.id.recognizedEmailText).setVisibility(View.GONE);
                findViewById(R.id.recoveryButton).setVisibility(View.GONE);
                findViewById(R.id.checkButton).setVisibility(View.GONE);
                findViewById(R.id.recoveryText).setVisibility(View.VISIBLE);

            }
        }); /* setOnclickListener */

    } /* onCreate() */
} /* ForgotPasswordScreen Class */



