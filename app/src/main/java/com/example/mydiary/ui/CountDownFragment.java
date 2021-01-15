package com.example.mydiary.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.mydiary.R;
import com.example.mydiary.activity.CreateCountDownActivity;
import com.example.mydiary.adapters.DairyAdapter;
import com.example.mydiary.database.DatabaseCount;
import com.example.mydiary.models.Count;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CountDownFragment extends Fragment {
    private ViewPager viewPager;
    private TabLayout tablayout;
    private DairyAdapter adapter;
    private DatabaseCount helper;
    private  List<Count> list = new ArrayList<>();
    private LinearLayout layoutTrue, layoutFalse;
    private FloatingActionButton fab;


    public static CountDownFragment newInstance() {
        CountDownFragment fragment = new CountDownFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_count_down, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        init(view);
        setupViewPager(viewPager);
        helper = new DatabaseCount(getContext());
        checkUI();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), CreateCountDownActivity.class));
                Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.out_bottom, R.anim.in_bottom);
            }
        });
    }

    private void checkUI() {
        list.clear();
        list = helper.getData();
        if (list.size() <= 0) {
            layoutTrue.setVisibility(View.GONE);
            layoutFalse.setVisibility(View.VISIBLE);
            fab.setVisibility(View.VISIBLE);
        } else {
            layoutTrue.setVisibility(View.VISIBLE);
            layoutFalse.setVisibility(View.GONE);
            fab.setVisibility(View.GONE);
        }

    }


    private void init(View view) {
        viewPager = view.findViewById(R.id.viewpager);
        tablayout = view.findViewById(R.id.tablayout);
        layoutTrue = view.findViewById(R.id.mLinealayoutTrue);
        layoutFalse = view.findViewById(R.id.mLinealayoutFalse);
        fab = view.findViewById(R.id.fab);
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new DairyAdapter(getChildFragmentManager());
        adapter.addFragment(new CountingDownFragment(), getString(R.string._in_progress));
        adapter.addFragment(new FinishedFragment(), getString(R.string._finished));
        viewPager.setAdapter(adapter);
        tablayout.setupWithViewPager(viewPager);
    }


    @Override
    public void onResume() {

        checkUI();
        super.onResume();
    }


}