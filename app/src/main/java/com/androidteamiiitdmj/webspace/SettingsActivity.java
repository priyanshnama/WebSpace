package com.androidteamiiitdmj.webspace;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {
    Button btn_logout,btn_back;
    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();

        //for google authentication
        {
            GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions
                    .Builder()
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();
            mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
        }
        btn_logout = findViewById(R.id.logout);
        btn_logout.setOnClickListener(v -> logout());
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        update_profile();
    }

    private void update_profile() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        TextView txt_profile_name = findViewById(R.id.profile_name);
        //String profile_name = mAuth.getCurrentUser().getDisplayName().toString();
        //Log.d("TAG","profile name is "+profile_name);
        //txt_profile_name.setText(profile_name);


        String profile_email = mAuth.getCurrentUser().getEmail();
        String profile_image = Objects.requireNonNull(mAuth.getCurrentUser().getPhotoUrl()).toString();
        ImageView imgProfilePic = findViewById(R.id.profile_image);
        TextView txt_profile_email = findViewById(R.id.profile_email);
        txt_profile_email.setText(profile_email);
        Glide.with(getApplicationContext()).load(profile_image)
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgProfilePic);
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                task -> Toast.makeText(this,"Signed out",Toast.LENGTH_LONG)
                        .show());
        btn_logout.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onBackPressed() {
        int state = btn_logout.getVisibility();
        if(state==4) {
            finishAffinity();
            System.exit(0);
        }
        else {
            super.onBackPressed();
        }
    }
}