package com.example.mydiary.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.mydiary.R;
import com.example.mydiary.adapters.CountAdapter;
import com.example.mydiary.database.DatabaseCount;
import com.example.mydiary.models.Count;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import static java.util.Collections.*;

public class FinishedFragment extends Fragment {
    private ArrayList<Count> list = new ArrayList<>();
    private RecyclerView mRecyclerview;
    private CountAdapter adapter;
    private LinearLayout no_item;

    public FinishedFragment() {
    }

    public static FinishedFragment newInstance(String param1, String param2) {
        FinishedFragment fragment = new FinishedFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_finished, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        setup();
    }

    private void setup() {

        for (int i = 0; i < CountingDownFragment.getList().size(); i++) {
            if (CountingDownFragment.getList().get(i).getVote() == 1) {
                list.add(CountingDownFragment.getList().get(i));
            }
        }
        sort(list);
        adapter = new CountAdapter(getActivity(), list);
        mRecyclerview.setHasFixedSize(true);
        mRecyclerview.setLayoutManager(new GridLayoutManager(getContext(), 1));
        mRecyclerview.setAdapter(adapter);
    }

    private void init(View v) {
        mRecyclerview = v.findViewById(R.id.mRecyclerview);
        no_item = v.findViewById(R.id.no_item);

    }

    private void checkUI() {
        if (list.size() <= 0) {
            no_item.setVisibility(View.VISIBLE);
        } else {
            no_item.setVisibility(View.GONE);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        checkUI();
    }

    @Override
    public void onResume() {
        super.onResume();
        checkUI();
    }

    @Override
    public void onPause() {
        super.onPause();
        checkUI();
    }

}