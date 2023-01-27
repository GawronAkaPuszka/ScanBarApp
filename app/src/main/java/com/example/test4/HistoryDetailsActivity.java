package com.example.test4;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

public class HistoryDetailsActivity extends AppCompatActivity {
    private String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_details);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            code = extras.getString("ID_CODE");
            this.setTitle(code);
        } else {

        }
    }
}