package com.androidteamiiitdmj.webspace;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;

public class ReportActivity extends AppCompatActivity {

    private Button btn_select_image,btn_submit;
    private EditText txt_title,txt_description;
    private TextView txt_file_name, txt_status;
    private RadioButton radio_report,radio_suggesstion;
    private ImageView img_file_image;
    private String title, description, TAG, name, email;
    private File image_file;
    private FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        user = FirebaseAuth.getInstance().getCurrentUser();

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
        btn_submit.setOnClickListener(this::submit);
        btn_select_image.setOnClickListener(this::select_image);

    }

    private void select_image(View v) {
    }

    private void submit(View v) {
        title = txt_title.getText().toString();
        description = txt_description.getText().toString();
        if(title.equals(""))showSoftKeyboard(txt_title);
        else if(description.equals(""))showSoftKeyboard(txt_description);
        else{
            if (radio_report.isChecked()) TAG = "Report";
            else if (radio_suggesstion.isChecked()) TAG = "Suggestion";
            email = user.getEmail();
            name = user.getDisplayName();
        }
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

    public void showSoftKeyboard(View view) {
        view.requestFocus();
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }
}
