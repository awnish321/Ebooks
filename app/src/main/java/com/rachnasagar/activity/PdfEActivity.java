package com.rachnasagar.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
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
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rachnasagar.Common.AllStaticMethod;
import com.rachnasagar.Common.ConnectionDetector;
import com.rachnasagar.Common.Networking;
import com.rachnasagar.R;
import com.rachnasagar.adapter.VinSetterGetter;
import com.radaee.reader.PDFNavAct;

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

public class PdfEActivity extends AppCompatActivity {

    String Book_Name, Book_Type, Book_Download_Link, Book_Image_Url, Book_Zip_Title;
    boolean deleted_zip, deleted_book;
    ProgressHUD dialoga;
    VinSetterGetter getter;
    ArrayList<VinSetterGetter> vinarrayList;
    String Download_Id, Login_UserID;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    private ProgressDialog mProgressDialog;
    String unzipLocation;
    String zipFile;
    public static String Value;
    File ebook_directory;
    File file_details, file_details1;
    File file_activities;
    String first;
    Context context;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);
        context =PdfEActivity.this;

        Download_Id = getIntent().getExtras().getString("Download_Id");
        first = getIntent().getExtras().getString("key1");
        preferences = getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        Login_UserID = preferences.getString("Login_UserId", "");
        cd = new ConnectionDetector(getApplicationContext());
        progressBar =(ProgressBar) findViewById(R.id.progressBar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ebook_directory = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        } else {
            ebook_directory = Environment.getExternalStorageDirectory();
        }
        isInternetPresent = cd.isConnectingToInternet();

        if (isInternetPresent)
        {
            DownloadBookDetailsURL();
        } else
        {
            AllStaticMethod.showAlertDialog(context, "No Internet Connection", "You don't have internet connection.", false);
        }
        Value = " ";
    }
    public void DownloadBookDetailsURL() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String urlmanual = Networking.url + "myDownloadDetails.php?";
        StringRequest postRequest = new StringRequest(Request.Method.POST, urlmanual,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
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

                                Book_Download_Link = getter.getDownloadLink();
                                Book_Name = getter.getBookName();
                                Book_Zip_Title = getter.getZipTitle();
                                Book_Type = getter.getBookType();

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                    unzipLocation = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) + "/.Rachna/" + Book_Type + "/";
                                    file_details = new File(ebook_directory, "/.Rachna/" + Book_Type + "/" + Book_Zip_Title + ".pdf");
                                    file_activities = new File(ebook_directory, "/.Rachna/" + Book_Type + "/" + "." + Book_Zip_Title);
                                } else {
                                    unzipLocation = Environment.getExternalStorageDirectory() + "/.Rachna/" + Book_Type + "/";
                                    file_details = new File(ebook_directory, "/.Rachna/" + Book_Type + "/" + Book_Zip_Title + ".pdf");
                                    file_activities = new File(ebook_directory, "/.Rachna/" + Book_Type + "/" + "." + Book_Zip_Title);
                                }
                                if (file_details.exists()) {
                                    if (first.contentEquals("value1")) {
                                        file_details1 = new File(ebook_directory, "/.Rachna/eBook");

                                        preferences = getSharedPreferences("BookDownloadDetailE", Context.MODE_PRIVATE);
                                        editor = preferences.edit();
                                        editor.putString(object.getString("Book_ID"),file_details.toString());
                                        editor.commit();

                                        Intent intent = new Intent(context, PDFNavAct.class);
                                        intent.putExtra("To_Open", "Ebook");
                                        intent.putExtra("pdf1", getter.getBookName());
                                        startActivity(intent);
                                        finish();
                                    }
                                } else {
//                                    progressBar.setVisibility(View.GONE);
                                    ebookDownLoadFile();
                                }

                            }
                        } catch (JSONException e) {
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
            } else {
                boolean deleted = file.delete();
                if (!deleted) {
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
            mProgressDialog = new ProgressDialog(context);
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
                if (lenghtOfFile == 0) {
                    new AlertDialog.Builder(context)
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
            mProgressDialog.setProgress(Integer.parseInt(progress[0]));
        }
        @Override
        protected void onPostExecute(String unused) {
            mProgressDialog.dismiss();
            if (result.equalsIgnoreCase("true")) {
                try {
                    unzip();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {

            }
        }
    }
    public void unzip() throws IOException {
        mProgressDialog = new ProgressDialog(context);
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                filev = new File(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) + "/" + Book_Zip_Title + ".zip");
            } else {
                filev = new File(Environment.getExternalStorageDirectory() + "/" + Book_Zip_Title + ".zip");
            }
            deleted_zip = filev.delete();
//            progressBar.setVisibility(View.VISIBLE);
            DownloadBookDetailsURL();
        }
        private void unzipEntry(ZipFile zipfile, ZipEntry entry, String outputDir) throws IOException
        {
            if (entry.isDirectory()) {
                createDir(new File(outputDir, entry.getName()));
                return;
            }
            File outputFile = new File(outputDir, entry.getName());
            if (!outputFile.getParentFile().exists()) {
                createDir(outputFile.getParentFile());
            }
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
                return;
            }
            if (!dir.mkdirs()) {
                throw new RuntimeException("Can not create dir " + dir);
            }
        }
    }

    private void deleteEbookFile(){
        deleted_book = file_details.delete();
        String message = "Please Wait....";
        dialoga = new ProgressHUD(context, com.radaee.viewlib.R.style.AppTheme);
        dialoga.setTitle("");
        dialoga.setContentView(R.layout.progress_hudd);
        dialoga.setCancelable(true);
        dialoga.getWindow().getAttributes().gravity = Gravity.CENTER;
        WindowManager.LayoutParams lp = dialoga.getWindow().getAttributes();
        lp.dimAmount = 0.2f;
        dialoga.getWindow().setAttributes(lp);
        dialoga.show();
    }
    private void ebookDownLoadFile(){
        Value = null;
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            PdfEActivity.DownloadMapAsync mew = new DownloadMapAsync();
            mew.execute(Book_Download_Link);
            Value = Book_Zip_Title + ".zip";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                zipFile = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) + "/" + Book_Zip_Title + ".zip";
            } else {
                zipFile = Environment.getExternalStorageDirectory() + "/" + Book_Zip_Title + ".zip";
            }
        } else {
            new AlertDialog.Builder(context)
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

}