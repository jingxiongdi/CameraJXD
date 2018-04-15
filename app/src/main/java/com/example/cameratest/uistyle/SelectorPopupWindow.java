package com.example.cameratest.uistyle;

import android.content.Context;
import android.graphics.Color;
import android.text.InputFilter;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.example.cameratest.R;
import com.example.cameratest.inteface.ListItemClickInteface;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/3/29.
 */

public class SelectorPopupWindow {
    private Context mContext = null;
    private View mParentView = null;
    private LayoutInflater mInflater;
    private PopupWindow mPopupWindow;
    private ListView mListView = null;
    private ListItemClickInteface itemClickInteface = null;
    private int mType = 0;

    public SelectorPopupWindow(Context context) {
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mParentView = mInflater.inflate(R.layout.popupwindow_selector, null);
        initPopView();
    }

    public SelectorPopupWindow(Context context,int type) {
        this.mContext = context;
        mType = type;
        mInflater = LayoutInflater.from(mContext);
        mParentView = mInflater.inflate(R.layout.popupwindow_selector, null);
        initPopView();
    }

    private void initPopView() {

        mPopupWindow = new PopupWindow(mParentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setAnimationStyle(R.style.popwin_anim_bottom_style);
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("删除");
        if(mType == 0){
            arrayList.add("分享");
        }

        mListView = (ListView) mParentView.findViewById(R.id.selector_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, arrayList);
        mListView.setAdapter(adapter);
        mListView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_DOWN){
                    if(keyCode == KeyEvent.KEYCODE_BACK){
                        dismiss();
                    }
                }
                return false;
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        itemClickInteface.delete();
                        break;
                    case 1:
                        itemClickInteface.shareThirdPlatform();
                        break;
                    default:
                        break;
                }
                dismiss();
            }
        });

        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(false);
        mPopupWindow.update();
    }

    public void setListItemListener(ListItemClickInteface listener){
        itemClickInteface = listener;
    }



    public void setOnKey(View.OnKeyListener l) {
        mParentView.setOnKeyListener(l);
    }

    public void setOnDismiss(PopupWindow.OnDismissListener listener) {
        mPopupWindow.setOnDismissListener(listener);
    }

    public void show(View view) {
        mPopupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    public boolean isShowing() {
        return mPopupWindow.isShowing();
    }

    public void dismiss() {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
    }

}
