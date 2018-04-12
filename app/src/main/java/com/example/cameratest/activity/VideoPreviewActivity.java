package com.example.cameratest.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.cameratest.R;
import com.example.cameratest.util.CommonUtils;

import java.io.File;

public class VideoPreviewActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        setViews();

        initData();
    }

    private void setViews() {

    }

    private void initData() {
    }


}
