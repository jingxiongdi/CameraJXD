package com.example.cameratest.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.cameratest.R;
import com.example.cameratest.adapter.PhotoPreviewAdapter;
import com.example.cameratest.inteface.ListItemClickInteface;
import com.example.cameratest.uistyle.SelectorPopupWindow;
import com.example.cameratest.util.CommonUtils;

import java.io.File;
import java.util.ArrayList;

public class PhotoPreviewActivity extends Activity implements ListItemClickInteface{
    private GridView imgGrid = null;
    private TextView hintText = null;
    private ArrayList<File> photoFiles = null;
    private PhotoPreviewAdapter photoPreviewAdapter = null;
    private SelectorPopupWindow selectorPopupWindow = null;
    private int currentLongClick = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_preview);

        setViews();

        initData();
    }

    private void setViews() {
        selectorPopupWindow = new SelectorPopupWindow(PhotoPreviewActivity.this);
        selectorPopupWindow.setListItemListener(this);
        imgGrid = (GridView) findViewById(R.id.grid_preview);
        imgGrid.setNumColumns(5);
        imgGrid.setVerticalSpacing(10);
        imgGrid.setHorizontalSpacing(10);
        imgGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(PhotoPreviewActivity.this,GalleryActivity.class);
                intent.putExtra("current_postion",position);
                startActivity(intent);
            }
        });

        imgGrid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                currentLongClick = position;
                if(selectorPopupWindow != null){
                   Log.d("imgGrid","selectorPopupWindow show");
                    selectorPopupWindow.show(findViewById(R.id.parent_view));
                }
                return true;
            }
        });
        hintText = (TextView) findViewById(R.id.hint);
    }

    private void initData() {
        photoFiles = CommonUtils.getImgs(PhotoPreviewActivity.this);
        photoPreviewAdapter = new PhotoPreviewAdapter(PhotoPreviewActivity.this,photoFiles);
        imgGrid.setAdapter(photoPreviewAdapter);
      }


    /**   * 根据指定的图像路径和大小来获取缩略图   * 此方法有两点好处：
     *  1. 使用较小的内存空间，第一次获取的bitmap实际上为null，只是为了读取宽度和高度，
     *      第二次读取的bitmap是根据比例压缩过的图像，第三次读取的bitmap是所要的缩略图。   *
     *  2. 缩略图对于原图像来讲没有拉伸，这里使用了2.2版本的新工具ThumbnailUtils，使   *
     *  用这个工具生成的图像不会被拉伸。   *
     *  @param imagePath 图像的路径   *
     *  @param width 指定输出图像的宽度   *
     *  @param height 指定输出图像的高度   *
     *  @return 生成的缩略图
     *  */
    private Bitmap getImageThumbnail(String imagePath, int width, int height) {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;   // 获取这个图片的宽和高，注意此处的bitmap为null
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        options.inJustDecodeBounds = false; // 设为 false
        // 计算缩放比
         int h = options.outHeight;
         int w = options.outWidth;
         int beWidth = w / width;
         int beHeight = h / height;
         int be = 1;
         if (beWidth < beHeight) {
             be = beWidth;
         } else {
             be = beHeight;
         }   if (be <= 0) {
             be = 1;
         }
         options.inSampleSize = be;
         // 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        // 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
         bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
         return bitmap;
    }

    @Override
    public void shareThirdPlatform() {
        Uri uri = Uri.fromFile(photoFiles.get(currentLongClick));
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.setType("image/jpeg");
        startActivity(Intent.createChooser(intent, "分享"));
    }

    @Override
    public void delete() {
        photoFiles.get(currentLongClick).delete();
        photoFiles = CommonUtils.getImgs(PhotoPreviewActivity.this);
        photoPreviewAdapter.notifyData(photoFiles);
    }
}
