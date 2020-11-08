package com.example.mydiary.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Count implements Comparable {
    private int id;
    private String title;
    private String des;
    private String place;
    private String date;
    private int filter;
    private int vote;

    public Count(int id, String title, String des, String place, String date, int filter, int vote) {
        this.id = id;
        this.title = title;
        this.des = des;
        this.place = place;
        this.date = date;
        this.filter = filter;
        this.vote = vote;
    }

    public Count() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getFilter() {
        return filter;
    }

    public void setFilter(int filter) {
        this.filter = filter;
    }

    public int getVote() {
        return vote;
    }

    public void setVote(int vote) {
        this.vote = vote;
    }

    @Override
    public int compareTo(Object o) {

        if (this.vote == ((Count) o).getVote()) {
            SimpleDateFormat format = new SimpleDateFormat("hh:mm - dd.MM.yyyy");
            try {
                long a = (format.parse(this.getDate()).getTime() - System.currentTimeMillis())/1000;
                long b = (format.parse(((Count) o).getDate()).getTime() - System.currentTimeMillis())/1000;
                return (int) (a - b);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return this.vote - ((Count) o).getVote();
    }
}
