package com.rachnasagar.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.rachnasagar.R;

import java.io.File;


public class Shoppling_List_Dual_Page extends Activity implements OnClickListener {

	
	ImageView headerTitleImage,header_search;
	TextView headerTitleText;
	String PdfFile ;
	 File ebook_directory;
	 File file_details;
	 Button Btn_Simple_Ebook,Btn_Interactive_Ebook;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.shopping_list_dual_page);

        // headerTitleText = (TextView) findViewById(R.id.header_title);
	        //  headerTitleImage.setVisibility(View.GONE);
             
	         // headerTitleText.setVisibility(View.VISIBLE);
//	     headerTitleText.setText(" My Shopping List ");
		/* header_search=(ImageView)findViewById(R.id.header_search) ;
        header_search.setVisibility(View.VISIBLE);
        header_search.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(Shoppling_List_Dual_Page.this,SearchActivityNew.class));
            }
        });*/
		Buttonsdetails();
		//  ebook_directory = Environment.getExternalStorageDirectory();
	          
	          ebook_directory = Environment.getExternalStorageDirectory().getAbsoluteFile();	
}
	
private void Buttonsdetails() {
		// TODO Auto-generated method stub
		Btn_Simple_Ebook=(Button)findViewById(R.id.btnn_Simple_Ebook);
		Btn_Interactive_Ebook=(Button)findViewById(R.id.btnn_Interactive_Ebook);
		Btn_Simple_Ebook.setOnClickListener(this);
		Btn_Interactive_Ebook.setOnClickListener(this);
		
	}
	@Override
	  public void onBackPressed() {
	          super.onBackPressed();
	          startActivity(new Intent(Shoppling_List_Dual_Page.this, Dashboard.class));
	          this.finish();
	}
	  @Override
		public void onClick(View v) {
		// TODO Auto-generated method stub
		  if(v==Btn_Simple_Ebook) {
			Intent intent = new Intent(Shoppling_List_Dual_Page.this, myDownloadsEBookList.class);
			startActivity(intent);
		}
		if(v==Btn_Interactive_Ebook) {
			Intent intent = new Intent(Shoppling_List_Dual_Page.this,My_Downloads_Intra_EBookList.class);
			startActivity(intent);
		}
	}
}
