package com.example.mydiary.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import com.google.android.material.tabs.TabItem;

import java.util.ArrayList;
import java.util.Collections;

public class EventFragment extends Fragment {
    private DatabaseCount helper;
    private ArrayList<Count> list;
    private RecyclerView mRecyclerview;
    private CountAdapter adapter;
    private TableLayout tabs2;
    private TabItem tab1;
    private TabItem tab2;
    private TabItem tab3;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_event, container, false);
        init(view);
        setUp();
        getData();
        countDown();

        return view;
    }

    private void init(View v) {
        mRecyclerview = v.findViewById(R.id.mRecyclerview);
       // tabs2 = v.findViewById(R.id.tablayout);
    }
    private void setUp(){
        helper = new DatabaseCount(getContext());
        list = new ArrayList<>();
        list = helper.getData();
        adapter = new CountAdapter(getContext(),list);
    }
    private void getData() {
        mRecyclerview.setHasFixedSize(true);
        mRecyclerview.setLayoutManager(new GridLayoutManager(getContext(),1));
        mRecyclerview.setAdapter(adapter);
    }
    public void updateData(ArrayList<Count> viewModels) {
        list.clear();
        list.addAll(viewModels);
        Collections.sort(list);
        adapter.notifyDataSetChanged();
    }

    private void countDown(){
        CountDownTimer count = new CountDownTimer(180000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                updateData(helper.getData());
            }

            @Override
            public void onFinish() {
                countDown();
            }
        }.start();
    }


}