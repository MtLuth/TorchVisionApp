package com.example.torchvisionapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.example.torchvisionapp.databinding.ActivityMainScreenAppBinding;
import com.example.torchvisionapp.fragment.HeaderFragment;
import com.example.torchvisionapp.fragment.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainScreenAppActivity extends AppCompatActivity {
    ActivityMainScreenAppBinding binding;
    BottomNavigationView navigationView;
    FrameLayout frameLayout;

    FrameLayout headerFrame;
    HomeFragment homeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen_app);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main_screen_app);

        navigationView = binding.bottomNavigation;

        headerFrame = binding.frameHeader;
        loadFragment(new HeaderFragment("HOME"), new HomeFragment());
    }

    public void loadFragment(HeaderFragment headerFragment, Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        ft.replace(R.id.frameLayout, fragment);
        ft.replace(R.id.frameHeader,headerFragment);
        ft.commit();
    }
}