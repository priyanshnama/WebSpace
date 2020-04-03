package com.androidteamiiitdmj.webspace;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

public class ReportActivity extends AppCompatActivity {

    private Button btn_select_image,btn_submit;
    private EditText txt_title,txt_description;
    private TextView txt_file_name;
    private RadioButton radio_report,radio_suggesstion;
    private ImageView img_file_image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        txt_title = findViewById(R.id.report_title);
        txt_description = findViewById(R.id.description);
        btn_select_image = findViewById(R.id.select_image);
        txt_file_name = findViewById(R.id.file_name);
        radio_report = findViewById(R.id.radio_report);
        radio_suggesstion = findViewById(R.id.radio_suggesstion);
        img_file_image = findViewById(R.id.file_image);
        btn_submit = findViewById(R.id.submit);

        txt_title.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
