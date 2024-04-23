package com.example.torchvisionapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.torchvisionapp.databinding.ActivityMainScreenAppBinding;
import com.example.torchvisionapp.fragment.FolderFragment;
import com.example.torchvisionapp.fragment.HeaderFragment;
import com.example.torchvisionapp.fragment.HomeFragment;
import com.example.torchvisionapp.fragment.MyFavoriteFragment;
import com.example.torchvisionapp.fragment.SettingFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainScreenAppActivity extends AppCompatActivity {
    ActivityMainScreenAppBinding binding;
    BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen_app);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main_screen_app);

        navigationView = binding.bottomNavigation;
        navigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                loadFragmentByItemSelected(item);
                return true;
            }
        });

        loadFragment(new HeaderFragment("HOME"), new HomeFragment());
    }

    public void loadFragment(HeaderFragment headerFragment, Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frameLayout, fragment);
        ft.replace(R.id.frameHeader,headerFragment);
        ft.commit();
    }

    public void loadFragmentByItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.navHome) {
            loadFragment(new HeaderFragment("HOME"), new HomeFragment());
        }
        if (item.getItemId() == R.id.navMyFolder) {
            loadFragment(new HeaderFragment("MY FOLDER"), new FolderFragment());
        }
        if (item.getItemId() == R.id.navHeart) {
            loadFragment(new HeaderFragment("MY FAVORITE"), new MyFavoriteFragment());
        }
        if (item.getItemId() == R.id.navSetting) {
            loadFragment(new HeaderFragment("SETTINGS"), new SettingFragment());
        }
    }
}