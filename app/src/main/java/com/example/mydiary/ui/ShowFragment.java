package com.example.mydiary.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mydiary.R;
import com.example.mydiary.adapters.ShowAdapter;
import com.example.mydiary.database.DatabaseHelper;
import com.example.mydiary.models.Diary;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Collections;

public class ShowFragment extends Fragment {
    private RecyclerView recyclerView;
    private ShowAdapter adapter;
    private ArrayList<Diary> list;
    private TabLayout tabs2;
    private DatabaseHelper helper;
    private int filter = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show, container, false);
        init(view);
        Filter();
        return view;
    }

    private void init(View view) {
        tabs2 = view.findViewById(R.id.tabs2);
        helper = new DatabaseHelper(getContext());
        recyclerView = view.findViewById(R.id.mRecyclerview);
        list = new ArrayList<>();
        setView();
    }

    private void Filter() {
        tabs2.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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
                setView();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setView() {
        list.clear();
        if (filter == 0) {
            list = helper.getData();
        } else {
            for (Diary diary : helper.getData()) {
                if (diary.getFilter() == filter) {
                    list.add(diary);
                }
            }
        }
        Collections.reverse(list);
        adapter = new ShowAdapter(getContext(), list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        setView();
    }
}