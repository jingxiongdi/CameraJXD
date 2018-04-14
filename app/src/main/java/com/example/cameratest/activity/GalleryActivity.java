package com.example.cameratest.activity;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.bumptech.glide.Glide;
import com.example.cameratest.CameraApp;
import com.example.cameratest.R;
import com.example.cameratest.util.CommonUtils;

import java.io.File;

public class GalleryActivity extends Activity {
    private int verticalMinDistance = 50;
    private  File[] imgs;
   // private ImageSwitcher imageSwitcher;
    private int curPos = 0;
    private ImageView curImgView;
    private GestureDetector mGestureDetector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        setViews();

        initData();
    }

    private void setViews() {
       // imageSwitcher = (ImageSwitcher) findViewById(R.id.cur_img);
        curImgView = (ImageView) findViewById(R.id.cur_img);
        mGestureDetector = new GestureDetector(GalleryActivity.this,new MyGestureListener());
        curImgView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d("pingan", "onTouch");
                return mGestureDetector.onTouchEvent(event);
            }
        });
    }

    private void initData() {

        imgs = CommonUtils.getImgs(GalleryActivity.this);

        if(imgs == null ||imgs.length<=0)
        {
            Toast.makeText(GalleryActivity.this,"当前目录下还没有存储照片",Toast.LENGTH_LONG).show();
            return;
        }
        curPos = getIntent().getIntExtra("current_postion",imgs.length-1);
        //curPos = imgs.length-1;
        Glide.with(this).load(imgs[curPos]).into(curImgView);
//        imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {//设置转化工厂
//
//            @Override
//            public View makeView() {
//                ImageView imageView=new ImageView(GalleryActivity.this);
//                imageView.setBackgroundColor(0xFFFFFFFF);
//                imageView.setScaleType(ImageView.ScaleType.CENTER);//居中显示
//                imageView.setLayoutParams(new ImageSwitcher.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));//定义组件
//                return imageView;
//            }
//        });
//        imageSwitcher.setImageURI(Uri.fromFile(imgs[0]));//初始化时显示，必须放在工厂后面，否则会报NullPointerException
//        imageSwitcher.setInAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));//设置动画
//        imageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_out));//设置动画
    }



    class MyGestureListener implements GestureDetector.OnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
           // Log.d("pingan", "onDown");
            /**
            *如果手势监听不起作用，这里要定义为true
             */
            return true;
        }

        @Override
        public void onShowPress(MotionEvent e) {
           // Log.d("pingan", "onShowPress");
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
           // Log.d("pingan", "onSingleTapUp");
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
           // Log.d("pingan", "onScroll");
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
           // Log.d("pingan", "onLongPress");
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            if (e2.getX() -e1.getX() >verticalMinDistance) {
                Log.d("pingan", "向右手势");
                if(curPos > 0)
                {
                    curPos--;
                    Glide.with(GalleryActivity.this).load(imgs[curPos]).into(curImgView);
                }
                else
                {
                    Toast.makeText(GalleryActivity.this,"已经是第一张图片了",Toast.LENGTH_SHORT).show();
                }

            } else if (e1.getX() -e2.getX() > verticalMinDistance){

                Log.d("pingan", "向左手势");
                if(curPos < imgs.length-1)
                {
                    curPos++;
                    Glide.with(GalleryActivity.this).load(imgs[curPos]).into(curImgView);
                }
                else
                {
                    Toast.makeText(GalleryActivity.this,"已经是最后一张图片了",Toast.LENGTH_SHORT).show();
                }

            }
//            else if (distanceY <- verticalMinDistance) {
//                Log.d("pingan", "向下手势");
//
//
//            } else if (distanceY > verticalMinDistance ) {
//
//                Log.d("pingan", "向上手势");
//
//            }
            return false;
        }
    }


}
