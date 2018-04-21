package com.example.cameratest;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.hardware.Camera;
import android.os.storage.StorageManager;
import android.view.Surface;

import com.orhanobut.hawk.Hawk;

import java.lang.reflect.Method;

public class CameraApp extends Application
{
    private static CameraApp app = null;

    private int mCameraDirection = 0;

    private boolean waterMark = true;

    public static CameraApp getInstance()
    {
        if(app == null)
        {
            app = new CameraApp();
        }
        return app;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        //初始化sharedpreference框架Hawk
        Hawk.init(this).build();
    }

    public int getmCameraDirection()
    {
        return mCameraDirection;
    }

    public void setmCameraDirection(int dir)
    {
        mCameraDirection = dir;
    }

    /**
            * 得到所有的存储路径（内部存储+外部存储）
        *
        * @param context
    * @return
            */
    public static String[] getAllSdPaths(Context context) {
        Method mMethodGetPaths = null;
        String[] paths = null;
        //通过调用类的实例mStorageManager的getClass()获取StorageManager类对应的Class对象
        //getMethod("getVolumePaths")返回StorageManager类对应的Class对象的getVolumePaths方法，这里不带参数
        StorageManager mStorageManager = (StorageManager)context
                .getSystemService(context.STORAGE_SERVICE);//storage
        try {
            mMethodGetPaths = mStorageManager.getClass().getMethod("getVolumePaths");
            paths = (String[]) mMethodGetPaths.invoke(mStorageManager);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return paths;
    }

    public void setCameraDisplayOrientation ( Activity activity , int cameraId , android.hardware.Camera camera )
    {
        android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo ( cameraId , info );
        int rotation = activity.getWindowManager ().getDefaultDisplay ().getRotation ();
        int degrees = 0 ;
        switch ( rotation )
        {
            case Surface.ROTATION_0 :
                degrees = 0 ;
                break ;
            case Surface.ROTATION_90 :
                degrees = 90 ;
                break ;
            case Surface.ROTATION_180 :
                degrees = 180 ;
                break ;
            case Surface.ROTATION_270 :
                degrees = 270 ;
                break ;
        }
        int result = 0;
        if ( info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT )
        {
            result = ( info.orientation + degrees ) % 360 ;
            result = ( 360 - result ) % 360 ; // compensate the mirror
        }
        else
        {
            // back-facing
            result = ( info.orientation - degrees + 360 ) % 360 ;
        }
        camera.setDisplayOrientation ( result );
    }

    public boolean isWaterMark() {
        return waterMark;
    }

    public void setWaterMark(boolean waterMark) {
        this.waterMark = waterMark;
    }
}