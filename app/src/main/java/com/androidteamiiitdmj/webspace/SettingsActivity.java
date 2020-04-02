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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {
    private Button btn_logout, updateButton;
    private GoogleSignInClient mGoogleSignInClient;
    private TextView txt_app_version;
    private String  app_version, latest_version, file_name;
    private static final int PERMISSION_STORAGE_CODE = 1000;
    private ProgressDialog progressDialog;
    private FirebaseUser user;
    private StorageReference storageReference;
    private FirebaseStorage firebaseStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        updateButton = findViewById(R.id.updateButton);
        btn_logout = findViewById(R.id.logout);
        updateButton.setOnClickListener(v -> get_latest_version());
        btn_logout.setOnClickListener(v -> logout());

        get_this_version();
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null) update_profile();
    }

    private void get_this_version() {
        txt_app_version = findViewById(R.id.app_version);
        app_version = (String) txt_app_version.getText();
    }

    private void update_profile() {
        //TextView txt_profile_name = findViewById(R.id.profile_name);
        //String profile_name = user.getDisplayName().toString();
        //for (UserInfo userInfo : user.getProviderData()) {
          //  if (profile_name == null && userInfo.getDisplayName() != null) {
            //    profile_name = userInfo.getDisplayName();
            //}
        //}
        //txt_profile_name.setText(profile_name);


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
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions
                .Builder()
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
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
                        latest_version = (String) data.getData().get("release_version");
                        int comparison = stringCompare(latest_version, app_version);
                        Log.d("TAG","This app version is " + app_version + " and latest version is " + latest_version);
                        progressDialog.dismiss();
                        if(comparison>0) {
                            file_name = "WebSpace " + latest_version + ".apk";
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
        switch(requestCode) {
            case PERMISSION_STORAGE_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    downloading();
                }
                else {
                    Toast.makeText(this,"Permission Denied",Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void startDownloading() {
        Log.d("TAG","startDownloading function");
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        StorageReference pathRefrence = storageReference.child("apk releases/" + file_name);
        StorageReference gsRefrence = firebaseStorage.getReferenceFromUrl("gs://web-space-0.appspot.com/apk releases/" + file_name);
        StorageReference httpsReference = firebaseStorage.getReferenceFromUrl("https://firebasestorage.googleapis.com/v0/b/web-space-0.appspot.com/o/apk%20releases%2FWebSpace%20"+latest_version+".apk");
        httpsReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                //downloading(uri);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                failureFunction();
            }
        });

        //WebSpace 20.04.01.apk
        storageReference = storageReference.child("apk releases/WebSpace " + latest_version + ".apk");
        //downloading(Uri.parse(storageReference.getPath()));
    }

    public static int stringCompare(String str1, String str2)
    {
        int l1 = str1.length();
        int l2 = str2.length();
        int lmin = Math.min(l1, l2);

        for (int i = 0; i < lmin; i++) {
            int str1_ch = (int)str1.charAt(i);
            int str2_ch = (int)str2.charAt(i);

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

    private void failureFunction() {
        Log.d("TAG","Download Failed");
    }

    public void downloading(){
        String uri = "https://bit.ly/wespaceapp";
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(uri));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setAllowedOverRoaming(false);
        request.setTitle("Webspace.apk");
        request.setDescription("Downloading update");
        request.setVisibleInDownloadsUi(true);
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "WebSpace.apk");
        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
    }
}