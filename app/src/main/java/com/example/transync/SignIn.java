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
import java.text.SimpleDateFormat;
import java.util.Date;

/*
 * Class handles the initial connection to the Heroku Database, which is hosted on
 * AWS Bucket. Does all the checks to make sure the connection is valid and stable with the
 * database. Once checks pass, stores the relevant connection variables in globals that
 * can be accessed when database calls are needed for the rest of the program screens.
 * This class also handles sign in information and validates that sign in is valid through the
 * database, database passwords are stored using a hash function for security purposes.
 */
public class SignIn extends Activity {

    public static Connection connection;
    public static Statement stmt;
    public static int userid;


    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);

        /* Sets the policy of the main thread to allow for outside connections */
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        /* Log data to test connection time */
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss.ms").format(new Date());
        System.out.println("Timestamp Start: " + timeStamp);

        /* Thread that starts up the connection to Heroku */
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

        /*
         * Waits on the connection thread, and if the connection is not
         * established after 15 seconds then it shuts down the program to the splash screen,
         * displaying a internet connection error.
         */
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

        /* More information for data logs */
        timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss.ms").format(new Date());
        System.out.println("Timestamp End: " + timeStamp);

        /* More checks on the connection to make sure it valid */
        if (stmt == null) {
            System.out.println("Connection to db failed. Shutting Down...");
            for (StackTraceElement trace : Thread.currentThread().getStackTrace()) {
                System.out.println(trace);
            }
            System.exit(1);
        } else {
            System.out.println("Connection to db successful!");
        }

        /* Sets up listener for forgot password screen so that it redirects correctly */
        Button forgotPassword = findViewById(R.id.info_forget);
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignIn.this, ForgotPasswordScreen.class);
                startActivity(i);
            } /* onClick() */
        }); /* setOnclickListener */

        /*
         * Sets up listener for sign in button, does verifying through database
         * to make sure login is valid then sends the user through to the next screen
         */
        Button signIn = findViewById(R.id.signin_button);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText user = findViewById(R.id.editUserText);
                final String email = user.getText().toString();

                EditText pass = findViewById(R.id.editSignInPass);
                final String password = pass.getText().toString();

                boolean attempt = login(email, password);

                if (attempt) {
                    Intent i = new Intent(SignIn.this, HomeScreen.class);
                    startActivity(i);
                } else {
                    System.out.println("Incorrect login given");
                    findViewById(R.id.incorrectSignIn).setVisibility(View.VISIBLE);
                }
            } /* onClick() */
        }); /* setOnclickListener */

        /* Sets up listener for the signup button so that it redirects correctly */
        Button signUp = findViewById(R.id.signup_button);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignIn.this, SignUp.class);
                startActivity(i);
            } /* onClick() */
        }); /* setOnclickListener */

    }

    /*
     * Method that starts up the connection to the heroku database. Uses a
     * URI that is planned to be more secure. Returns the results of the connection
     * after the attempt is made.
     */
    public static Connection getConnection() throws URISyntaxException, SQLException {
        @SuppressLint("AuthLeak") URI dbUri = new URI("postgres://ungvogbyninyqk:48f83f1a0cd8ae57d6cc2bb7337b81bfd12915b1dd7fdd8f5d461f61bed7f915@ec2-52-202-146-43.compute-1.amazonaws.com:5432/dbk1f16g641cet");

        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath() + "?sslmode=require&ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";

        return DriverManager.getConnection(dbUrl, username, password);
    } /* getConnection() */


    /*
     *  A function used for verifying the credentials used for a login.
     *  Calls the database using the credentials given, and verifies that
     *  they are valid. If they are not, it returns a false boolean and sets
     *  an incorrect login text to appear on the login screen. If they are
     *  correct, another database call is made requesting the userid of the
     *  successful login, a global variable is set to the userid, and true is returned.
     */
    public static boolean login(String email, String pass) {

        ResultSet rs;
        int out = 0;

        /* Initial verification call to db with given credentials */
        try {
            rs = stmt.executeQuery("SELECT CASE WHEN email like '" + email + "' AND(passhash = crypt('" + pass + "', passhash))= 'true' THEN 1 ELSE 0 END FROM users");
            while (rs.next()) {
                out = rs.getInt(1);
                if (out == 1) {
                    break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        /* Database is called again to get the userid associated with the correct user/pass, if the check passes */
        if (out == 1) {
            try {
                rs = stmt.executeQuery("SELECT userid FROM users WHERE email like '" + email + "' AND (passhash = crypt('" + pass + "', passhash))= 'true'");
                while (rs.next()) {
                    userid = rs.getInt(1);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            System.out.println("UserId: " + userid);
            return true;
        } else {
            return false;
        }
    } /* login() */
} /* SignIn Class */