package com.androidteamiiitdmj.webspace.ui.internet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import com.androidteamiiitdmj.webspace.R;

public class InternetFragment extends Fragment {

    private InternetViewModel internetViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        internetViewModel =
                ViewModelProviders.of(this).get(InternetViewModel.class);
        View root = inflater.inflate(R.layout.fragment_internet, container, false);
        final TextView textView = root.findViewById(R.id.text_internet);
        internetViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}
