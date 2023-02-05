package com.example.test4;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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
    private FloatingActionButton btSearch;

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
        btSearch = findViewById(R.id.bt_details_search);

        setTitle(code);
    }

    private void initListeners() {
        btDelete.setOnClickListener(view -> confirmDialog());

        btReturn.setOnClickListener(view -> finish());

        btSave.setOnClickListener(view -> {
            db.updateBarcodeName(code, txtName.getText().toString().trim());
            Toast.makeText(HistoryDetailsActivity.this, R.string.onDataUpdate, Toast.LENGTH_SHORT).show();
            btSave.hide();
        });

        txtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                btSave.hide();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() != 0 && !charSequence.toString().trim().equals(name)) {
                    btSave.show();
                } else {
                    btSave.hide();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btSearch.setOnClickListener(view -> startDetailsActivity());

        txtLink.setOnClickListener(view -> saveLinkToClipboard());
    }

    @SuppressLint("SetTextI18n")
    private void searchDbAndFillData() {
        Cursor cursor = db.readOneItem(code);
        if(cursor.getCount() == 0) {
            Toast.makeText(this, "Something went wrong :(", Toast.LENGTH_SHORT).show();
        } else {
            //loop going just once but i left it. To remember how to use cursor next time :)
            while(cursor.moveToNext()) {
                txtScanTimestamp.setText(getString(R.string.ScanTimestamp) + " " + cursor.getString(1));
                txtName.setText(cursor.getString(2));
                name = cursor.getString(2);
                byte[] imgByte = cursor.getBlob(3);
                if (imgByte != null)
                    imgScreenshot.setImageBitmap(BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length));
                txtScreenshotTimestamp.setText(getString(R.string.ScreenshotTimestamp) + " " + cursor.getString(4));
                txtLink.setText(cursor.getString(5));
                txtLinkTimestamp.setText(getString(R.string.SavedLinkTimestamp) + " " + cursor.getString(6));
            }
        }
    }

    private void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.delete) + " " + getTitle() + "?");
        builder.setMessage(getString(R.string.SureYouWantToDelete) + " " + getTitle() + "?");
        builder.setPositiveButton(R.string.yes, (dialogInterface, i) -> {
            db.deleteOneBarcode(code);
            finish();
        });
        builder.setNegativeButton(R.string.no, (dialogInterface, i) -> {

        });
        builder.create().show();
    }

    private void startDetailsActivity() {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("ID_CODE",code);
        intent.putExtra("LINK",txtLink.getText());
        startActivity(intent);
    }

    private void saveLinkToClipboard() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("LINK", txtLink.getText());
        clipboard.setPrimaryClip(clip);

        Toast.makeText(this, R.string.LinkCopiedToClipboard, Toast.LENGTH_SHORT).show();
    }
}