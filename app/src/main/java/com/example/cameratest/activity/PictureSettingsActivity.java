package com.example.cameratest.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.cameratest.CameraApp;
import com.example.cameratest.R;
import com.example.cameratest.util.Contants;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.List;

public class PictureSettingsActivity extends Activity
{
    private ArrayList<String> listPhotoSize = new ArrayList<>();
    private ArrayList<String> listPhotoQuality = new ArrayList<>();
    private ArrayAdapter<String> mAdapter;
    private TextView tvShow = null;
    private Spinner mPhotoSize = null;

    private TextView qualityText = null;
    private Spinner mPhotoQualitySpinner = null;
    private ArrayAdapter<String> mQualityAdapter;

    private TextView cameraText = null;
    private Spinner  mCameraSpinner = null;
    private ArrayAdapter<String> mCameraAdapter;
    private ToggleButton waterMark = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        setViews();

        initData();
    }

    private void setViews()
    {
        cameraText = (TextView) findViewById(R.id.camera);
        mCameraSpinner = (Spinner) findViewById(R.id.camera_spinner);

        tvShow = (TextView) findViewById(R.id.tvShow);
        mPhotoSize = (Spinner) findViewById(R.id.spDwon);

        qualityText = (TextView) findViewById(R.id.qulity);
        mPhotoQualitySpinner = (Spinner) findViewById(R.id.qulitySpiner);

        waterMark = (ToggleButton) findViewById(R.id.watermark);
        waterMark.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    CameraApp.getInstance().setWaterMark(true);
                    Toast.makeText(PictureSettingsActivity.this,"添加时间水印",Toast.LENGTH_SHORT).show();
                }
                else {
                    CameraApp.getInstance().setWaterMark(false);
                    Toast.makeText(PictureSettingsActivity.this,"去除时间水印",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initData()
    {
       initPhotoSize();
        for(int i = 10;i<=100;i=i+10)
        {
            listPhotoQuality.add(i+"");
        }
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
                initPhotoSize();
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

          /*新建适配器*/
        mAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,listPhotoSize);

        /*adapter设置一个下拉列表样式，参数为系统子布局*/
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

        /*spDown加载适配器*/
        mPhotoSize.setAdapter(mAdapter);

        mQualityAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,listPhotoQuality);
 /*adapter设置一个下拉列表样式，参数为系统子布局*/
        mQualityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
         /*soDown的监听器*/
        mPhotoSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String cityName = mAdapter.getItem(position);   //获取选中的那一项
                Log.d("jxd", "mPhotoSize " + position + " " + cityName);
                tvShow.setText("您设置的照片大小是" + cityName);
                if (CameraApp.getInstance().getmCameraDirection() == 0) {
                    Hawk.put(Contants.CURRENT_PHOTO_SIZE_BACK, position);
                } else {
                    Hawk.put(Contants.CURRENT_PHOTO_SIZE_FRONT, position);
                }

                // Toast.makeText(PictureSettingsActivity.this,"设置照片大小成功",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mPhotoQualitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String quality = mQualityAdapter.getItem(position);   //获取选中的那一项
                Log.d("jxd", "mQualityAdapter " + position + " " + quality);
                qualityText.setText("您设置的照片质量是" + quality + "%");
                Hawk.put(Contants.CURRENT_PHOTO_QUALITY, quality);
                // Toast.makeText(PictureSettingsActivity.this,"设置照片质量成功",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mPhotoQualitySpinner.setAdapter(mQualityAdapter);

        mCameraSpinner.setSelection(CameraApp.getInstance().getmCameraDirection());
        cameraText.setText("当前摄像头为: "+cameraList.get(CameraApp.getInstance().getmCameraDirection()));
        if(CameraApp.getInstance().getmCameraDirection() == 0)
        {
            if(Hawk.contains(Contants.CURRENT_PHOTO_SIZE_BACK))
            {
                int p = Hawk.get(Contants.CURRENT_PHOTO_SIZE_BACK);
                Log.d("jxd","CURRENT_PHOTO_SIZE : "+p);
                mPhotoSize.setSelection(p);
            }
        }
        else
        {
            if(Hawk.contains(Contants.CURRENT_PHOTO_SIZE_FRONT))
            {
                int p = Hawk.get(Contants.CURRENT_PHOTO_SIZE_FRONT);
                Log.d("jxd","CURRENT_PHOTO_SIZE : "+p);
                mPhotoSize.setSelection(p);
            }
        }


        if(Hawk.contains(Contants.CURRENT_PHOTO_QUALITY))
        {
            String p = Hawk.get(Contants.CURRENT_PHOTO_QUALITY);
            int pos = 0;
            try {
                pos = Integer.parseInt(p);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            Log.d("jxd","CURRENT_PHOTO_QUALITY : "+p+"   pos : "+pos);
            mPhotoQualitySpinner.setSelection(pos/10 - 1);
        }
    }

    private void initPhotoSize() {
        listPhotoSize.clear();
        if(CameraApp.getInstance().getmCameraDirection() == 0)
        {
            if(Hawk.contains(Contants.PHOTO_SIZE_BACK))
            {
                List<android.hardware.Camera.Size> supportedPictureSizes = Hawk.get(Contants.PHOTO_SIZE_BACK);
                for(int i = 0; i<supportedPictureSizes.size();i++)
                {
                    // Log.d("jxd", "PictureSettingsActivity supportedPictureSizes : " + supportedPictureSizes.get(i).width + " * " + supportedPictureSizes.get(i).height);
                    listPhotoSize.add(i,supportedPictureSizes.get(i).width+" * "+supportedPictureSizes.get(i).height);
                }
            }
            else
            {
                Toast.makeText(PictureSettingsActivity.this, "请先打开一次后置相机", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            if(Hawk.contains(Contants.PHOTO_SIZE_FRONT))
            {
                List<android.hardware.Camera.Size> supportedPictureSizes = Hawk.get(Contants.PHOTO_SIZE_FRONT);
                for(int i = 0; i<supportedPictureSizes.size();i++)
                {
                    // Log.d("jxd", "PictureSettingsActivity supportedPictureSizes : " + supportedPictureSizes.get(i).width + " * " + supportedPictureSizes.get(i).height);
                    listPhotoSize.add(i,supportedPictureSizes.get(i).width+" * "+supportedPictureSizes.get(i).height);
                }
            }
            else
            {
                Toast.makeText(PictureSettingsActivity.this, "请先打开一次前置相机", Toast.LENGTH_SHORT).show();
            }
        }

    }

}
