package com.rachnasagar.Common;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.rachnasagar.R;
import com.rachnasagar.activity.Book_Download_Page;
import com.rachnasagar.activity.UnzipUtil;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class AllStaticMethod {
    private static ProgressDialog mProgressDialog;

    public static void downloadZip(Context context, String Book_Zip_Title, String Book_Download_Link, String zipFile, String unzipLocation){

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

//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                    zipFile = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) + "/" + Book_Zip_Title + ".zip";
//                } else {
//                    zipFile = Environment.getExternalStorageDirectory() + "/" + Book_Zip_Title + ".zip";
//                }

                try {
                    URL url = new URL(aurl[0]);
                    URLConnection conexion = url.openConnection();
                    conexion.connect();
                    int lenghtOfFile = conexion.getContentLength();
                    InputStream input = new BufferedInputStream(url.openStream());
                    OutputStream output = new FileOutputStream(zipFile);
                    if (lenghtOfFile == 0) {
                        Toast.makeText(context, "You don't have internet connection", Toast.LENGTH_SHORT).show();
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
                Log.d("ANDRO_ASYNC", progress[0]);
                mProgressDialog.setProgress(Integer.parseInt(progress[0]));
            }
            @Override
            protected void onPostExecute(String unused) {
                mProgressDialog.dismiss();
                if (result.equalsIgnoreCase("true")) {
                    mProgressDialog = new ProgressDialog(context);
                    mProgressDialog.setMessage("Please Wait...Extracting zip file ... ");
                    mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    mProgressDialog.setCancelable(false);
                    mProgressDialog.show();

                }
            }
        }

    }

}
