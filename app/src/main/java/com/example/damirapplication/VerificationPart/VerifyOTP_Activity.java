package com.example.damirapplication.VerificationPart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.damirapplication.R;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import java.util.Objects;


public class VerifyOTP_Activity extends AppCompatActivity {



    private EditText inputCode1, inputCode2, inputCode3, inputCode4, inputCode5, inputCode6;

    private String verificationId;
    private long resendTimer = 0;
    private TextView textResend;


    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);
        TextView textMobile = findViewById(R.id.TextMobile);
        String mobileNumber = getIntent().getStringExtra("mobile");
        textMobile.setText(String.format("+7-%s", mobileNumber));


        inputCode1 = findViewById(R.id.firstPole);
        inputCode2 = findViewById(R.id.secondPole);
        inputCode3 = findViewById(R.id.thirdPole);
        inputCode4 = findViewById(R.id.fourthPole);
        inputCode5 = findViewById(R.id.fifthPole);
        inputCode6 = findViewById(R.id.sixthPole);
        SetupOTPInputs();

        final ProgressBar progressBar = findViewById(R.id.progressBar);
        final Button buttonVerify = findViewById(R.id.buttonVerifyOTP);

        verificationId = getIntent().getStringExtra("verificationId");

        buttonVerify.setOnClickListener(view -> {
            if (inputCode1.getText().toString().trim().isEmpty()
                    || inputCode2.getText().toString().trim().isEmpty()
                    || inputCode3.getText().toString().trim().isEmpty()
                    || inputCode4.getText().toString().trim().isEmpty()
                    || inputCode5.getText().toString().trim().isEmpty()
                    || inputCode6.getText().toString().trim().isEmpty()) {
                Toast.makeText(VerifyOTP_Activity.this, "Please enter valid code", Toast.LENGTH_SHORT).show();
                return;
            }

            String code = inputCode1.getText().toString() +
                    inputCode2.getText().toString() +
                    inputCode3.getText().toString() +
                    inputCode4.getText().toString() +
                    inputCode5.getText().toString() +
                    inputCode6.getText().toString();
            if (verificationId != null) {
                progressBar.setVisibility(View.VISIBLE);
                buttonVerify.setVisibility(View.INVISIBLE);
                PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(
                        verificationId,
                        code
                );
                FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                        .addOnCompleteListener(task -> {
                            progressBar.setVisibility(View.GONE);
                            buttonVerify.setVisibility(View.VISIBLE);
                            if (task.isSuccessful()) {
                                String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
                                Intent intent = new Intent(getApplicationContext(), NameSurnamePage.class);
                                intent.putExtra("userId", userId);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            } else {
                                Toast.makeText(VerifyOTP_Activity.this, "The verification code entered was invalid", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        textResend = findViewById(R.id.ResendOTP);
        textResend.setOnClickListener(v -> {
            if (resendTimer == 0) {
                // Start the resend timer
                resendTimer = 60;
                new Thread(() -> {
                    while (resendTimer > 0) {
                        runOnUiThread(() -> textResend.setText(String.format("Resend OTP (%d)", resendTimer)));
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        resendTimer--;
                    }
                    runOnUiThread(() -> {
                        textResend.setText("Resend OTP");
                        textResend.setClickable(true);
                    });
                }).start();

                // Resend the OTP
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "+7" + getIntent().getStringExtra("mobile"),
                        60,
                        java.util.concurrent.TimeUnit.SECONDS,
                        VerifyOTP_Activity.this,
                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(VerifyOTP_Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String newVerificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                verificationId = newVerificationId;
                                Toast.makeText(VerifyOTP_Activity.this, "OTP Sent", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        resendTimer = 0;
    }


    private void SetupOTPInputs() {
        inputCode1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty())
                    inputCode2.requestFocus();

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        inputCode2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty())
                    inputCode3.requestFocus();

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        inputCode3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty())
                    inputCode4.requestFocus();

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        inputCode4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty())
                    inputCode5.requestFocus();

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        inputCode5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty())
                    inputCode6.requestFocus();

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }


}