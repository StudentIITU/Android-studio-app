package com.example.damirapplication.VerificationPart;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.damirapplication.HomePage.HomePage;
import com.example.damirapplication.databinding.ActivityNameSurnamePageBinding;
import com.example.damirapplication.utilities.Constants;
import com.example.damirapplication.utilities.PreferenceManager;
import com.google.firebase.firestore.FirebaseFirestore;


import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

public class NameSurnamePage extends AppCompatActivity {
    private ActivityNameSurnamePageBinding binding;
    private String encodedImage;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNameSurnamePageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        setListeners();
    }
    private void setListeners() {
        binding.submitButton.setOnClickListener(v -> {
            if (isValidNameSurnameDetails()) {
                signUp();
            }
        });
        binding.addImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("*/*");
            String[] mimeTypes = {"image/jpeg", "image/png"};
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            pickImage.launch(intent);
        });
    }
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void signUp() {
        loading(true);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        HashMap<String, Object> user = new HashMap<>();
        user.put(Constants.KEY_USER_ID, getIntent().getStringExtra("userId"));
        user.put(Constants.KEY_FIRST_NAME, binding.firstNameInput.getText().toString());
        user.put(Constants.KEY_LAST_NAME, binding.lastNameInput.getText().toString());
        user.put(Constants.KEY_IMAGE, encodedImage);

        db.collection(Constants.KEY_COLLECTION_USERS)
                .document(getIntent().getStringExtra("userId"))
                .set(user)
                .addOnSuccessListener(documentReference -> {
                    loading(false);
                    preferenceManager.putBoolean(Constants.KEY_IS_VERIFIED, true);
                    preferenceManager.putString(Constants.KEY_USER_ID, getIntent().getStringExtra("userId"));
                    preferenceManager.putString(Constants.KEY_FIRST_NAME, binding.firstNameInput.getText().toString());
                    preferenceManager.putString(Constants.KEY_LAST_NAME, binding.lastNameInput.getText().toString());
                    preferenceManager.putString(Constants.KEY_IMAGE, encodedImage);
                    Intent intent = new Intent(getApplicationContext(), HomePage.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                })
                .addOnFailureListener(e -> {
                    loading(false);
                    showToast(e.getMessage());
                });
    }

    private String encodedImage(Bitmap bitmap) {
        int previewWidth = 150;
        int previewHeight = bitmap.getHeight() * previewWidth / bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return android.util.Base64.encodeToString(bytes, android.util.Base64.DEFAULT);
    }

    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    if (result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        try {
                            InputStream inputStream = getContentResolver().openInputStream(imageUri);
                            Bitmap bitmap = android.graphics.BitmapFactory.decodeStream(inputStream);
                            binding.profileImage.setImageBitmap(bitmap);
                            binding.addImage.setVisibility(View.GONE);
                            encodedImage = encodedImage(bitmap);
                        } catch (FileNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
    );

    private Boolean isValidNameSurnameDetails() {
        if (encodedImage == null) {
            showToast("Please select a profile picture");
            return false;
        } else if (binding.firstNameInput.getText().toString().isEmpty()) {
            showToast("Please enter your first name");
            return false;
        } else if (binding.lastNameInput.getText().toString().isEmpty()) {
            showToast("Please enter your last name");
            return false;
        } else {
            return true;
        }

    }

    private void loading(Boolean isLoading) {
        if (isLoading) {
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.submitButton.setVisibility(View.GONE);
            binding.BottonBackground.setVisibility(View.GONE);
        } else {
            binding.progressBar.setVisibility(View.GONE);
            binding.submitButton.setVisibility(View.VISIBLE);
            binding.BottonBackground.setVisibility(View.GONE);
        }
    }
}
