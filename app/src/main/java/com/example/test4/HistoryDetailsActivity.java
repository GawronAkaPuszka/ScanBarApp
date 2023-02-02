package com.example.test4;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.test4.ui.dashboard.MyDatabaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HistoryDetailsActivity extends AppCompatActivity {
    private String code;
    private TextView txtBarcode;
    private TextView txtName;
    private TextView txtScanTimestamp;
    private TextView txtScreenshotTimestamp;
    private TextView txtLink;
    private TextView txtLinkTimestamp;
    private ImageView imgScreenshot;

    private ImageButton btDelete;
    private FloatingActionButton btReturn;

    MyDatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_details);
        initItems();
        initListeners();
    }

    private void initItems() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            code = extras.getString("ID_CODE");
            this.setTitle(code);
        }

        db = new MyDatabaseHelper(this);

        txtBarcode = findViewById(R.id.txt_details_barcode);
        txtName = findViewById(R.id.txt_details_name);
        txtScanTimestamp = findViewById(R.id.txt_details_scan_timestamp);
        txtScreenshotTimestamp = findViewById(R.id.txt_details_screenshot_timestamp);
        txtLink = findViewById(R.id.txt_details_link);
        txtLinkTimestamp = findViewById(R.id.txt_details_link_timestamp);
        imgScreenshot = findViewById(R.id.img_screenshot_view);

        btDelete = findViewById(R.id.bt_details_delete);
        btReturn = findViewById(R.id.bt_details_return);
    }

    private void initListeners() {
        btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.deleteBarcode(code);
                finish();
            }
        });

        btReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}