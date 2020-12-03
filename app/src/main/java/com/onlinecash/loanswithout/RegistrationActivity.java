package com.onlinecash.loanswithout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.my.tracker.MyTracker;
import com.yandex.metrica.YandexMetrica;

import java.util.Objects;

public class RegistrationActivity extends AppCompatActivity {

    private static final int PICK_FROM_GALLERY = 2;
    private static final String EXTRA_ORDER = "ORDER";
    private static final String EXTRA_ITEM_ID = "item_id";
    private final static int FILE_CHOOSER_RESULT_CODE = 1;
    private ProgressBar webProgressBar;
    private WebChromeClient webChromeClient;
    private ValueCallback<Uri[]> uploadMessage;

    private WebView chooserWebView;
    private ValueCallback<Uri[]> chooserFilePathCallback;
    private WebChromeClient.FileChooserParams chooserFileChooserParams;

    public static Intent newIntent(Context packageContext, String order, String itemId) {
        Intent intent = new Intent(packageContext, RegistrationActivity.class);

        intent.putExtra(EXTRA_ORDER, order);
        intent.putExtra(EXTRA_ITEM_ID, itemId);

        return intent;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        SharedPreferences sharedPreferences = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        String facebookDeepLink = sharedPreferences.getString("facebook_deep_link", "not_available");

        String order = Objects.requireNonNull(getIntent().getStringExtra(EXTRA_ORDER));
        String itemId = Objects.requireNonNull(getIntent().getStringExtra(EXTRA_ITEM_ID));

        ImageButton closeImageButton = findViewById(R.id.closeImageButton);
        webProgressBar = findViewById(R.id.webProgressBar);
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

                webProgressBar.setVisibility(View.GONE);
            }
        });

        webChromeClient = new WebChromeClient() {
            public boolean onShowFileChooser(WebView mWebView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    chooserWebView = mWebView;
                    chooserFilePathCallback = filePathCallback;
                    chooserFileChooserParams = fileChooserParams;

                    ActivityCompat.requestPermissions(RegistrationActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PICK_FROM_GALLERY);

                    return true;
                } else {
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
            }
        };
        webView.setWebChromeClient(webChromeClient);
        new WebChromeClient() {
            public boolean onShowFileChooser(WebView mWebView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(RegistrationActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE
                            , Manifest.permission.WRITE_EXTERNAL_STORAGE
                            , Manifest.permission.CAMERA}, PICK_FROM_GALLERY);
                    return false;
                } else {
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
            }
        };


        if (Utils.isNetworkAvailable(getApplicationContext())) {
            String link = order + "&aff_sub1=" + Utils.instanceId[0]
                    + "&aff_sub2=" + facebookDeepLink
                    + "&aff_sub3=" + Utils.firebaseMessagingToken[0]
                    + "&aff_sub4=not_available"
                    + "&aff_sub5=" + Utils.googleAdvertisingId[0];

            String eventParameters = "{\"" + itemId + "\":{" + "\"more_details\":{\"" + link + "\"}}";
            YandexMetrica.reportEvent("external_link", eventParameters);
            MyTracker.trackEvent("external_link");

            webView.getSettings().setDomStorageEnabled(true);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setAllowContentAccess(true);
            webView.getSettings().setAllowFileAccess(true);
            webView.getSettings().setAllowFileAccessFromFileURLs(true);
            webView.getSettings().setAllowUniversalAccessFromFileURLs(true);

            webView.loadUrl(link);
        } else {
            webView.setVisibility(View.GONE);
            webProgressBar.setVisibility(View.GONE);
            connectionStatusTextView.setVisibility(View.VISIBLE);
        }

        closeImageButton.setOnClickListener(view -> onBackPressed());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED)
                webChromeClient.onShowFileChooser(chooserWebView, chooserFilePathCallback, chooserFileChooserParams);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == FILE_CHOOSER_RESULT_CODE) {
            if (null == uploadMessage) return;
            String result = intent == null || resultCode != RESULT_OK ? null
                    : intent.getDataString();

            uploadMessage.onReceiveValue(new Uri[]{Uri.parse(result)});
            uploadMessage = null;
        }
    }
}