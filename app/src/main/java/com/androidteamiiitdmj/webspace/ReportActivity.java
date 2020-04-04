package com.androidteamiiitdmj.webspace;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class ReportActivity extends AppCompatActivity {

    private Button btn_select_image,btn_submit;
    private EditText txt_title,txt_description;
    private TextView txt_file_name, txt_status;
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
        txt_status = findViewById(R.id.status);
        txt_file_name = findViewById(R.id.file_name);
        radio_report = findViewById(R.id.radio_report);
        radio_suggesstion = findViewById(R.id.radio_suggesstion);
        img_file_image = findViewById(R.id.file_image);
        btn_submit = findViewById(R.id.submit);

        txt_title.setOnFocusChangeListener(this::validation);
        txt_description.setOnFocusChangeListener(this::validation);

    }

    private void validation(View v,boolean hasFocus) {
        if(!hasFocus) {
            if ((txt_title.getText().toString().equals("")) && (v.getId() == R.id.report_title))
                txt_status.setText(R.string.report_title_not_valid);
            else if ((txt_description.getText().toString().equals("")) && (v.getId() == R.id.description))
                txt_status.setText(R.string.report_description_not_valid);
        }
        if(hasFocus){
            if((txt_status.getText().toString().equals(R.string.report_title_not_valid)) && v.getId()==R.id.report_title)
                txt_status.setText("");
            else if((txt_status.getText().toString().equals(R.string.report_description_not_valid)) && v.getId()==R.id.description)
                txt_status.setText("");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
