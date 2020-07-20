package com.example.transync;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

public class ReportScreen extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_screen);

        findViewById(R.id.typeIssueHere).setVisibility(View.VISIBLE);
        findViewById(R.id.reportSuccText).setVisibility(View.INVISIBLE);

        ImageButton menu = findViewById(R.id.menubutton);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ReportScreen.this, HomeScreen.class);
                startActivity(i);
            }
        });

        ImageButton passScreen = findViewById(R.id.passButton);
        passScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ReportScreen.this, PassScreen.class);
                startActivity(i);
            }
        });

        Button reportButton = findViewById(R.id.reportButton);
        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Spinner spinRoute = findViewById(R.id.routeSpinner);
                String routeSelect = spinRoute.getSelectedItem().toString();

                Spinner spinIssue = findViewById(R.id.issueSpinner);
                String issueSelect = spinIssue.getSelectedItem().toString();

                EditText descEntry = findViewById(R.id.typeIssueHere);
                String desc = descEntry.getText().toString();

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

                //TODO: DB call with issueId, routeSelect, and desc
                //TODO: Replace the route name array with the actual route names


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
                });

                display.start();

                try {
                    display.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Button returnButton = findViewById(R.id.returnButton);
                returnButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(ReportScreen.this, HomeScreen.class);
                        startActivity(i);
                    }
                });

            }
        });

    }
}