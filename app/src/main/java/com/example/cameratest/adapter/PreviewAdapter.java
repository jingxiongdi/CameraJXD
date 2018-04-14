package com.example.cameratest.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.util.Log;
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

import org.w3c.dom.Text;

import java.io.File;
import java.text.DecimalFormat;
import java.util.List;

/*
 * This Adapter for GridView, in epg
 */
public class PreviewAdapter extends BaseAdapter
{
    private LayoutInflater mInflater;
    private Context mContext;
    private List<VideoBean> videoBeanList;


    public PreviewAdapter(Context context, List<VideoBean> list)
    {
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
        videoBeanList = list;
    }

    public void notifyData( List<VideoBean> list){
        videoBeanList = list;
        notifyDataSetChanged();
    }
    
    public int getCount()
    {
        // TODO Auto-generated method stub
        return videoBeanList.size();
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


        if(videoBeanList.get(position).getThumbPath() == null){
            //Log.d("jxd",getVideoPhoto(videoBeanList.get(position).getPath())+" videoBeanList "+videoBeanList.size());
            viewHolder.imageGrid.setImageBitmap(videoBeanList.get(position).getBitmap());
            int ms = videoBeanList.get(position).getDuration();//Integer.parseInt();
            String s=getFormatTime(ms);
            viewHolder.durationText.setText(s);

        } else {
            File f = new File(videoBeanList.get(position).getThumbPath());
            Glide.with(mContext).load(f).into(viewHolder.imageGrid);

            int ms = videoBeanList.get(position).getDuration();
            String s=getFormatTime(ms);
            viewHolder.durationText.setText(s);
            viewHolder.durationText.setText(videoBeanList.get(position).getDuration());
        }
        File f = new File(videoBeanList.get(position).getPath());
        long length = f.length();
        float ff = length/(1024f*1024f);
        DecimalFormat df = new DecimalFormat("#.##");
        String text = df.format(ff);
        viewHolder.sizeText.setText(text+"M");
        viewHolder.nameText.setText(f.getName());
        if(position == videoBeanList.size()-1){
            mContext.sendBroadcast(new Intent("com.jxd.complete.videopreview"));
        }
        return convertView;
    }

    private String getFormatTime(int ms){
        String s="";
        int min =  ms /(1000*60);
        int ss = ms/1000;
        int mss = ms%1000;
        if(min != 0){
            s=min+"min"+ss+"s"+mss+"ms";
        }
        else if(min==0&&ss!=0)
        {
            s=ss+"s"+mss+"ms";
        }
        else if(min == 0&& ss == 0){
            s = mss+"ms";
        }

        return s;
    }


    
    private static class ViewHolder
    {
        private ImageView imageGrid;
        private TextView  durationText;
        private TextView sizeText;
        private MarqueeTextView nameText;
    }
    
}
