package com.example.transync;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.example.transync.SignIn.stmt;
import static com.example.transync.SignIn.userid;

/*
 * The following class determines if the the user has a valid pass
 * and then sets tbe view page up accordingly, either redirecting to
 * the buy a pass screen or pulling the pass from the database and displaying it.
 */

public class PassScreen extends Activity {

    @SuppressLint("SetTextI18n")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pass_screen);

        findViewById(R.id.noPassNotif).setVisibility(View.INVISIBLE);
        findViewById(R.id.qrCode).setVisibility(View.INVISIBLE);

        /*
         *  The click listener for the menu button, sets that the
         *  button should return to main menu upon press.
         */
        ImageButton menu = findViewById(R.id.menubutton3);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PassScreen.this, HomeScreen.class);
                startActivity(i);
            }
        }); /* setOnclickListener */


        /* Following to try-catches are database calls to determine if the user has a valid pass */
        boolean passHolder = false;
        try {
            ResultSet rs = stmt.executeQuery("SELECT CASE WHEN buspassid_fk IS NOT NULL THEN 1 ELSE 0 END FROM users WHERE userid=" + userid);
            while (rs.next()) {
                int bool = rs.getInt(1);
                if (bool == 1) {
                    passHolder = true;
                    break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            ResultSet rs = stmt.executeQuery("SELECT (expirationdate-current_timestamp) as timeremaining from users where userid=" + userid);
            while (rs.next()) {
                String timestamp = rs.getString(1);

                if (timestamp.contains("-")) {
                    passHolder = false;
                    break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        /*
         * This if statement determines if the user has a pass based on the previous checks
         * and then either sets the views to negative or displays the qr code.
         */
        if (!passHolder) {
            TextView passDesc = findViewById(R.id.Active_Pass_Text);
            passDesc.setText("No Pass Available");
            findViewById(R.id.noPassNotif).setVisibility(View.VISIBLE);
        }
        else {
            /* Following Code Determines the pass type and sets the view fields accordingly */
            String passType = "";
            try {
                ResultSet rs = stmt.executeQuery("SELECT buspass.typediscription from buspass, users where buspass.buspassid=users.buspassid_fk AND userid=" + userid);
                while (rs.next()) {
                    passType = rs.getString(1);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            TextView passDesc = findViewById(R.id.Active_Pass_Text);
            passDesc.setText(passType);

            findViewById(R.id.qrCode).setVisibility(View.VISIBLE);


            /* Following code finds the date and time of the pass, and sets fields in the view accordingly */
            ResultSet rs = null;
            String timestamp = "";

            try {
                rs = stmt.executeQuery("SELECT (expirationdate-current_timestamp) as timeremaining from users where userid=" + userid);
                rs.next();
                timestamp = rs.getString(1);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            System.out.println("Time Left Stamp: " + timestamp);

            if (timestamp.toLowerCase().contains("days")) {
                TextView days = findViewById(R.id.daysLeftCounter);
                days.setText("Days Left");
                TextView num = findViewById(R.id.timeLeftCounter);
                timestamp = timestamp.substring(0, timestamp.indexOf(" "));
                num.setText(timestamp);
            } else {
                TextView hours = findViewById(R.id.daysLeftCounter);
                hours.setText("Hours Left");
                TextView num = findViewById(R.id.timeLeftCounter);
                timestamp = timestamp.substring(0, timestamp.indexOf(":"));
                num.setText(timestamp);
            }
        }
    } /* onCreate() */

    /*
     * A method for pulling a qr code from the database, decoding it into a bitmap,
     * and then displaying the qr code to the app. Is currently buggy, so a placeholder
     * image is used instead on the main screen.
     */

    private void displayQRCode() throws SQLException {

        ResultSet rs = stmt.executeQuery("select qrcode from users where userid=" + userid);
        rs.next();
        byte[] bArray = rs.getBytes(1);

        findViewById(R.id.qrCode).setVisibility(View.VISIBLE);

        /*
        System.out.println("Size: " + bArray.length);

        Bitmap bitmap = BitmapFactory.decodeByteArray(bArray, 0, bArray.length);
        ImageView qrCode = findViewById(R.id.qrCode);
        qrCode.setImageBitmap(bitmap);


        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeByteArray(bArray, 0, bArray.length,options);

        options.inJustDecodeBounds = false;
        options.inSampleSize = calculateSampleSize(options.outWidth, options.outHeight, 288, 273);
        options = new BitmapFactory.Options();

        Bitmap unscaledBitmap = BitmapFactory.decodeResource(res, resId, options);

        if(unscaledBitmap == null)
        {
            Log.e("ERR","Failed to decode resource - " + resId + " " + res.toString());
            return null;
        }

        Bitmap bitmap1 = Bitmap.createScaledBitmap(bitmap, 288, 273,true);

        ImageView qrCode = (ImageView) findViewById(R.id.qrCode);
        qrCode.setImageBitmap(bitmap1);

         */
    } /* displayQRCode() */
} /* PassScreen Class */
