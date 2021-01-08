package com.example.mydiary.models;

public class Diary {
    private int id;
    private String title;
    private String content;
    private String date;
    private String address;
    private String image;
    private int vote;
    private int filter;
    private long realtime;


    public Diary() {
    }


    public Diary(int id, String title, String content, String date, String address, String image, int vote, int filter, long realtime) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.date = date;
        this.address = address;
        this.image = image;
        this.vote = vote;
        this.filter = filter;
        this.realtime = realtime;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getVote() {
        return vote;
    }

    public void setVote(int vote) {
        this.vote = vote;
    }

    public int getFilter() {
        return filter;
    }

    public void setFilter(int filter) {
        this.filter = filter;
    }

    public long getRealtime() {
        return realtime;
    }

    public void setRealtime(long realtime) {
        this.realtime = realtime;
    }
}
