package com.example.test4;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;

public class DetailsActivity extends AppCompatActivity {

    TextInputEditText txtInput;
    Button btFinishActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        btFinishActivity = findViewById(R.id.bt_finish_details_activity);
        btFinishActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("ID_CODE");
            txtInput = findViewById(R.id.txtInput);
            txtInput.setText(value);
            //The key argument here must match that used in the other activity
        }
    }


}