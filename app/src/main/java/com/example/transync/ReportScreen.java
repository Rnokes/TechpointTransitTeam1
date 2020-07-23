package com.example.transync;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.example.transync.SignIn.stmt;
import static com.example.transync.SignIn.userid;

/*
 *  Class that handles the manual reporting of issues with bus routes.
 *  Allows users to input the type of issue, the route of the associated issue,
 *  and a brief description of the issue. Then reports the issue to the database.
 */

public class ReportScreen extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_screen);

        /* Sets the visibility of error/succeed messages on creation */
        findViewById(R.id.typeIssueHere).setVisibility(View.VISIBLE);
        findViewById(R.id.reportSuccText).setVisibility(View.INVISIBLE);

        /*
         *  The click listener for the menu button, sets that the
         *  button should return to main menu upon press.
         */
        ImageButton menu = findViewById(R.id.menubutton);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ReportScreen.this, HomeScreen.class);
                startActivity(i);
            }
        });

        /*
         *  The click listener for the pass button, sets that the
         *  button should go to the pass screen upon press.
         */
        ImageButton passScreen = findViewById(R.id.passButton);
        passScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ReportScreen.this, PassScreen.class);
                startActivity(i);
            }
        }); /* setOnclickListener */

        /*
         *  The click listener for the report button, pulls the information from each of
         *  the input options, makes sure that an issue is selected, and sends all of the
         *  information to the database to be stored and then displayed in the results
         *  screen. Upon a successful report, it hides all of the input boxes, and reports
         *  the result of the database call, whether it succeeded or failed.
         */
        Button reportButton = findViewById(R.id.reportButton);
        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /* Following three declarations pull information from the screen's input */
                Spinner spinRoute = findViewById(R.id.routeSpinner);
                String routeSelect = spinRoute.getSelectedItem().toString();

                Spinner spinIssue = findViewById(R.id.issueSpinner);
                String issueSelect = spinIssue.getSelectedItem().toString();

                EditText descEntry = findViewById(R.id.typeIssueHere);
                String desc = descEntry.getText().toString();

                /* Following switch statement, selects the issue id of the report */
                System.out.println("Issue Selected: " + issueSelect);
                int issueId = -1;

                switch (issueSelect) {
                    case "Delayed Bus":
                        issueId = 1;
                        break;
                    case "Skipped Stop":
                        issueId = 2;
                        break;
                    case "Bus Cancelled":
                        issueId = 3;
                        break;
                    case "Other":
                        issueId = 4;
                        break;
                    default:
                        System.out.println("No issue Specified, Returning to Main Menu");
                        Intent i = new Intent(ReportScreen.this, HomeScreen.class);
                        startActivity(i);
                }


                /* Call to database that updates the report field with related info */
                try {
                    ResultSet rs = stmt.executeQuery("SELECT routeid FROM busRoutes WHERE routename='" + routeSelect + "'");
                    rs.next();
                    int routeId = rs.getInt(1);
                    stmt.executeUpdate("INSERT INTO affectedroutes(routeid, problemid, stopid, timestamp, resolved, submitted_by, verified, discription)" +
                                          "VALUES('" + routeId + "', '" + issueId + "', '" + 1 + "', CURRENT_TIMESTAMP, 'No', '" + userid + "', 'No', '" + desc + "')");
                } catch (SQLException e) {
                    e.printStackTrace();
                }


                /* Sets the visibility of the input boxes and then displays the db call result */
                Thread display = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        synchronized (this) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    findViewById(R.id.typeIssueHere).setVisibility(View.INVISIBLE);
                                    findViewById(R.id.reportButton).setVisibility(View.INVISIBLE);
                                    findViewById(R.id.issueSpinner).setVisibility(View.INVISIBLE);
                                    findViewById(R.id.routeSpinner).setVisibility(View.INVISIBLE);
                                    findViewById(R.id.reportSuccText).setVisibility(View.VISIBLE);
                                    findViewById(R.id.returnButton).setVisibility(View.VISIBLE);
                                }
                            });
                        }
                    }
                }); /* Thread Runnable() */
                display.start();

                try {
                    display.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                /*
                 *  The click listener for another menu button, sets that the
                 *  button should return to the main menu upon press.
                 */
                Button returnButton = findViewById(R.id.returnButton);
                returnButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(ReportScreen.this, HomeScreen.class);
                        startActivity(i);
                    }
                }); /* setOnclickListener */

            }
        }); /* setOnclickListener */

    } /* onCreate() */
} /* ReportScreen Class */