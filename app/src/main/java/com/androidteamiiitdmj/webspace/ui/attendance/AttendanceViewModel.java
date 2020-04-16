package com.androidteamiiitdmj.webspace.ui.attendance;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AttendanceViewModel extends ViewModel {
    private MutableLiveData<String> mText;
    // TODO: Implement the ViewModel
    public AttendanceViewModel(){
        mText = new MutableLiveData<>();
        mText.setValue("ATTENDANCE");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
