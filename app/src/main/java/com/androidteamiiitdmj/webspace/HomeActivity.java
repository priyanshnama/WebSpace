package com.androidteamiiitdmj.webspace;

import android.app.ActionBar;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.androidteamiiitdmj.webspace.ui.internet.InternetFragment;
import com.androidteamiiitdmj.webspace.ui.intranet.IntranetFragment;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class HomeActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private AppBarConfiguration mAppBarConfiguration;
    private Dialog panel;
    private FirebaseUser user;
    private String profile_image;
    private Button action;

    public HomeActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if(user==null) close_app();
        //View  btn_settings = findViewById
        // (R.id.action_settings);


        profile_image = Objects.requireNonNull(user.getPhotoUrl()).toString();
        ImageView imgProfilePic = findViewById(R.id.show_panel);
        Picasso.get().load(profile_image).into(imgProfilePic);

        action = findViewById(R.id.action);
        set_up_panel();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_internet, R.id.nav_intranet, R.id.nav_offline,R.id.nav_attendance)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navigationView, navController);

        findViewById(R.id.navigate).setOnClickListener(this::navigation);
        findViewById(R.id.contain_panel).setOnClickListener(v -> panel.show());

        //if( isConnectedToInternet())open_fragment_internet();
        //else open_offline();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void onActionClicked() {
        if((action.getAutofillHints()).toString()=="search")
            startActivity(new Intent(HomeActivity.this,Search.class));
        else
            addNewSubject();
    }

    private void addNewSubject() {
        Log.d("TAG","new subject added");
    }

    private void set_up_panel() {
        //set up panel
        panel = new Dialog(HomeActivity.this);
        panel.setContentView(R.layout.activity_panel);
        panel.setCancelable(true);
        Objects.requireNonNull(panel.getWindow()).setLayout(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);

        //there are a lot of settings, for panel, check them all out!
        panel.findViewById(R.id.btn_settings).setOnClickListener(this::open_settings);
        panel.findViewById(R.id.report).setOnClickListener(this::open_Report);

        String profile_name = Objects.requireNonNull(user).getDisplayName();
        TextView txt_profile_name = panel.findViewById(R.id.profile_name);
        for (UserInfo userInfo : user.getProviderData()) {
            if (profile_name == null && userInfo.getDisplayName() != null) {
                profile_name = userInfo.getDisplayName();
            }
        }
        txt_profile_name.setText(profile_name);

        String profile_email = Objects.requireNonNull(user).getEmail();
        TextView txt_profile_email = panel.findViewById(R.id.profile_email);
        txt_profile_email.setText(profile_email);

        ImageView imgProfilePicOfPanel = panel.findViewById(R.id.profile_image);
        Picasso.get().load(profile_image).into(imgProfilePicOfPanel);

        MobileAds.initialize(this, this::task);
        AdView panel_ad = panel.findViewById(R.id.panel_ad);
        AdRequest adRequest = new AdRequest.Builder().build();
        panel_ad.loadAd(adRequest);

        panel.show();
        panel.hide();
    }

    public static void removeOtherAddedFragments(@NonNull AppCompatActivity activity, @NonNull Fragment fragment) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        for (androidx.fragment.app.Fragment frag : fragmentManager.getFragments()) {
            if (frag != null && !frag.equals(fragment) && frag.isAdded()) {
                fragmentManager.beginTransaction().remove(frag).commit();
            }
        }
    }

    private void open_offline() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment,new IntranetFragment());
        fragmentTransaction.commit();
        Toast.makeText(this, "Not connected to internet", Toast.LENGTH_LONG).show();
    }

    private void open_fragment_internet() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment,new InternetFragment());
        fragmentTransaction.commit();
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
