package com.rachnasagar.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rachnasagar.R;

import java.util.List;

public class GridViewAdapterBooksDownload extends BaseAdapter {

	//Context
    private Context context;
    List<VinSetterGetter> setterGetters;
	VinSetterGetter vimSetter;
	boolean array[];

	public GridViewAdapterBooksDownload(Context context, List<VinSetterGetter> getters) {
		this.context = context;
		this.setterGetters = getters;
		array = new boolean[getters.size()];
	}
	@Override
	  public int getViewTypeCount() {

	   return getCount();
	  }
	  @Override
	  public int getItemViewType(int position) {

	   return position;
	  }
	  @Override
	public int getCount() {
		// TODO Auto-generated method stub
		return setterGetters.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return setterGetters.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@SuppressLint("NewApi") @Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		 View grid;
         LayoutInflater inflater = (LayoutInflater) context
             .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
         
         vimSetter = new VinSetterGetter();
			vimSetter = setterGetters.get(position);
			
			if (convertView == null) {
				grid = new View(context);
             	grid = inflater.inflate(R.layout.download_bookdetails, null);
             	TextView txt_Book_Title = (TextView) grid.findViewById(R.id.text_book_title);
             	TextView txt_Book_Type = (TextView) grid.findViewById(R.id.text_book_type);
             	txt_Book_Title.setText(vimSetter.getBookName());
             	txt_Book_Type.setText(vimSetter.getBookType());
             	new DownloadTask((ImageView) grid.findViewById(R.id.img_Book_Image))
             .execute((String) vimSetter.getBookImage_Url());
			} else {
             grid = (View) convertView;
         }
			return grid;
	}

}
