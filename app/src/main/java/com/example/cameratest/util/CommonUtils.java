package com.example.cameratest.util;

import android.content.Context;

import com.example.cameratest.CameraApp;
import com.example.cameratest.activity.GalleryActivity;

import java.io.File;

/**
 * Created by jingxiongdi on 2018/4/9.
 */

public class CommonUtils {
    public static File[] getImgs(Context context)
    {
        File folder = new File(CameraApp.getInstance().getAllSdPaths(context)[0]+"/CameraJXD/Photo");
      //  File folder = new File(CameraApp.getInstance().getAllSdPaths(context)[0]+"/DCIM/Camera");
        if(!folder.exists())
        {
            folder.mkdirs();
        }
        return folder.listFiles();
    }
}
