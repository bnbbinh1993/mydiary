package com.example.mydiary.models;

import android.text.format.DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EventCalendar implements Comparable {
    private int id;
    private String time;
    private String content;
    private long loc;


    public EventCalendar() {
    }

    public EventCalendar(int id, String time, String content, long loc) {
        this.id = id;
        this.time = time;
        this.content = content;
        this.loc = loc;
    }

    public long getLoc() {
        return loc;
    }

    public void setLoc(long loc) {
        this.loc = loc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int compareTo(Object o) {

        Calendar date1 = Calendar.getInstance();
        Calendar date2 = Calendar.getInstance();


        date1.setTimeInMillis(this.getLoc());
        date2.setTimeInMillis(((EventCalendar) o).getLoc());

        int y1 = date1.get(Calendar.YEAR);
        int y2 = date2.get(Calendar.YEAR);
        int m1 = date1.get(Calendar.MONTH) + 1;
        int m2 = date2.get(Calendar.MONTH) + 1;
        int d1 = date1.get(Calendar.DAY_OF_MONTH);
        int d2 = date2.get(Calendar.DAY_OF_MONTH);

        SimpleDateFormat f = new SimpleDateFormat("hh:mm - dd.MM.yyyy");
        String key1 = this.content + " - " + d1 + "." + m1 + "." + y1;
        String key2 = ((EventCalendar) o).getContent() + " - " + d2 + "." + m2 + "." + y2;

        try {
            date1.setTimeInMillis(f.parse(key1).getTime());
            date2.setTimeInMillis(f.parse(key2).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return (int) (date2.getTimeInMillis() - date1.getTimeInMillis());
    }
}
