package com.example.mydiary.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mydiary.R;
import com.example.mydiary.adapters.CountAdapter;
import com.example.mydiary.database.DatabaseCount;
import com.example.mydiary.models.Count;
import com.example.mydiary.models.Diary;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Collections;

public class EventFragment extends Fragment {
    private DatabaseCount helper;
    private ArrayList<Count> list;
    private RecyclerView mRecyclerview;
    private CountAdapter adapter;
    private TabLayout tablayout;
    private TabItem tab1;
    private TabItem tab2;
    private TabItem tab3;
    private int filter = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event, container, false);
        init(view);
        setUp();
        setData();
        countDown();
        Filter();
        return view;
    }

    private void init(View v) {
        mRecyclerview = v.findViewById(R.id.mRecyclerview);
        tablayout = v.findViewById(R.id.tablayout);
        tab1 = v.findViewById(R.id.tab1);
        tab2 = v.findViewById(R.id.tab2);
        tab3 = v.findViewById(R.id.tab3);
    }

    private void setUp() {
        helper = new DatabaseCount(getContext());
        list = new ArrayList<>();
        list = helper.getData();
        adapter = new CountAdapter(getContext(), list);
    }

    private void Filter() {
        tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    filter = 0;
                } else if (tab.getPosition() == 1) {
                    filter = 1;
                } else if (tab.getPosition() == 2) {
                    filter = 2;
                } else if (tab.getPosition() == 3) {
                    filter = 3;
                } else if (tab.getPosition() == 4) {
                    filter = 4;
                }
                updateFilter();

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void updateFilter() {
        list.clear();
        ArrayList<Count> test = new ArrayList<>();
        if (filter == 0) {
            test = helper.getData();
        } else {
            for (Count count : helper.getData()) {
                if (count.getVote() == (filter-1)) {
                    test.add(count);
                }
            }
        }

        updateData(test);
    }

    private void setData() {
        mRecyclerview.setHasFixedSize(true);
        mRecyclerview.setLayoutManager(new GridLayoutManager(getContext(), 1));
        mRecyclerview.setAdapter(adapter);
    }

    public void updateData(ArrayList<Count> viewModels) {
        list.clear();
        list.addAll(viewModels);
        Collections.sort(list);
        Log.d("TEST", "updateFilter: "+list.size());
        adapter.notifyDataSetChanged();
    }

    private void countDown() {
        CountDownTimer count = new CountDownTimer(180000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                updateFilter();

            }

            @Override
            public void onFinish() {
                countDown();
            }
        }.start();
    }


}