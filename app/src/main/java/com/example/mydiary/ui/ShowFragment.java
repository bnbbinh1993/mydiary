package com.example.mydiary.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mydiary.R;
import com.example.mydiary.activity.NoteActivity;
import com.example.mydiary.activity.ShowDiaryActivity;
import com.example.mydiary.adapters.AdapterSub;
import com.example.mydiary.adapters.ShowAdapter;
import com.example.mydiary.database.DatabaseHelper;
import com.example.mydiary.models.Diary;
import com.example.mydiary.models.ItemSub;
import com.example.mydiary.utils.OnClickItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShowFragment extends Fragment {
    private RecyclerView recyclerView;
    private AdapterSub adapter;
    private static ArrayList<Diary> list;
    private static ArrayList<ItemSub> result;
    private LinearLayout no_item;
    private DatabaseHelper helper;
    private FloatingActionButton fab;

    public static ArrayList<Diary> getList() {
        return list;
    }

    public static ShowFragment newInstance() {
        ShowFragment fragment = new ShowFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        setUp();
        initClick();
        fabRecyclerview();
    }

    private void initClick() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), NoteActivity.class));
                getActivity().overridePendingTransition(R.anim.out_bottom, R.anim.in_bottom);
            }
        });
    }

    private void setUp() {
        fab.setVisibility(View.VISIBLE);
        helper = new DatabaseHelper(getContext());
        list = new ArrayList<>();
        result = new ArrayList<>();
        setView();
    }

    private void init(View view) {
        no_item = view.findViewById(R.id.no_item);
        recyclerView = view.findViewById(R.id.mRecyclerview);
        fab = view.findViewById(R.id.fab);
    }


    private void fabRecyclerview() {
        Animation FabMenu_fadOut = AnimationUtils.loadAnimation(getActivity(),
                R.anim.in_left);
        Animation FabMenu_fadIn = AnimationUtils.loadAnimation(getActivity(),
                R.anim.out_left);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
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


    private void setView() {
        buildListItemSub();
        adapter = new AdapterSub(result, getActivity());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        updateUI();
    }

    private void buildListItemSub() {
        list.clear();
        list = helper.getData();
        Collections.reverse(list);
        List<Long> listDate = new ArrayList<>();
        List<Diary> diaryArrayList = new ArrayList<>();
        for (Diary model : list) {
            if (!listDate.contains(model.getRealtime())) {
                listDate.add(model.getRealtime());
            }
        }
        for (int i = 0; i < listDate.size(); i++) {
            for (Diary model : list) {
                if (listDate.get(i) == model.getRealtime()) {
                    diaryArrayList.add(model);
                }
            }
            result.add(new ItemSub(String.valueOf(listDate.get(i)), diaryArrayList));
            diaryArrayList.clear();
        }
        listDate.clear();
    }


    private void updateUI() {
        if (list.size() <= 0) {
            no_item.setVisibility(View.VISIBLE);
        } else {
            no_item.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setView();
        updateUI();
    }

}