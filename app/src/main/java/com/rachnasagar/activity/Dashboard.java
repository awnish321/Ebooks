package com.rachnasagar.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.rachnasagar.AccountPage;
import com.rachnasagar.Common.ConnectionDetector;
import com.rachnasagar.Common.ConnectionDetector;
import com.rachnasagar.Common.Networking;
import com.rachnasagar.FirstBottomPage;
import com.rachnasagar.R;
import com.rachnasagar.fragment.DemoFragment;
import com.rachnasagar.fragment.DownloadFragment;
import com.rachnasagar.fragment.EbookFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Dashboard extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    FragmentManager fragmentManager;
    private static final int PERMISSION_CALLBACK_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;

    String[] permissionsRequired = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.WRITE_SETTINGS, Manifest.permission.WRITE_APN_SETTINGS};
    private SharedPreferences permissionStatus;
    private boolean sentToSettings = false;

    ProgressHUD dialog;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    String Login_UserID;
    String Device_Id, Mob_Id, Mob_Product, Mob_Brand, Mob_Manufacture, Mob_Model;
    private ArrayList<String> names;
    GridView gv_subcat;
    // flag for Internet connection status
    Boolean isInternetPresent = false;
    // Connection detector class
    ConnectionDetector cd;
    PackageInfo pinfo;
    public static String PACKAGE_NAME;
    String sVersionName;
    int sVersionCode;
    String Login_Value;


    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_dashboard);
        bottomNavigationView = findViewById(R.id.nav_view);
        fragmentManager = getSupportFragmentManager();

        bottomNavigationView.setItemIconTintList(null);
        fragmentManager.beginTransaction().replace(R.id.frame, new DownloadFragment()).commit();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_product1:
                        Intent j=new Intent(Dashboard.this, FirstBottomPage.class);
                        startActivity(j);
                       // fragmentManager.beginTransaction().replace(R.id.frame, new EbookFragment()).commit();
                        break;
                    case R.id.navigation_ebook1:

                        fragmentManager.beginTransaction().replace(R.id.frame, new DownloadFragment()).commit();
                       // fragmentManager.beginTransaction().replace(R.id.frame, new DemoFragment()).commit();
                        break;
                    case R.id.navigation_notification1:
                        Intent k1=new Intent(Dashboard.this, FirstBottomPage.class);
                        startActivity(k1);
                       // fragmentManager.beginTransaction().replace(R.id.frame, new DownloadFragment()).commit();
                        break;
                    case R.id.navigation_account1:
                        Intent k2=new Intent(Dashboard.this, FirstBottomPage.class);
                        //k2.putExtra("key1","account");
                        startActivity(k2);
                       // fragmentManager.beginTransaction().replace(R.id.frame, new DownloadFragment()).commit();
                        break;
                }
                return true;
            }
        });
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        gv_subcat = (GridView) findViewById(R.id.Gv_subcat);
        permissionStatus = getSharedPreferences("permissionStatus", MODE_PRIVATE);
        //creating connection detector class instance
        cd = new ConnectionDetector(getApplicationContext());
        GetAndroidPermission();

        Device_Id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        Mob_Id = android.os.Build.ID;
        Mob_Product = android.os.Build.PRODUCT;
        Mob_Brand = android.os.Build.BRAND;
        Mob_Manufacture = android.os.Build.MANUFACTURER;
        Mob_Model = android.os.Build.MODEL;
        PACKAGE_NAME = getApplicationContext().getPackageName();
        try {
            pinfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (
                PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        sVersionCode = pinfo.versionCode; // 1
        sVersionName = pinfo.versionName; // 1.0

        System.out.println("details versionmain" + "  " + sVersionCode + " " + sVersionName + " " + PACKAGE_NAME);
        preferences = getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        Login_UserID = preferences.getString("Login_UserId", "");
        Login_Value = preferences.getString("Login_Value", "");
        if (Login_UserID.isEmpty()) {
        } else {
            GetAddressDetails();
        }
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            String message = "Please Wait....";
            dialog = new ProgressHUD(Dashboard.this, com.radaee.viewlib.R.style.AppTheme);
            dialog.setTitle("");
            dialog.setContentView(R.layout.progress_hudd);
            if (message == null || message.length() == 0) {
                dialog.findViewById(R.id.message).setVisibility(View.GONE);
            } else {
                TextView txt = (TextView) dialog.findViewById(R.id.message);
                txt.setText(message);
            }
        }
    }
    private void GetAndroidPermission() {
        if (ActivityCompat.checkSelfPermission(Dashboard.this, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(Dashboard.this, permissionsRequired[0])
            ) {
                //Show Information about why you need the permission
                ActivityCompat.requestPermissions(Dashboard.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);

            } else if (permissionStatus.getBoolean(permissionsRequired[0], false)) {
                ActivityCompat.requestPermissions(Dashboard.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);

            } else {
                //just request the permission
                ActivityCompat.requestPermissions(Dashboard.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
            }
            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(permissionsRequired[0], true);
            editor.commit();
        } else {
            //You already have the permission, just go ahead.
            proceedAfterPermission();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CALLBACK_CONSTANT) {
            //check if all permissions are granted
            boolean allgranted = false;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    allgranted = true;
                } else {
                    allgranted = false;
                    break;
                }
            }

            if (allgranted) {
                proceedAfterPermission();
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(Dashboard.this, permissionsRequired[0])) {
                ActivityCompat.requestPermissions(Dashboard.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);

            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(Dashboard.this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }
    }
    private void proceedAfterPermission() {
        //Toast.makeText(getBaseContext(), "We got All Permissions", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (sentToSettings) {
            if (ActivityCompat.checkSelfPermission(Dashboard.this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }
    }
    private void GetAddressDetails() {

        RequestQueue queue = Volley.newRequestQueue(this);
        String urlmanual = Networking.url + "get.php?";
        StringRequest postRequest = new StringRequest(Request.Method.POST, urlmanual,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        try {
                            final JSONArray array;
                            array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object;
                                object = new JSONObject(array.getString(i).toString());
                                preferences = getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
                                editor = preferences.edit();
                                editor.putString("Login_Name", object.get("Name").toString());
                                editor.putString("Login_MobileNo", object.get("Mobile_No").toString());
                                editor.putString("Login_EmailId", object.get("Email_Id").toString());
                                editor.commit();
                                System.out.println("detailldddd" + " " + object.get("Name").toString() + " " + object.get("Mobile_No").toString() + "  " + Login_UserID);
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
                params.put("userId", Login_UserID);
                params.put("action", "address");    // Second one u can change
                return params;
            }
        };
        queue.add(postRequest);
    }


}