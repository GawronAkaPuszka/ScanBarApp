package com.example.test4.ui.dashboard;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test4.HistoryDetailsActivity;
import com.example.test4.MainActivity;
import com.example.test4.databinding.FragmentHistoryBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class HistoryFragment extends Fragment {

    private FragmentHistoryBinding binding;
    private RecyclerView recycleView;
    private MyDatabaseHelper myDB;
    private ArrayList<String> barcode_id, barcode_last_scan_timestamp, barcode_name,
            barcode_photo_timestamp, barcode_link, barcode_link_timestamp;
    private ArrayList<Bitmap> barcode_photo;
    private CustomRecycleViewAdapter CustomAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHistoryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        initItems();
        initListeners();

        return root;
    }

    private void initItems() {
        recycleView = binding.historyRecView;

        //database init
        myDB = new MyDatabaseHelper(getContext());
        barcode_id = new ArrayList<>();
        barcode_last_scan_timestamp = new ArrayList<>();
        barcode_name = new ArrayList<>();
        barcode_photo = new ArrayList<>();
        barcode_photo_timestamp = new ArrayList<>();
        barcode_link = new ArrayList<>();
        barcode_link_timestamp = new ArrayList<>();

        storeDataInArrays();

        CustomAdapter = new CustomRecycleViewAdapter(binding.getRoot().getContext(),
                barcode_id, barcode_name, barcode_photo, barcode_last_scan_timestamp);
        recycleView.setAdapter(CustomAdapter);
        recycleView.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
    }

    private void initListeners() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    //database related stuff
    public void storeDataInArrays() {
        Cursor cursor = myDB.readAllData();
        if(cursor.getCount() == 0) {
            Toast.makeText(getContext(), "No data.", Toast.LENGTH_SHORT).show();
        } else {
            while(cursor.moveToNext()) {
                barcode_id.add(cursor.getString(0));
                barcode_last_scan_timestamp.add(cursor.getString(1));
                barcode_name.add(cursor.getString(2));
                byte[] imgByte = cursor.getBlob(3);
                if (imgByte != null)
                    barcode_photo.add(BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length));
                barcode_photo_timestamp.add(cursor.getString(4));
                barcode_link.add(cursor.getString(5));
                barcode_link_timestamp.add(cursor.getString(6));
            }
        }
    }
}