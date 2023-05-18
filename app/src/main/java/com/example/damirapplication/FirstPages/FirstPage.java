package com.example.damirapplication.FirstPages;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.damirapplication.R;
import com.example.damirapplication.VerificationPart.SendOTP_Activity;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class FirstPage extends AppCompatActivity {

    private OnboardingAdapter onboardingAdapter;
    private LinearLayout layoutOnBoardingIndicators;
    private MaterialButton buttonOnBoardingAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);

        layoutOnBoardingIndicators = findViewById(R.id.layoutOnBoardingIndicators);
        buttonOnBoardingAction = findViewById(R.id.buttonOnBoardingAction);

        setupOnboardingItems();

        ViewPager2 onboardingViewPager = findViewById(R.id.OnBoardingViewPager);
        onboardingViewPager.setAdapter(onboardingAdapter);

        setupOnboardingIndicators();
        setCurrentOnBoardingIndicator(0);

        onboardingViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentOnBoardingIndicator(position);
            }
        });

        buttonOnBoardingAction.setOnClickListener(v -> {
            if (onboardingViewPager.getCurrentItem() + 1 < onboardingAdapter.getItemCount()) {
                onboardingViewPager.setCurrentItem(onboardingViewPager.getCurrentItem() + 1);
            } else {
                // Navigate to the next activity
                startActivity(new Intent(getApplicationContext(), SendOTP_Activity.class));
                finish();
            }
        });
    }

    private void setupOnboardingItems() {
        List<OnboardingItem> onboardingItems = new ArrayList<>();

        OnboardingItem itemFirst = new OnboardingItem();
        itemFirst.setTitle("Your way to start messaging");
        itemFirst.setDescription("Our app is the best way to start messaging");
        itemFirst.setImage(R.drawable.girl_is_running_marathon);

        OnboardingItem itemSecond = new OnboardingItem();
        itemSecond.setTitle("Entertainment is here");
        itemSecond.setDescription("Start messaging with your friends and family and have fun");
        itemSecond.setImage(R.drawable.man_wearing_green_hoodie_using_phone);

        OnboardingItem itemThird = new OnboardingItem();
        itemThird.setTitle("Accessibility");
        itemThird.setDescription("The app is available for you 24/7");
        itemThird.setImage(R.drawable.girl_is_chatting);

        onboardingItems.add(itemFirst);
        onboardingItems.add(itemSecond);
        onboardingItems.add(itemThird);

        onboardingAdapter = new OnboardingAdapter(onboardingItems);
    }

    private void setupOnboardingIndicators() {
        ImageView[] indicators = new ImageView[onboardingAdapter.getItemCount()];
        LinearLayoutCompat.LayoutParams layoutParams = new LinearLayoutCompat.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(8, 0, 8, 0);
        for (int i = 0; i < indicators.length; i++) {
            indicators[i] = new ImageView(getApplicationContext());
            indicators[i].setImageDrawable(
                    ContextCompat.getDrawable(
                            getApplicationContext(),
                            R.drawable.onboarding_indicator_inactive
                    )
            );
            indicators[i].setLayoutParams(layoutParams);
            layoutOnBoardingIndicators.addView(indicators[i]);
        }
    }

    @SuppressLint("SetTextI18n")
    private void setCurrentOnBoardingIndicator(int index) {
        int childCount = layoutOnBoardingIndicators.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ImageView imageView = (ImageView) layoutOnBoardingIndicators.getChildAt(i);
            if (i == index) {
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(
                                getApplicationContext(),
                                R.drawable.onboarding_indicator_active
                        )
                );
            } else {
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(
                                getApplicationContext(),
                                R.drawable.onboarding_indicator_inactive
                        )
                );
            }
        }

        if (index == onboardingAdapter.getItemCount() - 1) {
            buttonOnBoardingAction.setText("Start");
        } else {
            buttonOnBoardingAction.setText("Next");
        }
    }
}