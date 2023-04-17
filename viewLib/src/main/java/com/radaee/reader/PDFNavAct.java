package com.radaee.reader;

import java.io.BufferedReader;
import java.io.File;

import java.util.ArrayList;

import com.radaee.pdf.Document;
import com.radaee.pdf.Global;
import com.radaee.util.PDFGridItem;
import com.radaee.util.PDFGridView;
import com.radaee.viewlib.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.content.ContentValues.TAG;

@SuppressLint("SdCardPath")
public class PDFNavAct extends Activity implements OnItemClickListener {

	String url = "http://rachnasagar.in/rsplws/ebookUserCheck.php?";
	//String url = "http://rachnasagar.in/rsplws/ebookUserCheck.php?userId=6&deviceId=11c313efe6821bc8";
	ProgressDialog dialog;
	int ret;
    Document doc;
    PDFGridItem item;
	private LinearLayout m_layout;
	private PDFGridView m_grid;
	Button Menubutton;
	String To_Open,Str_Msg,Str_Status;
	Boolean hide;
	ImageView headerTitleImage;
	ImageView header_search;
    @SuppressLint("InlinedApi")
	private File root;
	private ArrayList<File> fileList = new ArrayList<File>();
	private LinearLayout view;
	String Login_UserID,ZipTitle,Str_getnew;
	String Device_Id,Mob_Id,Mob_Product,Mob_Brand,Mob_Manufacture,Mob_Model;
	SharedPreferences preferences;
	Intent getstring;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //plz set this line to Activity in AndroidManifes.xml:
        //android:configChanges="orientation|keyboardHidden|screenSize"
        //otherwise, APP shall destroy this Activity and re-create a new Activity when rotate. 
        Global.Init( this );
        To_Open=getIntent().getExtras().getString("To_Open");
		m_layout = (LinearLayout)LayoutInflater.from(this).inflate(R.layout.pdf_nav, null);
		m_grid = (PDFGridView)m_layout.findViewById(R.id.pdf_nav);
		//m_path = (EditText)m_layout.findViewById(R.id.txt_path);
		//m_grid.PDFSetRootPath("/mnt/sdcard/Rachnasagar");
		m_grid.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View view) {

				return true;
			}
		});
		GetDevicedetails();
		preferences = getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
		Login_UserID = preferences.getString("Login_UserId", "");
		//	m_path.setText(m_grid.getPath());
		//m_path.setEnabled(false);
		m_grid.setOnItemClickListener(this);
		setContentView(m_layout);

		Log.d("ss55"    ,"u7u7u7");
		//m_grid.PDFSetRootPath("/storage/emulated/0/Android/data/com.rachnasagar/files/Documents/.Rachnasagar/eBook");
		if(To_Open.equalsIgnoreCase("Ebook")) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
				Log.d("rootpath", "111");
				m_grid.PDFSetRootPath("/storage/emulated/0/Android/data/com.rachnasagar/files/Documents/.Rachna/eBook");
				//headerTitleText.setText("eBook-Downloads");

			} else {
				Log.d("rootpath", "111");
				m_grid.PDFSetRootPath("/storage/emulated/0/.Rachna/eBook");
				//headerTitleText.setText("eBook-Downloads");
			}
		}else{

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
				     m_grid.PDFSetRootPath("/storage/emulated/0/Android/data/com.rachnasagar/files/Documents/.Rachna/Interactive eBook");
					//headerTitleText.setText("Interactive eBook-Downloads");
			} else {
				Log.d("rootpath", "111");
				//m_grid.PDFSetRootPath("/storage/emulated/0/.Rachna/Interactive eBook");
				m_grid.PDFSetRootPath("/storage/emulated/0/.Rachna/Interactive eBook");
				//headerTitleText.setText("Interactive eBook-Downloads");
			}
		}
		getstring=getIntent();
	}
    public ArrayList<File> getfile(File dir) {
		File listFile[] = dir.listFiles();
		if (listFile != null && listFile.length > 0) {
			for (int i = 0; i < listFile.length; i++) {

				if (listFile[i].isDirectory()) {
					fileList.add(listFile[i]);
					getfile(listFile[i]);

				} else {
					if (listFile[i].getName().endsWith(".png")
							|| listFile[i].getName().endsWith(".jpg")
							|| listFile[i].getName().endsWith(".jpeg")
							|| listFile[i].getName().endsWith(".gif"))
					{
						fileList.add(listFile[i]);
					}
				}

			}
		}
		return fileList;
	}

	@SuppressLint("NewApi")
	private void GetDevicedetails() {

		Device_Id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
		Mob_Id = android.os.Build.ID;
		Mob_Product= android.os.Build.PRODUCT;
		Mob_Brand= android.os.Build.BRAND;
		Mob_Manufacture= android.os.Build.MANUFACTURER;
		Mob_Model = android.os.Build.MODEL;
	}
		
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
    @SuppressLint("InlinedApi")
	@Override
    protected void onDestroy()
    {
    	super.onDestroy();
    }
    private void onFail(Document doc, String msg)//treat open failed.
    {
    	doc.Close();
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
    private void InputPswd(PDFGridItem item)//treat password
    {
		LinearLayout layout = (LinearLayout)LayoutInflater.from(this).inflate(R.layout.dlg_pswd, null);
		final EditText tpassword =layout.findViewById(R.id.txt_password);
		final PDFGridItem gitem = item;

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int which)
			{
				String password = tpassword.getText().toString();
				Log.d("key2345",password);
				Document doc = new Document();
				int ret = gitem.open_doc(doc, password);
				switch( ret )
				{
				case -1://need input password
					doc.Close();
					InputPswd(gitem);
					break;
				case -2://unknown encryption
					onFail(doc, "Open Failed: Unknown Encryption");
					break;
				case -3://damaged or invalid format
					onFail(doc, "Open Failed: Damaged or Invalid PDF file");
					break;
				case -10://access denied or invalid file path
					onFail(doc, "Open Failed: Access denied or Invalid path");
					break;
				case 0://succeeded, and continue
					InitView(doc);
					break;
				default://unknown error
					onFail(doc, "Open Failed: Unknown Error");
					break;
				}
			}});
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
			}
		});
		builder.setTitle("Input Password");
		builder.setCancelable(false);
		builder.setView(layout);

		AlertDialog dlg = builder.create();
		dlg.show();
    }
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)//listener for icon clicked.
	{
		 item = (PDFGridItem)arg1;
		if( item.is_dir() ) {
			m_grid.PDFGotoSubdir(item.get_name());
			//m_path.setText(m_grid.getPath());
		}
		else {
		    doc = new Document();
		    ret = item.open_doc(doc, null);
		    System.out.println("nameeeeedd" + "    " + item.get_name()+" "+Login_UserID+" "+Device_Id);
		    ZipTitle= item.get_name();
		    Str_getnew = ZipTitle.substring(ZipTitle.length() - 5);
		    System.out.println("lastaaaaa" + "      " + ZipTitle + "    " + Str_getnew);
		    //CheckEbookURL();
			//String Last5 = item.get_name();
			 new GetCheckStatus().execute();
		}
	}

	public class GetCheckStatus extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			dialog = new ProgressDialog(PDFNavAct.this);
			dialog.setMessage("Please wait...");
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			HttpHandler sh = new HttpHandler();
			String urlNew = url+"userId="+Login_UserID+"&deviceId="+Device_Id+"&zipTitle="+ZipTitle+"&bookType="+To_Open;
			// Making a request to url and getting response
			String jsonStr = sh.makeServiceCall(urlNew);
			System.out.println("CheckURK"+" "+urlNew);
			Log.e(TAG, "Response from url: " + jsonStr+" "+To_Open+" "+urlNew);
			if (jsonStr != null) {
				try {
					JSONObject jsonObj = new JSONObject(jsonStr);
					// Getting JSON Array node
					Str_Status = String.valueOf(jsonObj.get("status"));
					Str_Msg = String.valueOf(jsonObj.get("msg"));
					System.out.println("GOTIT"+" "+Str_Status+" "+Str_Msg);
					if(Str_Status.equalsIgnoreCase("TRUE")) {
						System.out.println("nameeeeedd" + "    " + Str_Status);
			/*StringBuffer buffer = new StringBuffer(array[0]);
			char firstCharacter = buffer.charAt(5);
			char lastCharacter = buffer.charAt(5);*/
						if (Str_getnew.equalsIgnoreCase("Hindi")) {
							hide = true;
						} else {
							hide = false;
						}
						switch (ret) {
							case -1://need input password
								doc.Close();
								InputPswd(item);
								break;
							case -2://unknown encryption
								onFail(doc, "Open Failed: Unknown Encryption");
								break;
							case -3://damaged or invalid format
								onFail(doc, "Open Failed: Damaged or Invalid PDF file");
								break;
							case -10://access denied or invalid file path
								onFail(doc, "Open Failed: Access denied or Invalid path");
								break;
							case 0://succeeded, and continue
								InitView(doc);
								break;
							default://unknown error
								onFail(doc, "Open Failed: Unknown Error");
								break;
						}
					}else
					{
						PDFNavAct.this.runOnUiThread(new Runnable() {
							public void run() {
								Toast.makeText(PDFNavAct.this, Str_Msg, Toast.LENGTH_SHORT).show();
							}
						});
						//Toast.makeText(getApplicationContext(),Str_Msg,Toast.LENGTH_SHORT).show();
					}
				} catch (final JSONException e) {
					Log.e(TAG, "Json parsing error: " + e.getMessage());
					runOnUiThread(new Runnable() {
						@Override
						public void run() {

							System.out.println("GOTIT"+" "+e.getMessage());
							Toast.makeText(getApplicationContext(),
									"Json parsing error: " + e.getMessage(),
									Toast.LENGTH_LONG)
									.show();
						}
					});
				}
			} else {
				Log.e(TAG, "Couldn't get json from server.");
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						final Dialog dialoga = new Dialog(PDFNavAct.this);
						dialoga.requestWindowFeature(Window.FEATURE_NO_TITLE);
						dialoga.setContentView(R.layout.dialog1);
						Window window = dialoga.getWindow();
						window.setLayout(WindowManager.LayoutParams.FILL_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
						dialoga.setCancelable(false);
						// set the custom dialog components - text, image and button
						TextView text = (TextView) dialoga.findViewById(R.id.error_msg);
						text.setText("You don't have internet connection. Please Check ?");
						Button dialogButton = (Button) dialoga.findViewById(R.id.b_error_button1);
						// if button is clicked, close the custom dialog
						dialogButton.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								finish();

							}
						});
						dialoga.show();
						Toast.makeText(getApplicationContext(), "Couldn't get json from server. Check LogCat for possible errors!", Toast.LENGTH_LONG).show();
					}
				});
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			// Dismiss the progress dialog
			if (dialog.isShowing())
				dialog.dismiss();
		}
	}
	public void InitView(Document doc)//process to view PDF file
    {
		PDFViewAct.ms_tran_doc = doc;
		Intent intent = new Intent(this, PDFViewAct.class);
		intent.putExtra("Ttshide", hide);
		startActivity(intent);
		System.out.println("BOOLOOOOOee" + "   " + hide);
    }
}
