
package com.example.torchvisionapp.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.torchvisionapp.AboutUs;
import com.example.torchvisionapp.ContactUs;
import com.example.torchvisionapp.ItemClickListener;
import com.example.torchvisionapp.R;
import com.example.torchvisionapp.SignInActivity;
import com.example.torchvisionapp.databinding.FragmentSettingsBinding;
import com.example.torchvisionapp.model.SettingItem;
import com.example.torchvisionapp.view.PrivacyListAdapter;
import com.example.torchvisionapp.view.SettingItemAdapter;
import com.example.torchvisionapp.viewmodel.MainScreenActivityViewModel;

import java.util.ArrayList;

public class SettingFragment extends Fragment implements ItemClickListener {

    MainScreenActivityViewModel viewModel;
    ArrayList<SettingItem> settingItemArrayList = new ArrayList<>();
    ArrayList<String> privacyList = new ArrayList<>();
    SettingItemAdapter settingItemAdapter;
    private String email;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("user5", email+"");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(getActivity())
                .get(MainScreenActivityViewModel.class);
        FragmentSettingsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(container.getContext()),
                R.layout.fragment_settings,
                container,
                false);
        if (email.equals("")) {
            binding.txtUid.setVisibility(View.INVISIBLE);
        } else {
            binding.txtUid.setText(email);
        }
        loadSettingItem(binding);
        loadPrivacy(binding);
        return binding.getRoot();
    }

    private void loadPrivacy(FragmentSettingsBinding binding) {

        privacyList = viewModel.loadPrivacyList();

        PrivacyListAdapter adapter =new PrivacyListAdapter(privacyList, getContext());
        binding.listPrivacy.setAdapter(adapter);
        binding.listPrivacy.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void loadSettingItem(FragmentSettingsBinding binding) {

        settingItemArrayList = viewModel.loadSettingItemList();
        Log.i("email", email);
        if (!email.equals("")) {
            settingItemArrayList.get(0).setTitle("Sign out");
        }
        settingItemAdapter = new SettingItemAdapter(settingItemArrayList, getContext());
        settingItemAdapter.setClickListener(this);
        binding.recyclerViewListSettingItem.setAdapter(settingItemAdapter);
        binding.recyclerViewListSettingItem.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onSettingItemClick(View v, int pos) {
        if (pos == 0) {
            Intent i = new Intent(v.getContext(), SignInActivity.class);
            startActivity(i);
        }
        if (pos==1) {
            Intent i = new Intent(v.getContext(), AboutUs.class);
            startActivity(i);
        }
        if(pos==2){
            Intent i = new Intent(v.getContext(), ContactUs.class);
            startActivity(i);
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
