package com.example.torchvisionapp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.torchvisionapp.R;
import com.example.torchvisionapp.databinding.FragmentSettingsBinding;
import com.example.torchvisionapp.model.SettingItem;
import com.example.torchvisionapp.view.PrivacyListAdapter;
import com.example.torchvisionapp.view.SettingItemAdapter;

import java.util.ArrayList;

public class SettingFragment extends Fragment {
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        FragmentSettingsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(container.getContext()),
                R.layout.fragment_settings,
                container,
                false);
        loadSettingItem(binding);
        loadPrivacy(binding);
        return binding.getRoot();
    }

    private void loadPrivacy(FragmentSettingsBinding binding) {
        ArrayList<String> privacyList = new ArrayList<>();
        privacyList.add("Your photos, your decision! You decide when and if any photos is uploaded to any cloud services.");
        privacyList.add("Text recognition is processed on-device. No photos are ever uploaded for data processing. Everything happens right on your phone.");
        privacyList.add("We do not collect ANY information about you.");
        PrivacyListAdapter adapter =new PrivacyListAdapter(privacyList, getContext());
        binding.listPrivacy.setAdapter(adapter);
        binding.listPrivacy.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void loadSettingItem(FragmentSettingsBinding binding) {
        ArrayList<SettingItem> settingItemArrayList = new ArrayList<>();

        SettingItem item1 = new SettingItem(R.drawable.icon_user, "Sign In");
        settingItemArrayList.add(item1);

        SettingItem item2 = new SettingItem(R.drawable.icon_about_us, "About Us");
        settingItemArrayList.add(item2);

        SettingItem item3 = new SettingItem(R.drawable.icon_phone, "Contact Us");
        settingItemArrayList.add(item3);

        SettingItemAdapter adapter = new SettingItemAdapter(settingItemArrayList, getContext());
        binding.recyclerViewListSettingItem.setAdapter(adapter);
        binding.recyclerViewListSettingItem.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}
