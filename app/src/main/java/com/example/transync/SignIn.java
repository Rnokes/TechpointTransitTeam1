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

import static java.lang.Thread.sleep;


public class SignIn extends Activity {

    public static Connection connection;
    public static Statement stmt;
    public static int userid;


    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss.ms").format(new Date());
        System.out.println("Timestamp Start: " + timeStamp);

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


        long initTime = System.currentTimeMillis();
        while (stmt == null) {
            if (System.currentTimeMillis() - initTime > 15000) {
                    System.out.println("Db connection took too long.");
                    findViewById(R.id.Internet_Failure).setVisibility(View.VISIBLE);
                    Intent i = new Intent(SignIn.this, Splash.class);
                    startActivity(i);
            } else {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss.ms").format(new Date());
        System.out.println("Timestamp End: " + timeStamp);


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

        Button forgotPassword = findViewById(R.id.info_forget);
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignIn.this, ForgotPasswordScreen.class);
                startActivity(i);
            }
        });


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
                    System.out.println("Incorrect login given");
                    findViewById(R.id.incorrectSignIn).setVisibility(View.VISIBLE);
                }
            }
        });


        Button signUp = findViewById(R.id.signup_button);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignIn.this, SignUp.class);
                startActivity(i);
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

        System.out.println("Login Call Using Creds: " + email + ", " + pass);

        ResultSet rs;
        int out = 0;

        // First call to db to verify id
        try {
            rs = dbCall("SELECT CASE WHEN email like '" + email +"' AND(passhash = crypt('"+ pass +"', passhash))= 'true' THEN 1 ELSE 0 END FROM users");
            while (rs.next()) {
                out = rs.getInt(1);
                if (out == 1) {
                    break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (out == 1) {
            // Call the db again and request the userid of the user/pass combo
            try {
                rs = dbCall("SELECT userid FROM users WHERE email like '" + email + "' AND (passhash = crypt('"+ pass +"', passhash))= 'true'");
                while (rs.next()) {
                    userid = rs.getInt(1);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            System.out.println("UserId: " + userid);
            return true;
        }
        else {
            return false;
        }
    }

    public static ResultSet dbCall(String query) throws SQLException {
        return stmt.executeQuery(query);
    }
}