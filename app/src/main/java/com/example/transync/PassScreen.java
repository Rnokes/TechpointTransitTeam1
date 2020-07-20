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

public class PassScreen extends Activity {

    @SuppressLint("SetTextI18n")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pass_screen);

        findViewById(R.id.noPassNotif).setVisibility(View.INVISIBLE);
        findViewById(R.id.qrCode).setVisibility(View.INVISIBLE);


        ImageButton menu = findViewById(R.id.menubutton3);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PassScreen.this, HomeScreen.class);
                startActivity(i);
            }
        });

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

        if (!passHolder) {
            TextView passDesc = findViewById(R.id.Active_Pass_Text);
            passDesc.setText("No Pass Available");
            findViewById(R.id.noPassNotif).setVisibility(View.VISIBLE);
        } else {

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

    }

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



        //Bitmap bitmap = BitmapFactory.decodeByteArray(bArray, 0, bArray.length);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeByteArray(bArray, 0, bArray.length,options);
        options.inJustDecodeBounds = false;

        options.inSampleSize = calculateSampleSize(options.outWidth, options.outHeight, 288, 273);

        options = new BitmapFactory.Options();

        //May use null here as well. The function may interpret the pre-used options variable in ways hard to tell.
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

    }
}
