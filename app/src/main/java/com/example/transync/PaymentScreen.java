package com.example.transync;

import com.paypal.android.sdk.payments.*;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static com.example.transync.PurchaseScreen.busTypePurchased;

public class PaymentScreen extends Activity {

    private static final String TAG = "paymentExample";

    /**
     * - Set to PayPalConfiguration.ENVIRONMENT_PRODUCTION to move real money.
     *
     * - Set to PayPalConfiguration.ENVIRONMENT_SANDBOX to use your test credentials
     * from https://developer.paypal.com
     *
     * - Set to PayPalConfiguration.ENVIRONMENT_NO_NETWORK to kick the tires
     * without communicating to PayPal's servers.
     */

    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_NO_NETWORK;

    // note that these credentials will differ between live & sandbox environments.
    private static final String CONFIG_CLIENT_ID = "AS43kxQznFJDM1czIi654wsxL6QxzsFQ51EtuaDkqwGZdKkwymYuOpN2WaaTC9lFlVcWDrLMCwq5kZom";

    private static final int REQUEST_CODE_PAYMENT = 1;
    private static final int REQUEST_CODE_FUTURE_PAYMENT = 2;
    private static final int REQUEST_CODE_PROFILE_SHARING = 3;

    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(CONFIG_ENVIRONMENT)
            .clientId(CONFIG_CLIENT_ID)
            // The following are only used in PayPalFuturePaymentActivity.
            .merchantName("IndyGo")
            .merchantPrivacyPolicyUri(Uri.parse("https://www.example.com/privacy"))
            .merchantUserAgreementUri(Uri.parse("https://www.example.com/legal"));

    @SuppressLint("SetTextI18n")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_screen);

        ImageButton menu = findViewById(R.id.menubutton2);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PaymentScreen.this, HomeScreen.class);
                startActivity(i);
            }
        });

        Button back = findViewById(R.id.back_button);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PaymentScreen.this, PurchaseScreen.class);
                startActivity(i);
            }
        });

        double price = 0.00;
        String timeLength = "";
        String type = "";

        switch(busTypePurchased) {
            case 1:
                // Daily
                price = 5.00;
                timeLength = "24 hours";
                type = "Daily";
                break;
            case 2:
                // Weekly
                price = 25.00;
                timeLength = "7 days";
                type = "Weekly";
                break;
            case 3:
                // Monthly
                price = 40.00;
                timeLength = "30 days";
                type = "Monthly";
                break;
            default:
                System.out.println("No Pass Specified, Returning to Main Menu");
                Intent i = new Intent(PaymentScreen.this, HomeScreen.class);
                startActivity(i);
        }

        TextView priceText = findViewById(R.id.pricedesc);
        TextView passType = findViewById(R.id.passtype);
        TextView indyGo = findViewById(R.id.indygo_desc);
        Button pay = findViewById(R.id.purchase_button);

        priceText.setText("Price: $" + price +"0");
        passType.setText(type);
        indyGo.setText("This pass we will be valid for " + timeLength + " in the IndyGo bus system upon purchase.");

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PaymentScreen.this, PayPalService.class);
                intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
                startService(intent);
                onBuyPressed();
            }
        });

    }

    public void onBuyPressed() {

        PayPalPayment thingToBuy = getPassToBuy(PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(PaymentScreen.this, PaymentActivity.class);

        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);

        startActivityForResult(intent, REQUEST_CODE_PAYMENT);
    }

    private PayPalPayment getPassToBuy(String paymentIntent) {
        switch(busTypePurchased) {
            case 1:
                // Daily
                return new PayPalPayment(new BigDecimal("5.00"), "USD", "Daily Pass", paymentIntent);

            case 2:
                // Weekly
                return new PayPalPayment(new BigDecimal("25.00"), "USD", "Weekly Pass", paymentIntent);

            case 3:
                // Monthly
                return new PayPalPayment(new BigDecimal("40.00"), "USD", "Monthly Pass", paymentIntent);

            default:
                System.out.println("No Pass Specified, Returning to Main Menu");
                Intent i = new Intent(PaymentScreen.this, HomeScreen.class);
                startActivity(i);
        }

        return null;
    }

    private PayPalOAuthScopes getOauthScopes() {
        /* create the set of required scopes
         * Note: see https://developer.paypal.com/docs/integration/direct/identity/attributes/ for mapping between the
         * attributes you select for this app in the PayPal developer portal and the scopes required here.
         */
        Set<String> scopes = new HashSet<String>(
                Arrays.asList(PayPalOAuthScopes.PAYPAL_SCOPE_EMAIL, PayPalOAuthScopes.PAYPAL_SCOPE_ADDRESS) );
        return new PayPalOAuthScopes(scopes);
    }

    @SuppressLint("SetTextI18n")
    protected void displayResultText(String result) {
        ((TextView)findViewById(R.id.resultText)).setText("Result : " + result);
        Toast.makeText(
                getApplicationContext(),
                result, Toast.LENGTH_LONG)
                .show();

        findViewById(R.id.passtype).setVisibility(View.INVISIBLE);
        findViewById(R.id.pricedesc).setVisibility(View.INVISIBLE);
        findViewById(R.id.indygo_desc).setVisibility(View.INVISIBLE);
        findViewById(R.id.purchase_button).setVisibility(View.INVISIBLE);

        Button back = findViewById(R.id.back_button);
        back.setText("");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PaymentScreen.this, PassScreen.class);
                startActivity(i);
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm =
                        data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {
                        Log.i(TAG, confirm.toJSONObject().toString(4));
                        Log.i(TAG, confirm.getPayment().toJSONObject().toString(4));

                        paymentSucceedToDB();
                        displayResultText("PaymentConfirmation info received from PayPal");

                    } catch (JSONException e) {
                        Log.e(TAG, "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i(TAG, "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i(
                        TAG,
                        "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        }
    }

    private void paymentSucceedToDB() {

        // Create throw to database
        // Need to send that the user has bought pass
        switch(busTypePurchased) {
            case 1:
                // Daily

                break;

            case 2:
                // Weekly

                break;

            case 3:
                // Monthly

                break;

            default:
                System.out.println("No Pass Specified, Returning to Main Menu");
                Intent i = new Intent(PaymentScreen.this, HomeScreen.class);
                startActivity(i);
        }

    }


    @Override
    public void onDestroy() {
        // Stop service when done
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }
}

