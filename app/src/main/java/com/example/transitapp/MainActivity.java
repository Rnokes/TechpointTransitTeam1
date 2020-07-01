package com.example.transitapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    public static final String EMAIL = "com.example.transitapp.MESSAGE";
    public static final String PASS = "com.example.transitapp.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    public void signIn(View view) {
        Intent intent;

        EditText editText = (EditText) findViewById(R.id.editTextTextEmailAddress);
        String email = editText.getText().toString();

        editText = (EditText) findViewById(R.id.editTextTextPassword);
        String pass = editText.getText().toString();


        /*
            Input a call to database here, using the email and pass variables
            if they are correct, then make a startActivity call to the bus pass
            area of the application; otherwise display an error message to user
            if time allows, implement an allowance of tries, so that it locks
            out after say 5 attempts
            TODO: Database call and check
            TODO: Make more secure if time allows

         if ( sql call succeeds ) {

            intent = new Intent(this, DisplayMessageActivity.class);
            intent.putExtra(EMAIL, email);
            intent.putExtra(PASS, pass);

            startActivity(intent);
         }
         else {

            intent = new Intent(this, DisplayErrorMessageActivity.class);
         }

         */

    }

}