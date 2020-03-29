package com.androidteamiiitdmj.webspace.ui.intranet;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class IntranetViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public IntranetViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is intranet fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}