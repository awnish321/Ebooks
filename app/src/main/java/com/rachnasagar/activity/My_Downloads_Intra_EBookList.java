package com.rachnasagar.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rachnasagar.Common.ConnectionDetector;
import com.rachnasagar.Common.Networking;
import com.rachnasagar.R;
import com.rachnasagar.adapter.GridViewAdapterBooksDownload;
import com.rachnasagar.adapter.VinSetterGetter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class My_Downloads_Intra_EBookList extends Activity{


	private static String URLBooks;
	ProgressHUD dialog;
	String message = "Please Wait....";
	ProgressDialog progressDialog;
	GridView gv_subcat;
	public static String[] name;
	public static String[] image_url;
	public static String[] price;
	public static String[] mrp;
	public static String[] book_id;
	VinSetterGetter getter;
	ArrayList<VinSetterGetter> vinarrayList;
	String Cat_Name, Category, Sub_name, Class_Name, Download_Id, Details, Login_UserID;
	boolean Shopping_List_Status;
	String Shopping_List_Msg;
	ImageView headerTitleImage, header_search;
	TextView headerTitleText;
	SharedPreferences preferences;
	SharedPreferences.Editor editor;
	// flag for Internet connection status
	Boolean isInternetPresent = false;
	// Connection detector class
	ConnectionDetector cd;

     	
     	 public void onCreate(Bundle savedInstanceState) {
     	 	super.onCreate(savedInstanceState);
     	 	this.requestWindowFeature(Window.FEATURE_NO_TITLE);
 	        setContentView(R.layout.my_downloads_ebook_list);

 	        //creating connection detector class instance
 	  			cd = new ConnectionDetector(getApplicationContext());
 	  			preferences = getSharedPreferences("LoginDetails",Context.MODE_PRIVATE);
 	  			Login_UserID = preferences.getString("Login_UserId", "");
 	  			// get Internet status
				isInternetPresent = cd.isConnectingToInternet();
				// check for Internet status
				if (isInternetPresent) {
					MyDownloadsList();
						String message = "Please Wait....";
			 			dialog = new ProgressHUD(My_Downloads_Intra_EBookList.this, com.radaee.viewlib.R.style.AppTheme);
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
				} else {
					// Internet connection is not present
					// Ask user to connect to Internet
					/*Misc.showAlertDialog(SubCat1.this, "No Internet Connection",
							"You don't have internet connection.", false);*/
					final Dialog dialoga = new Dialog(this);
					dialoga.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dialoga.setContentView(R.layout.dialog);
					Window window = dialoga.getWindow();
					window.setLayout(WindowManager.LayoutParams.FILL_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
					dialoga.setCancelable(false);
					// set the custom dialog components - text, image and button
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
				gv_subcat=(GridView)findViewById(R.id.Gv_subcat);

 }

 			private void MyDownloadsList() {
     	 	RequestQueue queue = Volley.newRequestQueue(this);
    		String urlmanual = Networking.url+"eBookMyDownloadList.php?";
    		StringRequest postRequest = new StringRequest(Method.POST, urlmanual, 
    		    new Response.Listener<String>() {
    		        @Override
    		        public void onResponse(String response) {
    		            // response
    		            Log.d("Response", response);
    		            try {
    						 final JSONArray array;
    						 array = new JSONArray(response);
    						 JSONObject object;
    						 vinarrayList = new ArrayList<VinSetterGetter>();
    						for (int i = 0; i < array.length(); i++) {
    							getter = new VinSetterGetter();
    							object = new JSONObject(array.getString(i).toString());
    							Shopping_List_Status =Boolean.parseBoolean(object.getString("status"));
    							Shopping_List_Msg= object.getString("msg");
    							if(Shopping_List_Status == true ) {
    			        		getter.setCategory(object.getString("Category"));
    			                getter.setBookID(object.getString("Book_ID"));
    			                getter.setBookImage_Url(object.getString("Img_Url"));
    			                getter.setSubCategory(object.getString("Sub_Category"));
    			                getter.setSubject(object.getString("Subject"));
    			                getter.setDownloadId(object.getString("Download_ID"));
    			                getter.setClassName(object.getString("Class"));
    			                getter.setBookType(object.getString("Book_Type"));
    			        		getter.setBookName(object.getString("Book_Name"));
    			                vinarrayList.add(getter);
    			                GridViewAdapterBooksDownload adapter = new GridViewAdapterBooksDownload(My_Downloads_Intra_EBookList.this,vinarrayList );
    			           	 gv_subcat.setAdapter(adapter);
    							}else
    			        		 {
    			        			 final Dialog dialoga = new Dialog(My_Downloads_Intra_EBookList.this);
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
    											My_Downloads_Intra_EBookList.this.finish();
    											startActivity(new Intent(My_Downloads_Intra_EBookList.this, Dashboard.class));
    										}
    									});
    						 
    									dialoga.show();
    			        			 
    			        		 }
    							if (dialog.isShowing())
    								dialog.dismiss();
    							gv_subcat.setOnItemClickListener(new OnItemClickListener(){
    								@Override
    						public void onItemClick(AdapterView<?> parent, View view,
    								int position, long id) {
    							// TODO Auto-generated method stub
										try {
									Details= array.get(position).toString();
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
										try {
									JSONObject myObject = new JSONObject(Details);
									
									 Download_Id= myObject.getString("Download_ID");
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
										Intent intent = new Intent(My_Downloads_Intra_EBookList.this, Book_Download_Page.class);
										intent.putExtra("Download_Id", Download_Id);
										intent.putExtra("key1", "value2");
										startActivity(intent);
    								}
    							});
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
							 MyDownloadsList();
						 } catch (Exception e) {
							 // TODO: handle exception
						 }
    		       }
    		    }
    		) {     
    		    @Override
    		    protected HashMap<String, String> getParams() {
    		    	HashMap<String, String>  params = new HashMap<String, String>();
    		    	params.put("userId", Login_UserID);
    		    	params.put("bookType", "Interactive eBook");
    		    	return params;
    		    }
    		};
    		queue.add(postRequest);
     	 }
}
