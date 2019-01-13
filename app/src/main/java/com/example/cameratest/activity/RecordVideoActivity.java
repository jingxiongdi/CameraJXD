package com.example.cameratest.activity;

import com.example.cameratest.CameraApp;
import com.example.cameratest.R;
import com.example.cameratest.uistyle.RoundImageView;
import com.example.cameratest.util.CommonUtils;
import com.example.cameratest.util.Contants;
import com.orhanobut.hawk.Hawk;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.io.File;
import java.util.Date;
import java.util.List;

public class RecordVideoActivity extends Activity implements SurfaceHolder.Callback{
    private SurfaceView mSurfaceView = null;
    private SurfaceHolder mSurfaceHolder = null;
    private TextView mTime = null;
    private Button mControlBtn = null;
    private Camera mCamera = null;
    private boolean mStartedFlg = false;
    private MediaRecorder mRecorder = null;
    private Handler mHandler = new Handler();
    private int mRecordTime = 0;
    private ToggleButton swichCameraBtn = null;
    private ToggleButton flashBtn = null;
    private boolean openFlashLight = false;
    private RoundImageView previewImageView = null;

    private Runnable mTimeRun = new Runnable() {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mRecordTime++;
                    mTime.setVisibility(View.VISIBLE);
                    int minute = mRecordTime / 60;
                    int second = mRecordTime % 60;
                    String m = "";
                    String s = "";
                    if (minute < 10) {
                        m = "0" + minute;
                    } else {
                        m = minute + "";
                    }

                    if (second < 10) {
                        s = "0" + second;
                    }
                    else {
                        s = second+"";
                    }

                    mTime.setText(m+":"+s);
                }
            });
            mHandler.postDelayed(this,1000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_video);

        setView();

        initData();
    }

    private void initData() {


    }

    /**
     * 2.1竖屏情况下：
     如果是前置摄像头：
     mediaRecorder.setOrientationHint(270);
     如果是后置摄像头：
     mediaRecorder.setOrientationHint(90);
     2.2横情况下：
     如果是前置摄像头：
     mediaRecorder.setOrientationHint(180);
     如果是后置摄像头：
     mediaRecorder.setOrientationHint(0);
     */
    private void initCamera() {
        try {
            mCamera = Camera.open(CameraApp.getInstance().getmCameraDirection());//默认开启后置
        } catch (RuntimeException e) {
            if ("Fail to connect to camera service".equals(e.getMessage())) {
                //提示无法打开相机，请检查是否已经开启权限
            } else if ("Camera initialization failed".equals(e.getMessage())) {
                //提示相机初始化失败，无法打开
            } else {
                //提示相机发生未知错误，无法打开
            }
            e.printStackTrace();
            return;
        }
        CameraApp.getInstance().setCameraDisplayOrientation(RecordVideoActivity.this,CameraApp.getInstance().getmCameraDirection(),mCamera);
        try {
            Camera.Parameters myParameters = mCamera.getParameters();
            List<Camera.Size> sizeList = myParameters.getSupportedPreviewSizes();//获取所有支持的camera尺寸
            Log.d("jxd", "optionSize : mSurfaceView " + mSurfaceView.getWidth() + " * " + mSurfaceView.getHeight());
            Camera.Size optionSize = getOptimalPreviewSize(sizeList, mSurfaceView.getHeight(), mSurfaceView.getWidth());//获取一个最为适配的camera.size
            Log.d("jxd", "optionSize : " + optionSize.width + " * " + optionSize.height);
            myParameters.setPreviewSize(optionSize.width, optionSize.height);//把camera.size赋值到parameters

       //     myParameters.setPreviewSize(1920, 1080);
            myParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
//            if(CameraApp.getInstance().getmCameraDirection() == 0)
//            {
//                if(!Hawk.contains(Contants.VIDEO_SIZE_BACK))
//                {
//                    List<Camera.Size> supportedVideoSize =  myParameters.getSupportedVideoSizes();
//                    for(int i = 0; i<supportedVideoSize.size();i++)
//                    {
//                        Log.d("jxd","supportedVideoSize : "+supportedVideoSize.get(i).width +" * "+supportedVideoSize.get(i).height);
//                    }
//                    Hawk.put(Contants.VIDEO_SIZE_BACK, supportedVideoSize);
//                }
//            }
//            else
//            {
//                if(!Hawk.contains(Contants.VIDEO_SIZE_FRONT))
//                {
//                    List<Camera.Size> supportedVideoSize =  myParameters.getSupportedVideoSizes();
//                    for(int i = 0; i<supportedVideoSize.size();i++)
//                    {
//                        Log.d("jxd","supportedVideoSize : "+supportedVideoSize.get(i).width +" * "+supportedVideoSize.get(i).height);
//                    }
//                    Hawk.put(Contants.VIDEO_SIZE_FRONT, supportedVideoSize);
//                }
//            }
            if (openFlashLight) {
                myParameters.setFlashMode(myParameters.FLASH_MODE_ON);
            } else
            {
                myParameters.setFlashMode(myParameters.FLASH_MODE_OFF);
            }
            mCamera.setParameters(myParameters);
            mCamera.setPreviewDisplay(mSurfaceHolder);
            mCamera.startPreview();

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    private void setView() {
        flashBtn = (ToggleButton) findViewById(R.id.flash_light);
        flashBtn.setOnCheckedChangeListener(flashLightListenr);
        swichCameraBtn = (ToggleButton) findViewById(R.id.toggle);
        swichCameraBtn.setOnCheckedChangeListener(mtoggle);

        mSurfaceView = (SurfaceView) findViewById(R.id.surface_view);
        mControlBtn = (Button) findViewById(R.id.start_photo);
        mTime = (TextView) findViewById(R.id.time);

        previewImageView = (RoundImageView) findViewById(R.id.video_preview);
        Bitmap bitmap = CommonUtils.getVideoBitmap(RecordVideoActivity.this);
        if(bitmap!=null){
            previewImageView.setImageBitmap(bitmap);
        }
        else{
            previewImageView.setBackgroundResource(R.mipmap.ic_launcher);
        }
        previewImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecordVideoActivity.this,VideoPreviewActivity.class);
                startActivity(intent);
                finish();
            }
        });

        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(this);
        mSurfaceHolder.setKeepScreenOn(true);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        mControlBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mStartedFlg) {
                    // Start
                    mRecordTime = 0;
                    mHandler.post(mTimeRun);
                    mStartedFlg = true;
                    mControlBtn.setText("停止录像");
                    if (mRecorder == null) {
                        mRecorder = new MediaRecorder(); // Create MediaRecorder
                    }
                    try {
                        Camera.Size size = null;
                        if(CameraApp.getInstance().getmCameraDirection() == 0)
                        {
                            if(Hawk.contains(Contants.CURRENT_VIDEO_SIZE_BACK))
                            {
                                int p  = Hawk.get(Contants.CURRENT_VIDEO_SIZE_BACK);
                                size = mCamera.getParameters().getSupportedVideoSizes().get(p);
                                Log.d("jxd","jpeg CURRENT_PHOTO_SIZE Back : "+size.width+" * "+size.height);
                            }
                        }
                        else
                        {
                            if(Hawk.contains(Contants.CURRENT_VIDEO_SIZE_FRONT))
                            {
                                int p  = Hawk.get(Contants.CURRENT_VIDEO_SIZE_FRONT);
                                size = mCamera.getParameters().getSupportedVideoSizes().get(p);
                                Log.d("jxd","jpeg CURRENT_PHOTO_SIZE front : "+size.width+" * "+size.height);
                            }
                        }
                        mCamera.unlock();
//                        Camera.Parameters myParameters = mCamera.getParameters();
//                        List<Camera.Size> sizeList = myParameters.getSupportedPreviewSizes();//获取所有支持的camera尺寸
//                        Log.d("jxd", "optionSize : mSurfaceView " + mSurfaceView.getWidth() + " * " + mSurfaceView.getHeight());
//                        Camera.Size optionSize = getOptimalPreviewSize(sizeList, mSurfaceView.getHeight(), mSurfaceView.getWidth());//获取一个最为适配的camera.size
//                        Log.d("jxd", "optionSize : " + optionSize.width + " * " + optionSize.height);
//                        myParameters.setPreviewSize(optionSize.width, optionSize.height);//把camera.size赋值到parameters
//                        if (openFlashLight) {
//                            myParameters.setFlashMode(myParameters.FLASH_MODE_ON);
//                        } else
//                        {
//                            myParameters.setFlashMode(myParameters.FLASH_MODE_OFF);
//                        }
//                        mCamera.setParameters(myParameters);
                        mRecorder.setCamera(mCamera);
                        // Set audio and video source and encoder

                        // 判断Android当前的屏幕是横屏还是竖屏。横竖屏判断
                        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                            //竖屏
                            if(CameraApp.getInstance().getmCameraDirection() == 0)
                            {
                                mRecorder.setOrientationHint(90);
                            }
                            else
                            {
                                mRecorder.setOrientationHint(270);
                            }
                        } else {
                            //横屏
                        }

                        // 这两项需要放在setOutputFormat之前
                        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                        mRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
 //                       mRecorder.setVideoSize(size.width, size.height);
 //                       mRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);

//                        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);// 视频输出格式
//                        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);// 音频格式
//                        mRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);// 视频录制格式

                        mRecorder.setVideoEncodingBitRate(1024*1024);
                        if(CameraApp.getInstance().getmCameraDirection() == 0 && Hawk.contains(Contants.CURRENT_VIDEO_SIZE_BACK))
                        {
                            Log.d("jxd","backrecord size : "+Hawk.get(Contants.CURRENT_VIDEO_SIZE_BACK));
                            mRecorder.setProfile(CamcorderProfile.get((Integer) Hawk.get(Contants.CURRENT_VIDEO_SIZE_BACK)));
                        }
                        else if(CameraApp.getInstance().getmCameraDirection() == 1 && Hawk.contains(Contants.CURRENT_VIDEO_SIZE_FRONT))
                        {
                            Log.d("jxd","frontrecord size : "+Hawk.get(Contants.CURRENT_VIDEO_SIZE_FRONT));
                            mRecorder.setProfile(CamcorderProfile.get((Integer) Hawk.get(Contants.CURRENT_VIDEO_SIZE_FRONT)));
                        }
                        else
                        {
                            mRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_720P));
                        }

                        mRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());

                        // Set output file path
                        String path = CameraApp.getAllSdPaths(RecordVideoActivity.this)[0];
                        if (path != null) {

                            File dir = new File(path + "/CameraJXD/Video");
                            if (!dir.exists()) {
                                dir.mkdirs();
                            }
                            path = dir + "/" + DateFormat.format("yyyyMMddhhmmss", new Date()).toString() + ".mp4";
                            mRecorder.setOutputFile(path);
                            Log.d("jxd", "bf mRecorder.prepare()");
                            mRecorder.prepare();
                            Log.d("jxd", "af mRecorder.prepare()");
                            Log.d("jxd", "bf mRecorder.start()");
                            mRecorder.start();   // Recording is now started
                            Log.d("jxd", "af mRecorder.start()");

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    // stop
                    if (mStartedFlg) {
                        mStartedFlg = false;
                        mHandler.removeCallbacks(mTimeRun);
                        mRecordTime = 0;
                        mTime.setVisibility(View.GONE);
                        mControlBtn.setText("开始录像");
//                        previewImageView.setBackgroundResource(0);
//                        previewImageView.setImageBitmap(CommonUtils.getVideoBitmap(RecordVideoActivity.this));
                        try {
                            mRecorder.stop();
                            mRecorder.reset();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }
            }
        });
    }

    private ToggleButton.OnCheckedChangeListener mtoggle = new ToggleButton.OnCheckedChangeListener(){

        @Override
        public void onCheckedChanged(CompoundButton buttonView,
                                     boolean isChecked) {
            // TODO Auto-generated method stub
            Log.d("jxd","isChecked "+isChecked);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;

            if(isChecked){
                //test.setOrientation(1);
                CameraApp.getInstance().setmCameraDirection(0);

            }else{
                //test.setOrientation(0);
                CameraApp.getInstance().setmCameraDirection(1);
            }
            initCamera();
        }

    };

    private ToggleButton.OnCheckedChangeListener flashLightListenr = new ToggleButton.OnCheckedChangeListener(){

        @Override
        public void onCheckedChanged(CompoundButton buttonView,
                                     boolean isChecked) {
            // TODO Auto-generated method stub
            Log.d("jxd","isChecked "+isChecked);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;

            if(isChecked){
                //test.setOrientation(1);
                openFlashLight = false;

            }else{
                //test.setOrientation(0);
                openFlashLight = true;
            }
            initCamera();
        }

    };

    /**
     * 解决预览变形问题
     * @param sizes
     * @param w
     * @param h
     * @return
     */
    private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) w / h;
        if (sizes == null) return null;

        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;

        // Try to find an size match aspect ratio and size
        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        // Cannot find the one match the aspect ratio, ignore the requirement
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mSurfaceHolder = holder;
        initCamera();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mSurfaceView = null;
        mSurfaceHolder = null;
        if (mRecorder != null) {
            mRecorder.release();
            mRecorder = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mRecorder != null) {
            mRecorder.release();
            mRecorder = null;
        }
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
