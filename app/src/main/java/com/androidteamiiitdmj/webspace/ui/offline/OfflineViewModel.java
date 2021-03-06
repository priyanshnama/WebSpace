package com.androidteamiiitdmj.webspace.ui.offline;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class OfflineViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public OfflineViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("OFFLINE");
    }

    public LiveData<String> getText() {
        return mText;
    }
}