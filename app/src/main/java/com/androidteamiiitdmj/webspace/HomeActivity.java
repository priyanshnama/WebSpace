package com.androidteamiiitdmj.webspace;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class HomeActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser()==null) System.exit(0);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        View  btn_settings = findViewById(R.id.action_settings);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        load_ad();
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

    @Override
    public boolean onSupportNavigateUp() {
        update_profile();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void open_settings(MenuItem item) {
        Intent loginIntent = new Intent(HomeActivity.this, SettingsActivity.class);
        this.startActivity(loginIntent);
    }

    public void update_profile() {
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

    public void open_Report(MenuItem item) {
        Intent loginIntent = new Intent(HomeActivity.this, ReportActivity.class);
        this.startActivity(loginIntent);
    }
}
