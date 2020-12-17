package com.example.mydiary.models;

public class EventCalendar {
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
}
