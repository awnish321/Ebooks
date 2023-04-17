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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.NetworkResponse;
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
import com.rachnasagar.activity.ProgressHUD;
import com.rachnasagar.adapter.GridViewAdapter;
import com.rachnasagar.adapter.VinSetterGetter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Handler;


public class EbookFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "config";
    View view;
    CardView Btn_Simple_Ebook1, Btn_Interactive_Ebook1;
    String PdfFile ;
    File ebook_directory;
    File file_details;
    FragmentManager fm;
    FirebaseRemoteConfig mFirebaseRemoteConfig;
    TextView tv1,tv2,tv3,tv4;

    String Login_UserID,Login_Value ;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Boolean isInternetPresent = false;
    // Connection detector class
    ConnectionDetector cd;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_ebook, container, false);
       // fm.beginTransaction().replace(R.id.frame.new PDFNavAct)
        Buttonsdetails();

        // fire();
        somedata();

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
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder().setMinimumFetchIntervalInSeconds(0).build();
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
        mFirebaseRemoteConfig.fetchAndActivate()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Boolean>() {
                    @Override
                    public void onComplete(@NonNull Task<Boolean> task) {
                        if (task.isSuccessful()) {
                            boolean updated = task.getResult();
                            Log.d(TAG, "Config params updated: " + updated);
                            //Toast.makeText(getActivity(), "Fetch and activate succeeded", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "Fetch failed", Toast.LENGTH_SHORT).show();
                        }
                        displayWelcomeMessage();
                    }
                });
    }
    private void Buttonsdetails() {

        tv1=view.findViewById(R.id.temp1);
        tv2=view.findViewById(R.id.temp2);
        tv3=view.findViewById(R.id.temp3);
        tv4=view.findViewById(R.id.temp4);



        Btn_Simple_Ebook1=view.findViewById(R.id.btn_ebooks);
        Btn_Interactive_Ebook1=view.findViewById(R.id.btn_interebooks);
        Btn_Simple_Ebook1.setOnClickListener(this);
        Btn_Interactive_Ebook1.setOnClickListener(this);


    }

    public void displayWelcomeMessage(){
        String s1=mFirebaseRemoteConfig.getString("a");
        String s2=mFirebaseRemoteConfig.getString("b");

        Log.d("checkfire1",s1);
        Log.d("checkfire2",s2);

        //tv1.setText(s1);
       // tv2.setText(s2);
        //tv3.setText(s1);
       // tv4.setText(s2);


    }
    @Override
    public void onClick(View v) {

       /* if(v==Btn_Simple_Ebook1) {
            file_details = new File(ebook_directory, "/.Rachna/eBook");
            Intent intent = new Intent(getActivity(),com.radaee.reader.PDFNavAct.class);
            intent.putExtra("To_Open","Ebook");
            Log.d("svvi", String.valueOf(file_details));
            startActivity(intent);
            if(file_details.isDirectory()){
                if(file_details.list().length>0){
                   *//* Intent intent = new Intent(getActivity(),com.radaee.reader.PDFNavAct.class);
                    intent.putExtra("To_Open","Ebook");
                    Log.d("svvi","ye whi hai");
                    startActivity(intent);*//*
                }
                else{
                    Toast.makeText(getActivity(), "Please download ebook11. ", Toast.LENGTH_SHORT).show();
                }
            }else{
                // Toast.makeText(getActivity(), "Please download ebook12. ", Toast.LENGTH_SHORT).show();
            }
        }
        if(v==Btn_Interactive_Ebook1) {
            file_details = new File(ebook_directory, "/.Rachna/Interactive eBook");
            Intent intent = new Intent(getActivity(),com.radaee.reader.PDFNavAct.class);
            intent.putExtra("To_Open","Interactive_Ebook");
            Log.d("svvi11", String.valueOf(file_details));
            startActivity(intent);
            if(file_details.isDirectory()){
                if(file_details.list().length>0){
                   *//*Intent intent = new Intent(getActivity(),com.radaee.reader.PDFNavAct.class);
                    intent.putExtra("To_Open","Interactive_Ebook");
                    startActivity(intent);
*//*
                }else {
                    Toast.makeText(getActivity(), "Please download Interactive ebook first13. ", Toast.LENGTH_SHORT).show();
                }
            }else {
               // Toast.makeText(getActivity(), "Please download Interactive ebook first14. ", Toast.LENGTH_SHORT).show();
            }
        }
    }

*/
        if(v==Btn_Simple_Ebook1) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                Log.d("rootpath", "111");
                file_details = new File("/storage/emulated/0/Android/data/com.rachnasagar/files/Documents/.Rachna/eBook");
                Log.d("tytytyt",file_details.toString());
                if(file_details.isDirectory()){
                    if(file_details.list().length>0){
                        Intent intent = new Intent(getActivity(),com.radaee.reader.PDFNavAct.class);
                        intent.putExtra("To_Open","Ebook");
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(getActivity(), "Please download ebook. ", Toast.LENGTH_SHORT).show();
                    }
                }else{

                    Toast.makeText(getActivity(), "Please download ebook. ", Toast.LENGTH_SHORT).show();
                }
                //headerTitleText.setText("eBook-Downloads");
            }else{
                file_details = new File(Environment.getExternalStorageDirectory(), "/.Rachna/eBook");
                Log.d("tytytyt",file_details.toString());
                if(file_details.isDirectory()){
                    if(file_details.list().length>0){
                        Intent intent = new Intent(getActivity(),com.radaee.reader.PDFNavAct.class);
                        intent.putExtra("To_Open","Ebook");
                        startActivity(intent);

                    }
                    else{
                        Toast.makeText(getActivity(), "Please download ebook. ", Toast.LENGTH_SHORT).show();
                    }
                }else{

                    Toast.makeText(getActivity(), "Please download ebook. ", Toast.LENGTH_SHORT).show();
                }
            }
        }
        if(v==Btn_Interactive_Ebook1) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                Log.d("rootpath", "111");

                file_details = new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "/.Rachna/Interactive eBook");
                Log.d("tytytyt33",file_details.toString());
                if(file_details.isDirectory()){
                    if(file_details.list().length>0){
                        Intent intent = new Intent(getActivity(),com.radaee.reader.PDFNavAct.class);
                        intent.putExtra("To_Open","Interactive_Ebook");
                        startActivity(intent);

                    }else {
                        Toast.makeText(getActivity(), "Please download Interactive ebook first. ", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getActivity(), "Please download Interactive ebook first. ", Toast.LENGTH_SHORT).show();
                }
                //headerTitleText.setText("eBook-Downloads");
            }else{
                file_details = new File(Environment.getExternalStorageDirectory(), "/.Rachna/Interactive eBook");
                if(file_details.isDirectory()){
                    if(file_details.list().length>0){
                        Intent intent = new Intent(getActivity(),com.radaee.reader.PDFNavAct.class);
                        intent.putExtra("To_Open","Interactive_Ebook");
                        startActivity(intent);

                    }else {
                        Toast.makeText(getActivity(), "Please download Interactive ebook first. ", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getActivity(), "Please download Interactive ebook first. ", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}


