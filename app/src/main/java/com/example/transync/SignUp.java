package com.example.transync;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.sql.SQLException;
import java.util.Objects;

import static com.example.transync.SignIn.stmt;

/*
 * Class that is responsible for handling account creation
 * and sign up. Pulls all entered data from the user and
 * registered a new account with the database. Sending live
 * emails is planned.
 */
public class SignUp extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        /* OnClick Listener for the register button, pulls all data and registers the account with the database */
        Button register = findViewById(R.id.register_button);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* Set Text Error Visibilities */
                findViewById(R.id.infoErr).setVisibility(View.INVISIBLE);
                findViewById(R.id.passErr).setVisibility(View.INVISIBLE);

                /* Following calls pull the entered data from the inputs */
                EditText fname = findViewById(R.id.firstNameEntry);
                final String firstName = fname.getText().toString();

                EditText lname = findViewById(R.id.lastNameEntry);
                final String lastName = lname.getText().toString();

                EditText emailEnt = findViewById(R.id.emailEntry);
                final String email = emailEnt.getText().toString();

                EditText phoneEnt = findViewById(R.id.editTextPhone);
                final String phone = phoneEnt.getText().toString();

                EditText passS = findViewById(R.id.passwordStart);
                final String passStart = passS.getText().toString();

                EditText passE = findViewById(R.id.passwordConfirm);
                final String passConfirm = passE.getText().toString();

                /* First do checks to make sure entered data is valid, if so makes the account and calls the database */
                if (Objects.equals(firstName, "") || Objects.equals(lastName, "") || Objects.equals(email, "") || Objects.equals(phone, "")) {
                    System.out.println("Reg info entered incorrectly.");
                    findViewById(R.id.infoErr).setVisibility(View.VISIBLE);
                }
                else if (!passStart.equals(passConfirm)) { // Passwords not matching
                    System.out.println("Reg passwords do not match.");
                    findViewById(R.id.passErr).setVisibility(View.VISIBLE);
                }
                else {  // Set calls to db and enter RegComp Screen
                    try {
                        stmt.executeUpdate("INSERT INTO users (firstname, lastname, email, passhash, phone, sms)" +
                                "VALUES('" + firstName + "','" + lastName + "','" + email + "', crypt('" + passStart + "', gen_salt('bf')),'" + phone + "','yes')");
                    } catch (SQLException e) {
                        System.out.println("Insertion failed.");
                        e.printStackTrace();
                    }

                    Intent i = new Intent(SignUp.this, RegComp.class);
                    startActivity(i);
                }
            } /* onclick() */
        }); /* setOnclickListener */
    } /* onCreate() */
} /* SignUp Class */
