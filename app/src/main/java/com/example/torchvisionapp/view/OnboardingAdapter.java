package com.example.torchvisionapp.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.PagerAdapter;

import com.example.torchvisionapp.R;
import com.example.torchvisionapp.databinding.SliderLayoutBinding;
import com.example.torchvisionapp.model.Onboarding;

import java.util.ArrayList;

public class OnboardingAdapter extends PagerAdapter {

    private Context context;
    private ArrayList<Onboarding> onboardings;

    public OnboardingAdapter(Context context, ArrayList<Onboarding> onboardings) {
        this.context = context;
        this.onboardings = onboardings;
    }

    @Override
    public int getCount() {
        return onboardings.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (LinearLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        SliderLayoutBinding binding = DataBindingUtil
                .inflate(
                        LayoutInflater.from(container.getContext()),
                        R.layout.slider_layout,
                        container,
                        false
        );
        binding.setOnboarding(onboardings.get(position));
        container.addView(binding.getRoot());
        return binding.getRoot();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout) object);
    }
}
