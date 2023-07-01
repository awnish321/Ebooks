package com.rachnasagar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rachnasagar.Common.ConnectionDetector;
import com.rachnasagar.Common.Networking;
import com.rachnasagar.activity.Dashboard;
import com.rachnasagar.activity.LoginActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class SplashScreen extends AppCompatActivity {

    private static final int TIME = 2* 1000;// 2 seconds
    String Device_Id,Mob_Id,Mob_Product,Mob_Brand,Mob_Manufacture,Mob_Model,Str_UserId;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    // flag for Internet connection status
    Boolean isInternetPresent = false;
    // Connection detector class
    ConnectionDetector cd;
    PackageInfo pinfo;
    public static String PACKAGE_NAME;
    String sVersionName;
    int sVersionCode;
    String Login_UserID,Login_Value ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        cd = new ConnectionDetector(getApplicationContext());
        GetDevicedetails();
        PACKAGE_NAME = getApplicationContext().getPackageName();
        try {
            pinfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        getSupportActionBar().hide();

        sVersionCode = pinfo.versionCode; // 1
        sVersionName = pinfo.versionName; // 1.0
        System.out.println("details version"+"  "+sVersionCode+" "+sVersionName+" "+PACKAGE_NAME);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run()
            {
                preferences = getSharedPreferences("LoginDetails",Context.MODE_PRIVATE);
                Login_UserID = preferences.getString("Login_UserId", "");
                Login_Value =  preferences.getString("Login_Value", "");
                System.out.println("Userid"+" "+Login_UserID);
                isInternetPresent = cd.isConnectingToInternet();
                if (isInternetPresent)
                {
                    if(Login_UserID.isEmpty())
                    {
                        //	Toast.makeText(SplashScreen.this, "Login_Blank--"+Login_UserID, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                        startActivity(intent);
                        SplashScreen.this.finish();

                    }else{
                        //	Toast.makeText(SplashScreen.this, "Login_Filled--"+Login_UserID, Toast.LENGTH_SHORT).show();
                        CheckLoginStatus();
                    }
                }else{
                   Aleart();
                }

			/*	Intent intent = new Intent(SplashScreen.this,
						MainActivityNew.class);
				startActivity(intent);*/

                //SplashScreen.this.finish();

              //  overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

            }
        }, TIME);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
            }
        }, TIME);

    }

    public void Aleart(){
    new AlertDialog.Builder(SplashScreen.this)
            .setMessage("You don't have internet connection")
            .setTitle("No Internet Connection")
            .setView(R.layout.lottiefile)
            .setIcon(R.drawable.fail)
            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    finish();
                }
            }).show();
}

    @Override
    public void onBackPressed() {
        this.finish();
        super.onBackPressed();
    }

    private void GetDevicedetails() {
        Device_Id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Mob_Id = android.os.Build.ID;
        Mob_Product= android.os.Build.PRODUCT;
        Mob_Brand= android.os.Build.BRAND;
        Mob_Manufacture= android.os.Build.MANUFACTURER;
        Mob_Model = android.os.Build.MODEL;
    }

    private void CheckLoginStatus() {
        // TODO Auto-generated method stub
        RequestQueue queue = Volley.newRequestQueue(this);
        String urlmanual = Networking.url+"checkLoginStatus.php?";
        StringRequest postRequest = new StringRequest(Request.Method.POST, urlmanual,
                new Response.Listener<String>()
                {
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
                                String Status=object.getString("status");
                                String Msg=object.getString("msg");
                                System.out.println("Activation" + " "+Status+"   "+Msg );
                                //--Always be true...--- When we want user to force fully logout then flase is used.....
                                if(Status.equalsIgnoreCase("true")) {
                                    Intent intent = new Intent(SplashScreen.this, FirstBottomPage.class);
                                    startActivity(intent);
                                    SplashScreen.this.finish();
                                    System.out.println("Activationupdate" + " "+Status+"   "+Msg );
                                }else {
                                    Toast.makeText(SplashScreen.this, Msg, Toast.LENGTH_LONG).show();
                                    preferences =getSharedPreferences("LoginDetails", 0);
                                    editor = preferences.edit();
                                    editor.clear();
                                    editor.commit();
                                    LogOutService();
                                }
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        CheckLoginStatus();
                    }
                }
        ) {
            @Override
            protected HashMap<String, String> getParams() {
                HashMap<String, String>  params = new HashMap<String, String>();
                params.put("userId", Login_UserID);
                params.put("deviceId", Device_Id);
                params.put("action", "checkLogin");
                return params;
            }
        };
        queue.add(postRequest);
    }

    private void LogOutService() {

        RequestQueue queue = Volley.newRequestQueue(this);
        String urlmanual = Networking.url+"logout.php?";
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
                                String Status = object.getString("Status");
                                String Msg = object.getString("msg");
                                System.out.println("dddssss"+"    "+Status+"    "+Msg);
                                if(Status.equalsIgnoreCase("true")) {
                                    if(Login_Value.equalsIgnoreCase("1")) {
                                        //Toast.makeText(getApplicationContext(), "Logout Sucessfully.", Toast.LENGTH_SHORT).show();
                                        Intent intent1 = new Intent(SplashScreen.this, LoginActivity.class);
                                        startActivity(intent1);
                                        SplashScreen.this.finish();
                                    }
                                    if(Login_Value.equalsIgnoreCase("2")) {
                                        //	Toast.makeText(getApplicationContext(), "Logout Sucessfully.", Toast.LENGTH_SHORT).show();
                                        Intent intent1 = new Intent(SplashScreen.this, LoginActivity.class);
                                        startActivity(intent1);
                                        SplashScreen.this.finish();
                                        try{
                                           // LoginManager.getInstance().logOut();
                                        }catch (Exception e){}
                                    }
                                    if(Login_Value.equalsIgnoreCase("3")) {

                                    }
                                }
                                else{
                                    Toast.makeText(SplashScreen.this, Msg, Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        LogOutService();
                    }
                }
        ) {
            @Override
            protected HashMap<String, String> getParams() {
                HashMap<String, String>  params = new HashMap<String, String>();
                params.put("userId", Login_UserID);
                params.put("action", "logout");
                return params;
            }
        };
        queue.add(postRequest);
    }

    public void MobileDeviceUpdate() {

        System.out.println("fuckoffss"+"    "+"aaStatus");

        // TODO Auto-generated method stub

        RequestQueue queue = Volley.newRequestQueue(this);
        String urlmanual = Networking.url+"mobile_devices.php?";
        StringRequest postRequest = new StringRequest(Request.Method.POST, urlmanual,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        try {
                            final JSONArray array;
                            array = new JSONArray(response);


                            System.out.println("fuckoffeee"+"    "+"Status");

                            for (int i = 0; i < array.length(); i++) {

                                JSONObject object;
                                object = new JSONObject(array.getString(i).toString());
                                String Status=object.getString("status");
                                System.out.println("fuckoffaa"+"    "+Status);
                                if(Status.equalsIgnoreCase("true"))
                                {
                                    System.out.println("fuckoff"+"    "+Status);
                                }
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        // Log.d("Error.Response", error.getMessage());
                    }
                }
        ) {
            @Override
            protected HashMap<String, String> getParams()
            {
                HashMap<String, String>  params = new HashMap<String, String>();
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
}
