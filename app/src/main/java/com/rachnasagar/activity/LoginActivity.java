package com.rachnasagar.activity;

import static com.rachnasagar.Common.Networking.url;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.rachnasagar.Common.ConnectionDetector;
import com.rachnasagar.Common.Misc;
import com.rachnasagar.Common.Networking;
import com.rachnasagar.Config2;
import com.rachnasagar.FirstBottomPage;
import com.rachnasagar.R;
import com.rachnasagar.RequestOtp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class LoginActivity extends AppCompatActivity {
    ProgressBar progressBar;
    //ProgressHUD dialog;
    ProgressHUD dialogoo;
    String message = "Please Wait....";
    ProgressDialog progressDialog;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    PackageInfo pinfo;
    public static String PACKAGE_NAME;
    String sVersionName;
    int sVersionCode;
    String Device_Id, Mob_Id, Mob_Product, Mob_Brand, Mob_Manufacture, Mob_Model;
    String Forget_pass_email;
    Dialog dialogforget;
    TextView privacypolicytxt, abouttext;
    EditText edit_username, edit_password, edt_Forget_pass;

    // flag for Internet connection status
    Boolean isInternetPresent = false;
    // Connection detector class
    ConnectionDetector cd;
    ImageView headerTitleImage;
    TextView headerTitleText, txt_grand_total;
    String str_username, str_password, Str_Status, Str_UserId, Str_otp1, Str_name, Str_Msg, Str_token1, Str_url1, Str_statusotp, Str_otp, Str_msgotp;
    private static String URLLogin;
    ImageView header_search;
    // TextView textclick,privacy,about;
    BottomNavigationView bottomNavigationView;
    AutoCompleteTextView searchedit;
    WebView webView2;
    ImageButton search_image_btn, whatsappbtn;
    String s70, s71, s72, s73, s80, s81, s82, s83, s84, holdvalue;
    ConstraintLayout linearLayout1, linearLayout3;

    LinearLayout linearLayout, linearLayout4;
    ArrayAdapter<String> adapter;
    Button btn_login, requestotpbtn, forgetpassbtn, createaccountbtn, skip_sign;
    String mCurrentUrl = "", getemail, getotp;
    Button request_submit;
    EditText request_edit, forget_edit;
    Dialog otpdialog;
    TextView text11, marquee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //for changing status bar icon colors
        setContentView(R.layout.activity_login1);
        getSupportActionBar().hide();
        btn_login = findViewById(R.id.btn_logins);
        marquee = findViewById(R.id.marqueeTextq);
        marquee.setSelected(true);
        cd = new ConnectionDetector(getApplicationContext());
        //setTvZoomInOutAnimation();
        Device_Id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Mob_Id = Build.ID;
        Mob_Product = Build.PRODUCT;
        Mob_Brand = Build.BRAND;
        Mob_Manufacture = Build.MANUFACTURER;
        Mob_Model = Build.MODEL;

        System.out.println("gdasaa" + "  " + Device_Id);
        PACKAGE_NAME = getApplicationContext().getPackageName();
        try {
            pinfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        sVersionCode = pinfo.versionCode; // 1
        sVersionName = pinfo.versionName; // 1.0
        System.out.println("details versionmain" + "  " + sVersionCode + " " + sVersionName + " " + PACKAGE_NAME);
        isInternetPresent = cd.isConnectingToInternet();
        ButtonDetails();
        weballsetting123();
        trick();
        fetchsearchview();
        WebSettings webSettings = webView2.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView2.setWebViewClient(new MyCustomWebViewClient());
        webView2.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        progressBar = new ProgressBar(LoginActivity.this);
        getemail = request_edit.getText().toString();
        request_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getemail = request_edit.getText().toString();
                SendMsg();
                otpdialog = new Dialog(LoginActivity.this);
                otpdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                otpdialog.setContentView(R.layout.forget);
                otpdialog.setCancelable(false);

                LinearLayout ln_outline = (LinearLayout) otpdialog.findViewById(R.id.dia_ln_outline1);
                View view1 = (View) otpdialog.findViewById(R.id.dia_view1);
                TextView Error_text = (TextView) otpdialog.findViewById(R.id.dia_error_title1);
                text11 = otpdialog.findViewById(R.id.textwrittn1);
                forget_edit = (EditText) otpdialog.findViewById(R.id.dia_error_msg1);
                getotp = forget_edit.getText().toString();
                Button btn_yes = (Button) otpdialog.findViewById(R.id.dia_b_yes1);
                Button cencel = (Button) otpdialog.findViewById(R.id.cancels1);
                cencel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        otpdialog.dismiss();
                    }
                });
                btn_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Str_Otp= ettext.getText().toString().trim();
                        if (request_edit.getText().toString().trim().isEmpty()) {
                            Toast.makeText(LoginActivity.this, "Please Enter OTP", Toast.LENGTH_LONG).show();
                        } else {
                            getotp = forget_edit.getText().toString();
                            LoginOtpUrl();


                        }
                        //dialogss.dismiss();
                    }

                });
                otpdialog.show();

            }
        });
        webView2.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                progressBar.setProgress(progress);
                if (progress == 100) {
                    progressBar.setVisibility(View.GONE);

                } else {
                    progressBar.setVisibility(View.VISIBLE);

                }
            }
        });
        webView2.setDownloadListener(new DownloadListener() {

            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                DownloadManager.Request request = new DownloadManager.Request(
                        android.net.Uri.parse(url));

                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED); //Notify client once download is completed!
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "Name of your downloadble file goes here, example: Mathematics II ");
                DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                dm.enqueue(request);
                Toast.makeText(getApplicationContext(), "Downloading File", //To notify the Client that the file is being downloaded
                        Toast.LENGTH_LONG).show();
            }
        });
        //AllBooks();
        bottomNavigationView.setItemIconTintList(null);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_product2:
                        linearLayout1.setVisibility(View.GONE);
                        if (isInternetPresent) {
                            Log.d("awaaa", "innnn");
                            linearLayout1.setVisibility(View.GONE);
                            linearLayout.setVisibility(View.GONE);
                            linearLayout4.setVisibility(View.GONE);

                            webView2.setVisibility(View.VISIBLE);
                            Log.d("awaaa1", "innnn");
                            startWebView("https://www.rachnasagar.in/mobile/");
                            // webView.loadUrl("https://rachnasagar.in/mobile/books");

                        } else {
                            Log.d("awaa2", "innnn");
                            linearLayout1.setVisibility(View.GONE);

                            webView2.setVisibility(View.GONE);
                            Log.d("awaaa3", "innnn");
                            linearLayout.setVisibility(View.VISIBLE);
                        }
                        break;

                    case R.id.navigation_ebook2:
                        linearLayout1.setVisibility(View.VISIBLE);
                        linearLayout.setVisibility(View.GONE);
                        linearLayout4.setVisibility(View.GONE);
                        webView2.setVisibility(View.GONE);

                        break;

                    case R.id.navigation_notification2:
                        linearLayout1.setVisibility(View.VISIBLE);
                        linearLayout.setVisibility(View.GONE);
                        linearLayout4.setVisibility(View.GONE);
                        webView2.setVisibility(View.GONE);
                        break;
                    case R.id.navigation_account2:
                        linearLayout1.setVisibility(View.VISIBLE);
                        linearLayout.setVisibility(View.GONE);
                        linearLayout4.setVisibility(View.GONE);
                        webView2.setVisibility(View.GONE);
                        //webView.loadUrl("https://www.rachnasagar.in/mobile/login");
                        break;
                }
                return true;
            }
        });
        searchedit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                Log.d("aao1", "aa gya");

                if (isInternetPresent) {
                    Log.d("awaaa", "innnn");
                    linearLayout1.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.GONE);
                    linearLayout4.setVisibility(View.GONE);
                    webView2.setVisibility(View.VISIBLE);
                    Log.d("awaaa1", "innnn");
                    holdvalue = searchedit.getAdapter().getItem(pos).toString();
                    Log.d("holdvalue", holdvalue);
                    AllBooks();

                } else {
                    Log.d("awaa2", "innnn");
                    linearLayout1.setVisibility(View.GONE);
                    webView2.setVisibility(View.GONE);
                    Log.d("awaaa3", "innnn");
                    linearLayout.setVisibility(View.VISIBLE);
                }
                //startWebView(s83);


            }

        });

        search_image_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (isInternetPresent) {
                    Log.d("awaaa", "innnn");
                    linearLayout1.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.GONE);
                    linearLayout4.setVisibility(View.GONE);
                    webView2.setVisibility(View.VISIBLE);
                    Log.d("awaaa1", "innnn");
                    AllBooks();

                } else {
                    Log.d("awaa2", "innnn");
                    linearLayout1.setVisibility(View.GONE);
                    webView2.setVisibility(View.GONE);
                    Log.d("awaaa3", "innnn");
                    linearLayout.setVisibility(View.VISIBLE);
                }
                //startWebView(s83);

            }
        });

        requestotpbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isInternetPresent) {
                    Log.d("awaaa", "innnn");
                    webView2.setVisibility(View.GONE);
                    linearLayout1.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.GONE);
                    linearLayout4.setVisibility(View.VISIBLE);
                    Log.d("awaaa1", "innnn");
                    /*startWebView("https://rachnasagar.in/mobile/requestOtp");*/
                    // webView.loadUrl("https://rachnasagar.in/mobile/books");

                } else {
                    Log.d("awaa2", "innnn");
                    linearLayout1.setVisibility(View.GONE);
                    webView2.setVisibility(View.GONE);

                    Log.d("awaaa3", "innnn");
                    linearLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        forgetpassbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isInternetPresent) {
                    Log.d("awaaa", "innnn");

                    linearLayout1.setVisibility(View.GONE);
                    linearLayout4.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.GONE);


                    Log.d("awaaa1", "innnn");

                    startWebView("https://rachnasagar.in/mobile/forget-passward");
                    webView2.setVisibility(View.VISIBLE);
                    // webView.loadUrl("https://rachnasagar.in/mobile/books");

                } else {
                    Log.d("awaa2", "innnn");
                    linearLayout1.setVisibility(View.GONE);

                    webView2.setVisibility(View.GONE);
                    Log.d("awaaa3", "innnn");
                    linearLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        createaccountbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isInternetPresent) {
                    Log.d("awaaa", "innnn");

                    linearLayout1.setVisibility(View.GONE);
                    linearLayout4.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.GONE);
                    Log.d("awaaa1", "innnn");
                    startWebView("https://rachnasagar.in/mobile/signup");
                    webView2.setVisibility(View.VISIBLE);
                    // webView.loadUrl("https://rachnasagar.in/mobile/books");

                } else {
                    Log.d("awaa2", "innnn");
                    linearLayout1.setVisibility(View.GONE);

                    webView2.setVisibility(View.GONE);
                    Log.d("awaaa3", "innnn");
                    linearLayout.setVisibility(View.VISIBLE);
                }
            }
        });
        whatsappbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://api.whatsapp.com/send?phone=9717998871";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        privacypolicytxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent pol = new Intent(LoginActivity.this, WebViewPrivacy.class);
                startActivity(pol);

            }
        });
        abouttext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent about = new Intent(LoginActivity.this, WebViewAbout.class);
                startActivity(about);
            }
        });
        skip_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isInternetPresent) {
                    Log.d("awaaa", "innnn");
                    linearLayout1.setVisibility(View.GONE);
                    linearLayout4.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.GONE);


                    Log.d("awaaa1", "innnn");
                    startWebView("https://rachnasagar.in/mobile/");
                    webView2.setVisibility(View.VISIBLE);


                } else {
                    Log.d("awaa2", "innnn");
                    linearLayout1.setVisibility(View.GONE);

                    webView2.setVisibility(View.GONE);
                    Log.d("awaaa3", "innnn");
                    linearLayout.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    private class MyCustomWebViewClient extends WebViewClient {
        @Override
        public void onLoadResource(WebView view, String url10) {
            System.out.println("mCurrentUrl1=" + url10);
            if (url10.contentEquals("https://www.rachnasagar.in/api/buynow")) {
                linearLayout1.setVisibility(View.VISIBLE);
                linearLayout4.setVisibility(View.GONE);
                linearLayout.setVisibility(View.GONE);
                webView2.setVisibility(View.GONE);

            } else if (url10.contentEquals("https://www.rachnasagar.in/api/seo/order")) {
                linearLayout1.setVisibility(View.VISIBLE);
                linearLayout4.setVisibility(View.GONE);
                linearLayout.setVisibility(View.GONE);
                webView2.setVisibility(View.GONE);

            } else if (url10.contentEquals("https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/fonts/fontawesome-webfont.woff2?v=4.7.0")) {
                linearLayout1.setVisibility(View.VISIBLE);
                linearLayout4.setVisibility(View.GONE);
                linearLayout.setVisibility(View.GONE);
                webView2.setVisibility(View.GONE);
            } else if (url10.contentEquals("https://api.whatsapp.com/send/?phone=919717998871&text&type=phone_number&app_absent=0")) {
                String url1 = "https://api.whatsapp.com/send?phone=9717998871";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url1));
                startActivity(i);
            }
            super.onLoadResource(view, url);
        }

        @Override
        public void onPageStarted(WebView webview, String url11, Bitmap favicon) {
            System.out.println("mCurrentUrl2=" + url11);

            if (progressDialog == null) {
                // in standard case YourActivity.this
                progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setMessage("Loading...");
                progressDialog.show();
            }
            //webview.setVisibility(webview.INVISIBLE);
            // scrollView.setVisibility(View.GONE);
        }

        @Override
        public void onPageFinished(WebView view, String url5) {
            super.onPageFinished(view, url5);
            System.out.println("mCurrentUr3" + url5);

            try {

                if (progressDialog.isShowing()) {

                    progressDialog.dismiss();

                    progressDialog = null;

                }

            } catch (Exception exception) {
                exception.printStackTrace();
            }

            // Toast.makeText(getApplicationContext(), url5,Toast.LENGTH_LONG).show();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url2) {
            mCurrentUrl = url2;
            Log.i("mCurrentUrl4=", mCurrentUrl);

            if (url2.startsWith("https://api.whatsapp.com/send/?phone=919717998871&text&type=phone_number&app_absent=0")) {
                String url1 = "https://api.whatsapp.com/send?phone=9717998871";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url1));
                startActivity(i);
                return true;
            }
            if (progressDialog == null) {
                // in standard case YourActivity.this
                progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setMessage("Loading...");
                progressDialog.show();
            }
            view.loadUrl(url2);
            return true;

        }
    }

    private void SendMsg() {

        RequestQueue queue = Volley.newRequestQueue(this);
        String urlmanual = "https://rachnasagar.in/rsplws/otpRequest.php?username=" + getemail + "&action=requestOtp";
        Log.d("Responseeee3", urlmanual);
        StringRequest postRequest = new StringRequest(Request.Method.POST, urlmanual,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Responseeee", response);

                        //Toast.makeText(MainActivity.this, "Please Check your Email", Toast.LENGTH_LONG).show();
                        try {
                            // vinsquesarrayList = new ArrayList<SetterGetter_Sub_Chap>();
                            JSONArray array;
                            array = new JSONArray(response);
                            JSONObject object = new JSONObject();
                            for (int i = 0; i < array.length(); i++) {
                                object = array.getJSONObject(i);
                                Str_statusotp = object.get("status").toString();
                                if (Str_statusotp.equalsIgnoreCase("false")) {
                                    text11.setText("Not registered with us");
                                    otpdialog.dismiss();
                                    Toast.makeText(LoginActivity.this, "Not registered with us", Toast.LENGTH_LONG).show();
                                    // Log.d("ttttt33",Str_Msg);
                                }
                                Str_otp = object.get("otp").toString();
                                Str_msgotp = object.get("msg").toString();
                                text11.setText("Please Check your Email");

                                if (Str_statusotp.equalsIgnoreCase("true")) {
                                    text11.setText("Please Check your Email");
                                    Toast.makeText(LoginActivity.this, "Please Check your Email", Toast.LENGTH_LONG).show();
                                }


                                //Toast.makeText(LoginActivity.this, Str_msgotp, Toast.LENGTH_SHORT).show();
                                //System.out.println("messsssss"+"  "+Str_msgotp);

                            }

                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        try {
                            //  Log.d("Error.Response", error.getMessage());

                        } catch (Exception e) {
                            // TODO: handle exception
                        }
                    }
                }
        ) {
            @Override
            protected HashMap<String, String> getParams() {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("email", getemail);
                params.put("action", "requestOtp");
                return params;
            }
        };
        queue.add(postRequest);
    }

    public void LoginOtpUrl() {
        getotp = forget_edit.getText().toString();
        RequestQueue queue = Volley.newRequestQueue(this);
        String urlmanual = "https://rachnasagar.in/rsplws/otpVerify.php?action=otpVerify&otp=" + Str_otp;
        StringRequest postRequest = new StringRequest(Request.Method.POST, urlmanual,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response211", response);
                        try {
                            JSONArray array;
                            array = new JSONArray(response);
                            JSONObject object = new JSONObject();
                            for (int i = 0; i < array.length(); i++) {
                                object = array.getJSONObject(i);
                                //String result = object.getString("Status");
                                //Toast.makeText(Login.this, object.get("Name")+" "+object.get("UserId"), Toast.LENGTH_SHORT).show();
                                Str_Status = object.get("status").toString();
                                Log.d("notlogin0", Str_Status);
                                if (Str_Status.trim().equalsIgnoreCase("false"))
                                    Toast.makeText(LoginActivity.this, "Not login", Toast.LENGTH_SHORT).show();
                                   /* System.out.println("notlogin"+Str_Status);
                                    if (dialog.isShowing())
                                        dialog.dismiss();*/

                                Str_Msg = object.get("msg").toString();
                                Str_url1 = object.get("URL").toString();
                                Str_UserId = object.get("UserId").toString();
                                Str_token1 = object.get("App_Token").toString();
                                Str_name = object.get("Name").toString();
                                Str_otp1 = object.get("otp").toString();
                                System.out.println("otpvalue1=" + Str_otp1);


                                if (Str_Status.trim().equalsIgnoreCase("true")) {
                                    Log.d("notlogin222", "aaya1" + getotp);
                                    if (Str_otp1.contentEquals(getotp)) {
                                        Log.d("notlogin222", "aaya2");
                                        Str_UserId = object.get("UserId").toString();
                                        preferences = getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
                                        editor = preferences.edit();
                                        editor.putString("Login_Name", object.get("Name").toString());
                                        editor.putString("Login_UserId", object.get("UserId").toString());
                                        editor.putString("Login_MobileNo", object.get("Mobile").toString());
                                        editor.putString("Login_Address", object.get("Address").toString());
                                        editor.putString("Login_EmailId", object.get("Email").toString());
                                        editor.putString("Login_Status", object.get("status").toString());
                                        editor.putString("App_Token", object.get("App_Token").toString());
                                        editor.putString("UserId", object.get("UserId").toString());
                                        editor.putString("Name", object.get("Name").toString());
                                        editor.putString("Login_Value", "1");//Login Value define -- Login done by direct api or fb or gmail
                                        editor.commit();
                                        MobileDeviceUpdate();
                                        GetAddressDetails();
                                        // Toast.makeText(LoginActivity.this, "logins", Toast.LENGTH_SHORT).show();
                                        Intent hhh = new Intent(LoginActivity.this, FirstBottomPage.class);
                                    /*hhh.putExtra("urltoken",Str_token1);
                                    Log.d("tytyty00",Str_token1);*/
                                        startActivity(hhh);
                                        finish();
                                    }


                                } else {
                                    Toast.makeText(LoginActivity.this, "OTP is incorrect", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("notlogins3", error.getMessage());


                    }
                }
        ) {
            @Override
            protected HashMap<String, String> getParams() {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("action", "otpVerify");
                params.put("otp", Str_otp);


                return params;
            }
        };
        queue.add(postRequest);
    }

    public void trick() {
        if (isInternetPresent) {
            linearLayout1.setVisibility(View.VISIBLE);
            linearLayout4.setVisibility(View.GONE);
            linearLayout.setVisibility(View.GONE);
            webView2.setVisibility(View.GONE);
            //  linearLayout3.setVisibility(View.GONE);
            weballsetting123();
            Log.d("vvv", "onn");
            startWebView("https://www.rachnasagar.in/mobile/");
            Log.d("vvv1", "onn");

        } else {
            linearLayout1.setVisibility(View.GONE);
            webView2.setVisibility(View.GONE);
            linearLayout.setVisibility(View.VISIBLE);
            // linearLayout3.setVisibility(View.GONE);
        }
    }

    private void ButtonDetails() {
        request_edit = findViewById(R.id.edit_emailrequest);
        privacypolicytxt = findViewById(R.id.privacytextview);
        abouttext = findViewById(R.id.aboutustextview);
        request_submit = findViewById(R.id.btn_submitrequest_otp);
        linearLayout4 = findViewById(R.id.request_linear);
        skip_sign = findViewById(R.id.btn_skip1);
        whatsappbtn = findViewById(R.id.whatsapp);
        requestotpbtn = findViewById(R.id.btn_request);
        forgetpassbtn = findViewById(R.id.btn_forget);
        createaccountbtn = findViewById(R.id.btn_create_account);
        searchedit = findViewById(R.id.serchviews1);
        webView2 = findViewById(R.id.webviews_log);
        linearLayout1 = findViewById(R.id.leniar4);
        linearLayout = findViewById(R.id.Leee);
        linearLayout3 = findViewById(R.id.leniar5);
        search_image_btn = findViewById(R.id.searchbtn1);
        searchedit = findViewById(R.id.serchviews1);
        bottomNavigationView = findViewById(R.id.navfirst_view1);
        edit_username = (EditText) findViewById(R.id.edit_usernames);
        edit_password = (EditText) findViewById(R.id.edit_passwords);
        btn_login = (Button) findViewById(R.id.btn_logins);
        btn_login.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("NewApi")
            @Override
            public void onClick(View arg0) {

                if (edit_username.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please Enter the Email Id.", Toast.LENGTH_SHORT).show();
                }
                if (edit_password.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please Enter the Password.", Toast.LENGTH_SHORT).show();
                } else {
                    str_username = edit_username.getText().toString().trim();
                    str_password = edit_password.getText().toString().trim();
                    isInternetPresent = cd.isConnectingToInternet();
                    LoginURL();
                    // check for Internet status
                }
            }
        });
    }

    public void LoginURL() {

        RequestQueue queue = Volley.newRequestQueue(this);
        String urlmanual = "https://rachnasagar.in/rsplws/login.php?";
        StringRequest postRequest = new StringRequest(Request.Method.POST, urlmanual,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response234", response);
                        try {
                            JSONArray array;
                            array = new JSONArray(response);
                            JSONObject object = new JSONObject();
                            for (int i = 0; i < array.length(); i++) {
                                object = array.getJSONObject(i);
                                //String result = object.getString("Status");
                                //Toast.makeText(Login.this, object.get("Name")+" "+object.get("UserId"), Toast.LENGTH_SHORT).show();
                                Str_Msg = object.get("msg").toString();
                                Str_Status = object.get("status").toString();
                                Log.d("notlogin0", Str_Status);
                                if (Str_Status.trim().equalsIgnoreCase("false"))
                                    Toast.makeText(LoginActivity.this, Str_Msg, Toast.LENGTH_SHORT).show();
                                   /* System.out.println("notlogin"+Str_Status);
                                    if (dialog.isShowing())
                                        dialog.dismiss();*/


                                Str_url1 = object.get("URL").toString();
                                Str_UserId = object.get("UserId").toString();
                                Str_token1 = object.get("App_Token").toString();
                                Str_name = object.get("Name").toString();
                                Log.d("apptokens", Str_token1);


                                if (Str_Status.trim().equalsIgnoreCase("True")) {
                                    Log.d("notlogin2", "aaya");
                                    Str_UserId = object.get("UserId").toString();
                                    preferences = getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
                                    editor = preferences.edit();
                                    editor.putString("Login_Name", object.get("Name").toString());
                                    editor.putString("Login_UserId", object.get("UserId").toString());
                                    editor.putString("Login_MobileNo", object.get("Mobile").toString());
                                    editor.putString("Login_Address", object.get("Address").toString());
                                    editor.putString("Login_EmailId", object.get("Email").toString());
                                    editor.putString("Login_Status", object.get("status").toString());
                                    editor.putString("App_Token", object.get("App_Token").toString());
                                    editor.putString("UserId", object.get("UserId").toString());
                                    editor.putString("Name", object.get("Name").toString());
                                    editor.putString("Login_Value", "1");//Login Value define -- Login done by direct api or fb or gmail
                                    editor.commit();
                                    MobileDeviceUpdate();
                                    GetAddressDetails();
                                    // Toast.makeText(LoginActivity.this, "logins", Toast.LENGTH_SHORT).show();
                                    Intent hhh = new Intent(LoginActivity.this, FirstBottomPage.class);
                                    /*hhh.putExtra("urltoken",Str_token1);
                                    Log.d("tytyty00",Str_token1);*/
                                    startActivity(hhh);
                                    finish();
                                }


                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        // Log.d("notlogins3", error.getMessage());
                      /*  try {
                            LoginURL(URLLogin);
                            String message = "Please Wait....";
                            dialog = new ProgressHUD(LoginActivity.this, com.radaee.viewlib.R.style.AppTheme);
                            dialog.setTitle("");
                            dialog.setContentView(R.layout.progress_hudd);
                            if (message == null || message.length() == 0) {
                                dialog.findViewById(R.id.message).setVisibility(View.GONE);
                            } else {
                                TextView txt = (TextView) dialog.findViewById(R.id.message);
                                txt.setText(message);
                            }
                            dialog.setCancelable(true);
                            dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
                            WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
                            lp.dimAmount = 0.2f;
                            dialog.getWindow().setAttributes(lp);
                            dialog.show();
                            Log.d("Error.Response", error.getMessage());

                        } catch (Exception e) {
                            // TODO: handle exception
                        }*/


                    }
                }
        ) {
            @Override
            protected HashMap<String, String> getParams() {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("userId", str_username);// Second one u can change
                params.put("userPassword", str_password);
                params.put("deviceId", Device_Id);
                return params;
            }
        };
        queue.add(postRequest);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        /*InputMethodManager imm = (InputMethodManager) getSystemService(Context.
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);*/
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //this.finish();
    }

    private void GetAddressDetails() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String urlmanual = url + "address.php?";
        StringRequest postRequest = new StringRequest(Request.Method.POST, urlmanual,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response666", response);
                        try {
                            final JSONArray array;
                            array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object;
                                object = new JSONObject(array.getString(i).toString());
                                String Status = object.getString("status");
                                System.out.println("aaaaddddd" + " " + Status + "   " + Str_UserId);
                                if (Status.equalsIgnoreCase("true")) {
                                    preferences = getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
                                    editor = preferences.edit();
                                    editor.putString("First_Name", object.get("First_Name").toString());
                                    editor.putString("Last_Name", object.get("Last_Name").toString());
                                    editor.putString("Update_Addess", object.get("Shipping_Address").toString());
                                    editor.putString("Update_City", object.get("Shipping_City").toString());
                                    editor.putString("Update_State", object.get("Shipping_State").toString());
                                    editor.putString("Update_Country", object.get("Shipping_Country").toString());
                                    editor.putString("Update_Landmark", object.get("Land_Mark").toString());
                                    editor.putString("Update_Pincode", object.get("Pin_Code").toString());
                                    editor.putString("Update_Status", object.get("status").toString());
                                    editor.putString("Update_Msg", object.get("Msg").toString());
                                    editor.commit();
                                    //   	  Toast.makeText(getApplicationContext(), "Donnnneeeee..... Detail Updated????", Toast.LENGTH_SHORT).show();
                                } else {

                                    //  Toast.makeText(getApplicationContext(), "Server Error.", Toast.LENGTH_SHORT).show();
                                }

                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        // Log.d("Error.Response", error.getMessage());
                    }
                }
        ) {
            @Override
            protected HashMap<String, String> getParams() {

                HashMap<String, String> params = new HashMap<String, String>();
                params.put("userId", Str_UserId);
                params.put("action", "address");    // Second one u can change
                return params;
            }
        };
        queue.add(postRequest);

    }

    public void MobileDeviceUpdate() {
        System.out.println("fiiiiii" + "    " + "aaStatus");
        RequestQueue queue = Volley.newRequestQueue(this);
        String urlmanual = url + "mobile_devices.php?";
        StringRequest postRequest = new StringRequest(Request.Method.POST, urlmanual,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        try {
                            final JSONArray array;
                            array = new JSONArray(response);
                            System.out.println("hcvcce" + "    " + "Status");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object;
                                object = new JSONObject(array.getString(i).toString());
                                String Status = object.getString("status");
                                System.out.println("hjccccc" + "    " + Status);
                                if (Status.equalsIgnoreCase("true")) {
                                    System.out.println("cccc" + "    " + Status);
                                }

                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        // Log.d("Error.Response", error.getMessage());
                    }
                }
        ) {
            @Override
            protected HashMap<String, String> getParams() {

                HashMap<String, String> params = new HashMap<String, String>();
                params.put("userId", Str_UserId);
                params.put("mid", Mob_Id);
                params.put("mproduct", Mob_Product);
                params.put("mbrand", Mob_Brand);
                params.put("mmanufacture", Mob_Manufacture);
                params.put("mmodel", Mob_Model);
                params.put("mdid", Device_Id);
			/*params.put("versionCode",Integer.toString(sVersionCode) );
			params.put("versionName", sVersionName);
			params.put("packageName", PACKAGE_NAME);*/

                return params;
            }
        };
        queue.add(postRequest);
    }

    public void fetchsearchview() {
        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
        StringRequest stringRequest = new StringRequest(Config2.DATA_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("pptt0", response);
                SHOWJSON3(response);
                if (s80.contentEquals("true")) {
                    Log.d("pptt00", response);
//                    webView.loadUrl(s83);
                } else {
                    // Toast.makeText(LoginActivity.this, "Not true", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(LoginActivity.this, "data not fetch...", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(stringRequest);
    }

    public void SHOWJSON3(String respone) {

        try {
            ArrayList al = new ArrayList();
            JSONArray jr = new JSONArray(respone);
            for (int i = 0; i < respone.length(); i++) {
                JSONObject jb1 = jr.getJSONObject(i);
                s80 = jb1.getString(Config2.DATA_Status2);
                s81 = jb1.getString(Config2.DATA_Product);
                s82 = jb1.getString(Config2.DATA_message);
                s83 = jb1.getString(Config2.DATA_result2);
                Log.d("allll", s83);
                if (jb1.has("Product_Title")) {
                    al.add(jb1.getString("Product_Title"));
                }
                adapter = new ArrayAdapter(LoginActivity.this, android.R.layout.simple_spinner_dropdown_item, al);
                searchedit.setAdapter(adapter);
                searchedit.setThreshold(0);
            }
        } catch (JSONException e) {
        }
    }

    public void searchForResults() {
        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
        StringRequest stringRequest = new StringRequest(Config2.DATA_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("RRResponse11100", response);
                //Toast.makeText(FirstBottomPage.this, "data fetch...", Toast.LENGTH_SHORT).show();
                SHOWJSON2(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Toast.makeText(FirstBottomPage.this, "data not fetch...", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);

    }

    public void SHOWJSON2(String respone) {
        try {
            Log.d("RRResponse1111", respone);
            // for(int i;i.get)
            JSONArray jr = new JSONArray(respone);
            JSONObject jb1 = jr.getJSONObject(0);
            s70 = jb1.getString(Config2.DATA_Status2);
            s71 = jb1.getString(Config2.DATA_Product);
            s72 = jb1.getString(Config2.DATA_message);
            s73 = jb1.getString(Config2.DATA_result2);
            Log.d("RRResponse1110", s70);
            Log.d("RRResponse1112", s71);
            Log.d("RRResponse1113", s72);
            Log.d("RRResponse1114", s73);

        } catch (JSONException e) {

        }
        //notificationBadge.setText(s82);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (webView2.canGoBack()) {
                        webView2.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("SetJavaScriptEnabled")
    private void startWebView(String url) {
        // Javascript inabled on webview
        webView2.requestFocus();
        webView2.getSettings().setJavaScriptEnabled(true);
        webView2.getSettings().setSavePassword(true);
        // Other webview options
        webView2.getSettings().setLoadWithOverviewMode(true);
        webView2.getSettings().setUseWideViewPort(true);
        webView2.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView2.setScrollbarFadingEnabled(false);
        webView2.getSettings().setBuiltInZoomControls(true);
        webView2.getSettings().setBuiltInZoomControls(true);
        webView2.getSettings().setAllowContentAccess(true);
        webView2.getSettings().setAllowFileAccess(true);
        webView2.getSettings().setAllowUniversalAccessFromFileURLs(true);
        webView2.getSettings().setAllowFileAccessFromFileURLs(true);
        // enable navigator.geolocation
        webView2.getSettings().setGeolocationEnabled(true);
        webView2.getSettings().setDomStorageEnabled(true);
        webView2.getWebChromeClient();
        webView2.loadUrl(url);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void weballsetting123() {
        webView2.getSettings().setJavaScriptEnabled(true);
        webView2.requestFocus();
        webView2.getSettings().setLightTouchEnabled(true);
        webView2.getSettings().setJavaScriptEnabled(true);
        webView2.getSettings().setGeolocationEnabled(true);
        webView2.setVerticalScrollBarEnabled(false);
        webView2.setHorizontalScrollBarEnabled(false);
        webView2.setSoundEffectsEnabled(true);
        WebSettings webSettings = webView2.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webView2.requestFocusFromTouch();

        webView2.setWebChromeClient(new WebChromeClient());
        webView2.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        webView2.getSettings().setUseWideViewPort(true);
        webView2.setWebViewClient(new WebViewClient());
        webView2.getWebChromeClient();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void AllBooks() {
        if (holdvalue.contentEquals("Together with English Pullout Worksheets for Class 5")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-english-pullout-worksheets-for-class-5?type=p_book&id=36");
        } else if (holdvalue.contentEquals("Together With English Pullout Worksheets Solution for Class 5")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-english-pullout-worksheets-solution-for-class-5?type=p_book&id=37");
        } else if (holdvalue.contentEquals("Together With English Pullout Worksheets for Class 6")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-english-pullout-worksheets-for-class-6?type=p_book&id=38");
        } else if (holdvalue.contentEquals("Together With English Pullout Worksheets for Class 7")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-english-pullout-worksheets-for-class-7?type=p_book&id=39");
        } else if (holdvalue.contentEquals("Together With English Pullout Worksheets Solution for Class 7")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-english-pullout-worksheets-solution-for-class-7?type=p_book&id=40");
        } else if (holdvalue.contentEquals("Together With English Pullout Worksheets for Class 8")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-english-pullout-worksheets-for-class-8?type=p_book&id=41");
        } else if (holdvalue.contentEquals("Together With English Pullout Worksheets Solution for Class 6")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-english-pullout-worksheets-solution-for-class-6?type=p_book&id=43");
        } else if (holdvalue.contentEquals("Together With English Pullout Worksheets solution for Class 8")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-english-pullout-worksheets-solution-for-class-8?type=p_book&id=44");
        } else if (holdvalue.contentEquals("")) {
            startWebView("");
        } else if (holdvalue.contentEquals("Together With NEP 2020 Based Synergy Learning And Activity Combo (4 IN 1) English Mathematics Environmental Studies, GK & Life Skil Book For Class 2 (Semester 1)")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-nep-2020-based-synergy-learning-and-activity-combo-4-in-1-english-mathematics-environmental-studies-gk-and-life-skil-book-for-class-2-semester-1?type=p_book&id=287");
        } else if (holdvalue.contentEquals("Together With NEP 2020 Based Synergy Learning And Activity Combo English Mathematics Environmental Studies, GK & Life Skill Book For Class 2 (Semester 2)")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-nep-2020-based-synergy-learning-and-activity-combo-english-mathematics-environmental-studies-gk-and-life-skill-book-for-class-2-semester-2?type=p_book&id=288");
        } else if (holdvalue.contentEquals("Together With NEP 2020 Based Synergy Learning And Activity Combo (5 IN 1) English Mathematics Science ,Social Studies , GK & Life Skills Book For Class 3 (Semester 1)")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-nep-2020-based-synergy-learning-and-activity-combo-5-in-1-english-mathematics-science-social-studies-gk-and-life-skills-book-for-class-3-semester-1?type=p_book&id=289");
        } else if (holdvalue.contentEquals("Together With NEP 2020 Based Synergy Learning And Activity Combo English Mathematics Science ,Social Studies , GK & Life Skills Book For Class 4 (Semester 1)")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-nep-2020-based-synergy-learning-and-activity-combo-english-mathematics-science-social-studies-gk-and-life-skills-book-for-class-4-semester-1?type=p_book&id=291");
        } else if (holdvalue.contentEquals("Together With NEP 2020 Based Synergy Learning And Activity Combo (5 IN 1) English Mathematics Science ,Social Studies,GK & Life Skills Book For Class 4 (Semester 2)")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-nep-2020-based-synergy-learning-and-activity-combo-5-in-1-english-mathematics-science-social-studies-gk-and-life-skills-book-for-class-4-semester-2?type=p_book&id=292");
        } else if (holdvalue.contentEquals("Together With NEP 2020 Based Synergy Learning And Activity Combo English Mathematics Science ,Social Studies , GK & Life Skills Book For Class 5 (Semester 1)")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-nep-2020-based-synergy-learning-and-activity-combo-english-mathematics-science-social-studies-gk-and-life-skills-book-for-class-5-semester-1?type=p_book&id=293");
        } else if (holdvalue.contentEquals("Together With NEP 2020 Based Synergy Learning And Activity Combo English Mathematics Science ,Social Studies , GK & Life Skills Book For Class 5 (Semester 2)")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-nep-2020-based-synergy-learning-and-activity-combo-english-mathematics-science-social-studies-gk-and-life-skills-book-for-class-5-semester-2?type=p_book&id=294");
        } else if (holdvalue.contentEquals("Together With Hands on Grammar English Work Book for Class 1")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-hands-on-grammar-english-work-book-for-class-1?type=p_book&id=375");
        } else if (holdvalue.contentEquals("Together With Hands on Grammar English Work Book for Class 2")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-hands-on-grammar-english-work-book-for-class-2?type=p_book&id=376");
        } else if (holdvalue.contentEquals("Together With Hands on Grammar English Work Book for Class 3")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-hands-on-grammar-english-work-book-for-class-3?type=p_book&id=377");
        } else if (holdvalue.contentEquals("Together With Hands on Grammar English Work Book for Class 4")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-hands-on-grammar-english-work-book-for-class-4?type=p_book&id=378");
        } else if (holdvalue.contentEquals("Together With Hands on Grammar English Work Book for Class 5")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-hands-on-grammar-english-work-book-for-class-5?type=p_book&id=379");
        } else if (holdvalue.contentEquals("Together With Expressions English Work Book for Class 6")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-expressions-english-work-book-for-class-6?type=p_book&id=380");
        } else if (holdvalue.contentEquals("Together With Expressions English Work Book for Class 7")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-expressions-english-work-book-for-class-7?type=p_book&id=381");
        } else if (holdvalue.contentEquals("Together With Expressions English Work Book for Class 8")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-expressions-english-work-book-for-class-8?type=p_book&id=382");
        } else if (holdvalue.contentEquals("Together With English Literature Reader for Class 4")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-english-literature-reader-for-class-4?type=p_book&id=383");
        } else if (holdvalue.contentEquals("Together With English Literature Reader for Class 5")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-english-literature-reader-for-class-5?type=p_book&id=384");
        } else if (holdvalue.contentEquals("Together With ICSE Expressions English Work Book for Class 2")) {
            startWebView("https://www.rachnasagar.in/mobile/icse/together-with-icse-expressions-english-work-book-for-class-2?type=p_book&id=388");
        } else if (holdvalue.contentEquals("Together With ICSE Expressions English Work Book for Class 3")) {
            startWebView("https://www.rachnasagar.in/mobile/icse/together-with-icse-expressions-english-work-book-for-class-3?type=p_book&id=389");
        } else if (holdvalue.contentEquals("Together With ICSE Expressions English Work Book for Class 4")) {
            startWebView("https://www.rachnasagar.in/mobile/icse/together-with-icse-expressions-english-work-book-for-class-4?type=p_book&id=390");
        } else if (holdvalue.contentEquals("Together With Expressions English Work Book for Class 5")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-expressions-english-work-book-for-class-5?type=p_book&id=391");
        } else if (holdvalue.contentEquals("Together With Expressions English Multiskill Coursebook (MCB) for Class 8")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-expressions-english-multiskill-coursebook-mcb-for-class-8?type=p_book&id=395");
        } else if (holdvalue.contentEquals("Together With English Pre Primer for Class LKG")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-english-pre-primer-for-class-lkg?type=p_book&id=396");
        } else if (holdvalue.contentEquals("Together With English Primer for Class UKG")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-english-primer-for-class-ukg?type=p_book&id=397");
        } else if (holdvalue.contentEquals("Together With Class UKG Term 1 Essence English, Mathematics, Environmental Studies , Art & Craft Book")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-class-ukg-term-1-essence-english-mathematics-environmental-studies-art-and-craft-book?type=p_book&id=407");
        } else if (holdvalue.contentEquals("Together With Class UKG Term 2 Essence English, Mathematics, Environmental Studies , Art & Craft Book,")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-class-ukg-term-2-essence-english-mathematics-environmental-studies-art-and-craft-book?type=p_book&id=408");
        } else if (holdvalue.contentEquals("Together With Class UKG Term 3 Essence English, Mathematics, Environmental Studies , Art & Craft Book")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-class-ukg-term-3-essence-english-mathematics-environmental-studies-art-and-craft-book?type=p_book&id=409");
        } else if (holdvalue.contentEquals("Together With Class 1 Term 2 Essence English, Mathematics, Environmental Studies , GK & Life Skills Book,")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-class-1-term-2-essence-english-mathematics-environmental-studies-gk-and-life-skills-book?type=p_book&id=411");
        } else if (holdvalue.contentEquals("Together With Class 1 Term 3 Essence English, Mathematics, Environmental Studies , Art & Craft Book")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-class-1-term-3-essence-english-mathematics-environmental-studies-art-and-craft-book?type=p_book&id=412");
        } else if (holdvalue.contentEquals("Together With Class 2 Term 1 Essence English, Mathematics, Environmental Studies, GK & Life Skills Book")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-class-2-term-1-essence-english-mathematics-environmental-studies-gk-and-life-skills-book?type=p_book&id=413");
        } else if (holdvalue.contentEquals("Together With Class 2 Term 2 Essence English, Mathematics, Environmental Studies , GK & Life Skills Book,")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-class-2-term-2-essence-english-mathematics-environmental-studies-gk-and-life-skills-book?type=p_book&id=414");
        } else if (holdvalue.contentEquals("Together With Class 2 Term 3 Essence English, Mathematics, Science, Social Studies, GK & Life Skills Book")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-class-2-term-3-essence-english-mathematics-science-social-studies-gk-and-life-skills-book?type=p_book&id=415");
        } else if (holdvalue.contentEquals("Together With Class 3 Term 1 Essence English, Mathematics, Science,Social Studies, GK & Life Skills Book,")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-class-3-term-1-essence-english-mathematics-science-social-studies-gk-and-life-skills-book?type=p_book&id=416");
        } else if (holdvalue.contentEquals("Together With Class 3 Term 2 Essence English, Mathematics, Science,Social Studies, GK & Life Skills Book")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-class-3-term-2-essence-english-mathematics-science-social-studies-gk-and-life-skills-book?type=p_book&id=417");
        } else if (holdvalue.contentEquals("Together With Class 3 Term 3 Essence English, Mathematics, Science,Social Studies , GK & Life Skills Book")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-class-3-term-3-essence-english-mathematics-science-social-studies-gk-and-life-skills-book?type=p_book&id=418");
        } else if (holdvalue.contentEquals("Together With Class 4 Term 1 Essence English, Mathematics, Environmental Studies , GK & Life Skills Book")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-class-4-term-1-essence-english-mathematics-environmental-studies-gk-and-life-skills-book?type=p_book&id=419");
        } else if (holdvalue.contentEquals("Together With Class 4 Term 2 Essence English, Mathematics, Science,Social Studies , GK & Life Skills Book")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-class-4-term-2-essence-english-mathematics-science-social-studies-gk-and-life-skills-book?type=p_book&id=420");
        } else if (holdvalue.contentEquals("Together With Class 4 Term 3 Essence English, Mathematics, Science,Social Studies , GK & Life Skills Book")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-class-4-term-3-essence-english-mathematics-science-social-studies-gk-and-life-skills-book?type=p_book&id=421");
        } else if (holdvalue.contentEquals("Together With Class 5 Term 1 Essence English, Mathematics, Science,Social Studies , GK & Life Skills Book")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-class-5-term-1-essence-english-mathematics-science-social-studies-gk-and-life-skills-book?type=p_book&id=422");
        } else if (holdvalue.contentEquals("Together With Class 5 Term 2 Essence English, Mathematics, Science,Social Studies , GK & Life Skills Book")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-class-5-term-2-essence-english-mathematics-science-social-studies-gk-and-life-skills-book?type=p_book&id=423");
        } else if (holdvalue.contentEquals("Together With Class 5 Term 3 Essence English, Mathematics, Science,Social Studies , GK & Life Skills Book")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-class-5-term-3-essence-english-mathematics-science-social-studies-gk-and-life-skills-book?type=p_book&id=424");
        } else if (holdvalue.contentEquals("Together with Expressions English Multiskill Coursebook (MCB) for Class 1")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-expressions-english-multiskill-coursebook-mcb-for-class-1?type=p_book&id=452");
        } else if (holdvalue.contentEquals("Together With ICSE Expressions English MCB (MultiSkill Coursebook) for Class 2")) {
            startWebView("https://www.rachnasagar.in/mobile/icse/together-with-icse-expressions-english-mcb-multiskill-coursebook-for-class-2?type=p_book&id=453");
        } else if (holdvalue.contentEquals("Together With ICSE Expressions English MCB (MultiSkill Coursebook) for Class 3")) {
            startWebView("https://www.rachnasagar.in/mobile/icse/together-with-icse-expressions-english-mcb-multiskill-coursebook-for-class-3?type=p_book&id=454");
        } else if (holdvalue.contentEquals("Together With ICSE Expressions English MCB (Multiskill Coursebook) for Class 4")) {
            startWebView("https://www.rachnasagar.in/mobile/icse/together-with-icse-expressions-english-mcb-multiskill-coursebook-for-class-4?type=p_book&id=455");
        } else if (holdvalue.contentEquals("Together with Expressions English Multiskill Coursebook (MCB) for Class 5")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-expressions-english-multiskill-coursebook-mcb-for-class-5?type=p_book&id=456");
        } else if (holdvalue.contentEquals("Together with Expressions English Multiskill Coursebook (MCB) for Class 6")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-expressions-english-multiskill-coursebook-mcb-for-class-6?type=p_book&id=457");
        } else if (holdvalue.contentEquals("Together with Expressions English Multiskill Coursebook (MCB) for Class 7")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-expressions-english-multiskill-coursebook-mcb-for-class-7?type=p_book&id=458");
        } else if (holdvalue.contentEquals("Together With ICSE Expressions English Work Book for Class 1")) {
            startWebView("https://www.rachnasagar.in/mobile/icse/together-with-icse-expressions-english-work-book-for-class-1?type=p_book&id=459");
        } else if (holdvalue.contentEquals("Together With ICSE Get Going English Grammar for Class 1")) {
            startWebView("https://www.rachnasagar.in/mobile/icse/together-with-icse-get-going-english-grammar-for-class-1?type=p_book&id=460");
        } else if (holdvalue.contentEquals("Together With ICSE Get Going English Grammar for Class 2")) {
            startWebView("https://www.rachnasagar.in/mobile/icse/together-with-icse-get-going-english-grammar-for-class-2?type=p_book&id=461");
        } else if (holdvalue.contentEquals("Together With ICSE Get Going English Grammar for Class 3")) {
            startWebView("https://www.rachnasagar.in/mobile/icse/together-with-icse-get-going-english-grammar-for-class-3?type=p_book&id=462");
        } else if (holdvalue.contentEquals("Together With ICSE Get Going English Grammar for Class 4")) {
            startWebView("https://www.rachnasagar.in/mobile/icse/together-with-icse-get-going-english-grammar-for-class-4?type=p_book&id=463");
        } else if (holdvalue.contentEquals("Together with Get Going English Grammar for Class 5")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-get-going-english-grammar-for-class-5?type=p_book&id=464");
        } else if (holdvalue.contentEquals("Together with Get Going English Grammar for Class 6")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-get-going-english-grammar-for-class-6?type=p_book&id=465");
        } else if (holdvalue.contentEquals("Together with Get Going English Grammar for Class 7")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-get-going-english-grammar-for-class-7?type=p_book&id=466");
        } else if (holdvalue.contentEquals("Together with Get Going English Grammar for Class 8")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-get-going-english-grammar-for-class-8?type=p_book&id=467");
        } else if (holdvalue.contentEquals("Together With Everything Blossoms C1 Recognise English Magic for Class UKG")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-everything-blossoms-c1-recognise-english-magic-for-class-ukg?type=p_book&id=517");
        } else if (holdvalue.contentEquals("Together With Everything Blossoms C2 Write English Cursive Magic for Class UKG")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-everything-blossoms-c2-write-english-cursive-magic-for-class-ukg?type=p_book&id=523");
        } else if (holdvalue.contentEquals("Together With Everything Blossoms C2 Write English Small Magic for Class UKG")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-everything-blossoms-c2-write-english-small-magic-for-class-ukg?type=p_book&id=524");
        } else if (holdvalue.contentEquals("Together With Dada Dadi Ki Kahaniyaan for Class 1 (English)")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-dada-dadi-ki-kahaniyaan-for-class-1-english?type=p_book&id=541");
        } else if (holdvalue.contentEquals("Together With Dada Dadi Ki Kahaniyaan for Class 2 (English)")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-dada-dadi-ki-kahaniyaan-for-class-2-english?type=p_book&id=542");
        } else if (holdvalue.contentEquals("Together With Dada Dadi Ki Kahaniyaan for Class 3 (English)")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-dada-dadi-ki-kahaniyaan-for-class-3-english?type=p_book&id=543");
        } else if (holdvalue.contentEquals("Together with English Multiskill Coursebook (MCB) for Class 1")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-english-multiskill-coursebook-mcb-for-class-1?type=p_book&id=548");
        } else if (holdvalue.contentEquals("Together with English Multiskill Coursebook (MCB) for Class 2")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-english-multiskill-coursebook-mcb-for-class-2?type=p_book&id=549");
        } else if (holdvalue.contentEquals("Together with English Multiskill Coursebook (MCB) for Class 3")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-english-multiskill-coursebook-mcb-for-class-3?type=p_book&id=550");
        } else if (holdvalue.contentEquals("Together with English Multiskill Coursebook (MCB) for Class 4")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-english-multiskill-coursebook-mcb-for-class-4?type=p_book&id=551");
        } else if (holdvalue.contentEquals("Together with English Multiskill Coursebook (MCB) for Class 5")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-english-multiskill-coursebook-mcb-for-class-5?type=p_book&id=552");
        } else if (holdvalue.contentEquals("Together with Stories of Panchtantra for Class 1 (English)")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-stories-of-panchtantra-for-class-1-english?type=p_book&id=565");
        } else if (holdvalue.contentEquals("Together with Stories of Panchtantra for Class 2 (English)")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-stories-of-panchtantra-for-class-2-english?type=p_book&id=566");
        } else if (holdvalue.contentEquals("Together with Stories of Panchtantra for Class 3 (English)")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-stories-of-panchtantra-for-class-3-english?type=p_book&id=567");
        } else if (holdvalue.contentEquals("Together with Stories of Panchtantra for Class 4 (English)")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-stories-of-panchtantra-for-class-4-english?type=p_book&id=568");
        } else if (holdvalue.contentEquals("Together with Stories of Panchtantra for Class 5 (English)")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-stories-of-panchtantra-for-class-5-english?type=p_book&id=569");
        } else if (holdvalue.contentEquals("Together With ICSE Expressions English Work Book for Class 6")) {
            startWebView("https://www.rachnasagar.in/mobile/icse/together-with-icse-expressions-english-work-book-for-class-6?type=p_book&id=719");
        } else if (holdvalue.contentEquals("Together With ICSE Expressions English Work Book for Class 7")) {
            startWebView("https://www.rachnasagar.in/mobile/icse/together-with-icse-expressions-english-work-book-for-class-7?type=p_book&id=720");
        } else if (holdvalue.contentEquals("Together With ICSE Expressions English Work Book for Class 8")) {
            startWebView("https://www.rachnasagar.in/mobile/icse/together-with-icse-expressions-english-work-book-for-class-8?type=p_book&id=721");
        } else if (holdvalue.contentEquals("Together with Expressions English Work Book for Class 2")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-expressions-english-work-book-for-class-2?type=p_book&id=722");
        } else if (holdvalue.contentEquals("Together with Expressions English Work Book for Class 3")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-expressions-english-work-book-for-class-3?type=p_book&id=723");
        } else if (holdvalue.contentEquals("Together with Expressions English Work Book for Class 4")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-expressions-english-work-book-for-class-4?type=p_book&id=724");
        } else if (holdvalue.contentEquals("Together With ICSE Expressions English Work Book for Class 5")) {
            startWebView("https://www.rachnasagar.in/mobile/icse/together-with-icse-expressions-english-work-book-for-class-5?type=p_book&id=725");
        } else if (holdvalue.contentEquals("Together With ICSE Expressions English MCB (Multiskill Coursebook) for Class 8")) {
            startWebView("https://www.rachnasagar.in/mobile/icse/together-with-icse-expressions-english-mcb-multiskill-coursebook-for-class-8?type=p_book&id=726");
        } else if (holdvalue.contentEquals("Together With ICSE English Pre Primer for Class LKG")) {
            startWebView("https://www.rachnasagar.in/mobile/icse/together-with-icse-english-pre-primer-for-class-lkg?type=p_book&id=727");
        } else if (holdvalue.contentEquals("Together With ICSE English Primer for Class UKG")) {
            startWebView("https://www.rachnasagar.in/mobile/icse/together-with-icse-english-primer-for-class-ukg?type=p_book&id=728");
        } else if (holdvalue.contentEquals("Together With ICSE Expressions English MCB (Multiskill Coursebook) for Class 1")) {
            startWebView("https://www.rachnasagar.in/mobile/icse/together-with-icse-expressions-english-mcb-multiskill-coursebook-for-class-1?type=p_book&id=754");
        } else if (holdvalue.contentEquals("ogether with Expressions English Multiskill Coursebook (MCB) for Class 2")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-expressions-english-multiskill-coursebook-mcb-for-class-2?type=p_book&id=755");
        } else if (holdvalue.contentEquals("Together with Expressions English Multiskill Coursebook (MCB) for Class 3")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-expressions-english-multiskill-coursebook-mcb-for-class-3?type=p_book&id=756");
        } else if (holdvalue.contentEquals("Together with Expressions English Multiskill Coursebook (MCB) for Class 4")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-expressions-english-multiskill-coursebook-mcb-for-class-4?type=p_book&id=757");
        } else if (holdvalue.contentEquals("Together With ICSE Expressions English MCB (MultiSkill Coursebook) for Class 5")) {
            startWebView("https://www.rachnasagar.in/mobile/icse/together-with-icse-expressions-english-mcb-multiskill-coursebook-for-class-5?type=p_book&id=758");
        } else if (holdvalue.contentEquals("Together With ICSE Expressions English MCB (MultiSkill Coursebook) for Class 6")) {
            startWebView("https://www.rachnasagar.in/mobile/icse/together-with-icse-expressions-english-mcb-multiskill-coursebook-for-class-6?type=p_book&id=759");
        } else if (holdvalue.contentEquals("Together With ICSE Expressions English MCB (MultiSkill Coursebook) for Class 7")) {
            startWebView("https://www.rachnasagar.in/mobile/icse/together-with-icse-expressions-english-mcb-multiskill-coursebook-for-class-7?type=p_book&id=760");
        } else if (holdvalue.contentEquals("Together with Expressions English Work Book for Class 1")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-expressions-english-work-book-for-class-1?type=p_book&id=761");
        } else if (holdvalue.contentEquals("Together with Get Going English Grammar for Class 1")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-get-going-english-grammar-for-class-1?type=p_book&id=762");
        } else if (holdvalue.contentEquals("Together with Get Going English Grammar for Class 2")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-get-going-english-grammar-for-class-2?type=p_book&id=763");
        } else if (holdvalue.contentEquals("Together with Get Going English Grammar for Class 3")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-get-going-english-grammar-for-class-3?type=p_book&id=764");
        } else if (holdvalue.contentEquals("Together with Get Going English Grammar for Class 4")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-get-going-english-grammar-for-class-4?type=p_book&id=765");
        } else if (holdvalue.contentEquals("Together With ICSE Get Going English Grammar for Class 6")) {
            startWebView("https://www.rachnasagar.in/mobile/icse/together-with-icse-get-going-english-grammar-for-class-6?type=p_book&id=767");
        } else if (holdvalue.contentEquals("Together With ICSE Get Going English Grammar for Class 7")) {
            startWebView("https://www.rachnasagar.in/mobile/icse/together-with-icse-get-going-english-grammar-for-class-7?type=p_book&id=768");
        } else if (holdvalue.contentEquals("Together With ICSE Get Going English Grammar for Class 8")) {
            startWebView("https://www.rachnasagar.in/mobile/icse/together-with-icse-get-going-english-grammar-for-class-8?type=p_book&id=769");
        } else if (holdvalue.contentEquals("Together With ICSE Everything Blossoms C1 Recognise English Magic for Class UKG")) {
            startWebView("https://www.rachnasagar.in/mobile/icse/together-with-icse-everything-blossoms-c1-recognise-english-magic-for-class-ukg?type=p_book&id=800");
        } else if (holdvalue.contentEquals("Together With ICSE Everything Blossoms C2 Write English Cursive Magic for Class UKG")) {
            startWebView("https://www.rachnasagar.in/mobile/icse/together-with-icse-everything-blossoms-c2-write-english-cursive-magic-for-class-ukg?type=p_book&id=806");
        } else if (holdvalue.contentEquals("Together With ICSE Everything Blossoms C2 Write English Small Magic for Class UKG")) {
            startWebView("https://www.rachnasagar.in/mobile/icse/together-with-icse-everything-blossoms-c2-write-english-small-magic-for-class-ukg?type=p_book&id=807");
        } else if (holdvalue.contentEquals("Together with Expressions English Literature Reader for Class 5")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-expressions-english-literature-reader-for-class-5?type=p_book&id=896");
        } else if (holdvalue.contentEquals("Together with Expressions English Literature Reader for Class 6")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-expressions-english-literature-reader-for-class-6?type=p_book&id=897");
        } else if (holdvalue.contentEquals("Together with Expressions English Literature Reader for Class 8")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-expressions-english-literature-reader-for-class-8?type=p_book&id=898");
        } else if (holdvalue.contentEquals("Together with Expressions English Literature Reader for Class 7")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-expressions-english-literature-reader-for-class-7?type=p_book&id=899");
        } else if (holdvalue.contentEquals("Together with Expressions English Literature Reader for Class 4")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-expressions-english-literature-reader-for-class-4?type=p_book&id=900");
        } else if (holdvalue.contentEquals("Together with Expressions English Literature Reader for Class 3")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-expressions-english-literature-reader-for-class-3?type=p_book&id=901");
        } else if (holdvalue.contentEquals("Together with Expressions English Literature Reader for Class 2")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-expressions-english-literature-reader-for-class-2?type=p_book&id=902");
        } else if (holdvalue.contentEquals("Together with Expressions English Literature Reader for Class 1")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-expressions-english-literature-reader-for-class-1?type=p_book&id=903");
        } else if (holdvalue.contentEquals("Together with Expressions English Solution/TRM for Class 1")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-expressions-english-solution-trm-for-class-1?type=p_book&id=922");
        } else if (holdvalue.contentEquals("Together with Expressions English Solution/TRM for Class 2")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-expressions-english-solution-trm-for-class-2?type=p_book&id=923");
        } else if (holdvalue.contentEquals("Together with Expressions English Solution/TRM for Class 3")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-expressions-english-solution-trm-for-class-3?type=p_book&id=924");
        } else if (holdvalue.contentEquals("Together with Expressions English Solution/TRM for Class 4")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-expressions-english-solution-trm-for-class-4?type=p_book&id=925");
        } else if (holdvalue.contentEquals("Together with Expressions English Solution/TRM for Class 5")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-expressions-english-solution-trm-for-class-5?type=p_book&id=926");
        } else if (holdvalue.contentEquals("Together with Expressions English Solution/TRM for Class 6")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-expressions-english-solution-trm-for-class-6?type=p_book&id=927");
        } else if (holdvalue.contentEquals("Together with Expressions English Solution/TRM for Class 7")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-expressions-english-solution-trm-for-class-7?type=p_book&id=928");
        } else if (holdvalue.contentEquals("Together with Expressions English Solution/TRM for Class 8")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-expressions-english-solution-trm-for-class-8?type=p_book&id=929");
        } else if (holdvalue.contentEquals("Together with Expressions English Worksheet for Class 1")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-expressions-english-worksheet-for-class-1?type=p_book&id=963");
        } else if (holdvalue.contentEquals("Forever With English Multiskill Coursebook for Class 1")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/forever-with-english-multiskill-coursebook-for-class-1?type=p_book&id=1107");
        } else if (holdvalue.contentEquals("Forever with English Multiskill Coursebook for Class 2")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/forever-with-english-multiskill-coursebook-for-class-2?type=p_book&id=1112");
        } else if (holdvalue.contentEquals("Forever with English Multiskill Coursebook for Class 3")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/forever-with-english-multiskill-coursebook-for-class-3?type=p_book&id=1117");
        } else if (holdvalue.contentEquals("Forever with English Multiskill Coursebook for Class 4")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/forever-with-english-multiskill-coursebook-for-class-4?type=p_book&id=1122");
        } else if (holdvalue.contentEquals("Forever with English Multiskill Coursebook for Class 5")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/forever-with-english-multiskill-coursebook-for-class-5?type=p_book&id=1128");
        } else if (holdvalue.contentEquals("Together with Expressions English Worksheets for Class 5")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-expressions-english-worksheets-for-class-5?type=p_book&id=1144");
        } else if (holdvalue.contentEquals("Together with Expressions English Worksheets for Class 2")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-expressions-english-worksheets-for-class-2?type=p_book&id=1145");
        } else if (holdvalue.contentEquals("Together with Expressions English Worksheets for Class 4")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-expressions-english-worksheets-for-class-4?type=p_book&id=1146");
        } else if (holdvalue.contentEquals("Together with Expressions English Worksheets for Class 3")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-expressions-english-worksheets-for-class-3?type=p_book&id=1147");
        } else if (holdvalue.contentEquals("Together With Peek-a-boo English Magic Rhyme Book A for Nursery")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-peek-a-boo-english-magic-rhyme-book-a-for-nursery?type=p_book&id=1190");
        } else if (holdvalue.contentEquals("Together With Peek-a-boo English Magic Rhyme Book B for LKG")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-peek-a-boo-english-magic-rhyme-book-b-for-lkg?type=p_book&id=1191");
        } else if (holdvalue.contentEquals("Together With Peek-a-boo English Magic Rhyme Book C")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-peek-a-boo-english-magic-rhyme-book-c?type=p_book&id=1192");
        } else if (holdvalue.contentEquals("Honeydew English NCERT Workbook cum Practice Material for Class 8")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/honeydew-english-ncert-workbook-cum-practice-material-for-class-8?type=p_book&id=1199");
        } else if (holdvalue.contentEquals("Honeycomb English NCERT Workbook cum Practice Material for Class 7")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/honeycomb-english-ncert-workbook-cum-practice-material-for-class-7?type=p_book&id=1205");
        } else if (holdvalue.contentEquals("Honeysuckle English NCERT Workbook cum Practice Material for Class 6")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/honeysuckle-english-ncert-workbook-cum-practice-material-for-class-6?type=p_book&id=1211");
        } else if (holdvalue.contentEquals("Marigold English NCERT Workbook cum Practice Material for Class 5")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/marigold-english-ncert-workbook-cum-practice-material-for-class-5?type=p_book&id=1217");
        } else if (holdvalue.contentEquals("Marigold English NCERT Workbook cum Practice Material for Class 4")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/marigold-english-ncert-workbook-cum-practice-material-for-class-4?type=p_book&id=1223");
        } else if (holdvalue.contentEquals("Marigold English NCERT Workbook cum Practice Material for Class 3")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/marigold-english-ncert-workbook-cum-practice-material-for-class-3?type=p_book&id=1227");
        } else if (holdvalue.contentEquals("Marigold English NCERT Workbook cum Practice Material for Class 2")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/marigold-english-ncert-workbook-cum-practice-material-for-class-2?type=p_book&id=1231");
        } else if (holdvalue.contentEquals("Marigold English NCERT Workbook cum Practice Material for Class 1")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/marigold-english-ncert-workbook-cum-practice-material-for-class-1?type=p_book&id=1232");
        } else if (holdvalue.contentEquals("Together with Peek-a-boo English Magic Alphabet Recognition A for Nursery")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-peek-a-boo-english-magic-alphabet-recognition-a-for-nursery?type=p_book&id=1244");
        } else if (holdvalue.contentEquals("Together With Peek-a-boo English Magic B2 Capital Letters")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-peek-a-boo-english-magic-b-capital-letters?type=p_book&id=1245");
        } else if (holdvalue.contentEquals("Together With Peek-a-boo English Magic B3(Capital and Small Letters)")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-peek-a-boo-english-magic-b-capital-and-small-letters?type=p_book&id=1246");
        } else if (holdvalue.contentEquals("Together With Peek-a-boo English Magic B1 (Capital Letters ) Strockwise")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-peek-a-boo-english-magic-b-capital-letters-strockwise?type=p_book&id=1247");
        } else if (holdvalue.contentEquals("Together With Peek-a-boo English Magic C")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-peek-a-boo-english-magic-c?type=p_book&id=1248");
        } else if (holdvalue.contentEquals("Together With Peek-a-boo English Magic (Writing Booklet ) Word Formation")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-peek-a-boo-english-magic-writing-booklet-word-formation?type=p_book&id=1251");
        } else if (holdvalue.contentEquals("Together With Peek-a-boo English Magic Writing Booklet( Capital Letters) for LKG")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-peek-a-boo-english-magic-writing-booklet-capital-letters-for-lkg?type=p_book&id=1252");
        } else if (holdvalue.contentEquals("Together With Peek-a-boo English Magic Writing Booklet( Capital and Small Letters)")) {
            startWebView("https://www.rachnasagar.in/mobile/icse/together-with-peek-a-boo-english-magic-writing-booklet-capital-and-small-letter?type=p_book&id=1253");
        } else if (holdvalue.contentEquals("Together With Peek-a-boo English Magic Writing Booklet (Cursive Letters)")) {
            startWebView("https://www.rachnasagar.in/mobile/icse/together-with-peek-a-boo-english-magic-writing-booklet-cursive-letter?type=p_book&id=1254");
        } else if (holdvalue.contentEquals("Together With Peek-a-boo English Magic Writing Booklet (Capital Letters) Strockwise for LKG")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-peek-a-boo-english-magic-writing-booklet-capital-letters-strockwise-for-lkg?type=p_book&id=1255");
        } else if (holdvalue.contentEquals("Together With CBSE Class 12 Physical Education (English Medium) Question Bank/Study Material Exam 2023 (Based on the latest Syllabus)")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-cbse-class-12-physical-education-english-medium-question-bank-study-material-exam-2023-based-on-the-latest-syllabus?type=p_book&id=1328");
        } else if (holdvalue.contentEquals("Together With CBSE Class 10 English Language & Literature Question Bank/Study Material Exam 2023 (Based on the latest Syllabus)")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-cbse-class-10-english-language-and-literature-question-bank-study-material-exam-2023-based-on-the-latest-syllabus?type=p_book&id=1332");
        } else if (holdvalue.contentEquals("Together With CBSE Class 9 English Language Literature Question Bank/Study Material Exam 2023 (Based on the latest Syllabus)")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-cbse-class-9-english-language-literature-question-bank-study-material-exam-2023-based-on-the-latest-syllabus?type=p_book&id=1337");
        } else if (holdvalue.contentEquals("Together With CBSE Class 12 English Core Question Bank/Study Material Exam 2023 (Based on the latest Syllabus)")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-cbse-class-12-english-core-question-bank-study-material-exam-2023-based-on-the-latest-syllabus?type=p_book&id=1360");
        } else if (holdvalue.contentEquals("Together with CBSE Class 10 English Language & Literature Previous 10+ Years' Chapter wise Solved Question Bank Exam 2022-23 (Based on the latest syllabus)")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-cbse-class-10-english-language-literature-previous-10-years-chapter-wise-solved-question-bank-exam-2022-23-based-on-the-latest-syllabus?type=p_book&id=1361");
        } else if (holdvalue.contentEquals("Together with CBSE Class 12 English Core Previous 10+ Years' Chapter wise Solved Question Bank Exam 2022-23 (Based on the latest syllabus)")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-cbse-class-12-english-core-previous-10-years-chapter-wise-solved-question-bank-exam-2022-23-based-on-the-latest-syllabus?type=p_book&id=1395");
        } else if (holdvalue.contentEquals("Together With Peek-a-boo English Magic Rhyme Book A for Nursery")) {
            startWebView("https://www.rachnasagar.in/mobile/icse/together-with-peek-a-boo-english-magic-rhyme-book-a-for-nurser?type=p_book&id=1401");
        } else if (holdvalue.contentEquals("Together with Peek-a-boo English Magic Alphabet Recognition A for Nursery")) {
            startWebView("https://www.rachnasagar.in/mobile/icse/together-with-peek-a-boo-english-magic-alphabet-recognition-a-for-nurser?type=p_book&id=1402");
        } else if (holdvalue.contentEquals("Together With Peek-a-boo English Magic Writing Booklet (Word Formation)")) {
            startWebView("https://www.rachnasagar.in/mobile/icse/together-with-peek-a-boo-english-magic-writing-booklet-word-formatio?type=p_book&id=1403");
        } else if (holdvalue.contentEquals("Together With ICSE Peek-a-boo English Magic Rhyme Book B for LKG")) {
            startWebView("https://www.rachnasagar.in/mobile/icse/together-with-icse-peek-a-boo-english-magic-rhyme-book-b-for-lkg?type=p_book&id=1413");
        } else if (holdvalue.contentEquals("Together With Peek-a-boo English Magic B (Capital Letters)")) {
            startWebView("https://www.rachnasagar.in/mobile/icse/together-with-peek-a-boo-english-magic-b-capital-letter?type=p_book&id=1414");
        } else if (holdvalue.contentEquals("Together With Peek-a-boo English Magic B (Capital and Small Letters)")) {
            startWebView("https://www.rachnasagar.in/mobile/icse/together-with-peek-a-boo-english-magic-b-capital-and-small-letter?type=p_book&id=1415");
        } else if (holdvalue.contentEquals("Together With Peek-a-boo English Magic B (Capital Letters ) Strockwise")) {
            startWebView("https://www.rachnasagar.in/mobile/icse/together-with-peek-a-boo-english-magic-b-capital-letters-strockwis?type=p_book&id=1416");
        } else if (holdvalue.contentEquals("Together With ICSE Peek-a-boo English Magic Writing Booklet (Capital Letters) for LKG")) {
            startWebView("https://www.rachnasagar.in/mobile/icse/together-with-icse-peek-a-boo-english-magic-writing-booklet-capital-letters-for-lkg?type=p_book&id=1417");
        } else if (holdvalue.contentEquals("Together With Peek-a-boo English Magic Writing Booklet (Capital and Small Letters)")) {
            startWebView("https://www.rachnasagar.in/mobile/state/together-with-peek-a-boo-english-magic-writing-booklet-capital-and-small-lette?type=p_book&id=1418");
        } else if (holdvalue.contentEquals("Together With Peek-a-boo English Magic Writing Booklet( Cursive Letters)")) {
            startWebView("https://www.rachnasagar.in/mobile/state/together-with-peek-a-boo-english-magic-writing-booklet-cursive-lette?type=p_book&id=1419");
        } else if (holdvalue.contentEquals("Together With ICSE Peek-a-boo English Magic Writing Booklet (Capital Letters) Strockwise for LKG")) {
            startWebView("https://www.rachnasagar.in/mobile/icse/together-with-icse-peek-a-boo-english-magic-writing-booklet-capital-letters-strockwise-for-lkg?type=p_book&id=1420");
        } else if (holdvalue.contentEquals("Together With Peek-a-boo English Magic Rhyme Book C")) {
            startWebView("https://www.rachnasagar.in/mobile/icse/together-with-peek-a-boo-english-magic-rhyme-book-?type=p_book&id=1428");
        } else if (holdvalue.contentEquals("Together With Peek-a-boo English Magic C")) {
            startWebView("https://www.rachnasagar.in/mobile/icse/together-with-peek-a-boo-eng-magic-c?type=p_book&id=1429");
        } else if (holdvalue.contentEquals("Together with Peek-a-boo English Magic Alphabet Recognition A for Nursery")) {
            startWebView("https://www.rachnasagar.in/mobile/state/together-with-peek-a-boo-english-magic-alphabet-recognition-a-for-nurse?type=p_book&id=1440");
        } else if (holdvalue.contentEquals("Together With Peek-a-boo English Magic Writing Booklet ( Word Formation)")) {
            startWebView("https://www.rachnasagar.in/mobile/state/together-with-peek-a-boo-english-magic-writing-booklet-word-formati?type=p_book&id=1441");
        } else if (holdvalue.contentEquals("Together With Peek-a-boo English Magic B (Capital Letters)")) {
            startWebView("https://www.rachnasagar.in/mobile/state/together-with-peek-a-boo-english-magic-b-capital-lette?type=p_book&id=1452");
        } else if (holdvalue.contentEquals("Together With Peek-a-boo English Magic B (Capital and Small Letters)")) {
            startWebView("https://www.rachnasagar.in/mobile/state/together-with-peek-a-boo-english-magic-b-capital-and-small-lette?type=p_book&id=1453");
        } else if (holdvalue.contentEquals("Together With Peek-a-boo English Magic B (Capital Letters) Strockwise")) {
            startWebView("https://www.rachnasagar.in/mobile/state/together-with-peek-a-boo-english-magic-b-capital-letters-strockwi?type=p_book&id=1454");
        } else if (holdvalue.contentEquals("Together With Peek-a-boo English Magic Writing Booklet (Capital Letters) for LKG")) {
            startWebView("https://www.rachnasagar.in/mobile/state/together-with-peek-a-boo-english-magic-writing-booklet-capital-letters-for-lk?type=p_book&id=1455");
        } else if (holdvalue.contentEquals("Together With Peek-a-boo English Magic Writing Booklet B3(Capital and Small Letters)")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-peek-a-boo-english-magic-writing-booklet-capital-and-small-letters?type=p_book&id=1456");
        } else if (holdvalue.contentEquals("Together With Peek-a-boo English Magic Writing Booklet (Cursive Letters)")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-peek-a-boo-english-magic-writing-booklet-cursive-letters?type=p_book&id=1457");
        } else if (holdvalue.contentEquals("Together With Peek-a-boo English Magic Writing Booklet (Capital Letters) Strockwise for LKG")) {
            startWebView("https://www.rachnasagar.in/mobile/state/together-with-peek-a-boo-english-magic-writing-booklet-capital-letters-strockwise-for-lk?type=p_book&id=1458");
        } else if (holdvalue.contentEquals("Together With Peek-a-boo English Magic Rhyme Book C")) {
            startWebView("https://www.rachnasagar.in/mobile/state/together-with-peek-a-boo-english-magic-rhyme-book?type=p_book&id=1461");
        } else if (holdvalue.contentEquals("Together With Peek-a-boo English Magic C")) {
            startWebView("https://www.rachnasagar.in/mobile/state/together-with-peek-a-boo-english-magicc?type=p_book&id=1462");
        } else if (holdvalue.contentEquals("Marigold English NCERT Workbook Solution /TRM for Class 1")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/marigold-english-ncert-workbook-solution-trm-for-class-1?type=p_book&id=1471");
        } else if (holdvalue.contentEquals("Marigold English NCERT Workbook Solution /TRM for Class 2")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/marigold-english-ncert-workbook-solution-trm-for-class-2?type=p_book&id=1472");
        } else if (holdvalue.contentEquals("Marigold English NCERT Workbook Solution /TRM for Class 3")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/marigold-english-ncert-workbook-solution-trm-for-class-3?type=p_book&id=1473");
        } else if (holdvalue.contentEquals("Marigold English NCERT Workbook Solution /TRM for Class 4")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/marigold-english-ncert-workbook-solution-trm-for-class-4?type=p_book&id=1474");
        } else if (holdvalue.contentEquals("arigold English NCERT Workbook Solution /TRM for Class 5")) {
            startWebView("ttps://www.rachnasagar.in/mobile/cbse/marigold-english-ncert-workbook-solution-trm-for-class-5?type=p_book&id=1475");
        } else if (holdvalue.contentEquals("Honeysuckle English NCERT Workbook Solution /TRM for Class 6")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/honeysuckle-english-ncert-workbook-solution-trm-for-class-6?type=p_book&id=1476");
        } else if (holdvalue.contentEquals("Honeycomb English NCERT Workbook Solution/TRM for Class 7")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/honeycomb-english-ncert-workbook-solution-trm-for-class-7?type=p_book&id=1477");
        } else if (holdvalue.contentEquals("Honeydew English NCERT Workbook Solution/TRM for Class 8")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/honeydew-english-ncert-workbook-solution-trm-for-class-8?type=p_book&id=1478");
        } else if (holdvalue.contentEquals("Together With CBSE Class 11 English Core Question Bank/Study Material Exam 2023 (Based on the latest Syllabus)")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-cbse-class-11-english-core-question-bank-study-material-exam-2023-based-on-the-latest-syllabus?type=p_book&id=1503");
        } else if (holdvalue.contentEquals("Together with English A DAV 1 and 2 Term Practice Material for Class 8")) {
            startWebView("https://www.rachnasagar.in/mobile//together-with-english-a-dav-1-and-2-term-practice-material-for-class-8?type=p_book&id=1509");
        } else if (holdvalue.contentEquals("Together With CBSE Class 9 English Language Literature POW Question Bank/Study Material Exam 2023 (Based on the latest Syllabus)")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-cbse-class-9-english-language-literature-pow-question-bank-study-material-exam-2023-based-on-the-latest-syllabus?type=p_book&id=1517");
        } else if (holdvalue.contentEquals("Rachna Sagar English Educational Games: You Go Left! I Go Right! for Age 5+")) {
            startWebView("https://www.rachnasagar.in/mobile/educational-kits/rachna-sagar-english-educational-games:-you-go-left!-i-go-right!-for-age-5-plush?type=p_book&id=1585");
        } else if (holdvalue.contentEquals("Rachna Sagar English Educational Games: Word Bingo for Age 5+")) {
            startWebView("https://www.rachnasagar.in/mobile/educational-kits/rachna-sagar-english-educational-games:-word-bingo-for-age-5-plush?type=p_book&id=1586");
        } else if (holdvalue.contentEquals("Rachna Sagar English Educational Games: It's Time To Learn Time! for Age 5+")) {
            startWebView("https://www.rachnasagar.in/mobile//rachna-sagar-english-educational-games:-it-s-time-to-learn-time!-for-age-5-plush?type=p_book&id=1587");
        } else if (holdvalue.contentEquals("Rachna Sagar English Educational Games: The Tense Maker for Age 5+")) {
            startWebView("https://www.rachnasagar.in/mobile/educational-kits/rachna-sagar-english-educational-games:-the-tense-maker-for-age-5-plush?type=p_book&id=1588");
        } else if (holdvalue.contentEquals("Together with CBSE Pariksha Pre-Board Papers English Core for Class 12 for Board Examination 2022-23")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-cbse-pariksha-pre-board-papers-english-core-for-class-12-for-board-examination-2022-23?type=p_book&id=1591");
        } else if (holdvalue.contentEquals("Rachna Sagar English Educational Games: Be An Action for Age 5+")) {
            startWebView("https://www.rachnasagar.in/mobile/educational-kits/rachna-sagar-english-educational-games:-be-an-action-for-age-5-plush?type=p_book&id=1605");
        } else if (holdvalue.contentEquals("Rachna Sagar English Educational Games: See Me! Spell Me! for Age 5+")) {
            startWebView("https://www.rachnasagar.in/mobile/educational-kits/rachna-sagar-english-educational-games:-see-me!-spell-me!-for-age-5-plush?type=p_book&id=1606");
        } else if (holdvalue.contentEquals("Rachna Sagar English Educational Games: King! I am the King! for Age 5+")) {
            startWebView("https://www.rachnasagar.in/mobile/educational-kits/rachna-sagar-english-educational-games:-king!-i-am-the-king!-for-age-5-plush?type=p_book&id=1607");
        } else if (holdvalue.contentEquals("Rachna Sagar English Educational Games: Check By Quality! Sweet or Salty? for Age 5+")) {
            startWebView("https://www.rachnasagar.in/mobile/educational-kits/rachna-sagar-english-educational-games:-check-by-quality!-sweet-or-salty?-for-age-5-plush?type=p_book&id=1608");
        } else if (holdvalue.contentEquals("Together with Happy Learning Pull-out Worksheets English A for Nursery")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-happy-learning-pull-out-worksheets-english-a-for-nursery?type=p_book&id=1630");
        } else if (holdvalue.contentEquals("Together with Happy Learning Pull-out Worksheets English B for LKG")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-happy-learning-pull-out-worksheets-english-b-for-lkg?type=p_book&id=1631");
        } else if (holdvalue.contentEquals("Together with Happy Learning Pull-out Worksheets English C for UKG")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-happy-learning-pull-out-worksheets-english-c-for-ukg?type=p_book&id=1632");
        } else if (holdvalue.contentEquals("Together with Happy Learning Pull-out Worksheets English for Class 1")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-happy-learning-pull-out-worksheets-english-for-class-1?type=p_book&id=1633");
        } else if (holdvalue.contentEquals("Together with Happy Learning Pull-out Worksheets English for Class 2")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-happy-learning-pull-out-worksheets-english-for-class-2?type=p_book&id=1634");
        } else if (holdvalue.contentEquals("Together with Happy Learning Pull-out Worksheets English for Class 3")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-happy-learning-pull-out-worksheets-english-for-class-3?type=p_book&id=1635");
        } else if (holdvalue.contentEquals("Together with Happy Learning Pull-out Worksheets English for Class 4")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-happy-learning-pull-out-worksheets-english-for-class-4?type=p_book&id=1636");
        } else if (holdvalue.contentEquals("Together with Happy Learning Pull-out Worksheets English for Class 5")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-happy-learning-pull-out-worksheets-english-for-class-5?type=p_book&id=1637");
        } else if (holdvalue.contentEquals("Together with Happy Learning Pull-out Worksheets English for Class 6")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-happy-learning-pull-out-worksheets-english-for-class-6?type=p_book&id=1638");
        } else if (holdvalue.contentEquals("Together with Happy Learning Pull-out Worksheets English for Class 7")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-happy-learning-pull-out-worksheets-english-for-class-7?type=p_book&id=1639");
        } else if (holdvalue.contentEquals("Together with Happy Learning Pull-out Worksheets English for Class 8")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-happy-learning-pull-out-worksheets-english-for-class-8?type=p_book&id=1640");
        } else if (holdvalue.contentEquals("Forever with English Multiskill Coursebook for Class 7")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/forever-with-english-multiskill-coursebook-for-class-7?type=p_book&id=1680");
        } else if (holdvalue.contentEquals("Forever with English Multiskill Coursebook for Class 8")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/forever-with-english-multiskill-coursebook-for-class-8?type=p_book&id=1681");
        } else if (holdvalue.contentEquals("Together With CBSE Class 11 English Core Pull Out Worksheets Question Bank Exam 2022-23 ( Based On Latest Syllabus)")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-cbse-class-11-english-core-pull-out-worksheets-question-bank-exam-2022-23-based-on-latest-syllabus?type=p_book&id=1697");
        } else if (holdvalue.contentEquals("Together With CBSE Class 12 English Core POW Question Bank/Study Material Exam 2023 (Based on the latest Syllabus)")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-cbse-class-12-english-core-pow-question-bank-study-material-exam-2023-based-on-the-latest-syllabus?type=p_book&id=1728");
        } else if (holdvalue.contentEquals("Together With ICSE Class 9 & 10 English Literature Question Bank Exam 2022-23 (Based on the latest Syllabus)")) {
            startWebView("https://www.rachnasagar.in/mobile/icse/together-with-icse-class-9-and-10-english-literature-question-bank-exam-2022-23-based-on-the-latest-syllabus?type=p_book&id=1741");
        } else if (holdvalue.contentEquals("Together With CBSE Class 10 English Language & Literature POW Question Bank/Study Material Exam 2023 (Based on the latest Syllabus)")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-cbse-class-10-english-language-and-literature-pow-question-bank-study-material-exam-2023-based-on-the-latest-syllabus?type=p_book&id=1742");
        } else if (holdvalue.contentEquals("Together With CBSE Class 10 English Language & Literature POW Solutions Question Bank/Study Material Exam 2023 (Based on the latest Syllabus)")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-cbse-class-10-english-language-and-literature-pow-solutions-question-bank-study-material-exam-2023-based-on-the-latest-syllabus?type=p_book&id=1743");
        } else if (holdvalue.contentEquals("Weaving Words English Table Calendar")) {
            startWebView("https://www.rachnasagar.in/mobile/educational-kits/weaving-words-english-table-calendar?type=p_book&id=1800");
        } else if (holdvalue.contentEquals("English Alphabet (Capital Letters)")) {
            startWebView("https://www.rachnasagar.in/mobile/educational-kits/english-alphabet-capital-letters?type=p_book&id=1809");
        } else if (holdvalue.contentEquals("English Alphabet (Capital and Small Letters)")) {
            startWebView("https://www.rachnasagar.in/mobile/educational-kits/english-alphabet-capital-and-small-letters?type=p_book&id=1810");
        } else if (holdvalue.contentEquals("Together With CBSE Class 9 English Language Literature POW Solution Question Bank/Study Material Exam 2023 (Based on the latest Syllabus)")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-cbse-class-9-english-language-literature-pow-solution-question-bank-study-material-exam-2023-based-on-the-latest-syllabus?type=p_book&id=1842");
        } else if (holdvalue.contentEquals("Together With CBSE Class 12 English Core POW Solution Question Bank/Study Material Exam 2023 (Based on the latest Syllabus)")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-cbse-class-12-english-core-pow-solution-question-bank-study-material-exam-2023-based-on-the-latest-syllabus?type=p_book&id=1873");
        } else if (holdvalue.contentEquals("Together with CBSE Pariksha Pre-Board Papers English Language & Literature Class 10 for Board Examination 2022-23")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-cbse-pariksha-pre-board-papers-english-language-literature-class-10-for-board-examination-2022-23?type=p_book&id=1888");
        } else if (holdvalue.contentEquals("Together With Peek-a-boo English Magic A Alphabet Recognition Perforated Practice Worksheet")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-peek-a-boo-english-magic-a-alphabet-recognition-perforated-practice-worksheet?type=p_book&id=1909");
        } else if (holdvalue.contentEquals("Together With Peek-a-boo English Magic B Perforated Practice worksheet")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-peek-a-boo-english-magic-b-perforated-practice-worksheet?type=p_book&id=1910");
        } else if (holdvalue.contentEquals("Together With Peek-a-boo English Magic C Perforated Practice worksheet")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-peek-a-boo-english-magic-c-perforated-practice-worksheet?type=p_book&id=1911");
        } else if (holdvalue.contentEquals("Together With Peek-a-boo English Magic A Alphabet Recognition TRM")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-peek-a-boo-english-magic-a-alphabet-recognition-trm?type=p_book&id=1929");
        } else if (holdvalue.contentEquals("Together With Peek-a-boo English Magic B1 (Capital letters -stroke Wise) TRM")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-peek-a-boo-english-magic-b1-capital-letters-stroke-wise-trm?type=p_book&id=1930");
        } else if (holdvalue.contentEquals("Together With Peek-a-boo English Magic B2 (Capital Letters) TRM")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-peek-a-boo-english-magic-b2-capital-letters-trm?type=p_book&id=1931");
        } else if (holdvalue.contentEquals("Together With Peek-a-boo English Magic B3 (Capital and Small letters) TRM")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-peek-a-boo-english-magic-b3-capital-and-small-letters-trm?type=p_book&id=1932");
        } else if (holdvalue.contentEquals("Together With Peek-a-boo English Magic C TRM")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-peek-a-boo-english-magic-c-trm?type=p_book&id=1933");
        } else if (holdvalue.contentEquals("Together With Peek-a-boo English Magic Rhyme book Series A,B,C TRM")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-peek-a-boo-english-magic-rhyme-book-series-a-b-c-trm?type=p_book&id=1934");
        } else if (holdvalue.contentEquals("Forever With English Teachers Resources manual for class 1")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/forever-with-english-teachers-resources-manual-for-class-1?type=p_book&id=1983");
        } else if (holdvalue.contentEquals("Forever With English Teachers Resources manual for class 2")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/forever-with-english-teachers-resources-manual-for-class-2?type=p_book&id=1984");
        } else if (holdvalue.contentEquals("Forever With English Teachers Resources manual for class 3")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/forever-with-english-teachers-resources-manual-for-class-3?type=p_book&id=1985");
        } else if (holdvalue.contentEquals("Forever With English Teachers Resources manual for class 4")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/forever-with-english-teachers-resources-manual-for-class-4?type=p_book&id=1986");
        } else if (holdvalue.contentEquals("Forever With English Teachers Resources manual for class 5")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/forever-with-english-teachers-resources-manual-for-class-5?type=p_book&id=1987");
        } else if (holdvalue.contentEquals("Forever With English Teachers Resource manual for class 6")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/forever-with-english-teachers-resource-manual-for-class-6?type=p_book&id=1988");
        } else if (holdvalue.contentEquals("Forever With English Teachers Resource manual for class 7")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/forever-with-english-teachers-resource-manual-for-class-7?type=p_book&id=1989");
        } else if (holdvalue.contentEquals("Forever With English Teachers Resource manual for class 8")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/forever-with-english-teachers-resource-manual-for-class-8?type=p_book&id=1990");
        } else if (holdvalue.contentEquals("Forever with English Practice Material for Class 1")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/forever-with-english-practice-material-for-class-1?type=p_book&id=1991");
        } else if (holdvalue.contentEquals("Forever with English Practice Material for Class 2")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/forever-with-english-practice-material-for-class-2?type=p_book&id=1992");
        } else if (holdvalue.contentEquals("Forever with English Practice Material for class 3")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/forever-with-english-practice-material-for-class-3?type=p_book&id=1993");
        } else if (holdvalue.contentEquals("Forever with English Practice Material for Class 4")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/forever-with-english-practice-material-for-class-4?type=p_book&id=1994");
        } else if (holdvalue.contentEquals("Forever with English Practice Material for class 5")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/forever-with-english-practice-material-for-class-5?type=p_book&id=1995");
        } else if (holdvalue.contentEquals("Forever with English Multiskill Coursebook for Class 6")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/forever-with-english-multiskill-coursebook-for-class-6?type=p_book&id=2022");
        } else if (holdvalue.contentEquals("Together With CBSE Pariksha Pre-Board Papers Class 10 English Language & Literature, Mathematics standard, Social Science (Set of 3) for Examination 2023")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-cbse-pariksha-pre-board-papers-class-10-english-language-literature-mathematics-standard-social-science-set-of-3-for-examination-2023?type=p_book&id=2042");
        } else if (holdvalue.contentEquals("Together With CBSE Pariksha Pre-Board Papers Class 10 English Language & Literature, Mathematics standard, Science (Set of 3) for Examination 2023")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-cbse-pariksha-pre-board-papers-class-10-english-language-literature-mathematics-standard-science-set-of-3-for-examination-2023?type=p_book&id=2043");
        } else if (holdvalue.contentEquals("Together With CBSE Pariksha Pre-Board Papers Class 10 English Language & Literature, Hindi B, Mathematics standard (Set of 3) for Examination 2023")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-cbse-pariksha-pre-board-papers-class-10-english-language-literature-hindi-b-mathematics-standard-set-of-3-for-examination-2023?type=p_book&id=2044");
        } else if (holdvalue.contentEquals("Together With CBSE Pariksha Pre-Board Papers Class 10 English Language & Literature, Hindi A, Mathematics standard (Set of 3) for Examination 2023")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-cbse-pariksha-pre-board-papers-class-10-english-language-literature-hindi-a-mathematics-standard-set-of-3-for-examination-2023?type=p_book&id=2045");
        } else if (holdvalue.contentEquals("Together With CBSE Pariksha Pre-Board Papers Class 10 English Language & Literature, Hindi A, Mathematics standard, Science, Social Science (Set of 5) for Examination 2023")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-cbse-pariksha-pre-board-papers-class-10-english-language-literature-hindi-a-mathematics-standard-science-social-science-set-of-5-for-examination-2023?type=p_book&id=2048");
        } else if (holdvalue.contentEquals("Together With CBSE Pariksha Pre-Board Papers Class 12 Accountancy, Mathematics, Business Studies, Economics, English Core (Set of 5) for Examination 2023")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-cbse-pariksha-pre-board-papers-class-12-accountancy-mathematics-business-studies-economics-english-core-set-of-5-for-examination-2023?type=p_book&id=2051");
        } else if (holdvalue.contentEquals("Together With CBSE English Core Sample Papers & Pre-Board Pariksha Papers (Combo) Class 12 for Board Examination 2023")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-cbse-english-core-sample-papers-pre-board-pariksha-papers-combo-class-12-for-board-examination-2023?type=p_book&id=2172");
        } else if (holdvalue.contentEquals("Together With CBSE English Language & Literature Sample Papers & Pre-Board Pariksha Papers (Combo) Class 10 for Board Examination 2023")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-cbse-english-language-literature-sample-papers-pre-board-pariksha-papers-combo-class-10-for-board-examination-2023?type=p_book&id=2183");
        } else if (holdvalue.contentEquals("Together with CBSE Class 10 English Language & Literature, Hindi B & Mathematics (Set of 3 Books) Previous 10+ Years� Chapter wise Solved Question Bank Exam 2022-23")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-cbse-class-10-english-language-literature-hindi-b-mathematics-set-of-3-books-previous-10-years-chapter-wise-solved-question-bank-exam-2022-23?type=p_book&id=2343");
        } else if (holdvalue.contentEquals("Together with CBSE Class 10 Hindi A, English Language & Literature & Mathematics (Set of 3 Books) Previous 10+ Years� Chapter wise Solved Question Bank Exam 2022-23")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-cbse-class-10-hindi-a-english-language-literature-mathematics-set-of-3-books-previous-10-years-chapter-wise-solved-question-bank-exam-2022-23?type=p_book&id=2344");
        } else if (holdvalue.contentEquals("Together with CBSE Class 10 Social Science, Mathematics, English Language and Literature (Set of 3 Books) Previous 10+ Years Chapter wise Solved Question Bank Exam 2022-23")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-cbse-class-10-social-science-mathematics-english-language-literature-set-of-3-books-previous-10-years-chapter-wise-solved-question-bank-exam-2022-23?type=p_book&id=2346");
        } else if (holdvalue.contentEquals("Together with CBSE Class 10 Mathematics, English Language & Literature & Science (Set of 3 Books) Previous 10+ Years� Chapter wise Solved Question Bank Exam 2022-23")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-cbse-class-10-mathematics-english-language-literature-science-set-of-3-books-previous-10-years-chapter-wise-solved-question-bank-exam-2022-23?type=p_book&id=2347");
        } else if (holdvalue.contentEquals("Together with CBSE Previous 10+ Years Question Bank Chapterwise English Language & Literature, Hindi B, Mathematics, Social Science and science (Combo of 5 books) for Class 10 (For 2022 - 23 Exam)")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-cbse-previous-10-years-question-bank-chapterwise-english-language-literature-hindi-b-mathematics-social-science-and-science-combo-of-5-books-for-class-10-for-2022-23-exam?type=p_book&id=2356");
        } else if (holdvalue.contentEquals("Together with CBSE Previous 10+ Years Question Bank Chapterwise English Language & Literature, Hindi A, Mathematics, Social Science and science (Combo of 5 books) for Class 10 (For 2022 - 23 Exam)")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-cbse-previous-10-years-question-bank-chapterwise-english-language-literature-hindi-a-mathematics-social-science-and-science-combo-of-5-books-for-class-10-for-2022-23exam?type=p_book&id=2357");
        } else if (holdvalue.contentEquals("Together with CBSE Class 12 Biology, Mathematics & English (Set of 3 Books) Previous 10+ Years� Chapter wise Solved Question Bank Exam 2022-23")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-cbse-class-12-biology-mathematics-english-set-of-3-books-previous-10-years-chapter-wise-solved-question-bank-exam-2022-23?type=p_book&id=2372");
        } else if (holdvalue.contentEquals("Together with CBSE Class 12 Mathematics, Chemistry & English (Set of 3 Books) Previous 10+ Years� Chapter wise Solved Question Bank Exam 2022-23")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-cbse-class-12-mathematics-chemistry-english-set-of-3-books-previous-10-years-chapter-wise-solved-question-bank-exam-2022-23?type=p_book&id=2373");
        } else if (holdvalue.contentEquals("Together with CBSE Class 12 English, Mathematics & Physics (Set of 3 Books) Previous 10+ Years� Chapter wise Solved Question Bank Exam 2022-23")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-cbse-class-12-english-mathematics-physics-set-of-3-books-previous-10-years-chapter-wise-solved-question-bank-exam-2022-23?type=p_book&id=2374");
        } else if (holdvalue.contentEquals("Together with CBSE Class 12 Business Studies, Mathematics & English (Set of 3 Books) Previous 10+ Years� Chapter wise Solved Question Bank Exam 2022-23")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-cbse-class-12-english-mathematics-physics-set-of-3-books-previous-10-years-chapter-wise-solved-question-bank-exam-2022-23?type=p_book&id=2375");
        } else if (holdvalue.contentEquals("Together with CBSE Class 12 English, Economics & Mathematics (Set of 3 Books) Previous 10+ Years� Chapter wise Solved Question Bank Exam 2022-23")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-cbse-class-12-english-economics-mathematics-set-of-3-books-previous-10-years-chapter-wise-solved-question-bank-exam-2022-23?type=p_book&id=2380");
        } else if (holdvalue.contentEquals("Together with CBSE Previous 10+ Years Question Bank Chapterwise English Core, Mathematics, Physics, Chemistry & Biology (Combo of 5 books) for Class 12 (For 2022 - 23 Exam)")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-cbse-previous-10-years-question-bank-chapterwise-english-core-mathematics-physics-chemistry-biology-combo-of-5-books-for-class-12-for-2022-23-exam?type=p_book&id=2388");
        } else if (holdvalue.contentEquals("Together With CBSE Sample Paper (EAD) Class 10 English Language & Literature for Board Examination 2023")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-cbse-sample-paper-ead-class-10-english-language-and-literature-for-board-examination-2023?type=p_book&id=2431");
        } else if (holdvalue.contentEquals("Together With CBSE Sample Paper (EAD) Class 12 English Core for Board Examination 2023")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-cbse-sample-paper-ead-class-12-english-core-for-board-examination-2023?type=p_book&id=2435");
        } else if (holdvalue.contentEquals("Together With CBSE Sample Paper (EAD) Class 10 Mathematics, Science, Social Science, Hindi A & English Language & Literature (combo of 5 Books) for Board Examination 2023")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-cbse-sample-paper-ead-class-10-mathematics-science-social-science-hindi-a-and-english-language-and-literature-combo-of-5-books-for-board-examination-2023?type=p_book&id=2463");
        } else if (holdvalue.contentEquals("Together With CBSE Sample Paper (EAD) Class 10 Mathematics, Science, Social Science, Hindi B & English Language & Literature (combo of 5 Books) for Board Examination 2023")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-cbse-sample-paper-ead-class-10-mathematics-science-social-science-hindi-b-and-english-language-and-literature-combo-of-5-books-for-board-examination-2023?type=p_book&id=2464");
        } else if (holdvalue.contentEquals("Together With CBSE Sample Paper (EAD) Class 12 Business Studies, Accountancy, Economics, Mathematics & English Core (combo of 5 Books) for Board Examination 2023")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-cbse-sample-paper-ead-class-12-business-studies-accountancy-economics-mathematics-and-english-core-combo-of-5-books-for-board-examination-2023?type=p_book&id=2466");
        } else if (holdvalue.contentEquals("Together With NEP 2020 Based Synergy Learning And Activity Combo (5 IN 1)English Mathematics Science ,Social Studies , GK & Life Skills Book For Class 3 Semester 2")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-nep-2020-based-synergy-learning-and-activity-combo-5-in-1-english-mathematics-science-social-studies-gk-and-life-skills-book-for-class-3-semester-2?type=p_book&id=2553");
        } else if (holdvalue.contentEquals("Together With CBSE Class 9 English Communicative Question Bank/Study Material Exam 2023 (Based on the latest Syllabus)")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-cbse-class-9-english-communicative-question-bank-study-material-exam-2023-based-on-the-latest-syllabus?type=p_book&id=2557");
        } else if (holdvalue.contentEquals("Together With NTA CUET English Entrance Exam Book UG-2022 (Solved Question Bank)")) {
            startWebView("https://www.rachnasagar.in/mobile/cuet�ug-nta/together-with-nta-cuet-english-entrance-exam-book-ug-2022-solved-question-bank?type=p_book&id=2558");
        } else if (holdvalue.contentEquals("Together With NTA CUET English, Biology & General Test (Set of 3 books) Entrance Exam Books UG-2022 (Solved Question Bank)")) {
            startWebView("https://www.rachnasagar.in/mobile/cuet�ug-nta/together-with-nta-cuet-english-biology-and-general-test-set-of-3-books-entrance-exam-books-ug-2022-solved-question-bank?type=p_book&id=2570");
        } else if (holdvalue.contentEquals("Together With NTA CUET English, History & General Test (Set of 3 books) Entrance Exam Books UG-2022 (Solved Question Bank) ")) {
            startWebView("https://www.rachnasagar.in/mobile/cuet�ug-nta/together-with-nta-cuet-english-history-and-general-test-set-of-3-books-entrance-exam-books-ug-2022-solved-question-bank?type=p_book&id=2571");
        } else if (holdvalue.contentEquals("Together With NTA CUET English, Chemistry & General Test (Set of 3 books) Entrance Exam Books UG-2022 (Solved Question Bank)")) {
            startWebView("https://www.rachnasagar.in/mobile/cuet�ug-nta/together-with-nta-cuet-english-chemistry-and-general-test-set-of-3-books-entrance-exam-books-ug-2022-solved-question-bank?type=p_book&id=2572");
        } else if (holdvalue.contentEquals("Together With NTA CUET English, Business Studies & General Test (Set of 3 books) Entrance Exam Books UG-2022 (Solved Question Bank)")) {
            startWebView("https://www.rachnasagar.in/mobile/cuet�ug-nta/together-with-nta-cuet-english-business-studies-and-general-test-set-of-3-books-entrance-exam-books-ug-2022-solved-question-bank?type=p_book&id=2573");
        } else if (holdvalue.contentEquals("Together With NTA CUET English, Economics & General Test (Set of 3 books) Entrance Exam Books UG-2022 (Solved Question Bank)")) {
            startWebView("https://www.rachnasagar.in/mobile/cuet�ug-nta/together-with-nta-cuet-english-economics-and-general-test-set-of-3-books-entrance-exam-books-ug-2022-solved-question-bank?type=p_book&id=2574");
        } else if (holdvalue.contentEquals("Together With NTA CUET English, Geography & General Test (Set of 3 books) Entrance Exam Books UG-2022 (Solved Question Bank)")) {
            startWebView("https://www.rachnasagar.in/mobile/cuet�ug-nta/together-with-nta-cuet-english-geography-and-general-test-set-of-3-books-entrance-exam-books-ug-2022-solved-question-bank?type=p_book&id=2575");
        } else if (holdvalue.contentEquals("Together With NTA CUET English, Mathematics & General Test (Set of 3 books) Entrance Exam Books UG-2022 (Solved Question Bank)")) {
            startWebView("https://www.rachnasagar.in/mobile/cuet�ug-nta/together-with-nta-cuet-english-mathematics-and-general-test-set-of-3-books-entrance-exam-books-ug-2022-solved-question-bank?type=p_book&id=2576");
        } else if (holdvalue.contentEquals("Together With NTA CUET English, Physics & General Test (Set of 3 books) Entrance Exam Books UG-2022 (Solved Question Bank)")) {
            startWebView("https://www.rachnasagar.in/mobile/cuet�ug-nta/together-with-nta-cuet-english-physics-and-general-test-set-of-3-books-entrance-exam-books-ug-2022-solved-question-bank?type=p_book&id=2577");
        } else if (holdvalue.contentEquals("Together With NTA CUET English, Accountancy & General Test (Set of 3 books) Entrance Exam Books UG-2022 (Solved Question Bank)")) {
            startWebView("https://www.rachnasagar.in/mobile/cuet�ug-nta/together-with-nta-cuet-english-accountancy-and-general-test-set-of-3-books-entrance-exam-books-ug-2022-solved-question-bank?type=p_book&id=2578");
        } else if (holdvalue.contentEquals("Together With NTA CUET English & General Test (Set of 2 books) Entrance Exam Books UG-2022 (Solved Question Bank)")) {
            startWebView("https://www.rachnasagar.in/mobile/cuet�ug-nta/together-with-nta-cuet-english-and-general-test-set-of-2-books-entrance-exam-books-ug-2022-solved-question-bank?type=p_book&id=2580");
        } else if (holdvalue.contentEquals("Happy Learning Pullout Worksheets Hindi A, English A, Mathematics A for LKG")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/happy-learning-pullout-worksheets-hindi-a-english-a-mathematics-a-for-lkg?type=p_book&id=2581");
        } else if (holdvalue.contentEquals("Together With NTA CUET English & Biology (Set of 2 books) Entrance Exam Books UG-2022 (Solved Question Bank)")) {
            startWebView("https://www.rachnasagar.in/mobile/cuet�ug-nta/together-with-nta-cuet-english-and-biology-set-of-2-books-entrance-exam-books-ug-2022-solved-question-bank?type=p_book&id=2583");
        } else if (holdvalue.contentEquals("Together With NTA CUET English & History (Set of 2 books) Entrance Exam Books UG-2022 (Solved Question Bank)")) {
            startWebView("https://www.rachnasagar.in/mobile/cuet�ug-nta/together-with-nta-cuet-english-and-history-set-of-2-books-entrance-exam-books-ug-2022-solved-question-bank?type=p_book&id=2584");
        } else if (holdvalue.contentEquals("Together With NTA CUET English & Chemistry (Set of 2 books) Entrance Exam Books UG-2022 (Solved Question Bank)")) {
            startWebView("https://www.rachnasagar.in/mobile/cuet�ug-nta/together-with-nta-cuet-english-and-chemistry-set-of-2-books-entrance-exam-books-ug-2022-solved-question-bank?type=p_book&id=2585");
        } else if (holdvalue.contentEquals("Together With NTA CUET English & Business Studies (Set of 2 books) Entrance Exam Books UG-2022 (Solved Question Bank)")) {
            startWebView("https://www.rachnasagar.in/mobile/cuet�ug-nta/together-with-nta-cuet-english-and-business-studies-set-of-2-books-entrance-exam-books-ug-2022-solved-question-bank?type=p_book&id=2586");
        } else if (holdvalue.contentEquals("Together With NTA CUET English & Economics (Set of 2 books) Entrance Exam Books UG-2022 (Solved Question Bank)")) {
            startWebView("https://www.rachnasagar.in/mobile/cuet�ug-nta/together-with-nta-cuet-english-and-economics-set-of-2-books-entrance-exam-books-ug-2022-solved-question-bank?type=p_book&id=2587");
        } else if (holdvalue.contentEquals("Together With NTA CUET English & Geography (Set of 2 books) Entrance Exam Books UG-2022 (Solved Question Bank)")) {
            startWebView("https://www.rachnasagar.in/mobile/cuet�ug-nta/together-with-nta-cuet-english-and-geography-set-of-2-books-entrance-exam-books-ug-2022-solved-question-bank?type=p_book&id=2588");
        } else if (holdvalue.contentEquals("Together With NTA CUET English & Mathematics (Set of 2 books) Entrance Exam Books UG-2022 (Solved Question Bank)")) {
            startWebView("https://www.rachnasagar.in/mobile/cuet�ug-nta/together-with-nta-cuet-english-and-mathematics-set-of-2-books-entrance-exam-books-ug-2022-solved-question-bank?type=p_book&id=2589");
        } else if (holdvalue.contentEquals("Together With NTA CUET English & Physics (Set of 2 books) Entrance Exam Books UG-2022 (Solved Question Bank)")) {
            startWebView("https://www.rachnasagar.in/mobile/cuet�ug-nta/together-with-nta-cuet-english-and-physics-set-of-2-books-entrance-exam-books-ug-2022-solved-question-bank?type=p_book&id=2590");
        } else if (holdvalue.contentEquals("Together With NTA CUET English & Accountancy (Set of 2 books) Entrance Exam Books UG-2022 (Solved Question Bank)")) {
            startWebView("https://www.rachnasagar.in/mobile/cuet�ug-nta/together-with-nta-cuet-english-and-accountancy-set-of-2-books-entrance-exam-books-ug-2022-solved-question-bank?type=p_book&id=2591");
        } else if (holdvalue.contentEquals("Together With NTA CUET English ,Physics, Chemistry &Mathematics (Set of 4 books) Entrance Exam Books UG-2022 (Solved Question Bank)")) {
            startWebView("https://www.rachnasagar.in/mobile/cuet�ug-nta/together-with-nta-cuet-english-physics-chemistry-and-mathematics-set-of-4-books-entrance-exam-books-ug-2022-solved-question-bank?type=p_book&id=2605");
        } else if (holdvalue.contentEquals("Together With NTA CUET English ,Physics, Chemistry & Biology (Set of 4 books) Entrance Exam Books UG-2022 (Solved Question Bank)")) {
            startWebView("https://www.rachnasagar.in/mobile/cuet�ug-nta/together-with-nta-cuet-english-physics-chemistry-and-biology-set-of-4-books-entrance-exam-books-ug-2022-solved-question-bank?type=p_book&id=2606");
        } else if (holdvalue.contentEquals("Together With NTA CUET English ,Business Studies, Economics & Accountancy (Set of 4 books) Entrance Exam Books UG-2022 (Solved Question Bank)")) {
            startWebView("https://www.rachnasagar.in/mobile/cuet�ug-nta/together-with-nta-cuet-english-business-studies-economics-and-accountancy-set-of-4-books-entrance-exam-books-ug-2022-solved-question-bank?type=p_book&id=2607");
        } else if (holdvalue.contentEquals("Together With NTA CUET English ,Business Studies, Economics & Geography (Set of 4 books) Entrance Exam Books UG-2022 (Solved Question Bank)")) {
            startWebView("https://www.rachnasagar.in/mobile/cuet�ug-nta/together-with-nta-cuet-english-business-studies-economics-and-geography-set-of-4-books-entrance-exam-books-ug-2022-solved-question-bank?type=p_book&id=2608");
        } else if (holdvalue.contentEquals("Together With NTA CUET English ,Physics, Chemistry, Mathematics & General Test (Set of 5 books) Entrance Exam Books UG-2022 (Solved Question Bank)")) {
            startWebView("https://www.rachnasagar.in/mobile/cuet�ug-nta/together-with-nta-cuet-english-physics-chemistry-mathematics-and-general-test-set-of-5-books-entrance-exam-books-ug-2022-solved-question-bank?type=p_book&id=2609");
        } else if (holdvalue.contentEquals("Together With NTA CUET English ,Physics, Chemistry, Biology & General Test (Set of 5 books) Entrance Exam Books UG-2022 (Solved Question Bank)")) {
            startWebView("https://www.rachnasagar.in/mobile/cuet�ug-nta/together-with-nta-cuet-english-physics-chemistry-biology-and-general-test-set-of-5-books-entrance-exam-books-ug-2022-solved-question-bank?type=p_book&id=2610");
        } else if (holdvalue.contentEquals("Together With NTA CUET English, Business Studies, Economics, Accountancy & General Test (Set of 5 books) Entrance Exam Books UG-2022 (Solved Question Bank)")) {
            startWebView("https://www.rachnasagar.in/mobile/cuet�ug-nta/together-with-nta-cuet-english-business-studies-economics-accountancy-and-general-test-set-of-5-books-entrance-exam-books-ug-2022-solved-question-bank?type=p_book&id=2611");
        } else if (holdvalue.contentEquals("Together With NTA CUET English, Business Studies, Economics, Geography & General Test (Set of 5 books) Entrance Exam Books UG-2022 (Solved Question Bank)")) {
            startWebView("https://www.rachnasagar.in/mobile/cuet�ug-nta/together-with-nta-cuet-english-business-studies-economics-geography-and-general-test-set-of-5-books-entrance-exam-books-ug-2022-solved-question-bank?type=p_book&id=2612");
        } else if (holdvalue.contentEquals("Happy Learning Pull-out Worksheets English, Hindi, Mathematics, EVS for Nursery")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/happy-learning-pull-out-worksheets-english-hindi-mathematics-evs-for-nursery?type=p_book&id=2618");
        } else if (holdvalue.contentEquals("Happy Learning Pull-out Worksheets English, Hindi, Mathematics, EVS, Science for Class 1")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/happy-learning-pull-out-worksheets-english-hindi-mathematics-evs-science-for-class-1?type=p_book&id=2619");
        } else if (holdvalue.contentEquals("Happy Learning Pull-out Worksheets English, Hindi, Mathematics, Social Studies, Science for Class 1")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/happy-learning-pull-out-worksheets-english-hindi-mathematics-social-studies-science-for-class-1?type=p_book&id=2620");
        } else if (holdvalue.contentEquals("Happy Learning Pull-out Worksheets English, Hindi, Mathematics, EVS, Science for Class 2")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/happy-learning-pull-out-worksheets-english-hindi-mathematics-evs-science-for-class-2?type=p_book&id=2621");
        } else if (holdvalue.contentEquals("Happy Learning Pull-out Worksheets English, Hindi, Mathematics, EVS, Social Science for Class 2")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/happy-learning-pull-out-worksheets-english-hindi-mathematics-evs-social-science-for-class-2?type=p_book&id=2622");
        } else if (holdvalue.contentEquals("Happy Learning Pull-out Worksheets English, Hindi, Mathematics, EVS, Science for Class 3")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/happy-learning-pull-out-worksheets-english-hindi-mathematics-evs-science-for-class-3?type=p_book&id=2623");
        } else if (holdvalue.contentEquals("Happy Learning Pull-out Worksheets English, Hindi, Mathematics, Social Studies, Science for Class 3")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/happy-learning-pull-out-worksheets-english-hindi-mathematics-social-studies-science-for-class-3?type=p_book&id=2624");
        } else if (holdvalue.contentEquals("Together With CBSE Class 9 English Communicative POW Solution Question Bank/Study Material Exam 2023 (Based on the latest Syllabus)")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-cbse-class-9-english-communicative-pow-solution-question-bank-study-material-exam-2023-based-on-the-latest-syllabus?type=p_book&id=2631");
        } else if (holdvalue.contentEquals("Together With CBSE Class 9 English Communicative POW Question Bank/Study Material Exam 2023 (Based on the latest Syllabus)")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-cbse-class-9-english-communicative-pow-question-bank-study-material-exam-2023-based-on-the-latest-syllabus?type=p_book&id=2632");
        } else if (holdvalue.contentEquals("Together With Peek-a-boo English Mathematics EVS Term 1 LKG")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-peek-a-boo-english-mathematics-evs-term-1-lkg?type=p_book&id=2636");
        } else if (holdvalue.contentEquals("Together With Peek-a-boo English Mathematics Environmental Studies Term 2 LKG")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-peek-a-boo-english-mathematics-environmental-studies-term-2-lkg?type=p_book&id=2637");
        } else if (holdvalue.contentEquals("Together With English Mathematics Environmental Studies Term 3 Class LKG")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-english-mathematics-environmental-studies-term-3-class-lkg?type=p_book&id=2638");
        } else if (holdvalue.contentEquals("Together With Peek-a-boo English Mathematics Environmental Studies Term 1 UKG")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-peek-a-boo-english-mathematics-environmental-studies-term-1-ukg?type=p_book&id=2639");
        } else if (holdvalue.contentEquals("Together With Peek-a-boo English Mathematics Environmental Studies Term 3 Class UKG")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-english-mathematics-environmental-studies-term-3-class-ukg--?type=p_book&id=2640");
        } else if (holdvalue.contentEquals("Together With Peek-a-boo English Mathematics Environmental Studies Term 2 Class UKG")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-english-mathematics-environmental-studies-term-2-class-ukg?type=p_book&id=2641");
        } else if (holdvalue.contentEquals("Together With NTA CUET Sanskrit Entrance Exam Book UG-2022 (Solved Question Bank)")) {
            startWebView("https://www.rachnasagar.in/mobile/cuet�ug-nta/together-with-nta-cuet-sanskrit-entrance-exam-book-ug-2022-solved-question-bank?type=p_book&id=2642");
        } else if (holdvalue.contentEquals("Together with CBSE and CUET Class 12 English Core Solved Question Bank Exam 2022-2023 (Based on the latest syllabus)")) {
            startWebView("https://www.rachnasagar.in/mobile/cuet�ug-nta/together-with-cbse-and-cuet-class-12-english-core-solved-question-bank-exam-2022-2023-based-on-the-latest-syllabus?type=p_book&id=2651");
        } else if (holdvalue.contentEquals("Together with CBSE and CUET Class 12 English Core with English Core POW (Pull Out Worksheets) Solved Question Bank Exam 2022-2023 (Based on the latest syllabus)")) {
            startWebView("https://www.rachnasagar.in/mobile/cuet�ug-nta/together-with-cbse-and-cuet-class-12-english-core-with-english-core-pow-pull-out-worksheets-solved-question-bank-exam-2022-2023-based-on-the-latest-syllabus?type=p_book&id=2657");
        } else if (holdvalue.contentEquals("Together With ICSE Class 9 Biology, Chemistry, Computer Application, Geography, History And Civics, Mathematics, English Language, Hindi, Physics (Set of 9 books) Question Bank Exam 2022-23")) {
            startWebView("https://www.rachnasagar.in/mobile/icse/together-with-icse-class-9-biology-chemistry-computer-application-geography-history-and-civics-mathematics-english-language-hindi-physics-set-of-9-books-question-bank-exam-2022-23?type=p_book&id=2659");
        } else if (holdvalue.contentEquals("Together With ICSE Class 9 Biology, Chemistry, Computer Application, Geography, History And Civics, Mathematics, English Literature , Hindi, Physics (Set of 9 books) Question Bank Exam 2022-23 (Based")) {
            startWebView("https://www.rachnasagar.in/mobile/icse/together-with-icse-class-9-biology-chemistry-computer-application-geography-history-and-civics-mathematics-english-literature-hindi-physics-set-of-9-books-question-bank-exam-2022-23-based?type=p_book&id=2660");
        } else if (holdvalue.contentEquals("Together With ICSE Class 9 Chemistry, Physics, English Literature, Computer Application, Mathematics (Set of 5 books) Question Bank Exam 2022-23 (Based on the latest Syllabus)")) {
            startWebView("https://www.rachnasagar.in/mobile/icse/together-with-icse-class-9-chemistry-physics-english-literature-computer-application-mathematics-set-of-5-books-question-bank-exam-2022-23-based-on-the-latest-syllabus?type=p_book&id=2663");
        } else if (holdvalue.contentEquals("Together With ICSE Class 9 Biology, Chemistry, Physics,English Language, Mathematics (Set of 5 books) Question Bank Exam 2022-23 (Based on the latest Syllabus)")) {
            startWebView("https://www.rachnasagar.in/mobile/icse/together-with-icse-class-9-biology-chemistry-physics-english-language-mathematics-set-of-5-books-question-bank-exam-2022-23-based-on-the-latest-syllabus?type=p_book&id=2664");
        } else if (holdvalue.contentEquals("Together With ICSE Class 9 Biology, Chemistry, Physics, English Literature, Mathematics (Set of 5 books) Question Bank Exam 2022-23 (Based on the latest Syllabus)")) {
            startWebView("https://www.rachnasagar.in/mobile/icse/together-with-icse-class-9-biology-chemistry-physics-english-literature-mathematics-set-of-5-books-question-bank-exam-2022-23-based-on-the-latest-syllabus?type=p_book&id=2665");
        } else if (holdvalue.contentEquals("Together With ICSE Class 9 Mathematics, Computer Application, Biology, Physics, Chemistry, Geography, History and Civics, English Language (Set of 8 books) Question Bank Exam 2022-23")) {
            startWebView("https://www.rachnasagar.in/mobile/icse/together-with-icse-class-9-mathematics-computer-application-biology-physics-chemistry-geography-history-and-civics-english-language-set-of-8-books-question-bank-exam-2022-23?type=p_book&id=2668");
        } else if (holdvalue.contentEquals("Together With ICSE Class 9 Mathematics, Computer Application, Biology, Physics, Chemistry, Geography, History and Civics, English Literature (Set of 8 books) Question Bank Exam 2022-23 (Based on the l")) {
            startWebView("https://www.rachnasagar.in/mobile/icse/together-with-icse-class-9-mathematics-computer-application-biology-physics-chemistry-geography-history-and-civics-english-literature-set-of-8-books-question-bank-exam-2022-23-based-on-the-l?type=p_book&id=2669");
        } else if (holdvalue.contentEquals("Together With ICSE Class 9 English Language, Hindi, Mathematics (Set of 3 books) Question Bank Exam 2022-23 (Based on the latest Syllabus)")) {
            startWebView("https://www.rachnasagar.in/mobile/icse/together-with-icse-class-9-english-language-hindi-mathematics-set-of-3-books-question-bank-exam-2022-23-based-on-the-latest-syllabus?type=p_book&id=2673");
        } else if (holdvalue.contentEquals("Together With ICSE Class 9 English Literature, Hindi , Mathematics (Set of 3 books) Question Bank Exam 2022-23 (Based on the latest Syllabus)")) {
            startWebView("https://www.rachnasagar.in/mobile/icse/together-with-icse-class-9-english-literature-hindi-mathematics-set-of-3-books-question-bank-exam-2022-23-based-on-the-latest-syllabus?type=p_book&id=2674");
        } else if (holdvalue.contentEquals("Together With CBSE Class 10 English Language & Literature, Hindi A, Mathematics (Basic), Social Science,Science Question Bank (Set of 5 books) Exam 2022-23 ( Based On Latest Syllabus)")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-cbse-class-10-english-language-and-literature-hindi-a-mathematics-basic-social-science-science-question-bank-set-of-5-books-exam-2022-23-based-on-latest-syllabus?type=p_book&id=2676");
        } else if (holdvalue.contentEquals("Together With CBSE Class 10 English Language & Literature, Hindi A, Mathematics (Standard), Social Science, Science Question Bank (Set of 5 books) Exam 2022-23 ( Based On Latest Syllabus)")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-cbse-class-10-english-language-and-literature-hindi-a-mathematics-standard-social-science-science-question-bank-set-of-5-books-exam-2022-23-based-on-latest-syllabus?type=p_book&id=2677");
        } else if (holdvalue.contentEquals("Together With CBSE Class 10 English Language & literature, Hindi B, Mathematics (Basic), Social Science, Science Question Bank (Set Of 5 books) Exam 2022-23 (Based On Latest Syllabus)")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-cbse-class-10-english-language-and-literature-hindi-b-mathematics-basic-social-science-science-question-bank-set-of-5-books-exam-2022-23-based-on-latest-syllabus?type=p_book&id=2678");
        } else if (holdvalue.contentEquals("Together With CBSE Class 10 English Language & literature, Hindi B, Mathematics (Standard), Social Science, Science Question Bank (Set Of 5 books) Exam 2022-23 ( Based On Latest Syllabus) .")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-cbse-class-10-english-language-and-literature-hindi-b-mathematics-standard-social-science-science-question-bank-set-of-5-books-exam-2022-23-based-on-latest-syllabus--?type=p_book&id=2679");
        } else if (holdvalue.contentEquals("Together With CBSE Class 10 English Language & Literature, English Language & Literature pull Out Worksheets, Mathematics (Standard), Science, Sanskrit Question Bank (Set of 5 books) Exam 2022-23")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-cbse-class-10-english-language-and-literature-english-language-and-literature-pull-out-worksheets-mathematics-standard-science-sanskrit-question-bank-set-of-5-books-exam-2022-23?type=p_book&id=2680");
        } else if (holdvalue.contentEquals("Together With CBSE Class 10 English Language & Literature, English Language & Literature pull Out Worksheets, Mathematics (Basic), Science, French Question Bank (Set of 5 books) Exam 2022-23")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-cbse-class-10-english-language-and-literature-english-language-and-literature-pull-out-worksheets-mathematics-basic-science-french-question-bank-set-of-5-books-exam-2022-23?type=p_book&id=2681");
        } else if (holdvalue.contentEquals("Together With CBSE Class 10 English Language & Literature , English Language & Literature pull Out Worksheets, Mathematics (Standard) , Social Science , French Question Bank (Set of 5 books) Exam 2022")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-cbse-class-10-english-language-and-literature-english-language-and-literature-pull-out-worksheets-mathematics-standard-social-science-french-question-bank-set-of-5-books-exam-2022?type=p_book&id=2682");
        } else if (holdvalue.contentEquals("Together With CBSE Class 10 Mathematics (Basic), Social Science, Science, Sanskrit, English Language & Literature Pull Out Worksheets Question Bank (Set of 5 books) Exam 2022-23")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-cbse-class-10-mathematics-basic-social-science-science-sanskrit-english-language-and-literature-pull-out-worksheets-question-bank-set-of-5-books-exam-2022-23?type=p_book&id=2683");
        } else if (holdvalue.contentEquals("Together With CBSE Class 10 Mathematics (Standard), Social Science, Science, Sanskrit, English Language & Literature Pull Out Worksheets Question Bank (Set of 5 books) Exam 2022-23")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-cbse-class-10-mathematics-standard-social-science-science-sanskrit-english-language-and-literature-pull-out-worksheets-question-bank-set-of-5-books-exam-2022-23?type=p_book&id=2684");
        } else if (holdvalue.contentEquals("Together With CBSE Class 11 Physics, Chemistry, Biology, Mathematics & English Core Question Bank (Set of 5 Books) Exam 2022-23 ( Based On Latest Syllabus)")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-cbse-class-11-physics-chemistry-biology-mathematics-and-english-core-question-bank-set-of-5-books-exam-2022-23-based-on-latest-syllabus?type=p_book&id=2685");
        } else if (holdvalue.contentEquals("Together With CBSE Class 11 Physics, Chemistry, Biology, Mathematics , English Cor Pull Out Worksheets (POW) Question Bank (Set of 5 Books ) Exam 2022-23 ( Based On Latest Syllabus)")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-cbse-class-11-physics-chemistry-biology-mathematics-english-cor-pull-out-worksheets-pow-question-bank-set-of-5-books-exam-2022-23-based-on-latest-syllabus?type=p_book&id=2686");
        } else if (holdvalue.contentEquals("Together With CBSE Class 11 English, English Core Pull Out Worksheet (POW), Physics, Chemistry, Mathematics Question Bank (Set of 5 books) Exam 2022-23 ( Based On Latest Syllabus).")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-cbse-class-11-english-english-core-pull-out-worksheet-pow-physics-chemistry-mathematics-question-bank-set-of-5-books-exam-2022-23-based-on-latest-syllabus--?type=p_book&id=2687");
        } else if (holdvalue.contentEquals("Together With CBSE Class 12 Accountancy, Mathematics, Business Studies, Economics & English Core (Set of 5 books) Question Bank Exam 2022-23 (Based on the latest Syllabus)")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-cbse-class-12-accountancy-mathematics-business-studies-economics-and-english-core-set-of-5-books-question-bank-exam-2022-23-based-on-the-latest-syllabus?type=p_book&id=2688");
        } else if (holdvalue.contentEquals("Together With CBSE Class 12 History, Geography, Political Science, English Core & English Core Pull Out Worksheets (POW) (Set of 5 books) Question Bank Exam 2022-23 (Based on the latest Syllabus)")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-cbse-class-12-history-geography-political-science-english-core-and-english-core-pull-out-worksheets-pow-set-of-5-books-question-bank-exam-2022-23-based-on-the-latest-syllabus?type=p_book&id=2689");
        } else if (holdvalue.contentEquals("Together With CBSE Class 12 History, Geography, Political Science, English Core & Economics (Set of 5 books) Question Bank Exam 2022-23 (Based on the latest Syllabus)")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-cbse-class-12-history-geography-political-science-english-core-and-economics-set-of-5-books-question-bank-exam-2022-23-based-on-the-latest-syllabus?type=p_book&id=2690");
        } else if (holdvalue.contentEquals("Together With CBSE Class 12 English Core, Mathematics, Physics, Chemistry & Informatics Practices (Set of 5 books) Question Bank Exam 2022-23 (Based on the latest Syllabus)")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-cbse-class-12-english-core-mathematics-physics-chemistry-and-informatics-practices-set-of-5-books-question-bank-exam-2022-23-based-on-the-latest-syllabus?type=p_book&id=2692");
        } else if (holdvalue.contentEquals("Together With CBSE Class 12 English Core, Mathematics, Physics, Chemistry & Computer Applications (Set of 5 books) Question Bank Exam 2022-23 (Based on the latest Syllabus)")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-cbse-class-12-english-core-mathematics-physics-chemistry-and-computer-applications-set-of-5-books-question-bank-exam-2022-23-based-on-the-latest-syllabus?type=p_book&id=2693");
        } else if (holdvalue.contentEquals("Together With CBSE Class 12 English Core, Mathematics, Physics, Chemistry & Biology (Set of 5 books) Question Bank Exam 2022-23 (Based on the latest Syllabus)")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-cbse-class-12-english-core-mathematics-physics-chemistry-and-biology-set-of-5-books-question-bank-exam-2022-23-based-on-the-latest-syllabus?type=p_book&id=2694");
        } else if (holdvalue.contentEquals("Together With CBSE Class 12 English Core, English Pull Out Worksheets (POW), Mathematics, Physics & Chemistry (Set of 5 books) Question Bank Exam 2022-23 (Based on the latest Syllabus)")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-cbse-class-12-english-core-english-pull-out-worksheets-pow-mathematics-physics-and-chemistry-set-of-5-books-question-bank-exam-2022-23-based-on-the-latest-syllabus?type=p_book&id=2695");
        } else if (holdvalue.contentEquals("Together With CBSE Class 12 Accountancy, Mathematics, Business Studies, English Core Pull Out Worksheets (POW) & Economics (Set of 5 books) Question Bank Exam 2022-23")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-cbse-class-12-accountancy-mathematics-business-studies-english-core-pull-out-worksheets-pow-and-economics-set-of-5-books-question-bank-exam-2022-23?type=p_book&id=2696");
        } else if (holdvalue.contentEquals("Together With CBSE Class 9 English Language & Literature, Hindi A, Mathematics, Social Science , Science Question Bank (Set of 5 books) Exam 2022-23 (Based On Latest Syllabus)")) {
            startWebView("https://www.rachnasagar.in/mobile//together-with-cbse-class-9-english-language-and-literature-hindi-a-mathematics-social-science-science-question-bank-set-of-5-books-exam-2022-23-based-on-latest-syllabus?type=p_book&id=2698");
        } else if (holdvalue.contentEquals("Together With CBSE Class 9 English Language & Literature, Hindi B, Mathematics, Social Science , Science Question Bank (Set of 5 books) Exam 2022-23 (Based On Latest Syllabus)")) {
            startWebView("https://www.rachnasagar.in/mobile//together-with-cbse-class-9-english-language-and-literature-hindi-b-mathematics-social-science-science-question-bank-set-of-5-books-exam-2022-23-based-on-latest-syllabus?type=p_book&id=2699");
        } else if (holdvalue.contentEquals("Together With CBSE Class 9 English Communicative, Hindi B, Mathematics, Social Science, Science Question Bank (Set of 5 Books) Exam 2022-23 ( Based On Latest Syllabus)")) {
            startWebView("https://www.rachnasagar.in/mobile//together-with-cbse-class-9-english-communicative-hindi-b-mathematics-social-science-science-question-bank-set-of-5-books-exam-2022-23-based-on-latest-syllabus?type=p_book&id=2701");
        } else if (holdvalue.contentEquals("Together With CBSE Class 9 English Language & Literature , English Language & Literature Pull Out Worksheets (POW), Mathematics, Social Science, French Question Bank (Set of 5 books) Exam 2022-23( Ba")) {
            startWebView("https://www.rachnasagar.in/mobile//together-with-cbse-class-9-english-language-and-literature-english-language-and-literature-pull-out-worksheets-pow-mathematics-social-science-french-question-bank-set-of-5-books-exam-2022-23-ba?type=p_book&id=2702");
        } else if (holdvalue.contentEquals("Together With CBSE Class 9 English Language & Literature ,English Language & Literature Pull Out Worksheets (POW), Science, Mathematics, Sanskrit Question Bank (Set of 5 books) Exam 2022-23( Based On")) {
            startWebView("https://www.rachnasagar.in/mobile//together-with-cbse-class-9-english-language-and-literature-english-language-and-literature-pull-out-worksheets-pow-science-mathematics-sanskrit-question-bank-set-of-5-books-exam-2022-23-based-on?type=p_book&id=2703");
        } else if (holdvalue.contentEquals("Together With CBSE Class 9 English Communicative, English Communicative POW, Mathematics, Social Science & French Question Bank (Set of 5 books) Exam 2022-23( Based On Latest Syllabus)")) {
            startWebView("https://www.rachnasagar.in/mobile//together-with-cbse-class-9-english-communicative-english-communicative-pow-mathematics-social-science-and-french-question-bank-set-of-5-books-exam-2022-23-based-on-latest-syllabus?type=p_book&id=2704");
        } else if (holdvalue.contentEquals("Together With CBSE Class 9 English Communicative, English Communicative Pull Out Worksheet (POW), Science, Mathematics & Sanskrit Question Bank (Set of 5 Books) Exam 2022-23 ( Based On Latest Syllabus")) {
            startWebView("https://www.rachnasagar.in/mobile//together-with-cbse-class-9-english-communicative-english-communicative-pull-out-worksheet-pow-science-mathematics-and-sanskrit-question-bank-set-of-5-books-exam-2022-23-based-on-latest-syllabus?type=p_book&id=2705");
        } else if (holdvalue.contentEquals("Together With CBSE Class 9 Mathematics, Social Science, Science, Sanskrit & English Language Literature Pull Out Worksheet (POW) Question Bank (Set of 5 books) Exam 2022-23 ( Based On Latest Syllabus)")) {
            startWebView("https://www.rachnasagar.in/mobile//together-with-cbse-class-9-mathematics-social-science-science-sanskrit-and-english-language-literature-pull-out-worksheet-pow-question-bank-set-of-5-books-exam-2022-23-based-on-latest-syllabus?type=p_book&id=2706");
        } else if (holdvalue.contentEquals("Together With CBSE Class 12 English Core, English Core Pull Out Worksheets (POW) & Computer Science (Set of 3 books) Question Bank Exam 2022-23 (Based on the latest Syllabus)")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-cbse-class-12-english-core-english-core-pull-out-worksheets-pow-and-computer-science-set-of-3-books-question-bank-exam-2022-23-based-on-the-latest-syllabus?type=p_book&id=2709");
        } else if (holdvalue.contentEquals("Together With CBSE Class 12 English Core, English Core Pull Out Worksheets (POW) & Informatics Practices (Set of 3 books) Question Bank Exam 2022-23 (Based on the latest Syllabus)")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-cbse-class-12-english-core-english-core-pull-out-worksheets-pow-and-informatics-practices-set-of-3-books-question-bank-exam-2022-23-based-on-the-latest-syllabus?type=p_book&id=2710");
        } else if (holdvalue.contentEquals("Together With CBSE Class 11 English Core, Economics, English Core Pull Out Worksheets Question Bank (Set Of 3 books)Exam 2022-23( Based On Latest Syllabus) .")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-cbse-class-11-english-core-economics-english-core-pull-out-worksheets-question-bank-set-of-3-books-exam-2022-23-based-on-latest-syllabus--?type=p_book&id=2715");
        } else if (holdvalue.contentEquals("Together With CBSE Class 11 English Core, Mathematics , English Core Pull Out Worksheets (POW) Question Bank (Set of 3 books) Exam 2022-23( Based On Latest Syllabus)")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-cbse-class-11-english-core-mathematics-english-core-pull-out-worksheets-pow-question-bank-set-of-3-books-exam-2022-23-based-on-latest-syllabus?type=p_book&id=2716");
        } else if (holdvalue.contentEquals("Together With CBSE Class 11 Mathematics, Economics , English Core Pull Out Worksheet (POW ) Question Bank (Set of 3 books) Exam 2022-23 ( Based On Latest Syllabus)")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-cbse-class-11-mathematics-economics-english-core-pull-out-worksheet-pow-question-bank-set-of-3-books-exam-2022-23-based-on-latest-syllabus?type=p_book&id=2717");
        } else if (holdvalue.contentEquals("Together With ICSE Question Bank Class 9 English Literature ,Hindi, Mathematics Exam 2023 (Study Material) Based On Latest Pattern")) {
            startWebView("https://www.rachnasagar.in/mobile/icse/together-with-icse-question-bank-class-9-english-literature-hindi-mathematics-exam-2023-study-material-based-on-latest-pattern?type=p_book&id=2726");
        } else if (holdvalue.contentEquals("Together With ICSE Class 10 English Language, Hindi, Mathematics (Set of 3 books) Question Bank Exam 2022-23 (Based on the latest Syllabus)")) {
            startWebView("https://www.rachnasagar.in/mobile/icse/together-with-icse-class-10-english-language-hindi-mathematics-set-of-3-books-question-bank-exam-2022-23-based-on-the-latest-syllabus?type=p_book&id=2731");
        } else if (holdvalue.contentEquals("Together With ICSE Class 10 English Literature ,Hindi, Mathematics (Set of 3 books) Question Bank Exam 2022-23 (Based on the latest Syllabus)")) {
            startWebView("https://www.rachnasagar.in/mobile/icse/together-with-icse-class-10-english-literature-hindi-mathematics-set-of-3-books-question-bank-exam-2022-23-based-on-the-latest-syllabus?type=p_book&id=2732");
        } else if (holdvalue.contentEquals("Together With CBSE Class 9 English Language & Literature, Hindi A, Mathematics Question Bank (Set of 3 books) Exam 2022-23 (Based On Latest Syllabus)")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-cbse-class-9-english-language-and-literature-hindi-a-mathematics-question-bank-set-of-3-books-exam-2022-23-based-on-latest-syllabus?type=p_book&id=2734");
        } else if (holdvalue.contentEquals("Together With CBSE Class 9 English Language & Literature, Hindi B, Mathematics Question Bank (Set of 3 books) Exam 2022-23 (Based On Latest Syllabus)")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-cbse-class-9-english-language-and-literature-hindi-b-mathematics-question-bank-set-of-3-books-exam-2022-23-based-on-latest-syllabus?type=p_book&id=2735");
        } else if (holdvalue.contentEquals("Together With CBSE Class 9 English Language & literature & Pull Out Worksheets (POW), French, Mathematics Question Bank (Set of 3 books) Exam 2022-23 (Based On Latest Syllabus)")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-cbse-class-9-english-language-and-literature-and-pull-out-worksheets-pow-french-mathematics-question-bank-set-of-3-books-exam-2022-23-based-on-latest-syllabus?type=p_book&id=2739");
        } else if (holdvalue.contentEquals("Together With CBSE Class 9 English Communicative, Hindi A, Mathematics Question Bank (Set of 3 books) Exam 2022-23 (Based On Latest Syllabus)")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-cbse-class-9-english-communicative-hindi-a-mathematics-question-bank-set-of-3-books-exam-2022-23-based-on-latest-syllabus?type=p_book&id=2740");
        } else if (holdvalue.contentEquals("Together With CBSE Class 9 English Communicative, Hindi B, Mathematics Question Bank (Set of 3 books) Exam 2022-23 (Based On Latest Syllabus)")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-cbse-class-9-english-communicative-hindi-b-mathematics-question-bank-set-of-3-books-exam-2022-23-based-on-latest-syllabus?type=p_book&id=2741");
        } else if (holdvalue.contentEquals("Together With CBSE Class 10 English Language & Literature, Hindi A, Mathematics (Basic) (Set of 3 books) Question Bank Exam 2022-23 (Based on the latest Syllabus)")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-cbse-class-10-english-language-and-literature-hindi-a-mathematics-basic-set-of-3-books-question-bank-exam-2022-23-based-on-the-latest-syllabus?type=p_book&id=2742");
        } else if (holdvalue.contentEquals("Together With CBSE Class 10 English Language And Literature, Hindi A, Mathematics (Standard) (Set of 3 books) Question Bank Exam 2022-23 (Based on the latest Syllabus)")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-cbse-class-10-english-language-and-literature-hindi-a-mathematics-standard-set-of-3-books-question-bank-exam-2022-23-based-on-the-latest-syllabus?type=p_book&id=2743");
        } else if (holdvalue.contentEquals("Together With CBSE Class 10 English Language And Literature, Hindi B, Mathematics (Basic) (Set of 3 books) Question Bank Exam 2022-23 (Based on the latest Syllabus)")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-cbse-class-10-english-language-and-literature-hindi-b-mathematics-basic-set-of-3-books-question-bank-exam-2022-23-based-on-the-latest-syllabus?type=p_book&id=2744");
        } else if (holdvalue.contentEquals("Together With CBSE Class 10 English Language & Literature, Hindi B, Mathematics (Standard) (Set of 3 books) Question Bank Exam 2022-23 (Based on the latest Syllabus)")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-cbse-class-10-english-language-and-literature-hindi-b-mathematics-standard-set-of-3-books-question-bank-exam-2022-23-based-on-the-latest-syllabus?type=p_book&id=2745");
        } else if (holdvalue.contentEquals("Together With CBSE Class 10 English Language And Literature,Hindi B, Mathematics(Standard) (Set of 3 books) Question Bank Exam 2022-23 (Based on the latest Syllabus)")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-cbse-class-10-english-language-and-literature-hindi-b-mathematics-standard-set-of-3-books-question-bank-exam-2022-23-based-on-the-latest-syllabu?type=p_book&id=2746");
        } else if (holdvalue.contentEquals("Together With CBSE Class 10 English Language &Literature Pull Out Worksheets, French, Mathematics (Basic) (Set of 3 books) Question Bank Exam 2022-23 (Based on the latest Syllabus)")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-cbse-class-10-english-language-and-literature-pull-out-worksheets-french-mathematics-basic-set-of-3-books-question-bank-exam-2022-23-based-on-the-latest-syllabus?type=p_book&id=2751");
        } else if (holdvalue.contentEquals("Together With CBSE Question Bank Class 9 English Communicative Pull Out Worksheet, French, Mathematics Exam 2023 (Study Material) Solved Paper Based on Latest Pattern")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-cbse-question-bank-class-9-english-communicative-pull-out-worksheet-french-mathematics-exam-2023-study-material-solved-paper-based-on-latest-pattern?type=p_book&id=2752");
        } else if (holdvalue.contentEquals("Together With CBSE Class 10 English Language & Literature Pull Out Worksheets, French, Mathematics (Standard) (Set of 3 books) Question Bank Exam 2022-23 (Based on the latest Syllabus)")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-cbse-class-10-english-language-and-literature-pull-out-worksheets-french-mathematics-standard-set-of-3-books-question-bank-exam-2022-23-based-on-the-latest-syllabus?type=p_book&id=2753");
        } else if (holdvalue.contentEquals("Together with ICSE English Language Question Bank for Class 9 & 10 (For 2022-2023 Examination)")) {
            startWebView("https://www.rachnasagar.in/mobile/icse/together-with-icse-english-language-question-bank-for-class-9-and-10-for-2022-2023-examination?type=p_book&id=2754");
        } else if (holdvalue.contentEquals("Together With ICSE Class 10 English Literature,Hindi,Biology,Chemistry,Mathematics (Set of 5 books) Question Bank Exam 2022-23 (Based on the latest Syllabus)")) {
            startWebView("https://www.rachnasagar.in/mobile/icse/together-with-icse-class-10-english-literature-hindi-biology-chemistry-mathematics-set-of-5-books-question-bank-exam-2022-23-based-on-the-latest-syllabus?type=p_book&id=2755");
        } else if (holdvalue.contentEquals("Together With ICSE Class 10 Mathematics, English Language, Hindi, Biology, Chemistry (Set of 5 books) Question Bank Exam 2022-23 (Based on the latest Syllabus)")) {
            startWebView("https://www.rachnasagar.in/mobile/icse/together-with-icse-class-10-mathematics-english-language-hindi-biology-chemistry-set-of-5-books-question-bank-exam-2022-23-based-on-the-latest-syllabus?type=p_book&id=2756");
        } else if (holdvalue.contentEquals("Together With ICSE Class 10 Computer Application,English Language,Geography,History & Civics,Hindi (Set of 5 books) Question Bank Exam 2022-23 (Based on the latest Syllabus)")) {
            startWebView("https://www.rachnasagar.in/mobile/icse/together-with-icse-class-10-computer-application-english-language-geography-history-and-civics-hindi-set-of-5-books-question-bank-exam-2022-23-based-on-the-latest-syllabus?type=p_book&id=2758");
        } else if (holdvalue.contentEquals("Together With ICSE Class 10 Geography ,History & Civics,Computer Application,English Literature,Hindi (Set of 5 books) Question Bank Exam 2022-23 (Based on the latest Syllabus)")) {
            startWebView("https://www.rachnasagar.in/mobile/icse/together-with-icse-class-10-geography-history-and-civics-computer-application-english-literature-hindi-set-of-5-books-question-bank-exam-2022-23-based-on-the-latest-syllabus?type=p_book&id=2759");
        } else if (holdvalue.contentEquals("Together With ICSE Class 10 Physics,Chemistry,Biology,English Language,Hindi (Set of 5 books) Question Bank Exam 2022-23 (Based on the latest Syllabus)")) {
            startWebView("https://www.rachnasagar.in/mobile/icse/together-with-icse-class-10-physics-chemistry-biology-english-language-hindi-set-of-5-books-question-bank-exam-2022-23-based-on-the-latest-syllabus?type=p_book&id=2760");
        } else if (holdvalue.contentEquals("Together With ICSE Class 10 Chemistry,Physics,Biology,English Literature,Hindi (Set of 5 books) Question Bank Exam 2022-23 (Based on the latest Syllabus)")) {
            startWebView("https://www.rachnasagar.in/mobile/icse/together-with-icse-class-10-chemistry-physics-biology-english-literature-hindi-set-of-5-books-question-bank-exam-2022-23-based-on-the-latest-syllabus?type=p_book&id=2761");
        } else if (holdvalue.contentEquals("Together With ICSE Class 10 English Literature, Computer Application, Mathematics, History & Civics , Geography(Set of 5 books) Question Bank Exam 2022-23 (Based on the latest Syllabus)")) {
            startWebView("https://www.rachnasagar.in/mobile/icse/together-with-icse-class-10-english-literature-computer-application-mathematics-history-and-civics-geography-set-of-5-books-question-bank-exam-2022-23-based-on-the-latest-syllabus?type=p_book&id=2762");
        } else if (holdvalue.contentEquals("Together With ICSE Class 10 History & Civics, Geography, English Language, Computer Application, Mathematics (Set of 5 books) Question Bank Exam 2022-23 (Based on the latest Syllabus)")) {
            startWebView("https://www.rachnasagar.in/mobile/icse/together-with-icse-class-10-history-and-civics-geography-english-language-computer-application-mathematics-set-of-5-books-question-bank-exam-2022-23-based-on-the-latest-syllabus?type=p_book&id=2763");
        } else if (holdvalue.contentEquals("Together With ICSE Class 10 Biology,Physics,English Literature,Computer Application,Mathematics (Set of 5 books) Question Bank Exam 2022-23 (Based on the latest Syllabus)")) {
            startWebView("https://www.rachnasagar.in/mobile/icse/together-with-icse-class-10-biology-physics-english-literature-computer-application-mathematics-set-of-5-books-question-bank-exam-2022-23-based-on-the-latest-syllabus?type=p_book&id=2765");
        } else if (holdvalue.contentEquals("Together With ICSE Class 10 English Language,Computer Application,Mathematics,Chemistry,Physics (Set of 5 books) Question Bank Exam 2022-23 (Based on the latest Syllabus)")) {
            startWebView("https://www.rachnasagar.in/mobile/icse/together-with-icse-class-10-english-language-computer-application-mathematics-chemistry-physics-set-of-5-books-question-bank-exam-2022-23-based-on-the-latest-syllabus?type=p_book&id=2766");
        } else if (holdvalue.contentEquals("Together With ICSE Class 10 Physics, English Literature, Chemistry, Computer Application, Mathematics(Set of 5 books) Question Bank Exam 2022-23 (Based on the latest Syllabus)")) {
            startWebView("https://www.rachnasagar.in/mobile/icse/together-with-icse-class-10-physics-english-literature-chemistry-computer-application-mathematics-set-of-5-books-question-bank-exam-2022-23-based-on-the-latest-syllabus?type=p_book&id=2767");
        } else if (holdvalue.contentEquals("Together With ICSE Class 10 Biology,Chemistry,Physics,English Language,Mathematics (Set of 5 books) Question Bank Exam 2022-23 (Based on the latest Syllabus)")) {
            startWebView("https://www.rachnasagar.in/mobile/icse/together-with-icse-class-10-biology-chemistry-physics-english-language-mathematics-set-of-5-books-question-bank-exam-2022-23-based-on-the-latest-syllabus?type=p_book&id=2768");
        } else if (holdvalue.contentEquals("Together With ICSE Class 10 Physics, English Literature, Mathematics, Biology, Chemistry (Set of 5 books) Question Bank Exam 2022-23 (Based on the latest Syllabus)")) {
            startWebView("https://www.rachnasagar.in/mobile/icse/together-with-icse-class-10-physics-english-literature-mathematics-biology-chemistry-set-of-5-books-question-bank-exam-2022-23-based-on-the-latest-syllabus?type=p_book&id=2769");
        } else if (holdvalue.contentEquals("Together With ICSE Class 10 Mathematics, Computer Application, Biology, Chemistry, Geography, History & Civics, English Language, Hindi, Physics (Set of 9 books) Question Bank Exam 2022-23 (Based on")) {
            startWebView("https://www.rachnasagar.in/mobile/icse/together-with-icse-class-10-mathematics-computer-application-biology-chemistry-geography-history-and-civics-english-language-hindi-physics-set-of-9-books-question-bank-exam-2022-23-based-on?type=p_book&id=2770");
        } else if (holdvalue.contentEquals("Together With ICSE Class 10 Mathematics, Computer Application, Biology, Chemistry, Geography, History & Civics, English Literature, Hindi, Physics (Set of 9 books) Question Bank Exam 2022-23 (Based o")) {
            startWebView("https://www.rachnasagar.in/mobile/icse/together-with-icse-class-10-mathematics-computer-application-biology-chemistry-geography-history-and-civics-english-literature-hindi-physics-set-of-9-books-question-bank-exam-2022-23-based-o?type=p_book&id=2771");
        } else if (holdvalue.contentEquals("ogether With ICSE Class 10 Mathematics, Computer Application, Biology,Physics,Chemistry, Geography, History & Civics, English Literature (Set of 8 books) Question Bank Exam 2022-23 (Based on the lat")) {
            startWebView("https://www.rachnasagar.in/mobile/icse/together-with-icse-class-10-mathematics-computer-application-biology-physics-chemistry-geography-history-and-civics-english-literature-set-of-8-books-question-bank-exam-2022-23-based-on-the-lat?type=p_book&id=2773");
        } else if (holdvalue.contentEquals("Together With ICSE Class 10 Mathematics, Computer Application, Biology,Physics,Chemistry, Geography, History & Civics, English Language (Set of 8 books) Question Bank Exam 2022-23 (Based on the latest")) {
            startWebView("https://www.rachnasagar.in/mobile/icse/together-with-icse-class-10-mathematics-computer-application-biology-physics-chemistry-geography-history-and-civics-english-language-set-of-8-books-question-bank-exam-2022-23-based-on-the-latest?type=p_book&id=2774");
        } else if (holdvalue.contentEquals("Together With CBSE Class 11 English Core POW Solution Question Bank/Study Material Exam 2023 (Based on the latest Syllabus)")) {
            startWebView("https://www.rachnasagar.in/mobile/cbse/together-with-cbse-class-11-english-core-pow-solution-question-bank-study-material-exam-2023-based-on-the-latest-syllabus?type=p_book&id=2787");

        } else {
            Toast.makeText(LoginActivity.this, "There is no books", Toast.LENGTH_SHORT).show();
        }
    }

}
