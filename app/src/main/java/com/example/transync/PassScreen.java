package com.example.transync;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static com.example.transync.SignIn.stmt;
import static com.example.transync.SignIn.userid;

public class PassScreen extends Activity {

    @SuppressLint("SetTextI18n")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pass_screen);

        findViewById(R.id.noPassNotif).setVisibility(View.INVISIBLE);


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
        }
        else {

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

            // TODO: Implement Time Left on PassScreen
            // Use: "SELECT (expirationdate-current_timestamp) as timeremaining from users where userid=" + userid


            try {
                displayQRCode();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    private void displayQRCode() throws SQLException {

        ResultSet rs = stmt.executeQuery("select qrcode from users where userid=" + userid);
        rs.next();
        byte[] bArray = rs.getBytes(1);

        /*
        ArrayList<Byte> byteArr = new ArrayList<Byte>();
        while (rs.next()) {
            byteArr.add(rs.getByte(1));
        }


        byte[] bArray = new byte[byteArr.size()];
        for (int x = 0; x < byteArr.size(); x++) {
            bArray[x] = byteArr.get(x);
        }
        */

        System.out.println("Size: " + bArray.length);

        Bitmap bitmap = BitmapFactory.decodeByteArray(bArray, 0, bArray.length);
        ImageView qrCode = findViewById(R.id.qrCode);
        qrCode.setImageBitmap(bitmap);

    }
}
