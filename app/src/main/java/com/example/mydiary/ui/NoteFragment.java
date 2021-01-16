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
import com.example.mydiary.activity.CreateNoteActivity;
import com.example.mydiary.adapters.DairyAdapter;
import com.example.mydiary.database.DatabaseHelper;
import com.example.mydiary.models.Diary;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class NoteFragment extends Fragment {
    private ViewPager viewPager;
    private TabLayout tablayout;
    private DairyAdapter adapter;
    private DatabaseHelper helper;
    private List<Diary> list = new ArrayList<>();
    private LinearLayout layoutTrue, layoutFalse;
    private FloatingActionButton fab;

    public static NoteFragment newInstance() {
        return new NoteFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_note, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        setupViewPager(viewPager);
        helper = new DatabaseHelper(getContext());
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
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), CreateNoteActivity.class));
                Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.out_bottom, R.anim.in_bottom);
            }
        });
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
        adapter.addFragment(new SaveNoteFragment(), getString(R.string._all));
        adapter.addFragment(new ImageFragment(), getString(R.string._image));
        viewPager.setAdapter(adapter);
        tablayout.setupWithViewPager(viewPager);
    }


    @Override
    public void onResume() {
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
        super.onResume();
    }
}