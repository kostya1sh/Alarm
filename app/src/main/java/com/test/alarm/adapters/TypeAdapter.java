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
 * Created by kostya on 29.09.2016.
 */

public class TypeAdapter extends BaseAdapter {
    private Context mContext;
    private List<Bitmap> bitmaps = new ArrayList<>();
    private List<String> values = new ArrayList<>();
    private int checkedItem = 0;

    public TypeAdapter(Context context, List<Bitmap> bitmaps, List<String> values, int checkedItem) {
        this.mContext = context;
        this.bitmaps = bitmaps;
        this.values = values;
        this.checkedItem = checkedItem;
    }

    @Override
    public int getCount() {
        return values.size();
    }

    @Override
    public Object getItem(int i) {
        return values.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (view == null) {
            view = inflater.inflate(R.layout.item_type_layout, null, false);
        }
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView1);
        RadioButton radioButton = (RadioButton) view.findViewById(R.id.rb1);
        imageView.setImageBitmap(bitmaps.get(i));
        radioButton.setText(values.get(i));

        if (i == checkedItem) {
            radioButton.setChecked(true);
        }

        return view;
    }
}