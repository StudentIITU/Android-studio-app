package com.example.damirapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;


import com.example.damirapplication.adapters.UsersAdapter;
import com.example.damirapplication.databinding.ActivityUsersBinding;
import com.example.damirapplication.listeners.UserListener;
import com.example.damirapplication.models.Users;
import com.example.damirapplication.utilities.Constants;
import com.example.damirapplication.utilities.PreferenceManager;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class UsersActivity extends AppCompatActivity implements UserListener {

    private ActivityUsersBinding binding;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUsersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        getUsers();
        setListeners();
    }

    private void setListeners() {
        binding.imageBack.setOnClickListener(v -> onBackPressed());
    }

    private void getUsers() {
        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USERS)
                .get()
                .addOnCompleteListener(task -> {
                    loading(false);
                    String currentUserId = preferenceManager.getString(Constants.KEY_USER_ID);
                    if (task.isSuccessful() && task.getResult() != null) {
                        List<Users> list = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                            if (currentUserId.equals(documentSnapshot.getId())) {
                                continue;
                            }
                            Users user = new Users();
                            user.firstName = documentSnapshot.getString(Constants.KEY_FIRST_NAME);
                            user.lastName = documentSnapshot.getString(Constants.KEY_LAST_NAME);
                            user.profileImage = documentSnapshot.getString(Constants.KEY_IMAGE);
                            user.token = documentSnapshot.getString(Constants.KEY_FCM_TOKEN);
                            user.id = documentSnapshot.getId();
                            list.add(user);
                        }
                        if (list.size() > 0) {
                            UsersAdapter adapter = new UsersAdapter(list, this);
                            binding.recyclerView.setAdapter(adapter);
                            binding.recyclerView.setVisibility(View.VISIBLE);
                        } else {
                            ShowErrorMessage();
                        }
                    } else {
                        ShowErrorMessage();
                    }
                });
    }

    private void ShowErrorMessage() {
        binding.textViewError.setText(String.format("%s", "No user available"));
        binding.textViewError.setVisibility(View.VISIBLE);
    }

    private void loading(Boolean isLoading) {
        if (isLoading) {
            binding.progressBar.setVisibility(View.VISIBLE);
        } else {
            binding.progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onUserClicked(Users users) {
        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
        intent.putExtra(Constants.KEY_USER, users);
        startActivity(intent);
    }
}
