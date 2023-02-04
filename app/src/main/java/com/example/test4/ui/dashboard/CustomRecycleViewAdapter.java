package com.example.test4.ui.dashboard;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test4.HistoryDetailsActivity;
import com.example.test4.R;

import java.util.ArrayList;

public class CustomRecycleViewAdapter extends RecyclerView.Adapter<CustomRecycleViewAdapter.MyViewHolder> {

    private final Context context;
    private final ArrayList barcode_id, barcode_name, barcode_photo, barcode_last_scan_timestamp;
    private final Activity activity;

    public CustomRecycleViewAdapter( Activity activity,
                                     Context context,
                                    ArrayList barcode_id,
                                    ArrayList barcode_name,
                                    ArrayList barcode_photo,
                                    ArrayList barcode_last_scan_timestamp) {
        this.activity = activity;
        this.context = context;
        this.barcode_id = barcode_id;
        this.barcode_name = barcode_name;
        this.barcode_photo = barcode_photo;
        this.barcode_last_scan_timestamp = barcode_last_scan_timestamp;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.custom_recycle_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.barcode_id.setText(String.valueOf(barcode_id.get(position)));
        holder.barcode_name.setText(String.valueOf(barcode_name.get(position)));
        //Photo config
        Bitmap screenshot = (Bitmap) barcode_photo.get(position);

        holder.barcode_photo.setImageBitmap(Bitmap.createScaledBitmap(screenshot, 40, 90, false ));
        holder.barcode_scan_timestamp.setText(String.valueOf(barcode_last_scan_timestamp.get(position)));
        holder.mainHistoryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, HistoryDetailsActivity.class);
                intent.putExtra("ID_CODE", String.valueOf(barcode_id.get(position)));
                activity.startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return barcode_id.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView barcode_id, barcode_name, barcode_scan_timestamp;
        ImageView barcode_photo;
        LinearLayout mainHistoryLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mainHistoryLayout = itemView.findViewById((R.id.main_history_layout));
            barcode_id = itemView.findViewById(R.id.txt_barcode_id);
            barcode_name = itemView.findViewById((R.id.txt_barcode_name));
            barcode_photo = itemView.findViewById(R.id.img_screenshot);
            barcode_scan_timestamp = itemView.findViewById((R.id.txt_barcode_last_scan));
        }
    }
}
