package com.example.test4.ui.home;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.ViewModelProvider;

import com.example.test4.DetailsActivity;
import com.example.test4.R;
import com.example.test4.databinding.FragmentHomeBinding;
import com.google.android.material.textfield.TextInputEditText;
import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.util.List;

public class HomeFragment extends Fragment implements
        DecoratedBarcodeView.TorchListener {

    private FragmentHomeBinding binding;
    private CaptureManager capture;
    private DecoratedBarcodeView barcodeScannerView;
    private ImageButton switchFlashlightButton;
    private boolean flashOn;
    private ImageButton btSubmitCode;
    private TextInputEditText txtInput;
    private String lastText;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        initItems();
        initListeners();

        capture = new CaptureManager(this.getActivity(), barcodeScannerView);
        capture.initializeFromIntent(this.getActivity().getIntent(), savedInstanceState);
        capture.setShowMissingCameraPermissionDialog(false);

        return root;
    }

    private void initItems() {
        barcodeScannerView = binding.homeDecoratedBarcodeView;
        txtInput = binding.homeTxtInput;
        switchFlashlightButton = binding.btFlashlight;
        btSubmitCode = binding.btSubmitCode;
        btSubmitCode.setClickable(false);
        flashOn = false;

        //if the device does not have flashlight in its camera,
        //then remove the switch flashlight button...
        if (!hasFlash()) switchFlashlightButton.setVisibility(View.GONE);

        //set status message and tell scanner to scan without
        //going back to previous activity (decodeContinuous does the trick)
        barcodeScannerView.setStatusText(getString(R.string.status_scanner_message));
        barcodeScannerView.decodeContinuous(callback);
    }

    private void initListeners() {
        barcodeScannerView.setTorchListener(this);

        btSubmitCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txtInput.getText().toString().trim().equals("")){
                    Toast.makeText(getContext(), "No code given", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(getActivity(), DetailsActivity.class);
                    intent.putExtra("ID_CODE",txtInput.getText().toString().trim());
                    startActivity(intent);
                }
            }
        });

        switchFlashlightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchFlashlight(view);
            }
        });

        txtInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() != 0)
                    btSubmitCode.setClickable(true);
                else
                    btSubmitCode.setClickable(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private final BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            /**
             * if(result.getText() == null || result.getText().equals(lastText)) {
             *                 //Prevent duplicate scans
             *             *    return;
             *             }
             */

            lastText = result.getText();
            Toast.makeText(getContext().getApplicationContext(), result.getText(), Toast.LENGTH_SHORT).show();
            //After scan find and show details in new Activity
            Intent i = new Intent(getActivity(), DetailsActivity.class);
            i.putExtra("ID_CODE",result.getText());
            startActivity(i);
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        if(capture != null)
            capture.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(capture != null)
            capture.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if(capture != null)
            capture.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(capture != null)
            capture.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(capture != null)
            capture.onSaveInstanceState(outState);
    }

    /**
     * Check if the device's camera has a Flashlight.
     * @return true if there is Flashlight, otherwise false.
     */
    private boolean hasFlash() {
        return this.getActivity().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    public void switchFlashlight(View view) {
        if (!flashOn) {
            barcodeScannerView.setTorchOn();
            flashOn = !flashOn;
        } else {
            barcodeScannerView.setTorchOff();
            flashOn = !flashOn;
        }
    }

    @Override
    public void onTorchOn() {
        switchFlashlightButton.setImageDrawable(
                ContextCompat.getDrawable(this.getContext(), R.drawable.ic_baseline_flashlight_off_24));
    }

    @Override
    public void onTorchOff() {
        switchFlashlightButton.setImageDrawable(
                ContextCompat.getDrawable(this.getContext(), R.drawable.ic_baseline_flashlight_on_24));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(capture != null)
            capture.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}