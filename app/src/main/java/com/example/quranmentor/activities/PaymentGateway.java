package com.example.quranmentor.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.quranmentor.R;
import com.example.quranmentor.models.Payment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PaymentGateway extends AppCompatActivity {
    private EditText amountEditText, cardNumberEditText, expiryDateEditText, cvvEditText, cardHolderEditText;
    private AppCompatButton payButton;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_gateway);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Payments");

        // Initialize views
        amountEditText = findViewById(R.id.amountEditText);
        cardNumberEditText = findViewById(R.id.cardNumberEditText);
        expiryDateEditText = findViewById(R.id.expiryDateEditText);
        cvvEditText = findViewById(R.id.cvvEditText);
        cardHolderEditText = findViewById(R.id.cardHolderEditText);
        payButton = findViewById(R.id.payButton);

        // Set up TextWatcher to format card number
        cardNumberEditText.addTextChangedListener(new TextWatcher() {
            private static final char space = ' ';

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Remove spacing char
                if (s.length() > 0 && (s.length() % 5) == 0) {
                    final char c = s.charAt(s.length() - 1);
                    if (space == c) {
                        s.delete(s.length() - 1, s.length());
                    }
                }
                // Insert char where needed
                if (s.length() > 0 && (s.length() % 5) == 0) {
                    char c = s.charAt(s.length() - 1);
                    // Only if its a digit where there should be a space we insert a space
                    if (Character.isDigit(c) && TextUtils.split(s.toString(), String.valueOf(space)).length <= 3) {
                        s.insert(s.length() - 1, String.valueOf(space));
                    }
                }
            }
        });
        // Set up pay button click listener
        payButton.setOnClickListener(view -> savePaymentInfo());
    }

    private void savePaymentInfo() {
        String amount = amountEditText.getText().toString().trim();
        String cardNumber = cardNumberEditText.getText().toString().trim();
        String expiryDate = expiryDateEditText.getText().toString().trim();
        String cvv = cvvEditText.getText().toString().trim();
        String cardHolder = cardHolderEditText.getText().toString().trim();

        if (amount.isEmpty() || cardNumber.isEmpty() || expiryDate.isEmpty() || cvv.isEmpty() || cardHolder.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        if (cardNumber.length() != 19) {
            Toast.makeText(this, "Invalid card number", Toast.LENGTH_SHORT).show();
            return;
        }

        if (cvv.length() != 3) {
            Toast.makeText(this, "Invalid CVV number", Toast.LENGTH_SHORT).show();
            return;
        }
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();

            // Create a new payment object
            Payment payment = new Payment(amount, cardNumber, expiryDate, cvv, cardHolder, userId);

            // Save payment to the database
            mDatabase.child(userId).setValue(payment)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(PaymentGateway.this, "Payment saved successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(PaymentGateway.this, "Failed to save payment", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(PaymentGateway.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
        }
    }
}














