package com.onlinecash.loanswithout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.Objects;

public class RegistrationActivity extends AppCompatActivity {

    private static final int PICK_FROM_GALLERY = 2;
    private static final String EXTRA_ORDER = "ORDER";
    private final static int FILE_CHOOSER_RESULT_CODE = 1;
    private ValueCallback<Uri[]> uploadMessage;

    public static Intent newIntent(Context packageContext, String order) {
        Intent intent = new Intent(packageContext, RegistrationActivity.class);

        intent.putExtra(EXTRA_ORDER, order);

        return intent;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        String order = Objects.requireNonNull(getIntent().getStringExtra(EXTRA_ORDER));

        TextView connectionStatusTextView = findViewById(R.id.connectionStatusTextView);

        WebView webView = findViewById(R.id.registrationWebView);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (savedInstanceState == null)
                    view.loadUrl(url);
                return true;
            }

            @TargetApi(Build.VERSION_CODES.N)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                if (savedInstanceState == null)
                    view.loadUrl(request.getUrl().toString());
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            public boolean onShowFileChooser(WebView mWebView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
                ActivityCompat.requestPermissions(RegistrationActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_FROM_GALLERY);

                if (uploadMessage != null) {
                    uploadMessage.onReceiveValue(null);
                    uploadMessage = null;
                }

                uploadMessage = filePathCallback;
                Intent intent = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    intent = fileChooserParams.createIntent();
                }
                try {
                    startActivityForResult(intent, FILE_CHOOSER_RESULT_CODE);
                } catch (ActivityNotFoundException e) {
                    uploadMessage = null;
                    return false;
                }
                return true;
            }
        });


        if (Utils.isNetworkAvailable(getApplicationContext())) {
            webView.getSettings().setJavaScriptEnabled(true);
            String link = order + "&aff_sub1=" + Utils.getInstanceId(getApplicationContext())
                    + "&aff_sub2=" + "deep"
                    + "&aff_sub3=" + Utils.token[0]
                    + "&aff_sub4=not_available"
                    + "&aff_sub5=" + Utils.googleAdvertisingId[0];
            webView.loadUrl(link);
        } else {
            connectionStatusTextView.setVisibility(View.VISIBLE);
        }
    }
}