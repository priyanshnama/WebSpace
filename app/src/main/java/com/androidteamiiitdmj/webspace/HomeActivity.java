package com.androidteamiiitdmj.webspace;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
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
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserInfo;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class HomeActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private AppBarConfiguration mAppBarConfiguration;

    public HomeActivity() {
    }

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
        Picasso.get().load(profile_image).into(imgProfilePic);

        findViewById(R.id.search).setOnClickListener(v->startActivity(new Intent(HomeActivity.this,Search.class)));





        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_internet, R.id.nav_intranet, R.id.nav_offline,R.id.nav_attendance)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navigationView, navController);






        findViewById(R.id.navigate).setOnClickListener(this::navigation);
        findViewById(R.id.contain_panel).setOnClickListener(this::show_panel);

        if( isConnectedToInternet())open_fragment_internet();
        else open_offline();
    }

    private void open_offline() {
        Toast.makeText(this, "Not connected to internet", Toast.LENGTH_LONG).show();
    }

    private void open_fragment_internet() {
        Toast.makeText(this, "Connected to internet", Toast.LENGTH_LONG).show();
    }

    private boolean isConnectedToInternet() {
        try{
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = null;
            if(connectivityManager!=null)networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo!=null && networkInfo.isConnected();
        }
        catch (Exception e){
            return false;
        }
    }

    private void show_panel(View view){
        //set up panel
        Dialog panel = new Dialog(HomeActivity.this);
        panel.setContentView(R.layout.activity_panel);
        panel.setCancelable(true);
        Objects.requireNonNull(panel.getWindow()).setLayout(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);

        //there are a lot of settings, for panel, check them all out!
        panel.findViewById(R.id.btn_settings).setOnClickListener(this::open_settings);
        panel.findViewById(R.id.report).setOnClickListener(this::open_Report);

        String profile_name = Objects.requireNonNull(mAuth.getCurrentUser()).getDisplayName();
        TextView txt_profile_name = panel.findViewById(R.id.profile_name);
        for (UserInfo userInfo : mAuth.getCurrentUser().getProviderData()) {
            if (profile_name == null && userInfo.getDisplayName() != null) {
                profile_name = userInfo.getDisplayName();
            }
        }
        txt_profile_name.setText(profile_name);

        String profile_email = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();
        TextView txt_profile_email = panel.findViewById(R.id.profile_email);
        txt_profile_email.setText(profile_email);

        String profile_image = Objects.requireNonNull(mAuth.getCurrentUser().getPhotoUrl()).toString();
        ImageView imgProfilePic = panel.findViewById(R.id.profile_image);
        Picasso.get().load(profile_image).into(imgProfilePic);

        //now that the panel is set up, it's time to show it
        panel.show();
        MobileAds.initialize(this, this::task);
        AdView panel_ad = panel.findViewById(R.id.panel_ad);
        AdRequest adRequest = new AdRequest.Builder().build();
        panel_ad.loadAd(adRequest);
    }

    private void task(InitializationStatus initializationStatus) {
    }


    private void navigation(View view) {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        if (!NavigationUI.navigateUp(navController, mAppBarConfiguration)) {
            super.onSupportNavigateUp();
        }
    }

    private void close_app() {
        finish();
        System.exit(0);
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
