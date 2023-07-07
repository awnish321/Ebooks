package com.radaee.reader;

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
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import static android.content.ContentValues.TAG;

@SuppressLint("SdCardPath")
public class PDFNavAct extends Activity implements OnItemClickListener {

	String url = "https://rachnasagar.in/rsplws/ebookUserCheck.php?";
	ProgressDialog dialog;
	int ret;
    Document doc;
    PDFGridItem item;
	private LinearLayout m_layout;
	private PDFGridView m_grid;
	String To_Open,Str_Msg,Str_Status;
	Boolean hide;
    @SuppressLint("InlinedApi")
	private File root;
	private ArrayList<File> fileList = new ArrayList<File>();
	private LinearLayout view;
	String Login_UserID,ZipTitle,Str_getnew;
	String Device_Id,Mob_Id,Mob_Product,Mob_Brand,Mob_Manufacture,Mob_Model;
	SharedPreferences preferences;
	Intent getstring;
	Context context;

	@Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        Global.Init( this );
        To_Open=getIntent().getExtras().getString("To_Open");
		m_layout = (LinearLayout)LayoutInflater.from(this).inflate(R.layout.pdf_nav, null);

		context=PDFNavAct.this;
		m_grid = (PDFGridView)m_layout.findViewById(R.id.pdf_nav);
//		m_grid.setOnLongClickListener(new View.OnLongClickListener() {
//			@Override
//			public boolean onLongClick(View view) {
//				return true;
//			}
//		});

		GetDevicedetails();
		preferences = getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
		Login_UserID = preferences.getString("Login_UserId", "");

		m_grid.setOnItemClickListener(this);
		setContentView(m_layout);
		if(To_Open.equalsIgnoreCase("Ebook"))
		{
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
			{
				m_grid.PDFSetRootPath("/storage/emulated/0/Android/data/com.rachnasagar/files/Documents/.Rachna/eBook");
				//headerTitleText.setText("eBook-Downloads");
			}
			else
			{
				m_grid.PDFSetRootPath("/storage/emulated/0/.Rachna/eBook");
				//headerTitleText.setText("eBook-Downloads");
			}
		}
		else
		{
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
			{
			m_grid.PDFSetRootPath("/storage/emulated/0/Android/data/com.rachnasagar/files/Documents/.Rachna/Interactive eBook");
			}
			else
			{
			m_grid.PDFSetRootPath("/storage/emulated/0/.Rachna/Interactive eBook");
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
    private void onFail(Document doc, String msg) {
    	doc.Close();
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
    private void InputPswd(PDFGridItem item) {
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
			}
		});
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
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		 item = (PDFGridItem)arg1;
		if( item.is_dir() ) {
			m_grid.PDFGotoSubdir(item.get_name());
		}
		else {
		    doc = new Document();
		    ret = item.open_doc(doc, null);
		    ZipTitle= item.get_name();
		    Str_getnew = ZipTitle.substring(ZipTitle.length() - 5);
			 new GetCheckStatus().execute();
		}
	}
	public class GetCheckStatus extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(PDFNavAct.this);
			dialog.setMessage("Please wait...");
			dialog.setCancelable(false);
			dialog.show();
		}
		@Override
		protected Void doInBackground(Void... arg0)
		{
			HttpHandler sh = new HttpHandler();
			String urlNew = url+"userId="+Login_UserID+"&deviceId="+Device_Id+"&zipTitle="+ZipTitle+"&bookType="+To_Open;
			String jsonStr = sh.makeServiceCall(urlNew);
			if (jsonStr != null) {
				try {
					JSONObject jsonObj = new JSONObject(jsonStr);
					Str_Status = String.valueOf(jsonObj.get("status"));
					Str_Msg = String.valueOf(jsonObj.get("msg"));
					if(Str_Status.equalsIgnoreCase("TRUE"))
					{
						if (Str_getnew.equalsIgnoreCase("Hindi")) {
							hide = true;
						} else {
							hide = false;
						}
						switch (ret)
						{
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
								Toast.makeText(PDFNavAct.this, Str_Msg, Toast.LENGTH_LONG).show();
							}
						});
					}
				} catch (final JSONException e) {
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
	public void InitView(Document doc) {
		PDFViewAct.ms_tran_doc = doc;
		Intent intent = new Intent(this, PDFViewAct.class);
		intent.putExtra("Ttshide", hide);
		startActivity(intent);
    }
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}
}
