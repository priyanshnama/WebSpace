package com.androidteamiiitdmj.webspace;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class ReportActivity extends AppCompatActivity {
    private EditText txt_title,txt_description;
    private TextView txt_status;
    private RadioButton radio_report,radio_suggestion;
    private String title, description, TAG, name, email;
    private FirebaseUser user;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        user = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
        txt_title = findViewById(R.id.report_title);
        txt_description = findViewById(R.id.description);
        txt_status = findViewById(R.id.status);
        radio_report = findViewById(R.id.radio_report);
        radio_suggestion = findViewById(R.id.radio_suggesstion);
        Button btn_submit = findViewById(R.id.submit);
        txt_title.setOnFocusChangeListener(this::validation);
        txt_description.setOnFocusChangeListener(this::validation);
        btn_submit.setOnClickListener(this::submit);
    }

    private void submit(View v) {
        title = txt_title.getText().toString();
        description = txt_description.getText().toString();
        if(title.equals(""))showSoftKeyboard(txt_title);
        else if(description.equals(""))showSoftKeyboard(txt_description);
        else{
            if (radio_report.isChecked()) TAG = "Reports";
            else if (radio_suggestion.isChecked()) TAG = "Suggestion";
            email = user.getEmail();
            name = user.getDisplayName();
            send_submission();
        }
    }

    private void send_submission() {
        Map<String, Object> data = new HashMap<>();
        data.put("email", email);
        data.put("name", name);
        data.put("Title", title);
        data.put("Description",description);
        firebaseFirestore.collection(TAG)
                .add(data)
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    Toast.makeText(this,"Thanks for Contributing",Toast.LENGTH_LONG).show();
                })
                .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));
    }

    private void validation(View v,boolean hasFocus) {
        if(!hasFocus) {
            if ((txt_title.getText().toString().equals("")) && (v.getId() == R.id.report_title))
                txt_status.setText(R.string.report_title_not_valid);
            else if ((txt_description.getText().toString().equals("")) && (v.getId() == R.id.description))
                txt_status.setText(R.string.report_description_not_valid);
        }
        if(hasFocus){
            if((txt_status.getText().toString().equals("Title can not be empty")) && v.getId()==R.id.report_title)
                txt_status.setText("");
            else if((txt_status.getText().toString().equals("Description can not empty")) && v.getId()==R.id.description)
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
            InputMethodManager inputMethodManager = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            assert inputMethodManager != null;
            inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }
}
