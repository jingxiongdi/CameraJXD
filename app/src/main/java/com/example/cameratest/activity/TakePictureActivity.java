package com.example.cameratest.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.example.cameratest.CameraApp;
import com.example.cameratest.R;
import com.example.cameratest.uistyle.RoundImageView;
import com.example.cameratest.util.CommonUtils;
import com.example.cameratest.util.Contants;
import com.orhanobut.hawk.Hawk;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TakePictureActivity extends Activity implements SurfaceHolder.Callback{
    private SurfaceView mSurfaceView = null;
    private SurfaceHolder mHolder = null;
    private Camera mCamera = null;
    private int viewWidth = 720;
    private int viewHeight = 1080;
    private ImageButton mTakePhoto = null;
    private RoundImageView roundImageView = null;
    private ToggleButton swichCameraBtn = null;
    private ToggleButton flashBtn = null;
    private boolean openFlashLight = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_picture);

        setViews();

        initData();
    }

    private void initData() {

        mHolder = mSurfaceView.getHolder();
        mHolder.addCallback(this);
        mHolder.setKeepScreenOn(true);
    }

    private void setViews() {
        //隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  WindowManager.LayoutParams.FLAG_FULLSCREEN);
        roundImageView = (RoundImageView) findViewById(R.id.round_img);
        ArrayList<File> fileList = CommonUtils.getImgs(TakePictureActivity.this);
        if(fileList!=null &&fileList.size() > 1){
            Glide.with(TakePictureActivity.this).load(fileList.get(0)).into(roundImageView);
        }
        flashBtn = (ToggleButton) findViewById(R.id.flash_light);
        flashBtn.setOnCheckedChangeListener(flashLightListenr);
        swichCameraBtn = (ToggleButton) findViewById(R.id.toggle);
        swichCameraBtn.setOnCheckedChangeListener(mtoggle);
     //   roundImageView.setBackgroundColor(R.color.colorPrimary);
        roundImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TakePictureActivity.this,PhotoPreviewActivity.class);
                startActivity(i);
            }
        });
        mTakePhoto = (ImageButton) findViewById(R.id.start_photo);
        mTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });
        mSurfaceView = (SurfaceView) findViewById(R.id.surface_view);
        mSurfaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("jxd", "onClick onAutoFocus success");
                if (mCamera != null) {
                    mCamera.autoFocus(new Camera.AutoFocusCallback() {
                        @Override
                        public void onAutoFocus(boolean success, Camera camera) {
                            if (success) {
                                Log.d("jxd", "setOnClickListener onAutoFocus success");
                            }
                        }
                    });
                }
            }
        });
        mSurfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
               // Log.d("jxd", "onTouch onAutoFocus success");
                if (event.equals(MotionEvent.ACTION_DOWN)) {
                    if (mCamera != null) {
                        mCamera.autoFocus(new Camera.AutoFocusCallback() {
                            @Override
                            public void onAutoFocus(boolean success, Camera camera) {
                                if (success) {
                                   // Log.d("jxd", "setOnTouchListener onAutoFocus success");
                                }
                            }
                        });
                    }
                }
                return false;
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
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
     * SurfaceHolder 回调接口方法
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
        //mCamera.setDisplayOrientation(90);//摄像头进行旋转90°
        CameraApp.getInstance().setCameraDisplayOrientation(TakePictureActivity.this, 0, mCamera);
        if (mCamera != null) {
            try {
                Camera.Parameters parameters = mCamera.getParameters();

                List<Camera.Size> supportedPreviewSizes = parameters.getSupportedPreviewSizes();
                for (int i = 0; i < supportedPreviewSizes.size(); i++) {
                    Log.d("jxd", "supportedPreviewSizes : " + supportedPreviewSizes.get(i).width + " * " + supportedPreviewSizes.get(i).height);
                }

//                if(CameraApp.getInstance().getmCameraDirection() == 0)
//                {
//                    if(!Hawk.contains(Contants.VIDEO_SIZE_BACK))
//                    {
//                        List<Camera.Size> supportedVideoSize =  parameters.getSupportedVideoSizes();
//                        for(int i = 0; i<supportedVideoSize.size();i++)
//                        {
//                            Log.d("jxd","supportedVideoSize : "+supportedVideoSize.get(i).width +" * "+supportedVideoSize.get(i).height);
//                        }
//                        Hawk.put(Contants.VIDEO_SIZE_BACK,supportedVideoSize);
//                    }
//                }
//                else
//                {
//                    if(!Hawk.contains(Contants.VIDEO_SIZE_FRONT))
//                    {
//                        List<Camera.Size> supportedVideoSize =  parameters.getSupportedVideoSizes();
//                        for(int i = 0; i<supportedVideoSize.size();i++)
//                        {
//                            Log.d("jxd","supportedVideoSize : "+supportedVideoSize.get(i).width +" * "+supportedVideoSize.get(i).height);
//                        }
//                        Hawk.put(Contants.VIDEO_SIZE_FRONT,supportedVideoSize);
//                    }
//                }


//                List<Camera.Size> supportedJpegThumbnailSizes = parameters.getSupportedJpegThumbnailSizes();
//                for(int i = 0; i<supportedJpegThumbnailSizes.size();i++)
//                {
//                    Log.d("jxd","supportedJpegThumbnailSizes : "+supportedJpegThumbnailSizes.get(i).width +" * "+supportedJpegThumbnailSizes.get(i).height);
//                }
                if (CameraApp.getInstance().getmCameraDirection() == 0) {
                    if (!Hawk.contains(Contants.PHOTO_SIZE_BACK)) {
                        List<Camera.Size> supportedPictureSizes = parameters.getSupportedPictureSizes();
                        for (int i = 0; i < supportedPictureSizes.size(); i++) {
                            Log.d("jxd", "supportedPictureSizes : " + supportedPictureSizes.get(i).width + " * " + supportedPictureSizes.get(i).height);
                        }
                        Hawk.put(Contants.PHOTO_SIZE_BACK, supportedPictureSizes);
                    }
                } else {
                    if (!Hawk.contains(Contants.PHOTO_SIZE_FRONT)) {
                        List<Camera.Size> supportedPictureSizes = parameters.getSupportedPictureSizes();
                        for (int i = 0; i < supportedPictureSizes.size(); i++) {
                            Log.d("jxd", "supportedPictureSizes : " + supportedPictureSizes.get(i).width + " * " + supportedPictureSizes.get(i).height);
                        }
                        Hawk.put(Contants.PHOTO_SIZE_FRONT, supportedPictureSizes);
                    }
                }


//                List<int[]> supportedPreviewFpsRange = parameters.getSupportedPreviewFpsRange();
//                for(int i = 0; i<supportedPreviewFpsRange.size();i++)
//                {
//                    Log.d("jxd","supportedPreviewFpsRange : =============== "+supportedPreviewFpsRange.get(i).length);
//                    for(int k = 0;k<supportedPreviewFpsRange.get(i).length;k++)
//                    {
//                        Log.d("jxd","supportedPreviewFpsRange : "+supportedPreviewFpsRange.get(i)[k]);
//                    }
//                }
                //设置预览照片的大小
                //        parameters.setPreviewFpsRange(viewWidth, viewHeight);
                //设置相机预览照片帧数
                //      parameters.setPreviewFpsRange(4, 10);
                //设置图片格式
                parameters.setPictureFormat(ImageFormat.JPEG);
                //设置图片的质量
                int pos = 100;
                if (Hawk.contains(Contants.CURRENT_PHOTO_QUALITY)) {
                    String p = Hawk.get(Contants.CURRENT_PHOTO_QUALITY);

                    try {
                        pos = Integer.parseInt(p);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Log.d("jxd", "jpeg CURRENT_PHOTO_QUALITY : " + pos);
                }
                parameters.set("jpeg-quality", pos);
                //设置照片的大小
                Camera.Size size = null;
                if (CameraApp.getInstance().getmCameraDirection() == 0) {
                    if (Hawk.contains(Contants.CURRENT_PHOTO_SIZE_BACK)) {
                        int p = Hawk.get(Contants.CURRENT_PHOTO_SIZE_BACK);
                        size = parameters.getSupportedPictureSizes().get(p);
                        Log.d("jxd", "jpeg CURRENT_PHOTO_SIZE Back : " + size.width + " * " + size.height);
                    }
                } else {
                    if (Hawk.contains(Contants.CURRENT_PHOTO_SIZE_FRONT)) {
                        int p = Hawk.get(Contants.CURRENT_PHOTO_SIZE_FRONT);
                        size = parameters.getSupportedPictureSizes().get(p);
                        Log.d("jxd", "jpeg CURRENT_PHOTO_SIZE front : " + size.width + " * " + size.height);
                    }
                }

                if (size != null) {
                    parameters.setPictureSize(size.width, size.height);
                } else {
                    // parameters.setPictureSize(parameters.getPictureSize().width, parameters.getPictureSize().height);
                }

                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                List<Camera.Size> sizeList = parameters.getSupportedPreviewSizes();//获取所有支持的camera尺寸
                Log.d("jxd", "optionSize : mSurfaceView " + mSurfaceView.getWidth() + " * " + mSurfaceView.getHeight());
                Camera.Size optionSize = getOptimalPreviewSize(sizeList, mSurfaceView.getHeight(), mSurfaceView.getWidth());//获取一个最为适配的camera.size
                Log.d("jxd", "optionSize : " + optionSize.width + " * " + optionSize.height);
                parameters.setPreviewSize(optionSize.width, optionSize.height);//把camera.size赋值到parameters
                if (openFlashLight) {
                    parameters.setFlashMode(parameters.FLASH_MODE_ON);
                } else
                {
                    parameters.setFlashMode(parameters.FLASH_MODE_OFF);
                }
                mCamera.setParameters(parameters);
                //通过SurfaceView显示预览
                mCamera.setPreviewDisplay(mHolder);
                //开始预览
                mCamera.startPreview();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

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


    private void takePhoto() {
        if (mCamera == null ) {
            return;
        }
       // mCamera.stopPreview();
        Log.d("jxd","takePhoto ");
        mCamera.takePicture(null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                new SavePictureTask().execute(data);
               // camera.startPreview();
            }
        });
    }

    // save pic
    class SavePictureTask extends AsyncTask<byte[], String, String> {
        @Override
        protected String doInBackground(byte[]... params) {
            final String fname = DateFormat.format("yyyyMMddhhmmss", new Date()).toString()+".jpg";

            Log.i("jxd", "fname="+fname+";dir="+ CameraApp.getInstance().getAllSdPaths(TakePictureActivity.this)[0]);
            //picture = new File(Environment.getExternalStorageDirectory(),fname);// create file
            File folder = new File(CameraApp.getInstance().getAllSdPaths(TakePictureActivity.this)[0]+"/CameraJXD/Photo");
            if(!folder.exists())
            {
                folder.mkdirs();
            }
            File picture = new File(CameraApp.getInstance().getAllSdPaths(TakePictureActivity.this)[0]+"/CameraJXD/Photo/"+fname);

            Log.d("jxd","path "+picture.getPath() +"getAbsolutePath "+picture.getAbsolutePath());
            try {
                FileOutputStream fos = new FileOutputStream(picture.getPath()); // Get file output stream
                fos.write(params[0]); // Written to the file
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            /**
             * 解决照片旋转90度的问题
             */
            Bitmap bitmap = BitmapFactory.decodeFile(picture.getPath());
            Matrix matrix = new Matrix();
            //前置摄像头要旋转270度
            if(CameraApp.getInstance().getmCameraDirection() == 1)
            {
                matrix.postRotate(270);
            }
            else
            {
                matrix.postRotate(90);
            }

            Bitmap dstbmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(),
                    matrix, true);
            if(CameraApp.getInstance().isWaterMark()){
                dstbmp = addTimeFlag(dstbmp);
            }
            FileOutputStream fOut = null;
            try {
                fOut = new FileOutputStream(Environment.getExternalStorageDirectory()+"/CameraJXD/Photo/"+"rotate"+fname);
                dstbmp.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                fOut.flush();
                fOut.close();
            } catch (Exception e) {

            }
            picture.delete();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mCamera.startPreview();
                    File f = new File(Environment.getExternalStorageDirectory() + "/CameraJXD/photo/" + "rotate" + fname);
                    Glide.with(TakePictureActivity.this).load(f).into(roundImageView);
                    Toast.makeText(TakePictureActivity.this, "拍照成功，存储路径为：" + Environment.getExternalStorageDirectory() + "/CameraJXD/photo/" + "rotate" + fname, Toast.LENGTH_SHORT).show();
                }
            });

            return null;
        }
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        initCamera();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    /**
     * 添加时间水印
     *
     */
    private Bitmap addTimeFlag(Bitmap src){
        // 获取原始图片与水印图片的宽与高
        int w = src.getWidth();
        int h = src.getHeight();
        Bitmap newBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas mCanvas = new Canvas(newBitmap);
        // 往位图中开始画入src原始图片
        mCanvas.drawBitmap(src, 0, 0, null);
        //添加文字
        Paint textPaint = new Paint();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String time = sdf.format(new Date(System.currentTimeMillis()));
        textPaint.setColor(Color.WHITE) ;
        textPaint.setTextSize(45);
//        String familyName = "宋体";
//        Typeface typeface = Typeface.create(familyName,
//                Typeface.BOLD_ITALIC);
//        textPaint.setTypeface(typeface);
//        textPaint.setTextAlign(Align.CENTER);

        mCanvas.drawText(time, (float)(w*3)/8, (float)(h*18)/19, textPaint);
        mCanvas.save(Canvas.ALL_SAVE_FLAG);
        mCanvas.restore();
        return newBitmap ;
    }
}
