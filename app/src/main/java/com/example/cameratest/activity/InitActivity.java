package com.example.cameratest.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.cameratest.R;
import com.example.cameratest.adapter.GridAdapter;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class InitActivity extends Activity implements EasyPermissions.PermissionCallbacks{
    private GridView mGridView = null;
    private GridAdapter mGridAdapter = null;
    private String[] mPermissionList = new String[]{Manifest.permission.CAMERA,Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);

        if (Build.VERSION.SDK_INT >= 23) {

            if (EasyPermissions.hasPermissions(InitActivity.this, mPermissionList)) {
            } else {
                EasyPermissions.requestPermissions(this, "拍照需要的权限", 1, mPermissionList);
            }
        } else {
        }

        setView();

        initData();
    }

    private void initData() {
        mGridAdapter = new GridAdapter(this);
        mGridView.setAdapter(mGridAdapter);
        mGridView.setOnItemClickListener(mItemClick);
    }

    private void setView() {
        mGridView = (GridView) findViewById(R.id.grid);
    }

    AdapterView.OnItemClickListener mItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch (position)
            {
                case 0:
                    Intent intent0 = new Intent(InitActivity.this,TakePictureActivity.class);
                    startActivity(intent0);
                    break;
                case 1:
                    Intent intent1 = new Intent(InitActivity.this,PictureSettingsActivity.class);
                    startActivity(intent1);
                    break;
                case 2:
                    Intent intent2 = new Intent(InitActivity.this,RecordVideoActivity.class);
                    startActivity(intent2);
                    break;
                case 3:
                    Intent intent3 = new Intent(InitActivity.this,VideoSettingsActivity.class);
                    startActivity(intent3);
                    break;
            }
        }
    };
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        Log.i("jxd", "onPermissionsGranted:" + requestCode + ":" + perms.size());
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Log.i("jxd", "onPermissionsDenied:" + requestCode + ":" + perms.size());
    }
}
