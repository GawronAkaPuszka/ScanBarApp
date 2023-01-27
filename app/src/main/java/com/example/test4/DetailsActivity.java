package com.example.test4;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;


public class DetailsActivity extends AppCompatActivity {

        private Button btFinishActivity;
        private WebView myWebView;
        protected ProgressBar progressBar;

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

            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                code = extras.getString("ID_CODE");
                activeView = "google";
                myWebView.loadUrl("https://www.google.com/search?q=" + code);
            } else {
                activeView = "none";
            }

            adjustItems();
        }

        private void initItems() {
            btFinishActivity = findViewById(R.id.bt_finish_details_activity);
            progressBar = findViewById(R.id.web_progress_bar);
            myWebView = findViewById(R.id.web_view);
            btAllegro = findViewById(R.id.img_bt_allegro);
            btCarrefour = findViewById(R.id.img_bt_carrefour);
            btObi = findViewById(R.id.img_bt_obi);
            btGoogle = findViewById(R.id.img_bt_google);

            WebSettings webSettings = myWebView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            myWebView.setWebViewClient(new CustomBrowser());
        }

        private void initOnClickListeners() {
            btFinishActivity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });

            btAllegro.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
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
