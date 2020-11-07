package com.example.mydiary.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mydiary.R;
import com.example.mydiary.adapters.ShowAdapter;
import com.example.mydiary.database.MyDatabaseHelper;
import com.example.mydiary.models.Show;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ShowFragment extends Fragment {
    private RecyclerView recyclerView;
    private ShowAdapter adapter;
    private ArrayList<Show> list;
    private TabLayout tabs2;
    private MyDatabaseHelper helper;
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
        helper = new MyDatabaseHelper(getContext());
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
            for (Show show : helper.getData()) {
                if (show.getFilter() == filter) {
                    list.add(show);
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