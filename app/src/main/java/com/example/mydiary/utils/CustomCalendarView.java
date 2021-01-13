package com.example.mydiary.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.mydiary.R;
import com.example.mydiary.callback.ItemClickCalendar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CustomCalendarView extends LinearLayout {

    private ImageButton btnNext, btnPrevious;
    private TextView mCurrentDate;
    private GridView mGridView;
    private static final int MAX_CALENDAR_DAYS = 42;
    private Calendar calendar = Calendar.getInstance();
    private Calendar calendarRed = Calendar.getInstance();
    private Context context;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MM - yyyy");
    private SimpleDateFormat montFormat = new SimpleDateFormat("MM");
    private SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
    private List<Date> dates = new ArrayList<>();
    private List<Long> listEvent = new ArrayList<>();
    private MyGridAdapter adapter;
    private Calendar calendarClick = (Calendar) calendarRed.clone();
    private long clickTime = calendar.getTimeInMillis();
    private ItemClickCalendar itemClick;

    public CustomCalendarView(Context context) {
        super(context);
    }

    public CustomCalendarView setEvent(List<Long> list) {
        this.listEvent = list;
        return this;
    }

    public void setClickItem(ItemClickCalendar clickItem) {
        this.itemClick = clickItem;
    }

    public List<Long> getEvent() {
        return listEvent;
    }

    public void notifyChangeDateEvent() {
        SetUp();
    }

    public long getTimeClick() {
        return clickTime;
    }

    public CustomCalendarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        IntializeLayout();
        SetUp();
        btnNext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.MONTH, 1);
                SetUp();
            }
        });
        btnPrevious.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.MONTH, -1);
                SetUp();
            }
        });
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (adapter.isEnabled(position)) {
                    Calendar a = Calendar.getInstance();
                    a.setTimeInMillis(dates.get(position).getTime());
                    calendarClick.setTimeInMillis(dates.get(position).getTime());
                    clickTime = calendarClick.getTimeInMillis();
                    itemClick.Click(position, dates);
                }
                SetUp();
            }
        });
    }


    public CustomCalendarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    private void IntializeLayout() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custom_calender, this);
        btnNext = view.findViewById(R.id.btnNext);
        btnPrevious = view.findViewById(R.id.btnPrevious);
        mCurrentDate = view.findViewById(R.id.mCurrentDate);
        mGridView = view.findViewById(R.id.mGridView);
    }

    private void SetUp() {

        mCurrentDate.setText(dateFormat.format(calendar.getTime()));
        dates.clear();
        Calendar monthCalendar = (Calendar) calendar.clone();
        monthCalendar.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayOfMonth = monthCalendar.get(Calendar.DAY_OF_WEEK) - 1;
        monthCalendar.add(Calendar.DAY_OF_MONTH, -firstDayOfMonth);
        while (dates.size() < MAX_CALENDAR_DAYS) {
            dates.add(monthCalendar.getTime());
            monthCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        adapter = new MyGridAdapter(context, dates, listEvent, calendarClick, calendar, calendarRed);
        mGridView.setAdapter(adapter);

    }

}
