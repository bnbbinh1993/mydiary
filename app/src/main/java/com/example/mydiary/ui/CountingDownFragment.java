package com.example.mydiary.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.OnScrollListener;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.example.mydiary.R;
import com.example.mydiary.activity.CreateCountDownActivity;
import com.example.mydiary.adapters.CountAdapter;
import com.example.mydiary.database.DatabaseCount;
import com.example.mydiary.models.Count;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Objects;

import static java.util.Collections.*;


public class CountingDownFragment extends Fragment {
    private DatabaseCount helper;
    private static ArrayList<Count> list = new ArrayList<>();
    private ArrayList<Count> listData = new ArrayList<>();
    private RecyclerView mRecyclerview;
    private CountAdapter adapter;
    private CountDownTimer count;
    private FloatingActionButton fab;
    private LinearLayout no_item;

    public static ArrayList<Count> getList() {
        return list;
    }

    public static CountingDownFragment newInstance() {
        return new CountingDownFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_counting_down, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        setUp();
        fabRecyclerview();
        countDown();
        initEvent();
    }

    private void initEvent() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), CreateCountDownActivity.class));
                Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.out_bottom, R.anim.in_bottom);
            }
        });
    }


    private void init(View v) {
        mRecyclerview = v.findViewById(R.id.mRecyclerview);
        fab = v.findViewById(R.id.fab);
        no_item = v.findViewById(R.id.no_item);

    }

    private void setUp() {
        list.clear();
        listData.clear();
        helper = new DatabaseCount(getContext());
        list = helper.getData();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getVote() == 0) listData.add(list.get(i));
        }
        sort(listData);
        adapter = new CountAdapter(getActivity(), listData);
        mRecyclerview.setHasFixedSize(true);
        mRecyclerview.setLayoutManager(new GridLayoutManager(getContext(), 1));
        mRecyclerview.setAdapter(adapter);
        updateUI();
    }

    private void fabRecyclerview() {
        Animation FabMenu_fadOut = AnimationUtils.loadAnimation(getActivity(),
                R.anim.in_left);
        Animation FabMenu_fadIn = AnimationUtils.loadAnimation(getActivity(),
                R.anim.out_left);
        mRecyclerview.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && fab.getVisibility() == View.VISIBLE) {
                    fab.startAnimation(FabMenu_fadOut);
                    fab.setVisibility(View.GONE);

                } else if (dy < 0 && fab.getVisibility() != View.VISIBLE) {
                    fab.startAnimation(FabMenu_fadIn);
                    fab.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void updateUI() {
        if (listData.size() <= 0) {
            no_item.setVisibility(View.VISIBLE);
            mRecyclerview.setVisibility(View.GONE);
        } else {
            no_item.setVisibility(View.GONE);
            mRecyclerview.setVisibility(View.VISIBLE);
        }
        adapter.notifyDataSetChanged();
    }

    private void countDown() {
        count = new CountDownTimer(1800000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFinish() {
                countDown();
            }
        };
    }

    @Override
    public void onPause() {
        super.onPause();
        if (count != null) {
            count.cancel();
        }
    }

    @Override
    public void onResume() {
        setUp();

        super.onResume();
        if (count != null) {
            count.start();
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        if (count != null) {
            count.start();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (count != null) {
            count.cancel();
        }
    }
}