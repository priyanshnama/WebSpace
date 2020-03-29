package com.androidteamiiitdmj.webspace;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainActivity extends AppCompatActivity {

    static final int GOOGLE_SIGN_IN=123;
    int SPLASH_TIME_OUT = 100;
    private FirebaseAuth mAuth;
    ProgressBar progressBar;
    GoogleSignInClient mGoogleSignInClient;
    GoogleSignInAccount account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseAnalytics.getInstance(this);
        Objects.requireNonNull(this.getSupportActionBar()).hide();

        new Handler().postDelayed(() -> {
            mAuth = FirebaseAuth.getInstance();
            if(mAuth.getCurrentUser() == null ) SignInGoogle();
            else open_home();
        },SPLASH_TIME_OUT);
    }

    void SignInGoogle(){
        progressBar = findViewById(R.id.progress_circular);
        account = null;
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions
                .Builder()
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
        Log.d("TAG","Google Signin Started");
        progressBar.setVisibility(View.VISIBLE);
        Intent signIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signIntent,GOOGLE_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GOOGLE_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                account = task.getResult(ApiException.class);
                assert account != null;
                boolean vaild_email = valid(account.getEmail());
                if (vaild_email) firebaseAuthWithGoogle(account);
                else
                {
                    account = null;
                    Log.d("TAG","Use Institute account");
                    mGoogleSignInClient.signOut().addOnCompleteListener(this,
                            newtask -> Toast.makeText(this,"Use Your Institute Account",Toast.LENGTH_LONG)
                                    .show());
                    SignInGoogle();
                }
            } catch (ApiException e) {
                Log.w("TAG", "Google sign in failed", e);
            }
        }
    }

    private boolean valid(String email) {
        Pattern p = Pattern.compile("\\w+(@iiitdmj.ac.in)");
        Matcher m = p.matcher(email);
        return m.find();
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d("TAG", "firebaseAuthWithGoogle:" + account.getId());
        Log.d("TAG", "firebaseAuthWithGoogle_email:" + account.getEmail());
        AuthCredential credential = GoogleAuthProvider
                .getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()){
                        Log.d("TAG","signin success");
                        open_home();
                    }
                    else{
                        Log.w("TAG","signin failure",task.getException());
                        Toast.makeText(this, "Signin Failed",Toast.LENGTH_SHORT)
                                .show();
                    }
                });
    }

    private void open_home(){
        Intent loginIntent = new Intent(MainActivity.this, HomeActivity.class);
        startActivity(loginIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        progressBar.setVisibility(View.INVISIBLE);
    }

    // to display banner ad
    {
        //AdView mAdView;
        //MobileAds.initialize(this, initializationStatus -> {
        //});
        //mAdView = findViewById(R.id.adView);
        //AdRequest adRequest = new AdRequest.Builder().build();
        //mAdView.loadAd(adRequest);
    }
}
