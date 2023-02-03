package com.example.test4.ui.dashboard;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.test4.R;

import java.io.ByteArrayOutputStream;

public class MyDatabaseHelper extends SQLiteOpenHelper  {
    private Context context;
    private static final String DATABASE_NAME = "BarcodeLibrary.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "my_barcodes";
    private static final String COLUMN_BARCODE_ID = "barcode_id";
    private static final String COLUMN_LAST_SCAN_TIMESTAMP = "barcode_last_scan_timestamp";
    private static final String COLUMN_NAME = "barcode_name";
    private static final String COLUMN_PHOTO = "barcode_photo";
    private static final String COLUMN_PHOTO_TIMESTAMP = "barcode_photo_timestamp";
    private static final String COLUMN_LINK = "barcode_link";
    private static final String COLUMN_LINK_TIMESTAMP = "barcode_link_timestamp";

    public MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query =  "CREATE TABLE " + TABLE_NAME +
                        " (" + COLUMN_BARCODE_ID + " TEXT PRIMARY KEY, " +
                        COLUMN_LAST_SCAN_TIMESTAMP + " TEXT, " +
                        COLUMN_NAME + " TEXT, " +
                        COLUMN_PHOTO + " BLOB, " +
                        COLUMN_PHOTO_TIMESTAMP + " TEXT, " +
                        COLUMN_LINK + " TEXT, " +
                        COLUMN_LINK_TIMESTAMP + " TEXT); ";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME );
        onCreate(db);
    }

    public void addBarcode(String code, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_BARCODE_ID, code);
        cv.put(COLUMN_LAST_SCAN_TIMESTAMP, "00.00");
        cv.put(COLUMN_NAME, "Insert name");
        byte[] blankPhoto = getBitmapAsByteArray(BitmapFactory.decodeResource(context.getResources(), R.drawable.question_mark));
        cv.put(COLUMN_PHOTO, blankPhoto);
        cv.put(COLUMN_PHOTO_TIMESTAMP, "No screenshot given");
        cv.put(COLUMN_LINK, "No link saved");
        cv.put(COLUMN_LINK_TIMESTAMP, "No link given");

        long result = db.insert(TABLE_NAME, null, cv);

        if(result == -1) {
            Toast.makeText(context, R.string.UpdateFailed, Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(context, R.string.UpdateSuccess, Toast.LENGTH_SHORT).show();
    }

    public void updateBarcodeName(String code, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_NAME, name);

        long result = db.update(TABLE_NAME, cv, "barcode_id=?", new String[]{code});

        if(result == -1) {
            Toast.makeText(context, R.string.UpdateFailed, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, R.string.UpdateSuccess, Toast.LENGTH_SHORT).show();
        }
    }

    public void updateBarcodeImage(String code, byte[] rawScreenshot) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_PHOTO, rawScreenshot);

        long result = db.update(TABLE_NAME, cv, "barcode_id=?", new String[]{code});

        if(result == -1) {
            Toast.makeText(context, R.string.UpdateFailed, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, R.string.UpdateSuccess, Toast.LENGTH_SHORT).show();
        }
    }

    public void updateBarcodeLink(String code, String link) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_LINK, link);

        long result = db.update(TABLE_NAME, cv, "barcode_id=?", new String[]{code});

        if(result == -1) {
            Toast.makeText(context, R.string.UpdateFailed, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, R.string.UpdateSuccess, Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteOneBarcode(String code) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME,"barcode_id=?", new String[]{code});

        if(result == -1) {
            Toast.makeText(context, R.string.DeleteFailed, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, R.string.DeleteSuccess, Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteAllBarcodes() {

    }

    public Cursor readAllData() {
        String query = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + COLUMN_LAST_SCAN_TIMESTAMP + " DESC";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public Cursor readOneItem(String code) {
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_BARCODE_ID + " = \"" + code + "\"";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }



    private static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }
}
