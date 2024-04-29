package com.example.torchvisionapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.torchvisionapp.databinding.ActivityMainScreenAppBinding;
import com.example.torchvisionapp.fragment.FolderFragment;
import com.example.torchvisionapp.fragment.HeaderFragment;
import com.example.torchvisionapp.fragment.HomeFragment;
import com.example.torchvisionapp.fragment.SettingFragment;
import com.example.torchvisionapp.viewmodel.MainScreenActivityViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainScreenAppActivity extends AppCompatActivity implements OnFragmentClick
{
    ActivityMainScreenAppBinding binding;
    BottomNavigationView navigationView;
    MainScreenActivityViewModel viewModel;
    String rootPath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen_app);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main_screen_app);

        navigationView = binding.bottomNavigation;

        viewModel = new ViewModelProvider(this)
                .get(MainScreenActivityViewModel.class);
        navigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                loadFragmentByItemSelected(item);
                return true;
            }
        });
        rootPath = this.getFilesDir().getPath();
        HomeFragment homeFragment = new HomeFragment();
        homeFragment.setOnFragmentClick(this);
        loadFragment(new HeaderFragment("HOME"), homeFragment);
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
            HomeFragment homeFragment = new HomeFragment();
            homeFragment.setOnFragmentClick(this);
            loadFragment(new HeaderFragment("HOME"), homeFragment);
        }
        if (item.getItemId() == R.id.navMyFolder) {
            FolderFragment folderFragment = new FolderFragment();
            folderFragment.setOpenFolderOnFragment(this);
            folderFragment.path = rootPath;
            loadFragment(new HeaderFragment("MY FOLDER"), folderFragment);
        }
        if (item.getItemId() == R.id.navSetting) {
            loadFragment(new HeaderFragment("SETTINGS"), new SettingFragment());
        }
    }

    @Override
    public void openFolder(String path, String folderName) {
        FolderFragment fragmentMyFolder = new FolderFragment();
        fragmentMyFolder.path = path;
        HeaderFragment headerFragment = new HeaderFragment(folderName);
        headerFragment.previousPath = rootPath;
        headerFragment.setOnFragmentClick(this);
        loadFragment(headerFragment, fragmentMyFolder);
    }

    @Override
    public void back(String previousPath) {
        FolderFragment fragmentMyFolder = new FolderFragment();
        fragmentMyFolder.path = previousPath;
        fragmentMyFolder.setOpenFolderOnFragment(this);
        HeaderFragment headerFragment = new HeaderFragment("MY FOLDERS");
        headerFragment.previousPath = "";
        loadFragment(headerFragment, fragmentMyFolder);
    }
}