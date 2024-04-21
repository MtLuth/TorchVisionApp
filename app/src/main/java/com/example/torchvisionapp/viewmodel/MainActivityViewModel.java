package com.example.torchvisionapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.torchvisionapp.R;
import com.example.torchvisionapp.model.Onboarding;

import java.util.ArrayList;

public class MainActivityViewModel extends AndroidViewModel {

    ArrayList<Onboarding> onboardings = new ArrayList<>();
    public MainActivityViewModel(@NonNull Application application) {
        super(application);
    }

    public ArrayList<Onboarding> getOnboardingList() {
        Onboarding item1 = new Onboarding(R.drawable.onboarding_1, R.string.heading_onboarding_1, R.drawable.pager1);
        Onboarding item2 = new Onboarding(R.drawable.onboarding_2, R.string.heading_onboarding_2, R.drawable.pager2);
        Onboarding item3 = new Onboarding(R.drawable.onboarding_3, R.string.heading_onboarding_3, R.drawable.pager3);

        onboardings.add(item1);
        onboardings.add(item2);
        onboardings.add(item3);

        return onboardings;
    }
}