//    private PaymentSheet paymentSheet;
//    private String customerID;
//    private String ephemeralKey;
//    private String clientSecret;
//
//    private static final String Secret_key = "sk_test_51PLANbEHKrtiZemdxhjTGm5gXj6NZA6etVOrgjwoMtLueQamekgzbNAi6a0gkIjXelVmzQk9HI9Scb7Np8LMBuW800KxXcUyzg";
//    private static final String Publishable_key = "pk_test_51PLANbEHKrtiZemdtqYtrlsWbrCQt80mNPFT6CFH2yKTxP9yNw2asTdbODB5r6r5BzHqztXCB6UDZKSCaPSYndsu00jnmq80se";
//// Initialize Stripe
//        PaymentConfiguration.init(this, Publishable_key);
//
//// Initialize PaymentSheet
//paymentSheet = new PaymentSheet(this, this::onPaymentResult);
//
//// Create customer
//createCustomer();
//private void createCustomer() {
//    StringRequest request = new StringRequest(Request.Method.POST, "https://api.stripe.com/v1/customers", response -> {
//        try {
//            JSONObject object = new JSONObject(response);
//            customerID = object.getString("id");
//            Toast.makeText(PaymentGateway.this, "Customer Created: " + customerID, Toast.LENGTH_SHORT).show();
//            Log.d("StripeResponse", "Customer Created: " + response); // Log response
//            getEphemeralKey(customerID);
//        } catch (JSONException e) {
//            Log.e("StripeError", "Failed to parse customer response", e);
//        }
//    }, error -> {
//        Log.e("StripeError", "Failed to create customer", error);
//        Toast.makeText(PaymentGateway.this, "Failed to create customer", Toast.LENGTH_SHORT).show();
//    }) {
//        @Override
//        public Map<String, String> getHeaders() throws AuthFailureError {
//            Map<String, String> header = new HashMap<>();
//            header.put("Authorization", "Bearer " + Secret_key);
//            return header;
//        }
//    };
//    RequestQueue requestQueue = Volley.newRequestQueue(PaymentGateway.this);
//    requestQueue.add(request);
//}
//
//private void getEphemeralKey(String customerID) {
//    StringRequest request = new StringRequest(Request.Method.POST, "https://api.stripe.com/v1/ephemeral_keys", response -> {
//        try {
//            JSONObject object = new JSONObject(response);
//            ephemeralKey = object.getString("id");
//            Toast.makeText(PaymentGateway.this, "Ephemeral Key Created: " + ephemeralKey, Toast.LENGTH_SHORT).show();
//            Log.d("StripeResponse", "Ephemeral Key Created: " + response); // Log response
//            getClientSecret(customerID, ephemeralKey);
//        } catch (JSONException e) {
//            Log.e("StripeError", "Failed to parse ephemeral key response", e);
//        }
//    }, error -> {
//        Log.e("StripeError", "Failed to create ephemeral key", error);
//        Toast.makeText(PaymentGateway.this, "Failed to create ephemeral key", Toast.LENGTH_SHORT).show();
//    }) {
//        @Override
//        public Map<String, String> getHeaders() throws AuthFailureError {
//            Map<String, String> header = new HashMap<>();
//            header.put("Authorization", "Bearer " + Secret_key);
//            header.put("Stripe-Version", "2024-04-10");
//            return header;
//        }
//
//        @Nullable
//        @Override
//        protected Map<String, String> getParams() throws AuthFailureError {
//            Map<String, String> params = new HashMap<>();
//            params.put("customer", customerID);
//            return params;
//        }
//    };
//    RequestQueue requestQueue = Volley.newRequestQueue(PaymentGateway.this);
//    requestQueue.add(request);
//}
//
//private void getClientSecret(String customerID, String ephemeralKey) {
//    StringRequest request = new StringRequest(Request.Method.POST, "https://api.stripe.com/v1/payment_intents", response -> {
//        try {
//            JSONObject object = new JSONObject(response);
//            clientSecret = object.getString("client_secret");
//            Toast.makeText(PaymentGateway.this, "Client Secret Created: " + clientSecret, Toast.LENGTH_SHORT).show();
//            Log.d("StripeResponse", "Client Secret Created: " + response); // Log response
//            paymentFlow();
//        } catch (JSONException e) {
//            Log.e("StripeError", "Failed to parse payment intent response", e);
//        }
//    }, error -> {
//        Log.e("StripeError", "Failed to create payment intent", error);
//        Toast.makeText(PaymentGateway.this, "Failed to create payment intent", Toast.LENGTH_SHORT).show();
//    }) {
//        @Override
//        public Map<String, String> getHeaders() throws AuthFailureError {
//            Map<String, String> header = new HashMap<>();
//            header.put("Authorization", "Bearer " + Secret_key);
//            return header;
//        }
//
//        @Nullable
//        @Override
//        protected Map<String, String> getParams() throws AuthFailureError {
//            Map<String, String> params = new HashMap<>();
//            params.put("customer", customerID);
//            params.put("amount", "1000"); // Ensure this is correct for the amount you want to charge
//            params.put("currency", "usd");
//            params.put("automatic_payment_methods[enabled]", "true");
//            return params;
//        }
//    };
//    RequestQueue requestQueue = Volley.newRequestQueue(PaymentGateway.this);
//    requestQueue.add(request);
//}
//
//private void paymentFlow() {
//    paymentSheet.presentWithPaymentIntent(clientSecret, new PaymentSheet.Configuration("Muhammad Usman",
//            new PaymentSheet.CustomerConfiguration(customerID, ephemeralKey)));
//}
//
//private void onPaymentResult(PaymentSheetResult paymentSheetResult) {
//    if (paymentSheetResult instanceof PaymentSheetResult.Completed) {
//        Toast.makeText(this, "Payment Successful", Toast.LENGTH_SHORT).show();
//    } else if (paymentSheetResult instanceof PaymentSheetResult.Canceled) {
//        Toast.makeText(this, "Payment Canceled", Toast.LENGTH_SHORT).show();
//        Log.e("PaymentResult", "Payment Canceled");
//    } else if (paymentSheetResult instanceof PaymentSheetResult.Failed) {
//        PaymentSheetResult.Failed result = (PaymentSheetResult.Failed) paymentSheetResult;
//        Toast.makeText(this, "Payment Failed: " + result.getError().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
//        Log.e("PaymentResult", "Payment Failed: " + result.getError().toString());
//    }
//}
