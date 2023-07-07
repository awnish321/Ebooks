package com.rachnasagar.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.rachnasagar.R;
import com.rachnasagar.activity.PdfEActivity;
import com.rachnasagar.activity.PdfIActivity;

import java.io.File;
import java.util.ArrayList;


public class GridViewAdapterIBooksDownload extends ArrayAdapter<VinSetterGetter> {
    private final Context context;
    private final ArrayList<VinSetterGetter> iEbookList;
    File file_details;

    public GridViewAdapterIBooksDownload(@NonNull Context context, ArrayList<VinSetterGetter> iEbookList1) {
        super(context, 0, iEbookList1);
        this.context = context;
        this.iEbookList = iEbookList1;
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

        String id=vimSetter.getBookID();

        Glide.with(context)
                .load(Uri.parse(vimSetter.getBookImage_Url()))
                .into(img_Book_Image);

        SharedPreferences preferences = context.getSharedPreferences("BookDownloadDetailI", Context.MODE_PRIVATE);
        String Book_ID = preferences.getString(id, "");
        if (!Book_ID.isEmpty()){
            img_download.setVisibility(View.GONE);
            img_delete.setVisibility(View.VISIBLE);
            img_success.setVisibility(View.VISIBLE);
        }else {
            img_download.setVisibility(View.VISIBLE);
            img_delete.setVisibility(View.GONE);
            img_success.setVisibility(View.GONE);
        }
        img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                file_details = new File(Book_ID);
                file_details.delete();
                SharedPreferences preferences = context.getSharedPreferences("BookDownloadDetailI", Context.MODE_PRIVATE);
                preferences.edit().remove(id).apply();
                img_success.setVisibility(View.GONE);
                img_delete.setVisibility(View.GONE);
                img_download.setVisibility(View.VISIBLE);
            }
        });
        img_Book_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PdfIActivity.class);
                intent.putExtra("Download_Id", vimSetter.downloadId);
                intent.putExtra("key1", "value1");
                getContext().startActivity(intent);
            }
        });
        img_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PdfIActivity.class);
                intent.putExtra("Download_Id", vimSetter.downloadId);
                intent.putExtra("key1", "value1");
                getContext().startActivity(intent);
            }
        });

        return grid;
    }

    @Override
    public int getCount() {
        return iEbookList.size();
    }
}
