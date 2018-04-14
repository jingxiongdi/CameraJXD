package com.example.cameratest.activity;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.example.cameratest.R;
import com.example.cameratest.util.CommonUtils;

import java.io.File;

public class PlayActivity extends Activity {
    private String playPath = "";
    private VideoView videoView = null;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        setViews();

        initData();
    }

    private void setViews() {
        videoView = (VideoView) findViewById(R.id.videoView);
        videoView.setMediaController(new MediaController(this));
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                PlayActivity.this.finish();
            }
        });
    }

    private void initData() {
        Bundle b = getIntent().getExtras();
        playPath =  b.getString("play_path","none");
        if(playPath.equals("none")) {
            Toast.makeText(PlayActivity.this,"没有获取到播放路径",Toast.LENGTH_LONG).show();
        }else{
            videoView.setVideoPath(playPath);
            videoView.start();
        }
    }

}
