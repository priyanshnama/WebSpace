package com.androidteamiiitdmj.webspace;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {
    private Button btn_logout, updateButton;
    private GoogleSignInClient mGoogleSignInClient;
    private TextView txt_app_version;
    private Double app_version=0.00,latest_version;
    private static final int PERMISSION_STORAGE_CODE = 1000;
    private ProgressDialog progressDialog;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        updateButton = findViewById(R.id.updateButton);
        get_this_version();
        user = FirebaseAuth.getInstance().getCurrentUser();
        updateButton.setOnClickListener(v -> get_latest_version());

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
        if(user!=null) update_profile();
    }

    private void get_this_version() {
        txt_app_version = findViewById(R.id.app_version);
        String version = (String) txt_app_version.getText();
        app_version = Double.parseDouble(version);
    }

    private void update_profile() {
        TextView txt_profile_name = findViewById(R.id.profile_name);
        String profile_name = user.getDisplayName().toString();
        for (UserInfo userInfo : user.getProviderData()) {
            if (profile_name == null && userInfo.getDisplayName() != null) {
                profile_name = userInfo.getDisplayName();
            }
        }
        txt_profile_name.setText(profile_name);


        String profile_email = user.getEmail();
        String profile_image = Objects.requireNonNull(user.getPhotoUrl()).toString();
        ImageView imgProfilePic = findViewById(R.id.profile_image);
        TextView txt_profile_email = findViewById(R.id.profile_email);
        txt_profile_email.setText(profile_email);
        Glide.with(getApplicationContext()).load(profile_image)
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgProfilePic);
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

    private void get_latest_version() {
        progressDialog = new ProgressDialog(SettingsActivity.this);
        progressDialog.setTitle("Checking for updates");
        progressDialog.show();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("app_versions")
                .document("latest_release")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot data = task.getResult();
                        latest_version = (Double) data.getData().get("release_version");
                        Log.d("TAG","This app version is " + app_version + " and latest version is " + latest_version);
                        progressDialog.dismiss();
                        if(latest_version>app_version) {
                            download_update();
                            Toast.makeText(this, "Download Started", Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(this,"You have the Latest Version",Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Log.w("TAG", "Error getting documents.", task.getException());
                    }
                });
    }

    private void download_update() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permissions,PERMISSION_STORAGE_CODE);
            } else {
                startDownloading();
            }
        }
        else {
            startDownloading();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode) {
            case PERMISSION_STORAGE_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startDownloading();
                }
                else {
                    Toast.makeText(this,"Permission Denied",Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void startDownloading() {
        Log.d("TAG","Downloading update");
        String url = "https://bit.ly/webspaceapp";
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI |
                DownloadManager.Request.NETWORK_MOBILE);
        request.setDescription("Downloading update");
        request.setTitle("WebSpace.apk");

        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "WebSpace.apk");

        // get download service and enqueue file
        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
    }
}