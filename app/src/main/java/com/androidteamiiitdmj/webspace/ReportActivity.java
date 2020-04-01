package com.androidteamiiitdmj.webspace;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.Objects;

public class ReportActivity extends AppCompatActivity {

    private EditText txt_title, txt_description;
    private TextView file_name;
    private Button upload_image, submit;
    private RadioButton radio_report, radio_suggestion;
    private ImageView file_image;
    private RadioGroup radioGroup;
    private StorageReference mStorageRef;
    private String title, description;
    private String type;
    private Uri img_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        mStorageRef = FirebaseStorage.getInstance().getReference();

        txt_title = findViewById(R.id.issue_title);
        txt_description = findViewById(R.id.description);
        file_name = findViewById(R.id.file_name);
        file_image = findViewById(R.id.file_image);
        upload_image = findViewById(R.id.upload_image);
        submit = findViewById(R.id.submit);
        radioGroup = findViewById(R.id.radioGroup);
        radio_report = findViewById(R.id.radio_report);
        radio_suggestion = findViewById(R.id.radio_suggestion);

        radio_suggestion.setChecked(true);
        upload_image.setOnClickListener(v -> uploadImage());
        submit.setOnClickListener(v -> submitReport());

        Toolbar toolbar = findViewById(R.id.toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            setActionBar(toolbar);

        }
    }

    private void submitReport() {
        imageUploader();
        prepare_data();

    }

    private void prepare_data() {
        if(radio_report.isChecked()) type = "REPORT";
        else type = "SUGGESTION";
        title = txt_title.getText().toString();
        description = txt_description.getText().toString();
    }

    private void imageUploader() {

    }

    private String getExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data.getData()!=null)
        {
            img_url = data.getData();
            File file = new File(Objects.requireNonNull(img_url.getPath()));
            file_image.setImageURI(img_url);
            file_name.setText(file.getName());
        }
    }

    private void uploadImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
