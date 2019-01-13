package com.example.cameratest.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;

import com.example.cameratest.CameraApp;
import com.example.cameratest.activity.GalleryActivity;
import com.example.cameratest.activity.VideoPreviewActivity;
import com.example.cameratest.bean.VideoBean;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by jingxiongdi on 2018/4/9.
 */

public class CommonUtils {
    public static ArrayList<File> getImgs(Context context) {
        File folder = new File(CameraApp.getInstance().getAllSdPaths(context)[0] + "/CameraJXD/Photo");
        //  File folder = new File(CameraApp.getInstance().getAllSdPaths(context)[0]+"/DCIM/Camera");
        if (!folder.exists()) {
            folder.mkdirs();
        }
        ArrayList<File> files = new ArrayList<>();
        for (File file : folder.listFiles()) {
            files.add(file);
        }
        Collections.sort(files, new FileComparator());
        return files;
    }

    public static Bitmap getVideoBitmap(Context context)
    {
        String path = CameraApp.getAllSdPaths(context)[0];
        if (path != null) {
            File f = new File(path + "/CameraJXD/Video");
            if(!f.exists()){
                f.mkdirs();
            }
            File[] files = f.listFiles();
            if(files == null || files.length==0){
                return null;
            }
            ArrayList<File> fileList = new ArrayList<>();
            for(File file:files){
                fileList.add(file);
            }
            Collections.sort(fileList, new CommonUtils.FileComparator());
            if(fileList.size() !=0){
               return getVideoPhoto(fileList.get(0).getPath());
            }

        }
        return null;
    }

    //根据路径得到视频缩略图
    public static Bitmap getVideoPhoto(String videoPath) {
        MediaMetadataRetriever media =new MediaMetadataRetriever();
        media.setDataSource(videoPath);
        Bitmap bitmap = media.getFrameAtTime();
        return bitmap;
    }

    public static class FileComparator implements Comparator<File> {

        @Override
        public int compare(File lhs, File rhs) {
            if(lhs.lastModified()<rhs.lastModified()){
                return 1;//最后修改的照片在前
            }else{
                return -1;
            }
        }

    }
}
