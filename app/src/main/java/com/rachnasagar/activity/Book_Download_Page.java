package com.rachnasagar.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rachnasagar.Common.ConnectionDetector;
import com.rachnasagar.Common.Networking;
import com.rachnasagar.R;
import com.rachnasagar.adapter.DownloadTask;
import com.rachnasagar.adapter.VinSetterGetter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class Book_Download_Page extends AppCompatActivity implements View.OnClickListener {

    String User_Book_Id, User_Download_Id, Book_Category, Book_Sub_Category, Book_Subjects, Book_Class, Book_Name, Book_Type, Book_Download_Link, Book_Image_Url, Book_Zip_Title;
    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int STORAGE_PERMISSION_CODE = 101;
    boolean deleted_zip, deleted_book, delete_activity;
    CardView btn_ebook, btn_interbooks;

    public static String Edit_Page = "";
    ProgressHUD dialoga;
    String message = "Please Wait....";
    ProgressDialog progressDialog;
    // GridView gv_subcat;
    VinSetterGetter getter;
    ArrayList<VinSetterGetter> vinarrayList;
    String Cat_Name, Category, Sub_name, Class_Name, Download_Id, Login_UserID, Login_Name, Login_MobileNo, Login_Delivery_Address;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    ImageView checkimage;
    TextView headerTitleText;
    // flag for Internet connection status
    Boolean isInternetPresent = false;
    // Connection detector class
    ConnectionDetector cd;
    ImageView img_Book_Img;
    TextView txt_book_name, txt_Ebook_msg;
    Button btn_Download_Book, btn_Delete_Book;
    private ProgressDialog mProgressDialog;
    //   String unzipLocation = Environment.getExternalStorageDirectory() + "/.RRRachnasagar/wwwwww/";
    String unzipLocation;
    String zipFile;
    public static String Value;
    //   private boolean isCanceled;
    File ebook_directory;
    File file_details, file_details1;
    File file_activities;
    String first;
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.book_download_page);
        getSupportActionBar().setTitle("Download Book");
        cd = new ConnectionDetector(getApplicationContext());
        checkimage = findViewById(R.id.checks);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ebook_directory = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
            Log.d("awa0", "" + ebook_directory);
        } else {
            ebook_directory = Environment.getExternalStorageDirectory();
            Log.d("awa00", "" + ebook_directory);
        }

        Download_Id = getIntent().getExtras().getString("Download_Id");
        first = getIntent().getExtras().getString("key1");
        preferences = getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        Login_UserID = preferences.getString("Login_UserId", "");
        isInternetPresent = cd.isConnectingToInternet();

        if (isInternetPresent)
        {
            DownloadBookDetailsURL();
            String message = "Please Wait....";
            Log.d("alert111", "No");
            dialoga = new ProgressHUD(Book_Download_Page.this, com.radaee.viewlib.R.style.AppTheme);
            dialoga.setTitle("");
            dialoga.setContentView(R.layout.progress_hudd);
            if (message == null || message.length() == 0) {
                dialoga.findViewById(R.id.message).setVisibility(View.GONE);
            } else {
                TextView txt = (TextView) dialoga.findViewById(R.id.message);
                txt.setText(message);
            }
            dialoga.setCancelable(true);
            dialoga.getWindow().getAttributes().gravity = Gravity.CENTER;
            WindowManager.LayoutParams lp = dialoga.getWindow().getAttributes();
            lp.dimAmount = 0.2f;
            dialoga.getWindow().setAttributes(lp);
            dialoga.show();
        } else
        {
            Log.d("alert1111", "No");
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
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    finish();
                }
            });
            dialoga.show();
        }
        Value = " ";
        BookDetailsData();
        btn_ebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                file_details1 = new File(ebook_directory, "/.Rachna/eBook");
                Intent intent = new Intent(Book_Download_Page.this, com.radaee.reader.PDFNavAct.class);
                intent.putExtra("To_Open", "Ebook");
                intent.putExtra("pdf1", getter.getBookName());
                Log.d("key234", getter.getBookName());
                startActivity(intent);
            }
        });
        btn_interbooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                file_details1 = new File(ebook_directory, "/.Rachna/Interactive eBook");
                Intent intent = new Intent(Book_Download_Page.this, com.radaee.reader.PDFNavAct.class);
                intent.putExtra("To_Open", "Interactive_Ebook");
                startActivity(intent);
            }
        });
    }

    private void BookDetailsData() {

        btn_ebook = findViewById(R.id.btn_Ebookss);
        btn_interbooks = findViewById(R.id.btn_Interbookss);
        img_Book_Img = (ImageView) findViewById(R.id.Img_Book_Image);
        txt_book_name = (TextView) findViewById(R.id.Txt_Book_Name);
        txt_Ebook_msg = (TextView) findViewById(R.id.Txt_Ebook_msg);
        btn_Download_Book = (Button) findViewById(R.id.btn_Download_Book);
        btn_Delete_Book = (Button) findViewById(R.id.btn_Delete_Book);
        btn_Download_Book.setOnClickListener(this);
        btn_Delete_Book.setOnClickListener(this);

    }

    public void DownloadBookDetailsURL() {

        RequestQueue queue = Volley.newRequestQueue(this);
        String urlmanual = Networking.url + "myDownloadDetails.php?";
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
                                getter.setBookID(object.getString("Book_ID"));
                                getter.setBookImage_Url(object.getString("Img_Url"));
                                getter.setCategory(object.getString("Category"));
                                getter.setSubCategory(object.getString("Sub_Category"));
                                getter.setSubject(object.getString("Subject"));
                                getter.setDownloadId(object.getString("Download_ID"));
                                getter.setClassName(object.getString("Class"));
                                getter.setBookType(object.getString("Book_Type"));
                                getter.setBookName(object.getString("Book_Name"));
                                getter.setDownloadLink(object.getString("Download_link"));
                                getter.setZipTitle(object.getString("Zip_Title"));
                                vinarrayList.add(getter);

                                new DownloadTask((ImageView) findViewById(R.id.Img_Book_Image))
                                        .execute((String) getter.getBookImage_Url());
                                txt_book_name.setText(getter.getBookName());

                                // headerTitleText.setText(getter.getBookType());

                                User_Book_Id = getter.getBookID();
                                User_Download_Id = getter.getDownloadId();
                                Book_Category = getter.getCategory();
                                Book_Sub_Category = getter.getSubCategory();
                                Book_Subjects = getter.getSubject();
                                Book_Class = getter.getClassName();
                                Book_Download_Link = getter.getDownloadLink();
                                Book_Image_Url = getter.getBookImage_Url();
                                Book_Name = getter.getBookName();
                                Book_Zip_Title = getter.getZipTitle();
                                Book_Type = getter.getBookType();
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                    //zipFile =Environment.getExternalStorageDirectory()+"/"+Book_Zip_Title+".zip"; //getExternalFilesDir(null)
                                    //unzipLocation = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES) +"/.Rachnasagar/"+Book_Type+"/";
                                    unzipLocation = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) + "/.Rachna/" + Book_Type + "/";
                                    file_details = new File(ebook_directory, "/.Rachna/" + Book_Type + "/" + Book_Zip_Title + ".pdf");
                                    file_activities = new File(ebook_directory, "/.Rachna/" + Book_Type + "/" + "." + Book_Zip_Title);
                                    //file_details.mkdir();
                                    Log.d("awa1", "" + unzipLocation);
                                    //unzipFile = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) + "/" + Book_Zip_Title+".zip";
                                } else {
                                    unzipLocation = Environment.getExternalStorageDirectory() + "/.Rachna/" + Book_Type + "/";
                                    //unzipLocation = getExternalFilesDir(null) +"/.Rachnasagar/"+Book_Type+"/";
                                    file_details = new File(ebook_directory, "/.Rachna/" + Book_Type + "/" + Book_Zip_Title + ".pdf");
                                    file_activities = new File(ebook_directory, "/.Rachna/" + Book_Type + "/" + "." + Book_Zip_Title);
                                    //zipFile= Environment.getExternalStorageDirectory() + "/" + Book_Zip_Title+".zip";
                                    Log.d("awa01", "" + ebook_directory);
                                }
                                Log.d("a11", "" + unzipLocation);

                                System.out.println("filedetails" + "     " + file_details + "   " + file_activities);

                                if (file_details.exists()) {
                                    if (first.contentEquals("value1")) {
                                        Log.d("roman1", "wwe Universal");
                                        btn_ebook.setVisibility(View.VISIBLE);
                                        btn_interbooks.setVisibility(View.GONE);
                                    } else {
                                        Log.d("roman2", "wwe Universal");
                                        btn_ebook.setVisibility(View.GONE);
                                        btn_interbooks.setVisibility(View.VISIBLE);
                                    }
                                } else {
                                    Log.d("roman3", "wwe Universal");
                                    btn_ebook.setVisibility(View.GONE);
                                    btn_interbooks.setVisibility(View.GONE);
                                }

                                if (file_details.exists()) {
                                    Log.d("roman4", "wwe Universal");

                                    checkimage.setVisibility(View.VISIBLE);
                                    btn_Download_Book.setVisibility(View.GONE);
                                    btn_Delete_Book.setVisibility(View.VISIBLE);
                                    txt_Ebook_msg.setVisibility(View.VISIBLE);
                                } else {
                                    Log.d("roman5", "wwe Universal");
                                    checkimage.setVisibility(View.GONE);
                                    btn_Download_Book.setVisibility(View.VISIBLE);
                                    btn_Delete_Book.setVisibility(View.GONE);
                                    txt_Ebook_msg.setVisibility(View.GONE);
                                }

                                System.out.println("dewwwww" + "     " + unzipLocation);
                                if (dialoga.isShowing())
                                    dialoga.dismiss();
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
                        try {
                            DownloadBookDetailsURL();
                        } catch (Exception e) {

                        }
                    }
                }
        ) {
            @Override
            protected HashMap<String, String> getParams() {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("userId", Login_UserID);
                params.put("downloadId", Download_Id);// Second one u can change
                return params;
            }
        };
        queue.add(postRequest);

    }

    @Override
    public void onClick(View v) {
        if (v == btn_Download_Book)
        {
            Value = null;
            // get Internet status
            isInternetPresent = cd.isConnectingToInternet();
            // check for Internet status
            if (isInternetPresent) {
                // Internet Connection is Present
                System.out.println("LINKAAAEEE" + "   " + Book_Download_Link);
                DownloadMapAsync mew = new DownloadMapAsync();
                mew.execute(Book_Download_Link);
                System.out.println("downloadlinkkaa" + "    " + Book_Download_Link + " " + Book_Zip_Title);
                Value = Book_Zip_Title + ".zip";
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    //zipFile =Environment.getExternalStorageDirectory()+"/"+Book_Zip_Title+".zip"; //getExternalFilesDir(null)
                    //zipFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/" + Book_Zip_Title+".zip";
                    //zipFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES) + "/" + Book_Zip_Title+".zip";
                    zipFile = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) + "/" + Book_Zip_Title + ".zip";
                    Log.d("awa2", "" + zipFile);
                } else {
                    zipFile = Environment.getExternalStorageDirectory() + "/" + Book_Zip_Title + ".zip";
                }

            } else {
                new AlertDialog.Builder(Book_Download_Page.this)
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
        }

        if (v == btn_Delete_Book) {
            deleted_book = file_details.delete();
            String message = "Please Wait....";
            dialoga = new ProgressHUD(Book_Download_Page.this, com.radaee.viewlib.R.style.AppTheme);
            dialoga.setTitle("");
            dialoga.setContentView(R.layout.progress_hudd);
            if (message == null || message.length() == 0) {
                dialoga.findViewById(R.id.message).setVisibility(View.GONE);
            } else {
                TextView txt = (TextView) dialoga.findViewById(R.id.message);
                txt.setText(message);
            }
            dialoga.setCancelable(true);
            dialoga.getWindow().getAttributes().gravity = Gravity.CENTER;
            WindowManager.LayoutParams lp = dialoga.getWindow().getAttributes();
            lp.dimAmount = 0.2f;
            dialoga.getWindow().setAttributes(lp);
            dialoga.show();

            if (deleted_book == true)
            {
                if (Book_Type.equalsIgnoreCase("eBook") || Book_Type.equalsIgnoreCase("Interactive eBook"))
                {
                    if (dialoga.isShowing())
                        dialoga.dismiss();
                    Toast.makeText(getApplicationContext(), " File Deleted. ", Toast.LENGTH_SHORT).show();

                    Log.d("roman6", "wwe Universal");
                    checkimage.setVisibility(View.GONE);
                    btn_ebook.setVisibility(View.GONE);
                    btn_interbooks.setVisibility(View.GONE);
                    btn_Download_Book.setVisibility(View.VISIBLE);
                    btn_Delete_Book.setVisibility(View.GONE);
                    txt_Ebook_msg.setVisibility(View.GONE);
                } else {
                    try {
                        System.out.println("fileINdele" + "     " + file_activities);
                        deleteDir(file_activities);
                        dialoga.dismiss();
                        Toast.makeText(getApplicationContext(), " File Deleted. ", Toast.LENGTH_SHORT).show();
                        Log.d("roman7", "wwe Universal");
                        checkimage.setVisibility(View.GONE);
                        btn_ebook.setVisibility(View.GONE);
                        checkimage.setVisibility(View.GONE);
                        btn_Download_Book.setVisibility(View.VISIBLE);
                        btn_Delete_Book.setVisibility(View.GONE);
                        txt_Ebook_msg.setVisibility(View.GONE);

                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

            }
        }
    }

    public void deleteDir(File dir) throws IOException {
        if (!dir.isDirectory()) {
            throw new IOException("Not a directory " + dir);
        }
        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++)
        {
            File file = files[i];

            if (file.isDirectory()) {
                deleteDir(file);
                if (dialoga.isShowing())
                    dialoga.dismiss();
                checkimage.setVisibility(View.GONE);
                btn_ebook.setVisibility(View.GONE);
                btn_interbooks.setVisibility(View.GONE);
                btn_Download_Book.setVisibility(View.VISIBLE);
                btn_Delete_Book.setVisibility(View.GONE);
                txt_Ebook_msg.setVisibility(View.GONE);

            } else {
                boolean deleted = file.delete();
                if (!deleted) {
                    Log.d("Book_Download_page", "notdelete");
                    // throw new IOException("Unable to delete file" + file);
                }
            }
        }
        dir.delete();
    }

    class DownloadMapAsync extends AsyncTask<String, String, String> {
        String result = "";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(Book_Download_Page.this);
            mProgressDialog.setMessage("Downloading Zip File..");
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }
        @Override
        protected String doInBackground(String... aurl) {
            int count;

            try {
                URL url = new URL(aurl[0]);
                URLConnection conexion = url.openConnection();
                conexion.connect();
                int lenghtOfFile = conexion.getContentLength();
                InputStream input = new BufferedInputStream(url.openStream());

                OutputStream output = new FileOutputStream(zipFile);

                System.out.println("tgtgtgttg" + " " + lenghtOfFile);

                if (lenghtOfFile == 0) {
		 		/*showAlertDialog(Book_Download_Page.this, "Error In Internet Connection",
						"You don't have proper internet connection.", false);*/

                    new AlertDialog.Builder(Book_Download_Page.this)
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

                byte data[] = new byte[1024];
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));
                    System.out.println("fgfffff" + " " + (int) ((total * 100) / lenghtOfFile));
                    output.write(data, 0, count);
                }
                output.close();
                input.close();
                result = "true";
            } catch (Exception e) {
                result = "false";
            }
            return null;
        }
        protected void onProgressUpdate(String... progress) {
            Log.d("ANDRO_ASYNC", progress[0]);
            mProgressDialog.setProgress(Integer.parseInt(progress[0]));
        }
        @Override
        protected void onPostExecute(String unused) {
            mProgressDialog.dismiss();
            if (result.equalsIgnoreCase("true")) {
                try {
                    unzip();

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {

            }
        }
    }

    public void unzip() throws IOException {
        mProgressDialog = new ProgressDialog(Book_Download_Page.this);
        mProgressDialog.setMessage("Please Wait...Extracting zip file ... ");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        new UnZipTask().execute(zipFile, unzipLocation);
    }

    private class UnZipTask extends AsyncTask<String, Void, Boolean> {
        @SuppressWarnings("rawtypes")
        @Override
        protected Boolean doInBackground(String... params) {
            String filePath = params[0];
            String destinationPath = params[1];

            File archive = new File(filePath);
            try {
                ZipFile zipfile = new ZipFile(archive);
                for (Enumeration e = zipfile.entries(); e.hasMoreElements(); ) {
                    ZipEntry entry = (ZipEntry) e.nextElement();
                    unzipEntry(zipfile, entry, destinationPath);
                }
                System.out.println("saaaaaaa" + "     " + unzipLocation);
                UnzipUtil d = new UnzipUtil(zipFile, unzipLocation);
                d.unzip();
            } catch (Exception e) {
                return false;
            }
            return true;
        }
        @Override
        protected void onPostExecute(Boolean result) {
            mProgressDialog.dismiss();
            File filev;
            // File filev = new File(Environment.getExternalStorageDirectory(), Book_Zip_Title+".zip");
            //Toast.makeText(getApplicationContext(), "Books downloading completed. Please check in Ebook.", Toast.LENGTH_SHORT).show();
            checkimage.setVisibility(View.VISIBLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                filev = new File(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) + "/" + Book_Zip_Title + ".zip");
                // filev = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES) + "/" + Book_Zip_Title+".zip");
                //filev.mkdirs();
                Log.d("awa3", "" + filev);

                // deleted_zip = filev.delete();
            } else {
                filev = new File(Environment.getExternalStorageDirectory() + "/" + Book_Zip_Title + ".zip");
                Log.d("awa03", "" + filev);
                //deleted_zip = filev.delete();
            }
            deleted_zip = filev.delete();
            Log.d("a13", "" + filev);
            System.out.println("dfdfdfdfdddddd" + "   " + filev);
            if (first.contentEquals("value1")) {
                Log.d("roman9", "wwe Universal");
                btn_ebook.setVisibility(View.VISIBLE);
            } else {
                Log.d("roman10", "wwe Universal");
                btn_interbooks.setVisibility(View.VISIBLE);
            }
            Log.d("roman11", "wwe Universal");
            checkimage.setVisibility(View.VISIBLE);
            btn_Download_Book.setVisibility(View.GONE);
            btn_Delete_Book.setVisibility(View.VISIBLE);
            txt_Ebook_msg.setVisibility(View.VISIBLE);
            ///  finish
        }
        private void unzipEntry(ZipFile zipfile, ZipEntry entry, String outputDir) throws IOException {

            if (entry.isDirectory()) {
                createDir(new File(outputDir, entry.getName()));
                return;
            }
            File outputFile = new File(outputDir, entry.getName());
            if (!outputFile.getParentFile().exists()) {
                createDir(outputFile.getParentFile());
            }
            // Log.v("", "Extracting: " + entry);
            BufferedInputStream inputStream = new BufferedInputStream(zipfile.getInputStream(entry));
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(outputFile));

            try {

            } finally {
                outputStream.flush();
                outputStream.close();
                inputStream.close();
            }
        }
        private void createDir(File dir) {
            if (dir.exists()) {
                Log.e("nottt1", "" + dir.exists());
                return;
            }
            if (!dir.mkdirs()) {
                Log.e("nottt2", "" + dir.exists());
                throw new RuntimeException("Can not create dir " + dir);
            }
        }
    }
}

