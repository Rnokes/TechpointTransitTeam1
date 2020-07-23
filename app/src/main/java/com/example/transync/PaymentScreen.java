package com.example.transync;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.WriterException;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalOAuthScopes;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

import static com.example.transync.PurchaseScreen.busTypePurchased;
import static com.example.transync.SignIn.stmt;
import static com.example.transync.SignIn.userid;

/*
 * Class that handles all payment using paypals sdk libraries.
 * It sets up the user for interacting with paypals, and then sends
 * information to paypals service to initiate a transaction, the class
 * then handles the receiving data after the transaction is handled.
 */
public class PaymentScreen extends Activity {

    private static final String TAG = "passPayment";
    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_NO_NETWORK;
    private static final String CONFIG_CLIENT_ID = "AS43kxQznFJDM1czIi654wsxL6QxzsFQ51EtuaDkqwGZdKkwymYuOpN2WaaTC9lFlVcWDrLMCwq5kZom";
    private static final int REQUEST_CODE_PAYMENT = 1;
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(CONFIG_ENVIRONMENT)
            .clientId(CONFIG_CLIENT_ID)
            .merchantName("IndyGo")
            .merchantPrivacyPolicyUri(Uri.parse("https://www.example.com/privacy"))
            .merchantUserAgreementUri(Uri.parse("https://www.example.com/legal"));

