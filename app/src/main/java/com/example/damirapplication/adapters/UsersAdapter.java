package com.example.damirapplication.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.damirapplication.databinding.ItemContainerUserBinding;
import com.example.damirapplication.listeners.UserListener;
import com.example.damirapplication.models.Users;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {

    private final List<Users> users;
    private final UserListener userListener;

    public UsersAdapter(List<Users> users, UserListener userListener) {
        this.users = users;
        this.userListener = userListener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemContainerUserBinding binding = ItemContainerUserBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new UserViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.SetUserData(users.get(position));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {

        ItemContainerUserBinding binding;

        UserViewHolder(ItemContainerUserBinding itemContainerUserBinding) {
            super(itemContainerUserBinding.getRoot());
            binding = itemContainerUserBinding;
        }

        void SetUserData(Users user) {
            binding.textName.setText(String.format(user.firstName, user.lastName));
            binding.imageProfile.setImageBitmap(getUserImage(user.profileImage));
            binding.getRoot().setOnClickListener(v -> userListener.onUserClicked(user));
        }

        private Bitmap getUserImage(String image) {
            byte[] bytes = Base64.decode(image, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        }
    }
}
