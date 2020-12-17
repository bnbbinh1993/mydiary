package com.example.mydiary.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mydiary.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MyGridAdapter extends ArrayAdapter implements DisableGridView {
    private List<Date> dates = new ArrayList<>();
    private List<Long> events = new ArrayList<>();
    private Calendar toDay = Calendar.getInstance();
    private Calendar currentCalendar;
    private LayoutInflater layoutInflater;
    private ItemClick itemClick;
    private Calendar calendar;


    public MyGridAdapter(@NonNull Context context, List<Date> dates, List<Long> event, Calendar currentCalendar, Calendar calendar, Calendar today) {
        super(context, R.layout.single_cell_layout);
        this.dates = dates;
        this.events = event;
        this.currentCalendar = currentCalendar;
        this.calendar = calendar;
        this.toDay = today;
        layoutInflater = LayoutInflater.from(context);
    }

    public void setItemClick(ItemClick listener) {
        itemClick = listener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Calendar dateCalendar = Calendar.getInstance();
        View view = convertView;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.single_cell_layout, parent, false);
        }
        dateCalendar.setTime(dates.get(position));

        int days = dateCalendar.get(Calendar.DAY_OF_MONTH);
        int months = dateCalendar.get(Calendar.MONTH) + 1;
        int years = dateCalendar.get(Calendar.YEAR);

        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        int currentYear = calendar.get(Calendar.YEAR);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        TextView day_number = view.findViewById(R.id.calendar_day);
        TextView testEvent = view.findViewById(R.id.testEvent);
        LinearLayout testClick = view.findViewById(R.id.testClick);
        day_number.setText(String.valueOf(days));

        for (int i = 0; i < events.size(); i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(events.get(i));
            int m = calendar.get(Calendar.MONTH) + 1;
            int y = calendar.get(Calendar.YEAR);
            int d = calendar.get(Calendar.DAY_OF_MONTH);
            if (days == d && months == m && years == y) {
                testEvent.setBackgroundResource(R.drawable.bg_event_calendar);
            }
        }
        if (calendar.getTimeInMillis() == toDay.getTimeInMillis() && days == day) {
            day_number.setTextColor(getContext().getResources().getColor(R.color.red));
        } else if (months == currentMonth && years == currentYear) {
            day_number.setTextColor(getContext().getResources().getColor(R.color.black));
        } else {
            view.setVisibility(View.GONE);
        }
        if (dateCalendar.getTimeInMillis() == currentCalendar.getTimeInMillis()) {
            testClick.setBackgroundResource(R.drawable.bg_select);
        }


        return view;
    }

    @Override
    public boolean isEnabled(int position) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dates.get(position));
        int m = calendar.get(Calendar.MONTH)+1;
        int y = calendar.get(Calendar.YEAR);

        int m1 = cal.get(Calendar.MONTH)+1;
        int y1 = cal.get(Calendar.YEAR);
        if (m==m1&& y==y1){
            return true;
        }
        return false;
    }

    @Override
    public int getCount() {
        return dates.size();
    }

    @Override
    public int getPosition(@Nullable Object item) {
        return dates.indexOf(item);
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return dates.get(position);
    }
}
