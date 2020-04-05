package com.androidteamiiitdmj.webspace.ui.internet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import com.androidteamiiitdmj.webspace.R;

public class InternetFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        InternetViewModel internetViewModel = ViewModelProviders.of(this).get(InternetViewModel.class);
        View root = inflater.inflate(R.layout.fragment_internet, container, false);
        final TextView textView = root.findViewById(R.id.text_internet);
        internetViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }
}
