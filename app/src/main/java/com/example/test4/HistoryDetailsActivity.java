package com.example.test4;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test4.ui.dashboard.MyDatabaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HistoryDetailsActivity extends AppCompatActivity {
    private String code;
    private TextView txtBarcode;
    private EditText txtName;
    private TextView txtScanTimestamp;
    private TextView txtScreenshotTimestamp;
    private TextView txtLink;
    private TextView txtLinkTimestamp;
    private ImageView imgScreenshot;

    private ImageButton btDelete;
    private FloatingActionButton btReturn;
    private FloatingActionButton btSave;

    MyDatabaseHelper db;

    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_details);
        initItems();
        searchDbAndFillData();
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
        btSave = findViewById(R.id.bt_details_save);
        btSave.hide();
    }

    private void initListeners() {
        btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.deleteOneBarcode(code);
                finish();
            }
        });

        btReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.updateBarcodeName(code, txtName.getText().toString().trim());
                Toast.makeText(HistoryDetailsActivity.this, R.string.onDataUpdate, Toast.LENGTH_SHORT).show();
                btSave.hide();
            }
        });

        txtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                btSave.hide();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() != 0 && charSequence.toString().trim() != name){
                    btSave.show();
                } else {
                    btSave.hide();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void searchDbAndFillData() {
        Cursor cursor = db.readOneItem(code);
        if(cursor.getCount() == 0) {
            Toast.makeText(this, "Something went wrong :(", Toast.LENGTH_SHORT).show();
        } else {
            //loop going just once but i left it. To remember how to use cursor next time :)
            while(cursor.moveToNext()) {
                txtScanTimestamp.setText(getString(R.string.ScanTimestamp) + cursor.getString(1));
                txtName.setText(cursor.getString(2));
                name = cursor.getString(2);
                byte[] imgByte = cursor.getBlob(3);
                if (imgByte != null)
                    imgScreenshot.setImageBitmap(BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length));
                txtScreenshotTimestamp.setText(getString(R.string.ScreenshotTimestamp) + cursor.getString(4));
                txtLink.setText(getString(R.string.SavedLink) + cursor.getString(5));
                txtLinkTimestamp.setText(getString(R.string.SavedLinkTimestamp) + cursor.getString(6));
            }
        }
    }
}