    @SuppressLint("SetTextI18n")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_screen);

        /*
         *  The click listener for the menu button, sets that the
         *  button should return to main menu upon press.
         */
        ImageButton menu = findViewById(R.id.menubutton2);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PaymentScreen.this, HomeScreen.class);
                startActivity(i);
            }
        }); /* setOnclickListener */

        /*
         *  The click listener for the map button, sets that the
         *  button should go to the pass screen upon press.
         */
        ImageButton passButton = findViewById(R.id.passButton3);
        passButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PaymentScreen.this, PassScreen.class);
                startActivity(i);
            }
        }); /* setOnclickListener */

        /*
         *  The click listener for the back button, sets that the
         *  button should go to the choose a pass upon press.
         */
        Button back = findViewById(R.id.back_button);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PaymentScreen.this, PurchaseScreen.class);
                startActivity(i);
            }
        }); /* setOnclickListener */


        /* Following switch statement sets the preceding variables depending on the pass that was selected previously */
        double price = 0.00;
        String timeLength = "";
        String type = "";

        switch (busTypePurchased) {
            case 1:
                /* Daily */
                price = 5.00;
                timeLength = "24 hours";
                type = "Daily";
                break;
            case 2:
                /* Weekly */
                price = 25.00;
                timeLength = "7 days";
                type = "Weekly";
                break;
            case 3:
                /* Monthly */
                price = 40.00;
                timeLength = "30 days";
                type = "Monthly";
                break;
            default:
                System.out.println("No Pass Specified, Returning to Main Menu");
                Intent i = new Intent(PaymentScreen.this, HomeScreen.class);
                startActivity(i);
        }

        /* Variables are set to the correct view ids for manipulation */
        TextView priceText = findViewById(R.id.pricedesc);
        TextView passType = findViewById(R.id.passtype);
        TextView indyGo = findViewById(R.id.indygo_desc);
        Button pay = findViewById(R.id.purchase_button);

        priceText.setText("Price: $" + price + "0");
        passType.setText(type);
        indyGo.setText("This pass we will be valid for " + timeLength + " in the IndyGo bus system upon purchase.");

        /*
         * This clickListener is set up so that once the user clicks the payment button,
         * a new activity is launched out of the paypal sdk, which starts the transaction
         * process for buying the pass and generating the qr code.
         */
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PaymentScreen.this, PayPalService.class);
                intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
                startService(intent);
                onBuyPressed();
            }
        }); /* setOnclickListener */

    } /* onCreate() */

    /*
     * Method for the paypal activity that tells the servers what is being
     * bought and for how much.
     */
    public void onBuyPressed() {
        PayPalPayment thingToBuy = getPassToBuy(PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(PaymentScreen.this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);

        startActivityForResult(intent, REQUEST_CODE_PAYMENT);
    } /* onBuyPressed() */

    /*
     * Method that finds the type of payment that needs to be
     * processed by the paypal servers. Is determined from previous screen global.
     */
    private PayPalPayment getPassToBuy(String paymentIntent) {
        switch (busTypePurchased) {
            case 1:
                /* Daily */
                return new PayPalPayment(new BigDecimal("5.00"), "USD", "Daily Pass", paymentIntent);

            case 2:
                /* Weekly */
                return new PayPalPayment(new BigDecimal("25.00"), "USD", "Weekly Pass", paymentIntent);

            case 3:
                /* Monthly */
                return new PayPalPayment(new BigDecimal("40.00"), "USD", "Monthly Pass", paymentIntent);

            default:
                System.out.println("No Pass Specified, Returning to Main Menu");
                Intent i = new Intent(PaymentScreen.this, HomeScreen.class);
                startActivity(i);
        }
        return null;
    } /* getPassToBuy() */

    /*
     * Small method to provide the scopes needed for authenticating in the paypal
     * activity that runs off of the paypal api.
     */
    private PayPalOAuthScopes getOauthScopes() {
        Set<String> scopes = new HashSet<>(Arrays.asList(PayPalOAuthScopes.PAYPAL_SCOPE_EMAIL, PayPalOAuthScopes.PAYPAL_SCOPE_ADDRESS));
        return new PayPalOAuthScopes(scopes);
    } /* getOauthScopes() */

    /*
     * Hides most of the screen, and then displays the result of
     * the transaction. And adds a button to go the home screen.
     */
    @SuppressLint("SetTextI18n")
    protected void displayResultText(String result) {
        ((TextView) findViewById(R.id.resultText)).setText("Result : " + result);
        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();

        findViewById(R.id.passtype).setVisibility(View.INVISIBLE);
        findViewById(R.id.pricedesc).setVisibility(View.INVISIBLE);
        findViewById(R.id.indygo_desc).setVisibility(View.INVISIBLE);
        findViewById(R.id.purchase_button).setVisibility(View.INVISIBLE);

        /*
         *  The click listener for the back button, sets that the
         *  button should go to the home screen upon press.
         */
        Button back = findViewById(R.id.back_button);
        back.setText("View My Bus Passes");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PaymentScreen.this, PassScreen.class);
                startActivity(i);
            }
        }); /* setOnclickListener */
    } /* displayResultText() */

    /*
     * Method that checks what happened during the paypal activity calls,
     * and checks whether the returned data was a successful transaction or
     * a failure, and then acts accordingly.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {
                        Log.i(TAG, confirm.toJSONObject().toString(4));
                        Log.i(TAG, confirm.getPayment().toJSONObject().toString(4));

                        paymentSucceedToDB();
                        createQR();
                        displayResultText("Payment Confirmation received from PayPal");
                    } catch (JSONException | SQLException e) {
                        Log.e(TAG, "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i(TAG, "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i(TAG,"An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        }
    } /* onActivityResult() */

    /*
     * Method that is called if the transaction is successful,
     * it generates a unique QR code and then encodes and compresses it
     * to a format that the database can accept. It then stores the
     * generated QR Code to the database under a userid.
     */
    private void createQR() {

        String TAG = "GenerateQRCode";
        Bitmap bitmap = null;
        QRGEncoder qrgEncoder;

        /* QR Code is generated based off of given data and a size of screen, and then is encoded to a bitmap */
        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        int height = point.y;
        int smallerDimension = Math.min(width, height);
        smallerDimension = smallerDimension * 3 / 4;
        String qrData = "Valid Pass : " + userid;
        qrgEncoder = new QRGEncoder(qrData, null, QRGContents.Type.TEXT, smallerDimension);
        try {
            bitmap = qrgEncoder.encodeAsBitmap();
        } catch (WriterException e) {
            Log.v(TAG, e.toString());
        }

        /* Bitmap is compressed and transferred to a byte array */
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        assert bitmap != null;
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
        byte[] bArray = bos.toByteArray();

        /* Call to database to store the QR Code. */
        try {
            stmt.executeUpdate("UPDATE users set qrcode='" + Arrays.toString(bArray) + "' where userid=" + userid);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    } /* createQR() */

    /*
     * If the transaction succeeds, this method is called, which calls the database
     * and sends all of the relevant information, and stores it based on userid.
     */
    private void paymentSucceedToDB() throws SQLException {
        switch (busTypePurchased) {
            case 1:
                /* Daily */
                stmt.executeUpdate("UPDATE users " +
                        "SET buspassid_fk='2', expirationdate= current_timestamp + interval '24 hours' " +
                        "WHERE userid=" + userid);
                break;

            case 2:
                /* Weekly */
                stmt.executeUpdate("UPDATE users " +
                        "SET buspassid_fk='3', expirationdate= current_timestamp + interval '7 days' " +
                        "WHERE userid=" + userid);
                break;

            case 3:
                /* Monthly */
                stmt.executeUpdate("UPDATE users " +
                        "SET buspassid_fk='4', expirationdate= current_timestamp + interval '31 days' " +
                        "WHERE userid=" + userid);
                break;

            default:
                System.out.println("No Pass Specified, Returning to Main Menu");
                Intent i = new Intent(PaymentScreen.this, HomeScreen.class);
                startActivity(i);
        }
    } /* paymentSucceedToDB() */

    /*
     * Method that stops the paypal service when the transaction is over.
     */
    @Override
    public void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    } /* onDestroy() */
}
