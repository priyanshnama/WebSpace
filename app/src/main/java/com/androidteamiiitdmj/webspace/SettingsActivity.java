package com.androidteamiiitdmj.webspace;

import android.Manifest;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {
    private String  app_version;
    private String latest_version;
    private static final int PERMISSION_STORAGE_CODE = 1000;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Button updateButton = findViewById(R.id.updateButton);
        updateButton.setOnClickListener(this::get_latest_version);
        get_this_version();
    }

    private void get_this_version() {
        TextView txt_app_version = findViewById(R.id.app_version);
        app_version = (String) txt_app_version.getText();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void get_latest_version(View v) {
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
                        assert data != null;
                        latest_version = (String) Objects.requireNonNull(data.getData()).get("release_version");
                        assert latest_version != null;
                        int comparison = stringCompare(latest_version, app_version);
                        Log.d("TAG","This app version is " + app_version + " and latest version is " + latest_version);
                        progressDialog.dismiss();
                        if(comparison>0) {
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
                downloading();
            }
        }
        else {
            downloading();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_STORAGE_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                downloading();
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    public static int stringCompare(String str1, String str2)
    {
        int l1 = str1.length();
        int l2 = str2.length();
        int minimum = Math.min(l1, l2);

        for (int i = 0; i < minimum; i++) {
            int str1_ch = str1.charAt(i);
            int str2_ch = str2.charAt(i);

            if (str1_ch != str2_ch) {
                return str1_ch - str2_ch;
            }
        }
        if (l1 != l2) {
            return l1 - l2;
        }
        else {
            return 0;
        }
    }

    public void downloading(){
        String uri = "https://bit.ly/wespaceapp";
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(uri));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setAllowedOverRoaming(false);
        request.setTitle("WebSpace.apk");
        request.setDescription("Downloading update");
        request.setVisibleInDownloadsUi(true);
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "WebSpace.apk");
        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        assert manager != null;
        manager.enqueue(request);
    }
}