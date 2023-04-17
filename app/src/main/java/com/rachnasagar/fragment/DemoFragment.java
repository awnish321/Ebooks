package com.rachnasagar.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.rachnasagar.R;
import com.rachnasagar.activity.Webviews;
import com.rachnasagar.activity.Webviews1;


public class DemoFragment extends Fragment {

    LottieAnimationView lottieAnimationView1,lottieAnimationView2;
    View view;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_demo, container, false);

        TextView textView = (TextView) view.findViewById(R.id.textView123);


        //Typeface typeface = getResources().getFont(R.font.pptt);
        //textView.setTypeface(typeface);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        lottieAnimationView1=view.findViewById(R.id.webanimationView);
        lottieAnimationView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getActivity(), Webviews1.class);
                startActivity(i);
            }
        });

        lottieAnimationView2=view.findViewById(R.id.webanimationView2);
        lottieAnimationView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getActivity(), Webviews1.class);
                startActivity(i);
            }
        });
        return view;
    }

}


