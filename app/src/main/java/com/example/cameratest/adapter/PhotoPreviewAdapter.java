package com.example.cameratest.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.cameratest.R;
import com.example.cameratest.bean.VideoBean;
import com.example.cameratest.uistyle.MarqueeTextView;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/*
 * This Adapter for GridView, in epg
 */
public class PhotoPreviewAdapter extends BaseAdapter
{
    private LayoutInflater mInflater;
    private Context mContext;
    private ArrayList<File> photoList;


    public PhotoPreviewAdapter(Context context, ArrayList<File> list)
    {
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
        photoList = list;
    }

    public void notifyData( ArrayList<File> list){
        photoList = list;
        notifyDataSetChanged();
    }
    
    public int getCount()
    {
        // TODO Auto-generated method stub
        return photoList.size();
    }
    
    public Object getItem(int position)
    {
        // TODO Auto-generated method stub
        return position;
    }
    
    public long getItemId(int position)
    {
        // TODO Auto-generated method stub
        return position;
    }
    
    public View getView(int position, View convertView, ViewGroup parent)
    {
        // TODO Auto-generated method stub
        final ViewHolder viewHolder;
        if (convertView == null)
        {
            viewHolder = new ViewHolder();
            if (viewHolder != null)
            {
                convertView = mInflater.inflate(R.layout.grid_item, null);
                viewHolder.imageGrid = (ImageView) convertView.findViewById(R.id.video_preview_img);
                viewHolder.durationText = (TextView) convertView.findViewById(R.id.duration);
                viewHolder.sizeText = (TextView) convertView.findViewById(R.id.size);
                viewHolder.nameText = (MarqueeTextView) convertView.findViewById(R.id.name);
                convertView.setTag(viewHolder);
            }
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Glide.with(mContext).load(photoList.get(position)).into(viewHolder.imageGrid);

        long length = photoList.get(position).length();
        float ff = length/(1024f*1024f);
        if(ff<1)
        {
            ff = ff*1024;
            DecimalFormat df = new DecimalFormat("#");
            String text = df.format(ff);
            viewHolder.sizeText.setText(text+"KB");
        }
        else
        {
            DecimalFormat df = new DecimalFormat("#.##");
            String text = df.format(ff);
            viewHolder.sizeText.setText(text+"MB");
        }
        viewHolder.durationText.setVisibility(View.GONE);
        viewHolder.nameText.setText(photoList.get(position).getName());
        return convertView;
    }




    
    private static class ViewHolder
    {
        private ImageView imageGrid;
        private TextView  durationText;
        private TextView sizeText;
        private MarqueeTextView nameText;
    }
    
}
