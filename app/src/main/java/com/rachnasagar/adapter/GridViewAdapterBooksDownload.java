package com.rachnasagar.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.rachnasagar.Common.AllStaticMethod;
import com.rachnasagar.R;
import com.rachnasagar.activity.Book_Download_Page;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class GridViewAdapterBooksDownload extends ArrayAdapter<VinSetterGetter> {

    public static String Value;
    String zipFile;
    String unzipLocation;
    String file_details;
    String file_activities;
    String name;
    File ebook_directory;

    public GridViewAdapterBooksDownload(@NonNull Context context, ArrayList<VinSetterGetter> courseModelArrayList) {
        super(context, 0, courseModelArrayList);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        View grid = convertView;
        if (grid == null) {
            grid = LayoutInflater.from(getContext()).inflate(R.layout.download_bookdetails, parent, false);
        }

        VinSetterGetter vimSetter = getItem(position);

        TextView txt_Book_Title = (TextView) grid.findViewById(R.id.text_book_title);
        TextView txt_Book_Type = (TextView) grid.findViewById(R.id.text_book_type);
        TextView ebook_text_book_type = (TextView) grid.findViewById(R.id.ebook_text_book_type);
        ImageView img_Book_Image = (ImageView) grid.findViewById(R.id.img_Book_Image);
        ImageView img_download = (ImageView) grid.findViewById(R.id.imgDownload);
        ImageView img_success = (ImageView) grid.findViewById(R.id.imgSuccess);
        ImageView img_delete = (ImageView) grid.findViewById(R.id.imgDelete);

        txt_Book_Title.setText(vimSetter.getBookName());
        txt_Book_Type.setText(vimSetter.getBookType());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ebook_directory = getContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
            Log.d("awa0", "" + ebook_directory);
        } else {
            ebook_directory = Environment.getExternalStorageDirectory();
            Log.d("awa00", "" + ebook_directory);
        }

//        unzipLocation = getContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) + "/.Rachna/" + Book_Type + "/";
//        file_details = new File(ebook_directory, "/.Rachna/" + Book_Type + "/" + Book_Zip_Title + ".pdf");
//        file_activities = new File(ebook_directory, "/.Rachna/" + Book_Type + "/" + "." + Book_Zip_Title);

        if (Objects.equals(vimSetter.getProduct_Subscription_Type(), "Paid")) {
            ebook_text_book_type.setText(" ( Paid )");
        }

        new DownloadTask((ImageView) grid.findViewById(R.id.img_Book_Image)).execute((String) vimSetter.getBookImage_Url());
        img_Book_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Book_Download_Page.class);
                intent.putExtra("Download_Id", vimSetter.downloadId);
                intent.putExtra("key1", "value1");
                getContext().startActivity(intent);
            }
        });

//        img_download.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Value = null;
//                Value = "Book_Zip_Title" + ".zip";
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                    zipFile = getContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) + "/" + "Book_Zip_Title" + ".zip";
//                    unzipLocation = getContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) + "/.Rachna/" + "Book_Type" + "/";
//                    Toast.makeText(getContext(), zipFile, Toast.LENGTH_SHORT).show();
//                    AllStaticMethod.downloadZip(getContext(),"zip","zipt",zipFile,unzipLocation);
//
//                } else {
//                    zipFile = Environment.getExternalStorageDirectory() + "/" + "Book_Zip_Title" + ".zip";
//                    unzipLocation = getContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) + "/.Rachna/" + "Book_Type" + "/";
//                    Toast.makeText(getContext(), zipFile, Toast.LENGTH_SHORT).show();
//                    AllStaticMethod.downloadZip(getContext(),"zip","zipt",zipFile,unzipLocation);
//                }
//            }
//        });

        return grid;
    }
}
