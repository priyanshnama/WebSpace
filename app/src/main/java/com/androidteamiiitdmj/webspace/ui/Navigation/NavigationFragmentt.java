package com.androidteamiiitdmj.webspace.ui.Navigation;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.androidteamiiitdmj.webspace.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;

public class NavigationFragmentt extends Activity {

    private FirebaseAuth mAuth;
    private NavigationViewModel NavigationViewModel;
    private TextView txt_profile_name,txt_profile_email;
    private ImageView imgProfilePic;
    String profile_name, profile_email, profile_image;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.nav_header_home, container, false);
        //Log.d("Tag","Profile name is loading ");
        update_profile(view);
        return view;
    }

    private void update_profile(View view) {
        mAuth = FirebaseAuth.getInstance();
        profile_name = mAuth.getCurrentUser().getDisplayName();
        profile_email = mAuth.getCurrentUser().getEmail();
        profile_image = mAuth.getCurrentUser().getPhotoUrl().toString();

        txt_profile_name = view.findViewById(R.id.profile_name);
        imgProfilePic = view.findViewById(R.id.profile_image);
        txt_profile_email = view.findViewById(R.id.profile_email);

        txt_profile_name.setText(profile_name);
        //Log.d("Tag","Profile name is "+profile_name);
        txt_profile_email.setText(profile_email);
        Glide.with(getApplicationContext()).load(profile_image)
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgProfilePic);
    }
}
