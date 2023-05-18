package com.example.damirapplication.FirstPages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.damirapplication.R;

import java.util.List;

public class OnboardingAdapter extends RecyclerView.Adapter<OnboardingAdapter.OnboardingHolder> {

    private final List<OnboardingItem> onboardingItems;

    public OnboardingAdapter(List<OnboardingItem> onboardingItems) {
        this.onboardingItems = onboardingItems;
    }

    @NonNull
    @Override
    public OnboardingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OnboardingHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_container_onboarding,
                        parent,
                        false
                ));
    }

    @Override
    public void onBindViewHolder(@NonNull OnboardingHolder holder, int position) {
        holder.setData(onboardingItems.get(position));
    }

    @Override
    public int getItemCount() {
        return onboardingItems.size();
    }

    class OnboardingHolder extends RecyclerView.ViewHolder {
        private TextView textTitle;
        private TextView description;
        private ImageView onboardingImage;

        public OnboardingHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.textTitle);
            description = itemView.findViewById(R.id.textDescription);
            onboardingImage = itemView.findViewById(R.id.imageOnboarding);
        }

        void setData(OnboardingItem onboardingItem) {
            textTitle.setText(onboardingItem.getTitle());
            description.setText(onboardingItem.getDescription());
            onboardingImage.setImageResource(onboardingItem.getImage());
        }
    }
}