package com.rachnasagar.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rachnasagar.Common.ConnectionDetector;
import com.rachnasagar.Common.Networking;
import com.rachnasagar.R;
import com.rachnasagar.adapter.GridViewAdapterEBooksDownload;
import com.rachnasagar.adapter.VinSetterGetter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class myDownloadsEBookList extends AppCompatActivity {

    ProgressHUD dialog;
    String message = "Please Wait....";
    GridView gv_subcat;
    public static String[] name;
    VinSetterGetter getter;
//    ArrayList<VinSetterGetter> vinarrayList;

    ArrayList<VinSetterGetter> ebookList = new ArrayList<VinSetterGetter>();

    String Login_UserID;
    boolean Shopping_List_Status;
    String Shopping_List_Msg;
    SharedPreferences preferences;
    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    Context context;
    File ebook_directory;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.my_downloads_ebook_list);

        context=myDownloadsEBookList.this;
        cd = new ConnectionDetector(getApplicationContext());

        preferences = getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        Login_UserID = preferences.getString("Login_UserId", "");
        // get Internet status
        isInternetPresent = cd.isConnectingToInternet();
//        vinarrayList = new ArrayList<VinSetterGetter>();
        // check for Internet status

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ebook_directory = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        } else {
            ebook_directory = Environment.getExternalStorageDirectory();
        }

        gv_subcat = (GridView) findViewById(R.id.Gv_subcat);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isInternetPresent) {
            MyDownloadsList();
            String message = "Please Wait....";
            dialog = new ProgressHUD(myDownloadsEBookList.this, com.radaee.viewlib.R.style.AppTheme);
            dialog.setTitle("");
            dialog.setContentView(R.layout.progress_hudd);
            if (message == null || message.length() == 0) {
                dialog.findViewById(R.id.message).setVisibility(View.GONE);
            } else {
                TextView txt = (TextView) dialog.findViewById(R.id.message);
                txt.setText(message);
            }
            dialog.setCancelable(true);
            Window window = dialog.getWindow();
            WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
            lp.dimAmount = 0.2f;
            dialog.getWindow().setAttributes(lp);
            dialog.show();
        } else {
            final Dialog dialoga = new Dialog(this);
            dialoga.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialoga.setContentView(R.layout.dialog);
            Window window = dialoga.getWindow();
            window.setLayout(WindowManager.LayoutParams.FILL_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            dialoga.setCancelable(false);
            TextView text = (TextView) dialoga.findViewById(R.id.error_msg);
            text.setText("You don't have internet connection. Please Check ?");
            Button dialogButton = (Button) dialoga.findViewById(R.id.b_error_button);
            // if button is clicked, close the custom dialog
            dialogButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    finish();
                }
            });
            dialoga.show();
        }

    }

    private void MyDownloadsList() {

        RequestQueue queue = Volley.newRequestQueue(this);
        String urlmanual = Networking.url + "eBookMyDownloadList.php?";
        StringRequest postRequest = new StringRequest(Request.Method.POST, urlmanual,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        try {
                            final JSONArray array;
                            array = new JSONArray(response);
                            JSONObject object;
                            ebookList.clear();
                            for (int i = 0; i < array.length(); i++) {
                                getter = new VinSetterGetter();
                                object = new JSONObject(array.getString(i).toString());
                                Shopping_List_Status = Boolean.parseBoolean(object.getString("status"));
                                Shopping_List_Msg = object.getString("msg");
                                if (Shopping_List_Status == true) {
                                    getter.setCategory(object.getString("Category"));
                                    getter.setBookID(object.getString("Book_ID"));
                                    getter.setBookImage_Url(object.getString("Img_Url"));
                                    getter.setSubCategory(object.getString("Sub_Category"));
                                    getter.setSubject(object.getString("Subject"));
                                    getter.setDownloadId(object.getString("Download_ID"));
                                    getter.setClassName(object.getString("Class"));
                                    getter.setBookType(object.getString("Book_Type"));
                                    getter.setBookName(object.getString("Book_Name"));
                                    getter.setProduct_Subscription_Type(object.getString("Product_Subscription_Type"));
                                    ebookList.add(getter);
                                }
                                else
                                {
                                    final Dialog dialoga = new Dialog(myDownloadsEBookList.this);
                                    dialoga.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    dialoga.setContentView(R.layout.dialog_response_server);
                                    dialoga.setCancelable(true);
                                    // set the custom dialog components - text, image and button
                                    TextView text = (TextView) dialoga.findViewById(R.id.error_msg);
                                    text.setText(Shopping_List_Msg);
                                    Button dialogButton = (Button) dialoga.findViewById(R.id.b_error_button);
                                    // if button is clicked, close the custom dialog
                                    dialogButton.setOnClickListener(new OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialoga.dismiss();
                                            myDownloadsEBookList.this.finish();
                                            startActivity(new Intent(myDownloadsEBookList.this, Dashboard.class));
                                        }
                                    });
                                    dialoga.show();
                                }
//                                gv_subcat.setOnItemClickListener(new AdapterView.OnItemClickListener()
//                                {
//                                    @Override
//                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
//                                    {
//                                        try {
//                                            Details = array.get(position).toString();
//                                        } catch (JSONException e) {
//
//                                            e.printStackTrace();
//                                        }
//                                        try {
//                                            JSONObject myObject = new JSONObject(Details);
//                                            Download_Id = myObject.getString("Download_ID");
//                                        } catch (JSONException e) {
//
//                                            e.printStackTrace();
//                                        }
//                                        Intent intent = new Intent(myDownloadsEBookList.this, Book_Download_Page.class);
//                                        intent.putExtra("Download_Id", Download_Id);
//                                        intent.putExtra("key1", "value1");
//                                        startActivity(intent);
//
//                                    }
//                                });
                            }
                            GridViewAdapterEBooksDownload adapter = new GridViewAdapterEBooksDownload(context, ebookList);
                            gv_subcat.setAdapter(adapter);

                            if (dialog.isShowing())
                                dialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        try {
                            MyDownloadsList();
                        } catch (Exception e) {

                        }
                    }
                }
        )
        {
            @Override
            protected HashMap<String, String> getParams() {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("userId", Login_UserID);
                params.put("bookType", "eBook");
                return params;
            }
        };
        queue.add(postRequest);
    }

}
