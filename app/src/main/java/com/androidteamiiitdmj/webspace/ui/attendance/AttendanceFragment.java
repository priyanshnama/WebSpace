package com.androidteamiiitdmj.webspace.ui.attendance;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidteamiiitdmj.webspace.R;

public class AttendanceFragment extends Fragment {

    private AttendanceViewModel attendanceViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        attendanceViewModel =
                ViewModelProviders.of(this).get(AttendanceViewModel.class);
        View root = inflater.inflate(R.layout.attendance_fragment, container, false);
        final TextView textView = root.findViewById(R.id.text_attendance);
        attendanceViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}
