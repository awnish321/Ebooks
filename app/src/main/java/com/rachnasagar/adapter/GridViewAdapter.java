package com.rachnasagar.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rachnasagar.R;

import java.util.List;

public class GridViewAdapter extends BaseAdapter {
	
	//Context
    private Context context;
    // private ArrayList<String> names;
   	List<VinSetterGetter> setterGetters;
	VinSetterGetter vimSetter;
	boolean array[];

    
    public GridViewAdapter(Context context , List<VinSetterGetter> getters){
        //Getting all the values
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
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		 View grid;
         LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
         
         if (convertView == null)
		 {
             grid = new View(context);
             grid = inflater.inflate(R.layout.gridview, null);
             TextView textView = (TextView) grid.findViewById(R.id.textView1);
			 vimSetter = new VinSetterGetter();
			 vimSetter = setterGetters.get(position);
             textView.setText(vimSetter.getCategoryName());
           //  imageView.setImageResource(Imageid[position]);
         }
		 else {
             grid = (View) convertView;
         }
         return grid;
    }

}
