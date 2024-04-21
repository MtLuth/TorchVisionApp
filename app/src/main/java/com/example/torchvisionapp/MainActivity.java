package com.example.torchvisionapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.torchvisionapp.databinding.ActivityMainBinding;
import com.example.torchvisionapp.model.Onboarding;
import com.example.torchvisionapp.view.OnboardingAdapter;
import com.example.torchvisionapp.viewmodel.MainActivityViewModel;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    ViewPager viewPager;
    Button btnNext;
    ArrayList<Onboarding> onboardings;
    ActivityMainBinding binding;
    MainActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        binding = DataBindingUtil.setContentView(
                this,
                R.layout.activity_main
        );

        btnNext = binding.btnNext;
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentItem = getItem();
                if (currentItem<2) {
                    viewPager.setCurrentItem(currentItem+1, true);
                }
                else {
                    Intent i = new Intent(getApplicationContext(), SignUpActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });

        viewModel = new ViewModelProvider(this)
                .get(MainActivityViewModel.class);
        
        getData();
    }

    private void getData() {
        onboardings = viewModel.getOnboardingList();
        displayOnSliderView();
    }

    private void displayOnSliderView() {
        viewPager = binding.viewPager;
        OnboardingAdapter adapter = new OnboardingAdapter(this, onboardings);
        viewPager.setAdapter(adapter);
    }

    private int getItem() {
       return viewPager.getCurrentItem();
    }
}