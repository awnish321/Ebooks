package com.rachnasagar.fragment;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.rachnasagar.Common.ConnectionDetector;
import com.rachnasagar.Common.Networking;
import com.rachnasagar.R;
import com.rachnasagar.activity.LoginActivity;
import com.rachnasagar.activity.My_Downloads_EBookList;
import com.rachnasagar.activity.My_Downloads_Intra_EBookList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;

public class DownloadFragment extends Fragment implements View.OnClickListener {

    View view;
    CardView Btn_Simple_Ebook, Btn_Interactive_Ebook;
    File ebook_directory;
    File file_details;
    FirebaseRemoteConfig mFirebaseRemoteConfig;
    TextView tv1,tv2,tv3,tv4,tv5;
    //LottieAnimationView lottieAnimationView;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Boolean isInternetPresent = false;
    // Connection detector class
    ConnectionDetector cd;
    PackageInfo pinfo;
    String Login_UserID,Login_Value;
    //TextView textView;
    String Device_Id,Mob_Id,Mob_Product,Mob_Brand,Mob_Manufacture,Mob_Model;
    String Cat_Name ,Str_Marquee ,	Str_Notify_Msg_Link,Str_Notify_Msg_Actvty;
    TextView headerTitleText,Txt_Marquee;
    public static String PACKAGE_NAME;
    String sVersionName;
    int sVersionCode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_download, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Download");
        //fire();
        Device_Id = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
        Mob_Id = android.os.Build.ID;
        Mob_Product= android.os.Build.PRODUCT;
        Mob_Brand= android.os.Build.BRAND;
        Mob_Manufacture= android.os.Build.MANUFACTURER;
        Mob_Model = android.os.Build.MODEL;
        Buttonsdetails();
        somedata();
        MarqueeShow();
        NotifyUpdate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ebook_directory =getContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
            Log.d("filedeatilsebook", String.valueOf(ebook_directory));
        }
        else {
            ebook_directory=(Environment.getExternalStorageDirectory());
            Log.d("filedeatilsebooks", String.valueOf(ebook_directory));
        }
        return view;
    }
    public void somedata() {
        cd = new ConnectionDetector(getActivity());
        preferences = getActivity().getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        Login_UserID = preferences.getString("Login_UserId", "");
        Login_Value =  preferences.getString("Login_Value", "");
        System.out.println("Userid"+" "+Login_UserID);
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            if(Login_UserID.isEmpty()) {
                //	Toast.makeText(SplashScreen.this, "Login_Blank--"+Login_UserID, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();

            }else{
            }
        }else{
            //Misc.showAlertDialog(SplashScreen.this, "No Internet Connection", "You don't have internet connection.", false);
            Aleart();
        }
    }

    public void Aleart(){
        new AlertDialog.Builder(getActivity())
                .setMessage("You don't have internet connection")
                .setTitle("No Internet Connection")
                .setView(R.layout.lottiefile)
                .setIcon(R.drawable.fail)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getActivity().finish();
                    }
                }).show();
    }
    public  void fire(){
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(0)
                .build();
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
        mFirebaseRemoteConfig.fetchAndActivate()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Boolean>() {
                    @Override
                    public void onComplete(@NonNull Task<Boolean> task) {
                        if (task.isSuccessful()) {
                            boolean updated = task.getResult();
                            //Log.d(TAG, "Config params updated: " + updated);
                            //Toast.makeText(getActivity(), "Fetch and activate succeeded", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(getActivity(), "Fetch failed", Toast.LENGTH_SHORT).show();
                        }
                        displayWelcomeMessage();
                    }
                });
    }

    public void displayWelcomeMessage(){
        String s1=mFirebaseRemoteConfig.getString("a");
        String s2=mFirebaseRemoteConfig.getString("b");
        String s3=mFirebaseRemoteConfig.getString("update");
        Log.d("checkfire1",s1);
        Log.d("checkfire2",s2);
        Log.d("checkfire3",s3);
        tv1.setText(s1);
        tv2.setText(s2);
        //tv3.setText(s1);
        //tv4.setText(s2);
        //tv5.setText(s3);
    }

    private void Buttonsdetails() {

        tv1=view.findViewById(R.id.temp5);
        tv2=view.findViewById(R.id.temp6);
        tv3=view.findViewById(R.id.temp7);
        tv4=view.findViewById(R.id.temp8);
       // tv5=view.findViewById(R.id.updatetext);
        //textView=view.findViewById(R.id.logouttext);
        Txt_Marquee= view.findViewById(R.id.Marquee_update);

        Txt_Marquee.setSelected(true);
        Txt_Marquee.setSingleLine(true);

        //lottieAnimationView=view.findViewById(R.id.spinnerImageView1);
        Btn_Simple_Ebook = view.findViewById(R.id.btn_Simple_Ebook);
        Btn_Interactive_Ebook = view.findViewById(R.id.btn_Interactive_Ebook);
        Btn_Simple_Ebook.setOnClickListener(this);
        Btn_Interactive_Ebook.setOnClickListener(this);
        //lottieAnimationView.setOnClickListener(this);
        //textView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {


        if(v==Btn_Simple_Ebook) {

            file_details = new File(ebook_directory, "/.Rachna/eBook");
            Log.d("filedeatils", String.valueOf(file_details));
            Log.d("filedeatils2", String.valueOf(file_details));
            Intent intent1 = new Intent(getActivity(), My_Downloads_EBookList.class);
            intent1.putExtra("To_Open","Ebook");
            startActivity(intent1);

        }

        if(v==Btn_Interactive_Ebook) {

            file_details = new File(ebook_directory, "/.Rachna/Interactive eBook");
            Log.d("filedeatils4", String.valueOf(file_details));
            Intent intent = new Intent(getActivity(), My_Downloads_Intra_EBookList.class);
            intent.putExtra("To_Open","Interactive_Ebook");
            intent.putExtra("hide1","button1");
            startActivity(intent);

        }
    }
    private void LogOutService() {

        preferences = getActivity().getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.clear();
        editor.commit();
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String urlmanual = Networking.url+"logout.php?";
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
                                String Status = object.getString("Status");
                                String Msg = object.getString("msg");
                                System.out.println("dddssss"+"    "+Status+"    "+Msg);
                                if(Status.equalsIgnoreCase("true"))
                                {
                                    if(Login_Value.equalsIgnoreCase("1")) {
                                        //Toast.makeText(getApplicationContext(), "Logout Sucessfully.", Toast.LENGTH_SHORT).show();
                                        Intent intent1 = new Intent(getActivity(), LoginActivity.class);
                                        startActivity(intent1);
                                        getActivity().finish();
                                    }
                                    if(Login_Value.equalsIgnoreCase("2")) {
                                        //	Toast.makeText(getApplicationContext(), "Logout Sucessfully.", Toast.LENGTH_SHORT).show();
                                        Intent intent1 = new Intent(getActivity(), LoginActivity.class);
                                        startActivity(intent1);
                                        getActivity().finish();
                                        try{
                                            // LoginManager.getInstance().logOut();

                                        }catch (Exception e){}

                                    }
                                    if(Login_Value.equalsIgnoreCase("3")) {

                                    }
                                }
                                else{
                                    Toast.makeText(getActivity(), Msg, Toast.LENGTH_SHORT).show();
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
                      //  LogOutService();
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
    private void NotifyUpdate() {



        // TODO Auto-generated method stub

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String urlmanual = Networking.url+"notifyUpdate.php?";
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

    			        		/* User_Name,User_Email_id,User_Mobile_No,User_Shipping_City,User_Shipping_State,User_Shipping_Country,User_Pin_Code,
    			   	          User_Land_Mark,User_Shipping_Address,;*/


                                int notifyupdate;

                                notifyupdate = Integer.parseInt((String) object.get("Notify_Status"));

                                String Status= object.get("status").toString();
                                String MSG= object.get("msg").toString();

                                System.out.println("seeebaaa"+" "+notifyupdate+" "+Status+" "+MSG);

                                if(Status.equalsIgnoreCase("true")) {

                                    if (notifyupdate == 1) {
                                        NotifyUpdateMsg();
                                    }
                                    if (notifyupdate == 2) {
                                        NotifyUpdateMsglink();
                                    }
                                    if (notifyupdate == 3) {
                                        //NotifyUpdateMsgActivity();
                                    }

                                }


                                System.out.println("seeebaaa"+" "+notifyupdate+" "+Status+" "+MSG);



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
            protected HashMap<String, String> getParams() {

                HashMap<String, String>  params = new HashMap<String, String>();
                params.put("notify", "notify");
                params.put("Device_Id", Device_Id);
                params.put("versionCode",Integer.toString(sVersionCode) );
                params.put("versionName", sVersionName);
                params.put("packageName", PACKAGE_NAME);


                return params;
            }
        };
        queue.add(postRequest);





    }
    private void NotifyUpdateMsg() {








        // TODO Auto-generated method stub

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String urlmanual = Networking.url+"notifyMessage.php?";
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
                                String Notify_Status= object.get("status").toString();
                                if(Notify_Status.equalsIgnoreCase("true")) {
                                    String	Str_Notify_Msg=object.get("Notify_Message").toString();
                                    System.out.println("Deegggg"+" "+object.get("Notify_Message").toString());
                                    final Dialog dialoga = new Dialog(getActivity());
                                    dialoga.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    dialoga.setContentView(R.layout.dialog);
                                    dialoga.setCancelable(false);
                                    // set the custom dialog components - text, image and button
                                    TextView text = (TextView) dialoga.findViewById(R.id.error_msg);
                                    text.setText(Str_Notify_Msg);
                                    Button dialogButton = (Button) dialoga.findViewById(R.id.b_error_button);
                                    // if button is clicked, close the custom dialog
                                    dialogButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialoga.dismiss();

                                        }
                                    });

                                    dialoga.show();
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
                params.put("notifymsg", "notifymsg");
                params.put("Device_Id", Device_Id);
				/*	params.put("versionCode",Integer.toString(sVersionCode) );
					params.put("versionName", sVersionName);
					params.put("packageName", PACKAGE_NAME);*/

                return params;
            }
        };
        queue.add(postRequest);
    }
    private void NotifyUpdateMsglink() {
        // TODO Auto-generated method stub
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String urlmanual = Networking.url+"notifyMessage.php?";
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

                                String Notify_Status= object.get("status").toString();

                                if(Notify_Status.equalsIgnoreCase("true"))
                                {

                                    String	Str_Notify_Msg=object.get("Notify_Message").toString();

                                    Str_Notify_Msg_Link=object.get("Notify_Link").toString();


                                    System.out.println("Deegggg"+" "+object.get("Notify_Message").toString());




                                    final Dialog dialoga = new Dialog(getActivity());
                                    dialoga.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    dialoga.setContentView(R.layout.dialog_msg_link);
                                    dialoga.setCancelable(false);

                                    // set the custom dialog components - text, image and button
                                    TextView text = (TextView) dialoga.findViewById(R.id.error_msg);
                                    text.setText(Str_Notify_Msg);



                                    Button dialogButton = (Button) dialoga.findViewById(R.id.b_error_button_link);
                                    // if button is clicked, close the custom dialog
                                    dialogButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {



                                            Intent i = new Intent(Intent.ACTION_VIEW);
                                            i.setData(Uri.parse(Str_Notify_Msg_Link));
                                            startActivity(i);


                                        }
                                    });

                                    Button dialogButtonCan = (Button) dialoga.findViewById(R.id.b_error_button);
                                    // if button is clicked, close the custom dialog
                                    dialogButtonCan.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {


                                            dialoga.dismiss();

                                        }
                                    });

                                    try {
                                        dialoga.show();
                                    }catch (Exception e){}

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
                params.put("notifymsg", "notifymsg");
                params.put("Device_Id", Device_Id);
					/*params.put("versionCode",Integer.toString(sVersionCode) );
					params.put("versionName", sVersionName);
					params.put("packageName", PACKAGE_NAME);*/

                return params;
            }
        };
        queue.add(postRequest);





    }
    private void MarqueeShow() {

        // TODO Auto-generated method stub

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String urlmanual = Networking.url+"mainScreenMarquee.php?";
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

    			        		/* User_Name,User_Email_id,User_Mobile_No,User_Shipping_City,User_Shipping_State,User_Shipping_Country,User_Pin_Code,
    			   	          User_Land_Mark,User_Shipping_Address,;*/
                                Str_Marquee=object.get("Marquee").toString();
                                Txt_Marquee.setText(Str_Marquee);
                                System.out.println("Deegggg"+" "+object.get("Marquee").toString());
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
                params.put("marquee", "marquee");


                return params;
            }
        };
        queue.add(postRequest);
        //up60aj7922
    }
}
