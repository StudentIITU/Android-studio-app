package com.example.damirapplication.VerificationPart;



import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.damirapplication.HomePage.HomePage;
import com.example.damirapplication.R;
import com.example.damirapplication.utilities.PreferenceManager;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;



public class SendOTP_Activity extends AppCompatActivity {

    private PreferenceManager preferenceManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_otp);
        final EditText input_mobile = findViewById(R.id.input_mobile);
        Button buttonGetOTP = findViewById(R.id.buttonGetOTP);

        final ProgressBar progressBar = findViewById(R.id.progressBar);

        buttonGetOTP.setOnClickListener(view -> {
            if (input_mobile.getText().toString().trim().isEmpty()) {
                // if the mobile number is empty
                // display an error message
                input_mobile.setError("Enter a valid mobile");
                input_mobile.requestFocus();
                return;
            }
            progressBar.setVisibility(View.VISIBLE);
            buttonGetOTP.setVisibility(View.INVISIBLE);


            // Get the selected country code from the CountryCodePicker view
            CountryCodePicker ccp = findViewById(R.id.ccp);
            String countryCode = ccp.getSelectedCountryCodeWithPlus();

            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            countryCode + input_mobile.getText().toString(),
                    60,
                    java.util.concurrent.TimeUnit.SECONDS,
                    SendOTP_Activity.this,
                    new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                        @Override
                        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                            progressBar.setVisibility(View.GONE);
                            buttonGetOTP.setVisibility(View.VISIBLE);
                            if (preferenceManager.isLoggedIn()) {
                                startActivity(new Intent(getApplicationContext(), HomePage.class));
                                finish();
                            }
                        }

                        @Override
                        public void onVerificationFailed(@NonNull FirebaseException e) {
                            progressBar.setVisibility(View.GONE);
                            buttonGetOTP.setVisibility(View.VISIBLE);
                            Toast.makeText(SendOTP_Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                            progressBar.setVisibility(View.GONE);
                            buttonGetOTP.setVisibility(View.VISIBLE);

                            Intent intent = new Intent(getApplicationContext(), VerifyOTP_Activity.class);
                            intent.putExtra("mobile", input_mobile.getText().toString());
                            intent.putExtra("verificationId", verificationId);
                            startActivity(intent);


                        }
                    });
        });

    }



}