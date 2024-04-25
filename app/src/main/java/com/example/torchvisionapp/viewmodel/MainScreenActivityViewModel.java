package com.example.torchvisionapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.AndroidViewModel;

import com.example.torchvisionapp.R;
import com.example.torchvisionapp.databinding.SettingItemBinding;
import com.example.torchvisionapp.model.SettingItem;

import java.util.ArrayList;

public class MainScreenActivityViewModel extends AndroidViewModel {
    ArrayList<SettingItem> settingItemArrayList = new ArrayList<>();
    ArrayList<String> privacyList = new ArrayList<>();
    public MainScreenActivityViewModel(@NonNull Application application) {
        super(application);
    }

    public ArrayList<SettingItem> loadSettingItemList() {
        SettingItem item1 = new SettingItem(R.drawable.icon_user, "Sign In");
        settingItemArrayList.add(item1);

        SettingItem item2 = new SettingItem(R.drawable.icon_about_us, "About Us");
        settingItemArrayList.add(item2);

        SettingItem item3 = new SettingItem(R.drawable.icon_phone, "Contact Us");
        settingItemArrayList.add(item3);

        return settingItemArrayList;
    }

    public ArrayList<String> loadPrivacyList() {
        privacyList.add("Your photos, your decision! You decide when and if any photos is uploaded to any cloud services.");
        privacyList.add("Text recognition is processed on-device. No photos are ever uploaded for data processing. Everything happens right on your phone.");
        privacyList.add("We do not collect ANY information about you.");
        return privacyList;
    }
}
