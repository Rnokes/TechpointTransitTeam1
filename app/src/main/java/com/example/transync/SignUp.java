package com.example.transync;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.sql.SQLException;

import static com.example.transync.SignIn.stmt;

public class SignUp extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        Button register = findViewById(R.id.register_button);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText fname = (EditText) findViewById(R.id.firstNameEntry);
                final String firstName = fname.getText().toString();

                EditText lname = (EditText) findViewById(R.id.lastNameEntry);
                final String lastName = lname.getText().toString();

                EditText dobEnt = (EditText) findViewById(R.id.dobEntry);
                final String dob = dobEnt.getText().toString();

                EditText emailEnt = (EditText) findViewById(R.id.emailEntry);
                final String email = emailEnt.getText().toString();

                EditText phoneEnt = (EditText) findViewById(R.id.editTextPhone);
                final String phone = phoneEnt.getText().toString();

                EditText pass = (EditText) findViewById(R.id.editTextTextPassword);
                final String password = pass.getText().toString();

                // Database Calls

                try {
                    stmt.executeUpdate("INSERT INTO users (firstname, lastname, email, password, phone, sms)" +
                            "VALUES('" + firstName + "','" + lastName + "','" + email + "','"+ password + "','"+ phone +"','yes')");
                } catch (SQLException e) {
                    System.out.println("Insertion failed.");
                    e.printStackTrace();
                }


                Intent i = new Intent(SignUp.this, RegComp.class);
                startActivity(i);


            }
        });

    }

}
