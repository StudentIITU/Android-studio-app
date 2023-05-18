package com.example.damirapplication.HomePage;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.Toast;


import com.example.damirapplication.VerificationPart.NameSurnamePage;
import com.example.damirapplication.VerificationPart.SendOTP_Activity;
import com.example.damirapplication.activities.UsersActivity;
import com.example.damirapplication.databinding.ActivityHomePageBinding;
import com.example.damirapplication.utilities.Constants;
import com.example.damirapplication.utilities.PreferenceManager;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;


public class HomePage extends AppCompatActivity {

    private ActivityHomePageBinding binding;

    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomePageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        loadUserDetails();
        getToken();
        setListeners();
    }

    private void setListeners() {
        binding.imageSignOut.setOnClickListener(v -> signOut());
        binding.FabNewChat.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), UsersActivity.class)));
    }

    private void loadUserDetails() {
        byte[] bytes = Base64.decode(preferenceManager.getString(Constants.KEY_IMAGE), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        binding.imageProfile.setImageBitmap(bitmap);
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void getToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnSuccessListener(this::updateToken)
                .addOnFailureListener(e -> showToast("Unable to get token"));
    }

    private void updateToken(String token) {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference =
                database.collection(Constants.KEY_COLLECTION_USERS)
                        .document(preferenceManager.getString(Constants.KEY_USER_ID));
        documentReference.update(Constants.KEY_FCM_TOKEN, token)
                .addOnFailureListener(e -> showToast("Unable to update token"));
    }

    private void signOut() {
        showToast("Signing out...");
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference =
                database.collection(Constants.KEY_COLLECTION_USERS)
                        .document(preferenceManager.getString(Constants.KEY_USER_ID));
        documentReference.update(Constants.KEY_FCM_TOKEN, null)
                .addOnSuccessListener(unused -> {
                    preferenceManager.clearPreferences();
                    startActivity(new Intent(getApplicationContext(), SendOTP_Activity.class));
                    finish();
                })
                .addOnFailureListener(e -> showToast("Unable to sign out"));
    }
}