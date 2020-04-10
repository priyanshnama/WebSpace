package com.androidteamiiitdmj.webspace;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class HomeActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private AppBarConfiguration mAppBarConfiguration;
    private View panel;
    private EditText search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser()==null) close_app();
        //View  btn_settings = findViewById
        // (R.id.action_settings);

        String profile_image = Objects.requireNonNull(mAuth.getCurrentUser().getPhotoUrl()).toString();
        ImageView imgProfilePic = findViewById(R.id.show_panel);
        Glide.with(getApplicationContext()).load(profile_image)
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgProfilePic);

        FloatingActionButton fab = findViewById(R.id.search);
        fab.setOnClickListener(this::search);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_internet, R.id.nav_intranet, R.id.nav_offline)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navigationView, navController);
        findViewById(R.id.navigate).setOnClickListener(this::navigation);
        findViewById(R.id.contain_panel).setOnClickListener(this::show_panel);

    }

    private void show_panel(View view){
        //set up panel
        Dialog panel = new Dialog(HomeActivity.this);
        panel.setContentView(R.layout.activity_panel);
        panel.setTitle("This is my custom panel box");
        panel.setCancelable(true);
        Objects.requireNonNull(panel.getWindow()).setLayout(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);

        //there are a lot of settings, for panel, check them all out!
        panel.findViewById(R.id.btn_settings).setOnClickListener(this::open_settings);
        panel.findViewById(R.id.report).setOnClickListener(this::open_Report);
        panel.findViewById(R.id.btn_logout).setOnClickListener(this::logout);
        String profile_email = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();
        TextView txt_profile_email = panel.findViewById(R.id.profile_email);
        txt_profile_email.setText(profile_email);
        String profile_image = Objects.requireNonNull(mAuth.getCurrentUser().getPhotoUrl()).toString();
        ImageView imgProfilePic = panel.findViewById(R.id.profile_image);
        Glide.with(getApplicationContext()).load(profile_image)
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgProfilePic);

        //now that the panel is set up, it's time to show it
        panel.show();
    }

    private void navigation(View view) {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        if (!NavigationUI.navigateUp(navController, mAppBarConfiguration)) {
            super.onSupportNavigateUp();
        }
    }

    private void logout(View view) {
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions
                .Builder()
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
        FirebaseAuth.getInstance().signOut();
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                task -> Toast.makeText(this,"Signed out",Toast.LENGTH_LONG)
                        .show());

    }

    private void something(View v) {
        Log.d("TAG","its working");
    }

    private void search(View view) {

    }

    private void close_app() {
        finish();
        System.exit(0);
    }

    private void load_ad() {
        //AdView mAdView;
        //MobileAds.initialize(this, initializationStatus -> {
        //});
        //mAdView = findViewById(R.id.adView);
        //AdRequest adRequest = new AdRequest.Builder().build();
        //mAdView.loadAd(adRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    public void open_settings(View view) {
        Intent settingsIntent = new Intent(HomeActivity.this, SettingsActivity.class);
        this.startActivity(settingsIntent);
    }

    public void open_Report(View view) {
        Intent reportIntent = new Intent(HomeActivity.this, ReportActivity.class);
        this.startActivity(reportIntent);
    }


}
