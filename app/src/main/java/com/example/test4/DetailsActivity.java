package com.example.test4;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

public class DetailsActivity extends AppCompatActivity {

    TextView txtInput;
    Button btFinishActivity;
    WebView myWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        btFinishActivity = findViewById(R.id.bt_finish_details_activity);
        myWebView = findViewById(R.id.web_view);
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
            myWebView.loadUrl("https://www.google.com/search?q=" + value);
            WebSettings webSettings = myWebView.getSettings();
            webSettings.setJavaScriptEnabled(true);
        }
    }


}