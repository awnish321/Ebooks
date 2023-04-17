package com.radaee.reader;

import com.radaee.viewlib.R;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.io.File;

public class ShowWebView extends Activity {
    String Uri;
    //private Button button;
    private WebView webView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_web_view);
        webView = findViewById(R.id.webView1);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey("Linkpass")) {
            Uri = extras.getString("Linkpass");
            Log.d("webbs111", "tttt");
        }else {
            Log.d("webbs9", "ttt");
            Toast.makeText(ShowWebView.this, "webs11", Toast.LENGTH_SHORT).show();
        }
        //Get webview
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

            Log.d("webbs1111", "tttt1"+"/.Rachna"+Uri);
            webView.getSettings().setAllowFileAccess(true);
            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setJavaScriptCanOpenWindowsAutomatically(false);
            webSettings.setAllowFileAccess(true);
            webSettings.setAllowContentAccess(true);
            webSettings.setLoadsImagesAutomatically(true);
            File extDir = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) ;
            //htmlPath is something like this: "ExampleApp/EnginePayload/de/html-file.html"
            File twine = new File(extDir.toString() + "/"+"/.Rachna"+Uri);
            webView.loadUrl(twine.getAbsolutePath());
            Log.d("webbe3",""+twine);
        } else {
            Log.d("webbs1111", "tttt1"+"/.Rachna"+Uri);
            webView.getSettings().setAllowFileAccess(true);
            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setJavaScriptCanOpenWindowsAutomatically(false);
            webSettings.setAllowFileAccess(true);
            webSettings.setAllowContentAccess(true);
            webSettings.setLoadsImagesAutomatically(true);
            File extDir = Environment.getExternalStorageDirectory() ;
            //htmlPath is something like this: "ExampleApp/EnginePayload/de/html-file.html"
            File twine = new File(extDir.toString() + "/"+"/.Rachna"+Uri);
            webView.loadUrl(twine.getAbsolutePath());
            Log.d("webbe3",""+twine);
        }
    }
    private void startWebView(String url) {
        //Create new webview Client to show progress dialog
        //When opening a url or click on link
        webView.setWebViewClient(new WebViewClient() {
            ProgressDialog progressDialog;

            //If you will not use this method url links are open in new browser not in webview
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            //Show loader on url load
            public void onLoadResource(WebView view, String url) {
                if (progressDialog == null) {
                    // in standard case YourActivity.this
                    progressDialog = new ProgressDialog(ShowWebView.this);
                    progressDialog.setMessage("Loading...");
                    progressDialog.show();
                }
            }

            public void onPageFinished(WebView view, String url) {
                try {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                        progressDialog = null;
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }

        });
        // Javascript enabled on webview
        webView.loadUrl(url);
    }
}