package com.test.alarm.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.test.alarm.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kostya on 28.09.2016.
 */

public class DifficultAdapter extends BaseAdapter {
    private Context mContext;
    private List<Bitmap> bitmaps = new ArrayList<>();
    private List<String> values = new ArrayList<>();
    private int checkedItem = 0;

    public DifficultAdapter(Context context, List<Bitmap> bitmaps, List<String> values, int checkedItem) {
        this.mContext = context;
        this.bitmaps = bitmaps;
        this.values = values;
        this.checkedItem = checkedItem;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Object getItem(int i) {
        return values.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    private static class ViewHolder {
        int position;
        ImageView imageView;
        RadioButton radioButton;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_difficult_layout, viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.position = i;
            viewHolder.imageView = (ImageView) view.findViewById(R.id.imageView1);
            viewHolder.radioButton = (RadioButton) view.findViewById(R.id.rb1);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        ImageView imageView = viewHolder.imageView;
        RadioButton radioButton = viewHolder.radioButton;

        imageView.setImageBitmap(bitmaps.get(i));
        radioButton.setText(values.get(i));

        if (i == checkedItem) {
            radioButton.setChecked(true);
        }

        return view;
    }
}
