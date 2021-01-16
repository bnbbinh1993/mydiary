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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mydiary.R;
import com.example.mydiary.activity.CreateNoteActivity;
import com.example.mydiary.adapters.AdapterSub;
import com.example.mydiary.database.DatabaseHelper;
import com.example.mydiary.models.Diary;
import com.example.mydiary.models.ItemSub;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class SaveNoteFragment extends Fragment {
    private RecyclerView recyclerView;
    private AdapterSub adapter;
    private static ArrayList<Diary> list;
    private ArrayList<ItemSub> result;
    private List<Long> listDate;
    private LinearLayout no_item;
    private DatabaseHelper helper;
    private FloatingActionButton fab;

    public static ArrayList<Diary> getList() {
        return list;
    }

    public static SaveNoteFragment newInstance() {
        return new SaveNoteFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_save_note, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        setUp();
        initClick();
        fabRecyclerview();
        addDateTest();
    }

    private void addDateTest() {
        list.clear();
        list = helper.getData();
        listDate = buildListDate(list);
        List<ItemSub> listTest = new ArrayList<>();
        adapter = new AdapterSub(listTest, getActivity());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        for (int i = 0; i < listDate.size(); i++) {
            List<Diary> diaryList = buildListDiary(listDate.get(i), list);
            Log.d("TAG", "Size: " + diaryList.size());
            listTest.add(new ItemSub(String.valueOf(listDate.get(i)), diaryList));

        }
        Collections.reverse(listTest);
        adapter.notifyDataSetChanged();
        updateUI();

    }

    private List<Long> buildListDate(List<Diary> data) {
        List<Long> res = new ArrayList<>();

        for (Diary model : list) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(model.getRealtime());
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int month = calendar.get(Calendar.MONTH) + 1;
            int year = calendar.get(Calendar.YEAR);
            String key = day + "/" + month + "/" + year;
            try {
                calendar.setTimeInMillis(new SimpleDateFormat("dd/MM/yyyy").parse(key).getTime());
                if (!res.contains(calendar.getTimeInMillis())) {
                    res.add(model.getRealtime());
                    Log.d("TAG", "" + model.getRealtime());
                } else {
                    Log.d("TAG", "Đã tồn tại!");
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        Log.d("TAG", "ListDate: " + res.size());
        return res;
    }

    private List<Diary> buildListDiary(long key, List<Diary> data) {
        List<Diary> res = new ArrayList<>();
        Calendar calendarKey = Calendar.getInstance();
        calendarKey.setTimeInMillis(key);
        Calendar calendarEvent = Calendar.getInstance();

        for (Diary model : data) {
            calendarEvent.setTimeInMillis(model.getRealtime());
            if (calendarKey.get(Calendar.DAY_OF_MONTH) == calendarEvent.get(Calendar.DAY_OF_MONTH)
                    && calendarKey.get(Calendar.MONTH) + 1 == calendarEvent.get(Calendar.MONTH) + 1
                    && calendarKey.get(Calendar.YEAR) == calendarEvent.get(Calendar.YEAR)) {
                res.add(model);
            } else {
                Log.d("TAG", "buildListDiary: Faild");
            }
        }
        return res;
    }

    private void initClick() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), CreateNoteActivity.class));
                Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.out_bottom, R.anim.in_bottom);
            }
        });
    }

    private void setUp() {
        fab.setVisibility(View.VISIBLE);
        helper = new DatabaseHelper(getContext());
        list = new ArrayList<>();
        result = new ArrayList<>();

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


    private void updateUI() {
        if (list.size() <= 0) {
            no_item.setVisibility(View.VISIBLE);
        } else {
            no_item.setVisibility(View.GONE);
        }

    }

    @Override
    public void onResume() {
        addDateTest();
        super.onResume();

    }

}