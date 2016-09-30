package com.test.alarm.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.UnicodeSetSpanner;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.test.alarm.MainActivity;
import com.test.alarm.R;
import com.test.alarm.adapters.DifficultAdapter;
import com.test.alarm.adapters.TypeAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by kostya on 26.09.2016.
 */

public class TypeAndDifficultFragment extends Fragment {
    private ListView lvDifficulties;
    private ListView lvTypes;
    private MainActivity mainActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainActivity = (MainActivity) getActivity();

        View rootView = inflater.inflate(R.layout.type_and_difficult_layout, container, false);

        initDifficulties(rootView, inflater);
        initTypes(rootView, inflater);

        mainActivity.setBackBtnOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().remove(TypeAndDifficultFragment.this).commit();
                mainActivity.setActionBarTitle(getResources().getString(R.string.set_alarm_fragment));
                mainActivity.setBackBtnVisibility(false);
                mainActivity.setBackBtnOnClickListener(null);
            }
        });

        return rootView;
    }

    private void initDifficulties(View rootView, LayoutInflater inflater) {
        lvDifficulties = (ListView) rootView.findViewById(R.id.lvDifficult);

        List<Bitmap> difficultBitmapList = new ArrayList<>();
        difficultBitmapList.add(BitmapFactory.decodeResource(getContext().getResources(),
                R.drawable.low_difficult));
        difficultBitmapList.add(BitmapFactory.decodeResource(getContext().getResources(),
                R.drawable.medium_difficult));
        difficultBitmapList.add(BitmapFactory.decodeResource(getContext().getResources(),
                R.drawable.high_difficult));

        List<String> difficultValues = Arrays.asList(getResources().getStringArray(R.array.difficulties));

        DifficultAdapter difficultAdapter = new DifficultAdapter(getContext(),
                difficultBitmapList, difficultValues, mainActivity.getDifficult());

        View difficultHeader = inflater.inflate(R.layout.list_view_header_layout, lvDifficulties, false);
        TextView tvHeaderDifficult = (TextView) difficultHeader.findViewById(R.id.textView);
        tvHeaderDifficult.setText(getResources().getString(R.string.difficult));
        lvDifficulties.addHeaderView(difficultHeader);
        lvDifficulties.setAdapter(difficultAdapter);
        lvDifficulties.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // zero element is header
                if (i > 0) {
                    RadioButton radioButton = (RadioButton) view.findViewById(R.id.rb1);
                    mainActivity.setDifficult(i - 1);
                    radioButton.setChecked(true);

                    for (int j = 1; j < adapterView.getChildCount(); j++) {
                        if (j != i) {
                            RadioButton rb = (RadioButton) adapterView.getChildAt(j).findViewById(R.id.rb1);
                            if (rb != null) {
                                rb.setChecked(false);
                            }
                        }
                    }
                }
            }
        });

    }

    private void initTypes(View rootView, LayoutInflater inflater) {
        lvTypes = (ListView) rootView.findViewById(R.id.lvType);

        List<Bitmap> typesBitmapList = new ArrayList<>();
        typesBitmapList.add(BitmapFactory.decodeResource(getContext().getResources(),
                R.drawable.expression));
        typesBitmapList.add(BitmapFactory.decodeResource(getContext().getResources(),
                R.drawable.connection));
        typesBitmapList.add(BitmapFactory.decodeResource(getContext().getResources(),
                R.drawable.random));

        List<String> typesValues = Arrays.asList(getResources().getStringArray(R.array.types));

        TypeAdapter typesAdapter = new TypeAdapter(getContext(),
                typesBitmapList, typesValues, mainActivity.getType());

        View typeHeader = inflater.inflate(R.layout.list_view_header_layout, lvTypes, false);
        TextView tvHeaderType = (TextView) typeHeader.findViewById(R.id.textView);
        tvHeaderType.setText(getResources().getString(R.string.type));
        lvTypes.addHeaderView(typeHeader);
        lvTypes.setAdapter(typesAdapter);
        lvTypes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    RadioButton radioButton = (RadioButton) view.findViewById(R.id.rb1);
                    mainActivity.setType(i - 1);
                    radioButton.setChecked(true);

                    for (int j = 1; j < adapterView.getChildCount(); j++) {
                        if (j != i) {
                            RadioButton rb = (RadioButton) adapterView.getChildAt(j).findViewById(R.id.rb1);
                            rb.setChecked(false);
                        }
                    }
                }
            }
        });
    }
}
