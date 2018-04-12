package com.example.cameratest.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cameratest.R;

/*
 * This Adapter for GridView, in epg
 */
public class GridAdapter extends BaseAdapter
{
    private LayoutInflater mInflater;
    private Context mContext;

    
    public GridAdapter(Context context)
    {
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }
    
    public int getCount()
    {
        // TODO Auto-generated method stub
        return 4;
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
                convertView = mInflater.inflate(R.layout.gridview_item, null);
                viewHolder.epgInfo = (TextView) convertView.findViewById(R.id.textViewGridItem);
                //viewHolder.epgInfo.setBackgroundResource(R.drawable.epghighlight);
                viewHolder.imageGrid = (ImageView) convertView.findViewById(R.id.imageViewitem);
                convertView.setTag(viewHolder);
            }
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        switch (position)
        {
            case 0:
                viewHolder.epgInfo.setText("拍照");
                viewHolder.imageGrid.setBackgroundResource(R.mipmap.ic_launcher);
                break;
            case 1:
                viewHolder.epgInfo.setText("拍照设置");
                viewHolder.imageGrid.setBackgroundResource(R.mipmap.ic_launcher);

                break;
            case 2:
                viewHolder.epgInfo.setText("录像");
                viewHolder.imageGrid.setBackgroundResource(R.mipmap.ic_launcher);
                break;
            case 3:
                viewHolder.epgInfo.setText("录像设置");
                viewHolder.imageGrid.setBackgroundResource(R.mipmap.ic_launcher);
                break;
        }



        return convertView;
    }
    
    private static class ViewHolder
    {
        private TextView epgInfo;
        private ImageView imageGrid;
    }
    
}
