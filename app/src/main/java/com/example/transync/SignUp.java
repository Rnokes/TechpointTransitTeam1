package com.example.transync;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SignUp extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        Button register = findViewById(R.id.register_button);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText user = (EditText) findViewById(R.id.firstNameEntry);
                final String email = user.getText().toString();

                EditText pass = (EditText) findViewById(R.id.editSignInPass);
                final String password = pass.getText().toString();


            }
        });

    }

}
