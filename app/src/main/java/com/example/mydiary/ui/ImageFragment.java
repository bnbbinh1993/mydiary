package com.example.mydiary.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mydiary.R;
import com.example.mydiary.adapters.AdapterImageSub;
import com.example.mydiary.models.Diary;
import com.example.mydiary.models.ImageSub;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class ImageFragment extends Fragment {

    private final ArrayList<ImageSub> list = new ArrayList<>();
    private ArrayList<Diary> res = new ArrayList<>();
    private RecyclerView mRecyclerview;
    private LinearLayout no_item;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_image, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI(view);
        initAction();

    }


    @SuppressLint("SimpleDateFormat")
    private List<Long> buildListDate(List<Diary> data) {
        List<Long> result = new ArrayList<>();
        for (Diary diary : res) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(diary.getRealtime());
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int month = calendar.get(Calendar.MONTH) + 1;
            int year = calendar.get(Calendar.YEAR);
            String key = day + "/" + month + "/" + year;
            try {
                calendar.setTimeInMillis(new SimpleDateFormat("dd/MM/yyyy").parse(key).getTime());
                if (!result.contains(calendar.getTimeInMillis())) {
                    result.add(diary.getRealtime());
                    Log.d("TAG", "" + diary.getRealtime());
                } else {
                    Log.d("TAG", "Đã tồn tại!");
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        Log.d("TAG", "ListDate: " + res.size());
        return result;
    }


    private void updateUI() {
        if (list.size() > 0) {
            no_item.setVisibility(View.GONE);
        } else {
            no_item.setVisibility(View.VISIBLE);
        }
    }

    private List<String> buildListImage(long key, List<Diary> data) {
        List<String> result = new ArrayList<>();
        Calendar calendarKey = Calendar.getInstance();
        calendarKey.setTimeInMillis(key);
        Calendar calendarEvent = Calendar.getInstance();

        for (Diary model : data) {
            calendarEvent.setTimeInMillis(model.getRealtime());
            if (calendarKey.get(Calendar.DAY_OF_MONTH) == calendarEvent.get(Calendar.DAY_OF_MONTH)
                    && calendarKey.get(Calendar.MONTH) + 1 == calendarEvent.get(Calendar.MONTH) + 1
                    && calendarKey.get(Calendar.YEAR) == calendarEvent.get(Calendar.YEAR)) {
                String s[] = model.getImage().trim().split("<->");
                if (s.length > 0) {
                    for (String value : s) {
                        if (!value.isEmpty()) {
                            result.add(value);
                        }
                    }
                }
            }
        }
        return result;
    }

    private void initAction() {
        list.clear();
        res = SaveNoteFragment.getList();
        List<Long> listDay = buildListDate(res);
        mRecyclerview.setHasFixedSize(true);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        for (int i = 0; i < listDay.size(); i++) {
            List<String> result = buildListImage(listDay.get(i), res);
            if (result.size() > 0) {
                list.add(new ImageSub(String.valueOf(listDay.get(i)), result));
            }
        }
        List<String> test = new ArrayList<>();
        String[] s = res.get(0).getImage().trim().split("<->");
        if (s.length > 0) {
            for (String value : s) {
                if (!value.isEmpty()) {
                    test.add(value);
                }
            }
        }
        Collections.reverse(list);
        AdapterImageSub adapterImageSub = new AdapterImageSub(list, getActivity());
        mRecyclerview.setAdapter(adapterImageSub);
        adapterImageSub.notifyDataSetChanged();
    }

    private void initUI(View v) {
        no_item = v.findViewById(R.id.no_item);
        mRecyclerview = v.findViewById(R.id.mRecyclerview);
    }

    @Override
    public void onStart() {
        super.onStart();
        initAction();
    }

    @Override
    public void onResume() {
        super.onResume();
        initAction();
    }

    @Override
    public void onPause() {
        super.onPause();
        initAction();
    }

}