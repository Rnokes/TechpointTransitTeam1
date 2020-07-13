package com.example.transync;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static java.lang.Thread.sleep;


public class SignIn extends Activity {

    public static Connection connection;
    public static Statement stmt;
    public static int userid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    connection = getConnection();

                } catch (URISyntaxException | SQLException e) {
                    e.printStackTrace();
                }

                try {
                    stmt = connection.createStatement();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        try {
            sleep(800);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (stmt == null) {
            System.out.println("Connection to db failed. Shutting Down...");
            for (StackTraceElement trace : Thread.currentThread().getStackTrace()) {
                System.out.println(trace);
            }
            System.exit(1);
        }
        else {
            System.out.println("Connection to db successful!");
        }


        Button signIn = findViewById(R.id.signin_button);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText user = (EditText) findViewById(R.id.editUserText);
                final String email = user.getText().toString();

                EditText pass = (EditText) findViewById(R.id.editSignInPass);
                final String password = pass.getText().toString();

                boolean attempt = login(email, password);

                if (attempt) {
                    Intent i = new Intent(SignIn.this, HomeScreen.class);
                    startActivity(i);
                }
                else {
                    //TODO: Add a pop up saying incorrect input, and set these listeners up in a loop.
                    System.out.println("Incorrect login given");
                }
            }
        });


    }


    public static Connection getConnection() throws URISyntaxException, SQLException {
        @SuppressLint("AuthLeak") URI dbUri = new URI("postgres://ungvogbyninyqk:48f83f1a0cd8ae57d6cc2bb7337b81bfd12915b1dd7fdd8f5d461f61bed7f915@ec2-52-202-146-43.compute-1.amazonaws.com:5432/dbk1f16g641cet");

        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath() + "?sslmode=require&ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";

        return DriverManager.getConnection(dbUrl, username, password);
    }

    public static boolean login(String email, String pass) {
        boolean emailCorrect = false;
        boolean passCorrect = false;

        System.out.println("Login Call Using Creds: " + email + ", " + pass);

        ResultSet rs = null;

        try {
            rs = dbCall("SELECT email, password,CASE WHEN email like '+ email +' AND password like '+ pass' THEN = 1 ELSE 0 END FROM users ");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Iterate through rs

        try {
            rs = dbCall("SELECT email, password,CASE WHEN email like '+ email +' AND password like '+ pass' THEN = 1 ELSE 0 END FROM users ");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Iterate through rs

        if (emailCorrect && passCorrect) {
            // Call the db again and request the userid of the user/pass combo
            userid = 1;
            return true;
        }

        return true;
    }

    public static ResultSet dbCall(String query) throws SQLException {
        ResultSet rs = null;
        rs = stmt.executeQuery(query);
        return rs;
    }
}