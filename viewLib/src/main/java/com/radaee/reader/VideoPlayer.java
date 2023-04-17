package com.radaee.reader;

import com.radaee.viewlib.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoPlayer extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        Log.d("videoplayes221","play11");

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        getWindow().setDimAmount(0.0f);
        
        VideoView video_player_view = (VideoView) findViewById(R.id.video_player_view);
        video_player_view .setMediaController(new MediaController(this));
        video_player_view .setVideoPath(getIntent().getStringExtra("VIDEOPATH"));
        video_player_view .start();
        Log.d("videoplayes222","play111"+video_player_view);
        
    }
}