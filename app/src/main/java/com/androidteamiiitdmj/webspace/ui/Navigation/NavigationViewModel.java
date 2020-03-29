package com.androidteamiiitdmj.webspace.ui.Navigation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class NavigationViewModel {
    private MutableLiveData<String> mText;

    public NavigationViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Navigation fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
