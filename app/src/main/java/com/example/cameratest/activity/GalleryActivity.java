package com.example.cameratest.activity;

import android.app.Activity;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.bumptech.glide.Glide;
import com.example.cameratest.CameraApp;
import com.example.cameratest.R;
import com.example.cameratest.util.CommonUtils;

import java.io.File;
import java.util.ArrayList;

public class GalleryActivity extends Activity {
    private String TAG = GalleryActivity.class.getSimpleName();
    private int verticalMinDistance = 50;
    private ArrayList<File> imgs;
   // private ImageSwitcher imageSwitcher;
    private int curPos = 0;
    private ImageView curImgView;
    private GestureDetector mGestureDetector;
    private Button viewInfo = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        setViews();

        initData();
    }

    private void setViews() {
        viewInfo = (Button) findViewById(R.id.view_info);
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
        viewInfo.setVisibility(View.GONE);

        viewInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ExifInterface exifInterface = new ExifInterface(
                            imgs.get(curPos).getPath());
                    String FFNumber = exifInterface
                            .getAttribute(ExifInterface.TAG_APERTURE);
                    String FDateTime = exifInterface
                            .getAttribute(ExifInterface.TAG_DATETIME);
                    String FExposureTime = exifInterface
                            .getAttribute(ExifInterface.TAG_EXPOSURE_TIME);
                    String FFlash = exifInterface
                            .getAttribute(ExifInterface.TAG_FLASH);
                    String FFocalLength = exifInterface
                            .getAttribute(ExifInterface.TAG_FOCAL_LENGTH);
                    String FImageLength = exifInterface
                            .getAttribute(ExifInterface.TAG_IMAGE_LENGTH);
                    String FImageWidth = exifInterface
                            .getAttribute(ExifInterface.TAG_IMAGE_WIDTH);
                    String FISOSpeedRatings = exifInterface
                            .getAttribute(ExifInterface.TAG_ISO);
                    String FMake = exifInterface
                            .getAttribute(ExifInterface.TAG_MAKE);
                    String FModel = exifInterface
                            .getAttribute(ExifInterface.TAG_MODEL);
                    String FOrientation = exifInterface
                            .getAttribute(ExifInterface.TAG_ORIENTATION);
                    String FWhiteBalance = exifInterface
                            .getAttribute(ExifInterface.TAG_WHITE_BALANCE);

                    Log.i(TAG, "FFNumber:" + FFNumber);
                    Log.i(TAG, "FDateTime:" + FDateTime);
                    Log.i(TAG, "FExposureTime:" + FExposureTime);
                    Log.i(TAG, "FFlash:" + FFlash);
                    Log.i(TAG, "FFocalLength:" + FFocalLength);
                    Log.i(TAG, "FImageLength:" + FImageLength);
                    Log.i(TAG, "FImageWidth:" + FImageWidth);
                    Log.i(TAG, "FISOSpeedRatings:" + FISOSpeedRatings);
                    Log.i(TAG, "FMake:" + FMake);
                    Log.i(TAG, "FModel:" + FModel);
                    Log.i(TAG, "FOrientation:" + FOrientation);
                    Log.i(TAG, "FWhiteBalance:" + FWhiteBalance);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
    }

    private void initData() {

        imgs = CommonUtils.getImgs(GalleryActivity.this);

        if(imgs == null ||imgs.size()<=0)
        {
            Toast.makeText(GalleryActivity.this,"当前目录下还没有存储照片",Toast.LENGTH_LONG).show();
            return;
        }
        curPos = getIntent().getIntExtra("current_postion",imgs.size()-1);
        //curPos = imgs.length-1;
        Glide.with(this).load(imgs.get(curPos)).into(curImgView);
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
                    Glide.with(GalleryActivity.this).load(imgs.get(curPos)).into(curImgView);
                }
                else
                {
                    Toast.makeText(GalleryActivity.this,"已经是第一张图片了",Toast.LENGTH_SHORT).show();
                }

            } else if (e1.getX() -e2.getX() > verticalMinDistance){

                Log.d("pingan", "向左手势");
                if(curPos < imgs.size()-1)
                {
                    curPos++;
                    Glide.with(GalleryActivity.this).load(imgs.get(curPos)).into(curImgView);
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
