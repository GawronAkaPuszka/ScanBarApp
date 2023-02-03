package com.example.test4;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.test4.ui.dashboard.MyDatabaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.Date;


public class DetailsActivity extends AppCompatActivity {

        private FloatingActionButton btGoBack;
        private FloatingActionButton btTakeScreenShot;
        private FloatingActionButton btSaveLink;
        private WebView myWebView;
        protected ProgressBar progressBar;
        private MyDatabaseHelper db;
        private WebSettings webSettings;

        //Shop list buttons
        private ImageButton btAllegro;
        private ImageButton btCarrefour;
        private ImageButton btObi;
        private ImageButton btGoogle;
        //======================
        private String code;
        private String activeView;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_details);
            initItems();
            initOnClickListeners();

            adjustItems();
        }

        private void initItems() {
            btGoBack = findViewById(R.id.bt_float_goback);
            progressBar = findViewById(R.id.web_progress_bar);
            myWebView = findViewById(R.id.web_view);
            btAllegro = findViewById(R.id.img_bt_allegro);
            btCarrefour = findViewById(R.id.img_bt_carrefour);
            btObi = findViewById(R.id.img_bt_obi);
            btGoogle = findViewById(R.id.img_bt_google);

            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                code = extras.getString("ID_CODE");
                if(code.equals("")) {
                    Toast.makeText(getApplicationContext(), "No code given", Toast.LENGTH_SHORT).show();
                    finish();
                }
                activeView = "google";
                myWebView.loadUrl("https://www.google.com/search?q=" + code);
            } else {
                activeView = "none";
            }

            btTakeScreenShot = findViewById(R.id.bt_float_screenshoot);
            btSaveLink = findViewById(R.id.bt_float_saveSite);
            db = new MyDatabaseHelper(this);
            Date currentTime = Calendar.getInstance().getTime();
            db.addBarcode(code, currentTime.toString());

            webSettings = myWebView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            myWebView.setWebViewClient(new CustomBrowser());
        }

        private void initOnClickListeners() {
            btGoBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });

            btTakeScreenShot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    insertScreenshotInDb(takeWebviewScreenshot());
                }
            });

            btAllegro.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    webSettings.setJavaScriptEnabled(true);
                    if (activeView != "allegro") {
                        myWebView.clearCache(true);
                        myWebView.loadUrl("https://www.allegro.pl/listing?string=" + code);
                        activeView = "allegro";
                        adjustItems();
                    }
                }
            });

            btCarrefour.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //javascript breaks on this page. Simple workaround is to disable js.
                    webSettings.setJavaScriptEnabled(false);
                    if (activeView != "carrefour") {
                        myWebView.clearCache(true);
                        myWebView.loadUrl("https://www.carrefour.pl/szukaj?q=" + code);
                        activeView = "carrefour";
                        adjustItems();
                    }
                }
            });

            btObi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (activeView != "obi") {
                        webSettings.setJavaScriptEnabled(true);
                        myWebView.clearCache(true);
                        myWebView.loadUrl("https://www.obi.pl/search/" + code);
                        activeView = "obi";
                        adjustItems();
                    }
                }
            });

            btGoogle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (activeView != "google") {
                        webSettings.setJavaScriptEnabled(true);
                        myWebView.clearCache(true);
                        myWebView.loadUrl("https://www.google.com/search?q=" + code);
                        activeView = "google";
                        adjustItems();
                    }
                }
            });
        }

        private void adjustItems() {
            if(activeView == "google")
                btGoogle.setVisibility(View.GONE);
            else
                btGoogle.setVisibility(View.VISIBLE);
            if(activeView == "allegro")
                btAllegro.setVisibility(View.GONE);
            else
                btAllegro.setVisibility(View.VISIBLE);
            if(activeView == "carrefour")
                btCarrefour.setVisibility(View.GONE);
            else
                btCarrefour.setVisibility(View.VISIBLE);
            if(activeView == "obi")
                btObi.setVisibility(View.GONE);
            else
                btObi.setVisibility(View.VISIBLE);
        }

        @Override
        public void onBackPressed() {
            if (myWebView.canGoBack()) {
                myWebView.goBack();
            } else {
                super.onBackPressed();
            }
        }

        private Bitmap takeWebviewScreenshot() {
            Bitmap screenshot = Bitmap.createBitmap(myWebView.getWidth(), myWebView.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(screenshot);
            myWebView.draw(canvas);
            return screenshot;
            //TODO fix screenshots
        }

        private void insertScreenshotInDb(Bitmap img) {
            byte[] rawScreenshot = getBitmapAsByteArray(img);
            db.updateBarcodeImage(code, rawScreenshot);
        }

        private static byte[] getBitmapAsByteArray(Bitmap bitmap) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
            return outputStream.toByteArray();
        }

        public class CustomBrowser extends WebViewClient {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String request) {
                view.loadUrl(request);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
                setTitle("Page Loading...");
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
                    setTitle(view.getTitle());
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_LONG).show();
                myWebView.loadUrl("file:///android_asset/lost.html");
            }
        }
    }
