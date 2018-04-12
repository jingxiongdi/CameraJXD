package com.example.cameratest.activity;

import com.example.cameratest.CameraApp;
import com.example.cameratest.R;
import com.example.cameratest.util.Contants;
import com.orhanobut.hawk.Hawk;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class VideoSettingsActivity extends Activity {
    private TextView cameraText = null;
    private Spinner mCameraSpinner = null;
    private ArrayAdapter<String> mCameraAdapter;

    private TextView tvShow = null;
    private Spinner mPhotoSize = null;
    private ArrayAdapter<String> mAdapter;
    private ArrayList<String> listVideoSize = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_setting);

        setView();

        initData();
    }

    private void initData() {
        initVideoSize();
        mPhotoSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String cityName = mAdapter.getItem(position);   //获取选中的那一项
                Log.d("jxd", "mPhotoSize " + position + " " + cityName);
                tvShow.setText("您设置的视频大小是" + cityName);
                if (CameraApp.getInstance().getmCameraDirection() == 0) {
                    Hawk.put(Contants.CURRENT_VIDEO_SIZE_BACK, position);
                } else {
                    Hawk.put(Contants.CURRENT_VIDEO_SIZE_FRONT, position);
                }

                // Toast.makeText(PictureSettingsActivity.this,"设置照片大小成功",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        /*新建适配器*/
        mAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,listVideoSize);

        /*adapter设置一个下拉列表样式，参数为系统子布局*/
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

        /*spDown加载适配器*/
        mPhotoSize.setAdapter(mAdapter);

        if(CameraApp.getInstance().getmCameraDirection() == 0)
        {
            if(Hawk.contains(Contants.CURRENT_VIDEO_SIZE_BACK))
            {
                int p = Hawk.get(Contants.CURRENT_VIDEO_SIZE_BACK);
                Log.d("jxd","CURRENT_VIDEO_SIZE_BACK : "+p);
                mPhotoSize.setSelection(p);
            }
        }
        else
        {
            if(Hawk.contains(Contants.CURRENT_VIDEO_SIZE_FRONT))
            {
                int p = Hawk.get(Contants.CURRENT_VIDEO_SIZE_FRONT);
                Log.d("jxd","CURRENT_VIDEO_SIZE_BACK : "+p);
                mPhotoSize.setSelection(p);
            }
        }
    }

    private void initVideoSize() {
        listVideoSize.clear();
        listVideoSize.add(0, "QUALITY_TIME_LAPSE_LOW");
        listVideoSize.add(1,"QUALITY_TIME_LAPSE_HIGH");
        listVideoSize.add(2,"176 x 144");
        listVideoSize.add(3,"352 x 288");
        listVideoSize.add(4,"720 x 480");
        listVideoSize.add(5,"1280 x 720");
        listVideoSize.add(6,"1920 x 1080");
        listVideoSize.add(7,"320x240");
//        if(CameraApp.getInstance().getmCameraDirection() == 0)
//        {
//            if(Hawk.contains(Contants.VIDEO_SIZE_BACK))
//            {
////                List<Camera.Size> supportedPictureSizes = Hawk.get(Contants.VIDEO_SIZE_BACK);
////                for(int i = 0; i<supportedPictureSizes.size();i++)
////                {
////                    // Log.d("jxd", "PictureSettingsActivity supportedPictureSizes : " + supportedPictureSizes.get(i).width + " * " + supportedPictureSizes.get(i).height);
////                    listVideoSize.add(i,supportedPictureSizes.get(i).width+" * "+supportedPictureSizes.get(i).height);
////                }
//            }
//            else
//            {
//                Toast.makeText(VideoSettingsActivity.this, "请先打开一次后置相机", Toast.LENGTH_SHORT).show();
//            }
//        }
//        else
//        {
//            if(Hawk.contains(Contants.VIDEO_SIZE_FRONT))
//            {
//                List<android.hardware.Camera.Size> supportedPictureSizes = Hawk.get(Contants.VIDEO_SIZE_FRONT);
//                for(int i = 0; i<supportedPictureSizes.size();i++)
//                {
//                    // Log.d("jxd", "PictureSettingsActivity supportedPictureSizes : " + supportedPictureSizes.get(i).width + " * " + supportedPictureSizes.get(i).height);
//                    listVideoSize.add(i,supportedPictureSizes.get(i).width+" * "+supportedPictureSizes.get(i).height);
//                }
//            }
//            else
//            {
//                Toast.makeText(VideoSettingsActivity.this, "请先打开一次前置相机", Toast.LENGTH_SHORT).show();
//            }
//        }
    }

    private void setView() {

        tvShow = (TextView) findViewById(R.id.tvShow);
        mPhotoSize = (Spinner) findViewById(R.id.spDwon);
        
        
        
        cameraText = (TextView) findViewById(R.id.camera);
        mCameraSpinner = (Spinner) findViewById(R.id.camera_spinner);

        final ArrayList<String> cameraList = new ArrayList<>();
        cameraList.add("后置摄像头");
        cameraList.add("前置摄像头");
        mCameraAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,cameraList);
        mCameraAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        mCameraSpinner.setAdapter(mCameraAdapter);
        mCameraSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("jxd", "mCameraSpinner " + position);
                cameraText.setText("当前摄像头为 " + cameraList.get(position));
                CameraApp.getInstance().setmCameraDirection(position);
                // Toast.makeText(PictureSettingsActivity.this,"设置摄像头成功",Toast.LENGTH_SHORT).show();
              //  initPhotoSize();
              //  mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

}
