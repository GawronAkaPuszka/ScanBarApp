package com.example.test4.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.test4.HistoryDetailsActivity;
import com.example.test4.databinding.FragmentHistoryBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HistoryFragment extends Fragment {

    private FragmentHistoryBinding binding;
    private FloatingActionButton btAdd;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHistoryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        initItems();
        initListeners();

        return root;
    }

    private void initItems() {
        btAdd = binding.btFloatActAdd;
    }

    private void initListeners() {
        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), HistoryDetailsActivity.class);
                i.putExtra("ID_CODE","jakiś kodzik XDDD");
                startActivity(i);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}