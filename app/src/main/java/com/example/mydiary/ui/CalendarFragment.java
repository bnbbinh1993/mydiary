package com.example.mydiary.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mydiary.R;
import com.example.mydiary.activity.AddEventActivity;
import com.example.mydiary.activity.ShowCalendarActivity;
import com.example.mydiary.adapters.EventCalendarAdapter;
import com.example.mydiary.database.DatabaseEvent;
import com.example.mydiary.models.EventCalendar;
import com.example.mydiary.utils.CustomCalendarView;
import com.example.mydiary.callback.ItemClick;
import com.example.mydiary.callback.ItemClickCalendar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;


public class CalendarFragment extends Fragment {

    private CustomCalendarView mCustomCalendarView;
    private List<Long> list = new ArrayList<>();
    private List<EventCalendar> listEvent = new ArrayList<>();
    private List<EventCalendar> listEventAll = new ArrayList<>();
    private RecyclerView recyclerView;
    private EventCalendarAdapter adapter;
    private FloatingActionButton btnFloat;
    private DatabaseEvent helper;
    private LinearLayout mLayoutNull;
    private LinearLayout mLayoutNotNull;
    private long today = 1000;
    private TextView txtDate;
    private TextView txtDate2;
    private Calendar a = Calendar.getInstance();
    private Date dateF = new Date();


    public static CalendarFragment newInstance() {
        CalendarFragment fragment = new CalendarFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        helper = new DatabaseEvent(getContext());
        initUI(view);
        initEvent();
        checkUI();
    }


    private void initEvent() {
        listEvent.clear();
        listEventAll.clear();
        listEventAll = helper.getData();

        int d = a.get(Calendar.DAY_OF_MONTH);
        int m = a.get(Calendar.MONTH) + 1;
        int y = a.get(Calendar.YEAR);
        SimpleDateFormat f = new SimpleDateFormat("dd.MM.yyyy");
        try {
            today = f.parse(d + "." + m + "." + y).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < listEventAll.size(); i++) {
            if (listEventAll.get(i).getLoc() == today) {
                listEvent.add(listEventAll.get(i));
            }
            Log.d("LOC", "Click: " + listEventAll.get(i).getLoc() + " - " + today);
        }


        dateF.setTime(today);

        String thu = (String) DateFormat.format("EEE", dateF); // T.4
        String dayOfTheWeek = (String) DateFormat.format("EEEE", dateF); // Thursday
        String day = (String) DateFormat.format("dd", dateF); // 20
        String monthString = (String) DateFormat.format("MMM", dateF); // Jun
        String monthNumber = (String) DateFormat.format("MM", dateF); // 06
        String year = (String) DateFormat.format("yyyy", dateF); // 2013

        txtDate.setText(day + " " + monthString + " " + year + ", " + dayOfTheWeek);
        txtDate2.setText(day + " " + monthString + " " + year + ", " + dayOfTheWeek);

        for (int i = 0; i < listEventAll.size(); i++) {
            if (!list.contains(listEventAll.get(i).getLoc())) {
                list.add(listEventAll.get(i).getLoc());
            }
        }


        mCustomCalendarView.setEvent(list);
        mCustomCalendarView.notifyChangeDateEvent();
        mCustomCalendarView.setClickItem(new ItemClickCalendar() {
            @Override
            public void Click(int p, List<Date> date) {
                listEvent.clear();
                listEventAll.clear();
                listEventAll = helper.getData();

                a.setTimeInMillis(date.get(p).getTime());
                int d = a.get(Calendar.DAY_OF_MONTH);
                int m = a.get(Calendar.MONTH) + 1;
                int y = a.get(Calendar.YEAR);
                SimpleDateFormat f = new SimpleDateFormat("dd.MM.yyyy");
                try {
                    today = f.parse(d + "." + m + "." + y).getTime();
                    dateF.setTime(today);
                    String thu = (String) DateFormat.format("EEE", dateF); // T.4
                    String dayOfTheWeek = (String) DateFormat.format("EEEE", dateF); // Thursday
                    String day = (String) DateFormat.format("dd", dateF); // 20
                    String monthString = (String) DateFormat.format("MMM", dateF); // Jun
                    String monthNumber = (String) DateFormat.format("MM", dateF); // 06
                    String year = (String) DateFormat.format("yyyy", dateF); // 2013

                    txtDate.setText(day + " " + monthString + " " + year + ", " + dayOfTheWeek);
                    txtDate2.setText(day + " " + monthString + " " + year + ", " + dayOfTheWeek);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < listEventAll.size(); i++) {
                    if (listEventAll.get(i).getLoc() == today) {
                        listEvent.add(listEventAll.get(i));
                    }
                    Log.d("LOC", "Click: " + listEventAll.get(i).getLoc() + " - " + today);
                }
                Log.d("SIZE", "Click: " + listEvent.size());

                updateUI();
            }
        });
        Collections.sort(listEvent);
        adapter = new EventCalendarAdapter(getContext(), listEvent);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        adapter.setOnClickItem(new ItemClick() {
            @Override
            public void Click(int position) {
                Intent intent = new Intent(getContext(), ShowCalendarActivity.class);
                int id = listEvent.get(position).getId();
                intent.putExtra("keyPosition", id);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.out_bottom, R.anim.in_bottom);
            }
        });
        btnFloat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddEventActivity.class);
                intent.putExtra("keyTime", today);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.out_bottom, R.anim.in_bottom);
            }
        });

    }

    private void updateUI() {
        checkUI();
        adapter.notifyDataSetChanged();
    }

    private void checkUI() {
        if (listEvent.size() > 0) {
            mLayoutNotNull.setVisibility(View.VISIBLE);
            mLayoutNull.setVisibility(View.GONE);

        } else {
            mLayoutNotNull.setVisibility(View.GONE);
            mLayoutNull.setVisibility(View.VISIBLE);
        }
    }

    private void initUI(View view) {
        mCustomCalendarView = view.findViewById(R.id.mCustomCalendarView);
        recyclerView = view.findViewById(R.id.mRecyclerview);
        btnFloat = view.findViewById(R.id.btnFloat);
        mLayoutNull = view.findViewById(R.id.mLayoutNull);
        mLayoutNotNull = view.findViewById(R.id.mLayoutNotNull);
        txtDate = view.findViewById(R.id.txtDate);
        txtDate2 = view.findViewById(R.id.txtDate2);
    }

    @Override
    public void onResume() {
        initEvent();
        updateUI();
        super.onResume();

    }
}