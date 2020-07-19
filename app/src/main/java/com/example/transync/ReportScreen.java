package com.example.transync;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import static java.lang.Thread.sleep;

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

        Button reportButton = findViewById(R.id.report_button);
        passScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Spinner spinRoute = findViewById(R.id.routeSpinner);
                String routeSelect = spinRoute.getSelectedItem().toString();

                Spinner spinIssue = findViewById(R.id.issueSpinner);
                String issueSelect = spinRoute.getSelectedItem().toString();

                EditText descEntry =  findViewById(R.id.typeIssueHere);
                String desc = descEntry.getText().toString();

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

                // DB call with issueId, routeSelect, and desc


                System.out.print("Issue Reported");

                findViewById(R.id.typeIssueHere).setVisibility(View.INVISIBLE);
                findViewById(R.id.reportSuccText).setVisibility(View.VISIBLE);

                try {
                    sleep(2800);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Intent i = new Intent(ReportScreen.this, HomeScreen.class);
                startActivity(i);
            }
        });

    }
}